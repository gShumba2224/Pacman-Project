package PacmanGrid;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.imageio.ImageIO;

import Utils.IntDimension;
import javafx.event.EventHandler;
import javafx.scene.image.Image;
import javafx.scene.image.PixelReader;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;

public final  class GridDrawer {
	
	private static IntDimension blockPixelDimensions = new IntDimension (200,200);
	private GridDrawer (){
	}

	public static IntDimension getBlockPixelDimensions() {
		return blockPixelDimensions;
	}

	public static void setBlockPixelDimensions(IntDimension blockPixelDimensions) {
		GridDrawer.blockPixelDimensions = blockPixelDimensions;
	}


	public static Grid drawFromImage (File imagePath) throws IOException{
		
		BufferedImage image = ImageIO.read(imagePath);
		IntDimension newGridDimensions = new IntDimension ((int)image.getWidth()/blockPixelDimensions.getX(),
														(int)image.getHeight()/blockPixelDimensions.getY());
		Grid newGrid = new Grid (newGridDimensions , blockPixelDimensions);
		IntDimension gridPos = new IntDimension (0,0);
		
		for (int row = 0; row < (int)image.getWidth() ; row = row + Block.getPixelDimensions().getX() ){
			
			for (int column = 0; column < (int)image.getHeight() ; column = column + Block.getPixelDimensions().getY()){
				int pixels[] = new int[3];
				image.getData().getPixel(row, column, pixels);
				
				if ( pixels[0] > 0 && pixels[1] > 0 && pixels[2] > 0){// no pill
					newGrid.addBlock(new Road (Pill.NONE),gridPos);
				}else{
					if (pixels[0] > 0 ){ //road with grape
						newGrid.addBlock(new Road (Pill.GRAPE),gridPos);
					
					} else if (pixels[1] > 0){ // road with power pill
						newGrid.addBlock(new Road (Pill.POWERPILL), gridPos);
					
					}else if (pixels[2] > 0){ //road with standard food
						newGrid.addBlock (new Road (Pill.STANDARDPILL), gridPos);
					
					}else {   //wall
						newGrid.addBlock (new Wall (), gridPos);
					}
				}
				gridPos.setY(gridPos.getY() + 1);
			}
			gridPos.setX(gridPos.getX() + 1);
			gridPos.setY(0);
		}
		return newGrid;
	}
}
