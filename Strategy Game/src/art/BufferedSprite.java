package art;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import javax.swing.JPanel;

import animation.Camera;

public class BufferedSprite {
	
	public static final int TRANSPARENT_PIXEL = 0;
	protected BufferedImage img;
	protected int x = 0;
	protected int y = 0;
	protected int width = 0;;
	protected int height = 0;
	protected boolean visible = true;
	
	public BufferedSprite(int imgWidth, int imgHeight)
	{
		img = new BufferedImage(imgWidth, imgHeight, BufferedImage.TYPE_4BYTE_ABGR);
		int i;
		int j;
		for(i = 0; i < img.getWidth(); i++)
		{
			for(j = 0; j < img.getHeight(); j++)
			{
				img.setRGB(i, j, TRANSPARENT_PIXEL);
			}
		}
		width = img.getWidth();
		height = img.getHeight();
	}
	
	public BufferedSprite(int x, int y, int imgWidth, int imgHeight)
	{
		img = new BufferedImage(imgWidth, imgHeight, BufferedImage.TYPE_4BYTE_ABGR);
		int i;
		int j;
		for(i = 0; i < img.getWidth(); i++)
		{
			for(j = 0; j < img.getHeight(); j++)
			{
				img.setRGB(i, j, TRANSPARENT_PIXEL);
			}
		}
		this.x = x;
		this.y = y;
		width = img.getWidth();
		height = img.getHeight();
	}
	
	public BufferedSprite(int x, int y, int imgWidth, int imgHeight, int width, int height)
	{
		img = new BufferedImage(imgWidth, imgHeight, BufferedImage.TYPE_4BYTE_ABGR);
		int i;
		int j;
		for(i = 0; i < img.getWidth(); i++)
		{
			for(j = 0; j < img.getHeight(); j++)
			{
				img.setRGB(i, j, TRANSPARENT_PIXEL);
			}
		}
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
	}
	
	public void setCheckedRGB(int x, int y, int colour)
	{
		if(x >= 0 && x < img.getWidth() && y >= 0 && y < img.getHeight())
		{
			img.setRGB(x, y, colour);
		}
	}
	
	public void setRGB(int x, int y, int colour)
	{
		img.setRGB(x, y, colour);
	}
	
	public void setRGB(int colour)
	{
		int i;
		int j;
		for(i = 0; i < img.getWidth(); i++)
		{
			for(j = 0; j < img.getHeight(); j++)
			{
				img.setRGB(i, j, colour);
			}
		}
		System.out.println("Set colour to: " + colour);
	}
	
	public void setVisible()
	{
		visible = true;
	}
	
	public void setInvisible()
	{
		visible = false;
	}
	
	public boolean getVisible()
	{
		return visible;
	}
	
	public int getImgHeight()
	{
		return img.getHeight();
	}
	
	public int getImgWidth()
	{
		return img.getWidth();
	}
	
	public BufferedImage getImg()
	{
		return img;
	}
	
	public void setImg(BufferedImage img)
	{
		this.img = img;
	}
	
	public int getHeight()
	{
		return height;
	}
	
	public int getWidth()
	{
		return width;
	}
	
	public int getRGB(int x, int y)
	{
		return img.getRGB(x, y);
	}
	
	public int getX1(Camera c, int lx)
	{
		return (int) (((lx + ((x))) - c.getX()) * c.getZoom());
	}

	public int getX2(Camera c, int lx)
	{
		return (int) (((lx + ((x + width))) - c.getX()) * c.getZoom());
	}

	public int convertY(Camera c, int y)
	{
		return (int) (((y) - c.getY()) * c.getZoom());
	}

	public int getY1(Camera c, int ly)
	{
		return (int) (((ly + ((y))) - c.getY()) * c.getZoom());
	}

	public int getY2(Camera c, int ly)
	{
		return (int) (((ly + ((y + height))) - c.getY()) * c.getZoom());
	}
	
	public void drawImage(Graphics2D g, Camera c, int lx, int ly)
	{
		if(visible)
		{
			try
			{
				int imageWidth = img.getWidth();
				int imageHeight = img.getHeight();
				g.drawImage(img, getX1(c, lx), getY1(c, ly), getX2(c, lx), getY2(c, ly), 0, 0, imageWidth, imageHeight, null);
				System.out.println("Image Width: " + img.getWidth());
				System.out.println("Image Height: " + img.getHeight());
				//System.out.println("X1: " + getX1(c, lx));
				//System.out.println("Y1: " + getY1(c, ly));
				//System.out.println("X2: " + getX2(c, lx));
				//System.out.println("Y2: " + getY2(c, ly));
			}
			catch(Exception e)
			{
				System.out.print(e);
				System.out.println();
			}
		}
	}
	
	public void drawImage(Graphics g, Camera c, int lx, int ly, JPanel panel)
	{
		if(visible)
		{
			try
			{
				int imageWidth = img.getWidth();
				int imageHeight = img.getHeight();
				g.drawImage(img, getX1(c, lx), getY1(c, ly), getX2(c, lx), getY2(c, ly), 0, 0, imageWidth, imageHeight, panel);
				//System.out.println("Image Width: " + img.getWidth());
				//System.out.println("Image Height: " + img.getHeight());
				//System.out.println("X1: " + getX1(c, lx));
				//System.out.println("Y1: " + getY1(c, ly));
				//System.out.println("X2: " + getX2(c, lx));
				//System.out.println("Y2: " + getY2(c, ly));
			}
			catch(Exception e)
			{
				System.out.print(e);
				System.out.println();
			}
		}
	}
	
	public void drawImage(Graphics g, Camera c, int lx, int ly)
	{
		if(visible)
		{
			try
			{
				int imageWidth = img.getWidth();
				int imageHeight = img.getHeight();
				g.drawImage(img, getX1(c, lx), getY1(c, ly), getX2(c, lx), getY2(c, ly), 0, 0, imageWidth, imageHeight, null);
				//System.out.println("Image Width: " + img.getWidth());
				//System.out.println("Image Height: " + img.getHeight());
				//System.out.println("X1: " + getX1(c, lx));
				//System.out.println("Y1: " + getY1(c, ly));
				//System.out.println("X2: " + getX2(c, lx));
				//System.out.println("Y2: " + getY2(c, ly));
			}
			catch(Exception e)
			{
				System.out.print(e);
				System.out.println();
			}
		}
	}
	
	public void drawImageBorder(Graphics g, Camera c, int lx, int ly)
	{
		if(visible)
		{
			try
			{
				int imageWidth = img.getWidth();
				int imageHeight = img.getHeight();
				int x1 = getX1(c, lx);
				int y1 = getY1(c, ly);
				int x2 = getX2(c, lx);
				int y2 = getY2(c, ly);
				g.drawImage(img, x1, y1, x2, y2, 0, 0, imageWidth, imageHeight, null);
				g.drawRect(x1, y1, x2 - x1, y2 - y1);
				//System.out.println("Image Width: " + img.getWidth());
				//System.out.println("Image Height: " + img.getHeight());
				//System.out.println("X1: " + getX1(c, lx));
				//System.out.println("Y1: " + getY1(c, ly));
				//System.out.println("X2: " + getX2(c, lx));
				//System.out.println("Y2: " + getY2(c, ly));
			}
			catch(Exception e)
			{
				System.out.print(e);
				System.out.println();
			}
		}
	}
	
	public void drawImg(BufferedSprite sprite)
	{
		Graphics g = img.getGraphics();
		g.drawImage(sprite.getImg(), 0, 0, sprite.getImgWidth(), sprite.getImgHeight(), 0, 0, sprite.getImgWidth(), sprite.getImgHeight(), null);
	}
	
	public boolean inImage(int x, int y)
	{
		return x >= 0 && x < img.getWidth() && y >= 0 && y < img.getHeight();
	}
	
	public boolean checkIfIn(Camera c, Point p, int lx, int ly)
	{
		return getX1(c, lx) <= p.getX() && p.getX() <= getX2(c, lx) && getY1(c, ly) <= p.getY() && p.getY() <= getY2(c, ly) && visible;
	}
	
	public boolean checkIfYIn(Camera c, Point p, int ly)
	{
		return getY1(c, ly) <= p.getY() && p.getY() <= getY2(c, ly) && visible;
	}
	
	public boolean checkIfXIn(Camera c, Point p, int lx)
	{
		return getX1(c, lx) <= p.getX() && p.getX() <= getX2(c, lx) && visible;
	}
	
	public static int colourMath(int c1, int a1, int c2, int a2)
	{
		double dc = (- c1 * ((double) (a1) / (double) (a1 + a2)) - c2 * ((double) (a2) / (double) (a1 + a2)));
		int sc = (int) (c2 + dc * (a2 / 255.0));
		return (int) sc;
	}
	
	public static void main(String[] args)
	{
		colourTest2();
	}
	
	public static void colourTest2()
	{
		BufferedImage[] img = test();
		for(int i = 0; i < 3; i++)
		{
			for(int j = 0; j < 3; j++)
			{
				System.out.println("-Pixel(" + i + "," + j + ")------------------");
				System.out.println("Expected A Value: " + Colour.getA(img[0].getRGB(i, j)));
				System.out.println("Actual   A Value: " + Colour.getA(img[1].getRGB(i, j)));
				System.out.println("Expected R Value: " + Colour.getR(img[0].getRGB(i, j)));
				System.out.println("Actual   R Value: " + Colour.getR(img[1].getRGB(i, j)));
				System.out.println("Expected G Value: " + Colour.getG(img[0].getRGB(i, j)));
				System.out.println("Actual   G Value: " + Colour.getG(img[1].getRGB(i, j)));
				System.out.println("Expected B Value: " + Colour.getB(img[0].getRGB(i, j)));
				System.out.println("Actual   B Value: " + Colour.getB(img[1].getRGB(i, j)));
			}
		}
	}
	
	public static void colourTest()
	{
		System.out.println("c1 = 128, c2 = 128, a1 = 128, a2 = 128, expected result: 128");
		System.out.println("Actual Result: " + colourMath(128, 128, 128, 128));
		System.out.println("c1 = 128, c2 = 128, a1 = 0, a2 = 128, expected result: 128");
		System.out.println("Actual Result: " + colourMath(128, 64, 128, 200));
		System.out.println("c1 = 128, c2 = 0, a1 = 128, a2 = 255, expected result: 0");
		System.out.println("Actual Result: " + colourMath(128, 128, 0, 255));
		System.out.println("c1 = 255, c2 = 128, a1 = 0, a2 = 128, expected result: 128");
		System.out.println("Actual Result: " + colourMath(255, 0, 128, 128));
		System.out.println("c1 = 128, c2 = 0, a1 = 255, a2 = 255, expected result: 255");
		System.out.println("Actual Result: " + colourMath(128, 128, 255, 255));
		System.out.println("c1 = 255, c2 = 128, a1 = 128, a2 = 0, expected result: 255");
		System.out.println("Actual Result: " + colourMath(255, 128, 128, 0));
	}
	
	public static BufferedImage mergeLayers(ArrayList<BufferedSprite> layerList)
	{
		BufferedImage img = new BufferedImage(layerList.get(0).getImgWidth(), layerList.get(0).getImgHeight(), BufferedImage.TYPE_4BYTE_ABGR);
		int j;
		int k;
		int a1;
		int a2;
		int r1;
		int r2;
		int g1;
		int g2;
		int b1;
		int b2;
		int c1;
		int c2;
//		int dr;
//		int dg;
//		int db;
		int sa;
		int sr;
		int sg;
		int sb;
		for(int i = 0; i < layerList.size(); i++)
		{
			for(j = 0; j < img.getWidth(); j++)
			{
				for(k = 0; k < img.getHeight(); k++)
				{
					a2 = Colour.getA(layerList.get(i).getRGB(j, k));
					if(a2 == 0xFF)
					{
						img.setRGB(j, k, layerList.get(i).getRGB(j, k));
						System.out.println("Opaque");
					}
					else if(a2 != 0)
					{
						c1 = layerList.get(i).getRGB(j, k);
						c2 = img.getRGB(j, k);
						r2 = Colour.getR(c1);
						g2 = Colour.getG(c1);
						b2 = Colour.getB(c1);
						a1 = Colour.getA(c2);
						r1 = Colour.getR(c2);
						g1 = Colour.getG(c2);
						b1 = Colour.getB(c2);
//						dr = Math.abs(r1 - r2);
//						dg = Math.abs(g1 - g2);
//						db = Math.abs(b1 - b2);
						sa = (int) ((255.0 - (double) a2) * ((double) a1 / 255.0) + (double) a2);
						sr = colourMath(r1, a1, r2, a2);
						sg = colourMath(g1, a1, g2, a2);
						sb = colourMath(b1, a1, b2, a2);
						img.setRGB(j, k, Colour.getColour(sa, sr, sg, sb));
						System.out.println("Translucent");
						System.out.println("a1: " + a1);
						System.out.println("r1: " + r1);
						System.out.println("g1: " + g1);
						System.out.println("b1: " + b1);
						System.out.println("a2: " + a2);
						System.out.println("r2: " + r2);
						System.out.println("g2: " + g2);
						System.out.println("b2: " + b2);
						System.out.println("sa: " + sa);
						System.out.println("sr: " + sr);
						System.out.println("sg: " + sg);
						System.out.println("sb: " + sb);
					}
				}
			}
		}
		return img;
	}
	
	public static BufferedImage[] test()
	{
		BufferedSprite test = new BufferedSprite(3, 3);
		BufferedSprite test1 = new BufferedSprite(3, 3);
		ArrayList<BufferedSprite> testList = new ArrayList<BufferedSprite>();
		testList.add(test);
		testList.add(test1);
		test.setRGB(0, 0, Colour.getColour(128, 255, 0, 0));
		test.setRGB(0, 1, Colour.getColour(128, 255, 0, 0));
		test.setRGB(0, 2, Colour.getColour(128, 255, 0, 0));
		test.setRGB(1, 0, Colour.getColour(255, 255, 0, 255));
		test.setRGB(1, 1, Colour.getColour(255, 255, 0, 255));
		test.setRGB(1, 2, Colour.getColour(255, 255, 0, 255));
		test.setRGB(2, 0, Colour.getColour(128, 0, 0, 255));
		test.setRGB(2, 1, Colour.getColour(128, 0, 0, 255));
		test.setRGB(2, 2, Colour.getColour(128, 0, 0, 255));
		test1.setRGB(0, 0, Colour.getColour(128, 255, 0, 0));
		test1.setRGB(1, 0, Colour.getColour(128, 255, 0, 0));
		test1.setRGB(2, 0, Colour.getColour(128, 255, 0, 0));
		test1.setRGB(0, 1, Colour.getColour(255, 255, 0, 255));
		test1.setRGB(1, 1, Colour.getColour(255, 255, 0, 255));
		test1.setRGB(2, 1, Colour.getColour(255, 255, 0, 255));
		test1.setRGB(0, 2, Colour.getColour(128, 0, 0, 255));
		test1.setRGB(1, 2, Colour.getColour(128, 0, 0, 255));
		test1.setRGB(2, 2, Colour.getColour(128, 0, 0, 255));
		BufferedImage img = mergeLayers(testList);
		test.drawImg(test1);
		return new BufferedImage[] {test.getImg(), img};
	}
}
