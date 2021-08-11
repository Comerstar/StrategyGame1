package terrain;

public interface TileInterface {
		
	public double getMovementCost();

	public int getTileType();
		
	public int getTileNumber();
		
	public String getTileName();
	
	public String getName();
		
	public boolean isBuilding();
	
	public Building getBuilding();
	
	public Tile getTile();
}
