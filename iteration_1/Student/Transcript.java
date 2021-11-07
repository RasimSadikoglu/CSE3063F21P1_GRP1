package Student;

import java.util.ArrayList;

public class Transcript {
    
    private ArrayList<Semester> semesters;

    public Transcript() {
        semesters = new ArrayList<Semester>();
    }

    public Transcript(ArrayList<Semester> semesters) {
        this.semesters = semesters;
    }

    public void addSemester(Semester s) {
        semesters.add(s);
    }

    public Semester getCurrentSemester() {
        if (semesters.size() == 0) {
            throw new IndexOutOfBoundsException("Failed to obtain current semester information! (Semester array is empty!)");
        }

        return semesters.get(semesters.size() - 1);
    }

    public float getGPA() {
        return getCompletedCredits() / getTotalCourseCredits();
    }

    public float getTotalCourseCredits() { // Waiting for semester class to be completed
        return 1;
    }

    public float getCompletedCredits() { // Waiting for semester class to be completed
        return 1;
    }

    public ArrayList<Semester> getSemesters() {
        return semesters;
    }
}
