package com.mkunori.lifegame.model;

/**
 * 盤面を操作するときの大きな動作モードを表す列挙型です。
 *
 * セルを編集するのか、パターンを配置するのかを切り替えるために使います。
 */
public enum ActionMode {

    /**
     * セルを編集するモードです。
     *
     * Edit Modeで選択したToggle、Draw、Eraseに応じてセルを編集します。
     */
    EDIT_CELL("Edit Cell"),

    /**
     * パターンを配置するモードです。
     *
     * Patternで選択したパターンを、クリックした位置に配置します。
     */
    PLACE_PATTERN("Place Pattern");

    private final String displayName;

    /**
     * 操作モードを作成します。
     *
     * @param displayName 画面に表示する名前
     */
    ActionMode(String displayName) {
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