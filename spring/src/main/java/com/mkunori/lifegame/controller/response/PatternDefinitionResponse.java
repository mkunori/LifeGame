package com.mkunori.lifegame.controller.response;

import com.mkunori.lifegame.model.PatternType;

/**
 * パターン定義APIで返すレスポンスです。
 *
 * JavaScript側のパターンプレビューで使うために、
 * パターン種別、表示名、相対座標、配置補正値を返します。
 *
 * @param patternType   パターン種別のenum名
 * @param displayName   画面表示用の名前
 * @param offsets       パターンを構成するセルの相対座標
 * @param rowAdjustment 基準行に加える補正値
 * @param colAdjustment 基準列に加える補正値
 */
public record PatternDefinitionResponse(
        String patternType,
        String displayName,
        int[][] offsets,
        int rowAdjustment,
        int colAdjustment) {

    /**
     * PatternTypeからレスポンスを作成します。
     *
     * @param patternType パターン種別
     * @return パターン定義レスポンス
     */
    public static PatternDefinitionResponse from(PatternType patternType) {
        return new PatternDefinitionResponse(
                patternType.name(),
                patternType.getDisplayName(),
                patternType.getOffsets(),
                patternType.getRowAdjustment(),
                patternType.getColAdjustment());
    }
}