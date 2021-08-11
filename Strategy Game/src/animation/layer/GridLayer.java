package animation.layer;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.Point;
import java.util.ArrayList;
import java.util.HashMap;

import javax.swing.ImageIcon;

import animation.AnimationState;
import animation.Camera;
import animation.palette.Palette;
import animation.sprite.Button;
import animation.sprite.DynamicTile;
import animation.sprite.Sprite;
import animation.sprite.SpriteInterface;
import map.BuildingMap;
import map.TileMap;
import map.UnitMap;

public class GridLayer implements SpriteInterface{
	
	/**
	 * 
	 */
	//Sprite[x][y]
	protected SpriteInterface[][] spriteMap;
	protected int x;
	protected int y;
	protected double scale;
	protected int tileHeight;
	protected int tileWidth;
	protected int height;
	protected int width;
	protected boolean visible;
	
	public GridLayer(Sprite[][] spriteMap)
	{
		this.spriteMap = spriteMap;
		width = spriteMap.length * tileWidth;
		if(width != 0)
		{
			height = spriteMap[0].length * tileHeight;
		}
	}
	
	public GridLayer(SpriteInterface[][] spriteMap)
	{
		this.spriteMap = spriteMap;
		width = spriteMap.length * tileWidth;
		if(width != 0)
		{
			height = spriteMap[0].length * tileHeight;
		}
	}
	
	public GridLayer(Sprite[][] spriteMap, double scale, boolean visible, int x, int y)
	{
		this.spriteMap = spriteMap;
		width = spriteMap.length * tileWidth;
		if(width != 0)
		{
			height = spriteMap[0].length * tileHeight;
		}
		this.scale = scale;
		this.visible = visible;
		this.x = x;
		this.y = y;
	}
	
	public GridLayer(SpriteInterface[][] spriteMap, double scale, boolean visible, int x, int y)
	{
		this.spriteMap = spriteMap;
		width = spriteMap.length * tileWidth;
		if(width != 0)
		{
			height = spriteMap[0].length * tileHeight;
		}
		this.scale = scale;
		this.visible = visible;
		this.x = x;
		this.y = y;
	}
	
	public GridLayer(TileMap tileMap, int tileSize, double scale, boolean visible, int x, int y)
	{
		this.spriteMap = new Sprite[tileMap.getMap()[0].length][tileMap.getMap().length];
		int i;
		int j;
		for(i = 0; i < tileMap.getMap()[0].length; i++)
		{
			for(j = 0; j < tileMap.getMap().length; j++)
			{
				spriteMap[i][j] = new Sprite("tiles", tileMap.getMap()[j][i].getTileName().toLowerCase(), new String[] {tileMap.getMap()[j][i].getTileName().toLowerCase()}, tileMap.getMap()[j][i].getTileName().toLowerCase(), true, true, tileSize, tileSize, tileSize * i, tileSize * j, i, j);
			}
		}
		this.scale = scale;
		this.tileWidth = tileSize;
		this.tileHeight = tileSize;
		this.x = x;
		this.y = y;
		this.visible = visible;
	}
	
	public GridLayer(TileMap tileMap, int i, int tileSize, double scale, boolean visible, int x, int y)
	{
		this.spriteMap = new DynamicTile[tileMap.getMap()[0].length][tileMap.getMap().length];
		int j;
		for(i = 0; i < tileMap.getMap()[0].length; i++)
		{
			for(j = 0; j < tileMap.getMap().length; j++)
			{
				spriteMap[i][j] = new DynamicTile("tiles", tileMap.getMap()[j][i].getTileName().toLowerCase(), new String[] {tileMap.getMap()[j][i].getTileName().toLowerCase()}, 1, i, j, this, i * tileSize, j * tileSize, tileSize, tileSize);
			}
		}
		for(i = 0; i < tileMap.getMap()[0].length; i++)
		{
			for(j = 0; j < tileMap.getMap().length; j++)
			{
				((DynamicTile) spriteMap[i][j]).updateDynamicTile();
			}
		}
		this.scale = scale;
		this.tileWidth = tileSize;
		this.tileHeight = tileSize;
		this.x = x;
		this.y = y;
		this.visible = visible;
	}
	
	public GridLayer(UnitMap unitMap, int tileSize, double scale, boolean visible, int x, int y)
	{
		this.spriteMap = new Sprite[unitMap.getMap()[0].length][unitMap.getMap().length];
		int i;
		int j;
		for(i = 0; i < unitMap.getMap()[0].length; i++)
		{
			for(j = 0; j < unitMap.getMap().length; j++)
			{
				if(unitMap.getMap()[j][i] != null)
				{
					spriteMap[i][j] = new Sprite("units", unitMap.getMap()[j][i].getName().toLowerCase(), new String[] {unitMap.getMap()[j][i].getName().toLowerCase()}, unitMap.getMap()[j][i].getName().toLowerCase(), true, true, tileSize, tileSize, tileSize * i, tileSize * j, i, j);
				}
				else
				{
					spriteMap[i][j] = null;
				}
			}
		}
		this.scale = scale;
		this.tileWidth = tileSize;
		this.tileHeight = tileSize;
		this.x = x;
		this.y = y;
		this.visible = visible;
	}
	 @Deprecated
	public GridLayer(UnitMap unitMap, int tileSize, int spriteWidth, int spriteHeight, double scale, boolean visible, int x, int y)
	{
		this.spriteMap = new Sprite[unitMap.getMap()[0].length][unitMap.getMap().length];
		int i;
		int j;
		for(i = 0; i < unitMap.getMap()[0].length; i++)
		{
			for(j = 0; j < unitMap.getMap().length; j++)
			{
				if(unitMap.getMap()[j][i] != null)
				{
					spriteMap[i][j] = new Sprite("units", unitMap.getMap()[j][i].getName().toLowerCase(), new String[] {unitMap.getMap()[j][i].getName().toLowerCase()}, unitMap.getMap()[j][i].getName().toLowerCase(), true, true, spriteWidth, spriteHeight, tileSize * i, tileSize * j, i, j);
				}
				else
				{
					spriteMap[i][j] = null;
				}
			}
		}
		this.scale = scale;
		this.tileWidth = tileSize;
		this.tileHeight = tileSize;
		this.x = x;
		this.y = y;
		this.visible = visible;
	}
	
	public GridLayer(UnitMap unitMap, int tileSize, int spriteWidth, int spriteHeight, double scale, boolean visible, int x, int y, Palette[] paletteList)
	{
		this.spriteMap = new Sprite[unitMap.getMap()[0].length][unitMap.getMap().length];
		int i;
		int j;
		for(i = 0; i < unitMap.getMap()[0].length; i++)
		{
			for(j = 0; j < unitMap.getMap().length; j++)
			{
				if(unitMap.getMap()[j][i] != null)
				{
					if(unitMap.getMap()[j][i].getPlayer() >= 0 && unitMap.getMap()[j][i].getPlayer() < paletteList.length)
					{
						spriteMap[i][j] = new Sprite(unitMap.getMap()[j][i], "overworldr", true, true,  spriteWidth, spriteHeight, tileSize * i, tileSize * j, i, j, paletteList[unitMap.getMap()[j][i].getPlayer()]);
					}
					else
					{
						spriteMap[i][j] = new Sprite(unitMap.getMap()[j][i], "overworldr", true, true,  spriteWidth, spriteHeight, tileSize * i, tileSize * j, i, j, Palette.DEFAULT_PALETTE);
					}
				}
				else
				{
					spriteMap[i][j] = null;
				}
			}
		}
		this.scale = scale;
		this.tileWidth = tileSize;
		this.tileHeight = tileSize;
		this.x = x;
		this.y = y;
		this.visible = visible;
	}
	
	@Deprecated
	public GridLayer(BuildingMap buildingMap, int tileSize, double scale, boolean visible, int x, int y)
	{
		this.spriteMap = new Sprite[buildingMap.getMap()[0].length][buildingMap.getMap().length];
		int i;
		int j;
		for(i = 0; i < buildingMap.getMap()[0].length; i++)
		{
			for(j = 0; j < buildingMap.getMap().length; j++)
			{
				if(buildingMap.getMap()[j][i] != null)
				{
					if(buildingMap.getMap()[j][i].getPlayer() >= 0)
					{
						spriteMap[i][j] = new Sprite("units", buildingMap.getMap()[j][i].getName().toLowerCase(), new String[] {buildingMap.getMap()[j][i].getName().toLowerCase()}, buildingMap.getMap()[j][i].getName().toLowerCase(), true, true, tileSize, tileSize, tileSize * i, tileSize * j, i, j);
					}
					else
					{
						spriteMap[i][j] = new Sprite("units", buildingMap.getMap()[j][i].getName().toLowerCase(), new String[] {buildingMap.getMap()[j][i].getName().toLowerCase()}, buildingMap.getMap()[j][i].getName().toLowerCase(), true, true, tileSize, tileSize, tileSize * i, tileSize * j, i, j);
					}
				}
				else
				{
					spriteMap[i][j] = null;
				}
			}
		}
		this.scale = scale;
		this.tileWidth = tileSize;
		this.tileHeight = tileSize;
		this.x = x;
		this.y = y;
		this.visible = visible;
	}
	
	public GridLayer(BuildingMap buildingMap, int tileSize, double scale, boolean visible, int x, int y, Palette[] paletteList)
	{
		this.spriteMap = new Sprite[buildingMap.getMap()[0].length][buildingMap.getMap().length];
		int i;
		int j;
		for(i = 0; i < buildingMap.getMap()[0].length; i++)
		{
			for(j = 0; j < buildingMap.getMap().length; j++)
			{
				if(buildingMap.getMap()[j][i] != null)
				{
					if(buildingMap.getMap()[j][i].getPlayer() >= 0 && buildingMap.getMap()[j][i].getPlayer() < paletteList.length)
					{
						spriteMap[i][j] = new Sprite(buildingMap.getMap()[j][i], new String[] {buildingMap.getMap()[j][i].getName().toLowerCase()}, buildingMap.getMap()[j][i].getName().toLowerCase(), true, true, tileSize, tileSize, tileSize * i, tileSize * j, i, j, paletteList[buildingMap.getMap()[j][i].getPlayer()]);
					}
					else
					{
						spriteMap[i][j] = new Sprite(buildingMap.getMap()[j][i], new String[] {buildingMap.getMap()[j][i].getName().toLowerCase()}, buildingMap.getMap()[j][i].getName().toLowerCase(), true, true, tileSize, tileSize, tileSize * i, tileSize * j, i, j, Palette.DEFAULT_PALETTE);
					}
				}
				else
				{
					spriteMap[i][j] = null;
				}
			}
		}
		this.scale = scale;
		this.tileWidth = tileSize;
		this.tileHeight = tileSize;
		this.x = x;
		this.y = y;
		this.visible = visible;
	}
	
	public GridLayer(Sprite sprite, int mapWidth, int mapHeight, int tileSize, double scale, boolean visible, int x, int y)
	{
		this.spriteMap = new Sprite[mapWidth][mapHeight];
		int i;
		int j;
		for(i = 0; i < mapWidth; i++)
		{
			for(j = 0; j < mapHeight; j++)
			{
				spriteMap[i][j] = new Sprite(sprite);
				spriteMap[i][j].setGridLocation(i, j);
				spriteMap[i][j].setLocation(i * tileSize, j * tileSize);
			}
		}
		this.scale = scale;
		this.tileWidth = tileSize;
		this.tileHeight = tileSize;
		this.x = x;
		this.y = y;
		this.visible = visible;
	}
	
	public GridLayer(DynamicTile tile, int mapWidth, int mapHeight, int tileSize, double scale, boolean visible, int x, int y)
	{
		this.spriteMap = new Sprite[mapWidth][mapHeight];
		int i;
		int j;
		DynamicTile dynamicTile;
		for(i = 0; i < mapWidth; i++)
		{
			for(j = 0; j < mapHeight; j++)
			{
				dynamicTile = tile.clone();
				dynamicTile.setGridLayer(this);
				spriteMap[i][j] = dynamicTile;
				spriteMap[i][j].setGridLocation(i, j);
				spriteMap[i][j].setLocation(i * tileSize, j * tileSize);
			}
		}
		this.scale = scale;
		this.tileWidth = tileSize;
		this.tileHeight = tileSize;
		this.x = x;
		this.y = y;
		this.visible = visible;
		for(i = 0; i < mapWidth; i++)
		{
			for(j = 0; j < mapHeight; j++)
			{
				((DynamicTile) spriteMap[i][j]).updateDynamicTile();
			}
		}
	}
	
	public GridLayer(DynamicTile tile, int mapWidth, int mapHeight, int tileSize, double scale, boolean visible, int x, int y, boolean zoom)
	{
		this.spriteMap = new Sprite[mapWidth][mapHeight];
		int i;
		int j;
		DynamicTile dynamicTile;
		for(i = 0; i < mapWidth; i++)
		{
			for(j = 0; j < mapHeight; j++)
			{
				dynamicTile = tile.clone();
				dynamicTile.setGridLayer(this);
				spriteMap[i][j] = dynamicTile;
				spriteMap[i][j].setGridLocation(i, j);
				spriteMap[i][j].setLocation(i * tileSize, j * tileSize);
			}
		}
		this.scale = scale;
		this.tileWidth = tileSize;
		this.tileHeight = tileSize;
		this.x = x;
		this.y = y;
		this.visible = visible;
		for(i = 0; i < mapWidth; i++)
		{
			for(j = 0; j < mapHeight; j++)
			{
				((DynamicTile) spriteMap[i][j]).updateDynamicTile();
			}
		}
		setZoom(zoom);
	}
	
	public void setZoom(boolean zoom)
	{
		int i;
		int j;
		for(i = 0; i < spriteMap.length; i++)
		{
			for(j = 0; j < spriteMap[0].length; j++)
			{
				spriteMap[i][j].setZoom(zoom);
			}
		}
	}
	
	public void setAnimated(boolean animated)
	{
		int i;
		int j;
		for(i = 0; i < spriteMap.length; i++)
		{
			for(j = 0; j < spriteMap[0].length; j++)
			{
				spriteMap[i][j].setAnimated(animated);
			}
		}
	}
	
	@Deprecated
	public GridLayer(DynamicTile tile, int i, int mapWidth, int mapHeight, int tileSize, double scale, boolean visible, int x, int y)
	{
		this.spriteMap = new Sprite[mapWidth][mapHeight];
		int j;
		DynamicTile dynamicTile;
		for(i = 0; i < mapWidth; i++)
		{
			for(j = 0; j < mapHeight; j++)
			{
				dynamicTile = tile.clone();
				dynamicTile.setGridLayer(this);
				spriteMap[i][j] = dynamicTile;
				spriteMap[i][j].setGridLocation(i, j);
				spriteMap[i][j].setLocation(i * tileSize, j * tileSize);
			}
		}
		incrementTimer();
		for(i = 0; i < mapWidth; i++)
		{
			for(j = 0; j < mapHeight; j++)
			{
				spriteMap[i][j] = spriteMap[i][j].getSprite();
			}
		}
		this.scale = scale;
		this.tileWidth = tileSize;
		this.tileHeight = tileSize;
		this.x = x;
		this.y = y;
		this.visible = visible;
	}
	
	public DynamicTile getDynamicTile()
	{
		return null;
	}
	
	public boolean inLayer(int x, int y)
	{
		return x >= 0 && y >= 0 && x < spriteMap.length && y < spriteMap[0].length;
	}
	
	public boolean inLayer(int[] coordinates)
	{
		return coordinates[0] >= 0 && coordinates[1] >= 0 && coordinates[0] < spriteMap.length && coordinates[1] < spriteMap[0].length;
	}
	
	public void drawLayer(Graphics g, Camera camera)
	{
		if(visible)
		{
			int i;
			int j;
			for(j = 0; j < spriteMap[0].length; j++)
			{
				for(i = 0; i < spriteMap.length; i++)
				{
					if(spriteMap[i][j] != null)
					{
						spriteMap[i][j].drawImage(g, camera, x, y, scale);
					}
				}
			}
		}
	}
	
	public void drawLayer(Graphics g, Camera camera, int i, int j)
	{
		if(visible)
		{
			for(j = 0; j < spriteMap[0].length; j++)
			{
				for(i = 0; i < spriteMap.length; i++)
				{
					if(spriteMap[i][j] != null)
					{
						spriteMap[i][j].drawImage(g, camera, x, y, scale);
					}
				}
			}
		}
	}
	
	public void drawPrintLayer(Graphics g, Camera camera)
	{
		if(visible)
		{
			int i;
			int j;
			for(j = 0; j < spriteMap[0].length; j++)
			{
				for(i = 0; i < spriteMap.length; i++)
				{
					if(spriteMap[i][j] != null)
					{
						spriteMap[i][j].drawPrintImage(g, camera, x, y, scale);
					}
				}
			}
		}
	}
	
	public void replaceSprite(Sprite sprite, int x, int y)
	{
		spriteMap[x][y] = sprite;
	}
	
	public void replaceSpriteCopy(Sprite sprite, int x, int y)
	{
		spriteMap[x][y] = sprite.clone();
	}
	
	public void increaseScale(double scale)
	{
		//System.out.println("Increasing Scale");
		this.scale += scale;
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
	
	public void swapSprites(int i1, int j1, int i2, int j2)
	{
		SpriteInterface sprite1 = spriteMap[i1][j1];
		SpriteInterface sprite2 = spriteMap[i2][j2];
		spriteMap[i2][j2] = sprite1;
		spriteMap[i1][j1] = sprite2;
		sprite1.setLocation(i2 * tileWidth, j2 * tileHeight);
		sprite2.setLocation(i1 * tileWidth, j1 * tileHeight);
		sprite1.setGridLocation(i2, j2);
		sprite2.setGridLocation(i1, j1);
	}
	
	public boolean notMasked(Point p, Camera c)
	{
		boolean masked = true;
		int j;
		for(int i = 0; i < spriteMap.length; i++)
		{
			for(j = 0; j < spriteMap[0].length; j++)
			{
				if(spriteMap[i][j].checkIfIn(p, c, x, y, scale))
				{
					masked = false;
					break;
				}
			}
		}
		return masked;
	}
	
	public void incrementTimer()
	{
		int j;
		for(int i = 0; i < spriteMap.length; i++)
		{
			for(j = 0; j < spriteMap[0].length; j++)
			{
				if(spriteMap[i][j] != null)
				{
					spriteMap[i][j].incrementTimer();
				}
			}
		}
	}
	
	public void incrementTimer(int i ,int j)
	{
		for(i = 0; i < spriteMap.length; i++)
		{
			for(j = 0; j < spriteMap[0].length; j++)
			{
				if(spriteMap[i][j] != null)
				{
					spriteMap[i][j].incrementTimer();
				}
			}
		}
	}
	
	public String getState(int x, int y)
	{
		return spriteMap[x][y].getState();
	}
	
	public void setState(int x, int y, String state)
	{
		spriteMap[x][y].setState(state);
	}
	
	public void setLayerState(String state)
	{
		int j;
		for(int i = 0; i < spriteMap.length; i++)
		{
			for(j = 0; j < spriteMap[0].length; j++)
			{
				if(spriteMap[i][j] != null)
				{
					spriteMap[i][j].setState(state);
				}
			}
		}
	}
	
	public SpriteInterface getSprite(int x, int y)
	{
		return spriteMap[x][y];
	}
	
	public SpriteInterface[][] getSpriteMap()
	{
		return spriteMap;
	}

	@Override
	public Sprite clone() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public AnimationState getAnimationState() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setState(String state) {
		int j;
		for(int i = 0; i < spriteMap.length; i++)
		{
			for(j = 0; j < spriteMap[0].length; j++)
			{
				if(spriteMap[i][j] != null)
				{
					spriteMap[i][j].setState(state);
				}
			}
		}
	}

	@Override
	public void setGridX(int x) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setGridY(int y) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setGridLocation(int x, int y) {
		// TODO Auto-generated method stub
		
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
		return null;
	}

	public GridLayer getGridLayer() {
		return this;
	}

	@Override
	public int getWidth() {
		return width;
	}

	@Override
	public int getHeight() {
		return height;
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
		return null;
	}

	@Override
	public HashMap<String, ArrayList<Image>> getImg() {
		return null;
	}

	@Override
	public String[] getStateList() {
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
	public void drawImageZoom(Graphics g, Camera c) {
		if(visible)
		{
			int i;
			int j;
			for(i = 0; i < spriteMap.length; i++)
			{
				for(j = 0; j < spriteMap[0].length; j++)
				{
					if(spriteMap[i][j] != null)
					{
						spriteMap[i][j].drawImage(g, c, x, y, scale);
					}
				}
			}
		}
	}

	@Deprecated
	@Override
	public void drawImageNoZoom(Graphics g, Camera c) {
		if(visible)
		{
			int i;
			int j;
			for(i = 0; i < spriteMap.length; i++)
			{
				for(j = 0; j < spriteMap[0].length; j++)
				{
					if(spriteMap[i][j] != null)
					{
						spriteMap[i][j].drawImage(g, c, x, y, scale);
					}
				}
			}
		}
	}

	@Override
	public void drawImage(Graphics g, Camera c) {
		if(visible)
		{
			int i;
			int j;
			for(i = 0; i < spriteMap.length; i++)
			{
				for(j = 0; j < spriteMap[0].length; j++)
				{
					if(spriteMap[i][j] != null)
					{
						spriteMap[i][j].drawImage(g, c, x, y, scale);
					}
				}
			}
		}
	}

	@Override
	public void drawImage(Graphics g, Camera c, int lx, int ly, double lz) {
		if(visible)
		{
			int i;
			int j;
			for(i = 0; i < spriteMap.length; i++)
			{
				for(j = 0; j < spriteMap[0].length; j++)
				{
					if(spriteMap[i][j] != null)
					{
						spriteMap[i][j].drawImage(g, c, (int) (x * lz + lx), (int) (y * lz + ly), scale * lz);
					}
				}
			}
		}
	}

	@Override
	public void drawPrintImage(Graphics g, Camera c, int lx, int ly, double lz) {
		if(visible)
		{
			int i;
			int j;
			for(i = 0; i < spriteMap.length; i++)
			{
				for(j = 0; j < spriteMap[0].length; j++)
				{
					if(spriteMap[i][j] != null)
					{
						spriteMap[i][j].drawPrintImage(g, c, (int) (x * lz + lx), (int) (y * lz + ly), scale * lz);
					}
				}
			}
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
		int i;
		int j;
		for(i = 0; i < spriteMap.length; i++)
		{
			for(j = 0; j < spriteMap[0].length; j++)
			{
				if(spriteMap[i][j] != null)
				{
					if(spriteMap[i][j].checkIfIn(p, c, (int) (x * lz + lx), (int) (y * lz + ly), scale * lz))
					{
						return true;
					}
				}
			}
		}
		return false;
	}

	@Override
	public boolean checkIfIn(int px, int py, Camera c, int lx, int ly, double lz) {
		int i;
		int j;
		for(i = 0; i < spriteMap.length; i++)
		{
			for(j = 0; j < spriteMap[0].length; j++)
			{
				if(spriteMap[i][j] != null)
				{
					if(spriteMap[i][j].checkIfIn(px, py, c, (int) (x * lz + lx), (int) (y * lz + ly), scale * lz))
					{
						return true;
					}
				}
			}
		}
		return false;
	}

	@Deprecated
	@Override
	public void drawImageZoom(Graphics g, Camera camera, int layerX, int layerY, double layerZoom) {
		if(visible)
		{
			int i;
			int j;
			for(i = 0; i < spriteMap.length; i++)
			{
				for(j = 0; j < spriteMap[0].length; j++)
				{
					if(spriteMap[i][j] != null)
					{
						spriteMap[i][j].drawImage(g, camera, x + layerX, y + layerY, scale * layerZoom);
					}
				}
			}
		}
	}

	@Deprecated
	@Override
	public void drawImageNoZoom(Graphics g, Camera camera, int layerX, int layerY, double layerZoom) {
		if(visible)
		{
			int i;
			int j;
			for(i = 0; i < spriteMap.length; i++)
			{
				for(j = 0; j < spriteMap[0].length; j++)
				{
					if(spriteMap[i][j] != null)
					{
						spriteMap[i][j].drawImage(g, camera, x + layerX, y + layerY, layerZoom);
					}
				}
			}
		}
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
	
	public void setSpriteLocation(int i, int j, int x, int y)
	{
		spriteMap[i][j].setLocation(x, y);
	}
	
	public int[] getGridLocation(Point p, Camera c, int lx, int ly, double lz)
	{
		//TODO fix
		int[] location = new int[] {-1, -1};
		int gridX = (int) (((((p.getX() / c.getZoom()) + c.getX()) / (scale * lz)) - (x * lz + lx)) / tileWidth);
		int gridY = (int) (((((p.getY() / c.getZoom()) + c.getY()) / (scale * lz)) - (y * lz + ly)) / tileHeight);
		System.out.println("GridLocation: " + gridX + "," + gridY);
		if(gridX >= 0 && gridX < spriteMap.length && gridY >= 0 && gridY < spriteMap[0].length)
		{
			location[1] = gridX;
			location[0] = gridY;
		}
		return location;
	}
	
	public int[] getGridLocation(Point p, Camera c)
	{
		int[] location = new int[] {-1, -1};
		int gridX = (int) (((((p.getX() / c.getZoom()) + c.getX()) / scale) - x) / tileWidth);
		int gridY = (int) (((((p.getY() / c.getZoom()) + c.getY()) / scale) - y) / tileHeight);
		System.out.println("GridLocation: " + gridX + "," + gridY);
		if(gridX >= 0 && gridX < spriteMap.length && gridY >= 0 && gridY < spriteMap[0].length)
		{
			location[1] = gridX;
			location[0] = gridY;
		}
		return location;
	}
}
