package Backend;
import Frontend.mainWindow;
import Frontend.KeyPanel;
import com.leapmotion.leap.*;
import com.leapmotion.leap.Frame;

import java.awt.*;
import java.util.Dictionary;
import java.util.HashMap;
import java.util.Map;
import java.lang.Math;

public class LeapMotionListener extends Listener {
    private static int MAX_FINGER_ID = 999;
    private static int ANGLE = 40;
    mainWindow mWindow;
    Corrector corrector;
    Hand mLastLeftHand;
    Hand mLastRightHand;
    Hand mCurrentLeftHand;
    Hand mCurrentRightHand;
//    Map<Integer, Float> mFingerMap;
    Map<Integer, Boolean> mFingerStatus;
//    Map<Integer, Vector> mFingerPos;
    private int[] descendingList = new int[MAX_FINGER_ID], risingList = new int[MAX_FINGER_ID];
//    private int descending = 0, rising = 0;
    private static final int MAX_DESCEND = 6, MAX_RISE = 4;
    private static final int DESCEND_DISTANCE = 8, RISE_DISTANCE = 7;
    private static final double VERTICAL_MOVING_RATE = 0.4;
    private int[] latestFingerIDList = new int[MAX_FINGER_ID];
    private int latestFingerInList = -1;
    private String recentClick = "";
    private String[] numberList = {"1", "2", "3", "4", "5", "6", "7", "8", "9", "0"};
    Frame mLastFrame;
    long mLastFrameId;

    public LeapMotionListener(mainWindow mWindow, Corrector correctorIn) {
        this.mWindow = mWindow;
//        mFingerMap = new HashMap<>();
        mFingerStatus = new HashMap<>();
//        mFingerPos = new HashMap<>();
        corrector = correctorIn;
    }


    @Override
    public void onInit(Controller controller) {
        System.out.println("Initialized");;
//        controller.enableGesture(Gesture.Type.TYPE_KEY_TAP);
        // use head mounted display
//        controller.setPolicy(Controller.PolicyFlag.POLICY_OPTIMIZE_HMD);
    }

    @Override
    public void onConnect(Controller controller) {
        controller.enableGesture(Gesture.Type.TYPE_KEY_TAP);
        System.out.println("Connected");
    }

    @Override
    public void onDisconnect(Controller controller) {
        //Note: not dispatched when running in a debugger.
        System.out.println("Disconnected");
    }

    @Override
    public void onExit(Controller controller) {
        System.out.println("Exited");
    }

    private void calNearestNine(float pressX_in, float pressY_in) {   //返回距离最近的九个字母按键
        float pressX = (pressX_in + 135) * 1000 / 270, pressY = (pressY_in + 50) * 300 / 100;
        String[] nearestNine = {"","","","","","","","",""};
        Map<String, Double> prob_dict = new HashMap<>();
        float[] distance = new float[9];
        float tmp_distance;
        for (int i = 0; i < KeyPanel.keynum.length; i++) {
            tmp_distance = (KeyPanel.keyX[i] - pressX) * (KeyPanel.keyX[i] - pressX) + (KeyPanel.keyY[i] - pressY) * (KeyPanel.keyY[i] - pressY);
            for (int j = 0; j < 9; j++) {
                if (tmp_distance < distance[j] || distance[j] == 0) {   //如果找到一个新的近的
                    for (int k = 8; k > j; k--) {   //安排好后面的数据
                        nearestNine[k] = nearestNine[k - 1];
                        distance[k] = distance[k - 1];
                    }
                    //再插进新的
                    nearestNine[j] = KeyPanel.keynum[i];
                    distance[j] = tmp_distance;
                    break;
                }
            }
        }
        recentClick = nearestNine[0];
        System.out.println(recentClick);
        for (String number:numberList) {
            if (recentClick.equals(number)) {
                mWindow.pushKey(recentClick, null);
                return;
            }
        }
        if (recentClick.equals("Backspace")) {
            mWindow.pushKey(recentClick, null);
            return;
        }
        double tot_distance = 0.0;
        for (int i = 0; i < 9; i++) {
            tot_distance += 1 / distance[i];
        }
        for (int i = 0; i < 9; i++) {
            prob_dict.put(nearestNine[i], 1 / (tot_distance * distance[i]));
        }
        for (int i = 0; i < 9; i++) {
            System.out.print(prob_dict);
        }
        System.out.println();
//        mWindow.pushKey(recentClick, null);
        corrector.dealWith(prob_dict, mWindow);
    }

    @Override
    public void onFrame(Controller controller) {
        Frame frame = controller.frame();
//        int fingerNum = frame.hands().count() * 5;
        int fingerNum = frame.hands().count();
        float[] x = new float[fingerNum];
        float[] y = new float[fingerNum];
        boolean[] push = new boolean[fingerNum];
        int index = 0;

        // 去除map中不合法id
        for (int key : mFingerStatus.keySet()) {
            if (!frame.finger(key).isValid()) {
                mFingerStatus.remove(key);
//                mFingerMap.remove(key);
//                mFingerPos.remove(key);
            }
        }

        // 重新修改
        for (Finger finger : frame.fingers()) {
//            int fingerID = 0;
            if (finger.type() == Finger.Type.TYPE_INDEX) { // 食指
                Bone bone0 = finger.bone(Bone.Type.TYPE_PROXIMAL);  // 手指指根处骨头
                Vector direct = bone0.direction();
                Bone bone1 = finger.bone(Bone.Type.TYPE_DISTAL);  // 手指末端处骨头
                x[index] = bone1.nextJoint().getX();
                y[index] = bone1.nextJoint().getZ();
                float angle = direct.angleTo(finger.hand().direction());
//                System.out.println("angle: " + angle);
                // 如果角度大于阈值，且只判断按下的一瞬间，则判定为按下
                if (angle < degree2Rad(180 - ANGLE)) {

                    System.out.println("set: "+mFingerStatus);
//                    System.out.println("DEBUG1");
//                    releaseAllKeys();
                    push[index] = true;
                    if (!mFingerStatus.containsKey(finger.id()) || !mFingerStatus.get(finger.id())) {
                        System.out.println(mFingerStatus.containsKey(finger.id()));
                        System.out.println(mFingerStatus.get(finger.id()));
                        mWindow.releaseKey(recentClick);
                        calNearestNine(x[index], y[index]);
                    }
//                    mFingerStatus.replace(finger.id(), true);
//                    mFingerStatus.remove(finger.id());
                    mFingerStatus.put(finger.id(), true);
                } else {
//                    System.out.println("DEBUG2");
                    mWindow.releaseKey(recentClick);
                    push[index] = false;
                    mFingerStatus.put(finger.id(), false);
                }
                index++;
            }
        }
        mWindow.update(fingerNum, x, y, push, null);

//        // 去除map中不合法id
//        for (int key : mFingerStatus.keySet()) {
//            if (!frame.finger(key).isValid()) {
//                mFingerStatus.remove(key);
////                mFingerMap.remove(key);
////                mFingerPos.remove(key);
//            }
//        }

//        for (Hand hand : frame.hands()) {
//            if (hand.palmVelocity().magnitude() > 150) { // 手移动速度过快，需要重新定位
//                for (Finger finger : frame.fingers()) {
//                    Bone bone = finger.bone(Bone.Type.TYPE_DISTAL);
//                    mFingerStatus.put(finger.id(), false); // 全部变成未按下
//                    mFingerMap.put(finger.id(), bone.nextJoint().getY());
//                    mFingerPos.put(finger.id(), bone.nextJoint());
//                }
//                break;
//            }
//        }

    }

    private void releaseAllKeys() {
        for (String key : KeyPanel.keynum) {
            mWindow.releaseKey(key);
        }
    }

    private float degree2Rad(float degree) {
        return (float)(degree / 180 * Math.PI);
    }
}
