package com.mkunori.lifegame.service;

import com.mkunori.lifegame.controller.request.CellPositionRequest;
import com.mkunori.lifegame.model.CellEditMode;
import com.mkunori.lifegame.model.CellPosition;
import com.mkunori.lifegame.model.LifeGameBoard;
import com.mkunori.lifegame.model.PatternType;

import java.util.List;

import org.springframework.stereotype.Service;

/**
 * ライフゲームの処理を担当するサービスクラスです。
 *
 * Controllerから呼び出され、盤面データの取得や更新を行います。
 * 画面から直接Modelを操作させず、Serviceを通して処理する構成にしています。
 */
@Service
public class LifeGameService {

    private final LifeGameBoard board = new LifeGameBoard(40, 70);

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
     * 指定された複数セルを、編集モードに応じてまとめて編集します。
     *
     * @param mode  セル編集モード
     * @param cells 編集するセル位置の一覧
     */
    public void editCells(CellEditMode mode, List<CellPosition> cells) {
        for (CellPosition cell : cells) {
            editCell(mode, cell.row(), cell.col());
        }
    }

    /**
     * 指定された1つのセルを、編集モードに応じて編集します。
     *
     * @param mode セル編集モード
     * @param row  行番号
     * @param col  列番号
     */
    private void editCell(CellEditMode mode, int row, int col) {
        switch (mode) {
            case TOGGLE -> board.toggleCell(row, col);
            case DRAW -> board.setCellAlive(row, col);
            case ERASE -> board.setCellDead(row, col);
        }
    }

    /**
     * 指定されたパターンを、指定位置を基準に配置します。
     *
     * @param patternType 配置するパターンの種類
     * @param row         基準にする行番号
     * @param col         基準にする列番号
     */
    public void placePattern(PatternType patternType, int row, int col) {
        board.placePattern(patternType, row, col);
    }
}