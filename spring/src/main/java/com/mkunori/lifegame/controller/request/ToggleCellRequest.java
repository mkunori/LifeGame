package com.mkunori.lifegame.controller.request;

/**
 * セルの生死切り替えAPIで受け取るリクエストです。
 *
 * JavaScriptから送られてくるJSONのrowとcolを受け取ります。
 *
 * @param row 切り替えるセルの行番号
 * @param col 切り替えるセルの列番号
 */
public record ToggleCellRequest(int row, int col) {
}