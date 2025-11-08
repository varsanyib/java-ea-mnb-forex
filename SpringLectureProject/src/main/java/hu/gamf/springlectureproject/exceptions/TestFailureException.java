package hu.gamf.springlectureproject.exceptions;

public class TestFailureException extends RuntimeException {
    private static final long serialVersionUID = 1L;
    public TestFailureException(Exception e) {
        super(e);
    }
    public TestFailureException(String message) {
        super("FAILURE:" + message);
    }
}
