package ru.geekbrains.Lesson7;

public class Cat {
    private String name;
    private int appetite;
    // 3. Добавил поле сытость
    private boolean hungry;


    public Cat(String name, int appetite) {
        this.name = name;
        this.appetite = appetite;
        // 3. Когда создаем котов, они голодны
        this.hungry = false;
    }

    public void setHungry (boolean hungry){
        this.hungry = hungry;
    }

    public void eat(Plate p) {
        if (p.decreaseFood(appetite)){
            System.out.println(name + ", съел: " + appetite + " ед.");
        // 3. Если коту удалось покушать (хватило еды), сытость = true.
            hungry = true;
            return;
        }
        // 4. Если коту мало еды в тарелке, то он её просто не трогает.
        System.out.println(name + " не может покушать т.к. в тарелке мало еды.");

    }

    public void info (){
        System.out.println(name + ", hungry: " + hungry+ "\n");
    }
}
