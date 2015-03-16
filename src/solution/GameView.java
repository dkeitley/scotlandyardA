package solution;
import javax.swing.*;
import java.net.*;
import scotlandyard.*;
import java.util.List;
import java.util.Map;

public class GameView {
	public MapView map;
	public RightSideView rsv;
	public MrXMovesBar movesBar;
	public LeftSideView lsv;
	//how is the view going to get infomation such as the players,
	// number of rounds etc. 
	private int numRounds;
	private List<Colour> colours;
	private Map<Colour,Integer> numTicketsMap;
	
	public GameView(int rounds, List<Colour> colours, Map<Colour,Integer> numTickets) {
		numRounds = rounds;
		this.colours = colours;
		numTicketsMap = numTickets;
		
	}
	public void run() {
		GameView gameView = new GameView(numRounds, colours, numTicketsMap);
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
		lsv = new LeftSideView(colours,numTicketsMap);
		box.add(lsv);
		box.add(map);
		box.add(rsv);
		box.add(movesBar);
		return box;
	}

}
