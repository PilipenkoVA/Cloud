package Game_One;

public class Game {
    int value1 ;
    int value2 ;
    int value3 ;

    public void start(){

        Player p1 = new Player();
        Player p2 = new Player();
        Player p3 = new Player();

        while (true){
            System.out.println("Задача отгадать число от 0 до 20...");
            int compVal = (int) (Math.random()*20);
            System.out.println("Загаданное число: " + compVal);
            System.out.print("\n");

            p1.guess();
            p2.guess();
            p3.guess();

            value1 = p1.number;
            System.out.println("Первый игрок думает что это число: "+ value1);
            value2 = p2.number;
            System.out.println("Второй игрок думает что это число: "+ value2);
            value3 = p3.number;
            System.out.println("Третий игрок думает что это число: "+ value3);

            System.out.print("\n");

            if (value1 == compVal){
                System.out.println("У нас есть победитель!!!");
                System.out.println("Победил  игрок номер один");
                break;
            }
            if (value2 == compVal){
                System.out.println("У нас есть победитель!!!");
                System.out.println("Победил игрок номер два");
                break;
            }
            if (value3 == compVal){
                System.out.println("У нас есть победитель!!!");
                System.out.println("Победил игрок номер три");
                break;
            }else {
                System.out.println("Еще один круг так как ни что не угадал");
                System.out.print("_________________________________________\n");
            }
        }

    }
}
