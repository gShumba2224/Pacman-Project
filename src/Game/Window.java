package Game;

import java.io.File;
import java.io.IOException;

import Agents.GenericAgent;
import Agents.Pacman;
import PacmanGrid.Grid;
import PacmanGrid.GridDrawer;
import Utils.IntDimension;
import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

public class Window extends Application {

	StackPane stackPane = new StackPane();
	Game game;
	public Window (){
		
	}

	public  Window (Game game){
		this.game = game;
		//layoutWindow(game);
	}
	
	public void layoutWindow (Game game){
		stackPane.getChildren().add(game.getGrid().getCanvas());
		for (GenericAgent agent : game.getPacmen()){stackPane.getChildren().add(agent.getGraphic());}
		//for (GenericAgent agent : game.getGhosts()){stackPane.getChildren().add(agent.getGraphic());}
		stackPane.setAlignment(Pos.TOP_LEFT);
	}
	    
	@Override
	public void start(Stage primaryStage) {
		layoutWindow(game);
        Scene scene = new Scene(stackPane, 750, 750);
       // scene.setOnKeyPressed(keyEventHandler);
        primaryStage.setScene(scene);
        primaryStage.show();
	}

	 public static void main(String[] args) {
	        launch(args);
	        
	    }
	
//	public static void main (String[] args){
//		Game game;
//		try {
//			game = new Game(5,5);
//			Window x = new Window (game);
//			x.launch();
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
//		
//	}

}
