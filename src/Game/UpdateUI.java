package Game;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CountDownLatch;

import Agents.GenericAgent;
import PacmanGrid.Block;
import PacmanGrid.Grid;
import PacmanGrid.Road;
import Utils.IntDimension;
import javafx.application.Platform;

interface Command {
    void runCommand(Game game, GenericAgent agent, Block block,Integer intParm, Boolean booleanParm );
}



class UpdateUI {
	private Map <Integer,Command> commands = new HashMap<Integer,Command>();
	public static final int ANIMATE_AGENT = 0; 
	public static final int CHANGE_SCARE_STATE = 1;
	public static final int UPDATE_ROAD = 2;
	 
	public  UpdateUI() {
		initMap();
	}
	
	
	public synchronized void doUpdate (int updateType,Game game, GenericAgent agent, Block block, Integer intParm, Boolean booleanParm){
		
		Command command = commands.get(updateType);
		if (Platform.isFxApplicationThread()== false){
			final CountDownLatch latch = new CountDownLatch(1);
			Platform.runLater(new Runnable() {
				@Override
				public void run() {
					command.runCommand(game, agent, block, intParm, booleanParm);
					latch.countDown();
				}
			});
			try {latch.await();} 
			catch (InterruptedException e) {e.printStackTrace();}
		}else{
			command.runCommand(game, agent, block, intParm, booleanParm);
		}
			
	}
	 
	private void initMap (){
		commands.put(UpdateUI.ANIMATE_AGENT, new Command (){
			@Override
			public void runCommand(Game game, GenericAgent agent, Block block, Integer intParm, Boolean booleanParm){
				animateAgent(agent, block);
			}
		});
		
		commands.put(UpdateUI.CHANGE_SCARE_STATE, new Command (){

			@Override
			public void runCommand(Game game, GenericAgent agent, Block block, Integer intParm, Boolean booleanParm) {
				scareAgents (game,intParm,booleanParm);
			}
		});
		
		commands.put(UpdateUI.UPDATE_ROAD, new Command (){

			@Override
			public void runCommand(Game game, GenericAgent agent, Block block, Integer intParm, Boolean booleanParm) {
				roadPills (game.getGrid() ,((Road)block) ,intParm);
			}
		});
	}
	private static void animateAgent (GenericAgent agent, Block toBlock){

		IntDimension toScreenLocation = new IntDimension (toBlock.getPixelDimensions().X * toBlock.getGridPosition().X,
										toBlock.getPixelDimensions().Y * toBlock.getGridPosition().Y);
		
		agent.getGraphic().setTranslateX(toScreenLocation.X);
		agent.getGraphic().setTranslateY(toScreenLocation.Y);
	}
	
	private static void scareAgents (Game game, int agentType, boolean state){
		for (GenericAgent agent : game.getAgents().get(agentType)){
			agent.setScared(state);}
	}
	
	private static void roadPills (Grid grid ,Road road, int pill){
		grid.updateRoad(road, pill);
	}

}
