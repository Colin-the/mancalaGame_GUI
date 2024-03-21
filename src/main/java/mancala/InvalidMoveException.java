package mancala;

public class InvalidMoveException extends RuntimeException {
    private static final long serialVersionUID = 3L;

    public InvalidMoveException() {
        super("Invalid move.");
    }

    public InvalidMoveException(final String message) {
        super(message);
    }
}
