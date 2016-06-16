package SwimmyFish.objects;

import java.awt.Image;
import java.awt.Toolkit;

import SwimmyFish.graphics.GameDriver;

public class CoralObstacle {
	
	Image lowerCoral, upperCoral;
	int uXRight, uXLeft, uYTop, uYBottom;
	int lXRight, lXLeft, lYTop, lYBottom;
	
	public CoralObstacle()
	{
		lowerCoral = Toolkit.getDefaultToolkit().getImage(GameDriver.class.getResource("/SwimmyFish/resources/CoralTall.png"));
		lXLeft = GameDriver.screenWidth/2;
		lXRight = lXLeft + 63;
		lYTop = 450;
		lYBottom = 600;
		uYTop = 0;
		uYBottom = 0;
		uXRight = 0;
		uXLeft = 0;
	}
	
	public CoralObstacle(int h)
	{
		
	}
	
	public void updatePos()
	{
		lXLeft-=1;
		lXRight-=1;
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
}
