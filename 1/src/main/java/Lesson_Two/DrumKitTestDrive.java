package Lesson_Two;

class DrumKit{

    boolean topHat = true;
    boolean share = true;


    void playShare(){
        System.out.println("бах бах ба-бах");
    }

    void playTopHat(){
        System.out.println("динь динь ди-динь");
    }
}
class DrumKitTestDrive {

    public static void main(String[] args) {
        DrumKit d = new DrumKit();

        d.playShare();

        d.share = false;

        if (d.share == true){
            d.playShare();
        }

        d.playTopHat();
    }
}