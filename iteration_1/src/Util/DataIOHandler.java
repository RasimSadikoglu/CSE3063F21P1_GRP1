package Util;

import java.io.File;
import java.io.FileWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import Course.Course;
import Main.Simulation;
import Student.Student;

public class DataIOHandler {

	static private Gson gson = new Gson();
	static public Course[] courses;
	public static String currentPath; // relative path to current directory
	static {
		try {
			currentPath = new File("./iteration_1").getCanonicalPath() + "/";
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	static { courses = readCourseInfo("jsonDocs/courses.json"); }

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

	/*
		Written for testing, reformat is needed. (BERK)
	*/
	static public void writeStudentsData(ArrayList<Student> students) {

		try {
			for (Student s: students) {

				FileWriter f = new FileWriter(currentPath + "jsonDocs/students/" + s.getId() + "t.json");
	
				Gson g = new GsonBuilder().setPrettyPrinting().create();
	
				g.toJson(s, f);
	
				f.close();
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

	static private void writeFile(String path, String data) {
		try {
			Path filePath = Paths.get(currentPath + path);
			Files.write(filePath, data.getBytes());
		} catch (Exception ex) {
			System.out.println(ex.toString());
		}
	}

}
