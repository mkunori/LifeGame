import javax.swing.JFrame;
import javax.swing.SwingUtilities;

import controller.LGController;
import model.LGModel;
import view.LGView;

/**
 * LifeGameアプリケーションを起動するクラス。
 */
public class LGMain {

    /** 初期行数 */
    private static final int DEFAULT_ROWS = 50;

    /** 初期列数 */
    private static final int DEFAULT_COLS = 100;

    /**
     * アプリケーションを起動する。
     *
     * @param args コマンドライン引数
     */
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            // MVCインスタンスを生成する
            LGModel model = new LGModel(DEFAULT_ROWS, DEFAULT_COLS);
            LGView view = new LGView(model);
            LGController controller = new LGController(model, view);

            view.setController(controller);

            // 起動する
            JFrame frame = new JFrame("LifeGame");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.add(view);
            frame.pack();
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
        });
    }
}
