package lt.ltech.numbers.player;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Random;

import lt.ltech.numbers.GameException;
import lt.ltech.numbers.game.GameConfiguration;
import lt.ltech.numbers.game.Number;

public class PlayerUtilities {
    private static Random r = new Random();

    public static Number randomNumber() {
        int numLength = GameConfiguration.numberLength();
        int minNumber = GameConfiguration.minNumber();
        int maxNumber = GameConfiguration.maxNumber();
        List<Integer> guessList = new ArrayList<Integer>();
        Integer[] guessArray = new Integer[numLength];
        while (guessList.size() < numLength) {
            int randomInt = r.nextInt(maxNumber - minNumber + 1);
            if (!guessList.contains(Integer.valueOf((int) randomInt))) {
                guessList.add((int) randomInt);
            }
        }
        try {
            return new Number(Arrays.asList(guessList.toArray(guessArray)));
        } catch (GameException e) {
            return null;
        }
    }

    public static List<Integer> randomSubset(Collection<Integer> numbers,
            int length) {
        List<Integer> set = new ArrayList<Integer>(numbers);
        List<Integer> subset = new ArrayList<Integer>();
        while (subset.size() < length) {
            int i = r.nextInt(set.size());
            Integer number = set.get(i);
            set.remove(number);
            subset.add(number);
        }
        return subset;
    }
}
