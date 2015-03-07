package solution;

import scotlandyard.*;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class ScotlandYardModel extends ScotlandYard {


    public ScotlandYardModel(int numberOfDetectives, List<Boolean> rounds, String graphFileName) throws IOException {
        super(numberOfDetectives, rounds, graphFileName);
    }

    @Override
    protected Move getPlayerMove(Colour colour) {
        return null;
    }

    @Override
    protected void nextPlayer() {

    }

    @Override
    protected void play(MoveTicket move) {

    }

    @Override
    protected void play(MoveDouble move) {

    }

    @Override
    protected void play(MovePass move) {

    }

    @Override
    protected List<Move> validMoves(Colour player) {
        return null;
    }

    @Override
    public void spectate(Spectator spectator) {

    }

    @Override
    public boolean join(Player player, Colour colour, int location, Map<Ticket, Integer> tickets) {
        return false;
    }

    @Override
    public List<Colour> getPlayers() {
        return null;
    }

    @Override
    public Set<Colour> getWinningPlayers() {
        return null;
    }

    @Override
    public int getPlayerLocation(Colour colour) {
        return 0;
    }

    @Override
    public int getPlayerTickets(Colour colour, Ticket ticket) {
        return 0;
    }

    @Override
    public boolean isGameOver() {
        return false;
    }

    @Override
    public boolean isReady() {
        return false;
    }

    @Override
    public Colour getCurrentPlayer() {
        return null;
    }

    @Override
    public int getRound() {
        return 0;
    }

    @Override
    public List<Boolean> getRounds() {
        return null;
    }
}
