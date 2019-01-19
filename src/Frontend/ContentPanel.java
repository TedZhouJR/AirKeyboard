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
    private ImageIcon click;
    private ImageIcon unclick;

    ContentPanel(){
        super();
        fingerNum = 0;
        fingerLocation = new Vector<>(KeyPanel.KEYNUM + 6);   // show center DEBUG
        for (int i = 0; i < KeyPanel.KEYNUM + 6; i++){
            fingerLocation.add(new Location(0, 0));
        }
        push = new boolean[KeyPanel.KEYNUM + 6];                          // show center DEBUG
        this.gesture = -1;
        this.toDraw = null;
        this.angle = 0;
        click = new ImageIcon("./resource/click.png");
        unclick = new ImageIcon("./resource/float.png");
    }

    void update(int num, int[] Xs, int[] Ys, boolean[] push){
        //System.out.println("Glass update");
        fingerNum = num;
        for (int i = 0; i < num; i++){
            fingerLocation.get(i).setLocation(Xs[i], Ys[i] + 300);
            this.push[i] = push[i];
        }
        this.gesture = mainWindow.DEFAULT;
        this.toDraw = null;
    }

    void moveGesture(int X, ImageIcon toDraw){
        this.gesture = mainWindow.MOVECURSOR;
        this.toDraw = toDraw;
        this.fingerNum = 1;
        fingerLocation.get(0).setLocation(X, 600);
    }

    void deleteGesture(double angle, ImageIcon toDraw){
        this.gesture = mainWindow.DELETE;
        this.toDraw = toDraw;
        this.fingerNum = 1;
        this.angle = -angle;
        fingerLocation.get(0).setLocation(800, 600);
    }

    private void drawGesture(Graphics2D g){
        float width = toDraw.getIconWidth();
        float height = toDraw.getIconHeight();
        g.drawImage(toDraw.getImage(), -40, -200,
                  80, 200, null);
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
                            g.drawImage(unclick.getImage(), x - 20, y - 20, 40, 40, null);
                        } else {
                            g.drawImage(click.getImage(), x - 20, y - 20, 40, 40, null);
                        }
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