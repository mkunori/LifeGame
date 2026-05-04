package com.mkunori.lifegame.service;

import com.mkunori.lifegame.model.LifeGameBoard;
import org.springframework.stereotype.Service;

/**
 * ライフゲームの処理を担当するサービスクラスです。
 *
 * Controllerから呼び出され、盤面データを用意します。
 * 今後は、世代更新やセル切り替えなどの処理もこのクラスから呼び出します。
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
}