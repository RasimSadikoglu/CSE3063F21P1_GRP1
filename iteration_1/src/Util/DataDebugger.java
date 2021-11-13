package Util;

import java.util.ArrayList;

public class DataDebugger {
    private RandomObjectGenerator generator;

    // Debug prints the RandomObjectGenerator's data
    public DataDebugger(RandomObjectGenerator generator){
        this.generator = generator;
    }

    public void debugBellGraph(){
        float scaleX = 1.0f;
        float scaleY = 0.1f;
        grapher(scaleX, scaleY, generator.bellShape);
    }

    private void grapher(float scaleX, float scaleY, ArrayList<?> arr){
        for (int i = 0; i < arr.size() * scaleY; i++){
            float yPos = 1 - ((float) i / (arr.size() * scaleY));
            System.out.println();
            for (int j = 0; j < arr.size() * scaleX; j++) {
                int arrPos = (int) ((float) j / scaleX);
                Double value = (Double)arr.get(arrPos);
                if (value >= yPos) {
                    System.out.print("*");
                } else {
                    System.out.print("-");
                }
            }
        }
    }
}
