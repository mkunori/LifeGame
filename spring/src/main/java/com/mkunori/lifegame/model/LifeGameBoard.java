package com.mkunori.lifegame.model;

/**
 * ライフゲームの盤面を表すクラスです。
 *
 * 盤面の行数、列数、生きているセルの状態を管理します。
 * このクラスは画面表示には直接関わらず、ライフゲームのデータを担当します。
 */
public class LifeGameBoard {

    private final int rows;
    private final int cols;
    private final boolean[][] cells;

    /**
     * 指定された行数と列数で盤面を作成します。
     *
     * @param rows 盤面の行数
     * @param cols 盤面の列数
     */
    public LifeGameBoard(int rows, int cols) {
        this.rows = rows;
        this.cols = cols;
        this.cells = new boolean[rows][cols];
        initializeSamplePattern();
    }

    /**
     * 盤面の行数を返します。
     *
     * @return 盤面の行数
     */
    public int getRows() {
        return rows;
    }

    /**
     * 盤面の列数を返します。
     *
     * @return 盤面の列数
     */
    public int getCols() {
        return cols;
    }

    /**
     * 盤面全体のセル状態を返します。
     *
     * @return セルの生死状態を表す二次元配列
     */
    public boolean[][] getCells() {
        return cells;
    }

    /**
     * 指定された位置のセルが生きているかどうかを返します。
     *
     * @param row 行番号
     * @param col 列番号
     * @return セルが生きている場合はtrue、死んでいる場合はfalse
     */
    public boolean isAlive(int row, int col) {
        return cells[row][col];
    }

    /**
     * 動作確認用の初期パターンを配置します。
     *
     * ここでは、ライフゲームでよく使われるブリンカーを配置しています。
     */
    private void initializeSamplePattern() {
        int centerRow = rows / 2;
        int centerCol = cols / 2;

        cells[centerRow][centerCol - 1] = true;
        cells[centerRow][centerCol] = true;
        cells[centerRow][centerCol + 1] = true;
    }
}