package Agents;

import javafx.scene.image.Image;

public class Ghost extends GenericAgent {
	
	public Ghost (){
		setScared (false);
	}
	
	public Ghost (Image image){
		super (image);
		setScared (false);
	}
}
