package Frontend;
import javax.swing.JLabel;
import java.awt.*;

class Key extends JLabel{
    private String strKeyNum;
    Key(String key){
        strKeyNum = key;
        this.setBackground(Color.CYAN);
        this.setOpaque(true);
        this.setText(strKeyNum);
    }

    void beClicked(){
        this.setBackground(Color.YELLOW);
        this.setText(strKeyNum);
    }
}
