package Frontend;
import javax.swing.*;
import java.awt.*;

public class mainWindow extends JFrame{
    private KeyPanel keyPanel;
    private TextPanel textPanel;
    private ContentPanel glassPanel;
    private int[] Xs;
    private int[] Ys;
    public mainWindow(String windowName){
        super(windowName);
        Xs = new int[10];
        Ys = new int[10];
        keyPanel = new KeyPanel();
        keyPanel.setPreferredSize(new Dimension(500, 300));
        textPanel = new TextPanel();
        textPanel.setPreferredSize(new Dimension(500, 300));
        JPanel contentPanel = (JPanel)this.getContentPane();
        contentPanel.add(keyPanel, BorderLayout.CENTER);
        contentPanel.add(textPanel, BorderLayout.NORTH);
        glassPanel = new ContentPanel();
        this.setGlassPane(glassPanel);
        glassPanel.setVisible(true);
        this.setLocationRelativeTo(null);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setResizable(false);
        setVisible(true);
        setSize(1000, 600);
    }

    public void update(int num, float[] X, float[] Y, String target){
        System.out.println("Begin update");
        for(int i = 0; i < num; i++){
            //用于float的坐标变换到电脑上的像素点坐标，目前是将400mm × 120mm大小虚拟键盘映射到小键盘上
            // 然后将坐标中心从键盘中间平移到左上角，以正常显示
            Xs[i] = (int)(((X[i] + 200.0) / 400.0) * 1000);
            Ys[i] = (int)(((Y[i] + 60.0) / 120.0) * 300);
        }
        glassPanel.update(num, Xs, Ys);
        glassPanel.repaint();
    }
}
