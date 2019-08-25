package solution;

import scotlandyard.*;
import java.util.*;

class PlayerImplementation implements Player {
	private GameViewController presenter = null;
	
	public Move notify(int location, List<Move> list) {
		return presenter.getPlayerMove(list);
	}
	
	public void setPresenter(GameViewController presenter) {
		this.presenter = presenter;
	}
}
