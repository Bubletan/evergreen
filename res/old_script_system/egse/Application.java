package egse;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;

import javax.swing.JFrame;
import javax.swing.JSplitPane;
import javax.swing.UIManager;

public final class Application extends JFrame {

	private static final long serialVersionUID = -4684746944931911383L;

	private Application() {
		setTitle("EvergreenScript Editor");
		Browser browser = new Browser(new File(System.getProperty("user.home") + "/workspace/Evergreen RS2 Framework/"));
		Editor editor = new Editor();
		JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT,
                browser, editor);
		add(splitPane);
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent event) {
				System.gc();
				System.exit(0);
			}
		});
		pack();
		setLocationRelativeTo(null);
		setVisible(true);
	}

	public static void main(String[] args) {
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Exception e) {
		}
		new Application();
	}
}
