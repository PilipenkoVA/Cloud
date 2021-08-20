public class Main {

//    1.	Написать метод, принимающий на вход два целых числа и проверяющий, что их сумма лежит в пределах от 10 до 20
//          (включительно), если да – вернуть true, в противном случае – false.
//    2.	Написать метод, которому в качестве параметра передается целое число, метод должен напечатать в консоль,
//          положительное ли число передали или отрицательное. Замечание: ноль считаем положительным числом.
//    3.	Написать метод, которому в качестве параметра передается целое число. Метод должен вернуть true, если число
//          отрицательное, и вернуть false если положительное.
//    4.	Написать метод, которому в качестве аргументов передается строка и число, метод должен отпечатать в консоль
//          указанную строку, указанное количество раз;
//    5.*   Написать метод, который определяет, является ли год високосным, и возвращает boolean (високосный - true,
//          не високосный - false). Каждый 4-й год является високосным, кроме каждого 100-го, при этом каждый 400-й – високосный.

    public static void main(String[] args) {

        System.out.println(within10and20(2,29));
        isPositiveOrNegative(2);
        System.out.println(isNegative(-7));
        printWordNTimes("Hello", 2);
    }

    //    Задание №1
    public static boolean within10and20(int x1, int x2) {
        System.out.println("Задание №1");
        if (x1 + x2 >= 10 & x1 + x2 <= 20){
            return true;
        }else {
            return false;
        }
    }
    //    Задание №2
    public static void isPositiveOrNegative(int x) {
        System.out.println("Задание №2");
        if (x >= 0) {
            System.out.println("число положительное");
        } else {
            System.out.println("число отрицательное");
        }
    }
    //    Задание №3
    public static boolean isNegative(int x) {
        System.out.println("Задание №3");
        if (x<0) {
            return true;
        }else
            return false;
    }
    //    Задание №4
    public static void printWordNTimes(String word, int times) {
        System.out.println("Задание №4");
        for (int i = 0; i < times; i++) {
            System.out.println(word);
        }
    }
}
