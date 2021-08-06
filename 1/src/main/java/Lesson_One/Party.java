package Lesson_One;

import java.awt.*;

public class Party {

    public static void Botle(int x){
        while (x > 1){
            System.out.println(x + " бутылок (бутылки) пива на стене");
//            System.out.println("Возьми одну пусти по кругу");
            x = x - 1;
            if (x == 1){
                System.out.println(x + " бутылкa пива на стене");
            }
            System.out.println("Возьми одну пусти по кругу");
        }
        System.out.println("the end");
    }
    public static void main(String[] args) {
        Botle(10);
    }
}




