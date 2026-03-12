import javax.swing.JFrame;
import javax.swing.SwingUtilities;

import model.LG1Model;
import view.LG1View;

public class LG1Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            LG1Model model = new LG1Model(30, 30);
            LG1View view = new LG1View(model);

            JFrame frame = new JFrame("LifeGame1Go");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.add(view);
            frame.pack();
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
        });
    }
}
