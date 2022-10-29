package MyClass;

import java.util.Random;

public class Match {

    public void displayListOfParticipants(Participant[] members) {
        for (int i = 0; i < 4; i++) {
            System.out.println("№" + (i + 1) + members[i].toString());
        }
    }

    public int getNumberOfWinner() {
        int min = 0;
        int max = 3;
        return new Random().nextInt((max - min) + 1) + min;
    }

    public float getCalculatedReward(int winner, int choice, float moneyRate, float[] membersCof) {
        float add = 0;
        if (winner == choice) {
            add = moneyRate * membersCof[winner];
            System.out.println("Поздравляю! Ваш выигрыш составил " + add + " монет");
        } else System.out.println("К сожалению, ваша ставка не зашла:(");
        return add;
    }
}


