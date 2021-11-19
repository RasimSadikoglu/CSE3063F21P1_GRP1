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
    /**
     * @return [gpa, total points]
     */
    public float[] getGPA() {

        float points = 0, totalCredits = 0;
        
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

                points += courseNote == -1 ? 0 : courseCredits * courseNote;

                totalCredits += courseCredits;
            }
        }
        
        // GPA of a student with no completed course is 0.
        float gpa = totalCredits == 0 ? 0 : points / totalCredits;
        return new float[]{gpa, points};
    }

    public ArrayList<Semester> getSemesters() {
        return semesters;
    }

    private Course getCourse(String courseName) {

        for (Course course: DataIOHandler.fallCourses) {
            if (course.getCourseName().equals(courseName)) return course;
        }

        for (Course course: DataIOHandler.springCourses) {
            if (course.getCourseName().equals(courseName)) return course;
        }

        return null;
    }

    public float getCourseNote(String courseName) {
        
        for (int i = semesters.size() - 1; i >= 0; i--) {
            
            TreeMap<String, letterNote> notes = semesters.get(i).getNotes();

            for (Map.Entry<String, letterNote> note: notes.entrySet()) {

                String cName = note.getKey();

                float courseNote = note.getValue().getNote();

                if (cName.equals(courseName)) return courseNote;
            }
        }

        return -3;
    }

    public ArrayList<Course> getfailedCourses(boolean getAll) {

        Course[] courses;

        if (semesters.size() % 2 == 0) courses = DataIOHandler.fallCourses;
        else courses = DataIOHandler.springCourses;

        ArrayList<Course> failedCourses = new ArrayList<Course>();
        
        TreeSet<String> allCourses = new TreeSet<String>();

        for (int i = semesters.size() - 1; i >= 0; i--) {

            TreeMap<String, letterNote> notes = semesters.get(i).getNotes();

            for (Map.Entry<String, letterNote> note: notes.entrySet()) {

                String courseName = note.getKey();

                float courseNote = note.getValue().getNote();

                if (allCourses.contains(courseName)) continue;

                boolean isOpen = getAll;

                if (!isOpen) for (Course course: courses) {
                    if (course.getCourseName().equals(courseName)) {
                        isOpen = true;
                        break;
                    }
                }

                if (!isOpen) continue;

                if (courseNote < 1f) failedCourses.add(getCourse(courseName));

                allCourses.add(courseName);

            }

        }

        return failedCourses;

    }

    public ArrayList<Course> getConditionalCourses() {

        Course[] courses;

        if (semesters.size() % 2 == 1) courses = DataIOHandler.fallCourses;
        else courses = DataIOHandler.springCourses;

        ArrayList<Course> conditionalCourses = new ArrayList<Course>();
        
        TreeSet<String> allCourses = new TreeSet<String>();

        for (int i = semesters.size() - 1; i >= 0; i--) {

            TreeMap<String, letterNote> notes = semesters.get(i).getNotes();

            for (Map.Entry<String, letterNote> note: notes.entrySet()) {

                String courseName = note.getKey();

                letterNote courseNote = note.getValue();

                if (allCourses.contains(courseName)) continue;

                boolean isOpen = false;

                for (Course course: courses) {
                    if (course.getCourseName().equals(courseName)) {
                        isOpen = true;
                        break;
                    }
                }

                if (!isOpen) continue;

                if (courseNote == letterNote.DC || courseNote == letterNote.DD) conditionalCourses.add(getCourse(courseName));

                allCourses.add(courseName);

            }

        }

        return conditionalCourses;

    }
}
