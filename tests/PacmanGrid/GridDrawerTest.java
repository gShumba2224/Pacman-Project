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
			 grid = GridDrawer.drawFromImage(new File ("C:\\Users\\GMAN\\Desktop\\Temp Stuff\\testGrid.jpg"), new IntDimension (200,200));
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		int numWalls = 0 ; int numRoad1 = 0; int numRoad2 = 0; int numRoad3 = 0;
		for (Block block: grid.getBlocks()){
			try{
				block = (Wall)block;
				numWalls ++;
			}catch (ClassCastException e){
				if ( ((Road)block ).getPill() == Pill.GRAPE ) {numRoad1++;}
				else if ( ((Road)block ).getPill() == Pill.POWERPILL ) {numRoad2++;}
				else if  ( ((Road)block ).getPill() == Pill.STANDARDPILL ) numRoad3++;
			}
		}
		assertEquals(grid.getBlockDimensions().getX(), 3);
		assertEquals(grid.getBlockDimensions().getY(), 3);
		assertEquals(grid.getNumberOfBlocks(), 9);
		assertEquals (grid.getBlocks().size(),9);
		
		assertEquals(numWalls, 3);
		assertEquals(numRoad1, 2);
		assertEquals(numRoad2, 2);
		assertEquals(numRoad3, 2);
		
		for (int x = 0 ; x < 3 ; x++){
			for (int y = 0; y < 3 ; y++){
				IntDimension gridPos = new IntDimension (x,y);
				assertEquals(grid.getBlock(gridPos).getGridPosition().getX(), gridPos.getX());
				assertEquals(grid.getBlock(gridPos).getGridNumber(), x * 3 + y);
			}
		}
		
		
	}

}