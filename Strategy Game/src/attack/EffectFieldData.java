package attack;

import unit.Unit;
import utilities.Location;

public class EffectFieldData {
	
	protected double range;
	protected EffectData[] effectList;
	protected boolean unitCentred;
	protected double duration;
	// 0 = self, 1 = enemy, 2 = either
	protected int target;
	
	protected EffectFieldData()
	{
		
	}
	
	public EffectFieldData (double range, EffectData[] effectList, boolean unitCentred, double duration, int target)
	{
		this.range = range;
		this.effectList = effectList;
		this.unitCentred = unitCentred;
		this.duration = duration;
		this.target = target;
	}
	
	public EffectFieldData (double range, EffectData[] effectList, long unitCentred, double duration, int target)
	{
		this.range = range;
		this.effectList = effectList;
		if (unitCentred == 0)
		{
			this.unitCentred = false;
		}
		else
		{
			this.unitCentred = true;
		}
		this.duration = duration;
		this.target = target;
	}
	
	public EffectFieldData clone()
	{
		return new EffectFieldData(range, effectList, unitCentred, duration, target);
	}
	
	public EffectFieldData (double range, EffectData[] effectList, double duration)
	{
		this.range = range;
		this.duration = duration;
		this.effectList = effectList;
		this.unitCentred = true;
	}
	
	public EffectFieldData (double range, int[] effectNumber, double[] effectPower, double duration)
	{
		this.range = range;
		this.duration = duration;
		this.unitCentred = false;
		this.effectList = EffectData.makeEffectList(effectNumber, effectPower);
	}
	
	public EffectFieldData (double range, long[] effectNumber, double[] effectPower, double duration)
	{
		this.range = range;
		this.duration = duration;
		this.unitCentred = false;
		this.effectList = EffectData.makeEffectList(effectNumber, effectPower);
	}
	
	public EffectFieldData (double range, long[] effectNumber, double[] effectPower, double duration, long unitCentred)
	{
		this.range = range;
		this.duration = duration;
		this.unitCentred = false;
		this.effectList = EffectData.makeEffectList(effectNumber, effectPower);
		if (unitCentred == 0)
		{
			this.unitCentred = false;
		}
		else
		{
			this.unitCentred = true;
		}
	}
	
	public EffectFieldData (double range, long[] effectNumber, double[] effectPower, double duration, long unitCentred, long target)
	{
		this.range = range;
		this.duration = duration;
		this.unitCentred = false;
		this.effectList = EffectData.makeEffectList(effectNumber, effectPower);
		if (unitCentred == 0)
		{
			this.unitCentred = false;
		}
		else
		{
			this.unitCentred = true;
		}
		this.target = ((Long) target).intValue();
	}
	
	public double getRange()
	{
		return range;
	}
	
	public EffectData[] getEffectList()
	{
		return effectList;
	}
	
	public int getTarget()
	{
		return target;
	}
	
	public boolean getUnitCentred()
	{
		return unitCentred;
	}
	
	public EffectField makeEffectField(Location centre, Unit unitCentre, boolean reducing, boolean active, int effectFieldID, int player)
	{
		return new EffectField(centre, range, effectList, unitCentre, unitCentred, duration, reducing, active, effectFieldID, target, player);
	}

	public EffectField makeEffectField(Unit unitCentre, boolean reducing, boolean active, int effectFieldID, int player)
	{
		Location centre = unitCentre.getLocation();
		return new EffectField(centre, range, effectList, unitCentre, unitCentred, duration, reducing, active, effectFieldID, target, player);
	}
	
	public EffectField makeEffectField(Location centre, Unit unitCentre, int effectFieldID, int player)
	{
		boolean reducing = true;
		if (duration < 0)
		{
			reducing = false;
		}
		return new EffectField(centre, range, effectList, unitCentre, unitCentred, duration, reducing, true, effectFieldID, target, player);
	}
	
	public EffectField makeEffectField(Unit unitCentre, int effectFieldID, int player)
	{
		Location centre = unitCentre.getLocation();
		boolean reducing = true;
		if (duration < 0)
		{
			reducing = false;
		}
		return new EffectField(centre, range, effectList, unitCentre, unitCentred, duration, reducing, true, effectFieldID, target, player);
	}
	
	public EffectField makeEffectField(Location centre, int effectFieldID, int player)
	{
		boolean reducing = true;
		if (duration < 0)
		{
			reducing = false;
		}
		return new EffectField(centre, range, effectList, null, unitCentred, duration, reducing, true, effectFieldID, target, player);
	}
}
