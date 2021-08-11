package unit;
import game.Game;
import attack.*;
import map.BuildingMap;
import map.UnitMap;
import terrain.Building;
import utilities.*;

import java.util.ArrayList;

import org.json.simple.*; 

/**
 * The core basic Unit with instance related variables, such as current health. The unit does not have checks for actions. 
 * The unit mainly handles the data for attacks, and their relavant effects.
 * 
 * @author The Programmer (Comerstar)
 */
public class Unit extends UnitData
{
	
	//                                                         0  1  2  3  4  5  6  7  8  9 10 11 12 13
	static final double[] boostReset = new double[] {0, 1, 0, 1, 0, 1, 0, 1, 0, 1, 0, 1, 0, 1};
	//Holds the maximum and minimum values statistics are allowed to be, 1: healthPointRecover, 2: moveRange, 3: defence, 4: effectResistance
	static double[][] statLimits = new double[][]{{0.0,65535.0},{0.0, 60000.0},{-200.0, 100.0}, {-200.0, 100.0}};
	protected double defactoHealthPointRecover;
	protected double healthPoint;
	protected double defactoMoveRange;
	protected double defactoDefence;
	protected double defactoEffectResistance;
	protected double defactoStaminaRecover;
	protected double stamina;
	protected double defactoVisionRange;
	//Holds attack bonuses. 0: Fixed Power  1: Multiplier Power  2: Fixed Range  3: Multiplier Range  4: Fixed Stamina  5: Multiplier Stamina  6: Move Fixed Stamina  
	//7: Move Multiplier Stamina 8: Fixed Attack AoE 9: Multiplier Attack AoE 10: Fixed InnerRange 11: Multiplier InnerRange, 12: Fixed Heal, 13: Multiplier Heal
	protected double[] boostList;
	protected Location location;
	protected Attack[] attackList;
	protected Attack[] supportList;
	protected Attack[] offensiveAttackList;
	protected ArrayList<Effect> effectList;
	protected ArrayList<EffectField> effectFieldList;
	protected ArrayList<EffectField> effectFieldCentredList;
	protected int unitID;
	protected int player;
	protected boolean moved;
	protected boolean attacked;
	protected boolean alive;
	protected double attackNumber;
	protected double moveNumber;
	protected double defactoMoveNumber;
	// 0 = normal unit, 1 = special unit, 2 = commander
	protected int isSpecial;
	
	public static double[][] getStatLimits()
	{
		return statLimits;
	}
	
	public Unit (int unitNumber, double maxHealthPoint, double healthPointRecover, double moveRange, 
			double defence, double effectResistance, double maxStamina, 
			double staminaRecover, double visionRange,
			Location location, Attack[] attackList, Attack[] supportList, Attack[] offensiveAttackList,
			int unitID, int unitType, String name, int player)
	{
		super();
		this.unitNumber = unitNumber;
		this.maxHealthPoint = maxHealthPoint;
		this.healthPointRecover = healthPointRecover;
		this.healthPoint = maxHealthPoint;
		this.moveRange = moveRange;
		this.defence = defence;
		this.effectResistance = effectResistance;
		this.maxStamina = maxStamina;
		this.staminaRecover = staminaRecover;
		this.stamina = maxStamina;
		this.visionRange = visionRange;
		this.location = location;
		this.attackList = attackList;
		this.supportList = supportList;
		this.offensiveAttackList = offensiveAttackList;
		this.effectList = new ArrayList<Effect>();
		this.effectFieldList = new ArrayList<EffectField>();
		this.effectFieldCentredList = new ArrayList<EffectField>();
		this.unitID = unitID;
		this.unitType = unitType;
		this.name = name;
		this.player = player;
		this.moved = false;
		this.attacked = false;
		this.alive = false;
		resetDefactoValues();
		this.isSpecial = 1;
	}
	
	protected Unit()
	{
		super();
	}
	
	protected void setUnitData(int unitNumber, int y, int x, int player, int unitID)
	{
		location = new Location(y, x);
		this.unitNumber = unitNumber;
		JSONArray unitValues = (JSONArray) unitInfo.get(((Integer) this.unitNumber).toString());
		maxHealthPoint = (double) unitValues.get(0);
		healthPoint = maxHealthPoint;
		healthPointRecover = (double) unitValues.get(1);
		moveRange = (double) unitValues.get(2);
		defence = (double) unitValues.get(3);
		effectResistance = (double) unitValues.get(4);
		this.player = player;
		maxStamina = (double) unitValues.get(5);
		staminaRecover = (double) unitValues.get(6);
		stamina = maxStamina;
		visionRange = (double) unitValues.get(7);
		name = (String) unitValues.get(12);
		unitType = ((Long) unitValues.get(10)).intValue();
		JSONArray attacks = (JSONArray) unitValues.get(9);
		attackList = new Attack[attacks.size()];
		Attack[] tempAttackList = new Attack[attacks.size()];
		int i;
		for(i = 0; i < attacks.size(); i++)
		{
			tempAttackList[i] = new Attack(((Long) attacks.get(i)).intValue(), this);
		}
		int supportNumber = 0;
		int attackNumber = 0;
		Attack tempAttack;
		ArrayList<Attack> tempSupport = new ArrayList<Attack>();
		ArrayList<Attack> tempOffensive = new ArrayList<Attack>();
		for(i = 0; i < tempAttackList.length; i++)
		{
			tempAttack = tempAttackList[i];
			if(tempAttack.getAttackSupport() == 0)
			{
				supportNumber++;
				tempSupport.add(tempAttack);
			}
			else if(tempAttack.getAttackSupport() == 1)
			{
				attackNumber++;
				tempOffensive.add(tempAttack);
			}
		}
		supportList = new Attack[supportNumber];
		offensiveAttackList = new Attack[attackNumber];
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
		this.unitID = unitID;
		effectList = new ArrayList<Effect>();
		moved = false;
		attacked = false;
		moveNumber = 1.0;
		this.attackNumber = 1.0;
		resetDefactoValues();
		this.alive = true;
		this.effectFieldCentredList = new ArrayList<EffectField>();
		this.effectFieldList = new ArrayList<EffectField>();
		isSpecial = 0;
	}
	
	public Unit (int unitNumber, double maxHealthPoint, double healthPointRecover, double moveRange, 
			double defence, double effectResistance, double maxStamina, 
			double staminaRecover, double visionRange,
			Location location, AttackData[] attackList, AttackData[] supportList, AttackData[] offensiveAttackList,
			int unitID, int unitType, String name, int player)
	{
		super();
		this.unitNumber = unitNumber;
		this.maxHealthPoint = maxHealthPoint;
		this.healthPointRecover = healthPointRecover;
		this.healthPoint = maxHealthPoint;
		this.moveRange = moveRange;
		this.defence = defence;
		this.effectResistance = effectResistance;
		this.maxStamina = maxStamina;
		this.staminaRecover = staminaRecover;
		this.stamina = maxStamina;
		this.visionRange = visionRange;
		this.location = location;
		this.player = player;
		this.attackList = ArrayHandler.initialiseAttackList(attackList, this);
		this.supportList = ArrayHandler.initialiseAttackList(supportList, this);
		this.offensiveAttackList = ArrayHandler.initialiseAttackList(offensiveAttackList, this);
		this.effectList = new ArrayList<Effect>();
		this.effectFieldList = new ArrayList<EffectField>();
		this.effectFieldCentredList = new ArrayList<EffectField>();
		this.unitID = unitID;
		this.unitType = unitType;
		this.name = name;
		this.moved = false;
		this.attacked = false;
		this.alive = false;
		resetDefactoValues();
		this.isSpecial = 1;
	}
	
	private Unit (double defactoHealthPointRecover, double healthPoint, double defactoMoveRange, double defactoDefence, double defactoEffectResistance, 
			double defactoStaminaRecover, double stamina, double defactoVisionRange, double[] boostList, Location location, Attack[] attackList, 
			ArrayList<Effect> effectList, ArrayList<EffectField> effectFieldList, ArrayList<EffectField> effectFieldCentredList, int unitID, int player, boolean moved, 
			boolean attacked, boolean alive, double attackNumber, double moveNumber, double defactoMoveNumber, int isSpecial, int unitNumber, double maxHealthPoint, 
			double healthPointRecover, double moveRange, double defence, double effectResistance, double maxStamina, double staminaRecover, double visionRange, 
			int unitType, String name)
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
	}
	
	public Unit clone()
	{
		return new Unit(defactoHealthPointRecover, healthPoint, defactoMoveRange, defactoDefence, defactoEffectResistance, 
				defactoStaminaRecover, stamina, defactoVisionRange, boostList, location, attackList, 
				effectList, effectFieldList, effectFieldCentredList, unitID, player, moved, 
				attacked, alive, attackNumber, moveNumber, defactoMoveNumber, isSpecial, unitNumber, maxHealthPoint, 
				healthPointRecover, moveRange, defence, effectResistance, maxStamina, staminaRecover, visionRange, 
				unitType, name);
	}
	
	public Unit(int unitNumber, int y, int x, int player, int unitID) 
	{
		super();
		
		location = new Location(y, x);
		this.unitNumber = unitNumber;
		JSONArray unitValues = (JSONArray) unitInfo.get(((Integer) this.unitNumber).toString());
		maxHealthPoint = (double) unitValues.get(0);
		healthPoint = maxHealthPoint;
		healthPointRecover = (double) unitValues.get(1);
		moveRange = (double) unitValues.get(2);
		defence = (double) unitValues.get(3);
		effectResistance = (double) unitValues.get(4);
		this.player = player;
		maxStamina = (double) unitValues.get(5);
		staminaRecover = (double) unitValues.get(6);
		stamina = maxStamina;
		visionRange = (double) unitValues.get(7);
		name = (String) unitValues.get(12);
		unitType = ((Long) unitValues.get(10)).intValue();
		JSONArray attacks = (JSONArray) unitValues.get(9);
		attackList = new Attack[attacks.size()];
		Attack[] tempAttackList = new Attack[attacks.size()];
		int i;
		for(i = 0; i < attacks.size(); i++)
		{
			tempAttackList[i] = new Attack(((Long) attacks.get(i)).intValue(), this);
		}
		int supportNumber = 0;
		int attackNumber = 0;
		Attack tempAttack;
		ArrayList<Attack> tempSupport = new ArrayList<Attack>();
		ArrayList<Attack> tempOffensive = new ArrayList<Attack>();
		for(i = 0; i < tempAttackList.length; i++)
		{
			tempAttack = tempAttackList[i];
			if(tempAttack.getAttackSupport() == 0)
			{
				supportNumber++;
				tempSupport.add(tempAttack);
			}
			else if(tempAttack.getAttackSupport() == 1)
			{
				attackNumber++;
				tempOffensive.add(tempAttack);
			}
		}
		supportList = new Attack[supportNumber];
		offensiveAttackList = new Attack[attackNumber];
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
		for(i = 0; i < tempSupport.size(); i++)
		{
			attackList[i + offensiveAttackList.length] = supportList[i]; 
		}
		this.unitID = unitID;
		effectList = new ArrayList<Effect>();
		moved = false;
		attacked = false;
		moveNumber = 1.0;
		this.attackNumber = 1.0;
		resetDefactoValues();
		this.alive = true;
		this.effectFieldCentredList = new ArrayList<EffectField>();
		this.effectFieldList = new ArrayList<EffectField>();
		isSpecial = 0;
	}
	
	public Unit(int unitNumber, double maxHealthPoint, double healthPointRecover, double healthPoint, double moveRange, double defence, double effectResistance, 
			double maxStamina, double staminaRecover, double stamina, double visionRange, int[] location, Attack[] attackList, 
			ArrayList<Effect> effects, int unitID, int unitType, double maxAttackRange, int player)
	{
		this.unitNumber = unitNumber;
		this.maxHealthPoint = maxHealthPoint;
		this.healthPointRecover = healthPointRecover;
		this.healthPoint = healthPoint;
		this.moveRange = moveRange;
		this.defence = defence;
		this.effectResistance = effectResistance;
		this.maxStamina = maxStamina;
		this.staminaRecover = staminaRecover;
		this.stamina = stamina;
		this.visionRange = visionRange;
		this.location = new Location(location);
		this.attackList = attackList;
		this.effectList = effects;
		this.unitID = unitID;
		this.unitType = unitType;
		this.moved = false;
		this.attacked = false;
		this.player = player;
		effectList = new ArrayList<Effect>();
		moved = false;
		attacked = false;
		this.alive = true;
		this.effectFieldCentredList = new ArrayList<EffectField>();
		this.effectFieldList = new ArrayList<EffectField>();
		moveNumber = 1.0;
		attackNumber = 1.0;
		isSpecial = 0;
	}
	
	public int getIsSpecial()
	{
		return isSpecial;
	}
	
	public double getMoveNumber()
	{
		return moveNumber;
	}
	
	public double getDefactoMoveNumber()
	{
		return defactoMoveNumber;
	}
	
	public double getAttackNumber()
	{
		return attackNumber;
	}
	
	public int getUnitType()
	{
		return unitType;
	}
	
	public int getPlayer()
	{
		return player;
	}

	public Location getLocation()
	{
		return location;
	}
	
	public double getHealthPoint()
	{
		return healthPoint;
	}
	
	public double getMaxHealthPoint()
	{
		return maxHealthPoint;
	}
	
	public double getHealthPointRecover()
	{
		return healthPointRecover;
	}
	
	public double getDefactoHealthPointRecover()
	{
		return defactoHealthPointRecover;
	}

	public double getMoveRange()
	{
		return moveRange;
	}
	
	public double getDefactoMoveRange()
	{
		return defactoMoveRange;
	}

	public double getDefence()
	{
		return defence;
	}
	
	public double getDefactoDefence()
	{
		return defactoDefence;
	}

	public double getEffectResistance()
	{
		return effectResistance;
	}
	
	public double getDefactoEffectResistance()
	{
		return defactoEffectResistance;
	}

	public double getMaxStamina()
	{
		return maxStamina;
	}
	
	public double getStaminaRecover()
	{
		return staminaRecover;
	}
	
	public double getDefactoStaminaRecover()
	{
		return defactoStaminaRecover;
	}
	
	public double getStamina()
	{
		return stamina;
	}
	
	public double getVisionRange()
	{
		return visionRange;
	}
	
	public double getDefactoVisionRange()
	{
		return defactoVisionRange;
	}
	
	public int getY()
	{
		return location.getY();
	}
	
	public int getX()
	{
		return location.getX();
	}
	
	public boolean isSpecial()
	{
		return false;
	}
	
	public Attack[] getAttackList()
	{
		return attackList;
	}
	
	public Attack getAttack(int i)
	{
		return attackList[i];
	}
	
	public Attack[] getSupportList()
	{
		return supportList;
	}
	
	public Attack getSupport(int i)
	{
		return supportList[i];
	}
	
	public Attack[] getOffensiveAttackList()
	{
		return offensiveAttackList;
	}
	
	public Attack getOffensiveAttack(int i)
	{
		return offensiveAttackList[i];
	}
	
	public int getUnitID()
	{
		return unitID;
	}
	
	public int getUnitNumber()
	{
		return unitNumber;
	}
	
	public ArrayList<Effect> getEffectList()
	{
		return effectList;
	}
	
	public Effect getEffectList(int i)
	{
		return effectList.get(i);
	}
	
	public ArrayList<EffectField> getEffectFieldList()
	{
		return effectFieldList;
	}
	
	public EffectField getEffectFieldList(int i)
	{
		return effectFieldList.get(i);
	}
	
	public boolean getMoved()
	{
		return moved;
	}
	
	public boolean getAttacked()
	{
		return attacked;
	}
	
	public String getName()
	{
		return name;
	}
	
	public double[] getBoostList()
	{
		return boostList;
	}
	
	public double getBoostList(int i)
	{
		return boostList[i];
	}
	
	public boolean getAlive()
	{
		return alive;
	}
	
	public SpecialUnit getSpecialUnit()
	{
		return null;
	}
	
	public Commander getCommander()
	{
		return null;
	}
	
	public boolean isBuilding()
	{
		return false;
	}
	
	public Building getBuilding()
	{
		return null;
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
		boostList = boostReset.clone();
	}
	
	public void addToBoostList (int i, double add)
	{
		boostList[i] += add;
	}
	
	public int getCounterAttack(Unit target)
	{
		int attackID = -1;
		if(player != -1)
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
		}
		return damage;
	}
	
	public void applyAttackEffects(Attack attack, Unit target)
	{
		int i = 0;
		Effect effect;
		if (target.getAlive() && target.getPlayer() != -1)
		{
			for(i = 0; i < attack.getTargetEffectList().length; i++)
			{
				effect = attack.getTargetEffectList(i).makeResistedEffect(target.getDefactoEffectResistance(), 0, player);
				target.addEffect(effect);
			}
		}
		if (alive)
		{
			for(i = 0; i < attack.getSelfEffectList().length; i++)
			{
				addEffect(attack.getSelfEffectList(i).makeEffect(0, player));
			}
		}
	}
	
	public void addAttackNumber(int attackID)
	{
		attackNumber += attackList[attackID].getNumberOfTarget();
	}
	
	public void addAttackNumber(Attack attack)
	{
		attackNumber += attack.getNumberOfTarget();
	}
	
	public void removeAttackNumber(int attackID)
	{
		attackNumber -= attackList[attackID].getNumberOfTarget();
	}
	
	public void removeAttackNumber(Attack attack)
	{
		attackNumber -= attack.getNumberOfTarget();
	}
	
	public void endTurn()
	{
		attacked = true;
		moved = true;
	}
	
	public void endAttack()
	{
		attacked = true;
	}
	
	public boolean getActiveCommander()
	{
		return false;
	}
	
	@Deprecated
	public void attack(Unit target, int attackNumber)
	{
		if(!attacked)
		{
			attackNumber--;
			if(attackNumber <= 0.0)
			{
				attacked = true;
			}
			Attack attack = attackList[attackNumber];
			this.stamina -= attack.getStaminaCost() * boostList[5] + boostList[4];
			if(target.getPlayer() == player)
			{
				target.damage(attack, this);
			}
			else
			{
				target.defendedDamage(attack, this);
			}
			this.applyAttackEffects(attack, target);
			int attackID = -1;
			if(!attack.AoE())
			{
				attackID = target.getCounterAttack(this);
			}
			if (attackID != -1)
			{
				if (target.getAlive() && target.getPlayer() != player)
				{
					this.damage(target.getAttackList()[attackID], target);
				}
			}
		}
	}
	
	/**
	 * {@summary Allows the unit to attack a set of units that it determines from a UnitMap. } The attack handles damage and applying effects. It does not handle effect fields. 
	 * The attack function is also used for support operations, such as heals, which are simply attacks with negative damage and that target units on the same side.
	 * Supports are never counter-attacked. Counter-attacks do not apply effects. 
	 * 
	 * Supports are classified as any action that targets the same side as the unit. The support is not defended by the targeted unit.
	 * Attacks are classified as any action that targets the opponent. The attack is always defended by the opposing side.
	 * 
	 * The attack fetches the Units in range of the AoE from the UnitMap, however with the addition of buildings, it will no longer give the correct list.
	 * 
	 * The attack process is as follows: 
	 * 1: It is determined if the attack is an AoE attack or Not
	 * 2: If it is, the designated units are damaged and DO NOT counter-attack. The effects are applied.
	 * 2: If it is not an AoE attack, the single unit is damaged, and counter-attacks. The effects are applied.
	 * 
	 * @deprecated Due to the addition of Buildings, which are not handled on the unitMap.
	 * @param unitTileMap The UnitMap of the game.
	 * @param attackNumber The number of the attack that is to be executed.
	 * @param location The location that the attack is to be executed from.
	 * @see #attack(ArrayList, Attack, Location)
	 */
	@Deprecated
	public void attack(UnitMap unitTileMap, int attackNumber, Location location)
	{
		if(!attacked)
		{
			attackNumber--;
			if(attackNumber <= 0.0)
			{
				attacked = true;
			}
			Attack attack = attackList[attackNumber];
			this.stamina -= attack.getStaminaCost() * boostList[5] + boostList[4];
			if(attack.AoE())
			{
				ArrayList<Unit> target = attack.getUnitsDamaged(location, unitTileMap);
				int i = 0;
				for(i = 0; i < target.size(); i++)
				{
					if(target.get(i).getPlayer() == player)
					{
						target.get(i).damage(attack, this);
					}
					else
					{
						target.get(i).defendedDamage(attack, this);
					}
					this.applyAttackEffects(attack, target.get(i));
				}
			}
			else
			{
				Unit target = unitTileMap.getMap(location);
				if(target.getPlayer() == player)
				{
					target.damage(attack, this);
				}
				else
				{
					target.defendedDamage(attack, this);
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
	
	@Deprecated
	public void attack(UnitMap unitTileMap, Attack attack, Location location)
	{
		if(!attacked)
		{
			attackNumber--;
			if(attackNumber <= 0.0)
			{
				attacked = true;
			}
			this.stamina -= attack.getStaminaCost() * boostList[5] + boostList[4];
			if(attack.AoE())
			{
				ArrayList<Unit> target = attack.getUnitsDamaged(location, unitTileMap);
				int i = 0;
				for(i = 0; i < target.size(); i++)
				{
					if(target.get(i).getPlayer() == player)
					{
						target.get(i).damage(attack, this);
					}
					else
					{
						target.get(i).defendedDamage(attack, this);
					}
					this.applyAttackEffects(attack, target.get(i));
				}
			}
			else
			{
				Unit target = unitTileMap.getMap(location);
				if(target.getPlayer() == player)
				{
					target.damage(attack, this);
				}
				else
				{
					target.defendedDamage(attack, this);
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
	
	/**
	 * {@summary Allows the unit to attack a set of targeted units. } The attack handles damage and applying effects. It does not handle effect fields. 
	 * The attack function is also used for support operations, such as heals, which are simply attacks with negative damage and that target units on the same side.
	 * Supports are never counter-attacked. Counter-attacks do not apply effects. 
	 * 
	 * Supports are classified as any action that targets the same side as the unit. The support is not defended by the targeted unit.
	 * Attacks are classified as any action that targets the opponent. The attack is always defended by the opposing side.
	 * 
	 * The attack process is as follows: 
	 * 1: It is determined if the attack is an AoE attack or Not
	 * 2: If it is, the designated units are damaged and DO NOT counter-attack. The effects are applied.
	 * 2: If it is not an AoE attack, the single unit is damaged, and counter-attacks. The effects are applied.
	 * 
	 * @param targetList The list of units that the game has decided are being attacked.
	 * @param attack The attack that is to be executed.
	 * @param location The targeted location of the attack.
	 */
	public void attack(ArrayList<Unit> targetList, Attack attack, Location location)
	{
		if(!attacked)
		{
			attackNumber--;
			if(attackNumber <= 0.0)
			{
				attacked = true;
			}
			this.stamina -= attack.getStaminaCost() * boostList[5] + boostList[4];
			if(attack.AoE())
			{
				int i = 0;
				for(i = 0; i < targetList.size(); i++)
				{
					if(targetList.get(i).getPlayer() == player)
					{
						targetList.get(i).damage(attack, this);
					}
					else
					{
						targetList.get(i).defendedDamage(attack, this);
					}
					this.applyAttackEffects(attack, targetList.get(i));
				}
			}
			else
			{
				if(targetList.size() == 1)
				{
					Unit target = targetList.get(0);
					if(target.getPlayer() == player)
					{
						target.damage(attack, this);
					}
					else
					{
						target.defendedDamage(attack, this);
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
	
	public void attack(Attack attack)
	{
		if(!attacked && attack.getTarget() == 3)
		{
			attackNumber--;
			if(attackNumber <= 0.0)
			{
				attacked = true;
			}
			this.stamina -= attack.getStaminaCost() * boostList[5] + boostList[4];
		}
	}
	
	public void special(int specialNumber, Location location, Game game, double[] entryData)
	{
		
	}
	
	public void special(Special special, Location location, Game game, double[] entryData)
	{
		
	}
	
	public void addSpecialPoint(double damage)
	{
		
	}
	
	public Special[] getSpecialList()
	{
		return new Special[0];
	}
	
	@Deprecated
	public ArrayList<EffectField> applyAttackEffectFields(Unit target, Attack attack)
	{
		EffectField effectField;
		EffectFieldData effectFieldData;
		ArrayList<EffectField> effectFieldList = new ArrayList<EffectField>();
		effectFieldData = attack.getSelfEffectField();
		if (effectFieldData != null)
		{
			if (effectFieldData.getUnitCentred())
			{
				effectField = effectFieldData.makeEffectField(this, 0, player);
				effectField.setUnitCentre(this);
				this.effectFieldCentredList.add(effectField);
			}
			else
			{
				effectField = effectFieldData.makeEffectField(this.getLocation(), 0, player);
			}
			effectFieldList.add(effectField);
		}
		else
		{
			
		}
		effectFieldData = attack.getTargetEffectField();
		if (effectFieldData != null)
		{
			effectField = attack.getTargetEffectField().makeEffectField(target, 0, player);
			effectField.setUnitCentre(target);
			target.effectFieldCentredList.add(effectField);
		}
		else
		{
			
		}
		return effectFieldList;
	}
	
	@Deprecated
	public ArrayList<EffectField> applyAttackEffectFields(Unit target, Attack attack, UnitMap unitMap)
	{
		EffectField effectField;
		EffectFieldData effectFieldData;
		ArrayList<EffectField> effectFieldList = new ArrayList<EffectField>();
		effectFieldData = attack.getSelfEffectField();
		if (effectFieldData != null)
		{
			if (effectFieldData.getUnitCentred())
			{
				effectField = effectFieldData.makeEffectField(this, 0, player);
				effectField.setUnitCentre(this);
				this.effectFieldCentredList.add(effectField);
			}
			else
			{
				effectField = effectFieldData.makeEffectField(this.getLocation(), 0, player);
			}
			effectField.applyEffects(unitMap);
			effectFieldList.add(effectField);
		}
		effectFieldData = attack.getTargetEffectField();
		if (effectFieldData != null)
		{
			
			if (effectFieldData.getUnitCentred())
			{
				effectField = effectFieldData.makeEffectField(target, 0, target.getPlayer());
				effectField.setUnitCentre(target);
				this.effectFieldCentredList.add(effectField);
			}
			else
			{
				effectField = effectFieldData.makeEffectField(target.getLocation(), 0, target.getPlayer());
			}
			effectField.applyEffects(unitMap);
			effectFieldList.add(effectField);
		}
		return effectFieldList;
	}
	
	public ArrayList<EffectField> applyAttackEffectFields(Unit target, Attack attack, UnitMap unitMap, BuildingMap buildingMap)
	{
		EffectField effectField;
		EffectFieldData effectFieldData;
		ArrayList<EffectField> effectFieldList = new ArrayList<EffectField>();
		effectFieldData = attack.getSelfEffectField();
		if (effectFieldData != null)
		{
			if (effectFieldData.getUnitCentred())
			{
				effectField = effectFieldData.makeEffectField(this, 0, player);
				effectField.setUnitCentre(this);
				this.effectFieldCentredList.add(effectField);
			}
			else
			{
				effectField = effectFieldData.makeEffectField(this.getLocation(), 0, player);
			}
			effectField.applyEffects(unitMap, buildingMap);
			effectFieldList.add(effectField);
		}
		effectFieldData = attack.getTargetEffectField();
		if (effectFieldData != null)
		{
			
			if (effectFieldData.getUnitCentred())
			{
				effectField = effectFieldData.makeEffectField(target, 0, target.getPlayer());
				effectField.setUnitCentre(target);
				this.effectFieldCentredList.add(effectField);
			}
			else
			{
				effectField = effectFieldData.makeEffectField(target.getLocation(), 0, target.getPlayer());
			}
			effectField.applyEffects(unitMap, buildingMap);
			effectFieldList.add(effectField);
		}
		return effectFieldList;
	}
	
	public ArrayList<EffectField> applyAttackEffectFields(ArrayList<Unit> target, Attack attack, UnitMap unitMap)
	{
		EffectField effectField;
		EffectFieldData effectFieldData;
		ArrayList<EffectField> effectFieldList = new ArrayList<EffectField>();
		effectFieldData = attack.getSelfEffectField();
		if (effectFieldData != null)
		{
			if (effectFieldData.getUnitCentred())
			{
				effectField = effectFieldData.makeEffectField(this, 0, player);
				effectField.setUnitCentre(this);
				this.effectFieldCentredList.add(effectField);
			}
			else
			{
				effectField = effectFieldData.makeEffectField(this.getLocation(), 0, player);
			}
			effectField.applyEffects(unitMap);
			effectFieldList.add(effectField);
		}
		effectFieldData = attack.getTargetEffectField();
		if (effectFieldData != null)
		{
			
			if (effectFieldData.getUnitCentred())
			{
				for(int i = 0; i < target.size(); i++)
				{
					effectField = effectFieldData.makeEffectField(target.get(i), 0, player);
					effectField.setUnitCentre(target.get(i));
					target.get(i).effectFieldCentredList.add(effectField);
					effectField.applyEffects(unitMap);
					effectFieldList.add(effectField);
				}
			}
			else
			{
				for(int i = 0; i < target.size(); i++)
				{
					effectField = effectFieldData.makeEffectField(target.get(i).getLocation(), 0, player);
					effectField.applyEffects(unitMap);
					effectFieldList.add(effectField);
				}
			}
		}
		return effectFieldList;
	}
	public ArrayList<EffectField> applyAttackEffectFields(ArrayList<Unit> target, Attack attack, UnitMap unitMap, BuildingMap buildingMap)
	{
		EffectField effectField;
		EffectFieldData effectFieldData;
		ArrayList<EffectField> effectFieldList = new ArrayList<EffectField>();
		effectFieldData = attack.getSelfEffectField();
		if (effectFieldData != null)
		{
			if (effectFieldData.getUnitCentred())
			{
				effectField = effectFieldData.makeEffectField(this, 0, player);
				effectField.setUnitCentre(this);
				this.effectFieldCentredList.add(effectField);
			}
			else
			{
				effectField = effectFieldData.makeEffectField(this.getLocation(), 0, player);
			}
			effectField.applyEffects(unitMap, buildingMap);
			effectFieldList.add(effectField);
		}
		effectFieldData = attack.getTargetEffectField();
		if (effectFieldData != null)
		{
			
			if (effectFieldData.getUnitCentred())
			{
				for(int i = 0; i < target.size(); i++)
				{
					effectField = effectFieldData.makeEffectField(target.get(i), 0, player);
					effectField.setUnitCentre(target.get(i));
					target.get(i).effectFieldCentredList.add(effectField);
					effectField.applyEffects(unitMap, buildingMap);
					effectFieldList.add(effectField);
				}
			}
			else
			{
				for(int i = 0; i < target.size(); i++)
				{
					effectField = effectFieldData.makeEffectField(target.get(i).getLocation(), 0, player);
					effectField.applyEffects(unitMap, buildingMap);
					effectFieldList.add(effectField);
				}
			}
		}
		return effectFieldList;
	}
		
	public ArrayList<EffectField> applyAttackEffectFields(Location target, Attack attack, UnitMap unitMap)
	{
		EffectField effectField;
		EffectFieldData effectFieldData;
		ArrayList<EffectField> effectFieldList = new ArrayList<EffectField>();
		effectFieldData = attack.getSelfEffectField();
		if (effectFieldData != null)
		{
			if (effectFieldData.getUnitCentred())
			{
				effectField = effectFieldData.makeEffectField(this, 0, player);
				effectField.setUnitCentre(this);
				this.effectFieldCentredList.add(effectField);
			}
			else
			{
				effectField = effectFieldData.makeEffectField(this.getLocation(), 0, player);
			}
			effectField.applyEffects(unitMap);
			effectFieldList.add(effectField);
		}
		effectFieldData = attack.getTargetEffectField();
		if (effectFieldData != null)
		{
			if (effectFieldData.getUnitCentred())
			{
				Unit unit = unitMap.getMap(target);
				if(unit != null)
				{
					effectField = effectFieldData.makeEffectField(unit, 0, player);
					effectField.setUnitCentre(unit);
					this.effectFieldCentredList.add(effectField);
					effectField.applyEffects(unitMap);
					effectFieldList.add(effectField);
				}
			}
			else
			{
				effectField = effectFieldData.makeEffectField(target, 0, player);
				effectField.applyEffects(unitMap);
				effectFieldList.add(effectField);
			}
		}
		return effectFieldList;
	}
	
	public ArrayList<EffectField> applyAttackEffectFields(Location target, Attack attack, UnitMap unitMap, BuildingMap buildingMap)
	{
		EffectField effectField;
		EffectFieldData effectFieldData;
		ArrayList<EffectField> effectFieldList = new ArrayList<EffectField>();
		effectFieldData = attack.getSelfEffectField();
		if (effectFieldData != null)
		{
			if (effectFieldData.getUnitCentred())
			{
				effectField = effectFieldData.makeEffectField(this, 0, player);
				effectField.setUnitCentre(this);
				this.effectFieldCentredList.add(effectField);
			}
			else
			{
				effectField = effectFieldData.makeEffectField(this.getLocation(), 0, player);
			}
			effectField.applyEffects(unitMap, buildingMap);
			effectFieldList.add(effectField);
		}
		effectFieldData = attack.getTargetEffectField();
		if (effectFieldData != null)
		{
			if (effectFieldData.getUnitCentred())
			{
				Unit unit = unitMap.getMap(target);
				if(unit != null)
				{
					effectField = effectFieldData.makeEffectField(unit, 0, player);
					effectField.setUnitCentre(unit);
					this.effectFieldCentredList.add(effectField);
					effectField.applyEffects(unitMap, buildingMap);
					effectFieldList.add(effectField);
				}
				Building building = buildingMap.getMap(target);
				if(building != null)
				{
					if(building.getPlayer() != -1)
					{
						effectField = effectFieldData.makeEffectField(building, 0, player);
						effectField.setUnitCentre(building);
						this.effectFieldCentredList.add(effectField);
						effectField.applyEffects(unitMap, buildingMap);
						effectFieldList.add(effectField);
					}
				}
			}
			else
			{
				effectField = effectFieldData.makeEffectField(target, 0, player);
				effectField.applyEffects(unitMap, buildingMap);
				effectFieldList.add(effectField);
			}
		}
		return effectFieldList;
	}
	
	public void addEffect (Effect effect)
	{
		effect.setEffectID(effectList.size());
		addEffectEffects(effect);
		effectList.add(effect);
	}
	
	public void addEffectField (EffectField effectField)
	{
		effectField.setEffectFieldID(effectFieldList.size());
		effectFieldList.add(effectField);
		addEffectFieldEffects(effectField);
	}
	
	public void addEffectFieldEffects(EffectField effectField)
	{
		int i = 0;
		for (i = 0; i < effectField.getEffectList().length; i++)
		{
			if (effectField.getPlayer() == player)
			{
				if(effectField.getTarget() == 0 || effectField.getTarget() == 2)
				{
					addEffectEffects(effectField.getEffectList(i).makeEffect(0, player));
				}
			}
			else
			{
				if(effectField.getTarget() == 1 || effectField.getTarget() == 2)
				{
					addEffectEffects(effectField.getEffectList(i).makeResistedEffect(defactoEffectResistance, 0, player));
				}
			}
		}
	}
	
	public void removeEffectField (int effectID)
	{
		EffectField effectField = effectFieldList.get(effectID);
		removeEffectFieldEffects(effectField);
		effectFieldList.remove(effectID);
	}
	
	public void removeEffectField (EffectField effectField)
	{
		if (effectFieldList.contains(effectField))
		{
			removeEffectFieldEffects(effectField);
			effectFieldList.remove(effectField);
		}
	}
	
	public void removeEffectFieldEffects(EffectField effectField)
	{
		int i = 0;
		for (i = 0; i < effectField.getEffectList().length; i++)
		{
			if (effectField.getPlayer() == player)
			{
				if(effectField.getTarget() == 0 || effectField.getTarget() == 2)
				{
					removeEffectEffects(effectField.getEffectList(i).makeEffect(0, player));
				}
			}
			else
			{
				if(effectField.getTarget() == 1 || effectField.getTarget() == 2)
				{
					removeEffectEffects(effectField.getEffectList(i).makeResistedEffect(defactoEffectResistance, 0, player));
				}
			}
		}
	}
	
	public void clearEffectFieldList()
	{
		effectFieldList.clear();
		fullUpdateEffectEffects();
	}
	
	public void clearEffectFieldCentredListEffect(UnitMap unitTileMap, BuildingMap buildingTileMap)
	{
		for(int i = 0; i < effectFieldCentredList.size(); i++)
		{
			effectFieldCentredList.get(i).removeEffects(unitTileMap, buildingTileMap);
			effectFieldCentredList.get(i).setInactive();
		}
		effectFieldCentredList.clear();
	}
	
	public void inactiveEffectFieldCentredList()
	{
		for(int i = 0; i < effectFieldCentredList.size(); i++)
		{
			effectFieldCentredList.get(i).setInactive();
		}
	}
	
	public void clearEffectFieldCentredlist()
	{
		effectFieldCentredList.clear();
	}
	
	public void updateEffectFieldList()
	{
		ArrayList<Integer> removeField = new ArrayList<Integer>();
		int i = 0;
		for (i = 0; i < effectFieldList.size(); i++)
		{
			if(effectFieldList.get(i).checkIfInRange(this) || !effectFieldList.get(i).getActive())
			{
				removeField.add(i);
			}
		}
		for (i = removeField.size() - 1; i >= 0; i--)
		{
			removeEffectField(i);
		}
	}
	
	public void updateEffectFieldList(ArrayList<EffectField> gameEffectFieldList)
	{
		int i = 0;
		clearEffectFieldList();
		EffectField effectField;
		for (i = 0; i < gameEffectFieldList.size(); i++)
		{
			effectField = gameEffectFieldList.get(i);
			if (this.location.calculateDistance(effectField.getCentre()) <= effectField.getRange())
			{
				if (this.player == effectField.getPlayer())
				{
					if (effectField.getTarget() == 0 || effectField.getTarget() == 2)
					{
						addEffectField(effectField);
					}
				}
				else
				{
					if (effectField.getTarget() == 1 || effectField.getTarget() == 2)
					{
						addEffectField(effectField);
					}
				}
			}
		}
		fullUpdateEffectEffects();
	}
	
	public void removeEffect (int effectID)
	{
		removeEffectEffects(effectList.get(effectID));
		effectList.remove(effectID);
		updateEffectID();
	}
	
	public void removeSameEffect (EffectData effect)
	{
		boolean removed = false;
		for(int i = 0; i < effectList.size(); i++)
		{
			if(effect.sameEffect(effectList.get(i)) && !removed)
			{
				effectList.remove(i);
				i--;
				removed = true;
			}
			else if(removed)
			{
				effectList.get(i).setEffectID(i);
			}
		}
	}
	
	public void updateEffectID ()
	{
		for (int i = 0; i < effectList.size(); i++)
		{
			effectList.get(i).setEffectID(i);
		}
	}
	
	public void move(int[] location, double moveRangeUsed)
	{
		if (!moved)
		{
			moved = true;
			this.location.set(location);
			this.stamina -= 5 * (moveRange - moveRangeUsed) * boostList[7] + boostList[6];
			updateEffectFieldList();
			fullUpdateEffectEffects();
		}
	}
	
	public void move(int[] location, double moveRangeUsed, ArrayList<EffectField> gameEffectFieldList)
	{
		if (!moved)
		{
			moved = true;
			this.location.set(location);
			this.stamina -= 5 * (moveRange - moveRangeUsed) * boostList[7] + boostList[6];
			updateEffectFieldList(gameEffectFieldList);
			fullUpdateEffectEffects();
		}
	}
	
	public void move(int[] location, double moveRangeUsed, ArrayList<EffectField> gameEffectFieldList, UnitMap unitTileMap)
	{
		if (!moved)
		{
			moved = true;
			int i = 0;
			for(i = 0; i < effectFieldCentredList.size(); i++)
			{
				effectFieldCentredList.get(i).removeEffects(unitTileMap);
			}
			this.location.set(location);
			this.stamina -= 5 * (moveRange - moveRangeUsed) * boostList[7] + boostList[6];
			updateEffectFieldList(gameEffectFieldList);
			for(i = 0; i < effectFieldCentredList.size(); i++)
			{
				effectFieldCentredList.get(i).applyEffects(unitTileMap);
			}
			fullUpdateEffectEffects();
		}
	}
	
	public void setUnitID(int unitID)
	{
		this.unitID = unitID;
	}
	
	public void addEffectEffects(Effect effect)
	{
		// TODO Add All Applicable Effects
		int effectType = effect.getEffectType();
		double effectPower = effect.getEffectPower();
		switch (effectType)
		{
		case 0:
			defactoMoveRange += effectPower;
			break;
		case 1:
			defactoMoveRange += effectPower / 100 * moveRange;
			break;
		case 2:
			defactoDefence += effectPower;
			break;
		case 3:
			defactoDefence += effectPower / 100 * defence;
			break;
		case 4:
			boostList[0] += effectPower;
			break;
		case 5:
			boostList[1] += effectPower / 100;
			break;
		case 6:
			boostList[2] += effectPower;
			break;
		case 7:
			boostList[3] += effectPower / 100;
			break;
		case 8:
			defactoHealthPointRecover += effectPower;
			break;
		case 9:
			defactoHealthPointRecover += effectPower / 100 * healthPointRecover;
			break;
		case 10:
			defactoStaminaRecover += effectPower;
			break;
		case 11: 
			defactoStaminaRecover += effectPower / 100 * staminaRecover;
			break;
		case 12:
			boostList[4] += effectPower;
			break;
		case 13:
			boostList[5] += effectPower / 100;
			break;
		case 14:
			boostList[6] += effectPower;
			break;
		case 15:
			boostList[7] += effectPower / 100;
			break;
		case 22:
			defactoEffectResistance += effectPower;
			break;
		case 23: 
			defactoEffectResistance += effectPower / 100 * effectResistance;
			break;
		case 24:
			defactoVisionRange += effectPower;
			break;
		case 25:
			defactoVisionRange += effectPower / 100 * visionRange;
			break;
		case 100:
			moved = true;
			attacked = true;
			break;
		case 102:
			attacked = true;
			break;
		case 103:
			moved = true;
			break;
		}
	}
	
	public void removeEffectEffects(Effect effect)
	{
		// TODO Add All Applicable Effects
		int effectType = -effect.getEffectType();
		double effectPower = effect.getEffectPower();
		switch (effectType)
		{
		case 0:
			defactoMoveRange += effectPower;
			break;
		case 1:
			defactoMoveRange += effectPower / 100 * moveRange;
			break;
		case 2:
			defactoDefence += effectPower;
			break;
		case 3:
			defactoDefence += effectPower / 100 * defence;
			break;
		case 4:
			boostList[0] += effectPower;
			break;
		case 5:
			boostList[1] += effectPower / 100;
			break;
		case 6:
			boostList[2] += effectPower;
			break;
		case 7:
			boostList[3] += effectPower / 100;
			break;
		case 8:
			defactoHealthPointRecover += effectPower;
			break;
		case 9:
			defactoHealthPointRecover += effectPower / 100 * healthPointRecover;
			break;
		case 10:
			defactoStaminaRecover += effectPower;
			break;
		case 11: 
			defactoStaminaRecover += effectPower / 100 * staminaRecover;
			break;
		case 12:
			boostList[4] += effectPower;
			break;
		case 13:
			boostList[5] += effectPower / 100;
			break;
		case 14:
			boostList[6] += effectPower;
			break;
		case 15:
			boostList[7] += effectPower / 100;
			break;
		case 22:
			defactoEffectResistance += effectPower;
			break;
		case 23: 
			defactoEffectResistance += effectPower / 100 * effectResistance;
			break;
		case 24:
			defactoVisionRange += effectPower;
			break;
		case 25:
			defactoVisionRange += effectPower / 100 * visionRange;
			break;
		case 100:
			moved = false;
			attacked = false;
			break;
		case 102:
			attacked = false;
			break;
		case 103:
			moved = false;
			break;
		}
	}
	
	public void fullUpdateEffectEffects()
	{
		// TODO Add All Applicable Effects
		int i = 0;
		int j = 0;
		Effect effect;
		int effectType = -1;
		double effectPower = -1;
		resetDefactoValues();
		for (i = 0; i < effectList.size(); i++)
		{
			effect = effectList.get(i);
			effectType = effect.getEffectType();
			effectPower = effect.getEffectPower();
			if(effect.getActive())
			{
				switch (effectType)
				{
				case 0:
					defactoMoveRange += effectPower;
					break;
				case 1:
					defactoMoveRange += effectPower / 100 * moveRange;
					break;
				case 2:
					defactoDefence += effectPower;
					break;
				case 3:
					defactoDefence += effectPower / 100 * defence;
					break;
				case 4:
					boostList[0] += effectPower;
					break;
				case 5:
					boostList[1] += effectPower / 100;
					break;
				case 6:
					boostList[2] += effectPower;
					break;
				case 7:
					boostList[3] += effectPower / 100;
					break;
				case 8:
					defactoHealthPointRecover += effectPower;
					break;
				case 9:
					defactoHealthPointRecover += effectPower / 100 * healthPointRecover;
					break;
				case 10:
					defactoStaminaRecover += effectPower;
					break;
				case 11: 
					defactoStaminaRecover += effectPower / 100 * staminaRecover;
					break;
				case 12:
					boostList[4] += effectPower;
					break;
				case 13:
					boostList[5] += effectPower / 100;
					break;
				case 14:
					boostList[6] += effectPower;
					break;
				case 15:
					boostList[7] += effectPower / 100;
					break;
				case 22:
					defactoEffectResistance += effectPower;
					break;
				case 23: 
					defactoEffectResistance += effectPower / 100 * effectResistance;
					break;
				case 24:
					defactoVisionRange += effectPower;
					break;
				case 25:
					defactoVisionRange += effectPower / 100 * visionRange;
					break;
				}
			}
		}
		EffectData[] effectFieldEffectList;
		EffectField effectField;
		for (i = 0; i < effectFieldList.size(); i++)
		{
			effectField = effectFieldList.get(i);
			effectFieldEffectList = effectFieldList.get(i).getEffectList();
			for (j = 0; j < effectFieldEffectList.length; j++)
			{
				if (effectField.getPlayer() != player)
				{
					effect = effectFieldEffectList[j].makeResistedEffect(defactoEffectResistance, 0, effectField, true, effectField.getPlayer());
				}
				else
				{
					effect = effectFieldEffectList[j].makeEffect(0, effectField, true, effectField.getPlayer());
				}
				effectType = effect.getEffectType();
				effectPower = effect.getEffectPower();
				if(effect.getActive())
				{
					switch (effectType)
					{
					case 0:
						defactoMoveRange += effectPower;
						break;
					case 1:
						defactoMoveRange += effectPower / 100 * moveRange;
						break;
					case 2:
						defactoDefence += effectPower;
						break;
					case 3:
						defactoDefence += effectPower / 100 * defence;
						break;
					case 4:
						boostList[0] += effectPower;
						break;
					case 5:
						boostList[1] += effectPower / 100;
						break;
					case 6:
						boostList[2] += effectPower;
						break;
					case 7:
						boostList[3] += effectPower / 100;
						break;
					case 8:
						defactoHealthPointRecover += effectPower;
						break;
					case 9:
						defactoHealthPointRecover += effectPower / 100 * healthPointRecover;
						break;
					case 10:
						defactoStaminaRecover += effectPower;
						break;
					case 11: 
						defactoStaminaRecover += effectPower / 100 * staminaRecover;
						break;
					case 12:
						boostList[4] += effectPower;
						break;
					case 13:
						boostList[5] += effectPower / 100;
						break;
					case 14:
						boostList[6] += effectPower;
						break;
					case 15:
						boostList[7] += effectPower / 100;
						break;
					case 22:
						defactoEffectResistance += effectPower;
						break;
					case 23: 
						defactoEffectResistance += effectPower / 100 * effectResistance;
						break;
					case 24:
						defactoVisionRange += effectPower;
						break;
					case 25:
						defactoVisionRange += effectPower / 100 * visionRange;
						break;
					}
				}
			}
		}
	}
	
	public void clearAllEffect()
	{
		effectList.clear();
	}
	
	public void incrementTurn()
	{
		// TODO Add All Applicable Effects
		int i = 0;
		int j = 0;
		Effect effect;
		int effectType = -1;
		double effectPower = -1;
		double preHealth = healthPoint;
		double preStamina = stamina;
		defactoHealthPointRecover = healthPointRecover;
		defactoStaminaRecover = staminaRecover;
		defactoMoveRange = moveRange;
		defactoDefence = defence;
		defactoVisionRange = visionRange; 
		defactoEffectResistance = effectResistance;
		boostList = boostReset.clone();
		ArrayList<Integer> removeEffects = new ArrayList<Integer>();
		moved = false;
		attacked = false;
		for (i = 0; i < effectList.size(); i++)
		{
			effect = effectList.get(i);
			effectType = effectList.get(i).getEffectType();
			effectPower = effect.getEffectPower();
			effect.incrementTurn();
			if (!effect.getActive())
			{
				removeEffects.add(i);
			}
			else
			{
				switch (effectType)
				{
				case 0:
					defactoMoveRange += effectPower;
					break;
				case 1:
					defactoMoveRange += effectPower / 100 * moveRange;
					break;
				case 2:
					defactoDefence += effectPower;
					break;
				case 3:
					defactoDefence += effectPower / 100 * defence;
					break;
				case 4:
					boostList[0] += effectPower;
					break;
				case 5:
					boostList[1] += effectPower / 100;
					break;
				case 6:
					boostList[2] += effectPower;
					break;
				case 7:
					boostList[3] += effectPower / 100;
					break;
				case 8:
					defactoHealthPointRecover += effectPower;
					break;
				case 9:
					defactoHealthPointRecover += effectPower / 100 * healthPointRecover;
					break;
				case 10:
					defactoStaminaRecover += effectPower;
					break;
				case 11: 
					defactoStaminaRecover += effectPower / 100 * staminaRecover;
					break;
				case 12:
					boostList[4] += effectPower;
					break;
				case 13:
					boostList[5] += effectPower / 100;
					break;
				case 14:
					boostList[6] += effectPower;
					break;
				case 15:
					boostList[7] += effectPower / 100;
					break;
				case 16:
					stamina += effectPower;
					break;
				case 17:
					stamina += effectPower / 100 * preStamina;
					break;
				case 18:
					healthPoint += effectPower;
					break;
				case 19:
					healthPoint += effectPower / 100 * preHealth;
					break;
				case 22:
					defactoEffectResistance += effectPower;
					break;
				case 23: 
					defactoEffectResistance += effectPower / 100 * effectResistance;
					break;
				case 24:
					defactoVisionRange += effectPower;
					break;
				case 25:
					defactoVisionRange += effectPower / 100 * visionRange;
					break;
				case 100:
					moved = true;
					attacked = true;
					break;
				case 102:
					attacked = true;
					break;
				case 103:
					moved = true;
					break;
				}
			}
		}
		for (i = removeEffects.size() - 1; i >= 0; i--)
		{
			effectList.remove(i);
		}
		EffectData[] effectFieldEffectList;
		EffectField effectField;
		for (i = 0; i < effectFieldList.size(); i++)
		{
			effectField = effectFieldList.get(i);
			effectFieldEffectList = effectFieldList.get(i).getEffectList();
			for (j = 0; j < effectFieldEffectList.length; j++)
			{
				if (effectField.getPlayer() != player)
				{
					effect = effectFieldEffectList[j].makeResistedEffect(defactoEffectResistance, 0, effectField, true, effectField.getPlayer());
				}
				else
				{
					effect = effectFieldEffectList[j].makeEffect(0, effectField, true, effectField.getPlayer());
				}
				effectType = effect.getEffectType();
				effectPower = effect.getEffectPower();
				if(effect.getActive())
				{
					switch (effectType)
					{
					case 0:
						defactoMoveRange += effectPower;
						break;
					case 1:
						defactoMoveRange += effectPower / 100 * moveRange;
						break;
					case 2:
						defactoDefence += effectPower;
						break;
					case 3:
						defactoDefence += effectPower / 100 * defence;
						break;
					case 4:
						boostList[0] += effectPower;
						break;
					case 5:
						boostList[1] += effectPower / 100;
						break;
					case 6:
						boostList[2] += effectPower;
						break;
					case 7:
						boostList[3] += effectPower / 100;
						break;
					case 8:
						defactoHealthPointRecover += effectPower;
						break;
					case 9:
						defactoHealthPointRecover += effectPower / 100 * healthPointRecover;
						break;
					case 10:
						defactoStaminaRecover += effectPower;
						break;
					case 11: 
						defactoStaminaRecover += effectPower / 100 * staminaRecover;
						break;
					case 12:
						boostList[4] += effectPower;
						break;
					case 13:
						boostList[5] += effectPower / 100;
						break;
					case 14:
						boostList[6] += effectPower;
						break;
					case 15:
						boostList[7] += effectPower / 100;
						break;
					case 16:
						stamina += effectPower;
						break;
					case 17:
						stamina += effectPower / 100 * preStamina;
						break;
					case 18:
						healthPoint += effectPower;
						break;
					case 19:
						healthPoint += effectPower / 100 * preHealth;
						break;
					case 22:
						defactoEffectResistance += effectPower;
						break;
					case 23: 
						defactoEffectResistance += effectPower / 100 * effectResistance;
						break;
					case 24:
						defactoVisionRange += effectPower;
						break;
					case 25:
						defactoVisionRange += effectPower / 100 * visionRange;
						break;
					case 100:
						moved = true;
						attacked = true;
						break;
					case 102:
						attacked = true;
						break;
					case 103:
						moved = true;
						break;
					}
				}
			}
		}
		healthPoint += defactoHealthPointRecover;
		stamina += defactoStaminaRecover;
		if (healthPoint > maxHealthPoint)
		{
			healthPoint = maxHealthPoint;
		}
		if (stamina > maxStamina)
		{
			stamina = maxStamina;
		}
	}
}
