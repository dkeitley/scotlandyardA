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
	private Set<Move> validMoves;
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
		rsv.addPassButtonListener(view.new PassListener(queue));

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
		
		validMoves = new HashSet<Move>();
		
		
		rsv.hideSecondMove();
		rsv.setRoundNum(1);
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
	}
	
	private void update(Move move)
	{
		updateLSV();
		updateRSV(move);
		return;
	}
	
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
	
	private void updateRSV(Move move)
	{
		//NEED TO SORT ROUND NUM OUT
		view.rsv.setRoundNum(model.getRound()+1);
		for(int i = 1; i <= model.getRound() ; i++) 
		{
			MoveTicket moveTicket = model.mrXMoves(i);
			String text = moveTicket.ticket.toString();
			if(model.getRounds().get(i)) {
				text += " " + moveTicket.target;
			}
			view.movesBar.addMrXMove(text,i);
		}
		return;
	}

	public Move getPlayerMove(List<Move> moves) 
	{
		updateComboBoxes(moves);
		return getFromQueue();
	}
	
	private void updateComboBoxes(List<Move> moves)
	{
		validMoves = new HashSet<Move>(moves);
		view.rsv.displayMoveControls();
		view.rsv.hideSecondMove();
		view.rsv.passButton.setVisible(false);
		if(model.getCurrentPlayer().equals(Colour.Black)) view.rsv.doubleMoveButton.setVisible(true);
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

	class FirstMoveLocationListener implements ItemListener {
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

	//bit of a hack, could be implemented better?
	class ClearButtonListener implements ActionListener {
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
		}
	}

	class DoubleMoveButtonListener implements ActionListener {
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

}
