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
	
	private GridDrawer (){
	}

	public static Grid drawFromImage (File imagePath,IntDimension blockPixelDimensions) throws IOException{
		
		BufferedImage image = ImageIO.read(imagePath);
		IntDimension newGridDimensions = new IntDimension ((int)image.getWidth()/blockPixelDimensions.getX(),
														(int)image.getHeight()/blockPixelDimensions.getY());
		Grid newGrid = new Grid (newGridDimensions , blockPixelDimensions);
		IntDimension gridPos = new IntDimension (0,0);
		
		int min = 50;
		
		for (int row = 0; row < (int)image.getWidth() ; row = row + blockPixelDimensions.getX() ){
			
			for (int column = 0; column < (int)image.getHeight() ; column = column + blockPixelDimensions.getY()){
				int pixels[] = new int[3];
				image.getData().getPixel(row, column, pixels);
				//System.out.println(row+"_"+column+"color_("+pixels[0]+"_"+pixels[1]+"_"+pixels[2]+")");
				
				if ( pixels[0] > min && pixels[1] > min && pixels[2] > min){// no pill
					newGrid.addBlock(new Road (blockPixelDimensions,Pill.NONE),gridPos);
				}else{
					if (pixels[0] > min ){ //road with grape
						newGrid.addBlock(new Road (blockPixelDimensions,Pill.GRAPE),gridPos);
					
					} else if (pixels[1] > min){ // road with power pill
						newGrid.addBlock(new Road (blockPixelDimensions,Pill.POWERPILL), gridPos);
					
					}else if (pixels[2] > min){ //road with standard food
						newGrid.addBlock (new Road (blockPixelDimensions,Pill.STANDARDPILL), gridPos);
					
					}else {   //wall
						newGrid.addBlock (new Wall (blockPixelDimensions), gridPos);
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
