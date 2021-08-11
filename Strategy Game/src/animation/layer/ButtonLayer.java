package animation.layer;

import java.awt.Point;
import java.util.ArrayList;

import animation.Camera;
import animation.sprite.Button;
import animation.sprite.ButtonInterface;

public class ButtonLayer extends Layer {
	
	public ArrayList<ButtonInterface> buttonList;
	
	public ButtonLayer(ArrayList<ButtonInterface> buttonList)
	{
		super(buttonList, 0, 0);
		this.buttonList = buttonList;
	}
	
	public ButtonLayer(ArrayList<ButtonInterface> buttonList, double scale, boolean visible, int x, int y)
	{
		super(buttonList, scale, 0, visible, x, y);
		this.buttonList = buttonList;
	}
	
	public ButtonLayer(ArrayList<Button> buttonList, int i)
	{
		super(buttonList, 0);
		this.buttonList = new ArrayList<ButtonInterface>();
		for(i = 0; i < buttonList.size(); i++)
		{
			this.buttonList.add(buttonList.get(i));
		}
	}
	
	public ButtonLayer(ArrayList<Button> buttonList, int i, double scale, boolean visible, int x, int y)
	{
		super(buttonList, 0, scale, visible, x, y);
		this.buttonList = new ArrayList<ButtonInterface>();
		for(i = 0; i < buttonList.size(); i++)
		{
			this.buttonList.add(buttonList.get(i));
		}
	}
	
	public ArrayList<ButtonInterface> getButtonList()
	{
		return buttonList;
	}
	
	public int mouseMoved(Point p, Camera c)
	{
		int numberClicked = -1;
		for(int i = 0; i < buttonList.size(); i++)
		{
			buttonList.get(i).setOver(p, c, x, y, scale);
			if(buttonList.get(i).getMouseOver())
			{
				numberClicked = i;
			}
		}
		return numberClicked;
	}
	
	public int mouseLeftClick(Point p, Camera c)
	{
		int numberClicked = -1;
		System.out.println("Mouse Left Click Button Layer");
		System.out.println("buttonList.size(): " + buttonList.size());
		for(int i = 0; i < buttonList.size(); i++)
		{
			buttonList.get(i).setOver(p, c, x, y, scale);
			if(buttonList.get(i).getMouseOver())
			{
				numberClicked = i;
				System.out.println("numberClicked: " + numberClicked);
			}
		}
		System.out.println("Mouse Left Click Button Layer END");
		return numberClicked;
	}
	
	public int mask(GridLayer layer, Point p, Camera c)
	{
		int numberClicked = -1;
		boolean search = layer.notMasked(p, c);
		if(search)
		{
			for(int i = 0; i < buttonList.size(); i++)
			{
				buttonList.get(i).setOver(p, c, x, y, scale);
				if(buttonList.get(i).getMouseOver())
				{
					numberClicked = i;
				}
			}
		}
		return numberClicked;
	}
	
	public void addSprite(Button button)
	{
		spriteList.add(button);
		buttonList.add(button);
	}
	
	public void removeSprite(Button button)
	{
		spriteList.remove(button);
		buttonList.remove(button);
	}
	
	public void addSprite(ButtonInterface button)
	{
		spriteList.add(button);
		buttonList.add(button);
	}
	
	public void removeSprite(ButtonInterface button)
	{
		spriteList.remove(button);
		buttonList.remove(button);
	}
	
	public int mask(Layer layer, Point p, Camera c)
	{
		int numberClicked = -1;
		boolean search = layer.notMasked(p, c);
		if(search)
		{
			for(int i = 0; i < buttonList.size(); i++)
			{
				buttonList.get(i).setOver(p, c, x, y, scale);
				if(buttonList.get(i).getMouseOver())
				{
					numberClicked = i;
				}
			}
		}
		return numberClicked;
	}
	
	@Override
	public void clearLayer()
	{
		super.clearLayer();
		buttonList.clear();
	}
}
