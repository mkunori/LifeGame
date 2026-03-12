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

public class LG1View extends JPanel {
    private int cellSize = 20; // グリッド1マスあたりのサイズpx
    private JPanel boardPanel;
    private LG1Controller controller;
    private LG1Model model;

    // コンストラクタ
    public LG1View(LG1Model model) {
        this.model = model;

        setLayout(new BorderLayout());
        boardPanel = new JPanel() {
            // 画面描画
            protected void paintComponent(Graphics g) {
                super.paintComponent(g); // 描画前に背景クリアしておかないと前の描画が残ることがある


                // MVCに反してViewからModelを直接参照しているが、例外的に許容する
                // 更新はしていない、かつ描画のたびにControllerを経由するのは重たいため
                for (int r = 0; r < model.getRows(); r++) {
                    for (int c = 0; c < model.getCols(); c++) {
                        int x = c * cellSize;
                        int y = r * cellSize;

                        if (model.getCell(r, c)) {
                            g.fillRect(x, y, cellSize, cellSize); // 塗りつぶす(生)
                        } else {
                            g.drawRect(x, y, cellSize, cellSize); // 塗りつぶさない(死)
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
        startButton.addActionListener(e -> controller.start());

        JButton stopButton = new JButton("Stop");
        stopButton.addActionListener(e -> controller.stop());

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(startButton);
        buttonPanel.add(stopButton);
        add(buttonPanel, BorderLayout.SOUTH);
    }

    // ViewへControllerを教える
    public void setController(LG1Controller controller) {
        this.controller = controller;
    }

    // 再描画する
    public void repaintBoard() {
        boardPanel.repaint();
    }
}
