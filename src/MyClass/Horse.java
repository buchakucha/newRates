package MyClass;

import MyInterface.Animal;

import java.util.Random;

public class Horse implements Animal {

    private final String name;
    private final int age;
    private final int speed;
    private final int power;

    public Horse() {
        name = createName();
        age = createAge();
        speed = createSpeed();
        power = createPower();
    }

    
    public String createName() {
        return "Лошадь";
    }

    
    public int createAge() {
        int min = 2;
        int max = 25;
        return new Random().nextInt((max - min) + 1) + min;
    }

    
    public int createSpeed() {
        int min = 35;
        int max = 60;
        return new Random().nextInt((max - min) + 1) + min;
    }

    
    public int createPower() {
        int min = 1;
        int max = 10;
        return new Random().nextInt((max - min) + 1) + min;
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
