package Actions;

import Agents.GenericAgent;
import Game.Game;
import PacmanGrid.Grid;
import Utils.IntDimension;

interface  Action {

	boolean doAction (GenericAgent agent,Game game, Object...parameters);
	boolean doAction (Object...parameters);
}
