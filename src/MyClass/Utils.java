package MyClass;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.InputMismatchException;

public class Utils {
    public static void rewriteFile(ArrayList<Account> accounts) {
        try {
            FileWriter reOut = new FileWriter("src/Accounts.txt", false);
            for (Account account : accounts) {
                reOut.write(account + "\n");
            }
            reOut.close();
        } catch (IOException e) {
            throw new RuntimeException("Какие-то пролемы с файлом", e);
        }
    }

    public static void readFile(ArrayList<Account> accounts) {
        try {
            BufferedReader in = new BufferedReader(new FileReader("src/Accounts.txt"));
            String buf = in.readLine();
            if (buf == null) return;
            while (buf != null) {
                String[] array = buf.trim().split(" +");
                Account person = new Account(array[0], array[1], Float.parseFloat(array[2]));
                accounts.add(person);
                buf = in.readLine();
            }
            in.close();
        } catch (IOException e) {
            throw new RuntimeException("Какие-то пролемы с файлом", e);
        }
    }

    public static void addNewAccountAtFile(String loginInput, String passwordInput) {
        try {
            FileWriter out = new FileWriter("src/Accounts.txt", true);
            out.write("\n" + loginInput + " " + passwordInput + " " + "1000");
            out.close();
        } catch (IOException e) {
            throw new RuntimeException("Какие-то пролемы с файлом", e);
        }
    }
}
