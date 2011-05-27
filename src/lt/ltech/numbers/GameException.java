package lt.ltech.numbers;

/**
 * A general exception used to indicate problems in the game engine.
 * @author Severinas Monkevicius
 */
@SuppressWarnings("serial")
public class GameException extends Exception {
    public GameException(String message) {
        super(message);
    }

    public GameException(Throwable cause) {
        super(cause);
    }

    public GameException(String message, Throwable cause) {
        super(message, cause);
    }
}
