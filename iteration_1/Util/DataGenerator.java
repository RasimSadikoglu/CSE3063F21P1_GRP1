package Util;

import java.util.ArrayList;

public class DataGenerator {
    private int bellDataSize;

    public ArrayList<Double> bellShape;

    public DataGenerator() {
        bellDataSize = 200;
        this.bellShape = new ArrayList<Double>();
        generateBellShapeAt(6.0f, 0.65f);
    }

    // x: between 0 and 1
    // defines the location of the highest point inside bell graph
    // shift value 0.5 means middle
    private void generateBellShapeAt(float sharpness, float shift){
        double maxVal = gaussianFunction(shift, sharpness, shift);
        double multiplier = 1 / maxVal;
        for (float i = 0; i < getBellDataSize(); i++){
            bellShape.add(gaussianFunction(i / getBellDataSize(), sharpness, shift) * multiplier);
        }
    }

    // gets the value from a bell shaped graph
    // sharpness and shift defines the graph
    // x is a position inside the graph
    private double gaussianFunction(float x, float sharpness, float shift){
        return (1 / (sharpness * Math.sqrt(2 * Math.PI))) * Math.exp(-Math.pow(x - shift, 2) / 2 * Math.pow(sharpness, 2));
    }


    public int getBellDataSize() {
        // even
        if (bellDataSize % 2 == 1){
            return bellDataSize;
        }
        //odd
        else{
            // makes sure middle input is always 0.5
            return bellDataSize + 1;
        }
    }
}
