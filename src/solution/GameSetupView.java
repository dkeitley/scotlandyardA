package solution;
import scotlandyard.*;
import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;
import java.net.*;

class GameSetupView
{
	
	private JButton addPlayer;
	 
	public GameSetupView()
	{
		addPlayer = new JButton("Add player");
	}
	
	public void run()
	{
		JFrame w = new JFrame();
		w.setBackground(new Color(255, 255, 255));
    	w.setDefaultCloseOperation(w.EXIT_ON_CLOSE);
		w.add(display());
		w.pack();
		w.setLocationByPlatform(true);
		w.setVisible(true);
	}
	
	private Box display()
	{
		Box display = Box.createVerticalBox();
		display.add(players());
		display.add(addPlayersButton());
		return display;
	}
	
	private Box players()
	{
		PlayerBox mrX = new PlayerBox(true);
		PlayerBox detective = new PlayerBox(false);
		Box players = Box.createVerticalBox();
		players.add(mrX);
		players.add(Box.createRigidArea(new Dimension(10, 5)));
		players.add(detective);
		return players;
	}
	
	private JButton addPlayersButton()
	{
		this.addPlayer.setActionCommand("addPlayer");
		return addPlayer;
	}
	
	public void addAddPlayerButtonListner(ActionListener listener)
	{
		this.addPlayer.addActionListener(listener);
	} 	
}



















