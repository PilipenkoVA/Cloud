package ru.geekbrains.Lesson7;

public class Plate {
    private int food;

    public Plate(int food) {
        this.food = food;
    }

    public boolean decreaseFood(int appetite) {
        // 2. Проверка на то чтобы в тарелке с едой не могло получиться отрицательного количества,
        if (food < appetite){
            return false;
        }
        food -= appetite;
        return true;
    }
    // 6. МЕТОД: добавления еды в тарелку
    public void addFood(int add){
        food += add;
        System.out.println("В тарелку добавили " + add + " ед. корма.");
    }

    public void info() {
        System.out.println("ИТОГО: в тарелке = " + food + " ед. корма.");
    }
}



