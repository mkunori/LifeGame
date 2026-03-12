package view;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.Timer;

import model.LG1Model;

public class LG1View extends JPanel {
    private int cellSize = 20;

    private Timer timer;

    private JPanel boardPanel;

    public LG1View(LG1Model model) {
        setLayout(new BorderLayout());
        boardPanel = new JPanel() {
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);

                for (int r = 0; r < 30; r++) {
                    for (int c = 0; c < 30; c++) {
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

                model.toggleCell(row, col);
                boardPanel.repaint();
            }
        });

        add(boardPanel, BorderLayout.CENTER);

        JButton startButton = new JButton("Start");
        JButton stopButton = new JButton("Stop");

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(startButton);
        buttonPanel.add(stopButton);
        add(buttonPanel, BorderLayout.SOUTH);

        timer = new Timer(200, e -> {
            model.nextGeneration();
            boardPanel.repaint();
        });

        startButton.addActionListener(e -> timer.start());
        stopButton.addActionListener(e -> timer.stop());
    }
}
