package Actions;

import static org.junit.Assert.*;

import java.io.File;
import java.io.IOException;

import org.junit.Test;

import Agents.Ghost;
import Agents.Pacman;
import Game.Game;
import PacmanGrid.GridDrawer;
import PacmanGrid.Pill;
import Utils.IntDimension;

public class AgentActionsTest {

	@Test
	public void moveAgentTest() {
		Ghost ghost = new Ghost();
		Pacman pacman = new Pacman ();
		Game game = new Game ();
		try {
			game.setGrid(GridDrawer.drawFromImage(new File ("C:\\Users\\GMAN\\Desktop\\Temp Stuff\\testGrid.jpg")));
		} catch (IOException e) {e.printStackTrace();}
		
		pacman.setLocation(new IntDimension (0,0));
		ghost.setLocation(new IntDimension (0,0));
		
		AgentActions.moveAgent(pacman, game, new IntDimension (0,1));
		assertEquals(0, pacman.getLocation().getX()); 
		assertEquals(1, pacman.getLocation().getY());
		assertEquals(game.getScore(),Pill.STANDARDPILL);
		
		AgentActions.moveAgent(pacman, game, new IntDimension (1,1));
		assertEquals(1, pacman.getLocation().getX()); 
		assertEquals(1, pacman.getLocation().getY());
		assertEquals(game.getScore(), Pill.POWERPILL + Pill.STANDARDPILL);
		
		AgentActions.moveAgent(pacman, game, new IntDimension (2,1));
		assertEquals(2, pacman.getLocation().getX()); 
		assertEquals(1, pacman.getLocation().getY());
		assertEquals(game.getScore(), Pill.GRAPE + Pill.POWERPILL + Pill.STANDARDPILL);
		
		//Move to previous pill
		AgentActions.moveAgent(pacman, game, new IntDimension (1,1));
		assertEquals(1, pacman.getLocation().getX()); 
		assertEquals(1, pacman.getLocation().getY());
		assertEquals(game.getScore(), Pill.GRAPE + Pill.POWERPILL + Pill.STANDARDPILL);
		
		// Move onto wall
		pacman.setLocation(new IntDimension (2,1));
		AgentActions.moveAgent(pacman, game, new IntDimension (2,0));
		assertEquals(2, pacman.getLocation().getX()); 
		assertEquals(1, pacman.getLocation().getY());
		assertEquals(game.getScore(), Pill.GRAPE + Pill.POWERPILL + Pill.STANDARDPILL);
		
		//Move to tile with Ghost while scared
		AgentActions.moveAgent(ghost, game, new IntDimension (1,2));
		AgentActions.moveAgent(pacman, game, new IntDimension (1,2));
		assertEquals(1, pacman.getLocation().getX()); assertEquals(2, pacman.getLocation().getY());
		assertEquals(1, ghost.getLocation().getX());  assertEquals(2, ghost.getLocation().getY());
		assertEquals(0, pacman.getLives());
		
		//Move to tile with Ghost not scared
		pacman.setScared(false); ghost.setScared(true);
		AgentActions.moveAgent(ghost, game, new IntDimension (1,2));
		AgentActions.moveAgent(pacman, game, new IntDimension (1,2));
		assertEquals(1, pacman.getLocation().getX()); assertEquals(2, pacman.getLocation().getY());
		assertEquals(1, ghost.getLocation().getX());  assertEquals(2, ghost.getLocation().getY());
		assertEquals(0, ghost.getLives());
	}

}
