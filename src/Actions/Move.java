package Actions;


import Agents.GenericAgent;
import Agents.Ghost;
import Agents.Pacman;
import Game.Game;
import PacmanGrid.Block;
import PacmanGrid.Grid;
import PacmanGrid.Pill;
import PacmanGrid.Road;
import Utils.IntDimension;

public class Move  {
	
	public static final int HITWALL = -1;
	public static final int GOT_KILLED = -2;
	public static final int GOT_STANDARD_PILL = Pill.STANDARDPILL;
	public static final int GOT_GRAPE_PILL = Pill.GRAPE;
	public static final int GOT_POWER_PILL = Pill.POWERPILL;
	public static final int KILLED_ENEMY = 4;
	public static final int GOT_NONE_PILL = 0;
	private Move (){
	}
	
	public static int moveAgent(GenericAgent agent, Game game, IntDimension location ) {
		
		//IntDimension location = (IntDimension)parameters[0];
		Grid grid = game.getGrid();
		int result = Move.GOT_NONE_PILL;
		try{
			Road road = (Road)grid.getBlock(location);
			IntDimension oldLocation = agent.getLocation();
			
			if (agent instanceof Pacman){
				if (road.getOccupiedBy() != null && agent.isScared() == true){
					//agent.setLives(agent.getLives() - 1);
					result = Move.GOT_KILLED;
				}else if ( road.getOccupiedBy() != null && agent.isScared() == false){
					//road.getOccupiedBy().setLives(agent.getLives() - 1);
					result = Move.KILLED_ENEMY;
				}else if (road.getOccupiedBy() == null && road.getPill() != Pill.NONE){
					game.setScore(game.getScore() + road.getPill());
					grid.updateRoad(road, Pill.NONE);
					result = road.getPill();
				}
			} else {
				if (road.getOccupiedBy() instanceof Pacman && agent.isScared() == false){
					Pacman pacman = (Pacman)road.getOccupiedBy();
					pacman.setLives(pacman.getLives()-1);
					if (pacman.getLives() < 0){game.end();}
					else {game.restart();}
					result = Move.KILLED_ENEMY;
				} else if (road.getOccupiedBy() instanceof Pacman && agent.isScared() == true){
					agent.setLives(agent.getLives() - 1);
					result = Move.GOT_KILLED;
				} else if (road.getOccupiedBy() instanceof Ghost){
					result = Move.HITWALL;
				}
			}
			agent.setLocation(road.getGridPosition());
			animateAgent(agent, road);
			road.setOccupiedBy(agent);
			game.getGrid().getBlock(oldLocation).setOccupiedBy(null);
		}catch (ClassCastException e){
			 result = Move.HITWALL;
		}
		return result;
	}
	
	private static void animateAgent (GenericAgent agent, Block toBlock){
		IntDimension toScreenLocation = new IntDimension (toBlock.getPixelDimensions().X * toBlock.getGridPosition().X,
										toBlock.getPixelDimensions().Y * toBlock.getGridPosition().Y);
		
		IntDimension currentScreenLocation = new IntDimension (agent.getLocation().X * toBlock.getPixelDimensions().X,
											agent.getLocation().Y * toBlock.getPixelDimensions().Y);
		
		IntDimension distance = new IntDimension (toScreenLocation.X - currentScreenLocation.X,
								toScreenLocation.Y - currentScreenLocation.Y);
		
		agent.getGraphic().setTranslateX(toScreenLocation.X);
		agent.getGraphic().setTranslateY(toScreenLocation.Y);
	}

}
