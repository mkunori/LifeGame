package view;

import java.awt.BorderLayout;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;

import controller.LG1Controller;
import model.LG1Model;

/**
 * LifeGame1Go の画面全体を表すビュークラス。
 * 盤面パネルと操作ボタンを配置する。
 */
public class LG1View extends JPanel {

    /** コントローラ層 */
    private LG1Controller controller;

    /** 盤面パネル */
    private BoardPanel boardPanel;

    /** 世代数ラベル */
    private JLabel generationLabel;

    /** 速度ラベル */
    private JLabel speedLabel;

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

        speedLabel = new JLabel("Speed: 200 ms");

        JSlider speedSlider = new JSlider(50, 500, 200);
        speedSlider.addChangeListener(e -> {
            int delay = speedSlider.getValue();
            controller.setSpeed(delay);
        });
        speedSlider.setMajorTickSpacing(100);
        speedSlider.setMinorTickSpacing(50);
        speedSlider.setPaintTicks(true);
        speedSlider.setPaintLabels(true);

        generationLabel = new JLabel("Generation: 0");

        JPanel statusPanel = new JPanel();
        statusPanel.add(speedLabel);
        statusPanel.add(speedSlider);
        statusPanel.add(generationLabel);

        JPanel southPanel = new JPanel(new BorderLayout());
        southPanel.add(buttonPanel, BorderLayout.NORTH);
        southPanel.add(statusPanel, BorderLayout.SOUTH);

        add(southPanel, BorderLayout.SOUTH);
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

    /**
     * 世代数ラベルを更新する。
     * 
     * @param generation 表示する世代数
     */
    public void updateGenerationLabel(int generation) {
        generationLabel.setText("Generation: " + generation);
    }

    /**
     * 速度ラベルを更新する。
     * 
     * @param delay 表示する更新間隔
     */
    public void updateSpeedLabel(int delay) {
        speedLabel.setText("Speed: " + delay + " ms");
    }
}
