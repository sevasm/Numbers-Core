package lt.ltech.numbers.game;

import java.io.Serializable;

/**
 * An answer is the combination of two numbers - the amount of digits present in
 * the number being guessed and the amount of digits that are in the correcto
 * position.
 * @author Severinas Monkevicius
 */
@SuppressWarnings("serial")
public class Answer implements Serializable {
    private final int present;
    private final int correct;

    private Answer(int present, int correct) {
        this.present = present;
        this.correct = correct;
    }

    /**
     * Creates a new Answer based on a number that is being guessed and on the
     * guess.
     * @param number the number that is being guessed.
     * @param guess the guess.
     * @return a new answer that will contain the number of digits in the guess
     *         that are also in the number being guessed and how many of those
     *         digits are in the correct position.
     */
    public static Answer answer(Number number, Number guess) {
        int present = 0;
        int correct = 0;
        for (int i = 0; i < number.getNumber().size(); i++) {
            int gDigit = guess.getNumber().get(i);
            if (number.getNumber().contains(gDigit)) {
                present++;
            } else {
                // If the current digit is not in the current number,
                // move on to the next digit
                continue;
            }
            int nDigit = number.getNumber().get(i);
            if (gDigit == nDigit) {
                correct++;
            }
        }
        return new Answer(present, correct);
    }

    /**
     * Returns the number of digits that were present both in the guess and in
     * the number being guessed.
     * @return the number of digits that were present both in the guess and in
     *         the number being guessed.
     */
    public int getPresent() {
        return present;
    }

    /**
     * Returns the number of digits that were not only present in both the guess
     * and in the number being guessed, but were also in their correct position.
     * @return the number of digits that were not only present in both the guess
     *         and in the number being guessed, but were also in their correct
     *         position.
     */
    public int getCorrect() {
        return correct;
    }

    public String toString() {
        return String.format("%d:%d", this.present, this.correct);
    }
}
