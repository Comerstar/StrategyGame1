package attack;

import java.util.ArrayList;

import org.json.simple.JSONArray;

import map.BuildingMap;
import map.UnitMap;
import unit.Unit;
import utilities.Location;

public class Attack extends AttackData{
	
	private int attackNumber;
	private double attackPower;
	private int attackType;
	private double attackRange;
	private EffectData[] targetEffectList;
	private EffectData[] selfEffectList;
	private double staminaCost;
	private String name;
	// 0 = self, 1 = enemy, 2 = either, 3 = not a unit
	private int target;
	private double areaOfEffect;
	private EffectFieldData targetEffectField;
	private EffectFieldData selfEffectField;
	private double innerRange;
	private int rangeShape;
	private double numberOfTarget;
	private Unit unit;
	private int player;
	// 0 = self, 1 = enemy, 2 = either
	private int areaOfEffectTarget;
	
	public Attack (int attackNumber, Unit unit) 
	{
		super();
		this.attackNumber = attackNumber;
		this.unit = unit;
		this.player = unit.getPlayer();
		
		JSONArray attackValues = (JSONArray) attackInfo.get(((Integer) this.attackNumber).toString());
		target = ((Long) attackValues.get(0)).intValue();
		staminaCost = (double) attackValues.get(1);
		attackPower = (double) attackValues.get(2);
		attackRange = (double) attackValues.get(3);
		attackType = ((Long) attackValues.get(4)).intValue();
		int i = 0;
		JSONArray effectList = (JSONArray) attackValues.get(5);
		targetEffectList = new EffectData[effectList.size()];
		JSONArray effectInfo;
		for (i = 0; i < effectList.size(); i++)
		{
			effectInfo = (JSONArray) effectList.get(i);
			targetEffectList[i] = new EffectData(((Long) effectInfo.get(0)).intValue(), (double) effectInfo.get(1), (double) effectInfo.get(2));
		}
		effectList = (JSONArray) attackValues.get(6);
		selfEffectList = new EffectData[effectList.size()];
		for (i = 0; i < effectList.size(); i++)
		{
			effectInfo = (JSONArray) effectList.get(i);
			selfEffectList[i] = new EffectData(((Long) effectInfo.get(0)).intValue(), (double) effectInfo.get(1), (double) effectInfo.get(2));
		}
		
		//Initialising Target Effect Field
		long[] effectNumberList;
		double[] effectPowerList;
		effectList = (JSONArray) attackValues.get(7);
		if ((double) effectList.get(0) != 0)
		{
			effectInfo = (JSONArray) effectList.get(4);
			effectNumberList = new long[effectInfo.size()];
			for (i = 0; i < effectInfo.size(); i++)
			{
				effectNumberList[i] = (long) effectInfo.get(i);
			}
			effectInfo = (JSONArray) effectList.get(5);
			effectPowerList = new double[effectInfo.size()];
			for (i = 0; i < effectInfo.size(); i++)
			{
				effectPowerList[i] = (double) effectInfo.get(i);
			}
			targetEffectField = new EffectFieldData((double) effectList.get(0), effectNumberList, effectPowerList, (double) effectList.get(1), (long) effectList.get(2), (long) effectList.get(3));
		}
		else
		{
			targetEffectField = null;
		}

		//Initialising Self Effect Field
		effectList = (JSONArray) attackValues.get(8);
		if ((double) effectList.get(0) != 0)
		{
			effectInfo = (JSONArray) effectList.get(4);
			effectNumberList = new long[effectInfo.size()];
			for (i = 0; i < effectInfo.size(); i++)
			{
				effectNumberList[i] = (long) effectInfo.get(i);
			}
			effectInfo = (JSONArray) effectList.get(5);
			effectPowerList = new double[effectInfo.size()];
			for (i = 0; i < effectInfo.size(); i++)
			{
				effectPowerList[i] = (double) effectInfo.get(i);
			}
			selfEffectField = new EffectFieldData((double) effectList.get(0), effectNumberList, effectPowerList, (double) effectList.get(1), (long) effectList.get(2), (long) effectList.get(3));
		}
		else
		{
			selfEffectField = null;
		}
		name = (String) attackValues.get(9);
		
		innerRange = (double) attackValues.get(10);
		areaOfEffect = (double) attackValues.get(11);
		rangeShape = ((Long) attackValues.get(12)).intValue();
		numberOfTarget = (double) attackValues.get(13);
		areaOfEffectTarget = ((Long) attackValues.get(14)).intValue();
		attackSupport = ((Long) attackValues.get(15)).intValue();
	}

	public Attack (int attackNumber, double attackPower,int attackType, double attackRange, EffectData[] targetEffectList, EffectData[] selfEffectList, 
			double staminaCost, String name, int target, double areaOfEffect, EffectFieldData targetEffectField, EffectFieldData selfEffectField, 
			double innerRange, int rangeShape, double numberOfTarget, Unit unit, int player, int areaOfEffectTarget)
	{
		this.attackNumber = attackNumber;
		this.attackPower = attackPower;
		this.attackType = attackType;
		this.attackRange = attackRange;
		this.targetEffectList = targetEffectList;
		this.selfEffectList = selfEffectList;
		this.staminaCost = staminaCost;
		this.name = name;
		this.target = target;
		this.areaOfEffect = areaOfEffect;
		this.targetEffectField = targetEffectField;
		this.selfEffectField = selfEffectField;
		this.innerRange = innerRange;
		this.rangeShape = rangeShape;
		this.numberOfTarget = numberOfTarget;
		this.unit = unit;
		this.player = player;
		this.areaOfEffectTarget = areaOfEffectTarget;
	}
	
	public Attack clone()
	{
		return new Attack(attackNumber, attackPower,attackType, attackRange, targetEffectList, selfEffectList, 
				staminaCost, name, target, areaOfEffect, targetEffectField, selfEffectField, 
				innerRange, rangeShape, numberOfTarget, unit, player, areaOfEffectTarget);
	}
	
	public Unit getUnit()
	{
		return unit;
	}
	
	public int getPlayer()
	{
		return player;
	}
	
	public double getAttackPower()
	{
		return attackPower;
	}
	
	public double getStaminaCost()
	{
		return staminaCost;
	}
	
	public double getAttackRange()
	{
		return attackRange;
	}
	
	public String getName()
	{
		return name;
	}
	
	public int getDefactoAreaOfEffect()
	{
		return ((Double) (areaOfEffect * this.unit.getBoostList(9) + this.unit.getBoostList(8))).intValue();
	}
	
	public EffectData[] getTargetEffectList()
	{
		return targetEffectList;
	}

	public EffectData getTargetEffectList(int i)
	{
		return targetEffectList[i];
	}
	
	public EffectData[] getSelfEffectList()
	{
		return selfEffectList;
	}
	
	public EffectData getSelfEffectList(int i)
	{
		return selfEffectList[i];
	}
	
	public EffectFieldData getTargetEffectField()
	{
		return targetEffectField;
	}
	
	public EffectFieldData getSelfEffectField()
	{
		return selfEffectField;
	}
	
	public int getAttackType()
	{
		return attackType;
	}
	
	public int getTarget()
	{
		return target;
	}
	
	public double getNumberOfTarget()
	{
		return numberOfTarget;
	}
	
	public double getAreaOfEffect()
	{
		return areaOfEffect;
	}
	
	public double getInnerRange()
	{
		return innerRange;
	}
	
	public int getRangeShape()
	{
		return rangeShape;
	}
	
	public boolean checkInRange(Location target)
	{
		boolean inRange = false;
		double defactoAttackRange = attackRange * unit.getBoostList(3) + unit.getBoostList(2);
		double defactoInnerRange = innerRange * unit.getBoostList(11) + unit.getBoostList(10);
		double defactoAreaOfEffect = areaOfEffect * this.unit.getBoostList(9) + this.unit.getBoostList(8);
		if(rangeShape == 0)
		{
			int distance = unit.getLocation().calculateDistance(target);
			if (distance <= defactoAttackRange + defactoAreaOfEffect && distance >= defactoInnerRange - defactoAreaOfEffect)
			{
				inRange = true;
			}
		}
		return inRange;
	}
	
	public boolean checkInRange(int y, int x)
	{
		boolean inRange = false;
		double defactoAttackRange = attackRange * unit.getBoostList(3) + unit.getBoostList(2);
		double defactoInnerRange = innerRange * unit.getBoostList(11) + unit.getBoostList(10);
		double defactoAreaOfEffect = areaOfEffect * unit.getBoostList(9) + unit.getBoostList(8);
		if(rangeShape == 0)
		{
			int distance = unit.getLocation().calculateDistance(y, x);
			if (distance <= defactoAttackRange + defactoAreaOfEffect && distance >= defactoInnerRange - defactoAreaOfEffect)
			{
				inRange = true;
			}
		}
		return inRange;
	}
	
	public boolean checkValidTarget(Location target)
	{
		boolean inRange = false;
		double defactoAttackRange = attackRange * unit.getBoostList(3) + unit.getBoostList(2);
		double defactoInnerRange = innerRange * unit.getBoostList(11) + unit.getBoostList(10);
		if(rangeShape == 0)
		{
			int distance = unit.getLocation().calculateDistance(target);
			if (distance <= defactoAttackRange && distance >= defactoInnerRange)
			{
				inRange = true;
			}
		}
		return inRange;
	}
	
	public boolean checkValidTarget(Location target, Location fromAttack)
	{
		boolean inRange = false;
		double defactoAttackRange = attackRange * unit.getBoostList(3) + unit.getBoostList(2);
		double defactoInnerRange = innerRange * unit.getBoostList(11) + unit.getBoostList(10);
		if(rangeShape == 0)
		{
			int distance = fromAttack.calculateDistance(target);
			if (distance <= defactoAttackRange && distance >= defactoInnerRange)
			{
				inRange = true;
			}
		}
		return inRange;
	}
	
	public boolean checkValidTarget(int y, int x)
	{
		boolean inRange = false;
		double defactoAttackRange = attackRange * unit.getBoostList(3) + unit.getBoostList(2);
		double defactoInnerRange = innerRange * unit.getBoostList(11) + unit.getBoostList(10);
		if(rangeShape == 0)
		{
			int distance = unit.getLocation().calculateDistance(y, x);
			if (distance <= defactoAttackRange && distance >= defactoInnerRange)
			{
				inRange = true;
			}
		}
		return inRange;
	}
	
	public boolean checkValidTarget(int y, int x, Location location)
	{
		boolean inRange = false;
		double defactoAttackRange = attackRange * unit.getBoostList(3) + unit.getBoostList(2);
		double defactoInnerRange = innerRange * unit.getBoostList(11) + unit.getBoostList(10);
		if(rangeShape == 0)
		{
			int distance = location.calculateDistance(y, x);
			if (distance <= defactoAttackRange && distance >= defactoInnerRange)
			{
				inRange = true;
			}
		}
		return inRange;
	}
	
	public boolean checkValidUnitTarget(Unit target)
	{
		boolean inRange = false;
		double defactoAttackRange = attackRange * unit.getBoostList(3) + unit.getBoostList(2);
		double defactoInnerRange = innerRange * unit.getBoostList(11) + unit.getBoostList(10);
		double defactoAreaOfEffect = areaOfEffect * this.unit.getBoostList(9) + this.unit.getBoostList(8);
		if(rangeShape == 0)
		{
			int distance = unit.getLocation().calculateDistance(target.getLocation());
			if (distance <= defactoAttackRange + defactoAreaOfEffect && distance >= defactoInnerRange - defactoAreaOfEffect)
			{
				if(player == target.getPlayer() && target.getPlayer() != -1)
				{
					if(this.target == 0 && this.target == 2)
					{
						inRange = true;
					}
				}
				else if(unit.getPlayer() != -1)
				{
					if(this.target == 1 && this.target == 2)
					{
						inRange = true;
					}
				}
			}
		}
		return inRange;
	}
	
	public boolean checkValidAttack(Unit target, Location attackLocation)
	{
		boolean inRange = false;
		double defactoAttackRange = attackRange * unit.getBoostList(3) + unit.getBoostList(2);
		double defactoInnerRange = innerRange * unit.getBoostList(11) + unit.getBoostList(10);
		double defactoAreaOfEffect = areaOfEffect * this.unit.getBoostList(9) + this.unit.getBoostList(8);
		if(rangeShape == 0)
		{
			int distance = unit.getLocation().calculateDistance(attackLocation);
			if (distance <= defactoAttackRange && distance >= defactoInnerRange)
			{
				distance = attackLocation.calculateDistance(target.getLocation());
				if(distance <= defactoAreaOfEffect)
				{
					if(unit.getPlayer() == target.getPlayer() && target.getPlayer() != -1)
					{
						if(this.target == 0 && this.target == 2)
						{
							inRange = true;
						}
					}
					else if(unit.getPlayer() != -1)
					{
						if(this.target == 1 && this.target == 2)
						{
							inRange = true;
						}
					}
				}
			}
		}
		return inRange;
	}
	
	public boolean AoE()
	{
		double defactoAreaOfEffect = areaOfEffect * this.unit.getBoostList(9) + this.unit.getBoostList(8);
		if(defactoAreaOfEffect >= 1.0)
		{
			return true;
		}
		else
		{
			return false;
		}
	}
	
	public ArrayList<Unit> getUnitsDamaged(Location attackLocation, UnitMap unitTileMap)
	{
		ArrayList<Unit> inRange = new ArrayList<Unit>();
		int i = 0;
		int j = 0;
		Unit unit;
		int defactoAreaOfEffect = ((Double) (areaOfEffect * this.unit.getBoostList(9) + this.unit.getBoostList(8))).intValue();
		for(i = 0; i < defactoAreaOfEffect + 1; i++)
		{
			for(j = 0; j < defactoAreaOfEffect + 1; j++)
			{
				attackLocation.translate(i - defactoAreaOfEffect + j, i - j);
				if(unitTileMap.inMap(attackLocation))
				{
					unit = unitTileMap.getMap(attackLocation);
					if(unit != null)
					{
						if(unit.getPlayer() == player && unit.getPlayer() != -1)
						{
							if(areaOfEffectTarget == 0 || areaOfEffectTarget == 2)
							{
								inRange.add(unit);
							}
						}
						else
						{
							if(areaOfEffectTarget == 1 || areaOfEffectTarget == 2)
							{
								inRange.add(unit);
							}
						}
					}
				}
				attackLocation.translate(-i + defactoAreaOfEffect - j, -i + j);
			}
		}
		for(i = 0; i < defactoAreaOfEffect; i++)
		{
			for(j = 0; j < defactoAreaOfEffect; j++)
			{
				attackLocation.translate(i - defactoAreaOfEffect + j + 1, i - j);
				if(unitTileMap.inMap(attackLocation))
				{
					unit = unitTileMap.getMap(attackLocation);
					if(unit != null)
					{
						if(unit.getPlayer() == player && unit.getPlayer() != -1)
						{
							if(target == 0 || target == 2)
							{
								inRange.add(unit);
							}
						}
						else
						{
							if(target == 1 || target == 2)
							{
								inRange.add(unit);
							}
						}
					}
				}
				attackLocation.translate(-i + defactoAreaOfEffect - j - 1, -i + j);
			}
		}
		return inRange;
	}
	
	public ArrayList<Unit> getUnitsDamaged(Location attackLocation, UnitMap unitTileMap, BuildingMap buildingTileMap)
	{
		ArrayList<Unit> inRange = new ArrayList<Unit>();
		int i = 0;
		int j = 0;
		Unit unit;
		int defactoAreaOfEffect = ((Double) (areaOfEffect * this.unit.getBoostList(9) + this.unit.getBoostList(8))).intValue();
		for(i = 0; i < defactoAreaOfEffect + 1; i++)
		{
			for(j = 0; j < defactoAreaOfEffect + 1; j++)
			{
				attackLocation.translate(i - defactoAreaOfEffect + j, i - j);
				if(unitTileMap.inMap(attackLocation))
				{
					unit = unitTileMap.getMap(attackLocation);
					if(unit != null)
					{
						if(unit.getPlayer() == player && unit.getPlayer() != -1)
						{
							if(areaOfEffectTarget == 0 || areaOfEffectTarget == 2)
							{
								inRange.add(unit);
							}
						}
						else if(unit.getPlayer() != -1)
						{
							if(areaOfEffectTarget == 1 || areaOfEffectTarget == 2)
							{
								inRange.add(unit);
							}
						}
					}
					unit = buildingTileMap.getMap(attackLocation);
					if(unit != null)
					{
						if(unit.getPlayer() == player && unit.getPlayer() != -1)
						{
							if(areaOfEffectTarget == 0 || areaOfEffectTarget == 2)
							{
								inRange.add(unit);
							}
						}
						else if(unit.getPlayer() != -1)
						{
							if(areaOfEffectTarget == 1 || areaOfEffectTarget == 2)
							{
								inRange.add(unit);
							}
						}
					}
				}
				attackLocation.translate(-i + defactoAreaOfEffect - j, -i + j);
			}
		}
		for(i = 0; i < defactoAreaOfEffect; i++)
		{
			for(j = 0; j < defactoAreaOfEffect; j++)
			{
				attackLocation.translate(i - defactoAreaOfEffect + j + 1, i - j);
				if(unitTileMap.inMap(attackLocation))
				{
					unit = unitTileMap.getMap(attackLocation);
					if(unit != null)
					{
						if(unit.getPlayer() == player && unit.getPlayer() != -1)
						{
							if(target == 0 || target == 2)
							{
								inRange.add(unit);
							}
						}
						else if(unit.getPlayer() != -1)
						{
							if(target == 1 || target == 2)
							{
								inRange.add(unit);
							}
						}
					}
					unit = buildingTileMap.getMap(attackLocation);
					if(unit != null)
					{
						if(unit.getPlayer() == player && unit.getPlayer() != -1)
						{
							if(areaOfEffectTarget == 0 || areaOfEffectTarget == 2)
							{
								inRange.add(unit);
							}
						}
						else if(unit.getPlayer() != -1)
						{
							if(areaOfEffectTarget == 1 || areaOfEffectTarget == 2)
							{
								inRange.add(unit);
							}
						}
					}
				}
				attackLocation.translate(-i + defactoAreaOfEffect - j - 1, -i + j);
			}
		}
		return inRange;
	}
}
