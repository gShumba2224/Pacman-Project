package AI;

import Agents.GenericAgent;
import Agents.Ghost;
import Agents.Pacman;
import Game.Game;
import Neurons.NeuralLayer;
import Neurons.NeuralNetwork;
import Neurons.NeuralNetworkReader;
import PacmanGrid.Block;
import PacmanGrid.Road;
import Utils.IntDimension;

public class InputReader extends NeuralNetworkReader {

	public Game game = null;
	public InputReader (Game game){
		this.game = game;
	}
	@Override
	public void readInputs(NeuralNetwork network, Object... parameters) {
		NeuralLayer inputLayer = network.getInputLayer();
		GenericAgent agent = (GenericAgent) parameters[0];
		Game game = (Game) parameters[1];
		IntDimension blockPos = new IntDimension (agent.getLocation().X,agent.getLocation().Y);
		Block block;
		blockPos.X = blockPos.X - 1;
		block = game.getGrid().getBlock(new IntDimension(10,9)); //Get Left
		setAgentInput(block, agent, inputLayer, 0);
		
		blockPos.X = blockPos.X + 2;
		block = game.getGrid().getBlock(blockPos); //Get Right
		setAgentInput(block, agent, inputLayer, 3);
		
		blockPos.X = blockPos.X -1; blockPos.Y = blockPos.Y - 1;
		block = game.getGrid().getBlock(blockPos); //Get Top
		setAgentInput(block, agent, inputLayer, 6);
		
		blockPos.Y = blockPos.Y + 2;
		block = game.getGrid().getBlock(blockPos); //Get Bottom
		setAgentInput(block, agent, inputLayer, 9);
		
		if (agent.isScared() == true){inputLayer.getNeurons().get(12).setOutputValue(0.0);
		}else {inputLayer.getNeurons().get(12).setOutputValue(1.0);}		
	}
    
    public double calculateDistance (IntDimension from, IntDimension to ){
    	IntDimension distance = new IntDimension (0,0);
		distance.X = Math.abs( from.X - to.X );
		distance.Y =  Math.abs(from.Y - to.Y );
		double magnitude = Math.sqrt((distance.X * distance.X ) + (distance.Y*distance.Y));
		return magnitude;
    }
    
    public double closestEnemyDistance ( GenericAgent agent, int agentType){
    	double closet = 0.0;
    	double distance ;
    	distance = calculateDistance(game.getAgents().get(agentType).get(0).getLocation(),agent.getLocation());
    	for (int i = 1; game.getAgents().get(agentType).size() > i ; i++){
    		GenericAgent enemy =  game.getAgents().get(agentType).get(i);
    		distance = calculateDistance(enemy.getLocation(), agent.getLocation());
    		if (distance < closet){closet = distance;}
    	}
    	return closet;
    }
    
    public void setAgentInput (Block block,GenericAgent agent, NeuralLayer inputLayer, int startIndex){
    	if (agent  instanceof Pacman){
    		setPacmanInput (block, agent,  inputLayer, startIndex);
    	}else {
    		setGhostInput (block, agent,  inputLayer, startIndex);
    	}
    }
	
    public void setPacmanInput (Block block,GenericAgent agent, NeuralLayer inputLayer, int startIndex){
    	try{
    		Road road = (Road)block;
    		inputLayer.getNeurons().get(startIndex).setOutputValue(1.0);
    		//System.out.println(block);
    		inputLayer.getNeurons().get(startIndex + 1).setOutputValue(road.getPill()/10);
    		double enemyDist = closestEnemyDistance(agent, GenericAgent.PACMAN);
    		if (enemyDist != 0){enemyDist =  1 - (1/enemyDist);}
    		else enemyDist = 0;
    		inputLayer.getNeurons().get(startIndex + 2).setOutputValue(enemyDist);
    	}catch (ClassCastException e){
    		for (int i = 0; i < 3; i++){
    			inputLayer.getNeurons().get(i).setOutputValue(-1);}
    	}
    }
    
    public void setGhostInput (Block block,GenericAgent agent, NeuralLayer inputLayer, int startIndex){
    	try{
    		Road road = (Road)block;
    		inputLayer.getNeurons().get(startIndex).setOutputValue(1.0);
    		if ( road.getOccupiedBy() != null && (road.getOccupiedBy() instanceof Ghost == true)){
    			inputLayer.getNeurons().get(startIndex + 1).setOutputValue(-1.0);
    		}else { inputLayer.getNeurons().get(startIndex + 1).setOutputValue(1.0);}
    		double enemyDist = closestEnemyDistance(agent, GenericAgent.GHOST);
    		if (enemyDist != 0){enemyDist =  1 - (1/enemyDist);}
    		else enemyDist = 0;
    		inputLayer.getNeurons().get(startIndex + 2).setOutputValue(enemyDist);
    	}catch (ClassCastException e){
    		for (int i = 0; i < 3; i++){
    			inputLayer.getNeurons().get(i).setOutputValue(-1);}
    	}
    }

}
