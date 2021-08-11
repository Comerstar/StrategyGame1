package animation;

import animation.layer.Layer;
import animation.sprite.Sprite;

public class AnimationCommand {
	
	//The state that the sprite should be set to
	String state;
	//The sprite that is to be effected
	Sprite sprite;
	//The layer of the sprite that is to be effected\
	Layer layer;
	//What kind of thing this is
	int action;
	int[] intialVector;
	int[] acceleration;
	//0 = Sprite State change, 1 = Sprite Motion Change, 2 = Sprite Motion and State Change, 3 = Layer update, 4 = refresh map
	int commandType;
	
	public AnimationCommand(int commandType, String state)
	{
		this.commandType = commandType;
		this.state = state;
	}
	
	public String getState()
	{
		return state;
	}
	
	public int getCommandType()
	{
		return commandType;
	}
}
