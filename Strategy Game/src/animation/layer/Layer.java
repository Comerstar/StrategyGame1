package animation.layer;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.Point;
import java.util.ArrayList;
import java.util.HashMap;

import javax.swing.ImageIcon;

import animation.AnimationState;
import animation.Camera;
import animation.sprite.Button;
import animation.sprite.ButtonInterface;
import animation.sprite.DynamicTile;
import animation.sprite.Sprite;
import animation.sprite.SpriteInterface;

public class Layer implements SpriteInterface{
	/**
	 * 
	 */
	protected ArrayList<SpriteInterface> spriteList;
	protected int x;
	protected int y;
	protected double scale;
	protected boolean visible;
	
	public Layer()
	{
		spriteList = new ArrayList<SpriteInterface>();
		x = 0;
		y = 0;
		scale = 1.0;
		visible = true;
	}
	
	public DynamicTile getDynamicTile()
	{
		return null;
	}
	
	protected Layer(int x, int y, double scale, boolean visible)
	{
		spriteList = new ArrayList<SpriteInterface>();
		this.x = x;
		this.y = y;
		this.scale = scale;
		this.visible = visible;
	}
	
	public Layer(ArrayList<SpriteInterface> spriteList)
	{
		this.spriteList = spriteList;
	}
	
	public Layer(ArrayList<Button> spriteList, int i)
	{
		this.spriteList = new ArrayList<SpriteInterface>();
		for(i = 0; i < spriteList.size(); i++)
		{
			this.spriteList.add(spriteList.get(i));
		}
	}
	
	public Layer(ArrayList<Button> spriteList, int i, double scale, boolean visible, int x, int y)
	{
		this.spriteList = new ArrayList<SpriteInterface>();
		for(i = 0; i < spriteList.size(); i++)
		{
			this.spriteList.add(spriteList.get(i));
		}
		this.scale = scale;
		this.visible = visible;
		this.x = x;
		this.y = y;
	}
	
	public Layer(ArrayList<ButtonInterface> spriteList, int i , int j)
	{
		this.spriteList = new ArrayList<SpriteInterface>();
		for(i = 0; i < spriteList.size(); i++)
		{
			this.spriteList.add(spriteList.get(i));
		}
	}
	
	public Layer(ArrayList<ButtonInterface> spriteList, double scale, int i, boolean visible, int x, int y)
	{
		this.spriteList = new ArrayList<SpriteInterface>();
		for(i = 0; i < spriteList.size(); i++)
		{
			this.spriteList.add(spriteList.get(i));
		}
		this.scale = scale;
		this.visible = visible;
		this.x = x;
		this.y = y;
	}
	
	public Layer(ArrayList<SpriteInterface> spriteList, double scale, boolean visible, int x, int y)
	{
		this.spriteList = spriteList;
		this.scale = scale;
		this.visible = visible;
		this.x = x;
		this.y = y;
	}
	
	public ArrayList<SpriteInterface> getSpriteList()
	{
		return spriteList;
	}
	
	public void drawLayer(Graphics g, Camera camera)
	{
		if(visible)
		{
			int i;
			for(i = 0; i < spriteList.size(); i++)
			{
				spriteList.get(i).drawImage(g, camera, x, y, scale);
			}
			//System.out.println("Drawing Layer");
		}
	}
	
	public void drawLayer(Graphics g, Camera camera, int i)
	{
		if(visible)
		{
			for(i = 0; i < spriteList.size(); i++)
			{
				spriteList.get(i).drawImage(g, camera, x, y, scale);
			}
			//System.out.println("Drawing Layer");
		}
	}
	
	public void drawPrintLayer(Graphics g, Camera camera)
	{
		int i;
		for(i = 0; i < spriteList.size(); i++)
		{
			spriteList.get(i).drawPrintImage(g, camera, x, y, scale);
			System.out.println("Sprite Visiblity: " + spriteList.get(i).getVisible());
		}
		//System.out.println("Drawing Layer");
		System.out.println("Layer Visibilty: " + visible);
	}
	
	public void increaseScale(double scale)
	{
		//System.out.println("Increasing Scale");
		this.scale += scale;
	}
	
	public double getScale()
	{
		return scale;
	}
	
	public void setScale(double scale)
	{
		this.scale = scale;
	}
	
	public void multiplyScale(double scale)
	{
		this.scale *= scale;
	}
	
	public void increaseX(int x)
	{
		this.x += x;
	}
	
	public void setX(int x)
	{
		this.x = x;
	}
	
	public void multiplyX(int x)
	{
		this.x *= x;
	}
	
	public void increaseY(int y)
	{
		this.y += y;
	}
	
	public void setY(int y)
	{
		this.y = y;
	}
	
	public void multiplyY(int y)
	{
		this.y *= y;
	}
	
	public void translate(int x, int y)
	{
		this.x += x;
		this.y += y;
	}
	
	public boolean notMasked(Point p, Camera c)
	{
		boolean notMasked = true;
		for(int i = 0; i < spriteList.size(); i++)
		{
			if(spriteList.get(i).checkIfIn(p, c, x, y, scale))
			{
				notMasked = false;
				break;
			}
		}
		return notMasked;
	}
	
	public void addSprite(Sprite sprite)
	{
		spriteList.add(sprite);
	}
	
	public void removeSprite(Sprite sprite)
	{
		spriteList.remove(sprite);
	}
	
	public void addSprite(SpriteInterface sprite)
	{
		spriteList.add(sprite);
	}
	
	public void removeSprite(SpriteInterface sprite)
	{
		spriteList.remove(sprite);
	}
	
	public void incrementTimer()
	{
		for(int i = 0; i < spriteList.size(); i++)
		{
			spriteList.get(i).incrementTimer();
		}
	}
	
	public void incrementTimer(int i)
	{
		for(i = 0; i < spriteList.size(); i++)
		{
			spriteList.get(i).incrementTimer();
		}
	}
	
	public void clearLayer()
	{
		spriteList.clear();
	}

	public void setAnimated(boolean animated)
	{
		
	}
	
	@Override
	public Sprite clone() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public AnimationState getAnimationState() {
		return null;
	}

	@Override
	public void setState(String state) {
		for(int i = 0; i < spriteList.size(); i++)
		{
			spriteList.get(i).setState(state);
		}
	}

	@Override
	public void setGridX(int x) {
		
	}

	@Override
	public void setGridY(int y) {
		
	}

	@Override
	public void setGridLocation(int x, int y) {
		
	}

	@Override
	public Sprite getSprite() {
		return null;
	}

	@Override
	public Button getButton() {
		return null;
	}

	@Override
	public TextBox getTextBox() {
		return null;
	}

	public Layer getLayer() {
		return this;
	}

	public GridLayer getGridLayer() {
		return null;
	}

	@Override
	public int getWidth() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int getHeight() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void setWidth(int width) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setHeight(int height) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public String getState() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public HashMap<String, ArrayList<Image>> getImg() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String[] getStateList() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int getX() {
		return x;
	}

	@Override
	public int getY() {
		return y;
	}

	@Override
	public int getGridX() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int getGridY() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int getAnimationTimer() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void setInvisible() {
		visible = false;
	}

	@Override
	public void setVisible() {
		visible = true;
	}

	@Override
	public boolean getVisible() {
		return visible;
	}

	@Override
	public boolean getAnimated() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean getZoom() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public ImageIcon getImageIconFromURL(String dir) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Image getImageFromURL(String dir) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Image getImage(int number) {
		// TODO Auto-generated method stub
		return null;
	}

	@Deprecated
	@Override
	public void drawImageZoom(Graphics g, Camera c, int lx, int ly, double lz) {
		if(visible)
		{
			int i;
			for(i = 0; i < spriteList.size(); i++)
			{
				spriteList.get(i).drawImage(g, c, x + lx, y + ly, scale * lz);
			}
			//System.out.println("Drawing Layer");
		}
	}

	@Deprecated
	@Override
	public void drawImageNoZoom(Graphics g, Camera c, int lx, int ly, double lz) {
		if(visible)
		{
			int i;
			for(i = 0; i < spriteList.size(); i++)
			{
				spriteList.get(i).drawImage(g, c, x + lx, y + ly, scale);
			}
			//System.out.println("Drawing Layer");
		}
	}
	
	@Deprecated
	@Override
	public void drawImageZoom(Graphics g, Camera c) {
		if(visible)
		{
			int i;
			for(i = 0; i < spriteList.size(); i++)
			{
				spriteList.get(i).drawImage(g, c, x, y, scale);
			}
			//System.out.println("Drawing Layer");
		}
	}

	@Deprecated
	@Override
	public void drawImageNoZoom(Graphics g, Camera c) {
		if(visible)
		{
			int i;
			for(i = 0; i < spriteList.size(); i++)
			{
				spriteList.get(i).drawImage(g, c, x, y, 1.0);
			}
			//System.out.println("Drawing Layer");
		}
	}

	@Deprecated
	@Override
	public void drawImage(Graphics g, Camera c) {
		if(visible)
		{
			int i;
			for(i = 0; i < spriteList.size(); i++)
			{
				spriteList.get(i).drawImage(g, c, x, y, scale);
			}
			//System.out.println("Drawing Layer");
		}
	}

	@Override
	public void drawImage(Graphics g, Camera c, int lx, int ly, double lz) {
		if(visible)
		{
			int i;
			for(i = 0; i < spriteList.size(); i++)
			{
				spriteList.get(i).drawImage(g, c, (int) (x * lz + lx), (int) (y * lz + ly), scale * lz);
			}
			//System.out.println("Drawing Layer");
		}
	}

	@Override
	public void drawPrintImage(Graphics g, Camera c, int lx, int ly, double lz) {
		if(visible)
		{
			int i;
			for(i = 0; i < spriteList.size(); i++)
			{
				spriteList.get(i).drawPrintImage(g, c, (int) (x * lz + lx), (int) (y * lz + ly), scale * lz);
			}
			//System.out.println("Drawing Layer");
		}
	}

	@Override
	public void setLocation(int x, int y) {
		this.x = x;
		this.y = y;
	}

	@Override
	public void setLocation(double x, double y) {
		this.x = (int) x;
		this.y = (int) y;
	}

	@Override
	public void setCentreLocation(double x, double y) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean checkIfInZoom(Point p, Camera c) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean checkIfInZoom(int px, int py, Camera c) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean checkIfIn(Point p, Camera c, int lx, int ly, double lz) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean checkIfIn(int px, int py, Camera c, int lx, int ly, double lz) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public int getX1(Camera c, int lx, double lz) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int getX2(Camera c, int lx, double lz) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int getY1(Camera c, int ly, double lz) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int getY2(Camera c, int ly, double lz) {
		// TODO Auto-generated method stub
		return 0;
	}

	public void setZoom(boolean zoom)
	{
		int i;
		for(i = 0; i < spriteList.size(); i++)
		{
			spriteList.get(i).setZoom(zoom);
		}
	}
	
	public void setSpriteLocation(int i, int x, int y)
	{
		spriteList.get(i).setLocation(x, y);
	}
	
	public void setSpriteInvisible()
	{
		for(int i = 0; i < spriteList.size(); i++)
		{
			spriteList.get(i).setInvisible();
		}
	}
}
