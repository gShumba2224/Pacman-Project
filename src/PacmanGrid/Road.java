package PacmanGrid;

import javafx.scene.paint.Color;

public class Road  extends Block{
	
	private Pill pill = new Pill (Pill.STANDARDPILL);
	
	public Road (){
		super ();
		this.setStrokeWidth(5.0);
		this.setStroke(Color.BLACK);
		this.setFill(Color.GRAY);
	}

}
