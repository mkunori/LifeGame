package com.mkunori.lifegame.model;

import java.util.Random;

/**
 * ライフゲームの盤面を表すクラスです。
 *
 * 盤面の行数、列数、生きているセルの状態を管理します。
 * このクラスは画面表示には直接関わらず、ライフゲームのデータとルールを担当します。
 */
public class LifeGameBoard {

    private final int rows;
    private final int cols;
    private boolean[][] cells;
    private final Random random = new Random();

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
     * 盤面を1世代進めます。
     *
     * 現在の盤面をもとに次の世代の盤面を作成し、
     * 最後に現在の盤面として置き換えます。
     */
    public void nextGeneration() {
        boolean[][] nextCells = new boolean[rows][cols];

        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < cols; col++) {
                int aliveNeighbors = countAliveNeighbors(row, col);

                if (cells[row][col]) {
                    nextCells[row][col] = aliveNeighbors == 2 || aliveNeighbors == 3;
                } else {
                    nextCells[row][col] = aliveNeighbors == 3;
                }
            }
        }

        cells = nextCells;
    }

    /**
     * 盤面上のすべてのセルを死んだ状態にします。
     */
    public void clear() {
        cells = new boolean[rows][cols];
    }

    /**
     * 盤面上のセルをランダムに生きた状態または死んだ状態にします。
     *
     * 現在は、およそ4分の1の確率でセルが生きるようにしています。
     */
    public void randomize() {
        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < cols; col++) {
                cells[row][col] = random.nextInt(4) == 0;
            }
        }
    }

    /**
     * 指定されたセルの周囲にある生きたセルの数を数えます。
     *
     * ライフゲームでは、上下左右と斜めを含む8方向のセルを隣接セルとして扱います。
     *
     * @param targetRow 調べたいセルの行番号
     * @param targetCol 調べたいセルの列番号
     * @return 周囲にある生きたセルの数
     */
    private int countAliveNeighbors(int targetRow, int targetCol) {
        int count = 0;

        for (int rowOffset = -1; rowOffset <= 1; rowOffset++) {
            for (int colOffset = -1; colOffset <= 1; colOffset++) {
                if (rowOffset == 0 && colOffset == 0) {
                    continue;
                }

                int neighborRow = targetRow + rowOffset;
                int neighborCol = targetCol + colOffset;

                if (isInside(neighborRow, neighborCol) && cells[neighborRow][neighborCol]) {
                    count++;
                }
            }
        }

        return count;
    }

    /**
     * 指定された位置が盤面の内側かどうかを判定します。
     *
     * @param row 行番号
     * @param col 列番号
     * @return 盤面の内側ならtrue、外側ならfalse
     */
    private boolean isInside(int row, int col) {
        return 0 <= row && row < rows && 0 <= col && col < cols;
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