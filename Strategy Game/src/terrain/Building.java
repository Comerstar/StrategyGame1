package terrain;

import java.util.ArrayList;

import org.json.simple.JSONArray;

import attack.Attack;
import attack.Effect;
import attack.EffectField;
import map.UnitMap;
import unit.*;
import utilities.ArrayHandler;
import utilities.JSONDecoder;
import utilities.Location;

public class Building extends Unit implements TileInterface
{
	//0: Nothing
	//1: Reactor / Generator - GetsResource
	//2: StrongHold - Special Building For Win Conditions
	//3: Fortress - A Unit of the Same Player can Stay on the Tile and have a Defence Boost
	//4: Barracks/Factory - A Building to Make Units
	//5: Armed Fortress - A Building that can Attack
	//6: Artillery Stations - A Building that has a Powerful Customisable Launcher
	private int buildingType;
	//0:
	//1: Resource Gained Per Turn
	//2: 
	//3: Defence Boost to Housed Units
	//4: Make, Base Production (Turns Used per Turn), Making?, Turns Left to Make
	//5: 
	//6: 
	private double[] buildingData;
	//private int buildingID;
	protected Unit making;
	protected int tileNumber;
	protected int tileType;
	protected double movementCost;
	protected String buildingName;
	protected int lastPlayer;
	
	public Building(int tileNumber, Location location)
	{
		super();
		this.tileNumber = tileNumber;
		JSONArray tileValues = (JSONArray) JSONDecoder.getTileInfo().get(((Integer) tileNumber).toString());
		movementCost = (double) tileValues.get(0);
		tileType = ((Long) tileValues.get(1)).intValue();
		buildingName = (String) tileValues.get(2);
		buildingType = ((Long) tileValues.get(3)).intValue();
		super.setUnitData(((Long) tileValues.get(4)).intValue(),location.getY(), location.getX(), -1, 0);
		
		JSONArray buildingData = (JSONArray) tileValues.get(5);
		this.buildingData = new double[buildingData.size()];
		for(int i = 0; i < buildingData.size(); i++)
		{
			this.buildingData[i] = (double) buildingData.get(i);
		}
		player = -1;
		healthPoint = 0;
	}
	
	public Building(int tileNumber, Location location, int buildingID)
	{
		super();
		this.tileNumber = tileNumber;
		JSONArray tileValues = (JSONArray) JSONDecoder.getTileInfo().get(((Integer) tileNumber).toString());
		//this.buildingID = buildingID;
		movementCost = (double) tileValues.get(0);
		tileType = ((Long) tileValues.get(1)).intValue();
		buildingName = (String) tileValues.get(2);
		buildingType = ((Long) tileValues.get(3)).intValue();
		super.setUnitData(((Long) tileValues.get(4)).intValue(),location.getY(), location.getX(), -1, 0);
		player = -1;
		healthPoint = 0;
	}
	
	private Building (double defactoHealthPointRecover, double healthPoint, double defactoMoveRange, double defactoDefence, double defactoEffectResistance, 
			double defactoStaminaRecover, double stamina, double defactoVisionRange, double[] boostList, Location location, Attack[] attackList, 
			ArrayList<Effect> effectList, ArrayList<EffectField> effectFieldList, ArrayList<EffectField> effectFieldCentredList, int unitID, int player, boolean moved, 
			boolean attacked, boolean alive, double attackNumber, double moveNumber, double defactoMoveNumber, int isSpecial, int unitNumber, double maxHealthPoint, 
			double healthPointRecover, double moveRange, double defence, double effectResistance, double maxStamina, double staminaRecover, double visionRange, 
			int unitType, String name, int buildingType, double[] buildingData, Unit making, int tileNumber, int tileType, double movementCost, 
			String buildingName)
	{
		super();
		this.unitNumber = unitNumber;
		this.maxHealthPoint = maxHealthPoint;
		this.healthPointRecover = healthPointRecover;
		this.moveRange = moveRange;
		this.defence = defence;
		this.effectResistance = effectResistance;
		this.maxStamina = maxStamina;
		this.staminaRecover = staminaRecover;
		this.visionRange = visionRange;
		this.attackList = attackList;
		this.unitType = unitType;
		this.name = name;
		this.defactoHealthPointRecover = defactoHealthPointRecover;
		this.healthPoint = healthPoint;
		this.defactoMoveRange = defactoMoveRange;
		this.defactoDefence = defactoDefence;
		this.defactoEffectResistance = defactoEffectResistance;
		this.defactoStaminaRecover = defactoStaminaRecover;
		this.stamina = stamina;
		this.defactoVisionRange = defactoVisionRange;
		this.boostList = boostList.clone();
		this.location = location.clone();
		this.attackList = ArrayHandler.cloneDeepAttackArray(attackList);
		this.effectList = ArrayHandler.cloneDeepEffectArray(effectList);
		this.effectFieldList = ArrayHandler.cloneDeepEffectFieldArray(effectFieldList);
		this.effectFieldCentredList = ArrayHandler.cloneDeepEffectFieldArray(effectFieldCentredList);
		this.unitID = unitID;
		this.player = player;
		this.moved = moved;
		this.attacked = attacked;
		this.alive = alive;
		this.attackNumber = attackNumber;
		this.moveNumber = moveNumber;
		this.defactoMoveNumber = defactoMoveNumber;
		this.isSpecial = isSpecial;
		this.buildingType = buildingType;
		this.buildingData = buildingData;
		//this.buildingID = buildingID;
		this.making = making;
		this.tileNumber = tileNumber;
		this.tileType = tileType;
		this.movementCost = movementCost;
		this.buildingName = buildingName;
	}
	
	public Building clone()
	{
		return new Building(defactoHealthPointRecover, healthPoint, defactoMoveRange, defactoDefence, defactoEffectResistance, 
				defactoStaminaRecover, stamina, defactoVisionRange, boostList, location, attackList, 
				effectList, effectFieldList, effectFieldCentredList, unitID, player, moved, 
				attacked, alive, attackNumber, moveNumber, defactoMoveNumber, isSpecial, unitNumber, maxHealthPoint, 
				healthPointRecover, moveRange, defence, effectResistance, maxStamina, staminaRecover, visionRange, 
				unitType, name, buildingType, buildingData, making, tileNumber, tileType, movementCost, 
				buildingName);
	}
	
	public int getBuildingType()
	{
		return buildingType;
	}
	
	public double[] getBuildingData()
	{
		return buildingData;
	}
	
	public double getBuildingData(int i)
	{
		return buildingData[i];
	}
	
	public double getResourceGain()
	{
		if(buildingType == 1)
		{
			return buildingData[0];
		}
		return 0;
	}
	
	public double getDefenceBoost()
	{
		if(buildingType == 3)
		{
			return buildingData[0];
		}
		return 0;
	}
	
	//public int getBuildingID()
	//{
	//	return buildingID;
	//}
	
	public void setLocation(Location location)
	{
		this.location = location.clone();
	}
	
	public void setLocation(int[] location)
	{
		this.location.set(location);
	}
	
	public void setLocation(int y, int x)
	{
		this.location.set(y, x);
	}
	
	public int getBuildingMake()
	{
		if(buildingType == 4)
		{
			return (int) (buildingData[0]);
		}
		return 0;
	}
	
	public void setMakingUnit(UnitData unit)
	{
		making = unit.makeUnit(player, location.clone(), 0);
		buildingData[2] = 1.0;
		buildingData[3] = unit.getTurnsToMake();
		if(buildingData[3] <= 0.0)
		{
			buildingData[2] = 2.0;
		}
		this.endTurn();
	}
	
	public void setMakingUnit(int unitNumber)
	{
		UnitData makingData = new UnitData(unitNumber);
		making = makingData.makeUnit(player, location.clone(), 0);
		buildingData[2] = 1.0;
		buildingData[3] = makingData.getTurnsToMake();
		if(buildingData[3] <= 0.0)
		{
			buildingData[2] = 2.0;
		}
		this.endTurn();
	}
	
	public void setMakingUnit(UnitData unit, Location location)
	{
		making = unit.makeUnit(player, location, 0);
		buildingData[2] = 1.0;
		buildingData[3] = unit.getTurnsToMake();
		if(buildingData[3] <= 0.0)
		{
			buildingData[2] = 2.0;
		}
		this.endTurn();
	}
	
	public void setMakingUnit(int unitNumber, Location location)
	{
		UnitData makingData = new UnitData(unitNumber);
		making = makingData.makeUnit(player, location, 0);
		buildingData[2] = 1.0;
		buildingData[3] = makingData.getTurnsToMake();
		if(buildingData[3] <= 0.0)
		{
			buildingData[2] = 2.0;
		}
		this.endTurn();
	}
	
	public int getCounterAttack(Unit target)
	{
		int attackID = -1;
		if(player != -1 && target.getPlayer() != player)
		{
			double maxAttackPower = 0.0;
			for (int i = 0; i < attackList.length; i++)
			{
				if(maxAttackPower < attackList[i].getAttackPower())
				{
					if (attackList[i].checkValidUnitTarget(target))
					{
						maxAttackPower = attackList[i].getAttackPower();
						attackID = i;
					}
				}
			}
		}
		return attackID;
	}
	
	public void defendedDamage(Attack attack, Unit attacker)
	{
		double attackerHealth = attacker.getHealthPoint() / attacker.getMaxHealthPoint();
		this.healthPoint -=  ((100.0 - this.defactoDefence) / 100.0) * attackerHealth * attack.getAttackPower() * attacker.getBoostList(1) + attacker.getBoostList(0);
		if (this.healthPoint < 1.0)
		{
			this.healthPoint = 0.0;
			this.alive = false;
			this.player = -1;
			stopMaking();
		}
	}
	
	public double returnDefendedDamage(Attack attack, Unit attacker)
	{
		double damage = ((100.0 - this.defactoDefence) / 100.0) * attacker.getHealthPoint() / attacker.getMaxHealthPoint() * attack.getAttackPower() * attacker.getBoostList(1) + attacker.getBoostList(0);
		this.healthPoint -=  damage;
		if (this.healthPoint < 1.0)
		{
			this.healthPoint = 0.0;
			this.alive = false;
			this.player = -1;
			stopMaking();
		}
		return damage;
	}
	
	public void damage(Attack attack, Unit attacker)
	{
		double attackerHealth = attacker.getHealthPoint() / attacker.getMaxHealthPoint();
		this.healthPoint -=  attack.getAttackPower() * attackerHealth * attacker.getBoostList(13) + attacker.getBoostList(12);
		if (this.healthPoint < 1.0)
		{
			this.healthPoint = 0.0;
			this.alive = false;
			this.player = -1;
			stopMaking();
		}
	}
	
	public double returnDamage(Attack attack, Unit attacker)
	{
		double damage = attacker.getHealthPoint() / attacker.getMaxHealthPoint() * attack.getAttackPower() * attacker.getBoostList(13) + attacker.getBoostList(12);
		this.healthPoint -= damage;
		if (this.healthPoint < 1.0)
		{
			this.healthPoint = 0.0;
			this.alive = false;
			this.player = -1;
			stopMaking();
		}
		return damage;
	}
	
	public void move()
	{
		
	}
	
	public void move(int[] location, double moveRangeUsed)
	{
		
	}
	
	public void move(int[] location, double moveRangeUsed, ArrayList<EffectField> gameEffectFieldList)
	{
		
	}
	
	public void move(int[] location, double moveRangeUsed, ArrayList<EffectField> gameEffectFieldList, UnitMap unitTileMap)
	{
		
	}
	public double making()
	{
		if(buildingType == 4)
		{
			return buildingData[2];
		}
		return 0.0;
	}
	
	public void capture(Unit capturer)
	{
		player = capturer.getPlayer();
		lastPlayer = player;
		alive = true;
		this.healthPoint = this.maxHealthPoint * capturer.getHealthPoint() / capturer.getMaxHealthPoint();
	}
	
	public void capture(Unit capturer, double healthRatio)
	{
		player = capturer.getPlayer();
		lastPlayer = player;
		alive = true;
		this.healthPoint = this.maxHealthPoint * capturer.getHealthPoint() / capturer.getMaxHealthPoint() * healthRatio;
	}
	
	public void captureHealthRatio(Unit capturer, double healthRatio)
	{
		player = capturer.getPlayer();
		lastPlayer = player;
		alive = true;
		this.healthPoint = this.maxHealthPoint * this.healthPoint / this.maxHealthPoint * healthRatio;
	}
	
	public Unit getMaking()
	{
		return making;
	}
	
	public Unit finishMaking()
	{
		buildingData[2] = 0.0;
		buildingData[3] = -1.0;
		return making;
	}
	
	public void stopMaking()
	{
		if(buildingType == 4)
		{
			buildingData[2] = 0.0;
			buildingData[3] = -1.0;
		}
	}
	
	public boolean getValidUnitMake(Unit make)
	{
		if(make.getTurnsToMake() == buildingData[0] && buildingType == 4)
		{
			return true;
		}
		return false;
	}

	//0: Nothing
	//1: Reactor / Generator - GetsResource
	//2: StrongHold - Special Building For Win Conditions
	//3: Fortress - A Unit of the Same Player can Stay on the Tile and have a Defence Boost
	//4: Barracks/Factory - A Building to Make Units
	//5: Armed Fortress - A Building that can Attack
	//6: Artillery Stations - A Building that has a Powerful Customisable Launcher
	//0:
	//1: Resource Gained Per Turn
	//2: 
	//3: Defence Boost to Housed Units
	//4: Make, Base Production (Turns Used per Turn), Making?, Turns Left to Make
	//5: 
	//6: 
	public void incrementTurn()
	{
		super.incrementTurn();
		moved = true;
		attacked = false;
		System.out.println("Incrementing Turn Attacked: " + attacked);
		if(buildingType == 1)
		{
			
		}
		else if(buildingType == 2)
		{
			
		}
		else if(buildingType == 3)
		{
			
		}
		else if(buildingType == 4)
		{
			if(buildingData[2] == 1.0)
			{
				buildingData[3] -= buildingData[1];
				if(buildingData[3] <= 0.0)
				{
					buildingData[3] = -1.0;
					buildingData[2] = 2.0;
				}
			}
		}
		else if(buildingType == 5)
		{
			
		}
		else if(buildingType == 6)
		{
			
		}
	}
	
	public boolean freeToBuild(UnitMap unitTileMap)
	{
		return unitTileMap.getMap(location) == null;
	}
	
	public double getMovementCost() 
	{
		return movementCost;
	}
	
	public int getTileType() 
	{
		return tileType;
	}
	
	public int getTileNumber() 
	{
		return tileNumber;
	}
	
	public boolean isBuilding() 
	{
		return true;
	}
	
	public Building getBuilding()
	{
		return this;
	}
	
	public String getTileName()
	{
		return buildingName;
	}
	
	public Tile getTile()
	{
		return new Tile(movementCost, tileType, tileNumber, name);
	}
	
	//public void setBuildingID(int i)
	//{
	//	buildingID = i;
	//}
	
	public int getLastPlayer()
	{
		return lastPlayer;
	}
}
