package game;
import map.*;
import terrain.*;

import java.util.ArrayList;

import attack.*;
import unit.*;
import utilities.*;

public class Game {

	
	//The way that a side can win/lose
	//0  = Active Commander Defeated
	//1  = <x Commanders Left
	//2  = <x Special Unit Left (Not Including Commanders)
	//3  = <x Normal Units Left (Not Including Special Units and Commanders)
	//4  = <x Special Unit Left (Including Commanders)
	//5  = <x Normal Units Left (Not Including Commanders)
	//6  = <x Normal Units Left (Not Including Special Units)
	//7  = <x Normal Units Left (Including Both Special Units and Commanders)
	//8  = <x Strong Holds Left
	//9  = <x Barracks Left
	//10 = <x Reactors/Generators Left
	//11 = <x Factories Left
	//12 = <x Fortresses Left
	//13 = >x Commanders In Area
	//14 = >x Special Units In Area (Not Including Commanders)
	//15 = >x Normal Units In Area (Not Including SpecialUnits and Commanders)
	//16 = >x Special Units In Area (Including Commanders)
	//17 = >x Normal Units In Area (Not Including Commanders)
	//18 = >x Normal Units In Area (Not Including SpecialUnits)
	//19 = >x Normal Units In Area (Including Both SpecialUnits and Commanders)
	private static final int numberOfWinConditions = 18;
	//The settings of the game
	//0: CommandEffects (false = 0, true = 1)
	//1: Specials (false = 0, true = 1)
	//2: FriendlyFire (false = 0, true = 1)
	//3: ResourceCost (false = 0, true = 1)
	//4: ChangeActiveCommander (still can move and attack = 0, can only move = 1, cannot move or attack = 2)
	//5: FogMap (false = 0, true = 1)
	//                                                                 0,   1,   2,   3,   4,   5
	private static final double[] defaultSettingList = new double[] {1.0, 1.0, 1.0, 1.0, 2.0, 0.0};
	//                                                             0,    1,    2,    3,    4,    5,    6,    7,    8,    9,   10,   11,   12,   13,   14,   15,   16,   17,   18,   19
	private static final double[] noWinCondition = new double[] {0.0, -1.0, -1.0, -1.0, -1.0, -1.0, -1.0, -1.0, -1.0, -1.0, -1.0, -1.0, -1.0,999.0,999.0,999.0,999.0,999.0,999.0,999.0};
	private int turn;
	private int currentTurn;
	private int player;
	private Player[] playerList;
	private TileMap tileMap;
	private UnitMap unitTileMap;
	private BuildingMap buildingTileMap;
	private ArrayList<Building> buildingList;
	private double[][] tileMapCost;
	private double[][] playerTileMapCost;
	private Unit selectedUnit;
	private TileInterface selectedTile;
	private Building selectedBuilding;
	private int selectedUnitID;
	private int selectedBuildingID;
	private boolean unitSelected = false;
	private boolean tileSelected = false;
	private boolean buildingSelected = false;
	private double[][] selectedUnitTileMapCost;
	private double[][] selectedMoveOptions;
	private Location selectedLocation;
	private int selectedAttackID;
	private Attack selectedAttack;
	private int selectedSpecialID;
	private Special selectedSpecial;
	private Player currentPlayer;
	private ArrayList<Unit> currentPlayerUnitList;
	private ArrayList<EffectField> effectFieldList;
	private boolean attacking;
	private ArrayList<Unit> selectedAttackTarget;
	//The settings of the game
	//0: CommandEffects (false = 0, true = 1)
	//1: Specials (false = 0, true = 1)
	//2: FriendlyFire (false = 0, true = 1)
	//3: ResourceCost (false = 0, true = 1)
	//4: ChangeActiveCommander (still can move and attack = 0, can only move = 1, cannot move or attack = 2)
	//5: FogMap (false = 0, true = 1)
	private double[] gameSettingList;
	
	//The way that a side can win/lose
	//0  = Active Commander Defeated
	//1  = <x Commanders Left
	//2  = <x Special Unit Left (Not Including Commanders)
	//3  = <x Normal Units Left (Not Including Special Units and Commanders)
	//4  = <x Special Unit Left (Including Commanders)
	//5  = <x Normal Units Left (Not Including Commanders)
	//6  = <x Normal Units Left (Not Including Special Units)
	//7  = <x Normal Units Left (Including Both Special Units and Commanders)
	//8  = <x Strong Holds Left
	//9  = <x Barracks Left
	//10 = <x Reactors/Generators Left
	//11 = <x Factories Left
	//12 = <x Fortresses Left
	//13 = >x Commanders In Area
	//14 = >x Special Units In Area (Not Including Commanders)
	//15 = >x Normal Units In Area (Not Including SpecialUnits and Commanders)
	//16 = >x Special Units In Area (Including Commanders)
	//17 = >x Normal Units In Area (Not Including Commanders)
	//18 = >x Normal Units In Area (Not Including SpecialUnits)
	//19 = >x Normal Units In Area (Including Both SpecialUnits and Commanders)
	private double[] winConditionList;
	private boolean won;
	private int[] winOrder;
	private int defeated;
	private int[][] visionRangeMap;
	
	public static int getNumberOfWinConditions()
	{
		return numberOfWinConditions;
	}
	
	public static double[] getDefaultSettingList()
	{
		return defaultSettingList;
	}
	
	public Game()
	{
		turn = 0;
		player = 2;
		currentTurn = 0;
		playerList = new Player[2];
		won = false;
		winOrder = new int[playerList.length];
		defeated = playerList.length + 1;
		winConditionList = noWinCondition.clone();
		gameSettingList = defaultSettingList.clone();
	}
	
	public Game(TileMap tileMap)
	{
		turn = 0;
		player = 2;
		playerList = new Player[2];
		currentTurn = 0;
		this.tileMap = tileMap;
		this.tileMapCost = tileMap.movementCostMap();
		int i = 0;
		int j = 0;
		for (i = 0; i < tileMap.getMap().length; i++)
		{
			for (j = 0; j < tileMap.getMap()[0].length; j++)
			{
				tileMapCost[i][j] = tileMap.getMap()[i][j].getMovementCost();
			}
		}
		currentPlayer = playerList[0];
		currentPlayerUnitList = currentPlayer.getUnitList();
		updateUnitIDs();
		effectFieldList = new ArrayList<EffectField>();
		selectedLocation = new Location();
		won = false;
		winOrder = new int[playerList.length];
		defeated = playerList.length + 1;
		winConditionList = noWinCondition.clone();
		gameSettingList = defaultSettingList.clone();
		for(i = 0; i < playerList.length; i++)
		{
			playerList[i].addCommandEffect(playerList);
		}
		buildingTileMap = new BuildingMap(tileMap);
		buildingList = buildingTileMap.getBuildingList();
		setUpVisionRangeMap();
	}
	
	public Game(ArrayList<ArrayList<Unit>> unitList, TileMap tileMap)
	{
		turn = 0;
		player = unitList.size();
		playerList = new Player[player];
		currentTurn = 0;
		this.tileMap = tileMap;
		this.tileMapCost = tileMap.movementCostMap();
		int i = 0;
		int j = 0;
		for (i = 0; i < player; i++)
		{
			playerList[i] = new Player(unitList.get(i), i, "", tileMap);
		}
		playerTileMapCost = ArrayHandler.cloneMatrix(tileMapCost);
		unitTileMap = new UnitMap(unitList, tileMap.getMap().length, tileMap.getMap()[0].length);
		int[] unitLocation;
		ArrayList<Unit> playerUnits;
		for (i = 0; i < unitList.size(); i++)
		{
			if (i == currentTurn)
			{
				i++;
			}
			if(i >= unitList.size())
			{
				break;
			}
			playerUnits = unitList.get(i);
			for (j = 0; j < playerUnits.size(); j++)
			{
				unitLocation = playerUnits.get(j).getLocation().getLocation().clone();
				playerTileMapCost[unitLocation[0]][unitLocation[1]] = 65535;
			}
		}
		unitTileMap.updateMap(unitList);
		currentPlayer = playerList[0];
		currentPlayerUnitList = currentPlayer.getUnitList();
		updateUnitIDs();
		effectFieldList = new ArrayList<EffectField>();
		selectedLocation = new Location();
		won = false;
		winOrder = new int[playerList.length];
		defeated = playerList.length + 1;
		winConditionList = noWinCondition.clone();
		gameSettingList = defaultSettingList.clone();
		for(i = 0; i < playerList.length; i++)
		{
			playerList[i].addCommandEffect(playerList);
		}
		buildingTileMap = new BuildingMap(tileMap);
		buildingList = buildingTileMap.getBuildingList();
		setUpVisionRangeMap();
	}
	
	public Game(Player[] playerList, TileMap tileMap)
	{
		turn = 0;
		player = playerList.length;
		currentTurn = 0;
		this.tileMap = tileMap;
		this.tileMapCost = tileMap.movementCostMap();
		int i = 0;
		int j = 0;
		this.playerList = playerList;
		playerTileMapCost = ArrayHandler.cloneMatrix(tileMapCost);
		unitTileMap = new UnitMap(playerList, tileMap.getMap().length, tileMap.getMap()[0].length);
		int[] unitLocation;
		ArrayList<Unit> playerUnits;
		for (i = 0; i < playerList.length; i++)
		{
			if (i == currentTurn)
			{
				i++;
			}
			if(i >= playerList.length)
			{
				break;
			}
			playerUnits = playerList[i].getUnitList();
			for (j = 0; j < playerUnits.size(); j++)
			{
				unitLocation = playerUnits.get(j).getLocation().getLocation().clone();
				playerTileMapCost[unitLocation[0]][unitLocation[1]] = 65535;
			}
		}
		unitTileMap.updateMap(playerList);
		currentPlayer = playerList[0];
		currentPlayerUnitList = currentPlayer.getUnitList();
		updateUnitIDs();
		effectFieldList = new ArrayList<EffectField>();
		selectedLocation = new Location();
		won = false;
		winOrder = new int[playerList.length];
		defeated = playerList.length + 1;
		winConditionList = noWinCondition.clone();
		gameSettingList = defaultSettingList.clone();
		for(i = 0; i < playerList.length; i++)
		{
			playerList[i].addCommandEffect(playerList);
		}
		buildingTileMap = new BuildingMap(tileMap);
		buildingList = buildingTileMap.getBuildingList();
		setUpVisionRangeMap();
	}
	
	public Game(Player[] playerList, TileMap tileMap, double[] settingList)
	{
		turn = 0;
		player = playerList.length;
		currentTurn = 0;
		this.tileMap = tileMap;
		this.tileMapCost = tileMap.movementCostMap();
		int i = 0;
		int j = 0;
		this.playerList = playerList;
		playerTileMapCost = ArrayHandler.cloneMatrix(tileMapCost);
		unitTileMap = new UnitMap(playerList, tileMap.getMap().length, tileMap.getMap()[0].length);
		int[] unitLocation;
		ArrayList<Unit> playerUnits;
		for (i = 0; i < playerList.length; i++)
		{
			if (i == currentTurn)
			{
				i++;
			}
			if(i >= playerList.length)
			{
				break;
			}
			playerUnits = playerList[i].getUnitList();
			for (j = 0; j < playerUnits.size(); j++)
			{
				unitLocation = playerUnits.get(j).getLocation().getLocation().clone();
				playerTileMapCost[unitLocation[0]][unitLocation[1]] = 65535;
			}
		}
		unitTileMap.updateMap(playerList);
		currentPlayer = playerList[0];
		currentPlayerUnitList = currentPlayer.getUnitList();
		updateUnitIDs();
		effectFieldList = new ArrayList<EffectField>();
		selectedLocation = new Location();
		won = false;
		winOrder = new int[playerList.length];
		defeated = playerList.length + 1;
		winConditionList = noWinCondition.clone();
		gameSettingList = settingList.clone();
		for(i = 0; i < playerList.length; i++)
		{
			playerList[i].addCommandEffect(playerList);
		}
		buildingTileMap = new BuildingMap(tileMap);
		buildingList = buildingTileMap.getBuildingList();
		setUpVisionRangeMap();
	}
	
	public Game(ArrayList<ArrayList<Unit>> unitList, TileMap tileMap, String[] playerNames)
	{
		turn = 0;
		player = unitList.size();
		playerList = new Player[player];
		currentTurn = 0;
		this.tileMap = tileMap;
		this.tileMapCost = tileMap.movementCostMap();
		int i = 0;
		playerTileMapCost = ArrayHandler.cloneMatrix(tileMapCost);
		for (i = 0; i < player; i++)
		{
			playerList[i] = new Player (unitList.get(i), i, playerNames[i], tileMap);
		}
		unitTileMap = new UnitMap(unitList, tileMap.getMap().length, tileMap.getMap()[0].length);
		unitTileMap.updateMap(unitList);
		updatePlayerTileMapCost();
		effectFieldList = new ArrayList<EffectField>();
		selectedLocation = new Location();
		won = false;
		winOrder = new int[playerList.length];
		defeated = playerList.length + 1;
		winConditionList = noWinCondition.clone();
		gameSettingList = defaultSettingList.clone();
		for(i = 0; i < playerList.length; i++)
		{
			playerList[i].addCommandEffect(playerList);
		}
		buildingTileMap = new BuildingMap(tileMap);
		buildingList = buildingTileMap.getBuildingList();
		setUpVisionRangeMap();
	}
	
	public double[] getWinConditionList()
	{
		return winConditionList;
	}
	
	public double getWinConditionList(int i)
	{
		return winConditionList[i];
	}
	
	public double[] getGameSettingList()
	{
		return gameSettingList;
	}
	
	public double getGameSettingList(int i)
	{
		return gameSettingList[i];
	}
	
	public boolean getTileSelected()
	{
		return tileSelected;
	}
	
	public ArrayList<Unit> getSelectedAttackTarget()
	{
		return selectedAttackTarget;
	}
	
	public boolean getAttacking()
	{
		return attacking;
	}
	
	public ArrayList<EffectField> getEffectFieldList()
	{
		return effectFieldList;
	}
	
	public int getTurn()
	{
		return turn;
	}
	
	public Location getSelectedLocation()
	{
		return selectedLocation;
	}
	
	public int getSelectedY()
	{
		return selectedLocation.getY();
	}
	
	public int getSelectedX()
	{
		return selectedLocation.getX();
	}
	
	public int getPlayer()
	{
		return player;
	}
	
	public UnitMap getUnitTileMap()
	{
		return unitTileMap;
	}
	
	public Attack getSelectedAttack()
	{
		return selectedAttack;
	}
	
	public Unit getSelectedUnit()
	{
		return selectedUnit;
	}
	
	public int getCurrentTurn()
	{
		return currentTurn;
	}
	
	public TileMap getTileMap()
	{
		return tileMap;
	}
	
	public TileInterface getSelectedTile()
	{
		return selectedTile;
	}
	
	public double[][] getPlayerTileMapCost()
	{
		return playerTileMapCost;
	}
	
	public double[][] getTileMapCost()
	{
		return tileMapCost;
	}
	
	public Player[] getPlayerList()
	{
		return playerList;
	}
	
	public Player getPlayerList(int i)
	{
		return playerList[i];
	}
	
	public Player getCurrentPlayer()
	{
		return currentPlayer;
	}
	
	public boolean getUnitSelected()
	{
		return unitSelected;
	}
	
	public Special getSelectedSpecial()
	{
		return selectedSpecial;
	}
	
	public int getSelectedSpecialID()
	{
		return selectedSpecialID;
	}
	
	public double[][] getSelectedUnitTileMapCost()
	{
		return selectedUnitTileMapCost;
	}
	
	public boolean getWon()
	{
		return won;
	}
	
	public int[] getWinOrder()
	{
		return winOrder;
	}
	
	public BuildingMap getBuildingTileMap()
	{
		return buildingTileMap;
	}
	
	public Building getSelectedBuilding()
	{
		return selectedBuilding;
	}
	
	public boolean getBuildingSelected()
	{
		return buildingSelected;
	}
	
	public int getSelectedBuildingID()
	{
		return selectedBuildingID;
	}
	
	public ArrayList<Building> getBuildingList()
	{
		return buildingList;
	}
	
	public int[][] getVisionRangeMap()
	{
		return visionRangeMap;
	}
	
	public int[] getVisionRangeMap(int i)
	{
		return visionRangeMap[i];
	}
	
	public int getVisionRangeMap(int i, int j)
	{
		return visionRangeMap[i][j];
	}
	
	public void setWinConditionList(int i, double value)
	{
		//The way that a side can win/lose
		//0  = Active Commander Defeated
		//1  = <x Commanders Left
		//2  = <x Special Unit Left (Not Including Commanders)
		//3  = <x Normal Units Left (Not Including Special Units and Commanders)
		//4  = <x Special Unit Left (Including Commanders)
		//5  = <x Normal Units Left (Not Including Commanders)
		//6  = <x Normal Units Left (Not Including Special Units)
		//7  = <x Normal Units Left (Including Both Special Units and Commanders)
		//8  = <x Strong Holds Left
		//9  = <x Barracks Left
		//10 = <x Reactors/Generators Left
		//11 = <x Factories Left
		//12 = <x Fortresses Left
		//13 = >x Commanders In Area
		//14 = >x Special Units In Area (Not Including Commanders)
		//15 = >x Normal Units In Area (Not Including SpecialUnits and Commanders)
		//16 = >x Special Units In Area (Including Commanders)
		//17 = >x Normal Units In Area (Not Including Commanders)
		//18 = >x Normal Units In Area (Not Including SpecialUnits)
		//19 = >x Normal Units In Area (Including Both SpecialUnits and Commanders)
		winConditionList[i] = value;
	}

	//The settings of the game
	//0: CommandEffects (false = 0, true = 1)
	//1: Specials (false = 0, true = 1)
	//2: FriendlyFire (false = 0, true = 1)
	//3: ResourceCost (false = 0, true = 1)
	//4: ChangeActiveCommander (still can move and attack = 0, can only move = 1, cannot move or attack = 2)
	//5: FogMap (false = 0, true = 1)
	public void setUpVisionRangeMap()
	{
		visionRangeMap = new int[tileMap.getHeight()][tileMap.getWidth()];
		int i;
		int j;
		int k;
		Unit unit;
		Location targetting;
		int intRange;
		if(gameSettingList[5] == 1.0)
		{
			for(k = 0; k < currentPlayer.getUnitList().size(); k++)
			{
				unit = currentPlayer.getUnitList().get(k);
				targetting = unit.getLocation().clone();
				intRange = ((Double) unit.getVisionRange()).intValue();
				for (i = 0; i < intRange + 1; i++)
				{
					for (j = 0; j < intRange + 1; j++)
					{
						targetting.translate(i - intRange + j, i - j);
						if (tileMap.inMap(targetting))
						{
							visionRangeMap[targetting.getY()][targetting.getX()] ++;
						}
						targetting.translate(-i + intRange - j, -i + j);
					}
				}
				for (i = 0; i < intRange; i++)
				{
					for (j = 0; j < intRange; j++)
					{
						targetting.translate(i - intRange + j + 1, i - j);
						if (tileMap.inMap(targetting))
						{
							visionRangeMap[targetting.getY()][targetting.getX()]++;
						}
						targetting.translate(-i + intRange - j - 1, -i + j);
					}
				}
			}
			for(k = 0; k < currentPlayer.getBuildingList().size(); k++)
			{
				unit = currentPlayer.getBuildingList().get(k);
				targetting = unit.getLocation().clone();
				intRange = ((Double) unit.getVisionRange()).intValue();
				for (i = 0; i < intRange + 1; i++)
				{
					for (j = 0; j < intRange + 1; j++)
					{
						targetting.translate(i - intRange + j, i - j);
						if (tileMap.inMap(targetting))
						{
							visionRangeMap[targetting.getY()][targetting.getX()] ++;
						}
						targetting.translate(-i + intRange - j, -i + j);
					}
				}
				for (i = 0; i < intRange; i++)
				{
					for (j = 0; j < intRange; j++)
					{
						targetting.translate(i - intRange + j + 1, i - j);
						if (tileMap.inMap(targetting))
						{
							visionRangeMap[targetting.getY()][targetting.getX()]++;
						}
						targetting.translate(-i + intRange - j - 1, -i + j);
					}
				}
			}
		}
		else
		{
			for(i = 0; i < visionRangeMap.length; i++)
			{
				for(j = 0; j < visionRangeMap[0].length; j++)
				{
					visionRangeMap[i][j] = 1;
				}
			}
		}
	}
	
	public void updateVisionRangeMap(Unit unit)
	{
		if(gameSettingList[5] == 1.0)
		{
			Location targetting = unit.getLocation().clone();
			int intRange = ((Double) unit.getVisionRange()).intValue();
			int i;
			int j;
			for (i = 0; i < intRange + 1; i++)
			{
				for (j = 0; j < intRange + 1; j++)
				{
					targetting.translate(i - intRange + j, i - j);
					if (tileMap.inMap(targetting))
					{
						visionRangeMap[targetting.getY()][targetting.getX()]++;
					}
					targetting.translate(-i + intRange - j, -i + j);
				}
			}
			for (i = 0; i < intRange; i++)
			{
				for (j = 0; j < intRange; j++)
				{
					targetting.translate(i - intRange + j + 1, i - j);
					if (tileMap.inMap(targetting))
					{
						visionRangeMap[targetting.getY()][targetting.getX()]++;
					}
					targetting.translate(-i + intRange - j - 1, -i + j);
				}
			}
		}
	}
	
	public void updateVisionRangeMap(Unit unit, Location oldLocation)
	{
		if(gameSettingList[5] == 1.0)
		{
			Location targetting = unit.getLocation().clone();
			int intRange = ((Double) unit.getVisionRange()).intValue();
			int i;
			int j;
			for (i = 0; i < intRange + 1; i++)
			{
				for (j = 0; j < intRange + 1; j++)
				{
					targetting.translate(i - intRange + j, i - j);
					if (tileMap.inMap(targetting))
					{
						visionRangeMap[targetting.getY()][targetting.getX()]++;
					}
					targetting.translate(-i + intRange - j, -i + j);
				}
			}
			for (i = 0; i < intRange; i++)
			{
				for (j = 0; j < intRange; j++)
				{
					targetting.translate(i - intRange + j + 1, i - j);
					if (tileMap.inMap(targetting))
					{
						visionRangeMap[targetting.getY()][targetting.getX()]++;
					}
					targetting.translate(-i + intRange - j - 1, -i + j);
				}
			}
			
			targetting.set(oldLocation);
			for (i = 0; i < intRange + 1; i++)
			{
				for (j = 0; j < intRange + 1; j++)
				{
					targetting.translate(i - intRange + j, i - j);
					if (tileMap.inMap(targetting))
					{
						visionRangeMap[targetting.getY()][targetting.getX()]--;
					}
					targetting.translate(-i + intRange - j, -i + j);
				}
			}
			for (i = 0; i < intRange; i++)
			{
				for (j = 0; j < intRange; j++)
				{
					targetting.translate(i - intRange + j + 1, i - j);
					if (tileMap.inMap(targetting))
					{
						visionRangeMap[targetting.getY()][targetting.getX()]--;
					}
					targetting.translate(-i + intRange - j - 1, -i + j);
				}
			}
		}
		
	}
	
	public void updateVisionRangeMap(Unit unit, int[] oldLocation)
	{
		if(gameSettingList[5] == 1.0)
		{
			Location targetting = unit.getLocation().clone();
			int intRange = ((Double) unit.getVisionRange()).intValue();
			int i;
			int j;
			for (i = 0; i < intRange + 1; i++)
			{
				for (j = 0; j < intRange + 1; j++)
				{
					targetting.translate(i - intRange + j, i - j);
					if (tileMap.inMap(targetting))
					{
						visionRangeMap[targetting.getY()][targetting.getX()]++;
					}
					targetting.translate(-i + intRange - j, -i + j);
				}
			}
			for (i = 0; i < intRange; i++)
			{
				for (j = 0; j < intRange; j++)
				{
					targetting.translate(i - intRange + j + 1, i - j);
					if (tileMap.inMap(targetting))
					{
						visionRangeMap[targetting.getY()][targetting.getX()]++;
					}
					targetting.translate(-i + intRange - j - 1, -i + j);
				}
			}
			
			targetting.set(oldLocation);
			for (i = 0; i < intRange + 1; i++)
			{
				for (j = 0; j < intRange + 1; j++)
				{
					targetting.translate(i - intRange + j, i - j);
					if (tileMap.inMap(targetting))
					{
						visionRangeMap[targetting.getY()][targetting.getX()]--;
					}
					targetting.translate(-i + intRange - j, -i + j);
				}
			}
			for (i = 0; i < intRange; i++)
			{
				for (j = 0; j < intRange; j++)
				{
					targetting.translate(i - intRange + j + 1, i - j);
					if (tileMap.inMap(targetting))
					{
						visionRangeMap[targetting.getY()][targetting.getX()]--;
					}
					targetting.translate(-i + intRange - j - 1, -i + j);
				}
			}
		}
		
	}
	
	public void deselect()
	{
		selectedUnit = null;
		unitSelected = false;
		selectedTile = null;
		tileSelected = false;
		selectedBuilding = null;
		buildingSelected = false;
	}
	
	public boolean selectedBuildingCanMake()
	{
		if(selectedBuilding.getBuildingType() == 4)
		{
			return selectedBuilding.freeToBuild(unitTileMap) && selectedBuilding.getBuildingData(2) == 0.0;
		}
		return false;
	}
	
	public boolean selectedBuildingCanMake(UnitData unit)
	{
		if(selectedBuilding.getBuildingType() == 4)
		{
			return selectedBuilding.freeToBuild(unitTileMap) && selectedBuilding.getBuildingData(2) == 0.0 && selectedBuilding.getBuildingData(0) == unit.getUnitMake() && currentPlayer.getResource() >= unit.getResourceCost();
		}
		return false;
	}
	
	public boolean selectedBuildingCanMake(int unitID)
	{
		if(unitID >= 0 && unitID < currentPlayer.getAllowedUnitList().size())
		{
			if(selectedBuilding.getBuildingType() == 4)
			{
				return selectedBuilding.freeToBuild(unitTileMap) && selectedBuilding.getBuildingData(2) == 0.0 && 
						selectedBuilding.getBuildingData(0) == currentPlayer.getAllowedUnitList(unitID).getUnitMake() && 
						currentPlayer.getResource() >= currentPlayer.getAllowedUnitList(unitID).getResourceCost();
			}
		}
		return false;
	}
	
	public void selectedBuildingStartMake(UnitData unit)
	{
		selectedBuilding.setMakingUnit(unit);
		currentPlayer.useResource(unit.getResourceCost());
		if(selectedBuilding.getBuildingData(2) == 2.0)
		{
			updateVisionRangeMap(selectedBuilding.getMaking());
		}
		currentPlayer.addFinishedUnit(selectedBuilding, unitTileMap);
		deselect();
	}

	
	public void selectedBuildingStartMake(int unitID)
	{
		selectedBuilding.setMakingUnit(currentPlayer.getAllowedUnitList(unitID));
		currentPlayer.useResource(currentPlayer.getAllowedUnitList(unitID).getResourceCost());
		currentPlayer.addFinishedUnit(selectedBuilding, unitTileMap);
		deselect();
	}
	public void select(int[] location)
	{

		if(visionRangeMap[location[0]][location[1]] > 0)
		{
			selectedLocation.set(location);
			tileSelected = true;
			selectedTile = tileMap.getMap(location[0], location[1]);
			Unit selected = unitTileMap.getMap(location[0],location[1]);
			if (selected != null)
			{
				if (selected.getPlayer() == currentTurn)
				{
					selectedUnit = selected;
					selectedUnitID = selectedUnit.getUnitID();
					unitSelected = true;
				}
				else
				{
					selectedUnit = selected;
					selectedUnitID = -1;
					unitSelected = true;
				}
			}
			Building selectedB;
			selectedB = buildingTileMap.getMap(location[0], location[1]);
			if (selectedB != null)
			{
				if (selectedB.getPlayer() == currentTurn)
				{
					selectedBuilding = selectedB;
					selectedBuildingID = selectedB.getUnitID();
					buildingSelected = true;
				}
				else
				{
					selectedBuilding = selectedB;
					selectedBuildingID = -1;
					buildingSelected = true;
				}
			}
		}
	}
	
	public void select(int y, int x)
	{
		if(visionRangeMap[y][x] > 0)
		{
			selectedLocation.set(y, x);
			tileSelected = true;
			selectedTile = tileMap.getMap(y, x);
			Unit selected = unitTileMap.getMap(y, x);
			if (selected != null)
			{
				if (selected.getPlayer() == currentTurn)
				{
					selectedUnit = selected;
					selectedUnitID = selectedUnit.getUnitID();
					unitSelected = true;
				}
				else
				{
					selectedUnit = selected;
					selectedUnitID = -1;
					unitSelected = true;
				}
			}
			Building selectedB;
			selectedB = buildingTileMap.getMap(y, x);
			if (selectedB != null)
			{
				if (selectedB.getPlayer() == currentTurn)
				{
					selectedBuilding = selectedB;
					selectedBuildingID = selectedB.getUnitID();
					buildingSelected = true;
				}
				else
				{
					selectedBuilding = selectedB;
					selectedBuildingID = -1;
					buildingSelected = true;
				}
			}
		}
	}
	
	public void captureBuilding(Location location)
	{
		Building captured = buildingTileMap.getMap(location);
		{
			if(captured != null && visionRangeMap[location.getY()][location.getX()] > 0)
			{
				currentPlayer.captureBuilding(captured, selectedUnit);
				updateVisionRangeMap(captured);
				selectedUnit.endTurn();
				deselect();
			}
		}
	}
	
	public boolean validCapture(int[] location)
	{
		if(selectedUnit.getLocation().calculateDistance(location) <= 1)
		{
			Building capture = buildingTileMap.getMap(location[0], location[1]);
			if(capture != null && visionRangeMap[location[0]][location[1]] > 0)
			{
				if(capture.getPlayer() == -1)
				{
					return true;
				}
			}
		}
		return false;
	}
	
	public boolean validCapture(int[] location, int[] unitLocation)
	{
		if(Location.calculateDistance(location, unitLocation) <= 1)
		{
			Building capture = buildingTileMap.getMap(location[0], location[1]);
			if(capture != null && visionRangeMap[location[0]][location[1]] > 0)
			{
				if(capture.getPlayer() == -1)
				{
					return true;
				}
			}
		}
		return false;
	}
	
	public void captureBuilding(int[] location)
	{
		Building captured = buildingTileMap.getMap(location[0], location[1]);
		{
			if(captured != null && visionRangeMap[location[0]][location[1]] > 0)
			{
				currentPlayer.captureBuilding(captured, selectedUnit);
				updateVisionRangeMap(captured);
				selectedUnit.endTurn();
				deselect();
			}
		}
	}
	
	public void captureBuilding(int y, int x)
	{
		Building captured = buildingTileMap.getMap(y, x);
		{
			if(captured != null && visionRangeMap[y][x] > 0)
			{
				currentPlayer.captureBuilding(captured, selectedUnit);
				updateVisionRangeMap(captured);
				selectedUnit.endTurn();
				deselect();
			}
		}
	}
	
	public void selectAttack(int attackID)
	{
		selectedAttackID = attackID;
		selectedAttack = selectedUnit.getAttack(attackID);
	}
	
	public boolean selectValidAttack(int attackID)
	{
		selectedAttack = selectedUnit.getAttack(attackID);
		selectedAttackID = attackID;
		if (selectedAttack.getStaminaCost() * selectedUnit.getBoostList(5) + selectedUnit.getBoostList(4) <= selectedUnit.getStamina())
		{
			return true;
		}
		else
		{
			return false;
		}
	}
	
	public void selectSpecial(int specialID)
	{
		selectedSpecial = selectedUnit.getSpecialUnit().getSpecialList(specialID);
		selectedSpecialID = specialID;
	}
	
	public boolean selectValidSpecial(int specialID)
	{
		if(selectedUnit.isSpecial())
		{
			selectedSpecial = selectedUnit.getSpecialUnit().getSpecialList(specialID);
			if (selectedSpecial.getStaminaCost() * selectedUnit.getBoostList(21) + selectedUnit.getBoostList(20) <= selectedUnit.getStamina())
			{
				if (selectedSpecial.getSpecialPointCost() * selectedUnit.getBoostList(19) + selectedUnit.getBoostList(18) <= selectedUnit.getSpecialUnit().getSpecialPoint())
				{
					return true;
				}
			}
		}
		return false;
	}
	
	public void selectUnit(int unitID)
	{
		selectedUnit = currentPlayerUnitList.get(unitID);
		selectedUnitID = unitID;
		unitSelected = true;
	}
	
	public void selectBuilding(int buildingID)
	{
		selectedBuilding = currentPlayer.getBuildingList(buildingID);
		selectedBuildingID = buildingID;
		buildingSelected = true;
	}
	
	@Deprecated
	public void selectedUnitAttack(Unit target, int attackNumber)
	{
		selectedUnit.attack(target, attackNumber);
		selectedAttack = selectedUnit.getAttack(attackNumber);
		ArrayList<EffectField> effectFieldAddList = selectedUnit.applyAttackEffectFields(target, selectedAttack, unitTileMap, buildingTileMap);
		for (int i = 0; i < effectFieldAddList.size(); i++)
		{
			effectFieldList.add(effectFieldAddList.get(i));
		}
		if (selectedUnit.getHealthPoint() == 0)
		{
			removeUnit(selectedUnit);
			selectedUnit.inactiveEffectFieldCentredList();
			selectedUnit.clearEffectFieldCentredlist();
		}
		if (target.getHealthPoint() == 0)
		{
			target.inactiveEffectFieldCentredList();
			target.clearEffectFieldCentredlist();
			removeUnit(target);
		}
		this.updateEffectFieldList();
		deselect();
		checkIfAnyDefeated();
	}
	
	public void endAttack()
	{
		attacking = false;
	}
	
	public void setActiveCommander(int i)
	{
		if(!currentPlayer.getActivelyCommanded())
		{
			currentPlayer.setActiveCommander(i, playerList);
			currentPlayer.getActiveCommander().endTurn();
		}
		else if(currentPlayer.getCommanderList(i) != currentPlayer.getActiveCommander() && !currentPlayer.getCommanderList(i).getMoved())
		{
			currentPlayer.changeActiveCommander(i, playerList);
			currentPlayer.getActiveCommander().endTurn();
		}
		checkIfAnyDefeated();
	}
	
	public boolean setActiveCommanderReturn(int i)
	{
		if(!currentPlayer.getActivelyCommanded())
		{
			currentPlayer.setActiveCommander(i, playerList);
			currentPlayer.getActiveCommander().endTurn();
			checkIfAnyDefeated();
			return true;
		}
		else if(currentPlayer.getCommanderList(i) != currentPlayer.getActiveCommander() && !currentPlayer.getCommanderList(i).getMoved())
		{
			currentPlayer.changeActiveCommander(i, playerList);
			currentPlayer.getActiveCommander().endTurn();
			checkIfAnyDefeated();
			return true;
		}
		checkIfAnyDefeated();
		return false;
	}
	
	@Deprecated
	public void selectedAttack(Unit target)
	{
		selectedUnit.attack(target, selectedAttackID);
		selectedAttack = selectedUnit.getAttack(selectedAttackID);
		ArrayList<EffectField> effectFieldAddList = selectedUnit.applyAttackEffectFields(target, selectedAttack, unitTileMap, buildingTileMap);
		for (int i = 0; i < effectFieldAddList.size(); i++)
		{
			effectFieldList.add(effectFieldAddList.get(i));
		}
		if (selectedUnit.getHealthPoint() == 0)
		{
			removeUnit(selectedUnit);
			selectedUnit.inactiveEffectFieldCentredList();
			selectedUnit.clearEffectFieldCentredlist();
		}
		if (target.getHealthPoint() == 0)
		{
			target.inactiveEffectFieldCentredList();
			target.clearEffectFieldCentredlist();
			removeUnit(target);
		}
		this.updateEffectFieldList();
		deselect();
		checkIfAnyDefeated();
	}
	
	@Deprecated
	public void attack(Unit target, Attack attack, Location location)
	{
		if (attack.getTarget() != 3)
		{
			selectedUnit.attack(unitTileMap, attack, location);
			ArrayList<EffectField> effectFieldAddList = selectedUnit.applyAttackEffectFields(target, attack, unitTileMap, buildingTileMap);
			for (int i = 0; i < effectFieldAddList.size(); i++)
			{
				effectFieldList.add(effectFieldAddList.get(i));
			}
			if (selectedUnit.getHealthPoint() == 0)
			{
				removeUnit(selectedUnit);
				selectedUnit.inactiveEffectFieldCentredList();
				selectedUnit.clearEffectFieldCentredlist();
			}
			if (target.getHealthPoint() == 0)
			{
				target.inactiveEffectFieldCentredList();
				target.clearEffectFieldCentredlist();
				removeUnit(target);
			}
			this.updateEffectFieldList();
		}
		else
		{
			ArrayList<EffectField> effectFieldAddList = selectedUnit.applyAttackEffectFields(location, attack, unitTileMap, buildingTileMap);
			for (int i = 0; i < effectFieldAddList.size(); i++)
			{
				effectFieldList.add(effectFieldAddList.get(i));
			}
			this.updateEffectFieldList();
		}
		deselect();
		checkIfAnyDefeated();
	}
	
	public void attack(Attack attack, Location location)
	{
		if (attack.getTarget() != 3)
		{
			if(!attacking && !selectedUnit.getAttacked())
			{
				selectedUnit.addAttackNumber(attack);
				attacking = true;
			}
			ArrayList<Unit> targetList = attack.getUnitsDamaged(location, unitTileMap, buildingTileMap);
			selectedUnit.attack(targetList, attack, location);
			ArrayList<EffectField> effectFieldAddList = selectedUnit.applyAttackEffectFields(targetList, attack, unitTileMap, buildingTileMap);
			for (int i = 0; i < effectFieldAddList.size(); i++)
			{
				effectFieldList.add(effectFieldAddList.get(i));
			}
			if (selectedUnit.getHealthPoint() == 0)
			{
				removeUnit(selectedUnit);
				selectedUnit.inactiveEffectFieldCentredList();
				selectedUnit.clearEffectFieldCentredlist();
			}
			for(int i = 0; i < targetList.size(); i++)
			{
				if (targetList.get(i).getHealthPoint() == 0)
				{
					targetList.get(i).inactiveEffectFieldCentredList();
					targetList.get(i).clearEffectFieldCentredlist();
					removeUnit(targetList.get(i));
				}
			}
			this.updateEffectFieldList();
			if(selectedUnit.getAttacked())
			{
				attacking = false;
			}
		}
		else
		{
			System.out.println("Target 3");
			if(!attacking && !selectedUnit.getAttacked())
			{
				selectedUnit.addAttackNumber(attack);
				attacking = true;
				System.out.println("Attack Number Added");
			}
			selectedUnit.attack(selectedAttack);
			ArrayList<EffectField> effectFieldAddList = selectedUnit.applyAttackEffectFields(location, attack, unitTileMap, buildingTileMap);
			for (int i = 0; i < effectFieldAddList.size(); i++)
			{
				effectFieldList.add(effectFieldAddList.get(i));
			}
			this.updateEffectFieldList();
			System.out.println("selectedUnit.getAttackNumber(): " + selectedUnit.getAttackNumber());
			System.out.println("selectedUnit.getAttacked(): " + selectedUnit.getAttacked());
			System.out.println("attacking" + attacking);
			if(selectedUnit.getAttacked())
			{
				System.out.println("stop attack");
				attacking = false;
			}
		}
		if(!attacking)
		{
			deselect();
		}
		checkIfAnyDefeated();
	}
	
	public void attack(Location location)
	{
		this.attack(selectedAttack, location);
	}
	
	public void special(Location location, double[] entryData)
	{
		selectedUnit.getSpecialUnit().special(selectedSpecial, location, this, entryData);
		checkIfAnyDefeated();
	}
	
	public boolean validTarget(int[] coordinate)
	{
		Location attackLocation = new Location(coordinate);
		if(selectedAttack.checkValidTarget(attackLocation) && visionRangeMap[coordinate[0]][coordinate[1]] > 0)
		{
			selectedAttackTarget = selectedAttack.getUnitsDamaged(attackLocation, unitTileMap, buildingTileMap);
			if (selectedAttackTarget.size() > 0)
			{
				return true;
			}
			else if (selectedAttack.getTarget() == 3)
			{
				return true;
			}
		}
		return false;
	}
	
	public boolean validTarget(int[] coordinate, Attack attack)
	{
		Location attackLocation = new Location(coordinate);
		if(attack.checkValidTarget(attackLocation) && visionRangeMap[coordinate[0]][coordinate[1]] > 0)
		{
			ArrayList<Unit> attackTarget = attack.getUnitsDamaged(attackLocation, unitTileMap, buildingTileMap);
			if (attackTarget.size() > 0)
			{
				return true;
			}
		}
		return false;
	}
	
	public boolean validTarget(int[] coordinate, Attack attack, int[] attackFrom)
	{
		Location attackLocation = new Location(coordinate);
		Location from = new Location(attackFrom);
		if(attack.checkValidTarget(attackLocation, from) && visionRangeMap[coordinate[0]][coordinate[1]] > 0)
		{
			ArrayList<Unit> attackTarget = attack.getUnitsDamaged(attackLocation, unitTileMap, buildingTileMap);
			if (attackTarget.size() > 0)
			{
				return true;
			}
		}
		return false;
	}
	
	public boolean validTarget(int y, int x, Attack attack, int yf, int xf)
	{
		Location attackLocation = new Location(y, x);
		Location from = new Location(yf, xf);
		if(attack.checkValidTarget(attackLocation, from) && visionRangeMap[y][x] > 0)
		{
			ArrayList<Unit> attackTarget = attack.getUnitsDamaged(attackLocation, unitTileMap, buildingTileMap);
			if (attackTarget.size() > 0)
			{
				return true;
			}
		}
		return false;
	}
	
	public ArrayList<Unit> getAttackTargetList(int[] coordinate, Attack attack)
	{
		Location attackLocation = new Location(coordinate);
		ArrayList<Unit> attackTarget = new ArrayList<Unit>();
		if(attack.checkInRange(attackLocation) && visionRangeMap[coordinate[0]][coordinate[1]] > 0)
		{
			attackTarget = attack.getUnitsDamaged(attackLocation, unitTileMap, buildingTileMap);
		}
		return attackTarget;
	}
	
	public void removeUnit (Unit remove)
	{
		if(!remove.isBuilding())
		{
			int removeUnitID = remove.getUnitID();
			playerList[remove.getPlayer()].removeUnit(removeUnitID, playerList);
			unitTileMap.updateMap(remove);
		}
		else
		{
			int removeUnitID = remove.getUnitID();
			playerList[remove.getBuilding().getLastPlayer()].removeBuilding(removeUnitID);
		}
	}
	
	public void updateUnitIDs()
	{
		for (int i = 0; i < currentPlayerUnitList.size(); i++)
		{
			currentPlayerUnitList.get(i).setUnitID(i);
		}
	}
	
	public void moveSelectedUnit(int[] location)
	{
		int[] selectedUnitOldLocation = selectedUnit.getLocation().getLocation();
		selectedTile = tileMap.getMap(location[0], location[1]);
		selectedUnit.move(location, selectedUnitTileMapCost[location[0]][location[1]], effectFieldList, unitTileMap);
		unitTileMap.updateMap(selectedUnit, selectedUnitOldLocation);
		updateVisionRangeMap(selectedUnit, selectedUnitOldLocation);
		checkIfAnyDefeated();
	}
	
	public void updateSelectedMovementRange()
	{
		selectedUnitTileMapCost = ArrayHandler.cloneMatrix(calculateUnitMovementRange(selectedUnitID));
	}
	
	public void updateSelectedMovementOptions()
	{
		selectedUnitTileMapCost = ArrayHandler.cloneMatrix(calculateUnitMovementRange(selectedUnit));
		selectedMoveOptions = ArrayHandler.cloneMatrix(selectedUnitTileMapCost);
		int i = 0;
		Unit unit;
		for(i = 0; i < currentPlayerUnitList.size(); i++)
		{
			if (i == selectedUnitID)
			{
				i++;
			}
			if (i >= currentPlayerUnitList.size())
			{
				break;
			}
			unit = currentPlayerUnitList.get(i);
			selectedMoveOptions[unit.getY()][unit.getX()] = 0.0;
		}
	}
	
	public void updatePlayerTileMapCost()
	{
		int i;
		int j;
		playerTileMapCost = ArrayHandler.cloneMatrix(tileMapCost);
		ArrayList<Unit> playerUnits;
		int[] unitLocation;
		for (i = 0; i < player; i++)
		{
			if (i == currentTurn)
			{
				i++;
			}
			if (i >= player)
			{
				break;
			}
			playerUnits = playerList[i].getUnitList();
			for (j = 0; j < playerUnits.size(); j++)
			{
				unitLocation = playerUnits.get(j).getLocation().getLocation().clone();
				playerTileMapCost[unitLocation[0]][unitLocation[1]] = 65535.0;
			}
		}
		for (i = 0; i < buildingList.size(); i++)
		{
			if(buildingList.get(i).getPlayer() != currentTurn)
			{
				unitLocation = buildingList.get(i).getLocation().getLocation().clone();
				playerTileMapCost[unitLocation[0]][unitLocation[1]] = 65535.0;
			}
		}
	}
	
	public boolean selectedUnitCheckValidMove(int[] location)
	{
		if (selectedMoveOptions[location[0]][location[1]] > 0.0 && selectedUnit.getLocation().calculateDistance(location)  * 5 * selectedUnit.getBoostList(7) + selectedUnit.getBoostList(6) <= selectedUnit.getStamina())
		{
			return true;
		}
		return false;
	}
	
	public String selectedUnitCheckValidMoveReason(int[] location)
	{
		if (selectedMoveOptions[location[0]][location[1]] > 0.0)
		{
			if (selectedUnit.getLocation().calculateDistance(location)  * 5 * selectedUnit.getBoostList(7) + selectedUnit.getBoostList(6)  <= selectedUnit.getStamina()) 
			{
				return "";
			}
			else
			{
				return "NOT ENOUGH STAMINA";
			}
		}
		else
		{
			return "OUT OF RANGE";
		}
	}
	
	public boolean selectedUnitUpdateValidMove(int[] location)
	{
		updateSelectedMovementOptions();
		return selectedUnitCheckValidMove(location);
	}
	
	public boolean selectedUnitUpdateValidMove(Location location)
	{
		updateSelectedMovementOptions();
		return selectedUnitCheckValidMove(location.getLocation());
	}
	
	public double[][] calculateSelectedMovementRange()
	{
		selectedUnitTileMapCost = ArrayHandler.cloneMatrix(calculateUnitMovementRange(selectedUnit));
		return selectedUnitTileMapCost;
	}
	
	public double[][] calculateUnitMovementRange(int unit)
	{
		//Maybe use recursive programming?? To replace the arrays
		
		Unit unitUsed = currentPlayerUnitList.get(unit);
		//tileMap.getMap().length * tileMap.getMap()[0].length
		double[][] unitSpace = new double[tileMap.getMap().length][tileMap.getMap()[0].length];
		ArrayList<int[]> tileToCheck = new ArrayList<int[]>();
		ArrayList<int[]> tileToCheckNext = new ArrayList<int[]>();
		int i = 0;
		int j = 0;
		int x;
		int y;
		for (i = 0; i < unitSpace.length; i++)
		{
			for (j = 0; j < unitSpace[0].length; j++)
			{
				unitSpace[i][j] = 0;
			}
		}
		tileToCheck.add(new int[]{unitUsed.getY(),unitUsed.getX()});
		unitSpace[unitUsed.getY()][unitUsed.getX()] = unitUsed.getDefactoMoveRange();
		double xyValue = 0;
		while (tileToCheck.size() > 0)
		{
			for(i = 0; i < tileToCheck.size(); i++)
			{
				x = tileToCheck.get(i)[0];
				y = tileToCheck.get(i)[1];
				if(x + 1 < unitSpace.length)
				{
					xyValue = unitSpace[x][y] - playerTileMapCost[x + 1][y];
					if (unitSpace[x + 1][y] < xyValue)
					{
						tileToCheckNext.add(new int[] {x + 1, y});
						unitSpace[x + 1][y] = xyValue;
					}
				}
				if(y + 1 < unitSpace[0].length)
				{
					xyValue = unitSpace[x][y] - playerTileMapCost[x][y + 1];
					if (unitSpace[x][y + 1] < xyValue)
					{
						tileToCheckNext.add(new int[] {x, y + 1});
						unitSpace[x][y + 1] = xyValue;
					}
				}
				if(x - 1 > -1)
				{
					xyValue = unitSpace[x][y] - playerTileMapCost[x - 1][y];
					if (unitSpace[x - 1][y] < xyValue)
					{
						tileToCheckNext.add(new int[] {x - 1, y});
						unitSpace[x - 1][y] = xyValue;
					}
				}
				if(y - 1 > -1)
				{
					xyValue = unitSpace[x][y] - playerTileMapCost[x][y - 1];
					if (unitSpace[x][y - 1] < xyValue)
					{
						tileToCheckNext.add(new int[] {x, y - 1});
						unitSpace[x][y - 1] = xyValue;
					}
				}
			}
			tileToCheck = ArrayHandler.cloneMatrix(tileToCheckNext);
			tileToCheckNext.clear();
		}
		return unitSpace;
	}
	
	public double[][] calculateUnitMovementRange(Unit unitUsed)
	{
		//Maybe use recursive programming?? To replace the arrays
		
		//tileMap.getMap().length * tileMap.getMap()[0].length
		double[][] playerTileMapCost = ArrayHandler.cloneMatrix(tileMapCost);
		int i;
		int j;
		ArrayList<Unit> playerUnits;
		int[] unitLocation;
		for (i = 0; i < playerList.length; i++)
		{
			if (i == unitUsed.getPlayer())
			{
				i++;
			}
			if(i >= playerList.length)
			{
				break;
			}
			playerUnits = playerList[i].getUnitList();
			for (j = 0; j < playerUnits.size(); j++)
			{
				unitLocation = playerUnits.get(j).getLocation().getLocation().clone();
				playerTileMapCost[unitLocation[0]][unitLocation[1]] = 65535;
			}
		}
		double[][] unitSpace = new double[tileMap.getMap().length][tileMap.getMap()[0].length];
		ArrayList<int[]> tileToCheck = new ArrayList<int[]>();
		ArrayList<int[]> tileToCheckNext = new ArrayList<int[]>();
		int x;
		int y;
		for (i = 0; i < unitSpace.length; i++)
		{
			for (j = 0; j < unitSpace[0].length; j++)
			{
				unitSpace[i][j] = 0;
			}
		}
		tileToCheck.add(new int[]{unitUsed.getY(),unitUsed.getX()});
		unitSpace[unitUsed.getY()][unitUsed.getX()] = unitUsed.getDefactoMoveRange();
		double xyValue = 0;
		while (tileToCheck.size() > 0)
		{
			for(i = 0; i < tileToCheck.size(); i++)
			{
				x = tileToCheck.get(i)[0];
				y = tileToCheck.get(i)[1];
				if(x + 1 < unitSpace.length)
				{
					xyValue = unitSpace[x][y] - playerTileMapCost[x + 1][y];
					if (unitSpace[x + 1][y] < xyValue)
					{
						tileToCheckNext.add(new int[] {x + 1, y});
						unitSpace[x + 1][y] = xyValue;
					}
				}
				if(y + 1 < unitSpace[0].length)
				{
					xyValue = unitSpace[x][y] - playerTileMapCost[x][y + 1];
					if (unitSpace[x][y + 1] < xyValue)
					{
						tileToCheckNext.add(new int[] {x, y + 1});
						unitSpace[x][y + 1] = xyValue;
					}
				}
				if(x - 1 > -1)
				{
					xyValue = unitSpace[x][y] - playerTileMapCost[x - 1][y];
					if (unitSpace[x - 1][y] < xyValue)
					{
						tileToCheckNext.add(new int[] {x - 1, y});
						unitSpace[x - 1][y] = xyValue;
					}
				}
				if(y - 1 > -1)
				{
					xyValue = unitSpace[x][y] - playerTileMapCost[x][y - 1];
					if (unitSpace[x][y - 1] < xyValue)
					{
						tileToCheckNext.add(new int[] {x, y - 1});
						unitSpace[x][y - 1] = xyValue;
					}
				}
			}
			tileToCheck = ArrayHandler.cloneMatrix(tileToCheckNext);
			tileToCheckNext.clear();
		}
		return unitSpace;
	}
	
	public void updateEffectFieldList()
	{
		ArrayList<Integer> remove = new ArrayList<Integer>();
		ArrayList<Unit> unitUpdateList;
		EffectField effectField;
		int i = 0;
		int j = 0;
		for (i = 0; i < effectFieldList.size(); i++)
		{
			effectField = effectFieldList.get(i);
			if (!effectField.getActive())
			{
				remove.add(i);
				unitUpdateList = effectField.getUnitsInRange(unitTileMap, buildingTileMap);
				for (j = 0; j < unitUpdateList.size(); j++)
				{
					unitUpdateList.get(i).removeEffectField(effectField);
				}
			}
		}
		for (i = remove.size() - 1; i >= 0; i--)
		{
			effectFieldList.remove((int) remove.get(i)); 
		}
	}
	
	public void advanceTurn()
	{
		deselect();
		int lastTurn = currentTurn;
		currentTurn ++;
		selectedUnitID = -1;
		int i = 0;
		boolean incrementTurn = false;
		checkIfAnyDefeated();
		if(currentTurn >= player)
		{
			currentTurn = 0;
			incrementTurn = true;
		}
		while (playerList[currentTurn].getDefeated())
		{
			currentTurn++;
			if(currentTurn >= player)
			{
				currentTurn = 0;
				incrementTurn = true;
			}
			if(currentTurn == lastTurn)
			{
				won = true;
				break;
			}
		}
		if(currentTurn >= player)
		{
			currentTurn = 0;
			incrementTurn = true;
		}
		if (incrementTurn)
		{
			turn++;
			ArrayList<Integer> remove = new ArrayList<Integer>();
			for (i = 0; i < effectFieldList.size(); i++)
			{
				effectFieldList.get(i).incrementTurn();
				if (!effectFieldList.get(i).getActive())
				{
					remove.add(i);
				}
			}
			player = playerList.length;
			for (i = remove.size() - 1; i >= 0; i--)
			{
				effectFieldList.remove((int) remove.get(i));
			}
			for (i = 0; i < player; i++)
			{
				playerList[i].incrementTurn(playerList, effectFieldList, unitTileMap);
			}
			for(i = 0; i < player; i++)
			{
				playerList[i].addCommandEffect(playerList);
			}
		}
		currentPlayer = playerList[currentTurn];
		currentPlayerUnitList = currentPlayer.getUnitList();
		updatePlayerTileMapCost();
		updateUnitIDs();
		setUpVisionRangeMap();
	}
	
	public void checkIfAnyDefeated()
	{
		int position = defeated - 1;
		Player player;
		for(int i = 0; i < playerList.length; i++)
		{
			if(!playerList[i].getDefeated())
			{
				//The way that a side can win/lose
				//0  = Active Commander Defeated
				//1  = <x Commanders Left
				//2  = <x Special Unit Left (Not Including Commanders)
				//3  = <x Normal Units Left (Not Including Special Units and Commanders)
				//4  = <x Special Unit Left (Including Commanders)
				//5  = <x Normal Units Left (Not Including Commanders)
				//6  = <x Normal Units Left (Not Including Special Units)
				//7  = <x Normal Units Left (Including Both Special Units and Commanders)
				//8  = <x Strong Holds Left
				//9  = <x Barracks Left
				//10 = <x Reactors/Generators Left
				//11 = <x Factories Left
				//12 = <x Fortresses Left
				//13 = >x Commanders In Area
				//14 = >x Special Units In Area (Not Including Commanders)
				//15 = >x Normal Units In Area (Not Including SpecialUnits and Commanders)
				//16 = >x Special Units In Area (Including Commanders)
				//17 = >x Normal Units In Area (Not Including Commanders)
				//18 = >x Normal Units In Area (Not Including SpecialUnits)
				//19 = >x Normal Units In Area (Including Both SpecialUnits and Commanders)
				player = playerList[i];
				if(winConditionList[0] == 1.0 && !player.getActivelyCommanded())
				{
					player.defeat();
					winOrder[i] = position;
					defeated--;
				}
				else if(winConditionList[1] > player.getCommanderList().size())
				{
					player.defeat();
					winOrder[i] = position;
					defeated--;
				}
				else if(winConditionList[2] > player.getSpecialUnitList().size() - player.getCommanderList().size())
				{
					player.defeat();
					winOrder[i] = position;
					defeated--;
				}
				else if(winConditionList[3] > player.getUnitList().size() - player.getSpecialUnitList().size() - player.getCommanderList().size())
				{
					player.defeat();
					winOrder[i] = position;
					defeated--;
				}
				else if(winConditionList[4] > player.getSpecialUnitList().size())
				{
					player.defeat();
					winOrder[i] = position;
					defeated--;
				}
				else if(winConditionList[5] > player.getUnitList().size() - player.getSpecialUnitList().size())
				{
					player.defeat();
					winOrder[i] = position;
					defeated--;
				}
				else if(winConditionList[6] > player.getUnitList().size() - player.getCommanderList().size())
				{
					player.defeat();
					winOrder[i] = position;
					defeated--;
				}
				else if(winConditionList[7] > player.getUnitList().size())
				{
					player.defeat();
					winOrder[i] = position;
					defeated--;
				}
			}
		}
		int sidesLeft = 0;
		for(int i = 0; i < playerList.length; i++)
		{
			if(!playerList[i].getDefeated())
			{
				sidesLeft++;
			}
		}
		if(sidesLeft < 2)
		{
			won = true;
		}
		for(int i = 0; i < playerList.length; i++)
		{
			if(!playerList[i].getDefeated())
			{
				winOrder[i] = 1;
			}
		}
	}
}

