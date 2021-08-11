package attack;

import org.json.simple.JSONArray;

import unit.*;
import utilities.ArrayHandler;

public class Special extends SpecialData{
	
	private double specialPointCost;
	private double staminaCost;
	private EffectData[] effectList;
	private Attack[] attackList;
	private UnitData[] unitList;
	private int specialNumber;
	private SpecialUnit unit;
	private int specialType;
	// 0: []
	// 1: [0: Type: 0 = discrete; 1 = range, 1> values for form changes (just 1,2 for discrete)]
	// 2: []
	// 3: []
	// 4: [0: Pay?, 1: Range] unitList - AllowedUnitList
	//
	private double[] specialData;
	private String name;
	
	public Special (double specialPointCost, double staminaCost, EffectData[] effectList, Attack[] attackList, UnitData[] unitList, int specialNumber, SpecialUnit unit, int specialType)
	{
		this.specialPointCost = specialPointCost;
		this.staminaCost = staminaCost;
		this.effectList = effectList;
		this.attackList = attackList;
		this.unitList = unitList;
		this.specialNumber = specialNumber;
		this.unit = unit;
		this.specialType = specialType;
	}
	
	public Special (double specialPointCost, double staminaCost, EffectData[] effectList, AttackData[] attackList, UnitData[] unitList, int specialNumber, SpecialUnit unit, int specialType)
	{
		this.specialPointCost = specialPointCost;
		this.staminaCost = staminaCost;
		this.effectList = effectList;
		this.attackList = ArrayHandler.initialiseAttackList(attackList, unit);
		this.unitList = unitList;
		this.specialNumber = specialNumber;
		this.unit = unit;
		this.specialType = specialType;
	}
	
	public Special (int specialNumber, SpecialUnit unit)
	{
		super();
		this.specialNumber = specialNumber;
		this.unit = unit;
		JSONArray specialData = (JSONArray) specialInfo.get(((Integer) specialNumber).toString());
		specialType = ((Long) specialData.get(0)).intValue();
		staminaCost = (double) specialData.get(1);
		specialPointCost = (double) specialData.get(2);
		int i = 0;
		
		JSONArray listData = (JSONArray) specialData.get(3);
		JSONArray listInfo;
		effectList = new EffectData[listData.size()];
		for(i = 0; i < listData.size(); i++)
		{
			listInfo = (JSONArray) listData.get(i);
			effectList[i] = new EffectData(((Long) listInfo.get(0)).intValue(), (double) listInfo.get(1), (double) listInfo.get(2));
		}
		
		listData = (JSONArray) specialData.get(4);
		attackList = new Attack[listData.size()];
		for(i = 0; i < listData.size(); i++)
		{
			attackList[i] = new Attack(((Long) listData.get(i)).intValue(), unit);
		}
		
		listData = (JSONArray) specialData.get(5);
		unitList = new UnitData[listData.size()];
		for(i = 0; i < listData.size(); i++)
		{
			unitList[i] = new UnitData(((Long) listData.get(i)).intValue());
		}
		
		listData = (JSONArray) specialData.get(6);
		this.specialData = new double[listData.size()];
		for(i = 0; i < listData.size(); i++)
		{
			this.specialData[i] = (double) listData.get(i);
		}
		
		name = (String) specialData.get(7);
	}
	
	public double[] getSpecialData()
	{
		return specialData;
	}
	
	public double getSpecialPointCost()
	{
		return specialPointCost;
	}
	
	public double getStaminaCost()
	{
		return staminaCost;
	}
	
	public EffectData[] getEffectList()
	{
		return effectList;
	}
	
	public UnitData[] getUnitList()
	{
		return unitList;
	}
	
	public double getSpecialNumber()
	{
		return specialNumber;
	}
	
	public SpecialUnit getUnit()
	{
		return unit;
	}
	
	public Attack[] getAttackList()
	{
		return attackList;
	}
	
	public int getSpecialType()
	{
		return specialType;
	}
	
	public String getName()
	{
		return name;
	}
}
