package Frontend;

import javax.swing.*;
import java.awt.*;

class TextPanel extends JPanel {
    JTextArea area;
    TextPanel(){
        super();
        Font areaFont = new Font("Times New Roman",0,20);
        area = new JTextArea();
        area.setLineWrap(true);
        area.setWrapStyleWord(true);
        area.setVisible(true);
        area.setFont(areaFont);
        JScrollPane areaScrollPane = new JScrollPane(area);
        areaScrollPane.setVerticalScrollBarPolicy(
                JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        areaScrollPane.setPreferredSize(new Dimension(900, 270));
        this.add(areaScrollPane);
    }
}
