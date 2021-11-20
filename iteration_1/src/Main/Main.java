package Main;

import Util.DataIOHandler;

class Main {
	public static void main(String[] args) throws Exception {
	
		// Don't forget to set correct paths for json files (Simulation.getData() method and the line below).
		Simulation simulation = DataIOHandler.readSimulationParameters("jsonDocs/simulationParameters.json");

		simulation.setRandomObjectGenerator();

		simulation.getData();

		simulation.start();

		System.out.println("Simulation has been ended!");
	}
}