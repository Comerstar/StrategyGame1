package test;
import java.io.File;
import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import animation.TextInterface;
import unit.*;
import utilities.*;
import terrain.*;
import map.*;
import game.*;

public class Test1 {
	
	public static void main(String[] args)
	{
		test2();
	}
	
	public static void test1()
	{
		Tile tile1 = new Tile(1);
		Tile tile2 = new Tile(2);
		Tile wall = new Tile(100);
		//Unit unit = new Unit(1, 100.0, 10.0, 100.0, 6.0, 20.0, 20.0, 100.0, 10.0, 100.0, 7.0, 10.0, new int[]{3,3}, tempAttackList.clone(), new double[1][1], 0, 0, 10.0, 0);
		TileMap map = new TileMap(new Tile[][]{{tile1,tile1,tile1,tile1,tile2,tile2,tile1},{tile1,tile1,tile1,tile1,tile1,tile1,tile1},{tile1,tile1,tile1,tile2,tile2,tile1,tile1},{tile1,wall,tile2,tile1,tile1,wall,tile1},{tile1,tile1,tile2,wall,wall,tile1,tile2},{tile1,tile1,wall,tile1,tile2,tile2,tile1},{tile1,tile1,tile1,wall,tile2,tile1,tile1}});
		Game game = new Game(map);
		double[][] unitSpace = game.calculateUnitMovementRange(0);
		for(int i = 0; i < unitSpace.length; i++)
		{
			System.out.print("|");
			for(int j = 0; j < unitSpace.length; j++)
			{
				System.out.print(unitSpace[i][j]);
				System.out.print("|");
			}
			System.out.println();
		}
		System.out.println("Done");
	}
	
	public static void test2()
	{
		Unit test1 = new Unit(0, 0, 1, 0, 0);
		Unit test2 = new Unit(3, 1, 0, 0, 1);
		Unit test3 = new Unit(2, 1, 2, 0, 2);
		Unit test4 = new Unit(2, 1, 1, 0, 3);
		Unit test12 = new Unit(0, 2, 1, 0, 4);
		Unit test13 = new Unit(0, 2, 0, 0, 4);
		Commander test5 = new Commander(9, 0, 0, 0, 5);
		Unit testReaper = new Unit(13, 4, 4, 1, 6);
		Unit test6 = new Unit(1, 5, 8, 1, 0);
		Unit test7 = new Unit(1, 7, 7, 1, 1);
		Unit test8 = new Unit(4, 6, 6, 1, 2);
		Unit test9 = new Unit(5, 6, 7, 1, 3);
		SpecialUnit test10 = new SpecialUnit(7, 7, 6, 1, 4);
		Commander test11 = new Commander(8, 7, 4, 1, 5);
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
		TileInterface[][] tileMap = new TileInterface[][] {{g ,m ,m ,g ,b1,f ,r4, f ,f },
										 		  		   {g ,m ,m ,g ,g ,g ,f , f ,f },
										 				   {m ,m ,g ,g ,g ,g ,w , f ,f },
										 				   {f ,g ,g ,w ,g ,f1,f , g ,r2},
										 				   {r1,g ,g ,m ,m ,g ,f , g ,g },
										 				   {f ,f ,g ,g ,w ,g ,w , g ,g },
										 				   {f ,r3,f ,f ,g ,g ,g , g ,g },
										 				   {m ,m ,f ,f ,f ,b2,f , m ,g }};
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
		tempSide.add(testReaper);
		Player player1 = new Player(ArrayHandler.cloneShallowUnitArray(tempSide), ArrayHandler.cloneIntegerArray(allowedUnitList), 1, "Test 2");
		player1.useResource(-100000.0);
		Game game = new Game(new Player[] {player0, player1}, map, new double[] {1.0, 1.0, 1.0, 1.0, 2.0, 0.0});
		game.setWinConditionList(7, 1.0);
		game.setWinConditionList(0, 1.0);
		TextInterface textInterface = new TextInterface(game);
		while (!game.getWon())
		{
			if(!game.getWon())
			{
				textInterface.renderScreen();
			}
			if(!game.getWon())
			{
				textInterface.playerAction();
			}
		}
		for(int i = 0; i < game.getWinOrder().length; i++)
		{
			System.out.print("Team ");
			System.out.print(i + 1);
			System.out.print(" placed in ");
			System.out.println(game.getWinOrder()[i]);
		}
	}
	
	public static void test3()
	{
		TileInterface[][]  tileTestMap = new TileInterface[3][3];
		Tile test00 = new Tile(0);
		Tile test10 = new Tile(1);
		Tile test20 = new Tile(2);
		Tile test01 = new Tile(4);
		Building test11 = new Building(4, new Location(1,1));
		Tile test21 = new Tile(3);
		Tile test02 = new Tile(0);
		Tile test12 = new Tile(1);
		Tile test22 = new Tile(2);
		tileTestMap[0][0] = test00;
		tileTestMap[1][0] = test10;
		tileTestMap[2][0] = test20;
		tileTestMap[0][1] = test01;
		tileTestMap[1][1] = test11;
		tileTestMap[2][1] = test21;
		tileTestMap[0][2] = test02;
		tileTestMap[1][2] = test12;
		tileTestMap[2][2] = test22;
		int i;
		int j;
		int k;
		int spaces = 12;
		String print;
		for(i = 0; i < 3; i++)
		{
			System.out.print("|");
			for(j = 0; j < 3; j++)
			{
				print = ((Integer) tileTestMap[i][j].getTileNumber()).toString();
				System.out.print(print);
				for(k = 0; k < spaces - print.length(); k++)
				{
					System.out.print(" ");
				}
				System.out.print("|");
			}
			System.out.println();
		}
		System.out.println();
		for(i = 0; i < 3; i++)
		{
			System.out.print("|");
			for(j = 0; j < 3; j++)
			{
				print = ((Double) tileTestMap[i][j].getMovementCost()).toString();
				System.out.print(print);
				for(k = 0; k < spaces - print.length(); k++)
				{
					System.out.print(" ");
				}
				System.out.print("|");
			}
			System.out.println();
		}
		System.out.println();
		for(i = 0; i < 3; i++)
		{
			System.out.print("|");
			for(j = 0; j < 3; j++)
			{
				print = ((Boolean) tileTestMap[i][j].isBuilding()).toString();
				System.out.print(print);
				for(k = 0; k < spaces - print.length(); k++)
				{
					System.out.print(" ");
				}
				System.out.print("|");
			}
			System.out.println();
		}
		System.out.println();
		for(i = 0; i < 3; i++)
		{
			System.out.print("|");
			for(j = 0; j < 3; j++)
			{
				print = ((Integer) tileTestMap[i][j].getTileType()).toString();
				System.out.print(print);
				for(k = 0; k < spaces - print.length(); k++)
				{
					System.out.print(" ");
				}
				System.out.print("|");
			}
			System.out.println();
		}
		System.out.println();
		for(i = 0; i < 3; i++)
		{
			System.out.print("|");
			for(j = 0; j < 3; j++)
			{
				print = tileTestMap[i][j].getTileName();
				System.out.print(print);
				for(k = 0; k < spaces - print.length(); k++)
				{
					System.out.print(" ");
				}
				System.out.print("|");
			}
			System.out.println();
		}
		System.out.println();
		for(i = 0; i < 3; i++)
		{
			System.out.print("|");
			for(j = 0; j < 3; j++)
			{
				print = tileTestMap[i][j].getName();
				System.out.print(print);
				for(k = 0; k < spaces - print.length(); k++)
				{
					System.out.print(" ");
				}
				System.out.print("|");
			}
			System.out.println();
		}
	}
	
	public static Set<String> listFilesUsingJavaIO(String dir) {
	    return Stream.of(new File(dir).listFiles())
	      .filter(file -> !file.isDirectory())
	      .map(File::getName)
	      .collect(Collectors.toSet());
	}
	
	public Set<String> listFilesUsingDirectoryStream(String dir) throws IOException {
	    Set<String> fileList = new HashSet<>();
	    try (DirectoryStream<Path> stream = Files.newDirectoryStream(Paths.get(dir))) {
	        for (Path path : stream) {
	            if (!Files.isDirectory(path)) {
	                fileList.add(path.getFileName()
	                    .toString());
	            }
	        }
	    }
	    return fileList;
	}
	
}