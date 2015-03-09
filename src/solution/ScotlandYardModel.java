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
    }


    @Override
    protected Move getPlayerMove(Colour colour) {
        return null;
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

    //returns valid moves for current player
    @Override
    protected List<Move> validMoves(Colour player) 
    {
        return null;
    }
    
    //returns all single moves for current player which are valid
    // using the validEdges() function
    private List<Move> singleMoves(Colour player)
    {
    	int location = colourToLocation.get(player);
        List<Move> validMoves = new ArrayList();
        for( Edge<Integer, Route> edge : validEdges(player))
        {
        	int target = edge.other(location);
        	Ticket moveType =  Ticket.fromRoute(edge.data());
        	Move moveTicket = new MoveTicket(player, target, moveType);
        	validMoves.add(moveTicket);
        }
        return validMoves;
    }
    
   // returns list of valid edges (that is edges comming from node player is  
   //currently located and for which he has tickets for travel) - CURRENTLY ONLY 
   // WORKS FOR NON MRX PLAYERS!!!!!!!  
    private List<Edge<Integer, Route>> validEdges(Colour player)
    {
    	int location = colourToLocation.get(player);
        List<Edge<Integer, Route>> possibleEdges  = londonGraph.getEdges(location);
        int numBus = getPlayerTickets(player, Ticket.valueOf("Bus"));
        int numTaxi = getPlayerTickets(player, Ticket.valueOf("Taxi"));
        int numUnderground = getPlayerTickets(player, Ticket.valueOf("Underground"));
        List<Edge<Integer, Route>> validEdges = new ArrayList();
        for(Edge<Integer, Route> edge : possibleEdges)
        {
        	Ticket edgeType = Ticket.fromRoute(edge.data());
        	if(edgeType == Ticket.valueOf("Bus") && numBus > 0)
        	{
        		validEdges.add(edge);
        	}
        	else if(edgeType == Ticket.valueOf("Taxi") && numTaxi > 0)
        	{
        		validEdges.add(edge);
        	}
        	else if(edgeType == Ticket.valueOf("Underground") && numUnderground > 0)
        	{
        		validEdges.add(edge);
        	}
        }
        return validEdges;
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
