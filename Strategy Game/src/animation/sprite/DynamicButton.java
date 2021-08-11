package animation.sprite;

import java.awt.Point;

import animation.Camera;

public class DynamicButton extends DynamicTile implements ButtonInterface{
	
	private boolean mouseOver;
	
	public DynamicButton(DynamicTile tile) {
		super(tile);
		mouseOver = false;
	}
	
	public static DynamicButton makeDynamicButton(DynamicTile tile)
	{
		return new DynamicButton(tile);
	}
	
	public void setDynamicButton(DynamicTile tile)
	{
		setDynamicTileData(tile);
		mouseOver = false;
	}
	
	public void setDynamicButton(DynamicButton tile)
	{
		setDynamicTileData(tile);
		mouseOver = tile.getMouseOver();
	}

	@Override
	public void setOver(Point p, Camera c, int lx, int ly, double lz) {
		mouseOver = checkIfIn(p, c, lx, ly, lz);
	}

	@Override
	public void setOver(int px, int py, Camera c, int lx, int ly, double lz) {
		mouseOver = checkIfIn(px, py, c, lx, ly, lz);
	}

	@Override
	public void setOver(boolean mouseOver) {
		this.mouseOver = mouseOver;
	}

	@Override
	public boolean getMouseOver() {
		return mouseOver;
	}
	
}
