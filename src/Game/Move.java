package Game;


import java.lang.reflect.Method;
import java.util.concurrent.CountDownLatch;

import Agents.GenericAgent;
import Agents.Ghost;
import Agents.Pacman;
import GeneticAlgorithm.Gene;
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
	public static final int SCARE_DURATION = 40;
	private static final UpdateUI uiUpdater = new UpdateUI();

	private static GenericAgent killer = null;
	private  static GenericAgent victim = null;
	private Move (){
	}
	
	public static int moveAgent(GenericAgent agent, Game game, IntDimension location ) {
		
		Grid grid = game.getGrid();
		int result = Move.GOT_NONE_PILL;
		if (agent.isDead() == true){return result;}
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
						uiUpdater.doUpdate(UpdateUI.CHANGE_SCARE_STATE,
								game,null, null, GenericAgent.PACMAN, false);
						uiUpdater.doUpdate(UpdateUI.CHANGE_SCARE_STATE,
								game,null, null, GenericAgent.GHOST, true);
					}
					result = road.getPill();
					game.setScore(game.getScore() + result);
					uiUpdater.doUpdate(UpdateUI.UPDATE_ROAD,game, null, road, Pill.NONE, null);
					//updateUI(grid,road, Pill.NONE);
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
			uiUpdater.doUpdate(UpdateUI.UPDATE_ROAD,game, null, ((Road)block), Pill.NONE, null);
			//updateUI(game.getGrid(), ((Road)block), Pill.NONE);
			uiUpdater.doUpdate(UpdateUI.ANIMATE_AGENT, null, agent, block, null, null);
			//updateUI(agent,block);
			agent.setLocation(new IntDimension(agent.getResetPos().X, agent.getResetPos().Y));
			if (oldLocation != null){game.getGrid().getBlock(oldLocation).setOccupiedBy(null);}
		}
	}
	
	private static void update (Game game,Road road,GenericAgent agent, int result){
		Move.killer = null;
		Move.victim = null;
		if (result == Move.GOT_KILLED ){
			Move.killer = road.getOccupiedBy();
			Move.victim = agent;
			road.setOccupiedBy(Move.killer);
			Move.victim.decrementLife();
			if (Move.victim.isDead() == false) {resetPosition(game, Move.victim);}
		}else if (result == Move.KILLED_ENEMY){
			Move.killer = agent;
			Move.victim = road.getOccupiedBy();
			road.setOccupiedBy(Move.killer);
			Move.victim.decrementLife();
			if (Move.victim.isDead() == false) {resetPosition(game, Move.victim);}
		}
		if (result != Move.GOT_KILLED){
			road.setOccupiedBy(agent);
			int x = road.getGridPosition().X;
			int y = road.getGridPosition().Y;
			IntDimension oldLocation = agent.getLocation();
			agent.setLocation(new IntDimension(x, y));
			uiUpdater.doUpdate(UpdateUI.ANIMATE_AGENT, null, agent, road, null, null);
			//updateUI(agent, road);
			game.getGrid().getBlock(oldLocation).setOccupiedBy(null);
		}
	}
	
	private static void ghostScare (Game game, GenericAgent pacman){
		if (pacman.isScared() == false ){
			int duration = game.getScaredGhostsDuration();
			if (duration > 0){
				duration = duration - 1;
				game.setScaredGhostsDuration(duration);
			}else{
				 //scareAgents ( game,GenericAgent.PACMAN, true);
				uiUpdater.doUpdate(UpdateUI.CHANGE_SCARE_STATE,game,null,null,GenericAgent.PACMAN,true);
				uiUpdater.doUpdate(UpdateUI.CHANGE_SCARE_STATE,game,null,null,GenericAgent.GHOST,false);

				 //scareAgents ( game,GenericAgent.GHOST, false);
				 game.setScaredGhostsDuration(0);
			}
		}
	}
	

	public static GenericAgent getKiller() {
		return killer;
	}

	public static GenericAgent getVictim() {
		return victim;
	}


	
}
