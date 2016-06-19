package SwimmyFish.objects;

import java.awt.Image;
import java.awt.Toolkit;

import SwimmyFish.graphics.GameDriver;

/**
 * Places the ocean floor image on the
 * bottom of the screen.
 * 
 * @author Larry Preuett
 * @version 1.0
 */

public class OceanFloor {
	
	private Image image = 
			Toolkit.getDefaultToolkit().getImage(GameDriver.class.getResource("/SwimmyFish/resources/OceanFloor.png"));
	
	private final int TOP = 474;
	private final int BOTTOM = 496;
	private int left, right;
	
	public OceanFloor()
	{
		left = 1;
		right = 501;
	}
	
	public OceanFloor(int x)
	{
		left = x;
		right = left + 500;
	}
	public void updatePos()
	{
		left--;
		right--;
	}
	
	public Image getImage()
	{
		return image;
	}
	
	public int getTop()
	{
		return TOP;
	}
	
	public int getBottom()
	{
		return BOTTOM;
	}
	
	public int getLeft()
	{
		return left;
	}
	
	public int getRight()
	{
		return right;
	}

}
