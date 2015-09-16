package PacmanGrid;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import Utils.IntDimension;
import javafx.event.EventHandler;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;

public final class GridDrawer extends Grid{
	
	private boolean doMirror = false;

	public GridDrawer(IntDimension dimension) {
		super(dimension);
	}
	
	public void startGridDrawer (){
		for (int x = 1; x <= this.getBlockDimensions().getX() ; x++ ){
			for (int y = 1 ; y <= this.getBlockDimensions().getY() ; y ++){
				final Block block = new Block ();
				block.setFill(Color.BLUE);
				addEvents(block, this);
				this.addBlock(block, new IntDimension (x,y-1));
				System.out.println(x + " " + y);
			}
		}
	}
	
	private void addEvents (Block block, Grid grid){
		EventHandler<MouseEvent> clickDragEvent  = new EventHandler<MouseEvent>(){
			@Override
			public void handle(MouseEvent event) {
            	if (block.getFill() == Color.BLUE){
            		block.setFill(Color.GRAY);
            	}else{
            		block.setFill(Color.BLUE);
            	}
			}
		};
		block.setOnMouseClicked(clickDragEvent);
	}
	
	public  void saveTemplate (String filePath){
		
		try {
			FileOutputStream OutStream = new FileOutputStream(filePath);
			ObjectOutputStream ObjectOutStream = new ObjectOutputStream(OutStream);
			ObjectOutStream.writeObject(this);
			ObjectOutStream.close();
		} catch (Exception e) {
			System.out.println(e);
		}
	}
	
	public static Grid readTemplate (String filePath){
		
		Grid gridDrawer = null;
		try{
		FileInputStream OutStream = new FileInputStream(filePath);
		ObjectInputStream ObjectOutStream = new ObjectInputStream(OutStream);
		gridDrawer = (Grid) ObjectOutStream.readObject();
		ObjectOutStream.close();
		} catch (Exception e){
			System.out.println(e);
		}
		return gridDrawer;
	}
	
	public Grid templateToFinal (){
		
		Grid finalGrid = new Grid (this.getBlockDimensions());
		Block newBlock;
		for (Block block : this.getBlocks()){
			if (block.getFill() == Color.BLUE){
				newBlock = new Wall ();
				newBlock.setGridNumber(block.getGridNumber());
				newBlock.setGridPosition(block.getGridPosition());
			}else{
				newBlock = new Road ();
				newBlock.setGridNumber(block.getGridNumber());
				newBlock.setGridPosition(block.getGridPosition());
			}
		}
		return finalGrid;
	}
	
	

}
