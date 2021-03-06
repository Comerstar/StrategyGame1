package animation;

import java.awt.Container;
import java.util.ArrayList;

import javax.swing.JFrame;
import javax.swing.JPanel;

import animation.layer.ButtonLayer;
import animation.layer.GridButtonLayer;
import animation.layer.GridLayer;
import animation.layer.Layer;
import animation.palette.Palette;
//import animation.layer.TextBox;
import animation.sprite.Button;
//import animation.sprite.Font;
import animation.sprite.SpriteInterface;
import game.Game;
import game.Player;
import interaction.GameInterface;
import map.TileMap;
import terrain.Building;
import terrain.Tile;
import terrain.TileInterface;
import unit.Commander;
import unit.SpecialUnit;
import unit.Unit;
import utilities.ArrayHandler;
import utilities.Location;

public class Window extends JFrame
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public Window(GamePanel gamePanel)
	{
		System.out.println("windowMade");
		
		setResizable(true);
		
		setTitle("Testing Strategy Game");
		setLocationRelativeTo(null);
		add(gamePanel);
		setSize(1200, 800);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setFocusable(true);
		setVisible(true);
	}
	
	public Window(JPanel panel, int width, int height, String name)
	{
System.out.println("windowMade");
		
		setResizable(true);
		
		setTitle(name);
		setLocationRelativeTo(null);
		add(panel);
		setSize(width, height);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setFocusable(true);
		setVisible(true);
	}

	@Deprecated
	public Window(Camera camera, GridLayer tileLayer, GridLayer unitLayer, Layer animationLayer, Layer menuLayer, int frameRate)
	{
		System.out.println("windowMade");
		add(new GamePanel(camera, tileLayer, unitLayer, animationLayer, menuLayer, frameRate));
		
		setResizable(true);
		
		setTitle("Testing Strategy Game");
		setLocationRelativeTo(null);
		setSize(1200, 800);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setVisible(true);
		//setFocusable(true);
	}

	@Deprecated
	public Window(Camera camera, GridLayer tileLayer, GridButtonLayer tileButtonLayer, GridLayer buildingLayer, GridLayer unitLayer, Layer animationLayer, Layer menuLayer, ButtonLayer menuButtonLayer, int frameRate, GameInterface gameInterface)
	{
		System.out.println("windowMade");
		Container pane = this.getContentPane();
		GamePanel gp = new GamePanel(camera, tileLayer, tileButtonLayer, buildingLayer, unitLayer, animationLayer, menuLayer, menuButtonLayer, frameRate, gameInterface, 3);
		pane.add(gp);
		
		setResizable(true);
		
		setTitle("Testing Strategy Game");
		setLocationRelativeTo(null);
		setSize(1200, 800);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setVisible(true);
		//setFocusable(true);
	}

	public Window(Camera camera, GridLayer tileLayer, GridButtonLayer tileButtonLayer, GridLayer buildingLayer, GridLayer unitLayer, Layer animationLayer, Layer menuLayer, ButtonLayer menuButtonLayer, int frameRate, GameInterface gameInterface, Palette[] paletteList)
	{
		System.out.println("windowMade");
		Container pane = this.getContentPane();
		GamePanel gp = new GamePanel(camera, tileLayer, tileButtonLayer, buildingLayer, unitLayer, animationLayer, menuLayer, menuButtonLayer, frameRate, gameInterface, 1, paletteList);
		pane.add(gp);
		
		setResizable(true);
		
		setTitle("Testing Strategy Game");
		setLocationRelativeTo(null);
		setSize(1200, 800);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setVisible(true);
		//setFocusable(true);
	}
	
	public static void main(String[] args)
	{
		Unit test1 = new Unit(0, 0, 1, 0, 0);
		Unit test2 = new Unit(3, 1, 0, 0, 1);
		Unit test3 = new Unit(2, 1, 2, 0, 2);
		Unit test4 = new Unit(2, 1, 1, 0, 3);
		Unit test12 = new Unit(0, 2, 1, 0, 4);
		Unit test13 = new Unit(0, 2, 0, 0, 5);
		Commander test5 = new Commander(8, 0, 0, 0, 6);
		//Unit testReaper = new Unit(13, 4, 4, 1, 6);
		Unit test6 = new Unit(1, 5, 8, 1, 0);
		Unit test7 = new Unit(1, 7, 7, 1, 1);
		Unit test14 = new Unit(4, 7, 8, 1, 2);
		Unit test8 = new Unit(4, 6, 6, 1, 3);
		Unit test9 = new Unit(5, 6, 7, 1, 4);
		SpecialUnit test10 = new SpecialUnit(7, 7, 6, 1, 5);
		Commander test11 = new Commander(9, 7, 4, 1, 6);
		Tile g = new Tile(0);
		Tile w = new Tile(1);
		Tile f = new Tile(2);
		Tile m = new Tile(3);
		Building r1 = new Building(4, new Location(0, 0));
		Building r2 = new Building(4, new Location(0, 0));
		Building r3 = new Building(4, new Location(0, 0));
		Building r4 = new Building(4, new Location(0, 0));
		Building b1= new Building(5, new Location(0, 0));
		Building b2 = new Building(5, new Location(0, 0));
		Building f1 = new Building(6, new Location(0, 0));
		TileInterface[][] tileMap = new TileInterface[][] {{g ,m ,m ,g ,b1,f ,r4,f ,f },
										 		  		   {g ,m ,m ,g ,g ,g ,f ,f ,f },
										 				   {m ,m ,g ,g ,g ,g ,w ,f ,f },
										 				   {f ,g ,g ,w ,g ,f1,f ,g ,r2},
										 				   {r1,g ,g ,m ,m ,g ,f ,g ,g },
										 				   {f ,f ,g ,g ,w ,g ,w ,g ,g },
										 				   {f ,r3,f ,f ,g ,g ,g ,g ,g },
										 				   {m ,m ,f ,f ,f ,b2,f ,m ,g }};

//		TileInterface[][] tileMap = new TileInterface[][] {{g ,g ,g ,g ,g ,g ,g ,g ,g ,g ,g ,g ,g },
//										 		  		   {g ,g ,g ,g ,g ,g ,g ,g ,g ,g ,g ,g ,g },
//										 				   {g ,g ,g ,g ,g ,g ,g ,g ,g ,g ,g ,g ,g },
//										 				   {g ,g ,g ,g ,g ,g ,g ,g ,g ,g ,g ,g ,g },
//										 				   {g ,g ,g ,g ,g ,g ,g ,g ,g ,g ,g ,g ,g },
//										 				   {g ,g ,g ,g ,g ,g ,g ,g ,g ,g ,g ,g ,g },
//										 				   {g ,g ,g ,g ,g ,g ,g ,g ,g ,g ,g ,g ,g },
//										 				   {g ,g ,g ,g ,g ,g ,g ,g ,g ,g ,g ,g ,g },
//										 				   {g ,g ,g ,g ,g ,g ,g ,g ,g ,g ,g ,g ,g },
//										 				   {g ,g ,g ,g ,g ,g ,g ,g ,g ,g ,g ,g ,g },
//										 				   {g ,g ,g ,g ,g ,g ,g ,g ,g ,g ,g ,g ,g },
//										 				   {g ,g ,g ,g ,g ,g ,g ,g ,g ,g ,g ,g ,g },
//										 				   {g ,g ,g ,g ,g ,g ,g ,g ,g ,g ,g ,g ,g }};
		Palette[] paletteList = new Palette[2];
		paletteList[0] = new Palette("palette1");
		paletteList[1] = new Palette("palette2");
		TileMap map = new TileMap(tileMap);
		ArrayList<Unit> tempSide = new ArrayList<Unit>();
		ArrayList<Integer> allowedUnitList = new ArrayList<Integer>();
		allowedUnitList.add(0);
		allowedUnitList.add(2);
		allowedUnitList.add(3);
		tempSide.add(test1);
		tempSide.add(test2);
		tempSide.add(test3);
		tempSide.add(test4);
		tempSide.add(test5);
		tempSide.add(test12);
		tempSide.add(test13);
		Player player0 = new Player(ArrayHandler.cloneShallowUnitArray(tempSide), ArrayHandler.cloneIntegerArray(allowedUnitList), 0, "Test 1");
		tempSide.clear();
		allowedUnitList.clear();
		allowedUnitList.add(1);
		allowedUnitList.add(4);
		allowedUnitList.add(5);
		tempSide.add(test6);
		tempSide.add(test7);
		tempSide.add(test8);
		tempSide.add(test9);
		tempSide.add(test10);
		tempSide.add(test11);
		tempSide.add(test14);
		//tempSide.add(testReaper);
		Player player1 = new Player(ArrayHandler.cloneShallowUnitArray(tempSide), ArrayHandler.cloneIntegerArray(allowedUnitList), 1, "Test 2");
		//player1.useResource(-100000.0);
		Game game = new Game(new Player[] {player0, player1}, map, new double[] {1.0, 1.0, 1.0, 1.0, 2.0, 0.0});
		GameInterface gameInterface = new GameInterface(game);
		game.setWinConditionList(7, 1.0);
		game.setWinConditionList(0, 1.0);
		Button test = new Button("units", "comerstar", new String[] {"overworldr","right"}, new String[] {"overworldr","right"}, "overworldr", true, true, 128, 256, 0 ,0);
		Button testawesomefish = new Button("units", "theawesomefish", new String[] {"overworldf"}, new String[] {"overworldf"}, "overworldf", true, true, 128, 256, 128 ,0);
		GridLayer testTileLayer = new GridLayer(map, 0, 128, 1.0, true, 0, 0);
		GridLayer testUnitLayer = new GridLayer(game.getUnitTileMap(), 128, 128, 256, 1.0, true, 0, -128, paletteList);
		GridLayer testBuildingLayer = new GridLayer(game.getBuildingTileMap(), 128, 1.0, true, 0, 0, paletteList);
		ArrayList<Button> spriteList1 = new ArrayList<Button>();
		ArrayList<SpriteInterface> spriteList2 = new ArrayList<SpriteInterface>();
		ArrayList<SpriteInterface> spriteList3 = new ArrayList<SpriteInterface>();
		spriteList2.add(test);
		spriteList2.add(testawesomefish);
		//Font standardFont = new Font("standard");
		Layer testLayer1 = new Layer(spriteList2, 1.0, true, 0, 0);
		Layer testLayer2 = new Layer(spriteList3, 1.0, true, 0, 0);
		//TextBox testTextBox = new TextBox(-64, -64, 1024, 1024, 1.0, true, "Text", standardFont, 10.0);
		Button tileButton = new Button("tiles", "gridedge", new String[] {"normal","attack","canhit","move","selected","validtarget","attackE","canhitE","moveE","selectedE","validtargetE"}, "normal", true, true, 128, 128,0, 0);
		GridButtonLayer testTileButtonLayer = new GridButtonLayer(tileButton, map.getWidth(), map.getHeight(), 128, 1.0, true, 0, 0);
		ButtonLayer testLayer3 = new ButtonLayer(spriteList1, 0, 1.0, true, 0, 0);
		Camera camera = new Camera(-64, -64, 0.6);
		System.out.println("TextBox Words: ");
		//for(int i = 0; i < testTextBox.getWordList().size(); i++)
		//{
		//	System.out.println(testTextBox.getWordList().get(i));
		//}
		System.out.println("TextBox Words End");
		Window window = new Window(camera, testTileLayer, testTileButtonLayer, testBuildingLayer, testUnitLayer, testLayer2, testLayer1, testLayer3, 30, gameInterface, paletteList);
		window.toFront();
	}
}
