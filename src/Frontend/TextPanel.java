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
