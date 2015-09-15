package PacmanGrid;
import java.util.Arrays;
import Utils.IntDimension;
import javafx.event.EventHandler;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.FlowPane;
import javafx.scene.paint.Color;

public class Grid extends FlowPane {

	private IntDimension blockDimensions;
	private int numberOfBlocks;
	private Block[] blocks;
	
	public Grid (IntDimension dimension){
		blockDimensions = dimension;
		numberOfBlocks = blockDimensions.getX() * blockDimensions.getY();
		blocks = new Block [this.numberOfBlocks];
		
		this.setPrefHeight(Block.getPixeldimensions().getY() * dimension.getY());
		this.setPrefWidth(Block.getPixeldimensions().getX() * dimension.getX());
		this.setMaxSize(this.getPrefWidth(), this.getPrefHeight());
		this.setMinSize(this.getPrefWidth(), this.getPrefHeight());
	}
	
	public Block getBlock (IntDimension dimension){
		return blocks[coordinateToGridNumber(dimension) ];
	}
	
	public Block[] getBlocks (IntDimension startDimension, IntDimension endDimension){
		int startElement = coordinateToGridNumber (startDimension);
		int endElement = coordinateToGridNumber (endDimension);
		Block [] output = Arrays.copyOfRange(blocks, startElement, endElement);
		return (output);
	}
	
	public Block[] getBlocks (){
		return blocks;
	}
	
	public void setBlocks (Block[] blocks,IntDimension dimension){
		this.blocks = blocks;
		blockDimensions = dimension;
		numberOfBlocks = blockDimensions.getX() * blockDimensions.getY();
		blocks = new Block [this.numberOfBlocks];
	}
	
	
	public void addBlock (Block block, IntDimension position){
		block.setGridPosition( position);
		block.setGridNumber (coordinateToGridNumber (position) );
		blocks[block.getGridNumber()] = block;
		this.getChildren().add(block);
	}
	

	private int coordinateToGridNumber (IntDimension dimension){
		int arrayElementNumber = dimension.getX() * blockDimensions.getX() - blockDimensions.getX() + dimension.getY() ;
		return (arrayElementNumber);
	}
	
	
	public IntDimension getBlockDimensions() {
		return blockDimensions;
	}

	public int getNumberOfBlocks() {
		return numberOfBlocks;
	}

	
}
