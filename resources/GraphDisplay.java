import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Graphics2D;
import java.awt.image.*;
import java.io.*;
import java.text.*;
import java.util.*;

import javax.imageio.*;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;

public class GraphDisplay {

	public static void main(String[] args) {
		
		// load up the image and the graph
		BufferedImage img = null;
		try
		{
			img = ImageIO.read(new File("map.jpg"));
		}
		catch( IOException e )
		{
			System.out.println(e);
		}
		
		
		Reader reader = new Reader();
		
		try
		{
			reader.read(args[0]);
		}
		catch( IOException e )
		{
			System.out.println(e);
		}
		
		Graph graph = reader.graph();
		
		
		// read the input positions
		File file = new File("pos.txt");	
		Scanner in = null;
        try 
        {
			in = new Scanner(file);
		} 
        catch (FileNotFoundException e) 
        {
			System.out.println(e);
		}
        
        Map<String, List<Integer>> coordinateMap = new HashMap<String, List<Integer>>();
        
        // get the number of nodes
        String topLine = in.nextLine();
        int numberOfNodes = Integer.parseInt(topLine);
        
        
        for(int i = 0; i < numberOfNodes; i++)
        {
        	String line = in.nextLine();
       
        	String[] parts = line.split(" ");
        	List<Integer> pos = new ArrayList<Integer>();
        	pos.add(Integer.parseInt(parts[1]));
        	pos.add(Integer.parseInt(parts[2]));
        	
        	String key = parts[0];
        	coordinateMap.put(key, pos);
        }
        
        
        Graphics2D g2d = img.createGraphics();
        BasicStroke bs = new BasicStroke(4);
		g2d.setStroke(bs);
		
		// get the list of colors
		int opacity = 255;
		List<Color> colours = new ArrayList<Color>();
		colours.add(new Color(255, 0, 0, opacity));
		colours.add(new Color(0, 255, 0, opacity));
		colours.add(new Color(0, 0, 255, opacity));
		
		
		// sort the edges
		Set<Edge> edges = graph.edges();
		List<Edge> sortedEdges = new ArrayList<Edge>();
		Edge.EdgeType[] types = new Edge.EdgeType[3];
		types[0] = Edge.EdgeType.LocalRoad;
		types[1] = Edge.EdgeType.MainRoad;
		types[2] = Edge.EdgeType.Underground;
		
		for(int i = 0; i < 3; i++)
		{
			Edge.EdgeType t = types[i];
			for(Edge e : edges)
			{
				if(e.type() == t)
				{
					sortedEdges.add(e);
				}
			}
		}
		
        // draw the edges on the map
        for(Edge e : sortedEdges)
        {
        	Color c = colours.get(0);
        	switch(e.type())
        	{
        	case MainRoad :
        		c = colours.get(1);
        		break;
        	case LocalRoad :
        		c = colours.get(2);
        		break;
        	case Underground :
        		c = colours.get(0);
        		break;	
        	}
        	
        	List<Integer> p1 = coordinateMap.get(e.id1());
        	List<Integer> p2 = coordinateMap.get(e.id2());
        	
        	
        	
        	g2d.setColor(c);
        	g2d.drawLine(p1.get(0), p1.get(1), p2.get(0), p2.get(1));
        }
        
        
        
        
        
		
		
		
		
		
	
		
		
		
		
		

		ImageIcon icon = new ImageIcon(img);
		JFrame frame = new JFrame();
		frame.setLayout(new FlowLayout());
		frame.setSize(1018, 809);
		JLabel lbl = new JLabel();
		lbl.setIcon(icon);
		frame.add(lbl);
		frame.setVisible(true);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		
		// display the image
		

	}

}
