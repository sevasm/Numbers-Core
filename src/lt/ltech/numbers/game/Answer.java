package lt.ltech.numbers.game;

import java.io.Serializable;

@SuppressWarnings("serial")
public class Answer implements Serializable {
    private final int present;
    private final int correct;

    private Answer(int present, int correct) {
        this.present = present;
        this.correct = correct;
    }

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

    public int getPresent() {
        return present;
    }

    public int getCorrect() {
        return correct;
    }

    public String toString() {
        return String.format("%d:%d", this.present, this.correct);
    }
}
