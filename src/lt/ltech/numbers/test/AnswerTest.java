package lt.ltech.numbers.test;

import java.util.Arrays;

import junit.framework.TestCase;
import lt.ltech.numbers.GameException;
import lt.ltech.numbers.game.Answer;
import lt.ltech.numbers.game.Number;

public class AnswerTest extends TestCase {
    public void testAnswer() throws GameException {
        Integer[] numberArray = { 1, 3, 7, 0 };
        Number number = new Number(Arrays.asList(numberArray));

        this.makeGuess(number, new Integer[] { 1, 3, 9, 2 }, (int) 2, (int) 2);
        this.makeGuess(number, new Integer[] { 2, 4, 6, 8 }, (int) 0, (int) 0);
        this.makeGuess(number, new Integer[] { 0, 9, 7, 2 }, (int) 2, (int) 1);
        this.makeGuess(number, new Integer[] { 1, 3, 7, 0 }, (int) 4, (int) 4);
    }

    private void makeGuess(Number number, Integer[] guessArray, int present,
            int correct) throws GameException {
        Number guess = new Number(Arrays.asList(guessArray));
        Answer answer = Answer.answer(number, guess);
        assertEquals(present, answer.getPresent());
        assertEquals(correct, answer.getCorrect());
    }
}
