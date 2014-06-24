import javax.swing.*;
import java.awt.*;

/**
 * 
 * @author Jonas Passweg
 * @version 1.5
 * 
 * The GUI class
 * Contents everything that has to do with GUI's
 * Is allowed to be a bit messy (maybe..)
 * stucked here with commenting right know
 *
 */
public class GUI {
	
	/** VARIABLES FOR JFRAME GUI */
	//Main frame
	static JFrame frame = new JFrame("SIMULATION");			//Frame for Simulation (main)
	static GridLayout layout;								//Layout of Simulation (main)
	//Algorithm
	static JFrame frameAlgorithm = new JFrame("A*-Search");	//Frame for Algorithm Simulation
	static GridLayout layoutAlgorithm;						//Layout of Algorithm Simulation
	//OpenList
	static JFrame frameOpenList = new JFrame("OpenList");	//Frame for OpenList
	static GridLayout layoutOpenList;						//Layout of OpenList
	//CloseList
	static JFrame frameCloseList = new JFrame("CloseList");	//Frame for CloseList
	static GridLayout layoutCloseList;						//Layout of CloseList
	//variables holder
	static int[][] openList;								//openList copy for initiation
	static int[][] closeList;								//closeList copy for initiation
	
	/** OTHER VARIABLES */
	//these will be initiated during the method initDimensions()
	public static int width;								//width of Grid
	public static int height;								//height of Grid
	public static int ScreenWidth;							//width of the Screen
	public static int ScreenHeight;							//height of the Screen
	
	public static int topBorder = 20;						//Space between top of screen and frames
	public static int leftBorder = 50;						//Space between left side of screen and frames
	public static int rightBorder = leftBorder;				//Space between right side of screen and frames
	public static int betweenBorder = 10;					//Space between two frames
	public static int DeepKonstant = 30;					
	//To set the right size of the simulation the number of fields are multiplied by DeepKonstant to get
	//a pixel number of a field number
	
	/** MAIN GUI METHOD */
	/**
	 * starting whole simulation
	 * 
	 * @param GRID	the Grid that needs
	 * @param main	if main window shall be displayed
	 * @param algo	if algorithm windows shall be displayed
	 */
	
	public static void startGUISimulation(String[][] GRID, boolean main, boolean algo) {
        initDimensions(GRID);	//Initiations of Dimensions
        
        //initialisation of frame
        frame.setSize(width * DeepKonstant + 100,height * DeepKonstant + 100);	//size of frame
        frame.setLocation(leftBorder, topBorder);								//location of frame
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);			//operation when closed
        layout = new GridLayout(height,width);									//layout of frame (Gridlayout)
        frame.setLayout(layout);
        frame.setResizable(false);												//set frame not rezisable
        
        //filling of frame with content (Grid content)
        for(int i = 0; i < GRID[0].length; i++) {
			for(int j = 0; j < GRID.length; j++) {
				//depending on the string from the Grid the corresponding image should be displayed
				switch (GRID[j][i].substring(0, 1)) {
				case "O":	//"O" stands for free field
					frame.add(new JLabel(new ImageIcon("WhiteBordered.png")));
					break;	
				case "X":	//"X" stands for blocked field
					frame.add(new JLabel(new ImageIcon("BlackBordered.png")));
					break;
				case "L":	//"L" stands for a field blocked by an agent
					frame.add(new JLabel(new ImageIcon("Agent.png")));
					break;
				default:	//in any other case a free field is added
					frame.add(new JLabel(new ImageIcon("WhiteBordered.png")));
					break;
				}
				
			}
		}
        
        //if asked (by main), the algorithm should be simulated
        if(algo) startGUIAlgorithm(GRID);		//starting simulation of algorithm
        if(algo) startGUIOpenList(openList);	//starting simulation of openList of algorithm
        if(algo) startGUICloseList(closeList);	//starting simulation of closeList of algorithm
        //for more informations about the lists look up under AlgorithmAgent
        
        //visibility of frame
        if(main) frame.setVisible(true);  
	}

	/** ALGORITHM SIMULATION */
	/** starting algorithm simulation
	 * 
	 * @param GRID the Grid that needs to be displayed
	 */
	public static void startGUIAlgorithm(String[][] GRID) {

        //initialisation of frame
        frameAlgorithm.setSize(width * DeepKonstant + 100,
        						height * DeepKonstant + 100);					//size of frame
        frameAlgorithm.setLocation(160 + betweenBorder 							//50 are the two borders of the frames
        		+ (width * DeepKonstant), topBorder);							//location of frame
        frameAlgorithm.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);	//operation when closed
        layoutAlgorithm = new GridLayout(height,width);							//layout of frame
        frameAlgorithm.setLayout(layoutAlgorithm);
        frameAlgorithm.setResizable(false);										//set frame not rezisable
        
        //filling of frame with content (Grid content)
        for(int i = 0; i < GRID[0].length; i++) {
			for(int j = 0; j < GRID.length; j++) {
				//depending on the string from the Grid the corresponding image should be displayed
				switch (GRID[j][i].substring(0, 1)) {
				case "O":	//O stands for free field
					frameAlgorithm.add(new JLabel(new ImageIcon("WhiteBordered.png")));
					break;
				case "X":	//blocked field
					frameAlgorithm.add(new JLabel(new ImageIcon("BlackBordered.png")));
					break;
				case "L":	//field blocked by an agent
					frameAlgorithm.add(new JLabel(new ImageIcon("Agent.png")));
					break;
				case "N":	//agent path went from north
					frameAlgorithm.add(new JLabel(new ImageIcon("North.png")));
					break;
				case "S":	//agent path went from south
					frameAlgorithm.add(new JLabel(new ImageIcon("South.png")));
					break;
				case "E":	//agent path went from east
					frameAlgorithm.add(new JLabel(new ImageIcon("East.png")));
					break;
				case "W":	//agent path went from west
					frameAlgorithm.add(new JLabel(new ImageIcon("West.png")));
					break;
				default:	//default
					frameAlgorithm.add(new JLabel(new ImageIcon("WhiteBordered.png")));
					break;
				}
			}
		}
        
        //initialisation of openlist and closelist variables
      	initLists();
        
        //visialibation of frame tülülü
        frameAlgorithm.setVisible(true);  
	}
	
	/** 
	 * starting simulation of openlist (first empty)
	 * @param list openlist
	 */
	public static void startGUIOpenList(int[][] list) {
		
		//initialisation of frame
        frameOpenList.setSize(100,700);											//size of frame
        frameOpenList.setLocation(ScreenWidth - 300 
        		- betweenBorder - rightBorder, topBorder);						//location of frame
        frameOpenList.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);	//operation when closed
        layoutOpenList = new GridLayout((width*height)/2,4);					//layout of frame
        frameOpenList.setLayout(layoutOpenList);
        frameOpenList.setResizable(false);										//set frame not rezisable
        
        //filling of frame with content
        for(int i = 0; i < list.length; i++) {
        	frameOpenList.add(new JLabel(list[i][0] + ""));		//at first nothing
        	frameOpenList.add(new JLabel(list[i][1] + ""));
        }
        
        //packing of framOpenList
        frameOpenList.pack();
        //visibility of frame tülülü
        frameOpenList.setVisible(true);
	}

	/** 
	 * starting simulation of closelist (first empty)
	 * @param list closelist
	 */
	public static void startGUICloseList(int[][] list) {
		//initialisation of frame
       frameCloseList.setSize(100,700);											//size of frame
       frameCloseList.setLocation(ScreenWidth - rightBorder - 150, topBorder);	//location of frame
       frameCloseList.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);	//operation when closed
       layoutCloseList = new GridLayout((width*height)/2,4);					//layout of frame
       frameCloseList.setLayout(layoutCloseList);
       frameCloseList.setResizable(false);										//set frame not rezisable
       
       //filling of frame with content
       for(int i = 0; i < list.length; i++) {
       	frameCloseList.add(new JLabel(list[i][0] + ""));	//at first nothing
       	frameCloseList.add(new JLabel(list[i][1] + ""));
       }
       
       //packing of frameCloseList
       frameCloseList.pack();
       //visibility of frame tülülü
       frameCloseList.setVisible(true);
	}
	
	/** REFRESHING SIMULATIONS */
	//refreshing simulation after every move
	public static void refreshGUISimulation(String[][] GRID) {
		frame.getContentPane().removeAll();	//removes everything from the frame
		
		//refilling of frame with new content (Grid content)
		for(int i = 0; i < GRID[0].length; i++) {
			for(int j = 0; j < GRID.length; j++) {
				//depending on the string from the Grid the corresponding image should be displayed
				switch (GRID[j][i].substring(0, 1)) {
				case "O":	//free field
					frame.add(new JLabel(new ImageIcon("WhiteBordered.png")));
					break;
				case "X":	//blocked field
					frame.add(new JLabel(new ImageIcon("BlackBordered.png")));
					break;
				case "L":	//field blocked by agent
					frame.add(new JLabel(new ImageIcon("Agent.png")));
					break;
				default:	//default
					frame.add(new JLabel(new ImageIcon("WhiteBordered.png")));
					break;
				}
			}
		}
		
		//revalidation of the frame
		frame.revalidate();					
	}
	
	//refreshing algorithm simulation after every move
	public static void refreshGUIAlgorithm(String[][] GRID, int[][] closeList) {
		frameAlgorithm.getContentPane().removeAll();
			
		//refilling of frame with new content (Grid content)
		for(int i = 0; i < GRID[0].length; i++) {
			for(int j = 0; j < GRID.length; j++) {
				
				//if field is on closeList
				boolean onCloseList = false;
				
				//goes through closeList
				for(int k = 0; k < closeList.length; k++) {
					//if the given coordination matches any of the ones on the closeList this coordination
					//is on the closeList
					if(closeList[k][0] == j && closeList[k][1] == i) {
						onCloseList = true;
					}
				}
				
				//depending on the string from the Grid the corresponding image should be displayed
				switch (GRID[j][i].substring(0, 1)) {
				//when on close List the fields will appear red
				case "O":	//free field
					if(onCloseList) frameAlgorithm.add(new JLabel(new ImageIcon("WhiteBorderedRed.png")));
					if(!onCloseList) frameAlgorithm.add(new JLabel(new ImageIcon("WhiteBordered.png")));
					break;
				case "X":	//blocked field (no red version)
					frameAlgorithm.add(new JLabel(new ImageIcon("BlackBordered.png")));
					break;
				case "L":	//field blocked by agent
					if(onCloseList) frameAlgorithm.add(new JLabel(new ImageIcon("AgentRed.png")));
					if(!onCloseList) frameAlgorithm.add(new JLabel(new ImageIcon("Agent.png")));
					break;
				case "N":	//agent path went from north
					if(onCloseList) frameAlgorithm.add(new JLabel(new ImageIcon("NorthRed.png")));
					if(!onCloseList) frameAlgorithm.add(new JLabel(new ImageIcon("North.png")));
					break;
				case "S":	//agent path went from south
					if(onCloseList) frameAlgorithm.add(new JLabel(new ImageIcon("SouthRed.png")));
					if(!onCloseList) frameAlgorithm.add(new JLabel(new ImageIcon("South.png")));
					break;
				case "E":	//agent path went from east
					if(onCloseList) frameAlgorithm.add(new JLabel(new ImageIcon("EastRed.png")));
					if(!onCloseList) frameAlgorithm.add(new JLabel(new ImageIcon("East.png")));
					break;
				case "W":	//agent path went from west
					if(onCloseList) frameAlgorithm.add(new JLabel(new ImageIcon("WestRed.png")));
					if(!onCloseList) frameAlgorithm.add(new JLabel(new ImageIcon("West.png")));
					break;
				default:	//default
					frameAlgorithm.add(new JLabel(new ImageIcon("WhiteBordered.png")));
					break;
				}
			}
		}
		
		
		//revalidating frame
		frameAlgorithm.revalidate();
	}
	
	//refreshing open list after every move
	public static void refreshGUIOpenList(int[][] list) {
		//removing everything from frame
		frameOpenList.getContentPane().removeAll();
			
		//refilling of frame with new content
        for(int i = 0; i < list.length; i++) {
        	frameOpenList.add(new JLabel(list[i][0] + ""));	//with x coordination
        	frameOpenList.add(new JLabel(list[i][1] + ""));	//and y coordination of a certain point
        }
		
        //packing of framOpenList
        frameOpenList.pack();
        //revalidating frame
        frameOpenList.revalidate();
	}
	
	//refreshing close list after every move
	public static void refreshGUICloseList(int[][] list) {
		//removing everything from frame
		frameCloseList.getContentPane().removeAll();
			
		//refilling of frame with new content
        for(int i = 0; i < list.length; i++) {
        	frameCloseList.add(new JLabel(list[i][0] + "")); //with x coordination
        	frameCloseList.add(new JLabel(list[i][1] + "")); //and y coordination of a certain point
        }
		
        //packing of frameCloseList
        frameCloseList.pack();
        //revalidating
		frameCloseList.revalidate();
	}
	
	/** OTHERS */
	/**
	 * @param GetRID = GRID
	 */
	private static void initDimensions(String[][] GetRID) {
		width = GetRID.length;		//width of Grid  (initiated here)
        height = GetRID[0].length;	//height of Grid (initiated here)
        initScreenSize();			//initiation of Screen dimensions variables
	}
	
	//initilaization of monitor Size
	private static void initScreenSize() {
		GraphicsDevice gd = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
		ScreenWidth = gd.getDisplayMode().getWidth();	//screenWidth
		ScreenHeight = gd.getDisplayMode().getHeight();	//screenHeight
	}
	
	//initialization of lists
	private static void initLists() {
		//initialisation of the two lists
		openList = new int[width * height][2];
        closeList = new int[width * height][2];
	}

	//disposing of all the frames of algorithm
	public static void disposeAlgorithm() {
		//disposing all frame of algorithm
		frameAlgorithm.dispose();
		frameOpenList.dispose();
		frameCloseList.dispose();
	}
}