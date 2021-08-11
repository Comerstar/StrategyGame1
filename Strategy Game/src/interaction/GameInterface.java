package interaction;

import java.util.ArrayList;

import animation.AnimationCommand;
import animation.GamePanel;
import attack.Special;
import game.Game;
import terrain.Building;
import terrain.TileInterface;
import unit.Unit;
import unit.UnitData;
import utilities.JSONDecoder;
import utilities.Location;

/**
 * A more general for of the TextInterface class. It is used to interact with a game, using only coordinate and integer inputs. It validates all user inputs, and handles all of the Game function calls. 
 * 
 * @author The Programmer (Comerstar)
 */
public class GameInterface {
	
	private Game game;
	// Valid Stages:
	//
	// notSelected       Tiles: Select a tile | Menus: N/A
	//
	// tileSelected      Tiles: Select another tile | Menus: End Turn
	// enemySelected     Tiles: Select another tile, Same tile -> enemySelectedMenu | Menus: N/A
	// enemySelectedMenu Tiles: Select another tile | Menus: End Turn
	// movedSelected:    Tiles: Select another tile, Same tile -> movedSelectedMenu | Menus: N/A
	// movedSelectedMenu Tiles: Select another tile Menus: End Turn
	// tempSelect        Tiles: return to main select Menus: N/A
	// 
	// move:             Tiles: Select a tile to move to -> chooseActionMenu, invalid tile without unit/building -> moveMenu, invalid tile with unit/building = tempSelect that unit/building | Menus: N/A
	// moveMenu          Tiles: Select a tile to move to -> chooseActionMenu, invalid tile without unit/building -> notSelected, invalid tile with unit/building = tempSelect that unit/building | Menus: End Turn, wait -> chooseActionMenu (changeCommander if applicable);
	//
	// chooseActionMenu: Tiles: Building/Unit -> tempSelect | Menus: attack -> chooseAttack, support -> chooseSupport, capture -> capture, (special -> chooseSpecial if applicable), wait
	//
	// chooseAttack:     Tiles: No building/unit -> chooseActionMenu, Building/Unit -> tempSelect | Menus: attack options -> attack, back -> chooseActionMenu, wait
	// attack:           Tiles: Select a tile to target, invalid tile without Building/Unit -> attackMenu, invalid with Building/Unit -> tempSelect | Menus: N/A
	// attackMenu:       Tiles: Select a tile to target, invalid tile without Building/Unit -> chooseAttack, invalid with Building/Unit -> tempSelect | Menus: choose attack
	//
	// chooseSupport:    Tiles: No building/unit -> chooseActionMenu, Building/Unit -> tempSelect | Menus: attack options -> attack, back -> chooseActionMenu, wait
	// support:          Tiles: Select a tile to target, invalid tile without Building/Unit -> attackMenu, invalid with Building/Unit -> tempSelect | Menus: N/A
	// supportMenu:      Tiles: Select a tile to target, invalid tile without Building/Unit -> chooseAttack, invalid with Building/Unit -> tempSelect | Menus: back -> chooseSupport
	// 
	// chooseSpecial:    Tiles: No building/unit -> chooseActionMenu, Building/Unit -> tempSelect | Menus: special options -> special (if applicable), back -> chooseActionMenu, wait
	// special:          Tiles: Select a tile to target, invalid tile without Building/Unit -> specialMenu, invalid with Building/Unit -> tempSelect | Menus: N/A
	// specialMenu:      Tiles: Select a tile to target, invalid tile without Building/Unit -> chooseSpecial, invalid with Building/Unit -> tempSelect | Menus: back -> chooseSpecial
	// 
	// capture:          Tiles: Select a tile to target, invalid tile without Building/Unit -> captureMenu, invalid with Building/Unit -> tempSelect | Menus: N/A
	// captureMenu       Tiles: Select a tile to target, invalid tile without Building/Unit -> captureMenu, invalid with Building/Unit -> tempSelect | Menus: back -> chooseAction, wait -> notSelected
	//
	// changeCommander:  Tiles: No building/unit -> moveMenu | Menus: commanderOptions -> notSelected, back -> moveMenu
	// 
	// building:         Tiles: Depends on Building | Menus: Depends on building.
	// 
	// 
	// 
	// 
	// 
	private String stage;
	private String lastStage;
	private Unit selectedUnit;
	private Building selectedBuilding;
	private TileInterface selectedTile;
	private int[] menuLocation = new int[2];
	
	/**
	 * Creates a game interface that interacts with an empty game.
	 */
	public GameInterface()
	{
		this.game = new Game();
		JSONDecoder.setupEffectName();
		stage = "notSelected";
		lastStage = "notSelected";
		selectedUnit = null;
		selectedBuilding = null;
	}
	
	/**
	 * Constructs a game interface which interacts with a given game.
	 * 
	 * @param game The game that the interface interacts with.
	 */
	public GameInterface(Game game)
	{
		this.game = game;
		JSONDecoder.setupEffectName();
		stage = "notSelected";
		lastStage = "notSelected";
		selectedUnit = null;
		selectedBuilding = null;
	}
	
	/**
	 * {@summary Responds to player input and performs the relevant actions to the game. }
	 * This function handles the user input in two forms, coordinates that are for selecting tiles, and commands that are used from selection from menus. 
	 * The function validates the user input, and checks, using game, that they are a valid action. The gameInterface then acts accordingly, either changing the stage that the user is on, or the updating game.
	 * It returns a list of animationCommands that command the GamePanel.
	 * 
	 * @param coordinateNumber The coordinate input from the player
	 * @param commandNumber The integer input of the player
	 * @param commandType Whether the input is a coordinate or a number.
	 * @param gamePanel The gamePanel through which the user did the input.
	 * @return A list of AnimationCommands that command the GamePanel to animate sprites or update sprite states.
	 * @see game.Game
	 * @see animation.GamePanel
	 * @see animation.AnimationCommand
	 */
	public ArrayList<AnimationCommand> playerInput(int[] coordinateNumber, int commandNumber, int commandType, GamePanel gamePanel)
	{
		ArrayList<AnimationCommand> animationCommands = new ArrayList<AnimationCommand>();
		// notSelected       Tiles: Select a tile | Menus: N/A
		if(stage == "notSelected")
		{
			if(commandType == 1)
			{
				
			}
			else if(commandType == 2)
			{
				if(game.getTileMap().inMap(coordinateNumber))
				{
					animationCommands = select(coordinateNumber, animationCommands);
				}
			}
		}
		//
		// tileSelected      Tiles: Select another tile | Menus: End Turn
		else if(stage == "tileSelected")
		{
			if(commandType == 1)
			{
				if(commandNumber == 0)
				{
					lastStage = stage;
					game.deselect();
					game.advanceTurn();
					stage = "notSelected";
					animationCommands.add(new AnimationCommand(4, "updateStage"));
					animationCommands.add(new AnimationCommand(4, "unitMapRefresh"));
					animationCommands.add(new AnimationCommand(4, "buildingMapRefresh"));
					animationCommands.add(new AnimationCommand(4, "clearTileInfo"));
					animationCommands.add(new AnimationCommand(4, "clearUnitInfo"));
					animationCommands.add(new AnimationCommand(4, "clearBuildingInfo"));
				}
			}
			else if(commandType == 2)
			{
				if(game.getTileMap().inMap(coordinateNumber))
				{

					animationCommands = select(coordinateNumber, animationCommands);
				}
			}
		}
		// enemySelected     Tiles: Select another tile, Same tile -> enemySelectedMenu | Menus: N/A
		else if(stage == "enemyUnitSelected")
		{
			if(commandType == 1)
			{
				
			}
			else if(commandType == 2)
			{
				if(game.getTileMap().inMap(coordinateNumber))
				{
					if(selectedUnit.getLocation().equals(coordinateNumber))
					{
						lastStage = stage;
						stage = "enemyUnitSelectedMenu";
						menuLocation = coordinateNumber.clone();
					}
					else
					{

						animationCommands = select(coordinateNumber, animationCommands);
					}
				}
			}
		}
		else if(stage == "enemyBuildingSelected")
		{
			if(commandType == 1)
			{
				
			}
			else if(commandType == 2)
			{
				if(game.getTileMap().inMap(coordinateNumber))
				{
					if(selectedBuilding.getLocation().equals(coordinateNumber))
					{
						lastStage = stage;
						stage = "enemyBuildingSelectedMenu";
						menuLocation = coordinateNumber.clone();
					}
					else
					{

						animationCommands = select(coordinateNumber, animationCommands);
					}
				}
			}
		}
		// enemySelectedMenu Tiles: Select another tile, Same -> notSelected | Menus: End Turn
		else if(stage == "enemyUnitSelectedMenu")
		{
			if(commandType == 1)
			{
				if(commandNumber == 0)
				{
					lastStage = stage;
					game.deselect();
					game.advanceTurn();
					stage = "notSelected";
					animationCommands.add(new AnimationCommand(4, "updateStage"));
					animationCommands.add(new AnimationCommand(4, "unitMapRefresh"));
					animationCommands.add(new AnimationCommand(4, "buildingMapRefresh"));
					animationCommands.add(new AnimationCommand(4, "clearTileInfo"));
					animationCommands.add(new AnimationCommand(4, "clearUnitInfo"));
					animationCommands.add(new AnimationCommand(4, "clearBuildingInfo"));
				}
			}
			else if(commandType == 2)
			{
				if(game.getTileMap().inMap(coordinateNumber))
				{
					if(selectedUnit.getLocation().equals(coordinateNumber))
					{
						animationCommands = deselect(animationCommands);
					}
					else
					{

						animationCommands = select(coordinateNumber, animationCommands);
					}
				}
			}
		}
		else if(stage == "enemyBuildingSelectedMenu")
		{
			if(commandType == 1)
			{
				if(commandNumber == 0)
				{
					lastStage = stage;
					game.deselect();
					game.advanceTurn();
					stage = "notSelected";
					animationCommands.add(new AnimationCommand(4, "updateStage"));
					animationCommands.add(new AnimationCommand(4, "unitMapRefresh"));
					animationCommands.add(new AnimationCommand(4, "buildingMapRefresh"));
					animationCommands.add(new AnimationCommand(4, "clearTileInfo"));
					animationCommands.add(new AnimationCommand(4, "clearUnitInfo"));
					animationCommands.add(new AnimationCommand(4, "clearBuildingInfo"));
				}
			}
			else if(commandType == 2)
			{
				if(game.getTileMap().inMap(coordinateNumber))
				{
					if(selectedBuilding.getLocation().equals(coordinateNumber))
					{
						animationCommands = deselect(animationCommands);
					}
					else
					{

						animationCommands = select(coordinateNumber, animationCommands);
					}
				}
			}
		}
		// movedSelected:    Tiles: Select another tile, Same tile -> movedSelectedMenu | Menus: N/A
		else if(stage == "movedUnitSelected")
		{
			if(commandType == 1)
			{
				
			}
			else if(commandType == 2)
			{
				if(game.getTileMap().inMap(coordinateNumber))
				{
					if(selectedUnit.getLocation().equals(coordinateNumber))
					{
						lastStage = stage;
						stage = "movedUnitSelectedMenu";
						menuLocation = coordinateNumber.clone();
					}
					else
					{

						animationCommands = select(coordinateNumber, animationCommands);
					}
				}
			}
		}
		else if(stage == "movedBuildingSelected")
		{
			if(commandType == 1)
			{
				
			}
			else if(commandType == 2)
			{
				if(game.getTileMap().inMap(coordinateNumber))
				{
					if(selectedBuilding.getLocation().equals(coordinateNumber))
					{
						lastStage = stage;
						stage = "movedBuildingSelectedMenu";
						menuLocation = coordinateNumber.clone();
					}
					else
					{

						animationCommands = select(coordinateNumber, animationCommands);
					}
				}
			}
		}
		// movedSelectedMenu: Tiles: Select another tile Menus: End Turn
		else if(stage == "movedUnitSelectedMenu")
		{
			if(commandType == 1)
			{
				if(commandNumber == 0)
				{
					lastStage = stage;
					game.deselect();
					game.advanceTurn();
					stage = "notSelected";
					animationCommands.add(new AnimationCommand(4, "updateStage"));
					animationCommands.add(new AnimationCommand(4, "unitMapRefresh"));
					animationCommands.add(new AnimationCommand(4, "buildingMapRefresh"));
					animationCommands.add(new AnimationCommand(4, "clearTileInfo"));
					animationCommands.add(new AnimationCommand(4, "clearUnitInfo"));
					animationCommands.add(new AnimationCommand(4, "clearBuildingInfo"));
				}
			}
			else if(commandType == 2)
			{
				if(game.getTileMap().inMap(coordinateNumber))
				{
					if(selectedUnit.getLocation().equals(coordinateNumber))
					{
						animationCommands = deselect(animationCommands);
					}
					else
					{

						animationCommands = select(coordinateNumber, animationCommands);
					}
				}
			}
		}

		else if(stage == "movedBuildingSelectedMenu")
		{
			if(commandType == 1)
			{
				if(commandNumber == 0)
				{
					lastStage = stage;
					game.deselect();
					game.advanceTurn();
					stage = "notSelected";
					animationCommands.add(new AnimationCommand(4, "updateStage"));
					animationCommands.add(new AnimationCommand(4, "unitMapRefresh"));
					animationCommands.add(new AnimationCommand(4, "buildingMapRefresh"));
					animationCommands.add(new AnimationCommand(4, "clearUnitInfo"));
					animationCommands.add(new AnimationCommand(4, "clearBuildingInfo"));
				}
			}
			else if(commandType == 2)
			{
				if(game.getTileMap().inMap(coordinateNumber))
				{
					if(selectedBuilding.getLocation().equals(coordinateNumber))
					{
						animationCommands = deselect(animationCommands);
					}
					else
					{

						animationCommands = select(coordinateNumber, animationCommands);
					}
				}
			}
		}
		// tempSelect: Tiles: return to main select Menus: N/A
		else if(stage == "tempUnitSelect")
		{
			if(commandType == 1)
			{
				
			}
			else if(commandType == 2)
			{
				stage = lastStage;
				selectedUnit = game.getSelectedUnit();
				selectedTile = game.getSelectedTile();
				animationCommands.add(new AnimationCommand(4, "updateStage"));
				animationCommands.add(new AnimationCommand(4, "updateTileInfo"));
				animationCommands.add(new AnimationCommand(4, "updateUnitInfo"));
			}
		}
		else if(stage == "tempBuildingSelect")
		{
			if(commandType == 1)
			{
				
			}
			else if(commandType == 2)
			{
				stage = lastStage;
				selectedBuilding = game.getSelectedBuilding();
				selectedTile = game.getSelectedTile();
				animationCommands.add(new AnimationCommand(4, "updateStage"));
				animationCommands.add(new AnimationCommand(4, "updateTileInfo"));
				animationCommands.add(new AnimationCommand(4, "updateBuildingInfo"));
			}
		}
		// 
		// move: Tiles: Select a tile to move to -> chooseActionMenu, invalid tile without unit/building -> moveMenu, invalid tile with unit/building = tempSelect that unit/building | Menus: N/A
		else if(stage == "move")
		{
			if(commandType == 1)
			{
				
			}
			else if(commandType == 2)
			{
				if(game.getTileMap().inMap(coordinateNumber))
				{
					if(game.selectedUnitUpdateValidMove(coordinateNumber))
					{
						game.moveSelectedUnit(coordinateNumber);
						lastStage = stage;
						stage = "chooseAction";
						menuLocation = coordinateNumber.clone();
						selectedTile = game.getSelectedTile();
						animationCommands.add(new AnimationCommand(4, "updateStage"));
						animationCommands.add(new AnimationCommand(4, "unitMapRefresh"));
						animationCommands.add(new AnimationCommand(4, "updateTileInfo"));
						animationCommands.add(new AnimationCommand(4, "updateUnitInfo"));
					}
					else
					{
						int size = animationCommands.size();
						animationCommands = this.select(coordinateNumber, animationCommands);
						if(size == animationCommands.size())
						{
							stage = "moveMenu";
							menuLocation = coordinateNumber.clone();
						}
					}
				}
			}
		}
		// moveMenu: Tiles: Select a tile to move to -> chooseActionMenu, invalid tile without unit/building -> move, invalid tile with unit/building = tempSelect that unit/building | Menus: End Turn, wait -> chooseActionMenu (changeCommander if applicable);
		else if(stage == "moveMenu")
		{
			if(commandType == 1)
			{
				if(commandNumber == 0)
				{
					game.moveSelectedUnit(game.getSelectedUnit().getLocation().getLocation());
					lastStage = stage;
					stage = "chooseAction";
					animationCommands.add(new AnimationCommand(4, "updateStage"));
				}
				else if(commandNumber == 1)
				{
					if(game.getSelectedUnit().getActiveCommander() && game.getCurrentPlayer().getCommanderList().size() > 1)
					{
						lastStage = stage;
						stage = "changeCommander";
						animationCommands.add(new AnimationCommand(4, "updateStage"));
					}
				}
			}
			else if(commandType == 2)
			{
				if(game.getTileMap().inMap(coordinateNumber))
				{
					if(game.selectedUnitUpdateValidMove(coordinateNumber))
					{
						game.moveSelectedUnit(coordinateNumber);
						lastStage = stage;
						stage = "chooseAction";
						menuLocation = coordinateNumber.clone();
						selectedTile = game.getSelectedTile();
						animationCommands.add(new AnimationCommand(4, "updateStage"));
						animationCommands.add(new AnimationCommand(4, "unitMapRefresh"));
						animationCommands.add(new AnimationCommand(4, "updateTileInfo"));
						animationCommands.add(new AnimationCommand(4, "updateUnitInfo"));
					}
					else
					{
						int size = animationCommands.size();
						animationCommands = this.select(coordinateNumber, animationCommands);
						if(size == animationCommands.size())
						{
							stage = "move";
						}
					}
				}
			}
		}
		//
		// chooseActionMenu: Tiles: Building/Unit -> tempSelect | Menus: attack -> chooseAttack, support -> chooseSupport, (special -> chooseSpecial if applicable), wait
		else if(stage == "chooseAction")
		{
			if(commandType == 1)
			{
				selectedUnit = game.getSelectedUnit();
				boolean attack = selectedUnit.getOffensiveAttackList().length > 0;
				boolean support = selectedUnit.getSupportList().length > 0;
				boolean capture = true;
				boolean special = selectedUnit.isSpecial();
				if(commandNumber == 0)
				{
					if(attack)
					{
						lastStage = stage;
						stage = "chooseAttack";
						animationCommands.add(new AnimationCommand(4, "updateStage"));
					}
					else if(support)
					{
						lastStage = stage;
						stage = "chooseSupport";
						animationCommands.add(new AnimationCommand(4, "updateStage"));
					}
					else if(capture)
					{
						lastStage = stage;
						stage = "capture";
						animationCommands.add(new AnimationCommand(4, "updateStage"));
					}
					else if(special)
					{
						lastStage = stage;
						stage = "chooseSpecial";
						animationCommands.add(new AnimationCommand(4, "updateStage"));
					}
					else
					{
						game.getSelectedUnit().endTurn();
						animationCommands = deselect(animationCommands);
					}
				}
				else if(commandNumber == 1)
				{
					if(support && attack)
					{
						lastStage = stage;
						stage = "chooseSupport";
						animationCommands.add(new AnimationCommand(4, "updateStage"));
					}
					else if(capture && (attack ^ support)) 
					{
						lastStage = stage;
						stage = "capture";
						animationCommands.add(new AnimationCommand(4, "updateStage"));
					}
					else if(special && (attack ^ support ^ capture) && !(attack && support && capture))
					{
						lastStage = stage;
						stage = "chooseSpecial";
						animationCommands.add(new AnimationCommand(4, "updateStage"));
					}
					else
					{
						game.getSelectedUnit().endTurn();
						animationCommands = deselect(animationCommands);
					}
				}
				else if(commandNumber == 2)
				{
					if(capture && attack && support)
					{
						lastStage = stage;
						stage = "capture";
						animationCommands.add(new AnimationCommand(4, "updateStage"));
					}
					else if(special && (!attack ^ !support ^ !capture) && (attack || support || capture))
					{
						lastStage = stage;
						stage = "chooseSpecial";
						animationCommands.add(new AnimationCommand(4, "updateStage"));
					}
					else
					{
						game.getSelectedUnit().endTurn();
						animationCommands = deselect(animationCommands);
					}
				}
				else if(commandNumber == 3)
				{
					if(special && attack && support && capture)
					{
						lastStage = stage;
						stage = "chooseSpecial";
						animationCommands.add(new AnimationCommand(4, "updateStage"));
					}
					else
					{
						game.getSelectedUnit().endTurn();
						animationCommands = deselect(animationCommands);
					}
				}
				else if(commandNumber == 4)
				{
					if(special && attack && support && capture)
					{
						game.getSelectedUnit().endTurn();
						animationCommands = deselect(animationCommands);
					}
				}
			}
			else if(commandType == 2)
			{
				animationCommands = tempselect(coordinateNumber, animationCommands);
				if(!stage.equals("tempUnitSelected") || !stage.equals("tempBuildingSelected"))
				{
					menuLocation = coordinateNumber.clone();
				}
			}
		}
		//
		// chooseAttack: Tiles: No building/unit -> chooseAction, Building/Unit -> tempSelect | Menus: attack options -> attack, back -> chooseActionMenu, wait
		else if(stage == "chooseAttack")
		{
			selectedUnit = game.getSelectedUnit();
			if(commandType == 1)
			{
				if(commandNumber >= 0 && commandNumber < selectedUnit.getOffensiveAttackList().length)
				{
					if(game.selectValidAttack(commandNumber))
					{
						game.selectAttack(commandNumber);
						stage = "attack";
						animationCommands.add(new AnimationCommand(4, "updateStage"));
					}
				}
				else if(commandNumber == selectedUnit.getOffensiveAttackList().length)
				{
					stage = "chooseAction";
					animationCommands.add(new AnimationCommand(4, "updateStage"));
				}
				else if(commandNumber == selectedUnit.getOffensiveAttackList().length + 1)
				{
					selectedUnit.endTurn();
					animationCommands = deselect(animationCommands);
				}
			}
			else if(commandType == 2)
			{
				animationCommands = tempselect(coordinateNumber, animationCommands);
				if(!stage.equals("tempUnitSelected") || !stage.equals("tempBuildingSelected"))
				{
					menuLocation = coordinateNumber.clone();
				}
			}
		}
		// attack: Tiles: Select a tile to target, invalid tile without Building/Unit -> attackMenu, invalid with Building/Unit -> tempSelect | Menus: N/A
		else if(stage == "attack")
		{
			if(commandType == 1)
			{
				
			}
			else if(commandType == 2)
			{
				if(game.validTarget(coordinateNumber))
				{
					game.attack(new Location(coordinateNumber));
					animationCommands.add(new AnimationCommand(4, "unitMapRefresh"));
					animationCommands.add(new AnimationCommand(4, "buildingMapRefresh"));
					if(!game.getAttacking())
					{
						animationCommands = deselect(animationCommands);
					}
				}
				else
				{
					animationCommands = tempselect(coordinateNumber, animationCommands);
					if(!stage.equals("tempUnitSelected") || !stage.equals("tempBuildingSelected"))
					{
						stage = "attackMenu";
						menuLocation = coordinateNumber.clone();
					}
				}
			}
		}
		// attackMenu: Tiles: Select a tile to target, invalid tile without Building/Unit -> chooseAttack, invalid with Building/Unit -> tempSelect | Menus: choose attack
		else if(stage == "attackMenu")
		{
			selectedUnit = game.getSelectedUnit();
			if(commandType == 1)
			{
				if(commandNumber == 0 && !game.getAttacking())
				{
					stage = "chooseAttack";
					animationCommands.add(new AnimationCommand(4, "updateStage"));
				}
				else if(commandNumber == 1 || (commandNumber == 0 && game.getAttacking()))
				{
					selectedUnit.endTurn();
					animationCommands = deselect(animationCommands);
				}
			}
			else if(commandType == 2)
			{
				if(game.validTarget(coordinateNumber))
				{
					game.attack(new Location(coordinateNumber));
					animationCommands.add(new AnimationCommand(4, "unitMapRefresh"));
					animationCommands.add(new AnimationCommand(4, "buildingMapRefresh"));
					if(!game.getAttacking())
					{
						animationCommands = deselect(animationCommands);
					}
				}
				else
				{
					animationCommands = tempselect(coordinateNumber, animationCommands);
					if((!stage.equals("tempUnitSelected") || !stage.equals("tempBuildingSelected")) && !game.getAttacking())
					{
						stage = "chooseAttack";
						animationCommands.add(new AnimationCommand(4, "updateStage"));
					}
					else if((!stage.equals("tempUnitSelected") || !stage.equals("tempBuildingSelected")))
					{
						selectedUnit.endTurn();
						animationCommands = deselect(animationCommands);
					}
				}
			}
		}
		//
		// chooseSupport:    Tiles: No building/unit -> chooseActionMenu, Building/Unit -> tempSelect | Menus: attack options -> attack, back -> chooseActionMenu, wait
		else if(stage == "chooseSupport")
		{
			if(commandType == 1)
			{
				if(commandNumber >= 0 && commandNumber < selectedUnit.getSupportList().length)
				{
					if(game.selectValidAttack(commandNumber + selectedUnit.getOffensiveAttackList().length))
					{
						game.selectAttack(commandNumber + selectedUnit.getOffensiveAttackList().length);
						stage = "support";
						animationCommands.add(new AnimationCommand(4, "updateStage"));
					}
				}
				else if(commandNumber == selectedUnit.getSupportList().length)
				{
					stage = "chooseAction";
					animationCommands.add(new AnimationCommand(4, "updateStage"));
				}
				else if(commandNumber == selectedUnit.getSupportList().length + 1)
				{
					selectedUnit.endTurn();
					animationCommands = deselect(animationCommands);
				}
			}
			else if(commandType == 2)
			{
				animationCommands = tempselect(coordinateNumber, animationCommands);
				if(!stage.equals("tempUnitSelected") || !stage.equals("tempBuildingSelected"))
				{
					menuLocation = coordinateNumber.clone();
				}
			}
		}
		// support:          Tiles: Select a tile to target, invalid tile without Building/Unit -> attackMenu, invalid with Building/Unit -> tempSelect | Menus: N/A
		else if(stage == "support")
		{
			if(commandType == 1)
			{
				
			}
			else if(commandType == 2)
			{
				if(game.validTarget(coordinateNumber))
				{
					game.attack(new Location(coordinateNumber));
					animationCommands.add(new AnimationCommand(4, "unitMapRefresh"));
					animationCommands.add(new AnimationCommand(4, "buildingMapRefresh"));
					if(!game.getAttacking())
					{
						animationCommands = deselect(animationCommands);
					}
				}
				else
				{
					animationCommands = tempselect(coordinateNumber, animationCommands);
					if((!stage.equals("tempUnitSelected") || !stage.equals("tempBuildingSelected")))
					{
						stage = "supportMenu";
						menuLocation = coordinateNumber.clone();
					}
				}
			}
		}
		// supportMenu:      Tiles: Select a tile to target, invalid tile without Building/Unit -> chooseAttack, invalid with Building/Unit -> tempSelect | Menus: back -> chooseSupport
		else if(stage == "supportMenu")
		{
			if(commandType == 1)
			{
				if(commandNumber == 0 && !game.getAttacking())
				{
					stage = "chooseSupport";
					animationCommands.add(new AnimationCommand(4, "updateStage"));
				}
				else if(commandNumber == 1 || (commandNumber == 0 && game.getAttacking()))
				{
					animationCommands = deselect(animationCommands);
				}
			}
			else if(commandType == 2)
			{
				if(game.validTarget(coordinateNumber))
				{
					game.attack(new Location(coordinateNumber));
					animationCommands.add(new AnimationCommand(4, "unitMapRefresh"));
					animationCommands.add(new AnimationCommand(4, "buildingMapRefresh"));
					if(!game.getAttacking())
					{
						animationCommands = deselect(animationCommands);
					}
				}
				else
				{
					animationCommands = tempselect(coordinateNumber, animationCommands);
					if((!stage.equals("tempUnitSelected") || !stage.equals("tempBuildingSelected")) && !game.getAttacking())
					{
						stage = "chooseAttack";
						animationCommands.add(new AnimationCommand(4, "updateStage"));
					}
					else if((!stage.equals("tempUnitSelected") || !stage.equals("tempBuildingSelected")))
					{
						selectedUnit.endTurn();
						animationCommands = deselect(animationCommands);
					}
				}
			}
		}
		// 
		// chooseSpecial:    Tiles: No building/unit -> chooseActionMenu, Building/Unit -> tempSelect | Menus: special options -> special (if applicable), back -> chooseActionMenu, wai
		else if(stage == "chooseSpecial")
		{
			if(commandType == 1)
			{
				if(commandNumber >= 0 && commandNumber < selectedUnit.getSpecialList().length)
				{
					if(game.selectValidSpecial(commandNumber))
					{
//						0: AttackType - Like a powerful attack, handled like an attack
//						1: FormChange - Changes the form value of the special unit. Can be discrete or continuous
//						2: CommandType - Has an effect on all of the units on the same side - Commanders only
//						3: ThreatenType? - Has an effect on all enemy units
//						4: UnitSpawner - Spawns units
						game.selectSpecial(commandNumber);
						if(game.getSelectedSpecial().getSpecialType() == 0)
						{
							stage = "special";
							animationCommands.add(new AnimationCommand(4, "updateStage"));
						}
						else if(game.getSelectedSpecial().getSpecialType() == 1)
						{
							stage = "special";
							animationCommands.add(new AnimationCommand(4, "updateStage"));
						}
						else if(game.getSelectedSpecial().getSpecialType() == 2)
						{
							game.special(null, new double[0]);
							game.deselect();
							stage = "notSelected";
							animationCommands.add(new AnimationCommand(4, "unitMapRefresh"));
							animationCommands.add(new AnimationCommand(4, "buildingMapRefresh"));
							animationCommands.add(new AnimationCommand(4, "updateStage"));
						}
						else if(game.getSelectedSpecial().getSpecialType() == 3)
						{
							game.special(null, new double[0]);
							game.deselect();
							stage = "notSelected";
							animationCommands.add(new AnimationCommand(4, "unitMapRefresh"));
							animationCommands.add(new AnimationCommand(4, "buildingMapRefresh"));
							animationCommands.add(new AnimationCommand(4, "updateStage"));
						}
						else if(game.getSelectedSpecial().getSpecialType() == 4)
						{
							stage = "special";
							animationCommands.add(new AnimationCommand(4, "updateStage"));
						}
					}
				}
				else if(commandNumber == selectedUnit.getSpecialList().length)
				{
					stage = "chooseAction";
					animationCommands.add(new AnimationCommand(4, "updateStage"));
				}
				else if(commandNumber == selectedUnit.getSpecialList().length + 1)
				{
					selectedUnit.endTurn();
					animationCommands = deselect(animationCommands);
				}
			}
			else if(commandType == 2)
			{
				animationCommands = tempselect(coordinateNumber, animationCommands);
				if(!stage.equals("tempUnitSelected") || !stage.equals("tempBuildingSelected"))
				{
					menuLocation = coordinateNumber.clone();
				}
			}
		}
		// special:          Tiles: Select a tile to target, invalid tile without Building/Unit -> specialMenu, invalid with Building/Unit -> tempSelect | Menus: N/A
		else if(stage == "special")
		{
			if(commandType == 1)
			{
				
			}
			else if(commandType == 2)
			{
//				0: AttackType - Like a powerful attack, handled like an attack
//				1: FormChange - Changes the form value of the special unit. Can be discrete or continuous
//				2: CommandType - Has an effect on all of the units on the same side - Commanders only
//				3: ThreatenType? - Has an effect on all enemy units
//				4: UnitSpawner - Spawns units
				if(game.getSelectedSpecial().getSpecialType() == 0)
				{
					if(game.validTarget(coordinateNumber, game.getSelectedSpecial().getAttackList()[0]))
					{
						game.special(new Location(coordinateNumber), new double[0]);
						animationCommands.add(new AnimationCommand(4, "unitMapRefresh"));
						animationCommands.add(new AnimationCommand(4, "buildingMapRefresh"));
						if(!game.getAttacking())
						{
							animationCommands = deselect(animationCommands);
						}
					}
					else
					{
						animationCommands = tempselect(coordinateNumber, animationCommands);
						if((!stage.equals("tempUnitSelected") || !stage.equals("tempBuildingSelected")))
						{
							stage = "specialMenu";
						}
					}
				}
				else if(game.getSelectedSpecial().getSpecialType() == 1)
				{
					
				}
				else if(game.getSelectedSpecial().getSpecialType() == 2)
				{
					
				}
				else if(game.getSelectedSpecial().getSpecialType() == 3)
				{
					
				}
				else if(game.getSelectedSpecial().getSpecialType() == 4)
				{
					
				}
			}
		}
		// specialMenu:      Tiles: Select a tile to target, invalid tile without Building/Unit -> chooseSpecial, invalid with Building/Unit -> tempSelect | Menus: back -> chooseSpecial
		else if(stage == "specialMenu")
		{
			if(commandType == 1)
			{
				if(commandNumber == 0 && !game.getAttacking())
				{
					stage = "chooseSpecial";
					animationCommands.add(new AnimationCommand(4, "updateStage"));
				}
				else if(commandNumber == 1 || (commandNumber == 0 && game.getAttacking()))
				{
					selectedUnit.endTurn();
					animationCommands = deselect(animationCommands);
				}
			}
			else if(commandType == 2)
			{
				if(game.getSelectedSpecial().getSpecialType() == 0)
				{
					if(game.validTarget(coordinateNumber, game.getSelectedSpecial().getAttackList()[0]))
					{
						game.special(new Location(coordinateNumber), new double[0]);
						animationCommands.add(new AnimationCommand(4, "unitMapRefresh"));
						animationCommands.add(new AnimationCommand(4, "buildingMapRefresh"));
						if(!game.getAttacking())
						{
							animationCommands = deselect(animationCommands);
						}
					}
					else
					{
						animationCommands = tempselect(coordinateNumber, animationCommands);
						if((!stage.equals("tempUnitSelected") || !stage.equals("tempBuildingSelected")))
						{
							menuLocation = coordinateNumber;
						}
					}
				}
				else if(game.getSelectedSpecial().getSpecialType() == 1)
				{
					
				}
				else if(game.getSelectedSpecial().getSpecialType() == 2)
				{
					
				}
				else if(game.getSelectedSpecial().getSpecialType() == 3)
				{
					
				}
				else if(game.getSelectedSpecial().getSpecialType() == 4)
				{
					
				}
			}
		}
		//
		// capture:          Tiles: Select a tile to target, invalid tile without Building/Unit -> captureMenu, invalid with Building/Unit -> tempSelect | Menus: N/A
		else if(stage == "capture")
		{
			if(commandType == 1)
			{
				
			}
			else if(commandType == 2)
			{
				if(game.validCapture(coordinateNumber))
				{
					game.captureBuilding(coordinateNumber);
					animationCommands = deselect(animationCommands);
					animationCommands.add(new AnimationCommand(4, "buildingMapRefresh"));
				}
				else
				{
					animationCommands = tempselect(coordinateNumber, animationCommands);
					if((!stage.equals("tempUnitSelected") || !stage.equals("tempBuildingSelected")))
					{
						stage = "captureMenu";
					}
				}
			}
		}
		// captureMenu       Tiles: Select a tile to target, invalid tile without Building/Unit -> captureMenu, invalid with Building/Unit -> tempSelect | Menus: back -> chooseAction, wait -> notSelected
		else if(stage == "captureMenu")
		{
			if(commandType == 1)
			{

				if(commandNumber == 0)
				{
					stage = "chooseAction";
					animationCommands.add(new AnimationCommand(4, "updateStage"));
				}
				else if(commandNumber == 1)
				{
					selectedUnit.endTurn();
					animationCommands = deselect(animationCommands);
				}
			}
			else if(commandType == 2)
			{
				if(game.validCapture(coordinateNumber))
				{
					game.captureBuilding(coordinateNumber);
					animationCommands = deselect(animationCommands);
					animationCommands.add(new AnimationCommand(4, "buildingMapRefresh"));
				}
				else
				{
					animationCommands = tempselect(coordinateNumber, animationCommands);
					if((!stage.equals("tempUnitSelected") || !stage.equals("tempBuildingSelected")))
					{
						menuLocation = coordinateNumber;
					}
				}
			}
		}
		// 
		// changeCommander:  Tiles: No building/unit -> moveMenu | Menus: commanderOptions -> notSelected, back -> moveMenu
		else if(stage == "changeCommander")
		{
			if(commandType == 1)
			{
				
			}
			else if(commandType == 2)
			{
				
			}
		}
		// 
		// building:         Tiles: Depends on Building | Menus: Depends on building.
		else if(stage == "building")
		{
			//0: Nothing
			//1: Reactor / Generator - GetsResource
			//2: StrongHold - Special Building For Win Conditions
			//3: Fortress - A Unit of the Same Player can Stay on the Tile and have a Defence Boost
			//4: Barracks/Factory - A Building to Make Units
			//5: Armed Fortress - A Building that can Attack
			//6: Artillery Stations - A Building that has a Powerful Customisable Launcher
			if(commandType == 1)
			{
				if(selectedBuilding.getBuildingType() == 1)
				{
					if(commandNumber == 0)
					{
						selectedBuilding.endTurn();
						animationCommands = deselect(animationCommands);
					}
				}
				else if(selectedBuilding.getBuildingType() == 2)
				{
					if(commandNumber == 0)
					{
						selectedBuilding.endTurn();
						animationCommands = deselect(animationCommands);
					}
				}
				else if(selectedBuilding.getBuildingType() == 3)
				{
					if(commandNumber == 0)
					{
						selectedBuilding.endTurn();
						animationCommands = deselect(animationCommands);
					}
				}
				else if(selectedBuilding.getBuildingType() == 4)
				{
					ArrayList<UnitData> unitAllowedMake = game.getCurrentPlayer().getAllowedUnitList();
					ArrayList<UnitData> unitMake = new ArrayList<UnitData>();
					for(int i = 0; i < unitAllowedMake.size(); i++)
					{
						if(unitAllowedMake.get(i).getUnitMake() == selectedBuilding.getBuildingMake())
						{
							unitMake.add(unitAllowedMake.get(i));
						}
					}
					if(commandNumber >= 0 && commandNumber < unitMake.size())
					{
						if(game.selectedBuildingCanMake(unitMake.get(commandNumber)))
						{
							game.selectedBuildingStartMake(unitMake.get(commandNumber));
							animationCommands = deselect(animationCommands);
							animationCommands.add(new AnimationCommand(4, "unitMapRefresh"));
						}
					}
					if(commandNumber == unitMake.size())
					{
						selectedBuilding.endTurn();
						animationCommands = deselect(animationCommands);
					}
				}
				else if(selectedBuilding.getBuildingType() == 5)
				{
					if(commandNumber == 0)
					{
						selectedBuilding.endTurn();
						animationCommands = deselect(animationCommands);
					}
				}
				else if(selectedBuilding.getBuildingType() == 6)
				{
					if(commandNumber == 0)
					{
						selectedBuilding.endTurn();
						animationCommands = deselect(animationCommands);
					}
				}
				else
				{
					if(commandNumber == 0)
					{
						selectedBuilding.endTurn();
						animationCommands = deselect(animationCommands);
					}
				}
			}
			else if(commandType == 2)
			{
				if(selectedBuilding.getBuildingType() == 1)
				{
					int size = animationCommands.size();
					animationCommands = this.select(coordinateNumber, animationCommands);
					if(size == animationCommands.size())
					{
						menuLocation = coordinateNumber.clone();
					}
				}
				else if(selectedBuilding.getBuildingType() == 2)
				{
					int size = animationCommands.size();
					animationCommands = this.select(coordinateNumber, animationCommands);
					if(size == animationCommands.size())
					{
						menuLocation = coordinateNumber.clone();
					}
				}
				else if(selectedBuilding.getBuildingType() == 3)
				{
					int size = animationCommands.size();
					animationCommands = this.select(coordinateNumber, animationCommands);
					if(size == animationCommands.size())
					{
						menuLocation = coordinateNumber.clone();
					}
				}
				else if(selectedBuilding.getBuildingType() == 4)
				{
					int size = animationCommands.size();
					animationCommands = this.select(coordinateNumber, animationCommands);
					if(size == animationCommands.size())
					{
						menuLocation = coordinateNumber.clone();
					}
				}
				else if(selectedBuilding.getBuildingType() == 5)
				{
					int size = animationCommands.size();
					animationCommands = this.select(coordinateNumber, animationCommands);
					if(size == animationCommands.size())
					{
						menuLocation = coordinateNumber.clone();
					}
				}
				else if(selectedBuilding.getBuildingType() == 6)
				{
					int size = animationCommands.size();
					animationCommands = this.select(coordinateNumber, animationCommands);
					if(size == animationCommands.size())
					{
						menuLocation = coordinateNumber.clone();
					}
				}
				else
				{
					
				}
			}
		}
		return animationCommands;
	}
	
	/**
	 * {@summary Returns the names of the menu options that should appear. }This depends on the state. The menu names are based off the {@link #playerInput(int[], int, int, GamePanel)} function.
	 * 
	 * @return The list of option names that the player has.
	 * @see #playerInput(int[], int, int, GamePanel)
	 */
	public ArrayList<String> playerMenuChoice()
	{
		ArrayList<String> menuOptions = new ArrayList<String>();
		// notSelected       Tiles: Select a tile | Menus: N/A
		if(stage == "notSelected")
		{

		}
		//
		// tileSelected      Tiles: Select another tile | Menus: End Turn
		else if(stage == "tileSelected")
		{
			menuOptions.add("End Turn");
		}
		// enemySelected     Tiles: Select another tile, Same tile -> enemySelectedMenu | Menus: N/A
		else if(stage == "enemyUnitSelected")
		{

		}
		else if(stage == "enemyBuildingSelected")
		{

		}
		// enemySelectedMenu Tiles: Select another tile, Same -> notSelected | Menus: End Turn
		else if(stage == "enemyUnitSelectedMenu")
		{
			menuOptions.add("End Turn");
		}
		else if(stage == "enemyBuildingSelectedMenu")
		{
			menuOptions.add("End Turn");
		}
		// movedSelected:    Tiles: Select another tile, Same tile -> movedSelectedMenu | Menus: N/A
		else if(stage == "movedUnitSelected")
		{

		}
		else if(stage == "movedBuildingSelected")
		{

		}
		// movedSelectedMenu: Tiles: Select another tile Menus: End Turn
		else if(stage == "movedUnitSelectedMenu")
		{
			menuOptions.add("End Turn");
		}
		else if(stage == "movedBuildingSelectedMenu")
		{
			menuOptions.add("End Turn");
		}
		// tempSelect: Tiles: return to main select Menus: N/A
		else if(stage == "tempUnitSelect")
		{

		}
		else if(stage == "tempBuildingSelect")
		{

		}
		// 
		// move: Tiles: Select a tile to move to -> chooseActionMenu, invalid tile without unit/building -> moveMenu, invalid tile with unit/building = tempSelect that unit/building | Menus: N/A
		else if(stage == "move")
		{

		}
		// moveMenu: Tiles: Select a tile to move to -> chooseActionMenu, invalid tile without unit/building -> move, invalid tile with unit/building = tempSelect that unit/building | Menus: End Turn, wait -> chooseActionMenu (changeCommander if applicable);
		else if(stage == "moveMenu")
		{
			menuOptions.add("Wait");
			if(game.getSelectedUnit().getActiveCommander() && game.getCurrentPlayer().getCommanderList().size() > 1)
			{
				menuOptions.add("Swap Commander");
			}
		}
		//
		// chooseActionMenu: Tiles: Building/Unit -> tempSelect | Menus: attack -> chooseAttack, support -> chooseSupport, (special -> chooseSpecial if applicable), wait
		else if(stage == "chooseAction")
		{
			selectedUnit = game.getSelectedUnit();
			boolean attack = selectedUnit.getOffensiveAttackList().length > 0;
			boolean support = selectedUnit.getSupportList().length > 0;
			boolean capture = true;
			boolean special = selectedUnit.isSpecial();
			if(attack)
			{
				menuOptions.add("Attack");
			}
			if(support)
			{
				menuOptions.add("Support");
			}
			if(capture)
			{
				menuOptions.add("Capture");
			}
			if(special)
			{
				menuOptions.add("Special");
			}
			menuOptions.add("Wait");
		}
		//
		// chooseAttack: Tiles: No building/unit -> chooseAction, Building/Unit -> tempSelect | Menus: attack options -> attack, back -> chooseActionMenu, wait
		else if(stage == "chooseAttack")
		{
			selectedUnit = game.getSelectedUnit();
			for(int i = 0; i < selectedUnit.getOffensiveAttackList().length; i++)
			{
				menuOptions.add(selectedUnit.getOffensiveAttack(i).getName());
			}
			menuOptions.add("Back");
			menuOptions.add("Wait");
		}
		// attack: Tiles: Select a tile to target, invalid tile without Building/Unit -> attackMenu, invalid with Building/Unit -> tempSelect | Menus: N/A
		else if(stage == "attack")
		{

		}
		// attackMenu: Tiles: Select a tile to target, invalid tile without Building/Unit -> chooseAttack, invalid with Building/Unit -> tempSelect | Menus: choose attack
		else if(stage == "attackMenu")
		{
			if(!game.getAttacking())
			{
				menuOptions.add("Back");
			}
			menuOptions.add("Wait");
		}
		//
		// chooseSupport:    Tiles: No building/unit -> chooseActionMenu, Building/Unit -> tempSelect | Menus: attack options -> attack, back -> chooseActionMenu, wait
		else if(stage == "chooseSupport")
		{
			selectedUnit = game.getSelectedUnit();
			for(int i = 0; i < selectedUnit.getSupportList().length; i++)
			{
				menuOptions.add(selectedUnit.getSupport(i).getName());
			}
			menuOptions.add("Back");
			menuOptions.add("Wait");
		}
		// support:          Tiles: Select a tile to target, invalid tile without Building/Unit -> attackMenu, invalid with Building/Unit -> tempSelect | Menus: N/A
		else if(stage == "support")
		{

		}
		// supportMenu:      Tiles: Select a tile to target, invalid tile without Building/Unit -> chooseAttack, invalid with Building/Unit -> tempSelect | Menus: back -> chooseSupport
		else if(stage == "supportMenu")
		{
			if(!game.getAttacking())
			{
				menuOptions.add("Back");
			}
			menuOptions.add("Wait");
		}
		// 
		// chooseSpecial:    Tiles: No building/unit -> chooseActionMenu, Building/Unit -> tempSelect | Menus: special options -> special (if applicable), back -> chooseActionMenu, wai
		else if(stage == "chooseSpecial")
		{
			selectedUnit = game.getSelectedUnit();
			for(int i = 0; i < selectedUnit.getSpecialList().length; i++)
			{
				menuOptions.add(selectedUnit.getSpecialList()[i].getName());
			}
			menuOptions.add("Back");
			menuOptions.add("Wait");
		}
		// special:          Tiles: Select a tile to target, invalid tile without Building/Unit -> specialMenu, invalid with Building/Unit -> tempSelect | Menus: N/A
		else if(stage == "special")
		{

		}
		// specialMenu:      Tiles: Select a tile to target, invalid tile without Building/Unit -> chooseSpecial, invalid with Building/Unit -> tempSelect | Menus: back -> chooseSpecial
		else if(stage == "specialMenu")
		{
			if(!game.getAttacking())
			{
				menuOptions.add("Back");
			}
			menuOptions.add("Wait");
		}
		//
		// capture:          Tiles: Select a tile to target, invalid tile without Building/Unit -> captureMenu, invalid with Building/Unit -> tempSelect | Menus: N/A
		else if(stage == "capture")
		{

		}
		// captureMenu       Tiles: Select a tile to target, invalid tile without Building/Unit -> captureMenu, invalid with Building/Unit -> tempSelect | Menus: back -> chooseAction, wait -> notSelected
		else if(stage == "captureMenu")
		{
			menuOptions.add("Back");
			menuOptions.add("Wait");
		}
		// 
		// changeCommander:  Tiles: No building/unit -> moveMenu | Menus: commanderOptions -> notSelected, back -> moveMenu
		else if(stage == "changeCommander")
		{

		}
		// 
		// building:         Tiles: Depends on Building | Menus: Depends on building.
		else if(stage == "building")
		{
			if(selectedBuilding.getBuildingType() == 1)
			{
				menuOptions.add("Wait");
			}
			else if(selectedBuilding.getBuildingType() == 2)
			{
				menuOptions.add("Wait");
			}
			else if(selectedBuilding.getBuildingType() == 3)
			{
				menuOptions.add("Wait");
			}
			else if(selectedBuilding.getBuildingType() == 4)
			{
				ArrayList<UnitData> unitAllowedMake = game.getCurrentPlayer().getAllowedUnitList();
				for(int i = 0; i < unitAllowedMake.size(); i++)
				{
					if(unitAllowedMake.get(i).getUnitMake() == selectedBuilding.getBuildingMake())
					{
						menuOptions.add(unitAllowedMake.get(i).getName());
					}
				}
				menuOptions.add("Wait");
			}
			else if(selectedBuilding.getBuildingType() == 5)
			{

				menuOptions.add("Wait");
			}
			else if(selectedBuilding.getBuildingType() == 6)
			{

				menuOptions.add("Wait");
			}
			else
			{
				menuOptions.add("Wait");
			}
		}
		return menuOptions;
	}
	
	/**
	 * Responds to user input and calls the relevant game functions. Handles all of the user input validation. 
	 * 
	 * @deprecated Deprecated due to using an out-of-date interaction system. Replaced by #playerInput(int[], int, int, GamePanel).
	 * @param coordinateNumber The coordinates that the user has inputed.
	 * @param commandNumber The number that the user inputed.
	 * @param commandType Whether the input was a number or a coordinate. 
	 * @param gamePanel The GamePanel through which the user inputed the information
	 * @return Returns a list of animation commands for the gamePanel to perform. 
	 * @see #playerInput(int[], int, int, GamePanel)
	 */
	@Deprecated
	public ArrayList<AnimationCommand> playerAction(int[] coordinateNumber, int commandNumber, int commandType, GamePanel gamePanel)
	{
		ArrayList<AnimationCommand> animationCommands = new ArrayList<AnimationCommand>();
		if(commandNumber == 0 && commandType == 1)
		{
			selectedUnit = null;
			selectedBuilding = null;
			game.advanceTurn();
		}
		else if(game.getUnitSelected())
		{
			Unit selectedUnit = game.getSelectedUnit();
			if (stage.equals("move"))
			{
				try
				{
					if (commandNumber == 1 && commandType == 1)
					{
						game.deselect();
					}
					else if (commandNumber == 2 && commandType == 1)
					{
						if(game.selectedUnitUpdateValidMove(selectedUnit.getLocation()))
						{
							game.moveSelectedUnit(selectedUnit.getLocation().getLocation());
							stage = "chooseattack";
						}
					}
					else if (commandNumber == 3 && commandType == 1 && game.getSelectedUnit().getActiveCommander())
					{
						stage = "changecommander";
					}
					else
					{
						if(game.selectedUnitUpdateValidMove(coordinateNumber) && commandType == 2)
						{
							game.moveSelectedUnit(coordinateNumber);
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
				if (0 < commandNumber && commandNumber < selectedUnit.getAttackList().length + 1 && game.selectValidAttack(commandNumber - 1) && commandType == 1)
				{
					game.selectAttack(commandNumber - 1);
					stage = "attack";
				}
				else if (selectedUnit.isSpecial())
				{
					if (selectedUnit.getAttackList().length < commandNumber && commandNumber < selectedUnit.getSpecialList().length + selectedUnit.getAttackList().length + 1 && game.selectValidSpecial(commandNumber - 1 - selectedUnit.getAttackList().length) && commandType == 1)
					{
						game.selectSpecial(commandNumber - 1 - selectedUnit.getAttackList().length);
						stage = "special";
					}
					else if (commandNumber == selectedUnit.getAttackList().length + selectedUnit.getSpecialList().length + 1 && commandType == 1)
					{
						game.deselect();
					}
					else if (commandNumber == selectedUnit.getAttackList().length + selectedUnit.getSpecialList().length + 2 && commandType == 1)
					{
						stage = "capture";
					}
					else System.out.println("Invalid Special");
				}
				else if (commandNumber == selectedUnit.getAttackList().length + 1 && commandType == 1)
				{
					game.deselect();
				}
				else if (commandNumber == selectedUnit.getAttackList().length + 2 && commandType == 1)
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
					if(game.validCapture(coordinateNumber))
					{
						game.captureBuilding(coordinateNumber);
					}
					else
					{
						System.out.println("Invalid Building to Capture");
					}
				}
				if(commandNumber == 1 && commandType == 1)
				{
					game.deselect();
				}
				if(commandNumber == 2 && commandType == 1)
				{
					stage = "chooseattack";
				}
			}
			else if (stage.equals("attack"))
			{
				if (commandNumber == 1 && commandType == 1)
				{
					if(game.getAttacking())
					{
						game.getSelectedUnit().endAttack();
						game.endAttack();
					}
					else
					{
						game.getSelectedUnit().removeAttackNumber(game.getSelectedAttack());
						game.endAttack();
					}
					game.deselect();
				}
				else if(commandNumber == 2 && commandType == 1)
				{
					if(!game.getAttacking())
					{
						game.getSelectedUnit().removeAttackNumber(game.getSelectedAttack());
						game.getSelectedUnit().endAttack();
						stage = "chooseattack";
					}
				}
				else
				{
					if(game.validTarget(coordinateNumber) && commandType == 2)
					{
						game.attack(new Location(coordinateNumber));
						if(!game.getUnitSelected())
						{
						}
					}
					else
					{
						System.out.println("Invalid Unit To Attack");
						if (!game.getAttacking())
						{
							stage = "chooseattack";
						}
					}
				}
			}
			else if(stage.equals("changecommander"))
			{
				if(commandNumber - 1 >= 0 && commandNumber - 1 < game.getCurrentPlayer().getCommanderList().size() && commandType == 1)
				{
					if(game.setActiveCommanderReturn(commandNumber - 1))
					{
						game.deselect();
					}
					else
					{
						stage = "move";
					}
				}
				else if(commandNumber == game.getCurrentPlayer().getCommanderList().size() + 1 && commandType == 1)
				{
					game.deselect();
				}
				else if(commandNumber == game.getCurrentPlayer().getCommanderList().size() + 2 && commandType == 1)
				{
					stage = "move";
				}
			}
			else if(stage.equals("special"))
			{
				Special selectedSpecial = game.getSelectedSpecial();
				if(selectedSpecial.getSpecialType() == 0)
				{
					if (commandNumber == 1 && commandType == 1)
					{
						if(game.getAttacking())
						{
							game.getSelectedUnit().endAttack();
							game.endAttack();
						}
						else
						{
							game.getSelectedUnit().removeAttackNumber(game.getSelectedAttack());
							game.endAttack();
						}
						game.deselect();
					}
					else if(commandNumber == 2 && commandType == 1)
					{
						if(!game.getAttacking())
						{
							game.getSelectedUnit().removeAttackNumber(game.getSelectedAttack());
							game.getSelectedUnit().endAttack();
							stage = "chooseattack";
						}
					}
					else
					{
						if(game.validTarget(coordinateNumber, game.getSelectedSpecial().getAttackList()[0]))
						{
							game.special(new Location(coordinateNumber), new double[0]);
						}
						else
						{
							System.out.println("Invalid Unit To Attack");
							if (!game.getAttacking())
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
					if (commandNumber == 1 && commandType == 1)
					{
						game.deselect();
					}
					else if(commandNumber == 2 && commandType == 1)
					{
						stage = "chooseattack";
					}
					else if(commandNumber == 3 && commandType == 1)
					{
						game.special(new Location(), new double[0]);
						game.deselect();
					}
				}
				else if(selectedSpecial.getSpecialType() == 3)
				{
					if (commandNumber == 1 && commandType == 1)
					{
						game.deselect();
					}
					else if(commandNumber == 2 && commandType == 1)
					{
						stage = "chooseattack";
					}
					else if(commandNumber == 3 && commandType == 1)
					{
						game.special(new Location(), new double[0]);
						game.deselect();
					}
				}
				else if(selectedSpecial.getSpecialType() == 4)
				{
					
				}
			}
			else if(stage.equals("nill"))
			{
				if (commandNumber == 1 && commandType == 1)
				{
					game.deselect();
				}
			}
		}
		else if(game.getBuildingSelected())
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
				if(commandNumber == 1 && commandType == 1)
				{
					game.deselect();
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
						if(game.selectedBuildingCanMake(commandNumber - 2))
						{
							game.selectedBuildingStartMake(commandNumber - 2);
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
				if (commandNumber == 1 && commandType == 1)
				{
					game.deselect();
				}
			}
		}
		else if(game.getTileSelected())
		{
			if(stage.equals("nill"))
			{
				if (commandNumber == 1 && commandType == 1)
				{
					game.deselect();
				}
			}
		}
		else
		{
			try
			{
				game.selectUnit(commandNumber - 1);
				Unit selectedUnit = game.getSelectedUnit();
				if(selectedUnit.getPlayer() == game.getCurrentTurn() && this.selectedBuilding != null)
				{
					if(selectedUnit != this.selectedBuilding && !stage.equals("building"))
					{
						this.selectedBuilding.endTurn();
					}
				}
				else if(selectedUnit.getPlayer() == game.getCurrentTurn() && this.selectedUnit != null)
				{
					if (selectedUnit != this.selectedUnit && !stage.equals("move"))
					{
						this.selectedUnit.endTurn();
					}
				}
				this.selectedUnit = game.getSelectedUnit();
				this.selectedBuilding = null;
				if (selectedUnit.getPlayer() == game.getCurrentTurn())
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
						game.select(coordinateNumber);
					}
					if(game.getUnitSelected())
					{
						Unit selectedUnit = game.getSelectedUnit();
						if(selectedUnit.getPlayer() == game.getCurrentTurn() && this.selectedBuilding != null)
						{
							if(selectedUnit != this.selectedBuilding && !stage.equals("building"))
							{
								this.selectedBuilding.endTurn();
							}
						}
						else if(selectedUnit.getPlayer() == game.getCurrentTurn() && this.selectedUnit != null)
						{
							if (selectedUnit != this.selectedUnit && !stage.equals("move"))
							{
								this.selectedUnit.endTurn();
							}
						}
						this.selectedUnit = game.getSelectedUnit();
						this.selectedBuilding = null;
						if (selectedUnit.getPlayer() == game.getCurrentTurn())
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
					else if(game.getBuildingSelected())
					{
						Building selectedBuilding = game.getSelectedBuilding();
						if(selectedBuilding.getPlayer() == game.getCurrentTurn() && this.selectedBuilding != null)
						{
							if(selectedBuilding != this.selectedBuilding && !stage.equals("building"))
							{
								this.selectedBuilding.endTurn();
							}
						}
						else if(selectedBuilding.getPlayer() == game.getCurrentTurn() && this.selectedUnit != null)
						{
							if (selectedBuilding != this.selectedUnit && !stage.equals("move"))
							{
								this.selectedUnit.endTurn();
							}
						}
						this.selectedUnit = null;
						this.selectedBuilding = game.getSelectedBuilding();
						if (selectedBuilding.getPlayer() == game.getCurrentTurn())
						{
							if (!selectedBuilding.getAttacked())
							{
								stage = "building";
								menuLocation = coordinateNumber;
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
							game.selectBuilding(commandNumber - game.getCurrentPlayer().getNumberOfUnit() - 1);
							Building selectedBuilding = game.getSelectedBuilding();
							if(selectedBuilding.getPlayer() == game.getCurrentTurn() && this.selectedBuilding != null)
							{
								if(selectedBuilding != this.selectedBuilding && !stage.equals("building"))
								{
									this.selectedBuilding.endTurn();
								}
							}
							this.selectedUnit = null;
							this.selectedBuilding = game.getSelectedBuilding();
							if (selectedBuilding.getPlayer() == game.getCurrentTurn())
							{
								if (!selectedBuilding.getAttacked())
								{
									stage = "building";
									menuLocation = coordinateNumber;
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
		return animationCommands;
	}
	
	public TileInterface getSelectedTile()
	{
		return selectedTile;
	}
	
	public Unit getSelectedUnit()
	{
		return selectedUnit;
	}
	
	public Building getSelectedBuilding()
	{
		return selectedBuilding;
	}
	
	public String getStage()
	{
		return stage;
	}
	
	public Game getGame()
	{
		return game;
	}
	
	public int[] getMenuLocation()
	{
		return menuLocation;
	}
	
	public void setStage(String stage)
	{
		this.stage = stage;
	}
	
	public ArrayList<AnimationCommand> select(int[] coordinateNumber, ArrayList<AnimationCommand> animationCommands)
	{
		game.deselect();
		game.select(coordinateNumber);
		selectedTile = game.getSelectedTile();
		if(game.getUnitSelected())
		{
			selectedUnit = game.getSelectedUnit();
			if(selectedUnit.getPlayer() != game.getCurrentTurn())
			{
				lastStage = stage;
				stage = "enemyUnitSelected";
				animationCommands.add(new AnimationCommand(4, "updateStage"));
				animationCommands.add(new AnimationCommand(4, "updateUnitInfo"));
				animationCommands.add(new AnimationCommand(4, "clearBuildingInfo"));
			}
			else if(selectedUnit.getMoved())
			{
				lastStage = stage;
				stage = "movedUnitSelected";
				selectedUnit.endTurn();
				animationCommands.add(new AnimationCommand(4, "updateStage"));
				animationCommands.add(new AnimationCommand(4, "updateUnitInfo"));
				animationCommands.add(new AnimationCommand(4, "clearBuildingInfo"));
				animationCommands.add(new AnimationCommand(4, "updateTileInfo"));
			}
			else if(!selectedUnit.getMoved())
			{
				lastStage = stage;
				stage = "move";
				animationCommands.add(new AnimationCommand(4, "updateStage"));
				animationCommands.add(new AnimationCommand(4, "updateUnitInfo"));
				animationCommands.add(new AnimationCommand(4, "clearBuildingInfo"));
				animationCommands.add(new AnimationCommand(4, "updateTileInfo"));
			}
		}
		else if(game.getBuildingSelected())
		{
			selectedBuilding = game.getSelectedBuilding();
			if(selectedBuilding.getPlayer() != game.getCurrentTurn())
			{
				lastStage = stage;
				stage = "enemyBuildingSelected";
				animationCommands.add(new AnimationCommand(4, "updateStage"));
				animationCommands.add(new AnimationCommand(4, "updateBuildingInfo"));
				animationCommands.add(new AnimationCommand(4, "clearUnitInfo"));
				animationCommands.add(new AnimationCommand(4, "updateTileInfo"));
			}
			else if(selectedBuilding.getAttacked())
			{
				lastStage = stage;
				stage = "movedBuildingSelected";
				selectedBuilding.endTurn();
				animationCommands.add(new AnimationCommand(4, "updateStage"));
				animationCommands.add(new AnimationCommand(4, "updateBuildingInfo"));
				animationCommands.add(new AnimationCommand(4, "clearUnitInfo"));
				animationCommands.add(new AnimationCommand(4, "updateTileInfo"));
			}
			else if(!selectedBuilding.getAttacked())
			{
				lastStage = stage;
				stage = "building";
				menuLocation = coordinateNumber;
				animationCommands.add(new AnimationCommand(4, "updateStage"));
				animationCommands.add(new AnimationCommand(4, "updateBuildingInfo"));
				animationCommands.add(new AnimationCommand(4, "clearUnitInfo"));
				animationCommands.add(new AnimationCommand(4, "updateTileInfo"));
			}
		}
		else if(game.getTileSelected())
		{
			lastStage = stage;
			stage = "tileSelected";
			menuLocation = coordinateNumber.clone();
			animationCommands.add(new AnimationCommand(4, "updateStage"));
			animationCommands.add(new AnimationCommand(4, "clearUnitInfo"));
			animationCommands.add(new AnimationCommand(4, "clearBuildingInfo"));
			animationCommands.add(new AnimationCommand(4, "updateTileInfo"));
		}
		return animationCommands;
	}
	
	public ArrayList<AnimationCommand> deselect(ArrayList<AnimationCommand> animationCommands)
	{
		lastStage = stage;
		stage = "notSelected";
		game.deselect();
		selectedUnit = null;
		selectedBuilding = null;
		selectedTile = null;
		animationCommands.add(new AnimationCommand(4, "updateStage"));
		animationCommands.add(new AnimationCommand(4, "clearUnitInfo"));
		animationCommands.add(new AnimationCommand(4, "clearBuildingInfo"));
		animationCommands.add(new AnimationCommand(4, "clearTileInfo"));
		return animationCommands;
	}
	
	public ArrayList<AnimationCommand> tempselect(int[] coordinateNumber, ArrayList<AnimationCommand> animationCommands)
	{
		if(game.getUnitTileMap().getMap(coordinateNumber[0], coordinateNumber[1]) != null)
		{
			lastStage = stage;
			selectedUnit = game.getUnitTileMap().getMap(coordinateNumber[0], coordinateNumber[1]);
			selectedTile = game.getTileMap().getMap(coordinateNumber[0], coordinateNumber[1]);
			stage = "tempUnitSelect";
			animationCommands.add(new AnimationCommand(4, "updateStage"));
			animationCommands.add(new AnimationCommand(4, "updateTileInfo"));
			animationCommands.add(new AnimationCommand(4, "updateUnitInfo"));
		}
		else if(game.getBuildingTileMap().getMap(coordinateNumber[0], coordinateNumber[1]) != null)
		{
			lastStage = stage;
			selectedBuilding = game.getBuildingTileMap().getMap(coordinateNumber[0], coordinateNumber[1]);
			selectedTile = game.getTileMap().getMap(coordinateNumber[0], coordinateNumber[1]);
			stage = "tempBuildingSelect";
			animationCommands.add(new AnimationCommand(4, "updateStage"));
			animationCommands.add(new AnimationCommand(4, "updateTileInfo"));
			animationCommands.add(new AnimationCommand(4, "updateBuildingInfo"));
		}
		return animationCommands;
	}
}
