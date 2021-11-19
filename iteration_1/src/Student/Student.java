package Student;

import java.util.ArrayList;

import Course.Course;

public class Student {
    private String id;
    private float gpa; // For json files
    private boolean isGraduate;
    private Transcript transcript;
    private transient int currentSemester;

    public Student() { // Default constructor in order to avade null exceptions
        this.id = "";
        this.transcript = new Transcript();
        gpa = 0;
        isGraduate = false;
        currentSemester = 1;
    }

    public Student(String id) {
        this.id = id;
        transcript = new Transcript();
        currentSemester = 1;
    }

    public String getId() {
        return id;
    }

    public Transcript getTranscript() {
        return transcript;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setTranscript(Transcript transcript) {
        this.transcript = transcript;
    }

    public void addCourse(Course newCourse) {
        transcript.addCourse(newCourse);
    }
    
    public float getCourseNote(String courseName) {
        return transcript.getCourseNote(courseName);
    }

    public int getCurrentSemester() {
        return currentSemester;
    }

    public void addSemester(Semester semester) {
        transcript.addSemester(semester);
        increaseSemesterCount();
    }

    public void updateGPA() {
        gpa = transcript.getGPA()[0];
    }

    public ArrayList<Course> getfailedCourses(boolean getAll) {
        return transcript.getfailedCourses(getAll);
    }

    public ArrayList<Course> getConditionalCourses() {
        return transcript.getConditionalCourses();
    }

    public void increaseSemesterCount() {
        currentSemester++;
    }

    public boolean getIsGraduate() {
        return isGraduate;
    }

    public boolean setIsGradute() {

        if (currentSemester < 9) return false;

        if (transcript.getGPA()[0] < 2) return false;

        if (!getfailedCourses(true).isEmpty()) return false;

        isGraduate = true;

        return true;
    }

    public void updateCurrentSemester() {
        currentSemester = transcript.getSemesters().size() + 1;
    }

    public float getGPA() {
        return gpa;
    }
}