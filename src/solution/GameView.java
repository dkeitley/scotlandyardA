package solution;
import javax.swing.*;
import java.net.*;
import scotlandyard.*;

public class GameView {
	public MapView map;
	public RightSideView rsv;
	public MrXMovesBar movesBar;
	private int numRounds;
	
	public GameView(int rounds) {
		numRounds = rounds;
	}
	public void run() {
		GameView gameView = new GameView(numRounds);
		SwingUtilities.invokeLater(gameView::display);
	}

	public void display() {
		JFrame window = new JFrame();
		window.setDefaultCloseOperation(window.EXIT_ON_CLOSE);
		window.add(createBox());
		window.pack();
		window.setLocationByPlatform(true);
		window.setVisible(true);
	}
	Box createBox() {
		Box box = Box.createHorizontalBox();
		map = new MapView();
		rsv = new RightSideView();
		movesBar = new MrXMovesBar(numRounds);
		box.add(map);
		box.add(rsv);
		box.add(movesBar);
		return box;
	}

}
