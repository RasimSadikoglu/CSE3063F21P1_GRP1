package Student;

import java.util.ArrayList;

import Course.Course;

public class Student {
    private String id;
    private float gpa;
    private float points;
    private float completedCredits;
    private float totalCredits;
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
        float[] gpaData = transcript.getGPA();
        gpa = gpaData[0];
        points = gpaData[1];
        completedCredits = gpaData[2];
        totalCredits = gpaData[3];
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

        if (gpa < 2) return false;

        if (completedCredits < 240) return false;

        return isGraduate = true;
    }

    public void updateCurrentSemester() {
        currentSemester = transcript.getSemesters().size() + 1;
    }

    public int getCourseCount(String courseGroup) {
        return transcript.getCourseCount(courseGroup);
    }

    /**
     * 
     * @return [gpa, points, completedCredits, totalCredits]
     */
    public float[] getGPA() {
        return new float[] {gpa, points, completedCredits, totalCredits};
    }
}