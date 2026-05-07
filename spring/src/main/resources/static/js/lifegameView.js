// ==================================================
// LifeGame View
// ==================================================

/**
 * APIから受け取った盤面データを画面に反映します。
 *
 * @param {object} board Spring Boot側から返された盤面データ
 * @param {HTMLElement} generationValue 世代数を表示する要素
 * @param {NodeListOf<HTMLButtonElement>} cellButtons セルボタン一覧
 */
function updateBoardView(board, generationValue, cellButtons) {
    updateGeneration(board.generation, generationValue);
    updateCells(board.cells, cellButtons);
}

/**
 * 世代数の表示を更新します。
 *
 * @param {number} generation 現在の世代数
 * @param {HTMLElement} generationValue 世代数を表示する要素
 */
function updateGeneration(generation, generationValue) {
    generationValue.textContent = generation;
}

/**
 * セルの表示を更新します。
 *
 * @param {boolean[][]} cells セルの生死状態
 * @param {NodeListOf<HTMLButtonElement>} cellButtons セルボタン一覧
 */
function updateCells(cells, cellButtons) {
    cellButtons.forEach((button) => {
        const row = Number(button.dataset.row);
        const col = Number(button.dataset.col);
        const alive = cells[row][col];

        setCellViewOnly(button, alive);
    });
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
 * セルの見た目だけを反転します。
 *
 * @param {HTMLButtonElement} button 見た目を反転したいセルボタン
 */
function toggleCellViewOnly(button) {
    const isAlive = button.classList.contains("alive");
    setCellViewOnly(button, !isAlive);
}