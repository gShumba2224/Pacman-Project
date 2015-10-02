package Agents;

import Game.Game;
import javafx.scene.image.Image;

public class Pacman extends GenericAgent {
	
	public Pacman (){
		setScared (true);
	}
	public Pacman (Image image){
		super (image);
		setScared (true);
	}
	
	
	
}
