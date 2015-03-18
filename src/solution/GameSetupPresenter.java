package solution;
import scotlandyard.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.*;
import java.util.List;
import java.lang.*;

class GameSetupPresenter
{
	public static void main(String[] args)
	{
		GameSetupView view2 = new GameSetupView();
		GameSetupPresenter presenter = new GameSetupPresenter(view2);
		view2.run();
	}
	
	private GameSetupView view;
	private List<Integer> OcupiedLocations = new ArrayList();
	
	public GameSetupPresenter(GameSetupView view)
	{
		this.view = view;
		this.view.addAddPlayerButtonListner(new Listener());
		this.view.addStartGameButtonListner(new Listener());
	}
	
	class Listener implements ActionListener
	{
		public void actionPerformed(ActionEvent event)
		{
			String action = event.getActionCommand();
			if(action.equals("addPlayer"))
			{
				if(view.getPlayerBoxes().size() < 6)
				{
					view.addPlayerBox();
				}
				else
				{
					view.displayErrorMessage("You can have a maximum of 6 players");
				}
			}
			else if(action.equals("startGame"))
			{
				if(validateInput())
				{
					ScotlandYardModel model = createModel(view.getShowRounds());
					for (PlayerBox box : view.getPlayerBoxes())
					{
						Colour colour = box.getColour();
						Map<Ticket, Integer> ticketMap = createTicketMap(box);
						int startingLocation = getStartLocation(colour);
						model.join(new PlayerImplementation(), colour, startingLocation, ticketMap);
					}
				}
				else
				{
					view.displayErrorMessage("Oops. Looks like you entered something wrong. Please try again");
				}
				
			}
		}
	}
	
	private int getStartLocation(Colour colour)
	{
		int[] mrXPossibleLocations = {146,166,51,104,127,106,45,172,35,132,78,170,71};
		int[] detectivePossibleLocations = {155,50,103,94,26,29,91,141,53,138,174,13,112,117,34};
		int[] possibleLocations;
		if(colour.equals(Colour.Black)) possibleLocations =  mrXPossibleLocations;
		else possibleLocations = detectivePossibleLocations;
		boolean locationOccupied = true;
		int location = 0;
		while(locationOccupied)
		{
			int randNum = (int) Math.floor(Math.random() * possibleLocations.length );
			location = possibleLocations[randNum];
			if(!OcupiedLocations.contains(location))
			{
				locationOccupied = false;
				OcupiedLocations.add(location);
			}
		}
		return location;
	}
	
	private Map<Ticket, Integer> createTicketMap(PlayerBox box)
	{
		Map<Ticket, Integer> tickets = new HashMap();
		tickets.put(Ticket.Taxi, Integer.parseInt(box.getNumTaxi()));
		tickets.put(Ticket.Bus, Integer.parseInt(box.getNumBus()));
		tickets.put(Ticket.Underground, Integer.parseInt(box.getNumUnderground()));
		if(box.getColour().equals(Colour.Black))
		{
			tickets.put(Ticket.DoubleMove, Integer.parseInt(box.getNumDouble()));
			tickets.put(Ticket.SecretMove, Integer.parseInt(box.getNumSecret()));
		}
		return tickets;
	}
	
	private boolean validateInput()
	{
		Set<Colour> colours = new HashSet();
		for (PlayerBox box : view.getPlayerBoxes())
		{
			if(!validateInt(box.getNumTaxi())) return false;
			if(!validateInt(box.getNumBus())) return false;
			if(!validateInt(box.getNumUnderground())) return false;
			if(box.getColour().equals(Colour.Black))
			{
				if(!validateInt(box.getNumDouble())) return false;
				if(!validateInt(box.getNumSecret())) return false;
			}
			if(colours.contains(box.getColour())) return false;
			else colours.add(box.getColour());
		}
		if(!validateInt(view.getNumRounds())) return false;
		String[] rounds = view.getShowRounds().split(" , | ,|, |,");
		int maxShowRound = 0;
		for(String round : rounds)
		{
			if(!validateInt(round)) return false;
			else if(maxShowRound < Integer.parseInt(round)) maxShowRound = Integer.parseInt(round);
		}
		if(Integer.parseInt(view.getNumRounds()) < maxShowRound ) return false;
		return true;
	}
	
	private boolean validateInt(String num)
	{
		try
		{
			if(Integer.parseInt(num) >= 0) return true;
			else return false;
		}
		catch(Exception e)
		{
			return false;
		}
	}
	
	private ScotlandYardModel createModel(String showRounds)
	{
		 int numDetctives = view.getPlayerBoxes().size() -1;
		 java.util.List<Boolean> rounds = createShowRoundsList(showRounds);
		 try
		 {
		 	return new ScotlandYardModel(numDetctives, rounds, "../resources/graph.txt"); 
		 }
		 catch(Exception e) 
		 {
		 	return null;
		 }
	}
	
	private java.util.List<Boolean> createShowRoundsList(String showRounds)
	{
		String[] rounds = showRounds.split(" , | ,|, |,");
		Boolean[] roundsList = new Boolean[Integer.parseInt(view.getNumRounds()) + 1];
		Arrays.fill(roundsList, false);
		for(String round : rounds)
		{
			int showRound = Integer.parseInt(round);
			roundsList[showRound] = true;
		}
		return Arrays.asList(roundsList);
	}
}























