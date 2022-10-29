package MyClass;


import MyInterface.Animal;


public class Participant {
    public final int speed;
    public final int age;
    public final int power;
    public final String name;
    public float coefficient;

    public Participant(Animal animal) {
        power = animal.getPower();
        age = animal.getAge();
        speed = animal.getSpeed();
        name = animal.getName();
        coefficient = createCoefficient();
    }

    private float createCoefficient() {
        float zmax = (70 + 10) / 2;
        float z = (speed + power) / age;
        return (2 - z / zmax);
    }

    public float getCoefficient() {
        return coefficient;
    }

    @Override
    public String toString() {
        StringBuilder out = new StringBuilder();
        out.append("\nБЕГУН: " + name +
                "\nВОЗРАСТ: " + age + " лет" +
                "\nСКОРОСТЬ: " + speed + " км/ч" +
                "\nСИЛА: " + power + " из 10" +
                "\nКОЭФФИЦИЕНТ: " + coefficient + "\n");
        return out.toString();
    }
}
