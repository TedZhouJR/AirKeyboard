package Frontend;
import java.awt.*;
import javax.swing.*;

public class mainWindow extends JFrame{
    private Key[] keyboard;

    public mainWindow(String windowName){
        super(windowName);
        this.setLocationRelativeTo(null);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
        setSize(300, 400);
    }
}
