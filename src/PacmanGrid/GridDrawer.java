package PacmanGrid;

import Utils.IntDimension;
import javafx.event.EventHandler;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;

public class GridDrawer extends Grid{

	public GridDrawer(IntDimension dimension) {
		super(dimension);
		startGridDrawer();
	}
	
	public void startGridDrawer (){
		for (int x = 1; x <= this.getBlockDimensions().getX() ; x++ ){
			for (int y = 1 ; y <= this.getBlockDimensions().getY() ; y ++){
				final Block block = new Block ();
				block.setFill(Color.BLUE);
			    block.setOnMouseClicked(new EventHandler<MouseEvent>()
			        {
			            @Override
			            public void handle(MouseEvent event) {
			            	if (block.getFill() == Color.BLUE){
			            		block.setFill(Color.GRAY);
			            	}else{
			            		block.setFill(Color.BLUE);
			            	}
			            }
			        });
				this.addBlock(block, new IntDimension (x,y-1));
				System.out.println(x + " " + y);
			}
		}
	}
	
	public void saveGrid (){
		
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
