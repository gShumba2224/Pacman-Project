package PacmanGrid;

import javafx.scene.paint.Color;

public class Wall extends Block{

	public Wall (){
		super ();
		this.setStrokeWidth(5.0);
		this.setStroke(Color.BLACK);
		this.setFill(Color.BLUE);
	}
}
