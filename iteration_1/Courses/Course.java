package Courses;

import People.Student;
import People.Teacher;

import java.util.ArrayList;

public class Course {
    private String courseName;
    private String courseGroup;
    private ArrayList<Course> prerequisites;
    private float credit;
    private ArrayList<Teacher> lecturers;
    private ArrayList<Student> students;

    public Course(String courseName, String courseGroup, float credit) {
        this.courseName = courseName;
        this.courseGroup = courseGroup;
        this.credit = credit;
    }

    public String getCourseName() {
        return courseName;
    }

    public String getCourseGroup() {
        return courseGroup;
    }

    public float getCredit() {
        return credit;
    }
}
