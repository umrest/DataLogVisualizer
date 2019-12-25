package dataObjects;

public class Motor {
    public static final char CHARACTER_ID = 'M';

    private int CAN_ID;
    private float percentVBus, currentDraw, encoderPosition, encoderVelocity;

    public Motor(int CAN_ID, float percentVBus, float currentDraw, float encoderPosition, float encoderVelocity){
        this.CAN_ID = CAN_ID;
        this.percentVBus = percentVBus;
        this.currentDraw = currentDraw;
        this.encoderPosition = encoderPosition;
        this.encoderVelocity = encoderVelocity;
    }


    //Getters
    public int getCAN_ID() {
        return CAN_ID;
    }

    public float getPercentVBus() {
        return percentVBus;
    }

    public float getCurrentDraw() {
        return currentDraw;
    }

    public float getEncoderPosition() {
        return encoderPosition;
    }

    public float getEncoderVelocity() {
        return encoderVelocity;
    }
}
