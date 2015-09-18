package PacmanGrid;


import static org.junit.Assert.*;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import org.junit.Before;
import org.junit.Test;

import Utils.IntDimension;

public class GridTest {

	private Grid unit;
	
	@Before
	public void generateGrid (){
		try {
			unit = GridDrawer.drawFromImage(new File ("C:\\Users\\GMAN\\Desktop\\Temp Stuff\\testGrid.jpg"));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	@Test
	public void addBlockTest() {
		unit = new Grid (new IntDimension (3,3), new IntDimension (200,200));
		unit.addBlock(new Block (), new IntDimension (0,0));
		unit.addBlock(new Block (), new IntDimension (0,1));
		
		IntDimension b1 = new IntDimension (0,0);
		assertEquals( unit.getBlock(b1).getGridNumber(), 0);
		assertEquals(unit.getBlock(b1).getGridPosition().getX(), 0);
		assertEquals(unit.getBlock(b1).getGridPosition().getY(), 0);
		
		IntDimension b2 = new IntDimension (0,1);
		assertEquals( unit.getBlock(b2).getGridNumber(), 1);
		assertEquals(unit.getBlock(b2).getGridPosition().getX(), 0);
		assertEquals(unit.getBlock(b2).getGridPosition().getY(), 1);
	}
	
	@Test
	public void getBlockTest (){
		Block block;
		
		IntDimension b1 = new IntDimension (0,0);
		block = unit.getBlock(b1);
		assertEquals(block.getGridNumber(), 0);
	
		
		block = unit.getBlock(new IntDimension (0,1));
		assertEquals(block.getGridNumber(), 1);
		System.out.println("sdsdfds  " + block.getGridPosition().getX());
	}


}
