package Department;

import java.util.ArrayList;
import java.util.TreeSet;

import Course.*;
import Student.*;
import Util.*;

public class ManagementSystem {

    RandomObjectGenerator randomObjectGenerator;
    int currentSemester;

    private final int[][] electiveCourses = {
        /*  NTE TE FTE*/
            {0, 0, 0},
            {1, 0, 0},
            {0, 0, 0},
            {0, 0, 0},
            {0, 0, 0},
            {0, 0, 0},
            {2, 1, 0},
            {3, 4, 1},
        };
    
    public ManagementSystem() {
        randomObjectGenerator = new RandomObjectGenerator();
        currentSemester = 1;
    }

    public ManagementSystem(RandomObjectGenerator rog) {
        randomObjectGenerator = rog;
        currentSemester = 1;
    }

    public void updateCurrentSemester(int currentSemester) {
        this.currentSemester = currentSemester;
    }

    public ArrayList<Course> submitCourseList(Student student, TreeSet<Course> courses) {

        int totalCredits = 0;
        ArrayList<Course> validCourses = new ArrayList<>();

        for (Course course: courses) {
            if (validCourses.size() == 10) continue;
            if (totalCredits + course.getCourseCredits() > 40) continue;
            if (check(course, student)) { // student parameter added
                Logger.addNewLog("SYSTEM-ADD COURSE-" + student.getId(), course.getCourseName() + " has been added to the course list.");
                validCourses.add(course);
                course.setNumberOfStudent(course.getNumberOfStudent() + 1);
                totalCredits += course.getCourseCredits();
            }
        }

        Logger.addNewLog("SYSTEM-APPROVE-" + student.getId(), "Course list has been sent to advisor approval.");

        return validCourses;
    }

    private boolean check(Course newCourse, Student student) {

        if (student.getCourseNote(newCourse.getCourseName()) >= 1) return false;
        
        String prerequisiteCourse = newCourse.getPrerequisiteCourse();

        if (!prerequisiteCourse.equals("")) { // if there is a prerequisite course
            if (student.getTranscript().getCourseNote(prerequisiteCourse) < 1) { // if student could not pass prerequisite course
                Logger.addNewLog("SYSTEM-FAIL PREREQUISITE-" + student.getId(), "Student couldn't take the course " + newCourse.getCourseName() + ".");
                return false;
            }
        }
        if (newCourse.getCourseQuota() != 0 && newCourse.getCourseQuota() <= newCourse.getNumberOfStudent()) { // if quota is not full
            Logger.addNewLog("SYSTEM-FAIL QUOTA-" + student.getId(), "Student couldn't take the course " + newCourse.getCourseName() + ".");
            return false;
        }

        return true;

    }

    public TreeSet<Course> getAddebleCourses(Student student) {

        Logger.addNewLog("SYSTEM-GET COURSE-" + student.getId(), "Get available courses.");

        TreeSet<Course> addableCourses = new TreeSet<Course>();

        int currentSemester = Math.min(student.getCurrentSemester(), 8);

        // add mandatory courses
        for (int i = 1; i <= currentSemester; i++) {
            addableCourses.addAll(getAllCourses("SME" + i));
        }
        
        int nteCount = electiveCourses[currentSemester - 1][0] - student.getCourseCount("NTE");
        int teCount = electiveCourses[currentSemester - 1][1] - student.getCourseCount("TE");
        int fteCount = electiveCourses[currentSemester - 1][2] - student.getCourseCount("FTE");

        addableCourses.addAll(getRandomCourses("NTE", student, addableCourses, nteCount));
        addableCourses.addAll(getRandomCourses("TE", student, addableCourses, teCount));
        addableCourses.addAll(getRandomCourses("FTE", student, addableCourses, fteCount));

        return addableCourses;
    }

    private TreeSet<Course> getRandomCourses(String courseCode, Student student, TreeSet<Course> currentCourses, int count) {

		ArrayList<Course> courses = getAllCourses(courseCode);

        // This will prevent null objects from being returned.
        TreeSet<Course> randomCourses = new TreeSet<Course>();

		while (!courses.isEmpty() && randomCourses.size() < count) {

			int randomIndex = this.randomObjectGenerator.getLinearRandom(0, courses.size());

			if (student.getCourseNote(courses.get(randomIndex).getCourseName()) >= 1) {
				courses.remove(randomIndex);
				continue;
			}

            if (currentCourses.contains(courses.get(randomIndex))) {
                courses.remove(randomIndex);
				continue;
            }

            if (courses.get(randomIndex).getCourseQuota() == 0) {
                randomCourses.add(courses.get(randomIndex));
                courses.remove(randomIndex);
				continue;
            }

			if (courses.get(randomIndex).getCourseQuota() > courses.get(randomIndex).getNumberOfStudent()) {
                randomCourses.add(courses.get(randomIndex));
                courses.remove(randomIndex);
			} else {
				courses.remove(randomIndex);
			}
		}

		return randomCourses;

    }

    private ArrayList<Course> getAllCourses(String courseGroup) {
        Course[] courses;
        
        if (currentSemester % 2 == 1) courses = DataIOHandler.fallCourses;
        else courses = DataIOHandler.springCourses;
        
        ArrayList<Course> matchedCourses = new ArrayList<>();
        for (Course course : courses) {
            if (courseGroup.equals(course.getCourseGroup())) matchedCourses.add(course);
        }

        return matchedCourses;
    }

    public void resetCourseQuotas() {

        Logger.addNewLog("SYSTEM-RESET-QUOTA", "Resetting course quotas.");

        for (Course course: DataIOHandler.fallCourses) course.setNumberOfStudent(0);
        for (Course course: DataIOHandler.springCourses) course.setNumberOfStudent(0);

    }

}