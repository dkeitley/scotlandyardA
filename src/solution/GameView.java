import javax.swing.*;
import java.net.*;

public class GameView {
	private MapView map;
	private RightSideView rsv;
	
	public static void main(String[] args) {
		GameView gameView = new GameView();
		SwingUtilities.invokeLater(gameView::run);
	}

	public void run() {
		JFrame window = new JFrame();
		window.setDefaultCloseOperation(window.EXIT_ON_CLOSE);
		window.add(display());
		window.pack();
		window.setLocationByPlatform(true);
		window.setVisible(true);

	}
	Box display() {
		Box box = Box.createHorizontalBox();
		map = new MapView();
		rsv = new RightSideView();
		box.add(map);
		box.add(rsv);
		return box;
	}

}
