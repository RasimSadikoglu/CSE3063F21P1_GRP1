package Main;

import Course.Course;
import Student.Student;
import Util.DataIOHandler;

class Main {
	public static void main(String[] args) {
		System.out.println("Hello World");

		// Test code snippet for Data IO:
		// ------------------------------------------------------------------
		Course[] courses = DataIOHandler.readCourseInfo("C:\\Users\\furka\\Desktop\\oop\\CSE3063F21P1_GRP1\\iteration_1\\jsonDocs\\courses.json");
		System.out.println(courses[0].getCourseName());

		Student testStudent = DataIOHandler.readStudentInfo("C:\\Users\\furka\\Desktop\\oop\\CSE3063F21P1_GRP1\\iteration_1\\jsonDocs\\testStudent.json");
		System.out.println(testStudent.getId());
		// ------------------------------------------------------------------
	}
}