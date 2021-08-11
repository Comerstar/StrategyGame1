package art;

import java.awt.Color;
import java.awt.Graphics;

import animation.Camera;

public class Slider extends BufferedSprite {
	
	private int sliderValue;
	private int minSliderValue;
	private int maxSliderValue;
	private Color sliderColour = Color.BLACK;
	
	public Slider(int imgWidth, int imgHeight) {
		super(imgWidth, imgHeight);
		minSliderValue = 0;
		maxSliderValue = imgHeight;
		sliderValue = 0;
	}
	
	public Slider(int x, int y, int imgWidth, int imgHeight) {
		super(x, y, imgWidth, imgHeight);
		minSliderValue = 0;
		maxSliderValue = imgHeight;
		sliderValue = 0;
	}
	
	public Slider(int x, int y, int imgWidth, int imgHeight, int sliderMin, int sliderMax) {
		super(x, y, imgWidth, imgHeight);
		minSliderValue = sliderMin;
		maxSliderValue = sliderMax;
		sliderValue = 0;
	}
	
	public int getSliderValue()
	{
		return sliderValue;
	}
	
	public void setSliderValue(int sliderValue)
	{
		this.sliderValue = sliderValue;
		if(this.sliderValue < minSliderValue)
		{
			this.sliderValue = minSliderValue;
		}
		else if(this.sliderValue > maxSliderValue)
		{
			this.sliderValue = maxSliderValue;
		}
	}
	
	public int convertCheckedYIntoSlider(Camera c, int y)
	{
		if(y < getY1(c, 0))
		{
			return minSliderValue;
		}
		if(y > getY2(c, 0))
		{
			return maxSliderValue;
		}
		return (int) ((((((double) y) / c.getZoom()) + c.getY()) - this.y) * ((double) (maxSliderValue - minSliderValue) / img.getHeight()) + minSliderValue);
	}
	
	public int convertYIntoSlider(Camera c, int y)
	{
		return (int) ((((((double) y) / c.getZoom()) + c.getY()) - this.y) * ((double) (maxSliderValue - minSliderValue) / img.getHeight()) + minSliderValue);
	}
	
	public int convertSliderIntoY(Camera c)
	{
		return (int) (((((sliderValue - minSliderValue) / ((double) (maxSliderValue - minSliderValue) / img.getHeight())) + this.y)- c.getY()) * c.getZoom());
	}
	
	public int convertImgYIntoSlider(int y)
	{
		return (int) (y * ((double) (maxSliderValue - minSliderValue) / img.getHeight()) + minSliderValue);
	}
	
	@Override
	public void drawImage(Graphics g, Camera c, int lx, int ly)
	{
		if(visible)
		{
			super.drawImage(g, c, lx, ly);
			Color graphicsColor = g.getColor();
			g.setColor(sliderColour);
			g.drawLine(getX1(c, lx) - 2, convertSliderIntoY(c), getX2(c, lx) + 2, convertSliderIntoY(c));
			g.setColor(graphicsColor);
		}
	}
	
	@Override
	public void drawImageBorder(Graphics g, Camera c, int lx, int ly)
	{
		if(visible)
		{
			super.drawImageBorder(g, c, lx, ly);
			Color graphicsColor = g.getColor();
			g.setColor(sliderColour);
			g.drawLine(getX1(c, lx) - 2, convertSliderIntoY(c), getX2(c, lx) + 2, convertSliderIntoY(c));
			g.setColor(graphicsColor);
		}
	}
}
