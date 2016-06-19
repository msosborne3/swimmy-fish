package SwimmyFish.objects;

import java.io.Serializable;

/**
 * Stores a high score.
 * Saves the score to a .txt file.
 * Imports the score if a .txt file can be found.
 * 
 * @author Larry Preuett
 * @version 1.0
 */
public class HighScore implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 4711766892328652525L;
	private int score;
	
	public HighScore() 
	{
		score = 0;
	}
	
	/**
	 * Updates score if the newScore passed is larger than the current score.
	 * @param newScore New score to compare to current score.
	 */
	public void updateHighScore(int newScore)
	{
		if (newScore > score)
			score = newScore;
	}
	
	/**
	 * Returns the current high score.
	 * @return score
	 */
	public int getHighScore()
	{
		return score;
	}
	
	private void saveHighScore()
	{
		
	}
	
	private void importHighScore(){}
}
