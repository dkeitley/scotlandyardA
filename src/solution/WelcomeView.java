package solution;
import scotlandyard.*;
import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;
import java.net.*;

class WelcomeView extends JFrame
{
	private JButton newGame;
	private JButton loadGame;
	private JButton quit;
	
	public WelcomeView()
	{	
		this.newGame = button("Start a new game");
		this.loadGame = button("Load a saved game");
		this.quit = button("Quit");
	}
	
	void run()
	{
    		setDefaultCloseOperation(EXIT_ON_CLOSE);
		add(display());
		pack();
		setLocationByPlatform(true);
		setVisible(true);	
	}
	
	private Box display()
	{
		Box box = Box.createHorizontalBox();
		box.add(graphic());
		Box display = Box.createVerticalBox();
		display.add(box);
		display.add(buttons());
		
		return display;
	}
	
	private JLabel graphic()
	{
		URL u = this.getClass().getResource("graphic.jpg");
   	 	ImageIcon icon = new ImageIcon(u);
   	 	return new JLabel(icon);
	} 
	
	private JPanel buttons()
	{
		JPanel buttons = new JPanel();
		GridLayout grid = new GridLayout(3,1, 10, 10);
		buttons.setLayout(grid);
		Border border = BorderFactory.createEmptyBorder(10, 10, 10, 10);
		buttons.setBorder(border);
		buttons.add(this.newGame);
		buttons.add(this.loadGame);
		buttons.add(this.quit);
		return buttons;
	}
	
	private JButton button(String buttonText)
	{
		Font font = new Font("TimesRoman", Font.PLAIN, 20);
		JButton button = new JButton(buttonText);
		button.setFont(font);
		button.setActionCommand(buttonText);
		return button; 
	}
	
	public void addNewGameButtonListner(ActionListener listener)
	{
		this.newGame.addActionListener(listener);
	}
	
	public void addLoadGameButtonListner(ActionListener listener)
	{
		this.loadGame.addActionListener(listener);
	}
	
	public void addQuitButtonListner(ActionListener listener)
	{
		this.quit.addActionListener(listener);
	}

	public void displayErrorMessage(String errorMessage) 
	{
		JOptionPane.showMessageDialog(this, errorMessage);
	}
}















