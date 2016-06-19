package SwimmyFish.objects;

import java.awt.Image;
import java.awt.Toolkit;


import SwimmyFish.graphics.GameDriver;


/**
 * Creates the fish and allows it to swim.
 * Allows the fish to accelerate and to fall
 * with gravity.
 * 
 * @author Zane Guess, Larry Preuett, and Morgan Osborne
 * @version 1.0
 */

public class Nemo {

	private Image swim, swimming;
	private int XLEFT = 0;
	private int XRIGHT = 0;
	private final double GRAVITY = 0.25;
	private double yTop, yBottom, yAccel;
	private int swimCount;
	
	private final int NEMOWIDTH = 81;
	private final int NEMOHEIGHT = 53;
	private final int SWIMCOUNTMAX = 15;
	
	public Nemo()
	{
		swim = Toolkit.getDefaultToolkit().getImage(GameDriver.class.getResource("/SwimmyFish/resources/nemo.png"));
		swimming = Toolkit.getDefaultToolkit().getImage(GameDriver.class.getResource("/SwimmyFish/resources/NemoSwimming.png"));
		yTop = GameDriver.screenHeight/2 -50;
		yBottom = yTop + NEMOHEIGHT;
		XLEFT = GameDriver.screenWidth/2 - NEMOWIDTH;
		XRIGHT = XLEFT + NEMOWIDTH;
		yAccel = 0;
		swimCount = 0;
	}
	
	public boolean isFish(int x, int y, int quadrant)
	{
		// image center: NEMOWIDTH/2, NEMOHEIGHT/2
		int xd =0;
		int yd = 0;
		int delta = 0;
		switch(quadrant)
		{
		case 1:
			// x units from the right side
			// y units from the top
			xd = NEMOWIDTH-x-NEMOWIDTH/2;
			yd = NEMOHEIGHT/2-y;
			delta = (int)Math.sqrt(Math.pow(xd, 2) + Math.pow(yd,2));
			return delta <= 25 ? true : false; 
		case 2:
			// x units from left
			// y units from top
			xd = NEMOWIDTH/2-x;
			yd = NEMOHEIGHT/2-y;
			delta = (int)Math.sqrt(Math.pow(xd,2)+Math.pow(yd, 2));
			return delta <= 15 ? true : false;

		case 3:
			// ********UNUSED********
			// x units from left
			// y units from bottom
			xd = NEMOWIDTH/2-x;
			yd = NEMOHEIGHT-y-NEMOHEIGHT/2;
			delta = (int)Math.sqrt(Math.pow(xd,2)+Math.pow(yd, 2));
			return delta <= 15 ? true : false;
		case 4:
			// x units from right
			// y units from bottom
			xd = NEMOWIDTH-x-NEMOWIDTH/2;
			yd = NEMOHEIGHT-y-NEMOHEIGHT/2;
			delta = (int)Math.sqrt(Math.pow(xd,2)+Math.pow(yd, 2));
			return delta <= 35 ? true : false;
		default:
			return false;
		}
	}
	
	public Image getImage()
	{	
		return (swimCount > 0) ? swimming : swim;
	}
	
	public int getXLeft()
	{
		return XLEFT;
	}
	
	public int getXRight()
	{
		return XRIGHT;
	}
	
	public int getYTop()	
	{
		return (int)yTop;
	}
	
	public int getYBottom()
	{
		return (int)yBottom;
	}
	
	public void updatePos()
	{
		yTop+=yAccel;
		yBottom+=yAccel;
		yAccel+=GRAVITY;
		
		if (swimCount > 0)
			swimCount--;
	}
	
	public void swim()
	{
		yAccel -=8;
		swimCount = SWIMCOUNTMAX;
	}
}