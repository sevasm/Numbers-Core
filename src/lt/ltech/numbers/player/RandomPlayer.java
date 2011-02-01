package lt.ltech.numbers.player;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import lt.ltech.numbers.GameException;
import lt.ltech.numbers.game.GameConfiguration;
import lt.ltech.numbers.game.GameState;
import lt.ltech.numbers.game.Number;

public class RandomPlayer implements ArtificialPlayer {
    public Number inventNumber() {
        return this.makeGuess(null);
    }

    public Number makeGuess(GameState gameState) {
        Random r = new Random();
        byte numLength = GameConfiguration.numberLength();
        byte minNumber = GameConfiguration.minNumber();
        byte maxNumber = GameConfiguration.maxNumber();
        List<Byte> guessList = new ArrayList<Byte>();
        Byte[] guessArray = new Byte[numLength];
        while (guessList.size() < numLength) {
            int randomInt = r.nextInt(maxNumber - minNumber + 1);
            if (!guessList.contains(Byte.valueOf((byte) randomInt))) {
                guessList.add((byte) randomInt);
            }
        }
        try {
            return new Number(Arrays.asList(guessList.toArray(guessArray)));
        } catch (GameException e) {
            return null;
        }
    }
}
