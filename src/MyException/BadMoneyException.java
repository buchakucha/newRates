package MyException;

public class BadMoneyException extends Exception {
    public BadMoneyException(String errorMessage) {
        super(errorMessage);
    }
}
