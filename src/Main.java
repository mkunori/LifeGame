import javax.swing.JFrame;
import javax.swing.SwingUtilities;

import controller.LifeGameController;
import model.LifeGameModel;
import view.LifeGameView;

/**
 * LifeGame1Go アプリケーションを起動するクラス。
 */
public class Main {

    /** 初期行数 */
    private static final int DEFAULT_ROWS = 30;

    /** 初期列数 */
    private static final int DEFAULT_COLS = 30;

    /**
     * アプリケーションを起動する。
     *
     * @param args コマンドライン引数
     */
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            // MVCインスタンスを生成する
            LifeGameModel model = new LifeGameModel(DEFAULT_ROWS, DEFAULT_COLS);
            LifeGameView view = new LifeGameView(model);
            LifeGameController controller = new LifeGameController(model, view);

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
