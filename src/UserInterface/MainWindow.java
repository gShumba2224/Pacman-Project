package UserInterface;
import java.io.File;
import java.io.IOException;

import Actions.AgentActions;
import Agents.Pacman;
import Game.Game;
import PacmanGrid.Grid;
import PacmanGrid.GridDrawer;
import Utils.IntDimension;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

public class MainWindow extends Application {
    
	private EventHandler<KeyEvent> keyEventHandler;
	private Game game;
    @Override
    public void start(Stage primaryStage) {
    	setKeyControlls();
    	setUpGame();
        StackPane root = new StackPane();
        root.getChildren().add(game.getGrid().getCanvas());
        root.getChildren().add(game.getPacman().getGraphic());
        root.setAlignment(Pos.TOP_LEFT);
        Scene scene = new Scene(root, 750, 750);
        scene.setOnKeyPressed(keyEventHandler);
        primaryStage.setScene(scene);
        primaryStage.show();
    }
    
    public void setKeyControlls (){
	    keyEventHandler =new EventHandler<KeyEvent>() {
		   	public void handle(final KeyEvent keyEvent) {
	   			IntDimension newLocation = new IntDimension (game.getPacman().getLocation().X,
											game.getPacman().getLocation().Y);
	   			
		   		if (keyEvent.getCode() == KeyCode.RIGHT) {
		   			newLocation.X = newLocation.X + 1;
		   			AgentActions.moveAgent(game.getPacman(), game, newLocation );
		   			game.getPacman().getGraphic().setScaleX(1);
		   			game.getPacman().getGraphic().setRotate(0);
		   			
		   		}else if ( keyEvent.getCode() == KeyCode.LEFT ){
		   			newLocation.X = newLocation.X -1;
		   			AgentActions.moveAgent(game.getPacman(), game, newLocation );
		   			game.getPacman().getGraphic().setScaleX(-1);
		   			game.getPacman().getGraphic().setRotate(0);
		   			
		   		}else if ( keyEvent.getCode() == KeyCode.UP ){
		   			newLocation.Y = newLocation.Y -1;
		   			AgentActions.moveAgent(game.getPacman(), game, newLocation );
		   			game.getPacman().getGraphic().setScaleX(1);
		   			game.getPacman().getGraphic().setRotate(-90);
		   				
		   		}else if ( keyEvent.getCode() == KeyCode.DOWN ){
		   			newLocation.Y = newLocation.Y + 1;
		   			AgentActions.moveAgent(game.getPacman(), game, newLocation );
		   			game.getPacman().getGraphic().setScaleX(1);
		   			game.getPacman().getGraphic().setRotate(90);
		   		}
		   		keyEvent.consume();
		   	}
		   };
    }

    public void setUpGame (){
    	Grid grid = null;
    	Pacman pacman = new Pacman (new Image (new File ("C:\\Users\\GMAN\\Desktop\\Temp Stuff\\agentPacman.png").toURI().toString()));
    	pacman.setLocation(new IntDimension (0,0));
		try {
			grid = GridDrawer.drawFromImage(new File ("C:\\Users\\GMAN\\Desktop\\Temp Stuff\\grid01.jpg"),new IntDimension (50,50));
			File file = new File ("C:\\Users\\GMAN\\Desktop\\Temp Stuff\\grid01.jpg");
			grid.setBackgroundImage(new Image (file.toURI().toString()));
			grid.drawPills();
			game = new Game(grid, pacman, null);
			AgentActions.moveAgent(pacman, game, new IntDimension (1,1));
		} catch (IOException e) {
			e.printStackTrace();
		}
    }
 public static void main(String[] args) {
        launch(args);
    }
}