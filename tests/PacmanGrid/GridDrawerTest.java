package PacmanGrid;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.io.File;
import java.io.IOException;

import org.junit.Before;

import Utils.IntDimension;


public class GridDrawerTest{
  
	@Test
	public void drawFromImageTest (){
		Grid grid = null;
		try {
			 grid = GridDrawer.drawFromImage(new File ("C:\\Users\\GMAN\\Desktop\\Temp Stuff\\testGrid.jpg"));
		} catch (IOException e) {
			e.printStackTrace();
		}
		assertEquals(grid.getBlockDimensions().getX(), 3);
		assertEquals(grid.getBlockDimensions().getY(), 3);
		assertEquals(grid.getNumberOfBlocks(), 9);
		assertEquals (grid.getBlocks().size(),9);
		for (Block block: grid.getBlocks()){
			assertTrue(block.getClass().equals(Wall.class) || block.getClass().equals(Road.class));
		}
		
		
		
	}

}