package com.mkunori.lifegame.controller.request;

/**
 * 盤面サイズ変更APIで受け取るリクエストです。
 *
 * JavaScriptから送られてくる行数と列数を受け取り、
 * 新しいサイズの盤面を作成するために使います。
 *
 * @param rows 新しい行数
 * @param cols 新しい列数
 */
public record ResizeBoardRequest(int rows, int cols) {
}