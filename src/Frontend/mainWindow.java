package Frontend;
import Backend.Corrector;

import javax.swing.*;
import java.awt.*;
import java.util.HashMap;
import java.util.Map;

public class mainWindow extends JFrame{
    private KeyPanel keyPanel;
    private TextPanel textPanel;
    private ContentPanel glassPanel;
    private CandidatePanel candidatePanel;
    public String inputWord;
    public String prefixWord;
    private Corrector corrector;
    private int[] Xs;
    private int[] Ys;
    public int keyBoardState;
    /* 定义不同的常量 */
    public final static int DEFAULT = 0;
    public final static int MOVECURSOR = 1;
    public final static int DELETE = 2;
    private final static String moveCursorPath = "./resource/moveCursor.png";
    private final static String deletePath = "./resource/delete.png";
    /*对应键盘的不同状态 */
    public final static int QWERTY = 0;
    public final static int NUMBER = 1;

    private ImageIcon moveCursorIcon;
    private ImageIcon deleteIcon;

    public mainWindow(String windowName, Corrector corrector){
        super(windowName);
        keyBoardState = 0;
        this.corrector = corrector;
        Xs = new int[10];
        Ys = new int[10];
        Color panelColor = new Color(42, 62, 80);
        moveCursorIcon = new ImageIcon(moveCursorPath);
        deleteIcon = new ImageIcon(deletePath);
        keyPanel = new KeyPanel(panelColor);
        // keyPanel.setBackground(Color.blue);  // DEBUG
        keyPanel.setPreferredSize(new Dimension(1000, 300));
        keyPanel.setBackground(panelColor);
        textPanel = new TextPanel();
        // textPanel.setBackground(Color.RED);   // DEBUG
        textPanel.setPreferredSize(new Dimension(1000, 300));
        textPanel.setBackground(panelColor);
        candidatePanel = new CandidatePanel();
        candidatePanel.setPreferredSize(new Dimension(1000, 100));
        candidatePanel.setBackground(panelColor);
        JPanel contentPanel = (JPanel)this.getContentPane();
        contentPanel.setSize(1000, 700);
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

    public void update(int num, float[] X, float[] Y, boolean[] push, int gesture){
        // System.out.println("Begin update");
        switch (gesture) {
            case DEFAULT:
                for (int i = 0; i < num; i++) {
                    // 用于float的坐标变换到电脑上的像素点坐标，目前是将400mm × 120mm大小虚拟键盘映射到小键盘上
                    // 然后将坐标中心从键盘中间平移到左上角，以正常显示
                    // TODO: 键盘可现实点的像素大小变为1000 × 400，键盘坐标应该如何改动？
                    Xs[i] = (int) (((X[i] + 135.0) / 270.0) * 1000);
                    Ys[i] = (int) (((Y[i] + 50.0) / 100.0) * 300);
                }
                glassPanel.update(num, Xs, Ys, push);
                break;
            case MOVECURSOR:
                glassPanel.moveGesture((int) (((X[0] + 135.0) / 270.0) * 1000), moveCursorIcon);
                break;
            case DELETE:
                glassPanel.deleteGesture((double)Y[0], deleteIcon);
                break;
            default:
                break;
        }
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
            // TODO: 解决多次backspace后候选单词比显示单词少一位的情况
            backSpace();
        }
    }

    public void releaseKey(String target){
        keyPanel.releaseKey(target.toUpperCase());
    }

    public void showCenter(int n){
        int[] centerX = new int[n];
        int[] centerY = new int[n];
        for(int i = 0; i < n; i++){
            centerX[i] = (int)KeyPanel.keyX[i];
            centerY[i] = (int)KeyPanel.keyY[i];
            System.out.printf("(%d, %d)\n", centerX[i], centerY[i]);
        }
        System.out.println(KeyPanel.keyX.length);
        System.out.println(KeyPanel.keyY.length);
        glassPanel.update(n, centerX, centerY, new boolean[n]);
        glassPanel.repaint();
    }

    public void moveCursor(boolean isLeft){
        String[] preWords = textPanel.moveCursor(isLeft);
        if(preWords != null) {
            String[] newwords = setInputWord(preWords);
            if (newwords != null) {
                candidatePanel.setWordlist(newwords);
            }
        }
    }

    private String[] setInputWord(String[] preWords){
        if(preWords.length >= 2){
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
            candidatePanel.setWordlist(null);
            return null;
        }
        Map<String, Double> prob_dict = new HashMap<>();
        return corrector.setList(prob_dict, this);
    }

    public void backSpace(){
        textPanel.backSpace();
        if(inputWord.length() > 0) {
            int length = inputWord.length() - 1;
            inputWord = inputWord.substring(0, length);
            if(inputWord.equals("")){
                candidatePanel.setWordlist(null);
                return;
            }
        }
        else{
            String text = textPanel.getText();
            String[] preWords = text.split(" +");
            if(preWords.length >= 2){
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
                candidatePanel.setWordlist(null);
                return;
            }
        }
        String lastKey = Character.toString(inputWord.charAt(inputWord.length() - 1));
        Map<String, Double> prob_dict = new HashMap<>();
        String[] newwords = corrector.setList(prob_dict, this);
        candidatePanel.setWordlist(newwords);               // 显示候选单词
    }

//    public boolean changeState(int state){
//        keyBoardState = state;
//        switch (keyBoardState){
//            case mainWindow.QWERTY
//        }
//    }
}
