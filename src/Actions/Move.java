package Actions;

import java.util.Timer;
import java.util.TimerTask;

import Agents.GenericAgent;
import Agents.Ghost;
import Agents.Pacman;
import Game.Game;
import PacmanGrid.Grid;
import PacmanGrid.Pill;
import PacmanGrid.Road;
import Utils.IntDimension;

class Move implements Action{
	
	private  int DEFAULTSPEED = 1000;
	private   Timer timer = new Timer ();
	protected Move (){
	}
	
	public boolean doAction(GenericAgent agent, Game game, Object...parameters ) {
		
		IntDimension location = (IntDimension)parameters[0];
		Grid grid = game.getGrid();
		try{
			Road road = (Road)grid.getBlock(location);
			IntDimension oldLocation = agent.getLocation();
			
			if (agent instanceof Pacman){
				System.out.println("instance mate");
				if (road.getOccupiedBy() != null && agent.isScared() == true){
					agent.setLives(agent.getLives() - 1);
					System.out.println("hit it moit");
				}else if ( road.getOccupiedBy() != null && agent.isScared() == false){
					road.getOccupiedBy().setLives(agent.getLives() - 1);
				}else if (road.getOccupiedBy() == null && road.getPill() != Pill.NONE){
					game.setScore(game.getScore() + road.getPill());
					road.setPill(Pill.NONE);
				}
			} else {
				if (road.getOccupiedBy() instanceof Pacman && agent.isScared() == false){
					Pacman pacman = (Pacman)road.getOccupiedBy();
					pacman.setLives(pacman.getLives()-1);
					if (pacman.getLives() < 0){game.end();}
					else {game.restart();}
				} else if (road.getOccupiedBy() instanceof Pacman && agent.isScared() == true){
					agent.setLives(agent.getLives() - 1);
				} else if (road.getOccupiedBy() instanceof Ghost){
					return false;
				}
			}
			agent.setLocation(road.getGridPosition());
			countDown (agent.getSpeed());
			animateAgent(agent, oldLocation, location);
			road.setOccupiedBy(agent);
			game.getGrid().getBlock(oldLocation).setOccupiedBy(null);
			return true;
		}catch (ClassCastException e){
			return false;
		}
	}
	
	private void animateAgent (GenericAgent agent, IntDimension fromLocation, IntDimension toLocation){
	}
	
	private  void countDown (int agentSpeed){
		
	    timer.scheduleAtFixedRate(new TimerTask() {
	    	int count = 0;
	        public void run() {
	        	System.out.println("secs  " + count);
	        	count ++;
	        }
	    }, 0, DEFAULTSPEED/agentSpeed);
	}

	@Override
	public boolean doAction(Object... parameters) {
		return false;
	}
}
