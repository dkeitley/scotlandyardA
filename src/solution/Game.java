package solution;
import scotlandyard.*;

class Game
{
	public static void main(String args[])
	{
		WelcomeView view = new WelcomeView();
		WelcomePresenter presenter = new WelcomePresenter(view);
		view.run();
	}
}
