package Search;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import PacmanGrid.Block;
import PacmanGrid.Grid;
import PacmanGrid.Road;
import PacmanGrid.Wall;
import Utils.DistanceCalculator;
import Utils.IntDimension;

public class A_StarSearch {
	
	private List <A_StarNode> closed = new ArrayList<A_StarNode> ();
	private Map <String,A_StarNode> nodes = new HashMap<String,A_StarNode> ();
	private Map <String,A_StarNode> open = new HashMap<String,A_StarNode> ();
	public static final int MOVE = 10;
	private int maxIterations = 10000;
	private int currentIterations = 0;
	private static A_StarNode defaultParent = new A_StarNode();
	
	public A_StarSearch (Grid grid){
		createNodeList(grid);
	}
	
	private void createNodeList (Grid grid){
		for (Block block : grid.getBlocks()){
			String key = String.valueOf(block.getGridPosition().X + ","
						+ String.valueOf( block.getGridPosition().Y) );
			A_StarNode node = new A_StarNode();
			node.block = block;
			nodes.put(key, node);
		}
	}
	
	private void resetNodeValues (){
		open.clear();
		for (A_StarNode node : nodes.values()){
			node.f_score = 0;
			node.g_score = 0;
			node.g_score = 0;
			node.isClosed = false;
			node.isInOpen = false;
			node.parent = defaultParent;
		}
	}
	
	private List<A_StarNode> getAdjacencies (IntDimension location){
		List<A_StarNode> adjacencies = new ArrayList <A_StarNode> ();
		int x = location.X; 
		int y = location.Y;
		A_StarNode node;
		
		node = nodes.get(String.valueOf( x-1) + "," + String.valueOf(y) ); //LEFT
		if (node.isClosed == false && node.block instanceof Road)adjacencies.add(node);
		
		node = nodes.get(String.valueOf( x+1) + "," + String.valueOf(y) ); //RIGHT
		if (node.isClosed == false && node.block instanceof Road)adjacencies.add(node);
		
		node = nodes.get(String.valueOf( x) + "," + String.valueOf(y-1) ); //TOP
		if (node.isClosed == false && node.block instanceof Road)adjacencies.add(node);
		
		node = nodes.get(String.valueOf( x) + "," + String.valueOf(y+1) ); //BOTTOM
		if (node.isClosed == false && node.block instanceof Road)adjacencies.add(node);
		
		return adjacencies;
	}
	
	private A_StarNode findLowestF (){
		Iterator<Entry<String, A_StarNode>> it = open.entrySet().iterator();
		A_StarNode lowestF= it.next().getValue();
		A_StarNode node;
		
		while (it.hasNext()){
			Entry<String,A_StarNode> entry = (Entry<String, A_StarNode>) it.next();
			node = entry.getValue();
			if (node.f_score < lowestF.f_score){
				lowestF = node;
			}
		}
		return (lowestF);
	}
	
	public static double stepToGoal (A_StarNode node){
		int steps = -1;
		while (!node.equals(defaultParent)){
			node = node.parent;
			steps++;
		}
		return steps*MOVE;
	}
	
	public A_StarNode beginSearch (IntDimension start, IntDimension end){
		
		resetNodeValues();
		String startSring = start.X+","+start.Y;
		String endString = end.X+","+end.Y;
		A_StarNode startNode = nodes.get(startSring);
		A_StarNode endNode = nodes.get(endString);
		
		open.put(startSring,startNode);
		startNode.isInOpen = true;
		
		startNode.h_score = DistanceCalculator.manhattanDistance(startNode.block.getGridPosition(), 
							endNode.block.getGridPosition());
		startNode.g_score = 0;
		startNode.f_score = startNode.g_score + startNode.h_score;
		
		A_StarNode goalNode =  search (startNode,endNode);
		currentIterations = 0;
		return goalNode;
	}
	
	private A_StarNode search(A_StarNode current, A_StarNode goal){
		List <A_StarNode> adjacencies = getAdjacencies (current.block.getGridPosition());
		for (A_StarNode node : adjacencies){
			if (node.isInOpen == false) {
				
				if (node.equals(goal) || current.equals(goal)){
					node.parent = current;
					return goal;
				}
				
				node.h_score = DistanceCalculator.manhattanDistance(node.block.getGridPosition(), 
								goal.block.getGridPosition());
				node.g_score = current.g_score + MOVE;
				node.f_score = node.g_score + node.h_score;
				node.parent = current;
				open.put(node.block.getGridPosition().X+","+
							node.block.getGridPosition().Y,node);
				node.isInOpen = true;
			}else{
				if (node.g_score > (MOVE + current.g_score)){
					node.parent = current;
					node.g_score = MOVE + current.g_score;
					node.f_score = node.g_score + node.h_score;
				}
			}
		}
		
		current.isClosed = true;
		current.isInOpen = false;
		open.remove(current.block.getGridPosition().X+","+
				current.block.getGridPosition().Y);
		if (currentIterations < maxIterations){
			currentIterations ++;
			search(findLowestF(), goal);
		}//else {return null;}
		return goal;
	}
	
}
