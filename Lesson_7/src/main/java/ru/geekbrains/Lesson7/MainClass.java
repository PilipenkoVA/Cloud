package ru.geekbrains.Lesson7;
//Урок 7. Практика ООП и работа со строками

//        1. Расширить задачу про котов и тарелки с едой.

public class MainClass {
    public static void main(String[] args) {
        // 5. Создаем массив котов и тарелку с едой. Все коты при создании голодны , сытость = false.
        Cat[] cats = {
                new Cat("Barsik_1", 5),
                new Cat("Barsik_2", 7),
                new Cat("Barsik_3", 3),
                new Cat("Barsik_4", 8),
                new Cat("Barsik_5", 4),
        };
        Plate plate = new Plate(20);
        plate.info();

        // 5. Попросим всех котов покушать из этой тарелки
        for (int i = 0; i < cats.length; i++) {
            cats[i].eat(plate);
            plate.info();
            // 5. Выводим информацию о сытости котов в консоль.
            cats[i].info();
        }
        plate.info();
        // Добавляем корм в тарелку
        plate.addFood(15);
        plate.info();

    }
}

