//Урок 5. Введение в ООП

public class Main {
    public static void main(String[] args) {
        //        4. Создаем массив из 5 сотрудников.
        Person[] people = new Person[5];
        people[0] = new Person("Ivanov Ivan1", "Enginer", "ivivan1@mailbox.com", 892312311, 30001, 30);
        people[1] = new Person("Ivanov Ivan2", "Driver", "ivivan2@mailbox.com", 892312312, 30002, 40);
        people[2] = new Person("Ivanov Ivan3", "Manager", "ivivan3@mailbox.com", 892312313, 30003, 47);
        people[3] = new Person("Ivanov Ivan4", "Driver", "ivivan4@mailbox.com", 892312314, 30004, 45);
        people[4] = new Person("Ivanov Ivan5", "Manager", "ivivan5@mailbox.com", 892312315, 30005, 25);

        //        5. С помощью цикла выводим информацию о сотрудниках старше 40 лет.
        for (int i = 0; i < people.length; i++) {
            if (people[i].getAge() >= 40){
                people[i].info();
            }
        }
    }
}
