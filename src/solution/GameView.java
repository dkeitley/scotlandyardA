package solution;
import javax.swing.*;
import java.net.*;
import scotlandyard.*;
import java.util.List;
import java.util.Map;

public class GameView {
	public JFrame window;
	public Box mainBox;
	public RightSideView rsv;
	public MrXMovesBar movesBar;
	public LeftSideView lsv;
	
	public GameView() {
		window = new JFrame();	
		window.setDefaultCloseOperation(window.EXIT_ON_CLOSE);
	}

	public void display(Box box) {
		window.add(box);
		window.pack();
		window.setLocationByPlatform(true);
		window.setVisible(true);
	}
	
	public void createView(LeftSideView lsv, MapView map, RightSideView rsv, MrXMovesBar movesBar) {
		mainBox = Box.createHorizontalBox();
		this.rsv = rsv;
		this.map = map;
		this.movesBar = movesBar;
		mainBox.add(lsv);
		mainBox.add(map);
		mainBox.add(rsv);
		mainBox.add(movesBar);
		display(mainBox);
	}

}
