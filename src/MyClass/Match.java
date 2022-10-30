package MyClass;

public class Match {

    public static void displayListOfParticipants(Participant[] participants) {
        System.out.println("В гонке участвуют:\n");
        for (int i = 0; i < 4; i++) {
            System.out.println("№" + (i + 1) + participants[i].toString());
        }
    }

    public static int getNumberOfWinner() {
        return Utils.getRandomInt(0, 3);
    }

    public static float getCalculatedReward(float stakedMoney, //вычисление
                                            float coefficientOfWinner) {
        return stakedMoney * coefficientOfWinner;
    }
}


