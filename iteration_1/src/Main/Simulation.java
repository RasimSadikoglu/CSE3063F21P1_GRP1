package Main;

import java.io.File;
import java.util.ArrayList;
import java.util.Map;
import java.util.TreeMap;
import java.util.TreeSet;

import Course.Course;
import Course.Schedule;
import Student.Semester;
import Student.Student;
import Util.DataIOHandler;
import Util.RandomObjectGenerator;

public class Simulation {
    private enum semesterName {Fall, Spring} 

    private RandomObjectGenerator randomObjectGenerator;

    private ArrayList<Student> students;
    private int currentSemester;
    private semesterName simulatedSemester;
    private int reCreateStudentData;
    private int yearlyStudentCount;
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

    public Simulation(){
        students = new ArrayList<Student>();
        currentSemester = 1;
        simulatedSemester = null;
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

        if (students == null || students.length == 0) return;

        for (File student: students) {
            Student newStudent = DataIOHandler.readStudentInfo("jsonDocs/students/before/" + student.getName());

            newStudent.updateCurrentSemester();

            this.students.add(newStudent);

            System.out.println(student.getName() + " has been read!");
        }

        currentSemester = this.students.get(this.students.size() - 1).getCurrentSemester();

    }

    public void start() {

        System.out.println("Simulation has been started!");

        if (simulatedSemester == semesterName.Fall) {
            reCreateStudentData += ((currentSemester + reCreateStudentData + 1) % 2);
        } else if (simulatedSemester == semesterName.Spring) {
            reCreateStudentData += ((currentSemester + reCreateStudentData) % 2);
        } else {
            System.out.println("There is no such semester!");
            System.exit(1);
        }

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

    private void advisorCheck(ArrayList<Course> currentCourses, Student student) {
    	
    	for(int i=0; i<currentCourses.size(); i++) {
            float requiredCredit = currentCourses.get(i).getRequiredCredits();
            if(student.getGPA()[1] < requiredCredit) {
                currentCourses.remove(currentCourses.get(i--));
            }
        }

        collisionCheck(currentCourses, student);

    }

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

    public void collisionCheck(ArrayList<Course> currentCourses, Student student) {
        for (int i = 0; i < currentCourses.size(); i++) {
            
            // Exclude NTE courses
            if (currentCourses.get(i).getCourseGroup().equals("NTE")) continue;

            // There is no 
            if (student.getCourseNote(currentCourses.get(i).getCourseName()) >= 0) continue;

            for (int j = i + 1; j < currentCourses.size(); j++) {
                int totalCollisionMinute = getTotalCollisionMinutes(currentCourses.get(i).getCourseSchedule(),
                        currentCourses.get(j).getCourseSchedule());

                // if there is any collision remove the course
                if (totalCollisionMinute > 60) {
                    currentCourses.remove(currentCourses.get(j--));
                }
            }
        }
    }

    private boolean systemCheck(Course newCourse, Student student) { // ERKAM

        if (student.getCourseNote(newCourse.getCourseName()) >= 1) return false;
        
        String prerequisiteCourse = newCourse.getPrerequisiteCourse();

        if (!prerequisiteCourse.equals("")) { // if there is a prerequisite course
            if (student.getTranscript().getCourseNote(prerequisiteCourse) < 1) { // if student could not pass prerequisite course
                return false;
            }
        }
        if (newCourse.getCourseQuota() != 0 && newCourse.getCourseQuota() <= newCourse.getNumberOfStudent()) { // if quota is not full
            return false;
        }

        return true;

    }

    private void courseRegistration() {
        this.students.forEach(student -> { // iterate through students
            if (student.getIsGraduate()) return;

            int currentSemester = Math.min(student.getCurrentSemester(), 8);

            TreeSet<Course> addableCourses = new TreeSet<Course>();

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

            int totalCredits = 0;
            ArrayList<Course> validCourses = new ArrayList<>();
            for (Course course: addableCourses) {
                if (validCourses.size() == 10) continue;
                if (totalCredits + course.getCourseCredits() > 40) continue;
                if (systemCheck(course, student)) { // student parameter added 
                    validCourses.add(course);
                    course.setNumberOfStudent(course.getNumberOfStudent() + 1);
                    totalCredits += course.getCourseCredits();
                }
            }
            advisorCheck(validCourses, student);

            if (validCourses.isEmpty() && student.getGPA()[0] < 2) validCourses.addAll(student.getConditionalCourses());
            
            student.addSemester(new Semester(validCourses));
        });
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
				continue;
            }

			if (courses.get(randomIndex).getCourseQuota() > courses.get(randomIndex).getNumberOfStudent()) {
                randomCourses.add(courses.get(randomIndex));
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

    private void createNewStudents(int numberOfStudent) {
        this.students.addAll(randomObjectGenerator.getRandomStudents(20 + currentSemester / 2));
    }

    private void finalPoints() {

        for (int i = 0; i < students.size(); i++) {

            if (students.get(i).getIsGraduate()) continue;

            TreeMap<String, Semester.letterNote> notes = students.get(i).getTranscript().getCurrentSemester().getNotes();

            for (Map.Entry<String, Semester.letterNote> note: notes.entrySet()){
                note.setValue(Semester.letterNote.values()[randomObjectGenerator.getBellRandom(0, 10)]);
            }

            students.get(i).updateGPA();
            students.get(i).setIsGradute();
        }         
    }

    private void resetCourseQuotas() {

        for (Course course: DataIOHandler.fallCourses) course.setNumberOfStudent(0);
        for (Course course: DataIOHandler.springCourses) course.setNumberOfStudent(0);

    }

    private void simulationLoop() {

        if (currentSemester % 2 == 1) createNewStudents(yearlyStudentCount);

        resetCourseQuotas();

        courseRegistration();

        finalPoints();

        this.currentSemester++;
    }
}
