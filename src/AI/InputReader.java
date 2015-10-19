package AI;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import Agents.GenericAgent;
import Agents.Ghost;
import Agents.Pacman;
import Game.Game;
import Neurons.NeuralLayer;
import Neurons.NeuralNetwork;
import Neurons.NeuralNetworkReader;
import Neurons.Neuron;
import PacmanGrid.Block;
import PacmanGrid.Grid;
import PacmanGrid.Road;
import PacmanGrid.Wall;
import Search.ArtifactSearch;
import Search.Container;
import Utils.IntDimension;

public class InputReader extends NeuralNetworkReader {
	
	ArtifactSearch search = null;
	public Game game = null;
	
	public InputReader (Game game){
		search = new ArtifactSearch(game.getGrid());
		this.game = game;
	}
	
	public List<Block> getAdjacentBlocks (IntDimension centre, Grid grid){
		
		List <Block> adjacentBlocks = new ArrayList<Block>();
		Block block;
		int x = centre.X;
		int y = centre.Y;
		
		block = grid.getBlock(new IntDimension(x-1, y)); //left
		adjacentBlocks.add(block);
		
		block = grid.getBlock(new IntDimension(x+1, y)); //right
		adjacentBlocks.add(block);
		
		block = grid.getBlock(new IntDimension(x, y-1)); //top
		adjacentBlocks.add(block);
		
		block = grid.getBlock(new IntDimension(x, y+1)); //bottom
		adjacentBlocks.add(block);
		
		return adjacentBlocks;
	}
	
	@Override
	public void readInputs(NeuralNetwork network, Object... parameters) {
		NeuralLayer inputLayer = network.getInputLayer();
		GenericAgent agent = (GenericAgent) parameters[0];
		Game game = (Game) parameters[1];
		
		setAgentInput(game, agent, inputLayer);
	}
    
    public void setAgentInput (Game game,GenericAgent agent, NeuralLayer inputLayer){
    	if (agent  instanceof Pacman){
    		setPacmanInput (game, agent,  inputLayer);
    	}else {
    	setGhostInput (game, agent,  inputLayer);
    	}
    }
	
    public void setPacmanInput (Game game,GenericAgent agent, NeuralLayer inputLayer){
		List <Neuron> neurons = inputLayer.getNeurons();
		List <Block> blocks = getAdjacentBlocks (agent.getLocation(), game.getGrid());
		Map<Integer,Container> pillDistances =search.findNearestPill(game.getGrid(), agent.getLocation(), 1000);
		List <Double> agentDistances = null;
		int index = 0;
		
    	for (int i = 0; i < inputLayer.getNeurons().size();i = i+7){
    		
    		Block block = blocks.get(index);
    		if ( block instanceof Road){
    			// closetPill
    			double distance =-1;
    			try{
    				 distance = pillDistances.get(block.getGridNumber()).distance;
    			}catch (NullPointerException e){distance = -1;}
    			if (distance > 0){
    				distance = 1-(distance /100);
    			}else{ distance = 1;}
    			neurons.get(i).setOutputValue(distance);
    			//closetPowerPill
    			distance = search.findPowerPillDistances(block,game.getGrid());
    			if (distance > 0){
    				distance = 1-(distance /100);
    			}else{ distance = 1;}
    			neurons.get(i+1).setOutputValue(distance);
    			
    			//ghost distance
    			agentDistances = search.findAgentDistances(game.getGhosts(), block);
    			if (agent.isScared() == true){
    				for (int j = 0; j < 5; j++){
    					distance = agentDistances.get(j);
    					if (distance != 0 ){distance = distance/100;}
    					neurons.get(i+2+j).setOutputValue(distance);
    				}
    			}else{
    				for (int j = 0; j < 5; j++){
    					distance = agentDistances.get(j);
    					if (distance == 0 ){distance = 1;}
    					else {distance = 1-(distance/100);}
    					neurons.get(i+2+j).setOutputValue(distance);
    				}
    			}
    		}else{
    			for (int j = 0; j < 7; j++){
    				neurons.get(i+j).setOutputValue(0);
    			}
    		}
    		index++;
    		if (index == 4){index = 0;}
    	}
    }
    
    public void removeOwnDistance (List <Double> agentDistances ){
		int count = 0;
		double lowest = agentDistances.get(0);
		for (int j = 0; j < agentDistances.size(); j++){
			if (agentDistances.get(j) < lowest){
				lowest = agentDistances.get(j);
				count ++;
			}
		}
		agentDistances.remove(count);
    }
    
    public void setGhostInput (Game game,GenericAgent agent, NeuralLayer inputLayer){

		List <Neuron> neurons = inputLayer.getNeurons();
		List <Block> blocks = getAdjacentBlocks (agent.getLocation(), game.getGrid());
		List <Double> agentDistances = null;
		double distance =0.0;
		
		int index = 0;
		for (int i = 0; i < inputLayer.getNeurons().size();i++){ 
			Block block = blocks.get(index);
			if (block instanceof Road){
				agentDistances = search.findAgentDistances(game.getPacmen(), block);
				distance = agentDistances.get(0);
				for (double val : agentDistances){if (val < distance){distance = val;}}
				
				if (agent.isScared() == false){
					if (distance > 0){distance = 1-(distance /100);}
					else{ distance = 10;}
				}else {distance = distance/100;}
				neurons.get(i).setOutputValue(distance);
			}else{
				neurons.get(i).setOutputValue(-1);
			}
			index++;
		}
    }

}
