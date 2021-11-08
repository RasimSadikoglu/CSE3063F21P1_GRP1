package Course;

public class Course {

    private String courseName;
    private String courseCode;
    private float requiredCredits;
    private String prerequisiteCourse;
    private float courseCredits;
    private int courseQuota;
    private Schedule courseSchedule;

    public Course() {
        courseName = "";
        courseCode = "";
        requiredCredits = 0;
        prerequisiteCourse = "";
        courseCredits = 0;
        courseQuota = 0;
        courseSchedule = new Schedule();
    }

    public Course(String courseName, String courseCode, float requiredCredits, String prerequisiteCourse, float courseCredits, int courseQuota, Schedule courseSchedule){
        this.courseName = courseName;
        this.courseCode = courseCode;
        this.requiredCredits = requiredCredits;
        this.prerequisiteCourse = prerequisiteCourse;
        this.courseCredits = courseCredits;
        this.courseQuota = courseQuota;
        this.courseSchedule = courseSchedule;
    }

    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public String getCourseCode() {
        return courseCode;
    }

    public void setCourseCode(String courseCode) {
        this.courseCode = courseCode;
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

    public Schedule getCourseSchedule() {
        return courseSchedule;
    }

    public void setCourseSchedule(Schedule courseSchedule) {
        this.courseSchedule = courseSchedule;
    }
}
