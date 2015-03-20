package scotlandyard;

import java.util.List;
import java.util.Map;

public abstract class ScotlandYard implements ScotlandYardView {
  /**
   * The constructor for a Scotland Yard game.
   *
   * @param numberOfDetectives the number of detectives playing.
   * @param rounds             a list of booleans whose length+1 is the number of moves Mr X will make. See {@link scotlandyard.ScotlandYardView#getRounds()} for more details.
   * @param graphFileName      location of the graph file name.
   */
  public ScotlandYard(int numberOfDetectives, List<Boolean> rounds, String graphFileName) {
  }

  /**
   * Starts playing the game.
   */
  final public void start() {
    while (isReady() && !isGameOver()) {
      turn();
    }
  }

  /**
   * Performs a turn by asking the current player for their move,
   * playing that move, and then passing priority to the next player.
   */
  final public void turn() {
    Move chosenMove = getPlayerMove(getCurrentPlayer());
    play(chosenMove);
    nextPlayer();
  }

  /**
   * Retrieves the move that the player associated to a given colour wants to make.
   *
   * @param colour the colour of the player whose turn it is.
   * @return the move they want to make.
   */
  abstract protected Move getPlayerMove(Colour colour);

  /**
   * Passes priority onto the next player whose turn it is to play.
   */
  abstract protected void nextPlayer();


  /**
   * Allows the game to play a given move.
   *
   * @param move the move that is to be played.
   */
  final protected void play(Move move) {
    if (move instanceof MoveTicket) play((MoveTicket) move);
    if (move instanceof MoveDouble) play((MoveDouble) move);
    if (move instanceof MovePass) play((MovePass) move);
  }

  abstract protected void play(MoveTicket move);

  abstract protected void play(MoveDouble move);

  abstract protected void play(MovePass move);

  /**
   * Produces the list of valid moves for a given player when it is their turn.
   *
   * @param player the player whose moves we want to see.
   * @return
   */
  abstract protected List<Move> validMoves(Colour player);

  /**
   * Allows spectators to join the game. They can only observe as if they
   * were a detective: only MrX's revealed locations can be seen.
   *
   * @param spectator the spectator that wants to be notified when a move is made.
   */
  abstract public void spectate(Spectator spectator);

  /**
   * Allows players to join the game with a given starting state. When the
   * last player has joined, the game must ensure that the first player to play is Mr X.
   *
   * @param player   player that wants to be notified when he must make moves
   * @param colour   colour of the player
   * @param location starting location of the player
   * @param tickets  starting tickets for that player
   * @return
   */
  abstract public boolean join(Player player, Colour colour, int location, Map<Ticket, Integer> tickets);
}
