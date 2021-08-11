package art;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.awt.Point;
import java.awt.Toolkit;
import java.util.ArrayList;

import javax.imageio.ImageIO;
import javax.swing.JPanel;

import animation.Camera;
import animation.layer.Layer;
import animation.layer.TextBox;
import animation.sprite.Font;

public class ArtPanel extends JPanel implements Runnable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private boolean running = false;
	private int delay = 0;
	private int frameRate = 30;
	private Camera camera = new Camera(-5.0, -1.0, 50.0);
	private Camera zeroCamera = new Camera(0.0, 0.0, 1.0);
	private ArrayList<ArrayList<BufferedSprite>> layerList = new ArrayList<ArrayList<BufferedSprite>>();
	private int i;
	private Thread thread;
	private int layer = 0;
	private int frame = 0;
	private Brush brush = new Brush();
	private String setting = "";
	private String writing = "";
	int colour = 0;
	private boolean alt = false;
	private boolean shift = false;
	private boolean capLock = false;
	private boolean command = false;
	//private boolean control = false;
	//private boolean shift = false;
	// slider 0 = r/h, 1 = g/s, 2 = b/l, 3 = a
	private ArrayList<Slider> sliderList = new ArrayList<Slider>(4);
	// which colour is being edited
	private int currentColour = -1;
	// which slider is being dragged
	private int currentSlider = -1;
	// 0 = sliderW, 1 = sliderH, 2 = patchW, 3 = patchH, 4 = textBoxH, 5 = buffer, 6 = stageTextBoxH
	//                                   0,   1,  2,  3,   4, 5,  6, 
	private int[] menuSize = new int[] {10, 350, 32, 32, 128, 5, 64};
	// 0 = rgb, 1 = hsl
	private int colourMode = 0;
	// Colour Patches for the brush colours
	private ArrayList<BufferedSprite> colourPatchList = new ArrayList<BufferedSprite> (2);
	private Layer textLayer = new Layer();
	private Font standardFont = new Font("standard");
	private double textSize = 2.0;
	private int leftToolBarWidth = 0;
	private int rightToolBarWidth = 0;
	private Color toolBarColour = Color.LIGHT_GRAY;
	private Color borderColour = Color.BLACK;
	
	public ArtPanel()
	{
		MouseAdapter mouseListener = new MouseAdapter();
		addMouseListener(mouseListener);
		addMouseMotionListener(mouseListener);
		addMouseWheelListener(mouseListener);
		addKeyListener(new KeyAdapter());
		addComponentListener(new PanelListener());
		setFocusable(true);
		startThread();
		ArrayList<BufferedSprite> lList = new ArrayList<BufferedSprite>();
		lList.add(new BufferedSprite(32,64));
		BufferedImage testImage = BufferedSprite.test()[1];
		for(int i = 0; i < 3; i++)
		{
			for(int j = 0; j < 3; j++)
			{
				lList.get(0).setRGB(i, j, testImage.getRGB(i, j));
			}
		}
		layerList.add(lList);
		for(int i = 0; i < 4; i++)
		{
			sliderList.add(new Slider(menuSize[5] + (menuSize[0] + 2) * i,58, menuSize[0], menuSize[1], 0, 255));
			System.out.println(5 + menuSize[0] * i);
			sliderList.get(i).setInvisible();
		}
		for(int i = 0; i < 2; i++)
		{
			colourPatchList.add(new BufferedSprite(menuSize[5] + menuSize[2] / 2 * i, menuSize[5] + menuSize[3] / 2 * i, menuSize[2], menuSize[3]));
		}
		colourPatchList.get(0).setRGB(brush.getColour1().getColour());
		colourPatchList.get(1).setRGB(brush.getColour2().getColour());
		leftToolBarWidth = Math.max(menuSize[5] + (menuSize[0] + 2) * 3 + menuSize[0], menuSize[5] + menuSize[2] / 2 * 1 + menuSize[2]) + menuSize[5];
		textLayer.addSprite(new TextBox(leftToolBarWidth + menuSize[5], this.getHeight() - menuSize[4] - menuSize[6], 
				this.getWidth() - leftToolBarWidth - rightToolBarWidth, menuSize[6], 1.0, false, "Hello", standardFont, textSize));
		setTextBoxText("Appear Please", 0);
		textLayer.addSprite(new TextBox(leftToolBarWidth + menuSize[5], this.getHeight() - menuSize[4], this.getWidth() - leftToolBarWidth - rightToolBarWidth,
				menuSize[4], 1.0, false, "Why won't this appear. Please appear, please....", standardFont, textSize));
		setTextBoxText("Appear Please, pretty please, I mean why won't you appear. Do you hate me or something? What did I ever do wrong? Please Tell me, explain.", 1);
	}
	
	public void setTextBoxText(String text, int box)
	{
		((TextBox) textLayer.getSpriteList().get(box)).setDisplay(text, textSize);
	}
	
	public void setTextBoxVisible(int box)
	{
		textLayer.getSpriteList().get(box).setVisible();
		System.out.println("Box " + box + " set Visible.");
	}
	
	public void setTextBoxInvisible(int box)
	{
		textLayer.getSpriteList().get(box).setInvisible();
	}

	public void updateTextBoxSizes()
	{
		textLayer.getSpriteList().get(0).setLocation(leftToolBarWidth + menuSize[5], this.getHeight() - menuSize[4] - menuSize[6]);
		System.out.println("TextBox Location: " + (leftToolBarWidth + menuSize[5]) + "," + (this.getHeight() - menuSize[4] - menuSize[6]));
		textLayer.getSpriteList().get(0).setWidth(this.getWidth() - leftToolBarWidth - rightToolBarWidth - menuSize[5] * 2);
		System.out.println("TextBox Width: " + (this.getWidth() - leftToolBarWidth - rightToolBarWidth - menuSize[5] * 2));
		textLayer.getSpriteList().get(0).setHeight(menuSize[6]);
		System.out.println("TextBox Height: " + (menuSize[6]));
		textLayer.getSpriteList().get(1).setLocation(leftToolBarWidth + menuSize[5], 
				this.getHeight() - menuSize[4]);
		System.out.println("TextBox Location: " + (leftToolBarWidth + menuSize[5]) + "," + (this.getHeight() - menuSize[4]));
		textLayer.getSpriteList().get(1).setWidth(this.getWidth() - leftToolBarWidth - rightToolBarWidth - menuSize[5] * 2);
		System.out.println("TextBox Width: " + (this.getWidth() - leftToolBarWidth - rightToolBarWidth - menuSize[5] * 2));
		textLayer.getSpriteList().get(1).setHeight(menuSize[4]);
		System.out.println("TextBox Height: " + (menuSize[4]));
		System.out.println("Window Width: " + this.getWidth());
		System.out.println("Window Height: " + this.getHeight());
	}
	
	public void startThread()
	{
		if(thread == null)
		{
			thread = new Thread(this);
			System.out.println("startingThread");
			thread.start();
			System.out.println("started?Thread");
		}
	}
	
	public void run()
	{
		System.out.println("startingRunning");
		long beforeTime = System.currentTimeMillis();
		delay = 1000 / frameRate;
		running = true;
		while(running)
		{
			delayRun(beforeTime);
			beforeTime = System.currentTimeMillis();
			repaintComponent();
		}
	}

	public void delayRun(long beforeTime)
	{
		long timeDiff, sleep;
		timeDiff = System.currentTimeMillis() - beforeTime;
		sleep = delay - timeDiff;
		
		if(sleep < 0)
		{
			sleep = 2;
		}
		
		try 
		{
			Thread.sleep(sleep);
		}
		catch (InterruptedException e)
		{
			System.out.println("Sleep Error");
		}
	}
	
	public void repaintComponent()
	{
		repaint();
		Toolkit.getDefaultToolkit().sync();
		camera.cameraMotion(this.getWidth(), this.getHeight());
		Toolkit.getDefaultToolkit().sync();
	}
	
	@Override
	public void paintComponent(Graphics g)
	{
		g.clearRect(0, 0, getWidth(), getHeight());
		g.setColor(borderColour);
		for(i = 0; i < layerList.get(frame).size(); i++)
		{
			try
			{
				layerList.get(frame).get(i).drawImageBorder(g, camera, 0, 0);
			}
			catch(IndexOutOfBoundsException e)
			{
				
			}
		}
		if(writing.equals("string") || writing.equals("int"))
		{
			g.setColor(toolBarColour);
			g.fillRect(leftToolBarWidth, textLayer.getSpriteList().get(0).getY(), getWidth() - rightToolBarWidth, getHeight());
			g.setColor(borderColour);
			g.drawLine(leftToolBarWidth, textLayer.getSpriteList().get(0).getY(), getWidth() - rightToolBarWidth, textLayer.getSpriteList().get(0).getY());
		}
		textLayer.drawLayer(g, zeroCamera);
		g.setColor(toolBarColour);
		g.fillRect(0, 0, leftToolBarWidth, getHeight());
		g.setColor(borderColour);
		g.drawLine(leftToolBarWidth, 0, leftToolBarWidth, getHeight());
		for(i = 0; i < sliderList.size(); i++)
		{
			sliderList.get(i).drawImageBorder(g, zeroCamera, 0, 0);
		}
		for(i = colourPatchList.size() - 1; i >= 0; i--)
		{
			colourPatchList.get(i).drawImageBorder(g, zeroCamera, 0, 0);
		}
	}
	
	public void updatePatches()
	{
		if(currentColour == 0)
		{
			colourPatchList.get(0).setRGB(brush.getColour1().getColour());
		}
		else if(currentColour == 1)
		{
			colourPatchList.get(1).setRGB(brush.getColour2().getColour());
		}
	}
	
	public void updateSliders()
	{
		if(colourMode == 0)
		{
			if(this.currentColour == 0)
			{
				int i;
				int j;
				for(i = 0; i < sliderList.get(0).getHeight(); i++)
				{
					for(j = 0; j < sliderList.get(0).getWidth(); j++)
					{
						sliderList.get(0).setRGB(j, i, Colour.getColour(255, sliderList.get(0).convertImgYIntoSlider(i), 0, 0));
					}
				}
				for(i = 0; i < sliderList.get(1).getHeight(); i++)
				{
					for(j = 0; j < sliderList.get(1).getWidth(); j++)
					{
						sliderList.get(1).setRGB(j, i, Colour.getColour(255, brush.getColour1().getR(), sliderList.get(1).convertImgYIntoSlider(i), 0));
					}
				}
				for(i = 0; i < sliderList.get(2).getHeight(); i++)
				{
					for(j = 0; j < sliderList.get(2).getWidth(); j++)
					{
						sliderList.get(2).setRGB(j, i, Colour.getColour(255, brush.getColour1().getR(), brush.getColour1().getG(), sliderList.get(2).convertImgYIntoSlider(i)));
					}
				}
				for(i = 0; i < sliderList.get(3).getHeight(); i++)
				{
					for(j = 0; j < sliderList.get(3).getWidth(); j++)
					{
						sliderList.get(3).setRGB(j, i, Colour.getColour(sliderList.get(3).convertImgYIntoSlider(i), brush.getColour1().getR(), brush.getColour1().getG(), brush.getColour1().getB()));
					}
				}
			}
			else if(this.currentColour == 1)
			{
				int i;
				int j;
				for(i = 0; i < sliderList.get(0).getHeight(); i++)
				{
					for(j = 0; j < sliderList.get(0).getWidth(); j++)
					{
						sliderList.get(0).setRGB(j, i, Colour.getColour(255, sliderList.get(0).convertImgYIntoSlider(i), 0, 0));
					}
				}
				for(i = 0; i < sliderList.get(1).getHeight(); i++)
				{
					for(j = 0; j < sliderList.get(1).getWidth(); j++)
					{
						sliderList.get(1).setRGB(j, i, Colour.getColour(255, brush.getColour2().getR(), sliderList.get(1).convertImgYIntoSlider(i), 0));
					}
				}
				for(i = 0; i < sliderList.get(2).getHeight(); i++)
				{
					for(j = 0; j < sliderList.get(2).getWidth(); j++)
					{
						sliderList.get(2).setRGB(j, i, Colour.getColour(255, brush.getColour2().getR(), brush.getColour2().getG(), sliderList.get(2).convertImgYIntoSlider(i)));
					}
				}
				for(i = 0; i < sliderList.get(3).getHeight(); i++)
				{
					for(j = 0; j < sliderList.get(3).getWidth(); j++)
					{
						sliderList.get(3).setRGB(j, i, Colour.getColour(sliderList.get(3).convertImgYIntoSlider(i), brush.getColour2().getR(), brush.getColour2().getG(), brush.getColour2().getB()));
					}
				}
			}
		}
	}
	
	private class KeyAdapter implements KeyListener
	{
		private int value = 0;
		private String stringValue = "";
		
		public KeyAdapter()
		{
			
		}
		
		@Override
		public void keyTyped(KeyEvent e) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void keyPressed(KeyEvent e) {
			int key = e.getKeyCode();
			if(key == KeyEvent.VK_DOWN)
			{
				System.out.println("Down Pressed");
				camera.setYa(4.0);
				camera.setYStopping(false);
			}
			else if(key == KeyEvent.VK_UP)
			{
				System.out.println("Up Pressed");
				camera.setYa(-4.0);
				camera.setYStopping(false);
			}
			else if(key == KeyEvent.VK_LEFT)
			{
				System.out.println("Left Pressed");
				camera.setXa(-4.0);
				camera.setXStopping(false);
			}
			else if(key == KeyEvent.VK_RIGHT)
			{
				System.out.println("Right Pressed");
				camera.setXa(4.0);
				camera.setXStopping(false);
			}
			else if(key == KeyEvent.VK_A)
			{
				System.out.println("A Pressed");
				if(writing.equals("string"))
				{
					if(shift ^ capLock)
					{
						stringValue += "A";
					}
					else
					{
						stringValue += "a";
					}
				}
			}
			else if(key == KeyEvent.VK_B)
			{
				System.out.println("B Pressed");
				if(writing.equals("string"))
				{
					if(shift ^ capLock)
					{
						stringValue += "B";
					}
					else
					{
						stringValue += "b";
					}
				}
			}
			else if(key == KeyEvent.VK_C)
			{
				System.out.println("C Pressed");
				if(writing.equals("string"))
				{
					if(shift ^ capLock)
					{
						stringValue += "C";
					}
					else
					{
						stringValue += "c";
					}
				}
			}
			else if(key == KeyEvent.VK_D)
			{
				System.out.println("D Pressed");
				if(writing.equals("string"))
				{
					if(shift ^ capLock)
					{
						stringValue += "D";
					}
					else
					{
						stringValue += "d";
					}
				}
			}
			else if(key == KeyEvent.VK_E)
			{
				System.out.println("E Pressed");
				if(writing.equals("string"))
				{
					if(shift ^ capLock)
					{
						stringValue += "E";
					}
					else
					{
						stringValue += "e";
					}
				}
			}
			else if(key == KeyEvent.VK_F)
			{
				System.out.println("F Pressed");
				if(writing.equals("string"))
				{
					if(shift ^ capLock)
					{
						stringValue += "F";
					}
					else
					{
						stringValue += "f";
					}
				}
			}
			else if(key == KeyEvent.VK_G)
			{
				System.out.println("G Pressed");
				if(writing.equals("string"))
				{
					if(shift ^ capLock)
					{
						stringValue += "G";
					}
					else
					{
						stringValue += "g";
					}
				}
			}
			else if(key == KeyEvent.VK_H)
			{
				System.out.println("H Pressed");
				if(writing.equals("string"))
				{
					if(shift ^ capLock)
					{
						stringValue += "H";
					}
					else
					{
						stringValue += "h";
					}
				}
			}
			else if(key == KeyEvent.VK_I)
			{
				System.out.println("I Pressed");
				if(writing.equals("string"))
				{
					if(shift ^ capLock)
					{
						stringValue += "I";
					}
					else
					{
						stringValue += "i";
					}
				}
			}
			else if(key == KeyEvent.VK_J)
			{
				System.out.println("J Pressed");
				if(writing.equals("string"))
				{
					if(shift ^ capLock)
					{
						stringValue += "J";
					}
					else
					{
						stringValue += "j";
					}
				}
			}
			else if(key == KeyEvent.VK_K)
			{
				System.out.println("K Pressed");
				if(writing.equals("string"))
				{
					if(shift ^ capLock)
					{
						stringValue += "K";
					}
					else
					{
						stringValue += "k";
					}
				}
			}
			else if(key == KeyEvent.VK_L)
			{
				System.out.println("L Pressed");
				if(command && !shift)
				{
					setting = "load";
					stringValue = System.getProperty("user.dir");
					writing = "string";
					setTextBoxText("Load", 0);
					setTextBoxVisible(0);
					setTextBoxText(stringValue, 1);
					setTextBoxVisible(1);
				}
				else if(command && shift)
				{
					setting = "loadType";
					stringValue = "";
					writing = "string";
					setTextBoxText("Load: Sprite Type", 0);
					setTextBoxVisible(0);
					setTextBoxText(stringValue, 1);
					setTextBoxVisible(1);
				}
				else if(writing.equals("string"))
				{
					if(shift ^ capLock)
					{
						stringValue += "L";
					}
					else
					{
						stringValue += "l";
					}
				}
			}
			else if(key == KeyEvent.VK_M)
			{
				System.out.println("M Pressed");
				if(command)
				{
					if(layer > 0)
					{
						layer--;
						ArrayList<BufferedSprite> tempList =  new ArrayList<BufferedSprite>();
						tempList.add(layerList.get(frame).get(layer));
						tempList.add(layerList.get(frame).get(layer + 1));
						layerList.get(frame).get(layer).setImg(BufferedSprite.mergeLayers(tempList));
						layerList.get(frame).remove(layer + 1);
					}
				}
				if(writing.equals("string"))
				{
					if(shift ^ capLock)
					{
						stringValue += "M";
					}
					else
					{
						stringValue += "m";
					}
				}
			}
			else if(key == KeyEvent.VK_N)
			{
				System.out.println("N Pressed");
				if(command && !shift && !alt)
				{
					setting = "newWidth";
					value = 0;
					writing = "int";
					setTextBoxText("New File: Width", 0);
					setTextBoxVisible(0);
					setTextBoxText(String.valueOf(value), 1);
					setTextBoxVisible(1);
				}
				else if(command && shift && !alt)
				{
					
				}
				else if (command && !shift && alt)
				{
					layerList.get(frame).add(layer + 1, new BufferedSprite(layerList.get(frame).get(layer).getImgWidth(), 
							layerList.get(frame).get(layer).getImgHeight()));
					layer++;
				}
				if(writing.equals("string"))
				{
					if(shift ^ capLock)
					{
						stringValue += "N";
					}
					else
					{
						stringValue += "n";
					}
				}
			}
			else if(key == KeyEvent.VK_O)
			{
				System.out.println("O Pressed");
				if(writing.equals("string"))
				{
					if(shift ^ capLock)
					{
						stringValue += "O";
					}
					else
					{
						stringValue += "o";
					}
				}
			}
			else if(key == KeyEvent.VK_P)
			{
				System.out.println("P Pressed");
				if(writing.equals("string"))
				{
					if(shift ^ capLock)
					{
						stringValue += "P";
					}
					else
					{
						stringValue += "p";
					}
				}
			}
			else if(key == KeyEvent.VK_Q)
			{
				System.out.println("Q Pressed");
				if(writing.equals("string"))
				{
					if(shift ^ capLock)
					{
						stringValue += "Q";
					}
					else
					{
						stringValue += "q";
					}
				}
			}
			else if(key == KeyEvent.VK_R)
			{
				System.out.println("R Pressed");
				if(writing.equals("string"))
				{
					if(shift ^ capLock)
					{
						stringValue += "R";
					}
					else
					{
						stringValue += "r";
					}
				}
			}
			else if(key == KeyEvent.VK_S)
			{
				System.out.println("S Pressed");
				if(command && !shift)
				{
					setting = "save";
					stringValue = System.getProperty("user.dir");
					writing = "string";
					setTextBoxText("Save", 0);
					setTextBoxVisible(0);
					setTextBoxText(stringValue, 1);
					setTextBoxVisible(1);
				}
				else if(command && shift)
				{
					setting = "saveType";
					stringValue = "";
					writing = "string";
					setTextBoxText("Save: Sprite Type", 0);
					setTextBoxVisible(0);
					setTextBoxText(stringValue, 1);
					setTextBoxVisible(1);
				}
				else if(writing.equals("string"))
				{
					if(shift ^ capLock)
					{
						stringValue += "S";
					}
					else
					{
						stringValue += "s";
					}
				}
				else if(setting.equals(""))
				{
					camera.setZa(-0.01);
					camera.setZStopping(false);;
				}
			}
			else if(key == KeyEvent.VK_T)
			{
				System.out.println("T Pressed");
				if(writing.equals("string"))
				{
					if(shift ^ capLock)
					{
						stringValue += "T";
					}
					else
					{
						stringValue += "t";
					}
				}
			}
			else if(key == KeyEvent.VK_U)
			{
				System.out.println("U Pressed");
				if(writing.equals("string"))
				{
					if(shift ^ capLock)
					{
						stringValue += "U";
					}
					else
					{
						stringValue += "u";
					}
				}
			}
			else if(key == KeyEvent.VK_V)
			{
				System.out.println("V Pressed");
				if(writing.equals("string"))
				{
					if(shift ^ capLock)
					{
						stringValue += "V";
					}
					else
					{
						stringValue += "v";
					}
				}
			}
			else if(key == KeyEvent.VK_W)
			{
				System.out.println("W Pressed");
				if(setting.equals(""))
				{
					camera.setZa(0.01);
					camera.setZStopping(false);
				}
				else if(writing.equals("string"))
				{
					if(shift ^ capLock)
					{
						stringValue += "W";
					}
					else
					{
						stringValue += "w";
					}
				}
			}
			else if(key == KeyEvent.VK_X)
			{
				System.out.println("X Pressed");
				if(writing.equals("string"))
				{
					if(shift ^ capLock)
					{
						stringValue += "X";
					}
					else
					{
						stringValue += "x";
					}
				}
			}
			else if(key == KeyEvent.VK_Y)
			{
				System.out.println("Y Pressed");
				if(writing.equals("string"))
				{
					if(shift ^ capLock)
					{
						stringValue += "Y";
					}
					else
					{
						stringValue += "y";
					}
				}
			}
			else if(key == KeyEvent.VK_Z)
			{
				System.out.println("Z Pressed");
				if(writing.equals("string"))
				{
					if(shift ^ capLock)
					{
						stringValue += "Z";
					}
					else
					{
						stringValue += "z";
					}
				}
			}
			else if(key == KeyEvent.VK_0)
			{
				System.out.println("0 Pressed");
				if(writing.equals("int"))
				{
					value *= 10;
				}
				else if(writing.equals("string"))
				{
					if(shift)
					{
						stringValue += ")";
					}
					else
					{
						stringValue += "0";
					}
				}
			}
			else if(key == KeyEvent.VK_1)
			{
				System.out.println("1 Pressed");
				if(writing.equals("int"))
				{
					value *= 10;
					value++;
				}
				else if(writing.equals("string"))
				{
					if(shift)
					{
						stringValue += "!";
					}
					else
					{
						stringValue += "1";
					}
				}
			}
			else if(key == KeyEvent.VK_2)
			{
				System.out.println("2 Pressed");
				if(writing.equals("int"))
				{
					value *= 10;
					value += 2;
				}
				else if(writing.equals("string"))
				{
					if(shift)
					{
						stringValue += "@";
					}
					else
					{
						stringValue += "2";
					}
				}
			}
			else if(key == KeyEvent.VK_3)
			{
				System.out.println("3 Pressed");
				if(writing.equals("int"))
				{
					value *= 10;
					value += 3;
				}
				else if(writing.equals("string"))
				{
					if(shift)
					{
						stringValue += "£";
					}
					else
					{
						stringValue += "3";
					}
				}
			}
			else if(key == KeyEvent.VK_4)
			{
				System.out.println("4 Pressed");
				if(writing.equals("int"))
				{
					value *= 10;
					value += 4;
				}
				else if(writing.equals("string"))
				{
					if(shift)
					{
						stringValue += "$";
					}
					else
					{
						stringValue += "4";
					}
				}
			}
			else if(key == KeyEvent.VK_5)
			{
				System.out.println("5 Pressed");
				if(writing.equals("int"))
				{
					value *= 10;
					value += 5;
				}
				else if(writing.equals("string"))
				{
					if(shift)
					{
						stringValue += "%";
					}
					else
					{
						stringValue += "5";
					}
				}
			}
			else if(key == KeyEvent.VK_6)
			{
				System.out.println("6 Pressed");
				if(writing.equals("int"))
				{
					value *= 10;
					value += 6;
				}
				else if(writing.equals("string"))
				{
					if(shift)
					{
						stringValue += "^";
					}
					else
					{
						stringValue += "6";
					}
				}
			}
			else if(key == KeyEvent.VK_7)
			{
				System.out.println("7 Pressed");
				if(writing.equals("int"))
				{
					value *= 10;
					value += 7;
				}
				else if(writing.equals("string"))
				{
					if(shift)
					{
						stringValue += "&";
					}
					else
					{
						stringValue += "7";
					}
				}
			}
			else if(key == KeyEvent.VK_8)
			{
				System.out.println("8 Pressed");
				if(writing.equals("int"))
				{
					value *= 10;
					value += 8;
				}
				else if(writing.equals("string"))
				{
					if(shift)
					{
						stringValue += "*";
					}
					else
					{
						stringValue += "8";
					}
				}
			}
			else if(key == KeyEvent.VK_9)
			{
				System.out.println("9 Pressed");
				if(writing.equals("int"))
				{
					value *= 10;
					value += 9;
				}
				else if(writing.equals("string"))
				{
					if(shift)
					{
						stringValue += "(";
					}
					else
					{
						stringValue += "9";
					}
				}
			}
			else if(key == KeyEvent.VK_SLASH)
			{
				if(writing.equals("string"))
				{
					if(shift)
					{
						stringValue += "?";
					}
					else
					{
						stringValue += "/";
					}
				}
			}
			else if(key == KeyEvent.VK_PERIOD)
			{
				if(writing.equals("string"))
				{
					if(shift)
					{
						stringValue += ">";
					}
					else
					{
						stringValue += ".";
					}
				}
			}
			else if(key == KeyEvent.VK_BACK_SPACE)
			{
				System.out.println("Back Space Pressed");
				if(writing.equals("int"))
				{
					value /= 10;
				}
				else if(writing.equals("string"))
				{
					if(stringValue.length() > 0)
					{
						stringValue = String.copyValueOf(stringValue.toCharArray(), 0, stringValue.length() - 1);
					}
				}
			}
			else if(key == KeyEvent.VK_ALT)
			{
				alt = true;
			}
			else if(key == KeyEvent.VK_SHIFT)
			{
				shift = true;
			}
			else if(key == KeyEvent.VK_META)
			{
				command = true;
			}
			else if(key == KeyEvent.VK_CAPS_LOCK)
			{
				System.out.println("Caps Lock Pressed");
				capLock = true;
			}
			else if(key == KeyEvent.VK_ESCAPE)
			{
				if(!writing.equals(""))
				{
					setTextBoxInvisible(0);
					setTextBoxInvisible(1);
				}
				if(!setting.equals(""))
				{
					setting = "";
					writing = "";
				}
			}
			else if(key == KeyEvent.VK_ENTER)
			{
				System.out.println("Enter Pressed");
				if(setting.equals("save"))
				{
					File file = new File(stringValue);
					System.out.println("Saving");
					try {
						ImageIO.write(BufferedSprite.mergeLayers(layerList.get(frame)), "png", file);
						file.createNewFile();
						System.out.println("Saved to file: " + file.getAbsolutePath());
					} catch (IOException e1) {
						System.out.println("Failed to Save");
						e1.printStackTrace();
					}
					writing = "";
					setting = "";
					setTextBoxInvisible(0);
					setTextBoxInvisible(1);
				}
			}
			if(writing.equals("string"))
			{
				setTextBoxText(stringValue, 1);
			}
			else if(writing.equals("int"))
			{
				setTextBoxText(String.valueOf(value), 1);
			}
		}

		@Override
		public void keyReleased(KeyEvent e) {
			int key = e.getKeyCode();
			if(key == KeyEvent.VK_ALT)
			{
				alt = false;
			}
			else if(key == KeyEvent.VK_SHIFT)
			{
				shift = false;
			}
			else if(key == KeyEvent.VK_CAPS_LOCK)
			{
				System.out.println("Caps Lock Released");
				capLock = false;
			}
			else if(key == KeyEvent.VK_META)
			{
				command = false;
			}
			else if(key == KeyEvent.VK_DOWN)
			{
				System.out.println("Down Released");
				camera.setYd(4.0);
				camera.setYs(0.0);
				camera.setYStopping(true);
			}
			else if(key == KeyEvent.VK_UP)
			{
				System.out.println("Up Released");
				camera.setYd(4.0);
				camera.setYs(0.0);
				camera.setYStopping(true);
			}
			else if(key == KeyEvent.VK_LEFT)
			{
				System.out.println("Left Released");
				camera.setXd(4.0);
				camera.setXs(0.0);
				camera.setXStopping(true);
			}
			else if(key == KeyEvent.VK_RIGHT)
			{
				System.out.println("Right Released");
				camera.setXd(4.0);
				camera.setXs(0.0);
				camera.setXStopping(true);
			}
			else if(key == KeyEvent.VK_W)
			{
				System.out.println("W Released");
				if(setting.equals(""))
				{
					camera.setZd(0.01);
					camera.setZs(0.0);
					camera.setZStopping(true);
				}
			}
			else if(key == KeyEvent.VK_S)
			{
				System.out.println("S Released");
				if(setting.equals(""))
				{
					camera.setZd(0.01);
					camera.setZs(0.0);
					camera.setZStopping(true);
				}
			}
		}
	}
	
	private class MouseAdapter implements MouseListener, MouseMotionListener, MouseWheelListener
	{
		public MouseAdapter()
		{
			
		}
		
		@Override
		public void mouseWheelMoved(MouseWheelEvent e) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void mouseDragged(MouseEvent e) {
			if(!alt)
			{
				boolean draw = true;
				Point p = e.getPoint();
				if(currentSlider >= 0)
				{
					draw = false;
					this.updateValues(sliderList.get(currentSlider).convertCheckedYIntoSlider(zeroCamera, (int) p.getY()));
					updateSliders();
					updatePatches();
				}
				if(draw)
				{
					double gridX = ((p.getX() / camera.getZoom()) + camera.getX());
					double gridY = ((p.getY() / camera.getZoom()) + camera.getY());
					//System.out.println("GridLocation: " + gridX + "," + gridY);
					if(gridX >= 0 && gridX < layerList.get(frame).get(layer).getImgWidth() && gridY >= 0 && gridY < layerList.get(frame).get(layer).getImgHeight())
					{
						if(e.getButton() == MouseEvent.BUTTON1)
						{
							for(int i = 0; i < brush.getPixels().size(); i++)
							{
								layerList.get(frame).get(layer).setCheckedRGB((int) (gridX + brush.getPixels().get(i)[0]), (int) (gridY + brush.getPixels().get(i)[1]) ,brush.getColour1().getColour());
							}
						}
						if(e.getButton() == MouseEvent.BUTTON2)
						{
							for(int i = 0; i < brush.getPixels().size(); i++)
							{
								layerList.get(frame).get(layer).setCheckedRGB((int) (gridX + brush.getPixels().get(i)[0]), (int) (gridY + brush.getPixels().get(i)[1]) ,brush.getColour2().getColour());
							}
						}
					}
				}
			}
			else if(alt)
			{
				Point p = e.getPoint();
				int gridX = (int) ((p.getX() / camera.getZoom()) + camera.getX());
				int gridY = (int) ((p.getY() / camera.getZoom()) + camera.getY());
				//System.out.println("GridLocation: " + gridX + "," + gridY);
				if(e.getButton() == MouseEvent.BUTTON1)
				{
					brush.setColour1(layerList.get(frame).get(layer).getRGB(gridX, gridY));
					updateSliders();
					updatePatches();
				}
				else if(e.getButton() == MouseEvent.BUTTON2)
				{
					brush.setColour2(layerList.get(frame).get(layer).getRGB(gridX, gridY));
					updateSliders();
					updatePatches();
				}
			}
		}

		@Override
		public void mouseMoved(MouseEvent e) {
			// TODO Auto-generated method stub
			
		}
		
		public void setValues()
		{
			if(colourMode == 0)
			{
				if(currentColour == 0)
				{
					sliderList.get(0).setSliderValue(brush.getColour1().getR());
					sliderList.get(1).setSliderValue(brush.getColour1().getG());
					sliderList.get(2).setSliderValue(brush.getColour1().getB());
					sliderList.get(3).setSliderValue(brush.getColour1().getA());
				}
				else if(currentColour == 1)
				{
					sliderList.get(0).setSliderValue(brush.getColour2().getR());
					sliderList.get(1).setSliderValue(brush.getColour2().getG());
					sliderList.get(2).setSliderValue(brush.getColour2().getB());
					sliderList.get(3).setSliderValue(brush.getColour2().getA());
				}
			}
			else if(colourMode == 1)
			{
				if(currentColour == 0)
				{
					if(currentSlider == 0)
					{

					}
					else if (currentSlider == 1)
					{

					}
					else if (currentSlider == 2)
					{

					}
					else if (currentSlider == 3)
					{

					}
				}
				else if(currentColour == 1)
				{
					if(currentSlider == 0)
					{

					}
					else if (currentSlider == 1)
					{

					}
					else if (currentSlider == 2)
					{

					}
					else if (currentSlider == 3)
					{

					}
				}
			}
		}
		
		public void updateValues(int sliderValue)
		{
			sliderList.get(currentSlider).setSliderValue(sliderValue);
			if(colourMode == 0)
			{
				if(currentColour == 0)
				{
					if(currentSlider == 0)
					{
						brush.getColour1().setR(sliderValue);
					}
					else if (currentSlider == 1)
					{
						brush.getColour1().setG(sliderValue);
					}
					else if (currentSlider == 2)
					{
						brush.getColour1().setB(sliderValue);
					}
					else if (currentSlider == 3)
					{
						brush.getColour1().setA(sliderValue);
					}
				}
				else if(currentColour == 1)
				{
					if(currentSlider == 0)
					{
						brush.getColour2().setR(sliderValue);
					}
					else if (currentSlider == 1)
					{
						brush.getColour2().setG(sliderValue);
					}
					else if (currentSlider == 2)
					{
						brush.getColour2().setB(sliderValue);
					}
					else if (currentSlider == 3)
					{
						brush.getColour2().setA(sliderValue);
					}
				}
			}
			else if(colourMode == 1)
			{
				if(currentColour == 0)
				{
					if(currentSlider == 0)
					{

					}
					else if (currentSlider == 1)
					{

					}
					else if (currentSlider == 2)
					{

					}
					else if (currentSlider == 3)
					{

					}
				}
				else if(currentColour == 1)
				{
					if(currentSlider == 0)
					{

					}
					else if (currentSlider == 1)
					{

					}
					else if (currentSlider == 2)
					{

					}
					else if (currentSlider == 3)
					{

					}
				}
			}
		}

		@Override
		public void mouseClicked(MouseEvent e) {
//			if(!alt)
//			{
//				boolean draw = true;
//				Point p = e.getPoint();
//				for(int i = 0; i < sliderList.size(); i++)
//				{
//					System.out.println("In???: " + sliderList.get(i).checkIfIn(zeroCamera, p, 0, 0));
//					if(sliderList.get(i).checkIfIn(zeroCamera, p, 0, 0))
//					{
//						currentSlider = i;
//						this.updateValues(sliderList.get(currentSlider).convertYIntoSlider(zeroCamera, (int) p.getY()));
//						currentSlider = -1;
//						draw = false;
//						break;
//					}
//				}
//				for(int i = 0; i < colourPatchList.size(); i++)
//				{
//					if(colourPatchList.get(i).checkIfIn(zeroCamera, p, 0, 0))
//					{
//						currentColour = i;
//						updateSliders();
//						for(int j = 0; j < sliderList.size(); j++)
//						{
//							sliderList.get(j).setVisible();
//						}
//						System.out.println("ColourPatching");
//						draw = false;
//						break;
//					}
//				}
//				if(draw)
//				{
//					if(currentColour != -1)
//					{
//						for(int j = 0; j < sliderList.size(); j++)
//						{
//							sliderList.get(j).setInvisible();
//						}
//						currentColour = -1;
//					}
//					double gridX = ((p.getX() / camera.getZoom()) + camera.getX());
//					double gridY = ((p.getY() / camera.getZoom()) + camera.getY());
//					//System.out.println("GridLocation: " + gridX + "," + gridY);
//					if(gridX >= 0 && gridX < layerList.get(layer).getImgWidth() && gridY >= 0 && gridY < layerList.get(layer).getImgHeight())
//					{
//						if(e.getButton() == MouseEvent.BUTTON1)
//						{
//							for(int i = 0; i < brush.getPixels().size(); i++)
//							{
//								layerList.get(layer).setCheckedRGB((int) (gridX + brush.getPixels().get(i)[0]), (int) (gridY + brush.getPixels().get(i)[1]) ,brush.getColour1().getColour());
//							}
//						}
//						if(e.getButton() == MouseEvent.BUTTON2)
//						{
//							for(int i = 0; i < brush.getPixels().size(); i++)
//							{
//								layerList.get(layer).setCheckedRGB((int) (gridX + brush.getPixels().get(i)[0]), (int) (gridY + brush.getPixels().get(i)[1]) ,brush.getColour2().getColour());
//							}
//						}
//					}
//				}
//			}
//			else if(alt)
//			{
//				Point p = e.getPoint();
//				int gridX = (int) ((p.getX() / camera.getZoom()) + camera.getX());
//				int gridY = (int) ((p.getY() / camera.getZoom()) + camera.getY());
//				//System.out.println("GridLocation: " + gridX + "," + gridY);
//				if(e.getButton() == MouseEvent.BUTTON1)
//				{
//					brush.setColour1(layerList.get(layer).getRGB(gridX, gridY));
//				}
//				else if(e.getButton() == MouseEvent.BUTTON2)
//				{
//					brush.setColour2(layerList.get(layer).getRGB(gridX, gridY));
//				}
//			}
		}

		@Override
		public void mousePressed(MouseEvent e) {
			if(!alt)
			{
				boolean draw = true;
				Point p = e.getPoint();
				for(int i = 0; i < sliderList.size(); i++)
				{
					System.out.println("In???: " + sliderList.get(i).checkIfIn(zeroCamera, p, 0, 0));
					if(sliderList.get(i).checkIfIn(zeroCamera, p, 0, 0))
					{
						System.out.println("StartCurrentSliding");
						currentSlider = i;
						this.updateValues(sliderList.get(currentSlider).convertCheckedYIntoSlider(zeroCamera, (int) p.getY()));
						updateSliders();
						updatePatches();
						draw = false;
						break;
					}
				}
				for(int i = 0; i < colourPatchList.size(); i++)
				{
					if(colourPatchList.get(i).checkIfIn(zeroCamera, p, 0, 0))
					{
						currentColour = i;
						this.setValues();
						updateSliders();
						for(int j = 0; j < sliderList.size(); j++)
						{
							sliderList.get(j).setVisible();
						}
						System.out.println("ColourPatching");
						draw = false;
						break;
					}
				}
				if(draw)
				{
					if(currentColour != -1)
					{
						for(int j = 0; j < sliderList.size(); j++)
						{
							sliderList.get(j).setInvisible();
						}
						currentColour = -1;
					}
					double gridX = ((p.getX() / camera.getZoom()) + camera.getX());
					double gridY = ((p.getY() / camera.getZoom()) + camera.getY());
					//System.out.println("GridLocation: " + gridX + "," + gridY);
					if(gridX >= 0 && gridX < layerList.get(frame).get(layer).getImgWidth() && gridY >= 0 && gridY < layerList.get(frame).get(layer).getImgHeight())
					{
						if(e.getButton() == MouseEvent.BUTTON1)
						{
							for(int i = 0; i < brush.getPixels().size(); i++)
							{
								layerList.get(frame).get(layer).setCheckedRGB((int) (gridX + brush.getPixels().get(i)[0]), (int) (gridY + brush.getPixels().get(i)[1]) ,brush.getColour1().getColour());
							}
						}
						if(e.getButton() == MouseEvent.BUTTON2)
						{
							for(int i = 0; i < brush.getPixels().size(); i++)
							{
								layerList.get(frame).get(layer).setCheckedRGB((int) (gridX + brush.getPixels().get(i)[0]), (int) (gridY + brush.getPixels().get(i)[1]) ,brush.getColour2().getColour());
							}
						}
					}
				}
			}
			else if(alt)
			{
				Point p = e.getPoint();
				int gridX = (int) ((p.getX() / camera.getZoom()) + camera.getX());
				int gridY = (int) ((p.getY() / camera.getZoom()) + camera.getY());
				//System.out.println("GridLocation: " + gridX + "," + gridY);
				if(e.getButton() == MouseEvent.BUTTON1)
				{
					brush.setColour1(layerList.get(frame).get(layer).getRGB(gridX, gridY));
					updateSliders();
					updatePatches();
				}
				else if(e.getButton() == MouseEvent.BUTTON2)
				{
					brush.setColour2(layerList.get(frame).get(layer).getRGB(gridX, gridY));
					updateSliders();
					updatePatches();
				}
			}
		}

		@Override
		public void mouseReleased(MouseEvent e) {
			currentSlider = -1;
		}

		@Override
		public void mouseEntered(MouseEvent e) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void mouseExited(MouseEvent e) {
			// TODO Auto-generated method stub
			
		}
	}
	
	private class PanelListener implements ComponentListener
	{
		public PanelListener()
		{
			
		}
		
		@Override
		public void componentResized(ComponentEvent e) {
			updateTextBoxSizes();
		}

		@Override
		public void componentMoved(ComponentEvent e) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void componentShown(ComponentEvent e) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void componentHidden(ComponentEvent e) {
			// TODO Auto-generated method stub
			
		}
		
	}
}
