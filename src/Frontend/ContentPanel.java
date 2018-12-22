package Frontend;

import javax.swing.*;
import java.awt.*;
import java.util.Vector;

public class ContentPanel extends JComponent {
    private int fingerNum;
    private Vector<Location> fingerLocation;
    private boolean[] push;

    ContentPanel(){
        super();
        fingerNum = 0;
        fingerLocation = new Vector<>(37);   // show center DEBUG
        for (int i = 0; i < 37; i++){
            fingerLocation.add(new Location(0, 0));
        }
        push = new boolean[37];                          // show center DEBUG
    }

    void update(int num, int[] Xs, int[] Ys, boolean[] push){
        //System.out.println("Glass update");
        fingerNum = num;
        for (int i = 0; i < num; i++){
            fingerLocation.get(i).setLocation(Xs[i], Ys[i] + 300);
            this.push[i] = push[i];
        }
    }

    @Override
    public void paint(Graphics g){
        //System.out.println("Paint");
        for (int i = 0; i < fingerNum; i++){
            int x = fingerLocation.get(i).getX();
            int y = fingerLocation.get(i).getY();
            if(y >= 300) {
                if(!push[i]) {
                    g.setColor(Color.RED);
                }
                else {
                    g.setColor(Color.YELLOW);
                }
                g.fillOval(x - 8, y - 8, 16, 16);
                // System.out.printf("(%d, %d)\n", x, y);
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