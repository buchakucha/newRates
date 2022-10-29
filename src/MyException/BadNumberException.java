package MyException;

public class BadNumberException extends Exception {
    private String s = "Участника с таким номером в этом забеге нет.";

    @Override
    public String getMessage() {
        return s;
    }
}
