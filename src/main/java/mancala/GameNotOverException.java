package mancala;

public class GameNotOverException extends RuntimeException {
    private static final long serialVersionUID = 2L;

    public GameNotOverException() {
        super("The game is not over yet.");
    }

    public GameNotOverException(final String message) {
        super(message);
    }
}
