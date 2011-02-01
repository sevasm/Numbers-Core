package lt.ltech.numbers.game;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import lt.ltech.numbers.GameException;
import lt.ltech.numbers.player.Player;

public class Round {
    private final Map<Player, Number> guesses;
    private final Map<Player, Answer> answers;

    public Round() {
        this.guesses = new HashMap<Player, Number>();
        this.answers = new HashMap<Player, Answer>();
    }

    public Map<Player, Number> getGuesses() {
        return Collections.unmodifiableMap(this.guesses);
    }

    public Map<Player, Answer> getAnswers() {
        return Collections.unmodifiableMap(this.answers);
    }

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
