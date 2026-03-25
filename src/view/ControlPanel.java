package view;

import java.awt.BorderLayout;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;

import controller.LifeGameController;

/**
 * ライフゲームの操作部品をまとめたパネル。
 * ボタン、スライダー、状態表示を担当する。
 */
public class ControlPanel extends JPanel {

    /** 開始ボタン */
    private JButton startButton;

    /** 停止ボタン */
    private JButton stopButton;

    /** ランダムボタン */
    private JButton randomButton;

    /** 盤面クリアボタン */
    private JButton clearButton;

    /** グライダーパターンボタン */
    private JButton gliderButton;

    /** ステータス表示ラベル */
    private JLabel statusLabel;

    /** 更新間隔表示ラベル */
    private JLabel speedLabel;

    /** 世代数表示ラベル */
    private JLabel generationLabel;

    /** 更新間隔変更スライダー */
    private JSlider speedSlider;

    /** スライダー最小値 */
    private static final int MIN_DELAY = 50;

    /** スライダー最大値 */
    private static final int MAX_DELAY = 500;

    /** スライダー初期値 */
    private static final int DEFAULT_DELAY = 200;

    /** 主目盛り間隔 */
    private static final int MAJOR_TICK_SPACING = 100;

    /** 補助目盛間隔 */
    private static final int MINOR_TICK_SPACING = 50;


    /**
     * 操作パネルを生成する。
     */
    public ControlPanel() {

        setLayout(new BorderLayout());

        startButton = new JButton("Start");
        stopButton = new JButton("Stop");
        randomButton = new JButton("Random");
        clearButton = new JButton("Clear");
        gliderButton = new JButton("Glider");

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(startButton);
        buttonPanel.add(stopButton);
        buttonPanel.add(randomButton);
        buttonPanel.add(clearButton);
        buttonPanel.add(gliderButton);

        statusLabel = new JLabel("Status: Stopped");
        speedLabel = new JLabel("Speed: 200 ms");
        generationLabel = new JLabel("Generation: 0");

        speedSlider = new JSlider(MIN_DELAY, MAX_DELAY, DEFAULT_DELAY);
        speedSlider.setMajorTickSpacing(MAJOR_TICK_SPACING);
        speedSlider.setMinorTickSpacing(MINOR_TICK_SPACING);
        speedSlider.setPaintTicks(true);
        speedSlider.setPaintLabels(true);

        JPanel statusPanel = new JPanel();
        statusPanel.add(statusLabel);
        statusPanel.add(speedLabel);
        statusPanel.add(speedSlider);
        statusPanel.add(generationLabel);

        add(buttonPanel, BorderLayout.NORTH);
        add(statusPanel, BorderLayout.SOUTH);

        updateRunningState(false);
    }

    /**
     * コントローラを設定する。
     * 
     * @param controller コントローラ
     */
    public void setController(LifeGameController controller) {

        startButton.addActionListener(e -> controller.start());
        stopButton.addActionListener(e -> controller.stop());
        randomButton.addActionListener(e -> controller.random());
        clearButton.addActionListener(e -> controller.clear());
        gliderButton.addActionListener(e -> controller.glider());

        speedSlider.addChangeListener(e -> {
            int delay = speedSlider.getValue();
            controller.setSpeed(delay);
        });
    }

    /**
     * 状態表示ラベルを更新する。
     * 
     * @param status 表示する状態
     */
    public void updateStatusLabel(String status) {
        statusLabel.setText("Status: " + status);
    }

    /**
     * 速度表示ラベルを更新する。
     * 
     * @param delay 表示する更新間隔
     */
    public void updateSpeedLabel(int delay) {
        speedLabel.setText("Speed: " + delay + " ms");
    }

    /**
     * 世代数表示ラベルを更新する。
     * 
     * @param generation 表示する世代数
     */
    public void updateGenerationLabel(int generation) {
        generationLabel.setText("Generation: " + generation);
    }

    /**
     * 実行状態に応じて、Start ボタンと Stop ボタンの有効・無効を切り替える。
     * 
     * @param running 実行中なら true、停止中なら false
     */
    public void updateRunningState(boolean running) {
        startButton.setEnabled(!running);
        stopButton.setEnabled(running);
    }
}
