package PacmanGrid;

import java.io.Serializable;

import Agents.GenericAgent;
import Utils.IntDimension;
import javafx.scene.shape.Rectangle;

public class Block extends Rectangle implements Serializable {
	
	public Block (){
		this.setHeight(pixelDimensions.getY());
		this.setWidth(pixelDimensions.getX());
		this.gridPosition= new IntDimension (0,0);
	}
	
	public Block (IntDimension gridPosition){
		this.setHeight(pixelDimensions.getY());
		this.setWidth(pixelDimensions.getX());
		this.gridPosition= gridPosition;
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
	
	public GenericAgent getOccupiedBy() {
		return occupiedBy;
	}

	public void setOccupiedBy(GenericAgent occupiedBy) {
		this.occupiedBy = occupiedBy;
	}


	private IntDimension gridPosition;
	private int gridNumber;	
	private GenericAgent occupiedBy = null;
	private static final IntDimension pixelDimensions = new IntDimension (30,30);
}
