package MyClass;

import MyException.BadAnswerException;
import MyException.BadMoneyException;
import MyException.BadNumberException;

import java.util.Scanner;
import java.util.ArrayList;

public class Main {
    public static final Scanner sc = new Scanner(System.in);

    public static void main(String[] argv) {
        ArrayList<Account> accounts = getAllAccounts();
        Dialog.greeting();
        try {
            Account currentAccount = signInAtAccount(accounts);
            accounts = addNewAccount(accounts, currentAccount);
            Dialog.inform(currentAccount);
            startGame(currentAccount);
        } catch (BadAnswerException | BadNumberException | BadMoneyException e) {
            Utils.updateAccountInformation(accounts);
            throw new RuntimeException(e);
        }
        Utils.updateAccountInformation(accounts);
    }

    private static void startGame(Account currentAccount)
            throws BadMoneyException, BadNumberException, BadAnswerException {
        if (!currentAccount.isEnoughMoneyForMatch()) return;
        while (haveMoney(currentAccount)) {
            float stakedMoney = getPlacedBet(currentAccount);
            Participant[] participants = getParticipants();
            float[] participantsCoefficients = getCoefficients(participants);
            Match.displayListOfParticipants(participants);
            calculateWinnings(currentAccount, stakedMoney, participantsCoefficients);
            if (!canContinue(currentAccount)) return;
        }
    }

    private static boolean haveMoney(Account currentAccount) {
        return currentAccount.getMoney() != 0;
    }

    private static boolean canContinue(Account currentAccount) throws BadAnswerException {
        if (!currentAccount.isEnoughMoneyForMatch()) {
            System.out.println("Участвовать в ставках вы не можете:(\nСпасибо за игру, до свидания!");
            return false;
        }
        return isGameContinue();
    }

    private static void calculateWinnings(Account currentAccount,
                                          float stakedMoney,
                                          float[] participantsCoefficients) throws BadNumberException {
        int choice = getChoiceOfPlayer();
        int winner = Match.getNumberOfWinner();
        System.out.println("Победитель забега участник №" + (winner + 1) + ".\n");
        if (isPlayerWin(choice, winner)) {
            System.out.println("Поздравляю! Ваш выигрыш составил " +
                    Match.getCalculatedReward(
                            stakedMoney,
                            participantsCoefficients[winner])
                    + " монет"
            );
            addWonMoney(currentAccount, stakedMoney, participantsCoefficients, winner);
        } else {
            System.out.println("К сожалению, ваша ставка не зашла:(");
        }
        System.out.println("У вас на счету " + currentAccount.getMoney() + " монет.\n");
    }

    private static boolean isPlayerWin(int choice, int winner) {
        return isTwoIntEqual(winner, choice - 1);
    }

    private static boolean isTwoIntEqual(int first, int second) {
        return first == second;
    }

    private static Account signInAtAccount(ArrayList<Account> accounts)
            throws BadAnswerException {
        Account currentAccount;
        int answer = sc.nextInt();
        if (isTwoIntEqual(answer, 1)) {
            currentAccount = getAuthorizedAccount(accounts);
        }
        else if (isTwoIntEqual(answer, 0)) {
            currentAccount = getRegisteredNewAccount(accounts);
        }
        else {
            throw new BadAnswerException("Не могу понять ДА это или НЕТ...Попробуйте ещё раз.");
        }
        return currentAccount;
    }

    private static void addWonMoney(Account currentAccount,
                                    float stakedMoney,
                                    float[] participantsCoefficients,
                                    int winner) {
        currentAccount.addMoney(Match.getCalculatedReward(
                stakedMoney,
                participantsCoefficients[winner]
        ));
    }

    private static int getChoiceOfPlayer() throws BadNumberException {
        System.out.println("На кого из участников будет ваша ставка?");
        int choice = sc.nextInt();
        if (isIncorrectChoice(choice)) {
            throw new BadNumberException("Участника с таким номером " +
                    "в этом забеге нет.");
        }
        return choice;
    }

    private static boolean isIncorrectChoice(int choice) {
        return (choice < 1) || (choice > 4);
    }

    private static boolean isGameContinue() throws BadAnswerException {
        System.out.println("Желаете продолжить? 1-да, 0-нет");
        int resume = sc.nextInt();
        if (isIncorrectResumeAnswer(resume)) {
            throw new BadAnswerException("Не могу понять ДА это или НЕТ...Попробуйте ещё раз.");
        }
        if (isTwoIntEqual(resume, 0)) {
            System.out.println("Спасибо за игру, до свидания!");
            return false;
        }
        return true;
    }

    private static boolean isIncorrectResumeAnswer(int resume) {
        return (resume != 0) && (resume != 1);
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

    private static float getPlacedBet(Account currentAccount) throws BadMoneyException {
        float stakedMoney;
        System.out.println("Сколько монет ваша ставка?");
        stakedMoney = sc.nextFloat();
        if (isVeryCunning(stakedMoney)) {
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

    private static boolean isVeryCunning(float stakedMoney) {
        return stakedMoney <= 0;
    }

    private static Account getRegisteredNewAccount(ArrayList<Account> accounts) {
        System.out.println("Придумайте логин:");
        String loginInput = getVerifiedNewLogin(accounts);
        System.out.println("Придумайте пароль:");
        String passwordInput = sc.next();
        Utils.addNewAccountAtFile(loginInput, passwordInput);
        return new Account(loginInput, passwordInput, 1000);
    }

    private static String getVerifiedNewLogin(ArrayList<Account> accounts) {
        boolean isNewLogin = false;
        String loginInput = null;
        while (!isNewLogin) {
            loginInput = sc.next();
            for (Account account : accounts) {
                isNewLogin = account.isUnequalLogin(loginInput);
                if (!isNewLogin) break;
            }
            if (!isNewLogin) {
                System.out.println("Такой логин уже есть:( Придумайте другой логин:");
            }
        }
        return loginInput;
    }

    private static ArrayList<Account> addNewAccount(ArrayList<Account> oldList,
                                                    Account newAccount) {
        ArrayList<Account> newList = new ArrayList<>(oldList);
        if (!newList.contains(newAccount)) {
            newList.add(newAccount);
        }
        return newList;
    }

    private static Account getAuthorizedAccount(ArrayList<Account> accounts) {
        boolean correctAcc = false;
        Account currentAccount = null;
        while (!correctAcc) {
            System.out.println("Введите логин:");
            String loginInput = sc.next();
            System.out.println("Введите пароль:");
            String passwordInput = sc.next();
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
        Utils.readAllAccounts(accounts);
        return accounts;
    }
}

