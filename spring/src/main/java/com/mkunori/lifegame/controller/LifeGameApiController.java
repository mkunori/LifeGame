package com.mkunori.lifegame.controller;

import com.mkunori.lifegame.controller.request.EditCellsRequest;
import com.mkunori.lifegame.controller.request.PlacePatternRequest;
import com.mkunori.lifegame.model.LifeGameBoard;
import com.mkunori.lifegame.service.LifeGameService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * ライフゲームのAPI処理を担当するコントローラーです。
 *
 * JavaScriptから呼び出されるリクエストを受け取り、
 * 盤面を更新したうえで、更新後の盤面データをJSON形式で返します。
 */
@RestController
public class LifeGameApiController {

    private final LifeGameService lifeGameService;

    /**
     * ライフゲームAPI用のコントローラーを作成します。
     *
     * SpringがLifeGameServiceを自動で渡してくれます。
     *
     * @param lifeGameService ライフゲームの処理を担当するサービス
     */
    public LifeGameApiController(LifeGameService lifeGameService) {
        this.lifeGameService = lifeGameService;
    }

    /**
     * 盤面を1世代進めて、更新後の盤面データを返します。
     *
     * JavaScriptのStepボタンや自動再生処理から呼び出されます。
     *
     * @return 更新後のライフゲーム盤面
     */
    @PostMapping("/lifegame/api/step")
    public LifeGameBoard step() {
        lifeGameService.nextGeneration();
        return lifeGameService.getBoard();
    }

    /**
     * 盤面をすべてクリアして、更新後の盤面データを返します。
     *
     * @return 更新後のライフゲーム盤面
     */
    @PostMapping("/lifegame/api/clear")
    public LifeGameBoard clear() {
        lifeGameService.clear();
        return lifeGameService.getBoard();
    }

    /**
     * 盤面を初期状態に戻して、更新後の盤面データを返します。
     *
     * @return 更新後のライフゲーム盤面
     */
    @PostMapping("/lifegame/api/reset")
    public LifeGameBoard reset() {
        lifeGameService.reset();
        return lifeGameService.getBoard();
    }

    /**
     * 盤面をランダムに配置して、更新後の盤面データを返します。
     *
     * @return 更新後のライフゲーム盤面
     */
    @PostMapping("/lifegame/api/random")
    public LifeGameBoard randomize() {
        lifeGameService.randomize();
        return lifeGameService.getBoard();
    }

    /**
     * 複数セルを編集して、更新後の盤面データを返します。
     *
     * ドラッグ描画で通過したセルをJavaScriptからまとめて受け取り、
     * 指定された編集モードに応じて、反転、描画、消去を行います。
     *
     * @param request 編集モードとセル位置の一覧を含むリクエスト
     * @return 更新後のライフゲーム盤面
     */
    @PostMapping("/lifegame/api/edit-cells")
    public LifeGameBoard editCells(@RequestBody EditCellsRequest request) {
        lifeGameService.editCells(request.mode(), request.cells());
        return lifeGameService.getBoard();
    }

    /**
     * 選択されたパターンを指定位置に配置して、更新後の盤面データを返します。
     *
     * JavaScriptから送られてきたJSONリクエストを受け取り、
     * requestに含まれるパターン種別と位置を使ってパターンを配置します。
     *
     * @param request 配置するパターンの種類と位置を含むリクエスト
     * @return 更新後のライフゲーム盤面
     */
    @PostMapping("/lifegame/api/pattern")
    public LifeGameBoard placePattern(@RequestBody PlacePatternRequest request) {
        lifeGameService.placePattern(request.patternType(), request.row(), request.col());
        return lifeGameService.getBoard();
    }
}