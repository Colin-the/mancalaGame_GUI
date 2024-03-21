package mancala;

public class PitNotFoundException extends RuntimeException {
    private static final long serialVersionUID = 7L;

    public PitNotFoundException() {
        super("Pit not found.");
    }

    public PitNotFoundException(final String message) {
        super(message);
    }
}