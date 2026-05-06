// 自動再生のタイマーIDを保存する変数です。
// null のときは自動再生していない状態です。
let autoPlayTimerId = null;

// 自動再生の間隔です。
// 300ミリ秒ごとに1世代進めます。
const autoPlayIntervalMillis = 300;

// Startボタンを取得します。
const startButton = document.getElementById("startButton");

// Stopボタンを取得します。
const stopButton = document.getElementById("stopButton");

// 世代数を表示している要素を取得します。
const generationValue = document.getElementById("generationValue");

// Startボタンが押されたとき、自動再生を開始します。
startButton.addEventListener("click", () => {
    startAutoPlay();
});

// Stopボタンが押されたとき、自動再生を停止します。
stopButton.addEventListener("click", () => {
    stopAutoPlay();
});

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
 * Spring Boot側のAPIを呼び出して、盤面を1世代進めます。
 *
 * APIから返ってきた盤面データを使って、
 * HTML上のセル表示と世代数表示を更新します。
 */
async function stepByApi() {
    try {
        const response = await fetch("/lifegame/api/step", {
            method: "POST"
        });

        if (!response.ok) {
            stopAutoPlay();
            console.error("Failed to step LifeGame:", response.status);
            return;
        }

        const board = await response.json();
        updateBoardView(board);
    } catch (error) {
        stopAutoPlay();
        console.error("Error while stepping LifeGame:", error);
    }
}

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
    const cellButtons = document.querySelectorAll(".cell");

    cellButtons.forEach((button) => {
        const row = Number(button.dataset.row);
        const col = Number(button.dataset.col);
        const alive = cells[row][col];

        button.classList.toggle("alive", alive);
        button.classList.toggle("dead", !alive);
    });
}