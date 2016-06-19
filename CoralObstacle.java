package SwimmyFish.objects;

import java.awt.Image;
import java.awt.Toolkit;

import SwimmyFish.graphics.GameDriver;

/**
 * Places the coral obstacles on the top and
 * bottom of the screen.
 * 
 * @author Larry Preuett
 * @version 1.0
 */

public class CoralObstacle {
	
	private Image lowerCoral, upperCoral;
	private int uXRight, uXLeft, uYTop, uYBottom;
	private int lXRight, lXLeft, lYTop, lYBottom;
	
	private final int TALLCORALHEIGHT = 169;
	private final int SHORTCORALHEIGHT = 90;
	private final int MEDIUMCORALHEIGHT = 143;
	private final int CORALWIDTH = 63;
	
	public CoralObstacle()
	{
		lowerCoral = Toolkit.getDefaultToolkit().getImage(GameDriver.class.getResource("/SwimmyFish/resources/LowerCoralTall.png"));
		lXLeft = GameDriver.screenWidth;
		lXRight = lXLeft + 63;
		lYTop = 306;
		lYBottom = 600;
		uYTop = 0;
		uYBottom = 0;
		uXRight = 0;
		uXLeft = 0;
	}
	
	public CoralObstacle(int height, int tallPlacement)
	{
		// universal variable settings
		lXLeft = GameDriver.screenWidth;
		lXRight = GameDriver.screenWidth + 63;
		uXLeft = GameDriver.screenWidth;
		uXRight = GameDriver.screenWidth + 63;
		uYTop = 0;
		lYBottom = 475;
		
		switch (height) 
		{
		case 0: // tall and short
			switch (tallPlacement) //******* nested switch
			{
			case 0: // tall on bottom
				lowerCoral = Toolkit.getDefaultToolkit().getImage
					(GameDriver.class.getResource("/SwimmyFish/resources/LowerCoralTall.png"));
				upperCoral = Toolkit.getDefaultToolkit().getImage
						(GameDriver.class.getResource("/SwimmyFish/resources/UpperCoralShort.png"));
				lYTop = lYBottom - TALLCORALHEIGHT;
				uYBottom = SHORTCORALHEIGHT;
				break;
			case 1: // tall on top
				lowerCoral = Toolkit.getDefaultToolkit().getImage
					(GameDriver.class.getResource("/SwimmyFish/resources/LowerCoralShort.png"));
				upperCoral = Toolkit.getDefaultToolkit().getImage
					(GameDriver.class.getResource("/SwimmyFish/resources/UpperCoralTall.png"));
				lYTop = lYBottom - SHORTCORALHEIGHT;
				uYBottom = TALLCORALHEIGHT;
				break;
			default:
				break;
			} //****************** end nested switch
			break;
		case 1: // medium
			lowerCoral = Toolkit.getDefaultToolkit().getImage
				(GameDriver.class.getResource("/SwimmyFish/resources/LowerCoralMedium.png"));
			upperCoral = Toolkit.getDefaultToolkit().getImage
				(GameDriver.class.getResource("/SwimmyFish/resources/UpperCoralMedium.png"));
			lYTop = lYBottom - MEDIUMCORALHEIGHT;
			uYBottom = MEDIUMCORALHEIGHT;
			break;
		default:
			break;
		}
	}
	
	public void updatePos()
	{
		lXLeft-=1;
		lXRight-=1;
		uXLeft-=1;
		uXRight-=1;
		return;
	}
	
	
	public Image getLowerImage()
	{
		return lowerCoral;
	}
	
	public Image getUpperImage()
	{
		return upperCoral;
	}
	
	public int getULeft()
	{
		return uXLeft;
	}
	
	public int getURight()
	{
		return uXRight;
	}
	public int getUBottom()
	{
		return uYBottom;
	}
	
	public int getUTop()
	{
		return uYTop;
	}
	
	public int getlLeft()
	{
		return lXLeft;
	}
	public int getlRight()
	{
		return lXRight;
	}
	public int getLTop()
	{
		return lYTop;
	}
	public int getLBottom()
	{
		return lYBottom;
	}
}
