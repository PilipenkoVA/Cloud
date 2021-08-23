
//  1. Создаем класс "Сотрудник" с полями: ФИО, должность, email, телефон, зарплата, возраст.
public class Person {
    private String name;
    private String position;
    private String email;
    private int phone;
    private int salary;
    private int age;

//  2. Создаем конструктор класса который будет заполнять поля при создании объекта.
    public Person(String name, String position, String email, int phone, int salary, int age) {
        this.name = name;
        this.position = position;
        this.email = email;
        this.phone = phone;
        this.salary = salary;
        this.age = age;
    }
//  3. Создаем метод, который будет выводить информацию об объекте в консоль.
    public void info(){
        System.out.println("Имя: " + name + ", должность: " + position + ", email: " + email + ", телефон: " + phone + ", зарплата: " + salary + ", возраст: " + age);
    }

//  Создаем "Getter" который будет использован для дальнейшей сортировки по возрасту
    public int getAge() {
        return age;
    }
}
