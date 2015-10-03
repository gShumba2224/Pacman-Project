package AI;

import java.io.IOException;
import java.util.ArrayList;

import Actions.Move;
import Agents.GenericAgent;
import Agents.Ghost;
import Agents.Pacman;
import Game.Game;
import GeneticAlgorithm.Evolve;
import GeneticAlgorithm.GeneticAlgorithm;
import GeneticAlgorithm.Genome;
import Neurons.InputNeuron;
import Neurons.NeuralLayer;
import Neurons.NeuralNetwork;
import Neurons.NeuralNetworkReader;
import Neurons.Neuron;
import PacmanGrid.Road;
import Utils.IntDimension;
import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

public class AiSimulatorMain  extends Application {
	
	StackPane stackPane = new StackPane();
	Game game; 
	AiMonitor monitor = new AiMonitor(GenericAgent.PACMAN);
	AiSimulator pacSim = new AiSimulator(GenericAgent.PACMAN, monitor);
	AiSimulator ghostSim = new AiSimulator(GenericAgent.GHOST, monitor);
	@Override
	public void start(Stage primaryStage) throws Exception {
		layoutWindow();
		 buildNetwork1 ();
        Scene scene = new Scene(stackPane, 750, 750);
       // scene.setOnKeyPressed(keyEventHandler);
        primaryStage.setScene(scene);
        primaryStage.show();
	}

	public void layoutWindow (){
		try {
			game = new Game(5, 5);
			game.setInitialAgentPositions(GenericAgent.GHOST, 
					new IntDimension (1,13),new IntDimension (6,13),new IntDimension (8,13),
					new IntDimension (13,13),new IntDimension (7,11));
			game.setInitialAgentPositions(GenericAgent.PACMAN,
					new IntDimension (5,1),new IntDimension (6,1),new IntDimension (8,1),
					new IntDimension (9,1),new IntDimension (7,8));
			game.reset(GenericAgent.GHOST);
			game.reset(GenericAgent.PACMAN);
		} catch (IOException e) {}
		stackPane.getChildren().add(game.getGrid().getCanvas());
		for (GenericAgent agent : game.getAgents().get(GenericAgent.GHOST)){
			stackPane.getChildren().add(agent.getGraphic());}
		for (GenericAgent agent : game.getAgents().get(GenericAgent.PACMAN)){
			stackPane.getChildren().add(agent.getGraphic());}
		stackPane.setAlignment(Pos.TOP_LEFT);
	}
	
	public void buildNetwork1 (){
		int a = game.getGrid().getBlockDimensions().X * game.getGrid().getBlockDimensions().Y;
		int b = a * 4;
		NeuralNetwork net1 = new NeuralNetwork(b, 4, 1, a);
		
		a = game.getGrid().getBlockDimensions().X * game.getGrid().getBlockDimensions().Y;
		b =  a * 3;
		NeuralNetwork net2 = new NeuralNetwork(b, 4, 1, b);
		
		NeuralNetworkReader reader1 = new NeuralNetworkReader() {
			@Override
			public void readInputs(NeuralNetwork network, Object... parameters) {
				NeuralLayer inputLayer = network.getInputLayer();
				GenericAgent agent = (GenericAgent) parameters[0];
				int index = 0;
				for (int x = 0 ; x < game.getGrid().getBlockDimensions().X; x++  ){
					for (int y = 0; y < game.getGrid().getBlockDimensions().Y ; y++){
						try{
							Road road = (Road) game.getGrid().getBlock(new IntDimension (x,y));
							GenericAgent occupent = road.getOccupiedBy();
							if (occupent != null){
								try{
									Ghost ghost = (Ghost)occupent;
									if (agent.isScared() == true){
										inputLayer.getNeurons().get(index).setOutputValue(-0.5);
									}else {inputLayer.getNeurons().get(index).setOutputValue(0.5);}
								}catch (ClassCastException e){
									inputLayer.getNeurons().get(index).setOutputValue(-0.5);
								}
							}else { inputLayer.getNeurons().get(index).setOutputValue(0.0);}
							inputLayer.getNeurons().get(index+1).setOutputValue(road.getPill()/10);
							inputLayer.getNeurons().get(index+2).setOutputValue(
									calculateDistance(road.getGridPosition(), agent.getLocation(), 1));
							inputLayer.getNeurons().get(index+3).setOutputValue(50);
						}catch (ClassCastException e){
							for (int i = 0 ; i < 4; i++){
								inputLayer.getNeurons().get(index + i).setOutputValue(-50);}
						}
						index = index + 4;
					}
				}
			}
		};
		net1.setInputReader(reader1);
		
		NeuralNetworkReader reader2 = new NeuralNetworkReader(){
			@Override
			public void readInputs(NeuralNetwork network, Object... parameters) {
				NeuralLayer inputLayer = network.getInputLayer();
				GenericAgent agent = (GenericAgent) parameters[0];
				int index = 0;
				for (int x = 0 ; x < game.getGrid().getBlockDimensions().X; x++  ){
					for (int y = 0; y < game.getGrid().getBlockDimensions().Y ; y++){
						try{
							Road road = (Road) game.getGrid().getBlock(new IntDimension (x,y));
							GenericAgent occupent = road.getOccupiedBy();
							if (occupent != null){
								try{
									Pacman pacman = (Pacman)occupent;
									if (agent.isScared() == true){
										inputLayer.getNeurons().get(index).setOutputValue(-0.5);
									}else {inputLayer.getNeurons().get(index).setOutputValue(0.5);}
								}catch (ClassCastException e){
									inputLayer.getNeurons().get(index).setOutputValue(-0.5);}
							}else { inputLayer.getNeurons().get(index).setOutputValue(0.0);}
							inputLayer.getNeurons().get(index+1).setOutputValue(
									calculateDistance(road.getGridPosition(), agent.getLocation(), 1));
							inputLayer.getNeurons().get(index+2).setOutputValue(50);
						}catch (ClassCastException e){
							for (int i = 0 ; i < 3; i++){
								inputLayer.getNeurons().get(index + i).setOutputValue(-50);}
						}
						index = index + 3;
					}
				}
			}
		};
		net1.setInputReader(reader2);
		
		GeneticAlgorithm alg1 = new Evolve(net1, 20, "ghost-avoid","move", "food", "enemy-kills"){
		@Override
		public void evaluateGenome (Genome genome , Object...p){
				NeuralNetwork network = (NeuralNetwork)p[0];
				GenericAgent agent = (GenericAgent)p[3];
				int result = moveAction (network, agent);
				double currentVal = 0.0;
				if (result != Move.GOT_KILLED){
					if (result != Move.HITWALL){
						currentVal = genome.getFitnessProperties().get("ghost-avoid");
						genome.getFitnessProperties().put( "ghost-avoid", currentVal+ 0.1);
						currentVal = genome.getFitnessProperties().get("move");
						genome.getFitnessProperties().put( "move", currentVal+ 0.05);
						
						if (result == Move.GOT_GRAPE_PILL){
							currentVal = genome.getFitnessProperties().get("food");
							genome.getFitnessProperties().put( "food", currentVal+ 0.5);
						}else if (result == Move.GOT_POWER_PILL){
							currentVal = genome.getFitnessProperties().get("food");
							genome.getFitnessProperties().put("food", currentVal+ 0.2);
						}else if (result == Move.GOT_STANDARD_PILL){
							currentVal = genome.getFitnessProperties().get("food");
							genome.getFitnessProperties().put("food", currentVal+ 0.1);
						}else if (result == Move.KILLED_ENEMY){
							currentVal = genome.getFitnessProperties().get("enemy-kills");
							genome.getFitnessProperties().put("enemy-kills", currentVal+ 5.0);
						}
					}
				}else {
					currentVal = genome.getFitnessProperties().get("ghost-avoid");
					genome.getFitnessProperties().put( "ghost-avoid", currentVal -  2.0);
				}
			}
		};
		alg1.setLogFile("C:\\Users\\GMAN\\Desktop\\Temp Stuff\\logfile.log");
		
		GeneticAlgorithm alg2 = new Evolve(net2, 20, "pac-avoid","move","enemy-kills"){
		@Override
		public void evaluateGenome (Genome genome , Object...p){
				NeuralNetwork network = (NeuralNetwork)p[0];
				GenericAgent agent = (GenericAgent)p[3];
				int result = moveAction (network, agent);
				double currentVal = 0.0;
				if (result != Move.GOT_KILLED){
					if (result != Move.HITWALL){
						currentVal = genome.getFitnessProperties().get("pac-avoid");
						genome.getFitnessProperties().put( "pac-avoid", currentVal+ 0.1);
						currentVal = genome.getFitnessProperties().get("move");
						genome.getFitnessProperties().put( "move", currentVal+ 0.05);
						if (result == Move.KILLED_ENEMY){
							currentVal = genome.getFitnessProperties().get("enemy-kills");
							genome.getFitnessProperties().put("enemy-kills", currentVal+ 5.0);
						}
					}
				}else {
					currentVal = genome.getFitnessProperties().get("pac-avoid");
					genome.getFitnessProperties().put( "pac-avoid", currentVal -  2.0);
				}
			}
		};
		alg2.setLogFile("C:\\Users\\GMAN\\Desktop\\Temp Stuff\\logfile2.log");
		
		pacSim.setSelectionProperties(6, 8, 10, 1);
		pacSim.setSimObjects(net1, alg1, game, GenericAgent.PACMAN);
		pacSim.setSimulationProperties(10, 10, 5);
		
		ghostSim.setSelectionProperties(6, 8, 10, 1);
		ghostSim.setSimObjects(net2, alg2, game, GenericAgent.GHOST);
		ghostSim.setSimulationProperties(10, 10, 5);
	}
	
    public int moveAction (NeuralNetwork network , GenericAgent agent) {
		ArrayList<Double> outputs = (ArrayList<Double>) network.getOutputs();
		IntDimension newLocation = new IntDimension ( agent.getLocation().X, agent.getLocation().Y);
		if (outputs.get(0) > 0.5 ){
			newLocation.X = newLocation.X + 1;
		}else if (outputs.get(1) > 0.5 ){
			newLocation.X = newLocation.X -1;
		}else if (outputs.get(2) > 0.5 ){
			newLocation.Y = newLocation.Y + 1;
		}else if (outputs.get(3) > 0.5 ){
			newLocation.Y = newLocation.Y - 1;
		}
		return (Move.moveAgent(agent, game, newLocation ));
	}
    
    private double calculateDistance (IntDimension from, IntDimension to , double divisor){
    	IntDimension distance = new IntDimension (0,0);
		distance.X = Math.abs( from.X - to.X );
		distance.Y =  Math.abs(from.Y - to.Y );
		double magnitude = Math.sqrt((distance.X * distance.X ) + (distance.Y*distance.Y));
		if (magnitude != 0){
			return 0.0;
		} else { return (divisor/magnitude);}
    }
	
  public static void main(String[] args) {
	 launch(args);
	  
//Game game = new Game();
//game.generateAgents(20, 20);
//
//AiMonitor monitor = new AiMonitor(GenericAgent.GHOST);
//AiSimulator m = new AiSimulator(GenericAgent.GHOST, monitor);
//m.setSimObjects(net1, alg, game, GenericAgent.GHOST);
//m.setSelectionProperties(5, 5, 5);
//m.setSimulationProperties(3, 1, 5);
//
//NeuralNetwork net2 = new NeuralNetwork(3, 3, 3, 3);
//NeuralNetworkReader reader2 = new NeuralNetworkReader() {
//	@Override
//	public void readInputs(NeuralNetwork network, Object... parameters) {
//		int count = 0;
//		for (Neuron neuron : network.getInputLayer().getNeurons()){
//			neuron.setOutputValue(count);
//			count ++;
//		}
//	}
//};
//net2.setInputReader(reader2);
//GeneticAlgorithm alg2 = new Evolve(net2, 20, "a","b"){
//@Override
//public void evaluateGenome (Genome genome , Object...p){
//	NeuralNetwork network = (NeuralNetwork)p[0];
//	if (network.getOutputs().get(0) > 0.5 ){
//		genome.getFitnessProperties().put("a", 1.0);
//		}
//	}
//};
//
//AiSimulator n = new AiSimulator(GenericAgent.PACMAN, monitor);
//n.setSimObjects(net2, alg2, game, GenericAgent.PACMAN);
//n.setSelectionProperties(5, 5, 5);
//n.setSimulationProperties(3, 1, 5);
//
//m.startThread("SD");
//n.startThread("SDF");
//}
  }
}
