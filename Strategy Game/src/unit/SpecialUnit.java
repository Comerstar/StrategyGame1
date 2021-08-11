package unit;

import java.util.ArrayList;

import org.json.simple.JSONArray;

import attack.*;
import game.Game;
import utilities.ArrayHandler;
import utilities.Location;

public class SpecialUnit extends Unit{

	//                                                                0  1  2  3  4  5  6  7  8  9 10 11 12 13 14 15 16 17 18 19 20, 21
	protected static final double[] specialBoostReset = new double[] {0, 1, 0, 1, 0, 1, 0, 1, 0, 1, 0, 1, 0, 1, 0, 1, 0, 1, 0, 1, 0, 1};
	protected double specialPoint;
	protected double maxSpecialPoint;
	protected double specialPointGain;
	protected double specialPointDamageGain;
	protected Special[] specialList;
	protected double form;
	//BoostList: Holds attack bonuses. 0: Fixed Power  1: Multiplier Power  2: Fixed Range  3: Multiplier Range  4: Fixed Stamina  5: Multiplier Stamina  6: Move Fixed Stamina  
	//7: Move Multiplier Stamina 8: Fixed Attack AoE 9: Multiplier Attack AoE 10: Fixed InnerRange 11: Multiplier InnerRange, 12: Fixed Heal, 13: Multiplier Heal
	//14: Fixed SPG, 15: Multiplier SPG, 16: Fixed SPDG, 17: Multiplier SPDG, 18: Fixed SPC 19: Multiplier SPC, 20 Fixed SpecialSC, 21 Multiplier SpecialSC
	
	public SpecialUnit (int unitType, int X, int Y, int player, int unitID)
	{
		super(unitType, X, Y, player, unitID);
		
		JSONArray unitValues = (JSONArray) unitInfo.get(((Integer) this.unitNumber).toString());
		maxSpecialPoint = (double) unitValues.get(15);
		specialPoint = 0.0;
		specialPointGain = (double) unitValues.get(16);
		specialPointDamageGain = (double) unitValues.get(17);
		JSONArray specialValues = (JSONArray) unitValues.get(18);
		specialList = new Special[specialValues.size()];
		for(int i = 0; i < specialValues.size(); i++)
		{
			specialList[i] = new Special(((Long) specialValues.get(i)).intValue(), this);
		}
		isSpecial = 1;
	}
	
	public SpecialUnit (int unitNumber, double maxHealthPoint, double healthPointRecover, double moveRange, 
			double defence, double effectResistance, double maxStamina, 
			double staminaRecover, double visionRange,
			Location location, Attack[] attackList, Attack[] supportList, Attack[] offensiveAttackList,  
			int unitID, int unitType, String name, int player, double maxSpecialPoint, double specialPointGain, double specialPointDamageGain, Special[] specialList)
	{
		super(unitNumber,  maxHealthPoint, healthPointRecover, moveRange, defence, effectResistance, maxStamina, staminaRecover, visionRange, location, attackList, supportList, offensiveAttackList, unitID, unitType, name, player);
		this.maxSpecialPoint =  maxSpecialPoint;
		this.specialPoint = maxSpecialPoint;
		this.specialPointGain = specialPointGain;
		this.specialPointDamageGain = specialPointDamageGain;
		this.specialList = specialList;
		form = 0;
		resetDefactoValues();
		this.isSpecial = 1;
	}
	
	public SpecialUnit (int unitNumber, double maxHealthPoint, double healthPointRecover, double moveRange, 
			double defence, double effectResistance, double maxStamina, 
			double staminaRecover, double visionRange,
			Location location, AttackData[] attackList, AttackData[] supportList, AttackData[] offensiveAttackList,  
			int unitID, int unitType, String name, int player, double maxSpecialPoint, double specialPointGain, double specialPointDamageGain, SpecialData[] specialList)
	{
		super(unitNumber,  maxHealthPoint, healthPointRecover, moveRange, defence, effectResistance, maxStamina, staminaRecover, visionRange, location, attackList, supportList, offensiveAttackList, unitID, unitType, name, player);
		this.maxSpecialPoint =  maxSpecialPoint;
		this.specialPoint = maxSpecialPoint;
		this.specialPointGain = specialPointGain;
		this.specialPointDamageGain = specialPointDamageGain;
		this.specialList = ArrayHandler.initialiseSpecialList(specialList, this);
		form = 0;
		resetDefactoValues();
		this.isSpecial = 1;
	}
	
	public void resetDefactoValues()
	{
		defactoHealthPointRecover = healthPointRecover;
		defactoStaminaRecover = staminaRecover;
		defactoMoveRange = moveRange;
		defactoDefence = defence;
		defactoVisionRange = visionRange; 
		defactoEffectResistance = effectResistance;
		defactoMoveNumber = moveNumber;
		attackNumber = 0.0;
		boostList = specialBoostReset.clone();
	}
	
	public double getSpecialPoint()
	{
		return specialPoint;
	}
	
	public double getMaxSpecialPoint()
	{
		return maxSpecialPoint;
	}
	
	public double getSpecialPointGain()
	{
		return specialPointGain;
	}
	
	public Special[] getSpecialList()
	{
		return specialList;
	}
	
	public Special getSpecialList(int i)
	{
		return specialList[i];
	}
	
	public boolean isSpecial()
	{
		return true;
	}
	
	public SpecialUnit getSpecialUnit()
	{
		return this;
	}
	
	public Commander getCommander()
	{
		return null;
	}
	
	/**
	 * A function that handles special attacks
	 * @param specialNumber - The number of the special attack
	 * @param location - A location target that is used for some specials
	 * @param game - The game that is currently in play. Used for some specials and player and unit data.
	 * @param entryData - Any numbers used in special attacks, such as form change numbers. 
	 */
	public void special(int specialNumber, Location location, Game game, double[] entryData)
	{
		Special special = specialList[specialNumber];
		specialPoint -= special.getSpecialPointCost() * boostList[19] + boostList[18];
		stamina -= special.getStaminaCost() * boostList[21] + boostList[20];
		if (special.getSpecialType() == 0)
		{
			game.attack(special.getAttackList()[0], location);
		}
		else if (special.getSpecialType() == 1)
		{
			form = entryData[0];
			attacked = true;
		}
		else if (special.getSpecialType() == 2)
		{
			int i;
			int j;
			for (i = 0; i < game.getCurrentPlayer().getNumberOfUnit(); i++)
			{
				for (j = 0; j < special.getEffectList().length; j++)
				{
					game.getCurrentPlayer().getUnit(i).addEffect(special.getEffectList()[j].makeEffect(0, player));
				}
			}
			attacked = true;
		}
		else if (special.getSpecialType() == 3)
		{
			int i;
			int j;
			int k;
			for (i = 0; i < game.getPlayer(); i++)
			{
				if(i == player)
				{
					i++;
				}
				for (j = 0; j < game.getPlayerList(i).getNumberOfUnit(); j++)
				{
					for (k = 0; k < special.getEffectList().length; k++)
					{
						game.getPlayerList(i).getUnit(j).addEffect(special.getEffectList()[k].makeEffect(0, player));
					}
				}
			}
			attacked = true;
		}
		else if (special.getSpecialType() == 4)
		{
			// TODO Finish
			attacked = true;
		}
	}
	
	public void special(Special special, Location location, Game game, double[] entryData)
	{
		if(!game.getAttacking())
		{
			specialPoint -= special.getSpecialPointCost() * boostList[19] + boostList[18];
			stamina -= special.getStaminaCost() * boostList[21] + boostList[20];
		}
		if (special.getSpecialType() == 0)
		{
			game.attack(special.getAttackList()[0], location);
		}
		else if (special.getSpecialType() == 1)
		{
			form = entryData[0];
			attacked = true;
		}
		else if (special.getSpecialType() == 2)
		{
			int i;
			int j;
			System.out.println("2 Special");
			for (i = 0; i < game.getCurrentPlayer().getNumberOfUnit(); i++)
			{
				for (j = 0; j < special.getEffectList().length; j++)
				{
					System.out.println("Adding Effects");
					game.getCurrentPlayer().getUnit(i).addEffect(special.getEffectList()[j].makeEffect(0, player));
				}
			}
			attacked = true;
		}
		else if (special.getSpecialType() == 3)
		{
			int i;
			int j;
			int k;
			for (i = 0; i < game.getPlayer(); i++)
			{
				if(i == player)
				{
					i++;
				}
				for (j = 0; j < game.getPlayerList(i).getNumberOfUnit(); j++)
				{
					for (k = 0; k < special.getEffectList().length; k++)
					{
						game.getPlayerList(i).getUnit(j).addEffect(special.getEffectList()[k].makeResistedEffect(game.getPlayerList(i).getUnit(j).getDefactoEffectResistance(), 0, player));
					}
				}
			}
			attacked = true;
		}
		else if (special.getSpecialType() == 4)
		{
			attacked = true;
		}
	}
	
	public void attack(ArrayList<Unit> targetList, Attack attack, Location location)
	{
		if(!attacked)
		{
			attackNumber--;
			if(attackNumber <= 0.0)
			{
				attacked = true;
			}
			this.stamina -= attack.getStaminaCost() * boostList[4] + boostList[5];
			if(attack.AoE())
			{
				int i = 0;
				for(i = 0; i < targetList.size(); i++)
				{
					if(targetList.get(i).getPlayer() == player)
					{
						specialPoint += Math.abs(targetList.get(i).returnDamage(attack, this) * specialPointDamageGain);
					}
					else
					{
						specialPoint += Math.abs(targetList.get(i).returnDefendedDamage(attack, this) * specialPointDamageGain);
					}
					this.applyAttackEffects(attack, targetList.get(i));
				}
				if(specialPoint > maxSpecialPoint)
				{
					specialPoint = maxSpecialPoint;
				}
			}
			else
			{
				if(targetList.size() == 1)
				{
					Unit target = targetList.get(0);
					if(target.getPlayer() == player)
					{
						specialPoint += Math.abs(target.returnDamage(attack, this) * specialPointDamageGain);
					}
					else
					{
						specialPoint += Math.abs(target.returnDefendedDamage(attack, this) * specialPointDamageGain);
					}
					if(specialPoint > maxSpecialPoint)
					{
						specialPoint = maxSpecialPoint;
					}
					this.applyAttackEffects(attack, target);
					int attackID = target.getCounterAttack(this);
					if (attackID != -1)
					{
						if (target.getAlive() && target.getPlayer() != player)
						{
							this.damage(target.getAttackList()[attackID], target);
						}
					}
				}
			}
		}
	}
	
	public void addSpecialPoint(double damage)
	{
		specialPoint += Math.abs(damage * specialPointDamageGain);
		if(specialPoint > maxSpecialPoint)
		{
			specialPoint = maxSpecialPoint;
		}
	}
	
	public void incrementTurn()
	{
		super.incrementTurn();
		specialPoint += specialPointGain;
		if(specialPoint > maxSpecialPoint)
		{
			specialPoint = maxSpecialPoint;
		}
	}
}
