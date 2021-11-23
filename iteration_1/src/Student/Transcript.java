package Student;

import java.util.ArrayList;
import java.util.Map;
import java.util.TreeMap;
import java.util.TreeSet;

import Course.Course;
import Course.Course.CourseGroup;
import Student.Semester.LetterNote;
import Util.DataIOHandler;

public class Transcript {
    
    private ArrayList<Semester> semesters;

    public Transcript() {
        semesters = new ArrayList<Semester>();
    }

    public void addSemester(Semester semester) {
        semesters.add(semester);
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

            TreeMap<String, LetterNote> notes = semesters.get(i).getNotes();

            for (Map.Entry<String, LetterNote> note: notes.entrySet()) {

                String courseName = note.getKey();

                float courseNote = note.getValue().getNote();

                if (courseNote == -2) continue;

                if (completedCourses.contains(courseName)) continue;

                completedCourses.add(courseName);

                float courseCredits = DataIOHandler.getInstance().getCourse(courseName).getCourseCredits();

                if (courseNote >= 1) completedCredits += courseCredits;

                totalCredits += courseCredits;

                points += courseNote == -1 ? 0 : courseCredits * courseNote;
            }
        }
        
        // GPA of a student with no completed course is 0.
        float gpa = totalCredits == 0 ? 0 : points / totalCredits;
        return new float[]{gpa, points, completedCredits, totalCredits};
    }

    public float getCourseNote(String courseName) {
        
        for (int i = semesters.size() - 1; i >= 0; i--) {
            
            TreeMap<String, LetterNote> notes = semesters.get(i).getNotes();

            if (notes.get(courseName) != null) return notes.get(courseName).getNote();
        }

        return -3;
    }

    public ArrayList<Course> getConditionalCourses() {

        Course[] courses;

        if (semesters.size() % 2 == 1) courses = DataIOHandler.getInstance().fallCourses;
        else courses = DataIOHandler.getInstance().springCourses;

        ArrayList<Course> conditionalCourses = new ArrayList<Course>();
        
        TreeSet<String> allCourses = new TreeSet<String>();

        for (int i = semesters.size() - 1; i >= 0; i--) {

            TreeMap<String, LetterNote> notes = semesters.get(i).getNotes();

            for (Map.Entry<String, LetterNote> note: notes.entrySet()) {

                String courseName = note.getKey();

                LetterNote courseNote = note.getValue();

                if (allCourses.contains(courseName)) continue;

                boolean isOpen = false;

                for (Course course: courses) {
                    if (course.getCourseName().equals(courseName)) {
                        isOpen = true;
                        break;
                    }
                }

                if (!isOpen) continue;

                if (courseNote == LetterNote.DC || courseNote == LetterNote.DD) conditionalCourses.add(DataIOHandler.getInstance().getCourse(courseName));

                allCourses.add(courseName);

            }

        }

        return conditionalCourses;

    }

    public int getCourseCount(CourseGroup courseGroup) {

        int count = 0;

        TreeSet<Course> allCourses = new TreeSet<Course>();

        for (int i = semesters.size() - 1; i >= 0; i--) {

            TreeMap<String, LetterNote> notes = semesters.get(i).getNotes();

            for (Map.Entry<String, LetterNote> note: notes.entrySet()) {

                Course course = DataIOHandler.getInstance().getCourse(note.getKey());

                if (allCourses.contains(course)) continue;

                if (note.getValue().getNote() < 1) continue;

                if (course.getCourseGroup() == courseGroup){ 
                    allCourses.add(course);
                    count++;
                }
                
            }

        }

        return count;
    }

    public ArrayList<Semester> getSemesters() {
        return semesters;
    }
}
