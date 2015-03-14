package solution;
import scotlandyard.*;
public class GameViewController {

	private ScotlandYardModel model;
	private GameView view;
	
	public GameViewController(ScotlandYardModel model, GameView view) {
		this.model = model;
		this.view = view;

		this.view.rsv.setGoButtonListener(new goButtonListener());
	}

	class goButtonListener implements ActionListener {
		public void actionPerformed(ActionEvent event) {
			//assuming detective for now...
			int target = view.rsv.getSingleMove();
			Ticket ticket = view.rsv.getSingleTicket();
			Colour colour = model.currentPlayer;
			MoveTicket move = new MoveTicket(colour,target,ticket);
			model.play(move);
		}
	}


	
}
