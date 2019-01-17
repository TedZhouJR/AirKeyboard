package Frontend;
import javax.swing.*;
import java.awt.*;

class Key extends JLabel{
    private String strKeyNum;
    private boolean boolPushed;
    private Color normal;
    private Color current;
    private int keyNum;
    Key(String key, int num){
        boolPushed = false;
        strKeyNum = key;
        keyNum = num;
        normal = new Color(101, 118, 133);
        current = normal;
        this.setBackground(current);
        this.setForeground(Color.WHITE);
        this.setOpaque(true);
        this.setText(strKeyNum);
        // this.repaint();
    }

    private void beClicked(){
        // this.setBackground(Color.YELLOW);
        current = current.darker();
        this.setBackground(current);
        boolPushed = true;
        repaint();
    }
    
    private void beRealsed(){
        // this.setBackground(Color.GRAY);
        current = normal;
        this.setBackground(current);
        boolPushed = false;
        repaint();
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
        if(target.equals(KeyPanel.keynum[keyNum].toUpperCase()) || target.equals(KeyPanel.numberkey[keyNum].toUpperCase())){
            this.beRealsed();
        }
    }

    void setKey(String target){
        this.strKeyNum = target;
        this.setText(target);
    }

//    @Override
//    public void paintComponent(Graphics g){
//        g.setColor(current);
//        g.drawRoundRect(0, 0, 80, 80, 5, 5);
//    }
}
