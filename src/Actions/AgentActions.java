package Actions;

import Agents.GenericAgent;
import Game.Game;
import Utils.IntDimension;

public final class AgentActions {
	private Move move = new Move ();
	
	public void moveAgent (GenericAgent agent, Game game, IntDimension location){
		move.doAction(agent, game, location);
	}
	
	public void trapPacman (){
		
	}

}
