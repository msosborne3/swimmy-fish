package SwimmyFish.graphics;

import java.awt.BorderLayout;
import java.awt.Canvas;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.geom.AffineTransform;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Random;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.Renderer;
import javax.swing.Timer;
import javax.swing.UIManager;
import javax.swing.border.EmptyBorder;
import SwimmyFish.objects.*;
import java.awt.Dimension;

@SuppressWarnings("serial")
public class GameDriver extends JFrame {
	
	private enum GameState {Menu, Tutorial, Gameplay, Paused, Gameover}	// All possible gamestates
	private GameState gameState; // represents the current state of the game
	
	private SFPanel contentPane;
	private SFActionListener timerListener;
	private SFKeyListener keyListener;
	private Timer timer;
	private Random rand;	
	private Nemo nemo;
	private Deque<CoralObstacle> coralDeque; // holds all the obstacles on screen
	private HighScore highScore;
	private int score; // score for the current game
	private Deque<OceanFloor> oceanFloors; // oceanFloor objects
	
	private final int GAMESPEED = 20; // 3000 ms = 3 s
	private final int NUMOBSTACLEHEIGHTS = 2;
	
	//public static fields
	public static final int screenWidth = 500;
	public static final int screenHeight = 600;
	
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Throwable e) {
			e.printStackTrace();
		}
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					GameDriver frame = new  GameDriver();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Initializes JFrame and all variables/components.
	 */
	public GameDriver() {
		getContentPane().setFont(new Font("Arial", Font.PLAIN, 11));
		setIconImage(Toolkit.getDefaultToolkit().getImage(GameDriver.class.getResource("/SwimmyFish/resources/nemo.png")));
		setResizable(false);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(350, 50, 500, 600); // 516, 639
		contentPane = new SFPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(new BorderLayout(0, 0));
		
		gameState = GameState.Menu; // start the game at the menu
		timerListener = new SFActionListener(); // initialize our defined Timer
		timer = new Timer(GAMESPEED, timerListener); // set for 3 second intervals - I think
		rand = new Random();
		coralDeque = new ArrayDeque<CoralObstacle>();
		oceanFloors = new ArrayDeque<OceanFloor>(); 
		importHighScore(); // attempt to import high score
		keyListener = new SFKeyListener(); // initialize our keylistener
		contentPane.addKeyListener(keyListener); // listen for keyevents
		contentPane.setFocusable(true); // give focus to the contentPane - so we can 'hear' key events
	}
	
	/**
	 * Checks for any collisions between the play and
	 * any obstacles.
	 * 
	 * @return boolean indicating whether or not a collision was detected.
	 */
	private boolean collisionDetection()
	{
		// check for collision with ceiling
		if (nemo.getYTop() <= 0)
			return true;
		// check for collision with floor
		if (nemo.getYBottom() >= screenHeight-125)
			return true;
		// check for collision with each of the obstacles
		for (CoralObstacle coral : coralDeque)
		{
			// check collision with upper coral
			if (checkUpperCoral(coral))
				return true;
			if (checkLowerCoral(coral))
				return true;
		}

		// no collision detected
		return false;
	}
	
	/**
	 * Checks for a collision between the player and the upper
	 * coral of a CoralObstacle.
	 * @param CoralObstacle to check for collision.
	 * @return boolean indicating whether or not a collision was detected
	 */
	private boolean checkUpperCoral(CoralObstacle coral)
	{
		if (nemo.getXRight()-4 >= coral.getULeft()
				&& nemo.getXRight()-4 <= coral.getURight()
				&& nemo.getYTop()+8 <= coral.getUBottom())
			return true;
		else if (nemo.getXLeft() >= coral.getULeft()
				&& nemo.getXLeft() <= coral.getURight()
				&& nemo.getYTop()+8 <= coral.getUBottom())
			return true;
		else if (nemo.getXLeft() <= coral.getULeft()
				&& nemo.getXRight()-4 >= coral.getURight()
				&& nemo.getYTop()+8 <= coral.getUBottom())
			return true;
		
		return false;
	}
	
	/**
	 * Checks for a collision between the player and the lower
	 * coral of a CoralObstacle.
	 * @param CoralObstacle to check for collision.
	 * @return boolean indicating whether or not a collision was detected
	 */
	private boolean checkLowerCoral(CoralObstacle coral)
	{
		if (nemo.getXRight()-4 >= coral.getlLeft()
				&& nemo.getXRight()-4 <= coral.getlRight()
				&& nemo.getYBottom()-8 >= coral.getLTop())
			return true;
		else if (nemo.getXLeft() >= coral.getlLeft()
				&& nemo.getXLeft() <= coral.getlRight()
				&& nemo.getYBottom()-8 >= coral.getLTop())
			return true;
		else if (nemo.getXLeft() <= coral.getlLeft()
				&& nemo.getXRight()-4 >= coral.getlRight()
				&& nemo.getYBottom()-8 >= coral.getLTop())
			return true;
		
		return false;
	}
	
	/**
	 * Updates the positions of all the corals and the player.
	 */
	private void updateObjectPositions()
	{
		nemo.updatePos();
		
		for (CoralObstacle coral: coralDeque)
			coral.updatePos();
		for (OceanFloor floor: oceanFloors)
			floor.updatePos();
	}
	
	/**
	 * Adds one to the players score if the tail of nemo
	 * is in line with the back edge of the obstacle.
	 */
	private void updateScore()
	{
		for (CoralObstacle coral : coralDeque)
		{
			if (nemo.getXLeft() == coral.getlRight())
				score++;
		}
	}
	
	/**
	 * Creates a new Nemo object
	 * Removes all elements in coralDeque
	 * Changes gameState to Gameplay
	 * Restarts the timer
	 */
	private void startNewGame()
	{
		//create new player
		nemo = new Nemo();
		//reset score
		score = 0;
		// remove all coralObstacles from deque
		while(coralDeque.size() > 0)
			coralDeque.removeLast();
		
		//remove all oceanFloors from deque
		while(oceanFloors.size() > 0)
			oceanFloors.removeLast();
		
		// create initial ocean floor instance
		oceanFloors.addFirst(new OceanFloor());
		//change the GameState
		gameState = GameState.Gameplay;
		//start the timer
		timer.restart();	
	}
	
	/**
	 * Saves the HighScore to highscore.ser
	 */
	private void saveHighScore()
	{
		try
		{
			File file = new File("highscore.ser");
			if (!file.exists())
				file.createNewFile();
			FileOutputStream fileOut = // file destination 
					new FileOutputStream(file, false);
			ObjectOutputStream out = new ObjectOutputStream(fileOut); //
			out.writeObject(highScore); // writes the object to location above - saved as an OBJECT - not a HighScore
			out.close();
			fileOut.close();
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}
	
	/**
	 * Attempts to import a HighScore object from:
	 * "highscore.ser"
	 * If the file cannot be found, a new highScore object is created.
	 */
	private void importHighScore()
	{
		try
		{
			FileInputStream fileIn = // file to read from
					new FileInputStream("highscore.ser"); 
			ObjectInputStream in = new ObjectInputStream(fileIn);
			highScore = (HighScore)in.readObject(); // it is stored as an OBJECT - so cast
			in.close();
			fileIn.close();
		}
		catch (Exception e)
		{
			// there was a problem importing - create a new highscore object
			highScore = new HighScore();
		}
	}
	
	/**
	 * Timer ActionListener
	 */
	private class SFActionListener implements ActionListener
	{
		private int numTicks = 0;
		private final int TIMEBETWEENOBSTACLES = 250;
		
		
		/**
		 * This method defines what the game does with each tick event.
		 */
		@Override
		public void actionPerformed(ActionEvent e) {
			// update object positions.
			updateObjectPositions();
			// update score
			updateScore();
			// remove corals off screen
			removeOffScreenCorals();
			// remove ocean floors off screen
			removeOffScreenOceanFloors();
			// repaint the screen
			contentPane.repaint();
			
			// spawn new obstacle of random height - every x seconds
			if (numTicks % TIMEBETWEENOBSTACLES == 0)
				createNewObstacle();
			
			for (OceanFloor floor: oceanFloors)
				if (floor.getLeft() == 0)
					createNewOceanFloor();
			
			// check for any collisions
			if (collisionDetection())
			{
				endGame();
				numTicks = -1; // reset the counter
				System.out.println("Collision Detected");
			}
			numTicks++; // update the tick counter
		}
	}
	
	/**
	 * Ends the current game and
	 */
	private void endGame()
	{
		gameState = GameState.Gameover; // The player lost - gameover
		timer.stop(); // stop the timer
		highScore.updateHighScore(score); // see if they got a new high score
		saveHighScore();
	}
	
	/*
	 * Creates a new coralObstacle instance
	 * and stores it in coralDeque.
	 */
	private void createNewObstacle()
	{
		coralDeque.addFirst(new CoralObstacle(rand.nextInt(NUMOBSTACLEHEIGHTS), rand.nextInt(2)));
	}
	
	/**
	 * Creates a new OceanFloor instance
	 * and stores it in oceanFloors
	 */
	private void createNewOceanFloor()
	{
		oceanFloors.addFirst(new OceanFloor(500));
	}
	/**
	 * Deletes the last coralObstacle in coralDeque if
	 * it is off screen.
	 */
	private void removeOffScreenCorals()
	{	
		// is the right x value off screen?
		if (coralDeque.size() > 0 && 
				coralDeque.peekLast().getlRight() < 0) // less than 0 to make sure there is no weird graphics issue
			coralDeque.removeLast(); // removes the last coral if it is off screen
	}	
	
	/**
	 * Deletes the last OceanFloor in oceanFloors if
	 * it is off screen
	 */
	private void removeOffScreenOceanFloors()
	{
		if (oceanFloors.size() > 0 &&
				oceanFloors.peekLast().getRight() < 0)
			oceanFloors.removeLast();
	}
	/**
	 * Handles key events
	 */
	private class SFKeyListener implements KeyListener
	{
		@Override
		public void keyReleased(KeyEvent arg0) {
			
			switch (gameState)
			{
			case Menu:
				if (arg0.getKeyCode() == KeyEvent.VK_SPACE)
				{
					startNewGame();
				}
				else if (arg0.getKeyCode() == KeyEvent.VK_T)
				{
					gameState = GameState.Tutorial;
					contentPane.repaint();
				}
				break;
			case Gameplay:
				// Press space to swim & press p to pause
				if (arg0.getKeyCode() == KeyEvent.VK_SPACE){
					nemo.swim();
				}
				else if (arg0.getKeyCode() == KeyEvent.VK_P)
				{
					// pause game
					timer.stop();
					gameState = GameState.Paused;
					// repaint panel
					contentPane.repaint();
				}
				
				break;
			case Paused:
				// Press p to unpause
				if (arg0.getKeyCode() == KeyEvent.VK_P)
				{
					// resume game
					gameState = GameState.Gameplay;
					timer.start();
				}
				
				break;
			case Tutorial:
				// Press esc or space or b to go back to menu
				if (arg0.getKeyCode() == KeyEvent.VK_SPACE // space
				|| arg0.getKeyCode() == KeyEvent.VK_ESCAPE // esc
				|| arg0.getKeyCode() == KeyEvent.VK_B) // b
				{
					gameState = GameState.Menu;
					contentPane.repaint();
				}
				break;
			case Gameover:
				// Pressing space restarts the game
				if (arg0.getKeyCode() == KeyEvent.VK_SPACE)
					startNewGame();
				break;
			default:
				// fail safe
				gameState = GameState.Menu;
				contentPane.repaint();
				break;
			}
		}
		
		@Override
		public void keyPressed(KeyEvent arg0) {
			// We are not interested in this event
			
		}

		@Override
		public void keyTyped(KeyEvent arg0) {
			// We are not interested in this event
			
		}
		
	}
	
	
	private class SFPanel extends JPanel
	{
		@Override
		public void paintComponent(Graphics g)
		{
			switch (gameState) {
			
			case Menu:
				
				Image bg = Toolkit.getDefaultToolkit()
						.getImage(GameDriver.class.getResource("/SwimmyFish/resources/MainMenu.png"));
				g.drawImage(bg, 0, 0, this);
				
				g.setFont(new Font("Helvetica Neue", Font.PLAIN, 20));
				g.drawString(String.valueOf("High Score: " + highScore.getHighScore()),
						screenWidth/2 - 65, 360);
				break;

			case Tutorial:

				// This needs a new image (tutorial)
				Image howToPlay = Toolkit.getDefaultToolkit()
						.getImage(GameDriver.class.getResource("/SwimmyFish/resources/HowToPlay.png"));
				g.drawImage(howToPlay, 0, 0, this);
				g.setFont(new Font("Helvetica Neue", Font.PLAIN, 20));
				g.drawString("Press 'b' to go back.", screenWidth/2 - 90, 135);
				g.drawString("Press 'p' to pause.", screenWidth/2 - 75, screenHeight/2-13);
				
				break;

			case Gameplay:
				Image bg1 = Toolkit.getDefaultToolkit()
						.getImage(GameDriver.class.getResource("/SwimmyFish/resources/background.png"));
				g.drawImage(bg1, 0, 0, this);
				
				drawFloors(g);
				drawNemo(g);
				drawCorals(g);
				
				//display score
				g.setFont(new Font("Helvetica Neue", Font.PLAIN, 60));
				g.drawString(String.valueOf(score), 15, 50);
				break;

			case Paused:
				Image bg2 = Toolkit.getDefaultToolkit()
						.getImage(GameDriver.class.getResource("/SwimmyFish/resources/background.png"));
				g.drawImage(bg2, 0, 0, this);
				
				drawFloors(g);
				drawNemo(g);
				drawCorals(g);

				g.setFont(new Font("Helvetica Neue", Font.PLAIN, 60));
				g.drawString("Paused", screenWidth/2 - 100, screenHeight/2);
				g.setFont(new Font("Helvetica Neue", Font.PLAIN,20));
				g.drawString("Press 'p' to continue", screenWidth/2-87, screenHeight/2+50);
				break;

			case Gameover:
				Image bg3 = Toolkit.getDefaultToolkit()
						.getImage(GameDriver.class.getResource("/SwimmyFish/resources/GameOver.png"));
				g.drawImage(bg3, 0, 0, this);

				drawFloors(g);
				drawNemo(g);
				drawCorals(g);
				
				g.setFont(new Font("Helvetica Neue", Font.PLAIN, 20));
				g.drawString(String.valueOf(score), screenWidth/2-5, screenHeight/2-42);
				g.drawString(String.valueOf(highScore.getHighScore()), screenWidth/2-10, screenHeight/2+8);
				break;
			default:
				break;
			}
			g.dispose();
		}
		
		/**
		 * Draws nemo at its current position
		 * @param Graphics g
		 */
		private void drawNemo(Graphics g)
		{
			g.drawImage(nemo.getImage(), nemo.getXLeft(), nemo.getYTop(), this);
		}
		
		/**
		 * Draws all the corals in coralDeque
		 * at their current location
		 * @param Grapihcs g
		 */
		private void drawCorals(Graphics g)
		{
			

			for(CoralObstacle coral: coralDeque)
			{
				// bottom
				g.drawImage(coral.getLowerImage(), coral.getlLeft(), coral.getLTop(), this);
				//top
				g.drawImage(coral.getUpperImage(), coral.getULeft(), coral.getUTop(), this);
			}
		}
		
		private void drawFloors(Graphics g)
		{
			for (OceanFloor floor: oceanFloors)
				g.drawImage(floor.getImage(), floor.getLeft(), floor.getTop(), this);
		}
	}
	
}
