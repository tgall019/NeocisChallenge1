package demo;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.ArrayList;

import javax.swing.*;

public class CircleGridDemo extends JFrame{
	
	public static CircleGridDemo frame; //frame made static for convenience
	
	public static JButton[] navButtons = new JButton[3]; // IGNORE
	public static MyButton[][] grid = new MyButton[20][20]; //square grid
	public static ArrayList<MyButton> blueButtons = new ArrayList<>(); //aux list of blue buttons
	
	public static Coordinate btnCoo = new Coordinate(0, 0); //auxiliary Coordinate obj for a button
	public static Coordinate coo; //auxiliary Coordinate obj
	public static double currentRadius1; //auxiliary variable to hold radius of circle
	public static float farthestRadius1 = 0; //aux variable to hold radius of outer red circle
	public static float closestRadius1 = 0; //aux variable to hold radius of inner red circle
	
	// ********** IGNORE ************
//	public static double currentRadius2; //auxiliary variable to hold radius of circle
//	public static float farthestRadius2 = 0; //aux variable to hold radius of outer red circle
//	public static float closestRadius2 = 0; //aux variable to hold radius of inner red circle
	// ******************************
	
	//The degree of precision when highlighting squares. The larger the value, the less precise.
	public static float MoE = 8; //A value of 8 works well. A value of less than 4 should be avoided
	
	// ************* IGNORE ****************
	//The rate at which the least square algorithm will look for a best fit. 
	//The larger the value, the less precise, but the less expensive.
	//public static float RoC = 1; //A value of 1 works well.
	// *************************************
	
	//Constructor that sets up the frame
	public CircleGridDemo() {
		//Structures the grid in the frame
		int xPos = 10, yPos = 10; //hard-coded design structure element
		for(int i = 0; i < 20; i++) {
			for(int j = 0; j < 20; j++) {
				// Set up every button
				grid[i][j] = new MyButton();
				grid[i][j].center = new Coordinate(xPos + 3, yPos + 3);
				grid[i][j].setCorners();
				grid[i][j].setBounds(xPos, yPos, 6, 6);
				grid[i][j].setBackground(Color.gray);
				grid[i][j].setBorder(null);
				grid[i][j].addMouseListener(new Part1Listener(i, j));
				grid[i][j].addMouseMotionListener(new Part1Listener(i, j));
				add(grid[i][j]);
				
				xPos += 16;
			}
			xPos = 10;
			yPos += 16;
		}
		
		// ******** IGNORE *********
//		//Part 1 Button
//		navButtons[0] = new JButton("Part 1");
//		navButtons[0].setBounds(10, 330, 151, 80);
//		navButtons[0].setBackground(Color.lightGray);
//		navButtons[0].addMouseListener(new NavListener());
//		add(navButtons[0]);
//		//Part 2 Button 1
//		navButtons[1] = new JButton("Part 2");
//		navButtons[1].setBounds(171, 330, 151, 37);
//		navButtons[1].setBackground(Color.lightGray);
//		navButtons[1].addMouseListener(new NavListener());
//		add(navButtons[1]);
//		//Part 2 Button 2 (Generate)
//		navButtons[2] = new JButton("Generate");
//		navButtons[2].setBounds(171, 373, 151, 37);
//		navButtons[2].setBackground(Color.lightGray);
//		navButtons[2].addMouseListener(new Part2Listener(0, 0));
//		navButtons[2].setEnabled(false);
//		add(navButtons[2]);
		//*****************************
	}

	// ### MAIN CLASS ###
	public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                gui(); 
            }
        });
    }
	
	public static void gui() {
		//Frame initiation
		frame = new CircleGridDemo();
		frame.getContentPane().setBackground(Color.white);
		frame.setSize(346, 380);
		frame.setLocationRelativeTo(null);
		frame.add(new CirclePanel1());
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);
	}
	
	//Changes squares from gray to blue (and 1 yellow)
	private void setBtnBlue(MyButton origin) {
		float dist; //aux variable to hold each button's distance from the origin
		
		//iterate through the list of buttons
		for(int i = 0; i < 20; i++) {
			for(int j = 0; j < 20; j++) {
				//set the origin to yellow
				if(grid[i][j] == origin) {
					grid[i][j].setBackground(Color.yellow);
					blueButtons.add(grid[i][j]);
				}
				
				dist = grid[i][j].center.distanceFrom(origin.center); //find dist (described above)
				if(Math.abs(dist - currentRadius1/2) <= MoE) { //check if distance between btn and circle is less than MoE
					//if so, change its color, and add it to the list of blue btns
					grid[i][j].setBackground(Color.blue);
					blueButtons.add(grid[i][j]);
				}
			}
		}
	}
	
	//Resets button colors to gray
	private void setBtnGray() {
		for(MyButton i : blueButtons) {
			i.setBackground(Color.gray);
		}
		blueButtons.clear();
	}
	
	// Finds the radius of the two red circles
	private void findRedCircles(Coordinate origin) {
		float farthest = 0, closest = 500;
		
		for(MyButton i : blueButtons) {
			if(i.getBackground() == Color.yellow) continue; //skip the origin
			
			//For the following four pairs:
			//Each pair checks if a corner has the farthest/closest distance from the origin
			
			//tl: top left
			if(i.tl.distanceFrom(origin) > farthest) farthest = i.tl.distanceFrom(origin);
			if(i.tl.distanceFrom(origin) < closest) closest = i.tl.distanceFrom(origin);
			
			//bl: bottom left
			if(i.bl.distanceFrom(origin) > farthest) farthest = i.bl.distanceFrom(origin);
			if(i.bl.distanceFrom(origin) < closest) closest = i.bl.distanceFrom(origin);
			
			//tr: top right
			if(i.tr.distanceFrom(origin) > farthest) farthest = i.tr.distanceFrom(origin);
			if(i.tr.distanceFrom(origin) < closest) closest = i.tr.distanceFrom(origin);
			
			//br: bottom right
			if(i.br.distanceFrom(origin) > farthest) farthest = i.br.distanceFrom(origin);
			if(i.br.distanceFrom(origin) < closest) closest = i.br.distanceFrom(origin);
		}
		
		farthestRadius1 = farthest;
		closestRadius1 = closest;
	}
	
	//Mouse Listener for Part 1
	class Part1Listener implements MouseListener, MouseMotionListener{
		int x, y;
		
		// This constructor will help with identifying squares
		public Part1Listener(int i, int j) {
			this.x = i; this.y = j;
		}
		
		@Override
		public void mouseClicked(MouseEvent e) {}
		
		@Override
		public void mousePressed(MouseEvent e) {
			closestRadius1 = 0;
			farthestRadius1 = 0;
			btnCoo = new Coordinate(grid[x][y].calcHorPos(y), grid[x][y].calcVerPos(x));
		}

		@Override
		public void mouseReleased(MouseEvent e) {
			setBtnGray(); //make sure all buttons are gray before starting
			setBtnBlue(grid[x][y]); //set buttons blue
			findRedCircles(grid[x][y].center); //algorithm that finds the red circles
			frame.update(getGraphics()); //update graphics to see red circles
		}

		@Override
		public void mouseEntered(MouseEvent e) {
			//Change color to black when cursor enters, unless blue
			if(grid[x][y].getBackground() != Color.blue && grid[x][y].getBackground() != Color.yellow) {
				grid[x][y].setBackground(Color.black);
			}
		}

		@Override
		public void mouseExited(MouseEvent e) {
			//Change color back to gray when cursor exits, unless blue
			if(grid[x][y].getBackground() != Color.blue && grid[x][y].getBackground() != Color.yellow) {
				grid[x][y].setBackground(Color.gray);
			}
		}

		@Override
		public void mouseDragged(MouseEvent e) {
			coo = new Coordinate(e.getX(), e.getY()); //get the cursor's coordinates
			currentRadius1 = coo.distanceFrom(x, y) * 2; //get the radius of the dynamic circle
			frame.update(frame.getGraphics()); //update graphics dynamically to see growing circle
		}

		@Override
		public void mouseMoved(MouseEvent e) {}
	}
	
	
	// ********* IGNORE ***************
//	private Coordinate findOrigin() {
//		float xTotal = 0, yTotal = 0;
//		//find sum of coordinates of all buttons along x and y
//		for(MyButton i : blueButtons) {
//			xTotal += i.center.x;
//			yTotal += i.center.y;
//		}
//		
//		//find averages, and subtract 10 (left padding)
//		xTotal = xTotal / blueButtons.size();
//		yTotal = yTotal / blueButtons.size();
//		
//		return new Coordinate(xTotal, yTotal);
//	}
//	
//	private void findRadius() {
//		closestRadius2 = 500;
//		float leastSquareTotal = 10000; //initial least square total
//		float currentSquareTotal = 0; //aux variable to hold current square total
//		
//		//find closest and farthest points (from the origin) and store their distances
//		for(MyButton i : blueButtons) {
//			if(i.center.distanceFrom(btnCoo) > farthestRadius2) {farthestRadius2 = i.center.distanceFrom(btnCoo);}
//			if(i.center.distanceFrom(btnCoo) < closestRadius2) {closestRadius2 = i.center.distanceFrom(btnCoo);}
//		}
//		
//		//Try every radius from closestRadius to farthestRadius with an increment of RoC (defined above)
//		for(int r = (int) Math.floor(closestRadius2); r < Math.ceil(farthestRadius2); r += RoC) {
//			//Find the least square total for all blue points, and find the sum of it
//			for(MyButton i : blueButtons) {
//				currentSquareTotal += LeastSquare.findArea(btnCoo, i, r); 
//			}
//			//if the current radius hits a new low, the value will be stored
//			if(currentSquareTotal < leastSquareTotal) {
//				leastSquareTotal = currentSquareTotal;
//				currentRadius2 = r;
//			}
//			currentSquareTotal = 0; //reset aux variable
//		}
//	}
//	
//	//Mouse Listener for Part 2
//	class Part2Listener implements MouseListener{
//		int x, y;
//		
//		public Part2Listener(int i, int j) {
//			this.x = i; this.y = j;
//		}
//		
//		@Override
//		public void mouseClicked(MouseEvent e) {		
//			if(e.getSource() == navButtons[2]) {
//				btnCoo = findOrigin(); //find the approximated best origin for the circle
//				findRadius(); //find the best radius
//			}
//			else {
//				if(grid[x][y].getBackground() == Color.blue) {
//					grid[x][y].setBackground(Color.gray);
//					blueButtons.remove(grid[x][y]);
//				}
//				else {
//					grid[x][y].setBackground(Color.blue);
//					blueButtons.add(grid[x][y]);
//				}
//			}
//			
//		}
//
//		@Override
//		public void mousePressed(MouseEvent e) {}
//
//		@Override
//		public void mouseReleased(MouseEvent e) {}
//
//		@Override
//		public void mouseEntered(MouseEvent e) {
//			//Change color to black when cursor enters, unless blue
//			if(grid[x][y].getBackground() != Color.blue && grid[x][y].getBackground() != Color.yellow) {
//				grid[x][y].setBackground(Color.black);
//			}
//			//System.out.println("Entered");
//		}
//
//		@Override
//		public void mouseExited(MouseEvent e) {
//			//Change color back to gray when cursor exits, unless blue
//			if(grid[x][y].getBackground() != Color.blue && grid[x][y].getBackground() != Color.yellow) {
//				grid[x][y].setBackground(Color.gray);
//			}
//		}
//		
//	}
//	
//	//Mouse Listener for Navigation Buttons
//	class NavListener implements MouseListener{
//		
//		@Override
//		public void mouseClicked(MouseEvent e) {
//			if(e.getSource() == navButtons[0]) {
//				//frame.add(new CirclePanel1());
//				System.out.println("Panel 1 added");
//				navButtons[1].setEnabled(false); //disable Part 2 Button
//			}
//			else {
//				//frame.add(new CirclePanel2());
//				System.out.println("Panel 2 added");
//				navButtons[0].setEnabled(false); //disable Part 1 Button
//				navButtons[1].setEnabled(false); //disable Part 2 Button
//				navButtons[2].setEnabled(true); //enable "Generate" Button
//			}
//			
//			//iterate through list of buttons
//			for(int i = 0; i < 20; i++) {
//				for(int j = 0; j < 20; j++) {
//					if(e.getSource() == navButtons[0]) { //if source = Part 1 Button
//						//System.out.println("Button Ready");
//						grid[i][j].addMouseListener(new Part1Listener(i, j)); //add listener
//						grid[i][j].addMouseMotionListener(new Part1Listener(i, j)); //add listener
//					}
//					else if(e.getSource() == navButtons[1]){ //if source = Part 2 Button
//						//frame.add(new CirclePanel2());
//						grid[i][j].addMouseListener(new Part2Listener(i, j)); //add listener
//					}
//				}
//			}
//		}
//
//		@Override
//		public void mousePressed(MouseEvent e) {}
//
//		@Override
//		public void mouseReleased(MouseEvent e) {}
//
//		@Override
//		public void mouseEntered(MouseEvent e) {}
//
//		@Override
//		public void mouseExited(MouseEvent e) {}
//		
//	}
	//*************************************
}
	

//Panel that draws circles for Part 1
class CirclePanel1 extends JPanel{	
	
	@Override
	protected void paintComponent(Graphics g) {
		//set up the brush
		super.paintComponent(g); 
		this.setOpaque(false);
		
		//Blue Circle
		g.setColor(Color.blue);
		g.drawOval((int) CircleGridDemo.btnCoo.x - (int)Math.round(CircleGridDemo.currentRadius1)/2, 
				(int) CircleGridDemo.btnCoo.y - (int)Math.round(CircleGridDemo.currentRadius1)/2, 
				(int)Math.round(CircleGridDemo.currentRadius1), 
				(int)Math.round(CircleGridDemo.currentRadius1));
		
		//Red Circles
		g.setColor(Color.red);
		g.drawOval((int) CircleGridDemo.btnCoo.x - (int)Math.round(CircleGridDemo.farthestRadius1), 
				(int) CircleGridDemo.btnCoo.y - (int)Math.round(CircleGridDemo.farthestRadius1), 
				(int)Math.round(CircleGridDemo.farthestRadius1)*2, 
				(int)Math.round(CircleGridDemo.farthestRadius1)*2);
		g.drawOval((int) CircleGridDemo.btnCoo.x - (int)Math.round(CircleGridDemo.closestRadius1), 
				(int) CircleGridDemo.btnCoo.y - (int)Math.round(CircleGridDemo.closestRadius1), 
				(int)Math.round(CircleGridDemo.closestRadius1)*2, 
				(int)Math.round(CircleGridDemo.closestRadius1)*2);
	}
}

// ************* IGNORE ***************

//Panel that draws a circle for Part 2
//class CirclePanel2 extends JPanel{	
//	
//	@Override
//	protected void paintComponent(Graphics g) {
//		super.paintComponent(g);		
//		this.setOpaque(false);
//		
//		//Blue Circle
//		g.setColor(Color.blue);
//		g.drawOval((int) CircleGridDemo.btnCoo.x, 
//				(int)CircleGridDemo.btnCoo.y, 
//				(int)Math.round(CircleGridDemo.currentRadius2), 
//				(int)Math.round(CircleGridDemo.currentRadius2));
//		System.out.print("Circle 2 printed");
//	}
//}
// **************************************



	





