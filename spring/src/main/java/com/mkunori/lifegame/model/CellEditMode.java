package com.mkunori.lifegame.model;

/**
 * セル編集時の動作モードを表す列挙型です。
 *
 * ドラッグ描画やセルクリック時に、セルを反転するのか、
 * 生きた状態にするのか、死んだ状態にするのかを切り替えるために使います。
 */
public enum CellEditMode {

    /**
     * セルの生死を反転します。
     */
    TOGGLE("Toggle"),

    /**
     * セルを生きた状態にします。
     */
    DRAW("Draw"),

    /**
     * セルを死んだ状態にします。
     */
    ERASE("Erase");

    private final String displayName;

    /**
     * セル編集モードを作成します。
     *
     * @param displayName 画面に表示する名前
     */
    CellEditMode(String displayName) {
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