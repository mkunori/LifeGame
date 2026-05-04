package com.mkunori.lifegame.controller;

import com.mkunori.lifegame.service.LifeGameService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * ライフゲーム画面を表示するためのコントローラーです。
 *
 * ブラウザからのリクエストを受け取り、表示に必要なデータをHTMLテンプレートへ渡します。
 */
@Controller
public class LifeGameController {

    private final LifeGameService lifeGameService;

    /**
     * ライフゲーム用のコントローラーを作成します。
     *
     * SpringがLifeGameServiceを自動で渡してくれます。
     *
     * @param lifeGameService ライフゲームの処理を担当するサービス
     */
    public LifeGameController(LifeGameService lifeGameService) {
        this.lifeGameService = lifeGameService;
    }

    /**
     * ライフゲーム画面を表示します。
     *
     * Modelに盤面データを入れることで、HTMLテンプレート側から盤面を参照できるようにします。
     *
     * @param model HTMLテンプレートへ渡すデータを入れるための入れ物
     * @return 表示するテンプレート名
     */
    @GetMapping("/lifegame")
    public String showLifeGame(Model model) {
        model.addAttribute("board", lifeGameService.getBoard());
        return "lifegame";
    }
}