package animation.sprite;

import java.awt.Point;

import animation.Camera;

public class Button extends Sprite implements ButtonInterface{
	
	private boolean mouseOver;
	
	public Button (String spriteType, String spriteName, String[] stateList, String state, boolean animated, boolean zoom, int width, int height)
	{
		super(spriteType, spriteName, stateList, state, animated, zoom, width, height);
		mouseOver = false;
	}
	
	public Button (String spriteType, String spriteName, String[] stateList, String state, boolean animated, boolean zoom, int width, int height, int x, int y)
	{
		super(spriteType, spriteName, stateList, state, animated, zoom, width, height, x, y);
		mouseOver = false;
	}
	
	public Button(String spriteType, String spriteName, String[] stateList, String state, boolean animated, boolean zoom, int width, int height, int x, int y, int gridX, int gridY)
	{
		super(spriteType, spriteName, stateList, state, animated, zoom, width, height, x, y, gridX, gridY);
		mouseOver = false;
	}
	
	public Button (String spriteType, String spriteName, String[] stateList, String[] endState, String state, boolean animated, boolean zoom, int width, int height, int x, int y)
	{
		super(spriteType, spriteName, stateList, endState, state, animated, zoom, width, height, x, y);
		mouseOver = false;
	}
	
	public Button(String spriteType, String spriteName, String[] stateList, String[] endState, String state, boolean animated, boolean zoom, int width, int height, int x, int y, int gridX, int gridY)
	{
		super(spriteType, spriteName, stateList, endState, state, animated, zoom, width, height, x, y, gridX, gridY);
		mouseOver = false;
	}
	
	public Button(Button button)
	{
		super(button);
		mouseOver = button.getMouseOver();
	}
	
	public Button clone()
	{
		return new Button(this);
	}
	
	public void setOver(Point p, Camera c, int lx, int ly, double lz)
	{
		mouseOver = checkIfIn(p, c, lx, ly, lz);
	}
	
	public void setOver(int px, int py, Camera c, int lx, int ly, double lz)
	{
		mouseOver = checkIfIn(px, py, c, lx, ly, lz);
	}
	
	public boolean getMouseOver()
	{
		return mouseOver;
	}
	
	public Sprite getSprite()
	{
		return this;
	}
	
	public Button getButton()
	{
		return this;
	}

	@Override
	public void setOver(boolean mouseOver) {
		this.mouseOver = mouseOver;
	}
}
