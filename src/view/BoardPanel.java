package view;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JPanel;

import controller.LifeGameController;
import model.LifeGameModel;

/**
 * ライフゲームの盤面を表示するパネル。
 * セルの描画とマウスクリックの受付を担当する。
 */
public class BoardPanel extends JPanel {

    /** ライフゲームのモデル層 */
    private LifeGameModel model;

    /** ライフゲームのコントローラ層 */
    private LifeGameController controller;

    /** 盤面のグリッド1マスあたりのサイズpx */
    private static final int CELL_SIZE = 20;

    /**
     * 盤面表示用パネルを生成する。
     * 
     * @param model 描画対象のモデル
     */
    public BoardPanel(LifeGameModel model) {
        this.model = model;

        int boardWidth = model.getCols() * CELL_SIZE;
        int boardHeight = model.getRows() * CELL_SIZE;

        setPreferredSize(new Dimension(boardWidth, boardHeight));

        addMouseListener(new MouseAdapter() {

            @Override
            public void mousePressed(MouseEvent e) {

                int col = e.getX() / CELL_SIZE;
                int row = e.getY() / CELL_SIZE;

                // クリックされたセルをControllerに伝える。
                controller.toggleCell(row, col);
            }
        });
    }

    /**
     * コントローラを設定する。
     * 
     * @param controller コントローラ
     */
    public void setController(LifeGameController controller) {
        this.controller = controller;
    }

    /**
     * 盤面を描画する。
     * 
     * @param g 描画に使用するGraphicsオブジェクト
     */
    @Override
    protected void paintComponent(Graphics g) {

        // 前回の描画内容が残らないようにクリアする。
        super.paintComponent(g);

        // 描画時のみモデルを参照してセル状態を表示する。
        for (int r = 0; r < model.getRows(); r++) {
            for (int c = 0; c < model.getCols(); c++) {

                int x = c * CELL_SIZE;
                int y = r * CELL_SIZE;

                if (model.getCell(r, c)) {
                    g.fillRect(x, y, CELL_SIZE, CELL_SIZE);
                } else {
                    g.drawRect(x, y, CELL_SIZE, CELL_SIZE);
                }
            }
        }
    }
}
