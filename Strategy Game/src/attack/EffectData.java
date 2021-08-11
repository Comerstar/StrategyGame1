package attack;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import utilities.JSONDecoder;

public class EffectData {

	private static JSONObject effectInfo;
	
	protected int effectType;
	protected double effectPower;
	protected double effectDuration;
	protected String name;
	protected int effectNumber;
	protected int sign;
	protected String effectNameMap;
	// 0 = self, 1 = enemy, 2 = either
	protected int target;
	
	protected EffectData()
	{
		
	}
	
	public EffectData (int effectType, double effectPower, double effectDuration, String name, int effectNumber, int sign, String effectNameMap, int target)
	{
		this.effectType = effectType;
		this.effectPower = effectPower;
		this.effectDuration = effectDuration;
		this.name = name;
		this.effectNumber = effectNumber;
		this.sign = sign;
		this.effectNameMap = effectNameMap;
		this.target = target;
	}
	
	public EffectData clone()
	{
		return new EffectData(effectType, effectPower, effectDuration, name, effectNumber, sign, effectNameMap, target);
	}
	
	public EffectData (int effectNumber, double effectDuration, double effectPower)
	{
		if (effectInfo == null)
		{
			JSONDecoder.setupEffectInfo();
			effectInfo = JSONDecoder.getEffectInfo();
		}
		JSONArray effectValues = (JSONArray) effectInfo.get(((Integer) effectNumber).toString());
		
		effectType = ((Long) effectValues.get(0)).intValue();
		name = (String) effectValues.get(1);
		sign = ((Long) effectValues.get(2)).intValue();
		
		this.effectNumber = effectNumber;
		this.effectPower = effectPower;
		this.effectDuration = effectDuration;
		if(sign == 1)
		{
			effectNameMap = "+" + ((Integer) effectType).toString();
		}
		else if(sign == -1)
		{
			effectNameMap = "-" + ((Integer) effectType).toString();
			this.effectPower = effectPower * sign;
		}
		else if(sign != 0)
		{
			effectNameMap = ((Integer) effectType).toString();
		}
	}
	
	public EffectData (int effectNumber, double effectDuration, double effectPower, int target)
	{
		if (effectInfo == null)
		{
			JSONDecoder.setupEffectInfo();
			effectInfo = JSONDecoder.getEffectInfo();
		}
		JSONArray effectValues = (JSONArray) effectInfo.get(((Integer) effectNumber).toString());
		
		effectType = ((Long) effectValues.get(0)).intValue();
		name = (String) effectValues.get(1);
		sign = ((Long) effectValues.get(2)).intValue();
		
		this.effectNumber = effectNumber;
		this.effectPower = effectPower;
		this.effectDuration = effectDuration;
		if(sign == 1)
		{
			effectNameMap = "+" + ((Integer) effectType).toString();
		}
		else if(sign == -1)
		{
			effectNameMap = "-" + ((Integer) effectType).toString();
			this.effectPower = effectPower * sign;
		}
		else if(sign != 0)
		{
			effectNameMap = ((Integer) effectType).toString();
		}
		this.target = target;
	}
	
	public EffectData (int effectNumber, double effectPower)
	{
		if (effectInfo == null)
		{
			JSONDecoder.setupEffectInfo();
			effectInfo = JSONDecoder.getEffectInfo();
		}
		JSONArray effectValues = (JSONArray) effectInfo.get(((Integer) effectNumber).toString());
		
		effectType = ((Long) effectValues.get(0)).intValue();
		name = (String) effectValues.get(1);
		sign = ((Long) effectValues.get(2)).intValue();
		
		this.effectNumber = effectNumber;
		effectDuration = -1;
		this.effectPower = effectPower;
		if(sign == 1)
		{
			effectNameMap = "+" + ((Integer) effectType).toString();
		}
		else if(sign == -1)
		{
			effectNameMap = "-" + ((Integer) effectType).toString();
			this.effectPower = effectPower * sign;
		}
		else if(sign != 0)
		{
			effectNameMap = ((Integer) effectType).toString();
		}
	}
	
	public int getEffectType()
	{
		return effectType;
	}
	
	public double getEffectPower()
	{
		return effectPower;
	}
	
	public double getResistedEffectPower(double resistance)
	{
		return effectPower * (100.0 - resistance) / 100.0;
	}
	
	public double getEffectDuration()
	{
		return effectDuration;
	}
	
	public double getResistedEffectDuration(double resistance)
	{
		return effectDuration * (100.0 - resistance) / 100.0;
	}
	
	public String getName()
	{
		return name;
	}
	
	public String getEffectNameMap()
	{
		return effectNameMap;
	}
	
	public int getEffectNumber()
	{
		return effectNumber;
	}
	
	public Effect makeEffect(int effectID, boolean reducing, EffectField sourceField, boolean fromField, boolean active, int player)
	{
		return new Effect(effectType, effectPower, effectDuration, name, effectNumber, sign, effectNameMap, effectID, reducing, sourceField, fromField, active, target, player);
	}
	
	public Effect makeEffect(int effectID, EffectField sourceField, boolean fromField, int player)
	{
		boolean reducing = true;
		if (effectDuration < 0)
		{
			reducing = false;
		}
		return new Effect(effectType, effectPower, effectDuration, name, effectNumber, sign, effectNameMap, effectID, reducing, sourceField, fromField, true, target, player);
	}
	
	public Effect makeEffect(int effectID, EffectField sourceField, int player)
	{
		boolean reducing = true;
		if (effectDuration < 0)
		{
			reducing = false;
		}
		return new Effect(effectType, effectPower, effectDuration, name, effectNumber, sign, effectNameMap, effectID, reducing, sourceField, true, true, target, player);
	}
	
	public Effect makeEffect(int effectID, int player)
	{
		boolean reducing = true;
		if (effectDuration < 0)
		{
			reducing = false;
		}
		return new Effect(effectType, effectPower, effectDuration, name, effectNumber, sign, effectNameMap, effectID, reducing, null, false, true, target, player);
	}
	
	public Effect makeResistedEffect(double resistance, int effectID, boolean reducing, EffectField sourceField, boolean fromField, boolean active, int player)
	{
		return new Effect(effectType, effectPower * (100.0 - resistance) / 100.0, effectDuration * (100.0 - resistance) / 100.0, name, effectNumber, sign, effectNameMap, effectID, reducing, sourceField, fromField, active, target, player);
	}
	
	public Effect makeResistedEffect(double resistance, int effectID, EffectField sourceField, boolean fromField, int player)
	{
		boolean reducing = true;
		if (effectDuration < 0)
		{
			reducing = false;
		}
		return new Effect(effectType, effectPower * (100.0 - resistance) / 100.0, effectDuration * (100.0 - resistance) / 100.0, name, effectNumber, sign, effectNameMap, effectID, reducing, sourceField, fromField, true, target, player);
	}
	

	public Effect makeResistedEffect(double resistance, int effectID, EffectField sourceField, int player)
	{
		boolean reducing = true;
		if (effectDuration < 0)
		{
			reducing = false;
		}
		return new Effect(effectType, effectPower * (100.0 - resistance) / 100.0, effectDuration * (100.0 - resistance) / 100.0, name, effectNumber, sign, effectNameMap, effectID, reducing, sourceField, true, true, target, player);
	}
	
	public Effect makeResistedEffect(double resistance, int effectID, int player)
	{
		boolean reducing = true;
		if (effectDuration < 0)
		{
			reducing = false;
		}
		return new Effect(effectType, effectPower * (100.0 - resistance) / 100.0, effectDuration * (100.0 - resistance) / 100.0, name, effectNumber, sign, effectNameMap, effectID, reducing, null, false, true, target, player);
	}
	
	public Effect makeResistedPowerEffect(double resistance, int effectID, int player)
	{
		boolean reducing = true;
		if (effectDuration < 0)
		{
			reducing = false;
		}
		return new Effect(effectType, effectPower * (100.0 - resistance) / 100.0, effectDuration, name, effectNumber, sign, effectNameMap, effectID, reducing, null, false, true, target, player);
	}
	
	public static EffectData[] makeEffectList(int[] effectNumberList, double[] effectDurationList, double[] effectPowerList)
	{
		int i = 0;
		EffectData[] effectList = new EffectData[effectNumberList.length];
		for (i = 0; i < effectNumberList.length; i++)
		{
			effectList[i] = new EffectData(effectNumberList[i], effectDurationList[i], effectPowerList[i]);
		}
		return effectList;
	}
	
	public static EffectData[] makeEffectList(int[] effectNumberList, double[] effectPowerList)
	{
		int i = 0;
		EffectData[] effectList = new EffectData[effectNumberList.length];
		for (i = 0; i < effectNumberList.length; i++)
		{
			effectList[i] = new EffectData(effectNumberList[i], effectPowerList[i]);
		}
		return effectList;
	}
	
	public static EffectData[] makeEffectList(long[] effectNumberList, double[] effectDurationList, double[] effectPowerList)
	{
		int i = 0;
		EffectData[] effectList = new EffectData[effectNumberList.length];
		for (i = 0; i < effectNumberList.length; i++)
		{
			effectList[i] = new EffectData(((Long) effectNumberList[i]).intValue(), effectDurationList[i], effectPowerList[i]);
		}
		return effectList;
	}
	
	public static EffectData[] makeEffectList(long[] effectNumberList, double[] effectPowerList)
	{
		int i = 0;
		EffectData[] effectList = new EffectData[effectNumberList.length];
		for (i = 0; i < effectNumberList.length; i++)
		{
			effectList[i] = new EffectData(((Long) effectNumberList[i]).intValue(), effectPowerList[i]);
		}
		return effectList;
	}
	
	public boolean effectEnemy()
	{
		return target == 1 || target == 2;
	}
	
	public boolean effectSelf()
	{
		return target == 0 || target == 2;
	}
	
	public boolean sameEffect(EffectData effect)
	{
		if(effect.getEffectNumber() == this.effectNumber)
		{
			if(effect.getEffectPower() == this.effectPower)
			{
				if(effect.getEffectDuration() == this.effectDuration)
				{
					return true;
				}
			}
		}
		return false;
	}
}
