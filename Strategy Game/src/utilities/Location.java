package utilities;

/**
 * A utility class that handles integer locations. Has utility functions such as checks, clones and translations. It handles the locations in the format (y,x). 
 * 
 * @author the Programmer (Comerstar)
 */
public class Location {
	
	int[] location;
	
	/**
	 * Calculates the taxicab distance between location1 and location2
	 * 
	 * @param location1 - the coordinates of the first location
	 * @param location2 - the coordinates of the second location
	 * @return the distance
	 */
	public static int calculateDistance(int[] location1, int[] location2)
	{
		return Math.abs(location1[0] - location2[0]) + Math.abs(location1[1] - location2[1]);
	}
	
	/**
	 * A Constructor for Location that initialises it with the location 0,0.
	 */
	public Location ()
	{
		location = new int[2];
	}
	
	/**
	 * A constructor for Location that initialises it to match the location given
	 * 
	 * @param y The y component of the new Location (the first value).
	 * @param x The x component of the new Location (the second value).
	 */
	public Location (int y, int x)
	{
		location = new int[] {y,x};
	}
	
	/**
	 * A constructor for Location that initialises it to match the location given in location.
	 * 
	 * @param location The location (as (y,x)) of the new Location.
	 */
	public Location (int[] location)
	{
		this.location = location.clone();
	}
	
	/**
	 * A constructor for Location that initialises it to match the given Location.
	 * 
	 * @param location The Location object that the new Location will be copying.
	 */
	public Location (Location location)
	{
		this.location = location.getLocation().clone();
	}
	
	/**
	 * Returns the y component (the first value) of the location. 
	 * 
	 * @return The y component (the first value) of the location. 
	 */
	public int getY()
	{
		return location[0];
	}
	
	/**
	 * Returns the x component (the second value) of the location. 
	 * 
	 * @return The x component (the second value) of the location. 
	 */
	public int getX()
	{
		return location[1];
	}
	
	/**
	 * Returns the Location's location in the form (y,x). 
	 * 
	 * @return Returns the Location's location. 
	 */
	public int[] getLocation()
	{
		return location;
	}
	
	/**
	 * Sets the Location location to the given values. 
	 * 
	 * @param location The new location of the Location. 
	 */
	public void set(int[] location)
	{
		this.location = location.clone();
	}
	
	/**
	 * Sets the Location's location to the given values.
	 * 
	 * @param y The new y (first) component of the Location.
	 * @param x The new x (second) component of the Location.
	 */
	public void set(int y, int x)
	{
		location[0] = y;
		location[1] = x;
	}
	
	/**
	 * Sets the Location to the same location as the given Location
	 * 
	 * @param location The Location that the Location will be copying.
	 */
	public void set(Location location)
	{
		this.location = location.getLocation().clone();
	}
	
	/**
	 * Sets the y (first) component of the Location to the given value.
	 * 
	 * @param y The new y (first) component of the Location.
	 */
	public void setY(int y)
	{
		location[0] = y;
	}
	
	/**
	 * Sets the x (second) component of the Location to the given value. 
	 * 
	 * @param x The new x (second) component of the Location.
	 */
	public void setX(int x)
	{
		location[0] = x;
	}
	
	/**
	 * Translates the Location by the given values.
	 * 
	 * @param location The value by which the Location's location is to be translated.
	 */
	public void translate(int[] location)
	{
		this.location[0] += location[0];
		this.location[1] += location[1];
	}
	
	/**
	 * Translates the Location by the given values.
	 * 
	 * @param y The amount by which the y (first) component of the Location is increased by.
	 * @param x The amount by which the x (second) component of the Location is increased by.
	 */
	public void translate(int y, int x)
	{
		location[0] += y;
		location[1] += x;
	}
	
	/**
	 * Increases the y (first) component of the location by the given value.
	 * 
	 * @param y The amount by which the y (first) component of the Location is increased by.
	 */
	public void translateY(int y)
	{
		location[0] += y;
	}
	
	/**
	 * Increases the x (second) component of the location by the given value.
	 * 
	 * @param x The amount by which the x (second) component of the Location is increased by.
	 */
	public void translateX(int x)
	{
		location[0] += x;
	}
	
	/**
	 * Calculates the taxicab distance between this location and the given Location.
	 * 
	 * @param location The location that the distance to this Location is to be calculated.
	 * @return The taxicab distance between this location and the given location.
	 */
	public int calculateDistance(int[] location)
	{
		return Math.abs(this.location[0] - location[0]) + Math.abs(this.location[1] - location[1]);
	}
	
	/**
	 * Calculates the taxicab distance between this location and the given Location.
	 * 
	 * @param location The Location that the distance to this Location is to be calculated.
	 * @return The taxicab distance between this location and the given location.
	 */
	public int calculateDistance(Location location)
	{
		return Math.abs(this.location[0] - location.getY()) + Math.abs(this.location[1] - location.getX());
	}
	
	/**
	 * Calculates the taxicab distance between this location and the given Location.
	 * 
	 * @param y The y (first) component of the location.
	 * @param x The x (second) component of the location.
	 * @return The taxicab distance between this location and the given location.
	 */
	public int calculateDistance(int y, int x)
	{
		return Math.abs(this.location[0] - y) + Math.abs(this.location[1] - x);
	}
	
	/**
	 * Returns a deep copy of the location.
	 * 
	 * @return The copied Location.
	 */
	public Location clone()
	{
		return new Location(location);
	}
	
	/**
	 * Checks if this Location and a given location are the same.
	 * 
	 * @param location The location that this location is being compared to.
	 * @return Whether they are the same location or not.
	 */
	public boolean equals(int[] location)
	{
		return location[0] == this.location[0] && location[1] == this.location[1];
	}
	
	/**
	 * Checks if this Location and a given location are the same.
	 * 
	 * @param y The y (first) component of the location that this location is being compared to.
	 * @param x The x (second) component of the location that this location is being compared to.
	 * @return Whether they are the same location or not.
	 */
	public boolean equals(int y, int x)
	{
		return y == this.location[0] && x == this.location[1];
	}
	
	/**
	 * Checks if this Location and a given location are the same.
	 * 
	 * @param location The Location that this location is being compared to.
	 * @return Whether they are the same location or not.
	 */
	public boolean equals(Location location)
	{
		return location.getLocation()[0] == this.location[0] && location.getLocation()[1] == this.location[1];
	}
}
