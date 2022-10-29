package MyClass;

import java.util.Random;

public class Match {

    public static void displayListOfParticipants(Participant[] participants) {
        for (int i = 0; i < 4; i++) {
            System.out.println("№" + (i + 1) + participants[i].toString());
        }
    }

    public static int getNumberOfWinner() {
        int min = 0;
        int max = 3;
        return new Random().nextInt((max - min) + 1) + min;
    }

    public static float getCalculatedReward(int winner, int choice, float stakedMoney, float[] participantsCoefficients) {
        float add = 0;
        if (winner == choice) {
            add = stakedMoney * participantsCoefficients[winner];
            System.out.println("Поздравляю! Ваш выигрыш составил " + add + " монет");
        } else System.out.println("К сожалению, ваша ставка не зашла:(");
        return add;
    }
}


