package AI;

import static org.junit.Assert.*;

import java.io.IOException;
import java.util.ArrayList;

import org.junit.Test;

import Agents.GenericAgent;
import Agents.Ghost;
import Agents.Pacman;

import static org.mockito.Mockito.*;

import Game.Game;
import Neurons.NeuralNetwork;
import Neurons.Neuron;
import PacmanGrid.Block;
import PacmanGrid.Grid;
import PacmanGrid.Pill;
import PacmanGrid.Road;
import PacmanGrid.Wall;
import Utils.IntDimension;

public class InputReaderTest {

	InputReader unit ;
	@Test
	public void test() throws IOException {
		
		Game game = mock(Game.class);
		Grid grid = mock(Grid.class);
		doNothing().when(grid).Grid(any (IntDimension.class), any (IntDimension.class));
		grid = new Grid(new IntDimension (15,15), new IntDimension(10, 10));
		ArrayList <Block>blocks = new ArrayList <Block> (15*15);
		for (int i = 0 ; i < 15*15 ; i ++){
			blocks.add(new Road ( new IntDimension(10, 10), Pill.NONE));
		}
		grid.setBlocks(blocks, new IntDimension (15,15));
		
		Wall topWall = new Wall(new IntDimension (10,10));
		topWall.setGridPosition(new IntDimension (10,9));
		grid.addBlock(topWall, topWall.getGridPosition());
		
		Road bottomRoad = new Road (new IntDimension (10,10),Pill.GRAPE);
		bottomRoad.setGridPosition(new IntDimension (10,11));
		grid.addBlock(bottomRoad, bottomRoad.getGridPosition());
		
		Road leftRoad = new Road (new IntDimension (10,10),Pill.POWERPILL);
		leftRoad.setGridPosition(new IntDimension (9,10));
		grid.addBlock(leftRoad, leftRoad.getGridPosition());
		
		Road rightRoad = new Road (new IntDimension (10,10),Pill.STANDARDPILL);
		rightRoad.setGridPosition(new IntDimension (11,10));
		grid.addBlock(rightRoad, rightRoad.getGridPosition());
		
		GenericAgent pacman = new Pacman ();
		pacman.setLocation(new IntDimension (10,10));
		
		NeuralNetwork network = new NeuralNetwork(13, 2, 1, 4);
		GenericAgent ghost = new Ghost ();
		ghost.setLocation(new IntDimension(10, 10));
		
		when (game.getGrid()).thenReturn(grid);
		//unit = new InputReader(game);
		//unit.readInputs(network,pacman,game);
		System.out.println(blocks.size());
		System.out.println(grid.getBlock(new IntDimension(10,10)));
		
//		for (Neuron neuron : network.getInputLayer().getNeurons()){
//			System.out.println(neuron.getOutputValue());
//		}
		
	}

}
