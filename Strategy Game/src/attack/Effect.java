package attack;

public class Effect extends EffectData{

	private int effectID;
	private boolean reducing;
	private EffectField sourceField;
	private boolean fromField;
	private boolean active;
	private int player;
	
	protected Effect (int effectType, double effectPower, double effectDuration, String name, int effectNumber, int sign, String effectNameMap, int effectID, 
			boolean reducing, EffectField sourceField, boolean fromField, boolean active, int target, int side)
	{
		this.effectType = effectType;
		this.effectPower = effectPower;
		this.effectDuration = effectDuration;
		this.name = name;
		this.effectNumber = effectNumber;
		this.sign = sign;
		this.effectNameMap = effectNameMap;
		this.effectID = effectID;
		this.reducing = reducing;
		this.sourceField = sourceField;
		this.fromField = fromField;
		this.active = active;
		this.target = target;
		this.player = side;
	}
	
	public Effect clone()
	{
		return new Effect(effectType, effectPower, effectDuration, name, effectNumber, sign, effectNameMap, effectID, reducing, sourceField, fromField, active, target, player);
	}
	
	public int getEffectType()
	{
		return effectType;
	}
	
	public double getEffectPower()
	{
		return effectPower;
	}
	
	public double getEffectDuration()
	{
		return effectDuration;
	}
	
	public int getEffectNumber()
	{
		return effectNumber;
	}
	
	public String getName()
	{
		return name;
	}
	
	public String getEffectNameMap()
	{
		return effectNameMap;
	}
	
	public EffectField getSourceField()
	{
		return sourceField;
	}
	
	public boolean getFromField()
	{
		return fromField;
	}
	
	public boolean getActive()
	{
		return active;
	}
	
	public int getEffectID()
	{
		return effectID;
	}
	
	public void incrementTurn()
	{
		if (reducing)
		{
			effectDuration --; 
			if (effectDuration < 0)
			{
				effectDuration = -1;
				active = false;
			}
		}
	}
	
	public void resistEffect(double resistance)
	{
		effectPower *= (100 - resistance) / 100;
		effectDuration *= (100 - resistance) / 100;
	}
	
	public void resistEffectPower(double resistance)
	{
		effectPower *= (100 - resistance) / 100;
	}
	
	public void resistEffectDuration(double resistance)
	{
		effectDuration *= (100 - resistance) / 100;
	}
	
	public void setEffectID(int effectID)
	{
		this.effectID = effectID;
	}
}
