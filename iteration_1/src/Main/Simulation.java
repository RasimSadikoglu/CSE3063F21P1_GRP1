package Main;

import java.util.ArrayList;
import java.util.Map;
import java.util.TreeMap;
import java.util.TreeSet;

import Course.*;
import Department.*;
import Student.*;
import Student.Semester.LetterNote;
import Util.*;

public class Simulation {
    private enum semesterName {Fall, Spring} 

    private RandomObjectGenerator randomObjectGenerator;
    private Advisor advisor;
    private ManagementSystem managementSystem;

    private ArrayList<Student> students;
    private int currentSemester;
    private semesterName simulatedSemester;
    private int recreationLoopCount;
    private int yearlyStudentCount;

    public Simulation(){
        students = new ArrayList<Student>();
        currentSemester = 1;
        simulatedSemester = null;
        recreationLoopCount = 0;
        yearlyStudentCount = 0;
        randomObjectGenerator = new RandomObjectGenerator();
        advisor = new Advisor();
        managementSystem = new ManagementSystem();
    }

    public void setup() {

        this.randomObjectGenerator = new RandomObjectGenerator(yearlyStudentCount);
        managementSystem = new ManagementSystem(randomObjectGenerator);

        if (recreationLoopCount != 0) return;

        students = DataIOHandler.readStudentsData("jsonDocs/students/before/");

        if (!students.isEmpty()) {
            Student lastStudent = this.students.get(this.students.size() - 1);
            currentSemester = lastStudent.getCurrentSemester();
            currentSemester += (Integer.parseInt(lastStudent.getId().substring(4,6)) - 20) * 2;
        }
    }

    public void start() {

        if (simulatedSemester == semesterName.Fall) {
            recreationLoopCount += ((currentSemester + recreationLoopCount + 1) % 2);
        } else if (simulatedSemester == semesterName.Spring) {
            recreationLoopCount += ((currentSemester + recreationLoopCount) % 2);
        } else {
            System.out.println("There is no such semester!");
            System.exit(1);
        }

        // Student data creation
        for (int i = 0; i < recreationLoopCount; i++) {
            simulationLoop();
        }

        // Save student data before simulation
        DataIOHandler.writeStudentsData(students, "jsonDocs/students/before/");

        Logger.setup();

        simulationLoop();

        // Save student data after simulation
        DataIOHandler.writeStudentsData(students, "jsonDocs/students/after/");
    }

    private void courseRegistration() {
        this.students.forEach(student -> { // iterate through students
            if (student.getIsGraduate()) return;

            TreeSet<Course> addableCourses = managementSystem.getAddebleCourses(student);

            ArrayList<Course> validCourses = managementSystem.submitCourseList(student, addableCourses);
            
            advisor.advisorCheck(validCourses, student);

            if (validCourses.isEmpty() && student.getGPA()[0] < 2) validCourses.addAll(student.getConditionalCourses());
            
            student.addSemester(new Semester(validCourses));
        });
    }

    private void generateExamScores() {

        for (Student student: students) {

            if (student.getIsGraduate()) continue;

            TreeMap<String, LetterNote> notes = student.getTranscript().getCurrentSemester().getNotes();

            for (Map.Entry<String, LetterNote> note: notes.entrySet()){
                note.setValue(LetterNote.values()[randomObjectGenerator.getBellRandom(0, 10)]);
                // Logger.addNewLog("SIMULATION-SET NOTE-" + student.getId(), "Generated random note for course " + note.getKey() + ".");
            }

            student.updateGPA();
            student.setIsGradute();
        }         
    }

    private void simulationLoop() {

        if (currentSemester % 2 == 1) {
            students.addAll(randomObjectGenerator.getRandomStudents(20 + currentSemester / 2));
        }

        managementSystem.resetCourseQuotas();

        managementSystem.updateCurrentSemester(currentSemester);

        courseRegistration();

        generateExamScores();

        this.currentSemester++;
    }

    public void end() {
        Logger.end();
    }
}
