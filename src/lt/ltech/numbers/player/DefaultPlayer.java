package lt.ltech.numbers.player;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import lt.ltech.numbers.GameException;
import lt.ltech.numbers.game.Answer;
import lt.ltech.numbers.game.GameConfiguration;
import lt.ltech.numbers.game.GameState;
import lt.ltech.numbers.game.Number;
import lt.ltech.numbers.game.Round;

/**
 * A behemoth of a class that implements the default computer player behaviour.
 * Can be improved upon in about a million ways, but currently it can solve the
 * game in an average of 9 guesses, which is not bad at all.
 * @author Severinas Monkevicius
 */
public class DefaultPlayer implements ArtificialPlayer {
    private final Player player;

    private final List<Pool<Integer>> pools;
    private final Set<Integer> notUsed;
    private final Set<Integer> used;

    private final int length = GameConfiguration.numberLength();
    private final int min = GameConfiguration.minNumber();
    private final int max = GameConfiguration.maxNumber();

    private GameState gameState;

    private final Comparator<Integer> reverseIntegerComparator = new Comparator<Integer>() {
        @Override
        public int compare(Integer o1, Integer o2) {
            return o2 - o1;
        }
    };

    public DefaultPlayer(Player player) {
        this.player = player;

        pools = new ArrayList<Pool<Integer>>();
        pools.add(createInitialPool());
        Collections.sort(pools);

        this.notUsed = new HashSet<Integer>();
        this.used = new HashSet<Integer>();
    }

    @Override
    public Number inventNumber() {
        return PlayerUtilities.randomNumber();
    }

    @Override
    public Number makeGuess(GameState gameState) {
        this.gameState = gameState;
        Round lastRound = getLastRound(gameState);
        if (lastRound != null) {
            updatePools(lastRound);
        }
        try {
            return makeGuess();
        } catch (GameException e) {
            e.printStackTrace();
            return null;
        }
    }

    private Number makeGuess() throws GameException {
        List<Integer> guess = new ArrayList<Integer>();

        if (used.size() == length) {
            return createNumber(new ArrayList<Integer>(used));
        }

        // Find all pools longer or equal to the target length
        // These pools will be used as the base of a guess
        Set<Pool<Integer>> longer = new TreeSet<Pool<Integer>>();
        for (Pool<Integer> pool: pools) {
            if (pool.getPool().size() >= length) {
                longer.add(pool);
            }
        }

        // Find all pools shorter than the target length
        // These pools can be used as filler
        Set<Pool<Integer>> shorter = new TreeSet<Pool<Integer>>();
        for (Pool<Integer> pool: pools) {
            if (pool.getPool().size() < length) {
                shorter.add(pool);
            }
        }

        // Create a list of available filler lengths
        Set<Integer> filler = new TreeSet<Integer>(reverseIntegerComparator);
        for (Pool<Integer> pool: shorter) {
            filler.add(pool.getPool().size());
            for (int i = 1; i < used.size() + notUsed.size() + 1; i++) {
                filler.add(pool.getPool().size() + i);
            }
        }
        // Any known used or unused numbers can be used as filler
        for (int i = 0; i < used.size() + notUsed.size() + 1; i++) {
            filler.add(i);
        }

        // Build up the number to guess - take the longest pool and see if it
        // can be split and filled with some filler
        for (Pool<Integer> pool: longer) {
            int rootLen = pool.getPool().size() / 2;

            int multiplier = -1;
            double add = 0;
            int len = rootLen;
            while (len > 0 && len < pool.getPool().size()) {
                int fillerLen = length - len;

                // Is the right amount of filler available
                if (len < length && filler.contains(fillerLen)) {
                    guess.addAll(PlayerUtilities.randomSubset(pool.getPool(),
                            len));

                    // Get the required filler

                    // Try the pools first
                    for (Pool<Integer> shortPool: shorter) {
                        if (shortPool.getPool().size() == fillerLen) {
                            guess.addAll(PlayerUtilities.randomSubset(
                                    shortPool.getPool(), fillerLen));
                            return createNumber(guess);
                        }
                    }

                    // Use known used/unused numbers as filler
                    for (Integer b: used) {
                        if (guess.size() < length) {
                            guess.add(b);
                        } else {
                            break;
                        }
                    }
                    for (Integer b: notUsed) {
                        if (guess.size() < length) {
                            guess.add(b);
                        } else {
                            break;
                        }
                    }
                    return createNumber(guess);
                }

                multiplier *= -1;
                add += 0.5;
                len = rootLen + (multiplier * (int) Math.ceil(add));
            }

            // No valid filler was found - just use the pool anyway as splitting
            // large pools is a priority
            guess.addAll(PlayerUtilities.randomSubset(pool.getPool(), length));
            return createNumber(guess);
        }

        // No long pool was available to split up and fill, split a short pool
        for (Pool<Integer> pool: shorter) {
            int rootLen = pool.getPool().size() / 2;

            int multiplier = -1;
            double add = 0;
            int len = rootLen;
            while (len > 0 && len < pool.getPool().size()) {
                int fillerLen = length - len;

                if (len < length && filler.contains(fillerLen)) {
                    // Find filler first - maybe it refers to the pool itself
                    Pool<Integer> fillerPool = null;
                    for (Pool<Integer> shortPool: shorter) {
                        if (!pool.equals(shortPool)
                                && shortPool.getPool().size() >= (fillerLen - (used
                                        .size() + notUsed.size()))) {
                            fillerPool = shortPool;
                        }
                    }

                    List<Integer> knownFiller = new ArrayList<Integer>();
                    if (fillerPool == null) {
                        // Use known used/unused numbers as filler
                        for (Integer b: used) {
                            if (knownFiller.size() < fillerLen) {
                                knownFiller.add(b);
                            } else {
                                break;
                            }
                        }
                        for (Integer b: notUsed) {
                            if (knownFiller.size() < fillerLen) {
                                knownFiller.add(b);
                            } else {
                                break;
                            }
                        }
                        // Was there enough filler in the known numbers?
                        if (knownFiller.size() == fillerLen) {
                            guess.addAll(PlayerUtilities.randomSubset(
                                    pool.getPool(), len));
                            guess.addAll(PlayerUtilities.randomSubset(
                                    knownFiller, fillerLen));
                            return createNumber(guess);
                        }
                    } else {
                        guess.addAll(PlayerUtilities.randomSubset(
                                pool.getPool(), len));
                        guess.addAll(PlayerUtilities.randomSubset(fillerPool
                                .getPool(), Math.min(fillerLen, fillerPool
                                .getPool().size())));

                        for (Integer b: used) {
                            if (guess.size() < length) {
                                guess.add(b);
                            } else {
                                break;
                            }
                        }
                        for (Integer b: notUsed) {
                            if (guess.size() < length) {
                                guess.add(b);
                            } else {
                                break;
                            }
                        }

                        return createNumber(guess);
                    }
                }

                multiplier *= -1;
                add += 0.5;
                len = rootLen + (multiplier * (int) Math.ceil(add));
            }
        }

        if (guess.size() != length) {
            throw new RuntimeException("I'm out of ideas!");
        }
        return createNumber(guess);
    }

    private void updatePools(Round round) {
        Number guess = round.getGuesses().get(player);
        Answer answer = round.getAnswers().get(player);

        List<Integer> digits = new ArrayList<Integer>();
        digits.addAll(guess.getNumber());
        Collections.sort(digits);

        int score = answer.getPresent();

        pools.add(new Pool<Integer>(digits, score));
        Collections.sort(pools);

        while (consolidatePools())
            ;
    }

    private boolean consolidatePools() {
        // check if any pools contain known in numbers
        for (int j = 0; j < pools.size(); ++j) {
            Pool<Integer> pool = pools.get(j);
            for (Integer i: used) {
                if (pool.getPool().contains(i)) {
                    pool.getPool().remove(pool.getPool().indexOf(i));
                    pool.setNum(pool.getNum() - 1);
                    if (checkPool(pool)) {
                        return true;
                    }
                }
            }
        }

        // check if any pools contain known out numbers
        for (int j = 0; j < pools.size(); ++j) {
            Pool<Integer> pool = pools.get(j);
            for (Integer o: notUsed) {
                if (pool.getPool().contains(o)) {
                    pool.getPool().remove(pool.getPool().indexOf(o));
                    if (checkPool(pool)) {
                        return true;
                    }
                }
            }
        }

        // check if pools contain other pools and split them if so
        for (int i = 0; i < pools.size(); ++i) {
            for (int j = 0; j < pools.size(); ++j) {
                if (i == j) {
                    continue;
                }
                Pool<Integer> pool1 = pools.get(i);
                Pool<Integer> pool2 = pools.get(j);
                if (pool1.contains(pool2)) {
                    for (Integer p2Int: pool2.getPool()) {
                        pool1.getPool().remove(pool1.getPool().indexOf(p2Int));
                    }
                    pool1.setNum(pool1.getNum() - pool2.getNum());
                    checkPool(pool1);
                    return true;
                }
            }
        }

        for (int i = 0; i < pools.size(); ++i) {
            if (checkPool(pools.get(i))) {
                i = 0;
            }
        }

        return false;
    }

    private boolean checkPool(Pool<Integer> pool) {
        // if pool is empty, remove it
        if (pool.getPool().size() == 0) {
            pools.remove(pool);
            return true;
        } else {

            // if pool's score is 0, add all it's numbers to OUT
            if (pool.getNum() == 0) {
                for (Integer outNum: pool.getPool()) {
                    notUsed.add(outNum);
                }
                pools.remove(pool);
                return true;
            }

            // if pool's score == it's size, add all it's numbers to IN
            if (pool.getPool().size() == pool.getNum()) {
                for (Integer inNum: pool.getPool()) {
                    used.add(inNum);
                }
                pools.remove(pool);
                return true;
            }
        }

        return false;
    }

    /**
     * Rearranges the ditits in a number to try and get the order of them right.
     * @param digits the digits that comprise the number.
     * @return a number with digits arranged so as to try and get their order
     *         right.
     * @throws GameException if an error is encountered
     */
    private Number createNumber(List<Integer> digits) throws GameException {
        List<Integer> arranged = new ArrayList<Integer>();
        while (arranged.size() < digits.size()) {
            arranged.add(null);
        }

        List<Integer> known = new ArrayList<Integer>();
        List<Integer> unknown = new ArrayList<Integer>();
        for (Integer i: digits) {
            if (used.contains(i)) {
                known.add(i);
            } else {
                unknown.add(i);
            }
        }
        known = PlayerUtilities.randomSubset(known, known.size());
        unknown = PlayerUtilities.randomSubset(unknown, unknown.size());

        outer: for (Integer i: known) {
            Collection<Integer> use = getUsedPositions(i);
            for (Integer u: use) {
                if (arranged.get(u) == null) {
                    arranged.set(u, i);
                    continue outer;
                }
            }
        }
        for (Integer i: known) {
            if (!arranged.contains(i)) {
                Collection<Integer> dontUse = getNotUsedPositions(i);
                Set<Integer> validPositions = new HashSet<Integer>();
                for (int j = 0; j < arranged.size(); j++) {
                    if (arranged.get(j) == null && !dontUse.contains(j)) {
                        validPositions.add(j);
                    }
                }
                if (validPositions.size() > 0) {
                    int validPosition = PlayerUtilities.randomSubset(
                            validPositions, 1).get(0);
                    arranged.set(validPosition, i);
                }
            }
        }
        for (Integer i: known) {
            if (!arranged.contains(i)) {
                unknown.add(i);
            }
        }

        unknown = PlayerUtilities.randomSubset(unknown, unknown.size());
        for (Integer i: unknown) {
            for (int j = 0; j < arranged.size(); j++) {
                if (arranged.get(j) == null) {
                    arranged.set(j, i);
                    break;
                }
            }
        }
        return new Number(arranged);
    }

    private Collection<Integer> getUsedPositions(Integer i) {
        Set<Integer> positions = new HashSet<Integer>();
        for (Round round: gameState.getRounds()) {
            Number guess = round.getGuesses().get(player);
            Answer answer = round.getAnswers().get(player);
            if (guess != null && answer != null
                    && guess.getNumber().contains(i)) {
                int present = answer.getPresent();
                int correct = answer.getCorrect();
                if (present == correct) {
                    int position = guess.getNumber().indexOf(i);
                    positions.add(position);
                }
            }
        }
        return positions;
    }

    private Collection<Integer> getNotUsedPositions(Integer i) {
        Set<Integer> positions = new HashSet<Integer>();
        for (Round round: gameState.getRounds()) {
            Number guess = round.getGuesses().get(player);
            Answer answer = round.getAnswers().get(player);
            if (guess != null && answer != null
                    && guess.getNumber().contains(i)) {
                int correct = answer.getCorrect();
                if (correct == 0) {
                    int position = guess.getNumber().indexOf(i);
                    positions.add(position);
                }
            }
        }
        return positions;
    }

    private Pool<Integer> createInitialPool() {
        List<Integer> initialList = new ArrayList<Integer>(max - min + 1);
        for (int i = (int) min; i <= max; i++) {
            initialList.add(i);
        }
        Collections.sort(initialList);
        return new Pool<Integer>(initialList, length);
    }

    /**
     * Retrieves the last round that this player has already played in the game.
     * @param gameState the game state.
     * @return the last round that the current player has already made a guess
     *         in.
     */
    private Round getLastRound(GameState gameState) {
        Round round = null;
        List<Round> rounds = gameState.getRounds();
        for (int i = rounds.size() - 1; i >= 0; i--) {
            Round r = rounds.get(i);
            Number guess = r.getGuesses().get(player);
            Answer answer = r.getAnswers().get(player);
            if (guess != null && answer != null) {
                round = r;
                break;
            }
        }
        return round;
    }

    public String toString() {
        String ls = System.getProperty("line.separator", "\n");
        StringBuilder sb = new StringBuilder();
        sb.append(String.format("%-10s", "Used: ")).append(used).append(ls);
        sb.append(String.format("%-10s", "Not used: ")).append(notUsed)
                .append(ls);
        sb.append(String.format("%-10s", "Pools: ")).append(pools);
        return sb.toString();
    }
}
