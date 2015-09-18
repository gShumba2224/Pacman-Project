package Actions;

import Agents.GenericAgent;
import Game.Game;
import Utils.IntDimension;

public final class AgentActions {
	private AgentActions(){}
	private static Move move = new Move ();
	
	public static void moveAgent (GenericAgent agent, Game game, IntDimension location){
		move.doAction(agent, game, location);
	}
	
	public static void trapPacman (){
		
	}

}
