package Actions;

import Agents.GenericAgent;
import Game.Game;
import Utils.IntDimension;

public final class AgentActions {
	private AgentActions(){}
	private static Move move = new Move ();
	
	public static boolean moveAgent (GenericAgent agent, Game game, IntDimension location){
		return (move.doAction(agent, game, location));
	}
	
	public static void trapPacman (){
		
	}

}
