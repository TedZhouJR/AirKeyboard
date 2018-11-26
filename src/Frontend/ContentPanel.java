package Frontend;

import javax.swing.*;
import java.awt.*;
import java.util.Vector;

public class ContentPanel extends JComponent {
    private int fingerNum;
    private Vector<Location> fingerLocation;

    ContentPanel(){
        super();
        fingerNum = 0;
        fingerLocation = new Vector<>(10);
        for (int i = 0; i < 10; i++){
            fingerLocation.add(new Location(0, 0));
        }
    }

    void update(int num, int[] Xs, int[] Ys){
        //System.out.println("Glass update");
        fingerNum = num;
        for (int i = 0; i < num; i++){
            fingerLocation.get(i).setLocation(Xs[i], Ys[i] + 300);
        }
    }

    @Override
    public void paint(Graphics g){
        //System.out.println("Paint");
        g.setColor(Color.RED);
        for (int i = 0; i < fingerNum; i++){
            int x = fingerLocation.get(i).getX();
            int y = fingerLocation.get(i).getY();
            if(y >= 300) {
                g.fillOval(x - 8, y - 8, 16, 16);
                //System.out.printf("(%d, %d)\n", x, y);
            }
        }
    }
}

class Location{
    private int X;
    private int Y;
    Location(int X, int Y){
        this.X = X;
        this.Y = Y;
    }

    void setLocation(int X, int Y){
        this.X = X;
        this.Y = Y;
    }

    int getX(){
        return this.X;
    }

    int getY(){
        return this.Y;
    }
}