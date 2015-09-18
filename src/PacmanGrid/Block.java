package PacmanGrid;

import java.io.Serializable;

import Agents.GenericAgent;
import Utils.IntDimension;
import javafx.scene.shape.Rectangle;

public class Block implements Serializable {
	
	public Block (IntDimension pixelDimensions){
		this.gridPosition= new IntDimension (0,0);
		this.pixelDimensions = pixelDimensions;
	}
	
	public Block (IntDimension pixelDimensions, IntDimension gridPosition){
		this.gridPosition= gridPosition;
		this.pixelDimensions = pixelDimensions;
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
	
	public  IntDimension getPixelDimensions() {
		return pixelDimensions;
	}
	
	public  void setPixelDimensions(IntDimension pixelDimensions) {
		 this.pixelDimensions = pixelDimensions;
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
	private IntDimension pixelDimensions ;
}
