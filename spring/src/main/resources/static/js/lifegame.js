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

// 盤面上のセルボタンをすべて取得します。
const cellButtons = document.querySelectorAll(".cell");

// 速度調整スライダーを取得します。
const speedSlider = document.getElementById("speedSlider");

// 現在の速度を表示する要素を取得します。
const speedValue = document.getElementById("speedValue");

// 世代数を表示している要素を取得します。
const generationValue = document.getElementById("generationValue");


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
    // マウスボタンを押したセルを反転し、ドラッグ開始状態にします。
    button.addEventListener("mousedown", (event) => {
        startCellDrag(event, button);
    });

    // ドラッグ中に別のセルへ入ったら、そのセルを反転します。
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
 * マウスボタンを押したセルを反転し、
 * その後に通過したセルも反転できる状態にします。
 *
 * @param {MouseEvent} event マウス操作のイベント
 * @param {HTMLButtonElement} button マウスボタンが押されたセルボタン
 */
function startCellDrag(event, button) {
    // 左クリック以外は処理しません。
    if (event.button !== 0) {
        return;
    }

    isDragging = true;
    draggedCellKeys = new Set();

    toggleCellOnceDuringDrag(button);
}

/**
 * ドラッグ中にセルへ入ったときの処理です。
 *
 * ドラッグ中でなければ何もしません。
 * ドラッグ中であれば、そのセルを1回だけ反転します。
 *
 * @param {HTMLButtonElement} button ドラッグ中に入ったセルボタン
 */
function dragOverCell(button) {
    if (!isDragging) {
        return;
    }

    toggleCellOnceDuringDrag(button);
}

/**
 * セルのドラッグ操作を終了します。
 *
 * マウスボタンが離されたらドラッグ状態を解除し、
 * 処理済みセルの記録もクリアします。
 */
function endCellDrag() {
    isDragging = false;
    draggedCellKeys.clear();
}

/**
 * 1回のドラッグ操作中に、指定されたセルを1回だけ反転します。
 *
 * 同じセルを何度も通過しても、同じドラッグ中は再反転しません。
 *
 * @param {HTMLButtonElement} button 反転したいセルボタン
 */
function toggleCellOnceDuringDrag(button) {
    const cellKey = createCellKey(button);

    if (draggedCellKeys.has(cellKey)) {
        return;
    }

    draggedCellKeys.add(cellKey);
    toggleCellByApi(button);
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
// API呼び出し関連
// ==================================================

/**
 * Spring Boot側のAPIを呼び出して、盤面を1世代進めます。
 */
async function stepByApi() {
    await updateBoardByApi("/lifegame/api/step");
}

/**
 * 指定されたセルボタンの行番号・列番号を使って、セルの生死を切り替えます。
 *
 * セルボタンに設定されているdata-rowとdata-colを読み取り、
 * JSONリクエストとしてSpring Boot側のtoggle APIへ送信します。
 *
 * @param {HTMLButtonElement} button クリックされたセルボタン
 */
async function toggleCellByApi(button) {
    const row = Number(button.dataset.row);
    const col = Number(button.dataset.col);

    await updateBoardByApi("/lifegame/api/toggle", {
        row: row,
        col: col
    });
}

/**
 * 選択されたパターンをAPIで盤面中央に配置します。
 *
 * セレクトボックスで選ばれているPatternTypeの値を読み取り、
 * JSONリクエストとしてSpring Boot側のパターン配置APIへ送信します。
 */
async function placePatternByApi() {
    const patternType = patternTypeSelect.value;

    await updateBoardByApi("/lifegame/api/pattern", {
        patternType: patternType
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