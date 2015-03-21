package solution;
import scotlandyard.*;
import java.net.*;
import java.nio.file.*;
import java.util.*;
import java.io.*;

class ModelReader
{
	//for testing purposes
	public static void main(String[] args)
	{
		ModelReader reader = new ModelReader();
		//reader.getModel("gameSave.txt");
	}	
	
	//converts file at fileName to a model
	public ScotlandYardModel getModel(String fileName) throws Exception
	{
		List<String> lines = read(fileName);
		List<Colour> orderOfPlay = getOrderOfPlay(lines.get(0));
		List<Boolean> rounds = buildShowRounds(lines.get(orderOfPlay.size() + 2));
		int numDetectives = orderOfPlay.size() - 1;		
		ScotlandYardModel model = new ScotlandYardModel(numDetectives, rounds, "../resources/graph.txt");
		for(int i = 1; i <= orderOfPlay.size(); i++)
		{
			joinPlayer(model, lines.get(i));
		}
		playMoves(lines, model, orderOfPlay);
		return model;	
	}
	
	//plays the moves that have been played already
	private void playMoves(List<String> lines, ScotlandYardModel model, List<Colour> orderOfPlay)
	{
		String[] goes = lines.get(orderOfPlay.size() + 3).split(",");
		int numberGoes = goes.length;
		int i = 0;
		while(i < numberGoes)
		{
			String[] go = goes[i].split(" ");
			if(go[1].equals("Double"))
			{
				MoveTicket move1 = buildMoveTicket(go[2].substring(0, go[2].length() - 1), go[3], go[4]);
				MoveTicket move2 = buildMoveTicket(go[2].substring(0, go[2].length() - 1), go[6], go[7]);
				model.play( new MoveDouble(Colour.valueOf(go[2].substring(0, go[2].length() - 1)), move1, move2));
				model.nextPlayer();
				i += 2;
			}
			else if(go[1].equals("Pass"))
			{
				model.play( new MovePass(Colour.valueOf(go[2])));
				model.nextPlayer();
			}
			else
			{
				model.play(buildMoveTicket(go[0], go[1], go[2]));
				model.nextPlayer();
			}
			i++;
		}
		return;
	}
	
	//joins a player to the game based on a line of text giving player info
	private void joinPlayer(ScotlandYardModel model, String line)
	{
		String[] strings = line.split(",");
		Colour colour = Colour.valueOf(strings[0]);
		int location = Integer.parseInt(strings[1]);
		Map<Ticket, Integer> tickets = getPlayerTicketMap(line);
		model.join(new PlayerImplementation(), colour, location, tickets);
		return;
	} 
	
	//reads in a file 
	private List<String> read(String fileName) throws Exception
	{
		List<String> lines;
		Path path = Paths.get(fileName);
		lines = Files.readAllLines(path);
		return lines;
	}
	
	//creates the ticket map for a player based on string from savefile
	private Map<Colour,Integer> getLocationMap(List<String> lines)
	{
		Map<Colour,Integer> locations = new HashMap();
		for(String line : lines)
		{
			String[] strings = line.split(",");
			Colour colour = Colour.valueOf(strings[0]);
			int location = Integer.parseInt(strings[1]);
			locations.put(colour, location);
		}
		return locations;
	}
	
	private List<Boolean> buildShowRounds(String line)
	{
		List<Boolean> showRounds = new ArrayList();
		String[] strings = line.split(",");
		for(String bool : strings)
		{
			if(bool.equals("true")) showRounds.add(true);
			else showRounds.add(false);
		}
		return showRounds;
	}
	
	private MoveTicket buildMoveTicket(String colour, String target, String ticket)
	{
		return new MoveTicket(Colour.valueOf(colour), Integer.parseInt(target), Ticket.valueOf(ticket));
	}
	
	//creates list of colours based on string
	private List<Colour> getOrderOfPlay(String line)
	{
		String[] colourStrings = line.split(",");
		List<Colour> orderOfPlay = new ArrayList();
		for(String colour : colourStrings)
		{
			orderOfPlay.add(Colour.valueOf(colour));
		}
		return orderOfPlay;
	}
	
	//reads in text and converst to call for ticket map
	private Map<Ticket, Integer> getPlayerTicketMap(String line)
	{
		String[] strings = line.split(",");
		boolean isBlack =false;
		if(strings[0].equals("Black")) isBlack = true;
		int taxi = Integer.parseInt(strings[2]);
		int bus = Integer.parseInt(strings[3]);
		int underground = Integer.parseInt(strings[4]);
		int doubleMove = 0, secret = 0;
		if(isBlack)
		{
			doubleMove = Integer.parseInt(strings[5]);
			secret = Integer.parseInt(strings[6]);
		}
		return createTicketMap(isBlack, taxi, bus, underground, doubleMove, secret);
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
}
























































