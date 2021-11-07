package Student;
import java.util.*;
import Course.Course;

public class Semester {

    private Map<Course, Float> notes=new TreeMap<Course, Float>();

    public Semester() {
        notes = new TreeMap<Course, Float>();
    }

    public void addNote(Course course, float note){
        notes.put(course,note);  
    }

    public void addNewCourse(Course course){
        notes.put(course,(float) -2);
    }

    public Map<Course, Float> getNotes() {
        return notes;
    }
}
