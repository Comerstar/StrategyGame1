package animation.layer;

import java.util.ArrayList;

import animation.sprite.Font;
import animation.sprite.Sprite;

public class TextBox extends Layer
{
	int height;
	int width;
	Font font;
	String display;
	ArrayList<String> wordList;
	
	
	public TextBox(int x, int y, int width, int height, double scale, boolean visible, String display, Font font, double fontSize)
	{
		super(x, y, scale, visible);
		this.font = font;
		this.width = width;
		this.height = height;
		int cursorX = 0;
		int cursorY = 0;
		int highestLetter = 0;
		int wordWidth = 0;
		int wordHeight = 0;
		wordList = new ArrayList<String>();
		ArrayList<Sprite> wordLetterList = new ArrayList<Sprite>();
		this.display = display;
		String character = "";
		String previousCharacter = "";
		Sprite sprite;
		String word = "";
		String preWordCharacter = "";
		int j;
		int letterX;
		for(int i = 0; i < display.length(); i++)
		{
			previousCharacter = character;
			character = String.valueOf(display.charAt(i));
			sprite = font.getLetterCopy(character);
			if(sprite != null)
			{
				sprite.setWidth((int) (sprite.getWidth() * fontSize));
				sprite.setHeight((int) (sprite.getHeight() * fontSize));
				if(highestLetter < sprite.getHeight())
				{
					highestLetter = sprite.getHeight();
					wordHeight = highestLetter;
				}
				if(character.equals(" "))
				{
					if(!word.equals(""))
					{
						if(cursorX + wordWidth > width)
						{
							cursorX = 0;
							cursorY += highestLetter + font.getLineSpacing() * fontSize;
							highestLetter = 0;
							if(cursorY + wordHeight> height)
							{
								break;
							}
						}
						letterX = 0;
						previousCharacter = preWordCharacter;
						character = preWordCharacter;
						for(j = 0; j < wordLetterList.size(); j++)
						{
							previousCharacter = character;
							character = String.valueOf(word.charAt(j));
							letterX += font.getSpacing(previousCharacter + character) * fontSize;
							wordLetterList.get(j).setLocation(cursorX + letterX, cursorY);
							letterX += wordLetterList.get(j).getWidth();
							spriteList.add(wordLetterList.get(j));
						}
						character = " ";
						preWordCharacter = character;
						wordLetterList.clear();
						wordList.add(word);
						word = "";
						wordHeight = 0;
						cursorX += wordWidth;
						spriteList.add(sprite);
						cursorX += sprite.getWidth();
						wordWidth = 0;
					}
				}
				else if(character.equals("\n"))
				{
					if(!word.equals(""))
					{
						if(cursorX + wordWidth > width)
						{
							cursorX = 0;
							cursorY += highestLetter + font.getLineSpacing() * fontSize;
							highestLetter = 0;
							if(cursorY + wordHeight> height)
							{
								break;
							}
						}
						letterX = 0;
						previousCharacter = preWordCharacter;
						character = preWordCharacter;
						for(j = 0; j < wordLetterList.size(); j++)
						{
							previousCharacter = character;
							character = String.valueOf(word.charAt(j));
							letterX += font.getSpacing(previousCharacter + character) * fontSize;
							wordLetterList.get(j).setLocation(cursorX + letterX, cursorY);
							letterX += wordLetterList.get(j).getWidth();
							spriteList.add(wordLetterList.get(j));
						}
						character = "\n";
						preWordCharacter = character;
						wordLetterList.clear();
						wordList.add(word);
						word = "";
						wordHeight = 0;
						cursorX = 0;
						cursorY += highestLetter + font.getLineSpacing() * fontSize;
						highestLetter = 0;
						if(cursorY + wordHeight> height)
						{
							break;
						}
						wordWidth = 0;
					}
				}
				else
				{
					wordLetterList.add(sprite);
					word += character;
					wordWidth += font.getSpacing(previousCharacter + character) * fontSize;
					wordWidth += sprite.getWidth();
					if(cursorY + highestLetter > height)
					{
						break;
					}
				}
			}
		}
		if(!word.equals(""))
		{
			boolean addWord = true;
			if(cursorX + wordWidth > width)
			{
				cursorX = 0;
				cursorY += highestLetter + font.getLineSpacing() * fontSize;
				highestLetter = 0;
				if(cursorY + wordHeight> height)
				{
					addWord = false;;
				}
			}
			if(addWord)
			{
				letterX = 0;
				previousCharacter = preWordCharacter;
				character = preWordCharacter;
				for(j = 0; j < wordLetterList.size(); j++)
				{
					previousCharacter = character;
					character = String.valueOf(word.charAt(j));
					letterX += font.getSpacing(previousCharacter + character) * fontSize;
					wordLetterList.get(j).setLocation(cursorX + letterX, cursorY);
					letterX += wordLetterList.get(j).getWidth();
					spriteList.add(wordLetterList.get(j));
				}
				wordLetterList.clear();
				wordList.add(word);
				word = "";
				wordHeight = 0;
				cursorX += wordWidth;
				wordWidth = 0;
			}
		}
	}
	
	public void setDisplay(String display, double fontSize)
	{
		int cursorX = 0;
		int cursorY = 0;
		int highestLetter = 0;
		int wordWidth = 0;
		int wordHeight = 0;
		wordList = new ArrayList<String>();
		ArrayList<Sprite> wordLetterList = new ArrayList<Sprite>();
		this.display = display;
		String character = "";
		String previousCharacter = "";
		Sprite sprite = null;
		String word = "";
		String preWordCharacter = "";
		spriteList.clear();
		int j;
		int letterX;
		for(int i = 0; i < display.length(); i++)
		{
			previousCharacter = character;
			character = String.valueOf(display.charAt(i));
			try
			{
				sprite = font.getLetterCopy(character);
			}
			catch (NullPointerException e)
			{
				System.out.println("Null Character: " + character);
			}
			System.out.println("Character: " + character);
			if(sprite != null)
			{
				sprite.setWidth((int) (sprite.getWidth() * fontSize));
				sprite.setHeight((int) (sprite.getHeight() * fontSize));
				if(highestLetter < sprite.getHeight())
				{
					highestLetter = sprite.getHeight();
					wordHeight = highestLetter;
				}
				if(character.equals(" "))
				{
					if(!word.equals(""))
					{
						if(cursorX + wordWidth > width)
						{
							cursorX = 0;
							cursorY += highestLetter + font.getLineSpacing() * fontSize;
							highestLetter = 0;
							if(cursorY + wordHeight> height)
							{
								break;
							}
						}
						letterX = 0;
						previousCharacter = preWordCharacter;
						character = preWordCharacter;
						for(j = 0; j < wordLetterList.size(); j++)
						{
							previousCharacter = character;
							character = String.valueOf(word.charAt(j));
							letterX += font.getSpacing(previousCharacter + character) * fontSize;
							wordLetterList.get(j).setLocation(cursorX + letterX, cursorY);
							letterX += wordLetterList.get(j).getWidth();
							spriteList.add(wordLetterList.get(j));
						}
						character = " ";
						preWordCharacter = character;
						wordLetterList.clear();
						wordList.add(word);
						word = "";
						wordHeight = 0;
						cursorX += wordWidth;
						spriteList.add(sprite);
						cursorX += sprite.getWidth();
						wordWidth = 0;
					}
				}
				else if(character.equals("\n"))
				{
					if(!word.equals(""))
					{
						if(cursorX + wordWidth > width)
						{
							cursorX = 0;
							cursorY += highestLetter + font.getLineSpacing() * fontSize;
							highestLetter = 0;
							if(cursorY + wordHeight> height)
							{
								break;
							}
						}
						letterX = 0;
						previousCharacter = preWordCharacter;
						character = preWordCharacter;
						for(j = 0; j < wordLetterList.size(); j++)
						{
							previousCharacter = character;
							character = String.valueOf(word.charAt(j));
							letterX += font.getSpacing(previousCharacter + character) * fontSize;
							wordLetterList.get(j).setLocation(cursorX + letterX, cursorY);
							letterX += wordLetterList.get(j).getWidth();
							spriteList.add(wordLetterList.get(j));
						}
						character = "\n";
						preWordCharacter = character;
						wordLetterList.clear();
						wordList.add(word);
						word = "";
						wordHeight = 0;
						cursorX = 0;
						cursorY += highestLetter + font.getLineSpacing() * fontSize;
						highestLetter = 0;
						System.out.println("Entering????");
						if(cursorY + wordHeight> height)
						{
							break;
						}
						wordWidth = 0;
					}
				}
				else
				{
					wordLetterList.add(sprite);
					word += character;
					wordWidth += font.getSpacing(previousCharacter + character) * fontSize;
					wordWidth += sprite.getWidth();
					if(cursorY + highestLetter > height)
					{
						break;
					}
				}
			}
		}
		if(!word.equals(""))
		{
			boolean addWord = true;
			if(cursorX + wordWidth > width)
			{
				cursorX = 0;
				cursorY += highestLetter + font.getLineSpacing() * fontSize;
				highestLetter = 0;
				if(cursorY + wordHeight> height)
				{
					addWord = false;;
				}
			}
			if(addWord)
			{
				letterX = 0;
				previousCharacter = preWordCharacter;
				character = preWordCharacter;
				for(j = 0; j < wordLetterList.size(); j++)
				{
					previousCharacter = character;
					character = String.valueOf(word.charAt(j));
					letterX += font.getSpacing(previousCharacter + character) * fontSize;
					wordLetterList.get(j).setLocation(cursorX + letterX, cursorY);
					letterX += wordLetterList.get(j).getWidth();
					spriteList.add(wordLetterList.get(j));
				}
				wordLetterList.clear();
				wordList.add(word);
				word = "";
				wordHeight = 0;
				cursorX += wordWidth;
				wordWidth = 0;
			}
		}
	}
	
	public ArrayList<String> getWordList()
	{
		return wordList;
	}
	
	@Override
	public TextBox getTextBox()
	{
		return this;
	}
}
