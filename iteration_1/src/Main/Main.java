package Main;

import Course.Course;
import Student.Student;
import Util.DataIOHandler;

class Main {
	public static void main(String[] args) {
		System.out.println("Hello World");

		// Test code snippet for Data IO:
		// ------------------------------------------------------------------
		Course[] courses = DataIOHandler.readCourseInfo("jsonDocs/courses.json");
		System.out.println(courses[0].getCourseName());

		Student testStudent = DataIOHandler.readStudentInfo("jsonDocs/testStudent.json");
		System.out.println(testStudent.getId());
		// ------------------------------------------------------------------
	}
}