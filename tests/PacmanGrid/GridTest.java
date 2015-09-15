package PacmanGrid;


import static org.junit.Assert.*;

import org.junit.Test;

import Utils.IntDimension;

public class GridTest {

	private Grid unit = new Grid ( new IntDimension (10,10));
	@Test
	public void addBlock() {
		
		Block block = new Wall ();
		unit.addBlock(block, new IntDimension (2,1));
		Block[] blocks = unit.getBlocks();
		int blockPos = 2 * 10 - 10 + 1;
		assertSame(block, blocks[blockPos]);
		assertEquals(block.getGridNumber(),blockPos);
	}

}
