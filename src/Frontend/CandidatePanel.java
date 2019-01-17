package Frontend;

import javax.swing.*;
import java.awt.*;
import java.util.Vector;

public class CandidatePanel extends JPanel {
    Vector<String> wordlist;
    private Key[] labelWord;
    private int length;
    CandidatePanel(){
        super();
        wordlist = new Vector<>(10);
        labelWord = new Key[6];
        length = 0;
        this.setLayout(new BoxLayout(this, BoxLayout.LINE_AXIS));
        this.setPreferredSize(new Dimension(1000, 100));
        this.setMinimumSize(new Dimension(1000, 100));
        Dimension wordSize = new Dimension(150, 80);
        Font wordFont = new Font("Times New Roman", Font.PLAIN, 16);
        Dimension rigid = new Dimension(2, 0);
        //Color labelColor = new Color(63, 81, 98);
        this.add(Box.createRigidArea(new Dimension(45, 0)));
        for(int i = 0; i < 6; i++){
            labelWord[i] = new Key("n" + i, 27 + i);
            labelWord[i].setText("");
            labelWord[i].setOpaque(true);
            // labelWord[i].setHorizontalAlignment(SwingConstants.CENTER);
            labelWord[i].setPreferredSize(wordSize);
            labelWord[i].setMaximumSize(wordSize);
            labelWord[i].setMinimumSize(wordSize);
            labelWord[i].setFont(wordFont);
            // labelWord[i].setBackground(labelColor);
            // System.out.printf("%d, %d, %d\n", Color.GRAY.getRed(), Color.GRAY.darker().getRed(), Color.DARK_GRAY.getRed());
            // labelWord[i].setForeground(Color.WHITE);
            this.add(labelWord[i]);
            this.add(Box.createRigidArea(rigid));
        }
        this.setVisible(true);
    }

    void setWordlist(String[] words){
        wordlist.removeAllElements();
        for(int i = 0; i < 6; i++){
            labelWord[i].setText("");
        }
        if(words != null) {
            length = (words.length > 6) ? 6 : words.length;
            for (int i = 0; i < length; i++) {
                wordlist.add(words[i]);
                labelWord[i].setHorizontalAlignment(SwingConstants.CENTER);
                labelWord[i].setText(words[i]);
            }
        }
    }

    String chooseWord(int num){
        String word = ""  + wordlist.get(num);
        for(int i = 0; i < length; i++){
            labelWord[i].setText("");
        }
        wordlist.removeAllElements();
        length = 0;
        labelWord[num].checkPush("n" + num);
        return word;
    }

    void releaseKey(int num){
        labelWord[num].checkRelease("n" + num);
    }
}
