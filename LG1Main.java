import javax.swing.JFrame;
import javax.swing.SwingUtilities;

import controller.LG1Controller;
import model.LG1Model;
import view.LG1View;

public class LG1Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            // MVCインスタンスを生成する
            LG1Model model = new LG1Model(30, 30);
            LG1View view = new LG1View(model);
            LG1Controller controller = new LG1Controller(model, view);

            view.setController(controller);

            // 起動する
            JFrame frame = new JFrame("LifeGame1Go");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.add(view);
            frame.pack();
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
        });
    }
}
