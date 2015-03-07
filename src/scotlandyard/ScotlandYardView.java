package scotlandyard;

import java.util.List;
import java.util.Set;

/**
 * A ScotlandYardView represents a view on a Scotland Yard game.
 */
public interface ScotlandYardView {
  /**
   * A list of the players who are playing the game in the order of play.
   *
   * @return The list of players.
   */
  List<Colour> getPlayers();

  /**
   * Returns the colours of the winning players. If Mr X it should contain a single
   * colour, else it should send the list of detective colours
   *
   * @return A set containing the colours of the winning players
   */
  Set<Colour> getWinningPlayers();

  /**
   * The location of a player with a given colour in its last known location.
   *
   * @param colour The colour of the player whose location is requested.
   * @return The location of the player whose location is requested.
   * If Black, then this returns 0 if MrX has never been revealed,
   * otherwise returns the location of MrX in his last known location.
   * MrX is revealed in round n when {@code rounds.get(n)} is true.
   */
  int getPlayerLocation(Colour colour);

  /**
   * The number of a particular ticket that a player has.
   *
   * @param colour The colour of the player whose tickets are requested.
   * @param ticket The type of tickets that is being requested.
   * @return The number of tickets of the given player.
   */
  int getPlayerTickets(Colour colour, Ticket ticket);

  /**
   * The game is over when MrX has been found or the agents are out of tickets. See the rules for other conditions.
   *
   * @return true when the game is over, false otherwise.
   */
  boolean isGameOver();

  /**
   * A game is ready when all the required players have joined.
   *
   * @return true when the game is ready to be played, false otherwise.
   */
  boolean isReady();

  Colour getCurrentPlayer();

  /**
   * @return the number of moves MrX has played.
   */
  abstract int getRound();

  /**
   * A list whose length+1 is the maximum number of moves that MrX can play in a game.
   * The getRounds().get(n) is true when MrX reveals the target location of move n,
   * and is false otherwise.
   * Thus, if getRounds().get(0) is true, then the starting location of MrX is revealed.
   *
   * @return a list of booleans that indicate the turns where MrX reveals himself.
   */
  abstract List<Boolean> getRounds();
}
