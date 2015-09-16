package Actions;

import Agents.GenericAgent;
import PacmanGrid.Grid;
import Utils.IntDimension;

public final class Move implements Action {
	
	public static boolean moveAgent (GenericAgent agent,Grid grid, IntDimension location){
		
		boolean moved = false;
		System.out.println( grid.getBlock(location).getClass() );
		return moved;
	}

	@Override
	public int preconditions(Object... input) {
		
		return 0;
	}

}
