package Main;

import Util.DataIOHandler;

class Main {
	public static void main(String[] args) throws Exception {
	
		Simulation simulation = DataIOHandler.readSimulationParameters("jsonDocs/simulationParameters.json");

		simulation.setup();

		simulation.start();

		simulation.end();
	}
}