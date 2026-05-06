// 自動再生のタイマーIDを保存する変数です。
// null のときは自動再生していない状態です。
let autoPlayTimerId = null;

// 自動再生の間隔です。
// スライダー操作によって値を変更します。
let autoPlayIntervalMillis = 300;

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

// 速度調整スライダーを取得します。
const speedSlider = document.getElementById("speedSlider");

// 現在の速度を表示する要素を取得します。
const speedValue = document.getElementById("speedValue");

// 世代数を表示している要素を取得します。
const generationValue = document.getElementById("generationValue");

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

// スライダーを動かしたとき、自動再生の速度を変更します。
speedSlider.addEventListener("input", () => {
    changeSpeed(Number(speedSlider.value));
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

/**
 * Spring Boot側のAPIを呼び出して、盤面を1世代進めます。
 *
 * APIから返ってきた盤面データを使って、
 * HTML上のセル表示と世代数表示を更新します。
 */
async function stepByApi() {
    await updateBoardByApi("/lifegame/api/step");
}

/**
 * 指定されたAPIを呼び出して、返ってきた盤面データを画面へ反映します。
 *
 * Clear、Reset、Randomなど、盤面を更新してLifeGameBoardを返すAPIで共通利用します。
 *
 * @param {string} url 呼び出すAPIのURL
 */
async function updateBoardByApi(url) {
    try {
        const response = await fetch(url, {
            method: "POST"
        });

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