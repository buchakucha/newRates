package MyClass;

import MyInterface.Animal;

public class Donkey implements Animal {
    private final String name;
    private final int age;
    private final int speed;
    private final int power;

    public Donkey() {
        name = createName();
        age = createAge();
        speed = createSpeed();
        power = createPower();
    }
    public String createName() {
        return "Осёл";
    }

    public int createAge() {
        return Utils.getRandomInt(2, 25);
    }

    public int createSpeed() {
        return Utils.getRandomInt(1, 70);
    }

    public int createPower() {
        return Utils.getRandomInt(1, 10);
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public int getAge() {
        return age;
    }

    @Override
    public int getSpeed() {
        return speed;
    }

    @Override
    public int getPower() {
        return power;
    }
}
