package Course;

public class Course {

    private String courseName;
    private String courseCode;
    private float requiredCredits;
    private String prerequisiteCourse;
    private float courseCredits;
    private int maxQuota;
    private int currentQuota;
    private Schedule courseSchedule;

    public Course() {
        courseName = "";
        courseCode = "";
        requiredCredits = 0;
        prerequisiteCourse = "";
        courseCredits = 0;
        maxQuota = 0;
        currentQuota = 0;
        courseSchedule = new Schedule();
    }

    public Course(String courseName, String courseCode, float requiredCredits, String prerequisiteCourse, float courseCredits, int maxQuota, int currentQuota, Schedule courseSchedule){
        this.courseName = courseName;
        this.courseCode = courseCode;
        this.requiredCredits = requiredCredits;
        this.prerequisiteCourse = prerequisiteCourse;
        this.courseCredits = courseCredits;
        this.maxQuota = maxQuota;
        this.currentQuota = currentQuota;
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

    public int getMaxQuota() {
        return maxQuota;
    }

    public void setCourseQuota(int maxQuota) {
        this.maxQuota = maxQuota;
    }

    public int getCurrentQuota() {
        return currentQuota;
    }

    public Schedule getCourseSchedule() {
        return courseSchedule;
    }

    public void setCourseSchedule(Schedule courseSchedule) {
        this.courseSchedule = courseSchedule;
    }
}
