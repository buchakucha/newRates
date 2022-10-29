package MyException;

public class BadMoneyException extends Exception {
    private String s = "Ну нет уж, это так не работает...До свидания!";

    @Override
    public String getMessage() {
        return s;
    }
}
