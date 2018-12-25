package Frontend;

import javax.swing.*;
import java.awt.*;

public class KeyPanel extends JPanel {
    private Key[] keys;
    public static final String[] keynum = {"0", "1", "2", "3", "4", "5", "6", "7", "8", "9",
                                          "Q", "W", "E", "R", "T", "Y", "U", "I", "O", "P",
                                          "A", "S", "D", "F", "G", "H", "J", "K", "L",
                                          "Z", "X", "C", "V", "B", "N", "M", "Backspace"};
    //1000*400
    public static final float[] keyY = {40, 40, 40, 40, 40, 40, 40, 40, 40, 40,
                                        125, 125, 125, 125, 125, 125, 125, 125, 125, 125,
                                        210, 210, 210, 210, 210, 210, 210, 210, 210,
                                        295, 295, 295, 295, 295, 295, 295, 40};
    public static final float[] keyX = {55, 140, 225, 310, 395, 480, 565, 650, 735, 820,
                                        117, 202, 287, 372, 457, 542, 627, 712, 797, 882,
                                        160, 245, 330, 415, 500, 585, 670, 755, 840,
                                        245, 330, 415, 500, 585, 670, 755, 925};
    KeyPanel(Color panelColor){
        super();
        this.setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
        Dimension panelDimension = new Dimension(800, 80);
        JPanel numPanel = new JPanel();
        numPanel.setLayout(new BoxLayout(numPanel, BoxLayout.LINE_AXIS));
        numPanel.setPreferredSize(panelDimension);
        JPanel top = new JPanel();
        top.setLayout(new BoxLayout(top, BoxLayout.LINE_AXIS));
        top.setPreferredSize(panelDimension);
        JPanel middle = new JPanel();
        middle.setLayout(new BoxLayout(middle, BoxLayout.LINE_AXIS));
        middle.setPreferredSize(panelDimension);
        JPanel bottom = new JPanel();
        bottom.setLayout(new BoxLayout(bottom, BoxLayout.LINE_AXIS));
        bottom.setPreferredSize(panelDimension);
        Dimension rigidBetweenPanel = new Dimension(0, 5);    //纵向的间隔宽度5像素
        this.add(numPanel);
        this.add(Box.createRigidArea(rigidBetweenPanel));
        this.add(top);
        this.add(Box.createRigidArea(rigidBetweenPanel));
        this.add(middle);
        this.add(Box.createRigidArea(rigidBetweenPanel));
        this.add(bottom);
        /*
        set background color for panel
         */
        numPanel.setBackground(panelColor);
        top.setBackground(panelColor);
        middle.setBackground(panelColor);
        bottom.setBackground(panelColor);

        keys = new Key[37];
        Dimension keyDimension = new Dimension(80, 80);       // 键盘大小 80 × 80
        Dimension rigidBoxDimension = new Dimension(5, 0);    // 键盘之间横向间隔 5像素
        Font keyFont = new Font("Times New Roman", Font.PLAIN, 20);
        for (int i = 0; i < 36; i++){
            keys[i] = new Key(keynum[i]);
            keys[i].setHorizontalAlignment(SwingConstants.CENTER);
            keys[i].setPreferredSize(keyDimension);
            keys[i].setMinimumSize(keyDimension);
            keys[i].setMaximumSize(keyDimension);
            keys[i].setFont(keyFont);
        }
        for (int i = 0; i < 10; i++){
            numPanel.add(keys[i]);
            if(i != 9){
                numPanel.add(Box.createRigidArea(rigidBoxDimension));
            }
        }
        for (int i = 10; i < 20; i++){
            top.add(keys[i]);
            if(i != 19){
                top.add(Box.createRigidArea(rigidBoxDimension));
            }
        }
        for (int i = 20; i < 29; i ++){
            middle.add(keys[i]);
            if(i != 28){
                middle.add(Box.createRigidArea(rigidBoxDimension));
            }
        }
        for (int i = 29; i < 36; i ++){
            bottom.add(keys[i]);
            if(i != 35){
                bottom.add(Box.createRigidArea(rigidBoxDimension));
            }
        }
        keys[36] = new Key(keynum[36]);
        keys[36].setHorizontalAlignment(SwingConstants.CENTER);
        Dimension backspaceDimen = new Dimension(120, 80);
        keys[36].setPreferredSize(backspaceDimen);
        keys[36].setMinimumSize(backspaceDimen);
        keys[36].setMaximumSize(backspaceDimen);
        keys[36].setFont(new Font("Times New Roman", Font.PLAIN, 20));
        numPanel.add(Box.createRigidArea(rigidBoxDimension));
        numPanel.add(keys[36]);
    }

    void pushKey(String target){
        for(int i = 0; i < 37; i++){
            keys[i].checkPush(target);
        }
    }

    void releaseKey(String target){
        for(int i = 0; i < 37; i++){
            keys[i].checkRelease(target);
        }
    }
}
