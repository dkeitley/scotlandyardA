package solution;
import scotlandyard.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.util.List;
import java.util.concurrent.*;

//probably worth storing currentPlayer in global variable
//some very smelly code here
//may want to put all listeners in a single class
//may want to initialise all listeners in a single function

public class GameViewController implements Spectator{

	private ScotlandYardModel model;
	private GameView view;
	
	private BlockingQueue<String> queue;

	private Colour currentPlayer;
	//don't think i need a lot of these
	private Set<Move> validMoves;
	private Set<MoveTicket> validMoveTickets;
	private Set<MoveDouble> validDoubleMoves; 

	private Set<Move> firstMoves;
	private Set<Move> secondMoves;

	private Set<Integer> firstMoveLocations;
	private Set<Ticket> firstMoveTickets;
	private Set<Ticket> secondMoveTickets;
	private Set<Integer> secondMoveLocations;

	private ItemListener firstListener;
	private ItemListener secondListener;

	
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
		

		firstMoveLocations = new HashSet<Integer>();
		firstMoveTickets = new HashSet<Ticket>();
	 	secondMoveTickets = new HashSet<Ticket>();
		secondMoveLocations = new HashSet<Integer>();
		firstMoves = new HashSet<Move>();
		secondMoves = new HashSet<Move>();
	}

	public void run() {
		LeftSideView lsv = new LeftSideView(model.getPlayers());
		initialiseLSV(lsv);

		RightSideView rsv = new RightSideView();
		
		initialiseRSV(rsv);
		MapView map = new MapView();

		MrXMovesBar movesBar = new MrXMovesBar(model.getRounds().size());

		
		//create a single function for this?
		rsv.addGoButtonListener(view.new PlayListener(queue));

		firstListener = new FirstMoveLocationListener();
		secondListener = new SecondMoveLocationListener();
		rsv.addFirstMoveLocationsListener(firstListener);
		rsv.addSecondMoveLocationsListener(secondListener);
		rsv.addClearButtonListener(new ClearButtonListener());
		rsv.addDoubleMoveButtonListener(new DoubleMoveButtonListener());
		
		view.createView(lsv,map,rsv,movesBar);
		model.start();
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

	private void initialiseRSV(RightSideView rsv) {
		currentPlayer = model.getCurrentPlayer();
		
		validMoves = new HashSet<Move>(model.validMoves(currentPlayer));
		
		validMoveTickets = createMoveTickets(validMoves);
		validDoubleMoves = createDoubleMoves(validMoves);
		
		for(MoveTicket moveTicket:validMoveTickets) {
			firstMoveLocations.add(moveTicket.target);
			firstMoveTickets.add(moveTicket.ticket);
		}
		
		for(MoveDouble doubleMove:validDoubleMoves) {
			firstMoves.add(doubleMove.moves.get(0));
			secondMoves.add(doubleMove.moves.get(1));
		}

		Set<MoveTicket> validSecondMoveTickets = createMoveTickets(secondMoves);	
		
		for(MoveTicket ticket2: validSecondMoveTickets) {
			secondMoveLocations.add(ticket2.target);
			secondMoveTickets.add(ticket2.ticket);
		}

		rsv.setFirstMoveLocations(firstMoveLocations);
		rsv.hideSecondMove();
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
	private Set<MoveDouble> createDoubleMoves(java.util.Set<Move> moves) {
		Set<MoveDouble> doubleMoves = new HashSet<MoveDouble>();
		for(Move move:moves) {
			if(move instanceof MoveDouble) {
				MoveDouble doubleMove = (MoveDouble) move;
				doubleMoves.add(doubleMove);
			}
		}
		return doubleMoves;
	}
	
	//updates the values in the first move (locations) combo boxes
	private void updateFirstMoves(Set<Integer> targets) {
		view.rsv.setFirstMoveLocations(targets);
	}
	
	public void notify(Move move)
	{
		//upadte code
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
		view.rsv.setRoundNum(model.getRound());
		for(int i = 1; i < model.getRound() ; i++) 
		{
			MoveTicket moveTicket = model.mrXMoves(i);
			view.movesBar.addMrXMove(moveTicket.ticket,model.getRounds().get(i),i,moveTicket.target);
		}
		return;
	}

	public Move getPlayerMove(List<Move> moves) 
	{
		//update combo boxes
		validMoves = new HashSet<Move>(moves);
		view.rsv.hideSecondMove();
		if(model.getCurrentPlayer().equals(Colour.Black)) view.rsv.doubleMoveButton.setVisible(true);
		else view.rsv.doubleMoveButton.setVisible(false);
		view.rsv.firstMoveLocationsBox.removeItemListener(firstListener);
		view.rsv.firstMoveLocationsBox.removeAllItems();
		view.rsv.addFirstMoveLocationsListener(firstListener);
		view.rsv.firstMoveTicketBox.removeAllItems();
		
		Set<Integer> targets = new HashSet<Integer>();
		validDoubleMoves.clear();
		for(Move move:moves) {
			if(move instanceof MoveTicket) {
				MoveTicket moveTicket = (MoveTicket) move;
				targets.add(moveTicket.target);
			} else if (move instanceof MoveDouble) {
				MoveDouble doubleMove = (MoveDouble) move;
				validDoubleMoves.add(doubleMove);
			}
		}
		updateFirstMoves(targets); 
		
		try
		{
			String isDoubleMove = queue.take();
			MoveTicket move1 = new MoveTicket(model.getCurrentPlayer(), Integer.parseInt(queue.take()), Ticket.valueOf(queue.take())); 
			if(isDoubleMove == "true")
			{
				MoveTicket move2 = new MoveTicket(model.getCurrentPlayer(), Integer.parseInt(queue.take()), Ticket.valueOf(queue.take()));
				return new MoveDouble(model.getCurrentPlayer(), move1, move2);
			}
			else
			{
				return move1;
			}
		}
		catch (Exception e)
		{
			throw new Error(e);
		}
	}
		
		
		
		/*	int firstTarget = view.rsv.getFirstLocation();
			Ticket firstTicket = view.rsv.getFirstTicket();
			
			int secondTarget = 0;
			Ticket secondTicket = null;
			
			if(view.rsv.secondMoveLocationsBox.isVisible()) {
				secondTarget = view.rsv.getSecondLocation();
				secondTicket = view.rsv.getSecondTicket();
				MoveTicket firstMove = new MoveTicket(currentPlayer,firstTarget,firstTicket);
				MoveTicket secondMove = new MoveTicket(currentPlayer,secondTarget,secondTicket);
				MoveDouble move  = new MoveDouble(currentPlayer,firstMove,secondMove);
				model.play(move);
				System.out.println(model.getPlayerTickets(currentPlayer,secondTicket));
				System.out.println(model.getPlayerTickets(currentPlayer,Ticket.DoubleMove));
				view.lsv.setNumTickets(currentPlayer,firstTicket,model.getPlayerTickets(currentPlayer,firstTicket));
				view.lsv.setNumTickets(currentPlayer,secondTicket,model.getPlayerTickets(currentPlayer,secondTicket));
				view.lsv.setNumTickets(currentPlayer,Ticket.DoubleMove,
				model.getPlayerTickets(currentPlayer,Ticket.DoubleMove));
			} else {
				MoveTicket move = new MoveTicket(currentPlayer,firstTarget,firstTicket);
				model.play(move);
				view.lsv.setNumTickets(currentPlayer,firstTicket,model.getPlayerTickets(currentPlayer,firstTicket));
				
			}
			
			//put in new function
			view.rsv.hideSecondMove();
			if(currentPlayer.equals(Colour.Black)) {
				int currentRound = model.getRound();
				List<Boolean> showRounds = model.getRounds();
			
				//make new function
				for(int i=1;i<currentRound;i++) {
					MoveTicket moveTicket = model.mrXMoves(i);
					view.movesBar.addMrXMove(moveTicket.ticket,showRounds.get(i),i,moveTicket);
				}
			}

			view.lsv.setLocation(currentPlayer,model.getPlayerLocation(currentPlayer));
			view.rsv.setRoundNum(model.getRound());
			
			model.nextPlayer();
			currentPlayer = model.getCurrentPlayer();
			validMoves = new HashSet<Move>(model.validMoves(currentPlayer));
			validMoveTickets = createMoveTickets(validMoves);
			validDoubleMoves = createDoubleMoves(validMoves);
		
			view.rsv.firstMoveLocationsBox.removeItemListener(firstListener);
			view.rsv.firstMoveLocationsBox.removeAllItems();
			view.rsv.addFirstMoveLocationsListener(firstListener);
			view.rsv.firstMoveTicketBox.removeAllItems();
			
			updateFirstMoves();
			
			
		}
	
	}*/

	class FirstMoveLocationListener implements ItemListener {
		public void itemStateChanged(ItemEvent event) {
			//NO WE DON'T WANT VALIDMOVES
			validMoveTickets = createMoveTickets(validMoves);
			
			
			int chosenLocation = view.rsv.getFirstLocation();
			System.out.println("chosen LOCATION: " +chosenLocation);
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
		public void itemStateChanged(ItemEvent event)  {
			int firstLocation = view.rsv.getFirstLocation();
			int secondLocation = view.rsv.getSecondLocation();			
			Set<Ticket> possibleTickets = new HashSet<Ticket>();
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

	//bit of a hack, could be implemented better?
	class ClearButtonListener implements ActionListener {
		public void actionPerformed(ActionEvent event) {
			
			view.rsv.secondMoveLocationsBox.removeItemListener(secondListener);
			view.rsv.secondMoveLocationsBox.removeAllItems();
			view.rsv.secondMoveTicketBox.removeAllItems();
			view.rsv.firstMoveTicketBox.removeAllItems();
			view.rsv.firstMoveLocationsBox.removeItemListener(firstListener);
			view.rsv.firstMoveLocationsBox.removeAllItems();
			
			//updateFirstMoves();

			view.rsv.addFirstMoveLocationsListener(firstListener);
			view.rsv.addSecondMoveLocationsListener(secondListener);
			view.rsv.hideSecondMove();
		}
	}

	class DoubleMoveButtonListener implements ActionListener {
		public void actionPerformed(ActionEvent event) {
			int location = view.rsv.getFirstLocation();
			Set<Integer> possibleLocations = new HashSet<Integer>();
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

}


	

