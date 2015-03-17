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
		SwingUtilities.invokeLater(view2::run);
		
		GameSetupPresenter presenter = new GameSetupPresenter(view2);
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
				view.addPlayerBox();
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
					view.displayErrorMessage("Oops. looks like you entered something wrong. Please try again");
				}
				
			}
		}
	}
	
	private int getStartLocation(Colour colour)
	{
		int[] mrXPossibleLocations = {1,2,3,4,5,6};
		int[] detectivePossibleLocations = {7,8,9,10,11,12,13,14,15};
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
		}
		if(!validateInt(view.getNumRounds())) return false;
		String[] rounds = view.getShowRounds().split(" , | ,|, |,");
		for(String round : rounds)
		{
			if(!validateInt(round)) return false;
		}
		return true;
	}
	
	private boolean validateInt(String num)
	{
		try
		{
			Integer.parseInt(num);
			return true;
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























