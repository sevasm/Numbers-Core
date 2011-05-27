package lt.ltech.numbers.game;

/**
 * A class that provides information on the rules of the game.
 * @author Severinas Monkevicius
 */
public class GameConfiguration {
    /**
     * The length of numbers that are being guessed.
     * @return the length of numbers that are being guessed.
     */
    public static int numberLength() {
        return 4;
    }

    /**
     * The smallest number that can be used in the game.
     * @return the smallest number that can be used in the game.
     */
    public static int minNumber() {
        return 0;
    }

    /**
     * The biggest number that can be used in the game.
     * @return the biggest number that can be used in the game.
     */
    public static int maxNumber() {
        return 9;
    }
}
