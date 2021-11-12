package Main;

import java.util.ArrayList;
import Course.Course;
import Student.Student;

public class Simulation {
    private ArrayList<Student> students;
    private ArrayList<Course> courses;
    int semester;

    public Simulation(){
        students = new ArrayList<Student>();
        courses = new ArrayList<Course>();
        semester = 0;
    }

    public Simulation(ArrayList<Student> students, ArrayList<Course> courses, int semester ){
        this.students = students;
        this.courses = courses;
        this.semester = semester;
    }

    public void advisorCheck(){
        
    }

    public void systemCheck(){
    
    }

    public void SimulationLoop(){
        
    }
}



