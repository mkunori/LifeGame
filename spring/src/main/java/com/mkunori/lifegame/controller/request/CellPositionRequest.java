package com.mkunori.lifegame.controller.request;

/**
 * 盤面上の1つのセル位置を表すリクエストです。
 *
 * ドラッグ描画などで、複数セルの位置をまとめて送るときに使います。
 *
 * @param row セルの行番号
 * @param col セルの列番号
 */
public record CellPositionRequest(int row, int col) {
}