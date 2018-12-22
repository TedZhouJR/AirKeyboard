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
        keyPanel.setPreferredSize(new Dimension(1000, 400));
        textPanel = new TextPanel();
        textPanel.setPreferredSize(new Dimension(1000, 300));
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
        setSize(1000, 700);
    }

    public void update(int num, float[] X, float[] Y, boolean[] push, String target){
        // System.out.println("Begin update");
        for(int i = 0; i < num; i++){
            // 用于float的坐标变换到电脑上的像素点坐标，目前是将400mm × 120mm大小虚拟键盘映射到小键盘上
            // 然后将坐标中心从键盘中间平移到左上角，以正常显示
            // TODO: 键盘可现实点的像素大小变为1000 × 400，键盘坐标应该如何改动？
            Xs[i] = (int)(((X[i] + 200.0) / 400.0) * 1000);
            Ys[i] = (int)(((Y[i] + 60.0) / 120.0) * 300);
        }
        glassPanel.update(num, Xs, Ys, push);
        glassPanel.repaint();
    }

    public void pushKey(String target){
        keyPanel.pushKey(target.toUpperCase());
        if(!target.toUpperCase().equals(KeyPanel.keynum[36].toUpperCase())) {
            textPanel.inputKey(target.toLowerCase());
            // TODO: 如何轮回的显示输入字符，即一个键被一直按下之后应该怎样不间断输出。
        }
        else {
            // BackSpace operation
            textPanel.backSpace();
        }
    }

    public void releaseKey(String target){
        keyPanel.releaseKey(target.toUpperCase());
    }

    public void showCenter(){
        int[] centerX = new int[37];
        int[] centerY = new int[37];
        for(int i = 0; i < 37; i++){
            centerX[i] = (int)KeyPanel.keyX[i];
            centerY[i] = (int)KeyPanel.keyY[i];
            System.out.printf("(%d, %d)\n", centerX[i], centerY[i]);
        }
        System.out.println(KeyPanel.keyX.length);
        System.out.println(KeyPanel.keyY.length);
        glassPanel.update(37, centerX, centerY, new boolean[37]);
        glassPanel.repaint();
    }
}
