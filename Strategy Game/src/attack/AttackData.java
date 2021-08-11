package attack;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import unit.Unit;
import utilities.JSONDecoder;

public class AttackData {
	
	protected static JSONObject attackInfo;
	protected int attackNumber;
	protected double attackPower;
	protected int attackType;
	protected double attackRange;
	protected EffectData[] targetEffectList;
	protected EffectData[] selfEffectList;
	protected double staminaCost;
	protected String name;
	// 0 = self, 1 = enemy, 2 = either, 3 = not a unit
	protected int target;
	protected double areaOfEffect;
	protected EffectFieldData targetEffectField;
	protected EffectFieldData selfEffectField;
	protected double innerRange;
	protected int rangeShape;
	protected double numberOfTarget;
	protected int areaOfEffectTarget;
	protected int attackSupport;
	
	protected AttackData()
	{
		if (attackInfo == null)
		{
			JSONDecoder.setupAttackInfo();
			attackInfo = JSONDecoder.getAttackInfo();
		}
	}
	
	public AttackData (int attackNumber)
	{
		this.attackNumber = attackNumber;
		if (attackInfo == null)
		{
			JSONDecoder.setupAttackInfo();
			attackInfo = JSONDecoder.getAttackInfo();
		}
		
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
	
	public Attack makeAttack(Unit unit)
	{
		return new Attack(attackNumber, attackPower, attackType, attackRange, targetEffectList, selfEffectList, 
				 staminaCost, name, target, areaOfEffect, targetEffectField, selfEffectField, 
				 innerRange, rangeShape, numberOfTarget, unit, unit.getPlayer(), areaOfEffectTarget);
	}
	
	
	public int getAttackNumber()
	{
		return attackNumber;
	}
	
	public double getAttackPower()
	{
		return attackPower;
	}
	
	public int getAttackType()
	{
		return attackType;
	}
	
	public double getAttackRange()
	{
		return attackRange;
	}
	
	public EffectData[] getTargetEffectList()
	{
		return targetEffectList;
	}
	
	public EffectData[] getSelfEffectList()
	{
		return selfEffectList;
	}
	
	public double getStaminaCost()
	{
		return staminaCost;
	}
	
	public String getName()
	{
		return name;
	}
	
	public int getTarget()
	{
		return target;
	}
	
	public double getAreaOfEffect()
	{
		return areaOfEffect;
	}
	
	public EffectFieldData getTargetEffectField()
	{
		return targetEffectField;
	}
	
	public EffectFieldData getSelfEffectField()
	{
		return selfEffectField;
	}
	
	public double getInnerRange()
	{
		return innerRange;
	}
	
	public int getRangeShape()
	{
		return rangeShape;
	}
	
	public double getNumberOfTarget()
	{
		return numberOfTarget;
	}
	
	public int getAreaOfEffectTarget()
	{
		return areaOfEffectTarget;
	}
	
	public int getAttackSupport()
	{
		return attackSupport;
	}
}
