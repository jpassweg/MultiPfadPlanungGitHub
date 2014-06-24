/**
 * 
 * @author Jonas Passweg
 * @version 1.2
 * 
 * Agent class with its informations
 * @param <PathOrder>
 *
 */
public class Agent {
	
	//Agent variables
	public static int ID;	//ID
	public static int defaultNumber = Grid.GRID.length * Grid.GRID[0].length;
	//the default number is equivalent to the number of places
	
	//These arrays functions in the way that their value is saved under the index of the ID
	//ex Sx[5] is the starting x-coordinate of the agent with the ID number 5
	public static int[] Sx = new int[defaultNumber];	//x-coordinate start
	public static int[] Sy = new int[defaultNumber];	//y-coordinate start
	public static int[] Lx = new int[defaultNumber];	//x-coordinate location
	public static int[] Ly = new int[defaultNumber];	//y-coordinate location
	public static int[] Gx = new int[defaultNumber];	//x-coordinate goal
	public static int[] Gy = new int[defaultNumber];	//y-coordinate goal
	
	public int[] Lax = new int[defaultNumber]; //last x-coordinate
	public int[] Lay = new int[defaultNumber]; //last y-coordinate
	
	/** AGENT DECIDE */
	//the main method of this class
	public Object decide(int momID) {
		//using temporary ID to choose which agent should move
		ID = momID;
		
		//all needed variables are stored in a array for simpler use
		int[] variables = {ID, Sx[ID], Sy[ID], Lx[ID], Ly[ID], Gx[ID], Gy[ID]};
		
		//return the action from AlgorithmAgent
		return AlgorithmAgent.decide(variables);
	}
	
	/** SET */
	//ID of agent
	public Agent(int id) {
		//ID is set to the given ID
		ID = id;
	}
	
	//set Start
	public void setStart(int x, int y, int momID) {
		//setting the start of the agent with the ID momID
		ID = momID;
		Sx[ID] = x;	//setting x coordinate
		Sy[ID] = y; //setting y coordinate
		
		//start and location are the same at start
		setLocation(x, y, momID);
		
		Lax[ID] = x; //at the beginning the last x position is the same as the start position
		Lay[ID] = y; //at the beginning the last y position is the same as the start position
	}
	
	//set Location
	public void setLocation(int x, int y, int momID) {
		//setting of the location of the agent with the ID momID
		ID = momID;
		Lx[ID] = x; //setting x coordinate
		Ly[ID] = y; //setting y coordinate
		
		//checking if location of agent is blocked at start
		if(Grid.GRID[x][y] != "O") {
			System.out.println("Placing start of agent" + ID + " on blocked field!!!");
		}
		
		//placing the agent on the Grid
		Grid.GRID[x][y] = "L" + ID;
	}
	
	//set Goal
	public void setGoal(int x, int y, int momID) {
		//setting of the goal location of the agent with the ID momID
		ID = momID;
		Gx[ID] = x; //setting x coordinate
		Gy[ID] = y; //setting y coordinate
	}
	
	/** GET / CHECK */
	//checking if Agent at Goal
	public boolean atGoal(int momID) {
		//checking if agent with ID momID is at his goal
		ID = momID;
		//the agent is at his goal if location coordinates are the same as goal coordinates
		if(Lx[ID] == Gx[ID] && Ly[ID] == Gy[ID]) {
			return true;
		} return false;
	}

	//get x position
	public int getXPosition(int momID) {
		//getting x position of agent with ID momID
		ID = momID;
		return Lx[ID];
	}

	//get y position
	public int getYPosition(int momID) {
		//getting y position of agent with ID momID
		ID = momID;
		return Ly[ID];
	}

	//get ID
	public int getID() {
		//getting ID of last used agent
		return ID;
	}

	//check if agent is blocked
	public boolean isBlocked(int momID) {
		//checking if agent with ID momID is blocked
		ID = momID;
		
		//a agent is blocked when every field around him is blocked ("X")
		if(Grid.GRID[Lx[ID]-1][Ly[ID]] == "X" && Grid.GRID[Lx[ID]][Ly[ID]-1] == "X"
				&& Grid.GRID[Lx[ID]+1][Ly[ID]] == "X" && Grid.GRID[Lx[ID]][Ly[ID]+1] == "X") {
			return true;
		} return false;
	}
	
	//get a List of a agent
	public int[] getAgentList(int momID) {
		//returning a list of agent with ID momID
		ID = momID;
		//the list contains x-start-location, y-start-location, x-goal-location and y-goal-location
		int[] agList = {Sx[ID],Sy[ID],Gx[ID],Gy[ID]};
		//returning this list
		return agList;
	}
	
	//get a List of all agents
	public int[][] getAgentsList() {
		//returning a list of all agents
		
		//this list contains the x-start-location, y-start-location, x-goal-location and y-goal-location
		//of all agents
		int[][] agsList = new int[Main.agentInfo.length][4];
		for(int i = 0; i < agsList.length; i++) {
			agsList[i][0] = Sx[i];
			agsList[i][1] = Sy[i];
			agsList[i][2] = Gx[i];
			agsList[i][3] = Gy[i];
		}
		
		//returning agsList
		return agsList;
	}
	
	//giving back Grid
	public String[][] getMapField() {
		//simply returning the GRID from Grid.java
		return Grid.GRID;
	}
	
	//giving back PathOrder
	//not any clue how this should work
	public <PathOrder> PathOrder getPathOrder() {
		//close enough
		return getPathOrder();
	}
	

	/** MOVING OF AGENT */
	//moving of agent (action)
	public void move(Object ac, int momID) {
		ID = momID;
		
		//removing actual location of Agent on Grid
		Grid.GRID[Lx[ID]][Ly[ID]] = "O";
		
		//set last position before changing
		Lax[ID] = Lx[ID]; 
		Lay[ID] = Ly[ID];
		
		//adding new location to Grid
		if(ac.equals(action.MOVE_EAST)) {
			Lx[ID]++;	//if moving east x-position grows
		} else if(ac.equals(action.MOVE_NORTH)) {
			Ly[ID]--;	//if moving north y-position shrinks
		} else if(ac.equals(action.MOVE_SOUTH)) {
			Ly[ID]++;	//if moving south y-position grows
		} else if(ac.equals(action.MOVE_WEST)) {
			Lx[ID]--;	//if moving west x-position shrinks
		}
		
		//adding new location to Grid
		Grid.GRID[Lx[ID]][Ly[ID]] = "L" + ID;
	}
	
}
