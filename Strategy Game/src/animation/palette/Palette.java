/**
 * 
 */
package animation.palette;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.net.URL;

import javax.swing.ImageIcon;

/**
 * A class that holds palettes for players. Holds the standard colours to replace and the colours that they are to be replaced with.
 * 
 * @author the Programmer (Comerstar)
 */
public class Palette {
	
	public static final BufferedImage PALETTE_R = getBufferedImageFromURL("resources/sprites/palettes/paletteR.png");
	public static final int[] PALETTE_R_LIST = getColourRList(PALETTE_R); 
	public static final Palette DEFAULT_PALETTE = new Palette("paletteR");
	private BufferedImage palette;
	private int[] paletteList; 
	
	public Palette(String paletteName)
	{
		palette = getBufferedImageFromURL("resources/sprites/palettes/" + paletteName + ".png");
		paletteList = getColourList(palette);
	}
	
	public static int[] getColourRList(BufferedImage palette)
	{
		int[] colourList = new int[palette.getHeight()];
		for(int i = 0; i < palette.getHeight(); i++)
		{
			colourList[i] = palette.getRGB(0, i);
		}
		return colourList;
	}
	
	public static int[] getColourList(BufferedImage palette)
	{
		int[] colourList = new int[palette.getHeight()];
		for(int i = 0; i < palette.getHeight(); i++)
		{
			colourList[i] = palette.getRGB(1, i);
		}
		return colourList;
	}
	
	public static BufferedImage getBufferedImageFromURL(String dir)
	{
		ImageIcon icon = null;
		URL imgURL = Palette.class.getClassLoader().getResource(dir);
		BufferedImage img = null;
		if(imgURL != null)
		{
			icon = new ImageIcon(imgURL);
			img = new BufferedImage(icon.getIconWidth(), icon.getIconHeight(), BufferedImage.TYPE_INT_ARGB);
			Graphics2D g = img.createGraphics();
			g.drawImage(icon.getImage(), 0, 0, null);
			g.dispose();
		}
		else
		{
			System.out.println("Unable to Find: " + dir);
		}
		return img;
	}
	
	public int getColour(int colour)
	{
		for(int i = 0; i < PALETTE_R_LIST.length; i++)
		{
			if(PALETTE_R_LIST[i] == colour)
			{
				return paletteList[i];
			}
		}
		return colour;
	}
}
