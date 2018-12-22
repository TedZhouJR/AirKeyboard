import Backend.LeapMotionListener;
import Frontend.mainWindow;
import com.leapmotion.leap.Controller;

import java.io.IOException;

import java.io.Console;
import java.util.Scanner;


public class Main {

    public static void main(String[] args){
        mainWindow mainwindow = new mainWindow("Air Key Board");
//        mainwindow.showCenter();
//        int num = 10;             //DEBUG
//        String key = null;
//        Scanner console = new Scanner(System.in);
//        while(true){
//            System.out.println("Please input push key");
//            key = console.next();
//            mainwindow.pushKey(key);
//            System.out.println("Please input release key");
//            key = console.next();
//            mainwindow.releaseKey(key);
//        }

        // Create a sample listener and controller
        LeapMotionListener listener = new LeapMotionListener(mainwindow);
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
