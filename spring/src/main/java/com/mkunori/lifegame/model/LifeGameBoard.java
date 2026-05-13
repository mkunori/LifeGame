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
    private int generation;
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
        this.generation = 0;
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
     * 現在の世代数を返します。
     *
     * @return 現在の世代数
     */
    public int getGeneration() {
        return generation;
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
     * 指定された位置のセルの生死を切り替えます。
     *
     * 生きているセルなら死んだ状態にし、死んでいるセルなら生きた状態にします。
     * 盤面の外側が指定された場合は、何もせずに処理を終えます。
     *
     * @param row 行番号
     * @param col 列番号
     */
    public void toggleCell(int row, int col) {
        if (!isInside(row, col)) {
            return;
        }

        cells[row][col] = !cells[row][col];
    }

    /**
     * 指定された位置のセルを生きた状態にします。
     *
     * 盤面の外側が指定された場合は、何もせずに処理を終えます。
     *
     * @param row 行番号
     * @param col 列番号
     */
    public void setCellAlive(int row, int col) {
        if (!isInside(row, col)) {
            return;
        }

        cells[row][col] = true;
    }

    /**
     * 指定された位置のセルを死んだ状態にします。
     *
     * 盤面の外側が指定された場合は、何もせずに処理を終えます。
     *
     * @param row 行番号
     * @param col 列番号
     */
    public void setCellDead(int row, int col) {
        if (!isInside(row, col)) {
            return;
        }

        cells[row][col] = false;
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
        generation++;
    }

    /**
     * 盤面上のすべてのセルを死んだ状態にします。
     */
    public void clear() {
        cells = new boolean[rows][cols];
        generation = 0;
    }

    /**
     * 盤面を初期状態に戻します。
     *
     * すべてのセルを死んだ状態にしたあと、動作確認用の初期パターンを配置します。
     * 世代数も0に戻します。
     */
    public void reset() {
        clear();
        initializeSamplePattern();
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

        generation = 0;
    }

    /**
     * 指定されたパターンを盤面中央に配置します。
     *
     * 配置前に盤面をクリアし、選択されたパターンだけを表示します。
     * 世代数も0に戻します。
     *
     * @param patternType 配置するパターンの種類
     */
    public void placePattern(PatternType patternType) {
        int baseRow = rows / 2;
        int baseCol = cols / 2;

        placePattern(patternType, baseRow, baseCol, true);
    }

    /**
     * 指定されたパターンを指定位置に配置します。
     *
     * 既存のセルは消さず、指定された位置を基準にパターンを追加します。
     * 世代数は0に戻します。
     *
     * @param patternType 配置するパターンの種類
     * @param baseRow     基準にする行番号
     * @param baseCol     基準にする列番号
     */
    public void placePattern(PatternType patternType, int baseRow, int baseCol) {
        placePattern(patternType, baseRow, baseCol, false);
    }

    /**
     * 指定されたパターンを配置します。
     *
     * clearBeforePlaceがtrueの場合は、配置前に盤面をクリアします。
     * falseの場合は、現在の盤面に追加する形で配置します。
     *
     * @param patternType      配置するパターンの種類
     * @param baseRow          基準にする行番号
     * @param baseCol          基準にする列番号
     * @param clearBeforePlace 配置前に盤面をクリアする場合はtrue
     */
    private void placePattern(
            PatternType patternType, int baseRow, int baseCol, boolean clearBeforePlace) {
        if (clearBeforePlace) {
            clear();
        }

        int adjustedBaseRow = baseRow + patternType.getRowAdjustment();
        int adjustedBaseCol = baseCol + patternType.getColAdjustment();

        placeCells(patternType.getOffsets(), adjustedBaseRow, adjustedBaseCol);

        generation = 0;
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
     * 基準位置からの相対座標をもとに、複数のセルを生きた状態にします。
     *
     * offsetsには、基準位置から見た行方向・列方向のずれを指定します。
     * 盤面の外側になるセルは無視します。
     *
     * @param offsets 配置するセルの相対座標
     * @param baseRow 基準にする行番号
     * @param baseCol 基準にする列番号
     */
    private void placeCells(int[][] offsets, int baseRow, int baseCol) {
        for (int[] offset : offsets) {
            int row = baseRow + offset[0];
            int col = baseCol + offset[1];

            if (isInside(row, col)) {
                cells[row][col] = true;
            }
        }
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