import org.junit.Before;
import org.junit.Test;
import scotlandyard.*;
import java.util.*;

import static org.junit.Assert.*;

public class PlayTests {


    @Test
    public void testInterpretOnlyCurrentPlayerMayMove() throws Exception {
        List<Boolean> rounds = Arrays.asList(true, true, true, true);
        ScotlandYard game = TestHelper.getGame(2, rounds, "test_resources/small_map.txt");
        TestHelper.addMrxToGame(game, 2);
        TestHelper.addDetectiveToGame(game, Colour.Blue, 4);
        TestHelper.addDetectiveToGame(game, Colour.Yellow, 1);

        Map<Colour, Integer> locs1 = getCurrentLocations(game, Colour.Black);
        game.turn();

        for(Colour col : locs1.keySet()) {
            assertEquals("When it is not a players move they should not move",
                    (int) locs1.get(col), (int) game.getPlayerLocation(col));
        }

        Map<Colour, Integer> locs2 = getCurrentLocations(game, Colour.Blue);
        game.turn();

        for(Colour col : locs2.keySet()) {
            assertEquals("When it is not a players move they should not move",
                    (int) locs2.get(col), (int) game.getPlayerLocation(col));
        }

        Map<Colour, Integer> locs3 = getCurrentLocations(game, Colour.Yellow);
        game.turn();

        for(Colour col : locs3.keySet()) {
            assertEquals("When it is not a players move they should not move",
                    (int) locs3.get(col), (int) game.getPlayerLocation(col));
        }
    }


    public Map<Colour, Integer> getCurrentLocations(ScotlandYard game, Colour exclude)
    {
        Map<Colour, Integer> locations = new HashMap<Colour, Integer>();
        List<Colour> cols = game.getPlayers();
        for(Colour col : cols) {
            if(col != exclude)
                locations.put(col, game.getPlayerLocation(col));

        }
        return locations;
    }


    public class TestPassTicketPlayer implements Player {
        MovePass chosen;
        public Move notify(int location, List<Move> moves) {
            for (Move move : moves) {
                if(move instanceof MovePass) {
                    chosen = (MovePass) move;
                    return move;
                }
            }
            return moves.iterator().next();
        }
    }

    // Move Pass play
    @Test
    public void testInterpretPassShouldNotChangePlayerLocation() throws Exception {
        ScotlandYard game = TestHelper.getGame(1, "test_resources/small_map.txt");
        TestHelper.addMrxToGame(game, 2);

        TestPassTicketPlayer testPlayer = new TestPassTicketPlayer();
        TestHelper.addDetectiveToGame(game, testPlayer, Colour.Blue, 7);

        // get the initial position
        int initialPosition = game.getPlayerLocation(Colour.Blue);
        game.turn();
        game.turn();

        assertEquals("After a pass move is played, the player should not have changed location",
                initialPosition, game.getPlayerLocation(Colour.Blue));


    }

    @Test
    public void testInterpretPassShouldNotChangePlayerTickets() throws Exception {
        ScotlandYard game = TestHelper.getGame(1, "test_resources/small_map.txt");
        TestHelper.addMrxToGame(game, 2);
        TestPassTicketPlayer testPlayer = new TestPassTicketPlayer();
        TestHelper.addDetectiveToGame(game, testPlayer, Colour.Blue, 7);

        // get the initial tickets
        Map<Ticket,Integer> initialTickets = new HashMap<Ticket, Integer>();
        for(Ticket ticket : TestHelper.tickets) {
            initialTickets.put(ticket, game.getPlayerTickets(Colour.Blue, ticket));
        }

        game.turn();
        game.turn();

        for(Ticket ticket : TestHelper.tickets) {
            assertEquals("After playing a pass move, the number of tickets held by the player should " +
                    "not have changes", (int) initialTickets.get(ticket),
                    (int) game.getPlayerTickets(Colour.Blue, ticket));
        }

    }

    @Test
    public void testInterpretPassShouldMoveTheCurrentPlayerOnByOne() throws Exception {
        ScotlandYard game = TestHelper.getGame(1, "test_resources/small_map.txt");
        TestHelper.addMrxToGame(game, 2);

        TestPassTicketPlayer testPlayer = new TestPassTicketPlayer();
        TestHelper.addDetectiveToGame(game, testPlayer, Colour.Blue, 7);

        // get the initial position
        game.turn();
        Colour currentPlayer = game.getCurrentPlayer();
        game.turn();

        assertFalse("The current player should have moved on after the pass move has been played",
                currentPlayer == game.getCurrentPlayer());

    }


    public class TestMoveTicketPlayer implements Player {
        MoveTicket chosen;
        public Move notify(int location, List<Move> moves) {
            for (Move move : moves) {
                if(move instanceof MoveTicket) {
                    chosen = (MoveTicket) move;
                    return move;
                }
            }
            return moves.iterator().next();
        }
    }

    // Move Ticket Interpret
    @Test
    public void testDetectiveTicketsGivenToMrX() throws Exception {
        ScotlandYard game = TestHelper.getGame(1, "test_resources/small_map.txt");
        TestHelper.addMrxToGame(game, 2);

        TestMoveTicketPlayer testPlayer = new TestMoveTicketPlayer();
        TestHelper.addDetectiveToGame(game, testPlayer, Colour.Blue, 5);

        game.turn();

        // get the initial tickets
        Map<Ticket,Integer> initialTickets = new HashMap<Ticket, Integer>();
        for(Ticket ticket : TestHelper.tickets) {
            initialTickets.put(ticket, game.getPlayerTickets(Colour.Black, ticket));
        }

        game.turn();

        assertEquals("After a detective has used a move ticket, the corresponding ticket needs to " +
                "be passed to Mr X", initialTickets.get(testPlayer.chosen.ticket)+1,
                game.getPlayerTickets(Colour.Black, testPlayer.chosen.ticket));

    }

    @Test
    public void testPlayerMovesToTheDesiredLocation() throws Exception {
        ScotlandYard game = TestHelper.getGame(1, "test_resources/small_map.txt");
        TestHelper.addMrxToGame(game, 2);

        TestMoveTicketPlayer testPlayer = new TestMoveTicketPlayer();
        TestHelper.addDetectiveToGame(game, testPlayer, Colour.Blue, 5);

        game.turn();
        game.turn();


        assertEquals("After playing a move ticket, the new location of the player should match " +
                "the ticket's target", testPlayer.chosen.target, game.getPlayerLocation(Colour.Blue));


    }

    @Test
    public void testPlayerTicketsDecrementedAfterMove() throws Exception {
        ScotlandYard game = TestHelper.getGame(1, "test_resources/small_map.txt");
        TestHelper.addMrxToGame(game, 2);

        TestMoveTicketPlayer testPlayer = new TestMoveTicketPlayer();
        TestHelper.addDetectiveToGame(game, testPlayer, Colour.Blue, 5);

        game.turn();

        // get the initial tickets
        Map<Ticket,Integer> initialTickets = new HashMap<Ticket, Integer>();
        for(Ticket ticket : TestHelper.tickets) {
            initialTickets.put(ticket, game.getPlayerTickets(Colour.Blue, ticket));
        }

        game.turn();

        assertEquals("After playing a move, ticket used should have been discarded by the player ",
                initialTickets.get(testPlayer.chosen.ticket)-1,
                game.getPlayerTickets(Colour.Blue, testPlayer.chosen.ticket));
    }

    public class DoubleMoveTicketPlayer implements Player {
        MoveDouble chosen;
        public Move notify(int location, List<Move> moves) {
            for (Move move : moves) {
                if(move instanceof MoveDouble) {
                    chosen = (MoveDouble) move;
                    return move;
                }
            }
            return moves.iterator().next();
        }
    }


    // Move Double Interpret
    @Test
    public void testDoubleMoveWork() throws Exception {
        ScotlandYard game = TestHelper.getGame(1, "test_resources/small_map.txt");
        DoubleMoveTicketPlayer testPlayer = new DoubleMoveTicketPlayer();
        TestHelper.addMrxToGame(game, testPlayer, 2);
        TestHelper.addDetectiveToGame(game, Colour.Blue, 5);

        game.turn();

        MoveTicket secondLocation = (MoveTicket) testPlayer.chosen.moves.get(1);

        assertEquals("After playing a double move the player should have moved to the correct " +
                        "location", secondLocation.target, game.getPlayerLocation(Colour.Black));
    }

    public void testDoubleMoveUsesTheCorrectTickets() throws Exception {
        ScotlandYard game = TestHelper.getGame(1, "test_resources/small_map.txt");
        DoubleMoveTicketPlayer testPlayer = new DoubleMoveTicketPlayer();
        TestHelper.addMrxToGame(game, testPlayer, 2);
        TestHelper.addDetectiveToGame(game, Colour.Blue, 5);


        // get the initial tickets
        Map<Ticket,Integer> initialTickets = new HashMap<Ticket, Integer>();
        for(Ticket ticket : TestHelper.tickets) {
            initialTickets.put(ticket, game.getPlayerTickets(Colour.Blue, ticket));
        }

        game.turn();

        MoveTicket firstLocation = (MoveTicket) testPlayer.chosen.moves.get(0);
        MoveTicket secondLocation = (MoveTicket) testPlayer.chosen.moves.get(1);


        assertEquals("After playing a double move, the correct tickets should have been removed " +
                "from the player", initialTickets.get(firstLocation.ticket)-1,
                game.getPlayerTickets(Colour.Black, firstLocation.ticket));

        assertEquals("After playing a double move, the correct tickets should have been removed " +
                "from the player", initialTickets.get(secondLocation.ticket)-1,
                game.getPlayerTickets(Colour.Black, secondLocation.ticket));

        assertEquals("After playing a double move, the correct tickets should have been removed " +
                "from the player", initialTickets.get(Ticket.DoubleMove)-1,
                game.getPlayerTickets(Colour.Black, Ticket.DoubleMove));

    }
}
