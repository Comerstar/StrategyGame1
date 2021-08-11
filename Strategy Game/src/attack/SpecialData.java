package attack;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import unit.*;
import utilities.JSONDecoder;

public class SpecialData {
	
	protected static JSONObject specialInfo;
	protected double specialPointCost;
	protected double staminaCost;
	protected EffectData[] effectList;
	protected AttackData[] attackList;
	protected UnitData[] unitList;
	protected int specialNumber;
	protected int specialType;
	// 0: []
	// 1: [0: Type: 0 = discrete; 1 = range, 1> values for form changes (just 1,2 for discrete)]
	// 2: []
	// 3: []
	// 4: [0: Pay?, 1: Range] unitList - AllowedUnitList
	//
	protected double[] specialData;
	protected String name;
	
	protected SpecialData()
	{
		if (specialInfo == null)
		{
			JSONDecoder.setupSpecialInfo();
			specialInfo = JSONDecoder.getSpecialInfo();
		}
	}
	
	public SpecialData(int specialNumber)
	{
		if (specialInfo == null)
		{
			JSONDecoder.setupSpecialInfo();
			specialInfo = JSONDecoder.getSpecialInfo();
		}
		this.specialNumber = specialNumber;
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
			attackList[i] = new AttackData(((Long) listData.get(i)).intValue());
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
	
	public Special makeSpecial(SpecialUnit unit)
	{
		return new Special(specialPointCost, staminaCost, effectList, attackList, unitList, specialNumber, unit, specialType);
	}
	
}
