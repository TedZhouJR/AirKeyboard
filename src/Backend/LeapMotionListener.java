package Backend;
import Frontend.mainWindow;
import Frontend.KeyPanel;
import com.leapmotion.leap.*;
import com.leapmotion.leap.Frame;

import java.util.HashMap;
import java.util.Map;

public class LeapMotionListener extends Listener {
    private static int MAX_FINGER_ID = 999;
    mainWindow mWindow;
    Corrector corrector;
    Map<Integer, Float> mFingerPosY;
    Map<Integer, Boolean> mFingerStatus;
    Map<Integer, Integer> mFingerRising, mFingerDescending;
    Map<Integer, Vector> mFingerPos;
    private static final double DESCEND_PERCENTAGE = 0.88, RISE_PERCENTAGE = 0.8, RISE_TIME = 3;
    private static final double DESCEND_DISTANCE = 2.5, RISE_DISTANCE = 0.0, DESCEND_TIME = 3;
    private int[] latestFingerIDList = new int[MAX_FINGER_ID];
    private int latestFingerInList = -1;
    private String recentClick = "";
    private String[] numberList = {"1", "2", "3", "4", "5", "6", "7", "8", "9", "0"};
    Frame mLastFrame;
    long mLastFrameId;

    public LeapMotionListener(mainWindow mWindow, Corrector correctorIn) {
        this.mWindow = mWindow;
        mFingerPosY = new HashMap<>();
        mFingerStatus = new HashMap<>();
        mFingerPos = new HashMap<>();
        mFingerRising = new HashMap<>();
        mFingerDescending = new HashMap<>();
        corrector = correctorIn;
    }


    @Override
    public void onInit(Controller controller) {
        System.out.println("Initialized");;
        // use head mounted display
//        controller.setPolicy(Controller.PolicyFlag.POLICY_OPTIMIZE_HMD);
    }

    @Override
    public void onConnect(Controller controller) {
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
        for (int i = 0; i < KeyPanel.keynum.length; i++) { //不遍历数字和退格
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
            if (nearestNine[0].equals(number)) {
                mWindow.pushKey(nearestNine[0], null);
                return;
            }
        }
        if (nearestNine[0].equals("Backspace")) {
            mWindow.pushKey(nearestNine[0], null);
            return;
        }
        double tot_distance = 0.0;
        for (int i = 0; i < 9; i++) {
            tot_distance += 1 / distance[i];
        }
        for (int i = 0; i < 9; i++) {
            prob_dict.put(nearestNine[i], 1 / (tot_distance * distance[i]));
        }
        corrector.dealWith(prob_dict, mWindow);
    }

    @Override
    public void onFrame(Controller controller) {
        // Get the most recent frame and report some basic information
        Frame frame = controller.frame();
        int fingerNum = frame.hands().count();
        float[] KeyboardX = new float[fingerNum];
        float[] KeyboardY = new float[fingerNum];
        boolean[] push = new boolean[fingerNum];
        int index = 0;

        for (Finger finger : frame.fingers()) {
            if (finger.type() == Finger.Type.TYPE_INDEX) {
                Bone bone = finger.bone(Bone.Type.TYPE_DISTAL);
                KeyboardX[index] = bone.nextJoint().getX();
                KeyboardY[index] = bone.nextJoint().getZ();
                if (mFingerPosY.containsKey(finger.id())) {

                    float lastPos = mFingerPosY.get(finger.id());
                    float presentPos = bone.nextJoint().getY();
                    float distanceY = Math.abs(lastPos - presentPos);

                    Vector lastPos2 = mFingerPos.get(finger.id());
                    Vector presentPos2 = bone.nextJoint();
                    float distance2 = lastPos2.distanceTo(presentPos2);

                    if (Math.abs(lastPos - presentPos) > 1 && (distanceY / distance2) > 0.9)
//                    System.out.println("distance:" + (lastPos - presentPos) +
//                            ", percentage: " + (distanceY / distance2));
                    if (mFingerStatus.get(finger.id())) { // 已经点击按下
                        push[index] = true;
                        int risingTime = mFingerRising.get(finger.id());
                        if (presentPos - lastPos > RISE_DISTANCE && (distanceY / distance2) > RISE_PERCENTAGE) { // 更新为按下
                            if (risingTime >= RISE_TIME) {
                                push[index] = false;
                                mFingerStatus.put(finger.id(), false);
                                mWindow.releaseKey(recentClick);
                                mFingerRising.put(finger.id(), 0);
                            } else {
                                mFingerRising.put(finger.id(), risingTime + 1);
                            }
                        }
                    } else { // 未点击按下
                        push[index] = false;
                        int descendingTime = mFingerDescending.get(finger.id());
                        if (lastPos - presentPos > DESCEND_DISTANCE && (distanceY / distance2) > DESCEND_PERCENTAGE) { // 更新为按下
                            if (descendingTime >= DESCEND_TIME) {
                                mWindow.releaseKey(recentClick);
                                push[index] = true;
                                mFingerStatus.put(finger.id(), true);
                                mFingerDescending.put(finger.id(), 0);
                                calNearestNine(KeyboardX[index], KeyboardY[index]);
                                mFingerDescending.put(finger.id(), 0);
                            } else {
                                mFingerDescending.put(finger.id(), descendingTime + 1);
                            }
                        }
                    }
                    mFingerPos.put(finger.id(), bone.nextJoint());
                    mFingerPosY.put(finger.id(), bone.nextJoint().getY());
                } else {
                        push[index] = false;
                        mFingerStatus.put(finger.id(), false);
                        mFingerPosY.put(finger.id(), bone.nextJoint().getY());
                        mFingerPos.put(finger.id(), bone.nextJoint());
                        mFingerRising.put(finger.id(), 0);
                        mFingerDescending.put(finger.id(), 0);
                    }
                index++;
            }
        }

        mWindow.update(fingerNum, KeyboardX, KeyboardY, push, null);

        // 去除map中不合法id
        for (int key : mFingerPosY.keySet()) {
            if (!frame.finger(key).isValid()) {
                mFingerPosY.remove(key);
                mFingerStatus.remove(key);
                mFingerPos.remove(key);
            }
        }

        for (Hand hand : frame.hands()) {
            if (hand.palmVelocity().magnitude() > 150) { // 手移动速度过快，需要重新定位
                for (Finger finger : frame.fingers()) {
                    Bone bone = finger.bone(Bone.Type.TYPE_DISTAL);
                    mFingerStatus.put(finger.id(), false); // 全部变成未按下
                    mFingerPosY.put(finger.id(), bone.nextJoint().getY());
                    mFingerPos.put(finger.id(), bone.nextJoint());
                }
                break;
            }
        }
    }
}
