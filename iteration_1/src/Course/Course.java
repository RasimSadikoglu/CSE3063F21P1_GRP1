package Course;

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
    private int numberOfStudent;
    private Schedule courseSchedule;

    public Course() {
        courseName = "";
        courseGroup = null;
        requiredCredits = 0;
        prerequisiteCourse = "";
        courseCredits = 0;
        courseQuota = 0;
        numberOfStudent = 0;
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
        return numberOfStudent;
    }

    public void setNumberOfStudent(int numberOfStudent) {
        this.numberOfStudent = numberOfStudent;
    }

    public Schedule getCourseSchedule() {
        return courseSchedule;
    }
    
    @Override
    public int compareTo(Course course) {
        return this.courseName.compareTo(course.getCourseName());
    } 
}
