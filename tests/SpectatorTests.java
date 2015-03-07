import org.junit.Test;
import scotlandyard.*;
import scotlandyard.Colour;

import java.util.*;


import static org.junit.Assert.*;


public class SpectatorTests {

    public class TestSpectator implements Spectator {
        Move move = null;
        public void notify(Move move) {
            this.move = move;
        }
    }

    public class TestPlayer implements Player {
        Move chosenMove;
        public Move notify(int location, List<Move> moves) {
            this.chosenMove = moves.iterator().next();
            return this.chosenMove;
        }
    }

    public class TestPlayer2 implements Player {
        Move chosenMove;
        List<Move> movesToUse;
        int count;


        TestPlayer2(List<Move> movesToUse) {
            this.movesToUse = movesToUse;
            count = 0;
        }

        public Move notify(int location, List<Move> moves) {
            chosenMove = movesToUse.get(count);
            count++;
            return chosenMove;
        }
    }

    @Test
    public void testSpectatorIsNotifiedOfADetectiveMove() throws Exception {
        ScotlandYard game = TestHelper.getGame(1, "test_resources/small_map.txt");
        TestHelper.addMrxToGame(game, 3);

        TestPlayer player = new TestPlayer();

        TestHelper.addDetectiveToGame(game, player, Colour.Blue, 7);

        TestSpectator spectator = new TestSpectator();
        game.spectate(spectator);
        game.turn();
        game.turn();

        assertNotNull("The spectator should have received a move",
                spectator.move);
    }

    @Test
    public void testSpectatorIsNotifiedOfADetectiveMove2() throws Exception {
        ScotlandYard game = TestHelper.getGame(1, "test_resources/small_map.txt");
        TestHelper.addMrxToGame(game, 3);

        TestPlayer player = new TestPlayer();

        TestHelper.addDetectiveToGame(game, player, Colour.Blue, 7);

        TestSpectator spectator = new TestSpectator();
        game.spectate(spectator);
        game.turn();
        game.turn();

        assertEquals("The spectators move should be the same as the detectives chosen move",
                player.chosenMove, spectator.move);
    }


    @Test
    public void testSpectatorIsNotifiedOfAMrXMove() throws Exception {
        ScotlandYard game = TestHelper.getGame(1, "test_resources/small_map.txt");
        TestHelper.addMrxToGame(game, 3);

        TestPlayer player = new TestPlayer();

        TestHelper.addDetectiveToGame(game, player, Colour.Blue, 7);

        TestSpectator spectator = new TestSpectator();
        game.spectate(spectator);
        game.turn();

        assertNotNull("The spectator should have received a mr X move",
                spectator.move);

    }

    @Test
    public void testSpectatorIsGivenMrXsLastKnowLocationIfHeIsHidden() throws Exception {
        List<Boolean> rounds = Arrays.asList(false, false, false);
        ScotlandYard game = TestHelper.getGame(1, rounds, "test_resources/small_map.txt");

        List<Move> movesToUse = new ArrayList<Move>();
        movesToUse.add(new MoveTicket(Colour.Black, 2, Ticket.Bus));
        movesToUse.add(new MoveTicket(Colour.Black, 1, Ticket.Bus));
        movesToUse.add(new MoveTicket(Colour.Black, 6, Ticket.Taxi));
        TestPlayer2 player = new TestPlayer2(movesToUse);


        TestHelper.addMrxToGame(game, player, 3);

        TestHelper.addDetectiveToGame(game, Colour.Blue, 7);

        TestSpectator spectator = new TestSpectator();
        game.spectate(spectator);
        game.turn();
        game.turn();
        game.turn();

        MoveTicket moveTicket = (MoveTicket) spectator.move;


        assertEquals("If Mr X is not currently visible, the location in the move should be " +
                "his last known location", game.getPlayerLocation(Colour.Black), moveTicket.target);
        assertEquals("If Mr X is not currently visible, the location in the move should be " +
                "his last known location", 0, moveTicket.target);
        assertEquals("If Mr X is not currently visible, the location in the move should be " +
                "his last known location", game.getPlayerLocation(Colour.Black), 0);
    }

    @Test
    public void testSpectatorIsGivenCorrectLocationWhenMrXIsVisible() throws Exception {
        List<Boolean> rounds = Arrays.asList(false, true, true);
        ScotlandYard game = TestHelper.getGame(1, rounds, "test_resources/small_map.txt");

        List<Move> movesToUse = new ArrayList<Move>();
        movesToUse.add(new MoveTicket(Colour.Black, 2, Ticket.Bus));
        movesToUse.add(new MoveTicket(Colour.Black, 1, Ticket.Bus));
        movesToUse.add(new MoveTicket(Colour.Black, 6, Ticket.Taxi));
        TestPlayer2 player = new TestPlayer2(movesToUse);


        TestHelper.addMrxToGame(game, player, 3);

        TestHelper.addDetectiveToGame(game, Colour.Blue, 7);

        TestSpectator spectator = new TestSpectator();
        game.spectate(spectator);
        assertEquals("If Mr X is not currently visible, the location in the move should be " +
                "his last known location", game.getPlayerLocation(Colour.Black), 0);
        game.turn();
        assertEquals("If Mr X is not currently visible, the location in the move should be " +
                "his last known location", game.getPlayerLocation(Colour.Black), 2);
        game.turn();
        assertEquals("If Mr X is not currently visible, the location in the move should be " +
                "his last known location", game.getPlayerLocation(Colour.Black), 2);
        game.turn();
        assertEquals("If Mr X is not currently visible, the location in the move should be " +
                "his last known location", game.getPlayerLocation(Colour.Black), 1);
        MoveTicket moveTicket = (MoveTicket) spectator.move;


        assertEquals("If Mr X is currently visible, the location in the move should be " +
                "his current location", ((MoveTicket)player.chosenMove).target, moveTicket.target);
    }

}
