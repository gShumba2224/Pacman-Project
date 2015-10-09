package Search;

import PacmanGrid.Block;

public class Container {
	public Block from = null;
	public Block to = null;
	public double distance = 0.0;
	
	public Container (){}
	public Container (Block from, Block to, double distance){
		this.from = from;
		this.to = to;
		this.distance = distance;
	}
}