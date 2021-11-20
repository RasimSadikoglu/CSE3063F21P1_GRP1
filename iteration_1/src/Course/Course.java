package Course;

public class Course implements Comparable<Course> {

    private String courseName;
    private String courseGroup;
    private float requiredCredits;
    private String prerequisiteCourse;
    private float courseCredits;
    private int courseQuota;
    private int numberOfStudent;
    private Schedule courseSchedule;

    public Course() {
        courseName = "";
        courseGroup = "";
        requiredCredits = 0;
        prerequisiteCourse = "";
        courseCredits = 0;
        courseQuota = 0;
        numberOfStudent = 0;
        courseSchedule = new Schedule();
    }

    public Course(String courseName, String courseGroup, float requiredCredits, String prerequisiteCourse, float courseCredits, int courseQuota, int numberOfStudent, Schedule courseSchedule){
        this.courseName = courseName;
        this.courseGroup = courseGroup;
        this.requiredCredits = requiredCredits;
        this.prerequisiteCourse = prerequisiteCourse;
        this.courseCredits = courseCredits;
        this.courseQuota = courseQuota;
        this.numberOfStudent = numberOfStudent;
        this.courseSchedule = courseSchedule;
    }

    public String getCourseName() {
        return courseName;
    }

    public String getCourseGroup() {
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
