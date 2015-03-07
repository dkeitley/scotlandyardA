import org.junit.Test;
import scotlandyard.*;
import java.util.*;

import static org.junit.Assert.*;



public class GetWinningPlayersTests {
    @Test
    public void testIfGameIsNotOverEmptyListIsReturned() throws Exception {
        ScotlandYard game = TestHelper.getGame(1);
        TestHelper.addMrxToGame(game, 2);
        TestHelper.addDetectiveToGame(game, Colour.Blue, 56);

        assertTrue("If the game is not over, an empty list is returned",
                game.getWinningPlayers().isEmpty());
    }

    @Test
    public void testIfGameIsOverNonEmptyListIsReturned() throws Exception {
        ScotlandYard game = TestHelper.getGame(1, "test_resources/small_map.txt");
        TestHelper.addMrxToGame(game, 1);
        TestHelper.addDetectiveToGame(game, Colour.Blue, 7);

        assertFalse("If the game is over, a non empty list is returned",
                game.getWinningPlayers().isEmpty());
    }

    @Test
    public void testIfMrXWonHisColourShouldBeReturned() throws Exception {
        ScotlandYard game = TestHelper.getGame(1, "test_resources/small_map.txt");
        TestHelper.addMrxToGame(game, 1);
        TestHelper.addDetectiveToGame(game, Colour.Blue, 7);

        assertTrue("If Mr X has won his colour should be returned",
                game.getWinningPlayers().contains(Colour.Black));
    }

    @Test
    public void testIfMrXWonOnlyHisColourShouldBeReturned() throws Exception {
        ScotlandYard game = TestHelper.getGame(1, "test_resources/small_map.txt");
        TestHelper.addMrxToGame(game, 1);
        TestHelper.addDetectiveToGame(game, Colour.Blue, 7);

        assertFalse("If Mr X has won his colour should be returned",
                game.getWinningPlayers().contains(Colour.Blue));
    }

    @Test
    public void testIfDetectivesWonAllTheirColoursAreReturned() throws Exception {
        ScotlandYard game = TestHelper.getGame(3, "test_resources/small_map.txt");
        TestHelper.addMrxToGame(game, 5);
        TestHelper.addDetectiveToGame(game, Colour.Blue, 2);
        TestHelper.addDetectiveToGame(game, Colour.Red, 4);
        TestHelper.addDetectiveToGame(game, Colour.Green, 6);

        assertTrue("If the detectives have won, all their colours should be in the " +
                "returned list", game.getWinningPlayers().contains(Colour.Blue));

        assertTrue("If the detectives have won, all their colours should be in the " +
                "returned list", game.getWinningPlayers().contains(Colour.Red));

        assertTrue("If the detectives have won, all their colours should be in the " +
                "returned list", game.getWinningPlayers().contains(Colour.Green));
    }

    @Test
    public void testIfDetectivesWonMrXColourIsNotReturned() throws Exception {
        ScotlandYard game = TestHelper.getGame(3, "test_resources/small_map.txt");
        TestHelper.addMrxToGame(game, 5);
        TestHelper.addDetectiveToGame(game, Colour.Blue, 2);
        TestHelper.addDetectiveToGame(game, Colour.Red, 4);
        TestHelper.addDetectiveToGame(game, Colour.Green, 6);

        assertFalse("If the detectives have won, mr X colour should nt be returned",
                game.getWinningPlayers().contains(Colour.Black));

    }

}
