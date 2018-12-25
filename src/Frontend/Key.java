package Frontend;
import javax.swing.JLabel;
import java.awt.*;

class Key extends JLabel{
    private String strKeyNum;
    private boolean boolPushed;
    private Color current;
    Key(String key){
        boolPushed = false;
        strKeyNum = key;
        current = new Color(101, 118, 133);
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
        current = current.brighter();
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
        if(target.equals(strKeyNum.toUpperCase())){
            this.beRealsed();
        }
    }

//    @Override
//    public void paintComponent(Graphics g){
//        g.setColor(current);
//        g.drawRoundRect(0, 0, 80, 80, 5, 5);
//    }
}
