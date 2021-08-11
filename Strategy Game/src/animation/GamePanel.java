package animation;

import java.awt.Graphics;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.util.ArrayList;

import javax.swing.JPanel;

import animation.layer.ButtonLayer;
import animation.layer.GridButtonLayer;
import animation.layer.GridLayer;
import animation.layer.Layer;
import animation.layer.TextBox;
import animation.palette.Palette;
import animation.sprite.DynamicTile;
import animation.sprite.Font;
import attack.Attack;
import game.Game;
import interaction.GameInterface;
import unit.Unit;
import utilities.Location;

public class GamePanel extends JPanel implements Runnable{
	
	//For the tiles and buildings
	private Camera camera;
	private GridLayer tileLayer;
	private GridButtonLayer tileButtonLayer;
	private GridLayer buildingLayer;
	private GridLayer unitLayer;
	private Layer animationLayer;
	private Layer informationLayer = new Layer();
	private Layer informationTextLayer = new Layer();
	private Layer menuLayer;
	private ButtonLayer menuButtonLayer;
	private Layer menuTextLayer;
	private Thread thread;
	private int frameRate;
	private int delay;
	private boolean running;
	//private int[] keyBindings;
	//private int inputNumber;
	private Game game;
	private GameInterface gameInterface;
	private int animationCounter;
	private int animationTimeDelay = 4;
	private int menuWidth = 8;
	private double menuScale = 2.0;
	private Font menuFont = new Font("standard");
	private DynamicTile menuTile = new DynamicTile("tiles", "menu", new String[] {"", "over"}, 0, 0, 0);
	private int[] informationBoxDimensions = new int[] {8, 4, 8, 11, 8};
	private double informationBoxScale = 2.0;
	private int tileSize = 128;
	private int menuUpdateDelay = 0;
	private int menuUpdateCounter = -4;
	private int lastCommand = -1;
	private int lastCommandNumber;
	private ArrayList<String> lastMenuOption;
	private Palette[] paletteList;
	private Camera zeroCamera = new Camera(0, 0, 1.0);
	// 0: 0.0 = actual, 1.0 = %
	private double[] settingList = new double[] {0.0};
	private Point mousePoint;
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public GamePanel()
	{
		setFocusable(true);
	}
	
	public Camera getCamera()
	{
		return camera;
	}
	
	public GridLayer getTileLayer()
	{
		return tileLayer;
	}
	
	public GridButtonLayer getTileButtonLayer()
	{
		return tileButtonLayer;
	}
	
	public GridLayer getBuildingLayer()
	{
		return buildingLayer;
	}
	
	public GridLayer getUnitLayer()
	{
		return unitLayer;
	}
	
	public Layer getAnimationLayer()
	{
		return animationLayer;
	}
	
	public Layer getMenuLayer()
	{
		return menuLayer;
	}
	
	public ButtonLayer getMenuButtonLayer()
	{
		return menuButtonLayer;
	}
	
	@Deprecated
	public GamePanel(Camera camera, GridLayer tileLayer, GridLayer unitLayer, Layer animationLayer, Layer menuLayer, int frameRate)
	{
		setFocusable(true);
		addKeyListener(new GameAdapter(this));
		MouseAdapter mouseAdapter = new MouseAdapter(this);
		addMouseListener(mouseAdapter);
		addMouseMotionListener(mouseAdapter);
		addMouseWheelListener(mouseAdapter);
		setFocusable(true);
		this.camera = camera;
		this.tileLayer = tileLayer;
		this.unitLayer = unitLayer;
		this.animationLayer = animationLayer;
		this.menuLayer = menuLayer;
		this.frameRate = frameRate;
		startThread();
	}
	
	@Deprecated
	public GamePanel(Camera camera, GridLayer tileLayer, GridButtonLayer tileButtonLayer, GridLayer buildingLayer, GridLayer unitLayer, Layer animationLayer, Layer menuLayer, ButtonLayer menuButtonLayer, int frameRate)
	{
		setFocusable(true);
		addKeyListener(new GameAdapter(this));
		addMouseListener(new MouseAdapter(this));
		setFocusable(true);
		this.camera = camera;
		this.tileLayer = tileLayer;
		this.tileButtonLayer = tileButtonLayer;
		this.buildingLayer = buildingLayer;
		this.unitLayer = unitLayer;
		this.animationLayer = animationLayer;
		this.menuLayer = menuLayer;
		this.menuButtonLayer = menuButtonLayer;
		this.menuTextLayer = new Layer();
		this.frameRate = frameRate;
		//inputNumber = 0;
		startThread();
	}
	
	@Deprecated
	public GamePanel(Camera camera, GridLayer tileLayer, GridButtonLayer tileButtonLayer, GridLayer buildingLayer, GridLayer unitLayer, Layer animationLayer, Layer menuLayer, ButtonLayer menuButtonLayer, int frameRate, GameInterface gameInterface, int animationTimeDelay)
	{
		setFocusable(true);
		addKeyListener(new GameAdapter(this));
		MouseAdapter mouseAdapter = new MouseAdapter(this);
		addMouseListener(mouseAdapter);
		addMouseMotionListener(mouseAdapter);
		addMouseWheelListener(mouseAdapter);
		setFocusable(true);
		this.camera = camera;
		this.tileLayer = tileLayer;
		this.tileButtonLayer = tileButtonLayer;
		this.buildingLayer = buildingLayer;
		this.unitLayer = unitLayer;
		this.animationLayer = animationLayer;
		this.menuLayer = menuLayer;
		this.menuButtonLayer = menuButtonLayer;
		this.menuTextLayer = new Layer();
		this.frameRate = frameRate;
		this.gameInterface = gameInterface;
		this.game = gameInterface.getGame();
		//inputNumber = 0;
		this.animationTimeDelay = animationTimeDelay;
		animationCounter = animationTimeDelay;
		setFocusable(true);
		startThread();
	}
	
	public GamePanel(Camera camera, GridLayer tileLayer, GridButtonLayer tileButtonLayer, GridLayer buildingLayer, GridLayer unitLayer, Layer animationLayer, Layer menuLayer, ButtonLayer menuButtonLayer, int frameRate, GameInterface gameInterface, int animationTimeDelay, Palette[] paletteList)
	{
		setFocusable(true);
		addKeyListener(new GameAdapter(this));
		MouseAdapter mouseAdapter = new MouseAdapter(this);
		addMouseListener(mouseAdapter);
		addMouseMotionListener(mouseAdapter);
		addMouseWheelListener(mouseAdapter);
		addComponentListener(new PanelListener());
		setFocusable(true);
		this.camera = camera;
		this.tileLayer = tileLayer;
		this.tileButtonLayer = tileButtonLayer;
		this.buildingLayer = buildingLayer;
		this.unitLayer = unitLayer;
		this.animationLayer = animationLayer;
		this.menuLayer = menuLayer;
		this.menuButtonLayer = menuButtonLayer;
		this.menuTextLayer = new Layer();
		this.frameRate = frameRate;
		this.gameInterface = gameInterface;
		this.game = gameInterface.getGame();
		//inputNumber = 0;
		this.animationTimeDelay = animationTimeDelay;
		animationCounter = animationTimeDelay;
		this.paletteList = paletteList;
		setFocusable(true);
		makeInformationBoxes();
		startThread();
	}
	
	@Override
	public void run() {
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
	
	public void makeInformationBoxes()
	{
		informationLayer.addSprite(new GridLayer(menuTile, informationBoxDimensions[0], informationBoxDimensions[1], menuTile.getHeight(), 
				informationBoxScale, true, 0, (int) (this.getHeight() - (informationBoxScale * menuTile.getHeight() * informationBoxDimensions[1])), false));
		informationTextLayer.addSprite(new TextBox((int) (menuTile.getWidth() * informationBoxScale / 2), 
				(int) (this.getHeight() - (informationBoxScale * menuTile.getHeight() * informationBoxDimensions[1] - menuTile.getHeight() / 2)), 
				(int) (menuTile.getHeight() * informationBoxScale * (informationBoxDimensions[0] - 1)), 
				(int) (menuTile.getHeight() * informationBoxScale * (informationBoxDimensions[1] - 0.5)), 1.0, true, "", menuFont, informationBoxScale));
		informationLayer.addSprite(new GridLayer(menuTile, informationBoxDimensions[0], informationBoxDimensions[1], menuTile.getHeight(), informationBoxScale, true, 
				(int) (informationBoxScale * menuTile.getHeight() * informationBoxDimensions[0]), 
				(int) (this.getHeight() - (informationBoxScale * menuTile.getHeight() * informationBoxDimensions[1])), false));
		informationTextLayer.addSprite(new TextBox((int) ((informationBoxScale * menuTile.getWidth() * informationBoxDimensions[0]) + menuTile.getWidth() * 
				informationBoxScale / 2), (int) (this.getHeight() - (informationBoxScale * menuTile.getHeight() * informationBoxDimensions[1] - menuTile.getHeight() / 2)),
				(int) (menuTile.getHeight() * informationBoxScale * (informationBoxDimensions[0] - 1)), (int) (menuTile.getHeight() * informationBoxScale * 
				(informationBoxDimensions[1] - 0.5)), 1.0, true, "", menuFont, informationBoxScale));
		informationLayer.addSprite(new GridLayer(menuTile, informationBoxDimensions[2], informationBoxDimensions[1], menuTile.getHeight(), informationBoxScale, true, 
				(int) (informationBoxScale * menuTile.getHeight() * informationBoxDimensions[0] * 2), 
				(int) (this.getHeight() - (informationBoxScale * menuTile.getHeight() * informationBoxDimensions[1])), false));
		informationTextLayer.addSprite(new TextBox((int) ((informationBoxScale * menuTile.getWidth() * informationBoxDimensions[0] * 2) + menuTile.getWidth() * 
				informationBoxScale / 2), (int) (this.getHeight() - (informationBoxScale * menuTile.getHeight() * informationBoxDimensions[1] - menuTile.getHeight() / 2)), 
				(int) (menuTile.getHeight() * informationBoxScale * (informationBoxDimensions[2] - 1)), (int) (menuTile.getHeight() * informationBoxScale * 
				(informationBoxDimensions[1] - 0.5)), 1.0, true, "", menuFont, informationBoxScale));
		informationLayer.addSprite(new GridLayer(menuTile, informationBoxDimensions[3], informationBoxDimensions[4], menuTile.getHeight(), informationBoxScale, true, 
				(int) (informationBoxScale * menuTile.getHeight() * (informationBoxDimensions[0] * 2 + informationBoxDimensions[2])), 
				(int) (this.getHeight() - (informationBoxScale * menuTile.getHeight() * informationBoxDimensions[4])), false));
		informationTextLayer.addSprite(new TextBox((int) ((informationBoxScale * menuTile.getWidth() * (informationBoxDimensions[0] * 2 + informationBoxDimensions[2]))
				+ menuTile.getWidth() * 
				informationBoxScale / 2), (int) (this.getHeight() - (informationBoxScale * menuTile.getHeight() * informationBoxDimensions[4] - menuTile.getHeight() / 2)), 
				(int) (menuTile.getWidth() * informationBoxScale * (informationBoxDimensions[3] - 1)), (int) (menuTile.getHeight() * informationBoxScale * 
				(informationBoxDimensions[4] - 0.5)), 1.0, true, "", menuFont, informationBoxScale));
		informationLayer.setSpriteInvisible();
	}
	
	public void setInformationBoxLocation()
	{
		informationLayer.setSpriteLocation(0, 0, (int) (this.getHeight() - (informationBoxScale * menuTile.getHeight() * informationBoxDimensions[1])));
		informationTextLayer.setSpriteLocation(0, (int) (menuTile.getWidth() * informationBoxScale / 2), 
				(int) (this.getHeight() - (informationBoxScale * menuTile.getHeight() * informationBoxDimensions[1] - menuTile.getHeight() / 2)));
		informationLayer.setSpriteLocation(1, (int) (informationBoxScale * menuTile.getHeight() * informationBoxDimensions[0]), 
				(int) (this.getHeight() - (informationBoxScale * menuTile.getHeight() * informationBoxDimensions[1])));
		informationTextLayer.setSpriteLocation(1,  
				(int) (informationBoxScale * menuTile.getHeight() * informationBoxDimensions[0] + (menuTile.getWidth() * informationBoxScale / 2)), 
				(int) (this.getHeight() - (informationBoxScale * menuTile.getHeight() * informationBoxDimensions[1] - menuTile.getHeight() / 2)));
		informationLayer.setSpriteLocation(2, 
				(int) (informationBoxScale * menuTile.getHeight() * informationBoxDimensions[0] * 2), 
				(int) (this.getHeight() - (informationBoxScale * menuTile.getHeight() * informationBoxDimensions[1])));
		informationTextLayer.setSpriteLocation(2,  
				(int) (informationBoxScale * menuTile.getHeight() * informationBoxDimensions[0] * 2 + (menuTile.getWidth() * informationBoxScale / 2)), 
				(int) (this.getHeight() - (informationBoxScale * menuTile.getHeight() * informationBoxDimensions[1] - menuTile.getHeight() / 2)));
		informationLayer.setSpriteLocation(3, 
				(int) (informationBoxScale * menuTile.getHeight() * (informationBoxDimensions[0] * 2 + informationBoxDimensions[2])), 
				(int) (this.getHeight() - (informationBoxScale * menuTile.getHeight() * informationBoxDimensions[4])));
		informationTextLayer.setSpriteLocation(3,  
				(int) (informationBoxScale * menuTile.getHeight() * (informationBoxDimensions[0] * 2 + informationBoxDimensions[2]) + 
						(menuTile.getWidth() * informationBoxScale / 2)), 
				(int) (this.getHeight() - (informationBoxScale * menuTile.getHeight() * informationBoxDimensions[4] - menuTile.getHeight() / 2)));
	}
	
	public void clearTileInformationBoxInfo()
	{
		if(informationTextLayer.getSpriteList().get(2) == null)
		{
			System.out.println("Null?");
		}
		else if(informationTextLayer.getSpriteList().get(2) instanceof TextBox)
		{
			informationTextLayer.getSpriteList().get(2).getTextBox().setDisplay("", informationBoxScale);
			informationLayer.getSpriteList().get(2).setInvisible();
		}
		else
		{
			System.out.println("Not a text Box?");
		}
	}
	
	public void clearBuildingInformationBoxInfo()
	{
		if(informationTextLayer.getSpriteList().get(1) == null)
		{
			System.out.println("Null?");
		}
		else if(informationTextLayer.getSpriteList().get(1) instanceof TextBox)
		{
			informationTextLayer.getSpriteList().get(1).getTextBox().setDisplay("", informationBoxScale);
			informationLayer.getSpriteList().get(1).setInvisible();
		}
		else
		{
			System.out.println("Not a text Box?");
		}
	}
	
	public void clearUnitInformationBoxInfo()
	{
		if(informationTextLayer.getSpriteList().get(0) == null)
		{
			System.out.println("Null?");
		}
		else if(informationTextLayer.getSpriteList().get(0) instanceof TextBox)
		{
			informationTextLayer.getSpriteList().get(0).getTextBox().setDisplay("", informationBoxScale);
			informationLayer.getSpriteList().get(0).setInvisible();
		}
		else
		{
			System.out.println("Not a text Box?");
		}
	}
	
	public void clearMoveInformationBoxInfo()
	{
		if(informationTextLayer.getSpriteList().get(3) == null)
		{
			System.out.println("Null?");
		}
		else if(informationTextLayer.getSpriteList().get(3) instanceof TextBox)
		{
			informationTextLayer.getSpriteList().get(3).getTextBox().setDisplay("", informationBoxScale);
			informationLayer.getSpriteList().get(3).setInvisible();
		}
		else
		{
			System.out.println("Not a text Box?");
		}
	}
	
	public void setMoveInformationBoxInfo(String text)
	{
		informationTextLayer.getSpriteList().get(3).getTextBox().setDisplay(text, informationBoxScale);
		informationLayer.getSpriteList().get(3).setVisible();
	}
	
	public void setTileInformationBoxInfo()
	{
		if(gameInterface.getSelectedTile() != null)
		{
			String tileInfo = gameInterface.getSelectedTile().getTileName() + "\nTerrain Difficulty: " + (int) (gameInterface.getSelectedTile().getMovementCost());
			informationTextLayer.getSpriteList().get(2).getTextBox().setDisplay(tileInfo, informationBoxScale);
			informationLayer.getSpriteList().get(2).setVisible();
		}
		else
		{
			clearBuildingInformationBoxInfo();
		}
	}
	
	public void setBuildingInformationBoxInfo()
	{
		if(gameInterface.getSelectedBuilding() != null)
		{
			String unitInfo;
			if(settingList[0] == 0.0)
			{
				unitInfo = gameInterface.getSelectedBuilding().getName() + "\nHP: " + ((int) gameInterface.getSelectedBuilding().getHealthPoint()) + "/"
						+ ((int) gameInterface.getSelectedBuilding().getMaxHealthPoint());
			}
			else
			{
				unitInfo = gameInterface.getSelectedBuilding().getName() + "\nHP: " + 
						(int) (gameInterface.getSelectedBuilding().getHealthPoint() / gameInterface.getSelectedBuilding().getMaxHealthPoint() * 100) + "%";
			}
			informationTextLayer.getSpriteList().get(1).getTextBox().setDisplay(unitInfo, informationBoxScale);
			informationLayer.getSpriteList().get(1).setVisible();
		}
		else
		{
			clearBuildingInformationBoxInfo();
		}
	}
	
	public void setUnitInformationBoxInfo()
	{
		if(gameInterface.getSelectedUnit() != null)
		{
			String unitInfo;
			if(settingList[0] == 0.0)
			{
				unitInfo = gameInterface.getSelectedUnit().getName() + "\nHP: " + ((int) gameInterface.getSelectedUnit().getHealthPoint()) + "/"
						+ ((int) gameInterface.getSelectedUnit().getMaxHealthPoint()) + "\nStamina: " + ((int) gameInterface.getSelectedUnit().getStamina())
						+ "/"+ ((int) gameInterface.getSelectedUnit().getMaxStamina());
			}
			else
			{
				unitInfo = gameInterface.getSelectedUnit().getName() + "\nHP: " + (int) (gameInterface.getSelectedUnit().getHealthPoint() /
						gameInterface.getSelectedUnit().getMaxHealthPoint() * 100) + "%\nStamina: " + (int) (gameInterface.getSelectedUnit().getStamina()
						/ gameInterface.getSelectedUnit().getMaxStamina() * 100) + "%";
			}
			if(gameInterface.getSelectedUnit().isSpecial())
			{
				unitInfo += "\nSP: " + ((int) gameInterface.getSelectedUnit().getSpecialUnit().getSpecialPoint()) + "/"+ 
			((int) gameInterface.getSelectedUnit().getSpecialUnit().getMaxSpecialPoint());
			}
			informationTextLayer.getSpriteList().get(0).getTextBox().setDisplay(unitInfo, informationBoxScale);
			informationLayer.getSpriteList().get(0).setVisible();
		}
		else
		{
			clearUnitInformationBoxInfo();
		}
	}
	
	public void makeMenu(ArrayList<String> menuOptions, int gridX, int gridY)
	{
		clearMenu();
		if(menuOptions.size() > 0)
		{
			updateMenuLocation();
			int x = 0;
			int y = 0;
			menuLayer.addSprite(new GridLayer(menuTile, menuWidth, menuOptions.size() + 1, menuTile.getWidth(), menuScale, true, x, y));
			x += menuTile.getWidth() / 2 * menuScale;
			y += menuTile.getHeight() / 2 * menuScale;
			for(int i = 0; i < menuOptions.size(); i++)
			{
				System.out.println("Making GridButtonLayer");
				menuButtonLayer.addSprite(new GridButtonLayer(menuTile, 0, menuWidth - 1, 1, menuTile.getWidth(), menuScale, true, x, (int) (y - menuScale)));
				menuTextLayer.addSprite(new TextBox(x + menuTile.getWidth(), y, (menuWidth - 1) * menuTile.getWidth(), (1) * menuTile.getHeight(), menuScale, true, menuOptions.get(i), menuFont, 1.0));
				y += menuTile.getHeight() * menuScale;
				menuButtonLayer.getButtonList().get(i).setOver(mousePoint, zeroCamera, menuButtonLayer.getX(), menuButtonLayer.getY(), menuButtonLayer.getScale());
				if(menuButtonLayer.getButtonList().get(i).getMouseOver())
				{
					menuButtonLayer.getButtonList().get(i).setState("over");
					lastCommandNumber = i;
					lastCommand = i;
					setLastMenuOption(gameInterface.playerMenuChoice());
					this.updateSelectedView(lastCommandNumber, lastMenuOption);
				}
			}
		}
	}
	
	public void updateMenuLocation()
	{
		setMenuLocation((int) ((gameInterface.getMenuLocation()[1] * tileSize - camera.getX()) * camera.getZoom()), (int) ((gameInterface.getMenuLocation()[0] * tileSize - camera.getY()) * camera.getZoom()));
	}
	
	public void setMenuLocation(int x, int y)
	{
		menuLayer.setLocation(x, y);
		menuButtonLayer.setLocation(x, y);
		menuTextLayer.setLocation(x, y);
	}
	
	public void clearMenu()
	{
		menuLayer.clearLayer();
		menuButtonLayer.clearLayer();
		menuTextLayer.clearLayer();
	}
	
	/**
	 * Increments the animation counter and if necessary, the animationTimer of the sprites
	 */
	public void incrementLayers()
	{
		camera.cameraMotion(this.getWidth(), this.getHeight());
		updateMenuLocation();
		animationCounter--;
		if(animationCounter == 0)
		{
			animationCounter = animationTimeDelay;
			tileLayer.incrementTimer(0, 0);
			tileButtonLayer.incrementTimer(0, 0);
			buildingLayer.incrementTimer(0, 0);
			unitLayer.incrementTimer(0, 0);
			animationLayer.incrementTimer(0);
			menuLayer.incrementTimer(0);
			menuButtonLayer.incrementTimer(0);
			menuTextLayer.incrementTimer(0);
			if(menuUpdateCounter < 0 && menuUpdateCounter > -3)
			{
				menuUpdateCounter = -4;
				this.updateSelectedView(lastCommandNumber, lastMenuOption);
			}
			else
			{
				menuUpdateCounter--;	
			}
		}
	}
	
	public void incrementCounter()
	{
		animationCounter--;
		if(animationCounter == 0)
		{
			animationCounter = animationTimeDelay;
			if(menuUpdateCounter < 0)
			{
				menuUpdateCounter = -1;
				this.updateSelectedView(lastCommandNumber, lastMenuOption);
			}
			else
			{
				menuUpdateCounter--;	
			}
		}
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
	
	public GamePanel startThreadReturn()
	{
		if(thread == null)
		{
			thread = new Thread(this);
			System.out.println("startingThread");
			thread.start();
			System.out.println("started?Thread");
		}
		return this;
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
	
	public void delayPrintRun(long beforeTime)
	{
		long timeDiff, sleep;
		timeDiff = System.currentTimeMillis() - beforeTime;
		sleep = delay - timeDiff;

		System.out.println("Sleep time: " + sleep);
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
		incrementLayers();
		Toolkit.getDefaultToolkit().sync();
	}
	
	@Override
	public void paintComponent (Graphics g)
	{
		//super.paintComponent(g);
		g.clearRect(0, 0, getWidth(), getHeight());
		tileLayer.drawLayer(g, camera, 0, 0);
		tileButtonLayer.drawLayer(g, camera, 0, 0);
		buildingLayer.drawLayer(g, camera, 0, 0);
		unitLayer.drawLayer(g, camera, 0, 0);
		animationLayer.drawLayer(g, camera, 0);
		informationLayer.drawLayer(g, zeroCamera, 0);
		informationTextLayer.drawLayer(g, zeroCamera, 0);
		menuLayer.drawLayer(g, zeroCamera, 0);
		menuButtonLayer.drawLayer(g, zeroCamera, 0);
		menuTextLayer.drawLayer(g, zeroCamera, 0);
		Toolkit.getDefaultToolkit().sync();
		g.dispose();
	}
	
	public void updateToStage()
	{
		String newStage = gameInterface.getStage();
		if(newStage.equals("notSelected")) 
		{
			updateTileButtonMapToDeselected();
		}
		else if(newStage.equals("move"))
		{
			updateTileButtonMapToMove();
		}
		else if(newStage.equals("moveMenu"))
		{
			updateTileButtonMapToMove();
		}
		else if(newStage.equals("attack"))
		{
			updateTileButtonMapToAttack();
		}
		else if(newStage.equals("attackMenu"))
		{
			updateTileButtonMapToAttack();
		}
		else if(newStage.equals("chooseAttack"))
		{
			updateTileButtonMapToChooseAttack();
		}
		else if(newStage.equals("support"))
		{
			updateTileButtonMapToAttack();
		}
		else if(newStage == "supportMenu")
		{
			updateTileButtonMapToAttack();
		}
		else if(newStage == "chooseSupport")
		{
			updateTileButtonMapToChooseSupport();
		}
		else if(newStage == "tempUnitSelect")
		{
			updateTileButtonMapToTempSelect();
		}
		else if(newStage == "movedUnitSelected")
		{
			updateTileButtonMapToTempSelect();
		}
		else if(newStage.equals("enemyUnitSelected"))
		{
			updateTileButtonMapToTempSelect();
		}
		else if(newStage == "movedUnitSelectedMenu")
		{
			updateTileButtonMapToTempSelect();
		}
		else if(newStage.equals("enemyUnitSelectedMenu"))
		{
			updateTileButtonMapToTempSelect();
		}
		else if(newStage.equals("tileSelected"))
		{
			updateTileButtonMapToDeselected();
		}
		else if(newStage == "chooseAction")
		{
			updateTileButtonMapToChooseAction();
		}
		else if(newStage == "capture")
		{
			updateTileButtonMapToCapture();
		}
		else if(newStage == "captureMenu")
		{
			updateTileButtonMapToCapture();
		}
		else if(newStage.equals("special"))
		{
			if(game.getSelectedSpecial().getSpecialType() == 0)
			{
				updateTileButtonMapToAttack(game.getSelectedSpecial().getAttackList()[0]);
			}
		}
		else if(newStage.equals("movedBuildingSelected"))
		{
			
		}
		else if(newStage.equals("tempBuildingSelected"))
		{
			
		}
		else if(newStage.equals("enemyBuildingSelected"))
		{
			
		}
		else if(newStage.equals("building"))
		{
			
		}
	}
	
	public void updateTileButtonMapToDeselected()
	{
		tileButtonLayer.setLayerState("normal");
	}
	
	public void updateTileButtonMapToNill()
	{
		tileButtonLayer.setLayerState("normal");
		tileButtonLayer.setState(game.getSelectedX(), game.getSelectedY(), "selected");
	}
	
	public void updateTileButtonMapToChooseAttack()
	{
		tileButtonLayer.setLayerState("normal");
		int k;
		int l;
		int m;
		int n;
		int o;
		int range;
		Unit selectedUnit = game.getSelectedUnit();
		for(k = 0; k < selectedUnit.getOffensiveAttackList().length; k++)
		{
			System.out.println(selectedUnit.getOffensiveAttackList()[k].getName());
			for (l = 0; l < game.getTileMap().getMap().length; l++)
			{
				for (m = 0; m < game.getTileMap().getMap()[0].length; m++)
				{
					if(selectedUnit.getOffensiveAttackList()[k].checkValidTarget(l, m) && game.getVisionRangeMap(l, m) > 0)
					{
						if(game.validTarget(new int[] {l, m} , selectedUnit.getOffensiveAttackList()[k]))
						{
							tileButtonLayer.setState(m, l, "validtarget");
						}
						else if(!tileButtonLayer.getState(m, l).equals("validtarget"))
						{
							tileButtonLayer.setState(m, l, "attack");
						}
						range = selectedUnit.getOffensiveAttackList()[k].getDefactoAreaOfEffect();
						for (n = 0; n < range + 1; n++)
						{
							for (o = 0; o < range + 1; o++)
							{
								if(game.getTileMap().inMap(n - range + o + l, n - o + m))
								{
									if(tileButtonLayer.getState(n - o + m, n - range + o + l).equals("normal"))
									{
										tileButtonLayer.setState(n - o + m, n - range + o + l, "canhit");
									}
								}
							}
						}
						for (n = 0; n < range; n++)
						{
							for (o = 0; o < range; o++)
							{
								if (game.getTileMap().inMap(n - range + o + 1 + l, n - o + m))
								{
									if(tileButtonLayer.getState(n - o + m, n - range + o + 1 + l).equals("normal"))
									{
										tileButtonLayer.setState(n - o + m, n - range + o + 1 + l, "canhit");
									}
								}
							}
						}
						
					}
				}
			}
		}
	}
	
	public void updateTileButtonMapToChooseAction()
	{
		tileButtonLayer.setLayerState("normal");
		int k;
		int l;
		int m;
		int n;
		int o;
		int range;
		Unit selectedUnit = game.getSelectedUnit();
		for(k = 0; k < selectedUnit.getAttackList().length; k++)
		{
			System.out.println(selectedUnit.getAttackList()[k].getName());
			for (l = 0; l < game.getTileMap().getMap().length; l++)
			{
				for (m = 0; m < game.getTileMap().getMap()[0].length; m++)
				{
					if(game.getSelectedUnit().getLocation().calculateDistance(l, m) < 2.0 && game.getVisionRangeMap(l, m) > 0)
					{
						if(game.validCapture(new int[] {l, m}) && !tileButtonLayer.getState(m, l).equals("move") && !tileButtonLayer.getState(m, l).equals("selected"))
						{
							tileButtonLayer.setState(m, l, "validtarget");
						}
						else if(!tileButtonLayer.getState(m, l).equals("move") && !tileButtonLayer.getState(m, l).equals("selected") && !tileButtonLayer.getState(m, l).equals("validtarget"))
						{
							tileButtonLayer.setState(m, l, "attack");
						}
					}
					if(selectedUnit.getAttackList()[k].checkValidTarget(l, m) && game.getVisionRangeMap(l, m) > 0)
					{
						if(game.validTarget(new int[] {l, m} , selectedUnit.getAttackList()[k]))
						{
							tileButtonLayer.setState(m, l, "validtarget");
						}
						else if(!tileButtonLayer.getState(m, l).equals("validtarget"))
						{
							tileButtonLayer.setState(m, l, "attack");
						}
						range = selectedUnit.getAttackList()[k].getDefactoAreaOfEffect();
						for (n = 0; n < range + 1; n++)
						{
							for (o = 0; o < range + 1; o++)
							{
								if(game.getTileMap().inMap(n - range + o + l, n - o + m))
								{
									if(tileButtonLayer.getState(n - o + m, n - range + o + l).equals("normal"))
									{
										tileButtonLayer.setState(n - o + m, n - range + o + l, "canhit");
									}
								}
							}
						}
						for (n = 0; n < range; n++)
						{
							for (o = 0; o < range; o++)
							{
								if (game.getTileMap().inMap(n - range + o + 1 + l, n - o + m))
								{
									if(tileButtonLayer.getState(n - o + m, n - range + o + 1 + l).equals("normal"))
									{
										tileButtonLayer.setState(n - o + m, n - range + o + 1 + l, "canhit");
									}
								}
							}
						}
						
					}
				}
			}
		}
	}
	
	public void updateTileButtonMapToChooseAction(int i, int j)
	{
		tileButtonLayer.setLayerState("normal");
		int k;
		int l;
		int m;
		int n;
		int o;
		int range;
		Unit selectedUnit = game.getSelectedUnit();
		for(k = 0; k < selectedUnit.getAttackList().length; k++)
		{
			for (l = 0; l < game.getTileMap().getMap().length; l++)
			{
				for (m = 0; m < game.getTileMap().getMap()[0].length; m++)
				{
					if(game.getVisionRangeMap(l, m) > 0)
					{
						if(game.validCapture(new int[] {l, m}, new int[] {i, j}) && !tileButtonLayer.getState(m, l).equals("move") && !tileButtonLayer.getState(m, l).equals("selected"))
						{
							tileButtonLayer.setState(m, l, "validtarget");
						}
						else if(!tileButtonLayer.getState(m, l).equals("move") && !tileButtonLayer.getState(m, l).equals("selected") && !tileButtonLayer.getState(m, l).equals("validtarget") && Location.calculateDistance(new int[] {l, m}, new int[] {i, j}) < 2.0)
						{
							tileButtonLayer.setState(m, l, "attack");
						}
					}
					if(selectedUnit.getAttackList()[k].checkValidTarget(l, m, new Location(i, j)) && game.getVisionRangeMap(l, m) > 0)
					{
						if(!tileButtonLayer.getState(m, l).equals("move") && !tileButtonLayer.getState(m, l).equals("selected") && game.validTarget(l, m, selectedUnit.getAttackList()[k], i, j))
						{
							tileButtonLayer.setState(m, l, "validtarget");
						}
						else if(!tileButtonLayer.getState(m, l).equals("move") && !tileButtonLayer.getState(m, l).equals("selected") && !tileButtonLayer.getState(m, l).equals("validtarget"))
						{
							tileButtonLayer.setState(m, l, "attack");
						}
						range = selectedUnit.getAttackList()[k].getDefactoAreaOfEffect();
						for (n = 0; n < range + 1; n++)
						{
							for (o = 0; o < range + 1; o++)
							{
								if(game.getTileMap().inMap(n - range + o + l, n - o + m))
								{
									if(tileButtonLayer.getState(n - o + m, n - range + o + l).equals("normal"))
									{
										tileButtonLayer.setState(n - o + m, n - range + o + l, "canhit");
									}
								}
							}
						}
						for (n = 0; n < range; n++)
						{
							for (o = 0; o < range; o++)
							{
								if (game.getTileMap().inMap(n - range + o + 1 + l, n - o + m))
								{
									if(tileButtonLayer.getState(n - o + m, n - range + o + 1 + l).equals("normal"))
									{
										tileButtonLayer.setState(n - o + m, n - range + o + 1 + l, "canhit");
									}
								}
							}
						}
					}
				}
			}
		}
//		for(k = 0; k < selectedUnit.getAttackList().length; k++)
//		{
//			System.out.println(selectedUnit.getAttackList()[k].getName());
//			for (l = 0; l < game.getTileMap().getMap().length; l++)
//			{
//				for (m = 0; m < game.getTileMap().getMap()[0].length; m++)
//				{
//					if(game.getSelectedUnit().getLocation().calculateDistance(l, m) < 2.0 && game.getVisionRangeMap(l, m) > 0)
//					{
//						if(game.validCapture(new int[] {l, m}, new int[] {i, j}) && !tileButtonLayer.getState(m, l).equals("move") && !tileButtonLayer.getState(m, l).equals("selected"))
//						{
//							tileButtonLayer.setState(m, l, "validtarget");
//						}
//						else if(!tileButtonLayer.getState(m, l).equals("move") && !tileButtonLayer.getState(m, l).equals("selected") && !tileButtonLayer.getState(m, l).equals("validtarget"))
//						{
//							tileButtonLayer.setState(m, l, "attack");
//						}
//					}
//					if(selectedUnit.getAttackList()[k].checkValidTarget(l, m) && game.getVisionRangeMap(l, m) > 0)
//					{
//						if(game.validTarget(l, m , selectedUnit.getAttackList()[k], i, j))
//						{
//							tileButtonLayer.setState(m, l, "validtarget");
//						}
//						else if(!tileButtonLayer.getState(m, l).equals("validtarget"))
//						{
//							tileButtonLayer.setState(m, l, "attack");
//						}
//						range = selectedUnit.getAttackList()[k].getDefactoAreaOfEffect();
//						for (n = 0; n < range + 1; n++)
//						{
//							for (o = 0; o < range + 1; o++)
//							{
//								if(game.getTileMap().inMap(n - range + o + l, n - o + m))
//								{
//									if(tileButtonLayer.getState(n - o + m, n - range + o + l).equals("normal"))
//									{
//										tileButtonLayer.setState(n - o + m, n - range + o + l, "canhit");
//									}
//								}
//							}
//						}
//						for (n = 0; n < range; n++)
//						{
//							for (o = 0; o < range; o++)
//							{
//								if (game.getTileMap().inMap(n - range + o + 1 + l, n - o + m))
//								{
//									if(tileButtonLayer.getState(n - o + m, n - range + o + 1 + l).equals("normal"))
//									{
//										tileButtonLayer.setState(n - o + m, n - range + o + 1 + l, "canhit");
//									}
//								}
//							}
//						}
//						
//					}
//				}
//			}
//		}
	}
	
	public void updateTileButtonMapToCapture()
	{
		tileButtonLayer.setLayerState("normal");
		int i;
		int j;
		double[][] moveValues = game.calculateSelectedMovementRange();
		for (i = 0; i < moveValues.length; i++)
		{
			for (j = 0; j < moveValues[0].length; j++)
			{
				if(game.getSelectedUnit().getLocation().calculateDistance(i, j) < 2.0 && game.getVisionRangeMap(i, j) > 0)
				{
					if(game.validCapture(new int[] {i, j}))
					{
						tileButtonLayer.setState(j, i, "validtarget");
					}
					else
					{
						tileButtonLayer.setState(j, i, "attack");
					}
				}
			}
		}
	}
	
	public void updateTileButtonMapToChooseSupport()
	{
		tileButtonLayer.setLayerState("normal");
		int k;
		int l;
		int m;
		int n;
		int o;
		int range;
		Unit selectedUnit = game.getSelectedUnit();
		for(k = 0; k < selectedUnit.getSupportList().length; k++)
		{
			System.out.println(selectedUnit.getSupportList()[k].getName());
			for (l = 0; l < game.getTileMap().getMap().length; l++)
			{
				for (m = 0; m < game.getTileMap().getMap()[0].length; m++)
				{
					if(selectedUnit.getSupportList()[k].checkValidTarget(l, m) && game.getVisionRangeMap(l, m) > 0)
					{
						if(game.validTarget(new int[] {l, m} , selectedUnit.getSupportList()[k]))
						{
							tileButtonLayer.setState(m, l, "validtarget");
						}
						else if(!tileButtonLayer.getState(m, l).equals("validtarget"))
						{
							tileButtonLayer.setState(m, l, "attack");
						}
						range = selectedUnit.getSupportList()[k].getDefactoAreaOfEffect();
						for (n = 0; n < range + 1; n++)
						{
							for (o = 0; o < range + 1; o++)
							{
								if(game.getTileMap().inMap(n - range + o + l, n - o + m))
								{
									if(tileButtonLayer.getState(n - o + m, n - range + o + l).equals("normal"))
									{
										tileButtonLayer.setState(n - o + m, n - range + o + l, "canhit");
									}
								}
							}
						}
						for (n = 0; n < range; n++)
						{
							for (o = 0; o < range; o++)
							{
								if (game.getTileMap().inMap(n - range + o + 1 + l, n - o + m))
								{
									if(tileButtonLayer.getState(n - o + m, n - range + o + 1 + l).equals("normal"))
									{
										tileButtonLayer.setState(n - o + m, n - range + o + 1 + l, "canhit");
									}
								}
							}
						}
						
					}
				}
			}
		}
	}
	
	public void updateTileButtonMapToMove()
	{
		tileButtonLayer.setLayerState("normal");
		int i;
		int j;
		int k;
		int l;
		int m;
		int n;
		int o;
		int range;
		Unit[][] unitTileMap = game.getUnitTileMap().getMap();
		Unit selectedUnit = game.getSelectedUnit();
		boolean[][][] testedAttack = new boolean[selectedUnit.getAttackList().length][unitTileMap.length][unitTileMap[0].length];
		double[][] moveValues = game.calculateSelectedMovementRange();
		for (i = 0; i < moveValues.length; i++)
		{
			for (j = 0; j < moveValues[0].length; j++)
			{
				if(moveValues[i][j] > 0.0)
				{
					if(unitTileMap[i][j] == null)
					{
						tileButtonLayer.setState(j, i, "move");
						for(k = 0; k < selectedUnit.getAttackList().length; k++)
						{
							for (l = 0; l < moveValues.length; l++)
							{
								for (m = 0; m < moveValues[0].length; m++)
								{
									if(Location.calculateDistance(new int[] {l, m}, new int[] {i, j}) < 2.0 && game.getVisionRangeMap(l, m) > 0)
									{
										if(game.validCapture(new int[] {l, m}, new int[] {i, j}) && !tileButtonLayer.getState(m, l).equals("move") && !tileButtonLayer.getState(m, l).equals("selected"))
										{
											tileButtonLayer.setState(m, l, "validtarget");
										}
										else if(!tileButtonLayer.getState(m, l).equals("move") && !tileButtonLayer.getState(m, l).equals("selected") && !tileButtonLayer.getState(m, l).equals("validtarget"))
										{
											tileButtonLayer.setState(m, l, "attack");
										}
									}
									if(selectedUnit.getAttackList()[k].checkValidTarget(l, m, new Location(i, j)) && game.getVisionRangeMap(l, m) > 0 && !testedAttack[k][l][m])
									{
										testedAttack[k][l][m] = true;
										if(!tileButtonLayer.getState(m, l).equals("move") && !tileButtonLayer.getState(m, l).equals("selected") && game.validTarget(l, m, selectedUnit.getAttackList()[k], i, j))
										{
											tileButtonLayer.setState(m, l, "validtarget");
										}
										else if(!tileButtonLayer.getState(m, l).equals("move") && !tileButtonLayer.getState(m, l).equals("selected") && !tileButtonLayer.getState(m, l).equals("validtarget"))
										{
											tileButtonLayer.setState(m, l, "attack");
										}
										range = selectedUnit.getAttackList()[k].getDefactoAreaOfEffect();
										for (n = 0; n < range + 1; n++)
										{
											for (o = 0; o < range + 1; o++)
											{
												if(game.getTileMap().inMap(n - range + o + l, n - o + m))
												{
													if(tileButtonLayer.getState(n - o + m, n - range + o + l).equals("normal"))
													{
														tileButtonLayer.setState(n - o + m, n - range + o + l, "canhit");
													}
												}
											}
										}
										for (n = 0; n < range; n++)
										{
											for (o = 0; o < range; o++)
											{
												if (game.getTileMap().inMap(n - range + o + 1 + l, n - o + m))
												{
													if(tileButtonLayer.getState(n - o + m, n - range + o + 1 + l).equals("normal"))
													{
														tileButtonLayer.setState(n - o + m, n - range + o + 1 + l, "canhit");
													}
												}
											}
										}
									}
								}
							}
						}
					}
					else if(unitTileMap[i][j] == game.getSelectedUnit())
					{
						tileButtonLayer.setState(j, i, "selected");
						for(k = 0; k < selectedUnit.getAttackList().length; k++)
						{
							System.out.println(selectedUnit.getAttackList()[k].getName());
							for (l = 0; l < moveValues.length; l++)
							{
								for (m = 0; m < moveValues[0].length; m++)
								{
									if(game.getSelectedUnit().getLocation().calculateDistance(l, m) < 2.0 && game.getVisionRangeMap(l, m) > 0)
									{
										if(game.validCapture(new int[] {l, m}) && !tileButtonLayer.getState(m, l).equals("move") && !tileButtonLayer.getState(m, l).equals("selected"))
										{
											tileButtonLayer.setState(m, l, "validtarget");
										}
										else if(!tileButtonLayer.getState(m, l).equals("move") && !tileButtonLayer.getState(m, l).equals("selected") && !tileButtonLayer.getState(m, l).equals("validtarget"))
										{
											tileButtonLayer.setState(m, l, "attack");
										}
									}
									if(selectedUnit.getAttackList()[k].checkValidTarget(l, m, new Location(i, j)) && game.getVisionRangeMap(l, m) > 0)
									{
										if(!tileButtonLayer.getState(m, l).equals("move") && !tileButtonLayer.getState(m, l).equals("selected") && game.validTarget(l, m, selectedUnit.getAttackList()[k], i, j))
										{
											tileButtonLayer.setState(m, l, "validtarget");
										}
										else if(!tileButtonLayer.getState(m, l).equals("move") && !tileButtonLayer.getState(m, l).equals("selected") && !tileButtonLayer.getState(m, l).equals("validtarget"))
										{
											tileButtonLayer.setState(m, l, "attack");
										}
										range = selectedUnit.getAttackList()[k].getDefactoAreaOfEffect();
										for (n = 0; n < range + 1; n++)
										{
											for (o = 0; o < range + 1; o++)
											{
												if(game.getTileMap().inMap(n - range + o + l, n - o + m))
												{
													if(tileButtonLayer.getState(n - o + m, n - range + o + l).equals("normal"))
													{
														tileButtonLayer.setState(n - o + m, n - range + o + l, "canhit");
													}
												}
											}
										}
										for (n = 0; n < range; n++)
										{
											for (o = 0; o < range; o++)
											{
												if (game.getTileMap().inMap(n - range + o + 1 + l, n - o + m))
												{
													if(tileButtonLayer.getState(n - o + m, n - range + o + 1 + l).equals("normal"))
													{
														tileButtonLayer.setState(n - o + m, n - range + o + 1 + l, "canhit");
													}
												}
											}
										}
										
									}
								}
							}
						}
					}
				}
			}
		}
	}
	
	public void updateTileButtonMapToTempSelect()
	{
		tileButtonLayer.setLayerState("normal");
		int i;
		int j;
		int k;
		int l;
		int m;
		int n;
		int o;
		int range;
		Unit[][] unitTileMap = game.getUnitTileMap().getMap();
		Unit selectedUnit = gameInterface.getSelectedUnit();
		boolean[][][] testedAttack = new boolean[selectedUnit.getAttackList().length][unitTileMap.length][unitTileMap[0].length];
		double[][] moveValues = game.calculateUnitMovementRange(selectedUnit);
		for (i = 0; i < moveValues.length; i++)
		{
			for (j = 0; j < moveValues[0].length; j++)
			{
				if(moveValues[i][j] > 0.0)
				{
					if(unitTileMap[i][j] == null)
					{
						tileButtonLayer.setState(j, i, "moveE");
						for(k = 0; k < selectedUnit.getAttackList().length; k++)
						{
							for (l = 0; l < moveValues.length; l++)
							{
								for (m = 0; m < moveValues[0].length; m++)
								{
									if(gameInterface.getSelectedUnit().getLocation().calculateDistance(l, m) < 2.0 && game.getVisionRangeMap(l, m) > 0)
									{
										if(game.validCapture(new int[] {l, m}) && !tileButtonLayer.getState(m, l).equals("moveE") && !tileButtonLayer.getState(m, l).equals("selectedE"))
										{
											tileButtonLayer.setState(m, l, "validtargetE");
										}
										else if(!tileButtonLayer.getState(m, l).equals("moveE") && !tileButtonLayer.getState(m, l).equals("selectedE") && !tileButtonLayer.getState(m, l).equals("validtargetE"))
										{
											tileButtonLayer.setState(m, l, "attackE");
										}
									}
									if(selectedUnit.getAttackList()[k].checkValidTarget(l, m, new Location(i, j)) && game.getVisionRangeMap(l, m) > 0 && !testedAttack[k][l][m])
									{
										testedAttack[k][l][m] = true;
										if(!tileButtonLayer.getState(m, l).equals("moveE") && !tileButtonLayer.getState(m, l).equals("selectedE") && game.validTarget(l, m, selectedUnit.getAttackList()[k], i, j))
										{
											tileButtonLayer.setState(m, l, "validtargetE");
										}
										else if(!tileButtonLayer.getState(m, l).equals("moveE") && !tileButtonLayer.getState(m, l).equals("selectedE") && !tileButtonLayer.getState(m, l).equals("validtargetE"))
										{
											tileButtonLayer.setState(m, l, "attackE");
										}
										range = selectedUnit.getAttackList()[k].getDefactoAreaOfEffect();
										for (n = 0; n < range + 1; n++)
										{
											for (o = 0; o < range + 1; o++)
											{
												if(game.getTileMap().inMap(n - range + o + l, n - o + m))
												{
													if(tileButtonLayer.getState(n - o + m, n - range + o + l).equals("normal"))
													{
														tileButtonLayer.setState(n - o + m, n - range + o + l, "canhitE");
													}
												}
											}
										}
										for (n = 0; n < range; n++)
										{
											for (o = 0; o < range; o++)
											{
												if (game.getTileMap().inMap(n - range + o + 1 + l, n - o + m))
												{
													if(tileButtonLayer.getState(n - o + m, n - range + o + 1 + l).equals("normal"))
													{
														tileButtonLayer.setState(n - o + m, n - range + o + 1 + l, "canhitE");
													}
												}
											}
										}
									}
								}
							}
						}
					}
					else if(unitTileMap[i][j] == game.getSelectedUnit())
					{
						tileButtonLayer.setState(j, i, "selectedE");
						for(k = 0; k < selectedUnit.getAttackList().length; k++)
						{
							System.out.println(selectedUnit.getAttackList()[k].getName());
							for (l = 0; l < moveValues.length; l++)
							{
								for (m = 0; m < moveValues[0].length; m++)
								{
									if(game.getSelectedUnit().getLocation().calculateDistance(l, m) < 2.0 && game.getVisionRangeMap(l, m) > 0)
									{
										if(game.validCapture(new int[] {l, m}) && !tileButtonLayer.getState(m, l).equals("moveE") && !tileButtonLayer.getState(m, l).equals("selectedE"))
										{
											tileButtonLayer.setState(m, l, "validtargetE");
										}
										else if(!tileButtonLayer.getState(m, l).equals("moveE") && !tileButtonLayer.getState(m, l).equals("selectedE") && !tileButtonLayer.getState(m, l).equals("validtargetE"))
										{
											tileButtonLayer.setState(m, l, "attackE");
										}
									}
									if(selectedUnit.getAttackList()[k].checkValidTarget(l, m, new Location(i, j)) && game.getVisionRangeMap(l, m) > 0)
									{
										if(!tileButtonLayer.getState(m, l).equals("moveE") && !tileButtonLayer.getState(m, l).equals("selectedE") && game.validTarget(l, m, selectedUnit.getAttackList()[k], i, j))
										{
											tileButtonLayer.setState(m, l, "validtargetE");
										}
										else if(!tileButtonLayer.getState(m, l).equals("moveE") && !tileButtonLayer.getState(m, l).equals("selectedE") && !tileButtonLayer.getState(m, l).equals("validtargetE"))
										{
											tileButtonLayer.setState(m, l, "attackE");
										}
										range = selectedUnit.getAttackList()[k].getDefactoAreaOfEffect();
										for (n = 0; n < range + 1; n++)
										{
											for (o = 0; o < range + 1; o++)
											{
												if(game.getTileMap().inMap(n - range + o + l, n - o + m))
												{
													if(tileButtonLayer.getState(n - o + m, n - range + o + l).equals("normal"))
													{
														tileButtonLayer.setState(n - o + m, n - range + o + l, "canhitE");
													}
												}
											}
										}
										for (n = 0; n < range; n++)
										{
											for (o = 0; o < range; o++)
											{
												if (game.getTileMap().inMap(n - range + o + 1 + l, n - o + m))
												{
													if(tileButtonLayer.getState(n - o + m, n - range + o + 1 + l).equals("normal"))
													{
														tileButtonLayer.setState(n - o + m, n - range + o + 1 + l, "canhitE");
													}
												}
											}
										}
										
									}
								}
							}
						}
					}
				}
			}
		}
	}
	
	public void updateTileButtonMapToAttack()
	{
		tileButtonLayer.setLayerState("normal");
		int i;
		int j;
		int k;
		int l;
		int range;
		double[][] moveValues = game.calculateSelectedMovementRange();
		for (i = 0; i < moveValues.length; i++)
		{
			for (j = 0; j < moveValues[0].length; j++)
			{
				if(game.getSelectedAttack().checkValidTarget(i, j) && game.getVisionRangeMap(i, j) > 0)
				{
					if(game.validTarget(new int[] {i, j}))
					{
						tileButtonLayer.setState(j, i, "validtarget");
					}
					else
					{
						tileButtonLayer.setState(j, i, "attack");
					}
					range = game.getSelectedAttack().getDefactoAreaOfEffect();
					for (k = 0; k < range + 1; k++)
					{
						for (l = 0; l < range + 1; l++)
						{
							if(game.getTileMap().inMap(k - range + l + i, k - l + j))
							{
								if(tileButtonLayer.getState(k - l + j, k - range + l + i).equals("normal"))
								{
									tileButtonLayer.setState(k - l + j, k - range + l + i, "canhit");
								}
							}
						}
					}
					for (k = 0; k < range; k++)
					{
						for (l = 0; l < range; l++)
						{
							if (game.getTileMap().inMap(k - range + l + 1 + i, k - l + j))
							{
								if(tileButtonLayer.getState(k - l + j, k - range + l + 1 + i).equals("normal"))
								{
									tileButtonLayer.setState(k - l + j, k - range + l + 1 + i, "canhit");
								}
							}
						}
					}
					
				}
			}
		}
	}
	
	public void updateTileButtonMapToAttack(Attack attack)
	{
		tileButtonLayer.setLayerState("normal");
		int i;
		int j;
		int k;
		int l;
		int range;
		double[][] moveValues = game.calculateSelectedMovementRange();
		for (i = 0; i < moveValues.length; i++)
		{
			for (j = 0; j < moveValues[0].length; j++)
			{
				if(attack.checkValidTarget(i, j) && game.getVisionRangeMap(i, j) > 0)
				{
					if(attack.getUnitsDamaged(new Location(i, j), game.getUnitTileMap(), game.getBuildingTileMap()).size() > 0)
					{
						tileButtonLayer.setState(j, i, "validtarget");
					}
					else
					{
						tileButtonLayer.setState(j, i, "attack");
					}
					range = attack.getDefactoAreaOfEffect();
					for (k = 0; k < range + 1; k++)
					{
						for (l = 0; l < range + 1; l++)
						{
							if(game.getTileMap().inMap(k - range + l + i, k - l + j))
							{
								if(tileButtonLayer.getState(k - l + j, k - range + l + i).equals("normal"))
								{
									tileButtonLayer.setState(k - l + j, k - range + l + i, "canhit");
								}
							}
						}
					}
					for (k = 0; k < range; k++)
					{
						for (l = 0; l < range; l++)
						{
							if (game.getTileMap().inMap(k - range + l + 1 + i, k - l + j))
							{
								if(tileButtonLayer.getState(k - l + j, k - range + l + 1 + i).equals("normal"))
								{
									tileButtonLayer.setState(k - l + j, k - range + l + 1 + i, "canhit");
								}
							}
						}
					}
					
				}
			}
		}
	}
	
	public void setMenuUpdateCounter()
	{
		menuUpdateCounter = menuUpdateDelay;
	}
	
	public void updateSelectedView(int commandNumber, ArrayList<String> menuOptions)
	{
		if(commandNumber != -1)
		{
			if(gameInterface.getStage().equals("chooseAction"))
			{
				if(menuOptions.get(commandNumber) == "Attack")
				{
					updateTileButtonMapToChooseAttack();
				}
				else if(menuOptions.get(commandNumber) == "Support")
				{
					updateTileButtonMapToChooseSupport();
				}
				else if(menuOptions.get(commandNumber) == "Capture")
				{
					updateTileButtonMapToCapture();
				}
				else if(menuOptions.get(commandNumber) == "Wait")
				{
					updateTileButtonMapToDeselected();
				}
			}
			else if(gameInterface.getStage() == "chooseAttack")
			{
				if(menuOptions.get(commandNumber) == "Back" || menuOptions.get(commandNumber) == "Wait")
				{
					updateTileButtonMapToDeselected();
					clearMoveInformationBoxInfo();
				}
				else
				{
					Unit selectedUnit = game.getSelectedUnit();
					updateTileButtonMapToAttack(selectedUnit.getOffensiveAttack(commandNumber));
					double ap = selectedUnit.getOffensiveAttack(commandNumber).getAttackPower();
					double sc = selectedUnit.getOffensiveAttack(commandNumber).getStaminaCost();
					double trueap = selectedUnit.getOffensiveAttack(commandNumber).getAttackPower() * (selectedUnit.getHealthPoint() / selectedUnit.getMaxHealthPoint())
							* selectedUnit.getBoostList(1) + selectedUnit.getBoostList(0);
					double truesc = selectedUnit.getOffensiveAttack(commandNumber).getStaminaCost() * selectedUnit.getBoostList(5) + selectedUnit.getBoostList(4);
					String apBoost;
					String scBoost;
					System.out.println("ap: " + ap);
					System.out.println("trueap: " + trueap);
					if(trueap - ap < 0)
					{
						apBoost = " - " + String.valueOf((int) (ap - trueap));
					}
					else if(trueap - ap > 0)
					{
						apBoost = " + " + String.valueOf((int) (trueap - ap));
					}
					else
					{
						apBoost = "";
					}
					if(truesc - sc < 0)
					{
						scBoost = " - " + String.valueOf((int) (sc - truesc));
					}
					else if(truesc - sc > 0)
					{
						scBoost = " + " + String.valueOf((int) (truesc - sc));
					}
					else
					{
						scBoost = "";
					}
					if(ap < 0)
					{
						setMoveInformationBoxInfo(menuOptions.get(commandNumber) + "\n" + "HP Heal: " + 
								(int) (-ap) + apBoost + "\n" + "Stamina Used: " + 
								(int) (sc) + scBoost);
					}
					else if(ap >= 0)
					{
						setMoveInformationBoxInfo(menuOptions.get(commandNumber) + "\n" + "Damage Dealt: " + 
								(int) (ap) + apBoost + "\n" + "Stamina Used: " + 
								(int) (sc) + scBoost);
					}
				}
			}
			else if(gameInterface.getStage() == "chooseSupport")
			{
				if(menuOptions.get(commandNumber) == "Back" || menuOptions.get(commandNumber) == "Wait")
				{
					updateTileButtonMapToDeselected();
					clearMoveInformationBoxInfo();
				}
				else
				{
					updateTileButtonMapToAttack(game.getSelectedUnit().getSupport(commandNumber));
					double ap = game.getSelectedUnit().getSupport(commandNumber).getAttackPower();
					if(ap <= 0)
					{
						setMoveInformationBoxInfo(menuOptions.get(commandNumber) + "\n" + "HP Heal: " + 
								(-game.getSelectedUnit().getSupport(commandNumber).getAttackPower()) + "\n" + "Stamina Used: " + 
								game.getSelectedUnit().getSupport(commandNumber).getStaminaCost());
					}
					else if(ap > 0)
					{
						setMoveInformationBoxInfo(menuOptions.get(commandNumber) + "\n" + "Damage Dealt: " + 
								(game.getSelectedUnit().getSupport(commandNumber).getAttackPower()) + "\n" + "Stamina Used: " + 
								game.getSelectedUnit().getSupport(commandNumber).getStaminaCost());
					}
				}
			}
		}
		else
		{
			updateToStage();
		}
	}
	
	public void setLastCommandNumber(int commandNumber)
	{
		lastCommandNumber = commandNumber;
	}
	
	public void setLastMenuOption(ArrayList<String> menuOption)
	{
		lastMenuOption = menuOption;
	}
	
	private class GameAdapter implements KeyListener
	{
		//private GamePanel gamePanel;
		
		public GameAdapter(GamePanel gamePanel)
		{
			super();
			//this.gamePanel = gamePanel;
			System.out.println("GameAdapter initialised");
		}

		@Override
		public void keyReleased(KeyEvent e)
		{
			System.out.println("Key Released");
			int key = e.getKeyCode();
			if(key == KeyEvent.VK_DOWN)
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
				camera.setZd(0.01);
				camera.setZs(0.0);
				camera.setZStopping(true);
			}
			else if(key == KeyEvent.VK_S)
			{
				System.out.println("S Released");
				camera.setZd(0.01);
				camera.setZs(0.0);
				camera.setZStopping(true);
			}
		}
		
		@Override
		public void keyPressed(KeyEvent e)
		{
			System.out.println("Key Pressed");
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
			else if(key == KeyEvent.VK_W)
			{
				System.out.println("W Pressed");
				camera.setZa(0.01);
				camera.setZStopping(false);
			}
			else if(key == KeyEvent.VK_S)
			{
				System.out.println("S Pressed");
				camera.setZa(-0.01);
				camera.setZStopping(false);
			}
		}

		@Override
		public void keyTyped(KeyEvent e) {
			System.out.println("Key Typed");
		}
	}
	
	private class MouseAdapter implements MouseListener, MouseMotionListener, MouseWheelListener
	{
		private GamePanel gamePanel;
		private int commandNumber;
		private boolean editingGame = false;
		
		public MouseAdapter(GamePanel gamePanel)
		{
			super();
			System.out.println("MouseAdapter Initialised");
			this.gamePanel = gamePanel;
		}
		
		@Override
		public void mouseMoved(MouseEvent e)
		{
			Point p = e.getPoint();
			mousePoint = p;
			int[] coordinateNumber; // = tileButtonLayer.mask(menuLayer, p, camera, zeroCamera);
			commandNumber = -1;
			commandNumber = menuButtonLayer.mouseMoved(p, zeroCamera);
			if(commandNumber < 0)
			{
				coordinateNumber = tileButtonLayer.mouseMoved(p, camera);
				if(gameInterface.getStage() == "move")
				{
					double[][] moveValues = game.calculateSelectedMovementRange();
					if(game.getTileMap().inMap(coordinateNumber) && moveValues[coordinateNumber[0]][coordinateNumber[1]] > 0.0 
							&& game.getUnitTileMap().getMap(coordinateNumber[0], coordinateNumber[1]) == null)
					{
						gamePanel.updateTileButtonMapToChooseAction(coordinateNumber[0], coordinateNumber[1]);
						//System.out.println("CoordinateNumber: (" + coordinateNumber[0] + "," + coordinateNumber[1] + ")");
					}
					else
					{
						updateToStage();
					}
				}
			}
			if(commandNumber != -1 && lastCommand == -1)
			{
				menuButtonLayer.getSpriteList().get(commandNumber).setState("over");
			}
			else if(commandNumber != -1 && lastCommand != -1)
			{
				menuButtonLayer.getSpriteList().get(lastCommand).setState("");
				menuButtonLayer.getSpriteList().get(commandNumber).setState("over");
			}
			else if(lastCommand != -1 && lastCommand < menuButtonLayer.getSpriteList().size() && commandNumber == -1)
			{
				menuButtonLayer.getSpriteList().get(lastCommand).setState("");
			}
			if(gamePanel.lastCommandNumber != commandNumber)
			{
				gamePanel.setMenuUpdateCounter();
				gamePanel.setLastCommandNumber(commandNumber);
				gamePanel.setLastMenuOption(gamePanel.gameInterface.playerMenuChoice());
			}
			lastCommand = commandNumber;
		}
		
		@Override
		public void mousePressed(MouseEvent e)
		{
			System.out.println("Mouse pressed");
			int button = e.getButton();
			System.out.print("Mouse button pressed: ");
			System.out.println(button);
			Point p = e.getPoint();
			if(p != null)
			{
				mousePoint = p;
				if(button == MouseEvent.BUTTON1)
				{
					if(!editingGame)
					{
						editingGame = true;
						int[] coordinateNumber = tileButtonLayer.mask(menuLayer, p, camera, zeroCamera);
						int commandNumber = menuButtonLayer.mouseLeftClick(p, zeroCamera);
						int commandType = -1;
						if(coordinateNumber[0] != -1 || coordinateNumber[1] != -1)
						{
							commandType = 2;
						}
						if(commandNumber != -1)
						{
							commandType = 1;
						}
						ArrayList<AnimationCommand> animationCommandList = gameInterface.playerInput(coordinateNumber, commandNumber, commandType, gamePanel);
						AnimationCommand animationCommand;
						for(int i = 0; i < animationCommandList.size(); i++)
						{
							animationCommand = animationCommandList.get(i);
							if(animationCommand.getCommandType() == 0)
							{

							}
							else if(animationCommand.getCommandType() == 1)
							{

							}
							else if(animationCommand.getCommandType() == 2)
							{

							}
							else if(animationCommand.getCommandType() == 3)
							{

							}
							else if(animationCommand.getCommandType() == 4)
							{
								if(animationCommand.getState().equals("buildingMapRefresh"))
								{
									buildingLayer = new GridLayer(game.getBuildingTileMap(), 128, 1.0, true, 0, 0, paletteList);
								}
								else if(animationCommand.getState().equals("unitMapRefresh"))
								{
									unitLayer = new GridLayer(game.getUnitTileMap(), 128, 128, 256, 1.0, true, 0, -128, paletteList);
								}
								else if(animationCommand.getState().equals("updateStage"))
								{
									updateToStage();
								}
								else if(animationCommand.getState().equals("updateTileInfo"))
								{
									setTileInformationBoxInfo();
								}
								else if(animationCommand.getState().equals("updateUnitInfo"))
								{
									setUnitInformationBoxInfo();
								}
								else if(animationCommand.getState().equals("updateBuildingInfo"))
								{
									setBuildingInformationBoxInfo();
								}
								else if(animationCommand.getState().equals("clearTileInfo"))
								{
									clearTileInformationBoxInfo();
								}
								else if(animationCommand.getState().equals("clearUnitInfo"))
								{
									clearUnitInformationBoxInfo();
								}
								else if(animationCommand.getState().equals("clearBuildingInfo"))
								{
									clearBuildingInformationBoxInfo();
								}
							}
						}
						this.gamePanel.makeMenu(gameInterface.playerMenuChoice(), coordinateNumber[1], coordinateNumber[0]);
						//animationCommands.get(0) is the type of command;
						System.out.println("Interface State: " + gameInterface.getStage());
						System.out.print("Selected: ");
						System.out.println(gameInterface.getGame().getUnitSelected());
						editingGame = false;
					}
				}
			}
		}

		@Override
		public void mouseDragged(MouseEvent e) {
			System.out.println("Mouse Dragged");
		}

		@Override
		public void mouseClicked(MouseEvent e) {
			System.out.println("Mouse Clicked");
			
		}

		@Override
		public void mouseReleased(MouseEvent e) {
			System.out.println("Mouse Released");
		}

		@Override
		public void mouseEntered(MouseEvent e) {
			//System.out.println("Mouse Entered");
			
		}

		@Override
		public void mouseExited(MouseEvent e) {
			//System.out.println("Mouse Exited");
		}

		@Override
		public void mouseWheelMoved(MouseWheelEvent e) {
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
			// TODO Auto-generated method stub
			setInformationBoxLocation();
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
