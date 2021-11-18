package Main;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

import Course.Course;
import Course.Schedule;
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
        { /* 2 */ "NTE1" },
        { /* 3 */ "NTE2" },
        { /* 4 */ },
        { /* 5 */ },
        { /* 6 */ },
        { /* 7 */ "UE", "TE1", "TE2" },
        { /* 8 */ "NTE3", "FTE", "TE3", "TE4", "TE5" }
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

    // 
    private ArrayList<int[]> getScheduleTimes(Schedule schedule){
        ArrayList<int[]> times = new ArrayList<int[]>();
            
        for (String str : schedule.getTimeTable()){
            // { startMinute, endMinute } (minute starts counting from beginning of the week)
            int[] courseTimes = new int[2];
            String[] splitedTime = str.split("-", 3);

            String[] splitedStartTime = splitedTime[1].split("\\.", 2);
            String[] splitedEndTime = splitedTime[2].split("\\.", 2);

            int day = Integer.parseInt(splitedTime[0]);
            courseTimes[0] = day * 24 * 60 + Integer.parseInt(splitedStartTime[0]) * 60 + Integer.parseInt(splitedStartTime[1]);
            courseTimes[1] = day * 24 * 60 + Integer.parseInt(splitedEndTime  [0]) * 60 + Integer.parseInt(splitedEndTime  [1]);

            times.add(courseTimes);
        }

        return times;
    }

    private int getTotalCollisionMinutes(Schedule firstSchedule, Schedule secondSchedule){
        ArrayList<int[]> firstTimes = getScheduleTimes(firstSchedule);
        ArrayList<int[]> secondTimes = getScheduleTimes(secondSchedule);

        int totalCollisionMinute = 0;

        for (int[] time1 : firstTimes){
            for (int[] time2 : secondTimes){
                // | ......1.............2..........................2............1........ |
                if      (time1[0] <= time2[0] && time1[1] >= time2[1]){
                    totalCollisionMinute += time1[1] - time1[0];
                }
                // | ......1.............2..........................1............2........ |
                else if (time1[0] <= time2[0] && time1[1] >= time2[0] && time1[1] <= time2[1]){
                    totalCollisionMinute += time1[1] - time2[0];
                }
                // | ......2.............1..........................1............2........ |
                else if (time1[0] >= time2[0] && time1[0] <= time2[1]){
                    totalCollisionMinute += time1[1] - time1[0];
                }
                // | ......2.............1..........................2............1........ |
                else if (time1[0] >= time2[0] && time1[0] <= time2[1] && time1[1] >= time2[1]){
                    totalCollisionMinute += time2[1] - time1[0];
                }
            }
        }

        return totalCollisionMinute;
    }

    public void collisionCheck(ArrayList<Course> currentCourses) {
        for (int i = 0; i < currentCourses.size(); i++){            
            for (int j = i + 1; j < currentCourses.size(); j++){
                int totalCollisionMinute = getTotalCollisionMinutes
                (
                    currentCourses.get(i).getCourseSchedule(), 
                    currentCourses.get(j).getCourseSchedule()
                );

                // if there is any collision remove the course
                if (totalCollisionMinute > 0){
                    currentCourses.remove(currentCourses.get(j--));
                }
            }
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
                addableCourses.add(getRandomCourse(semesterCourses[currentSemester - 1][i], student)); // TE and NTE course selection   
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

	private Course getRandomCourse(String courseCode, Student student) {
		Course[] courses = (Course[]) getAllCourses(courseCode).toArray();

		HashMap<Integer, Boolean> filledUpCourseIndexes = new HashMap<Integer, Boolean>();
		int filledUpCourseCount = 0;

		while (filledUpCourseCount < courses.length) {
			int randomIndex = this.randomObjectGenerator.getLinearRandom(0, courses.length - 1);
			if (filledUpCourseIndexes.get(randomIndex) == true) {
				filledUpCourseCount++;
				continue;
			}
			else if (student.getCourseNote(courses[randomIndex].getCourseName()) >= 1) {
				filledUpCourseCount++;
				continue;
			}

			if (courses[randomIndex].getcourseQuota() > courses[randomIndex].getnumberOfStudent()) {
				return courses[randomIndex];
			} else {
				filledUpCourseIndexes.put(randomIndex, true);
			}
		}

		return null;

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



