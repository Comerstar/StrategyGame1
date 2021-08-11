package game;

import java.util.ArrayList;

import attack.*;
import unit.*;
import terrain.*;
import map.*;

public class Player {
	
	private int turnNumber;
	private String name;
	private ArrayList<Unit> unitList;
	private ArrayList<UnitData> allowedUnitList;
	private ArrayList<SpecialUnit> specialUnitList;
	private ArrayList<Commander> commanderList;
	private ArrayList<Building> buildingList;
	private ArrayList<Integer> buildingNumberList;
	
	//Used for default setups. -1 is always custom.
	private int playerNumber;
	private double resource;
	private boolean defeated;
	private Commander activeCommander;
	private boolean activelyCommanded;
	private int activeCommanderID;
	
	public Player (int playerNumber, int turnNumber)
	{
		this.playerNumber = playerNumber;
		this.turnNumber = turnNumber;
		defeated = false;
	}
	
	public Player (ArrayList<Unit> unitList, int turnNumber, ArrayList<UnitData> allowedUnitList, String name, TileMap tileMap)
	{
		this.unitList = unitList;
		this.allowedUnitList = allowedUnitList;
		this.resource = 0;
		this.turnNumber = turnNumber;
		this.name = name; 
		playerNumber = -1;
		defeated = false;
		specialUnitList = new ArrayList<SpecialUnit>();
		commanderList = new ArrayList<Commander>();
		makeSpecialUnitAndCommanderList();
		if(commanderList.size() > 0)
		{
			setActiveCommander(0);
		}
		else
		{
			removeActiveCommander();
		}
		this.buildingList = new ArrayList<Building>();
		this.buildingList = tileMap.makeBuildingList(buildingList, playerNumber);
	}
	
	public Player (ArrayList<Unit> unitList, int[] allowedUnitList, int turnNumber, String name, TileMap tileMap)
	{
		this.unitList = unitList;
		this.allowedUnitList = UnitData.makeUnitArrayList(allowedUnitList);
		this.resource = 0;
		this.turnNumber = turnNumber;
		this.name = name; 
		playerNumber = -1;
		defeated = false;
		specialUnitList = new ArrayList<SpecialUnit>();
		commanderList = new ArrayList<Commander>();
		makeSpecialUnitAndCommanderList();
		if(commanderList.size() > 0)
		{
			setActiveCommander(0);
		}
		else
		{
			removeActiveCommander();
		}
		this.buildingList = new ArrayList<Building>();
		this.buildingList = tileMap.makeBuildingList(buildingList, playerNumber);
	}
	
	public Player (ArrayList<Unit> unitList, ArrayList<Integer> allowedUnitList, int turnNumber, String name, TileMap tileMap)
	{
		this.unitList = unitList;
		this.allowedUnitList = UnitData.makeUnitArrayList(allowedUnitList);
		this.resource = 0;
		this.turnNumber = turnNumber;
		this.name = name; 
		playerNumber = -1;
		defeated = false;
		specialUnitList = new ArrayList<SpecialUnit>();
		commanderList = new ArrayList<Commander>();
		makeSpecialUnitAndCommanderList();
		if(commanderList.size() > 0)
		{
			setActiveCommander(0);
		}
		else
		{
			removeActiveCommander();
		}
		this.buildingList = new ArrayList<Building>();
		this.buildingList = tileMap.makeBuildingList(buildingList, playerNumber);
	}
	
	public Player (ArrayList<Unit> unitList, ArrayList<Integer> allowedUnitList, int turnNumber, String name)
	{
		this.unitList = unitList;
		this.allowedUnitList = UnitData.makeUnitArrayList(allowedUnitList);
		this.resource = 0;
		this.turnNumber = turnNumber;
		this.name = name; 
		playerNumber = -1;
		defeated = false;
		specialUnitList = new ArrayList<SpecialUnit>();
		commanderList = new ArrayList<Commander>();
		makeSpecialUnitAndCommanderList();
		if(commanderList.size() > 0)
		{
			setActiveCommander(0);
		}
		else
		{
			removeActiveCommander();
		}
		this.buildingList = new ArrayList<Building>();
	}
	
	public Player (ArrayList<Unit> unitList, int turnNumber, String name, TileMap tileMap)
	{
		this.unitList = unitList;
		this.turnNumber = turnNumber;
		this.name = name;
		defeated = false;
		specialUnitList = new ArrayList<SpecialUnit>();
		commanderList = new ArrayList<Commander>();
		makeSpecialUnitAndCommanderList();
		if(commanderList.size() > 0)
		{
			setActiveCommander(0);
		}
		else
		{
			removeActiveCommander();
		}
		this.buildingList = new ArrayList<Building>();
		this.buildingList = tileMap.makeBuildingList(buildingList, playerNumber);
	}
	
	public ArrayList<Building> getBuildingList()
	{
		return buildingList;
	}
	
	public Building getBuildingList(int i)
	{
		return buildingList.get(i);
	}
	
	public ArrayList<Integer> getBuildingNumberList()
	{
		return buildingNumberList;
	}
	
	public ArrayList<SpecialUnit> getSpecialUnitList()
	{
		return specialUnitList;
	}
	
	public boolean getActivelyCommanded()
	{
		return activelyCommanded;
	}
	
	public SpecialUnit getSpecialUnitList(int i)
	{
		return specialUnitList.get(i);
	}
	
	public ArrayList<Commander> getCommanderList()
	{
		return commanderList;
	}
	
	public Commander getCommanderList(int i)
	{
		return commanderList.get(i);
	}
	
	public Commander getActiveCommander()
	{
		return activeCommander;
	}
	
	public boolean getDefeated()
	{
		return defeated;
	}
	
	public double getResource()
	{
		return resource;
	}
	
	public String getName()
	{
		return name;
	}
	
	public int getTurnNumber()
	{
		return turnNumber;
	}
	
	public ArrayList<Unit> getUnitList()
	{
		return unitList;
	}
	
	public Unit getUnit(int i)
	{
		return unitList.get(i);
	}
	
	public int getNumberOfUnit()
	{
		return unitList.size();
	}
	
	public ArrayList<UnitData> getAllowedUnitList()
	{
		return allowedUnitList;
	}
	
	public UnitData getAllowedUnitList(int i)
	{
		return allowedUnitList.get(i);
	}
	
	public int getPlayerNumber()
	{
		return playerNumber;
	}
	
	public void addFinishedUnit(Building selectedBuilding)
	{
		if(selectedBuilding.getBuildingType() == 4)
		{
			if(selectedBuilding.getBuildingData(2) == 2.0)
			{
				addUnit(selectedBuilding.finishMaking());
			}
		}
	}
	
	public void addFinishedUnit(Building selectedBuilding, UnitMap unitMap)
	{
		if(selectedBuilding.getBuildingType() == 4)
		{
			System.out.println(selectedBuilding.getBuildingData(2));
			if(selectedBuilding.getBuildingData(2) == 2.0)
			{
				Unit unit = selectedBuilding.finishMaking();
				addUnit(unit);
				unitMap.updateMapAdd(unit);
			}
		}
	}
	
	public void addUnit(Unit unit)
	{
		unit.setUnitID(unitList.size());
		unitList.add(unit);
	}
	
	public void useResource(double amount)
	{
		resource -= amount;
	}
	
	public void addBuilding(Building building)
	{
		building.setUnitID(buildingList.size());
		buildingList.add(building);
	}
	
	public void captureBuilding(Building building, Unit unit)
	{
		building.setUnitID(buildingList.size());
		building.capture(unit);
		buildingList.add(building);
	}
	
	public void changeActiveCommanderUnitList(int unitID)
	{
		if(unitList.get(unitID).getIsSpecial() == 2)
		{
			activeCommander.setInactiveCommander();
			activeCommander = unitList.get(unitID).getCommander();
			activeCommander.setActiveCommander();
			activeCommanderID = activeCommander.getUnitID();
			activelyCommanded = true;
		}
	}
	
	public void changeActiveCommanderSpecialUnitList(int unitID)
	{
		if(specialUnitList.get(unitID).getIsSpecial() == 2)
		{
			activeCommander.setInactiveCommander();
			activeCommander = specialUnitList.get(unitID).getCommander();
			activeCommander.setActiveCommander();
			activeCommanderID = activeCommander.getUnitID();
			activelyCommanded = true;
		}
	}
	
	public void changeActiveCommander(int unitID)
	{
		activeCommander.setInactiveCommander();
		activeCommander = commanderList.get(unitID);
		activeCommander.setActiveCommander();
		activeCommanderID = activeCommander.getUnitID();
		activelyCommanded = true;
	}
	
	public void changeActiveCommanderUnitList(int unitID, Player[] playerList)
	{
		if(unitList.get(unitID).getIsSpecial() == 2)
		{
			removeCommandEffect(playerList);
			activeCommander.setInactiveCommander();
			activeCommander = unitList.get(unitID).getCommander();
			activeCommander.setActiveCommander();
			activeCommanderID = activeCommander.getUnitID();
			activelyCommanded = true;
			addCommandEffect(playerList);
		}
	}
	
	public void changeActiveCommanderSpecialUnitList(int unitID, Player[] playerList)
	{
		if(specialUnitList.get(unitID).getIsSpecial() == 2)
		{
			removeCommandEffect(playerList);
			activeCommander.setInactiveCommander();
			activeCommander = specialUnitList.get(unitID).getCommander();
			activeCommander.setActiveCommander();
			activeCommanderID = activeCommander.getUnitID();
			activelyCommanded = true;
			addCommandEffect(playerList);
		}
	}
	
	public void changeActiveCommander(int unitID, Player[] playerList)
	{
		removeCommandEffect(playerList);
		activeCommander.setInactiveCommander();
		activeCommander = commanderList.get(unitID);
		activeCommander.setActiveCommander();
		activeCommanderID = activeCommander.getUnitID();
		activelyCommanded = true;
		addCommandEffect(playerList);
	}
	
	public void setActiveCommanderUnitList(int unitID)
	{
		if(unitList.get(unitID).getIsSpecial() == 2)
		{
			activeCommander = unitList.get(unitID).getCommander();
			activeCommander.setActiveCommander();
			activelyCommanded = true;
			activeCommanderID = activeCommander.getUnitID();
		}
	}
	
	public void setActiveCommanderSpecialUnitList(int unitID)
	{
		if(specialUnitList.get(unitID).getIsSpecial() == 2)
		{
			activeCommander = specialUnitList.get(unitID).getCommander();
			activeCommander.setActiveCommander();
			activelyCommanded = true;
			activeCommanderID = activeCommander.getUnitID();
		}
	}
	
	public void setActiveCommander(int unitID)
	{
		activeCommander = commanderList.get(unitID);
		activeCommander.setActiveCommander();
		activelyCommanded = true;
		activeCommanderID = activeCommander.getUnitID();
	}
	
	public void setActiveCommanderUnitList(int unitID, Player[] playerList)
	{
		if(unitList.get(unitID).getIsSpecial() == 2)
		{
			activeCommander = unitList.get(unitID).getCommander();
			activeCommander.setActiveCommander();
			activelyCommanded = true;
			activeCommanderID = activeCommander.getUnitID();
			addCommandEffect(playerList);
		}
	}
	
	public void setActiveCommanderSpecialUnitList(int unitID, Player[] playerList)
	{
		if(specialUnitList.get(unitID).getIsSpecial() == 2)
		{
			activeCommander = specialUnitList.get(unitID).getCommander();
			activeCommander.setActiveCommander();
			activelyCommanded = true;
			activeCommanderID = activeCommander.getUnitID();
			addCommandEffect(playerList);
		}
	}
	
	public void setActiveCommander(int unitID, Player[] playerList)
	{
		activeCommander = commanderList.get(unitID);
		activeCommander.setActiveCommander();
		activelyCommanded = true;
		activeCommanderID = activeCommander.getUnitID();
		addCommandEffect(playerList);
	}
	
	public void removeActiveCommander()
	{
		activeCommanderID = -1;
		activeCommander = null;
		activelyCommanded = false;
	}
	
	public void removeActiveCommander(Player[] playerList)
	{
		removeCommandEffect(playerList);
		activeCommanderID = -1;
		activeCommander = null;
		activelyCommanded = false;
	}
	
	public void removeBuilding(int buildingID)
	{
		buildingList.remove(buildingID);
		for (int i = buildingID; i < buildingList.size(); i++)
		{
			//buildingList.get(i).setBuildingID(i);
			buildingList.get(i).setUnitID(i);
		}
	}
	
	public void removeUnit(int unitID)
	{
		if(unitList.get(unitID).isSpecial())
		{
			removeSpecialUnit(unitList.get(unitID).getSpecialUnit());
		}
		unitList.remove(unitID);
		for (int i = unitID - 1; i < unitList.size(); i++)
		{
			unitList.get(i).setUnitID(i);
		}
	}
	
	public void removeSpecialUnit(int unitID)
	{
		specialUnitList.remove(unitList.get(unitID).getSpecialUnit());
	}
	
	public void removeCommander(int unitID)
	{
		if(unitID == activeCommanderID)
		{
			removeActiveCommander();
		}
		commanderList.remove(unitList.get(unitID).getCommander());
	}
	
	public void removeUnit(int unitID, Player[] playerList)
	{
		if(unitList.get(unitID).isSpecial())
		{
			removeSpecialUnit(unitID, playerList);
		}
		unitList.remove(unitID);
		for (int i = unitID; i < unitList.size(); i++)
		{
			unitList.get(i).setUnitID(i);
		}
	}
	
	public void removeSpecialUnit(int unitID, Player[] playerList)
	{
		if(unitList.get(unitID).getIsSpecial() == 2)
		{
			removeCommander(unitID, playerList);
		}
		specialUnitList.remove(unitList.get(unitID).getSpecialUnit());
	}
	
	public void removeCommander(int unitID, Player[] playerList)
	{
		if(unitList.get(unitID).getActiveCommander())
		{
			removeActiveCommander(playerList);
		}
		commanderList.remove(unitList.get(unitID).getCommander());
	}
	
	public void removeUnit(Unit unit)
	{
		int unitID = unit.getUnitID();
		unitList.remove(unit);
		if(unit.isSpecial())
		{
			removeSpecialUnit(unit.getSpecialUnit());
		}
		for (int i = unitID; i < unitList.size(); i++)
		{
			unitList.get(i).setUnitID(i);
		}
	}
	
	public void removeSpecialUnit(SpecialUnit unit)
	{
		specialUnitList.remove(unit);
		if(unit.getIsSpecial() == 2)
		{
			removeCommander(unit.getCommander());
		}
	}
	
	public void removeCommander(Commander unit)
	{
		if(unit.getActiveCommander())
		{
			removeActiveCommander();
		}
		commanderList.remove(unit);
	}
	
	public void updateUnitID()
	{
		for (int i = 0; i < unitList.size(); i++)
		{
			unitList.get(i).setUnitID(i);
			if(unitList.get(i).getActiveCommander())
			{
				activeCommanderID = i;
			}
		}
	}
	
	public void updateBuildingID()
	{
		for (int i = 0; i < buildingList.size(); i++)
		{
			buildingList.get(i).setUnitID(i);
		}
	}
	
	public void updateID()
	{
		for (int i = 0; i < unitList.size(); i++)
		{
			unitList.get(i).setUnitID(i);
			if(unitList.get(i).getActiveCommander())
			{
				activeCommanderID = i;
			}
		}
		for (int i = 0; i < buildingList.size(); i++)
		{
			buildingList.get(i).setUnitID(i);
			//buildingList.get(i).setBuildingID(i);
		}
	}
	
	public void defeat()
	{
		defeated = true;
	}
	
	public void makeSpecialUnitList()
	{
		int i;
		specialUnitList.clear();
		for(i = 0; i < unitList.size(); i++)
		{
			if(unitList.get(i).isSpecial())
			{
				specialUnitList.add(unitList.get(i).getSpecialUnit());
			}
		}
	}
	
	public void makeCommanderList()
	{
		int i;
		commanderList.clear();
		for(i = 0; i < unitList.size(); i++)
		{
			if(unitList.get(i).getIsSpecial() == 2)
			{
				commanderList.add(unitList.get(i).getCommander());
			}
		}
	}
	
	public void makeSpecialUnitAndCommanderList()
	{
		int i;
		specialUnitList.clear();
		commanderList.clear();
		for(i = 0; i < unitList.size(); i++)
		{
			if(unitList.get(i).isSpecial())
			{
				specialUnitList.add(unitList.get(i).getSpecialUnit());
				if(unitList.get(i).getIsSpecial() == 2)
				{
					commanderList.add(unitList.get(i).getCommander());
				}
			}
			
		}
	}
	
	public void addSelfCommandEffect(EffectData effect)
	{
		for(int i = 0; i < unitList.size(); i++)
		{
			unitList.get(i).addEffect(effect.makeEffect(0, turnNumber));
		}
		for(int i = 0; i < buildingList.size(); i++)
		{
			buildingList.get(i).addEffect(effect.makeEffect(0, turnNumber));
		}
	}
	
	public void addEnemyCommandEffect(EffectData effect)
	{
		for(int i = 0; i < unitList.size(); i++)
		{
			unitList.get(i).addEffect(effect.makeResistedPowerEffect(unitList.get(i).getDefactoEffectResistance(), 0, turnNumber));
		}
		for(int i = 0; i < buildingList.size(); i++)
		{
			buildingList.get(i).addEffect(effect.makeResistedPowerEffect(buildingList.get(i).getDefactoEffectResistance(), 0, turnNumber));
		}
	}
	
	public void removeSelfCommandEffect(EffectData effect)
	{
		for(int i = 0; i < unitList.size(); i++)
		{
			unitList.get(i).removeSameEffect(effect);
		}
		for(int i = 0; i < buildingList.size(); i++)
		{
			buildingList.get(i).removeSameEffect(effect);
		}
	}
	
	public void removeEnemyCommandEffect(EffectData effect)
	{
		for(int i = 0; i < unitList.size(); i++)
		{
			unitList.get(i).removeSameEffect(effect.makeResistedPowerEffect(unitList.get(i).getDefactoEffectResistance(), 0, turnNumber));
		}
		for(int i = 0; i < buildingList.size(); i++)
		{
			buildingList.get(i).removeSameEffect(effect.makeResistedPowerEffect(buildingList.get(i).getDefactoEffectResistance(), 0, turnNumber));
		}
	}
	
	public void addCommandEffect(Player[] playerList)
	{
		if(activelyCommanded)
		{
			int j;
			for(int i = 0; i < activeCommander.getCommandEffectList().length; i++)
			{
				if(activeCommander.getCommandEffectList(i).effectEnemy())
				{
					for(j = 0; j < playerList.length; j++)
					{
						if(j == turnNumber)
						{
							j++;
						}
						if(j >= playerList.length)
						{
							break;
						}
						playerList[j].addEnemyCommandEffect(activeCommander.getCommandEffectList(i));
					}
				}
				if(activeCommander.getCommandEffectList(i).effectSelf())
				{
					this.addSelfCommandEffect(activeCommander.getCommandEffectList(i));
				}
			}
		}
	}
	
	public void removeCommandEffect(Player[] playerList)
	{
		if(activelyCommanded)
		{
			int j;
			for(int i = 0; i < activeCommander.getCommandEffectList().length; i++)
			{
				if(activeCommander.getCommandEffectList(i).effectEnemy())
				{
					for(j = 0; j < playerList.length; j++)
					{
						if(j == turnNumber)
						{
							j++;
						}
						if(j >= playerList.length)
						{
							break;
						}
						playerList[j].removeEnemyCommandEffect(activeCommander.getCommandEffectList(i));
					}
				}
				if(activeCommander.getCommandEffectList(i).effectSelf())
				{
					this.removeSelfCommandEffect(activeCommander.getCommandEffectList(i));
				}
			}
		}
	}
	
	public boolean turnEnded()
	{
		int i;
		for(i = 0; i < unitList.size(); i++)
		{
			if(!unitList.get(i).getAttacked())
			{
				return false;
			}
		}
		for(i = 0; i < buildingList.size(); i++)
		{
			if(!buildingList.get(i).getAttacked())
			{
				return false;
			}
		}
		return true;
	}
	
	public void incrementTurn(Player[] playerList, ArrayList<EffectField> effectFieldList, UnitMap unitMap)
	{
		int i = 0;
		for(i = 0; i < unitList.size(); i++)
		{
			unitList.get(i).updateEffectFieldList(effectFieldList);
			unitList.get(i).incrementTurn();
		}
		for(i = 0; i < buildingList.size(); i++)
		{
			buildingList.get(i).incrementTurn();
			resource += buildingList.get(i).getResourceGain();
			this.addFinishedUnit(buildingList.get(i), unitMap);
		}
		updateID();
	}
}
