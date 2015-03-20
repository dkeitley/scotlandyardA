package solution;
import scotlandyard.*;
import java.net.*;
import java.nio.file.*;
import java.util.*;
import java.io.*;

class ModelReader
{
	public static void main(String[] args)
	{
		ModelReader reader = new ModelReader();
		reader.getModel("test.txt");
	}	
	
	public void getModel(String fileName)
	{
		List<String> lines = read(fileName);
		
	}
	
	private List<String> read(String fileName)
	{
		List<String> lines;
		try
		{
			Path path = Paths.get(fileName);
			lines = Files.readAllLines(path);
		}
		catch(Exception e)
		{
			throw new Error(e);
		}
		return lines;
	}
	
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
	
	private getPlayerTicketMap()
	{
	
	}
}































