package animation;
import java.util.ArrayList;
import java.util.Scanner;
import attack.Attack;
import attack.EffectField;
import attack.Special;
import game.Game;
import game.Player;
import terrain.Building;
import terrain.TileInterface;
import unit.Unit;
import utilities.JSONDecoder;
import utilities.Location;

public class TextInterface {
	
	private Game currentGame;
	private Scanner scanner;
	private String stage;
	private Unit selectedUnit;
	private Building selectedBuilding;
	
	public TextInterface()
	{
		currentGame = new Game();
		scanner = new Scanner(System.in);
		JSONDecoder.setupEffectName();
		stage = "nill";
	}
	
	public TextInterface(Game game)
	{
		currentGame = game;
		scanner = new Scanner(System.in);
		JSONDecoder.setupEffectName();
		stage = "nill";
	}
	
	public void playerAction()
	{
		String command = scanner.nextLine();
		command = command.replaceAll(" ", "");
		int commandNumber = -1;
		String[] coordinate = command.split(",");
		int[] coordinateNumber = new int[2];
		int commandType = 0;
		try
		{
			commandNumber = Integer.valueOf(command);
			commandType = 1;
		}
		catch (Exception e)
		{
			if (coordinate.length == 2)
			{
				try
				{
					coordinateNumber[1] = Integer.valueOf(coordinate[0]) - 1;
					coordinateNumber[0] = Integer.valueOf(coordinate[1]) - 1;
					commandType = 2;
				}
				catch (Exception f)
				{
					System.out.println("Invalid Input");
				}
			}
		}
		if(commandNumber == 0)
		{
			selectedUnit = null;
			selectedBuilding = null;
			currentGame.advanceTurn();
		}
		else if(currentGame.getUnitSelected())
		{
			Unit selectedUnit = currentGame.getSelectedUnit();
			if (stage.equals("move"))
			{
				try
				{
					if (commandNumber == 1)
					{
						currentGame.deselect();
					}
					else if (commandNumber == 2)
					{
						if(currentGame.selectedUnitUpdateValidMove(selectedUnit.getLocation()))
						{
							currentGame.moveSelectedUnit(selectedUnit.getLocation().getLocation());
							stage = "chooseattack";
						}
					}
					else if (commandNumber == 3 && currentGame.getSelectedUnit().getActiveCommander())
					{
						stage = "changecommander";
					}
					else
					{
						if(currentGame.selectedUnitUpdateValidMove(coordinateNumber) && commandType == 2)
						{
							currentGame.moveSelectedUnit(coordinateNumber);
							stage = "chooseattack";
						}
						else
						{
							System.out.println("Invalid Space to Move");
						}
					}
				}
				catch (Exception e)
				{
					
				}
			}
			else if (stage.equals("chooseattack"))
			{
				if (0 < commandNumber && commandNumber < selectedUnit.getAttackList().length + 1 && currentGame.selectValidAttack(commandNumber - 1))
				{
					currentGame.selectAttack(commandNumber - 1);
					stage = "attack";
				}
				else if (selectedUnit.isSpecial())
				{
					if (selectedUnit.getAttackList().length < commandNumber && commandNumber < selectedUnit.getSpecialList().length + selectedUnit.getAttackList().length + 1 && currentGame.selectValidSpecial(commandNumber - 1 - selectedUnit.getAttackList().length))
					{
						currentGame.selectSpecial(commandNumber - 1 - selectedUnit.getAttackList().length);
						stage = "special";
					}
					else if (commandNumber == selectedUnit.getAttackList().length + selectedUnit.getSpecialList().length + 1)
					{
						currentGame.deselect();
					}
					else if (commandNumber == selectedUnit.getAttackList().length + selectedUnit.getSpecialList().length + 2)
					{
						stage = "capture";
					}
					else System.out.println("Invalid Special");
				}
				else if (commandNumber == selectedUnit.getAttackList().length + 1)
				{
					currentGame.deselect();
				}
				else if (commandNumber == selectedUnit.getAttackList().length + 2)
				{
					stage = "capture";
				}
				else
				{
					System.out.println("Invalid Attack");
				}
			}
			else if (stage.equals("capture"))
			{
				if(commandType == 2)
				{
					if(currentGame.validCapture(coordinateNumber))
					{
						currentGame.captureBuilding(coordinateNumber);
					}
					else
					{
						System.out.println("Invalid Building to Capture");
					}
				}
				if(commandNumber == 1)
				{
					currentGame.deselect();
				}
				if(commandNumber == 2)
				{
					stage = "chooseattack";
				}
			}
			else if (stage.equals("attack"))
			{
				if (commandNumber == 1)
				{
					if(currentGame.getAttacking())
					{
						currentGame.getSelectedUnit().endAttack();
						currentGame.endAttack();
					}
					else
					{
						currentGame.getSelectedUnit().removeAttackNumber(currentGame.getSelectedAttack());
						currentGame.endAttack();
					}
					currentGame.deselect();
				}
				else if(commandNumber == 2)
				{
					if(!currentGame.getAttacking())
					{
						currentGame.getSelectedUnit().removeAttackNumber(currentGame.getSelectedAttack());
						currentGame.getSelectedUnit().endAttack();
						stage = "chooseattack";
					}
				}
				else
				{
					if(currentGame.validTarget(coordinateNumber) && commandType == 2)
					{
						currentGame.attack(new Location(coordinateNumber));
					}
					else
					{
						System.out.println("Invalid Unit To Attack");
						if (!currentGame.getAttacking())
						{
							stage = "chooseattack";
						}
					}
				}
			}
			else if(stage.equals("changecommander"))
			{
				if(commandNumber - 1 >= 0 && commandNumber - 1 < currentGame.getCurrentPlayer().getCommanderList().size())
				{
					if(currentGame.setActiveCommanderReturn(commandNumber - 1))
					{
						currentGame.deselect();
					}
					else
					{
						stage = "move";
					}
				}
				else if(commandNumber == currentGame.getCurrentPlayer().getCommanderList().size() + 1)
				{
					currentGame.deselect();
				}
				else if(commandNumber == currentGame.getCurrentPlayer().getCommanderList().size() + 2)
				{
					stage = "move";
				}
			}
			else if(stage.equals("special"))
			{
				Special selectedSpecial = currentGame.getSelectedSpecial();
				if(selectedSpecial.getSpecialType() == 0)
				{
					if (commandNumber == 1)
					{
						if(currentGame.getAttacking())
						{
							currentGame.getSelectedUnit().endAttack();
							currentGame.endAttack();
						}
						else
						{
							currentGame.getSelectedUnit().removeAttackNumber(currentGame.getSelectedAttack());
							currentGame.endAttack();
						}
						currentGame.deselect();
					}
					else if(commandNumber == 2)
					{
						if(!currentGame.getAttacking())
						{
							currentGame.getSelectedUnit().removeAttackNumber(currentGame.getSelectedAttack());
							currentGame.getSelectedUnit().endAttack();
							stage = "chooseattack";
						}
					}
					else
					{
						if(currentGame.validTarget(coordinateNumber, currentGame.getSelectedSpecial().getAttackList()[0]))
						{
							currentGame.special(new Location(coordinateNumber), new double[0]);
						}
						else
						{
							System.out.println("Invalid Unit To Attack");
							if (!currentGame.getAttacking())
							{
								stage = "chooseattack";
							}
						}
					}
				}
				else if(selectedSpecial.getSpecialType() == 1)
				{
					
				}
				else if(selectedSpecial.getSpecialType() == 2)
				{
					if (commandNumber == 1)
					{
						currentGame.deselect();
					}
					else if(commandNumber == 2)
					{
							stage = "chooseattack";
					}
					else if(commandNumber == 3)
					{
						currentGame.special(new Location(), new double[0]);
						currentGame.deselect();
					}
				}
				else if(selectedSpecial.getSpecialType() == 3)
				{
					if (commandNumber == 1)
					{
						currentGame.deselect();
					}
					else if(commandNumber == 2)
					{
							stage = "chooseattack";
					}
					else if(commandNumber == 3)
					{
						currentGame.special(new Location(), new double[0]);
						currentGame.deselect();
					}
				}
				else if(selectedSpecial.getSpecialType() == 4)
				{
					
				}
			}
			else if(stage.equals("nill"))
			{
				if (commandNumber == 1)
				{
					currentGame.deselect();
				}
			}
		}
		else if(currentGame.getBuildingSelected())
		{
			if(stage.equals("building"))
			{
				//0: Nothing
				//1: Reactor / Generator - GetsResource
				//2: StrongHold - Special Building For Win Conditions
				//3: Fortress - A Unit of the Same Player can Stay on the Tile and have a Defence Boost
				//4: Barracks/Factory - A Building to Make Units
				//5: Armed Fortress - A Building that can Attack
				//6: Artillery Stations - A Building that has a Powerful Customisable Launcher
				if(commandNumber == 1)
				{
					currentGame.deselect();
				}
				else if(selectedBuilding.getBuildingType() == 1)
				{
					
				}
				else if(selectedBuilding.getBuildingType() == 2)
				{
					
				}
				else if(selectedBuilding.getBuildingType() == 3)
				{
					
				}
				else if(selectedBuilding.getBuildingType() == 4)
				{
					if(commandType == 1)
					{
						if(currentGame.selectedBuildingCanMake(commandNumber - 2))
						{
							currentGame.selectedBuildingStartMake(commandNumber - 2);
						}
						else
						{
							System.out.println("Invalid Unit to Make");
						}
					}
				}
				else if(selectedBuilding.getBuildingType() == 5)
				{
					
				}
				else if(selectedBuilding.getBuildingType() == 6)
				{
					
				}
			}
			else if(stage.equals("nill"))
			{
				if (commandNumber == 1)
				{
					currentGame.deselect();
				}
			}
		}
		else if(currentGame.getTileSelected())
		{
			if(stage.equals("nill"))
			{
				if (commandNumber == 1)
				{
					currentGame.deselect();
				}
			}
		}
		else
		{
			try
			{
				currentGame.selectUnit(commandNumber - 1);
				Unit selectedUnit = currentGame.getSelectedUnit();
				if(selectedUnit.getPlayer() == currentGame.getCurrentTurn() && this.selectedBuilding != null)
				{
					if(selectedUnit != this.selectedBuilding && !stage.equals("building"))
					{
						this.selectedBuilding.endTurn();
					}
				}
				else if(selectedUnit.getPlayer() == currentGame.getCurrentTurn() && this.selectedUnit != null)
				{
					if (selectedUnit != this.selectedUnit && !stage.equals("move"))
					{
						this.selectedUnit.endTurn();
					}
				}
				this.selectedUnit = currentGame.getSelectedUnit();
				this.selectedBuilding = null;
				if (selectedUnit.getPlayer() == currentGame.getCurrentTurn())
				{
					if (!selectedUnit.getMoved())
					{
						stage = "move";
					}
					else if(selectedUnit.getAttacked())
					{
						stage = "nill";
					}
					else if (selectedUnit.getMoved())
					{
						stage = "chooseattack";
					}
				}
				else
				{
					stage = "nill";
				}
			}
			catch (Exception e)
			{
				try
				{
					if(commandType == 2)
					{
						currentGame.select(coordinateNumber);
					}
					if(currentGame.getUnitSelected())
					{
						Unit selectedUnit = currentGame.getSelectedUnit();
						if(selectedUnit.getPlayer() == currentGame.getCurrentTurn() && this.selectedBuilding != null)
						{
							if(selectedUnit != this.selectedBuilding && !stage.equals("building"))
							{
								this.selectedBuilding.endTurn();
							}
						}
						else if(selectedUnit.getPlayer() == currentGame.getCurrentTurn() && this.selectedUnit != null)
						{
							if (selectedUnit != this.selectedUnit && !stage.equals("move"))
							{
								this.selectedUnit.endTurn();
							}
						}
						this.selectedUnit = currentGame.getSelectedUnit();
						this.selectedBuilding = null;
						if (selectedUnit.getPlayer() == currentGame.getCurrentTurn())
						{
							if (!selectedUnit.getMoved())
							{
								stage = "move";
							}
							else if(selectedUnit.getAttacked())
							{
								stage = "nill";
							}
							else if (selectedUnit.getMoved())
							{
								stage = "chooseattack";
							}
						}
						else
						{
							stage = "nill";
						}
					}
					else if(currentGame.getBuildingSelected())
					{
						Building selectedBuilding = currentGame.getSelectedBuilding();
						if(selectedBuilding.getPlayer() == currentGame.getCurrentTurn() && this.selectedBuilding != null)
						{
							if(selectedBuilding != this.selectedBuilding && !stage.equals("building"))
							{
								this.selectedBuilding.endTurn();
							}
						}
						else if(selectedBuilding.getPlayer() == currentGame.getCurrentTurn() && this.selectedUnit != null)
						{
							if (selectedBuilding != this.selectedUnit && !stage.equals("move"))
							{
								this.selectedUnit.endTurn();
							}
						}
						this.selectedUnit = null;
						this.selectedBuilding = currentGame.getSelectedBuilding();
						if (selectedBuilding.getPlayer() == currentGame.getCurrentTurn())
						{
							if (!selectedBuilding.getAttacked())
							{
								stage = "building";
							}
							else
							{
								stage = "nill";
							}
						}
						else
						{
							stage = "nill";
						}
					}
					else
					{
						try
						{
							currentGame.selectBuilding(commandNumber - currentGame.getCurrentPlayer().getNumberOfUnit() - 1);
							Building selectedBuilding = currentGame.getSelectedBuilding();
							if(selectedBuilding.getPlayer() == currentGame.getCurrentTurn() && this.selectedBuilding != null)
							{
								if(selectedBuilding != this.selectedBuilding && !stage.equals("building"))
								{
									this.selectedBuilding.endTurn();
								}
							}
							this.selectedUnit = null;
							this.selectedBuilding = currentGame.getSelectedBuilding();
							if (selectedBuilding.getPlayer() == currentGame.getCurrentTurn())
							{
								if (!selectedBuilding.getAttacked())
								{
									stage = "building";
								}
								else
								{
									stage = "nill";
								}
							}
							else
							{
								stage = "nill";
							}
						}
						catch (Exception g)
						{
							System.out.println("Invalid Unit");
						}
					}
				}
				catch (Exception f)
				{
					
				}
			}
		}
	}
	
	public void printCostTileMap()
	{
		int i = 0;
		int j = 0;
		double[][] tileMapCost = currentGame.getTileMapCost();
		for (i = 0; i < tileMapCost.length; i++)
		{
			for (j = 0; j < tileMapCost[0].length; j++)
			{
				System.out.print("|");
				System.out.print(((Double) tileMapCost[i][j]).intValue());
			}
			System.out.println("|");
		}
	}
	
	public void printUnitMoveMap()
	{
		int i = 0;
		int j = 0;
		double[][] tileMapCost = currentGame.getSelectedUnitTileMapCost();
		for (i = 0; i < tileMapCost.length; i++)
		{
			for (j = 0; j < tileMapCost[0].length; j++)
			{
				System.out.print("|");
				System.out.print(((Double) tileMapCost[i][j]).intValue());
			}
			System.out.println("|");
		}
	}
	
	public void printPlayerCostTileMap()
	{
		int i = 0;
		int j = 0;
		double[][] playerTileMapCost = currentGame.getPlayerTileMapCost();
		for (i = 0; i < playerTileMapCost.length; i++)
		{
			for (j = 0; j < playerTileMapCost[0].length; j++)
			{
				System.out.print("|");
				System.out.print(((Double) playerTileMapCost[i][j]).intValue());
			}
			System.out.println("|");
		}
	}
	
	public void renderScreen ()
	{
		System.out.print("Player Turn: ");
		System.out.print(currentGame.getCurrentTurn() + 1);
		System.out.println();
		int printSpace = 3;
		Unit selectedUnit;
		Building selectedBuilding;
		TileInterface[][] printMap = currentGame.getTileMap().getMap();
		String[][] unitMap = new String[printMap.length][printMap[0].length];
		String[][] unitTypeMap = new String[printMap.length][printMap[0].length];
		String[][] unitMoveMap = new String[printMap.length][printMap[0].length];
		String[][] effectFieldMap = new String[printMap.length][printMap[0].length];
		int i = 0;
		int j = 0;
		int k = 0;
		Unit printUnit;
		int[] printLocation;
		int stringLength = 0;
		for (i = 0; i < unitMap.length; i++)
		{
			for (j = 0; j < unitMap[0].length; j++)
			{
				unitMap[i][j] = "";
				unitTypeMap[i][j] = "";
				unitMoveMap[i][j] = "";
				effectFieldMap[i][j] = "";
			}
		}
		
		ArrayList<Building> buildingList = currentGame.getBuildingList();
		for(i = 0; i < buildingList.size(); i++)
		{
			printUnit = buildingList.get(i);
			printLocation = printUnit.getLocation().getLocation();
			if(currentGame.getVisionRangeMap(printLocation[0], printLocation[1]) > 0)
			{
				unitMap[printLocation[0]][printLocation[1]] = ((Integer) (printUnit.getPlayer() + 1)).toString();
			}
		}
		
		Player[] playerList = currentGame.getPlayerList();
		for (i = 0; i < playerList.length; i++)
		{
			for (j = 0; j < playerList[i].getUnitList().size(); j++)
			{
				printUnit = playerList[i].getUnitList().get(j);
				printLocation = printUnit.getLocation().getLocation();
				if(currentGame.getVisionRangeMap(printLocation[0], printLocation[1]) > 0)
				{
					unitMap[printLocation[0]][printLocation[1]] = ((Integer) (printUnit.getPlayer() + 1)).toString();
					unitTypeMap[printLocation[0]][printLocation[1]] = ((Integer) printUnit.getUnitNumber()).toString();
				}
			}
		}
		ArrayList<EffectField> effectFieldList = currentGame.getEffectFieldList();
		EffectField effectField;
		int range;
		for (i = 0; i < effectFieldList.size(); i++)
		{
			effectField = effectFieldList.get(i);
			range = ((Double) effectField.getRange()).intValue();
			System.out.println(i);
			for (j = 0; j < range + 1; j++)
			{
				for (k = 0; k < range + 1; k++)
				{
					if (currentGame.getTileMap().inMap(j - range + k + effectField.getY(), j - k + effectField.getX()))
					{
						if(currentGame.getVisionRangeMap(j - range + k + effectField.getY(), j - k + effectField.getX()) > 0)
						{
							effectFieldMap[j - range + k + effectField.getY()][j - k + effectField.getX()] = ((Integer) (effectField.getPlayer() + 1)).toString();
						}
					}
				}
			}
			for (j = 0; j < range; j++)
			{
				for (k = 0; k < range; k++)
				{
					if (currentGame.getTileMap().inMap(j - range + k + 1 + effectField.getY(), j - k + effectField.getX()))
					{
						if(currentGame.getVisionRangeMap(j - range + k + 1 + effectField.getY(), j - k + effectField.getX()) > 0)
						{
							effectFieldMap[j - range + k + 1 + effectField.getY()][j - k + effectField.getX()] = ((Integer) (effectField.getPlayer() + 1)).toString();
						}
					}
				}
			}
		}
		int l;
		if(currentGame.getUnitSelected())
		{
			Unit[][] unitTileMap = currentGame.getUnitTileMap().getMap();
			selectedUnit = currentGame.getSelectedUnit();
			double[][] moveValues = currentGame.calculateSelectedMovementRange();
			for (i = 0; i < moveValues.length; i++)
			{
				for (j = 0; j < moveValues[0].length; j++)
				{
					if(moveValues[i][j] > 0.0 && stage.equals("move"))
					{
						if(unitTileMap[i][j] == null)
						{
							unitMoveMap[i][j] = ((Integer)((Double) moveValues[i][j]).intValue()).toString();
						}
						else if(unitTileMap[i][j].getPlayer() != selectedUnit.getPlayer() || unitTileMap[i][j].getUnitID() == selectedUnit.getUnitID())
						{
							unitMoveMap[i][j] = ((Integer)((Double) moveValues[i][j]).intValue()).toString();
						}
					}
					if(stage.equals("attack"))
					{
						if(currentGame.getSelectedAttack().checkValidTarget(i, j) && currentGame.getVisionRangeMap(i, j) > 0)
						{
							unitMoveMap[i][j] = "A";
							
							range = currentGame.getSelectedAttack().getDefactoAreaOfEffect();
							for (k = 0; k < range + 1; k++)
							{
								for (l = 0; l < range + 1; l++)
								{
									if(currentGame.getTileMap().inMap(k - range + l + i, k - l + j))
									{
										if(unitMoveMap[k - range + l + i][k - l + j].equals(""))
										{
											unitMoveMap[k - range + l + i][k - l + j] = "a";
										}
									}
								}
							}
							for (k = 0; k < range; k++)
							{
								for (l = 0; l < range; l++)
								{
									if (currentGame.getTileMap().inMap(k - range + l + 1 + i, k - l + j))
									{
										if(unitMoveMap[k - range + l + 1 + i][k - l + j].equals(""))
										{
											unitMoveMap[k - range + l + 1 + i][k - l + j] = "a";
										}
									}
								}
							}
							
						}
					}
					else if(stage.equals("capture"))
					{
						if(currentGame.getSelectedUnit().getLocation().calculateDistance(i, j) <= 1 && currentGame.getVisionRangeMap(i, j) > 0)
						{
							unitMoveMap[i][j] = "C";
						}
					}
					else if(stage.equals("special") && currentGame.getSelectedSpecial().getSpecialType() == 0)
					{
						if(currentGame.getSelectedSpecial().getAttackList()[0].checkValidTarget(i, j) && currentGame.getVisionRangeMap(i, j) > 0)
						{
							unitMoveMap[i][j] = "A";
							
							range = currentGame.getSelectedSpecial().getAttackList()[0].getDefactoAreaOfEffect();
							for (k = 0; k < range + 1; k++)
							{
								for (l = 0; l < range + 1; l++)
								{
									if(currentGame.getTileMap().inMap(k - range + l + i, k - l + j))
									{
										if (unitMoveMap[k - range + l + i][k - l + j].equals(""))
										{
											unitMoveMap[k - range + l + i][k - l + j] = "a";
										}
									}
								}
							}
							for (k = 0; k < range; k++)
							{
								for (l = 0; l < range; l++)
								{
									if (currentGame.getTileMap().inMap(k - range + l + 1 + i, k - l + j))
									{
										if(unitMoveMap[k - range + l + 1 + i][k - l + j].equals(""))
										{
											unitMoveMap[k - range + l + 1 + i][k - l + j] = "a";
										}
									}
								}
							}
							
						}
					}
				}
			}
		}
		for (i = 0; i < printMap.length + 2; i++)
		{
			System.out.print("|");
			for(j = 0; j < printSpace * 2; j++)
			{
				System.out.print("-");
			}
		}
		System.out.println("|");
		System.out.print("|");
		for(i = 0; i < printSpace * 2; i++)
		{
			System.out.print(" ");
		}
		for (i = 0; i < printMap[0].length; i++)
		{
			System.out.print("|");
			System.out.print(i + 1);
			stringLength = ((Integer) i).toString().length();
			for (j = 0; j < printSpace * 2 - stringLength; j++)
			{
				System.out.print(" ");
			}
		}
		System.out.println("|");
		for (i = 0; i < printMap.length + 2; i++)
		{
			System.out.print("|");
			for(j = 0; j < printSpace * 2; j++)
			{
				System.out.print("-");
			}
		}
		System.out.println("|");
		for (i = 0; i < printMap.length; i++)
		{
			System.out.print("|");
			System.out.print(i + 1);
			stringLength = ((Integer) i).toString().length();
			for (j = 0; j < printSpace * 2 - stringLength; j++)
			{
				System.out.print(" ");
			}
			for (j = 0; j < printMap[0].length; j++)
			{
				System.out.print("|");
				if(currentGame.getVisionRangeMap(i, j) > 0)
				{
					System.out.print(printMap[i][j].getTileNumber());
					stringLength = ((Integer) printMap[i][j].getTileNumber()).toString().length();
				}
				else
				{
					stringLength = 0;
				}
				for (k = 0; k < printSpace - stringLength; k++)
				{
					System.out.print(" ");
				}
				System.out.print(unitMap[i][j]);
				stringLength = (unitMap[i][j].length());
				for (k = 0; k < printSpace - stringLength; k++)
				{
					System.out.print(" ");
				}
			}
			System.out.println("|");
			System.out.print("|");
			System.out.print(i + 1);
			stringLength = ((Integer) i).toString().length();
			for (j = 0; j < printSpace * 2 - stringLength; j++)
			{
				System.out.print(" ");
			}
			for (j = 0; j < printMap[0].length; j++)
			{
				System.out.print("|");
				System.out.print(unitTypeMap[i][j]);
				for (k = 0; k < printSpace - unitTypeMap[i][j].length(); k++)
				{
					System.out.print(" ");
				}
				System.out.print(unitMoveMap[i][j]);
				for (k = 0; k < printSpace - unitMoveMap[i][j].length(); k++)
				{
					System.out.print(" ");
				}
			}
			System.out.println("|");
			System.out.print("|");
			System.out.print(i + 1);
			stringLength = ((Integer) i).toString().length();
			for (j = 0; j < printSpace * 2 - stringLength; j++)
			{
				System.out.print(" ");
			}
			for (j = 0; j < printMap[0].length; j++)
			{
				System.out.print("|");
				System.out.print(effectFieldMap[i][j]);
				for (k = 0; k < printSpace * 2 - effectFieldMap[i][j].length(); k++)
				{
					System.out.print(" ");
				}
			}
			System.out.println("|");
			for (j = 0; j < printMap.length + 2; j++)
			{
				System.out.print("|");
				for(k = 0; k < printSpace * 2; k++)
				{
					System.out.print("-");
				}
			}
			System.out.println("|");
		}
		i = 0;
		if(currentGame.getTileSelected())
		{
			TileInterface selectedTile = currentGame.getSelectedTile();
			System.out.println(selectedTile.getName());
			System.out.print("Movement Cost: ");
			System.out.println(selectedTile.getMovementCost());
		}
		if(currentGame.getBuildingSelected())
		{
			selectedBuilding = currentGame.getSelectedBuilding();
			System.out.println(selectedBuilding.getName());
			System.out.print("HP: ");
			System.out.print(selectedBuilding.getHealthPoint());
			System.out.print("/");
			System.out.println(selectedBuilding.getMaxHealthPoint());
			System.out.print("Stamina: ");
			System.out.print(selectedBuilding.getStamina());
			System.out.print("/");
			System.out.println(selectedBuilding.getMaxStamina());
			System.out.println("0: End Turn");
		}
		if(currentGame.getUnitSelected())
		{
			selectedUnit = currentGame.getSelectedUnit();
			System.out.println(selectedUnit.getName());
			System.out.print("HP: ");
			System.out.print(selectedUnit.getHealthPoint());
			System.out.print("/");
			System.out.println(selectedUnit.getMaxHealthPoint());
			System.out.print("Stamina: ");
			System.out.print(selectedUnit.getStamina());
			System.out.print("/");
			System.out.println(selectedUnit.getMaxStamina());
			System.out.println("0: End Turn");
			if (selectedUnit.isSpecial())
			{
				System.out.print("Special Points: ");
				System.out.print(selectedUnit.getSpecialUnit().getSpecialPoint());
				System.out.print("/");
				System.out.println(selectedUnit.getSpecialUnit().getMaxSpecialPoint());
			}
			for(j = 0; j < selectedUnit.getEffectList().size(); j++)
			{
				System.out.print(selectedUnit.getEffectList(j).getName());
				System.out.println(": ");
				System.out.print("Turns Left: ");
				System.out.println(selectedUnit.getEffectList(j).getEffectDuration());
				System.out.print("Power: ");
				System.out.println(selectedUnit.getEffectList(j).getEffectPower());
				System.out.print("Effect Type: ");
				System.out.println(JSONDecoder.getEffectName().get(selectedUnit.getEffectList(j).getEffectNameMap()));
			}
			for(j = 0; j < selectedUnit.getEffectFieldList().size(); j++)
			{
				effectField = selectedUnit.getEffectFieldList().get(j);
				System.out.print("Field ");
				System.out.print(j);
				System.out.println(": ");
				for (k = 0; k < effectField.getEffectList().length; k++)
				{
					System.out.print(effectField.getEffectList(k).getName());
					System.out.println(": ");
					System.out.print("Power: ");
					System.out.println(effectField.getEffectList(k).getEffectPower());
					System.out.print("Effect Type: ");
					System.out.println(JSONDecoder.getEffectName().get(effectField.getEffectList(k).getEffectNameMap()));
				}
			}
			if(stage.equals("chooseattack"))
			{
				System.out.println("Choose Attack");
				Attack[] attackList = selectedUnit.getAttackList();
				System.out.println("Attacks");
				for(i = 0; i < attackList.length; i++)
				{
					System.out.print(i + 1);
					System.out.print(": ");
					System.out.println(attackList[i].getName());
					System.out.print("Damage: ");
					System.out.println(attackList[i].getAttackPower());
					System.out.print("Range: ");
					System.out.println(attackList[i].getAttackRange());
					System.out.print("Stamina: ");
					System.out.println(attackList[i].getStaminaCost());
				}
				int specialOffset = i + 1;
				int deselectNumber = i + 1;
				if(selectedUnit.isSpecial())
				{
					System.out.println("Specials");
					Special[] specialList = selectedUnit.getSpecialUnit().getSpecialList();
					for(i = 0; i < specialList.length; i++)
					{
						System.out.print(i + specialOffset);
						System.out.print(": ");
						System.out.println(specialList[i].getName());
						System.out.print("Stamina: ");
						System.out.println(specialList[i].getStaminaCost());
						System.out.print("Special Points: ");
						System.out.println(specialList[i].getSpecialPointCost());
					}
					deselectNumber = i + specialOffset;
				}
				System.out.print(deselectNumber);
				System.out.println(": Deselect Unit");
				System.out.print(deselectNumber + 1);
				System.out.println(": Capture Building");
			}
			else if (stage.equals("move"))
			{
				System.out.println("Move");
				System.out.println("1: Deselect Unit");
				System.out.println("2: Stay");
				if(currentGame.getSelectedUnit().getActiveCommander())
				{
					System.out.println("3: Change Commander");
				}
			}
			else if (stage.equals("capture"))
			{
				System.out.println("Capture Building");
				System.out.println("1: Deselect Unit");
				System.out.println("2: Return to Choose Attack");
			}
			else if (stage.equals("attack"))
			{
				System.out.println("Attack");
				System.out.println(currentGame.getSelectedAttack().getName());
				System.out.println("1: Deselect Unit");
				System.out.println("2: Deselect Attack");
			}
			else if (stage.equals("changecommander"))
			{
				System.out.println("Change Commander");
				for(i = 0; i < currentGame.getCurrentPlayer().getCommanderList().size(); i++)
				{
					System.out.print(i + 1);
					System.out.print(": ");
					System.out.println(currentGame.getCurrentPlayer().getCommanderList(i).getName());
				}
				System.out.print(i + 1);
				System.out.println(": Deselect Unit");
				System.out.print(i + 2);
				System.out.println(": Return To Move");
			}
			else if (stage.equals("special"))
			{
				System.out.println("Special");
				Special special = currentGame.getSelectedSpecial();
				System.out.println(special.getName());
				if (special.getSpecialType() == 0)
				{
					System.out.println("1: Deselect Unit");
					System.out.println("2: Deselect Attack");
				}
				else if (special.getSpecialType() == 1)
				{
					
				}
				else if (special.getSpecialType() == 2)
				{
					System.out.println("1: Deselect Unit");
					System.out.println("2: Deselect Attack");
					System.out.println("3: Perform Special");
				}
				else if (special.getSpecialType() == 3)
				{
					System.out.println("1: Deselect Unit");
					System.out.println("2: Deselect Attack");
					System.out.println("3: Perform Special");
				}
				else if (special.getSpecialType() == 4)
				{
					
				}
			}
			else if (stage.equals("nill"))
			{
				System.out.println("1: Deselect Unit");
			}
		}
		else if(currentGame.getBuildingSelected())
		{
			selectedBuilding = currentGame.getSelectedBuilding();
			if(stage.equals("building"))
			{
				//0: Nothing
				//1: Reactor / Generator - GetsResource
				//2: StrongHold - Special Building For Win Conditions
				//3: Fortress - A Unit of the Same Player can Stay on the Tile and have a Defence Boost
				//4: Barracks/Factory - A Building to Make Units
				//5: Armed Fortress - A Building that can Attack
				//6: Artillery Stations - A Building that has a Powerful Customisable Launcher
				if(selectedBuilding.getBuildingType() == 1)
				{
					System.out.print("Resource Gained per Turn: ");
					System.out.println(selectedBuilding.getBuildingData(0));
				}
				else if(selectedBuilding.getBuildingType() == 2)
				{
					
				}
				else if(selectedBuilding.getBuildingType() == 3)
				{
					System.out.print("Defence Boost: ");
					System.out.println(selectedBuilding.getBuildingData(0));
				}
				else if(selectedBuilding.getBuildingType() == 4)
				{
					if(currentGame.selectedBuildingCanMake())
					{
						System.out.println("Make a Unit");
						for(i = 0; i < currentGame.getCurrentPlayer().getAllowedUnitList().size(); i++)
						{
							if(currentGame.getCurrentPlayer().getAllowedUnitList().get(i).getUnitMake() == selectedBuilding.getBuildingData(0))
							{
								System.out.print(i + 2);
								System.out.print(": ");
								System.out.println(currentGame.getCurrentPlayer().getAllowedUnitList().get(i).getName());
								System.out.print("Resource Cost: ");
								System.out.println(currentGame.getCurrentPlayer().getAllowedUnitList().get(i).getResourceCost());
								System.out.print("Turns to Make: ");
								System.out.println(currentGame.getCurrentPlayer().getAllowedUnitList().get(i).getTurnsToMake());
							}
						}
					}
					else if(selectedBuilding.getBuildingData(2) == 1.0)
					{
						System.out.print("Making: ");
						System.out.println(selectedBuilding.getMaking().getName());
						System.out.print("Turns Left: ");
						System.out.println(selectedBuilding.getBuildingData(3));
					}
				}
				else if(selectedBuilding.getBuildingType() == 5)
				{
					
				}
				else if(selectedBuilding.getBuildingType() == 6)
				{
					
				}
				System.out.println("1: Deselect Building");
			}
			else if(stage.equals("nill"))
			{
				//0: Nothing
				//1: Reactor / Generator - GetsResource
				//2: StrongHold - Special Building For Win Conditions
				//3: Fortress - A Unit of the Same Player can Stay on the Tile and have a Defence Boost
				//4: Barracks/Factory - A Building to Make Units
				//5: Armed Fortress - A Building that can Attack
				//6: Artillery Stations - A Building that has a Powerful Customisable Launcher
				if(selectedBuilding.getBuildingType() == 1)
				{
					System.out.print("Resource Gained per Turn: ");
					System.out.println(selectedBuilding.getBuildingData(0));
				}
				else if(selectedBuilding.getBuildingType() == 2)
				{
					
				}
				else if(selectedBuilding.getBuildingType() == 3)
				{
					System.out.print("Defence Boost: ");
					System.out.println(selectedBuilding.getBuildingData(0));
				}
				else if(selectedBuilding.getBuildingType() == 4)
				{
					if(currentGame.selectedBuildingCanMake())
					{
						
					}
					else if(selectedBuilding.getBuildingData(2) == 1.0)
					{
						System.out.print("Making: ");
						System.out.println(selectedBuilding.getMaking().getName());
						System.out.print("Turns Left: ");
						System.out.println(selectedBuilding.getBuildingData(3));
					}
				}
				else if(selectedBuilding.getBuildingType() == 5)
				{
					
				}
				else if(selectedBuilding.getBuildingType() == 6)
				{
					
				}
				System.out.println("1: Deselect Building");
			}
		}
		else
		{
			int buildingNumberOffset;
			System.out.println("0: End Turn");
			System.out.print("Resource: ");
			System.out.println(currentGame.getCurrentPlayer().getResource());
			System.out.println("Units");
			ArrayList<Unit> playerUnits = currentGame.getCurrentPlayer().getUnitList();
			for (i = 0; i < playerUnits.size(); i++ )
			{
				System.out.print(i + 1);
				System.out.println(": " + playerUnits.get(i).getName());
			}
			buildingNumberOffset = i;
			System.out.println("Buildings");
			ArrayList<Building> playerBuildings = currentGame.getCurrentPlayer().getBuildingList();
			for (i = 0; i < playerBuildings.size(); i++ )
			{
				System.out.print(i + 1 + buildingNumberOffset);
				System.out.println(": " + playerBuildings.get(i).getName());
			}
		}
		
	}
	
}
