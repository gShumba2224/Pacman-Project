package Game;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import Agents.GenericAgent;
import Agents.Ghost;
import Agents.Pacman;
import PacmanGrid.Block;
import PacmanGrid.Grid;
import PacmanGrid.Road;
import Utils.IntDimension;


class Positions {
	public Block pos1;
	public Block pos2;
	public int count = 0;
}

public class UnstickAgent {
	private Game game;
	private int STUCK_LIMIT = 6;
	
	private Map  <GenericAgent,Positions> agentPosMap = new HashMap <GenericAgent,Positions> ();
	
	
	public UnstickAgent (Game game){
		this.game = game;
		addToMap(game.getPacmen());
		addToMap(game.getGhosts());
	}

	private void addToMap (List <GenericAgent> agents){
		for (GenericAgent agent : agents){
			agentPosMap.put(agent, new Positions ());
		}
	}
	public Boolean checkIfStuck (GenericAgent agent){
		
		if (agentPosMap.get(agent).pos1 == null){
			agentPosMap.get(agent).pos1 = game.getGrid().getBlock(agent.getLocation());
			return false;
			
		}else if (agentPosMap.get(agent).pos2 == null){
			agentPosMap.get(agent).pos2 = game.getGrid().getBlock(agent.getLocation());
			return false;
		}
		Block currentBlock = game.getGrid().getBlock(agent.getLocation());
		Positions positions = agentPosMap.get(agent);
		if (currentBlock.equals(positions.pos1) || currentBlock.equals(positions.pos2)){
			positions.count++;
			if (positions.count >= STUCK_LIMIT){
				unstickAgent (agent);
				return true;
		}
		}else{
			positions.count = 0;
			return false;
		}
		return false;

	}

	private void unstickAgent(GenericAgent agent) {
		Positions positions = agentPosMap.get(agent);
		List <Block> blocks = game.getGrid().getAdjacentBlocks(agent.getLocation());
		Random rand = new Random ();
		boolean found = false;
		Block block = null;
		while (found == false){
			if (blocks.size() > 1){
				block = blocks.get(rand.nextInt(blocks.size()));
				if (block instanceof Road && block.equals(positions.pos1)==false
								&&  block.equals(positions.pos2) == false){
					found = true;
				}else{
					blocks.remove(block);
				}
			}else{block = blocks.get(0);}
		}
		Move.moveAgent(agent, game, block.getGridPosition());
	}

}
