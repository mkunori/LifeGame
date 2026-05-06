package com.mkunori.lifegame.service;

import com.mkunori.lifegame.model.LifeGameBoard;
import com.mkunori.lifegame.model.PatternType;
import org.springframework.stereotype.Service;

/**
 * ライフゲームの処理を担当するサービスクラスです。
 *
 * Controllerから呼び出され、盤面データの取得や更新を行います。
 * 画面から直接Modelを操作させず、Serviceを通して処理する構成にしています。
 */
@Service
public class LifeGameService {

    private final LifeGameBoard board = new LifeGameBoard(30, 50);

    /**
     * 現在の盤面を返します。
     *
     * @return 現在のライフゲーム盤面
     */
    public LifeGameBoard getBoard() {
        return board;
    }

    /**
     * 盤面を1世代進めます。
     */
    public void nextGeneration() {
        board.nextGeneration();
    }

    /**
     * 盤面上のすべてのセルを死んだ状態にします。
     */
    public void clear() {
        board.clear();
    }

    /**
     * 盤面を初期状態に戻します。
     */
    public void reset() {
        board.reset();
    }

    /**
     * 盤面上のセルをランダムに生きた状態または死んだ状態にします。
     */
    public void randomize() {
        board.randomize();
    }

    /**
     * 指定された位置のセルの生死を切り替えます。
     *
     * @param row 行番号
     * @param col 列番号
     */
    public void toggleCell(int row, int col) {
        board.toggleCell(row, col);
    }

    /**
     * 指定されたパターンを盤面中央に配置します。
     *
     * @param patternType 配置するパターンの種類
     */
    public void placePattern(PatternType patternType) {
        board.placePattern(patternType);
    }
}