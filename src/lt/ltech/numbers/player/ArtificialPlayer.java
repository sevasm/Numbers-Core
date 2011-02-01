package lt.ltech.numbers.player;

import lt.ltech.numbers.game.GameState;
import lt.ltech.numbers.game.Number;

public interface ArtificialPlayer {
    Number inventNumber();
    Number makeGuess(GameState gameState);
}
