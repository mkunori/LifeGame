package view;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JButton;
import javax.swing.JPanel;

import model.LG1Model;
import controller.LG1Controller;

/**
 * LifeGame1Go の画面表示を担当するビュークラス。
 * 盤面の描画とユーザー入力を処理する。
 */
public class LG1View extends JPanel {
    private int cellSize = 20; // px
    private JPanel boardPanel;
    private LG1Controller controller;
    private LG1Model model;

    /**
     * ビューを生成する。
     *
     * @param model 描画対象のモデル
     */
    public LG1View(LG1Model model) {
        this.model = model;

        setLayout(new BorderLayout());
        boardPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                // 描画前に背景クリアしておかないと前の描画が残ることがあるらしい
                super.paintComponent(g);
                // 描画時のみモデルを参照してセル状態を表示する
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
        };

        boardPanel.setPreferredSize(new Dimension(600, 600));
        boardPanel.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                int col = e.getX() / cellSize;
                int row = e.getY() / cellSize;

                controller.toggleCell(row, col);
            }
        });

        add(boardPanel, BorderLayout.CENTER);

        JButton startButton = new JButton("Start");
        JButton stopButton = new JButton("Stop");
        JButton randomButton = new JButton("Random");
        JButton clearButton = new JButton("Clear");
        JButton gliderButton = new JButton("Glider");
        clearButton.addActionListener(e -> controller.clear());
        randomButton.addActionListener(e -> controller.random());
        startButton.addActionListener(e -> controller.start());
        stopButton.addActionListener(e -> controller.stop());
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
    }

    /**
     * コントローラを設定する。
     *
     * @param controller コントローラ
     */
    public void repaintBoard() {
        boardPanel.repaint();
    }
}
