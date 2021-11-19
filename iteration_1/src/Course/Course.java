package Course;

public class Course {

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

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public String getCourseGroup() {
        return courseGroup;
    }

    public void setCourseGroup(String courseGroup) {
        this.courseGroup = courseGroup;
    }

    public float getRequiredCredits() {
        return requiredCredits;
    }

    public void setRequiredCredits(float requiredCredits) {
        this.requiredCredits = requiredCredits;
    }

    public String getPrerequisiteCourse() {
        return prerequisiteCourse;
    }

    public void setPrerequisiteCourse(String prerequisiteCourse) {
        this.prerequisiteCourse = prerequisiteCourse;
    }

    public float getCourseCredits() {
        return courseCredits;
    }

    public void setCourseCredits(float courseCredits) {
        this.courseCredits = courseCredits;
    }

    public int getCourseQuota() {
        return courseQuota;
    }

    public void setCourseQuota(int courseQuota) {
        this.courseQuota = courseQuota;
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

    public void setCourseSchedule(Schedule courseSchedule) {
        this.courseSchedule = courseSchedule;
    }
}
