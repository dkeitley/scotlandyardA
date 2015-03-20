package solution;
import scotlandyard.*;
import java.net.*;
import java.nio.file.*;
import java.util.*;
import java.io.*;

class ModelSaver
{
	/*public static void main(String[] args) throws IOException
	{
		ModelCreator creator = new ModelCreator();
		ScotlandYardModel model2 = creator.getModel(); 
		ModelSaver saver = new ModelSaver(model2);
		saver.save("test.txt");
	}*/
	
	private ScotlandYardModel model;
	
	public ModelSaver(ScotlandYardModel model)
	{
		this.model = model;
	}
	
	public void save(String fileName) throws IOException
	{
		String modelString = "";
		modelString += orderOfPlayToString(model) + "\n";
		for(Colour player : model.getPlayers())
		{
			modelString += playerToString(player, model) + "\n";
		}
		modelString += model.getCurrentPlayer() + "\n";
		modelString += model.getRound() + "\n";
		modelString += showRoundsToString(model) + "\n";
		modelString += mrXMovesToString(model);
		writeToFile(fileName, modelString);
		return;
	}
	
	private String mrXMovesToString(ScotlandYardModel model)
	{
		String moves = "";
		int numRounds = model.getRound();
		for(int i = 1; i <= numRounds; i++)
		{
			moves = moves + model.mrXMoves(i).toString() + ",";
		}
		if(numRounds == 0) return "";
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
	
	private String playerToString(Colour player, ScotlandYardModel model)
	{
		int location = 0, doubleMoves = 0, secret = 0, lastKnownLocation = 0;
		int bus, taxi, underground;
		taxi = model.getPlayerTickets(player, Ticket.Taxi);
		bus = model.getPlayerTickets(player, Ticket.Bus);
		underground = model.getPlayerTickets(player, Ticket.Underground);
		if(player.equals(Colour.Black))
		{
			location = model.getMrXLocation();
			doubleMoves =  model.getPlayerTickets(Colour.Black, Ticket.DoubleMove);
			secret = model.getPlayerTickets(Colour.Black, Ticket.SecretMove);
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




























