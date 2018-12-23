package Frontend;

import javax.swing.*;
import java.awt.*;
import java.util.Vector;

public class CandidatePanel extends JPanel {
    Vector<String> wordlist;
    private JLabel[] labelWord;
    private int length;
    CandidatePanel(){
        super();
        wordlist = new Vector<>(10);
        labelWord = new JLabel[10];
        length = 0;
        this.setLayout(new BoxLayout(this, BoxLayout.LINE_AXIS));
        this.setPreferredSize(new Dimension(1000, 50));
        this.setMinimumSize(new Dimension(1000, 50));
        Dimension wordSize = new Dimension(98, 50);
        Font wordFont = new Font("Times New Roman", Font.PLAIN, 14);
        Dimension rigid = new Dimension(2, 0);
        for(int i = 0; i < 10; i++){
            labelWord[i] = new JLabel();
            labelWord[i].setOpaque(true);
            // labelWord[i].setHorizontalAlignment(SwingConstants.CENTER);
            labelWord[i].setPreferredSize(wordSize);
            labelWord[i].setMaximumSize(wordSize);
            labelWord[i].setMinimumSize(wordSize);
            labelWord[i].setFont(wordFont);
            labelWord[i].setBackground(Color.PINK);
            this.add(labelWord[i]);
            this.add(Box.createRigidArea(rigid));
        }
        this.setVisible(true);
    }

    void setWordlist(String[] words){
        length = (words.length > 10) ? 10 : words.length;
        for(int i = 0; i < length; i++) {
            wordlist.add(words[i]);
            labelWord[i].setText(Integer.toString(i) + ". " + words[i]);
        }
    }

    String chooseWord(int num){
        String word = ""  + wordlist.get(num);
        for(int i = 0; i < length; i++){
            labelWord[i].setText("");
        }
        wordlist.removeAllElements();
        length = 0;
        return word;
    }
}
