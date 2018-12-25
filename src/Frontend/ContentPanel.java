package Frontend;

import javax.swing.*;
import java.awt.*;
import java.util.Vector;

public class ContentPanel extends JComponent {
    private int fingerNum;
    private Vector<Location> fingerLocation;
    private boolean[] push;
    private int gesture;
    private ImageIcon toDraw;
    private double angle;

    ContentPanel(){
        super();
        fingerNum = 0;
        fingerLocation = new Vector<>(37);   // show center DEBUG
        for (int i = 0; i < 37; i++){
            fingerLocation.add(new Location(0, 0));
        }
        push = new boolean[37];                          // show center DEBUG
        this.gesture = -1;
        this.toDraw = null;
        this.angle = 0;
    }

    void update(int num, int[] Xs, int[] Ys, boolean[] push){
        //System.out.println("Glass update");
        fingerNum = num;
        for (int i = 0; i < num; i++){
            fingerLocation.get(i).setLocation(Xs[i], Ys[i] + 350);
            this.push[i] = push[i];
        }
        this.gesture = mainWindow.DEFAULT;
        this.toDraw = null;
    }

    void moveGesture(int X, ImageIcon toDraw){
        this.gesture = mainWindow.MOVECURSOR;
        this.toDraw = toDraw;
        this.fingerNum = 1;
        fingerLocation.get(0).setLocation(X, 650);
    }

    void deleteGesture(int X, double angle, ImageIcon toDraw){
        this.gesture = mainWindow.DELETE;
        this.toDraw = toDraw;
        this.fingerNum = 1;
        this.angle = -angle;
        fingerLocation.get(0).setLocation(X, 650);
    }

    private void drawGesture(Graphics2D g){
        float width = toDraw.getIconWidth();
        float height = toDraw.getIconHeight();
        g.drawImage(toDraw.getImage(), -25, -200,
                  50, 200, null);
    }

    @Override
    public void paint(Graphics g){
        //System.out.println("Paint");
        Graphics2D g2 = (Graphics2D) g;
        switch (gesture) {
            case mainWindow.DEFAULT:
                for (int i = 0; i < fingerNum; i++) {
                    int x = fingerLocation.get(i).getX();
                    int y = fingerLocation.get(i).getY();
                    if (y >= 300) {
                        if (!push[i]) {
                            g.setColor(Color.RED);
                        } else {
                            g.setColor(Color.YELLOW);
                        }
                        g.fillOval(x - 8, y - 8, 16, 16);
                        // System.out.printf("(%d, %d)\n", x, y);
                    }
                }
                break;
            case mainWindow.DELETE:
                System.out.println("DELETE GESTURE");
                g2.translate(fingerLocation.get(0).getX(), fingerLocation.get(0).getY());
                g2.rotate(this.angle);
                drawGesture(g2);
                g2.rotate(-this.angle);
                g2.translate(-fingerLocation.get(0).getX(), fingerLocation.get(0).getY());
                break;
            case mainWindow.MOVECURSOR:
                System.out.println("MOVECURSOR");
                g2.translate(fingerLocation.get(0).getX(), fingerLocation.get(0).getY());
                drawGesture(g2);
                g2.translate(-fingerLocation.get(0).getX(), fingerLocation.get(0).getY());
                break;
            default:
                break;
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