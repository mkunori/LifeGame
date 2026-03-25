package model;

import java.util.Random;

/**
 * LifeGame1Go の盤面状態とゲームルールを管理するモデルクラス。
 */
public class LifeGameModel {

    /** ランダム初期化の生存確率 */
    private static final double DEFAULT_ALIVE_PROBABILITY = 0.3;

    /** 盤面の行数 */
    private int rows;

    /** 盤面の列数 */
    private int cols;

    /** 盤面の1セルごとの状態 */
    private boolean[][] grid;

    /** 世代数 */
    private int generation;

    /**
     * 指定した行数と列数でモデルを生成する。
     * 
     * @param rows 盤面の行数
     * @param cols 盤面の列数
     */
    public LifeGameModel(int rows, int cols) {
        this.rows = rows;
        this.cols = cols;

        grid = new boolean[rows][cols];
        resetGeneration();
    }

    /**
     * 盤面の行数を返す。
     *
     * @return 行数
     */
    public int getRows() {
        return rows;
    }

    /**
     * 盤面の列数を返す。
     *
     * @return 列数
     */
    public int getCols() {
        return cols;
    }

    /**
     * 現在の世代数を返す。
     * 
     * @return 世代数
     */
    public int getGeneration() {
        return generation;
    }

    /**
     * 世代数を 1 増やす。
     */
    public void incrementGeneration() {
        generation++;
    }

    /**
     * 世代数を 0 に戻す。
     */
    public void resetGeneration() {
        generation = 0;
    }

    /**
     * 指定したセルの状態を返す。
     *
     * @param r 行番号
     * @param c 列番号
     * @return 生存していれば true、死亡していれば false
     */
    public boolean getCell(int r, int c) {
        return grid[r][c];
    }

    /**
     * 指定したセルの状態を反転する。
     *
     * @param r 行番号
     * @param c 列番号
     */
    public void toggleCell(int r, int c) {
        grid[r][c] = !grid[r][c];
    }

    /**
     * 次世代の盤面を生成する。
     *
     * @return 盤面に変化があった場合 true
     */
    public boolean nextGeneration() {

        boolean[][] next = new boolean[rows][cols];
        boolean changed = false;

        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {

                int neighbor = countNeighbors(r, c);

                if (grid[r][c]) {
                    next[r][c] = (neighbor == 2 || neighbor == 3);
                } else {
                    next[r][c] = (neighbor == 3);
                }

                if (next[r][c] != grid[r][c]) {
                    changed = true;
                }
            }
        }

        grid = next;
        return changed;
    }

    /**
     * 指定したセルの周囲 8 マスに存在する生存セル数を数える。
     *
     * @param r 基準セルの行番号
     * @param c 基準セルの列番号
     * @return 周囲の生存セル数
     */
    private int countNeighbors(int r, int c) {

        int count = 0;

        for (int dr = -1; dr <= 1; dr++) {
            for (int dc = -1; dc <= 1; dc++) {

                if (dr == 0 && dc == 0) { // 自分自身は走査しない。
                    continue;
                }

                int nr = r + dr;
                int nc = c + dc;

                if (nr >= 0 && nr < rows && nc >= 0 && nc < cols) {
                    count += grid[nr][nc] ? 1 : 0;
                }
            }
        }

        return count;
    }

    /**
     * 生存しているセルが存在するか判定する。
     *
     * @return 生存セルが存在する場合 true
     */
    public boolean hasAliveCells() {

        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {

                if (grid[r][c]) {
                    return true;
                }
            }
        }

        return false;
    }

    /**
     * 盤面をランダムな状態で初期化する。
     * 各セルは DEFAULT_ALIVE_PROBABILITY の確率で生存状態になる。
     */
    public void randomize() {

        Random rand = new Random();

        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                grid[r][c] = rand.nextDouble() < DEFAULT_ALIVE_PROBABILITY;
            }
        }
    }

    /**
     * 盤面をクリアする。
     * 全セルを死亡状態にする。
     */
    public void clear() {

        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                grid[r][c] = false;
            }
        }
    }

    /**
     * 指定したパターンを指定位置に配置する。
     * 
     * @param patternType 配置するパターン
     * @param startRow    配置開始行
     * @param startCol    配置開始列
     */
    public void placePattern(PatternType patternType, int startRow, int startCol) {
        placePattern(patternType.getCells(), startRow, startCol);
    }

    /**
     * 指定した相対座標のパターンを盤面に配置する。
     * パターンの一部が盤面外に出る場合は何もしない。
     * 
     * @param pattern  配置するパターンの相対座標
     * @param startRow 配置開始行
     * @param startCol 配置開始列
     */
    private void placePattern(int[][] pattern, int startRow, int startCol) {

        if (!canPlacePattern(pattern, startRow, startCol)) {
            return;
        }

        for (int[] cell : pattern) {
            int row = startRow + cell[0];
            int col = startCol + cell[1];
            grid[row][col] = true;
        }
    }

    /**
     * 指定した位置にパターンを配置できるか判定する。
     * 
     * @param pattern  判定するパターンの相対座標
     * @param startRow 配置開始行
     * @param startCol 配置開始列
     * @return 配置できる場合 true
     */
    private boolean canPlacePattern(int[][] pattern, int startRow, int startCol) {

        for (int[] cell : pattern) {
            int row = startRow + cell[0];
            int col = startCol + cell[1];

            if (row < 0 || row >= rows || col < 0 || col >= cols) {
                return false;
            }
        }

        return true;
    }
}
