package MyClass;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class Utils {
    public void rewriteFile(ArrayList<Account> accounts) throws IOException {
        FileWriter reOut = new FileWriter("src/Accounts.txt", false);
        for (int i = 0; i < accounts.size(); i++) {
            reOut.write(accounts.get(i) + "\n");
        }
        reOut.close();
    }

    public void readFile(String login, String password, float money, ArrayList<Account> accounts) throws IOException {
        BufferedReader in = new BufferedReader(new FileReader("src/Accounts.txt"));
        String buf = in.readLine();
        if (buf == null) return;
        while (buf != null) {
            String[] array = buf.trim().split(" +");
            login = array[0];
            password = array[1];
            money = Float.parseFloat(array[2]);
            Account person = new Account(login, password, money);
            accounts.add(person);
            buf = in.readLine();
        }
        in.close();
    }

    public void addNewAccountAtFile(String loginInput, String passwordInput) throws IOException {
        FileWriter out = new FileWriter("src/Accounts.txt", true);
        out.write("\n" + loginInput + " " + passwordInput + " " + "1000");
        out.close();
    }
}
