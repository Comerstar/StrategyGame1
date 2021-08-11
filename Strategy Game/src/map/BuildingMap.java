package map;

import java.util.ArrayList;

import unit.*;
import terrain.*;
import utilities.Location;

public class BuildingMap {
	
	Building[][] map;
	private int height;
	private int width;
	
	public BuildingMap(ArrayList<ArrayList<Building>> buildingList, int height, int width)
	{
		this.height = height;
		this.width = width;
		map = new Building[height][width];
		int i = 0;
		int j = 0;
		ArrayList<Building> playerBuilding;
		Building building;
		for (i = 0; i < buildingList.size(); i++)
		{
			playerBuilding = buildingList.get(i);
			for (j = 0; j < playerBuilding.size(); j++)
			{
				building = playerBuilding.get(j);
				map[building.getY()][building.getX()] = building;
			}
		}
	}
	
	public BuildingMap(TileMap tileMap)
	{
		this.height = tileMap.getHeight();
		this.width = tileMap.getWidth();
		map = new Building[height][width];
		int i = 0;
		int j = 0;
		for (i = 0; i < height; i++)
		{
			for (j = 0; j < width; j++)
			{
				if(tileMap.getMap(i, j).isBuilding())
				{
					map[i][j] = tileMap.getMap(i, j).getBuilding();
					tileMap.getMap(i, j).getBuilding().setLocation(i, j);;
				}
			}
		}
	}
	
	public int getHeight()
	{
		return height;
	}
	
	public int getWidth()
	{
		return width;
	}
	
	public void updateMap(ArrayList<ArrayList<Building>> buildingList)
	{
		map = new Building[map.length][map[0].length];
		int i = 0;
		int j = 0;
		ArrayList<Building> playerBuilding;
		Building building;
		for (i = 0; i < buildingList.size(); i++)
		{
			playerBuilding = buildingList.get(i);
			for (j = 0; j < playerBuilding.size(); j++)
			{
				building = playerBuilding.get(j);
				map[building.getY()][building.getX()] = building;
			}
		}
	}
	
	public void updateMap(Building movedBuilding, int[] oldLocation)
	{
		map[oldLocation[0]][oldLocation[1]] = null;
		map[movedBuilding.getY()][movedBuilding.getX()] = movedBuilding;
	}
	
	public void updateMap(Building movedBuilding, int y, int x)
	{
		map[y][x] = null;
		map[movedBuilding.getY()][movedBuilding.getX()] = movedBuilding;
	}
	
	public void updateMap(Building removedBuilding)
	{
		map[removedBuilding.getY()][removedBuilding.getX()] = null;
	}
	
	public Unit[][] getUnitMap()
	{
		return map;
	}
	
	public Unit[] getUnitMap(int y)
	{
		return map[y];
	}
	
	public Building getUnitMap(int y,int x)
	{
		return map[y][x];
	}
	
	public Unit getUnitMap(Location location)
	{
		return map[location.getY()][location.getX()];
	}

	public Building[][] getMap()
	{
		return map;
	}

	public Building[] getMap(int y)
	{
		return map[y];
	}
	
	public Building getMap(int y,int x)
	{
		return map[y][x];
	}

	public Building getMap(Location location)
	{
		return map[location.getY()][location.getX()];
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
	
	public ArrayList<Building> getBuildingList()
	{
		ArrayList<Building> buildingList = new ArrayList<Building>();
		int i;
		int j;
		for(i = 0; i < height; i++)
		{
			for(j = 0; j < width; j++)
			{
				if(map[i][j] != null)
				{
					buildingList.add(map[i][j]);
				}
			}
		}
		return buildingList;
	}

}
