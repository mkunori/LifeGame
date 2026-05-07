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
// 同じセルを何度も反転しないために使います。
let draggedCellKeys = new Set();

// 1回のドラッグ操作中に通過したセル位置を記録します。
// マウスを離したときに、まとめてAPIへ送信します。
let draggedCells = [];


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

// パターン配置ボタンを取得します。
const placePatternButton = document.getElementById("placePatternButton");

// セル編集モード選択用のセレクトボックスを取得します。
const cellEditModeSelect = document.getElementById("cellEditMode");

// 盤面上のセルボタンをすべて取得します。
const cellButtons = document.querySelectorAll(".cell");

// 速度調整スライダーを取得します。
const speedSlider = document.getElementById("speedSlider");

// 現在の速度を表示する要素を取得します。
const speedValue = document.getElementById("speedValue");

// 世代数を表示している要素を取得します。
const generationValue = document.getElementById("generationValue");

// 盤面操作モード選択用のセレクトボックスを取得します。
const actionModeSelect = document.getElementById("actionMode");

// Edit Modeの補足説明を表示する要素を取得します。
const editModeHelp = document.getElementById("editModeHelp");

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
clearButton.addEventListener("click", () => {
    updateBoardByApi("/lifegame/api/clear");
});

// Resetボタンが押されたとき、APIを呼び出して初期状態へ戻します。
resetButton.addEventListener("click", () => {
    updateBoardByApi("/lifegame/api/reset");
});

// Randomボタンが押されたとき、APIを呼び出してランダム配置します。
randomButton.addEventListener("click", () => {
    updateBoardByApi("/lifegame/api/random");
});

// Placeボタンが押されたとき、選択されたパターンをAPIで配置します。
placePatternButton.addEventListener("click", () => {
    placePatternByApi();
});

// 各セルに、クリックとドラッグ描画用のイベントを登録します。
cellButtons.forEach((button) => {
    // マウスボタンを押したセルを編集し、ドラッグ開始状態にします。
    button.addEventListener("mousedown", (event) => {
        startCellDrag(event, button);
    });

    // ドラッグ中に別のセルへ入ったら、そのセルを編集します。
    button.addEventListener("mouseenter", () => {
        dragOverCell(button);
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

// Action Modeが変更されたとき、関連するUIの有効・無効を切り替えます。
actionModeSelect.addEventListener("change", () => {
    updateControlsForActionMode();
});


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
 * セルの見た目だけを反転します。
 *
 * @param {HTMLButtonElement} button 見た目を反転したいセルボタン
 */
function toggleCellViewOnly(button) {
    const isAlive = button.classList.contains("alive");
    setCellViewOnly(button, !isAlive);
}

/**
 * セルの見た目だけを指定された状態にします。
 *
 * @param {HTMLButtonElement} button 見た目を変更したいセルボタン
 * @param {boolean} alive 生きた状態として表示する場合はtrue
 */
function setCellViewOnly(button, alive) {
    button.classList.toggle("alive", alive);
    button.classList.toggle("dead", !alive);
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
 * Place PatternモードではEdit Modeは使わないため、
 * Edit Modeのセレクトボックスを無効化します。
 */
function updateControlsForActionMode() {
    const placePatternMode = isPlacePatternMode();

    cellEditModeSelect.disabled = placePatternMode;

    if (placePatternMode) {
        editModeHelp.textContent = "Place PatternではEdit Modeは使用しません";
    } else {
        editModeHelp.textContent = "";
    }
}

// ==================================================
// API呼び出し関連
// ==================================================

/**
 * Spring Boot側のAPIを呼び出して、盤面を1世代進めます。
 */
async function stepByApi() {
    await updateBoardByApi("/lifegame/api/step");
}

/**
 * 指定された複数セルを、選択中の編集モードでまとめて編集します。
 *
 * @param {{row: number, col: number}[]} cells 編集するセル位置の一覧
 */
async function editCellsByApi(cells) {
    const mode = cellEditModeSelect.value;

    await updateBoardByApi("/lifegame/api/edit-cells", {
        mode: mode,
        cells: cells
    });
}

/**
 * 指定されたセル位置を基準に、選択中のパターンを配置します。
 *
 * @param {HTMLButtonElement} button 配置位置として使うセルボタン
 */
async function placePatternAtCellByApi(button) {
    const patternType = patternTypeSelect.value;
    const row = Number(button.dataset.row);
    const col = Number(button.dataset.col);

    await updateBoardByApi("/lifegame/api/pattern", {
        patternType: patternType,
        row: row,
        col: col
    });
}

/**
 * 指定されたAPIを呼び出して、返ってきた盤面データを画面へ反映します。
 *
 * requestBodyを渡した場合は、JSONとしてSpring Boot側へ送信します。
 * requestBodyを省略した場合は、POSTだけを送信します。
 *
 * @param {string} url 呼び出すAPIのURL
 * @param {object|null} requestBody APIへ送るJSONデータ
 */
async function updateBoardByApi(url, requestBody = null) {
    try {
        const options = {
            method: "POST"
        };

        if (requestBody !== null) {
            options.headers = {
                "Content-Type": "application/json"
            };
            options.body = JSON.stringify(requestBody);
        }

        const response = await fetch(url, options);

        if (!response.ok) {
            stopAutoPlay();
            console.error("Failed to update LifeGame:", response.status);
            return;
        }

        const board = await response.json();
        updateBoardView(board);
    } catch (error) {
        stopAutoPlay();
        console.error("Error while updating LifeGame:", error);
    }
}

/**
 * 選択されたパターンをAPIで盤面中央に配置します。
 *
 * 盤面上のセルボタンから行数と列数を推定し、中央位置を基準として送信します。
 */
async function placePatternByApi() {
    const patternType = patternTypeSelect.value;
    const center = calculateBoardCenter();

    await updateBoardByApi("/lifegame/api/pattern", {
        patternType: patternType,
        row: center.row,
        col: center.col
    });
}

/**
 * 現在表示されている盤面の中央セル位置を計算します。
 *
 * セルボタンのdata-rowとdata-colをもとに最大行番号・最大列番号を探し、
 * その中央を返します。
 *
 * @return {{row: number, col: number}} 盤面中央のセル位置
 */
function calculateBoardCenter() {
    let maxRow = 0;
    let maxCol = 0;

    cellButtons.forEach((button) => {
        const row = Number(button.dataset.row);
        const col = Number(button.dataset.col);

        maxRow = Math.max(maxRow, row);
        maxCol = Math.max(maxCol, col);
    });

    return {
        row: Math.floor(maxRow / 2),
        col: Math.floor(maxCol / 2)
    };
}

// ==================================================
// 画面更新関連
// ==================================================

/**
 * APIから受け取った盤面データを画面に反映します。
 *
 * @param {object} board Spring Boot側から返された盤面データ
 */
function updateBoardView(board) {
    updateGeneration(board.generation);
    updateCells(board.cells);
}

/**
 * 世代数の表示を更新します。
 *
 * @param {number} generation 現在の世代数
 */
function updateGeneration(generation) {
    generationValue.textContent = generation;
}

/**
 * セルの表示を更新します。
 *
 * cellsはbooleanの二次元配列です。
 * trueならalive、falseならdeadとして表示を切り替えます。
 *
 * @param {boolean[][]} cells セルの生死状態
 */
function updateCells(cells) {
    cellButtons.forEach((button) => {
        const row = Number(button.dataset.row);
        const col = Number(button.dataset.col);
        const alive = cells[row][col];

        button.classList.toggle("alive", alive);
        button.classList.toggle("dead", !alive);
    });
}