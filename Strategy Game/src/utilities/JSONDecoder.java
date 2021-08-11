package utilities;

import java.io.File;
import java.io.FileNotFoundException; 
import java.io.FileReader;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class JSONDecoder {

	private static JSONParser parser;
	private static FileReader reader;
	private static URL JSONURL;
	private static JSONObject unitInfo;
	private static JSONObject tileInfo;
	private static JSONObject attackInfo;
	private static JSONObject effectInfo;
	private static JSONObject effectName;
	private static JSONObject specialInfo;
	private static JSONObject unitAnimationInfo;
	private static JSONObject tileAnimationInfo;
	
	public static void setupUnitInfo()
	{
		try {
			reader = null;
			JSONURL = null;
			JSONURL = JSONDecoder.class.getClassLoader().getResource("unit/UnitList.JSON");
			reader = new FileReader(new File(JSONURL.toURI()));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (URISyntaxException e) {
			e.printStackTrace();
		}
		parser  = new JSONParser();
		try {
			unitInfo = (JSONObject) parser.parse(reader);
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ParseException e) {
			e.printStackTrace();
		}
	}
	
	public static JSONObject getUnitInfo()
	{
		return unitInfo;
	}
	
	public static void setupTileInfo()
	{
		try {
			reader = null;
			JSONURL = null;
			JSONURL = JSONDecoder.class.getClassLoader().getResource("terrain/TileList.JSON");
			reader = new FileReader(new File(JSONURL.toURI()));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (URISyntaxException e) {
			e.printStackTrace();
		}
		parser  = new JSONParser();
		try {
			tileInfo = (JSONObject) parser.parse(reader);
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ParseException e) {
			e.printStackTrace();
		}
	}
	
	public static JSONObject getTileInfo()
	{
		return tileInfo;
	}
	
	public static void setupAttackInfo()
	{
		try {
			reader = null;
			JSONURL = null;
			JSONURL = JSONDecoder.class.getClassLoader().getResource("attack/AttackList.JSON");
			reader = new FileReader(new File(JSONURL.toURI()));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (URISyntaxException e) {
			e.printStackTrace();
		}
		parser  = new JSONParser();
		try {
			attackInfo = (JSONObject) parser.parse(reader);
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ParseException e) {
			e.printStackTrace();
		}
	}
	
	public static JSONObject getAttackInfo()
	{
		return attackInfo;
	}
	
	public static void setupEffectInfo()
	{
		try {
			reader = null;
			JSONURL = null;
			JSONURL = JSONDecoder.class.getClassLoader().getResource("attack/EffectList.JSON");
			reader = new FileReader(new File(JSONURL.toURI()));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (URISyntaxException e) {
			e.printStackTrace();
		}
		parser  = new JSONParser();
		try {
			effectInfo = (JSONObject) parser.parse(reader);
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ParseException e) {
			e.printStackTrace();
		}
	}
	
	public static JSONObject getEffectInfo()
	{
		return effectInfo;
	}
	
	public static void setupEffectName()
	{
		try {
			reader = null;
			JSONURL = null;
			JSONURL = JSONDecoder.class.getClassLoader().getResource("attack/EffectTypeList.JSON");
			reader = new FileReader(new File(JSONURL.toURI()));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (URISyntaxException e) {
			e.printStackTrace();
		}
		parser  = new JSONParser();
		try {
			effectName = (JSONObject) parser.parse(reader);
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ParseException e) {
			e.printStackTrace();
		}
	}
	
	public static JSONObject getEffectName()
	{
		return effectName;
	}
	
	public static void setupSpecialInfo()
	{
		try {
			reader = null;
			JSONURL = null;
			JSONURL = JSONDecoder.class.getClassLoader().getResource("attack/SpecialList.JSON");
			reader = new FileReader(new File(JSONURL.toURI()));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (URISyntaxException e) {
			e.printStackTrace();
		}
		parser  = new JSONParser();
		try {
			specialInfo = (JSONObject) parser.parse(reader);
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ParseException e) {
			e.printStackTrace();
		}
	}
	
	public static JSONObject getSpecialInfo()
	{
		return specialInfo;
	}
	
	public static void setupUnitAnimationInfo()
	{
		try {
			reader = null;
			JSONURL = null;
			JSONURL = JSONDecoder.class.getClassLoader().getResource("animation/sprite/UnitAnimation.JSON");
			reader = new FileReader(new File(JSONURL.toURI()));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (URISyntaxException e) {
			e.printStackTrace();
		}
		parser  = new JSONParser();
		try {
			unitAnimationInfo = (JSONObject) parser.parse(reader);
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ParseException e) {
			e.printStackTrace();
		}
	}
	
	public static JSONObject getUnitAnimationInfo()
	{
		return unitAnimationInfo;
	}
	
	public static void setupTileAnimationInfo()
	{
		try {
			reader = null;
			JSONURL = null;
			JSONURL = JSONDecoder.class.getClassLoader().getResource("animation/sprite/TileAnimation.JSON");
			reader = new FileReader(new File(JSONURL.toURI()));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (URISyntaxException e) {
			e.printStackTrace();
		}
		parser  = new JSONParser();
		try {
			tileAnimationInfo = (JSONObject) parser.parse(reader);
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ParseException e) {
			e.printStackTrace();
		}
	}
	
	public static JSONObject getTileAnimationInfo()
	{
		return tileAnimationInfo;
	}
	
}
