package art;

public class Colour {
	
	private int colour;
	private int a;
	// colour stored in rgb
	private int[] rgb;
	// colour stored in hsl
	private double[] hsl;
	
	public Colour(int colour)
	{
		this.colour = colour;
		rgb = new int[3];
		hsl = new double[3];
		updateRGB();
		updateA();
		updateHSL();
	}
	
	public Colour()
	{
		this.colour = 0;
		rgb = new int[3];
		hsl = new double[3];
		updateRGB();
		updateA();
		updateHSL();
	}
	
	public void setColour(int colour)
	{
		this.colour = colour;
		updateRGB();
		updateA();
		updateHSL();
	}
	
	public void setR(int r)
	{
		rgb[0] = r;
		updateColour();
		updateHSL();
	}
	
	public void setG(int g)
	{
		rgb[1] = g;
		updateColour();
		updateHSL();
	}
	
	public void setB(int b)
	{
		rgb[2] = b;
		updateColour();
		updateHSL();
	}
	
	public void setA(int a)
	{
		this.a = a;
		updateColour();
	}
	
	public void setH(double h)
	{
		hsl[0] = h;
		rgb = convertHSLIntoRGB(hsl);
		updateColour();
	}
	
	public void setS(double s)
	{
		hsl[1] = s;
		rgb = convertHSLIntoRGB(hsl);
		updateColour();
	}
	
	public void setL(double l)
	{
		hsl[2] = l;
		rgb = convertHSLIntoRGB(hsl);
		updateColour();
	}
	
	public int getColour()
	{
		return colour;
	}
	
	public void updateColour()
	{
		this.colour = a << 24 | rgb[0] << 16 | rgb[1] << 8 | rgb[2];
	}
	
	public void updateRGB()
	{
		rgb[0] = colour >> 16 & 0xFF;
		rgb[1] = colour >> 8 & 0xFF;
		rgb[2] = colour & 0xFF;
	}
	
	public int[] getRGB()
	{
		return rgb;
	}
	
	public int getRGB(int i)
	{
		return rgb[i];
	}
	
	public int getR()
	{
		return rgb[0];
	}
	
	public int getG()
	{
		return rgb[1];
	}
	
	public int getB()
	{
		return rgb[2];
	}
	
	public int getA()
	{
		return a;
	}
	
	public void updateA()
	{
		a = colour >> 24 & 0xFF;
	}
	
	public void updateHSL()
	{
		hsl = convertRGBIntoHSL(rgb);
	}
	
	public double getH()
	{
		return hsl[0];
	}
	
	public double getS()
	{
		return hsl[1];
	}
	
	public double getL()
	{
		return hsl[2];
	}
	
	public static int getColour(int a, int r, int g, int b)
	{
		return a << 24 | r << 16 | g << 8 | b;
	}
	
	public static int getA(int colour)
	{
		return colour >> 24 & 0xFF;
	}
	
	public static int getR(int colour)
	{
		return colour >> 16 & 0xFF;
	}
	
	public static int getG(int colour)
	{
		return colour >> 8 & 0xFF;
	}
	
	public static int getB(int colour)
	{
		return colour & 0xFF;
	}
	
	public static int[] convertHSLIntoRGB(double[] hsl)
	{
		double h = hsl[0] / 360.0;
		double s = hsl[1] / 100.0;
		double l = hsl[2] / 100.0;
		double r = 0.0;
		double g = 0.0;
		double b = 0.0;
		if(s == 0.0)
		{
			r = l;
			g = l;
			b = l;
		}
		else
		{
			double i;
			double j;
			if(l >= 0.5)
			{
				i = l * (1.0 + s);
			}
			else
			{
				i = l + s - l * s;
			}
			j = 2 * l - i;
			double tr;
			double tg;
			double tb;
			tr = h + 0.33;
			tg = h;
			tb = h - 0.33;
			if(tr < 0.0)
			{
				tr++;
			}
			else if(tr > 1.0)
			{
				tr--;
			}
			if(tg < 0.0)
			{
				tg++;
			}
			else if(tg > 1.0)
			{
				tg--;
			}
			if(tb < 0.0)
			{
				tb++;
			}
			else if(tb > 1.0)
			{
				tb--;
			}
			if(6.0 * tr < 1.0)
			{
				r = j + (i - j * 6.0 * tr);
			}
			else if(2.0 * tr < 1.0)
			{
				r = i;
			}
			else if(3.0 * tr < 2.0)
			{
				r = j + (i - j) * (0.666 - tr) * 6.0;
			}
			else
			{
				r = j;
			}
			if(6.0 * tg < 1.0)
			{
				g = j + (i - j * 6.0 * tg);
			}
			else if(2.0 * tg < 1.0)
			{
				g = i;
			}
			else if(3.0 * tg < 2.0)
			{
				g = j + (i - j) * (0.666 - tg) * 6.0;
			}
			else
			{
				g = j;
			}
			if(6.0 * tb < 1.0)
			{
				b = j + (i - j * 6.0 * tb);
			}
			else if(2.0 * tb < 1.0)
			{
				b = i;
			}
			else if(3.0 * tb < 2.0)
			{
				b = j + (i - j) * (0.666 - tb) * 6.0;
			}
			else
			{
				b = j;
			}
		}
		return new int[] {(int) (r * 255.0), (int) (g * 255.0), (int) (b * 255.0)};
	}
	
	public static double[] convertRGBIntoHSL(int[] rgb)
	{
		double r = rgb[0] / 255.0;
		double g = rgb[1] / 255.0;
		double b = rgb[2] / 255.0;
		double min = Math.min(r,Math.min(g, b));
		double max = Math.max(r,Math.max(g, b));
		double l = (min + max) / 2;
		double s;
		double h;
		if(min == max)
		{
			s = 0;
			h = 0;
		}
		else
		{
			if(l >= 0.5)
			{
				s = (max - min)/(max + min);
			}
			else
			{
				s = (max - min)/(2.0 - max - min);
			}
		}
		if(max == r)
		{
			h = (g - b) / (max - min);
		}
		else if(max == g)
		{
			h = 2.0 + (b - r) / (max - min);	
		}
		else
		{
			h = 4.0 + (r - g) / (max - min);
		}
		h *= 60;
		if(h < 0)
		{
			h += 360;
		}
		s *= 100;
		l *= 100;
		return new double[] {h, s, l};
	}
}
