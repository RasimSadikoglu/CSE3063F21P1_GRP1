package Student;

import java.util.ArrayList;
import java.util.Map;
import java.util.TreeMap;

import Course.Course;
import Util.DataIOHandler;

public class Semester {

    public static enum letterNote {
		AA(4f), BA(3.5f), BB(3f), CB(2.5f),
        CC(2f), DC(1.5f), DD(1f), FD(0.5f),
        FF(0f), DZ(-1f), NF(-2f); // NF = Not Finalized

		private final float note;
		private letterNote(float note) { this.note = note; }
		public float getNote() { return note; } 
	}

    private TreeMap<String, letterNote> notes;
    private float semesterGPA;
    private float points;
    private float completedCredits;
    private float totalCredits;

    public Semester() {
        semesterGPA = 0;
        points = 0;
        completedCredits = 0;
        totalCredits = 0;
        notes = new TreeMap<String, letterNote>();
    }

    public Semester(TreeMap<String, letterNote> notes) {
        this.notes = notes;
    } 
    
    public Semester(ArrayList<Course> courses) {
        notes = new TreeMap<String, letterNote>();
        courses.forEach(course -> {
            notes.put(course.getCourseName(), letterNote.NF);
        });
    }

    public void addNote(String courseName, letterNote note){
        notes.put(courseName, note);  
    }

    public void addNewCourse(String courseName){
        notes.put(courseName, letterNote.NF);
    }

    public TreeMap<String, letterNote> getNotes() {
        return notes;
    }

    public void updateSemesterInfo() {

        if (notes.isEmpty()) return;

        semesterGPA = 0;
        points = 0;
        completedCredits = 0;
        totalCredits = 0;

        for (Map.Entry<String, letterNote> note: notes.entrySet()) {
            
            float courseCredits = DataIOHandler.getCourse(note.getKey()).getCourseCredits();

            if (note.getValue().getNote() >= 1) completedCredits += courseCredits;

            totalCredits += courseCredits;

            points += note.getValue().getNote() == -1 ? 0 : courseCredits * note.getValue().getNote();

        }

        semesterGPA = points / totalCredits;

    }
}
