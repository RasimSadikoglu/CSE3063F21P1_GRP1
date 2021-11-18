package Main;

import java.io.File;
import java.util.ArrayList;
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
    private int currentSemester;
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

    public Simulation(){
        students = new ArrayList<Student>();
        currentSemester = 1; // true = "Fall", false = "Spring"
        simulatedSemester = "";
        reCreateStudentData = 0;
        yearlyStudentCount = 0;
    }

    // call after reading data
    public void setRandomObjectGenerator() {
        this.randomObjectGenerator = new RandomObjectGenerator(yearlyStudentCount);
    }

    public void getData() {

        if (reCreateStudentData != 0) return;

        File[] students = new File(DataIOHandler.currentPath + "jsonDocs/students/before/").listFiles();

        if (students == null) return;

        for (File student: students) {
            this.students.add(DataIOHandler.readStudentInfo("jsonDocs/students/before/" + student.getName()));

            System.out.println(student.getName() + " has been read!");
        }

    }

    public void start() {

        System.out.println("Simulation has been started!");

        // This will ensure last semester simulated will be match the parameter.
        if (simulatedSemester.equals("Fall"))
            reCreateStudentData += reCreateStudentData % 2;
        else if (simulatedSemester.equals("Spring"))
            reCreateStudentData += (reCreateStudentData + 1) % 2;
        else
            return; // Exception can be added.


        // Student data creation
        for (int i = 0; i < reCreateStudentData; i++) {
            simulationLoop();
        }

        // Save student data before simulation
        DataIOHandler.writeStudentsData(students, "jsonDocs/students/before/");

        simulationLoop();

        // Save student data after simulation
        DataIOHandler.writeStudentsData(students, "jsonDocs/students/after/");
    }

    private void advisorCheck(ArrayList<Course> currentCourses) { // BAHADIR
        // Check after all courses had been added.

        /*
         * 1. credit check 2. collision check // Seperate method might be necessary.
         */

        // remove necesssary courses.
    }

    //
    private ArrayList<int[]> getScheduleTimes(Schedule schedule) {
        ArrayList<int[]> times = new ArrayList<int[]>();

        for (String str : schedule.getTimeTable()) {
            // { startMinute, endMinute } (minute starts counting from beginning of the
            // week)
            int[] courseTimes = new int[2];
            String[] splitedTime = str.split("-", 3);

            String[] splitedStartTime = splitedTime[1].split("\\.", 2);
            String[] splitedEndTime = splitedTime[2].split("\\.", 2);

            int day = Integer.parseInt(splitedTime[0]);
            courseTimes[0] = day * 24 * 60 + Integer.parseInt(splitedStartTime[0]) * 60
                    + Integer.parseInt(splitedStartTime[1]);
            courseTimes[1] = day * 24 * 60 + Integer.parseInt(splitedEndTime[0]) * 60
                    + Integer.parseInt(splitedEndTime[1]);

            times.add(courseTimes);
        }

        return times;
    }

    private int getTotalCollisionMinutes(Schedule firstSchedule, Schedule secondSchedule) {
        ArrayList<int[]> firstTimes = getScheduleTimes(firstSchedule);
        ArrayList<int[]> secondTimes = getScheduleTimes(secondSchedule);

        int totalCollisionMinute = 0;

        for (int[] time1 : firstTimes) {
            for (int[] time2 : secondTimes) {
                // | ......1.............2..........................2............1........ |
                if (time1[0] <= time2[0] && time1[1] >= time2[1]) {
                    totalCollisionMinute += time1[1] - time1[0];
                }
                // | ......1.............2..........................1............2........ |
                else if (time1[0] <= time2[0] && time1[1] >= time2[0] && time1[1] <= time2[1]) {
                    totalCollisionMinute += time1[1] - time2[0];
                }
                // | ......2.............1..........................1............2........ |
                else if (time1[0] >= time2[0] && time1[0] <= time2[1]) {
                    totalCollisionMinute += time1[1] - time1[0];
                }
                // | ......2.............1..........................2............1........ |
                else if (time1[0] >= time2[0] && time1[0] <= time2[1] && time1[1] >= time2[1]) {
                    totalCollisionMinute += time2[1] - time1[0];
                }
            }
        }

        return totalCollisionMinute;
    }

    public void collisionCheck(ArrayList<Course> currentCourses) {
        for (int i = 0; i < currentCourses.size(); i++) {
            for (int j = i + 1; j < currentCourses.size(); j++) {
                int totalCollisionMinute = getTotalCollisionMinutes(currentCourses.get(i).getCourseSchedule(),
                        currentCourses.get(j).getCourseSchedule());

                // if there is any collision remove the course
                if (totalCollisionMinute > 0) {
                    currentCourses.remove(currentCourses.get(j--));
                }
            }
        }
    }

    private boolean systemCheck(Course newCourse, Student student) { // ERKAM
        
        String prerequisiteCourse = newCourse.getPrerequisiteCourse();

        if (!prerequisiteCourse.equals("")) { // if there is a prerequisite course
            System.out.println(prerequisiteCourse);
            if (student.getTranscript().getCourseNote(prerequisiteCourse) < 1) { // if student could not pass prerequisite course
                return false;
            }
        }
        if (newCourse.getCourseQuota() != 0 && newCourse.getCourseQuota() <= newCourse.getNumberOfStudent()) { // if quota is not full
            return false;
        }
        return true;

    }

    private void courseRegistiration() {
        this.students.forEach(student -> { // iterate through students
            System.out.println("Add courses for student " + student.getId() + "!");

            int currentSemester = student.getCurrentSemester();
            ArrayList<Course> addableCourses = new ArrayList<>();
            addableCourses.addAll(getAllCourses("SME" + currentSemester)); // add mandatory courses
            addableCourses.addAll(student.getfailedCourses()); // add failed courses

            for (int i = 0; i < semesterCourses[currentSemester - 1].length; i++) {
                addableCourses.addAll(getRandomCourse(semesterCourses[currentSemester - 1][i], student, addableCourses)); // TE and NTE course selection   
            }

            ArrayList<Course> validCourses = new ArrayList<>();
            addableCourses.forEach(course -> {
                if (systemCheck(course, student)) { // student parameter added 
                    validCourses.add(course);
                    course.setNumberOfStudent(course.getNumberOfStudent() + 1);
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

            if (courses.get(randomIndex).getCourseQuota() == 0) {
                randomCourse.add(courses.get(randomIndex));
				return randomCourse;
            }

			if (courses.get(randomIndex).getCourseQuota() > courses.get(randomIndex).getNumberOfStudent()) {
                randomCourse.add(courses.get(randomIndex));
				return randomCourse;
			} else {
				courses.remove(randomIndex);
			}
		}

		return randomCourse;

    }

    private ArrayList<Course> getAllCourses(String courseCode) {
        Course[] courses;
        
        if (currentSemester % 2 == 1) courses = DataIOHandler.fallCourses;
        else courses = DataIOHandler.springCourses;
        
        ArrayList<Course> matchedCourses = new ArrayList<>();
        for (Course course : courses) {
            course.getCourseGroup().forEach(entityCode -> {
                if (entityCode.equals(courseCode)) matchedCourses.add(course);
            });
        }

        return matchedCourses;
    }

    private void createNewStudents(int numberOfStudent) {
        this.students.addAll(randomObjectGenerator.getRandomStudents(20 + currentSemester / 2));
    }

    private void finalPoints() {

        for (int i = 0; i < students.size(); i++) {

            System.out.println("Add notes for student " + students.get(i).getId() + "!");

            TreeMap<String, Semester.letterNote> notes = students.get(i).getTranscript().getCurrentSemester().getNotes();

            for (Map.Entry<String, Semester.letterNote> note: notes.entrySet()){
                note.setValue(Semester.letterNote.values()[randomObjectGenerator.getBellRandom(0, 10)]);
            }

            students.get(i).updateGPA();
        }         
    }

    private void simulationLoop() {

        if (currentSemester % 2 == 1) createNewStudents(yearlyStudentCount);

        courseRegistiration();

        finalPoints();

        this.currentSemester++;
    }
}
