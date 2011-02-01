package lt.ltech.numbers.test;

import java.util.Arrays;

import junit.framework.TestCase;
import lt.ltech.numbers.GameException;
import lt.ltech.numbers.game.Answer;
import lt.ltech.numbers.game.Number;

public class AnswerTest extends TestCase {
    public void testAnswer() throws GameException {
        Byte[] numberArray = { 1, 3, 7, 0 };
        Number number = new Number(Arrays.asList(numberArray));

        this.makeGuess(number, new Byte[] { 1, 3, 9, 2 }, (byte) 2, (byte) 2);
        this.makeGuess(number, new Byte[] { 2, 4, 6, 8 }, (byte) 0, (byte) 0);
        this.makeGuess(number, new Byte[] { 0, 9, 7, 2 }, (byte) 2, (byte) 1);
        this.makeGuess(number, new Byte[] { 1, 3, 7, 0 }, (byte) 4, (byte) 4);
    }

    private void makeGuess(Number number, Byte[] guessArray, byte present,
            byte correct) throws GameException {
        Number guess = new Number(Arrays.asList(guessArray));
        Answer answer = Answer.answer(number, guess);
        assertEquals(present, answer.getPresent());
        assertEquals(correct, answer.getCorrect());
    }
}
