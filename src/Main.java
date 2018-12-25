import Backend.Corrector;
import Backend.LeapMotionListener;
import Frontend.mainWindow;
import com.leapmotion.leap.Controller;

import java.io.IOException;

import java.io.Console;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;


public class Main {

    public static void main(String[] args){
        Corrector conrrector = new Corrector();
        mainWindow mainwindow = new mainWindow("Air Key Board", conrrector);
        // mainwindow.showCenter();
        int num = 10;             //DEBUG
        float [] Xs = new float[10];
        float [] Ys = new float[10];
        int gesture = -1;
        while(true){
            Scanner console = new Scanner(System.in);
            System.out.println("Please input gesture");
            gesture = console.nextInt();
            switch (gesture) {
                case mainWindow.DEFAULT:
                    System.out.println("Please input num");
                    num = console.nextInt();
                    System.out.println("Please input location");
                    for (int i = 0; i < num; i++) {
                        Xs[i] = console.nextFloat();
                        Ys[i] = console.nextFloat();
                    }
                    break;
                case mainWindow.MOVECURSOR:
                    System.out.println("Please input X");
                    Xs[0] = console.nextFloat();
                    num = 1;
                    break;
                case mainWindow.DELETE:
                    System.out.println("Please input X");
                    Xs[0] = console.nextFloat();
                    System.out.println("Please input angle");
                    Ys[0] = console.nextFloat();
                    num = 1;
                    break;
                default:
                    break;
            }
            mainwindow.update(num, Xs, Ys, new boolean[10], gesture);
        }

        // Create a sample listener and controller
//        LeapMotionListener listener = new LeapMotionListener(mainwindow, conrrector);
//        Controller controller = new Controller();
//
//        // Have the sample listener receive events from the controller
//        controller.addListener(listener);
//
//        // Keep this process running until Enter is pressed
//        System.out.println("Press Enter to quit...");
//        try {
//            System.in.read();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//
//        // Remove the sample listener when done
//        controller.removeListener(listener);
    }

}
