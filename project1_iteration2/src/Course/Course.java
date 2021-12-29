package Course;

import java.util.ArrayList;

import Student.Student;

public class Course implements Comparable<Course> {

    public enum CourseGroup {
        SME1(11), SME2(10), SME3(9), SME4(8),
        SME5(7), SME6(6), SME7(5), SME8(4),
        TE(3), FTE(2), NTE(1);

        private int priority;
        private CourseGroup(int p) { priority = p; }
        public int getPriority() { return priority; }
    }

    private String courseName;
    private CourseGroup courseGroup;
    private float requiredCredits;
    private String prerequisiteCourse;
    private float courseCredits;
    private int courseQuota;
    private ArrayList<Student> students;
    private Schedule courseSchedule;

    public Course() {
        courseName = "";
        courseGroup = null;
        requiredCredits = 0;
        prerequisiteCourse = "";
        courseCredits = 0;
        courseQuota = 0;
        students = new ArrayList<Student>();
        courseSchedule = new Schedule();
    }

    public String getCourseName() {
        return courseName;
    }

    public CourseGroup getCourseGroup() {
        return courseGroup;
    }

    public float getRequiredCredits() {
        return requiredCredits;
    }

    public String getPrerequisiteCourse() {
        return prerequisiteCourse;
    }

    public float getCourseCredits() {
        return courseCredits;
    }

    public int getCourseQuota() {
        return courseQuota;
    }

    public int getNumberOfStudent() {
        return students.size();
    }

    public void clearStudents() {
        students.clear();
    }

    public void addStudent(Student student) {
        students.add(student);
    }

    public void removeStudent(Student student) {
        students.remove(student);
    }

    public Schedule getCourseSchedule() {
        return courseSchedule;
    }
    
    @Override
    public int compareTo(Course course) {
        return this.courseName.compareTo(course.getCourseName());
    } 
}