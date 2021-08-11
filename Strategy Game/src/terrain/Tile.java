package terrain;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import utilities.JSONDecoder;

public class Tile implements TileInterface{
	
	//Variables
	//movementCost is how much moveRange a unit has to pay to cross the tile
	protected double movementCost;
	//Tile type is the type of tile that it is (mountain, water, factory, etc.)
	protected int tileType;
	//Tile number is the value of that particular tile variant
	protected int tileNumber;
	protected String name;
	protected static JSONObject tileInfo; 
	
	public Tile(double movementCost, int tileType, int tileNumber, String name)
	{
		this.movementCost = movementCost;
		this.tileType = tileType;
		this.tileNumber = tileNumber;
		this.name = name;
	}
	
	/**
	 * A simple constructor for tile. 
	 * 
	 * @param movementCost - the movement cost of a tile
	 */
	public Tile (double movementCost)
	{
		this.movementCost = movementCost;
	}
	
	public Tile (int tileNumber)
	{
		if (tileInfo == null)
		{
			JSONDecoder.setupTileInfo();
			tileInfo = JSONDecoder.getTileInfo();
		}
		this.tileNumber = tileNumber;
		JSONArray tileValues = (JSONArray) tileInfo.get(((Integer) this.tileNumber).toString());
		movementCost = (double) tileValues.get(0);
		tileType = ((Long) tileValues.get(1)).intValue();
		name = (String) tileValues.get(2);
	}
	
	/**
	 * Returns the Tile's movement cost
	 * @return the Tile's movementCost
	 */
	public double getMovementCost()
	{
		return movementCost;
	}

	public int getTileType()
	{
		return tileType;
	}
	
	public int getTileNumber()
	{
		return tileNumber;
	}
	
	public String getTileName()
	{
		return name;
	}
	
	public String getName()
	{
		return name;
	}
	
	public Tile getTile()
	{
		return this;
	}
	
	public boolean isBuilding()
	{
		return false;
	}
	
	public Building getBuilding()
	{
		return null;
	}
}
