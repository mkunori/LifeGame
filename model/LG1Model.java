package model;

public class LG1Model {
    private int rows;
    private int cols;
    private boolean[][] grid;

    public LG1Model(int rows, int cols) {
        this.rows = rows;
        this.cols = cols;

        grid = new boolean[rows][cols];
    }

    public boolean getCell(int r, int c) {
        return grid[r][c];
    }

    public void toggleCell(int r, int c) {
        grid[r][c] = !grid[r][c];
    }

    // 次世代を生成する
    public void nextGeneration() {
        boolean[][] next = new boolean[rows][cols];

        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                int neighbor = countNeighbors(r, c); // 周囲の命の個数

                if (grid[r][c]) {
                    next[r][c] = (neighbor == 2 || neighbor == 3);
                } else {
                    next[r][c] = (neighbor == 3);
                }
            }
        }

        grid = next;
    }

    // 周囲の命の個数を計算する
    private int countNeighbors(int r, int c) {
        int count = 0;

        // 周囲を1マスずつ走査する
        for (int dr = -1; dr <= 1; dr++) {
            for (int dc = -1; dc <= 1; dc++) {
                if (dr == 0 && dc == 0) {
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
}
