package com.mkunori.lifegame.controller;

import com.mkunori.lifegame.model.ActionMode;
import com.mkunori.lifegame.model.CellEditMode;
import com.mkunori.lifegame.model.LifeGameBoard;
import com.mkunori.lifegame.model.PatternType;
import com.mkunori.lifegame.service.LifeGameService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * ライフゲームの画面表示を担当するコントローラーです。
 *
 * ブラウザから画面表示用のリクエストを受け取り、
 * Thymeleafテンプレートに必要なデータを渡します。
 */
@Controller
public class LifeGamePageController {

    private final LifeGameService lifeGameService;

    /**
     * ライフゲーム画面用のコントローラーを作成します。
     *
     * SpringがLifeGameServiceを自動で渡してくれます。
     *
     * @param lifeGameService ライフゲームの処理を担当するサービス
     */
    public LifeGamePageController(LifeGameService lifeGameService) {
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
        model.addAttribute("cellEditModes", CellEditMode.values());
        model.addAttribute("actionModes", ActionMode.values());

        model.addAttribute("minRows", LifeGameBoard.MIN_ROWS);
        model.addAttribute("maxRows", LifeGameBoard.MAX_ROWS);
        model.addAttribute("minCols", LifeGameBoard.MIN_COLS);
        model.addAttribute("maxCols", LifeGameBoard.MAX_COLS);

        return "lifegame";
    }
}