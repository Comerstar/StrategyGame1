package unit;


import java.util.ArrayList;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import attack.*;
import utilities.JSONDecoder;
import utilities.Location;

public class UnitData {
	protected static JSONObject unitInfo;
	//Holds the maximum and minimum values stats are allowed to be, 1: healthPointRecover, 2: moveRange, 3: defence, 4: effectResistance
	protected static double[][] statLimits = new double[][]{{0.0,65535.0},{0.0, 60000.0},{-200.0, 100.0}, {-200.0, 100.0}};
	protected int unitNumber;
	protected double maxHealthPoint;
	protected double healthPointRecover;
	protected double moveRange;
	protected double defence;
	protected double effectResistance;
	protected double maxStamina;
	protected double staminaRecover;
	protected double visionRange;
	protected AttackData[] attackList;
	protected AttackData[] offensiveAttackList;
	protected AttackData[] supportList;
	protected int unitType;
	protected String name;
	// 0 = normal unit, 1 = special unit, 2 = commander
	protected int isSpecial;
	protected double maxSpecialPoint;
	protected double specialPointGain;
	protected double specialPointDamageGain;
	protected SpecialData[] specialList;
	protected double form;
	protected EffectData[] commandEffectList;
	protected double resourceCost;
	protected int unitMake;
	protected double turnsToMake;
	
	public static JSONObject getUnitInfo()
	{
		return unitInfo;
	}
	
	protected UnitData()
	{
		if (unitInfo == null)
		{
			JSONDecoder.setupUnitInfo();
			unitInfo = JSONDecoder.getUnitInfo();
		}
	}
	
	public UnitData (int unitNumber)
	{
		if (unitInfo == null)
		{
			JSONDecoder.setupUnitInfo();
			unitInfo = JSONDecoder.getUnitInfo();
		}
		
		this.unitNumber = unitNumber;
		JSONArray unitValues = (JSONArray) unitInfo.get(((Integer) this.unitNumber).toString());
		maxHealthPoint = (double) unitValues.get(0);
		healthPointRecover = (double) unitValues.get(1);
		moveRange = (double) unitValues.get(2);
		defence = (double) unitValues.get(3);
		effectResistance = (double) unitValues.get(4);
		maxStamina = (double) unitValues.get(5);
		staminaRecover = (double) unitValues.get(6);
		visionRange = (double) unitValues.get(7);
		resourceCost = (double) unitValues.get(8);
		JSONArray attacks = (JSONArray) unitValues.get(9);
		attackList = new AttackData[attacks.size()];
		System.out.println("unitNumber: " + unitNumber);
		System.out.println("attacks.size(): " + attacks.size());
		System.out.println("attacksList.length: " + attackList.length);
		AttackData[] tempAttackList = new AttackData[attacks.size()];
		int i;
		for(i = 0; i < attacks.size(); i++)
		{
			tempAttackList[i] = new AttackData(((Long) attacks.get(i)).intValue());
		}
		AttackData tempAttack;
		ArrayList<AttackData> tempSupport = new ArrayList<AttackData>();
		ArrayList<AttackData> tempOffensive = new ArrayList<AttackData>();
		for(i = 0; i < tempAttackList.length; i++)
		{
			tempAttack = tempAttackList[i];
			if(tempAttack.getAttackSupport() == 0)
			{
				tempSupport.add(tempAttack);
			}
			else if(tempAttack.getAttackSupport() == 1)
			{
				tempOffensive.add(tempAttack);
			}
		}
		supportList = new AttackData[tempSupport.size()];
		offensiveAttackList = new AttackData[tempOffensive.size()];
		System.out.println("tempSupport.size(): " + tempSupport.size());
		System.out.println("tempOffensive.size(): " + tempOffensive.size());
		for(i = 0; i < tempSupport.size(); i++)
		{
			supportList[i] = tempSupport.get(i);
		}
		for(i = 0; i < tempOffensive.size(); i++)
		{
			offensiveAttackList[i] = tempOffensive.get(i);
		}
		
		for(i = 0; i < offensiveAttackList.length; i++)
		{
			attackList[i] = offensiveAttackList[i];
		}
		for(i = 0; i < supportList.length; i++)
		{
			attackList[i + offensiveAttackList.length] = supportList[i]; 
		}
		System.out.println("attacksList.length: " + attackList.length);
		unitType = ((Long) unitValues.get(10)).intValue();
		unitMake = ((Long) unitValues.get(11)).intValue();
		name = (String) unitValues.get(12);
		isSpecial = ((Long) unitValues.get(13)).intValue();
		turnsToMake = (double) unitValues.get(14);
		if(isSpecial >= 1)
		{
			maxSpecialPoint = (double) unitValues.get(15);
			specialPointGain = (double) unitValues.get(16);
			specialPointDamageGain = (double) unitValues.get(17);
			JSONArray specialValues = (JSONArray) unitValues.get(18);
			specialList = new SpecialData[specialValues.size()];
			for(i = 0; i < specialValues.size(); i++)
			{
				specialList[i] = new SpecialData(((Long) specialValues.get(i)).intValue());
			}
			if(isSpecial == 2)
			{
				
			}
		}
	}
	
	public int getUnitMake()
	{
		return unitMake;
	}
	
	public double getTurnsToMake()
	{
		return turnsToMake;
	}
	
	public double getResourceCost()
	{
		return resourceCost;
	}
	
	public int getIsSpecial()
	{
		return isSpecial;
	}
	
	public EffectData[] getCommandEffectList()
	{
		return commandEffectList;
	}
	
	public EffectData getCommandEffectList(int i)
	{
		return commandEffectList[i];
	}
	
	public Unit makeUnit(int player, Location location, int unitID)
	{
		if(isSpecial == 0)
		{
			return new Unit(unitNumber, maxHealthPoint,  healthPointRecover,  moveRange, defence,  effectResistance,  maxStamina, staminaRecover,  visionRange, location, attackList, supportList, offensiveAttackList, unitID,  unitType,  name,  player);
		}
		else if(isSpecial == 1)
		{
			return new SpecialUnit(unitNumber, maxHealthPoint,  healthPointRecover,  moveRange, defence,  effectResistance,  maxStamina, staminaRecover,  visionRange, location, attackList, supportList, offensiveAttackList, unitID,  unitType,  name,  player, maxSpecialPoint, specialPointGain, specialPointDamageGain, specialList);
		}
		else if(isSpecial == 2)
		{
			return new Commander(unitNumber, maxHealthPoint,  healthPointRecover,  moveRange, defence,  effectResistance,  maxStamina, staminaRecover,  visionRange, location, attackList, supportList, offensiveAttackList, unitID,  unitType,  name,  player, maxSpecialPoint, specialPointGain, specialPointDamageGain, specialList, commandEffectList);
		}
		return null;
	}
	
	public static UnitData[] makeUnitArray(ArrayList<Integer> unitList)
	{
		UnitData[] unitData = new UnitData[unitList.size()];
		for(int i = 0; i < unitList.size(); i++)
		{
			unitData[i] = new UnitData(unitList.get(i));
		}
		return unitData;
	}
	
	public static ArrayList<UnitData> makeUnitArrayList(ArrayList<Integer> unitList)
	{
		ArrayList<UnitData> unitData = new ArrayList<UnitData>();
		for(int i = 0; i < unitList.size(); i++)
		{
			unitData.add(new UnitData(unitList.get(i)));
		}
		return unitData;
	}
	
	public static UnitData[] makeUnitArray(int[] unitList)
	{
		UnitData[] unitData = new UnitData[unitList.length];
		for(int i = 0; i < unitList.length; i++)
		{
			unitData[i] = new UnitData(unitList[i]);
		}
		return unitData;
	}
	
	public static ArrayList<UnitData> makeUnitArrayList(int[] unitList)
	{
		ArrayList<UnitData> unitData = new ArrayList<UnitData>();
		for(int i = 0; i < unitList.length; i++)
		{
			unitData.add(new UnitData(unitList[i]));
		}
		return unitData;
	}

	public String getName() 
	{
		return name;
	}
	
	public AttackData[] getOffensiveAttackList()
	{
		return offensiveAttackList;
	}
	
	public AttackData[] getSupportList()
	{
		return supportList;
	}
}
