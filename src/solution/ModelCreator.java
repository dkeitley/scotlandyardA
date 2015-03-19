package solution;
import scotlandyard.*;
import java.util.*;

class ModelCreator
{	
	public ScotlandYardModel getModel()
	{
		//number of detectives; show rounds as a comma seperated list
		ScotlandYardModel model = createModel(2, "2,5,8");
		//are you mrX?; numTaxi; numBus; numUnderground; numDouble; numSecret
		Map<Ticket, Integer> ticketMapMrX = createTicketMap(true, 5,4,8,1,3);
		Map<Ticket, Integer> ticketMapDetective1 = createTicketMap(false, 7,2,6,0,0);
		Map<Ticket, Integer> ticketMapDetective2 = createTicketMap(false, 1,2,3,0,0);
		// player; colour; start location; ticket map
		model.join(new PlayerImplementation(), Colour.Black, 1, ticketMapMrX);
		model.join(new PlayerImplementation(), Colour.Red, 11, ticketMapDetective1);
		model.join(new PlayerImplementation(), Colour.Blue, 21, ticketMapDetective2);
		
		int numTurns = 6;
		for(int i = 0; i < numTurns; i++)
		{
			List<Move> moves = model.validMoves(model.getCurrentPlayer());
			Move move = moves.get(0);
			if (move instanceof MoveTicket) model.play((MoveTicket) move);
    		if (move instanceof MoveDouble) model.play((MoveDouble) move);
    		if (move instanceof MovePass) model.play((MovePass) move);
			model.nextPlayer();
		}
		
		return model;
	}
		
	
	private static Map<Ticket, Integer> createTicketMap(boolean mrX, int taxi, int bus, int underground, int doubleMoves, int secret)
	{
		Map<Ticket, Integer> tickets = new HashMap();
		tickets.put(Ticket.Taxi, taxi);
		tickets.put(Ticket.Bus, bus);
		tickets.put(Ticket.Underground, underground);
		if(mrX)
		{
			tickets.put(Ticket.DoubleMove, doubleMoves);
			tickets.put(Ticket.SecretMove, secret);
		}
		return tickets;
	}
	
	private static ScotlandYardModel createModel(int numDetectives, String showRounds)
	{
		 List<Boolean> rounds = createShowRoundsList(showRounds);
		 try
		 {
		 	return new ScotlandYardModel(numDetectives, rounds, "../resources/graph.txt"); 
		 }
		 catch(Exception e) 
		 {
		 	return null;
		 }
	}
	
	private static List<Boolean> createShowRoundsList(String showRounds)
	{
		String[] rounds = showRounds.split(" , | ,|, |,");
		Boolean[] roundsList = new Boolean[11]; // 10 rounds 
		Arrays.fill(roundsList, false);
		for(String round : rounds)
		{
			int showRound = Integer.parseInt(round);
			roundsList[showRound] = true;
		}
		return Arrays.asList(roundsList);
	}
}
