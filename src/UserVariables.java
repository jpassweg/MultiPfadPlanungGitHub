import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

/**
 * 
 * @author Jonas Passweg
 * @version 1.2
 * 
 * This class stores the information about agents and obstacles (more specific 
 * their starting and targeted locations). This class can later be replaced by
 * a text file.
 * This class is therefore mainly created for the user to easier get to the variables
 * he might want to change.
 * 
 */
public class UserVariables {
	//variables for the Grid
	public static int lengthOfGrid;				//length of the Grid
	public static int defaultLength = 10;		//default length if not initialised
	public static int heightOfGrid;				//height of the Grid
	public static int defaultHeight = 10;		//default height if not initialised
	
	public static String path = "InputFile.txt";//the path of the input file
	
	/* list of agents with agentInfo.length == number of agents
	 * {x-coordination start, y-coordination start, x-coordination goal, y-coordination goal}
	 */
	public static int[][] agentInfo;
	
	/* list of obstacles
	 * {x-coordination,y-coordination} (Is marked with an X on the Grid)
	 */
	public static int[][] obstaclesInfo;
	
	/**
	 * getting the information out of the input file "InputFile.txt";
	 * the information in this file are stored as follow:
	 * lengthOfGrid
	 * heightOfGrid
	 * x-start-location y-start-location x-goal-location y-goal-location (for all agents. always separated with " ")
	 * x-location y-location											 (for all obstacles. always separated with " ")
	 * @throws IOException
	 */
	public static void getInputFile() throws IOException {
		//the content of the InputFile will be saved in a string array where the index stands for the line number
		String[] content = readFile();
		
		//if the file is empty the program needs default variables which are shown in the else loop
		if(content != null) {
			//telling that the input file was read
			System.out.println("Input file found and read!");
			
			//getting the length and height of the Grid
			lengthOfGrid = Integer.parseInt(content[0]);
			heightOfGrid = Integer.parseInt(content[1]);
			
			String[] agents = content[2].split(" ");	//splitting up the agent line in a string array
			int numberOfAgents = agents.length / 4;		//the length of agents contains 4 coordinations per agent
			agentInfo = new int[numberOfAgents][4];		//now we can first initialised the agentInfo array
			
			//part is the number of the part of the coordinations
			int part = 0;
			for(int i = 0; i < numberOfAgents; i++) {					//goes through agents
				for(int j = 0; j < agentInfo[0].length; j++) {			//goes through agents 4 coordinations
					agentInfo[i][j] = Integer.parseInt(agents[part]);	//giving the agent to proper coordination
					part++;												//continue to next part
				}
			}
			
			//same as with agents but with obstacles
			String[] obstacles = content[3].split(" ");		//splitting up the obstacles line in its coordinations
			int numberOfObstacles = obstacles.length / 2;	//because obstacles have only 2 coordination
			obstaclesInfo = new int[numberOfObstacles][2];	//now we can first initialised the obstaclesInfo array
			
			//same as with agents
			part = 0;
			for(int i = 0; i < numberOfObstacles; i++) {
				for(int j = 0; j < obstaclesInfo[0].length; j++) {
					obstaclesInfo[i][j] = Integer.parseInt(obstacles[part]);
					part++;
				}
			}
			
		} else {//as said if no file was found the default variables are set
			lengthOfGrid = defaultLength;
			heightOfGrid = defaultHeight;
			agentInfo = new int[0][0];
			obstaclesInfo = new int[0][0];
		} //end of if-else loop
	}

	/**
	 * reading of the File
	 * @param path path of input file
	 * @return content of file in String array
	 * @throws IOException
	 */
	public static String[] readFile() throws IOException {
		//storing the content in a string array (index stands for the line)
		String[] content = null;
		
		//if file exists the program reads it
		if ((new File(path)).exists()) {
			try {//try-catch loop
				//new filereader and bufferedreader
				FileReader fr = new FileReader(path);
				BufferedReader textReader = new BufferedReader(fr);
				
				//storing number of lines
				int numberOfLines = readLines();
				
				//content is a String array with as index the numbers of lines
				content = new String[numberOfLines];
				
				//storing each line in content with the corresponding index i
				for(int i = 0; i < numberOfLines; i++) {
					content[i] = textReader.readLine();
				}
				
				//finally closing textReader
				textReader.close();
			} catch (Exception e) {
				//doing the exception
				e.printStackTrace();
			}
		//else we return null as content
		} else {
			content = null;
		}
		return content;
	}
	
	public static int readLines() throws IOException {
		//new filereader and bufferedreader
		FileReader file = new FileReader(path);
		BufferedReader bf = new BufferedReader(file);
		
		//while readLine() gives something back the file is not finished
		int numberOfLines = 0;
		while(bf.readLine() != null) {
			numberOfLines++;
		}
		
		//closing buffered reader
		bf.close();
		
		//returning number of lines
		return numberOfLines;
	}
}
