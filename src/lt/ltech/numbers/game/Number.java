package lt.ltech.numbers.game;

import java.io.Serializable;
import java.util.Collections;
import java.util.List;

import lt.ltech.numbers.GameException;

@SuppressWarnings("serial")
public class Number implements Serializable {
    private final List<Integer> number;

    public Number(List<Integer> number) throws GameException {
        this.validateNumber(number);
        this.number = Collections.unmodifiableList(number);
    }

    public List<Integer> getNumber() {
        return this.number;
    }

    private void validateNumber(List<Integer> number) throws GameException {
        int size = number.size();
        int numberLength = GameConfiguration.numberLength();
        if (size != numberLength) {
            throw new GameException(String.format(
                    "Illegal number length - %d instead of %d", size,
                    numberLength));
        }

        int min = GameConfiguration.minNumber();
        int max = GameConfiguration.maxNumber();
        for (Integer digit: number) {
            if (digit == null) {
                throw new GameException("Illegal digit - null");
            }
            if (digit < min || digit > max) {
                throw new GameException(String.format("Illegal digit - %d; "
                        + "should be between %d and %d", digit, min, max));
            }
        }
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (Integer digit: number) {
            sb.append(digit.toString());
        }
        return sb.toString();
    }
}
