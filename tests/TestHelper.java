import scotlandyard.*;
import solution.*;

import java.util.*;
import java.io.IOException;

public class TestHelper
{

    public final static Colour[] colours = { Colour.Black, Colour.Blue, Colour.Green,
            Colour.Red, Colour.White, Colour.Yellow
    };

    public final static int[] locations = {
            5, 15, 63, 67, 98, 121, 140
    };


    public final static int[] mrXTicketNumbers = { 4, 3, 3, 2, 5 };
    public final static int[] detectiveTicketNumbers = { 11, 8, 4, 0, 0 };

    public final static Ticket[] tickets = { Ticket.Taxi, Ticket.Bus,
            Ticket.Underground, Ticket.DoubleMove,
            Ticket.SecretMove };



    public static class SingleMovePlayer implements  Player {
        public MoveTicket chosenMove;
        public Move notify(int location, List<Move> moves) {
            for(Move move : moves) {
                if(move instanceof MoveTicket) {
                    chosenMove = (MoveTicket) move;
                    return move;
                }
            }
            return null;
        }
    }


    public static class DoubleMovePlayer implements  Player {
        public MoveDouble chosenMove;
        public Move notify(int location, List<Move> moves) {
            for(Move move : moves) {
                if(move instanceof MoveDouble) {
                    chosenMove = (MoveDouble) move;
                    return move;
                }
            }
            return null;
        }
    }


    public static SingleMovePlayer getSingleMovePlayer() {
        return new SingleMovePlayer();
    }

    public static DoubleMovePlayer getDoubleMovePlayer() {
        return new DoubleMovePlayer();
    }

    public static List<Boolean> getRounds() {
        List<Boolean> rounds = new ArrayList<Boolean>();
        rounds.add(false);
        rounds.add(false);
        rounds.add(true);
        rounds.add(false);
        rounds.add(false);
        return rounds;
    }


    public static void addMrxToGame(ScotlandYard game, int location) {
        addMrxToGame(game, TestHelper.getPlayer(), location);
    }

    public static void addMrxToGame(ScotlandYard game, Player player, int location) {
        game.join(player, Colour.Black, location, TestHelper.getTickets(true));
    }

    public static void addDetectiveToGame(ScotlandYard game, Player player, Colour colour, int location) {
        game.join(player, colour, location, TestHelper.getTickets(false));
    }

    public static void addDetectiveToGame(ScotlandYard game, Colour colour, int location) {
        addDetectiveToGame(game, TestHelper.getPlayer(), colour, location);
    }




    public static Map<Ticket, Integer> getTickets(boolean mrX)
    {
        Map<Ticket, Integer> tickets = new HashMap<Ticket, Integer>();
        for (int i = 0; i < TestHelper.tickets.length; i++) {
            if(mrX)
                tickets.put(TestHelper.tickets[i], TestHelper.mrXTicketNumbers[i]);
            else
                tickets.put(TestHelper.tickets[i], TestHelper.detectiveTicketNumbers[i]);
        }
        return tickets;
    }

    public static ScotlandYard getGame(int numDetectives, List<Boolean> rounds)
    {
        return getGame(numDetectives, rounds, "resources/graph.txt");
    }

    public static ScotlandYard getGame(int numDetectives, String mapFilename)
    {
        return getGame(numDetectives, TestHelper.getRounds(), mapFilename);
    }

    public static ScotlandYard getGame(int numDetectives, List<Boolean> rounds, String graphFilename) {

        ScotlandYard game = null;
        try
        {
            game = new ScotlandYardModel(numDetectives, rounds, graphFilename);
        }
        catch(IOException e)
        {
            System.err.println(e);
            System.exit(1);
        }

        return game;
    }



    public static ScotlandYard getGame(int numDetectives) {
        return getGame(numDetectives, TestHelper.getRounds(), "resources/graph.txt");
    }


    public static ScotlandYard subscribedGame(int numDetectives, String graphFilename)
    {
        ScotlandYard game = TestHelper.getGame(numDetectives, TestHelper.getRounds(), graphFilename);
        for (int i = 0; i < numDetectives+1; i++) {
            game.join(TestHelper.getPlayer(),
                    colours[i], locations[i],
                    TestHelper.getTickets(colours[i] == Colour.Black));
        }
        return game;
    }


    public static ScotlandYard subscribedGame(int numDetectives)
    {
        return subscribedGame(numDetectives, "resources/graph.txt");
    }

    public static class TestPlayer implements Player {
        public List<Move> moves;
        public int location;

        public Move notify(int location, List<Move> moves) {
            this.moves = moves;
            this.location = location;
            if(moves.size() > 0)
                return this.moves.get(0);
            return null;
        }
    }

    public static Player getPlayer()
    {
        return new SingleMovePlayer();
    }
}
