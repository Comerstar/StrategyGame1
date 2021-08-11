package attack;

import java.util.ArrayList;

import map.BuildingMap;
import map.UnitMap;
import unit.Unit;
import utilities.Location;

public class EffectField extends EffectFieldData{
	
	private Location centre;
	private Unit unitCentre;
	private boolean reducing;
	private boolean active;
	private int effectFieldID;
	private int player;
	
	protected EffectField (Location centre, double range, EffectData[] effectList, Unit unitCentre, boolean unitCentred, double duration, boolean reducing, boolean active,
			int effectFieldID, int target, int player)
	{
		this.centre = centre;
		this.range = range;
		this.effectList = effectList;
		this.unitCentre = unitCentre;
		this.unitCentred = unitCentred;
		this.duration = duration;
		this.reducing = reducing;
		this.active = active;
		this.effectFieldID = effectFieldID;
		this.target = target;
		this.player = player;
	}
	
	public EffectField clone()
	{
		return new EffectField(centre, range, effectList, unitCentre, unitCentred, duration, reducing, active, effectFieldID, target, player);
	}
	
	public int getEffectFieldID()
	{
		return effectFieldID;
	}
	
	public int getPlayer()
	{
		return player;
	}
	
	public double getRange()
	{
		return range;
	}
	
	public EffectData[] getEffectList()
	{
		return effectList;
	}
	
	public EffectData getEffectList(int i)
	{
		return effectList[i];
	}
	
	public boolean getActive()
	{
		return active;
	}
	
	public Location getCentre()
	{
		return centre;
	}
	
	public int getCentre(int i)
	{
		return centre.getLocation()[i];
	}
	
	public int getY()
	{
		return centre.getY();
	}
	
	public int getX()
	{
		return centre.getX();
	}
	
	public void setUnitCentre (Unit unit)
	{
		unitCentre = unit;
		unitCentred = true;
	}
	
	public boolean checkIfInRange(Unit unit)
	{
		double distance = this.centre.calculateDistance(unit.getLocation());
		if (distance <= range)
		{
			return true;
		}
		else
		{
			return false;
		}
	}
	
	public void incrementTurn()
	{
		if(reducing)
		{
			duration--;
			if (duration < 0)
			{
				active = false;
			}
		}
		else if(unitCentred)
		{
			if (!unitCentre.getAlive())
			{
				active = false;
			}
		}
	}
	
	public void fullUpdate(UnitMap unitMap)
	{
		if(reducing)
		{
			duration--;
			if (duration < 0)
			{
				active = false;
			}
		}
		else if(unitCentred)
		{
			if (unitCentre.getAlive())
			{
				active = false;
			}
		}
		applyEffects(unitMap);
	}
	
	public void fullUpdate(UnitMap unitMap, BuildingMap buildingMap)
	{
		if(reducing)
		{
			duration--;
			if (duration < 0)
			{
				active = false;
			}
		}
		else if(unitCentred)
		{
			if (unitCentre.getAlive())
			{
				active = false;
			}
		}
		applyEffects(unitMap, buildingMap);
	}
	
	public void setEffectFieldID(int effectFieldID)
	{
		this.effectFieldID = effectFieldID;
	}
	
	public void setInactive()
	{
		active = false;
	}
	
	public void applyEffects(UnitMap unitMap)
	{
		if (active)
		{
			int i = 0;
			int j = 0;
			int intRange = ((Double) range).intValue();
			Unit target;
			Location targetting = centre.clone();
			for (i = 0; i < intRange + 1; i++)
			{
				for (j = 0; j < intRange + 1; j++)
				{
					targetting.translate(i - intRange + j, i - j);
					if (unitMap.inMap(targetting))
					{
						
						target = unitMap.getMap(targetting.getY(), targetting.getX());
						if (target != null)
						{
							if (target.getPlayer() == player && target.getPlayer() != -1)
							{
								if(this.target == 0 || this.target == 2)
								{
									target.addEffectField(this);
								}
							}
							else if(target.getPlayer() != -1)
							{
								if(this.target == 1 || this.target == 2)
								{
									target.addEffectField(this);
								}
							}
						}
					}
					targetting.translate(-i + intRange - j, -i + j);
				}
			}
			for (i = 0; i < intRange; i++)
			{
				for (j = 0; j < intRange; j++)
				{
					targetting.translate(i - intRange + j + 1, i - j);
					if (unitMap.inMap(targetting))
					{
						
						target = unitMap.getMap(targetting.getY(), targetting.getX());
						if (target != null)
						{
							if (target.getPlayer() == player && target.getPlayer() != -1)
							{
								if(this.target == 0 || this.target == 2)
								{
									target.addEffectField(this);
								}
							}
							else if(target.getPlayer() != -1)
							{
								if(this.target == 1 || this.target == 2)
								{
									target.addEffectField(this);
								}
							}
						}
					}
					targetting.translate(-i + intRange - j - 1, -i + j);
				}
			}
		}
	}
	public void applyEffects(UnitMap unitMap, BuildingMap buildingMap)
	{
		if (active)
		{
			int i = 0;
			int j = 0;
			int intRange = ((Double) range).intValue();
			Unit target;
			Location targetting = centre.clone();
			for (i = 0; i < intRange + 1; i++)
			{
				for (j = 0; j < intRange + 1; j++)
				{
					targetting.translate(i - intRange + j, i - j);
					if (unitMap.inMap(targetting))
					{
						target = unitMap.getMap(targetting.getY(), targetting.getX());
						if (target != null)
						{
							if (target.getPlayer() == player && target.getPlayer() != -1)
							{
								if(this.target == 0 || this.target == 2)
								{
									target.addEffectField(this);
								}
							}
							else if(target.getPlayer() != -1)
							{
								if(this.target == 1 || this.target == 2)
								{
									target.addEffectField(this);
								}
							}
						}
						target = buildingMap.getMap(targetting.getY(), targetting.getX());
						if (target != null)
						{
							if (target.getPlayer() == player && target.getPlayer() != -1)
							{
								if(this.target == 0 || this.target == 2)
								{
									target.addEffectField(this);
								}
							}
							else if(target.getPlayer() != -1)
							{
								if(this.target == 1 || this.target == 2)
								{
									target.addEffectField(this);
								}
							}
						}
					}
					targetting.translate(-i + intRange - j, -i + j);
				}
			}
			for (i = 0; i < intRange; i++)
			{
				for (j = 0; j < intRange; j++)
				{
					targetting.translate(i - intRange + j + 1, i - j);
					if (unitMap.inMap(targetting))
					{
						
						target = unitMap.getMap(targetting.getY(), targetting.getX());
						if (target != null)
						{
							if (target.getPlayer() == player && target.getPlayer() != -1)
							{
								if(this.target == 0 || this.target == 2)
								{
									target.addEffectField(this);
								}
							}
							else if(target.getPlayer() != -1)
							{
								if(this.target == 1 || this.target == 2)
								{
									target.addEffectField(this);
								}
							}
						}
						target = buildingMap.getMap(targetting.getY(), targetting.getX());
						if (target != null)
						{
							if (target.getPlayer() == player && target.getPlayer() != -1)
							{
								if(this.target == 0 || this.target == 2)
								{
									target.addEffectField(this);
								}
							}
							else if(target.getPlayer() != -1)
							{
								if(this.target == 1 || this.target == 2)
								{
									target.addEffectField(this);
								}
							}
						}
					}
					targetting.translate(-i + intRange - j - 1, -i + j);
				}
			}
		}
	}
	
	public void removeEffects(UnitMap unitMap)
	{
		if (active)
		{
			int i = 0;
			int j = 0;
			int intRange = ((Double) range).intValue();
			Unit target;
			Location targetting = centre.clone();
			for (i = 0; i < intRange + 1; i++)
			{
				for (j = 0; j < intRange + 1; j++)
				{
					targetting.translate(i - intRange + j, i - j);
					if (unitMap.inMap(targetting))
					{
						
						target = unitMap.getMap(targetting.getY(), targetting.getX());
						if (target != null)
						{
							if (target.getPlayer() == player && target.getPlayer() != -1)
							{
								if(this.target == 0 || this.target == 2)
								{
									target.removeEffectField(this);
								}
							}
							else if(target.getPlayer() != -1)
							{
								if(this.target == 1 || this.target == 2)
								{
									target.removeEffectField(this);
								}
							}
						}
					}
					targetting.translate(-i + intRange - j, -i + j);
				}
			}
			for (i = 0; i < intRange; i++)
			{
				for (j = 0; j < intRange; j++)
				{
					targetting.translate(i - intRange + j + 1, i - j);
					if (unitMap.inMap(targetting))
					{
						
						target = unitMap.getMap(targetting.getY(), targetting.getX());
						if (target != null)
						{
							if (target.getPlayer() == player && target.getPlayer() != -1)
							{
								if(this.target == 0 || this.target == 2)
								{
									target.removeEffectField(this);
								}
							}
							else if(target.getPlayer() != -1)
							{
								if(this.target == 1 || this.target == 2)
								{
									target.removeEffectField(this);
								}
							}
						}
					}
					targetting.translate(-i + intRange - j - 1, -i + j);
				}
			}
		}
	}
	
	public void removeEffects(UnitMap unitMap, BuildingMap buildingMap)
	{
		if (active)
		{
			int i = 0;
			int j = 0;
			int intRange = ((Double) range).intValue();
			Unit target;
			Location targetting = centre.clone();
			for (i = 0; i < intRange + 1; i++)
			{
				for (j = 0; j < intRange + 1; j++)
				{
					targetting.translate(i - intRange + j, i - j);
					if (unitMap.inMap(targetting))
					{
						target = unitMap.getMap(targetting.getY(), targetting.getX());
						if (target != null)
						{
							if (target.getPlayer() == player && target.getPlayer() != -1)
							{
								if(this.target == 0 || this.target == 2)
								{
									target.removeEffectField(this);
								}
							}
							else if(target.getPlayer() != -1)
							{
								if(this.target == 1 || this.target == 2)
								{
									target.removeEffectField(this);
								}
							}
						}
						target = buildingMap.getMap(targetting.getY(), targetting.getX());
						if (target != null)
						{
							if (target.getPlayer() == player && target.getPlayer() != -1)
							{
								if(this.target == 0 || this.target == 2)
								{
									target.removeEffectField(this);
								}
							}
							else if(target.getPlayer() != -1)
							{
								if(this.target == 1 || this.target == 2)
								{
									target.removeEffectField(this);
								}
							}
						}
					}
					targetting.translate(-i + intRange - j, -i + j);
				}
			}
			for (i = 0; i < intRange; i++)
			{
				for (j = 0; j < intRange; j++)
				{
					targetting.translate(i - intRange + j + 1, i - j);
					if (unitMap.inMap(targetting))
					{
						target = unitMap.getMap(targetting.getY(), targetting.getX());
						if (target != null)
						{
							if (target.getPlayer() == player && target.getPlayer() != -1)
							{
								if(this.target == 0 || this.target == 2)
								{
									target.removeEffectField(this);
								}
							}
							else if(target.getPlayer() != -1)
							{
								if(this.target == 1 || this.target == 2)
								{
									target.removeEffectField(this);
								}
							}
						}
						target = buildingMap.getMap(targetting.getY(), targetting.getX());
						if (target != null)
						{
							if (target.getPlayer() == player && target.getPlayer() != -1)
							{
								if(this.target == 0 || this.target == 2)
								{
									target.removeEffectField(this);
								}
							}
							else if(target.getPlayer() != -1)
							{
								if(this.target == 1 || this.target == 2)
								{
									target.removeEffectField(this);
								}
							}
						}
					}
					targetting.translate(-i + intRange - j - 1, -i + j);
				}
			}
		}
	}
	
	public ArrayList<Unit> getUnitsInRange(UnitMap unitMap)
	{
		ArrayList<Unit> unitList = new ArrayList<Unit>();
		if (active)
		{
			int i = 0;
			int j = 0;
			int k = 0;
			int intRange = ((Double) range).intValue();
			Unit target;
			Location targetting = centre.clone();
			for (i = 0; i < intRange + 1; i++)
			{
				for (j = 0; j < intRange + 1; j++)
				{
					targetting.translate(i - intRange + j, i - j);
					if (unitMap.inMap(targetting))
					{
						
						target = unitMap.getMap(targetting.getY(), targetting.getX());
						if (target != null)
						{
							for (k = 0; k < effectList.length; k++)
							{
								if (target.getPlayer() == player && target.getPlayer() != -1)
								{
									if(this.target == 0 || this.target == 2)
									{
										unitList.add(target);
									}
								}
								else if(target.getPlayer() != -1)
								{
									if(this.target == 1 || this.target == 2)
									{
										unitList.add(target);
									}
								}
							}
						}
					}
					targetting.translate(-i + intRange - j, -i + j);
				}
			}
			for (i = 0; i < intRange; i++)
			{
				for (j = 0; j < intRange; j++)
				{
					targetting.translate(i - intRange + j + 1, i - j);
					if (unitMap.inMap(targetting))
					{
						
						target = unitMap.getMap(targetting.getY(), targetting.getX());
						if (target != null)
						{
							for (i = 0; i < effectList.length; i++)
							{
								if (target.getPlayer() == player && target.getPlayer() != -1)
								{
									if(this.target == 0 || this.target == 2)
									{
										unitList.add(target);
									}
								}
								else if(target.getPlayer() != -1)
								{
									if(this.target == 1 || this.target == 2)
									{
										unitList.add(target);
									}
								}
							}
						}
					}
					targetting.translate(-i + intRange - j - 1, -i + j);
				}
			}
		}
		return unitList;
	}
	
	public ArrayList<Unit> getUnitsInRange(UnitMap unitMap, BuildingMap buildingMap)
	{
		ArrayList<Unit> unitList = new ArrayList<Unit>();
		if (active)
		{
			int i = 0;
			int j = 0;
			int k = 0;
			int intRange = ((Double) range).intValue();
			Unit target;
			Location targetting = centre.clone();
			for (i = 0; i < intRange + 1; i++)
			{
				for (j = 0; j < intRange + 1; j++)
				{
					targetting.translate(i - intRange + j, i - j);
					if (unitMap.inMap(targetting))
					{
						
						target = unitMap.getMap(targetting.getY(), targetting.getX());
						if (target != null)
						{
							for (k = 0; k < effectList.length; k++)
							{
								if (target.getPlayer() == player && target.getPlayer() != -1)
								{
									if(this.target == 0 || this.target == 2)
									{
										unitList.add(target);
									}
								}
								else if(target.getPlayer() != -1)
								{
									if(this.target == 1 || this.target == 2)
									{
										unitList.add(target);
									}
								}
							}
						}
						target = buildingMap.getMap(targetting.getY(), targetting.getX());
						if (target != null)
						{
							for (k = 0; k < effectList.length; k++)
							{
								if (target.getPlayer() == player && target.getPlayer() != -1)
								{
									if(this.target == 0 || this.target == 2)
									{
										unitList.add(target);
									}
								}
								else if(target.getPlayer() != -1)
								{
									if(this.target == 1 || this.target == 2)
									{
										unitList.add(target);
									}
								}
							}
						}
					}
					targetting.translate(-i + intRange - j, -i + j);
				}
			}
			for (i = 0; i < intRange; i++)
			{
				for (j = 0; j < intRange; j++)
				{
					targetting.translate(i - intRange + j + 1, i - j);
					if (unitMap.inMap(targetting))
					{
						
						target = unitMap.getMap(targetting.getY(), targetting.getX());
						if (target != null)
						{
							for (i = 0; i < effectList.length; i++)
							{
								if (target.getPlayer() == player && target.getPlayer() != -1)
								{
									if(this.target == 0 || this.target == 2)
									{
										unitList.add(target);
									}
								}
								else if(target.getPlayer() != -1)
								{
									if(this.target == 1 || this.target == 2)
									{
										unitList.add(target);
									}
								}
							}
						}
						target = buildingMap.getMap(targetting.getY(), targetting.getX());
						if (target != null)
						{
							for (k = 0; k < effectList.length; k++)
							{
								if (target.getPlayer() == player && target.getPlayer() != -1)
								{
									if(this.target == 0 || this.target == 2)
									{
										unitList.add(target);
									}
								}
								else if(target.getPlayer() != -1)
								{
									if(this.target == 1 || this.target == 2)
									{
										unitList.add(target);
									}
								}
							}
						}
					}
					targetting.translate(-i + intRange - j - 1, -i + j);
				}
			}
		}
		return unitList;
	}
}
