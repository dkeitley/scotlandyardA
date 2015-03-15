import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.net.*;

class WelcomeView
{
	public static void main(String[] args )
	{
		WelcomeView program = new WelcomeView();
		SwingUtilities.invokeLater(program::run);
	}
	
	void run()
	{
		JFrame w = new JFrame();
    	w.setDefaultCloseOperation(w.EXIT_ON_CLOSE);
		w.add(display());
		w.pack();
		w.setLocationByPlatform(true);
		w.setVisible(true);	
	}
	
	private Box display()
	{
		Box display = Box.createVerticalBox();
		display.add(graphic());
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
		buttons.add(button("Start a new game"));
		buttons.add(button("Load a saved game"));
		buttons.add(button("Quit"));
		return buttons;
	}
	
	private JButton button(String buttonText)
	{
		Font font = new Font("TimesRoman", Font.PLAIN, 20);
		JButton button = new JButton(buttonText);
		button.setFont(font);
		return button; 
	}
}
