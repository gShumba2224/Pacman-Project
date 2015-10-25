package Agents;

import java.io.Serializable;

import GeneticAlgorithm.Genome;
import Utils.IntDimension;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class GenericAgent implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 772949724872181769L;
	public static  int PACMAN = 0;
	public static int GHOST = 1;

	private IntDimension location = new IntDimension(0, 0);
	private ImageView graphic;
	protected boolean isScared;
	private int lives = 1;
	private int speed = 1;
	private IntDimension resetPos = null;
	private Genome controller = null;
	private boolean isDead = false;
	private String name;

	public GenericAgent (Image image){
		graphic = new ImageView (image);
	}
	public GenericAgent (){
	}
	
	public void kill (){
		graphic.setOpacity(0.25);
		location = null;
		lives = 0;
		isDead = true;
	}
	
	public void revive ( int lives, boolean state){
		graphic.setOpacity(1);
		this.lives = lives;
		isDead = false;
		this.setScared(state);
	}
	
	public void decrementLife (){
		lives --;
		if (lives < 0){kill();}
		System.out.println("LIVES LEFT = " + lives);
	}
	
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
	
	public IntDimension getResetPos() {
		return resetPos;
	}
	public void setResetPos(IntDimension resetPos) {
		this.resetPos = resetPos;
	}
	public Genome getController() {
		return controller;
	}
	public void setController(Genome controller) {
		this.controller = controller;
	}
	public boolean isDead() {
		return isDead;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
	
	
}
