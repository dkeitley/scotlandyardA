package solution;
import scotlandyard.*;
import java.net.*;
import java.nio.file.*;
import java.util.*;
import java.io.*;

class ModelSaver
{	
	private ScotlandYardModel model;
	
	public ModelSaver(ScotlandYardModel model)
	{
		this.model = model;
	}
	
	//compiles all lines and calles save on created string
	public void save(String fileName) throws IOException
	{
		try {
			File file = new File(fileName);
			file.delete();
		} catch (Exception e) {};

		String modelString = "";
		modelString += orderOfPlayToString(model) + "\n";
		for(Colour player : model.getPlayers())
		{
			modelString += playerToString(player, model) + "\n";
		}
		modelString += model.getCurrentPlayer() + "\n";
		modelString += showRoundsToString(model) + "\n";
		modelString += movesToString(model);
		writeToFile(fileName, modelString);
		return;
	}
	
	//prints line of all moves made in game so far
	private String movesToString(ScotlandYardModel model)
	{
		String moves = "";
		for(Move move : model.movesMade())
		{
			String moveString = "";
			if (move instanceof MoveTicket) moveString = ((MoveTicket) move).toString();
   			if (move instanceof MoveDouble) moveString =  ((MoveDouble) move).toString();
    		if (move instanceof MovePass) moveString =  ((MovePass) move).toString();
			
			moves = moves + moveString  + ",";
		}
		if(model.movesMade().size() == 0) return "";
		return moves.substring(0, moves.length() - 1);
	}
	
	private String showRoundsToString(ScotlandYardModel model)
	{
		String rounds = "";
		for(boolean bol :model.getRounds())
		{
			rounds = rounds + bol + ",";
		}
		return rounds.substring(0, rounds.length() - 1);
	}
	
	private String orderOfPlayToString(ScotlandYardModel model)
	{
		String players = "";
		List<Colour> orderOfPlay = model.getPlayers();
		for(Colour player : orderOfPlay)
		{
			players = players + player.toString() + ",";
		}
		players = players.substring(0, players.length() - 1);
		return players;
	}
	
	//converts player atributed (tickets, colous location etc) to string
	private String playerToString(Colour player, ScotlandYardModel model)
	{
		int location = 0, doubleMoves = 0, secret = 0, lastKnownLocation = 0;
		int bus, taxi, underground;
		taxi = model.getPlayerOriginalTickets(player, Ticket.Taxi);
		bus = model.getPlayerOriginalTickets(player, Ticket.Bus);
		underground = model.getPlayerOriginalTickets(player, Ticket.Underground);
		if(player.equals(Colour.Black))
		{
			location = model.getMrXLocation();
			doubleMoves =  model.getPlayerOriginalTickets(Colour.Black, Ticket.DoubleMove);
			secret = model.getPlayerOriginalTickets(Colour.Black, Ticket.SecretMove);
			lastKnownLocation = model.getPlayerLocation(Colour.Black);
		}
		else location = model.getPlayerLocation(player);
		String playerString = player.toString() + "," + location + ",";
		playerString = playerString + taxi  + "," + bus + "," + underground;
		if(player.equals(Colour.Black))
		{
			playerString = playerString + "," + doubleMoves + "," + secret + "," + lastKnownLocation;
		}
		return playerString;
	} 
	
	private void writeToFile(String fileName, String text) throws IOException
	{
    		Files.write(Paths.get(fileName), text.getBytes(), StandardOpenOption.CREATE);
  	}
}

