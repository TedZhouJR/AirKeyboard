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
        String a[] = new String[4];
        a[0] = "worc";
        a[1] = "word";
        a[2] = "worr";
        a[3] = "worf";
        Corrector corrector = new Corrector();
        List<String> list = new ArrayList<>();
        list = corrector.dealWith(a);
        for (int kk=0;kk<list.size(); kk++) {
            System.out.println(list.get(kk));
        }
        /*mainWindow mainwindow = new mainWindow("Air Key Board");
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
        controller.removeListener(listener);*/
    }

}
