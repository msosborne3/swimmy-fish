package SwimmyFish.graphics;

import java.awt.BorderLayout;
import java.awt.Canvas;
import java.awt.Cursor;
import java.awt.EventQueue;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
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
import javax.swing.JPanel;
import javax.swing.Timer;
import javax.swing.UIManager;
import javax.swing.border.EmptyBorder;
import SwimmyFish.objects.*;
import java.awt.Dimension;

public class GameDriver extends JFrame {
	
	private enum GameState {Menu, Tutorial, Gameplay, Paused, Gameover}	// All possible gamestates
	private GameState gameState; // represents the current state of the game
	
	private JPanel contentPane;
	private final SFCanvas canvas = new SFCanvas();
	private SFActionListener timerListener;
	private SFKeyListener keyListener;
	private Timer timer;
	private Random rand;	
	private Nemo nemo;
	private Deque<CoralObstacle> coralDeque; // holds all the obstacles on screen
	private HighScore highScore;
	private int score; // score for the current game
	
	private final int GAMESPEED = 30; // 3000 ms = 3 s
	private final int NUMOBSTACLEHEIGHTS = 4;
	
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
					GameDriver frame = new GameDriver();
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
		setIconImage(Toolkit.getDefaultToolkit().getImage(GameDriver.class.getResource("/SwimmyFish/resources/nemo.png")));
		setResizable(false);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 516, 639);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(new BorderLayout(0, 0));
		canvas.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
		contentPane.add(canvas, BorderLayout.CENTER);
		
		gameState = GameState.Menu; // start the game at the menu
		timerListener = new SFActionListener(); // initialize our defined Timer
		timer = new Timer(GAMESPEED, timerListener); // set for 3 second intervals - I think
		rand = new Random(NUMOBSTACLEHEIGHTS); // set for 4 different heights
		newNemo(); // initialize player
		coralDeque = new ArrayDeque<CoralObstacle>();
		importHighScore(); // attempt to import high score
		score = 0;
		keyListener = new SFKeyListener(); // initialize our keylistener
		canvas.addKeyListener(keyListener); // listen for key events in canvas
		canvas.setFocusable(true); // gives focus to the canvas - so we dont need to click on it for it to accept input
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
		if (nemo.getYBottom() >= screenHeight)
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
		if (nemo.getXRight() >= coral.getULeft() 
				&& nemo.getXRight() <= coral.getURight()
				&& nemo.getYTop() <= coral.getUBottom())
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
		if (nemo.getXRight() >= coral.getlLeft()
				&& nemo.getXRight() <= coral.getlRight()
				&& nemo.getYBottom() >= coral.getLTop())
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
	 * Stores a new Nemo object in nemo
	 */
	private void newNemo()
	{
		nemo = new Nemo();
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
		private final int TIMEBETWEENOBSTACLES = 200;
		
		
		/**
		 * This method defines what the game does with each tick event.
		 */
		@Override
		public void actionPerformed(ActionEvent e) {
			// update object positions.
			updateObjectPositions();
			// update score
			updateScore();
			// remove corals off screen - it will save us several checks later
			removeOffScreenCorals();
			
			// repaint the screen
			canvas.repaint();
			
			// spawn new obstacle of random height - every x seconds
			if (numTicks % TIMEBETWEENOBSTACLES == 0)
				coralDeque.addFirst(new CoralObstacle());
			
			// check for any collisions
			if (collisionDetection())
			{
				gameState = GameState.Gameover; // The player lost - gameover
				timer.stop(); // stop the timer
				highScore.updateHighScore(score); // see if they got a new high score
				saveHighScore();
				numTicks = 0; // reset the counter
				
				System.out.println("Collision Detected");
			}
			
			//System.out.println("Tick Tock");
			
			numTicks++; // update the tick counter
		}
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
					gameState = GameState.Gameplay;
					timer.start();
				}
				else if (arg0.getKeyCode() == KeyEvent.VK_T)
					gameState = GameState.Tutorial;
				break;
			case Gameplay:
				// Press space to swim & press p to pause
				if (arg0.getKeyCode() == KeyEvent.VK_SPACE)
					nemo.swim();
				else if (arg0.getKeyCode() == KeyEvent.VK_P)
				{
					// pause game
					timer.stop();
					gameState = GameState.Paused;
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
					gameState = GameState.Menu;
				break;
			case Gameover:
				// Pressing space restarts the game
				if (arg0.getKeyCode() == KeyEvent.VK_SPACE)
				{
					gameState = GameState.Gameplay;
					while (!coralDeque.isEmpty()) // remove all coral objects
						coralDeque.removeLast(); // removeLast == removeFirst for this purpose
					newNemo(); // create a new nemo object
					timer.restart();
				}
				break;
			default:
				// fail safe
				gameState = GameState.Menu;
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
	
	private class SFCanvas extends Canvas
	{
		
		@Override
		public void paint(Graphics g)
		{
			Image bg = Toolkit.getDefaultToolkit().getImage(GameDriver.class.getResource("/SwimmyFish/resources/background.png"));
			g.drawImage(bg, 0, 0, this);
			g.drawImage(nemo.getImage(), nemo.getXLeft(), nemo.getYTop(), this);
			
			for (CoralObstacle coral: coralDeque)
				g.drawImage(coral.getLowerImage(), coral.getlLeft(), coral.getLTop(), this);
		}
	}
	
}
