package com.mkunori.lifegame.controller;

import com.mkunori.lifegame.service.LifeGameService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * ライフゲーム画面を表示するためのコントローラーです。
 *
 * ブラウザからのリクエストを受け取り、表示に必要なデータをHTMLテンプレートへ渡します。
 * また、Stepボタンなどの操作リクエストを受け取り、Serviceへ処理を依頼します。
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

    /**
     * 盤面を1世代進めます。
     *
     * StepボタンからPOSTリクエストを受け取り、Serviceに盤面更新を依頼します。
     * 更新後はライフゲーム画面へリダイレクトします。
     *
     * @return リダイレクト先
     */
    @PostMapping("/lifegame/step")
    public String step() {
        lifeGameService.nextGeneration();
        return "redirect:/lifegame";
    }

    /**
     * 盤面をすべてクリアします。
     *
     * ClearボタンからPOSTリクエストを受け取り、Serviceに盤面クリアを依頼します。
     * 更新後はライフゲーム画面へリダイレクトします。
     *
     * @return リダイレクト先
     */
    @PostMapping("/lifegame/clear")
    public String clear() {
        lifeGameService.clear();
        return "redirect:/lifegame";
    }

    /**
     * 盤面をランダムに配置します。
     *
     * RandomボタンからPOSTリクエストを受け取り、Serviceにランダム配置を依頼します。
     * 更新後はライフゲーム画面へリダイレクトします。
     *
     * @return リダイレクト先
     */
    @PostMapping("/lifegame/random")
    public String randomize() {
        lifeGameService.randomize();
        return "redirect:/lifegame";
    }

    /**
     * 指定されたセルの生死を切り替えます。
     *
     * セルからPOSTリクエストを受け取り、Serviceにセルの切り替えを依頼します。
     * 更新後はライフゲーム画面へリダイレクトします。
     *
     * @param row 切り替えるセルの行番号
     * @param col 切り替えるセルの列番号
     * @return リダイレクト先
     */
    @PostMapping("/lifegame/toggle")
    public String toggle(@RequestParam int row, @RequestParam int col) {
        lifeGameService.toggleCell(row, col);
        return "redirect:/lifegame";
    }
}