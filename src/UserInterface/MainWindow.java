package UserInterface;
import java.io.File;
import java.io.IOException;

import PacmanGrid.Grid;
import PacmanGrid.GridDrawer;
import Utils.IntDimension;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

public class MainWindow extends Application {
    
	private IntDimension windowSize ;
    @Override
    public void start(Stage primaryStage) {
    	
    	GridDrawer.setBlockPixelDimensions(new IntDimension (50,50));
    	Grid grid = null;;
		try {
			grid = GridDrawer.drawFromImage(new File ("C:\\Users\\GMAN\\Desktop\\Temp Stuff\\grid01.jpg"));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		File file = new File ("C:\\Users\\GMAN\\Desktop\\Temp Stuff\\grid01.jpg");
		grid.setBackgroundImage(new Image (file.toURI().toString()));
        FlowPane root = new FlowPane();
        root.getChildren().add(grid.getCanvas());
        Scene scene = new Scene(root, 750, 750);

        primaryStage.setTitle("Hello World!");
        primaryStage.setScene(scene);
        primaryStage.show();
        //primaryStage.setResizable(false);
    }
 public static void main(String[] args) {
        launch(args);
    }
}