package com.mkunori.lifegame.service;

import com.mkunori.lifegame.model.CellEditMode;
import com.mkunori.lifegame.model.CellPosition;
import com.mkunori.lifegame.model.LifeGameBoard;
import com.mkunori.lifegame.model.PatternType;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.web.context.annotation.SessionScope;

/**
 * ライフゲームの処理を担当するサービスクラスです。
 *
 * Controllerから呼び出され、盤面データの取得や更新を行います。
 * 画面から直接Modelを操作させず、Serviceを通して処理する構成にしています。
 */
@Service
@SessionScope
public class LifeGameService {

    private final LifeGameBoard board = new LifeGameBoard(60, 100);

    /**
     * 1回のセル編集APIで受け付ける最大セル数です。
     *
     * 極端に大きなリクエストによるCPU・メモリ負荷を避けるために使います。
     */
    private static final int MAX_EDIT_CELLS = 1000;

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
     * 極端に大きなリクエストや不正なリクエストを避けるため、
     * 編集モードやセル一覧の内容を確認してからModelへ委譲します。
     *
     * @param mode  セル編集モード
     * @param cells 編集するセル位置の一覧
     */
    public void editCells(CellEditMode mode, List<CellPosition> cells) {
        validateEditCellsRequest(mode, cells);

        board.editCells(mode, cells);
    }

    /**
     * 複数セル編集リクエストの内容を確認します。
     *
     * @param mode  セル編集モード
     * @param cells 編集するセル位置の一覧
     */
    private void validateEditCellsRequest(CellEditMode mode, List<CellPosition> cells) {
        if (mode == null) {
            throw new IllegalArgumentException("mode must not be null.");
        }

        if (cells == null) {
            throw new IllegalArgumentException("cells must not be null.");
        }

        if (cells.isEmpty()) {
            throw new IllegalArgumentException("cells must not be empty.");
        }

        if (cells.size() > MAX_EDIT_CELLS) {
            throw new IllegalArgumentException(
                    "cells must not contain more than " + MAX_EDIT_CELLS + " items.");
        }

        if (cells.stream().anyMatch(cell -> cell == null)) {
            throw new IllegalArgumentException("cells must not contain null.");
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

    /**
     * 盤面を指定されたサイズに変更します。
     *
     * サイズ変更時は、既存のセル状態を引き継がず、
     * 新しい空の盤面を作成します。
     *
     * @param rows 新しい行数
     * @param cols 新しい列数
     */
    public void resize(int rows, int cols) {
        board.resize(rows, cols);
    }
}