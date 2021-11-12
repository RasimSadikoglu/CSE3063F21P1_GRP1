package Util;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import com.google.gson.Gson;

import Course.Course;
import Main.Simulation;
import Student.Student;

public class DataIOHandler {

	static private Gson gson = new Gson();

	static public Course[] readCourseInfo(String path) {
		String coursesStr = readFile(path);
		Course[] courses = gson.fromJson(coursesStr, Course[].class);
		return courses;
	}

	static public Student readStudentInfo(String studentName) {
		String studentStr = readFile(studentName);
		return gson.fromJson(studentStr, Student.class);
	}

	static public Simulation readSimulationParameters(String path) {
		String simulation = readFile(path);
		return gson.fromJson(simulation, Simulation.class);
	}

	static public void exportStudentInfo(String studentName, String studentData) {
		writeFile(studentName, studentData);
	}

	static private String readFile(String path) {
		try {
			Path filePath = Paths.get(path);
			byte[] data = Files.readAllBytes(filePath);
			return new String(data, "UTF-8");
		} catch (Exception ex) {
			System.out.println(ex.toString());
		}

		return null;
	}

	static private void writeFile(String path, String data) {
		try {
			Path filePath = Paths.get(path);
			Files.write(filePath, data.getBytes());
		} catch (Exception ex) {
			System.out.println(ex.toString());
		}
	}

}
