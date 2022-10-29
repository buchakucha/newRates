package MyException;

public class BadAnswerException extends Exception {
    public BadAnswerException(String errorMessage) {
        super(errorMessage);
    }
}
