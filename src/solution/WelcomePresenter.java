package solution;
import scotlandyard.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

class WelcomePresenter
{
	//for testing 
	/*public static void main(String[] args)
	{
		WelcomeView view2 = new WelcomeView();
		SwingUtilities.invokeLater(view2::run);
		WelcomePresenter presenter = new WelcomePresenter(view2);
	}*/
	
	private WelcomeView view;
	
	//adds listners
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
		//handles startup buttons
		public void actionPerformed(ActionEvent event)
		{
			String action = event.getActionCommand();
			if(action.equals("Start a new game"))
			{
				GameSetupView setupView = new GameSetupView();
				GameSetupPresenter presenter = new GameSetupPresenter(setupView);
				setupView.run();
				view.dispose();
			}
			else if(action.equals("Load a saved game"))
			{
				try {
					ModelReader reader = new ModelReader();
					ScotlandYardModel model = reader.getModel("gameSave.txt");
					GameView gameView = new GameView();
					GameViewController controller = new GameViewController(model, gameView);
					view.dispose();
					controller.run();
				} catch(Exception e) {
					view.displayErrorMessage("Oops, no games saved. Please start a new game.");
				}
			}
			else if(action.equals("Quit"))
			{
				System.exit(0);
			}
		}
	}
}

