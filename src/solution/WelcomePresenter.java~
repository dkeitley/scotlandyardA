package solution;
import scotlandyard.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

class WelcomePresenter
{
	public static void main(String[] args)
	{
		WelcomeView view2 = new WelcomeView();
		SwingUtilities.invokeLater(view2::run);
		
		WelcomePresenter presenter = new WelcomePresenter(view2);
		 
	}
	
	WelcomeView view;
	
	public WelcomePresenter(WelcomeView view)
	{
		this.view = view;
		Listener listener = new Listener();
		view.addNewGameButtonListner(listener);
		view.addLoadGameButtonListner(listener);
		view.addQuitButtonListner(listener);
	}
	
	class Listener implements ActionListener
	{
		public void actionPerformed(ActionEvent event)
		{
			String action = event.getActionCommand();
			if(action.equals("Start a new game"))
			{
				System.out.println("start");
			}
			else if(action.equals("Load a saved game"))
			{
				System.out.println("load has been clicked");
			}
			else if(action.equals("Quit"))
			{
				System.exit(0);
			}
		}
	}
}
