package Lesson_One;

public class BeerSong {
    public static void main(String[] args) {
        int beer = 99;
        String word = "бутылок (бутылки)";
        System.out.println(beer+" "+word+" пива на стене");
        while (beer > 0){
            if (beer == 1){
                word = "бутылка";
            }
//
//            System.out.println(beer+" "+word+" пива");
            System.out.println("возьми одну");
            System.out.println("пусти по кругу");
            beer= beer - 1;
            if (beer > 0){
                System.out.println(beer+" "+word+" пива на стене");
            }else {
                System.out.println("Нет пива на стене");

            }

        }

    }
}
