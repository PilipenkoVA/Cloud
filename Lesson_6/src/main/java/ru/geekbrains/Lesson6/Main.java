package ru.geekbrains.Lesson6;//Урок 6. Продвинутое ООП

import ru.geekbrains.Lesson6.Animals.Animal;
import ru.geekbrains.Lesson6.Animals.Cat;
import ru.geekbrains.Lesson6.Animals.Dog;

public class Main {

    public static void main(String[] args) {

        // Создал массив животных
        Animal[] animals = {
                new Cat("Мурзик"),
                new Dog("Бобик")
        };
        System.out.println("...Задача пробежать дистанцию...");
        for (Animal animal : animals) {
            // вызывае метод "run" которому в качестве параметра каждому методу передается длина препятствия.
            animal.run(300);
        }
        System.out.println();                       // <-- просто пустая строка для красоты вывода текста

        System.out.println("...Задача проплыть дистанцию...");
        for (Animal animal : animals) {
            // вызывае метод "swim" которому в качестве параметра каждому методу передается длина препятствия.
            animal.swim(7);
        }
        System.out.println();                       // <-- просто пустая строка для красоты вывода текста

        // 4. * Подсчет животных
        System.out.println("...Подсчет животных...");
        int sum = 0;
        for (int i = 0; i < animals.length; i++) {
            sum += 1;
        }
        System.out.println("Создано: " + sum + " ед. животных");
    }
}
