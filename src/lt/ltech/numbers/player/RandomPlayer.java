package lt.ltech.numbers.player;

import lt.ltech.numbers.game.GameState;
import lt.ltech.numbers.game.Number;

public class RandomPlayer implements ArtificialPlayer {
    public Number inventNumber() {
        return this.makeGuess(null);
    }

    public Number makeGuess(GameState gameState) {
        return PlayerUtilities.randomNumber();
    }
}
