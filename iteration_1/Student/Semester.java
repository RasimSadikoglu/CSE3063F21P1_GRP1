package Student;
import java.util.*;
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

    private TreeMap<Course, Float> notes;

    public Semester() {
        notes = new TreeMap<Course, Float>();
    }

    public void addNote(Course course, float note){
        notes.put(course, note);  
    }

    public void addNewCourse(Course course){
        notes.put(course, (float)-2);
    }

    public TreeMap<Course, Float> getNotes() {
        return notes;
    }
}
