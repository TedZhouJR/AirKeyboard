package Frontend;
import javax.swing.*;
import java.awt.*;

public class mainWindow extends JFrame{
    private KeyPanel keyPanel;
    private TextPanel textPanel;
    public mainWindow(String windowName){
        super(windowName);
        keyPanel = new KeyPanel();
        keyPanel.setPreferredSize(new Dimension(500, 300));
        textPanel = new TextPanel();
        textPanel.setPreferredSize(new Dimension(500, 300));
        JPanel contentPanel = (JPanel) this.getContentPane();
        contentPanel.add(keyPanel, BorderLayout.CENTER);
        contentPanel.add(textPanel, BorderLayout.NORTH);
        this.setLocationRelativeTo(null);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
        setSize(1000, 600);
    }
}
