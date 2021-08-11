package animation;

public class Camera {
	
	private double x;
	private double y;
	private double zoom;
	private double xm = 0.0;
	private double ym = 0.0;
	private double zm = 0.0;
	private double maxXm = 25.0;
	private double maxYm = 25.0;
	private double maxZm = 0.05;
	private double xa = 0.0;
	private double ya = 0.0;
	private double za = 0.0;
	private double xs = 0.0;
	private double ys = 0.0;
	private double zs = 0.0;
	private boolean xStopping = false;
	private boolean yStopping = false;
	private boolean zStopping = false;
	
	public Camera (double x, double y, double zoom)
	{
		this.x = x; 
		this.y = y; 
		this.zoom = zoom;
	}
	
	public double getX()
	{
		return x;
	}
	
	public double getY()
	{
		return y;
	}
	
	public double getZoom()
	{
		return zoom;
	}
	
	@Deprecated
	public void increaseZoom(double zoom)
	{
		//System.out.println("Increasing Scale");
		if(this.zoom + zoom > 0)
		{
			this.zoom += zoom;
		}
	}
	
	public void increaseZoom(double zoom, int w, int h)
	{
		if(this.zoom + zoom > 0.1)
		{
//			System.out.println("Increasing Scale");
//			System.out.println("width: " + w);
//			System.out.println("height: " + h);
//			System.out.println("X increase: " + (w * zoom) / (2 * this.zoom));
//			System.out.println("Y increase: " + (h * zoom) / (2 * this.zoom));
			translate(((w / this.zoom) - (w / (this.zoom + zoom))) / 2, ((h / this.zoom) - (h / (this.zoom + zoom))) / 2);
			this.zoom += zoom;
		}
	}
	
	@Deprecated
	public void setZoom(double zoom)
	{
		this.zoom = zoom;
	}
	
	public void setZoom(double zoom, int w, int h)
	{
		increaseZoom(zoom - this.zoom, w, h);
	}
	
	@Deprecated
	public void multiplyZoom(double zoom)
	{
		this.zoom *= zoom;
	}
	
	public void multiplyZoom(double zoom, int w, int h)
	{
		increaseZoom(this.zoom * (zoom - 1.0), w, h);
	}
	
	public void increaseX(double x)
	{
		this.x += x;
	}
	
	public void setX(double x)
	{
		this.x = x;
	}
	
	public void multiplyX(double x)
	{
		this.x *= x;
	}
	
	public void increaseY(double y)
	{
		this.y += y;
	}
	
	public void setY(double y)
	{
		this.y = y;
	}
	
	public void multiplyY(double y)
	{
		this.y *= y;
	}
	
	public void translate(double x, double y)
	{
		this.x += x;
		this.y += y;
	}
	
	public void translate()
	{
		x += xm;
		y += ym;
	}
	
	public void cameraTranslate()
	{
		x += xm * (1.0 / zoom);
		y += ym * (1.0 / zoom);
	}
	
	public void cameraMotion(int w, int h)
	{
		x += xm * (1.0 / zoom);
		y += ym * (1.0 / zoom);
		multiplyZoom(1.0 + zm, w, h);
		if(xa < 0)
		{
			if(xm >= xs && xs >= xm + xa && xStopping)
			{
				xm = 0.0;
				xa = 0.0;
//				System.out.println("xm: " + xm);
//				System.out.println("xa: " + xa);
//				System.out.println("xStopping: " + xStopping);
//				System.out.println("stopping");
			}
			else
			{
				increaseXm(xa);
//				System.out.println("xm: " + xm);
//				System.out.println("xa: " + xa);
//				System.out.println("xStopping: " + xStopping);
			}
		}
		else
		{
			if(xm <= xs && xs <= xm + xa && xStopping)
			{
				xm = 0.0;
				xa = 0.0;
//				System.out.println("xm: " + xm);
//				System.out.println("xa: " + xa);
//				System.out.println("xStopping: " + xStopping);
//				System.out.println("stopping");
			}
			else
			{
				increaseXm(xa);
//				System.out.println("xm: " + xm);
//				System.out.println("xa: " + xa);
//				System.out.println("xStopping: " + xStopping);
			}
		}
		if(ya < 0)
		{
			if(ym >= ys && ys >= ym + ya && yStopping)
			{
				ym = 0.0;
				ya = 0.0;
			}
			else
			{
				increaseYm(ya);
			}
		}
		else
		{
			if(ym <= ys && ys <= ym + ya && yStopping)
			{
				ym = 0.0;
				ya = 0.0;
			}
			else
			{
				increaseYm(ya);
			}
		}
		if(za < 0)
		{
			if(zm >= zs && zs >= zm + za && zStopping)
			{
				zm = 0.0;
				za = 0.0;
			}
			else
			{
				increaseZm(za);
			}
		}
		else
		{
			if(zm <= zs && zs <= zm + za && zStopping)
			{
				zm = 0.0;
				za = 0.0;
			}
			else
			{
				increaseZm(za);
			}
		}
	}
	
	public void increaseXm(double x)
	{
		xm += x;
		if(Math.abs(xm) > maxXm)
		{
			xm = Math.copySign(maxXm, xm);
		}
	}
	
	public void increaseYm(double y)
	{
		ym += y;
		if(Math.abs(ym) > maxYm)
		{
			ym = Math.copySign(maxYm, ym);
		}
	}
	
	public void increaseZm(double z)
	{
		zm += z;
		if(Math.abs(zm) > maxZm)
		{
			zm = Math.copySign(maxZm, zm);
		}
	}
	
	public void setXs(double x)
	{
		xs = x;
	}
	
	public void setYs(double y)
	{
		ys = y;
	}
	
	public void setZs(double z)
	{
		zs = z;
	}
	
	public void setXa(double x)
	{
		xa = x;
	}
	
	public void setYa(double y)
	{
		ya = y;
	}
	
	public void setZa(double z)
	{
		za = z;
	}

	public void setXd(double x)
	{
		xa = Math.copySign(x, xm * -1);
	}
	
	public void setYd(double y)
	{
		ya = Math.copySign(y, ym * -1);
	}
	
	public void setZd(double z)
	{
		za = Math.copySign(z, zm * -1);
	}
	
	public void setXm(double x)
	{
		xm = x;
		if(Math.abs(xm) > maxXm)
		{
			xm = Math.copySign(maxXm, xm);
		}
	}
	
	public void setYm(double y)
	{
		ym = y;
		if(Math.abs(ym) > maxYm)
		{
			ym = Math.copySign(maxYm, ym);
		}
	}
	
	public void setZm(double z)
	{
		zm = z;
		if(Math.abs(zm) > maxZm)
		{
			zm = Math.copySign(maxZm, zm);
		}
	}

	public void setMaxXm(double maxXm)
	{
		this.maxXm = maxXm;
		if(Math.abs(xm) > maxXm)
		{
			xm = Math.copySign(maxXm, xm);
		}
	}
	
	public void setMaxYm(double maxYm)
	{
		this.maxYm = maxYm;
		if(Math.abs(ym) > maxYm)
		{
			ym = Math.copySign(maxYm, ym);
		}
	}
	
	public void setMaxZm(double maxZm)
	{
		this.maxZm = maxZm;
		if(Math.abs(zm) > maxZm)
		{
			zm = Math.copySign(maxZm, zm);
		}
	}
	
	public void setXStopping(boolean stopping)
	{
		this.xStopping = stopping;
	}
	
	public void setYStopping(boolean stopping)
	{
		this.yStopping = stopping;
	}
	
	public void setZStopping(boolean stopping)
	{
		this.zStopping = stopping;
	}
}
