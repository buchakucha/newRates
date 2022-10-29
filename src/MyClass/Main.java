package MyClass;

import MyException.BadAnswerException;
import MyException.BadMoneyException;
import MyException.BadNumberException;

import java.io.*;
import java.util.InputMismatchException;
import java.util.Scanner;
import java.util.ArrayList;

public class Main {
    public static void main(String[] argv) throws IOException, BadAnswerException, InputMismatchException, BadMoneyException {
        Utils util = new Utils();
        String login = "";
        String password = "";
        float money = 0;
        String loginInput = "";
        String passwordInput;
        Account current = new Account(null, null, 0);
        ArrayList<Account> accounts = new ArrayList<>();
        util.readFile(login, password, money, accounts);
        Dialog.greeting();
        Scanner sc = new Scanner(System.in);
        try {
            int answer = sc.nextInt();
            if (answer == 1) {
                boolean correctAcc = false;
                while (!correctAcc) {
                    System.out.println("Введите логин:");
                    loginInput = sc.next();
                    System.out.println("Введите пароль:");
                    passwordInput = sc.next();
                    for (int i = 0; i < accounts.size(); i++) {
                        correctAcc = accounts.get(i).checkLoginAndPassword(loginInput, passwordInput);
                        if (correctAcc) {
                            current = accounts.get(i);
                            break;
                        }
                    }
                    if (!correctAcc)
                        System.out.println("Ошибка! Проверьте правильность ввода данных.\nПопробуйте еще раз.\n");
                }
            } else if (answer == 0) {
                boolean newAcc = false;
                System.out.println("Придумайте логин:");
                while (!newAcc) {
                    loginInput = sc.next();
                    for (int i = 0; i < accounts.size(); i++) {
                        newAcc = accounts.get(i).checkNewLogin(loginInput);
                        if (!newAcc) break;
                    }
                    if (!newAcc) System.out.println("Такой логин уже есть:( Придумайте другой логин:");
                }
                System.out.println("Придумайте пароль:");
                passwordInput = sc.next();
                util.addNewAccountAtFile(loginInput, passwordInput);
                current = new Account(loginInput, passwordInput, 1000);
                accounts.add(current);
            } else {
                throw new BadAnswerException();
            }
            Dialog.inform(current);
            if (!current.isEnoughMoneyForMatch()) return;
            while (current.getMoney() != 0) {
                float moneyRate = 0;
                while (!current.tryRates) {
                    System.out.println("Сколько монет ваша ставка?");
                    moneyRate = sc.nextFloat();
                    if (moneyRate <= 0) throw new BadMoneyException();
                    current.placeBet(moneyRate);
                }
                current.tryRates = false;
                util.rewriteFile(accounts);
                Match match = new Match();
                System.out.println("В гонке участвуют:\n");
                Participant member1 = new Participant(new Horse());
                Participant member2 = new Participant(new Donkey());
                Participant member3 = new Participant(new Camel());
                Participant member4 = new Participant(new Giraffe());
                Participant[] members = {member1, member2, member3, member4};
                float[] membersCof = new float[4];
                for (int i = 0;  i < 4; i++) {
                    membersCof[i] = members[i].getCoefficient();
                }
                match.displayListOfParticipants(members);
                System.out.println("На кого из участников будет ваша ставка?");
                int choice = sc.nextInt();
                if ((choice <= 0) || (choice >= 5)) throw new BadNumberException();
                int winner = match.getNumberOfWinner();
                System.out.println("Победитель забега участник №" + (winner + 1) + ".\n");
                float addMoney = match.getCalculatedReward(winner, choice - 1, moneyRate, membersCof);
                current.addMoney(addMoney);
                util.rewriteFile(accounts);
                System.out.println("У вас на счету " + current.getMoney() + " монет.\n");
                if (!current.isEnoughMoneyForMatch()) return;
                System.out.println("Желаете продолжить? 1-да, 0-нет");
                int resume = sc.nextInt();
                if ((resume != 0) && (resume != 1)) throw new BadAnswerException();
                if (resume == 0) {
                    System.out.println("Спасибо за игру, до свидания!");
                    return;
                }
            }
        } catch (BadAnswerException e) {
            System.out.println(e.getMessage());
            return;
        } catch (InputMismatchException e) {
            System.out.println("Но ведь это даже не цифра...Попробуйте еще раз.");
            return;
        } catch (BadMoneyException e) {
            System.out.println(e.getMessage());
            return;
        } catch (BadNumberException e) {
            System.out.println(e.getMessage());
            return;
        } catch (IOException e) {
            System.out.println("Какие-то пролемы с файлом.");
            return;
        }
    }
}

