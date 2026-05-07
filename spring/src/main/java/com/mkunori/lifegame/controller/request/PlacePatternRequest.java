package com.mkunori.lifegame.controller.request;

import com.mkunori.lifegame.model.PatternType;

/**
 * パターン配置APIで受け取るリクエストです。
 *
 * JavaScriptから送られてくるJSONのpatternTypeを受け取ります。
 *
 * @param patternType 配置するパターンの種類
 */
public record PlacePatternRequest(PatternType patternType) {
}