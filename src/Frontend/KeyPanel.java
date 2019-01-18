package Frontend;

import javax.swing.*;
import java.awt.*;

public class KeyPanel extends JPanel {
    private Key[] keys;
    public static final String[] keynum = {"Q", "W", "E", "R", "T", "Y", "U", "I", "O", "P",
                                          "A", "S", "D", "F", "G", "H", "J", "K", "L",
                                          "?123", "Z", "X", "C", "V", "B", "N", "M",
                                            "n0", "n1", "n2", "n3", "n4", "n5"};
    public static final String[] numberkey = {"", "", "?", "-", "\"","" , "0", "1", "2", "3",
                                              "", "", ",", ".", "!", "", "4", "5", "6",
                                              "ABC", "", "", "'", "", "7", "8", "9",
                                              "n0", "n1", "n2", "n3", "n4", "n5"};
    //1000*300
    public static final float[] keyY = {165, 165, 165, 165, 165, 165, 165, 165, 165, 165,
                                        250, 250, 250, 250, 250, 250, 250, 250, 250,
                                        335, 335, 335, 335, 335, 335, 335, 335,
                                        50, 50, 50, 50, 50, 50};
    public static final float[] keyX = {117, 202, 287, 372, 457, 542, 627, 712, 797, 882,
                                        160, 245, 330, 415, 500, 585, 670, 755, 840,
                                        202, 287, 372, 457, 542, 627, 712, 797,
                                        120, 272, 424, 576, 728, 880};
    KeyPanel(Color panelColor){
        super();

        /* init the keyboard (keyboard size 1000 * 300)*/
        this.setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
        Dimension panelDimension = new Dimension(800, 80);
//        JPanel numPanel = new JPanel();
//        numPanel.setLayout(new BoxLayout(numPanel, BoxLayout.LINE_AXIS));
//        numPanel.setPreferredSize(panelDimension);
        JPanel top = new JPanel();
        top.setLayout(new BoxLayout(top, BoxLayout.LINE_AXIS));
        top.setPreferredSize(panelDimension);
        JPanel middle = new JPanel();
        middle.setLayout(new BoxLayout(middle, BoxLayout.LINE_AXIS));
        middle.setPreferredSize(panelDimension);
        JPanel bottom = new JPanel();
        bottom.setLayout(new BoxLayout(bottom, BoxLayout.LINE_AXIS));
        bottom.setPreferredSize(panelDimension);
        Dimension rigidBetweenPanel = new Dimension(0, 5);    // 纵向的间隔宽度5像素
        Dimension rigidTopPanel = new Dimension(0, 25);       // 最上层间隔25像素
//        this.add(numPanel);
//        this.add(Box.createRigidArea(rigidBetweenPanel));
        this.add(Box.createRigidArea(rigidTopPanel));
        this.add(top);
        this.add(Box.createRigidArea(rigidBetweenPanel));
        this.add(middle);
        this.add(Box.createRigidArea(rigidBetweenPanel));
        this.add(bottom);
        /*
        set background color for panel
         */
//        numPanel.setBackground(panelColor);
        top.setBackground(panelColor);
        middle.setBackground(panelColor);
        bottom.setBackground(panelColor);

        keys = new Key[27];
        Dimension keyDimension = new Dimension(80, 80);       // 键盘大小 80 × 80
        Dimension rigidBoxDimension = new Dimension(5, 0);    // 键盘之间横向间隔 5像素
        Font keyFont = new Font("Times New Roman", Font.PLAIN, 20);
        for (int i = 0; i < 27; i++){
            keys[i] = new Key(keynum[i], i);
            keys[i].setHorizontalAlignment(SwingConstants.CENTER);
            keys[i].setPreferredSize(keyDimension);
            keys[i].setMinimumSize(keyDimension);
            keys[i].setMaximumSize(keyDimension);
            keys[i].setFont(keyFont);
        }
//        for (int i = 0; i < 10; i++){
//            numPanel.add(keys[i]);
//            if(i != 9){
//                numPanel.add(Box.createRigidArea(rigidBoxDimension));
//            }
//        }
        for (int i = 0; i < 10; i++){
            top.add(keys[i]);
            if(i != 9){
                top.add(Box.createRigidArea(rigidBoxDimension));
            }
        }
        for (int i = 10; i < 19; i ++){
            middle.add(keys[i]);
            if(i != 18){
                middle.add(Box.createRigidArea(rigidBoxDimension));
            }
        }
        for (int i = 19; i < 27; i ++){
            bottom.add(keys[i]);
            if(i != 26){
                bottom.add(Box.createRigidArea(rigidBoxDimension));
            }
        }
//        keys[36] = new Key(keynum[36]);
//        keys[36].setHorizontalAlignment(SwingConstants.CENTER);
//        Dimension backspaceDimen = new Dimension(120, 80);
//        keys[36].setPreferredSize(backspaceDimen);
//        keys[36].setMinimumSize(backspaceDimen);
//        keys[36].setMaximumSize(backspaceDimen);
//        keys[36].setFont(new Font("Times New Roman", Font.PLAIN, 20));
//        numPanel.add(Box.createRigidArea(rigidBoxDimension));
//        numPanel.add(keys[36]);
    }

    void pushKey(String target){
        for(int i = 0; i < 27; i++){
            keys[i].checkPush(target);
        }
    }

    void releaseKey(String target){
        for(int i = 0; i < 27; i++){
            keys[i].checkRelease(target);
        }
    }

    void changeToNum(){
        for(int i = 0; i < 27; i++){
            keys[i].setKey(KeyPanel.numberkey[i]);
        }
    }

    void changeToChar(){
        for(int i = 0; i < 27; i++){
            keys[i].setKey(KeyPanel.keynum[i]);
        }
    }
}
