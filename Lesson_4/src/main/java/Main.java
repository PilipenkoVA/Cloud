//        Задание 1
//        1. Полностью разобраться с кодом, попробовать переписать с нуля, стараясь не подглядывать в методичку.

//        Задание 2
//        2. Переделать проверку победы, чтобы она не была реализована просто набором условий, например, с использованием циклов.

import java.util.Random;
import java.util.Scanner;

public class Main {
    
    private static char[][] map;
    private static Scanner scanner;
    private static Random random;
    
    private static final int SIZE = 3;
    private static final char ELEMENT_EMPTY = '_';
    private static final char ELEMENT_X = 'X';
    private static final char ELEMENT_O = 'O';
    
    public static void main(String[] args) {
        scanner = new Scanner(System.in);
        random = new Random();

        initMap();
        printMap();

        while (true){
            turnPlayer();
            printMap();
            System.out.println("____________");
            if (checkWin(ELEMENT_X)){
                System.out.println("Игра окончена - Выиграл игрок");
                break;
            }
            if (isMapFull()){
                System.out.println("Игра окончена - НИЧЬЯ");
                break;
            }

            turnComp();
            printMap();
            System.out.println("____________");
            if (checkWin(ELEMENT_O)){
                System.out.println("Игра окончена - Выиграл компьютер");
                break;
            }
            if (isMapFull()){
                System.out.println("Игра окончена - НИЧЬЯ");
                break;
            }
        }
    }
//    ПРОВЕРКА НА ВЫИГРЫШ
    public static boolean checkWin (char element){

//      ВАРИАНТ ПРОВЕРКИ С ПОМОЩЬЮ ЦИКЛОВ
        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                if (map[i][0] == element && map[i][1] == element && map[i][2] == element){      // проверяем строки
                    return true;
                }
                if (map[0][j] == element && map[1][j] == element && map[2][j] == element){      // проверяем столбцы
                    return true;
                }
                if (map[0][0] == element && map[1][1] == element && map[2][2] == element){      // проверяем диагонали
                     return true;
                }
                 if (map[2][0] == element && map[1][1] == element && map[0][2] == element){     // проверяем диагонали
                     return true;
                 }
            }
        }

//      ВАРИАНТ ПРОВЕРКИ ПОБЕДЫ КАК НА УРОКЕ
//        if (map[0][0] == element && map[0][1] == element && map[0][2] == element){
//            return true;
//        }
//        if (map[1][0] == element && map[1][1] == element && map[1][2] == element) {
//            return true;
//        }
//        if (map[2][0] == element && map[2][1] == element && map[2][2] == element) {
//            return true;
//        }
//
//        if (map[0][0] == element && map[1][0] == element && map[2][0] == element){
//            return true;
//        }
//        if (map[0][1] == element && map[1][1] == element && map[2][1] == element) {
//            return true;
//        }
//        if (map[0][2] == element && map[1][2] == element && map[2][2] == element) {
//            return true;
//        }
//
//        if (map[0][0] == element && map[1][1] == element && map[2][2] == element){
//            return true;
//        }
//        if (map[2][0] == element && map[1][1] == element && map[0][2] == element) {
//            return true;
//        }
        return false;
    }

//    ПРОВЕРКА НА ЗАПОЛНЕННОСТЬ ПОЛЯ
    public static boolean isMapFull (){
        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                if (map[i][j] == ELEMENT_EMPTY){
                    return false;
                }
            }
        }
        return true;
    }

    //    ХОД КОМПЬЮТЕРА
    public static void turnComp(){
        int x, y;

        do {
            x = random.nextInt(SIZE);
            y = random.nextInt(SIZE);
        } while (!isValid(x, y));
        map[x][y] = ELEMENT_O;
    }

//    ХОД ИГРОКА
    public static void turnPlayer(){
        int x, y;

        do {
            System.out.println("Сделайте ход");
            System.out.println("Введите координату - Х");
            x = scanner.nextInt() - 1;
            System.out.println("Введите координату - Y");
            y = scanner.nextInt() - 1;
        } while (!isValid(x, y));
            map[x][y] = ELEMENT_X;
    }

//    ПРОВЕРКА ХОДА
    private static boolean isValid(int x, int y) {
        if (x < 0 || y < 0 || x >= SIZE || y >= SIZE){
            return false;
        }
        if (map[x][y] != ELEMENT_EMPTY){
            return false;
        }
        return true;
    }

    //    ПЕЧАТЬ ПОЛЯ
    private static void printMap() {
        System.out.print("  ");                                 // печатаем 2 пробела перед номерами столбцов (для красоты)
        for (int i = 1; i <= SIZE; i++) {                       // создаем цикл для печати номеров столбцов
            System.out.print(i + " ");                          // печатаем номера столбцов
        }
        System.out.println();                                   // после номеров столбцов делаем пробел
//           циклы для печати поля (массива)
        for (int i = 0; i < SIZE; i++) {
            System.out.print(i + 1 + " ");                      // печатаем номера строк
            for (int j = 0; j < SIZE; j++) {
                System.out.print(map[i][j] + " ");              // печать поля
            }
            System.out.println();                               // печать поля
        }
    }
//    ИНИЦИАЛИЗАЦИЯ ПОЛЯ
    public static void initMap (){
        map = new char[SIZE][SIZE];                             // Создаем массив (игровое поле)
//           циклы для заполнения поля (массива)
        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                map[i][j] = ELEMENT_EMPTY;                     // Заполняем массив символами
            }
        }
    }
}
