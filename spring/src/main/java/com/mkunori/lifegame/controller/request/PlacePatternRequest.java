package com.mkunori.lifegame.controller.request;

import com.mkunori.lifegame.model.PatternType;

/**
 * パターン配置APIで受け取るリクエストです。
 *
 * JavaScriptから送られてくるパターン種別と配置位置を受け取ります。
 *
 * @param patternType 配置するパターンの種類
 * @param row         配置の基準にする行番号
 * @param col         配置の基準にする列番号
 */
public record PlacePatternRequest(PatternType patternType, int row, int col) {
}