package Student;

import Course.Course;

public class Student {
    private String id;
    private Transcript transcript;
    private int currentSemester;

    public Student() { // Default constructor in order to avade null exceptions
        this.id = "";
        this.transcript = new Transcript();
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
}