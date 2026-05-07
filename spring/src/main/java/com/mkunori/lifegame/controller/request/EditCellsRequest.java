package com.mkunori.lifegame.controller.request;

import com.mkunori.lifegame.model.CellEditMode;
import com.mkunori.lifegame.model.CellPosition;

import java.util.List;

/**
 * 複数セル編集APIで受け取るリクエストです。
 *
 * JavaScriptから送られてくる編集モードと複数のセル位置を受け取り、
 * まとめてセルを編集するために使います。
 *
 * @param mode  セル編集モード
 * @param cells 編集するセル位置の一覧
 */
public record EditCellsRequest(CellEditMode mode, List<CellPosition> cells) {
}