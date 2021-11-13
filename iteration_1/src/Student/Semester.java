package Student;

import java.util.TreeMap;

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

    public Semester() {
        notes = new TreeMap<String, letterNote>();
    }

    public Semester(TreeMap<String, letterNote> notes) {
        this.notes = notes;
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
}
