package solution;
import scotlandyard.*;
import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;
import java.net.*;

class PlayerBox extends JPanel
{
	private JComboBox colourOptions;
	private JTextField numTaxi;
	private JTextField numBus;
	private JTextField numDouble;
	private JTextField numSecret;
	private JTextField numUnderground;
	private boolean mrX;
	
	public PlayerBox(boolean playIsMrX)
	{
		this.numTaxi = new JTextField("11");
		this.numBus = new JTextField("8");
		this.numUnderground = new JTextField("4");
		this.numDouble = new JTextField();
		this.numSecret = new JTextField();
		this.mrX = playIsMrX;
		this.setBackground(new Color(169, 169, 169));
		//this.add(graphic());
		this.add(options());
	}
	
	/*private JLabel graphic()
	{
		URL u = this.getClass().getResource("testImage.png");
   	 	ImageIcon icon = new ImageIcon(u);
   	 	return new JLabel(icon);
	}*/
	
	private JPanel options()
	{
		JPanel options = new JPanel();
		options.setLayout(new GridLayout(0, 2, 10, 10));
		options.setBackground(new Color(169, 169, 169));
        addColourOptions(options);
        addTicketOptions(options);
        return options;
	}
	
	private void addColourOptions(JPanel options)
	{
		options.add(new JLabel("Colour of player: "));
        options.add(new JLabel(""));
        options.add(new JLabel("Colour: ", SwingConstants.RIGHT));
		if(mrX)
		{
			Colour[] colourOptions = {Colour.Black};
			this.colourOptions = new JComboBox(colourOptions);
		}
		else
		{
			this.colourOptions = new JComboBox(Colour.values());
		}
		options.add(this.colourOptions);
		return;
	}
	
	private void addTicketOptions(JPanel options)
	{
		options.add(new JLabel("Number of tickets: "));
        options.add(new JLabel(""));
		options.add(new JLabel("Taxi: ", SwingConstants.RIGHT));
        options.add(this.numTaxi);
        options.add(new JLabel("Bus: ", SwingConstants.RIGHT));
        options.add(this.numBus);
        options.add(new JLabel("Underground: ", SwingConstants.RIGHT));
        options.add(this.numUnderground);
        if(mrX)
        {
		    options.add(new JLabel("Double Moves: ", SwingConstants.RIGHT));
		    options.add(this.numDouble);
		    options.add(new JLabel("Secret Moves: ", SwingConstants.RIGHT));
		    options.add(this.numSecret);
        }
        return;
	}
	
	public Colour getColour()
	{
		return (Colour) colourOptions.getSelectedItem();
	}
	
	public String getNumTaxi()
	{
		return numTaxi.getText();
	}
	
	public String getNumBus()
	{
		return numBus.getText();
	}
	
	public String getNumUnderground()
	{
		return numUnderground.getText();
	}
	
	public String getNumDouble()
	{
		return numDouble.getText();
	}
	
	public String getNumSecret()
	{
		return numSecret.getText();
	}
}





