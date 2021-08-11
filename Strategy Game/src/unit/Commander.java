package unit;

import org.json.simple.JSONArray;

import attack.*;
import utilities.Location;

public class Commander extends SpecialUnit {
	
	private EffectData[] commandEffectList;
	private boolean activeCommander;
	
	public Commander (int unitType, int X, int Y, int player, int unitID)
	{
		super(unitType, X, Y, player, unitID);
		
		JSONArray effectValues = (JSONArray) ((JSONArray) (unitInfo.get(((Integer) unitNumber).toString()))).get(19);
		commandEffectList = new EffectData[effectValues.size()];
		JSONArray effectData;
		for(int i = 0; i < effectValues.size(); i++)
		{
			effectData = (JSONArray) effectValues.get(i);
			commandEffectList[i] = new EffectData(((Long) effectData.get(0)).intValue(), 0.0, (double) effectData.get(1), ((Long) effectData.get(2)).intValue());
		}
		isSpecial = 2;
		activeCommander = false;
	}
	
	public Commander (int unitNumber, double maxHealthPoint, double healthPointRecover, double moveRange, 
			double defence, double effectResistance, double maxStamina, 
			double staminaRecover, double visionRange,
			Location location, Attack[] attackList, AttackData[] supportList, AttackData[] offensiveAttackList, 
			int unitID, int unitType, String name, int player, double maxSpecialPoint, double specialPointGain, double specialPointDamageGain, Special[] specialList, EffectData[] commandEffectList)
	{
		super(unitNumber, maxHealthPoint, healthPointRecover, moveRange, defence, effectResistance, maxStamina, staminaRecover, visionRange, location, attackList, supportList, offensiveAttackList, unitID, unitType, name, player, maxSpecialPoint, specialPointGain, specialPointDamageGain, specialList);
		this.commandEffectList = commandEffectList;
		this.isSpecial = 2;
		resetDefactoValues();
	}
	
	public Commander (int unitNumber, double maxHealthPoint, double healthPointRecover, double moveRange, 
			double defence, double effectResistance, double maxStamina, 
			double staminaRecover, double visionRange,
			Location location, AttackData[] attackList, AttackData[] supportList, AttackData[] offensiveAttackList, 
			int unitID, int unitType, String name, int player, double maxSpecialPoint, double specialPointGain, double specialPointDamageGain, SpecialData[] specialList, EffectData[] commandEffectList)
	{
		super(unitNumber, maxHealthPoint, healthPointRecover, moveRange, defence, effectResistance, maxStamina, staminaRecover, visionRange, location, attackList, supportList, offensiveAttackList, unitID, unitType, name, player, maxSpecialPoint, specialPointGain, specialPointDamageGain, specialList);
		this.commandEffectList = commandEffectList;
		this.isSpecial = 2;
		resetDefactoValues();
	}
	
	public EffectData[] getCommandEffectList()
	{
		return commandEffectList;
	}
	
	public EffectData getCommandEffectList(int i)
	{
		return commandEffectList[i];
	}
	
	public SpecialUnit getSpecialUnit()
	{
		return this;
	}
	
	public Commander getCommander()
	{
		return this;
	}
	
	public boolean getActiveCommander()
	{
		return activeCommander;
	}
	
	public void setActiveCommander()
	{
		activeCommander = true;
	}
	
	public void setInactiveCommander()
	{
		activeCommander = false;
	}
}
