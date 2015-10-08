package Search;

import PacmanGrid.Block;

public class A_StarNode {
	
	public int f_score = 0;
	public int g_score = 0;
	public int h_score = 0;
	public boolean isClosed = false;
	public boolean isInOpen = false;
	public A_StarNode parent = null;
	public Block block = null;	
}
