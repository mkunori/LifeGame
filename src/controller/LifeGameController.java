package controller;

import javax.swing.Timer;

import model.LifeGameModel;
import model.PatternType;
import view.LifeGameView;

/**
 * LifeGame1Go の操作を制御するコントローラクラス。
 * モデルの更新と画面の再描画を仲介し、タイマーによる世代更新も管理する。
 */
public class LifeGameController {

    /** スライダー初期値 */
    private static final int DEFAULT_DELAY = 200;

    /** 盤面クリック時の動作モード */
    private ClickMode clickMode = ClickMode.TOGGLE;

    /** 盤面状態とゲームルールを管理するモデル */
    private LifeGameModel model;

    /** 画面全体を表すビュー */
    private LifeGameView view;

    /** 世代更新の周期ms */
    private Timer timer;

    /**
     * コントローラを生成する。
     * 
     * @param model ライフゲームの状態を管理するモデル
     * @param view  画面表示を担当するビュー
     */
    public LifeGameController(LifeGameModel model, LifeGameView view) {

        this.model = model;
        this.view = view;

        timer = new Timer(DEFAULT_DELAY, e -> step());

        view.updateGenerationLabel(model.getGeneration());
        view.updateSpeedLabel(timer.getDelay());
        view.updateStatusLabel("Stopped");
        view.updateRunningState(false);
    }

    /**
     * 世代更新を開始する。
     */
    public void start() {
        timer.start();
        view.updateStatusLabel("Running");
        view.updateRunningState(true);
    }

    /**
     * 世代更新を停止する。
     */
    public void stop() {
        timer.stop();
        view.updateStatusLabel("Stopped");
        view.updateRunningState(false);
    }

    /**
     * 盤面がクリックされたときの処理を行う。
     * 
     * @param row クリックされた行
     * @param col クリックされた列
     */
    public void handleBoardClick(int row, int col) {

        switch (clickMode) {
            case TOGGLE -> model.toggleCell(row, col);
            case GLIDER -> model.placePattern(PatternType.GLIDER, row, col);
            case BLOCK -> model.placePattern(PatternType.BLOCK, row, col);
            case BLINKER -> model.placePattern(PatternType.BLINKER, row, col);
            case GOSPER_GLIDER_GUN -> model.placePattern(PatternType.GOSPER_GLIDER_GUN, row, col);
        }

        view.repaintBoard();
    }

    /**
     * 盤面がドラッグされたとき処理を行う。
     * Toggleモードのときは通過したセルを生存状態にする。
     * 
     * @param row ドラッグされた行
     * @param col ドラッグされた列
     */
    public void handleBoardDrag(int row, int col) {

        if (clickMode == ClickMode.TOGGLE) {
            model.setCellAlive(row, col);
            view.repaintBoard();
        }
    }

    /**
     * 盤面をランダムな状態で初期化する。
     * 世代数も 0 に戻し、停止状態にする。
     */
    public void random() {

        timer.stop();
        model.randomize();
        model.resetGeneration();

        view.updateStatusLabel("Stopped");
        view.updateRunningState(false);
        view.updateGenerationLabel(model.getGeneration());
        view.repaintBoard();
    }

    /**
     * 盤面をクリアする。
     * 世代数も 0 に戻し、停止状態にする。
     */
    public void clear() {

        timer.stop();
        model.clear();
        model.resetGeneration();

        view.updateStatusLabel("Stopped");
        view.updateRunningState(false);
        view.updateGenerationLabel(model.getGeneration());
        view.repaintBoard();
    }

    /**
     * ゲームを 1 世代進める。
     * 変化がない場合、または生存セルがなくなった場合は自動停止する。
     */
    private void step() {

        boolean changed = model.nextGeneration();
        model.incrementGeneration();

        view.updateGenerationLabel(model.getGeneration());

        if (!changed || !model.hasAliveCells()) {
            timer.stop();
            view.updateStatusLabel("Stopped");
            view.updateRunningState(false);
        }

        view.repaintBoard();
    }

    /**
     * 世代更新の間隔を変更する。
     * 
     * @param delay ミリ秒単位の更新間隔
     */
    public void setSpeed(int delay) {

        timer.setDelay(delay);

        view.updateSpeedLabel(delay);
    }

    /**
     * 現在のクリックモードを返す。
     * 
     * @return 現在のクリックモード
     */
    public ClickMode getClickMode() {
        return clickMode;
    }

    /**
     * セル反転モードに切り替える。
     */
    public void setToggleMode() {
        clickMode = ClickMode.TOGGLE;
    }

    /**
     * Glider配置モードに切り替える。
     */
    public void setGliderMode() {
        clickMode = ClickMode.GLIDER;
    }

    /**
     * Block配置モードに切り替える。
     */
    public void setBlockMode() {
        clickMode = ClickMode.BLOCK;
    }

    /**
     * Blinker配置モードに切り替える。
     */
    public void setBlinkerMode() {
        clickMode = ClickMode.BLINKER;
    }

    /**
     * Gosper Glider Gun配置モードに切り替える。
     */
    public void setGosperGliderGunMode() {
        clickMode = ClickMode.GOSPER_GLIDER_GUN;
    }
}
