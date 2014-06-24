import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import javax.swing.*;

public class EditorGUI {	
	JFrame frameDimension = new JFrame("EDITOR");
	
	JFrame frame = new JFrame("EDITOR");
	JFrame frameAgent = new JFrame("AGENTS");
	JFrame frameObstacles = new JFrame("OBSTACLES");
	JFrame frameGrid = new JFrame("GRID");		//Frame for Simulation (main)
	GridLayout layout;								//Layout of Simulation (main)
	
	public int[] list = null;
	
	public int startLocation = 0;
	public JButton closeButton = new JButton("Close");
	public JButton saveButton = new JButton("Save");
	 JButton startSimulation = new JButton("Start");
	public int agFrameHeight = 3;
	public JLabel[] agentList;
	public int[][] agList;
	public int WIDTH;
	public int HEIGHT;
	
	public String[] args;
	
	/**
	 * starting GUI
	 */
	public void startEditorGUI(String[] args){
		frameDimension.setSize(260,95);												//size of frameDimension
        frameDimension.setLocation(50, 50);												//location of frameDimension
        frameDimension.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);			//operation when closed
        layout = new GridLayout(3,2);													//layout of frameDimension (Gridlayout)
        frameDimension.setLayout(layout);
        frameDimension.setResizable(false);												//set frame not rezisable
        
        this.args = args;
        
        frameDimension.add(new JLabel("Length: "));
        final JTextField length = new JTextField();
        frameDimension.add(length);
        
        frameDimension.add(new JLabel("Height: "));
        final JTextField height = new JTextField();
        frameDimension.add(height);
        
        JButton exitButton = new JButton("Cancel");
        exitButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {frameDimension.dispose();}
		});
        
        frameDimension.add(exitButton);
        
        JButton startButton = new JButton("OK");
		startButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				try {
					int len = Integer.parseInt(length.getText());
					int hei = Integer.parseInt(height.getText());
					if(len >= 5 && hei >= 5) {
						startEditorGUI(len,hei);
						frameDimension.dispose();
					} else {
						System.out.println("Bigger than 4");
					}
					
				} catch (Exception e) {
					System.out.println("Only numbers!!!");
				}
			}	
		});
		
		frameDimension.add(startButton);
        frameDimension.setVisible(true);
	}
	
	public void startEditorGUI(int width, int height){
        frameAgent.setLocation(width * 30 + 50, 50);								//location of frame
        frameAgent.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);			//operation when closed
        layout = new GridLayout(agFrameHeight,1);									//layout of frame (Gridlayout)
        frameAgent.setLayout(layout);
        frameAgent.setResizable(false);												//set frame not rezisable
       
        WIDTH = width;
        HEIGHT = height;
       
        closeButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				frameAgent.dispose();
				frameGrid.dispose();
			}
		});
        
        saveButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				saveFile();
			}
		});
        
        frameAgent.add(closeButton);
        frameAgent.add(saveButton);
        
        startSimulation.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				frameAgent.dispose();
				frameGrid.dispose();
				try {
					Main.simu = false;
					Main.algo = false;
					Main.main(args);
				} catch (IOException e) {
					// do exception things
					e.printStackTrace();
				}
			}
		});
        
        frameAgent.add(startSimulation);
       
        agentList = new JLabel[width*height];
        for(int i = 0; i < agentList.length; i++) {
        	agentList[i] = new JLabel("null");
        }
        
        agList = new int[width][height];
        for(int i = 0; i < agList.length; i++) {
        	for(int j = 0; j < agList[0].length; j++) {
            	agList[i][j] = 0;
            }
        }
        
        frameGrid.setSize(width * 30,height * 30);		//size of frame
        frameGrid.setLocation(20, 50);								//location of frame
        frameGrid.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);			//operation when closed
        layout = new GridLayout(height,width,1,1);									//layout of frame (Gridlayout)
        frameGrid.setLayout(layout);
        frameGrid.setResizable(false);												//set frame not rezisable
        
        list = new int[width*height];;
        
        for(int i = 0; i < list.length; i++) {
        	list[i] = 0;
        }
        
        //filling of frame with content (Grid content)
        for(int i = 0; i < list.length; i++) {
			
        	switch (list[i]) {
			case 0:
				frameGrid.add(new JLabel(new ImageIcon("WhiteBordered.png")));
				break;
			case 1:
				frameGrid.add(new JLabel(new ImageIcon("BlackBordered.png")));
				break;
			default:
				frameGrid.add(new JLabel(new ImageIcon("WhiteBordered.png")));
				break;
			} 
			     	
		}
        
        
		
        frameGrid.addMouseListener(new MouseListener() {
			
			@Override
			public void mouseClicked(MouseEvent e) {
				
				double x = e.getX() * WIDTH / (WIDTH * 30);
				double y = e.getY() * HEIGHT / (HEIGHT * 30);
				int num = (int) Math.round((y-1)*(WIDTH) + x);
				try {
					if(e.getButton() == 3) {
						list[num] = 0;
					} else {
						list[num] = 1;
					}
					refreshEditorGUI();
				} catch (Exception e2) {
					//e2.printStackTrace();
				}
					
			}

			@Override
			public void mouseEntered(MouseEvent e) {}
			@Override
			public void mouseExited(MouseEvent e) {}
			@Override
			public void mousePressed(MouseEvent e) {
				double x = e.getX() * WIDTH / (WIDTH * 30);
				double y = e.getY() * HEIGHT / (HEIGHT * 30);
				int num = (int) Math.round((y-1)*(WIDTH) + x);
				try {
					list[num] = 2;
					startLocation = num;
					refreshEditorGUI();
				} catch (Exception e2) {
					//e2.printStackTrace();
				}
			}
			@Override
			public void mouseReleased(MouseEvent e) {
				double x = e.getX() * WIDTH / (WIDTH * 30);
				double y = e.getY() * HEIGHT / (HEIGHT * 30);
				int num = (int) Math.round((y-1)*(WIDTH) + x);
				try {
					if(list[num] == 2) {
						addAgent(startLocation, num);
						refreshEditorGUI();
					} else if(list[num] == 3) {
						System.out.println("goal cant be taken twice!!");
						list[startLocation] = 0;
						refreshEditorGUI();
					} else {
						list[num] = 3;
						addAgent(startLocation, num);
						refreshEditorGUI();
					}
					
				} catch (Exception e2) {
					//e2.printStackTrace();
				}
			}
		});
		
        frameGrid.pack();
        frameAgent.pack();
        //visibility of frame
        frameGrid.setVisible(true);
        frameAgent.setVisible(true);
        
        
	}
	
	private void refreshEditorGUI() {
		frameGrid.getContentPane().removeAll();	//removes everything from the frame
		
		//refilling of frame with new content (Grid content)
		for(int i = 0; i < list.length; i++) {
			
			switch (list[i]) {
			case 0:
				frameGrid.add(new JLabel(new ImageIcon("WhiteBordered.png")));
				break;
			case 1:
				frameGrid.add(new JLabel(new ImageIcon("BlackBordered.png")));
				break;
			case 2:
				frameGrid.add(new JLabel(new ImageIcon("Agent.png")));
				break;
			case 3:
				frameGrid.add(new JLabel(new ImageIcon("Goal.png")));
				break;
			default:
				frameGrid.add(new JLabel(new ImageIcon("WhiteBordered.png")));
				break;
			}    
			    	
		}
		
		frameGrid.revalidate();					//revalidation of the frame
	}
	
	private void addAgent(int startLoc, int endLoc) {
		if(startLoc != endLoc) {
			frameAgent.getContentPane().removeAll();	//removes everything from the frame
			int startX = startLoc % WIDTH;
			int startY = (startLoc - (startLoc % WIDTH)) / WIDTH;
			int goalX = endLoc % WIDTH;
			int goalY = (endLoc - (endLoc % WIDTH)) / WIDTH;
			
			agFrameHeight = 3;
			
			//refilling of frame with new content (Grid content)
			for(int i = 0; i < list.length; i++) { 
				if(list[i] == 2) {
					agFrameHeight++;
					if(i == startLoc) {
						agentList[i] = new JLabel("Agent" + i + ": (" + startX + "/" + startY + ") (" 
					+ goalX + "/" + goalY + ")");
						frameAgent.add(agentList[i]);
					} else {
						frameAgent.add(agentList[i]);
					}
				}
			}
			
			
			layout = new GridLayout(agFrameHeight,1);									//layout of frame (Gridlayout)
	        frameAgent.setLayout(layout);
			
			frameAgent.add(saveButton);
			frameAgent.add(closeButton);
			frameAgent.add(startSimulation);
			
			frameAgent.pack();
			frameAgent.revalidate();					//revalidation of the frame
		}
	}
	
	private void saveFile() {
		String results;
		results = WIDTH + System.lineSeparator() + HEIGHT;
		
		String agentsStr = "";
		for(int i = 0; i < list.length; i++) {
			if(list[i] == 2) {
				String str = agentList[i].getText().replaceAll("[^0-9]+", " ");      
				String[] strArr = str.split(" ");
				agentsStr += strArr[2] + " " + strArr[3] + " " 
						+ strArr[4] + " " + strArr[5] + " ";
			}
		}
		
		String obstaclesStr = "";
		for(int i = 0; i < list.length; i++) {
			if(list[i] == 1) {
				obstaclesStr += (i % WIDTH) + " " + ((i - (i % WIDTH)) / WIDTH) + " ";
			}
		}
		
		results += System.lineSeparator() + agentsStr;
		results += System.lineSeparator() + obstaclesStr;
		
		//try-catch loop is needed for such an action
		try {
			//new file ResultsOutputFile.txt is created
		    File outputFile = new File("InputFile.txt");

            //a new filewriter is created on the outputFile, 
            //which basically prints a string in a file
            FileWriter fw = new FileWriter(outputFile);
            fw.write(results);	//The string results is written on the file
            fw.close();			//the filewriter is closed
		            
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	
}
