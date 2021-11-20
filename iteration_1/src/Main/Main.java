package Main;

import Util.DataIOHandler;

class Main {
	public static void main(String[] args) {
	
		Simulation simulation = DataIOHandler.readSimulationParameters("jsonDocs/simulationParameters.json");

		simulation.setup();

		simulation.start();

		simulation.end();
	}
}