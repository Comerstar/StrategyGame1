package animation.sprite;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.Point;
import java.util.ArrayList;
import java.util.HashMap;

import javax.swing.ImageIcon;

import animation.AnimationState;
import animation.Camera;
import animation.layer.GridLayer;
import animation.layer.Layer;
import animation.layer.TextBox;

public interface SpriteInterface
{
	
	public Sprite clone();
	
	public AnimationState getAnimationState();
	
	public void setState(String state);
	
	public void setGridX(int x);
	
	public void setGridY(int y);
	
	public void setGridLocation(int x, int y);
	
	public void setAnimated(boolean animated);
	
	public DynamicTile getDynamicTile();
	
	public Sprite getSprite();
	
	public Button getButton();
	
	public TextBox getTextBox();

	public Layer getLayer();

	public GridLayer getGridLayer();
	
	public int getWidth();
	
	public int getHeight();
	
	public void setWidth(int width);
	
	public void setHeight(int height);
	
	public String getState();
	
	public HashMap<String, ArrayList<Image>> getImg();
	
	public void incrementTimer();
	
	public String[] getStateList();
	
	public int getX();
	
	public int getY();
	
	public int getGridX();
	
	public int getGridY();
	
	public int getAnimationTimer();
	
	public void setInvisible();
	
	public void setVisible();
	
	public boolean getVisible();
	
	public boolean getAnimated();
	
	public boolean getZoom();
	
	public ImageIcon getImageIconFromURL(String dir);
	
	public Image getImageFromURL(String dir);
	
	public Image getImage(int number);

	@Deprecated
	public void drawImageZoom(Graphics g, Camera c);

	@Deprecated
	public void drawImageZoom(Graphics g, Camera c, int lx, int ly, double lz);

	@Deprecated
	public void drawImageNoZoom(Graphics g, Camera c);

	@Deprecated
	public void drawImageNoZoom(Graphics g, Camera c, int lx, int ly, double lz);
	
	public void drawImage(Graphics g, Camera c);
	
	public void drawImage(Graphics g, Camera c, int lx, int ly, double lz);
	
	public void drawPrintImage(Graphics g, Camera c, int lx, int ly, double lz);
	
	public void increaseX(int x);
	
	public void setX(int x);
	
	public void multiplyX(int x);
	
	public void increaseY(int y);
	
	public void setY(int y);
	
	public void multiplyY(int y);
	
	public void translate(int x, int y);
	
	public void setLocation(int x, int y);
	
	public void setLocation(double x, double y);
	
	public void setCentreLocation(double x, double y);
	
	public void setZoom(boolean zoom);
	
	@Deprecated
	public boolean checkIfInZoom(Point p, Camera c);

	@Deprecated
	public boolean checkIfInZoom(int px, int py, Camera c);
	
	public boolean checkIfIn(Point p, Camera c, int lx, int ly, double lz);

	public int getX1(Camera c, int lx, double lz);

	public int getX2(Camera c, int lx, double lz);

	public int getY1(Camera c, int ly, double lz);

	public int getY2(Camera c, int ly, double lz);
	
	public boolean checkIfIn(int px, int py, Camera c, int lx, int ly, double lz);
}
