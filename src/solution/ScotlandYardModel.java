package solution;

import scotlandyard.*;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.*;

public class ScotlandYardModel extends ScotlandYard {

private final Graph londonGraph;
private Colour currentPlayer;
private final Map<Colour,Player> colourToPlayer;
private Map<Colour,Integer> colourToLocation;
private Map<Colour,Map<Ticket,Integer>> colourToTickets;
private List<Colour> orderOfPlay; 
private int numberOfDetectives;
private final List<Boolean> showRounds; 

    //Class constructor reads and stores graph and game attributes (e.g. num of players, show rounds). 	
    public ScotlandYardModel(int numberOfDetectives, List<Boolean> rounds, String graphFileName) throws IOException{ 
        super(numberOfDetectives, rounds, graphFileName); // ask TA
        
		ScotlandYardGraphReader reader = new ScotlandYardGraphReader();
		Graph<Integer,Route> graph = null;
		try {
			graph = reader.readGraph(graphFileName);
			
		} catch(Exception e) {
			System.err.println(e.getMessage());
			
		}
		londonGraph = graph;
		this.numberOfDetectives = numberOfDetectives;
		showRounds = rounds;
		
		orderOfPlay = new ArrayList<Colour>();		
		colourToPlayer = new HashMap<Colour,Player>();
		colourToTickets = new HashMap<Colour,Map<Ticket,Integer>>();
		colourToLocation = new HashMap<Colour, Integer>();
		
		currentPlayer = Colour.valueOf("Black");
    }


    @Override
    protected Move getPlayerMove(Colour colour) 
    {
       // wrong - valid moves and not checked what player has resturned to us
        int location = colourToLocation.get(colour);
        List<Move> moves = validMoves(colour);
        Player player = colourToPlayer.get(colour);
        Move returnMove = player.notify(location, moves);
        return returnMove;
    }

    //increments player
    @Override
    protected void nextPlayer() {
		int index = orderOfPlay.indexOf(currentPlayer);
		int size = orderOfPlay.size();
		if(index == size - 1) currentPlayer = orderOfPlay.get(0);
		else  currentPlayer = orderOfPlay.get(index + 1);
    }

    //plays a single move by updating relevent properties
    @Override
    protected void play(MoveTicket move)
    {
    	Colour player = move.colour;
    	int target = move.target;
    	Ticket ticket = move.ticket;
    	colourToLocation.put(player, target);
    	int numTickets = getPlayerTickets(player, ticket);
    	colourToTickets.get(player).put(ticket, numTickets - 1);
    }

    //plays a double move by updating relevent properties
    @Override
    protected void play(MoveDouble move) 
    {
		List<Move> moves = move.moves;
    	MoveTicket firstMove = (MoveTicket) moves.get(0);
    	MoveTicket secondMove = (MoveTicket) moves.get(1);
    	Colour player = firstMove.colour;
    	int numDoubleTickets = getPlayerTickets(player, Ticket.valueOf("DoubleMove"));
    	colourToTickets.get(player).put(Ticket.valueOf("DoubleMove"), numDoubleTickets - 1);
    	play(firstMove);
    	play(secondMove);
    }

    @Override
    protected void play(MovePass move) 
    {
    }

    //vists all valid moves for given player
    @Override
    protected List<Move> validMoves(Colour player) 
    {
        int location = colourToLocation.get(player);
        List<Move> makableMoves = new ArrayList();
        List<MoveTicket> singleMoves = singleMoves(location, player);
        List<MoveDouble> doubleMoves = doubleMoves(player);
        for(MoveTicket move : singleMoves)
        {
        	if(player.equals(Colour.valueOf("Black")))
        	{
        		int numSecret = getPlayerTickets(Colour.valueOf("Black"), Ticket.valueOf("SecretMove"));
        		if( enoughTicketsMrX(move, numSecret) && !moveOcupyTest(move) )
        		{
        			makableMoves.add(move);
        		}
        	}
        	else
        	{
        		int numBus = getPlayerTickets(player, Ticket.valueOf("Bus"));
				int numTaxi = getPlayerTickets(player, Ticket.valueOf("Taxi"));
				int numUnderground = getPlayerTickets(player, Ticket.valueOf("Underground"));
        		if( enoghTicketsDetective(move, numBus, numTaxi, numUnderground) && !moveOcupyTest(move) )
        		{
        			makableMoves.add(move);
        		}
        	}
        }
        if(player.equals(Colour.valueOf("Black")))
        {
		    for(MoveDouble move : doubleMoves)
		    {
		    		int numSecret = getPlayerTickets(Colour.valueOf("Black"), Ticket.valueOf("SecretMove"));
		    		if( enoughTicketsMrX(move, numSecret) && !moveOcupyTest(move) )
		    		{
		    			makableMoves.add(move);
		    		}
		    }
        }
        if (makableMoves.size() == 0) makableMoves.add(new MovePass(player));
        return makableMoves;
    }
    
    // checks to see if for given double move, the move can be made with
    // @numSecretTickets
    private boolean enoughTicketsMrX(MoveDouble move, int numSectetTickets)
    {
    	int numDouble = getPlayerTickets(Colour.valueOf("Black"), Ticket.valueOf("DoubleMove"));
    	if(numDouble <= 0) return false;
    	List<Move> moves = move.moves;
    	MoveTicket firstMove = (MoveTicket) moves.get(0);
    	Ticket m1Ticket = firstMove.ticket;
    	MoveTicket secondMove = (MoveTicket) moves.get(1);
    	if( !enoughTicketsMrX(firstMove, numSectetTickets) ) return false;
    	if(m1Ticket.equals(Ticket.valueOf("SecretMove"))) numSectetTickets --;
    	if( !enoughTicketsMrX(secondMove, numSectetTickets) ) return false;
    	else return true;
    }
    
    //checks, for a single moves, that for given move MrX has got 
    //enough ticekts to make move
    private boolean enoughTicketsMrX(MoveTicket move, int numSectetTickets)
    {
    	Ticket ticket = move.ticket;
    	if(ticket.equals(Ticket.valueOf("SecretMove")))
    	{
    		if(numSectetTickets > 0) return true;
    		else return false;
    	}
    	return true;
    }
    
    //checks, for a single moves, that for given move a detective has got 
    //enough ticekts to make move
    private boolean enoghTicketsDetective(MoveTicket move, int numBus, int numTaxi, int numUnderground)
    {
    	Ticket ticket = move.ticket;
    	if(ticket.equals(Ticket.valueOf("Bus")) && numBus > 0) return true;
		else if(ticket.equals(Ticket.valueOf("Taxi")) && numTaxi > 0) return true;
		else if(ticket.equals(Ticket.valueOf("Underground")) && numUnderground > 0) return true;
		else return false;
    }
    
    //checks if player (bar player making @move or MrX) is at either 2 nodes making 
    //up double move
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
    
    //checks if player (bar player making @move or MrX) is at target node
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
    
    //returns all possible double moves
    private List<MoveDouble> doubleMoves(Colour player)
    {
    	int location = colourToLocation.get(player);
    	List<MoveDouble> possibleMoves = new ArrayList();
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
    
    //returns list of unValidated moveTickets for @player at @location
    private List<MoveTicket> singleMoves(int location, Colour player)
    {
    	List<MoveTicket> possibleMoveTickets = new ArrayList();
    	List<Edge<Integer, Route>> possibleEdges  = londonGraph.getEdges(location);
    	for (Edge<Integer, Route> edge : possibleEdges)
    	{
    		possibleMoveTickets.add(edgeToTicket(location, player, edge));
    	}
    	return possibleMoveTickets;
    }
    
    //produces move for @player at @location moving along @edge
    private MoveTicket edgeToTicket(int location, Colour player, Edge<Integer, Route> edge)
    {
    	int target = edge.other(location);
        Ticket moveType =  Ticket.fromRoute(edge.data());
        return new MoveTicket(player, target, moveType);
    }

    @Override
    public void spectate(Spectator spectator) {

    }

    //Adds a player to the game, storing the player's colour, location and tickets. 
    @Override 
    public boolean join(Player player, Colour colour, int location, Map<Ticket, Integer> tickets) {
		if(orderOfPlay!=null && orderOfPlay.contains(colour)) return false; //how should we handle false return? 
			else {
				orderOfPlay.add(colour);
				sortColours(orderOfPlay);
				colourToPlayer.put(colour,player);
				colourToLocation.put(colour,location);
				colourToTickets.put(colour,tickets);
	   	}
		return true;
    }

    @Override
    public List<Colour> getPlayers() {
        return orderOfPlay; // do we want a separate list for players not in order of play?
    }

    @Override
    public Set<Colour> getWinningPlayers() {
        return null;
    }

    @Override
    public int getPlayerLocation(Colour colour) {
    	return colourToLocation.get(colour);
    }

    @Override
    public int getPlayerTickets(Colour colour, Ticket ticket) {
        Map<Ticket,Integer> ticketMap = colourToTickets.get(colour);
        return ticketMap.get(ticket);
    }

    @Override
    public boolean isGameOver() {
        return false;
    }

    //Checks whether enough players have been added and that black is to start. 
    @Override
    public boolean isReady() {
    	if(orderOfPlay.size() == numberOfDetectives +1 && orderOfPlay.get(0).equals(Colour.Black)) {
    		return true;
    	} else return false;
    }

    @Override
    public Colour getCurrentPlayer() {
        return currentPlayer; //currentPlayer;
    }

    @Override
    public int getRound() {
        return 0;
    }

    @Override
    public List<Boolean> getRounds() {
        return null;
    }

    //Ensures Black is at the front of the list. 
    private List<Colour> sortColours(List<Colour> colours) {
	int index = colours.indexOf(Colour.Black); //not sure this the best way to implement? 
	if(index != (-1)) {
		Colour firstColour = colours.get(0);
		colours.set(0,Colour.Black);
		colours.set(index,firstColour);	
	}
	return colours;
    }
}
