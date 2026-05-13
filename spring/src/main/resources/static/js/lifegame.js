// ==================================================
// 状態変数
// ==================================================

// 自動再生のタイマーIDを保存する変数です。
// null のときは自動再生していない状態です。
let autoPlayTimerId = null;

// 自動再生の間隔です。
// スライダー操作によって値を変更します。
let autoPlayIntervalMillis = 300;

// セルをドラッグ中かどうかを表す変数です。
let isDragging = false;

// 1回のドラッグ操作中に、すでに処理したセルを記録します。
// 同じセルを何度も編集しないために使います。
let draggedCellKeys = new Set();

// 1回のドラッグ操作中に通過したセル位置を記録します。
// マウスを離したときに、まとめてAPIへ送信します。
let draggedCells = [];

// 現在プレビュー表示しているセルを記録します。
// プレビューを消すときに使います。
let previewCellButtons = [];

// Java側から取得したパターン定義を保存します。
// keyはPatternTypeのenum名、valueはパターン定義です。
let patternDefinitions = {};

// パターン定義をJava側から取得できたかどうかを表します。
// false の場合は、Place Pattern関連の操作を無効化します。
let patternDefinitionsLoaded = false;

// ==================================================
// HTML要素の取得
// ==================================================

// Stepボタンを取得します。
const stepButton = document.getElementById("stepButton");

// Startボタンを取得します。
const startButton = document.getElementById("startButton");

// Stopボタンを取得します。
const stopButton = document.getElementById("stopButton");

// Clearボタンを取得します。
const clearButton = document.getElementById("clearButton");

// Resetボタンを取得します。
const resetButton = document.getElementById("resetButton");

// Randomボタンを取得します。
const randomButton = document.getElementById("randomButton");

// パターン選択用のセレクトボックスを取得します。
const patternTypeSelect = document.getElementById("patternType");

// セル編集モード選択用のセレクトボックスを取得します。
const cellEditModeSelect = document.getElementById("cellEditMode");

// 盤面操作モード選択用のセレクトボックスを取得します。
const actionModeSelect = document.getElementById("actionMode");

// Edit Modeの補足説明を表示する要素を取得します。
const editModeHelp = document.getElementById("editModeHelp");

// 盤面上のセルボタンをすべて取得します。
const cellButtons = document.querySelectorAll(".cell");

// 速度調整スライダーを取得します。
const speedSlider = document.getElementById("speedSlider");

// 現在の速度を表示する要素を取得します。
const speedValue = document.getElementById("speedValue");

// 世代数を表示している要素を取得します。
const generationValue = document.getElementById("generationValue");

// 盤面全体の要素を取得します。
const boardElement = document.querySelector(".board");

// Patternの補足説明を表示する要素を取得します。
const patternHelp = document.getElementById("patternHelp");

// ==================================================
// イベント登録
// ==================================================

// Stepボタンが押されたとき、APIを呼び出して1世代進めます。
stepButton.addEventListener("click", () => {
    stepByApi();
});

// Startボタンが押されたとき、自動再生を開始します。
startButton.addEventListener("click", () => {
    startAutoPlay();
});

// Stopボタンが押されたとき、自動再生を停止します。
stopButton.addEventListener("click", () => {
    stopAutoPlay();
});

// Clearボタンが押されたとき、APIを呼び出して盤面をクリアします。
clearButton.addEventListener("click", async () => {
    const board = await clearBoardApi();
    applyBoardIfAvailable(board);
});

// Resetボタンが押されたとき、APIを呼び出して初期状態へ戻します。
resetButton.addEventListener("click", async () => {
    const board = await resetBoardApi();
    applyBoardIfAvailable(board);
});

// Randomボタンが押されたとき、APIを呼び出してランダム配置します。
randomButton.addEventListener("click", async () => {
    const board = await randomizeBoardApi();
    applyBoardIfAvailable(board);
});

// 各セルに、クリック、ドラッグ描画、パターンプレビュー用のイベントを登録します。
cellButtons.forEach((button) => {
    // マウスボタンを押したセルを編集し、ドラッグ開始状態にします。
    button.addEventListener("mousedown", (event) => {
        startCellDrag(event, button);
    });

    // ドラッグ中に別のセルへ入ったら、そのセルを編集します。
    // Place Patternモードの場合は、プレビュー位置を更新します。
    button.addEventListener("mouseenter", () => {
        dragOverCell(button);
        updatePatternPreview(button);
    });
});

// 画面上でマウスボタンを離したら、ドラッグ終了にします。
document.addEventListener("mouseup", () => {
    endCellDrag();
});

// スライダーを動かしたとき、自動再生の速度を変更します。
speedSlider.addEventListener("input", () => {
    changeSpeed(Number(speedSlider.value));
});

// Action Modeが変更されたとき、関連するUIの有効・無効を切り替え、プレビューを消します。
actionModeSelect.addEventListener("change", () => {
    updateControlsForActionMode();
    clearPatternPreview();
});

// Patternが変更されたとき、古いパターンプレビューを消します。
patternTypeSelect.addEventListener("change", () => {
    clearPatternPreview();
});

// 盤面からマウスが出たら、パターンプレビューを消します。
boardElement.addEventListener("mouseleave", () => {
    clearPatternPreview();
});

// ==================================================
// 初期化
// ==================================================

// 初期表示時に必要なデータ取得とUI状態の整理を行います。
initialize();

/**
 * 初期表示時の準備を行います。
 *
 * Java側からパターン定義を取得し、
 * Action Modeに合わせてUI状態を整えます。
 */
async function initialize() {
    await loadPatternDefinitions();
    updateControlsForActionMode();
}

/**
 * Java側からパターン定義を取得して、JavaScript側で使いやすい形に変換します。
 */
async function loadPatternDefinitions() {
    const definitions = await getPatternDefinitionsApi();

    if (definitions === null) {
        console.error("Pattern definitions could not be loaded.");
        patternDefinitions = {};
        patternDefinitionsLoaded = false;
        return;
    }

    patternDefinitions = {};

    definitions.forEach((definition) => {
        patternDefinitions[definition.patternType] = definition;
    });

    patternDefinitionsLoaded = true;
}

// ==================================================
// 自動再生関連
// ==================================================

/**
 * 自動再生を開始します。
 *
 * すでに自動再生中の場合は、二重にタイマーを作らないように何もしません。
 */
function startAutoPlay() {
    if (autoPlayTimerId !== null) {
        return;
    }

    autoPlayTimerId = setInterval(() => {
        stepByApi();
    }, autoPlayIntervalMillis);
}

/**
 * 自動再生を停止します。
 *
 * setIntervalで作成したタイマーを停止し、
 * タイマーIDをnullに戻します。
 */
function stopAutoPlay() {
    if (autoPlayTimerId === null) {
        return;
    }

    clearInterval(autoPlayTimerId);
    autoPlayTimerId = null;
}

/**
 * 自動再生の速度を変更します。
 *
 * 自動再生中に速度を変更した場合は、
 * 新しい速度でタイマーを作り直します。
 *
 * @param {number} intervalMillis 自動再生の間隔
 */
function changeSpeed(intervalMillis) {
    autoPlayIntervalMillis = intervalMillis;
    speedValue.textContent = `${intervalMillis} ms`;

    if (autoPlayTimerId !== null) {
        stopAutoPlay();
        startAutoPlay();
    }
}


// ==================================================
// セルドラッグ操作関連
// ==================================================

/**
 * セルのドラッグ操作を開始します。
 *
 * Action ModeがEdit Cellの場合はセル編集を開始します。
 * Action ModeがPlace Patternの場合は、クリック位置にパターンを配置します。
 *
 * @param {MouseEvent} event マウス操作のイベント
 * @param {HTMLButtonElement} button マウスボタンが押されたセルボタン
 */
function startCellDrag(event, button) {
    // 左クリック以外は処理しません。
    if (event.button !== 0) {
        return;
    }

    if (isPlacePatternMode()) {
        placePatternAtCellByApi(button);
        return;
    }

    isDragging = true;
    draggedCellKeys = new Set();
    draggedCells = [];

    addCellOnceDuringDrag(button);
}

/**
 * ドラッグ中にセルへ入ったときの処理です。
 *
 * Edit Cellモードでドラッグ中の場合だけ、そのセルを1回だけ記録します。
 *
 * @param {HTMLButtonElement} button ドラッグ中に入ったセルボタン
 */
function dragOverCell(button) {
    if (!isDragging || isPlacePatternMode()) {
        return;
    }

    addCellOnceDuringDrag(button);
}

/**
 * セルのドラッグ操作を終了します。
 *
 * マウスボタンが離されたらドラッグ状態を解除し、
 * 記録したセルをまとめてAPIへ送信します。
 */
function endCellDrag() {
    if (!isDragging) {
        return;
    }

    isDragging = false;

    if (draggedCells.length > 0) {
        editCellsByApi(draggedCells);
    }

    draggedCellKeys.clear();
    draggedCells = [];
}

/**
 * 1回のドラッグ操作中に、指定されたセルを1回だけ記録します。
 *
 * 同じセルを何度も通過しても、同じドラッグ中は再記録しません。
 * 実際のAPI送信は、マウスを離したタイミングでまとめて行います。
 *
 * @param {HTMLButtonElement} button 記録したいセルボタン
 */
function addCellOnceDuringDrag(button) {
    const cellKey = createCellKey(button);

    if (draggedCellKeys.has(cellKey)) {
        return;
    }

    draggedCellKeys.add(cellKey);

    draggedCells.push({
        row: Number(button.dataset.row),
        col: Number(button.dataset.col)
    });

    applyCellEditViewOnly(button);
}

/**
 * セルの見た目だけを、選択中の編集モードに応じて変更します。
 *
 * APIの結果を待たずに、ドラッグ中の操作感を軽くするために使います。
 * 最終的にはAPIから返ってきた盤面データで画面全体を正しい状態に更新します。
 *
 * @param {HTMLButtonElement} button 見た目を変更したいセルボタン
 */
function applyCellEditViewOnly(button) {
    const mode = cellEditModeSelect.value;

    switch (mode) {
        case "TOGGLE":
            toggleCellViewOnly(button);
            break;
        case "DRAW":
            setCellViewOnly(button, true);
            break;
        case "ERASE":
            setCellViewOnly(button, false);
            break;
        default:
            console.warn("Unknown cell edit mode:", mode);
            break;
    }
}

/**
 * セルを一意に識別するためのキーを作成します。
 *
 * 行番号と列番号を組み合わせて、"10,20" のような文字列を作ります。
 *
 * @param {HTMLButtonElement} button セルボタン
 * @return {string} セルを識別するキー
 */
function createCellKey(button) {
    return `${button.dataset.row},${button.dataset.col}`;
}


// ==================================================
// モード制御関連
// ==================================================

/**
 * 現在のAction ModeがPlace Patternかどうかを判定します。
 *
 * @return {boolean} Place Patternモードの場合はtrue
 */
function isPlacePatternMode() {
    return actionModeSelect.value === "PLACE_PATTERN";
}

/**
 * 現在のAction Modeに合わせて、関連するUIの有効・無効を切り替えます。
 *
 * Edit CellモードではEdit Modeを使い、Patternは使いません。
 * Place PatternモードではPatternを使い、Edit Modeは使いません。
 * パターン定義を取得できていない場合は、Pattern操作を無効化します。
 */
function updateControlsForActionMode() {
    const placePatternMode = isPlacePatternMode();

    cellEditModeSelect.disabled = placePatternMode;
    patternTypeSelect.disabled = !placePatternMode || !patternDefinitionsLoaded;

    if (!patternDefinitionsLoaded) {
        editModeHelp.textContent = "";
        patternHelp.textContent = "パターン定義を取得できませんでした";
        clearPatternPreview();
        return;
    }

    if (placePatternMode) {
        editModeHelp.textContent = "Place PatternではEdit Modeは使用しません";
        patternHelp.textContent = "";
    } else {
        editModeHelp.textContent = "";
        patternHelp.textContent = "Place Patternのときだけ使用します";
        clearPatternPreview();
    }
}

// ==================================================
// API呼び出しの利用処理
// ==================================================

/**
 * Spring Boot側のAPIを呼び出して、盤面を1世代進めます。
 */
async function stepByApi() {
    const board = await stepBoardApi();
    applyBoardIfAvailable(board);
}

/**
 * 指定された複数セルを、選択中の編集モードでまとめて編集します。
 *
 * @param {{row: number, col: number}[]} cells 編集するセル位置の一覧
 */
async function editCellsByApi(cells) {
    const mode = cellEditModeSelect.value;
    const board = await editCellsApi(mode, cells);

    applyBoardIfAvailable(board);
}

/**
 * 指定されたセル位置を基準に、選択中のパターンを配置します。
 *
 * パターン定義を取得できていない場合や、
 * パターンが盤面外にはみ出す場合は配置しません。
 *
 * @param {HTMLButtonElement} button 配置位置として使うセルボタン
 */
async function placePatternAtCellByApi(button) {
    if (!patternDefinitionsLoaded) {
        return;
    }

    if (!canPlacePatternAt(button)) {
        return;
    }

    const patternType = patternTypeSelect.value;
    const row = Number(button.dataset.row);
    const col = Number(button.dataset.col);
    const board = await placePatternApi(patternType, row, col);

    clearPatternPreview();
    applyBoardIfAvailable(board);
}

/**
 * APIから受け取った盤面データがあれば画面に反映します。
 *
 * API呼び出しに失敗してnullが返ってきた場合は、
 * 自動再生を停止します。
 *
 * @param {object|null} board 更新後の盤面データ
 */
function applyBoardIfAvailable(board) {
    if (board === null) {
        stopAutoPlay();
        return;
    }

    updateBoardView(board, generationValue, cellButtons);
}

// ==================================================
// パターンプレビュー関連
// ==================================================

/**
 * 指定されたセルを基準に、選択中パターンのプレビューを表示します。
 *
 * Place Patternモードでない場合やパターン定義を取得できていない場合は、
 * プレビューを消して何もしません。
 *
 * @param {HTMLButtonElement} baseButton 基準にするセルボタン
 */
function updatePatternPreview(baseButton) {
    clearPatternPreview();

    if (!isPlacePatternMode() || !patternDefinitionsLoaded) {
        return;
    }

    const patternType = patternTypeSelect.value;
    const patternDefinition = patternDefinitions[patternType];

    if (patternDefinition === undefined) {
        console.warn("Unknown pattern type:", patternType);
        return;
    }

    const baseRow = Number(baseButton.dataset.row);
    const baseCol = Number(baseButton.dataset.col);
    const adjustedBase = adjustPatternBase(patternDefinition, baseRow, baseCol);
    const previewClass = decidePreviewClass(patternDefinition.offsets, adjustedBase);

    patternDefinition.offsets.forEach((offset) => {
        const row = adjustedBase.row + offset[0];
        const col = adjustedBase.col + offset[1];
        const button = findCellButton(row, col);

        if (button !== null) {
            button.classList.add(previewClass);
            previewCellButtons.push(button);
        }
    });
}

/**
 * パターンプレビューに使うCSSクラスを決めます。
 *
 * 盤面外にはみ出す場合は赤色用のpreview-invalid、
 * 既存の生きているセルと重なる場合は黄色用のpreview-overlap、
 * それ以外の場合は青色用のpreviewを返します。
 *
 * @param {number[][]} offsets パターンの相対座標
 * @param {{row: number, col: number}} adjustedBase 調整後の基準位置
 * @return {string} プレビューに使うCSSクラス名
 */
function decidePreviewClass(offsets, adjustedBase) {
    if (!isPatternInsideBoard(offsets, adjustedBase)) {
        return "preview-invalid";
    }

    if (overlapsAliveCell(offsets, adjustedBase)) {
        return "preview-overlap";
    }

    return "preview";
}

/**
 * 指定されたパターンが盤面内にすべて収まるかどうかを判定します。
 *
 * パターンのセルが1つでも盤面外になる場合はfalseを返します。
 *
 * @param {number[][]} offsets パターンの相対座標
 * @param {{row: number, col: number}} adjustedBase 調整後の基準位置
 * @return {boolean} すべて盤面内に収まる場合はtrue
 */
function isPatternInsideBoard(offsets, adjustedBase) {
    return offsets.every((offset) => {
        const row = adjustedBase.row + offset[0];
        const col = adjustedBase.col + offset[1];

        return findCellButton(row, col) !== null;
    });
}

/**
 * 指定された行番号・列番号に対応するセルボタンを探します。
 *
 * 見つからない場合はnullを返します。
 *
 * @param {number} row 行番号
 * @param {number} col 列番号
 * @return {HTMLButtonElement|null} 対応するセルボタン
 */
function findCellButton(row, col) {
    return document.querySelector(`.cell[data-row="${row}"][data-col="${col}"]`);
}

/**
 * パターン定義に従って、プレビュー表示や配置判定で使う基準位置を調整します。
 *
 * @param {object} patternDefinition パターン定義
 * @param {number} baseRow 元の基準行
 * @param {number} baseCol 元の基準列
 * @return {{row: number, col: number}} 調整後の基準位置
 */
function adjustPatternBase(patternDefinition, baseRow, baseCol) {
    return {
        row: baseRow + patternDefinition.rowAdjustment,
        col: baseCol + patternDefinition.colAdjustment
    };
}

/**
 * 現在表示しているパターンプレビューを消します。
 */
function clearPatternPreview() {
    previewCellButtons.forEach((button) => {
        button.classList.remove("preview");
        button.classList.remove("preview-overlap");
        button.classList.remove("preview-invalid");
    });

    previewCellButtons = [];
}

/**
 * 指定されたセル位置を基準に、選択中パターンを配置できるか判定します。
 *
 * パターン定義を取得できていない場合や、
 * パターンが盤面からはみ出す場合はfalseを返します。
 *
 * @param {HTMLButtonElement} baseButton 基準にするセルボタン
 * @return {boolean} 配置できる場合はtrue
 */
function canPlacePatternAt(baseButton) {
    if (!patternDefinitionsLoaded) {
        return false;
    }

    const patternType = patternTypeSelect.value;
    const patternDefinition = patternDefinitions[patternType];

    if (patternDefinition === undefined) {
        return false;
    }

    const baseRow = Number(baseButton.dataset.row);
    const baseCol = Number(baseButton.dataset.col);
    const adjustedBase = adjustPatternBase(patternDefinition, baseRow, baseCol);

    return isPatternInsideBoard(patternDefinition.offsets, adjustedBase);
}

/**
 * 指定されたパターンが、既存の生きているセルと重なるかどうかを判定します。
 *
 * 盤面内にあるプレビュー対象セルの中に、すでにaliveクラスを持つセルがあればtrueを返します。
 *
 * @param {number[][]} offsets パターンの相対座標
 * @param {{row: number, col: number}} adjustedBase 調整後の基準位置
 * @return {boolean} 既存の生きているセルと重なる場合はtrue
 */
function overlapsAliveCell(offsets, adjustedBase) {
    return offsets.some((offset) => {
        const row = adjustedBase.row + offset[0];
        const col = adjustedBase.col + offset[1];
        const button = findCellButton(row, col);

        return button !== null && button.classList.contains("alive");
    });
}