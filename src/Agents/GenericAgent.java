package Agents;

import java.io.Serializable;

import Utils.IntDimension;
import javafx.scene.image.ImageView;

public class GenericAgent implements Serializable {

	public IntDimension getLocation() {
		return location;
	}
	public void setLocation(IntDimension location) {
		this.location = location;
	}
	public ImageView getGraphic() {
		return graphic;
	}
	public void setGraphic(ImageView graphic) {
		this.graphic = graphic;
	}
	public boolean isScared() {
		return isScared;
	}
	public void setScared(boolean isScared) {
		this.isScared = isScared;
	}
	public int getSpeed() {
		return speed;
	}
	public void setSpeed(int speed) {
		this.speed = speed;
	}
	public int getLives() {
		return lives;
	}
	public void setLives(int lives) {
		this.lives = lives;
	}


	private IntDimension location;
	private ImageView graphic;
	private boolean isScared;
	private int lives;
	private int speed;
}
