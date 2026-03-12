package model;

import java.util.Random;

public class LG1Model {
    private int rows;
    private int cols;
    private boolean[][] grid;

    // コンストラクタ
    public LG1Model(int rows, int cols) {
        this.rows = rows;
        this.cols = cols;

        grid = new boolean[rows][cols];
    }

    public int getRows() {
        return rows;
    }

    public int getCols() {
        return cols;
    }

    // セルの値を取得する
    public boolean getCell(int r, int c) {
        return grid[r][c];
    }

    // セルを反転する
    public void toggleCell(int r, int c) {
        grid[r][c] = !grid[r][c];
    }

    // 次世代を生成する
    public boolean nextGeneration() {
        boolean[][] next = new boolean[rows][cols];
        boolean changed = false;

        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                int neighbor = countNeighbors(r, c); // 周囲の命の個数

                if (grid[r][c]) {
                    next[r][c] = (neighbor == 2 || neighbor == 3);
                } else {
                    next[r][c] = (neighbor == 3);
                }

                // 次世代で変化したか？
                if (next[r][c] != grid[r][c]) {
                    changed = true;
                }
            }
        }
        grid = next;

        return changed;
    }

    // 周囲の生存セルの個数を計算する
    private int countNeighbors(int r, int c) {
        int count = 0;

        // 周囲の生存セルを1マスずつ走査する
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

    // 生き残りが居るか？
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

    // 初期配置で生存者をランダムに配置する
    public void randomize() {
        Random rand = new Random();

        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                // ライフゲームではランダム20%~35%くらいがバランスよい
                grid[r][c] = rand.nextDouble() < 0.3;
            }
        }
    }

    // 全セル死亡させる
    public void clear() {
        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                grid[r][c] = false;
            }
        }
    }

    // Gliderパターン
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
