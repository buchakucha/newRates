package MyClass;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

public class Utils {
    private static final String filePath = "src/Accounts.txt";
    public static void updateAccountInformation(ArrayList<Account> accounts) {
        try {
            FileWriter reOut = new FileWriter(filePath, false);
            for (Account account : accounts) {
                reOut.write(account + "\n");
            }
            reOut.close();
        } catch (IOException e) {
            throw new RuntimeException("Какие-то пролемы с файлом", e);
        }
    }

    public static void readAllAccounts(ArrayList<Account> accounts) {
        try {
            BufferedReader in = new BufferedReader(new FileReader(filePath));
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
            FileWriter out = new FileWriter(filePath, true);
            out.write("\n" + loginInput + " " + passwordInput + " " + "1000");
            out.close();
        } catch (IOException e) {
            throw new RuntimeException("Какие-то пролемы с файлом", e);
        }
    }

    public static int getRandomInt(int min, int max) {
        return new Random().nextInt((max - min) + 1) + min;
    }
}

