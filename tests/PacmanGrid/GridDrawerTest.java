package PacmanGrid;

import org.junit.Test;
import static org.junit.Assert.*;

import Utils.IntDimension;

public class GridDrawerTest {
	
	private GridDrawer unit = new GridDrawer (new IntDimension (10,10));
	
	
	private void saveTemplateTest (){
		unit.saveTemplate("C:\\Users\\GMAN\\Desktop\\Temp Stuff\\pacman.grid");
	}
	
	@Test
	public void startGridDrawerTest (){
		unit.startGridDrawer();
		assertEquals(unit.getBlocks().size(), 100);
	}

}
