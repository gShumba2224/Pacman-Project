package PacmanGrid;

import javafx.scene.paint.Color;

public class Road  extends Block{
	
	private int pill = Pill.NONE;

	public Road (){
		super ();
	}
	
	public Road (int pillType){
		this.pill = pillType;
	}
	
	public int getPill() {
		return pill;
	}

	public void setPill(int pill) {
		this.pill = pill;
	}

}
