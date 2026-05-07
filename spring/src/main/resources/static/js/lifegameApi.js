// ==================================================
// LifeGame API
// ==================================================

/**
 * 指定されたAPIを呼び出して、更新後の盤面データを取得します。
 *
 * requestBodyを渡した場合は、JSONとしてSpring Boot側へ送信します。
 * requestBodyを省略した場合は、POSTだけを送信します。
 *
 * @param {string} url 呼び出すAPIのURL
 * @param {object|null} requestBody APIへ送るJSONデータ
 * @return {Promise<object|null>} 更新後の盤面データ。失敗した場合はnull
 */
async function postBoardApi(url, requestBody = null) {
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
            console.error("Failed to update LifeGame:", response.status);
            return null;
        }

        return await response.json();
    } catch (error) {
        console.error("Error while updating LifeGame:", error);
        return null;
    }
}

/**
 * 盤面を1世代進めます。
 *
 * @return {Promise<object|null>} 更新後の盤面データ
 */
async function stepBoardApi() {
    return await postBoardApi("/lifegame/api/step");
}

/**
 * 指定された複数セルを編集します。
 *
 * @param {string} mode セル編集モード
 * @param {{row: number, col: number}[]} cells 編集するセル位置の一覧
 * @return {Promise<object|null>} 更新後の盤面データ
 */
async function editCellsApi(mode, cells) {
    return await postBoardApi("/lifegame/api/edit-cells", {
        mode: mode,
        cells: cells
    });
}

/**
 * 指定された位置にパターンを配置します。
 *
 * @param {string} patternType 配置するパターン種別
 * @param {number} row 基準にする行番号
 * @param {number} col 基準にする列番号
 * @return {Promise<object|null>} 更新後の盤面データ
 */
async function placePatternApi(patternType, row, col) {
    return await postBoardApi("/lifegame/api/pattern", {
        patternType: patternType,
        row: row,
        col: col
    });
}

/**
 * 盤面をクリアします。
 *
 * @return {Promise<object|null>} 更新後の盤面データ
 */
async function clearBoardApi() {
    return await postBoardApi("/lifegame/api/clear");
}

/**
 * 盤面を初期状態に戻します。
 *
 * @return {Promise<object|null>} 更新後の盤面データ
 */
async function resetBoardApi() {
    return await postBoardApi("/lifegame/api/reset");
}

/**
 * 盤面をランダム配置にします。
 *
 * @return {Promise<object|null>} 更新後の盤面データ
 */
async function randomizeBoardApi() {
    return await postBoardApi("/lifegame/api/random");
}