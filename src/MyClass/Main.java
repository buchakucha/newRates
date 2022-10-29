package MyClass;

import MyException.BadAnswerException;
import MyException.BadMoneyException;
import MyException.BadNumberException;

import java.util.Scanner;
import java.util.ArrayList;

public class Main {
    public static void main(String[] argv) throws BadAnswerException, BadNumberException, BadMoneyException {
        Scanner sc = new Scanner(System.in);
        Dialog.greeting();
        ArrayList<Account> accounts = getAllAccounts();
        Account currentAccount = signInAtAccount(sc, accounts);
        Dialog.inform(currentAccount);
        startGame(sc, accounts, currentAccount);
    }

    private static void startGame(Scanner sc,
                                  ArrayList<Account> accounts,
                                  Account currentAccount)
            throws BadMoneyException, BadNumberException, BadAnswerException {
        if (!currentAccount.isEnoughMoneyForMatch()) return;
        while (currentAccount.getMoney() != 0) {
            float stakedMoney = 0;
            while (!currentAccount.isCanParticipate()) {
                stakedMoney = getPlacedBet(currentAccount, sc);
            }
            currentAccount.setCanParticipate(false);
            System.out.println("В гонке участвуют:\n");
            Participant[] participants = getParticipants();
            float[] participantsCoefficients = getCoefficients(participants);
            Match.displayListOfParticipants(participants);
            int choice = getChoiceOfPlayer(sc);
            int winner = Match.getNumberOfWinner();
            System.out.println("Победитель забега участник №" + (winner + 1) + ".\n");
            addWonMoney(currentAccount, accounts, stakedMoney, participantsCoefficients, choice, winner);
            System.out.println("У вас на счету " + currentAccount.getMoney() + " монет.\n");
            if (!currentAccount.isEnoughMoneyForMatch()) {
                System.out.println("Участвовать в ставках вы не можете:(\nСпасибо за игру, до свидания!");
                return;
            }
            if (isGameNotContinue(sc)) {
                Utils.rewriteFile(accounts);
                return;
            }
        }
    }

    private static Account signInAtAccount(Scanner sc, ArrayList<Account> accounts) throws BadAnswerException {
        Account currentAccount;
        int answer = sc.nextInt();
        if (answer == 1) {
            currentAccount = getAuthorizedAccount(accounts, sc);
        } else if (answer == 0) {
            currentAccount = getRegisteredNewAccount(accounts, sc);
        } else {
            throw new BadAnswerException("Не могу понять ДА это или НЕТ...Попробуйте ещё раз.");
        }
        return currentAccount;
    }

    private static void addWonMoney(Account currentAccount, ArrayList<Account> accounts, float stakedMoney, float[] participantsCoefficients, int choice, int winner) {
        float winningMoney = Match.getCalculatedReward(
                winner,
                choice - 1,
                stakedMoney,
                participantsCoefficients
        );
        currentAccount.addMoney(winningMoney);
    }

    private static int getChoiceOfPlayer(Scanner sc) throws BadNumberException {
        System.out.println("На кого из участников будет ваша ставка?");
        int choice = sc.nextInt();
        if ((choice <= 0) || (choice >= 5)) {
            throw new BadNumberException("Участника с таким номером в этом забеге нет.");
        }
        return choice;
    }

    private static boolean isGameNotContinue(Scanner sc) throws BadAnswerException {
        System.out.println("Желаете продолжить? 1-да, 0-нет");
        int resume = sc.nextInt();
        if ((resume != 0) && (resume != 1)) {
            throw new BadAnswerException("Не могу понять ДА это или НЕТ...Попробуйте ещё раз.");
        }
        if (resume == 0) {
            System.out.println("Спасибо за игру, до свидания!");
            return true;
        }
        return false;
    }

    private static float[] getCoefficients(Participant[] participants) {
        float[] participantsCoefficients = new float[4];
        for (int i = 0; i < 4; i++) {
            participantsCoefficients[i] = participants[i].getCoefficient();
        }
        return participantsCoefficients;
    }

    private static Participant[] getParticipants() {
        Participant horseParticipant = new Participant(new Horse());
        Participant donkeyParticipant = new Participant(new Donkey());
        Participant camelParticipant = new Participant(new Camel());
        Participant giraffeParticipant = new Participant(new Giraffe());
        return new Participant[]{horseParticipant, donkeyParticipant, camelParticipant, giraffeParticipant};
    }

    private static float getPlacedBet(Account currentAccount, Scanner sc) throws BadMoneyException {
        float stakedMoney;
        System.out.println("Сколько монет ваша ставка?");
        stakedMoney = sc.nextFloat();
        if (stakedMoney <= 0) throw new BadMoneyException("Ну нет уж, это так не работает...До свидания!");
        currentAccount.placeBet(stakedMoney);
        return stakedMoney;
    }

    private static Account getRegisteredNewAccount(ArrayList<Account> accounts, Scanner sc) {
        Account currentAccount;
        String passwordInput;
        String loginInput = "";
        boolean newAcc = false;
        System.out.println("Придумайте логин:");
        while (!newAcc) {
            loginInput = sc.next();
            for (Account account : accounts) {
                newAcc = account.checkNewLogin(loginInput);
                if (!newAcc) break;
            }
            if (!newAcc) System.out.println("Такой логин уже есть:( Придумайте другой логин:");
        }
        System.out.println("Придумайте пароль:");
        passwordInput = sc.next();
        Utils.addNewAccountAtFile(loginInput, passwordInput);
        currentAccount = new Account(loginInput, passwordInput, 1000);
        accounts.add(currentAccount);
        return currentAccount;
    }

    private static Account getAuthorizedAccount(ArrayList<Account> accounts, Scanner sc) {
        String passwordInput;
        String loginInput;
        boolean correctAcc = false;
        Account currentAccount = new Account(null, null, 0);
        while (!correctAcc) {
            System.out.println("Введите логин:");
            loginInput = sc.next();
            System.out.println("Введите пароль:");
            passwordInput = sc.next();
            for (Account account : accounts) {
                correctAcc = account.checkLoginAndPassword(loginInput, passwordInput);
                if (correctAcc) {
                    currentAccount = account;
                    break;
                }
            }
            if (!correctAcc)
                System.out.println("Ошибка! Проверьте правильность ввода данных." +
                        "\nПопробуйте еще раз.\n");
        }
        return currentAccount;
    }

    private static ArrayList<Account> getAllAccounts() {
        ArrayList<Account> accounts = new ArrayList<>();
        Utils.readFile(accounts);
        return accounts;
    }
}

