import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

/**
 * 
 * @author Jonas Passweg
 * @version 1.9
 * 
 * This class builds a GUI for agents to find their goal with and without communication
 * This project include these classes:
 * Main.java 					- main class					(All processes and Edition)
 * UserVariables.java			- user variables				(information about agents and obstacles are saved there)
 * Grid.java 					- grid							(storing the Grid and its information)
 * GUI.java						- gui class						(getting the Grid in a GUI for the user as simulation)
 * 																(Also every other GUI is stored there eg. A*-Search-GUI)
 * ArtificialIntelligence.java	- interface						(not necessary) (excluded)
 * action.java 					- possible actions of an agent	(not necessary)
 * Agent.java					- the agents itself				(all its variables and methods are stored in this class)
 * AlgorithmAgent.java			- algorithm of agents			(to decide which way to go) (A* Algorithm)
 * Notes						- notes							(not used as references)
 * 
 * This project is a Matura work by Jonas Passweg. It should look at how agents can behave to best fulfil there task.
 * Here the agents needs to move from a point A to B on the shortest path. So we consider it a Path-Planning-Task. Later
 * I (Jonas) will also include Multi-Path-Planning for further information on the topic and examine both to see the 
 * differences in efficiency and other categories.
 * 
 * Mostly after fully understanding the meaning of the class I place the items that can be changed by the user as
 * variables in the VARIABLES section for simpler using and less problems with users.
 */

//Start Main class
public class Main {
	
	/** MAIN CONTROL */
	/** Main method
	 * @param args null
	 * @throws IOException because of the reading of the InputFile of UserVariables.java
	 */
	public static void main(String[] args) throws IOException {
		MainControl();	//Goes over to control method
	}
	
	public static boolean start = false;
	
	/** ControlCenter (called by main)
	 * @throws IOException 
	 * 
	 */
	public static void MainControl() throws IOException {
		//start message
		System.out.println("Main.java START SIMULATION");
		
		//Try to get Information from InputFile
		UserVariables.getInputFile();
		
		//starting simulation (GUI)
		StartSimulation();
	}
	
	/** VARIABLES */
	//variables for the Grid
	public static int lengthOfGrid;	//length of the Grid
	public static int heightOfGrid;	//height of the Grid
	
	//agents
	public static Agent[] agents;					//array of agents
	public static int[][] agentInfo;
	/* list of agents with agentInfo.length == number of agents
	 * {x-coordination start, y-coordination start, x-coordination goal, y-coordination goal}
	 * this is saved under UserVariables */
	
	//obstacles: {x-coordination,y-coordination} (Is marked with an X on the Grid)
	//this is saved under UserVariables
	public static int[][] obstacles;
	
	//others
	public static int wait = 100 ; 					//Time between each refreshing/step of the Grid (excluded now)
	public static boolean countMovements = false;	//If moves shall be counted and printed
	public static boolean simu = true;				//if simulation shall be visible
	public static boolean algo = true;				//If algorithm simulation shall be visible
	public static boolean disposeAlgo = true;		//If algorithm should be disposed after simulation is finished
	public static boolean showFile = true;			//If outputFile will be displayed at the end of the simulation
	public static String results;					//results which will be saved in a file
	
	/** MAIN PROGRAM AND SIMULATION */
	/** Starting Simulation 
	 * 
	 */
	public static void StartSimulation() {
		//initiation of variables from InputFile
		initMap();
		
		//New Grid field with lengthOfGrid as length and heightOfGrid as height
		//This Grid is mometanely filled with O's 
		//(obstacles are X's and agents are represented with L plus the ID number)
		Grid field = new Grid();
		field.createGrid(lengthOfGrid, heightOfGrid);
		
		//Obstacles initialisation
		obstaclesInit();
		
		//Agents initialisation as an field of Agents
		agentsInit();
		
		//starting Simulation GUI of every main windows and others
		//first boolean: 	activation of main Window if true
		//second boolean: 	activation of other Windows it true
		field.startSimulation(simu, algo);
		
		//counting the moves that were done (will maybe be printed in the console)
		int countMoves = 0;	
		
		//boolean if everything is finished (every agent is at his Goal)
		boolean finished = false;
		
		//starting to write on string of output File
		startOutputFile();
		
		/** PROCESS */
		while(!finished) {
			
			//refresh output file
			refreshOutputFile(countMoves);
			
			//counting Moves and printing them out
			if(countMovements==true) System.out.println("Move: " + countMoves);
			countMoves++;	//adding move
			
			//moving Agents
			agentsMoveWoC(field);
			
			//checking if every agent is at his Goal;
			finished = checkGoal();
		}
		//refresh output file
		refreshOutputFile(countMoves);
		
		//if everything is finished
		theEND(countMoves);
	}

	/** INITIALISAIONS */
	
	/**
	 * initiation of map variables from UserVariables.java 
	 */
	private static void initMap() {
		lengthOfGrid = UserVariables.lengthOfGrid;	//length of Grid
		heightOfGrid = UserVariables.heightOfGrid;	//height of Grid
		agentInfo = UserVariables.agentInfo;		//coordinations of agents
		obstacles = UserVariables.obstaclesInfo;	//coordinations of obstacles
	}
	
	/**Initialisation of obstacles
	 * 
	 */
	public static void obstaclesInit() {
		//for loop goes through the obstacles array
		for (int i = 0; i < obstacles.length; i++) {
			//If there is nothing on the location of the obstacle that is being placed everything is fine
			//It is directly put on the Grid so every new initialisation of the Grid is not good
			if(Grid.GRID[obstacles[i][0]][obstacles[i][1]] == "O") {
				Grid.GRID[obstacles[i][0]][obstacles[i][1]] = "X";	//sign for obstacle
			//in the other case the obstacle can not be placed!!	
			} else {
				System.out.println("Obstacle could not be placed on " + obstacles[i][0] + "/" + obstacles[i][1] + ".");
			}
		}		 
	}
	
	/**
	 * Initialisation of Agents
	 * This needs to be changed if new Agents are to be made
	 * 
	 */
	public static void agentsInit() {
		agents = new Agent[agentInfo.length]; //Initialisation of agents array
		
		for(int i = 0; i < agentInfo.length; i++) {	//goes through agentInfo and fills this into the corresponding agent
			agents[i] = new Agent(i);		//new Agent with ID number
			agents[i].setStart(agentInfo[i][0], agentInfo[i][1], i);	
			//setting start coordinations (at beginning same as location coordinations)
			agents[i].setGoal(agentInfo[i][2], agentInfo[i][3], i);
			//setting goal coordinations
			
			//printing out the important information for the user
			System.out.println("agent" + i + " start: (" + agentInfo[i][0] + "/" + agentInfo[i][1] +")" + 
					" goal: (" + agentInfo[i][2] + "/" + agentInfo[i][3] +")");
		}
	}
	
	/** OTHER STUFF */
	/**
	 * The output file
	 */
	public static void startOutputFile() {
		//results String is filled with starting informations
		results = "Output file of Main.java "
				+ System.lineSeparator() + "This file contains the results from the Multe-Agent-Pathfinding Simulation"
				+ System.lineSeparator() + "by Jonas Passweg"
				+ System.lineSeparator();
		
		results += System.lineSeparator() + "Length of Grid: " + lengthOfGrid
				+ System.lineSeparator() + "Height of Grid: " + heightOfGrid;
		
		//The for loop goes through the agent array and prints out it start and goal location on results String
		for(int i = 0; i < agentInfo.length; i++) {
			results += System.lineSeparator() + "Agent" + i + "   Start: (" + agentInfo[i][0] + "/" + agentInfo[i][1] +")" + 
					" goal: (" + agentInfo[i][2] + "/" + agentInfo[i][3] +")";
		}
		
		//The obstacles are now printex on the resuslts String
		results += System.lineSeparator() + "Obstacles:";
		
		//one after another
		for(int j = 0; j < obstacles.length; j++) {
			results += " (" + obstacles[j][0] + "/" + obstacles[j][1] + ")";
		}
		
		//for format reasons we add another line at the end
		results += System.lineSeparator();
	}
	
	/** 
	 * refresh output file
	 * @param countMoves 
	 */
	private static void refreshOutputFile(int moves) {
		//adding new moves to results string
		results += System.lineSeparator() + "Move " + moves;
		
		//for format reasons free spaces are added
		if(moves!=0) { //if i would be 0 the for loop would not work
			for(int i = moves + 1; i <= 100; i = i * 10) {
				results += " ";
			} 
		} else {
			results += "  ";
		}
		
		//for loop goes through agent array and adds the location to the results String
		for(int i = 0; i < agentInfo.length; i++) {
			results += " (" + Agent.Lx[i] + "/" + Agent.Ly[i] +") ";
		}
	}
	
	
	/** Checking if every agent is at his Goal
	 * 
	 * @return finished is true if every agent is at his goal
	 */
	public static boolean checkGoal() {
		boolean finished = true;
		//if every agent is at his goal finished will be left true
		
		//for loop goes through every agent and checks if it is at its goal
		for(int i = 0; i < agentInfo.length; i++) {
			if(!agents[i].atGoal(i)) {
				finished = false;	
				//Obviously if one agent is not at his goal-location then the task is not finished
			}
		}
		
		return finished;
	}
	
	/** MOVING AGENTS */
	/** WoC: without comunication
	 * online
	 * every agent move for himself one after another one step after another
	 * 
	 */
	public static void agentsMoveWoC(Grid field) {
		for(int i = 0; i < agentInfo.length; i++) {
			//telling agent i to decide which way he wants to go (because he knows)
			Object action = agents[i].decide(i);
			//telling the agent i to move that way
			agents[i].move(action, i);
			//refreshing Simulation
			field.refreshSimulation();
			//waiting time between every move if simulation is shown
			if(simu) try {Thread.sleep(wait);} catch (InterruptedException e) {e.printStackTrace();}
		}
	}
	
	/**WC: with comunication
	 * 
	 */
	public static void agentsMoveWC() {
		//not implemented ;-(
	}
	
	/** THE END */
	/** when everything is finished
	 * 
	 * @param moves amounts of move needed to fulfil the task
	 */
	public static void theEND(int moves) {
		//disposing of the algorithm simulation if wanted
		if(disposeAlgo) Grid.disposeAlgorithmSimulation();
		//end text
		System.out.println("Every agent is at his goal location.");
		System.out.println("A total of " + moves + " moves was needed to fulfill the task.");
		//creating and displaying output file
		createOutputFile();
	}

	/**
	 * creating and displaying output file
	 */
	private static void createOutputFile() {
		//try-catch loop is needed for such an action
		try {
			//new file ResultsOutputFile.txt is created
            File outputFile = new File("ResultsOutputFile.txt");

            //a new filewriter is created on the outputFile, 
            //which basically prints a string in a file
            FileWriter fw = new FileWriter(outputFile);
            fw.write(results);	//The string results is written on the file
            fw.close();			//the filewriter is closed
            
            //if file exists it can be displayed
            if ((new File("ResultsOutputFile.txt")).exists() && showFile) {	
            	//don't understand this code but it will open ResultsOutputFile.txt
            	//copied from: http://www.mkyong.com/java/how-to-open-a-pdf-file-in-java/
    			Process p = Runtime
    			   .getRuntime()
    			   .exec("rundll32 url.dll,FileProtocolHandler ResultsOutputFile.txt");
    			p.waitFor();
    		} else {
    			//If file doesn't exist it will be printed out in the console
    			System.out.println("File is not exists");
    		}
          //needs two catches
        } catch (IOException iox) {
            //do stuff with exception
            iox.printStackTrace();
        } catch (InterruptedException e) {
			//do stuff with expection
			e.printStackTrace();
		}
	}
}
