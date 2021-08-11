package animation.sprite;

import java.awt.Image;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.HashMap;

import javax.swing.ImageIcon;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class Font {
	
	public static String[] allCharacterList = new String[] {"A","B","C","D","E","F","G","H","I","J","K","L","M","N","O","P","Q","R","S","T","U","V","W","X","Y","Z",
			"a","b","c","d","e","f","g","h","i","j","k","l","m","n","o","p","q","r","s","t","u","v","w","x","y","z",
			"0","1","2","3","4","5","6","7","8","9",
			".","!","?"," ","_","-",",","'","+","=","<",">","[","]","{","}","(",")","%","@","*","|","\"","\\","/","$","&","^","<=",">=",":",";","\n"};
	private Sprite[] letterList;
	private String[] allowedLetterList;
	private HashMap<String,Integer> letterSpacing;
	
	public Font (String fontName)
	{
		String fullDir = "resources/text/" + fontName + "/" + fontName + ".png";
		ImageIcon img = getImageIconFromURL(fullDir);
		JSONObject info = getJSONInfo("resources/text/" + fontName + "/" + "map.JSON");
		if(img != null)
		{
			Image image = img.getImage();
			if(info != null)
			{
				JSONArray jsonArray;
				letterList = new Sprite[info.size() - 1];
				allowedLetterList = new String[info.size() - 1];
				int i;
				int x1;
				int y1;
				int x2;
				int y2;
				int added = 0;
				for(i = 0; i < allCharacterList.length; i++)
				{
					if(info.containsKey(allCharacterList[i]))
					{
						jsonArray = (JSONArray) info.get(allCharacterList[i]);
						allowedLetterList[added] = allCharacterList[i];
						x1 = ((Long)jsonArray.get(0)).intValue();
						y1 = ((Long)jsonArray.get(1)).intValue();
						x2 = ((Long)jsonArray.get(2)).intValue();
						y2 = ((Long)jsonArray.get(3)).intValue();
						letterList[added] = new Sprite(image, allCharacterList[i], true, true, x2 - x1, y2 - y1, 0, 0, 0, 0, x1, y1, x2, y2);
						added++;
					}
				}
			}
		}
		JSONObject spacingInfo = getJSONInfo("resources/text/" + fontName + "/" + "spacing.JSON");
		JSONArray jsonArray;
		letterSpacing = new HashMap<String, Integer>(spacingInfo.size(),(float) 1.0);
		for(int i = 0; i < (long) spacingInfo.get("size"); i++)
		{
			jsonArray = (JSONArray) spacingInfo.get(Integer.toString(i));
			letterSpacing.put((String) jsonArray.get(0), ((Long) jsonArray.get(1)).intValue());
		}
	}

	public static JSONObject getJSONInfo(String dir)
	{
		FileReader reader = null;
		URL JSONURL = null;
		try {
			JSONURL = Font.class.getClassLoader().getResource(dir);
			reader = new FileReader(new File(JSONURL.toURI()));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (URISyntaxException e) {
			e.printStackTrace();
		}
		JSONParser parser  = new JSONParser();
		try {
			return (JSONObject) parser.parse(reader);
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	
	public ImageIcon getImageIconFromURL(String dir)
	{
		ImageIcon icon = null;
		URL imgURL = getClass().getClassLoader().getResource(dir);
		if(imgURL != null)
		{
			icon = new ImageIcon(imgURL);
		}
		else
		{
			System.out.println("Unable to Find: " + dir);
		}
		return icon;
	}
	
	public Sprite getLetter(String letter)
	{
		for(int i = 0; i < allowedLetterList.length; i++) 
		{
			if(allowedLetterList[i].equals(letter))
			{
				return letterList[i];
			}
		}
		return null;
	}
	
	public Sprite getLetterCopy(String letter)
	{
		for(int i = 0; i < allowedLetterList.length; i++) 
		{
			if(allowedLetterList[i].equals(letter))
			{
				return letterList[i].clone();
			}
		}
		return null;
	}
	
	public String[] getAllowedLetterList()
	{
		return allowedLetterList;
	}
	
	public int getSpacing(String letters)
	{
		if(letterSpacing.containsKey(letters))
		{
			return letterSpacing.get(letters);
		}
		else
		{
			return letterSpacing.get("standard");
		}
	}
	
	public int getLineSpacing()
	{
		return letterSpacing.get("line");
	}
}
