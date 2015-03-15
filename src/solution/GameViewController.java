package solution;
import scotlandyard.*;
import java.awt.*;
import java.awt.event.*;
public class GameViewController {

	private ScotlandYardModel model;
	private GameView view;
	
	public GameViewController(ScotlandYardModel model, GameView view) {
		this.model = model;
		this.view = view;
		this.view.rsv.addGoButtonListener(new goButtonListener());
		java.util.List<Move> initialSingleMoves = this.model.validMoves(Colour.Black);
		//System.err.println(initialSingleMoves);
		this.view.rsv.setSingleMoveBox(initialSingleMoves);
	}


	class goButtonListener implements ActionListener {
		public void actionPerformed(ActionEvent event) {
			//assuming detective for now...
			int target = view.rsv.getSingleMove();
			Ticket ticket = view.rsv.getSingleTicket();
			Colour colour = model.getCurrentPlayer();
			MoveTicket move = new MoveTicket(colour,target,ticket);
			model.play(move);
		}
	}


	
}
