package lt.ltech.numbers.player;

import lt.ltech.numbers.game.GameState;
import lt.ltech.numbers.game.Number;

/**
 * An interface that describes the AI for the numbers game.
 * @author Severinas Monkevicius
 */
public interface ArtificialPlayer {
    /**
     * This is called once per player per game and the number returned by it is
     * what the opponent of this player will try to guess.
     * @return a number.
     */
    Number inventNumber();

    /**
     * This is called once per round per player and the number returned by it
     * will be used as this player's next guess.
     * @param gameState the state of the current game, which can be used to
     *        determine what the next guess should be.
     * @return the number that will be used as the next guess for this player.
     */
    Number makeGuess(GameState gameState);
}
