package map;

import java.util.ArrayList;

import game.Player;
import unit.Unit;
import utilities.Location;

public class UnitMap {
	
	Unit[][] map;
	private int height;
	private int width;
	
	public UnitMap(ArrayList<ArrayList<Unit>> unitList, int height, int width)
	{
		this.height = height;
		this.width = width;
		map = new Unit[height][width];
		int i = 0;
		int j = 0;
		ArrayList<Unit> playerUnit;
		Unit unit;
		for (i = 0; i < unitList.size(); i++)
		{
			playerUnit = unitList.get(i);
			for (j = 0; j < playerUnit.size(); j++)
			{
				unit = playerUnit.get(j);
				map[unit.getY()][unit.getX()] = unit;
			}
		}
	}
	
	public UnitMap(Player[] playerList, int height, int width)
	{
		this.height = height;
		this.width = width;
		map = new Unit[height][width];
		int i = 0;
		int j = 0;
		ArrayList<Unit> playerUnit;
		Unit unit;
		for (i = 0; i < playerList.length; i++)
		{
			playerUnit = playerList[i].getUnitList();
			for (j = 0; j < playerUnit.size(); j++)
			{
				unit = playerUnit.get(j);
				map[unit.getY()][unit.getX()] = unit;
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
	
	public void updateMap(ArrayList<ArrayList<Unit>> unitList)
	{
		map = new Unit[map.length][map[0].length];
		int i = 0;
		int j = 0;
		ArrayList<Unit> playerUnit;
		Unit unit;
		for (i = 0; i < unitList.size(); i++)
		{
			playerUnit = unitList.get(i);
			for (j = 0; j < playerUnit.size(); j++)
			{
				unit = playerUnit.get(j);
				map[unit.getY()][unit.getX()] = unit;
			}
		}
	}
	
	public void updateMap(Player[] playerList)
	{
		map = new Unit[map.length][map[0].length];
		int i = 0;
		int j = 0;
		ArrayList<Unit> playerUnit;
		Unit unit;
		for (i = 0; i < playerList.length; i++)
		{
			playerUnit = playerList[i].getUnitList();
			for (j = 0; j < playerUnit.size(); j++)
			{
				unit = playerUnit.get(j);
				map[unit.getY()][unit.getX()] = unit;
			}
		}
	}
	
	public void updateMap(Unit movedUnit, int[] oldLocation)
	{
		map[oldLocation[0]][oldLocation[1]] = null;
		map[movedUnit.getY()][movedUnit.getX()] = movedUnit;
	}
	
	public void updateMap(Unit movedUnit, int y, int x)
	{
		map[y][x] = null;
		map[movedUnit.getY()][movedUnit.getX()] = movedUnit;
	}
	
	public void updateMap(Unit removedUnit)
	{
		map[removedUnit.getY()][removedUnit.getX()] = null;
	}
	
	public void updateMapAdd(Unit addedUnit)
	{
		map[addedUnit.getY()][addedUnit.getX()] = addedUnit;
	}
	
	public Unit[][] getMap()
	{
		return map;
	}
	
	public Unit[] getMap(int y)
	{
		return map[y];
	}
	
	public Unit getMap(int y,int x)
	{
		return map[y][x];
	}
	
	public Unit getMap(Location location)
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
}
	

