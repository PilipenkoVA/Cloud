package ru.geekbrains.Lesson6.Animals;    //        1. Создал класс родитель Животные.

public abstract class Animal {
    protected String name;

    //        2. Все животные могут бежать и плыть. В качестве параметра каждому методу передается длина препятствия.

    public abstract void run (int lenght);

    public abstract void swim(int lenght);
}
