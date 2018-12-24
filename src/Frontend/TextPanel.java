package Frontend;

import javax.swing.*;
import java.awt.*;

class TextPanel extends JPanel {
    private JTextArea area;
    TextPanel(){
        super();
        Font areaFont = new Font("Times New Roman",Font.PLAIN,20);
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

    void inputKey(String key){
        area.append(key);
        area.setCaretPosition(area.getText().length());
    }

    void backSpace(){
        int length = area.getText().length();
        if(length != 0) {
            area.setText(area.getText().substring(0, length - 1));
            area.setCaretPosition(area.getText().length());
        }
    }

    String getText(){
        return area.getText();
    }

    void changeLastWord(String word, String lastWord){
        String text = area.getText();
        int wordLen = lastWord.length();
        int textLen = text.length();
        text = text.substring(0, textLen - wordLen) + word + " ";
        area.setText(text);
        area.setCaretPosition(area.getText().length());
    }

    void moveCursor(){
        int pos = area.getCaretPosition();
        area.setCaretPosition(pos - 1);
    }
}
