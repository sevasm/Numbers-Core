package lt.ltech.numbers.player;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;

import lt.ltech.numbers.GameException;
import lt.ltech.numbers.game.GameState;
import lt.ltech.numbers.game.Number;

import org.apache.log4j.Logger;

public class ConsolePlayer implements ArtificialPlayer {
    private Logger logger = Logger.getRootLogger();

    public Number inventNumber() {
        return this.makeGuess(null);
    }

    public Number makeGuess(GameState gameState) {
        Number n = null;
        do {
            n = this.readGuess();
        } while (n == null);
        return n;
    }

    public Number readGuess() {
        Number guess = null;
        System.out.print("Please enter your guess: ");
        BufferedReader br = null;
        try {
            br = new BufferedReader(new InputStreamReader(System.in));
            String line = br.readLine();
            String[] splitLine = line.split("");
            Byte[] guessArray = new Byte[splitLine.length - 1];
            for (int i = 1; i < splitLine.length; i++) {
                guessArray[i - 1] = Byte.valueOf(splitLine[i]);
            }
            guess = new Number(Arrays.asList(guessArray));
        } catch (NumberFormatException e) {
            logger.info(e);
            System.out.println("The number you have entered "
                    + "contains non-digit characters");
        } catch (GameException e) {
            logger.info(e);
            System.out.println("The number you have entered was invalid: "
                    + e.getMessage());
        } catch (IOException e) {
            logger.info(e);
            System.out.println("An error has occured "
                    + "while reading from the console: " + e.getMessage());
        }
        return guess;
    }
}
