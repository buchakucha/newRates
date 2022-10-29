package MyException;

public class BadAnswerException extends Exception {
    private String s = "Не могу понять ДА это или НЕТ...Попробуйте ещё раз.";

    @Override
    public String getMessage() {
        return s;
    }
}
