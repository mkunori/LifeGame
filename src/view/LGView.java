package view;

import java.awt.BorderLayout;

import javax.swing.JPanel;

import controller.LGController;
import model.LGModel;

/**
 * LifeGameの画面全体を表すビュークラス。
 * 盤面パネルと操作パネルを配置する。
 */
public class LGView extends JPanel {

    /** 盤面パネル */
    private BoardPanel boardPanel;

    /** 操作パネル */
    private ControlPanel controlPanel;

    /**
     * ビューを生成する。
     * 
     * @param model 描画対象のモデル
     */
    public LGView(LGModel model) {

        setLayout(new BorderLayout());

        boardPanel = new BoardPanel(model);
        controlPanel = new ControlPanel();

        add(boardPanel, BorderLayout.CENTER);
        add(controlPanel, BorderLayout.SOUTH);
    }

    /**
     * コントローラを設定する。
     * 
     * @param controller コントローラ
     */
    public void setController(LGController controller) {
        // サブビュークラスにコントローラを渡す。
        boardPanel.setController(controller);
        controlPanel.setController(controller);
    }

    /**
     * 盤面を再描画する。
     */
    public void repaintBoard() {
        boardPanel.repaint();
    }

    /**
     * 世代数表示ラベルを更新する。
     * 
     * @param generation 表示する世代数
     */
    public void updateGenerationLabel(int generation) {
        controlPanel.updateGenerationLabel(generation);
    }

    /**
     * 速度ラベルを更新する。
     * 
     * @param delay 表示する更新間隔
     */
    public void updateSpeedLabel(int delay) {
        controlPanel.updateSpeedLabel(delay);
    }

    /**
     * 状態表示ラベルを更新する。
     * 
     * @param status 表示する状態
     */
    public void updateStatusLabel(String status) {
        controlPanel.updateStatusLabel(status);
    }

    /**
     * 実行状態に応じて、 Start ボタンと Stop ボタンの有効・無効を切り替える。
     * 
     * @param running 実行中なら true、停止中なら false
     */
    public void updateRunningState(boolean running) {
        controlPanel.updateRunningState(running);
    }
}
