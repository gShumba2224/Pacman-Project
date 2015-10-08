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
import Utils.IntDimension;

public class A_StarSearch {
	
	private List <A_StarNode> closed = new ArrayList<A_StarNode> ();
	private Map <String,A_StarNode> nodes = new HashMap<String,A_StarNode> ();
	private Map <String,A_StarNode> open = new HashMap<String,A_StarNode> ();
	private static final int MOVE = 10;
	private int maxIterations = 100;
	private int currentIterations = 0;
	
	public A_StarSearch (Grid grid){
		createNodeList(grid);
		beginSearch(new IntDimension (12,13), new IntDimension(6, 4));
//		for (Entry en : nodes.entrySet()){
//			A_StarNode n = (A_StarNode)en.getValue();
//			System.out.println("key =" + en.getKey() + " pos = " + n.block.getGridPosition().X +
//					"," +  n.block.getGridPosition().Y);
//		} 
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
	private int manhattanDistance (IntDimension from, IntDimension to){
		int x = Math.abs( from.X - to.X );
		int y =  Math.abs(from.Y - to.Y );
		return (x+y);
	}
	
	private List<A_StarNode> getAdjacencies (IntDimension location){
		List<A_StarNode> adjacencies = new ArrayList <A_StarNode> ();
		//String[] seperatedKey = location.split(",");
		int x = location.X; //Integer.valueOf(seperatedKey[0]);
		int y = location.Y;// Integer.valueOf(seperatedKey[1]);
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
	
	public void beginSearch (IntDimension start, IntDimension end){
		
		String startSring = start.X+","+start.Y;
		String endString = end.X+","+end.Y;
		A_StarNode startNode = nodes.get(startSring);
		A_StarNode endNode = nodes.get(endString);
		
		open.put(startSring,startNode);
		startNode.isInOpen = true;
		
		startNode.h_score = manhattanDistance(startNode.block.getGridPosition(), 
							endNode.block.getGridPosition());
		startNode.g_score = 0;
		startNode.f_score = startNode.g_score + startNode.h_score;
		System.out.println("START = "+ start.X+ ","+ start.Y);
		search (startNode,endNode);
	}
	
	private void search(A_StarNode current, A_StarNode goal){
		List <A_StarNode> adjacencies = getAdjacencies (current.block.getGridPosition());
		for (A_StarNode node : adjacencies){
			if (node.isInOpen == false) {
				if (node.equals(goal)){
					node.parent = current;
					boolean d = false;
					A_StarNode n = node;
					while (d == false){
						System.out.println("WOOOO = " + n.block.getGridPosition().X +
								","+ n.block.getGridPosition().Y);
						n = n.parent;
						if (n == null){
							d= true;
						}
					}
					return;
				}
				
				node.h_score = manhattanDistance(node.block.getGridPosition(), 
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
		open.remove(current.block.getGridPosition().X+","+
				current.block.getGridPosition().Y);
		if (currentIterations < maxIterations){currentIterations ++;}
		else {return;}
		System.out.println("iterations = " +currentIterations);
		search(findLowestF(), goal);

	}
	
}
