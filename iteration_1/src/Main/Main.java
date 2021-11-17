package Main;

import Util.DataIOHandler;

class Main {
	public static void main(String[] args) throws Exception {
		System.out.println("Hello World");
		
		// Don't forget to set correct paths for json files (Simulation.getData() method and the line below).
		Simulation sim = DataIOHandler.readSimulationParameters("jsonDocs/simulationParameters.json");

		sim.setRandomObjectGenerator();

		sim.getData();

		sim.start();

		sim.end();
	}
}