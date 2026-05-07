package com.mkunori.lifegame.controller.request;

import java.util.List;

/**
 * 複数セルの生死切り替えAPIで受け取るリクエストです。
 *
 * JavaScriptから送られてくる複数のセル位置を受け取り、
 * まとめてセルの生死を反転するために使います。
 *
 * @param cells 切り替えるセル位置の一覧
 */
public record ToggleCellsRequest(List<CellPositionRequest> cells) {
}