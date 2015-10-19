package Search;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.hamcrest.core.IsInstanceOf;

import Agents.GenericAgent;
import PacmanGrid.Block;
import PacmanGrid.Grid;
import PacmanGrid.Pill;
import PacmanGrid.Road;
import PacmanGrid.Wall;
import Utils.DistanceCalculator;
import Utils.IntDimension;
import sun.net.www.content.text.Generic;

public class ArtifactSearch {
	
	private A_StarSearch aStar = null;
	private Grid grid = null;
	
	public  ArtifactSearch (Grid grid){
		aStar = new A_StarSearch(grid);
		this.grid = grid;
	}
	
	private List <Block> getValidBlocks (Grid grid, IntDimension start){
		int x = start.X;
		int y = start.Y;
		List <Block> validBlocks = new ArrayList<Block>();
		Block block;
		
		block = grid.getBlock(new IntDimension(x-1, y));
		if (block instanceof Road)validBlocks.add(block);
		
		
		block = grid.getBlock(new IntDimension(x+1, y));
		if (block instanceof Road)validBlocks.add(block);
	
		block = grid.getBlock(new IntDimension(x, y-1));
		if (block instanceof Road)validBlocks.add(block);
		
		block = grid.getBlock(new IntDimension(x, y+1));
		if (block instanceof Road)validBlocks.add(block);
		
		return validBlocks;
		
	}
	
	private IntDimension[] growBounds (Grid grid,List <Block> storageList,int maxIterations,
										IntDimension min, IntDimension max){
		
		IntDimension newMin = new IntDimension(min.X-1, min.Y-1);
		IntDimension newMax = new IntDimension (max.X+1,max.Y+1);
		
		if (newMin.X < 0){newMin.X = 0;}
		if (newMin.Y < 0){newMin.Y = 0;}
		if (newMax.X > 14){newMax.X = 14;}
		if (newMax.Y > 14){newMax.Y = 14;}
		
		Block block;
		
		for (int i = newMin.X; i <= newMax.X && i < 15; i++){
			block = grid.getBlock(new IntDimension(i, newMin.Y));
			if (block instanceof Road && ((Road)block).getPill() == Pill.STANDARDPILL)storageList.add(block);
			
			block = grid.getBlock(new IntDimension(i, newMax.Y));
			if (block instanceof Road && ((Road)block).getPill() == Pill.STANDARDPILL)storageList.add(block);
		}
		
		for (int i = newMin.Y+1; i <= newMax.Y-1 && i < 15; i++){
			block = grid.getBlock(new IntDimension(newMin.X, i));
			if (block instanceof Road && ((Road)block).getPill() == Pill.STANDARDPILL)storageList.add(block);
			block = grid.getBlock(new IntDimension(newMax.X, i));
			if (block instanceof Road && ((Road)block).getPill() == Pill.STANDARDPILL)storageList.add(block);
		}
		
		maxIterations = maxIterations - 1;
		if (storageList.size() == 0 && maxIterations > 0){
			growBounds ( grid,storageList,maxIterations, newMin, newMax);
		}
		return new IntDimension[] {newMin,newMax};
	}
	
	public Map <Integer,Container>  findNearestPill (Grid grid, IntDimension start, int maxIterations){
		
		List <Block> moveToBlocks = getValidBlocks(grid, start);
		List <Block> pillBlocks = new ArrayList <Block>();
		Map <Integer,Container> distanceMap = new HashMap<Integer,Container>();
		
		growBounds ( grid,pillBlocks,maxIterations,start, start);
		if (pillBlocks.size() == 0) {return null;}
	
		for (Block block : pillBlocks){
			for (Block moveToBlock : moveToBlocks){
				double distance = DistanceCalculator.manhattanDistance(moveToBlock.getGridPosition(),
									block.getGridPosition());
				
				if (distanceMap.containsKey(moveToBlock.getGridNumber())== true &&
				distance < distanceMap.get(moveToBlock.getGridNumber()).distance){
					 distanceMap.get(moveToBlock.getGridNumber()).to = block;
					 distanceMap.get(moveToBlock.getGridNumber()).distance = distance;
					 
				}else if (distanceMap.containsKey(moveToBlock.getGridNumber())== false){
					Container container = new Container(moveToBlock, block, distance);
					distanceMap.put(moveToBlock.getGridNumber(), container);
				}
			}
		}
		
		getExactDistance (distanceMap);
		return distanceMap;
	}
	
	public void getExactDistance (Map <Integer,Container> distanceMap ){
		Iterator <Entry<Integer,Container>> it = distanceMap.entrySet().iterator();
		while (it.hasNext()){
			Entry<Integer, Container> entry = it.next();
			
			if (entry.getValue().from.equals(entry.getValue().to) == true){
				entry.getValue().distance = 0;
			}else{
				A_StarNode node = aStar.beginSearch(entry.getValue().from.getGridPosition(), 
									entry.getValue().to.getGridPosition());
				
				if (node == null)entry.getValue().distance = entry.getValue().distance * A_StarSearch.MOVE;
				entry.getValue().distance = A_StarSearch.stepToGoal(node);
			}
		}
	}
	
	public List<Double> findAgentDistances (List<GenericAgent> agents, Block from){
		List<Double> distanceMap = new ArrayList <Double> ();
		for (GenericAgent agent : agents){
			double distance = 0.0;
			A_StarNode node = aStar.beginSearch(from.getGridPosition(), agent.getLocation());
			if (node == null){
				distance = DistanceCalculator.manhattanDistance(from.getGridPosition(),
								agent.getLocation())*A_StarSearch.MOVE;
			}else{distance = A_StarSearch.stepToGoal(node);}
			distanceMap.add(distance);
		}
		return distanceMap;
	}
	
	public double findPowerPillDistances(Block block,Grid grid){
		IntDimension[] powerPills = {new IntDimension(1, 1), new IntDimension(13, 1),
									new IntDimension(1, 13), new IntDimension(13, 13)};
		
		double lowestDist = -1.0;
		for (int i = 0; i < powerPills.length; i++){
			double distance = 0.0;
			if ( ((Road )grid.getBlock(powerPills[i])).getPill() != Pill.NONE){
				A_StarNode node = aStar.beginSearch(powerPills[i], block.getGridPosition());
				if (node == null){
					distance = DistanceCalculator.manhattanDistance(powerPills[i],
							 block.getGridPosition())*A_StarSearch.MOVE;
				}else{distance = A_StarSearch.stepToGoal(node);}
				if (distance < lowestDist || lowestDist == -1){lowestDist = distance;}
			}else {distance = A_StarSearch.MOVE * 20;}
		}
		return lowestDist;
	}
}

