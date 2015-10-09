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
	
	ArtifactSearch search ;

	public Game game = null;
	public InputReader (Game game){
		this.game = game;
	}
	
	private List<Block> getAdjacentBlocks (IntDimension centre, Grid grid){
		
		List <Block> adjacentBlocks = new ArrayList<Block>();
		Block block;
		int x = centre.X;
		int y = centre.Y;
		
		block = grid.getBlock(new IntDimension(x-1, y)); //left
		adjacentBlocks.add(block);
		
		block = grid.getBlock(new IntDimension(x+1, y)); //right
		adjacentBlocks.add(block);
		
		block = grid.getBlock(new IntDimension(x, y)); //top
		adjacentBlocks.add(block);
		
		block = grid.getBlock(new IntDimension(x, y-1)); //bottom
		adjacentBlocks.add(block);
	
		return adjacentBlocks;
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
    
    

    
    public void setAgentInput (Block block,GenericAgent agent, NeuralLayer inputLayer, int startIndex){
    	if (agent  instanceof Pacman){
    		setPacmanInput (block, agent,  inputLayer, startIndex);
    	}else {
    		setGhostInput (block, agent,  inputLayer, startIndex);
    	}
    }
	
    public void setPacmanInput (Block block,GenericAgent agent, NeuralLayer inputLayer, int startIndex){
		Map<Integer,Container> pillDistances = search.findNearestPill(game.getGrid(), block.getGridPosition(), 1000);
    	
		List <Neuron> neurons = inputLayer.getNeurons();
    	for (int i = 0; i < inputLayer.getNeurons().size();i = i+ 8){
    		if ( block instanceof Road){// {neurons.get(i).setOutputValue(-1);}
    			neurons.get(i).setOutputValue(1);
    			double pillDist = pillDistances.get(block.getGridNumber()).distance;
    			if (pillDist == 0){neurons.get(i+1).setOutputValue(1);}
    			else{
    				pillDist = 1-(pillDist /100);
    				neurons.get(i+1).setOutputValue(pillDist);}
    			pillDist = search.findPowerPillDistances(block);
    		}
    	}
    		
    		
    	
    }
    
    public void setGhostInput (Block block,GenericAgent agent, NeuralLayer inputLayer, int startIndex){

    }

}
