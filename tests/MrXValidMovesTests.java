import org.junit.Test;
import scotlandyard.*;
import java.util.*;


import static org.junit.Assert.*;

public class MrXValidMovesTests extends ValidMovesTests {

    @Test
    public void testMrXValidMoves() throws Exception {

        ScotlandYard game = ValidMovesTests.simpleGame(1);
        player1 = new TestHelper.TestPlayer();
        player2 = new TestHelper.TestPlayer();
        TestHelper.addMrxToGame(game, player1, 3);
        TestHelper.addDetectiveToGame(game, player2, Colour.Blue, 7);
        game.turn();
        game.turn();

        List<Move> moves = player1.moves;
        List<Move> expected = MrXValidMovesTests.expectedMrXMoves();


        for(Move move : expected) {
            assertTrue("Mr X Valid tickets should contain " + move,
                    moves.contains(move));
        }
    }

    @Test
    public void testMrXValidMoves2() throws Exception {

        ScotlandYard game = ValidMovesTests.simpleGame(1);
        player1 = new TestHelper.TestPlayer();
        player2 = new TestHelper.TestPlayer();
        TestHelper.addMrxToGame(game, player1, 3);
        TestHelper.addDetectiveToGame(game, player2, Colour.Blue, 7);
        game.turn();
        game.turn();

        List<Move> moves = player1.moves;
        List<Move> expected = MrXValidMovesTests.expectedMrXMoves();

        for(Move move : moves) {
            assertTrue("Mr X Valid tickets should not contain tickets that aren't expected " + move,
                    expected.contains(move));
        }
    }

    @Test
    public void testMrXNoDoubleMovesInDoubleMoves() throws Exception {
        ScotlandYard game = ValidMovesTests.simpleGame(1);
        player1 = new TestHelper.TestPlayer();
        player2 = new TestHelper.TestPlayer();
        TestHelper.addMrxToGame(game, player1, 3);
        TestHelper.addDetectiveToGame(game, player2, Colour.Blue, 7);
        game.turn();

        List<Move> moves = player1.moves;

        for(Move move : moves) {
            if(move instanceof MoveDouble) {
                List<Move> subMoves = ((MoveDouble) move).moves;
                for(Move submove : subMoves) {
                    assertFalse("Mr X Valid double moves should not contain double moves",
                            submove instanceof MoveDouble);
                }
            }
        }

    }

    @Test
    public void testMrXNoDoubleMovesIfNoDoubleMoveTickets() throws Exception {
        ScotlandYard game = ValidMovesTests.simpleGame(1);
        player1 = new TestHelper.TestPlayer();
        player2 = new TestHelper.TestPlayer();

        Map<Ticket, Integer> mrxTickets = TestHelper.getTickets(true);
        mrxTickets.put(Ticket.DoubleMove, 0);
        game.join(player1, Colour.Black, 3, mrxTickets);


        TestHelper.addDetectiveToGame(game, player2, Colour.Blue, 7);
        game.turn();

        List<Move> moves = player1.moves;
        for(Move move : moves ) {
            assertFalse("If Mr X has no double moves tickets, there should be no double moves in the " +
                    "set of valid moves", move instanceof MoveDouble);
        }
    }


    @Test
    public void testMrXNoSecretMovesIfNoSecretMoveTickets() throws Exception {
        ScotlandYard game = ValidMovesTests.simpleGame(1);
        player1 = new TestHelper.TestPlayer();
        player2 = new TestHelper.TestPlayer();

        Map<Ticket, Integer> mrxTickets = TestHelper.getTickets(true);
        mrxTickets.put(Ticket.SecretMove, 0);
        game.join(player1, Colour.Black, 3, mrxTickets);

        TestHelper.addDetectiveToGame(game, player2, Colour.Blue, 7);
        game.turn();

        List<Move> moves = player1.moves;
        for(Move move : moves ) {
            if(move instanceof MoveTicket) {
                MoveTicket moveTicket = (MoveTicket) move;
                assertFalse("If Mr X has no secret move tickets, there should be no secret moves in the " +
                        "set of valid moves", moveTicket.ticket == Ticket.SecretMove);
            }
        }
    }

    @Test
    public void testMrXValidMovesMustNotContainPasses() throws Exception {
        ScotlandYard game = ValidMovesTests.simpleGame(1);
        player1 = new TestHelper.TestPlayer();
        player2 = new TestHelper.TestPlayer();

        Map<Ticket, Integer> mrxTickets = TestHelper.getTickets(true);
        mrxTickets.put(Ticket.SecretMove, 0);
        game.join(player1, Colour.Black, 3, mrxTickets);

        TestHelper.addDetectiveToGame(game, player2, Colour.Blue, 7);
        game.turn();

        List<Move> moves = player1.moves;
        for(Move move: moves) {
            assertFalse("Mr X Must never be given a pass move",
                    move instanceof MovePass);
        }
    }

    @Test
    public void testMrXValidMovesMustNotContainPasses2() throws Exception {
        ScotlandYard game = ValidMovesTests.simpleGame(1);
        player1 = new TestHelper.TestPlayer();
        player2 = new TestHelper.TestPlayer();

        Map<Ticket, Integer> mrxTickets = TestHelper.getTickets(true);
        mrxTickets.put(Ticket.SecretMove, 0);
        game.join(player1, Colour.Black, 7, mrxTickets);

        TestHelper.addDetectiveToGame(game, player2, Colour.Blue, 7);
        game.turn();

        List<Move> moves = player1.moves;
        for(Move move: moves) {
            assertEquals("Mr X Must never be given a pass move, instead the game should be over and he gets" +
                    " an empty list", 0, moves.size());
        }
    }

    @Test
    public void testMrXMustHaveEnoughTicketsForADoubleMove() throws Exception {
        ScotlandYard game = ValidMovesTests.simpleGame(1);
        player1 = new TestHelper.TestPlayer();
        player2 = new TestHelper.TestPlayer();

        Map<Ticket, Integer> mrxTickets = TestHelper.getTickets(true);
        mrxTickets.put(Ticket.Bus, 1);
        game.join(player1, Colour.Black, 3, mrxTickets);

        TestHelper.addDetectiveToGame(game, player2, Colour.Blue, 7);
        game.turn();

        Move testMove = new MoveDouble(Colour.Black,
                new MoveTicket(Colour.Black, 2, Ticket.Bus),
                new MoveTicket(Colour.Black, 3, Ticket.Bus));

        List<Move> moves = player1.moves;



        assertFalse("If Mr X does not have enough tickets to make both of his double " +
                "moves, the double move should not be in the set of valid moves",
                moves.contains(testMove));

    }

    @Test
    public void testMrXCannotMoveToALocationOccupiedByAnotherPlayer() throws  Exception {
        ScotlandYard game = ValidMovesTests.simpleGame(1);
        player1 = new TestHelper.TestPlayer();
        player2 = new TestHelper.TestPlayer();

        TestHelper.addMrxToGame(game, player1, 3);
        TestHelper.addDetectiveToGame(game, player2, Colour.Blue, 2);
        game.turn();


        List<Move> moves = player1.moves;
        for(Move move : moves) {
            if(move instanceof MoveTicket) {
                MoveTicket ticketMove = (MoveTicket) move;
                assertFalse("Mr X can't move onto a location occupied by another player",
                        ticketMove.target == game.getPlayerLocation(Colour.Blue));
            }
        }
    }

    @Test
    public void testMrXCanntMoveToALocationOccupiedByAnnotherPlayerDuringADoubleMove() throws Exception {
        ScotlandYard game = ValidMovesTests.simpleGame(1);
        player1 = new TestHelper.TestPlayer();
        player2 = new TestHelper.TestPlayer();

        TestHelper.addMrxToGame(game, player1, 3);
        TestHelper.addDetectiveToGame(game, player2, Colour.Blue, 2);
        game.turn();

        List<Move> moves = player1.moves;
        for(Move move : moves) {
            if(move instanceof MoveDouble) {
                MoveTicket m1 = (MoveTicket) ((MoveDouble) move).moves.get(0);
                MoveTicket m2 = (MoveTicket) ((MoveDouble) move).moves.get(1);
                assertFalse("Mr X can't move onto a location occupied by another player during a double move",
                        m1.target == game.getPlayerLocation(Colour.Blue));
                assertFalse("Mr X can't move onto a location occupied by another player during a double move",
                        m2.target == game.getPlayerLocation(Colour.Blue));
            }
        }
    }


    public static Move doubleMove(int target1, int target2, Ticket ticket1, Ticket ticket2) {
        return new MoveDouble(Colour.Black,
                new MoveTicket(Colour.Black, target1, ticket1),
                new MoveTicket(Colour.Black, target2, ticket2));
    }


    public static List<Move> expectedMrXMoves() {

        List<Move> expected = new ArrayList<Move>();
        expected.add(new MoveTicket(Colour.Black, 4, Ticket.Taxi));
        expected.add(new MoveTicket(Colour.Black, 2, Ticket.Bus));
        expected.add(new MoveTicket(Colour.Black, 4, Ticket.SecretMove));
        expected.add(new MoveTicket(Colour.Black, 2, Ticket.SecretMove));
        expected.add(new MoveTicket(Colour.Black, 6, Ticket.SecretMove));

        expected.add(doubleMove(2,3,Ticket.Bus,Ticket.Bus));
        expected.add(doubleMove(2,3,Ticket.Bus,Ticket.SecretMove));
        expected.add(doubleMove(2,3,Ticket.SecretMove,Ticket.Bus));
        expected.add(doubleMove(2,3,Ticket.SecretMove,Ticket.SecretMove));

        expected.add(doubleMove(2,5,Ticket.Bus,Ticket.Underground));
        expected.add(doubleMove(2,5,Ticket.Bus,Ticket.SecretMove));
        expected.add(doubleMove(2,5,Ticket.SecretMove,Ticket.Underground));
        expected.add(doubleMove(2,5,Ticket.SecretMove,Ticket.SecretMove));

        expected.add(doubleMove(2,6,Ticket.Bus,Ticket.Taxi));
        expected.add(doubleMove(2,6,Ticket.Bus,Ticket.SecretMove));
        expected.add(doubleMove(2,6,Ticket.SecretMove,Ticket.Taxi));
        expected.add(doubleMove(2,6,Ticket.SecretMove,Ticket.SecretMove));

        expected.add(doubleMove(2,1,Ticket.Bus,Ticket.Bus));
        expected.add(doubleMove(2,1,Ticket.Bus,Ticket.SecretMove));
        expected.add(doubleMove(2,1,Ticket.SecretMove,Ticket.Bus));
        expected.add(doubleMove(2,1,Ticket.SecretMove,Ticket.SecretMove));

        expected.add(doubleMove(6,1,Ticket.SecretMove,Ticket.Bus));
        expected.add(doubleMove(6,1,Ticket.SecretMove,Ticket.SecretMove));

        expected.add(doubleMove(6,1,Ticket.SecretMove,Ticket.Taxi));
        expected.add(doubleMove(6,1,Ticket.SecretMove,Ticket.SecretMove));

        expected.add(doubleMove(6,5,Ticket.SecretMove,Ticket.Taxi));
        expected.add(doubleMove(6,5,Ticket.SecretMove,Ticket.SecretMove));

        expected.add(doubleMove(6,2,Ticket.SecretMove,Ticket.Taxi));
        expected.add(doubleMove(6,2,Ticket.SecretMove,Ticket.SecretMove));

        expected.add(doubleMove(6,3,Ticket.SecretMove,Ticket.SecretMove));

        expected.add(doubleMove(4,1,Ticket.Taxi,Ticket.Underground));
        expected.add(doubleMove(4,1,Ticket.Taxi,Ticket.SecretMove));
        expected.add(doubleMove(4,1,Ticket.SecretMove,Ticket.Underground));
        expected.add(doubleMove(4,1,Ticket.SecretMove,Ticket.SecretMove));

        expected.add(doubleMove(4,3,Ticket.Taxi,Ticket.Taxi));
        expected.add(doubleMove(4,3,Ticket.Taxi,Ticket.SecretMove));
        expected.add(doubleMove(4,3,Ticket.SecretMove,Ticket.Taxi));
        expected.add(doubleMove(4,3,Ticket.SecretMove,Ticket.SecretMove));

        expected.add(doubleMove(4,5,Ticket.Taxi,Ticket.Taxi));
        expected.add(doubleMove(4,5,Ticket.Taxi,Ticket.SecretMove));
        expected.add(doubleMove(4,5,Ticket.SecretMove,Ticket.Taxi));
        expected.add(doubleMove(4,5,Ticket.SecretMove,Ticket.SecretMove));

        return expected;
    }
}
