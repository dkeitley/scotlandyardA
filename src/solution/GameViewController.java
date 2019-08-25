package solution;

import scotlandyard.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.util.List;
import java.util.concurrent.*;
import javax.swing.*;

public class GameViewController implements Spectator{

	private ScotlandYardModel model;
	private GameView view;
	private BlockingQueue<String> queue;
	private Set<Move> validMoves;
	private ItemListener firstListener;
	private ItemListener secondListener;
	
	//subscribes players to this
	public GameViewController(ScotlandYardModel model, GameView view) {
		this.model = model;
		model.spectate(this);
		
		for(Player player: model.getPlayerList())
		{
			PlayerImplementation playerImp = (PlayerImplementation) player;
			playerImp.setPresenter(this);
		}
		
		this.view = view;
		queue = new LinkedBlockingQueue<String>();
	}

	//intialises and creates view
	public void run() {
		LeftSideView lsv = new LeftSideView(model.getPlayers());
		initialiseLSV(lsv);
		RightSideView rsv = new RightSideView();
		initialiseRSV(rsv);
		MapView map = new MapView();
		MrXMovesBar movesBar = new MrXMovesBar(model.getRounds().size()-1);
		view.createView(lsv,map,rsv,movesBar);
		updateMovesBar();
		addListeners();
		Thread t = new Thread(new Runnable() {           
       		public void run(){ 
            	model.start();
				win();
        	}
        });	
       	t.start();
	}

	//adds actionListeners to widgets in view
	private void addListeners() {
		firstListener = new FirstMoveLocationListener();
		secondListener = new SecondMoveLocationListener();
		view.rsv.addGoButtonListener(view.new PlayListener(queue));
		view.rsv.addPassButtonListener(view.new PassListener(queue));
		view.rsv.addFirstMoveLocationsListener(firstListener);
		view.rsv.addSecondMoveLocationsListener(secondListener);
		view.rsv.addClearButtonListener(new ClearButtonListener());
		view.rsv.addDoubleMoveButtonListener(new DoubleMoveButtonListener());
		view.rsv.addSaveButtonListener(new SaveButtonListener());
	}

	//displays winning display box at the end of the game and exits
	private void win()
	{
		if(model.getWinningPlayers().contains(Colour.Black))
		{
			view.displayMessage("Congratulations MrX you win. Click OK to exit");
		}
		else
		{
			view.displayMessage("Congratulations Detectives you win. Click OK to exit");
		}
		System.exit(0);
		return;	
	}

	//Populates initial player info in LSV
	private void initialiseLSV(LeftSideView lsv) {
		
		for(Colour colour:model.getPlayers()) {
			
			lsv.setNumTickets(colour,Ticket.Taxi,model.getPlayerTickets(colour,Ticket.Taxi));
			lsv.setNumTickets(colour,Ticket.Bus,model.getPlayerTickets(colour,Ticket.Bus));
			lsv.setNumTickets(colour,Ticket.Underground,
			model.getPlayerTickets(colour,Ticket.Underground));
			
			lsv.setLocation(colour,model.getPlayerLocation(colour)); 
			
			if(colour.equals(Colour.Black)) {
				lsv.setNumTickets(colour,Ticket.SecretMove,
				model.getPlayerTickets(colour,Ticket.SecretMove));
				
				lsv.setNumTickets(colour,Ticket.DoubleMove,
				model.getPlayerTickets(colour,Ticket.DoubleMove));
			}	
		}
	}

	//asks RSV to display initial round number and the Mr X show rounds 
	private void initialiseRSV(RightSideView rsv) {
		validMoves = new HashSet<Move>();
		rsv.hideSecondMove();
		rsv.setRoundNum(1);
		rsv.passButton.setVisible(false);
		List<Boolean> showRounds = model.getRounds();
		String text = "Show Rounds: ";
		int i=0;
		for(Boolean bool:showRounds) {
			if(bool == true) {
				text+= " " + i;
			}
			i++;
		}
		rsv.setShowRounds(text);
	}

	//creates a set of MoveTickets from a set of Moves
	private Set<MoveTicket> createMoveTickets(Set<Move> moves) {
		Set<MoveTicket> moveTickets = new HashSet<MoveTicket>();
		for(Move move:moves) {
			if(move instanceof MoveTicket) {
				MoveTicket ticket = (MoveTicket) move;
				moveTickets.add(ticket);
			}
		}
		return moveTickets;
	}

	//creates a set of MoveDoubles from a set of Moves
	private Set<MoveDouble> createDoubleMoves(Set<Move> moves) {
		Set<MoveDouble> doubleMoves = new HashSet<MoveDouble>();
		for(Move move:moves) {
			if(move instanceof MoveDouble) {
				MoveDouble doubleMove = (MoveDouble) move;
				doubleMoves.add(doubleMove);
			}
		}
		return doubleMoves;
	}
	
	public void notify(Move move)
	{
		update(move);
		return;
	}
	
	//updates view based on move that has hust been made
	private void update(Move move)
	{
		updateLSV();
		updateRSV(move);
		return;
	}

	//refreshes each players tickets and location in LSV 
	private void updateLSV()
	{
		List<Colour> colours = model.getPlayers();
		String[] detectiveTickets = {"Taxi", "Bus", "Underground"};
		String[] mrXTickets = {"Taxi", "Bus", "Underground", "SecretMove", "DoubleMove"};
		String[] tickets;
		for(Colour colour : colours)
		{
			if(colour.equals(Colour.Black)) tickets = mrXTickets;
			else tickets = detectiveTickets;
			for(String ticket : tickets)
			{
				int num = model.getPlayerTickets(colour, Ticket.valueOf(ticket));
				view.lsv.setNumTickets(colour, Ticket.valueOf(ticket), num);
			}
			view.lsv.setLocation(colour, model.getPlayerLocation(colour));
		}
		return;
	}

	//asks view to display ticket used by Mr X and update round num
	private void updateRSV(Move move)
	{
		if(move instanceof MoveDouble) {
			view.rsv.setRoundNum(model.getRound()+2);
		}
		List<Colour> players = model.getPlayers();
		if(model.getCurrentPlayer().equals(players.get(players.size() -1))) { 
			view.rsv.setRoundNum(model.getRound()+1);
		}
		updateMovesBar();
		return;
	}
	
	private void updateMovesBar() {
		for(int j = 1;j<model.getRounds().size();j++) {
			view.movesBar.addMrXMove(j + ") ",j);
		}
		
		for(int i = 1; i <= model.getRound() ; i++) 
		{
			MoveTicket moveTicket = model.mrXMoves(i);
			String text =i + ") " + moveTicket.ticket.toString();
			if(model.getRounds().get(i)) {
				text += " " + moveTicket.target;
			}
			view.movesBar.addMrXMove(text,i);
		}
	}

	//@return getFromQueue()   move made by player
	public Move getPlayerMove(List<Move> moves) 
	{
		updateComboBoxes(moves);
		return getFromQueue();
	}

	//resets move controls for next turn
	private void updateComboBoxes(List<Move> moves)
	{
		validMoves = new HashSet<Move>(moves);
		view.rsv.displayMoveControls();
		view.rsv.hideSecondMove();
		view.rsv.passButton.setVisible(false);
		if(model.getCurrentPlayer().equals(Colour.Black))
		{
			view.rsv.doubleMoveButton.setVisible(true);
			if(model.getPlayerTickets(Colour.Black,Ticket.DoubleMove) == 0) {
				view.rsv.doubleMoveButton.setVisible(false);
			}
		
			view.lsv.setLocation(Colour.Black, model.getMrXLocation());
		}
		else view.rsv.doubleMoveButton.setVisible(false);
		view.rsv.firstMoveLocationsBox.removeItemListener(firstListener);
		view.rsv.firstMoveLocationsBox.removeAllItems();
		view.rsv.addFirstMoveLocationsListener(firstListener);
		view.rsv.firstMoveTicketBox.removeAllItems();
		Set<Integer> targets = new HashSet<Integer>();
		for(MoveTicket moveTicket : createMoveTickets(validMoves)) {
			targets.add(moveTicket.target);
		}
		if(targets.size() == 0) {
				view.rsv.hideMoveControls();
				view.rsv.passButton.setVisible(true);
		}
		view.rsv.setFirstMoveLocations(targets);
		view.rsv.currentPlayer.setText("Current Player: " + model.getCurrentPlayer());
		return;
	}

	//pulls move just made from queue
	private Move getFromQueue()
	{
		try
		{
			String isDoubleMove = queue.take();
			int target = Integer.parseInt(queue.take());
			if(target == -1) {
				queue.take();
				return new MovePass(model.getCurrentPlayer());
			}
			MoveTicket move1 = new MoveTicket(model.getCurrentPlayer(), target, Ticket.valueOf(queue.take())); 
			if(isDoubleMove == "true")
			{
				MoveTicket move2 = new MoveTicket(model.getCurrentPlayer(), Integer.parseInt(queue.take()), Ticket.valueOf(queue.take()));
				return findDoubleMove(move1,move2);
			}
			else
			{
				return findMoveTicket(move1);
			}
		}
		catch (Exception e)
		{
			throw new Error(e);
		}
	}

	//finds MoveTicket in validMoves (due to no hash equality)
	public Move findMoveTicket(MoveTicket move) {
		for(Move validMove: validMoves) {
			if(validMove instanceof MoveTicket) {
				MoveTicket moveTicket = (MoveTicket) validMove;
				if(moveTicket.target == move.target && moveTicket.ticket == move.ticket) {
					return validMove;
				}
			}
		}
		return null;
	}

	//finds Double Move in validMoves (due to no hash equality)
	public Move findDoubleMove(MoveTicket a, MoveTicket b) {
		for(Move doubleMove: validMoves) {
			if(doubleMove instanceof MoveDouble) {
				MoveDouble moveDouble = (MoveDouble) doubleMove;
				MoveTicket firstMove = (MoveTicket) moveDouble.moves.get(0);
				MoveTicket secondMove = (MoveTicket) moveDouble.moves.get(1);
				if(firstMove.target == a.target
				   && firstMove.ticket.equals(a.ticket)
				   && secondMove.target == b.target
				   && secondMove.ticket.equals(b.ticket)) {
					return doubleMove;
				   }
			}
		}
		return null;
	}

	class FirstMoveLocationListener implements ItemListener {

		//populates ticket box with those required for selected location 
		public void itemStateChanged(ItemEvent event) {
		
			Set<MoveTicket> validMoveTickets = createMoveTickets(validMoves);
			int chosenLocation = view.rsv.getFirstLocation();
			Set<Ticket> possibleTickets = new HashSet<Ticket>();

			for(MoveTicket moveTicket : validMoveTickets) {
				if(moveTicket.target == chosenLocation) {
					possibleTickets.add(moveTicket.ticket);
				}
			}
			view.rsv.firstMoveTicketBox.removeAllItems();
			view.rsv.setFirstMoveTickets(possibleTickets);
		}
	}
	
	class SecondMoveLocationListener implements ItemListener {

		//populates ticket box with those required for selected location 
		public void itemStateChanged(ItemEvent event)  {
			int firstLocation = view.rsv.getFirstLocation();
			int secondLocation = view.rsv.getSecondLocation();			
			Set<Ticket> possibleTickets = new HashSet<Ticket>();
			Set<MoveDouble> validDoubleMoves = createDoubleMoves(validMoves);
			for(MoveDouble moveDouble: validDoubleMoves) {
				MoveTicket firstTicket = (MoveTicket) moveDouble.moves.get(0);
				MoveTicket secondTicket = (MoveTicket) moveDouble.moves.get(1);
				if(secondTicket.target == secondLocation && firstTicket.target == firstLocation) {
					possibleTickets.add(secondTicket.ticket);
				}
			} 
			
			view.rsv.secondMoveTicketBox.removeAllItems();
			view.rsv.setSecondMoveTickets(possibleTickets);
			
		}
	}

	class ClearButtonListener implements ActionListener {
	
		//resets chosen move in combobox to default move
		public void actionPerformed(ActionEvent event) {
			
			view.rsv.secondMoveLocationsBox.removeItemListener(secondListener);
			view.rsv.secondMoveLocationsBox.removeAllItems();
			view.rsv.secondMoveTicketBox.removeAllItems();
			view.rsv.firstMoveTicketBox.removeAllItems();
			view.rsv.firstMoveLocationsBox.removeItemListener(firstListener);
			view.rsv.firstMoveLocationsBox.removeAllItems();
			view.rsv.addFirstMoveLocationsListener(firstListener);
			view.rsv.addSecondMoveLocationsListener(secondListener);
			view.rsv.hideSecondMove();
			updateComboBoxes(new ArrayList<Move>(validMoves));
		}
	}

	class DoubleMoveButtonListener implements ActionListener {
	
		//displays input fields required for making double move
		public void actionPerformed(ActionEvent event) {
		
			int location = view.rsv.getFirstLocation();
			Set<Integer> possibleLocations = new HashSet<Integer>();
			Set<MoveDouble> validDoubleMoves = createDoubleMoves(validMoves);
			for(MoveDouble moveDouble: validDoubleMoves) {
				MoveTicket firstMove = (MoveTicket) moveDouble.moves.get(0);
				if(firstMove.target == location) {
					MoveTicket secondMove = (MoveTicket) moveDouble.moves.get(1);
					possibleLocations.add(secondMove.target);
				}
			}
			view.rsv.setSecondMoveLocations(possibleLocations);
			view.rsv.displaySecondMove();
		}
	}

	class SaveButtonListener implements ActionListener {
		
		//saves game
		public void actionPerformed(ActionEvent event) {
			ModelSaver saver = new ModelSaver(model);
			try {
				saver.save("gameSave.txt");
				view.displayMessage("Game Saved.");
			} catch (Exception e) {
				view.displayMessage("Oops something went wrong!");
			}
		}
	}
	
}

