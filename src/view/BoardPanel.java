package view;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JPanel;

import controller.ClickMode;
import controller.LifeGameController;
import model.LifeGameModel;
import model.PatternType;

/**
 * ライフゲームの盤面を表示するパネル。
 * セルの描画とマウス入力の受付を担当する。
 */
public class BoardPanel extends JPanel {

    /** ライフゲームのモデル層 */
    private LifeGameModel model;

    /** ライフゲームのコントローラ層 */
    private LifeGameController controller;

    /** マウスの現在位置の行 (セル単位) */
    private int hoverRow = -1;

    /** マウスの現在位置の列 (セル単位) */
    private int hoverCol = -1;

    /** 盤面のグリッド1マスあたりのサイズpx */
    private static final int CELL_SIZE = 15;

    /** プレビュー表示用のカラー(置ける) */
    private static final Color PREVIEW_COLOR = new Color(0, 0, 255, 80);

    /** プレビュー表示用のカラー(置けない) */
    private static final Color PREVIEW_ERROR_COLOR = new Color(255, 0, 0, 80);

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
            public void mouseExited(MouseEvent e) {

                hoverRow = -1;
                hoverCol = -1;

                repaint();
            }

            @Override
            public void mousePressed(MouseEvent e) {

                int col = e.getX() / CELL_SIZE;
                int row = e.getY() / CELL_SIZE;

                // クリックされたセルをControllerに伝える。
                if (controller != null) {
                    controller.handleBoardClick(row, col);
                }
            }
        });

        addMouseMotionListener(new MouseAdapter() {

            @Override
            public void mouseMoved(MouseEvent e) {

                int col = e.getX() / CELL_SIZE;
                int row = e.getY() / CELL_SIZE;

                hoverRow = row;
                hoverCol = col;

                repaint();
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

        drawPreview(g);
    }

    /**
     * 現在のモードに応じたプレビューを描画する。
     * 
     * @param g 描画に使用するGraphicsオブジェクト
     */
    private void drawPreview(Graphics g) {

        if (controller == null) {
            return;
        }

        if (hoverRow < 0 || hoverCol < 0) {
            return;
        }

        ClickMode clickMode = controller.getClickMode();

        if (clickMode == ClickMode.TOGGLE) {
            drawTogglePreview(g);
            return;
        }

        PatternType patternType = clickMode.getPatternType();

        if (patternType == null) {
            return;
        }

        drawPatternPreview(g, patternType);
    }

    /**
     * Toggle モード用のプレビューを描画する。
     * 
     * @param g 描画に使用する Graphics オブジェクト
     */
    private void drawTogglePreview(Graphics g) {

        if (hoverRow < 0 || hoverCol < 0) {
            return;
        }

        Graphics2D g2 = (Graphics2D) g.create();

        if (hoverRow < model.getRows() && hoverCol < model.getCols()) {
            g2.setColor(PREVIEW_COLOR);
        } else {
            g2.setColor(PREVIEW_ERROR_COLOR);
        }

        int x = hoverCol * CELL_SIZE;
        int y = hoverRow * CELL_SIZE;

        g2.fillRect(x, y, CELL_SIZE, CELL_SIZE);
        g2.dispose();
    }

    /**
     * パターン配置用のプレビューを描画する。
     * 
     * @param g           描画に使用する Graphics オブジェクト
     * @param patternType プレビュー表示するパターン
     */
    private void drawPatternPreview(Graphics g, PatternType patternType) {

        int[][] cells = patternType.getCells();

        Graphics2D g2 = (Graphics2D) g.create();

        boolean canPlace = canPlacePattern(patternType);

        if (canPlace) {
            g2.setColor(PREVIEW_COLOR);
        } else {
            g2.setColor(PREVIEW_ERROR_COLOR);
        }

        for (int[] cell : cells) {
            int row = hoverRow + cell[0];
            int col = hoverCol + cell[1];

            if (row < 0 || row >= model.getRows() || col < 0 || col >= model.getCols()) {
                continue;
            }

            int x = col * CELL_SIZE;
            int y = row * CELL_SIZE;

            g2.fillRect(x, y, CELL_SIZE, CELL_SIZE);
        }

        g2.dispose();
    }

    /**
     * 指定した位置にパターンを配置できるか判定する。
     * 
     * @param patternType 判定するパターン
     * @return 配置可能なら true
     */
    private boolean canPlacePattern(PatternType patternType) {

        int[][] cells = patternType.getCells();

        for (int[] cell : cells) {
            int row = hoverRow + cell[0];
            int col = hoverCol + cell[1];

            if (row < 0 || row >= model.getRows() || col < 0 || col >= model.getCols()) {
                return false;
            }
        }

        return true;
    }
}
