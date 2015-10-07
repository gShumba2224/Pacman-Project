//package UserInterface;
//import java.io.File;
//import java.io.IOException;
//import java.util.ArrayList;
//import java.util.Map;
//
//import AI.AiSimulator;
//import Actions.AgentActions;
//import Actions.Move;
//import Agents.GenericAgent;
//import Agents.Pacman;
//import Game.Game;
//import GeneticAlgorithm.Evolve;
//import GeneticAlgorithm.Genome;
//import Neurons.DuplicateNeuronID_Exception;
//import Neurons.NeuralNetwork;
//import Neurons.Neuron;
//import PacmanGrid.Block;
//import PacmanGrid.Grid;
//import PacmanGrid.GridDrawer;
//import PacmanGrid.Road;
//import Utils.IntDimension;
//import javafx.application.Application;
//import javafx.event.ActionEvent;
//import javafx.event.EventHandler;
//import javafx.geometry.Pos;
//import javafx.scene.Scene;
//import javafx.scene.control.Button;
//import javafx.scene.image.Image;
//import javafx.scene.input.KeyCode;
//import javafx.scene.input.KeyEvent;
//import javafx.scene.layout.FlowPane;
//import javafx.scene.layout.StackPane;
//import javafx.stage.Stage;
//
//public class MainWindow extends Application {
//    
//	private EventHandler<KeyEvent> keyEventHandler;
//	private Game game;
//	private AiSimulator pacmanSim;
//	private AiSimulator ghostSim;
//	
//	
//    @Override
//    public void start(Stage primaryStage) {
//    	//setKeyControlls();
//    	setUpGame();
//        StackPane root = new StackPane();
//        root.getChildren().add(game.getGrid().getCanvas());
//        for (Pacman pacman : game.pacmen){
//        	root.getChildren().add(pacman.getGraphic());
//        }
//        
//        root.setAlignment(Pos.TOP_LEFT);
//        Scene scene = new Scene(root, 750, 750);
//       // scene.setOnKeyPressed(keyEventHandler);
//        primaryStage.setScene(scene);
//        primaryStage.show();
//    }
//    
//    public void setKeyControlls (){
//	    keyEventHandler =new EventHandler<KeyEvent>() {
//		   	public void handle(final KeyEvent keyEvent) {
//	   			IntDimension newLocation = new IntDimension (game.getPacman().getLocation().X,
//											game.getPacman().getLocation().Y);
//	   			
//		   		if (keyEvent.getCode() == KeyCode.RIGHT) {
//		   			newLocation.X = newLocation.X + 1;
//		   			Move.moveAgent(game.getPacman(), game, newLocation );
//		   			game.getPacman().getGraphic().setScaleX(1);
//		   			game.getPacman().getGraphic().setRotate(0);
//		   			
//		   		}else if ( keyEvent.getCode() == KeyCode.LEFT ){
//		   			newLocation.X = newLocation.X -1;
//		   			Move.moveAgent(game.getPacman(), game, newLocation );
//		   			game.getPacman().getGraphic().setScaleX(-1);
//		   			game.getPacman().getGraphic().setRotate(0);
//		   			
//		   		}else if ( keyEvent.getCode() == KeyCode.UP ){
//		   			newLocation.Y = newLocation.Y -1;
//		   			Move.moveAgent(game.getPacman(), game, newLocation );
//		   			game.getPacman().getGraphic().setScaleX(1);
//		   			game.getPacman().getGraphic().setRotate(-90);
//		   				
//		   		}else if ( keyEvent.getCode() == KeyCode.DOWN ){
//		   			newLocation.Y = newLocation.Y + 1;
//		   			Move.moveAgent(game.getPacman(), game, newLocation );
//		   			game.getPacman().getGraphic().setScaleX(1);
//		   			game.getPacman().getGraphic().setRotate(90);
//		   		}
//		   		keyEvent.consume();
//		   	}
//		   };
//    }
//    
//    public void setupAi (){
//    	int inputSize = game.getGrid().getBlocks().size();
//    	NeuralNetwork networkPacman = new NeuralNetwork(inputSize * 4, 4, 1, inputSize);
//    	Evolve evolve = new Evolve(networkPacman, 6, "move","standardPill","powerPill","grapePill"){
//    		@Override
//    		public void evaluateGenome(Genome genome ,Object...parameters) {
//    			ArrayList<Double> outputs = (ArrayList<Double>) networkPacman.getOutputs();
//    			Pacman pacman = (Pacman) parameters[0];
//    			int action = (Integer) parameters[1];
//    			Map<String,Double> fitnesses = genome.getFitnessProperties();
//    			if (action == Move.GOT_STANDARD_PILL){
//    				fitnesses.put("standardPill", fitnesses.get("standardPill")+ 1.0);
//    			}
//    		}
//    	};
//    	
//    	ArrayList<GenericAgent> pacmanAgents = new ArrayList<GenericAgent> ();
//    	for (int i = 0 ; i < evolve.getPopulation().size()){
//    		Pacman pacman = 
//    		pacmanAgents.add
//    	}
//    	AiSimulator sim = new AiSimulator(networkPacman, evolve, game){
//    		@Override
//    		public void readInputs(){
//    	    	readFromInputs(networkPacman);
//    		};
//    		@Override
//    		public int applyAction (Genome genome, Object...parameters){
//    			return moveAction(networkPacman, game); 
//    		}
//
//    	};
//    	
//    	try {
//			sim.runSimulation(5, 1000*5, 10, 3, 3, 5);
//		} catch (DuplicateNeuronID_Exception e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//   	
//    }
//    
//    public void readFromInputs (NeuralNetwork network){
//    	int i = 0;
//    	Neuron neuron;
//    	for (int x = 0 ; x < game.getGrid().getBlockDimensions().X; x++){
//    		for (int y = 0; y < game.getGrid().getBlockDimensions().Y; y++){
//    			Block block = game.getGrid().getBlock(new IntDimension (x,y));
//    			neuron = network.getInputLayer().getNeurons().get(i);
//    			neuron.setOutputValue(x);
//    			neuron = network.getInputLayer().getNeurons().get(i+1);
//    			neuron.setOutputValue(y);
//    			neuron = network.getInputLayer().getNeurons().get(i+2);
//    			try{
//    				Road road = (Road) block;
//    				neuron.setOutputValue (road.getPill());
//    				neuron = network.getInputLayer().getNeurons().get(i+3);
//    				neuron.setOutputValue(1);
//    			} catch (ClassCastException e){
//    				neuron.setOutputValue(0);
//    				neuron = network.getInputLayer().getNeurons().get(i+3);
//    				neuron.setOutputValue(-1.0);
//    			}
//    			i = i + 4;
//    		}
//    	}
//    }
//    
//    public int moveAction (NeuralNetwork network , GenericAgent agent) {
//		ArrayList<Double> outputs = (ArrayList<Double>) network.getOutputs();
//		IntDimension newLocation = new IntDimension ( agent.getLocation().X, agent.getLocation().Y);
//		if (outputs.get(0) > 0.5 ){
//			newLocation.X = newLocation.X + 1;
//		}else if (outputs.get(1) > 0.5 ){
//			newLocation.X = newLocation.X -1;
//		}else if (outputs.get(2) > 0.5 ){
//			newLocation.Y = newLocation.Y + 1;
//		}else if (outputs.get(3) > 0.5 ){
//			newLocation.Y = newLocation.Y - 1;
//		}
//		return (Move.moveAgent(agent, game, newLocation ));
//	}
//  
//   
//
//    public void setUpGame (){
//    	Grid grid = null;
//    	Pacman pacman = null;
//    	//ArrayList <Pacman> pacmen = new ArrayList <Pacman> ();
//
//		try {
//			grid = GridDrawer.drawFromImage(new File ("C:\\Users\\GMAN\\Desktop\\Temp Stuff\\grid01.jpg"),new IntDimension (50,50));
//			File file = new File ("C:\\Users\\GMAN\\Desktop\\Temp Stuff\\grid01.jpg");
//			grid.setBackgroundImage(new Image (file.toURI().toString()));
//			grid.drawPills();
//			game = new Game(grid, null);
//	    	for (int i = 0 ; i < 6 ; i++){
//	    		pacman = new Pacman (new Image (new File ("C:\\Users\\GMAN\\Desktop\\Temp Stuff\\agentPacman.png").toURI().toString()));
//	    		pacman.setLocation(new IntDimension (0,0));
//	    		Move.moveAgent(pacman, game, new IntDimension (1+i,1));
//	    		game.pacmen.add(pacman);
//	    	}
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
//    }
// public static void main(String[] args) {
//        launch(args);
//    }
//}