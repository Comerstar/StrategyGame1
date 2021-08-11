package animation.sprite;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Point;
import java.awt.image.BufferedImage;
import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;

import org.json.simple.JSONArray;

import animation.AnimationState;
import animation.Camera;
import animation.layer.GridLayer;
import animation.layer.Layer;
import animation.layer.TextBox;
import animation.palette.Palette;
import unit.UnitData;
import utilities.JSONDecoder;

public class Sprite implements SpriteInterface
{
	
	/**
	 * 
	 */
	protected HashMap<String, ArrayList<Image>> img;
	protected String state;
	protected int animationTimer;
	protected boolean animated;
	protected boolean zoom;
	protected int x;
	protected int y;
	protected int height;
	protected int width;
	protected boolean visible;
	protected int gridX;
	protected int gridY;
	protected String[] stateList;
	protected AnimationState animationState;
	protected int sx1;
	protected int sy1;
	protected int sx2;
	protected int sy2;
	protected String spriteName;
	
	protected Sprite(int gridX, int gridY) {
		this.gridX = gridX;
		this.gridY = gridY;
	}
	
	protected void setSpriteData(String spriteType, String spriteName, String[] stateList, String[] tileStateList)
	{
		this.spriteName = spriteType + "/" + spriteName;
		this.stateList = stateList;
		String fullDir = "resources/sprites/" + spriteType + "/" + spriteName + "/";
		img = new HashMap<String, ArrayList<Image>>(stateList.length + 1, (float) 1.0);
		ArrayList<Image> tempImg;
		ImageIcon img;
		int j;
		int k;
		for(int i = 0; i < stateList.length; i++)
		{
			for(j = 0; j < tileStateList.length; j++)
			{
				tempImg = new ArrayList<Image>();
				k = 1;
				while(true) 
				{
					img = getImageIconFromURL(fullDir + stateList[i] + ((Integer) k).toString() + tileStateList[j] + ".png");
					if(img != null)
					{
						sx1 = 0;
						sy1 = 0;
						tempImg.add(img.getImage());
						sx2 = -1;
						sy2 = -1;
					}
					else
					{
						break;
					}
					k++;
				}
				if(tempImg.size() > 0)
				{
					this.img.put(stateList[i] + tileStateList[j], tempImg);
				}
			}
		}
		animated = true;
		zoom = true;
		visible = true;
		animationState = new AnimationState(stateList, stateList);
	}
	
	protected void setSpriteData(String spriteType, String spriteName, String[] stateList)
	{
		this.spriteName = spriteType + "/" + spriteName;
		this.stateList = stateList;
		String fullDir = "resources/sprites/" + spriteType + "/" + spriteName + "/";
		img = new HashMap<String, ArrayList<Image>>(stateList.length + 1, (float) 1.0);
		ArrayList<Image> tempImg;
		ImageIcon img;
		int j;
		for(int i = 0; i < stateList.length; i++)
		{
			tempImg = new ArrayList<Image>();
			j = 1;
			while(true) 
			{
				img = getImageIconFromURL(fullDir + stateList[i] + ((Integer) j).toString() + ".png");
				if(img != null)
				{
					sx1 = 0;
					sy1 = 0;
					tempImg.add(img.getImage());
					sx2 = -1;
					sy2 = -1;
				}
				else
				{
					break;
				}
				j++;
			}
			if(tempImg.size() > 0)
			{
				this.img.put(stateList[i], tempImg);
			}
		}
		animated = true;
		zoom = true;
		visible = true;
		animationState = new AnimationState(stateList, stateList);
	}
	
	protected void setSpriteData(String spriteType, String spriteName, String[] stateList, String state)
	{
		this.state = state;
		this.spriteName = spriteType + "/" + spriteName;
		this.stateList = stateList;
		String fullDir = "resources/sprites/" + spriteType + "/" + spriteName + "/";
		img = new HashMap<String, ArrayList<Image>>(stateList.length + 1, (float) 1.0);
		ArrayList<Image> tempImg;
		ImageIcon img;
		int j;
		for(int i = 0; i < stateList.length; i++)
		{
			tempImg = new ArrayList<Image>();
			j = 1;
			while(true) 
			{
				img = getImageIconFromURL(fullDir + stateList[i] + ((Integer) j).toString() + ".png");
				if(img != null)
				{
					sx1 = 0;
					sy1 = 0;
					tempImg.add(img.getImage());
					sx2 = -1;
					sy2 = -1;
				}
				else
				{
					break;
				}
				j++;
			}
			if(tempImg.size() > 0)
			{
				this.img.put(stateList[i], tempImg);
			}
		}
		animated = true;
		zoom = true;
		visible = true;
		animationState = new AnimationState(stateList, stateList);
	}
	
	//spriteType E.g. units  spriteName E.g. comerstar
	public Sprite(String spriteType, String spriteName, String[] stateList)
	{
		this.spriteName = spriteType + "/" + spriteName;
		this.stateList = stateList;
		String fullDir = "resources/sprites/" + spriteType + "/" + spriteName + "/";
		img = new HashMap<String, ArrayList<Image>>(stateList.length + 1, (float) 1.0);
		ArrayList<Image> tempImg;
		ImageIcon img;
		int j;
		for(int i = 0; i < stateList.length; i++)
		{
			tempImg = new ArrayList<Image>();
			j = 1;
			while(true) 
			{
				img = getImageIconFromURL(fullDir + stateList[i] + ((Integer) j).toString() + ".png");
				if(img != null)
				{
					sx1 = 0;
					sy1 = 0;
					tempImg.add(img.getImage());
					sx2 = -1;
					sy2 = -1;
				}
				else
				{
					break;
				}
				j++;
			}
			if(tempImg.size() > 0)
			{
				this.img.put(stateList[i], tempImg);
			}
		}
		animated = true;
		zoom = true;
		visible = true;
		animationState = new AnimationState(stateList, stateList);
	}
	
	public Sprite(String[] directories, String state)
	{
		this.spriteName = "";
		ArrayList<Image> tempImg = new ArrayList<Image>();
		ImageIcon img;
		for(int i = 0; i < directories.length; i++)
		{
			img = getImageIconFromURL(directories[i]);
			if(img != null)
			{
				tempImg.add(img.getImage());
			}
			if(tempImg.size() > 0)
			{
				this.img.put(state, tempImg);
			}
		}
		this.state = state;
		animated = true;
		zoom = true;
		visible = true;
		animationState = new AnimationState(stateList, stateList);
	}
	
	public Sprite(String spriteType, String spriteName, String[] stateList, boolean animated, boolean zoom)
	{
		this.spriteName = spriteType + "/" + spriteName;
		this.stateList = stateList;
		String fullDir = "resources/sprites/" + spriteType + "/" + spriteName + "/";
		img = new HashMap<String, ArrayList<Image>>(stateList.length + 1, (float) 1.0);
		ArrayList<Image> tempImg;
		ImageIcon img;
		int j;
		for(int i = 0; i < stateList.length; i++)
		{
			tempImg = new ArrayList<Image>();
			j = 1;
			while(true) 
			{
				img = getImageIconFromURL(fullDir + stateList[i] + ((Integer) j).toString());
				if(img != null)
				{
					sx1 = 0;
					sy1 = 0;
					tempImg.add(img.getImage());
					sx2 = -1;
					sy2 = -1;
				}
				else
				{
					break;
				}
				j++;
			}
			if(tempImg.size() > 0)
			{
				this.img.put(stateList[i], tempImg);
			}
		}
		this.animated = animated;
		this.zoom = zoom;
		visible = true;
		animationState = new AnimationState(stateList, stateList);
	}
	
	public Sprite(String spriteType, String spriteName, String[] stateList, String state, boolean animated, boolean zoom)
	{
		this.spriteName = spriteType + "/" + spriteName;
		this.stateList = stateList;
		String fullDir = "resources/sprites/" + spriteType + "/" + spriteName + "/";
		img = new HashMap<String, ArrayList<Image>>(stateList.length + 1, (float) 1.0);
		ArrayList<Image> tempImg;
		ImageIcon img;
		int j;
		for(int i = 0; i < stateList.length; i++)
		{
			tempImg = new ArrayList<Image>();
			j = 1;
			while(true) 
			{
				img = getImageIconFromURL(fullDir + stateList[i] + ((Integer) j).toString());
				if(img != null)
				{
					sx1 = 0;
					sy1 = 0;
					tempImg.add(img.getImage());
					sx2 = -1;
					sy2 = -1;
				}
				else
				{
					break;
				}
				j++;
			}
			if(tempImg.size() > 0)
			{
				this.img.put(stateList[i], tempImg);
			}
		}
		this.animated = animated;
		this.zoom = zoom;
		this.state = state;
		visible = true;
		animationState = new AnimationState(stateList, stateList);
	}
	
	public Sprite(String spriteType, String spriteName, String[] stateList, String state, boolean animated, boolean zoom, int width, int height)
	{
		this.spriteName = spriteType + "/" + spriteName;
		this.stateList = stateList;
		String fullDir = "resources/sprites/" + spriteType + "/" + spriteName + "/";
		img = new HashMap<String, ArrayList<Image>>(stateList.length + 1, (float) 1.0);
		ArrayList<Image> tempImg;
		ImageIcon img;
		int j;
		for(int i = 0; i < stateList.length; i++)
		{
			tempImg = new ArrayList<Image>();
			j = 1;
			while(true) 
			{
				img = getImageIconFromURL(fullDir + stateList[i] + ((Integer) j).toString());
				if(img != null)
				{
					sx1 = 0;
					sy1 = 0;
					tempImg.add(img.getImage());
					sx2 = -1;
					sy2 = -1;
				}
				else
				{
					break;
				}
				j++;
			}
			if(tempImg.size() > 0)
			{
				this.img.put(stateList[i], tempImg);
			}
		}
		this.animated = animated;
		this.zoom = zoom;
		this.state = state;
		visible = true;
		this.height = height;
		this.width = width;
		animationState = new AnimationState(stateList, stateList);
	}
	
	public Sprite(String spriteType, String spriteName, String[] stateList, String state, boolean animated, boolean zoom, int width, int height, int x, int y)
	{
		this.spriteName = spriteType + "/" + spriteName;
		this.stateList = stateList;
		String fullDir = "resources/sprites/" + spriteType + "/" + spriteName + "/";
		img = new HashMap<String, ArrayList<Image>>(stateList.length + 1, (float) 1.0);
		ArrayList<Image> tempImg;
		ImageIcon img;
		int j;
		for(int i = 0; i < stateList.length; i++)
		{
			tempImg = new ArrayList<Image>();
			j = 1;
			while(true) 
			{
				img = getImageIconFromURL(fullDir + stateList[i] + ((Integer) j).toString() + ".png");
				if(img != null)
				{
					sx1 = 0;
					sy1 = 0;
					tempImg.add(img.getImage());
					sx2 = -1;
					sy2 = -1;
				}
				else
				{
					break;
				}
				j++;
			}
			if(tempImg.size() > 0)
			{
				this.img.put(stateList[i], tempImg);
			}
		}
		this.animated = animated;
		this.zoom = zoom;
		this.state = state;
		visible = true;
		this.height = height;
		this.width = width;
		this.x = x;
		this.y = y;
		animationState = new AnimationState(stateList, stateList);
	}
	
	public Sprite(String spriteType, String spriteName, String[] stateList, String[] endState, String state, boolean animated, boolean zoom, int width, int height, int x, int y)
	{
		this.spriteName = spriteType + "/" + spriteName;
		this.stateList = stateList;
		String fullDir = "resources/sprites/" + spriteType + "/" + spriteName + "/";
		img = new HashMap<String, ArrayList<Image>>(stateList.length + 1, (float) 1.0);
		ArrayList<Image> tempImg;
		ImageIcon img;
		int j;
		for(int i = 0; i < stateList.length; i++)
		{
			tempImg = new ArrayList<Image>();
			j = 1;
			while(true) 
			{
				img = getImageIconFromURL(fullDir + stateList[i] + ((Integer) j).toString() + ".png");
				if(img != null)
				{
					sx1 = 0;
					sy1 = 0;
					tempImg.add(img.getImage());
					sx2 = -1;
					sy2 = -1;
				}
				else
				{
					break;
				}
				j++;
			}
			if(tempImg.size() > 0)
			{
				this.img.put(stateList[i], tempImg);
			}
		}
		this.animated = animated;
		this.zoom = zoom;
		this.state = state;
		visible = true;
		this.height = height;
		this.width = width;
		this.x = x;
		this.y = y;
		animationState = new AnimationState(stateList, endState);
	}
	
	public Sprite(UnitData unit, String state, boolean animated, boolean zoom, int width, int height, int x, int y, int gridX, int gridY, Palette palette)
	{
		//TODO Use JSON data instead of just the data.
		if(JSONDecoder.getUnitAnimationInfo() == null)
		{
			JSONDecoder.setupUnitAnimationInfo();
		}
		this.spriteName = "units/" + unit.getName().toLowerCase();
		JSONArray unitAnimationInfo = (JSONArray) JSONDecoder.getUnitAnimationInfo().get(unit.getName());
		String unitFileName = (String) unitAnimationInfo.get(0);
		String fullDir = "resources/sprites/units/" + unitFileName + "/";
		JSONArray stateListInfo = (JSONArray) unitAnimationInfo.get(1);
		JSONArray endStateListInfo = (JSONArray) unitAnimationInfo.get(2);
		int i;
		stateList = new String[stateListInfo.size()];
		String[] endStateList = new String[stateListInfo.size()];
		for(i = 0; i < stateListInfo.size(); i++)
		{
			stateList[i] = (String) stateListInfo.get(i);
			endStateList[i] = (String) endStateListInfo.get(i);
		}
		img = new HashMap<String, ArrayList<Image>>(stateList.length + 1, (float) 1.0);
		ArrayList<Image> tempImg;
		ImageIcon img;
		Image image;
		BufferedImage bufferedImage;
		int j;
		int k;
		int l;
		for(i = 0; i < stateList.length; i++)
		{
			tempImg = new ArrayList<Image>();
			j = 1;
			while(true) 
			{
				img = getImageIconFromURL(fullDir + stateList[i] + ((Integer) j).toString() + ".png");
				if(img != null)
				{
					bufferedImage = new BufferedImage(img.getIconWidth(), img.getIconHeight(), BufferedImage.TYPE_INT_ARGB);
					Graphics2D g = bufferedImage.createGraphics();
					g.drawImage(img.getImage(), 0, 0, null);
					g.dispose();
					for(k = 0; k < bufferedImage.getWidth(); k++)
					{
						for(l = 0; l < bufferedImage.getHeight(); l++)
						{
							bufferedImage.setRGB(k, l, palette.getColour(bufferedImage.getRGB(k, l)));
						}
					}
					image = bufferedImage;
					sx1 = 0;
					sy1 = 0;
					tempImg.add(image);
					sx2 = -1;
					sy2 = -1;
				}
				else
				{
					break;
				}
				j++;
			}
			if(tempImg.size() > 0)
			{
				this.img.put(stateList[i], tempImg);
			}
		}
		this.animated = animated;
		this.zoom = zoom;
		this.state = state;
		visible = true;
		this.height = height;
		this.width = width;
		this.x = x;
		this.y = y;
		this.gridX = gridX;
		this.gridY = gridY;
		animationState = new AnimationState(stateList, endStateList);
	}
	
	public Sprite(UnitData unit, String[] stateList, String state, boolean animated, boolean zoom, int width, int height, int x, int y, int gridX, int gridY, Palette palette)
	{
		//TODO Use JSON data instead of just the data.
		if(JSONDecoder.getUnitAnimationInfo() == null)
		{
			JSONDecoder.setupUnitAnimationInfo();
		}
		this.stateList = stateList;
		JSONArray unitAnimationInfo = (JSONArray) JSONDecoder.getUnitAnimationInfo().get(unit.getName());
		String unitFileName = (String) unitAnimationInfo.get(0);
		this.spriteName = "units/" + unitFileName;
		String fullDir = "resources/sprites/units/" + unitFileName + "/";
		img = new HashMap<String, ArrayList<Image>>(stateList.length + 1, (float) 1.0);
		ArrayList<Image> tempImg;
		ImageIcon img;
		Image image;
		BufferedImage bufferedImage;
		int j;
		int k;
		int l;
		for(int i = 0; i < stateList.length; i++)
		{
			tempImg = new ArrayList<Image>();
			j = 1;
			while(true) 
			{
				img = getImageIconFromURL(fullDir + stateList[i] + ((Integer) j).toString() + ".png");
				if(img != null)
				{
					bufferedImage = new BufferedImage(img.getIconWidth(), img.getIconHeight(), BufferedImage.TYPE_INT_ARGB);
					Graphics2D g = bufferedImage.createGraphics();
					g.drawImage(img.getImage(), 0, 0, null);
					g.dispose();
					for(k = 0; k < bufferedImage.getWidth(); k++)
					{
						for(l = 0; l < bufferedImage.getHeight(); l++)
						{
							bufferedImage.setRGB(k, l, palette.getColour(bufferedImage.getRGB(k, l)));
						}
					}
					image = bufferedImage;
					sx1 = 0;
					sy1 = 0;
					tempImg.add(image);
					sx2 = -1;
					sy2 = -1;
				}
				else
				{
					break;
				}
				j++;
			}
			if(tempImg.size() > 0)
			{
				this.img.put(stateList[i], tempImg);
			}
		}
		this.animated = animated;
		this.zoom = zoom;
		this.state = state;
		visible = true;
		this.height = height;
		this.width = width;
		this.x = x;
		this.y = y;
		this.gridX = gridX;
		this.gridY = gridY;
		animationState = new AnimationState(stateList, stateList);
	}
	
	public Sprite(String spriteType, String spriteName, String[] stateList, String state, boolean animated, boolean zoom, int width, int height, int x, int y, int gridX, int gridY)
	{
		this.spriteName = spriteType + "/" + spriteName;
		this.stateList = stateList;
		String fullDir = "resources/sprites/" + spriteType + "/" + spriteName + "/";
		img = new HashMap<String, ArrayList<Image>>(stateList.length + 1, (float) 1.0);
		ArrayList<Image> tempImg;
		ImageIcon img;
		int j;
		for(int i = 0; i < stateList.length; i++)
		{
			tempImg = new ArrayList<Image>();
			j = 1;
			while(true) 
			{
				img = getImageIconFromURL(fullDir + stateList[i] + ((Integer) j).toString() + ".png");
				if(img != null)
				{
					sx1 = 0;
					sy1 = 0;
					tempImg.add(img.getImage());
					sx2 = -1;
					sy2 = -1;
				}
				else
				{
					break;
				}
				j++;
			}
			if(tempImg.size() > 0)
			{
				this.img.put(stateList[i], tempImg);
			}
		}
		this.animated = animated;
		this.zoom = zoom;
		this.state = state;
		visible = true;
		this.height = height;
		this.width = width;
		this.x = x;
		this.y = y;
		this.gridX = gridX;
		this.gridY = gridY;
		animationState = new AnimationState(stateList, stateList);
	}
	
	public Sprite(String spriteType, String spriteName, String[] stateList, String[] endState, String state, boolean animated, boolean zoom, int width, int height, int x, int y, int gridX, int gridY)
	{
		this.spriteName = spriteType + "/" + spriteName;
		this.stateList = stateList;
		String fullDir = "resources/sprites/" + spriteType + "/" + spriteName + "/";
		img = new HashMap<String, ArrayList<Image>>(stateList.length + 1, (float) 1.0);
		ArrayList<Image> tempImg;
		ImageIcon img;
		int j;
		for(int i = 0; i < stateList.length; i++)
		{
			tempImg = new ArrayList<Image>();
			j = 1;
			while(true) 
			{
				img = getImageIconFromURL(fullDir + stateList[i] + ((Integer) j).toString() + ".png");
				if(img != null)
				{
					sx1 = 0;
					sy1 = 0;
					tempImg.add(img.getImage());
					sx2 = -1;
					sy2 = -1;
				}
				else
				{
					break;
				}
				j++;
			}
			if(tempImg.size() > 0)
			{
				this.img.put(stateList[i], tempImg);
			}
		}
		this.animated = animated;
		this.zoom = zoom;
		this.state = state;
		visible = true;
		this.height = height;
		this.width = width;
		this.x = x;
		this.y = y;
		this.gridX = gridX;
		this.gridY = gridY;
		animationState = new AnimationState(stateList, endState);
	}
	
	public Sprite(String spriteType, String spriteName, String[] stateList, String state)
	{
		this.spriteName = spriteType + "/" + spriteName;
		this.stateList = stateList;
		String fullDir = "resources/sprites/" + spriteType + "/" + spriteName + "/";
		img = new HashMap<String, ArrayList<Image>>(stateList.length + 1, (float) 1.0);
		ArrayList<Image> tempImg;
		ImageIcon img;
		int j;
		for(int i = 0; i < stateList.length; i++)
		{
			tempImg = new ArrayList<Image>();
			j = 1;
			while(true) 
			{
				img = getImageIconFromURL(fullDir + stateList[i] + ((Integer) j).toString());
				if(img != null)
				{
					sx1 = 0;
					sy1 = 0;
					tempImg.add(img.getImage());
					sx2 = -1;
					sy2 = -1;
				}
				else
				{
					break;
				}
				j++;
			}
			if(tempImg.size() > 0)
			{
				this.img.put(stateList[i], tempImg);
			}
		}
		this.state = state;
		visible = true;
		animationState = new AnimationState(stateList, stateList);
	}
	
	public Sprite(String[] directories, String state, boolean animated, boolean zoom)
	{
		this.spriteName = "";
		img = new HashMap<String, ArrayList<Image>>(2, (float) 1.0);
		ArrayList<Image> tempImg = new ArrayList<Image>();
		ImageIcon img;
		for(int i = 0; i < directories.length; i++)
		{
			img = getImageIconFromURL(directories[i]);
			if(img != null)
			{
				sx1 = 0;
				sy1 = 0;
				tempImg.add(img.getImage());
				sx2 = -1;
				sy2 = -1;
			}
			if(tempImg.size() > 0)
			{
				this.img.put(state, tempImg);
			}
		}
		this.state = state;
		this.animated = animated;
		this.zoom = zoom;
		visible = true;
		stateList = new String[] {state};
		animationState = new AnimationState(stateList, stateList);
	}
	
	public Sprite(String[] directories, String state, boolean animated, boolean zoom, int width, int height)
	{
		this.spriteName = "";
		this.img = new HashMap<String, ArrayList<Image>>(2, (float) 1.0);
		ArrayList<Image> tempImg = new ArrayList<Image>();
		ImageIcon img;
		for(int i = 0; i < directories.length; i++)
		{
			img = getImageIconFromURL(directories[i]);
			if(img != null)
			{
				sx1 = 0;
				sy1 = 0;
				tempImg.add(img.getImage());
				sx2 = -1;
				sy2 = -1;
			}
			if(tempImg.size() > 0)
			{
				this.img.put(state, tempImg);
			}
		}
		this.state = state;
		this.animated = animated;
		this.zoom = zoom;
		visible = true;
		this.width = width;
		this.height = height;
		stateList = new String[] {state};
		animationState = new AnimationState(stateList, stateList);
	}
	
	public Sprite(String dir)
	{
		ImageIcon img = getImageIconFromURL(dir);
		if(img == null)
		{
			System.out.println("Unable to Find: " + dir);
		}
	}
	
	public Sprite(String[] directories, String state, boolean animated, boolean zoom, int width, int height, int x, int y)
	{
		this.spriteName = "";
		this.img = new HashMap<String, ArrayList<Image>>(2, (float) 1.0);
		ArrayList<Image> tempImg = new ArrayList<Image>();
		ImageIcon img;
		for(int i = 0; i < directories.length; i++)
		{
			img = getImageIconFromURL(directories[i]);
			if(img != null)
			{
				sx1 = 0;
				sy1 = 0;
				tempImg.add(img.getImage());
				sx2 = -1;
				sy2 = -1;
			}
			if(tempImg.size() > 0)
			{
				this.img.put(state, tempImg);
			}
		}
		this.state = state;
		this.animated = animated;
		this.zoom = zoom;
		visible = true;
		this.width = width;
		this.height = height;
		this.x = x;
		this.y = y;
		stateList = new String[] {state};
		animationState = new AnimationState(stateList, stateList);
	}
	
	public Sprite(Image img, String state, boolean animated, boolean zoom, int width, int height, int x, int y, int gridX, int gridY)
	{
		this.spriteName = "";
		this.img = new HashMap<String, ArrayList<Image>>(2, (float) 1.0);
		ArrayList<Image> tempImg = new ArrayList<Image>();
		if(img != null)
		{
			tempImg.add(img);
			sx1 = 0;
			sy1 = 0;
			sx2 = -1;
			sy2 = -1;
		}
		this.img.put(state, tempImg);
		this.state = state;
		this.animated = animated;
		this.zoom = zoom;
		visible = true;
		this.width = width;
		this.height = height;
		this.x = x;
		this.y = y;
		this.gridX = gridX;
		this.gridY = gridY;
		animationState = new AnimationState(stateList, stateList);
	}
	
	public Sprite(Image img, String state, boolean animated, boolean zoom, int width, int height, int x, int y, int gridX, int gridY, int sx1, int sy1, int sx2, int sy2)
	{
		this.spriteName = "";
		this.img = new HashMap<String, ArrayList<Image>>(2, (float) 1.0);
		ArrayList<Image> tempImg = new ArrayList<Image>();
		if(img != null)
		{
			tempImg.add(img);
		}
		if(tempImg.size() > 0)
		{
			this.img.put(state, tempImg);
		}
		this.state = state;
		this.animated = animated;
		this.zoom = zoom;
		visible = true;
		this.width = width;
		this.height = height;
		this.x = x;
		this.y = y;
		this.gridX = gridX;
		this.gridY = gridY;
		this.sx1 = sx1;
		this.sy1 = sy1;
		this.sx2 = sx2;
		this.sy2 = sy2;
		stateList = new String[] {state};
		animationState = new AnimationState(stateList, stateList);
	}
	
	public Sprite(Image[] img, String state, boolean animated, boolean zoom, int width, int height, int x, int y, 
			int gridX, int gridY)
	{
		this.spriteName = "";
		this.img = new HashMap<String, ArrayList<Image>>(2, (float) 1.0);
		ArrayList<Image> tempImg = new ArrayList<Image>();
		if(img != null)
		{
			for(int i = 0; i < img.length; i++)
			{
				sx1 = 0;
				sy1 = 0;
				sx2 = -1;
				sy2 = -1;
				tempImg.add(img[i]);
			}
		}
		if(tempImg.size() > 0)
		{
			this.img.put(state, tempImg);
		}
		this.state = state;
		this.animated = animated;
		this.zoom = zoom;
		visible = true;
		this.width = width;
		this.height = height;
		this.x = x;
		this.y = y;
		this.gridX = gridX;
		this.gridY = gridY;
		stateList = new String[] {state};
		animationState = new AnimationState(stateList, stateList);
	}
	
	public Sprite(String[] directories, String state, boolean animated, boolean zoom, int width, int height, int x, int y, 
			int gridX, int gridY)
	{
		this.spriteName = "";
		this.img = new HashMap<String, ArrayList<Image>>(2, (float) 1.0);
		ArrayList<Image> tempImg = new ArrayList<Image>();
		ImageIcon img;
		for(int i = 0; i < directories.length; i++)
		{
			img = getImageIconFromURL(directories[i]);
			if(img != null)
			{ 
				sx1 = 0;
				sy1 = 0;
				tempImg.add(img.getImage());
				sx2 = -1;
				sy2 = -1;
			}
			if(tempImg.size() > 0)
			{
				this.img.put(state, tempImg);
			}
		}
		this.state = state;
		this.animated = animated;
		this.zoom = zoom;
		visible = true;
		this.width = width;
		this.height = height;
		this.x = x;
		this.y = y;
		this.gridX = gridX;
		this.gridY = gridY;
		stateList = new String[] {state};
		animationState = new AnimationState(stateList, stateList);
	}
	
	public Sprite(Sprite sprite)
	{
		this.spriteName = sprite.getSpriteName();
		stateList = sprite.getStateList().clone();
		img = new HashMap<String, ArrayList<Image>>(stateList.length + 1, (float) 1.0);
		ArrayList<Image> tempImg;
		int i;
		int j;
		for(i = 0; i < stateList.length; i++)
		{
			tempImg = new ArrayList<Image>();
			for(j = 0; j < sprite.getImg().get(stateList[i]).size(); j++)
			{
				tempImg.add(sprite.getImg().get(stateList[i]).get(j));
			}
			img.put(stateList[i], tempImg);
		}
		this.state = sprite.getState();
		this.animated = sprite.getAnimated();
		this.zoom = sprite.getZoom();
		visible = true;
		this.width = sprite.getWidth();
		this.height = sprite.getHeight();
		this.x = sprite.getX();
		this.y = sprite.getY();
		this.gridX = sprite.getGridX();
		this.gridY = sprite.getGridY();
		animationState = sprite.getAnimationState();
		this.sx1 = sprite.getSX1();
		this.sy1 = sprite.getSY1();
		this.sx2 = sprite.getSX2();
		this.sy2 = sprite.getSY2();
	}
	
	public Sprite(DynamicTile tile)
	{
		this.spriteName = tile.getSpriteName();
		stateList = tile.getStateList().clone();
		img = new HashMap<String, ArrayList<Image>>(stateList.length + 1, (float) 1.0);
		ArrayList<Image> tempImg;
		int i;
		int j;
		int k;
		for(i = 0; i < stateList.length; i++)
		{
			for(j = 0; j < tile.getTileStateList().length; j++)
			{
				tempImg = new ArrayList<Image>();
				for(k = 0; k < tile.getImg().get(stateList[i] + tile.getTileStateList()[j]).size(); k++)
				{
					tempImg.add(tile.getImg().get(stateList[i] + tile.getTileStateList()[j]).get(k));
				}
				img.put(stateList[i] + tile.getTileStateList()[j], tempImg);
			}
		}
		this.state = tile.getState();
		this.animated = tile.getAnimated();
		this.zoom = tile.getZoom();
		visible = true;
		this.width = tile.getWidth();
		this.height = tile.getHeight();
		this.x = tile.getX();
		this.y = tile.getY();
		this.gridX = tile.getGridX();
		this.gridY = tile.getGridY();
		animationState = tile.getAnimationState();
		this.sx1 = tile.getSX1();
		this.sy1 = tile.getSY1();
		this.sx2 = tile.getSX2();
		this.sy2 = tile.getSY2();
	}
	
	public void setSpriteData(Sprite sprite)
	{
		this.spriteName = sprite.getSpriteName();
		stateList = sprite.getStateList().clone();
		img = new HashMap<String, ArrayList<Image>>(stateList.length + 1, (float) 1.0);
		ArrayList<Image> tempImg;
		int i;
		int j;
		for(i = 0; i < stateList.length; i++)
		{
			tempImg = new ArrayList<Image>();
			for(j = 0; j < sprite.getImg().get(stateList[i]).size(); j++)
			{
				tempImg.add(sprite.getImg().get(stateList[i]).get(j));
			}
			img.put(stateList[i], tempImg);
		}
		this.state = sprite.getState();
		this.animated = sprite.getAnimated();
		this.zoom = sprite.getZoom();
		visible = true;
		this.width = sprite.getWidth();
		this.height = sprite.getHeight();
		this.x = sprite.getX();
		this.y = sprite.getY();
		this.gridX = sprite.getGridX();
		this.gridY = sprite.getGridY();
		animationState = sprite.getAnimationState();
		this.sx1 = sprite.getSX1();
		this.sy1 = sprite.getSY1();
		this.sx2 = sprite.getSX2();
		this.sy2 = sprite.getSY2();
	}
	
	public String getSpriteName()
	{
		return spriteName;
	}
	
	public void setSpriteName(String name)
	{
		this.spriteName = name;
	}
	
	public int getSX1()
	{
		return sx1;
	}
	
	public int getSY1()
	{
		return sy1;
	}
	
	public int getSX2()
	{
		return sx2;
	}
	
	public int getSY2()
	{
		return sy2;
	}
	
	public void setSX1(int sx1)
	{
		this.sx1 = sx1;
	}
	
	public void setSY1(int sy1)
	{
		this.sy1 = sy1;
	}
	
	public void setSX2(int sx2)
	{
		this.sx2 = sx2;
	}
	
	public void setSY2(int sy2)
	{
		this.sy2 = sy2;
	}
	
	public Sprite clone()
	{
		return new Sprite(this);
	}
	
	public AnimationState getAnimationState()
	{
		return animationState;
	}
	
	public void setState(String state)
	{
		animationTimer = 0;
		this.state = state;
	}
	
	public void setGridX(int x)
	{
		gridX = x;
	}
	
	public void setGridY(int y)
	{
		gridY = y;
	}
	
	public void setGridLocation(int x, int y)
	{
		gridX = x;
		gridY = y;
	}
	
	public Sprite getSprite()
	{
		return this;
	}
	
	public Button getButton()
	{
		return null;
	}
	
	public TextBox getTextBox()
	{
		return null;
	}

	public Layer getLayer() {
		return null;
	}

	public GridLayer getGridLayer() {
		return null;
	}

	public DynamicTile getDynamicTile()
	{
		return null;
	}
	
	public int getWidth()
	{
		return width;
	}
	
	public int getHeight()
	{
		return height;
	}
	
	public void setWidth(int width)
	{
		this.width = width;
	}
	
	public void setHeight(int height)
	{
		this.height = height;
	}
	
	public void setSize(int width, int height)
	{
		this.width = width;
		this.height = height;
	}
	
	public String getState()
	{
		return state;
	}
	
	public HashMap<String, ArrayList<Image>> getImg()
	{
		return img;
	}
	
	public void incrementTimer()
	{
		if(animated)
		{
			animationTimer++;
			try
			{
				if(animationTimer >= img.get(state).size())
				{
					animationTimer = 0;
					state = animationState.endState(state);
				}
			}
			catch(NullPointerException e)
			{
				System.out.println("SpriteName: " + spriteName);
			}
		}
	}
	
	public String[] getStateList()
	{
		return stateList;
	}
	
	public int getX()
	{
		return x;
	}
	
	public int getY()
	{
		return y;
	}
	
	public int getGridX()
	{
		return gridX;
	}
	
	public int getGridY()
	{
		return gridY;
	}
	
	public int getAnimationTimer()
	{
		return animationTimer;
	}
	
	public void setInvisible()
	{
		visible = false;
	}
	
	public void setVisible()
	{
		visible = true;
	}
	
	public boolean getVisible()
	{
		return visible;
	}
	
	public boolean getAnimated()
	{
		return animated;
	}
	
	public boolean getZoom()
	{
		return zoom;
	}
	
	public ImageIcon getImageIconFromURL(String dir)
	{
		ImageIcon icon = null;
		URL imgURL = getClass().getClassLoader().getResource(dir);
		if(imgURL != null)
		{
			icon = new ImageIcon(imgURL);
		}
		else
		{
			System.out.println("Unable to Find: " + dir);
		}
		return icon;
	}
	
	public Image getImageFromURL(String dir)
	{
		File file = new File(dir);
		Image img = null;
		try
		{
			img = ImageIO.read(file);
		}
		catch(Exception e)
		{
			
		}
		return img;
	}
	
	public Image getImage(int number)
	{
		if(0 <= number && number < img.get(state).size())
		{
			return img.get(state).get(number);
		}
		else
		{
			return img.get(state).get(0);
		}
	}

	@Deprecated
	public void drawImageZoom(Graphics g, Camera c)
	{
		if(sx2 == -1 && sy2 == -1)
		{
			Image image = getImage(animationTimer);
			int imageWidth = image.getWidth(null);
			int imageHeight = image.getHeight(null);
			g.drawImage(image, ((int) ((x - c.getX()) * c.getZoom())), 
							   ((int) ((y - c.getY()) * c.getZoom())), 
							   ((int) ((x - c.getX() + width) * c.getZoom())), 
							   ((int) ((y - c.getY() + height) * c.getZoom())), 0, 0, imageWidth, imageHeight, null);
		}
		else
		{

			Image image = getImage(animationTimer);
			g.drawImage(image, ((int) ((x - c.getX()) * c.getZoom())), 
							   ((int) ((y - c.getY()) * c.getZoom())), 
							   ((int) ((x - c.getX() + width) * c.getZoom())), 
							   ((int) ((y - c.getY() + height) * c.getZoom())), sx1, sy1, sx2, sy2, null);
		}
	}

	@Override
	@Deprecated
	public void drawImageNoZoom(Graphics g, Camera c)
	{
		if(sx2 == -1 && sy2 == -1)
		{
			Image image = getImage(animationTimer);
			int imageWidth = image.getWidth(null);
			int imageHeight = image.getHeight(null);
			g.drawImage(image, x - (int) c.getX(), y - (int) c.getY(), x - (int) c.getX() + width, y - (int) c.getY() + height, 0, 0, imageWidth, imageHeight, null);
		}
		else
		{
			Image image = getImage(animationTimer);
			g.drawImage(image, x - (int) c.getX(), y - (int) c.getY(), x - (int) c.getX() + width, y - (int) c.getY() + height, sx1, sy1, sx2, sy2, null);
		}
	}
	
	public void drawImage(Graphics g, Camera c)
	{
		try
		{
			if(sx2 == -1 && sy2 == -1)
			{
				Image image = getImage(animationTimer);
				int imageWidth = image.getWidth(null);
				int imageHeight = image.getHeight(null);
				g.drawImage(image, getX1(c, 0, 1.0), getY1(c, 0, 1.0), getX2(c, 0, 1.0), getY2(c, 0, 1.0), 0, 0, imageWidth, imageHeight, null);
			}
			else
			{
				Image image = getImage(animationTimer);
				g.drawImage(image, getX1(c, 0, 1.0), getY1(c, 0, 1.0), getX2(c, 0, 1.0), getY2(c, 0, 1.0), sx1, sy1, sx2, sy2, null);
			}
		}
		catch(Exception e)
		{
//			System.out.print(e);
//			System.out.println();
//			System.out.print("State: ");
//			System.out.println(state);
//			System.out.print("Animation Timer: ");
//			System.out.println(animationTimer);
		}
	}
	
	@Override
	public void setAnimated(boolean animated)
	{
		this.animated = animated;
	}

	@Override
	public void drawImage(Graphics g, Camera c, int lx, int ly, double lz)
	{
		try
		{
			if(sx2 == -1 && sy2 == -1)
			{
				Image image = getImage(animationTimer);
				int imageWidth = image.getWidth(null);
				int imageHeight = image.getHeight(null);
				g.drawImage(image, getX1(c, lx, lz), getY1(c, ly, lz), getX2(c, lx, lz), getY2(c, ly, lz), 0, 0, imageWidth, imageHeight, null);
			}
			else
			{
				Image image = getImage(animationTimer);
				g.drawImage(image, getX1(c, lx, lz), getY1(c, ly, lz), getX2(c, lx, lz), getY2(c, ly, lz), sx1, sy1, sx2, sy2, null);
			}
		}
		catch(Exception e)
		{
			System.out.print(e);
			System.out.println();
			System.out.print("State: ");
			System.out.println(state);
			System.out.print("Animation Timer: ");
			System.out.println(animationTimer);
		}
	}

	@Override
	@Deprecated
	public void drawImageZoom(Graphics g, Camera c, int lx, int ly, double lz) {
		try
		{
			if(sx2 == -1 && sy2 == -1)
			{
				Image image = img.get(state).get(animationTimer);
				int imageWidth = image.getWidth(null);
				int imageHeight = image.getHeight(null);
				g.drawImage(image, 
						((int) ((lx) + (x - c.getX()) * c.getZoom() * lz)), 
						((int) ((ly) + (y - c.getY()) * c.getZoom() * lz)), 
						((int) ((lx) + (x - c.getX() + width) * c.getZoom() * lz)), 
						((int) ((ly) + (y - c.getY() + height) * c.getZoom() * lz)), 0, 0, imageWidth, imageHeight, null);
			}
			else
			{
				Image image = img.get(state).get(animationTimer);
				g.drawImage(image, 
						((int) ((lx) + (x - c.getX()) * c.getZoom() * lz)), 
						((int) ((ly) + (y - c.getY()) * c.getZoom() * lz)), 
						((int) ((lx) + (x - c.getX() + width) * c.getZoom() * lz)), 
						((int) ((ly) + (y - c.getY() + height) * c.getZoom() * lz)), sx1, sy1, sx2, sy2, null);
			}
		}
		catch(Exception e)
		{
			System.out.print(e);
			System.out.println();
			System.out.print("State: ");
			System.out.println(state);
			System.out.print("Animation Timer: ");
			System.out.println(animationTimer);
		}
	}

	@Override
	@Deprecated
	public void drawImageNoZoom(Graphics g, Camera c, int lx, int ly, double lz) {
		try
		{
			if(sx2 == -1 && sy2 == -1)
			{
				Image image = img.get(state).get(animationTimer);
				int imageWidth = image.getWidth(null);
				int imageHeight = image.getHeight(null);
				g.drawImage(image, 
						((int) ((lx) + (x - c.getX()) * lz)), 
						((int) ((ly) + (y - c.getY()) * lz)), 
						((int) ((lx) + (x - c.getX() + width) * lz)), 
						((int) ((ly) + (y - c.getY() + height) * lz)), 0, 0, imageWidth, imageHeight, null);
			}
			else
			{
				Image image = img.get(state).get(animationTimer);
				g.drawImage(image, 
						((int) ((lx) + (x - c.getX()) * lz)), 
						((int) ((ly) + (y - c.getY()) * lz)), 
						((int) ((lx) + (x - c.getX() + width) * lz)), 
						((int) ((ly) + (y - c.getY() + height) * lz)), sx1, sy1, sx2, sy2, null);
			}
		}
		catch(Exception e)
		{
			System.out.print(e);
			System.out.println();
			System.out.print("State: ");
			System.out.println(state);
			System.out.print("Animation Timer: ");
			System.out.println(animationTimer);
		}
	}
	
	public void drawPrintImage(Graphics g, Camera c, int lx, int ly, double lz)
	{
		try
		{
			if(sx2 == -1 && sy2 == -1)
			{
				Image image = img.get(state).get(animationTimer);
				int imageWidth = image.getWidth(null);
				int imageHeight = image.getHeight(null);
				g.drawImage(image, 
						((int) ((lx) + (x - c.getX()) * c.getZoom() * lz)), 
						((int) ((ly) + (y - c.getY()) * c.getZoom() * lz)), 
						((int) ((lx) + (x - c.getX() + width) * c.getZoom() * lz)), 
						((int) ((ly) + (y - c.getY() + height) * c.getZoom() * lz)), 0, 0, imageWidth, imageHeight, null);
				System.out.println("x: " + Integer.toString(x));
				System.out.println("y: " + Integer.toString(y));
				System.out.println("width: " + Integer.toString(width));
				System.out.println("height: " + Integer.toString(height));
				System.out.println("sx1: " + Integer.toString(sx1));
				System.out.println("sy1: " + Integer.toString(sy1));
				System.out.println("sx2: " + Integer.toString(sx2));
				System.out.println("sy2: " + Integer.toString(sy2));
				System.out.println("imageWidth: " + Integer.toString(imageWidth));
				System.out.println("imageHeight: " + Integer.toString(imageHeight));
				System.out.println("spriteName: " + spriteName);
				System.out.println("state: " + state);
				System.out.println();
			}
			else
			{
				Image image = img.get(state).get(animationTimer);
				g.drawImage(image, 
						((int) ((lx) + (x - c.getX()) * c.getZoom() * lz)), 
						((int) ((ly) + (y - c.getY()) * c.getZoom() * lz)), 
						((int) ((lx) + (x - c.getX() + width) * c.getZoom() * lz)), 
						((int) ((ly) + (y - c.getY() + height) * c.getZoom() * lz)), sx1, sy1, sx2, sy2, null);
				System.out.println("x: " + Integer.toString(x));
				System.out.println("y: " + Integer.toString(y));
				System.out.println("width: " + Integer.toString(width));
				System.out.println("height: " + Integer.toString(height));
				System.out.println("sx1: " + Integer.toString(sx1));
				System.out.println("sy1: " + Integer.toString(sy1));
				System.out.println("sx2: " + Integer.toString(sx2));
				System.out.println("sy2: " + Integer.toString(sy2));
				System.out.println("spriteName: " + spriteName);
				System.out.println("state: " + state);
				System.out.println();
			}
		}
		catch(Exception e)
		{
			System.out.print(e);
			System.out.println();
			System.out.print("State: ");
			System.out.println(state);
			System.out.print("Animation Timer: ");
			System.out.println(animationTimer);
		}
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
	
	public void setLocation(int x, int y)
	{
		this.x = x;
		this.y = y;
	}
	
	public void setLocation(double x, double y)
	{
		this.x = (int) x;
		this.y = (int) y;
	}
	
	public void setCentreLocation(double x, double y)
	{
		this.x = ((Double) x).intValue() - width / 2;
		this.y = ((Double) y).intValue() - height / 2;
	}
	
	@Deprecated
	public boolean checkIfInZoom(Point p, Camera c)
	{
		return ((x - c.getX()) * c.getZoom()) < p.getX() && ((y - c.getY()) * c.getZoom()) < p.getY() && ((x - c.getX() + width) * c.getZoom()) > p.getX() && ((y - c.getY() + height) * c.getZoom()) > p.getY();
	}
	
	@Deprecated
	public boolean checkIfInZoom(int px, int py, Camera c)
	{
		return ((x - c.getX()) * c.getZoom()) < px && ((y - c.getY()) * c.getZoom()) < py && ((x - c.getX() + width) * c.getZoom()) > px && ((y - c.getY() + height) * c.getZoom()) > py;
	}

	public int getX1(Camera c, int lx, double lz)
	{
		if(zoom)
		{
			return (int) (((lx + ((x) * lz)) - c.getX()) * c.getZoom());
		}
		else
		{
			return (int) ((lx + ((x) * lz)) - c.getX());
		}
	}

	public int getX2(Camera c, int lx, double lz)
	{
		if(zoom)
		{
			return (int) (((lx + ((x + width) * lz)) - c.getX()) * c.getZoom());
		}
		else
		{
			return (int) ((lx + ((x + width) * lz)) - c.getX());
		}
	}

	public int getY1(Camera c, int ly, double lz)
	{
		if(zoom)
		{
			return (int) (((ly + ((y) * lz)) - c.getY()) * c.getZoom());
		}
		else
		{
			return (int) ((ly + ((y) * lz)) - c.getY());
		}
	}

	public int getY2(Camera c, int ly, double lz)
	{
		if(zoom)
		{
			return (int) (((ly + ((y + height) * lz)) - c.getY()) * c.getZoom());
		}
		else
		{
			return (int) ((ly + ((y + height) * lz)) - c.getY());
		}
	}
	
	public boolean checkIfIn(Point p, Camera c, int lx, int ly, double lz)
	{
		return   getX1(c, lx, lz) < p.getX() && 
				 getY1(c, ly, lz) < p.getY() && 
				 getX2(c, lx, lz) > p.getX() && 
				 getY2(c, ly, lz) > p.getY();
	}
	
	public boolean checkIfIn(int px, int py, Camera c, int lx, int ly, double lz)
	{
		return   getX1(c, lx, lz) < px && 
				 getY1(c, ly, lz) < py && 
				 getX2(c, lx, lz) > px && 
				 getY2(c, ly, lz) > py;
	}
	
	public void setZoom(boolean zoom)
	{
		this.zoom = zoom;
	}
}
