package lt.ltech.numbers.game;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import lt.ltech.numbers.GameException;
import lt.ltech.numbers.player.Player;

/**
 * This class maintains the state of the game. More specifically it tracks the
 * players in the game and the rounds played.
 * @author Severinas Monkevicius
 */
@SuppressWarnings("serial")
public class GameState implements Serializable {
    private List<Round> rounds;
    private GameStep gameStep;
    private List<Player> players;

    /**
     * Creates a new game state or simply a new game.
     */
    public GameState() {
        this.players = new ArrayList<Player>();
        this.rounds = new ArrayList<Round>();
        this.gameStep = new GameStep(GameStep.Type.INITIAL);
    }

    /**
     * Adds a new player to the game. Players can only be added until the last
     * player already added has picked their number.
     * @param player the player to add.
     * @throws GameException if the player canno be added.
     */
    public void addPlayer(Player player) throws GameException {
        if (this.gameStep.type() == GameStep.Type.GUESS
                || this.gameStep.type() == GameStep.Type.GAME_OVER) {
            throw new GameException("Cannot add a new player "
                    + "to a game that has already started");
        }
        if (this.gameStep.type() == GameStep.Type.INITIAL) {
            this.gameStep = new GameStep(GameStep.Type.SET_NUMBER, player);
        }
        player.setIndex((int) this.players.size());
        this.players.add(player);
    }

    /**
     * Assigns a number to a player.
     * @param player the player to whom the number is to be assigned.
     * @param number the number to assign.
     * @return the next step in the current game.
     * @throws GameException if the number cannot be assigned.
     */
    public GameStep setNumber(Player player, Number number)
            throws GameException {
        GameStep gs = new GameStep(GameStep.Type.SET_NUMBER, player);
        this.checkStep(gs, this.gameStep);

        int playerIndex = this.players.indexOf(player);
        this.players.get(playerIndex).setNumber(number);

        GameStep.Type nextGameStepType = null;
        Player nextPlayer = null;
        if (playerIndex == this.players.size() - 1) {
            nextGameStepType = GameStep.Type.GUESS;
            nextPlayer = this.players.get(0);
        } else {
            nextGameStepType = GameStep.Type.SET_NUMBER;
            nextPlayer = this.players.get(this.players.indexOf(player) + 1);
        }
        this.gameStep = new GameStep(nextGameStepType, nextPlayer);

        return this.gameStep;
    }

    /**
     * Makes a guess.
     * @param player the player making the guess.
     * @param guess the guess to make.
     * @return the next step in the current game.
     * @throws GameException if the guess cannot be made.
     */
    public GameStep guess(Player player, Number guess) throws GameException {
        GameStep gs = new GameStep(GameStep.Type.GUESS, player);
        this.checkStep(gs, this.gameStep);

        Round r = this.getLastRound();
        if (r != null) {
            Map<Player, Number> guesses = r.getGuesses();
            if (r.getGuesses().containsKey(player)) {
                if (this.players.size() == guesses.size()) {
                    r = new Round();
                    this.rounds.add(r);
                } else {
                    throw new GameException(String.format(
                            "%s has already made a guess this round", player));
                }
            }
        } else {
            r = new Round();
            this.rounds.add(r);
        }

        int playerIndex = this.players.indexOf(player);
        int opponentIndex = playerIndex + 1;
        if (opponentIndex == this.players.size()) {
            opponentIndex = 0;
        }
        Player opponent = this.players.get(opponentIndex);

        r.addGuess(player, opponent, guess);

        if (this.isGameOver()) {
            this.gameStep = new GameStep(GameStep.Type.GAME_OVER);
        } else {
            Player nextPlayer = null;
            if (playerIndex == this.players.size() - 1) {
                nextPlayer = this.players.get(0);
            } else {
                nextPlayer = this.players.get(playerIndex + 1);
            }
            this.gameStep = new GameStep(GameStep.Type.GUESS, nextPlayer);
        }

        return this.gameStep;
    }

    /**
     * Returns the current step in this game.
     * @return the current step in this game.
     */
    public GameStep getGameStep() {
        return this.gameStep;
    }

    /**
     * Returns the last round in the current game.
     * @return the last round in the current game or <code>null</code> if no
     *         rounds have taken place.
     */
    public Round getLastRound() {
        if (this.rounds.size() == 0) {
            return null;
        }
        return this.rounds.get(this.rounds.size() - 1);
    }

    /**
     * Validates a game step.
     * @param expected the expected game step.
     * @param actual the actual game step.
     * @throws IllegalStateException if the actual step is invalid.
     */
    private void checkStep(GameStep expected, GameStep actual) {
        if (!expected.equals(actual)) {
            throw new IllegalStateException(String.format(
                    "Illegal step - %s; expected %s", actual, expected));
        }
    }

    /**
     * Returns whether the game is over.
     * @return <code>true</code> if the game has a winner; <code>false</code>
     *         otherwise.
     */
    public boolean isGameOver() {
        return this.getWinner() != null;
    }

    /**
     * Returns whether the given player has been registered for this game.
     * @param player the player.
     * @return <code>true</code> if the player has been registered to this game;
     *         <code>false</code> otherwise.
     */
    public boolean containsPlayer(Player player) {
        return this.players.contains(player);
    }

    /**
     * Returns all of rounds of this game.
     * @return all of rounds of this game.
     */
    public List<Round> getRounds() {
        return Collections.unmodifiableList(this.rounds);
    }

    /**
     * Returns the winner of this game.
     * @return the winner of this game or <code>null</code> if there is no
     *         winner.
     */
    public Player getWinner() {
        Player winner = null;
        Round lastRound = this.getLastRound();
        if (lastRound == null) {
            return winner;
        }
        int correct = GameConfiguration.numberLength();
        for (Player player: lastRound.getAnswers().keySet()) {
            Answer answer = lastRound.getAnswers().get(player);
            if (answer.getCorrect() == correct) {
                winner = player;
                break;
            }
        }
        return winner;
    }
}
