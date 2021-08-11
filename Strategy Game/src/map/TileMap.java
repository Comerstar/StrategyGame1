package map;

import java.util.ArrayList;

import terrain.*;
import utilities.Location;

public class TileMap {
	private TileInterface[][] map;
	private int height;
	private int width;
	
	public TileMap (Tile[][] map)
	{
		this.map = map;
		this.height = map.length;
		this.width = map[0].length;
	}
	
	public TileMap (TileInterface[][] map)
	{
		this.map = map;
		this.height = map.length;
		this.width = map[0].length;
	}
	
	public int getHeight()
	{
		return height;
	}
	
	public int getWidth()
	{
		return width;
	}
	
	public TileInterface[][] getMap()
	{
		return map;
	}
	
	public TileInterface[] getMap(int y)
	{
		return map[y];
	}
	
	public TileInterface getMap(int y, int x)
	{
		return map[y][x];
	}
	
	public double getMovementCost(int y, int x)
	{
		return map[y][x].getMovementCost();
	}
	
	public double[][] movementCostMap()
	{
		double[][] movementCostMap = new double[map.length][map[0].length];
		int i = 0;
		int j = 0;
		for (i = 0; i < movementCostMap.length; i++)
		{
			for (j = 0; j < movementCostMap[0].length; j++)
			{
				movementCostMap[i][j] = map[i][j].getMovementCost();
			}
		}
		return movementCostMap;
	}
	
	public boolean inMap (Location location)
	{
		return (location.getX() >= 0) && (location.getY() >= 0) && (location.getX() < width) && (location.getY() < height);
	}

	public boolean inMap (int y, int x)
	{
		return (x >= 0) && (y >= 0) && (x < width) && (y < height);
	}

	public boolean inMap (int[] location)
	{
		return (location[1] >= 0) && (location[0] >= 0) && (location[1] < width) && (location[0] < height);
	}
	
	public ArrayList<Building> makeBuildingList(ArrayList<Building> buildingList, int player)
	{
		int i;
		int j;
		for(i = 0; i < height; i++)
		{
			for(j = 0; j < width; j++)
			{
				if(map[i][j].isBuilding())
				{
					if(map[i][j].getBuilding().getPlayer() == player)
					{
						buildingList.add(map[i][j].getBuilding());
					}
				}
			}
		}
		return buildingList;
	}
	
	public static ArrayList<Building> makeBuildingList(ArrayList<Building> buildingList, int player, TileMap tileMap)
	{
		int i;
		int j;
		for(i = 0; i < tileMap.getHeight(); i++)
		{
			for(j = 0; j < tileMap.getWidth(); j++)
			{
				if(tileMap.getMap(i, j).isBuilding())
				{
					if(tileMap.getMap(i, j).getBuilding().getPlayer() == player)
					{
						buildingList.add(tileMap.getMap(i, j).getBuilding());
					}
				}
			}
		}
		return buildingList;
	}
}
