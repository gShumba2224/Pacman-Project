package PacmanGrid;
import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import Utils.IntDimension;
import javafx.event.EventHandler;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.TilePane;
import javafx.scene.paint.Color;

public class Grid extends Canvas implements Serializable {
	
	private IntDimension blockDimensions;
	private int numberOfBlocks;
	private ArrayList<Block> blocks;
	
	public Grid (IntDimension dimension){
		
		super (dimension.getX() * Block.getPixelDimensions().getX() ,dimension.getY() * Block.getPixelDimensions().getY());
		blockDimensions = dimension;
		numberOfBlocks = blockDimensions.getX() * blockDimensions.getY();
		blocks = new ArrayList <Block> (numberOfBlocks);
		
//		GraphicsContext gc = this.getGraphicsContext2D();
//		gc.setFill(Color.RED);
//		gc.fillRect(0, 0, this.getWidth(), this.getHeight());
//		int x = 0;
//		int y = 0;
//		int count = 0;
//		for (int row = 0; row < 600; row = row + 200 ){
//			
//			for (int column = 0; column < 600 ; column = column + 200){
//				
//				if (y > 2) y = 0;
//				gc.setFill(Color.BLUE);
//				gc.setStroke(Color.BLACK);
//				gc.setLineWidth(1.0);
//				int num = x * 3 + y ;
//				gc.fillText(String.valueOf(coordinateToGridNumber(new IntDimension (x,y))), row + 100, column + 100);
//				gc.strokeRect(row, column, Block.getPixelDimensions().getX() , Block.getPixelDimensions().getY());
//				y++;
//				count ++;
//			}
//			x ++;
//			
//		}
//		System.out.println(count + "count");
		
//		this.setPrefHeight(Block.getPixeldimensions().getY() * dimension.getY());
//		this.setPrefWidth(Block.getPixeldimensions().getX() * dimension.getX());
//		this.setMaxSize(this.getPrefWidth(), this.getPrefHeight());
//		this.setMinSize(this.getPrefWidth(), this.getPrefHeight());
	}
	
	public Block getBlock (IntDimension dimension){
		return blocks.get( coordinateToGridNumber(dimension) );
	}
	
	public ArrayList<Block> getBlocks (IntDimension startDimension, IntDimension endDimension){
		int startElement = coordinateToGridNumber (startDimension);
		int endElement = coordinateToGridNumber (endDimension);
		return (ArrayList<Block>) (blocks.subList(startElement, endElement));
	}
	
	public ArrayList <Block> getBlocks (){
		return blocks;
	}
	
	public void setBlocks (ArrayList <Block> blocks,IntDimension dimension){
		this.blocks = blocks;
		blockDimensions = dimension;
		numberOfBlocks = blockDimensions.getX() * blockDimensions.getY();
	}
	
	
	public void addBlock (Block block, IntDimension position){
		block.setGridPosition( position);
		block.setGridNumber (coordinateToGridNumber (position) );
		blocks.add(block.getGridNumber(), block);
		//this.getChildren().add(block);
		
	}

	protected int coordinateToGridNumber (IntDimension dimension){
		int arrayElementNumber = dimension.getX() * blockDimensions.getX() + dimension.getY() ;
//		int arrayElementNumber;
//		if (dimension.getX() == 0&& dimension.getY() == 0){
//			arrayElementNumber = 0;
//		}else{
//			arrayElementNumber = dimension.getX() * blockDimensions.getX() - blockDimensions.getX() + dimension.getY() ;
//		}
		return (arrayElementNumber);
	}
	
	
	
	public IntDimension getBlockDimensions() {
		return blockDimensions;
	}

	public int getNumberOfBlocks() {
		return numberOfBlocks;
	}
}
