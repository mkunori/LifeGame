package view;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JPanel;

import controller.LG1Controller;
import model.LG1Model;

/**
 * ライフゲームの盤面を表示するパネル。
 * セルの描画とマウスクリックの受付を担当する。
 */
public class BoardPanel extends JPanel {

    /** ライフゲームのモデル層 */
    private LG1Model model;

    /** ライフゲームのコントローラ層 */
    private LG1Controller controller;

    /** 盤面のグリッド1マスあたりのサイズpx */
    private int cellSize = 20;

    /**
     * 盤面表示用パネルを生成する。
     * 
     * @param model 描画対象のモデル
     */
    public BoardPanel(LG1Model model) {
        this.model = model;

        int boardWidth = model.getCols() * cellSize;
        int boardHeight = model.getRows() * cellSize;

        setPreferredSize(new Dimension(boardWidth, boardHeight));

        addMouseListener(new MouseAdapter() {

            @Override
            public void mousePressed(MouseEvent e) {

                int col = e.getX() / cellSize;
                int row = e.getY() / cellSize;

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
    public void setController(LG1Controller controller) {
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

                int x = c * cellSize;
                int y = r * cellSize;

                if (model.getCell(r, c)) {
                    g.fillRect(x, y, cellSize, cellSize);
                } else {
                    g.drawRect(x, y, cellSize, cellSize);
                }
            }
        }
    }
}
