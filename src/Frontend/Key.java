package Frontend;
import javax.swing.JLabel;
import java.awt.*;

class Key extends JLabel{
    private String strKeyNum;
    private boolean boolPushed;
    Key(String key){
        boolPushed = false;
        strKeyNum = key;
        this.setBackground(Color.CYAN);
        this.setOpaque(true);
        this.setText(strKeyNum);
    }

    private void beClicked(){
        this.setBackground(Color.YELLOW);
        boolPushed = true;
    }
    
    private void beRealsed(){
        this.setBackground(Color.CYAN);
        boolPushed = false;
    }
    
    boolean getStatus(){
        return this.boolPushed;
    }
    void checkPush(String target){
        if (target.equals(strKeyNum.toUpperCase())){
            this.beClicked();
        }
    }

    void checkRelease(String target){
        if(target.equals(strKeyNum.toUpperCase())){
            this.beRealsed();
        }
    }
}
