package mancala;

public class NoSuchPlayerException extends RuntimeException {
    private static final long serialVersionUID = 8L;

    public NoSuchPlayerException() {
        super("Player not found.");
    }

    public NoSuchPlayerException(final String message) {
        super(message);
    }
}
