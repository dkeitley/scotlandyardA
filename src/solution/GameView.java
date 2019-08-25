package solution;

import javax.swing.*;
import java.net.*;
import scotlandyard.*;
import java.util.List;
import java.util.Map;
import java.util.concurrent.*;
import java.awt.event.*;
import java.awt.Color;
import java.awt.BorderLayout;

public class GameView {
	private JFrame window;
	protected RightSideView rsv;
	protected MrXMovesBar movesBar;
	protected LeftSideView lsv;
	
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
	
	//@param JPanels which make up GameView
	//constructs box containing all view components
	public void createView(LeftSideView lsv, MapView map, RightSideView rsv, MrXMovesBar movesBar) {
		Box mainBox = Box.createHorizontalBox();
		this.rsv = rsv;
		this.lsv = lsv;
		this.movesBar = movesBar;
		Box rsvBox = Box.createVerticalBox();
		rsvBox.add(movesBar);
		rsvBox.add(rsv);
		mainBox.add(lsv);
		mainBox.add(map);
		mainBox.add(rsvBox);
		display(mainBox);
	}

	class PlayListener implements ActionListener {	
		private BlockingQueue<String> queue;
		
		public PlayListener(BlockingQueue queue) {
			this.queue = queue;
		}

		//interprets user's chosen move and puts on blocking queue
		public void actionPerformed(ActionEvent event) {
			
			String isDoubleMove ="false";
			String firstTarget = Integer.toString(rsv.getFirstLocation());
			String firstTicket = rsv.getFirstTicket().toString();
			String secondTarget = null;
			String secondTicket = null;
			
			if (rsv.secondMove.isVisible()) {
				isDoubleMove = "true";
				secondTarget = Integer.toString(rsv.getSecondLocation());
				secondTicket = rsv.getSecondTicket().toString();
			}
			
			queue.add(isDoubleMove);
			queue.add(firstTarget);
			queue.add(firstTicket);
			
			if(rsv.secondMove.isVisible()) {
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
		
		//places pass move on blocking queue
		public void actionPerformed(ActionEvent event) {
			queue.add("false");
			queue.add("-1");
			queue.add("Pass");
		}
	}
	   
   public void displayMessage(String message) 
	{
		JOptionPane.showMessageDialog(window, message);
	}
}

