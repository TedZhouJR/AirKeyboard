package Backend;
import Frontend.mainWindow;
import com.leapmotion.leap.*;

public class LeapMotionListener extends Listener {
    mainWindow mWindow;

    public LeapMotionListener(mainWindow mWindow) {
        this.mWindow = mWindow;
    }


    @Override
    public void onInit(Controller controller) {
        System.out.println("Initialized");
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

    @Override
    public void onFrame(Controller controller) {
        // Get the most recent frame and report some basic information
        Frame frame = controller.frame();
        System.out.println("new frame");
        int fingerNum = frame.hands().count() * 5;
        float[] x = new float[fingerNum];
        float[] y = new float[fingerNum];
        int index = 0;
        for (Hand hand : frame.hands()) {
            String handType = hand.isLeft() ? "Left hand" : "Right hand";
            System.out.println(handType);
            // Get fingers
            for (Finger finger : hand.fingers()) {
                //Get Bones
                Bone bone = finger.bone(Bone.Type.TYPE_DISTAL);
                System.out.println(bone.nextJoint());
                x[index] = bone.nextJoint().getX();
                y[index] = bone.nextJoint().getZ();
                index++;
            }
        }
        mWindow.update(fingerNum, x, y, null);

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
