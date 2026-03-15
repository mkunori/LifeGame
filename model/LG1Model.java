package model;

import java.util.Random;

/**
 * LifeGame1Go の盤面状態とゲームルールを管理するモデルクラス。
 */
public class LG1Model {
    private int rows;
    private int cols;
    private boolean[][] grid;

    /**
     * LifeGame1Go の盤面状態とゲームルールを管理するモデルクラス。
     */
    public LG1Model(int rows, int cols) {
        this.rows = rows;
        this.cols = cols;

        grid = new boolean[rows][cols];
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
                if (dr == 0 && dc == 0) { // 自分自身は走査しない
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
     * 各セルは 30% の確率で生存状態になる。
     */
    public void randomize() {

        Random rand = new Random();

        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {

                grid[r][c] = rand.nextDouble() < 0.3;
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
     * 指定位置を左上として Glider パターンを配置する。
     *
     * @param r 配置開始行
     * @param c 配置開始列
     */
    public void placeGlider(int r, int c) {
        if (r + 2 >= rows || c + 2 >= cols) {
            return;
        }
        grid[r][c + 1] = true;
        grid[r + 1][c + 2] = true;
        grid[r + 2][c] = true;
        grid[r + 2][c + 1] = true;
        grid[r + 2][c + 2] = true;
    }
}
