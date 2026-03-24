package view;

import java.awt.BorderLayout;

import javax.swing.JButton;
import javax.swing.JPanel;

import controller.LG1Controller;
import model.LG1Model;

/**
 * LifeGame1Go の画面全体を表すビュークラス。
 * 盤面パネルと操作ボタンを配置する。
 */
public class LG1View extends JPanel {

    /** ライフゲームのコントローラ層 */
    private LG1Controller controller;

    /** ライフゲームの盤面パネル */
    private BoardPanel boardPanel;

    /**
     * ビューを生成する。
     *
     * @param model 描画対象のモデル
     */
    public LG1View(LG1Model model) {

        setLayout(new BorderLayout());

        boardPanel = new BoardPanel(model);
        add(boardPanel, BorderLayout.CENTER);

        JButton startButton = new JButton("Start");
        startButton.addActionListener(e -> controller.start());

        JButton stopButton = new JButton("Stop");
        stopButton.addActionListener(e -> controller.stop());

        JButton randomButton = new JButton("Random");
        randomButton.addActionListener(e -> controller.random());

        JButton clearButton = new JButton("Clear");
        clearButton.addActionListener(e -> controller.clear());

        JButton gliderButton = new JButton("Glider");
        gliderButton.addActionListener(e -> controller.glider());

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(startButton);
        buttonPanel.add(stopButton);
        buttonPanel.add(randomButton);
        buttonPanel.add(clearButton);
        buttonPanel.add(gliderButton);

        add(buttonPanel, BorderLayout.SOUTH);
    }

    /**
     * コントローラを設定する。
     *
     * @param controller コントローラ
     */
    public void setController(LG1Controller controller) {
        this.controller = controller;

        // 盤面パネルにも同じコントローラを渡す。
        boardPanel.setController(controller);
    }

    /**
     * 盤面を再描画する。
     */
    public void repaintBoard() {
        boardPanel.repaint();
    }
}
