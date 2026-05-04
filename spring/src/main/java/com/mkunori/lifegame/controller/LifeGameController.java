package com.mkunori.lifegame.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * ライフゲーム画面を表示するためのコントローラーです。
 *
 * ブラウザからのリクエストを受け取り、表示するHTMLテンプレート名を返します。
 */
@Controller
public class LifeGameController {

    /**
     * ライフゲーム画面を表示します。
     *
     * @return 表示するテンプレート名
     */
    @GetMapping("/lifegame")
    public String showLifeGame() {
        return "lifegame";
    }
}