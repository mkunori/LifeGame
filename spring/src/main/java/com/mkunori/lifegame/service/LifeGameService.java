package com.mkunori.lifegame.service;

import com.mkunori.lifegame.model.LifeGameBoard;
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
}