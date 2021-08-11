package utilities;

import java.util.ArrayList;

import attack.*;
import unit.*;

public class ArrayHandler {
	
	public static Object[] cloneArray(Object[] array)
	{
		Object[] newArray = new Object[array.length];
		for (int i = 0; i < array.length; i++)
		{
			newArray[i] = array[i];
		}
		return newArray;
	}
	
	public static Object[][] cloneMatrix(Object[][] array)
	{
		Object[][] newArray = new Object[array.length][array[0].length];
		int j = 0;
		for (int i = 0; i < array.length; i++)
		{
			for (j = 0; j < array[0].length; j++)
			{
				newArray[i][j] = array[i][j];
			}
		}
		return newArray;
	}
	
	public static double[][] cloneMatrix(double[][] array)
	{
		double[][] newArray = new double[array.length][array[0].length];
		int j = 0;
		for (int i = 0; i < array.length; i++)
		{
			for (j = 0; j < array[0].length; j++)
			{
				newArray[i][j] = array[i][j];
			}
		}
		return newArray;
	}
	
	public static ArrayList<int[]> cloneMatrix(ArrayList<int[]> array)
	{
		ArrayList<int[]> newArray = new ArrayList<int[]>();
		for (int i = 0; i < array.size(); i++)
		{
			newArray.add(array.get(i).clone());
		}
		return newArray;
	}
	
	public static ArrayList<Integer> cloneIntegerArray(ArrayList<Integer> array)
	{
		ArrayList<Integer> newArray = new ArrayList<Integer>();
		for (int i = 0; i < array.size(); i++)
		{
			newArray.add(array.get(i));
		}
		return newArray;
	}
	
	public static ArrayList<Unit> cloneShallowUnitArray(ArrayList<Unit> array)
	{
		ArrayList<Unit> newArray = new ArrayList<Unit>();
		for (int i = 0; i < array.size(); i++)
		{
			newArray.add(array.get(i));
		}
		return newArray;
	}
	
	public static ArrayList<Unit> cloneDeepUnitArray(ArrayList<Unit> array)
	{
		ArrayList<Unit> newArray = new ArrayList<Unit>();
		for (int i = 0; i < array.size(); i++)
		{
			newArray.add(array.get(i).clone());
		}
		return newArray;
	}
	
	public static ArrayList<Effect> cloneShallowEffectArray(ArrayList<Effect> array)
	{
		ArrayList<Effect> newArray = new ArrayList<Effect>();
		for (int i = 0; i < array.size(); i++)
		{
			newArray.add(array.get(i));
		}
		return newArray;
	}
	
	public static ArrayList<Effect> cloneDeepEffectArray(ArrayList<Effect> array)
	{
		ArrayList<Effect> newArray = new ArrayList<Effect>();
		for (int i = 0; i < array.size(); i++)
		{
			newArray.add(array.get(i).clone());
		}
		return newArray;
	}
	
	public static ArrayList<EffectData> cloneShallowEffectDataArray(ArrayList<EffectData> array)
	{
		ArrayList<EffectData> newArray = new ArrayList<EffectData>();
		for (int i = 0; i < array.size(); i++)
		{
			newArray.add(array.get(i));
		}
		return newArray;
	}
	
	public static ArrayList<EffectData> cloneDeepEffectDataArray(ArrayList<EffectData> array)
	{
		ArrayList<EffectData> newArray = new ArrayList<EffectData>();
		for (int i = 0; i < array.size(); i++)
		{
			newArray.add(array.get(i).clone());
		}
		return newArray;
	}
	
	public static ArrayList<EffectFieldData> cloneShallowEffectFieldDataArray(ArrayList<EffectFieldData> array)
	{
		ArrayList<EffectFieldData> newArray = new ArrayList<EffectFieldData>();
		for (int i = 0; i < array.size(); i++)
		{
			newArray.add(array.get(i));
		}
		return newArray;
	}
	
	public static ArrayList<EffectFieldData> cloneDeepEffectFieldDataArray(ArrayList<EffectFieldData> array)
	{
		ArrayList<EffectFieldData> newArray = new ArrayList<EffectFieldData>();
		for (int i = 0; i < array.size(); i++)
		{
			newArray.add(array.get(i).clone());
		}
		return newArray;
	}
	
	public static ArrayList<EffectField> cloneShallowEffectFieldArray(ArrayList<EffectField> array)
	{
		ArrayList<EffectField> newArray = new ArrayList<EffectField>();
		for (int i = 0; i < array.size(); i++)
		{
			newArray.add(array.get(i));
		}
		return newArray;
	}
	
	public static ArrayList<EffectField> cloneDeepEffectFieldArray(ArrayList<EffectField> array)
	{
		ArrayList<EffectField> newArray = new ArrayList<EffectField>();
		for (int i = 0; i < array.size(); i++)
		{
			newArray.add(array.get(i).clone());
		}
		return newArray;
	}
	
	public static ArrayList<Attack> cloneShallowAttackArray(ArrayList<Attack> array)
	{
		ArrayList<Attack> newArray = new ArrayList<Attack>();
		for (int i = 0; i < array.size(); i++)
		{
			newArray.add(array.get(i));
		}
		return newArray;
	}
	
	public static ArrayList<Attack> cloneDeepAttackArray(ArrayList<Attack> array)
	{
		ArrayList<Attack> newArray = new ArrayList<Attack>();
		for (int i = 0; i < array.size(); i++)
		{
			newArray.add(array.get(i).clone());
		}
		return newArray;
	}
	
	//Array Clones
	
	public static Unit[] cloneShallowUnitArray(Unit[] array)
	{
		Unit[] newArray = new Unit[array.length];
		for (int i = 0; i < array.length; i++)
		{
			newArray[i] = array[i];
		}
		return newArray;
	}
	
	public static Unit[] cloneDeepUnitArray(Unit[] array)
	{
		Unit[] newArray = new Unit[array.length];
		for (int i = 0; i < array.length; i++)
		{
			newArray[i] = array[i].clone();
		}
		return newArray;
	}
	
	public static Effect[] cloneShallowEffectArray(Effect[] array)
	{
		Effect[] newArray = new Effect[array.length];
		for (int i = 0; i < array.length; i++)
		{
			newArray[i] = array[i];
		}
		return newArray;
	}
	
	public static Effect[] cloneDeepEffectArray(Effect[] array)
	{
		Effect[] newArray = new Effect[array.length];
		for (int i = 0; i < array.length; i++)
		{
			newArray[i] = array[i].clone();
		}
		return newArray;
	}
	
	public static EffectData[] cloneShallowEffectDataArray(EffectData[] array)
	{
		EffectData[] newArray = new EffectData[array.length];
		for (int i = 0; i < array.length; i++)
		{
			newArray[i] = array[i];
		}
		return newArray;
	}
	
	public static EffectData[] cloneDeepEffectDataArray(EffectData[] array)
	{
		EffectData[] newArray = new EffectData[array.length];
		for (int i = 0; i < array.length; i++)
		{
			newArray[i] = array[i].clone();
		}
		return newArray;
	}
	
	public static EffectFieldData[] cloneShallowEffectFieldDataArray(EffectFieldData[] array)
	{
		EffectFieldData[] newArray = new EffectFieldData[array.length];
		for (int i = 0; i < array.length; i++)
		{
			newArray[i] = array[i];
		}
		return newArray;
	}
	
	public static EffectFieldData[] cloneDeepEffectFieldDataArray(EffectFieldData[] array)
	{
		EffectFieldData[] newArray = new EffectFieldData[array.length];
		for (int i = 0; i < array.length; i++)
		{
			newArray[i] = array[i].clone();
		}
		return newArray;
	}
	
	public static EffectField[] cloneShallowEffectFieldArray(EffectField[] array)
	{
		EffectField[] newArray = new EffectField[array.length];
		for (int i = 0; i < array.length; i++)
		{
			newArray[i] = array[i];
		}
		return newArray;
	}
	
	public static EffectField[] cloneDeepEffectFieldArray(EffectField[] array)
	{
		EffectField[] newArray = new EffectField[array.length];
		for (int i = 0; i < array.length; i++)
		{
			newArray[i] = array[i].clone();
		}
		return newArray;
	}
	
	public static Attack[] cloneShallowAttackArray(Attack[] array)
	{
		Attack[] newArray = new Attack[array.length];
		for (int i = 0; i < array.length; i++)
		{
			newArray[i] = array[i];
		}
		return newArray;
	}
	
	public static Attack[] cloneDeepAttackArray(Attack[] array)
	{
		Attack[] newArray = new Attack[array.length];
		for (int i = 0; i < array.length; i++)
		{
			newArray[i] = array[i].clone();
		}
		return newArray;
	}
	
	public static Attack[] initialiseAttackList(AttackData[] attackData, Unit unit)
	{
		Attack[] attackList = new Attack[attackData.length];
		System.out.println("attackData.length: " + attackData.length);
		for(int i = 0; i < attackData.length; i++)
		{
			System.out.println("i: " + i);
			attackList[i] = attackData[i].makeAttack(unit);
		}
		return attackList;
	}
	
	public static Special[] initialiseSpecialList(SpecialData[] specialData, SpecialUnit unit)
	{
		Special[] specialList = new Special[specialData.length];
		for(int i = 0; i <  specialData.length; i++)
		{
			specialList[i] = specialData[i].makeSpecial(unit);
		}
		return specialList;
	}
}
