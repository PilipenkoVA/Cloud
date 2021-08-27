package ru.geekbrains.Lesson6.Animals;//        1. Создал класс Собака с наследованием от класса Животное.

public class Dog extends Animal{
    //        3. У каждого животного есть ограничения на действия (бег: собака 500 м.; плавание: собака 10 м.).
    protected int maxRun = 500;
    protected int maxSwim = 10;

    public Dog(String name) {
        this.name = name;
    }

    @Override
    public void run(int lenght) {
        if (lenght > maxRun){
            System.out.println(name + " пробежал " + maxRun + " и лег отдохнуть");
        }else {
            System.out.println(name + " пробежал " + lenght + " м.");
        }
    }

    @Override
    public void swim(int lenght) {
        if (lenght > maxSwim){
            System.out.println(name + " проплыл " + maxSwim + " и пошел ко дну");
        }else {
            System.out.println(name + " проплыл " + lenght + " м.");
        }

    }
}
