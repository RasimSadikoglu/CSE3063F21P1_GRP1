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
     * @return [gpa, points, completedCredits, totalCredits]
     */
    public float[] getGPA() {

        float points = 0, completedCredits = 0, totalCredits = 0;
        
        /* 
        * Students can take a course more than one time. However, only the last 
        * time they took the course effects the gpa. Traversing semesters array
        * in the reverse order ensures this. Also keeping courses inside a set
        * prevents a course from effecting gpa twice.
        */ 

        TreeSet<String> completedCourses = new TreeSet<String>();

        for (int i = semesters.size() - 1; i >= 0; i--) {
            semesters.get(i).updateSemesterInfo();

            TreeMap<String, letterNote> notes = semesters.get(i).getNotes();

            for (Map.Entry<String, letterNote> note: notes.entrySet()) {

                String courseName = note.getKey();

                float courseNote = note.getValue().getNote();

                if (courseNote == -2) continue;

                if (completedCourses.contains(courseName)) continue;

                completedCourses.add(courseName);

                float courseCredits = DataIOHandler.getCourse(courseName).getCourseCredits();

                if (courseNote >= 1) completedCredits += courseCredits;

                totalCredits += courseCredits;

                points += courseNote == -1 ? 0 : courseCredits * courseNote;
            }
        }
        
        // GPA of a student with no completed course is 0.
        float gpa = totalCredits == 0 ? 0 : points / totalCredits;
        return new float[]{gpa, points, completedCredits, totalCredits};
    }

    public ArrayList<Semester> getSemesters() {
        return semesters;
    }

    public float getCourseNote(String courseName) {
        
        for (int i = semesters.size() - 1; i >= 0; i--) {
            
            TreeMap<String, letterNote> notes = semesters.get(i).getNotes();

            if (notes.get(courseName) != null) return notes.get(courseName).getNote();
        }

        return -3;
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

                if (courseNote == letterNote.DC || courseNote == letterNote.DD) conditionalCourses.add(DataIOHandler.getCourse(courseName));

                allCourses.add(courseName);

            }

        }

        return conditionalCourses;

    }

    public int getCourseCount(String courseGroup) {

        int count = 0;

        TreeSet<Course> allCourses = new TreeSet<Course>();

        for (int i = semesters.size() - 1; i >= 0; i--) {

            TreeMap<String, letterNote> notes = semesters.get(i).getNotes();

            for (Map.Entry<String, letterNote> note: notes.entrySet()) {

                Course course = DataIOHandler.getCourse(note.getKey());

                if (allCourses.contains(course)) continue;

                if (note.getValue().getNote() < 1) continue;

                if (course.getCourseGroup().equals(courseGroup)) count++;

            }

        }

        return count;
    }
}
