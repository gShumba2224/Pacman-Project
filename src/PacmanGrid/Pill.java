package PacmanGrid;

import java.io.Serializable;

public class Pill implements Serializable {
	
	private int pointsWorth;
	private int type;
	
	public final static int POWERPILL = 10;
	public final static int STANDARDPILL = 2;
	public final static int GRAPE = 20;
	
	public Pill (int pillType){
		type = pillType;
	}
	
	public int getType (){
		return type;
	}
	
	public void setType (int pillType){
		type = pillType;
	}
	
}

