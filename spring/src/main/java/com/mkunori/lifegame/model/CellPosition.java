package com.mkunori.lifegame.model;

/**
 * 盤面上の1つのセル位置を表す値オブジェクトです。
 *
 * @param row セルの行番号
 * @param col セルの列番号
 */
public record CellPosition(int row, int col) {
}