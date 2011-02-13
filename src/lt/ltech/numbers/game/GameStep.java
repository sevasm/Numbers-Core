package lt.ltech.numbers.game;

import java.io.Serializable;

import lt.ltech.numbers.player.Player;

@SuppressWarnings("serial")
public class GameStep implements Serializable {
    public enum Type implements Serializable {
        INITIAL, SET_NUMBER, GUESS, GAME_OVER
    }

    private final Type type;
    private final Player player;

    public GameStep(Type type, Player player) {
        this.type = type;
        this.player = player;
    }

    public GameStep(Type type) {
        this(type, null);
    }

    public Type type() {
        return this.type;
    }

    public Player player() {
        return this.player;
    }

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
