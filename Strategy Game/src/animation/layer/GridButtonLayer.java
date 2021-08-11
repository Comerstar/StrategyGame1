package animation.layer;

import java.awt.Point;

import animation.Camera;
import animation.sprite.Button;
import animation.sprite.ButtonInterface;
import animation.sprite.DynamicButton;
import animation.sprite.DynamicTile;

public class GridButtonLayer extends GridLayer implements ButtonInterface{
	
	ButtonInterface[][] buttonMap;
	private boolean mouseOver = false;
	
	public GridButtonLayer(Button[][] buttonMap)
	{
		super(buttonMap);
		this.buttonMap = buttonMap;
	}
	
	public GridButtonLayer(Button[][] buttonMap, double scale, boolean visible, int x, int y)
	{
		super(buttonMap, scale, visible, x, y);
		this.buttonMap = buttonMap;
	}
	
	public GridButtonLayer(Button button, int mapWidth, int mapHeight, int tileSize, double scale, boolean visible, int x, int y)
	{
		super(button, mapWidth, mapHeight, tileSize, scale, visible, x, y);
		buttonMap = new Button[spriteMap.length][spriteMap[0].length];
		int j;
		for(int i = 0; i < spriteMap.length; i++)
		{
			for(j = 0; j < spriteMap[0].length; j++)
			{
				buttonMap[i][j] = new Button(button);
				buttonMap[i][j].setGridLocation(i, j);
				buttonMap[i][j].setLocation(i * tileSize, j * tileSize);
			}
		}
	}
	

	public GridButtonLayer(DynamicTile tile, int i, int mapWidth, int mapHeight, int tileSize, double scale, boolean visible, int x, int y)
	{
		super(tile, mapWidth, mapHeight, tileSize, scale, visible, x, y);
		this.buttonMap = new ButtonInterface[mapWidth][mapHeight];
		int j;
		DynamicTile dynamicTile;
		DynamicButton dynamicButton;
		for(i = 0; i < mapWidth; i++)
		{
			for(j = 0; j < mapHeight; j++)
			{
				dynamicTile = tile.clone();
				dynamicTile.setGridLayer(this);
				dynamicTile.setGridLocation(i, j);
				dynamicTile.setLocation(i * tileSize, j * tileSize);
				dynamicButton = DynamicButton.makeDynamicButton(dynamicTile);
				buttonMap[i][j] = dynamicButton;
				//System.out.println("dynamicButton.getState(): " + dynamicButton.getState());
				
			}
		}
		this.scale = scale;
		this.tileWidth = tileSize;
		this.tileHeight = tileSize;
		this.x = x;
		this.y = y;
		this.visible = visible;
	}
	
	public ButtonInterface[][] getButtonMap()
	{
		return buttonMap;
	}
	
	public void replaceButton(Button button, int x, int y)
	{
		spriteMap[x][y] = button;
		buttonMap[x][y] = button;
	}
	
	public void replaceButtonCopy(Button button, int x, int y)
	{
		Button b = button.clone();
		spriteMap[x][y] = b;
		buttonMap[x][y] = b;
	}
	
	public int[] mouseMoved(Point p, Camera c)
	{
		int[] coordinatesClicked = new int[] {-1, -1};
//		int i;
//		int j;
//		for(i = 0; i < buttonMap.length; i++)
//		{
//			for(j = 0; j < buttonMap[0].length; j++)
//			{
//				buttonMap[i][j].setOver(p, c, x, y, scale);
//				if(buttonMap[i][j].getMouseOver())
//				{
//					coordinatesClicked[1] = i;
//					coordinatesClicked[0] = j;
//				}
//			}
//		}
		coordinatesClicked = getGridLocation(p, c);
		if(coordinatesClicked[0] != -1)
		{
			buttonMap[coordinatesClicked[1]][coordinatesClicked[0]].setOver(true);
		}
		return coordinatesClicked;
	}
	
	public int[] mouseLeftClick(Point p, Camera c)
	{
		int[] coordinatesClicked = new int[] {-1, -1};
//		int i;
//		int j;
//		for(i = 0; i < buttonMap.length; i++)
//		{
//			for(j = 0; j < buttonMap[0].length; j++)
//			{
//				buttonMap[i][j].setOver(p, c, x, y, scale);
//				if(buttonMap[i][j].getMouseOver())
//				{
//					coordinatesClicked[1] = i;
//					coordinatesClicked[0] = j;
//				}
//			}
//		}
		coordinatesClicked = getGridLocation(p, c);
		if(coordinatesClicked[0] != -1)
		{
			buttonMap[coordinatesClicked[1]][coordinatesClicked[0]].setOver(true);
		}
		return coordinatesClicked;
	}
	
	public int[] mask(GridLayer layer, Point p, Camera c)
	{
		int[] coordinatesClicked = new int[] {-1, -1};
//		int i;
//		int j;
		boolean search = layer.notMasked(p, c);
		if(search)
		{
//			for(i = 0; i < buttonMap.length; i++)
//			{
//				for(j = 0; j < buttonMap[0].length; j++)
//				{
//					buttonMap[i][j].setOver(p, c, x, y, scale);
//					if(buttonMap[i][j].getMouseOver())
//					{
//						coordinatesClicked[1] = buttonMap[i][j].getGridX();
//						coordinatesClicked[0] = buttonMap[i][j].getGridY();
//					}
//				}
//			}
			coordinatesClicked = getGridLocation(p, c);
			if(coordinatesClicked[0] != -1)
			{
				buttonMap[coordinatesClicked[0]][coordinatesClicked[1]].setOver(true);
			}
		}
		return coordinatesClicked;
	}
	
	public int[] mask(Layer layer, Point p, Camera c)
	{
		int[] coordinatesClicked = new int[] {-1, -1};
//		int i;
//		int j;
		boolean search = layer.notMasked(p, c);
		if(search)
		{
//			for(i = 0; i < buttonMap.length; i++)
//			{
//				for(j = 0; j < buttonMap[0].length; j++)
//				{
//					buttonMap[i][j].setOver(p, c, x, y, scale);
//					if(buttonMap[i][j].getMouseOver())
//					{
//						coordinatesClicked[1] = buttonMap[i][j].getGridX();
//						coordinatesClicked[0] = buttonMap[i][j].getGridY();
//					}
//				}
//			}
			coordinatesClicked = getGridLocation(p, c);
			if(coordinatesClicked[0] != -1)
			{
				buttonMap[coordinatesClicked[1]][coordinatesClicked[0]].setOver(true);
			}
		}
		return coordinatesClicked;
	}
	
	public int[] mask(Layer layer, Point p, Camera c1, Camera c2)
	{
		int[] coordinatesClicked = new int[] {-1, -1};
//		int i;
//		int j;
		boolean search = layer.notMasked(p, c2);
		if(search)
		{
//			for(i = 0; i < buttonMap.length; i++)
//			{
//				for(j = 0; j < buttonMap[0].length; j++)
//				{
//					buttonMap[i][j].setOver(p, c, x, y, scale);
//					if(buttonMap[i][j].getMouseOver())
//					{
//						coordinatesClicked[1] = buttonMap[i][j].getGridX();
//						coordinatesClicked[0] = buttonMap[i][j].getGridY();
//					}
//				}
//			}
			coordinatesClicked = getGridLocation(p, c1);
			if(coordinatesClicked[0] != -1)
			{
				buttonMap[coordinatesClicked[1]][coordinatesClicked[0]].setOver(true);
			}
		}
		return coordinatesClicked;
	}
	
	public void swapSprites(int i1, int j1, int i2, int j2)
	{
		ButtonInterface button1 = buttonMap[i1][j1];
		ButtonInterface button2 = buttonMap[i2][j2];
		buttonMap[i2][j2] = button1;
		buttonMap[i1][j1] = button2;
		button1.setLocation(i2 * tileWidth, j2 * tileHeight);
		button2.setLocation(i1 * tileWidth, j1 * tileHeight);
		button1.setGridLocation(i2, j2);
		button2.setGridLocation(i1, j1);
		super.swapSprites(i1, j1, i2, j2);
	}

	@Override
	public void setOver(Point p, Camera c, int lx, int ly, double lz) {
		int i;
		int j;
		mouseOver = false;
		for(i = 0; i < buttonMap.length; i++)
		{
			for(j = 0; j < buttonMap[0].length; j++)
			{
				if(buttonMap[i][j] != null)
				{
					buttonMap[i][j].setOver(p, c, x + lx, y + ly, scale * lz);
					if(buttonMap[i][j].getMouseOver())
					{
						mouseOver = true;
					}
				}
			}
		}
	}

	@Override
	public void setOver(int px, int py, Camera c, int lx, int ly, double lz) {
		int i;
		int j;
		mouseOver = false;
		for(i = 0; i < buttonMap.length; i++)
		{
			for(j = 0; j < buttonMap[0].length; j++)
			{
				if(buttonMap[i][j] != null)
				{
					buttonMap[i][j].setOver(px, py, c, x + lx, y + ly, scale * lz);
					if(buttonMap[i][j].getMouseOver())
					{
						mouseOver = true;
					}
				}
			}
		}
	}
	
	public void setOverGrid(Point p, Camera c, int lx, int ly, double lz) {
		//TODO fix
		mouseOver = false;
		int[] coordinatesClicked = getGridLocation(p, c, lx, ly, lz);
		if(coordinatesClicked[0] != -1)
		{
			buttonMap[coordinatesClicked[1]][coordinatesClicked[0]].setOver(true);
			mouseOver = true;
		}
	}
	
	public void setOverGrid(Point p, Camera c) {
		mouseOver = false;
		int[] coordinatesClicked = getGridLocation(p, c);
		if(coordinatesClicked[0] != -1)
		{
			buttonMap[coordinatesClicked[1]][coordinatesClicked[0]].setOver(true);
			mouseOver = true;
		}
	}

	@Override
	public boolean getMouseOver() {
		return mouseOver;
	}

	@Override
	public void setOver(boolean mouseOver) {
		this.mouseOver = mouseOver;
	}
}
