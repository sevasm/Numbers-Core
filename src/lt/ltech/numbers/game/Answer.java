package lt.ltech.numbers.game;

public class Answer {
    private final byte present;
    private final byte correct;

    private Answer(byte present, byte correct) {
        this.present = present;
        this.correct = correct;
    }

    public static Answer answer(Number number, Number guess) {
        byte present = 0;
        byte correct = 0;
        for (int i = 0; i < number.getNumber().size(); i++) {
            byte gDigit = guess.getNumber().get(i);
            if (number.getNumber().contains(gDigit)) {
                present++;
            } else {
                // If the current digit is not in the current number,
                // move on to the next digit
                continue;
            }
            byte nDigit = number.getNumber().get(i);
            if (gDigit == nDigit) {
                correct++;
            }
        }
        return new Answer(present, correct);
    }

    public byte getPresent() {
        return present;
    }

    public byte getCorrect() {
        return correct;
    }

    public String toString() {
        return String.format("%d:%d", this.present, this.correct);
    }
}
