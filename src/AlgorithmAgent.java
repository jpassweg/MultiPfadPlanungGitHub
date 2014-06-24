/**
 * 
 * @author Jonas Passweg
 * @version 1.5
 * 
 * This class will mostly consists of an approach to the A*-algorithm to use its function
 * to get the Agents from point A to B the fastest way. In this class the agents dont communicate
 * with another and see other agents are non-moving obstacles. Therefore the path need to updated after
 * every turn (online).
 * 
 * Brief explanation of lists:
 * openList: points which are adjacent to points already looked at but were not yet looked at
 * closeList: points which were already looked at
 * 
 * Brief explanation of whole algorithm:
 * A path-finding algorithm which works with the basic principle that instead of after every new examined field
 * looking at each surrounded fields it only looks at the next best field which is mathematically calculated
 * by the following equation: F = G + H, where:
 * 	- G = the movement cost to move from the starting point A to a given square on the grid, following the path 
 * 	  generated to get there. 
 * 	- H = the estimated movement cost to move from that given square on the grid to the final destination, point B. 
 * 	  This is often referred to as the heuristic, because it's only a guess.
 * The one with the lowest score of all fields that surrounded already examined fields is now looked at. To have an
 * overview of all possible next Fields, these are saved in openList whereas already examined Fields are saved in
 * closeList and are removed from the openList.
 * 
 * not commented enough
 * 
 */
public class AlgorithmAgent {
	public static int ID;	//ID
	
	public static int Sx;	//x-coordination start
	public static int Sy;	//y-coordination start
	
	public static int Lx;	//x-coordination location
	public static int Ly;	//y-coordination location
	public static int LocX;	//x-coordination location (which will be changed over process)
	public static int LocY;	//y-coordination location (which will be changed over process)
	
	public static int Gx;	//x-coordination goal
	public static int Gy;	//y-coordination goal
	
	//These lists contain the x and y coordinations of points
	public static int[][] openList;		//openlist
	//A list with all possible fields that can potentially bring to the goal (the margin fields)
	public static int[][] closeList;	//closelist
	//A list with all fields that were already examined and are not the goal location
	
	public static boolean foundGoal;			//if agent found his goal
	public static int defaultListNumber;		//the biggest a list can be
	public static String[][] gr;				//a clone of the grid to work with
	public static int wait = Main.wait;			//number of milliseconds to wait between each move
	
	/**
	 * This is the main method of this class
	 * it controls the deciding of the next movement of an agent
	 * 
	 * param are all explained by the variables
	 * @param iD
	 * @param sx
	 * @param sy
	 * @param lx
	 * @param ly
	 * @param gx
	 * @param gy
	 * @return the action of the agent
	 */
	public static Object decide(int[] agent) {
		
		//Cloning the Main Grid on gr
		gr = new String[Grid.GRID.length][Grid.GRID[0].length]; //grid for algorithm
		for(int i = 0; i < Grid.GRID.length; i++) {				//goes through x-coordinates
			for(int j = 0; j < Grid.GRID[0].length; j++) {		//goes through y-coordinates
				gr[i][j] = Grid.GRID[i][j];						//cloning each field
			}
		}
		
		//Cloning the variables
		ID = agent[0]; Sx = agent[1]; Sy = agent[2]; Lx = agent[3]; Ly = agent[4]; Gx = agent[5]; Gy = agent[6];
		
		LocX = Lx;		//x-location of Agent
		LocY = Ly;		//y-location of Agent
		
		//starting of A*-Algorithm (online/without communication)
		return startAStarAlgorithm();
	}

	/**
	 * Now the Algorithm is fixed to A*-Algorithm
	 * 
	 * @return action of agent
	 */
	private static Object startAStarAlgorithm() {
		//initiation of Lists which are needed for A* (open- and closeList)
		initLists(gr);	
		
		/* refreshing Simulation
		 * GUI Algorithm contains 3 frames:
		 */
		GUI.refreshGUIAlgorithm(gr, closeList);	//main frame with simulation of algorithm on Grid
		GUI.refreshGUIOpenList(openList);		//listing of fields in openList
		GUI.refreshGUICloseList(closeList);		//listing of fields in closeList
		
		//checking if other agent is on own goal (when true moving goal to new free location)
		checkGoal();
		
		//searching Best path and giving an action back
		return searchBestPath(gr);
	}

	private static void initLists(String[][] gr) {
		//creating the two lists and filling them with defaulfListNumber (because no coordinate can be that high ^^)
		defaultListNumber = gr.length * gr[0].length;
		
		//initiation of lists
		//they need pro index two fields to save the x- and y-coordination
		openList = new int[defaultListNumber][2];
		closeList = new int[defaultListNumber][2];
		
		//the lists are here filled with defaultnumber to not be confused with already used indexes of the list
		for(int i = 0; i < openList.length; i++) {
			openList[i][0] = defaultListNumber;
			openList[i][1] = defaultListNumber;
			
			closeList[i][0] = defaultListNumber;
			closeList[i][1] = defaultListNumber;
		}
	}
	
	/**
	 * This method is only here to look if an agent is on the goal location of another agent.
	 * In this case a temporary goal location needs to be placed as near as possible to the other goal
	 */
	private static void checkGoal() {
		//distance between goal and new temporary goal (distance calculated only north, east, west or soutg of goal)
		int range = 1;
		
		//while goal was not found agent needs to search a new one
		boolean found = false;
		
		//if goal location already is free the agent can find it's goal
		if(Grid.GRID[Gx][Gy] == "O" || (Lx==Gx && Ly==Gy)) {
			found = true;
		}
		
		//while appropriate goal location was not found
		while(!found) {
			//j is the range left and right of the new ranged goal
			//this is done to also get possible new diagonal goals
			for(int j = 0; j <= range; j++) {
				//this needs to be done so that the agent is sure that it's goal location is on the Grid and free
				if(Gx-range >= 0) {
					if(Gy+j < gr.length && Grid.GRID[Gx-range][Gy+j].substring(0, 1) == "O") {
						Gx -= range; Gy -=j; found = true; break; }; 
						//if found the agent can break it's search
					if(Gy-j >= 0 && Grid.GRID[Gx-range][Gy-j].substring(0, 1) == "O") {
						Gx -= range; Gy -=j; found = true; break; };
						//if found the agent can break it's search
				} if(Gx+range < gr.length) {
					if(Gy+j < gr.length && Grid.GRID[Gx+range][Gy+j].substring(0, 1) == "O") {
						Gx += range; Gy -=j; found = true; break; };
						//if found the agent can break it's search
					if(Gy-j >= 0 && Grid.GRID[Gx+range][Gy-j].substring(0, 1) == "O") {
						Gx += range; Gy -=j; found = true; break; };
						//if found the agent can break it's search
				} if(Gx-j >= 0) {
					if(Gy-range >= 0 && Grid.GRID[Gx-j][Gy-range].substring(0, 1) == "O") {
						Gy -= range; Gx -=j; found = true; break; };
						//if found the agent can break it's search
					if(Gy+range < gr.length && Grid.GRID[Gx-j][Gy+range].substring(0, 1) == "O") {
						Gy += range; Gx -=j; found = true; break; };
						//if found the agent can break it's search
				} if(Gx+j < gr.length) {
					if(Gy-range >= 0 && Grid.GRID[Gx+j][Gy-range].substring(0, 1) == "O") {
						Gy -= range; Gx -=j; found = true; break; };
						//if found the agent can break it's search
					if(Gy+range < gr.length && Grid.GRID[Gx+j][Gy+range].substring(0, 1) == "O") {
						Gy += range; Gx -=j; found = true; break; };
						//if found the agent can break it's search
				}
			}
			//range get's bigger every time an agent can't find it's goal in the smaller range
			range++;
		}
		
	}
	
	/**
	 * This method searches the best path of one agent
	 * @param gr
	 * @return
	 */
	private static Object searchBestPath(String[][] gr) {
		//We assume that until now the goal location was not found
		foundGoal = false;
		
		//If the agent if already on its goal location nothing needs to be done
		if(LocX == Gx && LocY == Gy) foundGoal = true;
		
		//while the agent didn't found the goal it continuous to search
		while(foundGoal == false) {
			/* When every field was examined and no goal was found a funny bug happens that LocX and LocY
			* become the defaultListNumber. In this case only an approach to the goal can be made. If not 
			* there are still unexamined fields left and the algorithm can continue normally by refilling 
			* the lists.
			*/ 
			if(LocX != defaultListNumber && LocY != defaultListNumber) {
				gr = fillList(gr);			//filling list
			} else {
				return approachToGoal();	//approaching to goal location without full path order
			}
			
			//if goal is still not found we need to search for the next best field
			if(!foundGoal) searchForNextBestField();
			
			//refreshing simulation
			GUI.refreshGUIAlgorithm(gr, closeList);
			
			//if algorithm is not shown you don't need to wait between each stpes
			if(Main.algo) try {Thread.sleep(wait);} catch (InterruptedException e) {e.printStackTrace();}
		}
		//evaluating of Grid to get the best move
		return evaluateGrid();
	}

	/**
	 * If there is no possible way to the goal location the agent should at least try
	 * to approach the goal location
	 * @return
	 */
	private static Object approachToGoal() {
		//first we calculate the distance between goal location and the surrounding locations of the current location
		int north = Math.abs(((Lx) - Gx)) + Math.abs(((Ly - 1) - Gy));
		int south = Math.abs(((Lx) - Gx)) + Math.abs(((Ly + 1) - Gy));
		int west = Math.abs(((Lx - 1) - Gx)) + Math.abs(((Ly) - Gy));
		int east = Math.abs(((Lx + 1) - Gx)) + Math.abs(((Ly) - Gy));
		//saving them in an array
		int array[] = {north, south, west, east};
		
		//smallest distance will be the location in the array with the index = index
		int index = 0;
		//the comparison value of the array with index = index
		int min = array[index];
		//if another surrounding location is nearer to the goal than the one with index = index the index changes to j
		for(int j = 1; j < array.length; j++) {
			if(array[j] < min) {
				//if there is a nearer location the comparison value as well as the index changes
				min = array[j];
				index = j;
			}	
		}
		
		/* After a lot of tryings I came to the point that it would be best that in such case the agent should
		 * either go in the direction that it needs to or if this is not available in the two locations diagonal.
		 * I don't know really why but this worked best for all tested examples (the only thing is that agents don't
		 * tend to wait even if it would spare one move) 
		 */
		switch (index) {
			case 0:
				//We also always need to check if the potential next location is free
				//if index is 0 the way is north
				if(Ly-1 >= 0 && Grid.GRID[Lx][Ly-1] == "O") {
					return action.MOVE_NORTH;
					//or west
				} else if(Lx-1 >= 0 && Grid.GRID[Lx-1][Ly] == "O") {
					return action.MOVE_WEST;
					//or east
				} else if(Lx+1 < Grid.GRID.length && Grid.GRID[Lx+1][Ly] == "O") {
					return action.MOVE_EAST;
				}
			case 1:
				//if index is 1 the way is south
				if(Ly+1 < Grid.GRID[0].length && Grid.GRID[Lx][Ly+1] == "O") {
					return action.MOVE_SOUTH;
					//or west
				} else if(Lx-1 >= 0 && Grid.GRID[Lx-1][Ly] == "O") {
					return action.MOVE_WEST;
					//or east
				} else if(Lx+1 < Grid.GRID.length && Grid.GRID[Lx+1][Ly] == "O") {
					return action.MOVE_EAST;
				}
			case 2:
				//if index is 2 the way is west
				if(Lx-1 >= 0 && Grid.GRID[Lx-1][Ly] == "O") {
					return action.MOVE_WEST;
					//or south
				} else if(Ly+1 < Grid.GRID[0].length && Grid.GRID[Lx][Ly+1] == "O") {
					return action.MOVE_SOUTH;
					//or north
				} else if(Ly-1 >= 0 && Grid.GRID[Lx][Ly-1] == "O") {
					return action.MOVE_NORTH;
				} 
			case 3:
				//if index is 3 the way is east
				if(Lx+1 < Grid.GRID.length && Grid.GRID[Lx+1][Ly] == "O") {
					return action.MOVE_EAST;
					//or south
				} else if(Ly+1 < Grid.GRID[0].length && Grid.GRID[Lx][Ly+1] == "O") {
					return action.MOVE_SOUTH;
					//or north
				} else if(Ly-1 >= 0 && Grid.GRID[Lx][Ly-1] == "O") {
					return action.MOVE_NORTH;
				}
			default:
				//in any starnge case the agent needs to wait
				return action.WAIT;
			}
	}

	/**
	 * filling the lists
	 * @param gr
	 * @return
	 */
	public static String[][] fillList(String[][] gr) {
		//when filling new lists first the actual location needs to be added to the closeList
		addLocToClosedList(LocX, LocY);
		//then the locations surrounding the new location need to be added to the openList
		gr = fillOpenList(gr);
		
		//and refreshing the GUI would also be great
		GUI.refreshGUICloseList(closeList);
		GUI.refreshGUIOpenList(openList);
		
		//and returning the new grid with the filled lists
		//(which are also added to the Grid gr)
		return gr;
	}
	
	/**
	 * filling the openlist
	 * @param gr
	 * @return
	 */
	private static String[][] fillOpenList(String[][] gr) {
		//filling openlist with field on the right
		if(LocX+1 < gr.length && gr[LocX+1][LocY] == "O") { //looking if this field is free
			gr[LocX+1][LocY] = "W"; //placing W because the agent path comes from west
			if(LocX+1 == Gx && LocY == Gy ) { //if agent is already at goal the goal was found
				foundGoal = true;	//so we don't need to continue to search
				LocX++;				//but we still need to update the location
			}
			addLocToOpenList(LocX + 1, LocY); 	//after checking all things we add the new possible location
											 	//to openList
		//filling openList with field on the left
		} if(LocX-1 >= 0 && gr[LocX-1][LocY] == "O") { //looking if this field is free
			gr[LocX-1][LocY] = "E"; //placing E because the agent path comes from east
			if(LocX-1 == Gx && LocY == Gy ) { //if agent is already at goal the goal was found
				foundGoal = true;	//so we don't need to continue to search
				LocX--;				//but we still need to update the location
			}
			addLocToOpenList(LocX - 1, LocY);	//after checking all things we add the new possible location
		 										//to openList
		//filling openList with field below
		} if(LocY+1 < gr[0].length && gr[LocX][LocY+1] == "O") { //looking if this field is free
			gr[LocX][LocY+1] = "N"; //placing N because the agent path comes from north
			if(LocX == Gx && LocY+1 == Gy ) { //if agent is already at goal the goal was found
				foundGoal = true;	//so we don't need to continue to search
				LocY++;				//but we still need to update the location
			}
			addLocToOpenList(LocX, LocY + 1);	//after checking all things we add the new possible location
		 										//to openList
			//filling openList with field above
		} if(LocY-1 >= 0 && gr[LocX][LocY-1] == "O") { //looking if this field is free
			gr[LocX][LocY-1] = "S"; //placing S because the agent path comes from south
			if(LocX == Gx && LocY-1 == Gy ) { //if agent is already at goal the goal was found
				foundGoal = true;	//so we don't need to continue to search
				LocY--;				//but we still need to update the location
			}
			addLocToOpenList(LocX, LocY - 1);	//after checking all things we add the new possible location
		 										//to openList
		}
		return gr;	//returning gr
	}
	
	/**
	 * add specific location to closeList
	 * @param x
	 * @param y
	 */
	private static void addLocToClosedList(int x, int y) {
		//adding location(x,y) to closeList
		
		//looking for the next free place
		//(a place on the list is free when the x coordination equals the defaultListNumber
		int count = 0;
		while(closeList[count][0] != defaultListNumber) {
			count++;
		}
		
		//adding location(x,y)
		closeList[count][0] = x;
		closeList[count][1] = y;
	}
	
	
	/**
	 * add specific location to openList
	 * @param x
	 * @param y
	 */
	private static void addLocToOpenList(int x, int y) {
		//adding location(x,y) to openList
		
		//looking for the next free place
		//(a place on the list is free when the x coordination equals the defaultListNumber
		int count = 0;
		while(openList[count][0] != defaultListNumber) {
			count++;
		}
		
		//adding location(x,y)
		openList[count][0] = x;
		openList[count][1] = y;
	}

	/**
	 * search for next best field of openList
	 */
	private static void searchForNextBestField() {
		//searching for next best field
		//goes through every field of the openList and chooses the next best to evaluate
		
		int min = defaultListNumber;	//the new minimum distance f
		int index = 0;					//the index of that field with the minimum distance f
		
		//goes through the openList
		for(int i = 0; i < defaultListNumber; i++) {
			//calculates f = g + h
			int f = Math.abs((openList[i][0] - Gx)) + Math.abs((openList[i][1] - Gy))		
					//h: distance to goal
					+ Math.abs((openList[i][0] - Lx)) + Math.abs((openList[i][1] - Ly));	
					//g: distance to start (of that move)
			
			//if there is a new minimum f the minimum and the index are changed
			if(f <= min) {
				min = f;
				index = i;
			}
		}
		
		//new location to be examined is added
		LocX = openList[index][0];
		LocY = openList[index][1];
		
		//examined location is removed from openList
		openList[index][0] = defaultListNumber;
		openList[index][1] = defaultListNumber;
	}

	/**
	 * After getting the whole path the program think backwards until the first move he did
	 * to get to the goal
	 * @return
	 */
	private static Object evaluateGrid() {
		//the direction he went first
		String direction = "WAIT";
		
		//goes back the way it went with LocX and LocY until it gets to the start location of that move
		while(!(LocX == Lx && LocY == Ly)) {
			direction = gr[LocX][LocY];	//the last direction it went
			switch (direction) {
			case "E":	//when it came from east LocX needs to grow
				LocX = LocX +1;
				break;
			case "W":	//when it came from west LocX needs to shrink
				LocX = LocX -1;
				break;
			case "N":	//when it came from north LocY needs to shrink
				LocY = LocY -1;
				break;
			case "S":	//when it came from south LocY needs to grow
				LocY = LocY +1;
				break;
			default:
				break;
			} 
		}
		
		//now the agent is finished with the search and can give back the first direction he examined
		switch (direction) {
		case "N":	//if it came from north it needs to move south
			return action.MOVE_SOUTH;
		case "S":	//if it came from south it needs to move north
			return action.MOVE_NORTH;
		case "W":	//if it came from west it needs to move east
			return action.MOVE_EAST;
		case "E":	//and if it came from east it needs to move west
			return action.MOVE_WEST;
		default:
			break;
		}
		//else he can wait
		return action.WAIT;
	}
}
