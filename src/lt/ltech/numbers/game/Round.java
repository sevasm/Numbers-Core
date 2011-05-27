package lt.ltech.numbers.game;

import java.io.Serializable;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import lt.ltech.numbers.GameException;
import lt.ltech.numbers.player.Player;

/**
 * A round is a collection of guesses and answers grouped by players they belong
 * to.
 * @author Severinas Monkevicius
 */
@SuppressWarnings("serial")
public class Round implements Serializable {
    private final Map<Player, Number> guesses;
    private final Map<Player, Answer> answers;

    public Round() {
        this.guesses = new HashMap<Player, Number>();
        this.answers = new HashMap<Player, Answer>();
    }

    /**
     * Returns all guesses made during this round.
     * @return all guesses made during this round.
     */
    public Map<Player, Number> getGuesses() {
        return Collections.unmodifiableMap(this.guesses);
    }

    /**
     * Returns the answers in this round.
     * @return the answers in this round.
     */
    public Map<Player, Answer> getAnswers() {
        return Collections.unmodifiableMap(this.answers);
    }

    /**
     * Adds a new guess made by a player against another player.
     * @param player the player making the guess.
     * @param opponent the player against whom the guess is being made.
     * @param guess the guess.
     * @throws GameException if the guess cannot be made.
     */
    public void addGuess(Player player, Player opponent, Number guess)
            throws GameException {
        if (this.guesses.containsKey(player)) {
            throw new GameException(String.format("%s "
                    + "has already made a guess this round", player));
        }
        this.guesses.put(player, guess);
        Answer answer = Answer.answer(opponent.getNumber(), guess);
        this.answers.put(player, answer);
    }
}
