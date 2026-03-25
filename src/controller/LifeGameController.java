package controller;

import javax.swing.Timer;

import model.LifeGameModel;
import view.LifeGameView;

/**
 * LifeGame1Go の操作を制御するコントローラクラス。
 * モデルの更新と画面の再描画を仲介し、タイマーによる世代更新も管理する。
 */
public class LifeGameController {

    /** 盤面状態とゲームルールを管理するモデル */
    private LifeGameModel model;

    /** 画面全体を表すビュー */
    private LifeGameView view;

    /** 世代更新の周期ms */
    private Timer timer;

    /** スライダー初期値 */
    private static final int DEFAULT_DELAY = 200;

    /** グライダー初期位置行 */
    private static final int GLIDER_START_ROW = 5;

    /** グライダー初期位置列 */
    private static final int GLIDER_START_COL = 5;

    /**
     * コントローラを生成する。
     * 
     * @param model ライフゲームの状態を管理するモデル
     * @param view 画面表示を担当するビュー
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
     * 指定したセルの生死を反転する。
     * 
     * @param r 行番号
     * @param c 列番号
     */
    public void toggleCell(int r, int c) {

        model.toggleCell(r, c);
        view.repaintBoard();
    }

    /**
     * 盤面をランダムな状態で初期化する。
     * 世代数も 0 に戻し、停止状態にする。
     */
    public void random() {

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
        model.clear();
        model.resetGeneration();

        view.updateStatusLabel("Stopped");
        view.updateRunningState(false);
        view.updateGenerationLabel(model.getGeneration());
        view.repaintBoard();
    }

    /**
     * Glider パターンを配置する。
     */
    public void glider() {

        model.placeGlider(GLIDER_START_ROW, GLIDER_START_COL);

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
}
