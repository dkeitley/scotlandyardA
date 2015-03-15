package solution;
import scotlandyard.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

class GameSetupPresenter
{
	public static void main(String[] args)
	{
		GameSetupView view2 = new GameSetupView();
		SwingUtilities.invokeLater(view2::run);
		
		GameSetupPresenter presenter = new GameSetupPresenter(view2);
	}
	
	private GameSetupView view;
	
	public GameSetupPresenter(GameSetupView view)
	{
		this.view = view;
		this.view.addAddPlayerButtonListner(new Listener());
	}
	
	class Listener implements ActionListener
	{
		public void actionPerformed(ActionEvent event)
		{
			String action = event.getActionCommand();
			if(action.equals("addPlayer"))
			{
				System.out.println("addPlayer");
			}
		}
	}
}
