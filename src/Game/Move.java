package Game;


import Agents.GenericAgent;
import Agents.Ghost;
import Agents.Pacman;
import PacmanGrid.Block;
import PacmanGrid.Grid;
import PacmanGrid.Pill;
import PacmanGrid.Road;
import Utils.IntDimension;
import javafx.application.Platform;
import javafx.concurrent.Task;

public class Move  {
	
	public static final int HITWALL = -1;
	public static final int GOT_KILLED = -2;
	public static final int GOT_STANDARD_PILL = Pill.STANDARDPILL;
	public static final int GOT_GRAPE_PILL = Pill.GRAPE;
	public static final int GOT_POWER_PILL = Pill.POWERPILL;
	public static final int KILLED_ENEMY = 4;
	public static final int GOT_NONE_PILL = 0;
	public static final int SCARE_DURATION = 20;

	private static GenericAgent killer = null;
	private  static GenericAgent victim = null;
	private Move (){
	}
	
	public static int moveAgent(GenericAgent agent, Game game, IntDimension location ) {
		
		Grid grid = game.getGrid();
		int result = Move.GOT_NONE_PILL;
		try{
			Road road = (Road)grid.getBlock(location);
			if (agent instanceof Pacman){
				ghostScare(game, agent);
				if (road.getOccupiedBy() instanceof Ghost && agent.isScared() == true){
					result = Move.GOT_KILLED;
				}else if (road.getOccupiedBy() instanceof Ghost && agent.isScared() == false){
					result = Move.KILLED_ENEMY;
				}else if (road.getPill() != Pill.NONE  ){
					if (road.getPill() == Pill.POWERPILL){
						game.setScaredGhostsDuration(SCARE_DURATION);
						 scareAgents ( game,GenericAgent.PACMAN, false);
						 scareAgents ( game,GenericAgent.GHOST, true);
					}
					result = road.getPill();
					updateUI(grid,road, Pill.NONE);
				}
			} else {
				if (road.getOccupiedBy() instanceof Pacman && agent.isScared() == false){
					Pacman pacman = (Pacman)road.getOccupiedBy();
					result = Move.KILLED_ENEMY;
				} else if (road.getOccupiedBy() instanceof Pacman && agent.isScared() == true){
					result = Move.GOT_KILLED;
				} else if (road.getOccupiedBy() instanceof Ghost){
					result = Move.HITWALL;
				}
			}
			update(game, road, agent, result);
		}catch (ClassCastException e){
			 result = Move.HITWALL;}
		return result;
	}
	
	public static void resetPosition (Game game ,GenericAgent agent){
		Block block = game.getGrid().getBlock(new IntDimension(agent.getResetPos().X, agent.getResetPos().Y));
		IntDimension oldLocation = agent.getLocation();
		if (block instanceof Road){
			updateUI(game.getGrid(), ((Road)block), Pill.NONE);
			updateUI(agent,block);
			agent.setLocation(new IntDimension(agent.getResetPos().X, agent.getResetPos().Y));
			game.getGrid().getBlock(oldLocation).setOccupiedBy(null);
		}
	}
	
	private static void update (Game game,Road road,GenericAgent agent, int result){
		Move.killer = null;
		Move.victim = null;
		if (result == Move.GOT_KILLED ){
			Move.killer = road.getOccupiedBy();
			Move.victim = agent;
			resetPosition(game, Move.victim);
			road.setOccupiedBy(Move.killer);
		}else if (result == Move.KILLED_ENEMY){
			Move.killer = agent;
			Move.victim = road.getOccupiedBy();
			resetPosition(game, Move.victim);
			road.setOccupiedBy(Move.killer);
		}
		if (result != Move.GOT_KILLED){
			road.setOccupiedBy(agent);
			int x = road.getGridPosition().X;
			int y = road.getGridPosition().Y;
			IntDimension oldLocation = agent.getLocation();
			agent.setLocation(new IntDimension(x, y));
			updateUI(agent, road);
			game.getGrid().getBlock(oldLocation).setOccupiedBy(null);
		}
	}
	
	private static void updateUI (Grid grid ,Road road, int pill){
		
		Platform.runLater(new Runnable() {
		    public void run() {
		    	grid.updateRoad(road, pill);
		    }
		});
	}
	
	private static void updateUI (GenericAgent agent, Block toBlock){
		
		Platform.runLater(new Runnable() {
		    public void run() {
		    	animateAgent ( agent,  toBlock);
		    }
		});
	}
	
	private static void animateAgent (GenericAgent agent, Block toBlock){

		IntDimension toScreenLocation = new IntDimension (toBlock.getPixelDimensions().X * toBlock.getGridPosition().X,
										toBlock.getPixelDimensions().Y * toBlock.getGridPosition().Y);
		
//		IntDimension currentScreenLocation = new IntDimension (agent.getLocation().X * toBlock.getPixelDimensions().X,
//											agent.getLocation().Y * toBlock.getPixelDimensions().Y);
//		
//		IntDimension distance = new IntDimension (toScreenLocation.X - currentScreenLocation.X,
//								toScreenLocation.Y - currentScreenLocation.Y);
		
		agent.getGraphic().setTranslateX(toScreenLocation.X);
		agent.getGraphic().setTranslateY(toScreenLocation.Y);
	}
	
	private static void ghostScare (Game game, GenericAgent pacman){
		if (pacman.isScared() == false ){
			int duration = game.getScaredGhostsDuration();
			if (duration > 0){
				duration = duration - 1;
				game.setScaredGhostsDuration(duration);
			}else{
				 scareAgents ( game,GenericAgent.PACMAN, true);
				 scareAgents ( game,GenericAgent.GHOST, false);
				 game.setScaredGhostsDuration(0);
			}
		}
	}
	
	private static void scareAgents (Game game, int agentType, boolean state){
		Platform.runLater( new Runnable() {
			@Override
			public void run() {
				for (GenericAgent agent : game.getAgents().get(agentType)){
					agent.setScared(state);}
			}
		});
	}

	public static GenericAgent getKiller() {
		return killer;
	}

	public static GenericAgent getVictim() {
		return victim;
	}


	
}
