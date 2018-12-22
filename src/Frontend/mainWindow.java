package Frontend;
import javax.swing.*;
import java.awt.*;

public class mainWindow extends JFrame{
    private KeyPanel keyPanel;
    private TextPanel textPanel;
    private ContentPanel glassPanel;
    private CandidatePanel candidatePanel;
    public String inputWord;
    public String prefixWord;
    private int[] Xs;
    private int[] Ys;
    public mainWindow(String windowName){
        super(windowName);
        Xs = new int[10];
        Ys = new int[10];
        keyPanel = new KeyPanel();
        // keyPanel.setBackground(Color.blue);  // DEBUG
        keyPanel.setPreferredSize(new Dimension(1000, 400));
        textPanel = new TextPanel();
        // textPanel.setBackground(Color.RED);   // DEBUG
        textPanel.setPreferredSize(new Dimension(1000, 300));
        candidatePanel = new CandidatePanel();
        candidatePanel.setPreferredSize(new Dimension(1000, 50));
        JPanel contentPanel = (JPanel)this.getContentPane();
        contentPanel.setSize(1000, 750);
        contentPanel.add(keyPanel, BorderLayout.SOUTH);
        contentPanel.add(candidatePanel, BorderLayout.CENTER);
        contentPanel.add(textPanel, BorderLayout.NORTH);
        glassPanel = new ContentPanel();
        this.setGlassPane(glassPanel);
        glassPanel.setVisible(true);
        this.setLocationRelativeTo(null);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setContentPane(contentPanel);
        this.pack();
        setVisible(true);
        this.setResizable(false);
        prefixWord = "";
        inputWord = "";
        // setSize(1000, 780);   // 30为最上面一行的大小
        setVisible(true);
    }

    public void update(int num, float[] X, float[] Y, boolean[] push, String target){
        // System.out.println("Begin update");
        for(int i = 0; i < num; i++){
            // 用于float的坐标变换到电脑上的像素点坐标，目前是将400mm × 120mm大小虚拟键盘映射到小键盘上
            // 然后将坐标中心从键盘中间平移到左上角，以正常显示
            // TODO: 键盘可现实点的像素大小变为1000 × 400，键盘坐标应该如何改动？
            Xs[i] = (int)(((X[i] + 200.0) / 400.0) * 1000);
            Ys[i] = (int)(((Y[i] + 60.0) / 120.0) * 400);
        }
        glassPanel.update(num, Xs, Ys, push);
        glassPanel.repaint();
    }

    public void pushKey(String target, String[] words){
        keyPanel.pushKey(target.toUpperCase());
        if(!target.toUpperCase().equals(KeyPanel.keynum[36].toUpperCase())) {
            int targetLen = target.length();
            if(targetLen == 1){
                char first = target.charAt(0);
                if((first >= '0') && (first <= '9')){
                    prefixWord = candidatePanel.chooseWord(Integer.parseInt(target));
                    textPanel.changeLastWord(prefixWord, inputWord);
                    inputWord = "";
                    return;
                }
            }
            textPanel.inputKey(target.toLowerCase());
            inputWord = inputWord + target.toLowerCase();
            candidatePanel.setWordlist(words);               // 显示候选单词
            // TODO: 如何轮回的显示输入字符，即一个键被一直按下之后应该怎样不间断输出。
        }
        else {
            // BackSpace operation
            textPanel.backSpace();
            if(inputWord.length() > 0) {
                int length = inputWord.length() - 1;
                inputWord = inputWord.substring(0, length);
                if(inputWord.equals("")){
                    return;
                }
            }
            else{
                String text = textPanel.getText();
                String[] preWords = text.split(" +");
                if(preWords.length > 2){
                    inputWord = preWords[preWords.length - 1];
                    prefixWord = preWords[preWords.length - 2];
                }
                else if(preWords.length == 1){
                    inputWord = preWords[0];
                    prefixWord = "";
                }
                else{
                    inputWord = "";
                    prefixWord = "";
                    return;
                }
            }
            String lastKey = Character.toString(inputWord.charAt(inputWord.length() - 1));
            // 调用东池的函数重新计算wordlist并显示
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
