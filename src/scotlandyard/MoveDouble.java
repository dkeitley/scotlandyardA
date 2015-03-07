package scotlandyard;

import java.util.ArrayList;
import java.util.List;

public class MoveDouble extends Move {
  public final List<Move> moves;

  public MoveDouble(Colour player, Move move1, Move move2) {
    super(player);
    moves = new ArrayList<Move>();
    moves.add(move1);
    moves.add(move2);
  }


  @Override
  public boolean equals(Object obj) {
    if (obj instanceof MoveDouble) {

      MoveDouble that = (MoveDouble) obj;

      if (!colour.equals(that.colour))
        return false;

      boolean same = true;
      for (int i = 0; i < moves.size(); i++) {
        if (!moves.get(i).equals(that.moves.get(i))) {
          same = false;
          break;
        }
      }
      return same;
    }
    return false;
  }



    @Override
  public String toString() {
    if (moves.size() == 2) {
      MoveTicket m1 = (MoveTicket) moves.get(0);
      MoveTicket m2 = (MoveTicket) moves.get(1);
      return "Move Double " + super.toString() + ": " + m1.target + " " + m1.ticket + " -> " + m2.target + " " + m2.ticket;
    } else {
//            return "Move Double " + super.toString();
      return " Hi ";
    }
  }
}
