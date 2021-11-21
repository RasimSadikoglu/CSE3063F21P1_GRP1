package Student;

import java.util.ArrayList;

import Course.Course;
import Course.Course.CourseGroup;

public class Student {
    private String name;
    private String id;
    private float gpa;
    private float points;
    private float completedCredits;
    private float totalCredits;
    private boolean isGraduated;
    private Transcript transcript;
    private transient int currentSemester;

    public Student() {
        name = "";
        this.id = "";
        this.transcript = new Transcript();
        gpa = 0;
        isGraduated = false;
        currentSemester = 1;
    }

    public Student(String id) {
        this();
        this.id = id;
    }

    public Student(String name, String id) {
        this(id);
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public Transcript getTranscript() {
        return transcript;
    }
    
    public float getCourseNote(String courseName) {
        return transcript.getCourseNote(courseName);
    }

    public int getCurrentSemester() {
        return currentSemester;
    }

    public void addSemester(Semester semester) {
        transcript.addSemester(semester);
        currentSemester++;
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

    public boolean getIsGraduate() {
        return isGraduated;
    }

    public boolean setIsGradute() {

        if (gpa < 2) return false;

        if (completedCredits < 240) return false;

        return isGraduated = true;
    }

    public void updateCurrentSemester() {
        currentSemester = transcript.getSemesters().size() + 1;
    }

    public int getCourseCount(CourseGroup courseGroup) {
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