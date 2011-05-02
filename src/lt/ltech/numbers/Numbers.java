package lt.ltech.numbers;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import lt.ltech.numbers.game.GameState;
import lt.ltech.numbers.game.GameStep;
import lt.ltech.numbers.game.Number;
import lt.ltech.numbers.game.Round;
import lt.ltech.numbers.player.ArtificialPlayer;
import lt.ltech.numbers.player.ConsolePlayer;
import lt.ltech.numbers.player.DefaultPlayer;
import lt.ltech.numbers.player.Player;

import org.apache.log4j.Logger;

public class Numbers {
    private GameState gs;
    private Player humanPlayer;
    private Player computerPlayer;
    private Map<Player, ArtificialPlayer> players;
    private Logger logger = Logger.getRootLogger();

    public static void main(String[] args) throws GameException {
        playGame();
    }

    public static void playGame() throws GameException {
        Numbers n = new Numbers();

        n.gs = new GameState();

        n.humanPlayer = new Player(UUID.randomUUID(), "Human Player");
        n.humanPlayer.setId(1l);
        n.computerPlayer = new Player(UUID.randomUUID(), "Computer Player");
        n.computerPlayer.setId(2l);
        n.players = new HashMap<Player, ArtificialPlayer>();
        n.players.put(n.humanPlayer, new ConsolePlayer());
        n.players.put(n.computerPlayer, new DefaultPlayer(n.computerPlayer));
        n.gs.addPlayer(n.humanPlayer);
        n.gs.addPlayer(n.computerPlayer);

        while (n.gs.getGameStep().type() != GameStep.Type.GAME_OVER) {
            n.handleGameStep(n.gs.getGameStep());
        }
        n.logger.info(String.format("Game over, %s has won", n.gs.getWinner()));
    }

    private void handleGameStep(GameStep step) throws GameException {
        Player p = step.player();
        switch (step.type()) {
            case SET_NUMBER: {
                Number n = this.players.get(p).inventNumber();
                this.gs.setNumber(p, n);
                this.logger.info(String.format("%s has selected %s", p, n));
                break;
            }
            case GUESS: {
                Number g = this.players.get(p).makeGuess(this.gs);
                this.gs.guess(p, g);
                Round r = this.gs.getLastRound();
                this.logger.info(String.format("%s guesses %s %s", p, r
                        .getGuesses().get(p), r.getAnswers().get(p)));
                break;
            }
        }
    }
}
