package Util;

import java.util.ArrayList;
import java.util.Random;
import Student.*;


public class RandomObjectGenerator {
    private final int bellDataSize = 200;
    private final int numOfStudents = 70;

    public ArrayList<Double> bellShape;
    public ArrayList<String> studentIds;

    public RandomObjectGenerator() {
        this.bellShape = new ArrayList<Double>();
        this.studentIds = new ArrayList<String>();

        generateBellShapeAt(6.0f, 0.65f);
        generateStudentIds(18);
        generateStudentIds(19);
    }


    // Generates Course, Semester, Transcript and Student classes
    public Student generateRandomStudent(){
        return null;
    }


    // entrance year example 2018=18, 2000=00, 2023=23
    private void generateStudentIds(int entranceYear){
        StringBuilder s = new StringBuilder("1501");
        s.append(String.valueOf(entranceYear));
        for (int i = 1; i <= numOfStudents; i++){
            int requiredZeros = 2 - (int)Math.log10(i);
            for (int j = 0; j < requiredZeros; j++){
                s.append(0);
            }
            s.append(i);

            studentIds.add(s.toString());
            s.delete(s.length() - 3, s.length());
        }

        String temp;
        for (int i = 0; i < numOfStudents; i++){
            int replaceIndex = (int)(Math.random() * numOfStudents);
            temp = studentIds.get(i);
            studentIds.set(i, studentIds.get(replaceIndex));
            studentIds.set(replaceIndex, temp);
        }
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
        // odd
        else{
            // makes sure middle input is always 0.5
            return bellDataSize + 1;
        }
    }
}
