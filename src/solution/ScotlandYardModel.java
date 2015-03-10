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
       int location = colourToLocation.get(colour);
       Player player = colourToPlayer.get(colour);
       List<Move> validMoves = validMoves(colour);
       Move playerMove = player.notify(location, validMoves);
       if (validMoves.contains(playerMove)) return playerMove;
       else return playerMove; // what should we do if player retuens bad move ????
    }

    @Override
    protected void nextPlayer() {

    }

    @Override
    protected void play(MoveTicket move) {

    }

    @Override
    protected void play(MoveDouble move) {

    }

    @Override
    protected void play(MovePass move) {

    }

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
        return makableMoves;
    }
    
    private boolean enoughTicketsMrX(MoveDouble move, int numSectetTickets)
    {
    	int numDouble = getPlayerTickets(Colour.valueOf("Black"), Ticket.valueOf("DoubleMove"));
    	if(numDouble <= 0) return false;
    	String[] substrings = move.toString().split(" -> | |: ");
    	Colour player = Colour.valueOf(substrings[2]);
    	int m1Target = Integer.parseInt(substrings[3]);
    	Ticket m1Ticket = Ticket.valueOf(substrings[4]);
    	int m2Target = Integer.parseInt(substrings[5]);
    	Ticket m2Ticket = Ticket.valueOf(substrings[6]);
    	MoveTicket firstMove = new MoveTicket(player, m1Target, m1Ticket);
    	MoveTicket secondMove = new MoveTicket(player, m2Target, m2Ticket);
    	if( !enoughTicketsMrX(firstMove, numSectetTickets) ) return false;
    	if(m1Ticket.equals(Ticket.valueOf("SecretMove"))) numSectetTickets --;
    	if( !enoughTicketsMrX(secondMove, numSectetTickets) ) return false;
    	else return true;
    }
    
    //checks, for a single moves, that for given move MrX has got 
    //enough ticekts to make move
    private boolean enoughTicketsMrX(MoveTicket move, int numSectetTickets)
    {
    	String[] substrings = move.toString().split(" ");
    	Ticket ticket = Ticket.valueOf(substrings[2]);
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
    	String[] substrings = move.toString().split(" ");
    	Ticket ticket = Ticket.valueOf(substrings[2]);
    	if(ticket.equals(Ticket.valueOf("Bus")) && numBus > 0) return true;
		else if(ticket.equals(Ticket.valueOf("Taxi")) && numTaxi > 0) return true;
		else if(ticket.equals(Ticket.valueOf("Underground")) && numUnderground > 0) return true;
		else return false;
    }
    
    //checks if player (bar player making @move) is at either 2 nodes making 
    //up double move
    private boolean moveOcupyTest(MoveDouble move)
    {
    	String[] substrings = move.toString().split(" -> | |: ");
    	Colour player = Colour.valueOf(substrings[2]);
    	int m1Target = Integer.parseInt(substrings[3]);
    	Ticket m1Ticket = Ticket.valueOf(substrings[4]);
    	int m2Target = Integer.parseInt(substrings[5]);
    	Ticket m2Ticket = Ticket.valueOf(substrings[6]);
    	MoveTicket firstMove = new MoveTicket(player, m1Target, m1Ticket);
    	MoveTicket secondMove = new MoveTicket(player, m2Target, m2Ticket);
    	if( !moveOcupyTest(firstMove) && !moveOcupyTest(secondMove) )
    	{
    		return false;
    	}
    	else return true;
    }
    
    //checks if player (bar player making @move) is at target node
    private boolean moveOcupyTest(MoveTicket move)
    {
    	String[] substrings = move.toString().split(" ");
    	int target = Integer.parseInt(substrings[substrings.length - 2]);
    	String color =  substrings[0];
    	int currentLocation = colourToLocation.get(Colour.valueOf(color));
    	if(currentLocation == target || !ocupy(target) ) return false;
    	else return true;
    }
    
    //returns true if a player is currently at the given node 
    private boolean ocupy(int node)
    {
    	for(Colour player : orderOfPlay)
    	{
    		if(colourToLocation.get(player).equals(node)) return true;
    	}
    	return false;
    }
    
    // returns all posible moves (only double if player is MrX but this is 
    //only valodation done) for given player at given location
    private List<MoveDouble> doubleMoves(Colour player)
    {
    	int location = colourToLocation.get(player);
    	List<MoveDouble> possibleMoves = new ArrayList();
    	List<MoveTicket> singleMoves = singleMoves(location, player);
    	if(player == Colour.valueOf("Black"))
    	{
    		for(MoveTicket fisrtMove : singleMoves)
    		{
    			String[] substrings = fisrtMove.toString().split(" ");
    			int target = Integer.parseInt(substrings[substrings.length - 2]);
    			for(MoveTicket secondMove : singleMoves(target, player))
    			{
    				possibleMoves.add(new MoveDouble(player, fisrtMove, secondMove));
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
        return currentPlayer;
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
