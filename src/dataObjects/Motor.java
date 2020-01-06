package dataObjects;

import java.util.ArrayList;

public class Motor {
    public static final char CHARACTER_ID = 'M';

    private final int CAN_ID;
    private ArrayList<Float> percentVBusList, currentDrawList, encoderPositionList, encoderVelocityList;

    public Motor(int CAN_ID) {
        this.CAN_ID = CAN_ID;
        this.percentVBusList = new ArrayList<Float>();
        this.currentDrawList = new ArrayList<Float>();
        this.encoderPositionList = new ArrayList<Float>();
        this.encoderVelocityList = new ArrayList<Float>();
    }

    public void addData(float percentVBus, float currentDraw, float encoderPosition, float encoderVelocity) {
        percentVBusList.add(percentVBus);
        currentDrawList.add(currentDraw);
        encoderPositionList.add(encoderPosition);
        encoderVelocityList.add(encoderVelocity);
    }


    //Getters
    public int getCAN_ID() {
        return CAN_ID;
    }

    public ArrayList<Float> getPercentVBusList() {
        return percentVBusList;
    }

    public ArrayList<Float> getCurrentDrawList() {
        return currentDrawList;
    }

    public ArrayList<Float> getEncoderPositionList() {
        return encoderPositionList;
    }

    public ArrayList<Float> getEncoderVelocityList() {
        return encoderVelocityList;
    }

    public boolean isActive() {
        return !(percentVBusList.isEmpty() && currentDrawList.isEmpty() && encoderPositionList.isEmpty() && encoderVelocityList.isEmpty());
    }
}
