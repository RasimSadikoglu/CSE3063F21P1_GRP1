package People;

import java.util.ArrayList;
import java.util.Map;

import Courses.Course;

public class Student extends Person {
    private int semester;
    private String id;
    private ArrayList<Course> currentCourses;
    private Map<Course, Float> passNotes;
    private Teacher consultant;

    public Student(String name, String mail, String phoneNumber, String gender, int age,  String department, int semester, String id) {
        super(name, mail, phoneNumber, gender, age, department);
        this.semester = semester;
        this.id = id;
    }

    public int getSemester() {
        return semester;
    }

    public void setSemester(int semester) {
        this.semester = semester;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Teacher getConsultant() {
        return consultant;
    }

    public void setConsultant(Teacher consultant) {
        this.consultant = consultant;
    }



}
