package com.mkunori.lifegame.controller;

import com.mkunori.lifegame.model.LifeGameBoard;
import com.mkunori.lifegame.model.PatternType;
import com.mkunori.lifegame.service.LifeGameService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

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
     * Modelに盤面データとパターン選択肢を入れることで、
     * HTMLテンプレート側から盤面や選択肢を参照できるようにします。
     *
     * @param model HTMLテンプレートへ渡すデータを入れるための入れ物
     * @return 表示するテンプレート名
     */
    @GetMapping("/lifegame")
    public String showLifeGame(Model model) {
        model.addAttribute("board", lifeGameService.getBoard());
        model.addAttribute("patternTypes", PatternType.values());
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
     * 盤面を初期状態に戻します。
     *
     * ResetボタンからPOSTリクエストを受け取り、Serviceに盤面の初期化を依頼します。
     * 更新後はライフゲーム画面へリダイレクトします。
     *
     * @return リダイレクト先
     */
    @PostMapping("/lifegame/reset")
    public String reset() {
        lifeGameService.reset();
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

    /**
     * 選択されたパターンを盤面中央に配置します。
     *
     * Pattern選択フォームからPOSTリクエストを受け取り、Serviceにパターン配置を依頼します。
     * 更新後はライフゲーム画面へリダイレクトします。
     *
     * @param patternType 配置するパターンの種類
     * @return リダイレクト先
     */
    @PostMapping("/lifegame/pattern")
    public String placePattern(@RequestParam PatternType patternType) {
        lifeGameService.placePattern(patternType);
        return "redirect:/lifegame";
    }

    /**
     * 盤面を1世代進めて、更新後の盤面データを返します。
     *
     * JavaScriptの自動再生処理から呼び出されます。
     * 通常の画面遷移ではなく、JSON形式のデータとして盤面を返します。
     *
     * @return 更新後のライフゲーム盤面
     */
    @PostMapping("/lifegame/api/step")
    @ResponseBody
    public LifeGameBoard stepApi() {
        lifeGameService.nextGeneration();
        return lifeGameService.getBoard();
    }
}