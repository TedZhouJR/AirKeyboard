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
        int pos = area.getCaretPosition();
        area.insert(key, pos);
        area.setCaretPosition(pos + 1);
    }

    void backSpace(){
        int length = area.getCaretPosition();
        if(length != 0) {
            area.select(length - 1, length);
            area.replaceSelection("");
            area.setCaretPosition(length - 1);
        }
    }

    String getText(){
        int pos = area.getCaretPosition();
        return area.getText().substring(0, pos);
    }

    void changeLastWord(String word, String lastWord){
        int pos = area.getCaretPosition();
        String text = area.getText();
        int wordLen = lastWord.length();
        area.replaceRange(word + " ", pos - wordLen, pos);
        area.setCaretPosition(pos - wordLen + word.length() + 1);
    }

    String[] moveCursor(boolean isLeft){
        int pos = area.getCaretPosition();
        int length = area.getText().length();
        if(isLeft) {
            if(pos > 0) {
                pos = pos - 1;
                area.setCaretPosition(pos);
            }
            else{
                return null;
            }
        }
        else{
            if(pos < length){
                pos = pos + 1;
                area.setCaretPosition(pos);
            }
            else{
                return null;
            }
        }
        String preText = area.getText().substring(0, pos);
        return preText.split(" +");
    }
}
