package Util;

import java.util.ArrayList;
import Student.*;


public class RandomObjectGenerator {
    private int numOfStudents;

    private double shift;
    private double sharpness;

    ArrayList<Double> bellShape;

    public RandomObjectGenerator(int numOfStudents) {
        this.numOfStudents = numOfStudents;
        shift = 0.0;
        sharpness = 2.5;
    }

    public void setBell(double shift, double sharpness){
        this.shift = shift;
        this.sharpness = sharpness;
    }

    // entrance year example 2018=18, 2000=00, 2023=23
    // input should be the shortened version of the year
    public ArrayList<Student> getRandomStudents(int entranceYear){
        // Student array to be returned
        ArrayList<Student> studentList = new ArrayList<Student>();
        
        ArrayList<String> studentIds = generateStudentIds(entranceYear);

        for (int i = 0; i < numOfStudents; i++){
            Student student = new Student();
            student.setId(studentIds.get(i));
            studentList.add(student);
        }

        return studentList;
    }

    // get a random double. '[randMin, randMax)'
    public double getLinearRandom(double randMin, double randMax){
        double randomValue = Math.random();
        double minMaxDiff = randMax - randMin;

        randomValue *= minMaxDiff;
        randomValue += randMin;

        return randomValue;
    }

    // get a random integer. '[randMin, randMax)'
    public int getLinearRandom(int randMin, int randMax){
        return (int)getLinearRandom((double)randMin, (double)randMax);
    }

    public double getBellRandom(double randMin, double randMax){
        double minMaxDiff = randMax - randMin;
        
        double maxVal = gaussianFunction(shift, sharpness, shift);

        double randNum = Math.random() * 2.0 - 1.0;
        double bellRandom = gaussianFunction(randNum, sharpness, shift) / maxVal;

        bellRandom *= minMaxDiff;
        bellRandom += randMin;

        return bellRandom;
    }

    public int getBellRandom(int randMin, int randMax){
        return (int)getBellRandom((double)randMin, (double)randMax);
    }

    // gets the value from a bell shaped graph
    // sharpness and shift defines the graph
    // x is a position inside the graph
    private double gaussianFunction(double x, double sharpness, double shift){
        return (1 / (sharpness * Math.sqrt(2 * Math.PI))) * Math.exp(-Math.pow(x - shift, 2) / 2 * Math.pow(sharpness, 2));
    }

    // entrance year example 2018=18, 2000=00, 2023=23
    private ArrayList<String> generateStudentIds(int entranceYear){
        ArrayList<String> studentIds = new ArrayList<String>();

        // creates id array with increasing ids
        StringBuilder s = new StringBuilder("1501");
        s.append(String.valueOf(entranceYear));
        for (int i = 1; i <= numOfStudents; i++){
            s.append(String.format("%03d", i));
            studentIds.add(s.toString());
            s.delete(6, 9);
        }

        // shuffles id array
        String temp;
        for (int i = 0; i < numOfStudents; i++){
            int replaceIndex = (int)(Math.random() * numOfStudents);
            temp = studentIds.get(i);
            studentIds.set(i, studentIds.get(replaceIndex));
            studentIds.set(replaceIndex, temp);
        }

        return studentIds;
    }
}
