package solution;
import scotlandyard.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.util.List;

public class GameViewController {

	private ScotlandYardModel model;
	private GameView view;
	
	public GameViewController(ScotlandYardModel model, GameView view) {
		this.model = model;
		this.view = view;
	}

	public void run() {
		LeftSideView lsv = new LeftSideView(model.getPlayers());
		initialiseLSV(lsv);

		RightSideView rsv = new RightSideView();
		initialiseRSV(rsv);

		MapView map = new MapView();

		MrXMovesBar movesBar = new MrXMovesBar(model.getRounds().size());
		
		rsv.addGoButtonListener(new goButtonListener());
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
		Colour currentPlayer = model.getCurrentPlayer();
		
		java.util.Set<Integer> locations = new HashSet<Integer>();
		java.util.Set<Ticket> tickets = new HashSet<Ticket>();

		java.util.Set<Move> firstMoves = new HashSet<Move>();
		java.util.Set<Move> secondMoves = new HashSet<Move>();
		
		java.util.Set<Move> makeableMoves = new HashSet<Move>(model.validMoves(currentPlayer));
		java.util.Set<MoveTicket> moveTickets = createMoveTickets(makeableMoves);
		java.util.Set<MoveDouble> doubleMoves = createDoubleMoves(makeableMoves);
		
		for(MoveTicket moveTicket:moveTickets) {
			locations.add(moveTicket.target);
			tickets.add(moveTicket.ticket);
		}
		
		for(MoveDouble doubleMove:doubleMoves) {
			firstMoves.add(doubleMove.moves.get(0));
			secondMoves.add(doubleMove.moves.get(1));
		}

		java.util.Set<MoveTicket> firstMoveTickets = createMoveTickets(firstMoves);
		java.util.Set<MoveTicket> secondMoveTickets = createMoveTickets(secondMoves);
		//need to deal with case where in a double move they play another double move
		java.util.Set<Integer> firstLocations = new HashSet<Integer>();
		java.util.Set<Integer> secondLocations = new HashSet<Integer>();
		java.util.Set<Ticket> firstTickets = new HashSet<Ticket>();
		java.util.Set<Ticket> secondTickets = new HashSet<Ticket>();
		
		for(MoveTicket ticket1:firstMoveTickets) {
			firstLocations.add(ticket1.target);
			firstTickets.add(ticket1.ticket);
		}

		for(MoveTicket ticket2:secondMoveTickets) {
			secondLocations.add(ticket2.target);
			secondTickets.add(ticket2.ticket);
		}

		rsv.setSingleMoves(locations);
		rsv.setSingleTickets(tickets);
		rsv.setDoubleMoves1(firstLocations);
		rsv.setDoubleTickets1(firstTickets);
		rsv.setDoubleMoves2(secondLocations);
		rsv.setDoubleTickets2(secondTickets);
	}

	private java.util.Set<MoveTicket> createMoveTickets(java.util.Set<Move> moves) {
		java.util.Set<MoveTicket> moveTickets = new HashSet<MoveTicket>();
		for(Move move:moves) {
			if(move instanceof MoveTicket) {
				MoveTicket ticket = (MoveTicket) move;
				moveTickets.add(ticket);
			}
		}
		return moveTickets;
	}

	private java.util.Set<MoveDouble> createDoubleMoves(java.util.Set<Move> moves) {
		java.util.Set<MoveDouble> doubleMoves = new HashSet<MoveDouble>();
		for(Move move:moves) {
			if(move instanceof MoveDouble) {
				MoveDouble doubleMove = (MoveDouble) move;
				doubleMoves.add(doubleMove);
			}
		}
		return doubleMoves;
	}
	//changes the values in the single move locations combo box
	private void updateSingleMoves(java.util.Set<Move> moves) {
		java.util.Set<Integer> nodes = new HashSet<Integer>();
		for(Move move:moves) {
			if(move instanceof MoveTicket) {
				MoveTicket ticket = (MoveTicket) move;
				nodes.add(ticket.target);
			}
		}
		view.rsv.setSingleMoves(nodes);
	}

	//changes the values in the single move ticket combobox
	private void updateSingleTickets(java.util.Set<Move> moves) {
		java.util.Set<Ticket> tickets = new HashSet<Ticket>();
		for(Move move:moves) {
			if(move instanceof MoveTicket) {
				MoveTicket ticket = (MoveTicket) move;
				tickets.add(ticket.ticket);
			}
		}
		view.rsv.setSingleTickets(tickets);
	}

	class goButtonListener implements ActionListener {
		public void actionPerformed(ActionEvent event) {
			//assuming detective for now...
			int target = view.rsv.getSingleMove();
			Ticket ticket = view.rsv.getSingleTicket();
			Colour colour = model.getCurrentPlayer();
			MoveTicket move = new MoveTicket(colour,target,ticket);
			model.play(move);
			view.lsv.setNumTickets(colour,ticket,model.getPlayerTickets(colour,ticket));
			
			if(colour.equals(Colour.Black)) {
				int currentRound = model.getRound();
				Boolean showRound = model.getRounds().get(currentRound);
				int location = model.getPlayerLocation(colour);
				view.movesBar.addMrXMove(ticket,showRound,currentRound,location);
				
			}

			
			//update circles on map
			//increment player
			model.nextPlayer();
			Colour newPlayer = model.getCurrentPlayer();
			//get valid moves for next player
			List<Move> nextMoves = model.validMoves(newPlayer);
			Set<Move> movesSet = new HashSet<Move>(nextMoves);
			//update new location
			view.rsv.singleMoveBox.removeAllItems();
			view.rsv.singleTicketBox.removeAllItems();
			updateSingleMoves(movesSet);
			updateSingleTickets(movesSet);
		}
	}
}


	

