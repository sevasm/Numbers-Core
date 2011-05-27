package lt.ltech.numbers.game;

import java.io.Serializable;

import lt.ltech.numbers.player.Player;

/**
 * A game step is used to determine what actions the game expects to happen
 * next. Every game step is a combination of a type which determines what kind
 * of action should be taken next and a player that determines which player is
 * expected to take this action.
 * @author Severinas Monkevicius
 */
@SuppressWarnings("serial")
public class GameStep implements Serializable {
    /**
     * The types of game steps.
     * @author Severinas Monkevicius
     */
    public enum Type implements Serializable {
        INITIAL, SET_NUMBER, GUESS, GAME_OVER
    }

    private final Type type;
    private final Player player;

    /**
     * Creates a new game step with a given type and a given player.
     * @param type the type.
     * @param player the player.
     */
    public GameStep(Type type, Player player) {
        this.type = type;
        this.player = player;
    }

    /**
     * Creates a new game step with a given type.
     * @param type the type.
     */
    public GameStep(Type type) {
        this(type, null);
    }

    /**
     * Returns the type of the game step.
     * @return the type of the game step.
     */
    public Type type() {
        return this.type;
    }

    /**
     * Returns the player that is expected to take the next action in the game.
     * @return the player that is expected to take the next action in the game.
     */
    public Player player() {
        return this.player;
    }

    /**
     * Returns whether this game step needs a player.
     * @return whether this game step needs a player.
     */
    public boolean expectsPlayer() {
        return this.type == Type.SET_NUMBER || this.type == Type.GUESS;
    }

    public String toString() {
        if (this.expectsPlayer()) {
            return String.format("%s - %s", this.type, this.player);
        } else {
            return String.format("%s", this.type);
        }
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof GameStep)) {
            return false;
        }
        GameStep gs = (GameStep) o;
        if (this.type != gs.type) {
            return false;
        }
        if (this.expectsPlayer()) {
            return this.player.equals(gs.player);
        }
        return true;
    }
}
