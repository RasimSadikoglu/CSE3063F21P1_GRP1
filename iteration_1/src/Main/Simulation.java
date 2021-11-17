package Main;

import java.util.ArrayList;

import Course.Course;
import Student.Semester;
import Student.Student;
import Util.*;
import java.io.File;

public class Simulation {
    private ArrayList<Student> students;
    private boolean currentSemester;
    private String simulatedSemester;
    private int reCreateStudentData;
    private int yearlyStudentCount;
    private String[][] semesterCourses = {
        { /* 1 */ },
        { /* 2 */ "NTE1" },
        { /* 3 */ "NTE2" },
        { /* 4 */ },
        { /* 5 */ },
        { /* 6 */ },
        { /* 7 */ "UE", "TE1", "TE2" },
        { /* 8 */ "NTE3", "FTE", "TE3", "TE4", "TE5" }
    };
    public Simulation(){
        students = new ArrayList<Student>();
        currentSemester = true; // true = "Fall", false = "Spring"
        simulatedSemester = "";
        reCreateStudentData = 0;
        yearlyStudentCount = 0;
    }

    public void getData() {

		if (reCreateStudentData != 0) return;

        File[] students = new File(DataIOHandler.currentPath + "jsonDocs/students").listFiles();

        for (File student: students) {
            this.students.add(DataIOHandler.readStudentInfo("jsonDocs/students/" + student.getName()));
            this.students.get(this.students.size() - 1).updateGPA();
        }

        System.out.println("Number of students: " + this.students.size() + "\n");
    }

    public void start() {

        // This will ensure last semester simulated will be match the parameter.
        if (simulatedSemester.equals("Fall")) reCreateStudentData += reCreateStudentData % 2;
        else if (simulatedSemester.equals("Spring")) reCreateStudentData += (reCreateStudentData + 1) % 2;
        else return; // Exception can be added.

        for (int i = 0; i < reCreateStudentData; i++) {
            simulationLoop();
        }

    }

    public void end() {
        DataIOHandler.writeStudentsData(students);
    }

    private void advisorCheck(ArrayList<Course> currentCourses){ // BAHADIR
        // Check after all courses had been added.

        /*
            1. credit check
            2. collision check // Seperate method might be necessary.
        */

        // remove necesssary courses.
    }

    private void collisionCheck(ArrayList<Course> currentCourses) { // MUCAHIT
        // remove necessary courses.
    }

    private boolean systemCheck(Course newCourse){ // ERKAM
        // Check for can new course be added.

        /*
            Check for prerequsite and quota.

            1. Check for prerequisite
            2. Compare current quota with max quota
        */

        return true;
    }

    private void courseRegistiration() { // NAZMI

        // Add course depending on the "courseCode"
        /*
            1. Semester Course Groups = SME1
            2. Semester Course Groups = SME2, NTE1
            3. Semester Course Groups = SME3, NTE2
            4. Semester Course Groups = SME4
            5. Semester Course Groups = SME5
            6. Semester Course Groups = SME6
            7. Semester Course Groups = SME7, UE, TE1, TE2
            8. Semester Course Groups = SME8, NTE3, FTE, TE3, TE4, TE5
        */
        
        this.students.forEach(student -> { // iterate through students
            int currentSemester = student.getCurrentSemester();
            ArrayList<Course> addableCourses = new ArrayList<>();
            addableCourses.addAll(getAllCourses("SME" + currentSemester)); // add mandatory courses
            addableCourses.addAll(student.getfailedCourses()); // add failed courses

            for (int i = 0; i < semesterCourses[currentSemester - 1].length; i++) {
                addableCourses.add(getRandomCourse(semesterCourses[currentSemester - 1][i])); // TE and NTE course selection   
            }
            
            ArrayList<Course> validCourses = new ArrayList<>();
            addableCourses.forEach(course -> {
                if (systemCheck(course)){ 
                    validCourses.add(course);
                    course.setNumberOfStudent(course.getnumberOfStudent() + 1);
                }
            });
            advisorCheck(validCourses);
            student.addSemester(new Semester(validCourses));
        });
    }

    private Course getRandomCourse(String courseCode) { // BERK
        Course[] courses = DataIOHandler.courses;

        /*
            Suggested Way:
            1. Get all courses in that group
            2. Shuffle the array
            3. Return first match
        */

        /*
            Write a method that returns a random course in given course group,
            that it has not filled up yet.
        */

        return null;
    }

    private ArrayList<Course> getAllCourses(String courseCode) { // HASAN
        Course[] courses = DataIOHandler.courses;

        /*
            Write a method that returns all courses in given course group.
        */

        return null;
    }

    private void createNewStudents(int numberOfStudent) { // MUCAHIT
        // this.students.addAll(giveMeSomeStudents(numberOfStudent)); 
    }

    private void finalPoints() { // BURAK

        /*

            loop students {
                student.currentSemester.notes;

                for note in notes {
                    note = rand() // Random method MUCAHIT
                }
            }

        */

    }

    private void simulationLoop(){

        if (currentSemester) createNewStudents(yearlyStudentCount);

        courseRegistiration();

        finalPoints();

        this.currentSemester ^= true;
    }
}



