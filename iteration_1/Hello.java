import java.util.ArrayList;

import Courses.Course;
import Util.DataHandler;

class Hello {
    public static void main (String[] args) {
        System.out.println("Hello World");
        Course cs = new Course("b", "c", 2);
        ArrayList<Course> a = new ArrayList<>();
        a.add(cs);
        DataHandler<ArrayList<Course>> dh = new DataHandler<>();
        dh.writeToJson(a, ".", "Courses.json"); 
        System.out.println(dh.parseFromJson(".", "Courses.json").get(0));
    }
}