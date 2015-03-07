import org.junit.Test;
import scotlandyard.*;
import java.util.*;

import static org.junit.Assert.*;



public class PlayerNotifyTests {

    public class PlayerTest implements Player {
        int location;
        boolean notified = false;

        public Move notify(int location, List<Move> moves) {
            this.notified = true;
            this.location = location;
            return moves.iterator().next();
        }
    }


    @Test
    public void testThecurrentPlayerIsNotifiedOnTheirTurn() throws Exception {
        ScotlandYard game = TestHelper.getGame(1, "test_resources/small_map.txt");
        TestHelper.addMrxToGame(game, 2);

        PlayerTest player = new PlayerTest();
        TestHelper.addDetectiveToGame(game, player, Colour.Blue, 3);


        game.turn();
        game.turn();
        assertTrue("Player has been notified when it is their turn",
                player.notified);

    }

    @Test
    public void testThePlayerIsNotifiedWithTheCorrectLocation() throws Exception {
        ScotlandYard game = TestHelper.getGame(1, "test_resources/small_map.txt");
        TestHelper.addMrxToGame(game, 2);

        PlayerTest player = new PlayerTest();
        TestHelper.addDetectiveToGame(game, player, Colour.Blue, 3);


        game.turn();
        int locationBeforeMove = game.getPlayerLocation(Colour.Blue);
        game.turn();
        assertEquals("The player is notified with their position which is correct",
                locationBeforeMove, player.location);
    }

    @Test
    public void testMrXIsNotifiedWithCorrectLocation() throws Exception {
        ScotlandYard game = TestHelper.getGame(1, "test_resources/small_map.txt");

        TestHelper.TestPlayer player = new TestHelper.TestPlayer();
        int mxLocation = 2;
        TestHelper.addMrxToGame(game, player, mxLocation);
        TestHelper.addDetectiveToGame(game,Colour.Blue, 3);


        game.turn();
        assertEquals("Mr X is notified with their position which is correct",
                mxLocation, player.location);
    }


    @Test
    public void testMrXLocationNotifyIsCorrectEvenWhenHidden() throws Exception {
        List<Boolean> rounds =Arrays.asList(false, false);
        ScotlandYard game = TestHelper.getGame(1, rounds, "test_resources/small_map.txt");

        TestHelper.TestPlayer player = new TestHelper.TestPlayer();
        int mxLocation = 2;
        TestHelper.addMrxToGame(game, player, mxLocation);
        TestHelper.addDetectiveToGame(game,Colour.Blue, 3);


        game.turn();
        assertFalse("Mr X player should be notified of his actual location, not his last " +
                        "known location", game.getPlayerLocation(Colour.Black) == player.location);
    }


}
