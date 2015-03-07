import org.junit.Test;
import scotlandyard.*;
import java.util.*;

import static org.junit.Assert.*;


public class IsGameOverTests {

    public class TestPlayer implements Player {
        Move nextMove;

        public Move notify(int location, List<Move> moves) {
            return nextMove;
        }
    }

    @Test
    public void testIsGameOverShouldBeFalseIfIsRunningIsFalse() throws Exception {
        ScotlandYard game = TestHelper.getGame(1, "test_resources/small_map.txt");
        TestHelper.addMrxToGame(game, 4);

        assertFalse("If the game is not ready it should also not be over", game.isGameOver());
    }


    @Test
    public void testIsGameOverShouldBeFalseIfNotGameOver() throws Exception {
        ScotlandYard game = TestHelper.getGame(1, "test_resources/small_map.txt");
        TestHelper.addMrxToGame(game, 4);
        TestHelper.addDetectiveToGame(game, Colour.Blue, 1);
        game.turn();

        assertFalse("If the game is running but not over, isGameOver shoudl be false",
                game.isGameOver());

    }


    // mr X win conditions
    @Test
    public void testIsGameOverWhenDetectivesAreAllStuck() throws Exception {
        ScotlandYard game = TestHelper.getGame(2, "test_resources/small_map.txt");
        TestHelper.addMrxToGame(game, 4);


        Map<Ticket, Integer> tickets = new HashMap<Ticket, Integer>();
        tickets.put(Ticket.Bus, 0);
        tickets.put(Ticket.Underground, 0);
        tickets.put(Ticket.Taxi, 0);
        tickets.put(Ticket.DoubleMove, 0);
        tickets.put(Ticket.SecretMove, 0);

        game.join(TestHelper.getPlayer(), Colour.Blue, 1, tickets);
        game.join(TestHelper.getPlayer(), Colour.Green, 2, tickets);


        assertTrue("If the detectives cant move because they are out of tickets " +
                "the game should be over", game.isGameOver());

    }


    @Test
    public void testIsGameOverWhenNumberOfRoundsHasBeenPlayedAndDetectivesHavePlayed() throws Exception {
        List<Boolean> rounds = Arrays.asList(true, true, true);

        ScotlandYard game = TestHelper.getGame(1, rounds, "test_resources/small_map.txt");
        TestHelper.addMrxToGame(game, 4);
        TestHelper.addDetectiveToGame(game, Colour.Blue, 1);

        game.turn();
        assertFalse("Game should not be over after first move", game.isGameOver());

        game.turn();
        assertFalse("Game should not be over after second move", game.isGameOver());

        game.turn();
        assertFalse("Game should not be over after third move", game.isGameOver());

        game.turn();
        assertTrue("Game should be over after forth move", game.isGameOver());

    }


    // detective win conditions
    @Test
    public void testIsGameOverIfMrXIsCaught() throws Exception {
        List<Boolean> rounds = Arrays.asList(true, true);

        ScotlandYard game = TestHelper.getGame(1, rounds, "test_resources/small_map.txt");
        TestPlayer mrX = new TestPlayer();
        TestPlayer detective = new TestPlayer();

        TestHelper.addMrxToGame(game, mrX, 3);
        TestHelper.addDetectiveToGame(game, detective, Colour.Blue, 1);

        mrX.nextMove = new MoveTicket(Colour.Black, 2, Ticket.Bus);
        game.turn();

        detective.nextMove = new MoveTicket(Colour.Blue, 2, Ticket.Bus);
        game.turn();

        assertTrue("If Mr X has Been landed on, the game should be over",
                game.isGameOver());

    }

    @Test
    public void testIsGameOverIfMrXHasNowhereToMove() throws Exception {
        List<Boolean> rounds = Arrays.asList(true, true);

        ScotlandYard game = TestHelper.getGame(3, rounds, "test_resources/small_map.txt");

        TestHelper.addMrxToGame(game, 5);
        TestHelper.addDetectiveToGame(game, Colour.Blue, 6);
        TestHelper.addDetectiveToGame(game, Colour.Green, 2);
        TestHelper.addDetectiveToGame(game, Colour.Red, 4);


        assertTrue("If Mr X has nowhere to move he has lost the game",
                game.isGameOver());


    }


    @Test
    public void testBeforeInitialiseGameOverState() throws Exception {
        int numDetectives = 1;
        ScotlandYard game = TestHelper.getGame(numDetectives);

        assertFalse("Before the game is initialised, the isGameOver() function should return false",
                game.isGameOver());
    }


    @Test
    public void testGameIsOverWithOnlyOnePlayer() throws Exception {
        int numDetectives = 0;
        ScotlandYard game = TestHelper.getGame(numDetectives);
        TestHelper.addMrxToGame(game, 5);

        assertTrue("If there is only Mr X in the game, the game should be over",
                game.isGameOver());
    }

    @Test
    public void testGameIsNotOverAfterInitialisingAnNPlayerGame() throws Exception {
        int numDetectives = 1;
        ScotlandYard game = TestHelper.getGame(numDetectives);
        TestHelper.addMrxToGame(game, 5);
        TestHelper.addDetectiveToGame(game, Colour.Blue, 15);

        assertFalse("isGameOver() should be false after adding N players to an N player game " +
                "where  N > 1", game.isGameOver());
    }

}
