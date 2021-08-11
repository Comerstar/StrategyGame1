package art;

import java.util.ArrayList;

public class Brush {
	
	public static final int DIAMOND_SHAPE = 0;
	public static final int SQUARE_SHAPE = 1;
	public static final int CIRCLE_SHAPE = 2;
	public static final int RECTANGLE_SHAPE = 3;
	public static final int OVAL_SHAPE = 4;
	public static final int FILL_SHAPE = 5;
	private int width;
	private int height;
	private Colour colour1;
	private Colour colour2;
	private int shape;
	private ArrayList<double[]> pixels = new ArrayList<double[]>();
	
	public Brush()
	{
		width = 1;
		height = 1;
		colour1 = new Colour();
		colour2 = new Colour();
		shape = 0;
		updatePixels();
	}
	
	public void setColour1(Colour colour)
	{
		this.colour1 = colour;
	}
	
	public void setColour1(int colour)
	{
		this.colour1.setColour(colour);
	}
	
	public void setColour2(Colour colour)
	{
		this.colour2 = colour;
	}
	
	public void setColour2(int colour)
	{
		this.colour2.setColour(colour);
	}
	
	public Colour getColour1()
	{
		return colour1;
	}
	
	public Colour getColour2()
	{
		return colour2;
	}
	
	public void setWidth(int width)
	{
		this.width = width;
		updatePixels();
	}
	
	public int getWidth()
	{
		return width;
	}
	
	public void setHeight(int height)
	{
		this.height = height;
		updatePixels();
	}
	
	public int getHeight()
	{
		return height;
	}
	
	public void setShape(int shape)
	{
		this.shape = shape;
		updatePixels();
	}
	
	public int getShape()
	{
		return shape;
	}
	
	public ArrayList<double[]> getPixels()
	{
		return pixels;
	}
	
	public void updatePixels()
	{
		pixels = getPixelList();
	}
	
	public ArrayList<double[]> getPixelList()
	{
		ArrayList<double[]> pixelList = new ArrayList<double[]>();
		if(shape == DIAMOND_SHAPE)
		{
			if(width % 2 == 0)
			{
				int k;
				int l;
				for (k = 0; k < width - 1; k++)
				{
					for (l = 0; l < width; l++)
					{
						pixelList.add(new double[] {k - width + 1 + l + 0.5, k - l + 0.5});
					}
				}
				for (k = 0; k < width; k++)
				{
					for (l = 0; l < width - 1; l++)
					{
						pixelList.add(new double[] {k - width + 2 + l - 0.5, k - l - 0.5});
					}
				}
			}
			else
			{
				int k;
				int l;
				for (k = 0; k < width; k++)
				{
					for (l = 0; l < width; l++)
					{
						pixelList.add(new double[] {k - width + 1 + l, k - l});
					}
				}
				for (k = 0; k < width - 1; k++)
				{
					for (l = 0; l < width - 1; l++)
					{
						pixelList.add(new double[] {k - width + + 2 + l, k - l});
					}
				}
			}
		}
		else if(shape == SQUARE_SHAPE)
		{
			if(width % 2 == 0)
			{
				int i;
				int j;
				for(i = (width) / -2 + 1; i <= (width) / 2; i++)
				{
					for(j = (width) / -2 + 1; j <= (width) / 2; j++)
					{
						pixelList.add(new double[] {i - 0.5, j - 0.5});
					}
				}
			}
			else
			{
				int i;
				int j;
				for(i = (width - 1) / -2; i <= (width - 1) / 2; i++)
				{
					for(j = (width - 1) / -2; j <= (width - 1) / 2; j++)
					{
						pixelList.add(new double[] {i, j});
					}
				}
			}
		}
		else if(shape == CIRCLE_SHAPE)
		{
			if(width % 2 == 0)
			{
				int i; 
				int j;
				for(i = (width) / -2 + 1; i <= (width) / 2; i++)
				{
					for(j = (width) / -2 + 1; j <= (width) / 2; j++)
					{
						if((i - 0.5) * (i - 0.5) + (j - 0.5) * (j - 0.5) < width * width / 4)
						{
							pixelList.add(new double[] {i - 0.5, j - 0.5});
						}
					}
				}
			}
			else
			{
				int i; 
				int j;
				for(i = (width - 1) / -2; i <= (width - 1) / 2; i++)
				{
					for(j = (width - 1) / -2; j <= (width - 1) / 2; j++)
					{
						if(i * i + j * j < width * width / 4)
						{
							pixelList.add(new double[] {i, j});
						}
					}
				}
			}
		}
		else if(shape == RECTANGLE_SHAPE)
		{
			int i;
			int j;
			for(i = 0; i < width; i++)
			{
				for(j = 0; j < height; j++)
				{
					pixelList.add(new double[]{i, j});
				}
			}
		}
		else if(shape == OVAL_SHAPE)
		{
			
		}
		else if(shape == FILL_SHAPE)
		{
			
		}
		return pixelList;
	}
}
