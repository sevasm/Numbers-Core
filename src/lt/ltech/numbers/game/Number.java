package lt.ltech.numbers.game;

import java.io.Serializable;
import java.util.Collections;
import java.util.List;

import lt.ltech.numbers.GameException;

@SuppressWarnings("serial")
public class Number implements Serializable {
    private final List<Byte> number;

    public Number(List<Byte> number) throws GameException {
        this.validateNumber(number);
        this.number = Collections.unmodifiableList(number);
    }

    public List<Byte> getNumber() {
        return this.number;
    }

    private void validateNumber(List<Byte> number) throws GameException {
        int size = number.size();
        int numberLength = GameConfiguration.numberLength();
        if (size != numberLength) {
            throw new GameException(String.format(
                    "Illegal number length - %d instead of %d", size,
                    numberLength));
        }

        byte min = GameConfiguration.minNumber();
        byte max = GameConfiguration.maxNumber();
        for (Byte digit: number) {
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
        for (Byte digit: number) {
            sb.append(digit.toString());
        }
        return sb.toString();
    }
}
