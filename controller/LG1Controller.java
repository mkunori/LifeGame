package controller;

import javax.swing.Timer;
import model.LG1Model;
import view.LG1View;

public class LG1Controller {
    private LG1Model model;
    private LG1View view;
    private Timer timer; // 世代更新の間隔

    // コンストラクタ
    public LG1Controller(LG1Model model, LG1View view) {
        this.model = model;
        this.view = view;

        timer = new Timer(200, e -> step()); // とりあえず200ms周期にしておいた
    }

    public void start() {
        timer.start();
    }

    public void stop() {
        timer.stop();
    }

    public void toggleCell(int r, int c) {
        model.toggleCell(r, c);
        view.repaintBoard();
    }

    private void step() {
        boolean changed = model.nextGeneration();
        // 変化なしか全滅なら自動停止する
        if (!changed || !model.hasAliveCells()) {
            timer.stop();
        }
        view.repaintBoard();
    }
}
