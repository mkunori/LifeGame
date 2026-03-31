package controller;

import model.PatternType;

/**
 * 盤面クリック時の動作モード。
 */
public enum ClickMode {
    TOGGLE("Toggle"),
    GLIDER("Glider"),
    BLOCK("Block"),
    BLINKER("Blinker"),
    TOAD("Toad"),
    BEACON("Beacon"),
    GOSPER_GLIDER_GUN("Gosper Glider Gun");

    /** 画面表示用の名前 */
    private final String displayName;

    /**
     * 動作モードを生成する。
     * 
     * @param displayName 画面表示用の名前
     */
    ClickMode(String displayName) {
        this.displayName = displayName;
    }

    /**
     * 対応する配置パターンを返す。
     * Toggleモードではnullを返す。
     * 
     * @return 対応する配置パターン
     */
    public PatternType getPatternType() {
        return switch (this) {
            case TOGGLE -> null;
            case GLIDER -> PatternType.GLIDER;
            case BLOCK -> PatternType.BLOCK;
            case BLINKER -> PatternType.BLINKER;
            case TOAD -> PatternType.TOAD;
            case BEACON -> PatternType.BEACON;
            case GOSPER_GLIDER_GUN -> PatternType.GOSPER_GLIDER_GUN;
        };
    }

    /**
     * 画面表示用の名前を返す。
     * 
     * @return 画面に表示する名前
     */
    @Override
    public String toString() {
        return displayName;
    }

    /**
     * 選択されたモードをControllerに適用する。
     * 
     * @param controller コントローラ
     */
    public void apply(LGController controller) {
        switch (this) {
            case TOGGLE -> controller.setToggleMode();
            case GLIDER -> controller.setGliderMode();
            case BLOCK -> controller.setBlockMode();
            case BLINKER -> controller.setBlinkerMode();
            case TOAD -> controller.setToadMode();
            case BEACON -> controller.setBeaconMode();
            case GOSPER_GLIDER_GUN -> controller.setGosperGliderGunMode();
        }
    }
}
