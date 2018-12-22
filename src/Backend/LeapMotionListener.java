package Backend;
import Frontend.mainWindow;
import Frontend.KeyPanel;
import com.leapmotion.leap.*;

import java.util.HashMap;
import java.util.Map;
import java.lang.Math;

public class LeapMotionListener extends Listener {
    mainWindow mWindow;
    Corrector corrector;
    Hand mLastLeftHand;
    Hand mLastRightHand;
    Hand mCurrentLeftHand;
    Hand mCurrentRightHand;
    Map<Integer, Float> mFingerMap;
    Map<Integer, Boolean> mFingerStatus;
    Map<Integer, Vector> mFingerPos;
    private int descending = 0, rising = 0;
    private int MAX_DESCEND = 5, MAX_RISE = 4;
    Frame mLastFrame;
    long mLastFrameId;

    public LeapMotionListener(mainWindow mWindow) {
        this.mWindow = mWindow;
        mFingerMap = new HashMap<>();
        mFingerStatus = new HashMap<>();
        mFingerPos = new HashMap<>();
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

    private String[] calNearestNine(float pressX_in, float pressY_in) {   //返回距离最近的九个字母按键
        float pressX = (pressX_in + 150) * 1000 / 300, pressY = (pressY_in + 50) * 300 / 100;
        String[] nearestNine = {"","","","","","","","",""};
        float[] distance = new float[9];
        float tmp_distance;
        for (int i = 10; i < KeyPanel.keynum.length - 1; i++) { //不遍历数字和退格
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
        System.out.println("press x is " + pressX + ", press y is " + pressY);
        for (int i = 0; i < 3; i++) {
            System.out.print(nearestNine[i] + " distance is " + distance[i] + ", ");
        }
        System.out.println();
        return nearestNine;
    }

    @Override
    public void onFrame(Controller controller) {
        // Get the most recent frame and report some basic information
        Frame frame = controller.frame();
//        System.out.println("new frame");
//        int fingerNum = frame.hands().count() * 5;
        int fingerNum = frame.hands().count();
        float[] x = new float[fingerNum];
        float[] y = new float[fingerNum];
        boolean[] push = new boolean[fingerNum];
        int index = 0;

        for (Finger finger : frame.fingers()) {
//            System.out.println(finger.type() == Finger.Type.TYPE_INDEX);
            if (finger.type() == Finger.Type.TYPE_INDEX) {
                Bone bone = finger.bone(Bone.Type.TYPE_DISTAL);
                x[index] = bone.nextJoint().getX();
                y[index] = bone.nextJoint().getZ();
//            push[index] = false;
                if (mFingerMap.containsKey(finger.id())) {
                    float lastPos = mFingerMap.get(finger.id());
                    float presentPos = bone.nextJoint().getY();
                    float distance = lastPos - presentPos; // 高度方向的距离信息

                    Vector lastPos2 = mFingerPos.get(finger.id());
                    Vector presentPos2 = bone.nextJoint();
                    float distance2 = lastPos2.distanceTo(presentPos2);
                    if (mFingerStatus.get(finger.id())) { // 已经点击按下
                        push[index] = true;
                        if (presentPos2.getY() - lastPos2.getY() > 7) { // 更新为松开
                            if (rising < MAX_RISE) {
                                rising++;
                            } else {
                                push[index] = false;
                                mFingerStatus.put(finger.id(), false);
                                mFingerMap.put(finger.id(), bone.nextJoint().getY());
                                mFingerPos.put(finger.id(), bone.nextJoint());
                                rising = 0;
                            }
                        } else {
                            rising = 0;
                        }
                    } else { // 未点击按下
                        push[index] = false;
//                        if (distance2 > 10 && distance2 < 40 && distance > 0) { // 更新为按下
                        if (lastPos2.getY() - presentPos2.getY() > 8) { // 更新为按下
                            if (descending < MAX_DESCEND) {
                                descending++;
                            } else {
                                push[index] = true;
                                mFingerStatus.put(finger.id(), true);
                                mFingerMap.put(finger.id(), bone.nextJoint().getY()); // 此位置需要固定
                                mFingerPos.put(finger.id(), bone.nextJoint());
                                calNearestNine(x[index], y[index]);
                                descending = 0;
                            }
//                        System.out.println("click!");
//                        String[] pressed = new String[10];
//                        corrector.dealWith(pressed);
                        } else {
                            descending = 0;
                        }
                    }
                } else {
                    push[index] = false;
                    mFingerStatus.put(finger.id(), false);
                    mFingerMap.put(finger.id(), bone.nextJoint().getY());
                    mFingerPos.put(finger.id(), bone.nextJoint());
                }
                index++;
            }
        }

        mWindow.update(fingerNum, x, y, push, null);

        // 去除map中不合法id
        for (int key : mFingerMap.keySet()) {
            if (!frame.finger(key).isValid()) {
                mFingerMap.remove(key);
                mFingerStatus.remove(key);
                mFingerPos.remove(key);
            }
        }

        for (Hand hand : frame.hands()) {
            if (hand.palmVelocity().magnitude() > 150) { // 手移动速度过快，需要重新定位
//                System.out.println("reset!");
                for (Finger finger : frame.fingers()) {
                    Bone bone = finger.bone(Bone.Type.TYPE_DISTAL);
                    mFingerStatus.put(finger.id(), false); // 全部变成未按下
                    mFingerMap.put(finger.id(), bone.nextJoint().getY());
                    mFingerPos.put(finger.id(), bone.nextJoint());
                }
                break;
            }
        }

//        mFingerMap = new HashMap<>();
//        for (Finger finger : frame.fingers()) {
//            Bone bone = finger.bone(Bone.Type.TYPE_DISTAL);
//            float pos = bone.nextJoint().getY();
//            mFingerMap.put(finger.id(), pos);
//        }
//
//        mFingerStatus = new HashMap<>();



//        mLastFrame = controller.
//        mLastFrameId = controller.frame().id();
//        for (Hand hand : frame.hands()) {
//            String handType = hand.isLeft() ? "Left hand" : "Right hand";
//            System.out.println(handType);
//
////            // Get fingers
////            for (Finger finger : hand.fingers()) {
////                finger.type();
////                //Get Bones
////                Bone bone = finger.bone(Bone.Type.TYPE_DISTAL);
////                System.out.println(bone.nextJoint());
////                x[index] = bone.nextJoint().getX();
////                y[index] = bone.nextJoint().getZ();
////                index++;
////            }
//
//            if (hand.isLeft()) {
//                mCurrentLeftHand = hand;
//                System.out.println("save left hand");
//            }
//            if (hand.isRight()) {
//                mCurrentRightHand = hand;
//                System.out.println("save right hand");
//            }
//        }
//
//        // System.out.println("left count: " + mLastLeftHand.fingers().count());
////        System.out.println("right count: " + mLastRightHand.fingers().count());
//
//        for (int id : mFingerMap.keySet()) {
//            Finger fingerNew = frame.finger(id);
//            if (fingerNew.isValid()) {
//                System.out.println("valid");
//                Bone boneOld = fingerNew.bone(Bone.Type.TYPE_DISTAL);
//                x[index] = boneOld.nextJoint().getX();
//                y[index] = boneOld.nextJoint().getZ();
//                Bone boneNew = fingerNew.bone(Bone.Type.TYPE_DISTAL);
//                float distance = boneOld.nextJoint().getY() - boneNew.nextJoint().getY();
//                if (distance > 20 && distance < 50) {
//                    push[index] = true;
//                } else {
//                    push[index] = false;
//                }
//                index++;
//            }
//        }
//
//        for (Finger fingerOld : mLastLeftHand.fingers()) {
//            Finger fingerNew = mCurrentLeftHand.finger(fingerOld.id());
//            System.out.println("new id: " + fingerOld.id());
//            if (fingerNew.isValid()) {
//                System.out.println("valid");
//                Bone boneOld = fingerOld.bone(Bone.Type.TYPE_DISTAL);
//                x[index] = boneOld.nextJoint().getX();
//                y[index] = boneOld.nextJoint().getZ();
//                Bone boneNew = fingerNew.bone(Bone.Type.TYPE_DISTAL);
//                float distance = boneOld.nextJoint().getY() - boneNew.nextJoint().getY();
//                if (distance > 20 && distance < 50) {
//                    push[index] = true;
//                } else {
//                    push[index] = false;
//                }
//                index++;
//            }
//        }
//        for (Finger fingerOld : mLastRightHand.fingers()) {
//            Finger fingerNew = mCurrentRightHand.finger(fingerOld.id());
//            System.out.println("new id: " + fingerOld.id());
//            if (fingerNew.isValid()) {
//                System.out.println("valid");
//                Bone boneOld = fingerOld.bone(Bone.Type.TYPE_DISTAL);
//                x[index] = boneOld.nextJoint().getX();
//                y[index] = boneOld.nextJoint().getZ();
//                Bone boneNew = fingerNew.bone(Bone.Type.TYPE_DISTAL);
//                float distance = boneOld.nextJoint().getY() - boneNew.nextJoint().getY();
//                if (distance > 20 && distance < 50) {
//                    push[index] = true;
//                } else {
//                    push[index] = false;
//                }
//                index++;
//            }
//        }


//
//        mFingerMap = new HashMap<>();

//        System.out.println("Frame id: " + frame.id()
//                + ", timestamp: " + frame.timestamp()
//                + ", hands: " + frame.hands().count()
//                + ", fingers: " + frame.fingers().count());
//
//        //Get hands
//        for(Hand hand : frame.hands()) {
//            String handType = hand.isLeft() ? "Left hand" : "Right hand";
//            System.out.println("  " + handType + ", id: " + hand.id()
//                    + ", palm position: " + hand.palmPosition());
//
//            // Get the hand's normal vector and direction
//            Vector normal = hand.palmNormal();
//            Vector direction = hand.direction();
//
//            // Calculate the hand's pitch, roll, and yaw angles
//            System.out.println("  pitch: " + Math.toDegrees(direction.pitch()) + " degrees, "
//                    + "roll: " + Math.toDegrees(normal.roll()) + " degrees, "
//                    + "yaw: " + Math.toDegrees(direction.yaw()) + " degrees");
//
//            // Get arm bone
//            Arm arm = hand.arm();
//            System.out.println("  Arm direction: " + arm.direction()
//                    + ", wrist position: " + arm.wristPosition()
//                    + ", elbow position: " + arm.elbowPosition());
//
//            // Get fingers
//            for (Finger finger : hand.fingers()) {
//                System.out.println("    " + finger.type() + ", id: " + finger.id()
//                        + ", length: " + finger.length()
//                        + "mm, width: " + finger.width() + "mm");
//
//                //Get Bones
//                for(Bone.Type boneType : Bone.Type.values()) {
//                    Bone bone = finger.bone(boneType);
//                    System.out.println("      " + bone.type()
//                            + " bone, start: " + bone.prevJoint()
//                            + ", end: " + bone.nextJoint()
//                            + ", direction: " + bone.direction());
//                }
//            }
//        }
//
//        if (!frame.hands().isEmpty()) {
//            System.out.println();
//        }
    }
}
