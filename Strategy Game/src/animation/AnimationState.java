package animation;

import java.util.HashMap;

public class AnimationState {
	
	String[] states;
	String[] endStates;
	HashMap<String, String> stateToEndState;
	
	public AnimationState(String[] states, String[]  endStates)
	{
		this.states = states;
		this.endStates = endStates;
		stateToEndState = new HashMap<String, String>(states.length + 1, (float) 1.0);
		for(int i = 0; i < states.length; i++)
		{
			stateToEndState.put(states[i], endStates[i]);
			//System.out.println("states[" + i + "]: " + states[i]);
			//System.out.println("endStates[" + i + "]: " + endStates[i]);
		}
	}
	
	public String endState(String state)
	{
		return stateToEndState.get(state);
	}
}
