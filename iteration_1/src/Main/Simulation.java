package Main;

import java.io.File;
import java.util.ArrayList;
import java.util.Map;
import java.util.TreeMap;

import Course.Course;
import Student.Semester;
import Student.Student;
import Util.DataIOHandler;
import Util.RandomObjectGenerator;

public class Simulation {
    private RandomObjectGenerator randomObjectGenerator;

    private ArrayList<Student> students;
    private boolean currentSemester;
    private String simulatedSemester;
    private int reCreateStudentData;
    private int yearlyStudentCount;
    private String[][] semesterCourses = {
        { /* 1 */ },
        { /* 2 */ "NTE" },
        { /* 3 */ "NTE" },
        { /* 4 */ },
        { /* 5 */ },
        { /* 6 */ },
        { /* 7 */ "NTE", "TE", "EP1" },
        { /* 8 */ "NTE", "TE", "TE", "TE", "TE", "EP2" }
    };

	/*
	 * private enum semesters{ Fall, Spring }
	 * 
	 */

    public Simulation(){
        students = new ArrayList<Student>();
        currentSemester = true; // true = "Fall", false = "Spring"
        simulatedSemester = "";
        reCreateStudentData = 0;
        yearlyStudentCount = 0;
    }

    // call after reading data
    public void setRandomObjectGenerator(){
        this.randomObjectGenerator = new RandomObjectGenerator(yearlyStudentCount);
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

    private void collisionCheck(ArrayList<Course> currentCourses) {
        // holds index of the course to remove
        ArrayList<Course> courseRemoveQueue = new ArrayList<Course>();

        for (int i = 0; i < currentCourses.size(); i++){
            for (int j = i + 1; j < currentCourses.size(); j++){
                if (currentCourses.get(i) == currentCourses.get(j)){
                    courseRemoveQueue.add(currentCourses.get(i));
                }
            }
        }

        for (Course course : courseRemoveQueue){
            currentCourses.remove(course);
        }
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

    private void courseRegistiration() {
        
        this.students.forEach(student -> { // iterate through students
            int currentSemester = student.getCurrentSemester();
            ArrayList<Course> addableCourses = new ArrayList<>();
            addableCourses.addAll(getAllCourses("SME" + currentSemester)); // add mandatory courses
            addableCourses.addAll(student.getfailedCourses()); // add failed courses

            for (int i = 0; i < semesterCourses[currentSemester - 1].length; i++) {
                addableCourses.addAll(getRandomCourse(semesterCourses[currentSemester - 1][i], student, addableCourses)); // TE and NTE course selection   
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

	private ArrayList<Course> getRandomCourse(String courseCode, Student student, ArrayList<Course> currentCourses) {
		ArrayList<Course> courses = getAllCourses(courseCode);

        // This will prevent null objects from being returned.
        ArrayList<Course> randomCourse = new ArrayList<Course>();

		while (!courses.isEmpty()) {
			int randomIndex = this.randomObjectGenerator.getLinearRandom(0, courses.size());

			if (student.getCourseNote(courses.get(randomIndex).getCourseName()) >= 1) {
				courses.remove(randomIndex);
				continue;
			}

            if (currentCourses.contains(courses.get(randomIndex))) {
                courses.remove(randomIndex);
				continue;
            }

            if (courses.get(randomIndex).getcourseQuota() == 0) {
                randomCourse.add(courses.get(randomIndex));
				return randomCourse;
            }

			if (courses.get(randomIndex).getcourseQuota() > courses.get(randomIndex).getnumberOfStudent()) {
                randomCourse.add(courses.get(randomIndex));
				return randomCourse;
			} else {
				courses.remove(randomIndex);
			}
		}

		return randomCourse;

    }
    
    private ArrayList<Course> getAllCourses(String courseCode) {
        Course[] courses = DataIOHandler.courses;
        
        ArrayList<Course> matchedCourses = new ArrayList<>();
        for (Course course : courses) {
            course.getCourseGroup().forEach(entityCode -> {
                if (entityCode == courseCode) matchedCourses.add(course);
            });
        }

        return matchedCourses;
    }

    private void createNewStudents(int numberOfStudent) {
        this.students.addAll(randomObjectGenerator.getRandomStudents(18)); 
    }

    private void finalPoints() {

        for (int i = 0; i < students.size(); i++){
        
            TreeMap<String, Semester.letterNote> notes = students.get(i).getTranscript().getCurrentSemester().getNotes();

            for (Map.Entry<String, Semester.letterNote> note: notes.entrySet()){
                note.setValue(Semester.letterNote.values()[randomObjectGenerator.getBellRandom(1, 11)]);
            }
        }         
    }

    private void simulationLoop(){

        if (currentSemester) createNewStudents(yearlyStudentCount);

        courseRegistiration();

        finalPoints();

        this.currentSemester ^= true;
    }
}



