package solution;
import javax.swing.*;
import java.net.*;
import scotlandyard.*;
import java.util.List;
import java.util.Map;
import java.util.concurrent.*;
import java.awt.event.*;

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
		this.lsv = lsv;
		this.movesBar = movesBar;
		mainBox.add(lsv);
		mainBox.add(map);
		mainBox.add(rsv);
		mainBox.add(movesBar);
		display(mainBox);
	}

	class PlayListener implements ActionListener {	
		private BlockingQueue<String> queue;
		
		public PlayListener(BlockingQueue queue) {
			this.queue = queue;
		}
		
		public void actionPerformed(ActionEvent event) {
			
			String isDoubleMove ="false";
			String firstTarget = Integer.toString(rsv.getFirstLocation());
			String firstTicket = rsv.getFirstTicket().toString();
			String secondTarget = null;
			String secondTicket = null;
			
			if (rsv.secondMoveLocationsBox.isVisible()) {
				isDoubleMove = "true";
				secondTarget = Integer.toString(rsv.getSecondLocation());
				secondTicket = rsv.getSecondTicket().toString();
			}
			
			queue.add(isDoubleMove);
			queue.add(firstTarget);
			queue.add(firstTicket);
			
			if(rsv.secondMoveLocationsBox.isVisible()) {
				queue.add(secondTarget);
				queue.add(secondTicket);
			}	
	
		}
   }
   
   class PassListener implements ActionListener {	
  	 private BlockingQueue<String> queue;
		
   	 public PassListener(BlockingQueue queue) {
			this.queue = queue;
	 }
   	 public void actionPerformed(ActionEvent event) {
   			queue.add("false");
   			queue.add("-1");
   			queue.add("Pass");
   	}
   }

}
