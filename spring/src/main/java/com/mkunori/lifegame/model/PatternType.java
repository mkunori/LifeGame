package com.mkunori.lifegame.model;

/**
 * ライフゲームで配置できるパターンの種類を表す列挙型です。
 *
 * 各パターンは、画面に表示する名前、配置するセルの相対座標、
 * そして必要に応じた配置位置の補正値を持ちます。
 */
public enum PatternType {

    /**
     * 3つのセルが一直線に並ぶ振動子パターンです。
     */
    BLINKER(
            "Blinker",
            new int[][] {
                    { 0, -1 },
                    { 0, 0 },
                    { 0, 1 }
            }),

    /**
     * 2x2の安定したパターンです。
     */
    BLOCK(
            "Block",
            new int[][] {
                    { 0, 0 },
                    { 0, 1 },
                    { 1, 0 },
                    { 1, 1 }
            }),

    /**
     * 斜め方向へ移動していく代表的なパターンです。
     */
    GLIDER(
            "Glider",
            new int[][] {
                    { 0, 1 },
                    { 1, 2 },
                    { 2, 0 },
                    { 2, 1 },
                    { 2, 2 }
            }),

    /**
     * 6つのセルで構成される振動子パターンです。
     */
    TOAD(
            "Toad",
            new int[][] {
                    { 0, 1 },
                    { 0, 2 },
                    { 0, 3 },
                    { 1, 0 },
                    { 1, 1 },
                    { 1, 2 }
            }),

    /**
     * 2つのブロックが斜めに重なったような振動子パターンです。
     */
    BEACON(
            "Beacon",
            new int[][] {
                    { 0, 0 },
                    { 0, 1 },
                    { 1, 0 },
                    { 1, 1 },
                    { 2, 2 },
                    { 2, 3 },
                    { 3, 2 },
                    { 3, 3 }
            }),

    /**
     * グライダーを周期的に発射する有名な大型パターンです。
     */
    GOSPER_GLIDER_GUN(
            "Gosper Glider Gun",
            new int[][] {
                    { 0, 24 },
                    { 1, 22 },
                    { 1, 24 },
                    { 2, 12 },
                    { 2, 13 },
                    { 2, 20 },
                    { 2, 21 },
                    { 2, 34 },
                    { 2, 35 },
                    { 3, 11 },
                    { 3, 15 },
                    { 3, 20 },
                    { 3, 21 },
                    { 3, 34 },
                    { 3, 35 },
                    { 4, 0 },
                    { 4, 1 },
                    { 4, 10 },
                    { 4, 16 },
                    { 4, 20 },
                    { 4, 21 },
                    { 5, 0 },
                    { 5, 1 },
                    { 5, 10 },
                    { 5, 14 },
                    { 5, 16 },
                    { 5, 17 },
                    { 5, 22 },
                    { 5, 24 },
                    { 6, 10 },
                    { 6, 16 },
                    { 6, 24 },
                    { 7, 11 },
                    { 7, 15 },
                    { 8, 12 },
                    { 8, 13 }
            },
            -4,
            -18),

    /**
     * 横方向へ移動する軽量宇宙船パターンです。
     */
    LWSS(
            "LWSS",
            new int[][] {
                    { 0, 1 },
                    { 0, 4 },
                    { 1, 0 },
                    { 2, 0 },
                    { 2, 4 },
                    { 3, 0 },
                    { 3, 1 },
                    { 3, 2 },
                    { 3, 3 }
            }),

    /**
     * 周期3で振動する有名な大型振動子パターンです。
     */
    PULSAR(
            "Pulsar",
            new int[][] {
                    { 0, 2 }, { 0, 3 }, { 0, 4 }, { 0, 8 }, { 0, 9 }, { 0, 10 },
                    { 2, 0 }, { 2, 5 }, { 2, 7 }, { 2, 12 },
                    { 3, 0 }, { 3, 5 }, { 3, 7 }, { 3, 12 },
                    { 4, 0 }, { 4, 5 }, { 4, 7 }, { 4, 12 },
                    { 5, 2 }, { 5, 3 }, { 5, 4 }, { 5, 8 }, { 5, 9 }, { 5, 10 },

                    { 7, 2 }, { 7, 3 }, { 7, 4 }, { 7, 8 }, { 7, 9 }, { 7, 10 },
                    { 8, 0 }, { 8, 5 }, { 8, 7 }, { 8, 12 },
                    { 9, 0 }, { 9, 5 }, { 9, 7 }, { 9, 12 },
                    { 10, 0 }, { 10, 5 }, { 10, 7 }, { 10, 12 },
                    { 12, 2 }, { 12, 3 }, { 12, 4 }, { 12, 8 }, { 12, 9 }, { 12, 10 }
            },
            -6,
            -6),

    /**
     * 周期15で振動する代表的な振動子パターンです。
     */
    PENTADECATHLON(
            "Pentadecathlon",
            new int[][] {
                    { 0, 1 },
                    { 1, 1 },
                    { 2, 0 }, { 2, 2 },
                    { 3, 1 },
                    { 4, 1 },
                    { 5, 1 },
                    { 6, 1 },
                    { 7, 0 }, { 7, 2 },
                    { 8, 1 },
                    { 9, 1 }
            },
            -4,
            -1),

    /**
     * 小さい初期配置から長時間複雑に変化する有名なパターンです。
     */
    R_PENTOMINO(
            "R-pentomino",
            new int[][] {
                    { 0, 1 },
                    { 0, 2 },
                    { 1, 0 },
                    { 1, 1 },
                    { 2, 1 }
            },
            -1,
            -1),

    /**
     * 少数のセルから長時間成長する有名なメソセラ型パターンです。
     */
    ACORN(
            "Acorn",
            new int[][] {
                    { 0, 1 },
                    { 1, 3 },
                    { 2, 0 },
                    { 2, 1 },
                    { 2, 4 },
                    { 2, 5 },
                    { 2, 6 }
            },
            -1,
            -3),

    /**
     * 長く変化したあと最終的に消滅する有名なパターンです。
     */
    DIEHARD(
            "Diehard",
            new int[][] {
                    { 0, 6 },
                    { 1, 0 }, { 1, 1 },
                    { 2, 1 },
                    { 2, 5 },
                    { 2, 6 },
                    { 2, 7 }
            },
            -1,
            -3);

    private final String displayName;
    private final int[][] offsets;
    private final int rowAdjustment;
    private final int colAdjustment;

    /**
     * パターン種別を作成します。
     *
     * 配置位置の補正が不要なパターンでは、
     * 行方向・列方向の補正値を0として扱います。
     *
     * @param displayName 画面に表示する名前
     * @param offsets     パターンを構成するセルの相対座標
     */
    PatternType(String displayName, int[][] offsets) {
        this(displayName, offsets, 0, 0);
    }

    /**
     * パターン種別を作成します。
     *
     * 大きなパターンなど、クリック位置や中央位置から少しずらして配置したい場合は、
     * rowAdjustmentとcolAdjustmentで基準位置を補正します。
     *
     * @param displayName   画面に表示する名前
     * @param offsets       パターンを構成するセルの相対座標
     * @param rowAdjustment 基準行に加える補正値
     * @param colAdjustment 基準列に加える補正値
     */
    PatternType(String displayName, int[][] offsets, int rowAdjustment, int colAdjustment) {
        this.displayName = displayName;
        this.offsets = offsets;
        this.rowAdjustment = rowAdjustment;
        this.colAdjustment = colAdjustment;
    }

    /**
     * 画面に表示する名前を返します。
     *
     * @return 画面に表示する名前
     */
    public String getDisplayName() {
        return displayName;
    }

    /**
     * パターンを構成するセルの相対座標を返します。
     *
     * 各要素は {行方向のずれ, 列方向のずれ} を表します。
     *
     * @return パターンを構成するセルの相対座標
     */
    public int[][] getOffsets() {
        return offsets;
    }

    /**
     * 基準行に加える補正値を返します。
     *
     * @return 基準行に加える補正値
     */
    public int getRowAdjustment() {
        return rowAdjustment;
    }

    /**
     * 基準列に加える補正値を返します。
     *
     * @return 基準列に加える補正値
     */
    public int getColAdjustment() {
        return colAdjustment;
    }
}