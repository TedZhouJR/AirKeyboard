package Frontend;

import javax.swing.*;
import java.awt.*;

public class KeyPanel extends JPanel {
    private Key[] keys;
    private static final String[] keynum = {"Q", "W", "E", "R", "T", "Y", "U", "I", "O", "P",
                                          "A", "S", "D", "F", "G", "H", "J", "K", "L",
                                          "Z", "X", "C", "V", "B", "N", "M"};
    KeyPanel(){
        super();
        this.setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
        JPanel top = new JPanel();
        Dimension panelDimension = new Dimension(800, 80);
        top.setLayout(new BoxLayout(top, BoxLayout.LINE_AXIS));
        top.setPreferredSize(panelDimension);
        JPanel middle = new JPanel();
        middle.setLayout(new BoxLayout(middle, BoxLayout.LINE_AXIS));
        middle.setPreferredSize(panelDimension);
        JPanel bottom = new JPanel();
        bottom.setLayout(new BoxLayout(bottom, BoxLayout.LINE_AXIS));
        bottom.setPreferredSize(panelDimension);
        this.add(top);
        this.add(Box.createVerticalGlue());
        this.add(middle);
        this.add(Box.createVerticalGlue());
        this.add(bottom);
        keys = new Key[26];
        Dimension keyDimension = new Dimension(80, 80);
        Dimension rigidBoxDimension = new Dimension(5, 0);
        for (int i = 0; i < 26; i++){
            keys[i] = new Key(keynum[i]);
            keys[i].setHorizontalAlignment(SwingConstants.CENTER);
            keys[i].setPreferredSize(keyDimension);
            keys[i].setMinimumSize(keyDimension);
            keys[i].setMaximumSize(keyDimension);
        }
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
        for (int i = 19; i < 26; i ++){
            bottom.add(keys[i]);
            if(i != 25){
                bottom.add(Box.createRigidArea(rigidBoxDimension));
            }
        }
    }
}
