package People;

import java.util.ArrayList;

import Courses.Course;

public class Teacher extends Person{
    private ArrayList<Course> givenCourses;
    private ArrayList<Student> assignedStudents;
    private int[] officeHours; // [startHour, startMinute, endHour, endMinute]
    private String officeRoom;

    public Teacher(String name, String mail, String phoneNumber, String gender, int age, String department, String officeRoom) {
        super(name, mail, phoneNumber, gender, age, department);
        this.officeRoom = officeRoom;
    }
}
