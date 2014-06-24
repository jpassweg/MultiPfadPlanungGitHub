/**
 * 
 * @author Jonas Passweg
 * @version 1.3
 * 
 * The GRID class
 * Contents the Grid itself which can be accessed by any other class
 * 
 */
public class Grid {
		//Grid as a 2D-Array
		public static String[][] GRID;
		
		/** 
		 * Initialisation of the Grid
		 * 
		 * @param x width of Grid that should be created
		 * @param y height of Grid that should be created
		 */
		public void createGrid(int x, int y) {
			GRID = new String[x][y];
			
			//fills the Grid with O's
			for(int i = 0; i < GRID[0].length; i++) {	//goes through y coordinates of Grid
				for(int j = 0; j < GRID.length; j++) {	//goes through x coordinates of Grid
					GRID[j][i] = "O";					//fill specific field with O
				}
			}
		}
		
		/** 
		 * starting Simulation GUI - see main for boolean explanations
		 * 
		 * @param main if main windows should be displayed
		 * @param algo if algorithm windows should be displayed
		 */
		public void startSimulation(boolean main, boolean algo) {
			GUI.startGUISimulation(GRID, main, algo);
		}
		
		/** 
		 * refreshing Simulation
		 */
		public void refreshSimulation() {
			GUI.refreshGUISimulation(GRID);
		}
		
		/**
		 * disposing Simulation of Algorithm
		 */
		public static void disposeAlgorithmSimulation() {
			GUI.disposeAlgorithm();
		}
}
