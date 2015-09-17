package Actions;

import java.util.Timer;
import java.util.TimerTask;

import Agents.GenericAgent;
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
				if (road.getOccupiedBy() != null && agent.isScared() == true){
					agent.setLives(agent.getLives() - 1);
				}else if (road.getPill() != Pill.NONE){
					game.setScore(game.getScore() + road.getPill());
					road.setPill(Pill.NONE);
				}
			} else {
				agent.
			}
			agent.setLocation(road.getGridPosition());
			countDown (agent.getSpeed());
			animateAgent(agent, oldLocation, location);
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
