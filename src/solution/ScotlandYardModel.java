package solution;

import scotlandyard.*;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.*;

public class ScotlandYardModel extends ScotlandYard implements java.io.Serializable {
	private final Graph<Integer,Route> londonGraph;
	private Colour currentPlayer;
	private final Map<Colour,Player> colourToPlayer;
	private Map<Colour,Integer> colourToLocation;
	private Map<Colour,Map<Ticket,Integer>> colourToTickets;
	private Map<Colour,Map<Ticket,Integer>> colourToOriginalTickets;
	private List<Colour> orderOfPlay; 
	private int numberOfDetectives;
	private final List<Boolean> showRounds; 
	private int currentRound; 
	private List<Spectator> spectators;
	private int lastKnownLocation;
	private List<MoveTicket> mrXMoves;
	private List<Move> movesMade;
	
	//so part way through a trun a game can't end - all internal refrences to 
	//checking if a game is over should refer to this not the function
	private Boolean isGameOver;
    
    //test function 
    private void playerState(Colour player)
    {
    	System.err.println("----------------------------");
    	System.err.println("colour -> " + player);
    	System.err.println("CurrentPlayer -> " + player.equals(currentPlayer));
    	System.err.println("location -> " + colourToLocation.get(player));
    	for( Ticket ticket : colourToTickets.get(player).keySet())
    	{
    		System.err.println(ticket + " -> " + colourToTickets.get(player).get(ticket));
    	}
    	System.err.println("order of play index -> " + orderOfPlay.indexOf(player));
    	System.err.println("-----------------------------");
    	return;
    }
    
    //Class constructor reads and stores graph and game attributes (e.g. num of players, show rounds). 	
    public ScotlandYardModel(int numberOfDetectives, List<Boolean> rounds, String graphFileName) throws IOException{ 
        super(numberOfDetectives, rounds, graphFileName);
		ScotlandYardGraphReader reader = new ScotlandYardGraphReader();
		Graph<Integer,Route> graph = null;
		try 
		{
			graph = reader.readGraph(graphFileName);
		} 
		catch(Exception e) 
		{
			System.err.println(e.getMessage());
		}
		londonGraph = graph;
		this.numberOfDetectives = numberOfDetectives;
		showRounds = rounds;
		orderOfPlay = new ArrayList<Colour>();		
		colourToPlayer = new HashMap<Colour,Player>();
		colourToTickets = new HashMap<Colour,Map<Ticket,Integer>>();
		colourToOriginalTickets = new HashMap<Colour,Map<Ticket,Integer>>();
		colourToLocation = new HashMap<Colour, Integer>();
		spectators = new ArrayList<Spectator>();
		mrXMoves = new ArrayList<MoveTicket>();
		movesMade = new ArrayList<Move>();
		currentPlayer = Colour.valueOf("Black");
		currentRound = 0;
		lastKnownLocation = 0;
		isGameOver = false;
    }

    //asks a player for a move giving it the valid moves it can make 
    @Override
    protected Move getPlayerMove(Colour colour) 
    {
        int location = colourToLocation.get(colour);
        List<Move> moves = validMoves(colour);
        Player player = colourToPlayer.get(colour);
        Move returnMove = player.notify(location, moves);
        if(moves.contains(returnMove)) return returnMove;
        else return new MovePass(colour);
    }

    //sets next player, according to orderOfPlay, to currentPlayer 
    @Override
    protected void nextPlayer() 
    {
		int index = orderOfPlay.indexOf(currentPlayer);
		int size = orderOfPlay.size();
		if(index == size - 1) currentPlayer = orderOfPlay.get(0);
		else  currentPlayer = orderOfPlay.get(index + 1);
    }

    //plays a single move by moving the relevent player and adjusting MrX's
    // and the player who has just moved tickets. Also increments round when
    //required and updates spectators
    @Override
    protected void play(MoveTicket move)
    {
    	Colour player = move.colour;
    	int target = move.target;
    	Ticket ticket = move.ticket;
    	colourToLocation.put(player, target);
    	putPlayerTickets(player, ticket , -1);
    	if(player.equals(Colour.Black))
    	{
    		currentRound++;
    		mrXMoves.add(move);
    	}
    	else
    	{
    		putPlayerTickets(Colour.Black, ticket , 1);	
    	}
    	movesMade.add(move);
    	updateSpectators(player, move);
    }
    
    //plays a double move by calling play on two moveTickets
    @Override
    protected void play(MoveDouble move) 
    {
		List<Move> moves = move.moves;
    	MoveTicket firstMove = (MoveTicket) moves.get(0);
    	MoveTicket secondMove = (MoveTicket) moves.get(1);
    	Colour player = firstMove.colour;
    	putPlayerTickets(player, Ticket.DoubleMove , -1);
    	MoveTicket notifyMove = new MoveTicket(player, 0, Ticket.DoubleMove);
    	for(Spectator s: spectators) 
    	{
			s.notify(new MoveDouble(player, notifyMove, notifyMove));
    	}
    	movesMade.add(move);
    	play(firstMove);
    	play(secondMove);
    }

    //notifies spectators that a pass move has been made
    @Override
    protected void play(MovePass move) 
    {
    	for(Spectator s:spectators)
    	{
			s.notify(move); 
		}
		movesMade.add(move);
    	return;
    }
	
	//returns correct move to notifty players with based on roundNum
    private MoveTicket buildNotifyMove(MoveTicket move, int roundNum)
    {
    	Colour colour = move.colour;
    	int target;
    	if(colour.equals(Colour.Black) && showRounds.get(roundNum) != true)
    	{
    		target = 0;
    	}
    	else
    	{
    		target = move.target;
    	}
    	return new MoveTicket(colour, target, move.ticket);
    }
    
    //notifies spectators that colour has made move (incl. Mr X) & updates
    //last known location for mrX
    private void updateSpectators(Colour colour, MoveTicket move) 
    {
		MoveTicket notifyMove = buildNotifyMove(move, currentRound);
    	if(showRounds.get(currentRound) == true && colour.equals(Colour.Black))
    	{
			lastKnownLocation = move.target;
    	}
    	for(Spectator s:spectators) 
    	{
			s.notify(notifyMove); 
		}
    }

    // checks if players are stuck or game is over and returns pass moves or
    // empty lists as appropriate as well as updating spectators
    @Override
    protected List<Move> validMoves(Colour player) 
    {
        List<Move> makableMoves = new ArrayList();
        if(isGameOver) makableMoves.add(new MovePass(player));
        else makableMoves = makableMoves(player);
        if(makableMoves.size() == 0 && !player.equals(Colour.Black)) 
        {
        	makableMoves.add(new MovePass(player));
        }
        return makableMoves;
    }
    
   	//gets moves for player using singleMoves() and doubleMoves() and
    //checks they are valid using enoughTickets() and moveOcupyTest()
    // Does not include pass moves
    protected List<Move> makableMoves(Colour player)
    {
    	int location = colourToLocation.get(player);
        List<Move> makableMoves = new ArrayList();
        List<MoveTicket> singleMoves = singleMoves(location, player);
        List<MoveDouble> doubleMoves = doubleMoves(player);
        int numBus = getPlayerTickets(player, Ticket.valueOf("Bus"));
		int numTaxi = getPlayerTickets(player, Ticket.valueOf("Taxi"));
		int numUnderground = getPlayerTickets(player, Ticket.valueOf("Underground"));
		int numSecret;
		if(player.equals(Colour.Black)) numSecret = getPlayerTickets(Colour.valueOf("Black"), Ticket.valueOf("SecretMove"));
		else numSecret = 0;
        for(MoveTicket move : singleMoves)
        {
        	if( enoughTickets(move, numSecret, numBus, numTaxi, numUnderground) && !moveOcupyTest(move) )
        	{
        	    makableMoves.add(move);
        	}
        }
		for(MoveDouble move : doubleMoves)
		{
			if( enoughTickets(move, numSecret, numBus, numTaxi, numUnderground) && !moveOcupyTest(move) )
			{
				makableMoves.add(move);
			}
		}
        return makableMoves;
    }
    
    //returns true if MrX has enough tickets to make the double move
    private boolean enoughTickets(MoveDouble move, int numSectetTickets, int numBus, int numTaxi, int numUnderground)
    {
    	int numDouble = getPlayerTickets(Colour.valueOf("Black"), Ticket.valueOf("DoubleMove"));
    	if(numDouble <= 0) return false;
    	List<Move> moves = move.moves;
    	MoveTicket firstMove = (MoveTicket) moves.get(0);
    	Ticket m1Ticket = firstMove.ticket;
    	MoveTicket secondMove = (MoveTicket) moves.get(1);
    	if( !enoughTickets(firstMove, numSectetTickets, numBus, numTaxi, numUnderground) ) return false;
    	if(m1Ticket.equals(Ticket.valueOf("SecretMove"))) numSectetTickets --;
    	else if(m1Ticket.equals(Ticket.valueOf("Bus"))) numBus --;
    	else if(m1Ticket.equals(Ticket.valueOf("Taxi"))) numTaxi --;
    	else if(m1Ticket.equals(Ticket.valueOf("Underground"))) numUnderground --;
    	if( !enoughTickets(secondMove, numSectetTickets, numBus, numTaxi, numUnderground) ) return false;
    	else return true;
    }
    
    //returns true if player making move has enough tickets to make the single move
    private boolean enoughTickets(MoveTicket move, int numSectetTickets, int numBus, int numTaxi, int numUnderground)
    {
    	Ticket ticket = move.ticket;
    	Colour player = move.colour;
    	if(ticket.equals(Ticket.valueOf("SecretMove")) && player.equals(Colour.Black) && numSectetTickets > 0 ) return true;
    	else if(ticket.equals(Ticket.valueOf("Bus")) && numBus > 0) return true;
		else if(ticket.equals(Ticket.valueOf("Taxi")) && numTaxi > 0) return true;
		else if(ticket.equals(Ticket.valueOf("Underground")) && numUnderground > 0) return true;
    	return false;
    }
    
    //returns true if any player (exept player making move or MrX) is at 
    //either 2 target nodes making up double move. Returns false otherwise
    private boolean moveOcupyTest(MoveDouble move)
    {
    	List<Move> moves = move.moves;
    	MoveTicket firstMove = (MoveTicket) moves.get(0);
    	MoveTicket secondMove = (MoveTicket) moves.get(1);
    	if( !moveOcupyTest(firstMove) && !moveOcupyTest(secondMove) )
    	{
    		return false;
    	}
    	else return true;
    }
    
    //returns true if any player (exept player making move or MrX) is at 
    //target node of the move. Returns false otherwise
    private boolean moveOcupyTest(MoveTicket move)
    {
    	int target = move.target;
    	Colour color = move.colour;
    	int currentLocation = colourToLocation.get(color);
    	if(currentLocation == target || !ocupy(target) ) return false;
    	else return true;
    }
    
    //returns true if a player except MrX is currently at the given node 
    private boolean ocupy(int node)
    {
    	for(Colour player : orderOfPlay)
    	{
    		if(colourToLocation.get(player).equals(node))
    		{
    			if(player.equals(Colour.valueOf("Black"))) return false;
    			else return true;
    		}
    	}
    	return false;
    }
    
    //returns list of all possible (unValidated) double moves for player at 
    //location
    private List<MoveDouble> doubleMoves(Colour player)
    {
    	int location = colourToLocation.get(player);
    	List<MoveDouble> possibleMoves = new ArrayList();
    	if(currentRound + 2 == showRounds.size()) return possibleMoves;
    	List<MoveTicket> singleMoves = singleMoves(location, player);
    	if(player.equals(Colour.valueOf("Black")))
    	{
    		for(MoveTicket firstMove : singleMoves)
    		{
    			int target = firstMove.target;
    			for(MoveTicket secondMove : singleMoves(target, player))
    			{
    				possibleMoves.add(new MoveDouble(player, firstMove, secondMove));
    			}
    		}
    	}
    	return possibleMoves;
    }
    
    //returns list of all possible (unValidated) single moves for player at 
    //location
    private List<MoveTicket> singleMoves(int location, Colour player)
    {
    	List<MoveTicket> possibleMoveTickets = new ArrayList();
    	List<Edge<Integer, Route>> possibleEdges  = londonGraph.getEdges(location);
    	for (Edge<Integer, Route> edge : possibleEdges)
    	{
    		possibleMoveTickets.addAll(edgeToTicket(location, player, edge));
    	}
    	return possibleMoveTickets;
    }
    
    //produces moves for player at location moving along edge
    private List<MoveTicket> edgeToTicket(int location, Colour player, Edge<Integer, Route> edge)
    {
    	List<MoveTicket> moves = new ArrayList();
    	int target = edge.other(location);
        Ticket moveType =  Ticket.fromRoute(edge.data());
        moves.add(new MoveTicket(player, target, moveType));
        if(!moveType.equals(Ticket.SecretMove))
        {
        	moves.add(new MoveTicket(player, target, Ticket.SecretMove));
        }
        return moves;
    }

    @Override
    public void spectate(Spectator spectator) 
    {
		spectators.add(spectator);
    }

    //Adds a player to the game, storing the player's colour, location and tickets. 
    @Override 
    public boolean join(Player player, Colour colour, int location, Map<Ticket, Integer> tickets) {
		if(orderOfPlay != null && orderOfPlay.contains(colour)) return false;
		else 
		{
			orderOfPlay.add(colour);
			sortColours(orderOfPlay);
			colourToPlayer.put(colour,player);
			colourToLocation.put(colour,location);
			if(showRounds.get(0) == true && colour.equals(Colour.Black)) lastKnownLocation = location;
			colourToTickets.put(colour,tickets);
			colourToOriginalTickets.put(colour, cloneTicketMap(tickets));
	   	}
		return true;
    }
    
    private Map<Ticket, Integer> cloneTicketMap(Map<Ticket, Integer> tickets)
    {
    	Map<Ticket, Integer> newTicketMap = new HashMap();
    	for(Ticket ticket : tickets.keySet())
    	{
    		newTicketMap.put(ticket, tickets.get(ticket));
    	}
    	return  newTicketMap;
    }

    @Override
    public List<Colour> getPlayers() 
    {
        return orderOfPlay;
    }

    @Override
    public Set<Colour> getWinningPlayers() 
    {
        Set<Colour> mrX = new HashSet(); 
        mrX.add(Colour.Black);
        Set<Colour> detectives = new HashSet(orderOfPlay);
        detectives.remove(Colour.Black);
        Set<Colour> empty = new HashSet(); 
        
        if(!isReady()) return empty;
        if (makableMoves(Colour.Black).size() == 0) return detectives;
        int mrxLocation = colourToLocation.get(Colour.Black);
        Boolean allDetectivesStuck = true; 
        for(Colour player : orderOfPlay)
        {
        	if(!player.equals(Colour.Black))
        	{
        		int playerLocation = colourToLocation.get(player); 
        		if(playerLocation == mrxLocation) return detectives;
        		if(makableMoves(player).size() > 0) allDetectivesStuck = false;
        	}
        }
        if(allDetectivesStuck) return mrX;
        if(currentRound == showRounds.size() -1 && currentPlayer.equals(Colour.Black))
        {
        	return mrX;
        }
        return empty;
    }
	
	//returns detectives location or mrX lastknown location
    @Override
    public int getPlayerLocation(Colour colour) 
    {
    	if(colour.equals(Colour.Black)) return lastKnownLocation;
    	else return colourToLocation.get(colour);
    }
    
    //returns mrX actual location
    public int getMrXLocation()
    {
    	return colourToLocation.get(Colour.Black);
    }
    
    //returns moveTicket representing move mrX made on roundNum
    public MoveTicket mrXMoves(int roundNum)
    {
    	if(mrXMoves.size() < roundNum) return null; 
    	else return mrXMoves.get(roundNum - 1);
    }
    
    public List<Move> movesMade()
    {
    	return movesMade;
    }

    @Override
    public int getPlayerTickets(Colour colour, Ticket ticket) 
    {
        Map<Ticket,Integer> ticketMap = colourToTickets.get(colour);
        return ticketMap.get(ticket);
    }
    
    public int getPlayerOriginalTickets(Colour colour, Ticket ticket) 
    {
        Map<Ticket,Integer> ticketMap = colourToOriginalTickets.get(colour);
        return ticketMap.get(ticket);
    }
    
    //adds @number to @tickets held by @colour
    private void putPlayerTickets(Colour colour, Ticket ticket, int number)
    {
    	Map<Ticket,Integer> ticketMap = colourToTickets.get(colour);
    	int numTickets = ticketMap.get(ticket);
    	ticketMap.put(ticket, numTickets + number);
    	colourToTickets.put(colour, ticketMap);
    }
	
	//checks if game is over using getWinningPlayers()
    @Override
    public boolean isGameOver()
    {
       if(getWinningPlayers().size() == 0) return isGameOver = false;
       else return isGameOver = true;	
    }

    //Checks whether enough players have been added and that black is to start. 
    @Override
    public boolean isReady() 
    {
    	if(orderOfPlay.size() == numberOfDetectives +1 && orderOfPlay.get(0).equals(Colour.Black))
    	{
    		return true;
    	} 
    	else return false;
    }

    @Override
    public Colour getCurrentPlayer()
    {
        return currentPlayer; 
    }

    @Override
    public int getRound() 
    {
        return currentRound;
    }
    
    public List<Player> getPlayerList()
    {
    	List<Player> players = new ArrayList();
    	for(Colour colour : orderOfPlay)
    	{
    		players.add(colourToPlayer.get(colour));
    	}
    	return players;
    }
	
	//@return list of show rounds 
    @Override
    public List<Boolean> getRounds() 
    {
        return showRounds;
    }

    //Ensures Black is at the front of the list 
    private List<Colour> sortColours(List<Colour> colours) 
    {
		int index = colours.indexOf(Colour.Black);
		if(index != (-1))
		{
			Colour firstColour = colours.get(0);
			colours.set(0,Colour.Black);
			colours.set(index,firstColour);	
		}
		return colours;
    }
}

