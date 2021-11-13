package Main;

import java.util.ArrayList;
import java.util.Arrays;

import Course.Course;
import Student.Student;
import Util.*;
import java.io.File;

public class Simulation {
    private ArrayList<Student> students;
    private ArrayList<Course> courses;
    private boolean currentSemester;
    private String simulatedSemester;
    private int reCreateStudentData;

    public Simulation(){
        students = new ArrayList<Student>();
        courses = new ArrayList<Course>();
        currentSemester = true; // true = "Fall", false = "Spring"
        simulatedSemester = "";
        reCreateStudentData = 0;
    }

    // public Simulation(ArrayList<Student> students, ArrayList<Course> courses, String simulatedSemester, boolean reCreateStudentData){
    //     this.students = students;
    //     this.courses = courses;
    //     this.currentSemester = 0;
    //     this.simulatedSemester = simulatedSemester;
    //     this.reCreateStudentData = reCreateStudentData;
    // }

    public void getData() {
        Course[] courses = DataIOHandler.readCourseInfo("jsonDocs/courses.json");
		this.courses.addAll(Arrays.asList(courses));

		if (reCreateStudentData == 0) return;

        File[] students = new File(DataIOHandler.currentPath + "jsonDocs/students").listFiles();

        for (File student: students) {
            this.students.add(DataIOHandler.readStudentInfo("jsonDocs/students/" + student.getName()));
        }

        System.out.println("Number of students: " + this.students.size() + "\n");
    }

    public void start() {
        // if (reCreateStudentData) createStudentData();
        
        // loop this.start();
    }

    private void advisorCheck(ArrayList<Course> currentCourses){
        // Check after all courses had been added.

        /*
            1. credit check
            2. collision check // Seperate method might be necessary.
        */

        // remove necesssary courses.
    }

    private boolean systemCheck(ArrayList<Course> currentCourses, Course newCourse){
        // Check for can new course be added.

        /*
            Check for prerequsite and quota.

            1. Check for prerequisite
            2. Compare current quota with max quota
        */

        return true;
    }

    private void courseRegistiration() {

        // Add course depending on the "courseCode"

        // For a second semester student, add courses that course code equals to "SME2" or "NTE1"

        /*
            loop students as student {

                ArrayList currentCourses = new ArrayList;

                addebleCourses = [course.courseCode == SME2 or NTE1 for course in courses]; // It can also be a method;

                loop addebleCourses as course {

                    if (systemCheck(currentCourses, course)) currentCourses.add(course);

                }

                advisorCheck(currentCourses);

                Semester s = new Semester(currentCourses);

                student.addSemester(s);

                student.increaseSemesterCount();
            }

        */

    }

    private void createNewStudents(int numberOfStudent) {
        // this.students.addAll(giveMeSomeStudents(numberOfStudent));
    }

    private void finalPoints() {

        /*

            loop students {
                student.currentSemester;

                for course in currentSemester {
                    note = rand()
                }
            }

        */

    }

    private void SimulationLoop(){

        // if (currentSemester) createNewStudents(); - the students registered the school that year

        // courseRegistiration();

        // finalPoints();

        // this.currentSemester ^= true;
    }
}



