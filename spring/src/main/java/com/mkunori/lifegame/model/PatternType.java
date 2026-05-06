package com.mkunori.lifegame.model;

/**
 * ライフゲームで配置できるパターンの種類を表す列挙型です。
 *
 * 画面の選択肢から送られてきた値を受け取り、どのパターンを配置するか判断するために使います。
 */
public enum PatternType {

    /**
     * 3つのセルが一直線に並ぶ振動子パターンです。
     */
    BLINKER("Blinker"),

    /**
     * 2x2の安定したパターンです。
     */
    BLOCK("Block"),

    /**
     * 斜め方向へ移動していく代表的なパターンです。
     */
    GLIDER("Glider"),

    /**
     * 6つのセルで構成される振動子パターンです。
     */
    TOAD("Toad"),

    /**
     * 2つのブロックが斜めに重なったような振動子パターンです。
     */
    BEACON("Beacon"),

    /**
     * グライダーを周期的に発射する有名な大型パターンです。
     */
    GOSPER_GLIDER_GUN("Gosper Glider Gun");

    private final String displayName;

    /**
     * パターン種別を作成します。
     *
     * @param displayName 画面に表示する名前
     */
    PatternType(String displayName) {
        this.displayName = displayName;
    }

    /**
     * 画面に表示する名前を返します。
     *
     * @return 画面に表示する名前
     */
    public String getDisplayName() {
        return displayName;
    }
}