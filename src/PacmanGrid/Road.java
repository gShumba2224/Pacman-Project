package PacmanGrid;

import Utils.IntDimension;
import javafx.scene.paint.Color;

public class Road  extends Block{
	
	private int pill = Pill.NONE;

	public Road (IntDimension pixelDimensions, int pillType){
		super ( pixelDimensions);
		this.pill = pillType;
	}
	
	public Road (IntDimension pixelDimensions, IntDimension gridPosition, int pillType){
		super (pixelDimensions,gridPosition);
		this.pill = pillType;
	}
	
	public int getPill() {
		return pill;
	}

	public void setPill(int pill) {
		this.pill = pill;
	}
}
