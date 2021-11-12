package Main;

import Course.Course;
import Student.Student;
import Util.DataIOHandler;

class Main {
	public static void main(String[] args) {
		System.out.println("Hello World");
		
		// Don't forget to set correct paths for json files (Simulation.getData() method and the line below).

		Simulation sim = DataIOHandler.readSimulationParameters("/home/rasim/Yandex.Disk/Files/Workspaces/CSE3063F21P1_GRP1/iteration_1/jsonDocs/simulationParameters.json");

		sim.getData();

		sim.start();
	}
}