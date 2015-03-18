package solution;
import scotlandyard.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.util.List;

//probably worth storing currentPlayer in global variable
//some very smelly code here
//may want to put all listeners in a single class
//may want to initialise all listeners in a single function

public class GameViewController {

	private ScotlandYardModel model;
	private GameView view;

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

	private ActionListener firstListener;
	private ActionListener secondListener;

	
	public GameViewController(ScotlandYardModel model, GameView view) {
		this.model = model;
		this.view = view;

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
		rsv.addGoButtonListener(new GoButtonListener());

		firstListener = new FirstMoveLocationListener();
		secondListener = new SecondMoveLocationListener();
		rsv.addFirstMoveLocationsListener(firstListener);
		rsv.addSecondMoveLocationsListener(secondListener);
		rsv.addClearButtonListener(new ClearButtonListener());
		rsv.addDoubleMoveButtonListener(new DoubleMoveButtonListener());
		
		view.createView(lsv,map,rsv,movesBar);

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
	private void updateFirstMoves() {
		view.rsv.setFirstMoveLocations(firstMoveLocations);
	}


	class GoButtonListener implements ActionListener {
		public void actionPerformed(ActionEvent event) {
		
			int target = view.rsv.getFirstLocation();
			Ticket ticket = view.rsv.getFirstTicket();
			
			MoveTicket move = new MoveTicket(currentPlayer,target,ticket);
			model.play(move);
			view.lsv.setNumTickets(currentPlayer,ticket,model.getPlayerTickets(currentPlayer,ticket));
			view.rsv.hideSecondMove();
			if(currentPlayer.equals(Colour.Black)) {
				int currentRound = model.getRound();
				Boolean showRound = model.getRounds().get(currentRound);
				int location = model.getPlayerLocation(currentPlayer);
				view.movesBar.addMrXMove(ticket,showRound,currentRound,location);
			}

			view.lsv.setLocation(currentPlayer,model.getPlayerLocation(currentPlayer));
			view.rsv.setRoundNum(model.getRound());
			
			model.nextPlayer();
			currentPlayer = model.getCurrentPlayer();
			validMoves = new HashSet<Move>(model.validMoves(currentPlayer));
			validMoveTickets = createMoveTickets(validMoves);
			validDoubleMoves = createDoubleMoves(validMoves);
		
			
			view.rsv.firstMoveLocationsBox.removeAllItems();
			view.rsv.firstMoveTicketBox.removeAllItems();
			updateFirstMoves();
			
		}
	
	}

	class FirstMoveLocationListener implements ActionListener {
		public void actionPerformed(ActionEvent event) {
		
			validMoveTickets = createMoveTickets(validMoves);
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
	
	class SecondMoveLocationListener implements ActionListener {
		public void actionPerformed(ActionEvent event) {
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
			
			view.rsv.secondMoveLocationsBox.removeActionListener(secondListener);
			view.rsv.secondMoveLocationsBox.removeAllItems();
			view.rsv.secondMoveTicketBox.removeAllItems();
			view.rsv.firstMoveTicketBox.removeAllItems();
			view.rsv.firstMoveLocationsBox.removeActionListener(firstListener);
			view.rsv.firstMoveLocationsBox.removeAllItems();
			
			updateFirstMoves();

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


	

