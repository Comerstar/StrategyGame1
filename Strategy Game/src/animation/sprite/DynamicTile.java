package animation.sprite;

import java.awt.Graphics;
import java.awt.Image;
import java.util.ArrayList;

import animation.Camera;
import animation.layer.GridLayer;

import java.util.Random;

public class DynamicTile extends Sprite {
	
	private static Random r = new Random();
	private int dynamicType;
	private GridLayer gridLayer;
	private String tileState;
	private String[] tileStateList;
	
	public DynamicTile(String spriteType, String spriteName, String[] stateList, int dynamicType, int gridX, int gridY)
	{
		super(gridX, gridY);
		this.stateList = stateList;
		this.dynamicType = dynamicType;
		this.spriteName = spriteType + "/" + spriteName;
		this.state = "";
		this.tileState = "";
		if(dynamicType == 0)
		{
			this.tileStateList = new String[]{"", "T", "R", "B", "L", "TR", "RB", "BL", "TL", "TB", "RL", "TRB", "RBL", "TBL", "TRL","TRBL"};
			this.setSpriteData(spriteType, spriteName, stateList, tileStateList);
		}
		else if(dynamicType == 1)
		{
			String[] tileStateList = new String[] {"a","b","c","d","e","f","g","h","i","j","k","l","m","n","o","p","q","r","s","t","u","v","w","x","y","z",
					"A","B","C","D","E","F","G","H","I","J","K","L","M","N","O","P","Q","R","S","T","U","V","W","X","Y","Z"};
			ArrayList<String> acceptedTileStateList = new ArrayList<String>();
			this.setSpriteData(spriteType, spriteName, stateList, tileStateList);
			for(int i = 0; i < stateList.length; i++)
			{
				for(int j = 0; j < tileStateList.length; j++)
				{
					if(img.get(stateList[i] + tileStateList[j]) != null)
					{
						acceptedTileStateList.add(tileStateList[j]);
					}
				}
			}
			this.tileStateList = new String[acceptedTileStateList.size()];
			for(int i = 0; i < acceptedTileStateList.size(); i++)
			{
				this.tileStateList[i] = acceptedTileStateList.get(i);
			}
		}
		this.width = img.get("").get(0).getWidth(null);
		this.height = img.get("").get(0).getHeight(null);
		state = stateList[0];
	}
	
	public DynamicTile(String spriteType, String spriteName, String[] stateList, int dynamicType, int gridX, int gridY, int x, int y, int width, int height)
	{
		super(gridX, gridY);
		setLocation(x, y);
		this.width = width;
		this.height = height;
		this.dynamicType = dynamicType;
		this.spriteName = spriteType + "/" + spriteName;
		this.state = "";
		this.tileState = "";
		if(dynamicType == 0)
		{
			this.tileStateList = new String[]{"", "T", "R", "B", "L", "TR", "RB", "BL", "TL", "TB", "RL", "TRB", "RBL", "TBL", "TRL","TRBL"};
			this.setSpriteData(spriteType, spriteName, stateList, tileStateList);
		}
		else if(dynamicType == 1)
		{
			String[] tileStateList = new String[] {"a","b","c","d","e","f","g","h","i","j","k","l","m","n","o","p","q","r","s","t","u","v","w","x","y","z",
					"A","B","C","D","E","F","G","H","I","J","K","L","M","N","O","P","Q","R","S","T","U","V","W","X","Y","Z"};
			ArrayList<String> acceptedTileStateList = new ArrayList<String>();
			this.setSpriteData(spriteType, spriteName, stateList, tileStateList);
			for(int i = 0; i < stateList.length; i++)
			{
				for(int j = 0; j < tileStateList.length; j++)
				{
					if(img.get(stateList[i] + tileStateList[j]) != null)
					{
						acceptedTileStateList.add(tileStateList[j]);
					}
				}
			}
			this.tileStateList = new String[acceptedTileStateList.size()];
			for(int i = 0; i < acceptedTileStateList.size(); i++)
			{
				this.tileStateList[i] = acceptedTileStateList.get(i);
			}
		}
		state = stateList[0];
	}
	
	public DynamicTile(DynamicTile tile)
	{
		super(tile);
		state = tile.getState();
		gridLayer = tile.getGridLayer();
		dynamicType = tile.getDynamicType();
		tileStateList = tile.getTileStateList().clone();
		tileState = tile.getTileState();
	}
	
	public DynamicTile clone()
	{
		return new DynamicTile(this);
	}
	
	public void setDynamicTileData(DynamicTile tile)
	{
		setSpriteData(tile);
		state = tile.getState();
		gridLayer = tile.getGridLayer();
		dynamicType = tile.getDynamicType();
		tileStateList = tile.getTileStateList();
		tileState = tile.getTileState();
	}
	
	public DynamicTile(String spriteType, String spriteName, String[] stateList, int dynamicType, int gridX, int gridY, GridLayer gridLayer)
	{
		super(gridX, gridY);
		this.gridLayer = gridLayer;
		this.dynamicType = dynamicType;
		this.spriteName = spriteType + "/" + spriteName;
		this.state = "";
		this.tileState = "";
		if(dynamicType == 0)
		{
			SpriteInterface sprite;
			this.tileStateList = new String[]{"", "T", "R", "B", "L", "TR", "RB", "BL", "TL", "TB", "RL", "TRB", "RBL", "TBL", "TRL","TRBL"};
			if(gridY - 1 >= 0)
			{
				sprite = gridLayer.getSprite(gridX, gridY - 1);
				if(sprite != null)
				{
					if(sprite.getSprite() != null)
					{
						if(sprite.getSprite().getSpriteName().equals(this.spriteName))
						{
							tileState += "T";
						}
					}
				}
			}
			if(gridX + 1 < gridLayer.getSpriteMap().length)
			{
				sprite = gridLayer.getSprite(gridX + 1, gridY);
				if(sprite != null)
				{
					if(sprite.getSprite() != null)
					{
						if(sprite.getSprite().getSpriteName().equals(this.spriteName))
						{
							tileState += "R";
						}
					}
				}
			}
			if(gridY + 1 < gridLayer.getSpriteMap()[0].length)
			{
				sprite = gridLayer.getSprite(gridX, gridY + 1);
				if(sprite != null)
				{
					if(sprite.getSprite() != null)
					{
						if(sprite.getSprite().getSpriteName().equals(this.spriteName))
						{
							tileState += "B";
						}
					}
				}
			}
			if(gridX - 1 >= 0)
			{
				sprite = gridLayer.getSprite(gridX - 1, gridY);
				if(sprite != null)
				{
					if(sprite.getSprite() != null)
					{
						if(sprite.getSprite().getSpriteName().equals(this.spriteName))
						{
							tileState += "L";
						}
					}
				}
			}
			this.setSpriteData(spriteType, spriteName, stateList, tileStateList);
		}
		else if(dynamicType == 1)
		{
			String[] tileStateList = new String[] {"a","b","c","d","e","f","g","h","i","j","k","l","m","n","o","p","q","r","s","t","u","v","w","x","y","z",
					"A","B","C","D","E","F","G","H","I","J","K","L","M","N","O","P","Q","R","S","T","U","V","W","X","Y","Z"};
			ArrayList<String> acceptedTileStateList = new ArrayList<String>();
			this.setSpriteData(spriteType, spriteName, stateList, tileStateList);
			for(int i = 0; i < stateList.length; i++)
			{
				for(int j = 0; j < tileStateList.length; j++)
				{
					if(img.get(stateList[i] + tileStateList[j]) != null)
					{
						acceptedTileStateList.add(tileStateList[j]);
					}
				}
			}
			this.tileStateList = new String[acceptedTileStateList.size()];
			for(int i = 0; i < acceptedTileStateList.size(); i++)
			{
				this.tileStateList[i] = acceptedTileStateList.get(i);
			}
			//int max = 2147483647;
			int a = gridX * gridX;
			//System.out.println(a);
			r.setSeed((long) a);
			a = r.nextInt();
			r.setSeed((long) a + gridY);
			a = r.nextInt(tileStateList.length);
			for(int i = 0; i < tileStateList.length; i++)
			{
				if(a % tileStateList.length == i)
				{
					tileState = tileStateList[i];
				}
			}
		}
		state = stateList[0];
	}
	
	public DynamicTile(String spriteType, String spriteName, String[] stateList, int dynamicType, int gridX, int gridY, GridLayer gridLayer, int x, int y, int width, int height)
	{
		super(gridX, gridY);
		setLocation(x, y);
		this.width = width;
		this.height = height;
		this.gridLayer = gridLayer;
		this.dynamicType = dynamicType;
		this.spriteName = spriteType + "/" + spriteName;
		this.state = "";
		this.tileState = "";
		this.stateList = stateList;
		if(dynamicType == 0)
		{
			SpriteInterface sprite;
			this.tileStateList = new String[]{"", "T", "R", "B", "L", "TR", "RB", "BL", "TL", "TB", "RL", "TRB", "RBL", "TBL", "TRL","TRBL"};
			if(gridY - 1 >= 0)
			{
				sprite = gridLayer.getSprite(gridX, gridY - 1);
				if(sprite != null)
				{
					if(sprite.getSprite() != null)
					{
						if(sprite.getSprite().getSpriteName().equals(this.spriteName))
						{
							tileState += "T";
						}
					}
				}
			}
			if(gridX + 1 < gridLayer.getSpriteMap().length)
			{
				sprite = gridLayer.getSprite(gridX + 1, gridY);
				if(sprite != null)
				{
					if(sprite.getSprite() != null)
					{
						if(sprite.getSprite().getSpriteName().equals(this.spriteName))
						{
							tileState += "R";
						}
					}
				}
			}
			if(gridY + 1 < gridLayer.getSpriteMap()[0].length)
			{
				sprite = gridLayer.getSprite(gridX, gridY + 1);
				if(sprite != null)
				{
					if(sprite.getSprite() != null)
					{
						if(sprite.getSprite().getSpriteName().equals(this.spriteName))
						{
							tileState += "B";
						}
					}
				}
			}
			if(gridX - 1 >= 0)
			{
				sprite = gridLayer.getSprite(gridX - 1, gridY);
				if(sprite != null)
				{
					if(sprite.getSprite() != null)
					{
						if(sprite.getSprite().getSpriteName().equals(this.spriteName))
						{
							tileState += "L";
						}
					}
				}
			}
			this.setSpriteData(spriteType, spriteName, stateList, tileStateList);
		}
		else if(dynamicType == 1)
		{
			String[] tileStateList = new String[] {"a","b","c","d","e","f","g","h","i","j","k","l","m","n","o","p","q","r","s","t","u","v","w","x","y","z",
					"A","B","C","D","E","F","G","H","I","J","K","L","M","N","O","P","Q","R","S","T","U","V","W","X","Y","Z"};
			ArrayList<String> acceptedTileStateList = new ArrayList<String>();
			this.setSpriteData(spriteType, spriteName, stateList, tileStateList);
			for(int i = 0; i < stateList.length; i++)
			{
				for(int j = 0; j < tileStateList.length; j++)
				{
					if(img.get(stateList[i] + tileStateList[j]) != null)
					{
						acceptedTileStateList.add(tileStateList[j]);
					}
				}
			}
			this.tileStateList = new String[acceptedTileStateList.size()];
			//System.out.println(acceptedTileStateList.size());
			for(int i = 0; i < acceptedTileStateList.size(); i++)
			{
				this.tileStateList[i] = acceptedTileStateList.get(i);
			}
			int max = 2147483647;
			int a = (gridX - gridY + gridX * gridY + gridX * gridX * gridX - gridY) ^ (max - gridX - gridY * gridX - gridY);
			r.setSeed((long) a);
			a = r.nextInt();
			for(int i = 0; i < this.tileStateList.length; i++)
			{
				if(max / this.tileStateList.length * i < a && a < max / tileStateList.length * (i + 1))
				{
					tileState = this.tileStateList[i];
					//System.out.println("Set tileState To : |" + tileState + "|");
				}
			}
		}
		state = stateList[0];
	}
	
	public void updateDynamicTile()
	{
		if(dynamicType == 0)
		{
			this.tileState = "";
			SpriteInterface sprite;
			if(gridY - 1 >= 0)
			{
				sprite = gridLayer.getSprite(gridX, gridY - 1);
				if(sprite != null)
				{
					if(sprite.getSprite() != null)
					{
						if(sprite.getSprite().getSpriteName().equals(this.spriteName))
						{
							tileState += "T";
						}
					}
				}
			}
			if(gridX + 1 < gridLayer.getSpriteMap().length)
			{
				sprite = gridLayer.getSprite(gridX + 1, gridY);
				if(sprite != null)
				{
					if(sprite.getSprite() != null)
					{
						if(sprite.getSprite().getSpriteName().equals(this.spriteName))
						{
							tileState += "R";
						}
					}
				}
			}
			if(gridY + 1 < gridLayer.getSpriteMap()[0].length)
			{
				sprite = gridLayer.getSprite(gridX, gridY + 1);
				if(sprite != null)
				{
					if(sprite.getSprite() != null)
					{
						if(sprite.getSprite().getSpriteName().equals(this.spriteName))
						{
							tileState += "B";
						}
					}
				}
			}
			if(gridX - 1 >= 0)
			{
				sprite = gridLayer.getSprite(gridX - 1, gridY);
				if(sprite != null)
				{
					if(sprite.getSprite() != null)
					{
						if(sprite.getSprite().getSpriteName().equals(this.spriteName))
						{
							tileState += "L";
						}
					}
				}
			}
		}
		else if(dynamicType == 1)
		{
			//int max = 2147483647;
			int a = gridX * gridX;
			//System.out.println(a);
			r.setSeed((long) a);
			a = r.nextInt();
			r.setSeed((long) a + gridY);
			a = r.nextInt(tileStateList.length);
			if(a < 0)
			{
				a *= -1;
			}
			//System.out.println(a);
			for(int i = 0; i < tileStateList.length; i++)
			{
				//System.out.println("max / tileStateList.length * i: " + max / tileStateList.length * i);
				if(a % tileStateList.length == i)
				{
					tileState = tileStateList[i];
					//System.out.println("Set tileState To : |" + tileState + "|");
				}
			}
			//System.out.println("EndSetting tileState");
		}
	}
	
	public void setGridLayer(GridLayer gridLayer)
	{
		this.gridLayer = gridLayer;
	}
	
	@Override
	public void incrementTimer()
	{
		if(animated)
		{
			animationTimer++;
			try
			{
				if(animationTimer >= img.get(state + tileState).size())
				{
					animationTimer = 0;
					if(animationState.endState(state) == null)
					{
						System.out.println("Error State: |" + state + "|");
					}
					state = animationState.endState(state);
				}
			}
			catch(NullPointerException e)
			{
				System.out.println("Error State: |" + state + "|");
				System.out.println("Error TileState: |" + tileState + "|");
				System.out.println(e);
			}
		}
	}
	
	public void setDynamicType(int dynamicType)
	{
		this.dynamicType = dynamicType;
	}
	
	public int getDynamicType()
	{
		return dynamicType;
	}
	
	public void setGridX(int x)
	{
		gridX = x;
		updateDynamicTile();
	}
	
	public void setGridY(int y)
	{
		gridY = y;
		updateDynamicTile();
	}
	
	public void setGridLocation(int x, int y)
	{
		gridX = x;
		gridY = y;
		updateDynamicTile();
	}
	
	@Override
	public Image getImage(int number)
	{
		if(0 <= number && number < img.get(state + tileState).size())
		{
			return img.get(state + tileState).get(number);
		}
		else
		{
			return img.get(state + tileState).get(0);
		}
	}
	
	public String[] getTileStateList()
	{
		return tileStateList;
	}
	
	public String getTileState()
	{
		return tileState;
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
			//System.out.print("State: |");
			//System.out.println(state + tileState + "|");
			//System.out.print("Animation Timer: ");
			//System.out.println(animationTimer);
			//System.out.println("ImageSize: " + img.get(state + tileState).size());
		}
		catch(Exception e)
		{
			System.out.print(e);
			System.out.println();
			System.out.print("State: |");
			System.out.println(state + tileState + "|");
			System.out.print("Animation Timer: ");
			System.out.println(animationTimer);
		}
	}
}
