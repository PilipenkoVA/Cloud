package ru.geekbrains.Lesson6.Animals;    //        1. Создал класс Кот с наследованием от класса Животное.

public class Cat extends Animal{
    //        3. У каждого животного есть ограничения на действия (бег: кот 200 м; плавание: кот не умеет плавать.).
    protected int maxRun = 200;

    public Cat(String name) {
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
        System.out.println(name + " не полез в воду т.к. не умеет плавать");
    }
}
