package com.mkunori.lifegame.service;

import static org.junit.jupiter.api.Assertions.assertThrows;

import com.mkunori.lifegame.model.CellEditMode;
import com.mkunori.lifegame.model.CellPosition;
import com.mkunori.lifegame.model.PatternType;

import java.util.List;

import org.junit.jupiter.api.Test;

/**
 * LifeGameServiceの入力値チェックを確認するテストクラスです。
 */
class LifeGameServiceTest {

    /**
     * セル編集で、行番号が負数の場合は例外になることを確認します。
     */
    @Test
    void editCells_行番号が負数なら例外になる() {
        LifeGameService service = new LifeGameService();

        assertThrows(
                IllegalArgumentException.class,
                () -> service.editCells(
                        CellEditMode.DRAW,
                        List.of(new CellPosition(-1, 0))));
    }

    /**
     * セル編集で、列番号が負数の場合は例外になることを確認します。
     */
    @Test
    void editCells_列番号が負数なら例外になる() {
        LifeGameService service = new LifeGameService();

        assertThrows(
                IllegalArgumentException.class,
                () -> service.editCells(
                        CellEditMode.DRAW,
                        List.of(new CellPosition(0, -1))));
    }

    /**
     * セル編集で、行番号が盤面の行数以上の場合は例外になることを確認します。
     */
    @Test
    void editCells_行番号が盤面外なら例外になる() {
        LifeGameService service = new LifeGameService();
        int outOfRangeRow = service.getBoard().getRows();

        assertThrows(
                IllegalArgumentException.class,
                () -> service.editCells(
                        CellEditMode.DRAW,
                        List.of(new CellPosition(outOfRangeRow, 0))));
    }

    /**
     * セル編集で、列番号が盤面の列数以上の場合は例外になることを確認します。
     */
    @Test
    void editCells_列番号が盤面外なら例外になる() {
        LifeGameService service = new LifeGameService();
        int outOfRangeCol = service.getBoard().getCols();

        assertThrows(
                IllegalArgumentException.class,
                () -> service.editCells(
                        CellEditMode.DRAW,
                        List.of(new CellPosition(0, outOfRangeCol))));
    }

    /**
     * パターン配置で、基準行が負数の場合は例外になることを確認します。
     */
    @Test
    void placePattern_行番号が負数なら例外になる() {
        LifeGameService service = new LifeGameService();

        assertThrows(
                IllegalArgumentException.class,
                () -> service.placePattern(PatternType.BLINKER, -1, 0));
    }

    /**
     * パターン配置で、基準列が負数の場合は例外になることを確認します。
     */
    @Test
    void placePattern_列番号が負数なら例外になる() {
        LifeGameService service = new LifeGameService();

        assertThrows(
                IllegalArgumentException.class,
                () -> service.placePattern(PatternType.BLINKER, 0, -1));
    }

    /**
     * パターン配置で、基準行が盤面の行数以上の場合は例外になることを確認します。
     */
    @Test
    void placePattern_行番号が盤面外なら例外になる() {
        LifeGameService service = new LifeGameService();
        int outOfRangeRow = service.getBoard().getRows();

        assertThrows(
                IllegalArgumentException.class,
                () -> service.placePattern(PatternType.BLINKER, outOfRangeRow, 0));
    }

    /**
     * パターン配置で、基準列が盤面の列数以上の場合は例外になることを確認します。
     */
    @Test
    void placePattern_列番号が盤面外なら例外になる() {
        LifeGameService service = new LifeGameService();
        int outOfRangeCol = service.getBoard().getCols();

        assertThrows(
                IllegalArgumentException.class,
                () -> service.placePattern(PatternType.BLINKER, 0, outOfRangeCol));
    }
}