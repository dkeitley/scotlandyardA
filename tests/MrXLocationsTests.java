import org.junit.Test;
import scotlandyard.*;
import java.util.*;

import static org.junit.Assert.*;

public class MrXLocationsTests  {

    @Test
    public void testMrXLocationIsZeroIfHeHasNotYetShownHimself() throws Exception {
        int numDetectives = 1;
        int mrXLocation = 5;

        List<Boolean> rounds = new ArrayList<Boolean>();
        rounds.add(false);
        rounds.add(false);

        ScotlandYard game = TestHelper.getGame(numDetectives, rounds);
        TestHelper.addMrxToGame(game, mrXLocation);
        TestHelper.addDetectiveToGame(game, Colour.Blue, 15);


        assertEquals("If Mr X has not yet revealed himself, his last known location " +
                "should be zero", 0, game.getPlayerLocation(Colour.Black));
    }


    @Test
    public void testMrXLocationIsCorrectAfterDisplayRoundIsFirstRound() throws Exception {
        int numDetectives = 1;
        int mrXLocation = 5;

        List<Boolean> rounds = new ArrayList<Boolean>();
        rounds.add(true);
        rounds.add(false);

        ScotlandYard game = TestHelper.getGame(numDetectives, rounds);
        TestHelper.addMrxToGame(game, mrXLocation);
        TestHelper.addDetectiveToGame(game, Colour.Blue, 15);


        assertEquals("If at the current round, Mr X is visible, Mr X's last known location " +
                "is his current location", mrXLocation, game.getPlayerLocation(Colour.Black));
    }


    @Test
    public void testMrXLocationIsCorrectAfterDisplayRoundOccursNRoundsIntoTheGame() throws Exception {
        int numDetectives = 1;
        int mrXLocation = 5;

        List<Boolean> rounds = new ArrayList<Boolean>();
        rounds.add(false);
        rounds.add(true);
        rounds.add(false);

        ScotlandYard game = TestHelper.getGame(numDetectives, rounds);

        TestHelper.SingleMovePlayer player = TestHelper.getSingleMovePlayer();

        game.join(player, Colour.Black, mrXLocation, TestHelper.getTickets(true));
        TestHelper.addDetectiveToGame(game, Colour.Blue, 15);

        game.turn();
        game.turn();

        MoveTicket mt = player.chosenMove;
        assertEquals("If Mr X is due to display himself N rounds into the game, the display data" +
                " should be accurate at that round", mt.target, game.getPlayerLocation(Colour.Black));

    }


}
