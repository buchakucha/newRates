package MyClass;

import MyException.BadAnswerException;
import MyException.BadMoneyException;
import MyException.BadNumberException;

import java.util.Scanner;
import java.util.ArrayList;

public class Main {
    public static void main(String[] argv) {
        Scanner sc = new Scanner(System.in);
        ArrayList<Account> accounts = getAllAccounts();
        Dialog.greeting();
        try {
            Account currentAccount = signInAtAccount(sc, accounts);
            accounts = addNewAccount(accounts, currentAccount);
            Dialog.inform(currentAccount);
            startGame(sc, currentAccount);
        } catch (BadAnswerException | BadNumberException | BadMoneyException e) {
            Utils.updateAccountInformation(accounts);
            throw new RuntimeException(e);
        }
        Utils.updateAccountInformation(accounts);
    }

    private static void startGame(Scanner sc,
                                  Account currentAccount)
            throws BadMoneyException, BadNumberException, BadAnswerException {
        if (!currentAccount.isEnoughMoneyForMatch()) return;
        while (currentAccount.getMoney() != 0) {
            float stakedMoney = getPlacedBet(currentAccount, sc);
            Participant[] participants = getParticipants();
            float[] participantsCoefficients = getCoefficients(participants);
            Match.displayListOfParticipants(participants);
            calculateWinnings(sc, currentAccount, stakedMoney, participantsCoefficients);
            if (!canContinue(sc, currentAccount)) return;
        }
    }

    private static boolean canContinue(Scanner sc, Account currentAccount) throws BadAnswerException {
        if (!currentAccount.isEnoughMoneyForMatch()) {
            System.out.println("Участвовать в ставках вы не можете:(\nСпасибо за игру, до свидания!");
            return false;
        }
        return isGameContinue(sc);
    }

    private static void calculateWinnings(Scanner sc,
                                          Account currentAccount,
                                          float stakedMoney,
                                          float[] participantsCoefficients) throws BadNumberException {
        int choice = getChoiceOfPlayer(sc);
        int winner = Match.getNumberOfWinner();
        System.out.println("Победитель забега участник №" + (winner + 1) + ".\n");
        if (winner == choice - 1) {
            System.out.println("Поздравляю!");
            addWonMoney(currentAccount, stakedMoney, participantsCoefficients, winner);
        } else {
            System.out.println("К сожалению, ваша ставка не зашла:(");
        }
        System.out.println("У вас на счету " + currentAccount.getMoney() + " монет.\n");
    }

    private static Account signInAtAccount(Scanner sc,
                                           ArrayList<Account> accounts) throws BadAnswerException {
        Account currentAccount;
        int answer = sc.nextInt();
        if (answer == 1) { //пользователь уже есть
            currentAccount = getAuthorizedAccount(accounts, sc);
        } else if (answer == 0) { //новый пользователь
            currentAccount = getRegisteredNewAccount(accounts, sc);
        } else {
            throw new BadAnswerException("Не могу понять ДА это или НЕТ...Попробуйте ещё раз.");
        }
        return currentAccount;
    }

    private static void addWonMoney(Account currentAccount, //действие
                                    float stakedMoney,
                                    float[] participantsCoefficients,
                                    int winner) {
        float winningMoney = Match.getCalculatedReward(
                stakedMoney,
                participantsCoefficients[winner]
        );
        currentAccount.addMoney(winningMoney);
        System.out.println("Ваш выигрыш составил " + winningMoney + " монет");
    }

    private static int getChoiceOfPlayer(Scanner sc) throws BadNumberException {
        System.out.println("На кого из участников будет ваша ставка?");
        int choice = sc.nextInt();
        if ((choice <= 0) || (choice >= 5)) {
            throw new BadNumberException("Участника с таким номером в этом забеге нет.");
        }
        return choice;
    }

    private static boolean isGameContinue(Scanner sc) throws BadAnswerException {
        System.out.println("Желаете продолжить? 1-да, 0-нет");
        int resume = sc.nextInt();
        if ((resume != 0) && (resume != 1)) {
            throw new BadAnswerException("Не могу понять ДА это или НЕТ...Попробуйте ещё раз.");
        }
        if (resume == 0) {
            System.out.println("Спасибо за игру, до свидания!");
            return false;
        }
        return true;
    }

    private static float[] getCoefficients(Participant[] participants) {
        float[] participantsCoefficients = new float[participants.length];
        for (int i = 0; i < participants.length; i++) {
            participantsCoefficients[i] = participants[i].getCoefficient();
        }
        return participantsCoefficients;
    }

    private static Participant[] getParticipants() {
        Participant horseParticipant = new Participant(new Horse());
        Participant donkeyParticipant = new Participant(new Donkey());
        Participant camelParticipant = new Participant(new Camel());
        Participant giraffeParticipant = new Participant(new Giraffe());
        return new Participant[]{
                horseParticipant,
                donkeyParticipant,
                camelParticipant,
                giraffeParticipant
        };
    }

    private static float getPlacedBet(Account currentAccount, Scanner sc) throws BadMoneyException {
        float stakedMoney;
        System.out.println("Сколько монет ваша ставка?");
        stakedMoney = sc.nextFloat();
        if (stakedMoney <= 0) {
            throw new BadMoneyException("Ну нет уж, это так не работает...До свидания!");
        }
        float currentMoney = currentAccount.getMoney();
        if (currentAccount.canPlaceBet(stakedMoney)) {
            currentAccount.setMoney(currentMoney - stakedMoney);
            System.out.println("Отлично! На вашем счету " + currentAccount.getMoney() + " монет");
        } else {
            throw new BadMoneyException("На вашем счету недостаточно средств. Введите сумму не более " + currentMoney);
        }
        return stakedMoney;
    }

    private static Account getRegisteredNewAccount(ArrayList<Account> accounts, Scanner sc) {
        Account newAccount;
        String passwordInput;
        String loginInput = null;
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
        newAccount = new Account(
                loginInput,
                passwordInput,
                1000);
        return newAccount;
    }

    private static ArrayList<Account> addNewAccount(ArrayList<Account> oldList,
                                                    Account newAccount) {
        ArrayList<Account> newList = new ArrayList<>(oldList); //копия
        if (!newList.contains(newAccount)) {
            newList.add(newAccount);
        }
        return newList;
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
            if (!correctAcc) {
                System.out.println("Ошибка! Проверьте правильность ввода данных." +
                        "\nПопробуйте еще раз.\n");
            }
        }
        return currentAccount;
    }

    private static ArrayList<Account> getAllAccounts() {
        ArrayList<Account> accounts = new ArrayList<>();
        Utils.readFile(accounts);
        return accounts;
    }
}

