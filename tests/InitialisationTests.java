import org.junit.Before;
import org.junit.Test;
import scotlandyard.*;
import java.util.*;

import static org.junit.Assert.*;

public class InitialisationTests {


    @Before
    public void setUp() throws Exception {

    }

    @Test
    public void testGameIsNotReadyAfterNotAddingAllThePlayers() throws Exception {
        int numDetectives = 1;
        ScotlandYard game = TestHelper.getGame(numDetectives);
        TestHelper.addMrxToGame(game, 5);

        assertFalse("The game should not be ready after only adding N-1 players " +
                "to a N player game", game.isReady());
    }


    @Test
    public void testSinglePlayerGameIsReadyAfterAddingPlayer() throws Exception {
        int numDetectives = 0;
        ScotlandYard game = TestHelper.getGame(numDetectives);
        TestHelper.addMrxToGame(game, 5);

        assertTrue("The game should be ready after 1 player has been added to" +
                "a 1 player game", game.isReady());


    }


    @Test
    public void testGetGamePlayersShouldReturnFullListOfColours() throws Exception {
        int numDetectives = 1;
        ScotlandYard game = TestHelper.getGame(numDetectives);
        TestHelper.addMrxToGame(game, 5);
        TestHelper.addDetectiveToGame(game, Colour.Blue, 15);

        Colour[] expectedColours = { Colour.Black, Colour.Blue };
        List<Colour> actualColours = game.getPlayers();


        assertArrayEquals("The list of colours from getPlayers() should match the players added",
                expectedColours, actualColours.toArray());
    }

    @Test
    public void testGameIsReadyAfterAddingEnoughPlayers() throws Exception {
        int numDetectives = 1;
        ScotlandYard game = TestHelper.getGame(numDetectives);
        TestHelper.addMrxToGame(game, 5);
        TestHelper.addDetectiveToGame(game, Colour.Blue, 15);

        assertTrue("The game should be ready after N players have been added to" +
                "an N player game", game.isReady());
    }

    @Test
    public void testLocationsMatchInitialLocationsAfterGameHasBeenInitialised() throws Exception {
        int numDetectives = 1;
        int mrXLocationToCheck = 5;
        int detectiveLocationToCheck = 15;

        List<Boolean> rounds = Arrays.asList(true, true);
        ScotlandYard game = TestHelper.getGame(numDetectives, rounds);
        TestHelper.addMrxToGame(game, mrXLocationToCheck);
        TestHelper.addDetectiveToGame(game, Colour.Blue, detectiveLocationToCheck);

        assertEquals("After the players have been added, the values returned from getPlayerLocations" +
                " should match the locations used in join game",
                mrXLocationToCheck, game.getPlayerLocation(Colour.Black));

        assertEquals("After the players have been added, the values returned from getPlayerLocations" +
                        " should match the locations used in join game",
                detectiveLocationToCheck,  game.getPlayerLocation(Colour.Blue));
    }

    @Test
    public void testGetPlayerTicketsMatchesTheTicketsEachPlayerHasBeenAssigned() throws Exception {
        int numDetectives = 1;
        ScotlandYard game = TestHelper.getGame(numDetectives);
        TestHelper.addMrxToGame(game, 5);
        TestHelper.addDetectiveToGame(game, Colour.Blue, 15);

        // test for Mr X tickets
        for (int i = 0; i < TestHelper.tickets.length; i++) {
            assertEquals("Mr X should have the number of tickets assigned to him after initialisation",
                    TestHelper.mrXTicketNumbers[i], game.getPlayerTickets(Colour.Black, TestHelper.tickets[i]));
        }

        // test for detective tickets
        for (int i = 0; i < TestHelper.tickets.length; i++) {
            assertEquals("Detective should have the number of tickets assigned to him after initialisation",
                    TestHelper.detectiveTicketNumbers[i], game.getPlayerTickets(Colour.Blue, TestHelper.tickets[i]));
        }
    }

}
