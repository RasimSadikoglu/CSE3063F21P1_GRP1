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
    private int currentSemester;
    private String simulatedSemester;
    private boolean reCreateStudentData;

    public Simulation(){
        students = new ArrayList<Student>();
        courses = new ArrayList<Course>();
        currentSemester = 0;
        simulatedSemester = "";
        reCreateStudentData = false;
    }

    // public Simulation(ArrayList<Student> students, ArrayList<Course> courses, String simulatedSemester, boolean reCreateStudentData){
    //     this.students = students;
    //     this.courses = courses;
    //     this.currentSemester = 0;
    //     this.simulatedSemester = simulatedSemester;
    //     this.reCreateStudentData = reCreateStudentData;
    // }

    public void getData() {
        Course[] courses = DataIOHandler.readCourseInfo("/home/rasim/Yandex.Disk/Files/Workspaces/CSE3063F21P1_GRP1/iteration_1/jsonDocs/courses.json");
		this.courses.addAll(Arrays.asList(courses));

		if (reCreateStudentData) return;

        File[] students = new File("/home/rasim/Yandex.Disk/Files/Workspaces/CSE3063F21P1_GRP1/iteration_1/jsonDocs/students").listFiles();

        for (File student: students) {
            this.students.add(DataIOHandler.readStudentInfo(student.toPath().toString()));
        }

        System.out.println("Number of students: " + this.students.size() + "\n");
    }

    public void start() {
        // if (reCreateStudentData) createStudentData();

        // if (continue) this.start();
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
            }

        */

    }

    private void createNewStudents(int numberOfStudent) {

    }

    private void finalPoints() {

    }

    private void SimulationLoop(){

        // if (currentSemester % 2 == 1) createNewStudents(); - the students registered the school that year

        // courseRegistiration();

        // finalPoints();

        // this.currentSemester++;
    }
}



