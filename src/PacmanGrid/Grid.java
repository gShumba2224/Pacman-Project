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
	
	public Grid (IntDimension dimension , IntDimension blockSize){
		
		canvas = new Canvas (dimension.getX() * blockSize.getX() ,dimension.getY() * blockSize.getY());
		blockDimensions = dimension;
		numberOfBlocks = blockDimensions.getX() * blockDimensions.getY();
		blocks = new ArrayList <Block> (numberOfBlocks);
		blockPixelDimensions = blockSize;
	}


	public void drawGrid (){
		GraphicsContext gc = canvas.getGraphicsContext2D();
		gc.setFill(Color.RED);
		gc.fillRect(0, 0, canvas.getWidth(), canvas.getHeight());
		int x = 0;
		int y = 0;
		int count = 0;
		for (int row = 0; row < 600; row = row + 200 ){
			for (int column = 0; column < 600 ; column = column + 200){
				if (y > 2) y = 0;
				gc.setFill(Color.BLUE);
				gc.setStroke(Color.BLACK);
				gc.setLineWidth(1.0);
				int num = x * 3 + y ;
				gc.fillText(String.valueOf(coordinateToGridNumber(new IntDimension (x,y))), row + 100, column + 100);
				gc.strokeRect(row, column, blockPixelDimensions.getX() , blockPixelDimensions.getY());
				y++;
				count ++;
			}
			x ++;
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
}
