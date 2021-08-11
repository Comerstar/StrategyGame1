package animation.sprite;

import java.awt.Point;

import animation.Camera;

public interface ButtonInterface extends SpriteInterface{
	public void setOver(Point p, Camera c, int lx, int ly, double lz);
	
	public void setOver(int px, int py, Camera c, int lx, int ly, double lz);
	
	public void setOver(boolean mouseOver);
	
	public boolean getMouseOver();
}
