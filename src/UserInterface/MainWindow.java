package UserInterface;
import PacmanGrid.Grid;
import PacmanGrid.GridDrawer;
import Utils.IntDimension;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

public class MainWindow extends Application {
    
	private IntDimension windowSize ;
    @Override
    public void start(Stage primaryStage) {
    	final GridDrawer grid = new GridDrawer ( new IntDimension (20,20));
    	grid.startGridDrawer();
        
    	Button saveButton = new Button();
        saveButton.setText("Save Grid");
        saveButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
            	grid.saveTemplate("C:\\Users\\GMAN\\Desktop\\Temp Stuff\\pacman.grid");
                System.out.println("Save Grid");
            }
        });
        
    	Button loadButton = new Button();
    	loadButton.setText("Load Grid");
    	loadButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
            	GridDrawer newGrid = (GridDrawer)grid.readTemplate("C:\\Users\\GMAN\\Desktop\\Temp Stuff\\pacman.grid");
                newGrid.startGridDrawer();
                FlowPane parent = (FlowPane) loadButton.getParent();
                parent.getChildren().remove(grid);
          
                parent.getChildren().add(newGrid);
            	System.out.println("Load Grid");
            }
        });
        
        
        FlowPane root = new FlowPane();
        root.getChildren().add(saveButton);
        root.getChildren().add(loadButton);
        root.getChildren().add(grid);


 Scene scene = new Scene(root, 600, 600);

        primaryStage.setTitle("Hello World!");
        primaryStage.setScene(scene);
        primaryStage.show();
        //primaryStage.setResizable(false);
    }
 public static void main(String[] args) {
        launch(args);
    }
}