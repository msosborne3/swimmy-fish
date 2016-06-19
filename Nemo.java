package SwimmyFish.objects;

import java.awt.Image;
import java.awt.Toolkit;

import SwimmyFish.graphics.GameDriver;

public class Nemo {

	private Image swim, swimming;
	private int XLEFT = 0;
	private int XRIGHT = 0;
	private final int GRAVITY = 9;
	private int yTop, yBottom, yAccel;
	
	public Nemo()
	{
		swim = Toolkit.getDefaultToolkit().getImage(GameDriver.class.getResource("/SwimmyFish/resources/nemo.png"));
		yTop = 500;
		yBottom = 85;
		XLEFT = 0;
		XRIGHT = 80;
	}
	
	public Image getImage()
	{
		return swim;
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
		return yTop;
	}
	
	public int getYBottom()
	{
		return yBottom;
	}
	
	public void updatePos()
	{
		XLEFT+=1;
		XRIGHT+=1;
		return;
	}
	
	public void swim()
	{
		yTop-=10;
		return;
	}
}
