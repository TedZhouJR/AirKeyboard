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
//        mainwindow.showCenter();
//        int num = 10;             //DEBUG
//        float [] Xs = new float[10];
//        float [] Ys = new float[10];
//        while(true){
//            System.out.println("Please input num");
//            Scanner console = new Scanner(System.in);
//            num = console.nextInt();
//            System.out.println("Please input location");
//            for (int i = 0; i < num; i++){
//                Xs[i] = console.nextFloat();
//                Ys[i] = console.nextFloat();
//            }
//            mainwindow.update(num, Xs, Ys, new boolean[10], null);
//        }

        // Create a sample listener and controller
        LeapMotionListener listener = new LeapMotionListener(mainwindow, conrrector);
        Controller controller = new Controller();

        // Have the sample listener receive events from the controller
        controller.addListener(listener);

        // Keep this process running until Enter is pressed
        System.out.println("Press Enter to quit...");
        try {
            System.in.read();
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Remove the sample listener when done
        controller.removeListener(listener);
    }

}
