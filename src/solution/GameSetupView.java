package solution;
import scotlandyard.*;
import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;
import java.net.*;

class GameSetupView extends JFrame
{
	
	private JButton addPlayer;
	private JButton startGame;
	private JTextField numRounds;
	private JTextField showRounds;
	private Box players;
	private JScrollPane scrollable;
	private java.util.List<PlayerBox> playerBoxes;
	private JFrame w;
	 
	public GameSetupView()
	{
		addPlayer = new JButton("Add player");
		startGame = new JButton("Start Game");
		this.numRounds = new JTextField();
		this.showRounds = new JTextField();
		this.players = Box.createVerticalBox();
		this.playerBoxes = new java.util.ArrayList();
	}
	
	public void run()
	{
		setBackground(new Color(255, 255, 255));
    	setDefaultCloseOperation(EXIT_ON_CLOSE);
		this.scrollable = display();
		add(this.scrollable);
		pack();
		setLocationByPlatform(true);
		setVisible(true);
	}
	
	private JScrollPane display()
	{
		Box display = Box.createVerticalBox();
		display.add(players());
		display.add(addPlayersButton());
		display.add(gameOptions());
		display.add(addStartGameButton());
		return new JScrollPane(display);
	}
	
	private Box players()
	{
		PlayerBox mrX = new PlayerBox(true);
		this.playerBoxes.add(mrX);
		this.players.add(mrX);
		this.players.add(Box.createRigidArea(new Dimension(10, 5)));
		PlayerBox detective = new PlayerBox(false);
		this.players.add(detective);
		this.playerBoxes.add(detective);
		return players;
	}
	
	private JButton addPlayersButton()
	{
		this.addPlayer.setActionCommand("addPlayer");
		return addPlayer;
	}
	
	private JPanel gameOptions()
	{
		JPanel options = new JPanel();
		options.setLayout(new GridLayout(0, 2, 10, 10));
		options.add(new JLabel("Game Options: "));
		options.add(new JLabel(""));
		options.add(new JLabel("Number of rounds: ", SwingConstants.RIGHT));
		options.add(numRounds);
		options.add(new JLabel("MrX reveal rounds (enter as comma sperated list): ", SwingConstants.RIGHT));
		options.add(showRounds);
		return options;
	}
	
	private JButton addStartGameButton()
	{
		this.startGame.setActionCommand("startGame");
		return startGame;
	}
	
	public void addStartGameButtonListner(ActionListener listener)
	{
		this.startGame.addActionListener(listener);
		return;
	}
	
	public void addAddPlayerButtonListner(ActionListener listener)
	{
		this.addPlayer.addActionListener(listener);
		return;
	}
	
	public void addPlayerBox()
	{
		this.players.add(Box.createRigidArea(new Dimension(10, 5)));
		PlayerBox detective = new PlayerBox(false);
		this.players.add(detective);
		this.playerBoxes.add(detective);
		this.players.validate();
        this.players.repaint();
        this.scrollable.validate();
        this.scrollable.repaint();
	}
	
	public  java.util.List<PlayerBox> getPlayerBoxes()
	{
		return playerBoxes;
	}
	
	public String getNumRounds()
	{
		return numRounds.getText();
	}
	
	public String getShowRounds()
	{
		return showRounds.getText();
	}	
	
	public void displayErrorMessage(String errorMessage) 
	{
		JOptionPane.showMessageDialog(this, errorMessage);
	}	
}



















