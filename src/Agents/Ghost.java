package Agents;

import javafx.scene.effect.ColorAdjust;
import javafx.scene.image.Image;

public class Ghost extends GenericAgent {
	
	private ColorAdjust colorAdjust = new ColorAdjust();
	public Ghost (){
		setScared (false);
		this.getGraphic().setEffect(colorAdjust);
	}
	
	public Ghost (Image image){
		super (image);
		setScared (false);
		this.getGraphic().setEffect(colorAdjust);
	}
	
	@Override
	public void setScared (boolean scared){
		this.isScared = scared;
		if (this.isScared == true){
			colorAdjust.setHue(-0.8);
			colorAdjust.setBrightness(0.6);
		}else{
			colorAdjust.setHue(0);
			colorAdjust.setBrightness(0);
		}
		
	}
}
