package Student;

import java.util.ArrayList;
import java.util.Map;
import java.util.TreeMap;
import java.util.TreeSet;

import Course.Course;
import Student.Semester.letterNote;
import Util.DataIOHandler;

public class Transcript {
    
    private ArrayList<Semester> semesters;

    public Transcript() {
        semesters = new ArrayList<Semester>();
    }

    public Transcript(ArrayList<Semester> semesters) {
        this.semesters = semesters;
    }

    public void addSemester(Semester semester) {
        semesters.add(semester);
    }

    public void addCourse(Course course) {
        semesters.get(semesters.size() - 1).addNewCourse(course.getCourseName());
    }

    public Semester getCurrentSemester() {
        if (semesters.size() == 0) {
            throw new IndexOutOfBoundsException("Failed to obtain current semester information! (Semester array is empty!)");
        }

        return semesters.get(semesters.size() - 1);
    }

    public float getGPA() {

        float point = 0, totalCredits = 0;
        
        /* 
        * Students can take a course more than one time. However, only the last 
        * time they took the course effects the gpa. Traversing semesters array
        * in the reverse order ensures this. Also keeping courses inside a set
        * prevents a course from effecting gpa twice.
        */ 

        TreeSet<String> completedCourses = new TreeSet<String>();

        for (int i = semesters.size() - 1; i >= 0; i--) {
            TreeMap<String, letterNote> notes = semesters.get(i).getNotes();

            for (Map.Entry<String, letterNote> note: notes.entrySet()) {

                String courseName = note.getKey();

                float courseNote = note.getValue().getNote();

                if (courseNote == -2) continue;

                if (completedCourses.contains(courseName)) continue;

                completedCourses.add(courseName);

                float courseCredits = getCourse(courseName).getCourseCredits();

                point += courseNote == -1 ? 0 : courseCredits * courseNote;

                totalCredits += courseCredits;
            }
        }
        
        // GPA of a student with no completed course is 0.
        return totalCredits == 0 ? 0 : point / totalCredits;
    }

    public ArrayList<Semester> getSemesters() {
        return semesters;
    }

    private Course getCourse(String courseName) {

        for (Course course: DataIOHandler.courses) {
            if (course.getCourseName().equals(courseName)) return course;
        }

        return null;
    }

    public float getCourseNote(String courseName) { // ERKAM
        return 0;
    }

    public ArrayList<Course> getfailedCourses() { // RASIM
        return null;
    }
}
