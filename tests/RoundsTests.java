import org.junit.Test;
import scotlandyard.*;
import java.util.*;

import static org.junit.Assert.*;

public class RoundsTests {

    public class RoundTestSpectator implements Spectator {

        ScotlandYardView view;
        RoundTestSpectator(ScotlandYardView view) {
            this.view = view;
        }

        boolean checkRound = false;
        int round;
        public void notify(Move move) {

            if(checkRound)
            {
                round = view.getRound();
                checkRound = false;
            }

            if(move instanceof MoveDouble)
                checkRound = true;

        }
    }

    @Test
    public void testAfterInitialisationTheRoundShouldBeZero() throws Exception {
        int numDetectives = 1;
        ScotlandYard game = TestHelper.getGame(numDetectives);
        TestHelper.addMrxToGame(game, 5);
        TestHelper.addDetectiveToGame(game, Colour.Blue, 15);

        assertEquals("After the game is initialised, but before MrX plays, the round should be 0", 0, game.getRound());
    }


    @Test
    public void testAfterMrXMakesASingleMoveTheRoundShouldHaveIncremenetedByOne() throws Exception {
        int numDetectives = 1;
        ScotlandYard game = TestHelper.getGame(numDetectives);
        Player player= TestHelper.getSingleMovePlayer();
        TestHelper.addMrxToGame(game, player, 5);
        TestHelper.addDetectiveToGame(game, Colour.Blue, 15);
        int roundOne = game.getRound();
        game.turn();

        assertEquals("After Mr X has a go, the round should have incremented by one",
                roundOne + 1, game.getRound());
    }


    @Test
    public void testAfterMrXMakesADoubleMoveTheRoundsShouldHaveIncrementedTwice() throws Exception {
        int numDetectives = 1;
        ScotlandYard game = TestHelper.getGame(numDetectives, "test_resources/small_map.txt");
        TestHelper.addMrxToGame(game, TestHelper.getDoubleMovePlayer(), 5);
        TestHelper.addDetectiveToGame(game, Colour.Blue, 15);
        int roundOne = game.getRound();
        game.turn();

        assertEquals("After Mr X plays a double move the round should increment twice",
                roundOne+2, game.getRound());

    }


    @Test
    public void testAfterFirstMoveOfADoubleMoveRoundsShouldIncrementByOne() throws Exception {
        int numDetectives = 1;
        ScotlandYard game = TestHelper.getGame(numDetectives, "test_resources/small_map.txt");
        TestHelper.addMrxToGame(game, TestHelper.getDoubleMovePlayer(), 5);
        TestHelper.addDetectiveToGame(game, Colour.Blue, 15);

        RoundTestSpectator spectator = new RoundTestSpectator(game);
        game.spectate(spectator);

        int roundOne = game.getRound();
        game.turn();

        assertEquals("After Mr X plays the first ticket of a double move the round should increment once",
                roundOne+1, spectator.round);

    }



    @Test
    public void testTheVisibilityOfTheRoundsShouldCorrespondToTheInputRounds() throws Exception {
        int numDetectives = 1;
        List<Boolean> rounds = Arrays.asList(true, false, true, false, true);
        ScotlandYard game = TestHelper.getGame(numDetectives, rounds, "test_resources/small_map.txt");
        TestHelper.addMrxToGame(game, TestHelper.getDoubleMovePlayer(), 5);
        TestHelper.addDetectiveToGame(game, Colour.Blue, 15);


        for (int i = 0; i < rounds.size(); i++) {
            assertEquals("The set of rounds provided by the game matches the set of rounds given to the game",
                    rounds.get(i), game.getRounds().get(i));
        }

    }

    @Test
    public void testAfterInitialisationGetCurrentPlayerIsBlack() throws Exception {
        int numDetectives = 1;
        ScotlandYard game = TestHelper.getGame(numDetectives);
        TestHelper.addMrxToGame(game, 5);
        TestHelper.addDetectiveToGame(game, Colour.Blue, 15);

        assertEquals("After the game has been initialised, the getCurrentPlayer() function should " +
                "return MrX's colour", Colour.Black, game.getCurrentPlayer());
    }

    @Test
    public void testAfterTheTurnAfterMrXTheCurrentPlayerIsCorrect() throws Exception {
        int numDetectives = 1;
        ScotlandYard game = TestHelper.getGame(numDetectives);
        TestHelper.addMrxToGame(game, 5);
        TestHelper.addDetectiveToGame(game, Colour.Blue, 15);
        game.turn();
        assertEquals("After the game has been initialised, and mr x has had a go, the getCurrentPlayer() function should " +
                "return first detective's colour", Colour.Blue, game.getCurrentPlayer());

    }
}
