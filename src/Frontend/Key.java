package Frontend;
import javax.swing.JLabel;
import java.awt.*;

class Key extends JLabel{
    private String strKeyNum;
    Key(String key){
        strKeyNum = key;
        this.setOpaque(true);
    }

    void beClicked(){
        this.setBackground(Color.YELLOW);
        this.setText(strKeyNum);
    }
}
