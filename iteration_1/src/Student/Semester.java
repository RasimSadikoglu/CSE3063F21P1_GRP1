package Student;

import java.util.TreeMap;
import Course.Course;

public class Semester {

    /* Note Format:
    4.0 : AA
    3.5 : BA
    3.0 : BB
    2.5 : CB
    2.0 : CC
    1.5 : DC
    1.0 : DD
    0.5 : FD
    0.0 : FF
    -1  : DZ
    -2  : Not Finalized
    */

    private TreeMap<String, Float> notes;

    public Semester() {
        notes = new TreeMap<String, Float>();
    }

    public Semester(TreeMap<String, Float> notes) {
        this.notes = notes;
    }

    public void addNote(String course, float note){
        notes.put(course, note);  
    }

    public void addNewCourse(String course){
        notes.put(course, (float)-2);
    }

    public TreeMap<String, Float> getNotes() {
        return notes;
    }
}
