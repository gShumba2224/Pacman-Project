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

public class Grid  implements Serializable {
	
	private IntDimension blockDimensions;
	private int numberOfBlocks;
	private ArrayList<Block> blocks;
	private IntDimension blockPixelDimensions;
	private Canvas canvas;
	private int totalScores = 0;
	
	public Grid (IntDimension dimension , IntDimension blockSize){
		
		canvas = new Canvas (dimension.getX() * blockSize.getX() ,dimension.getY() * blockSize.getY());
		blockDimensions = dimension;
		numberOfBlocks = blockDimensions.getX() * blockDimensions.getY();
		blocks = new ArrayList <Block> (numberOfBlocks);
		blockPixelDimensions = blockSize;
	}
	
	public void resetGrid (){
		for (Block block : blocks){
			if (block instanceof Road){
				updateRoad( (Road)block, Pill.STANDARDPILL);
			}
		}
		updateRoad(((Road)this.getBlock(new IntDimension(1, 1))), Pill.POWERPILL);
		updateRoad(((Road)this.getBlock(new IntDimension(13, 1))), Pill.POWERPILL);
		updateRoad(((Road)this.getBlock(new IntDimension(13, 13))), Pill.POWERPILL);
		updateRoad(((Road)this.getBlock(new IntDimension(1, 13))), Pill.POWERPILL);
	}
	
	public void updateRoad (Road road, int pill){
		road.setPill(pill);
		drawPills (road);
	}

	public void drawPills (){
		Road road;
		for (Block block : blocks){
			try{
				road = (Road) block;
				drawPills(road);
			}catch (ClassCastException e){}
		}
	}
	
	public void drawPills(Road road){
		GraphicsContext gc = canvas.getGraphicsContext2D();
		IntDimension pillScale;
		IntDimension startPos;
		pillScale = new IntDimension ((int)(blockPixelDimensions.X*0.2), (int)(blockPixelDimensions.Y*0.2));
		startPos =new IntDimension(
				(road.getGridPosition().X*blockPixelDimensions.X)+(blockPixelDimensions.X/2)-(pillScale.X/2),
				(road.getGridPosition().Y*blockPixelDimensions.Y)+(blockPixelDimensions.Y/2)-(pillScale.Y/2));
		
		if (road.getPill() == Pill.STANDARDPILL){gc.setFill(Color.WHITE);}
		else if (road.getPill() == Pill.POWERPILL){gc.setFill(Color.RED);}
		else if (road.getPill() == Pill.GRAPE){gc.setFill(Color.GREEN);}
		else if (road.getPill() == Pill.NONE){gc.setFill(Color.BLUE);}
		gc.fillOval(startPos.X, startPos.Y,pillScale.X, pillScale.Y);
	}

	public void drawGridExtras (){
		GraphicsContext gc = canvas.getGraphicsContext2D();
		gc.setFill(Color.PINK);
		gc.setStroke(Color.PINK);
		gc.setLineWidth(0.1);
		
		for (Block block : blocks){
			IntDimension startPos = new IntDimension (0,0);
			startPos.X = block.getGridPosition().X * blockPixelDimensions.X;
			startPos.Y = block.getGridPosition().Y * blockPixelDimensions.Y;
			gc.strokeRect(startPos.X, startPos.Y, blockPixelDimensions.X, blockPixelDimensions.Y);
			gc.strokeText(block.getGridPosition().X + "," + block.getGridPosition().Y, 
					startPos.X+10, startPos.Y+10);
		}
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
		block.setGridPosition( new IntDimension (position.getX(), position.getY()));
		block.setGridNumber (coordinateToGridNumber (position) );
		blocks.add(block.getGridNumber(), block);
	}

	protected int coordinateToGridNumber (IntDimension dimension){
		int arrayElementNumber = dimension.getX() * blockDimensions.getX() + dimension.getY() ;
		return (arrayElementNumber);
	}
	
	public IntDimension getBlockDimensions() {
		return blockDimensions;
	}

	public int getNumberOfBlocks() {
		return numberOfBlocks;
	}
	
	
	public Canvas getCanvas (){
		return canvas;
	}
	
	public void  set (Canvas canvas){
		 this.canvas = canvas;
	}
	
	public void setBackgroundImage (Image image){
		GraphicsContext gc = canvas.getGraphicsContext2D();
		gc.drawImage(image, 0, 0);
	}
	
	public IntDimension getBlockPixelDimensions() {
		return blockPixelDimensions;
	}

	public void setBlockPixelDimensions(IntDimension blockPixelDimensions) {
		this.blockPixelDimensions = blockPixelDimensions;
	}

	public int getTotalScores() {
		return totalScores;
	}

	public void setTotalScores(int totalScores) {
		this.totalScores = totalScores;
	}
	
}
