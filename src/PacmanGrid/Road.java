package PacmanGrid;

import Utils.IntDimension;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Shape;

public class Road  extends Block{
	
	private int pill = Pill.NONE;
	private Circle pillGraphic = null;

	public Road (IntDimension pixelDimensions, int pillType){
		super ( pixelDimensions);
		this.pill = pillType;
		setPillGraphic();
	}
	
	public Road (IntDimension pixelDimensions, IntDimension gridPosition, int pillType){
		super (pixelDimensions,gridPosition);
		this.pill = pillType;
	}
	
	private void setPillGraphic() {
		if (pill != Pill.NONE){
			pillGraphic = new Circle(this.getPixelDimensions().X/2);
			if (pill == Pill.STANDARDPILL){pillGraphic.setFill(Color.WHITE);}
			else if (pill == Pill.POWERPILL){pillGraphic.setFill(Color.YELLOW);}
			else if (pill == Pill.GRAPE){pillGraphic.setFill(Color.PURPLE);}
		}
		else {pillGraphic = null;}
	}
	
	public int getPill() {
		return pill;
	}

	public void setPill(int pill) {
		this.pill = pill;
		setPillGraphic();
	}

	public Shape getPillGraphic() {
		return pillGraphic;
	}


	
	
}
