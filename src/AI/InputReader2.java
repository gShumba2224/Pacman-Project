package AI;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;

import Agents.GenericAgent;
import Game.Game;
import Neurons.NeuralLayer;
import PacmanGrid.Block;
import PacmanGrid.Road;
import Search.Container;
import Utils.IntDimension;

public class InputReader2 extends InputReader{
	
	private Block safeBlock = null;
	private Block pointBlock = null;
	private final double MAXDIST = 200;

	public InputReader2(Game game) {
		super(game);
	}
	
	public double normalizeDist (double distance, boolean doInvert){
		if (distance != 0){distance = distance/MAXDIST;}
		if (doInvert == true) {distance = 1 - distance;}
		return distance;
	}
	
	public void setBestBlocks (Map<Integer,Container> pillDistances, GenericAgent agent){
		List <Block> adjacentBlocks = game.getGrid().getAdjacentBlocks(agent.getLocation());
		List <Double> agentDistances = null;
		Block block;
		safeBlock = null;
		pointBlock = null;
		Random rand = new Random ();
		
		double safeBest = 0.0;
		double pointBest = 0.0;
		
		for (int i = 0; i < adjacentBlocks.size(); i++){
			 block =  adjacentBlocks.get(i);
			if (block instanceof Road){
				agentDistances = search.findAgentDistances(game.getGhosts(), block);
				double safeScore = MAXDIST;
				if (agentDistances.size() != 0){ safeScore = findClosestEnemy(agentDistances);}
				
				double pointScore = MAXDIST;
				Container container = pillDistances.get(block.getGridNumber());
				if (container!= null){
					pointScore = container.distance + search.findPowerPillDistances(block,game.getGrid());
					if (agent.isScared() == false){
						pointScore = pointScore + safeScore;
					}
				}
				if (safeBlock == null || safeScore > safeBest ){
					safeBlock = block;
					safeBest = safeScore;
				}else if (safeScore == safeBest){
					if (rand.nextDouble() >= 0.5){safeBlock = block;}
				}
				if (pointBlock == null || pointScore < pointBest ){
					pointBlock = block;
					pointBest = pointScore;
				}else if (pointScore == pointBest){
					if (rand.nextDouble() >= 0.5){pointBlock = block;}
				}
			}
		}
	}

	private double findClosestEnemy (List <Double> distanceList){
		double distance = distanceList.get(0);
		for (Double val: distanceList){if (val < distance){distance = val;}}
		return distance;
	}
	
  public void setPacmanInput (Game game,GenericAgent agent, NeuralLayer inputLayer){
		Map<Integer,Container> pillDistances =search.findNearestPill(game.getGrid(), agent.getLocation(), 1000);
		Block  currentBlock = game.getGrid().getBlock(agent.getLocation());
		List <Double> agentDistances = search.findAgentDistances(game.getGhosts(), currentBlock);
		double distance = 0.0;
		
		Iterator <Entry<Integer,Container>> it = pillDistances.entrySet().iterator();
		distance = it.next().getValue().distance;
		while (it.hasNext() == true){
			double currentVal = it.next().getValue().distance ;
			if (currentVal < distance){distance = currentVal;}
		}
    	inputLayer.getNeurons().get(0).setOutputValue(normalizeDist(distance,false));
    	
    	distance = search.findPowerPillDistances(currentBlock,game.getGrid());
    	inputLayer.getNeurons().get(1).setOutputValue(normalizeDist(distance,false));
    	
    	if (agentDistances.size() != 0){
        	distance = agentDistances.get(0);
        	for (double currentVal : agentDistances){if (currentVal < distance){distance = currentVal;}} 
    	}else{distance = MAXDIST;}
    	
    	inputLayer.getNeurons().get(2).setOutputValue(normalizeDist(distance,false));

    	if (agent.isScared() == true){inputLayer.getNeurons().get(3).setOutputValue(-1);}
    	else{inputLayer.getNeurons().get(3).setOutputValue(1);}
    	setBestBlocks(pillDistances, agent);
    }

	public Block getSafeBlock() {
		return safeBlock;
	}
	
	public Block getPointBlock() {
		return pointBlock;
	}
	  
}
