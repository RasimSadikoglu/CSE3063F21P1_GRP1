package Util;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import Course.Course;
import Main.Simulation;
import Student.Student;

public class DataIOHandler {

	static private Gson gson = new GsonBuilder().setPrettyPrinting().create();
	static public Course[] fallCourses;
	static public Course[] springCourses;
	static public String currentPath; // relative path to current directory
	static {
		try {
			currentPath = new File("./iteration_1").getCanonicalPath() + "/";
		} catch (Exception e) {
			e.printStackTrace();
		}
		fallCourses = readCourseInfo("jsonDocs/fallCourses.json");
		springCourses = readCourseInfo("jsonDocs/springCourses.json");
	}

	static public Course[] readCourseInfo(String path) {
		String coursesStr = readFile(path);
		Course[] courses = gson.fromJson(coursesStr, Course[].class);
		return courses;
	}

	static public Course getCourse(String courseName) {

        for (Course course: DataIOHandler.fallCourses) {
            if (course.getCourseName().equals(courseName)) return course;
        }

        for (Course course: DataIOHandler.springCourses) {
            if (course.getCourseName().equals(courseName)) return course;
        }

        return null;
    }

	static public Student readStudentInfo(String studentName) {
		String studentStr = readFile(studentName);
		return gson.fromJson(studentStr, Student.class);
	}

	static public Simulation readSimulationParameters(String path) {
		String simulation = readFile(path);
		return gson.fromJson(simulation, Simulation.class);
	}

	static public void exportStudentInfo(Student student, String path) {
		String studentData = gson.toJson(student);
		writeStudentInfo(path, studentData);
	}

	static private void writeStudentInfo(String path, String studentData) {
		writeFile(path, studentData, false);
	}

	static public ArrayList<Student> readStudentsData(String path) {

		ArrayList<Student> students = new ArrayList<Student>();

		File[] studentFiles = new File(DataIOHandler.currentPath + path).listFiles();

        if (students == null || studentFiles.length == 0) return students;

        for (File student: studentFiles) {
            Student newStudent = DataIOHandler.readStudentInfo(path + student.getName());

            newStudent.updateCurrentSemester();

            students.add(newStudent);
        }

		return students;

	}

	static public void writeStudentsData(ArrayList<Student> students, String path) {

		try {
			for (Student student : students) {
				exportStudentInfo(student, currentPath + path + student.getId() + ".json");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	static private String readFile(String path) {
		try {
			Path filePath = Paths.get(currentPath + path);
			byte[] data = Files.readAllBytes(filePath);
			return new String(data, "UTF-8");
		} catch (Exception ex) {
			System.out.println(ex.toString());
		}

		return null;
	}

	static public void writeFile(String path, String data, boolean append) {
		try {
			Path filePath = Paths.get(path);
			Files.write(filePath, data.getBytes(), StandardOpenOption.CREATE, StandardOpenOption.WRITE, append ? StandardOpenOption.APPEND : StandardOpenOption.TRUNCATE_EXISTING);
		} catch (Exception ex) {
			System.out.println(ex.toString());
		}
	}

	static public void resetStudentData(boolean deleteBefore) {

		if (new File(DataIOHandler.currentPath + "jsonDocs/students/").listFiles() == null) {
			new File(DataIOHandler.currentPath + "jsonDocs/students/").mkdir();
		}

		File[] afterDirectory = new File(DataIOHandler.currentPath + "jsonDocs/students/after/").listFiles();

		if (afterDirectory == null) new File(DataIOHandler.currentPath + "jsonDocs/students/after/").mkdir();
		else for (File student: afterDirectory) {
			student.delete();
		}

		File[] beforeDirectory = new File(DataIOHandler.currentPath + "jsonDocs/students/before/").listFiles();

		if (beforeDirectory == null) new File(DataIOHandler.currentPath + "jsonDocs/students/before/").mkdir();
		else if (deleteBefore) for (File student: beforeDirectory) {
			student.delete();
		}

	}

}
