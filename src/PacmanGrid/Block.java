package PacmanGrid;

import java.io.Serializable;

import Utils.IntDimension;
import javafx.scene.shape.Rectangle;

public class Block extends Rectangle implements Serializable {
	
	public Block (){
		this.setHeight(pixelDimensions.getY());
		this.setWidth(pixelDimensions.getX());
	}

	public IntDimension getGridPosition() {
		return gridPosition;
	}
	public void setGridPosition(IntDimension gridPosition) {
		this.gridPosition = gridPosition;
	}
	public int getGridNumber() {
		return gridNumber;
	}
	protected void setGridNumber(int gridNumber) {
		this.gridNumber = gridNumber;
	}
	
	public static IntDimension getPixelDimensions() {
		return pixelDimensions;
	}
	
	private IntDimension gridPosition;
	private int gridNumber;	
	private static final IntDimension pixelDimensions = new IntDimension (20,20);

}
