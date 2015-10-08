package AI;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import Actions.Move;
import Agents.GenericAgent;
import Agents.Ghost;
import Agents.Pacman;
import Game.Game;
import GeneticAlgorithm.Evolve;
import GeneticAlgorithm.Gene;
import GeneticAlgorithm.GeneticAlgorithm;
import GeneticAlgorithm.Genome;
import Neurons.DuplicateNeuronID_Exception;
import Neurons.InputConnection;
import Neurons.InputNeuron;
import Neurons.NeuralLayer;
import Neurons.NeuralNetwork;
import Neurons.NeuralNetworkReader;
import Neurons.Neuron;
import PacmanGrid.Block;
import PacmanGrid.Road;
import Search.A_StarSearch;
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
        pacSim.startThread("PACSIM");
        ghostSim.startThread("Ghost Sim");
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
		NeuralNetwork net1 =  new NeuralNetwork(2, 2, 1, 2);
		NeuralNetwork net2 = new NeuralNetwork(13, 4, 1, 10);
		
		net1.getInputLayer().getNeurons().get(0).setOutputValue(0.8);
		net1.getInputLayer().getNeurons().get(1).setOutputValue(0.4);
		
		net1.getHiddenLayers().get(0).getBiasNeuron().setOutputValue(-1);
		net1.getOutputLayer().getBiasNeuron().setOutputValue(-1);
		
		A_StarSearch x = new A_StarSearch(game.getGrid());
		

		
//		InputReader reader = new InputReader(game);
//		InputReader reader2 = new InputReader(game);
//		
//		net1.setInputReader(reader);
//		net2.setInputReader(reader2);
//		
//		int gen = 800;
//		int temp = 10;
//		int boltz = 10;
//		int elite = 10;
//		int pop = 100;
//		int runs = 12;
//		
//		AgentEvolver pacEvolver = new AgentEvolver(net1, pop,GenericAgent.PACMAN, 0,2,-1,1);
//		AgentEvolver ghostEvolver = new AgentEvolver(net2,pop,GenericAgent.GHOST, 0,2,-1,1);
//		
//		pacSim.setSelectionProperties(elite, boltz, temp, 1);
//		pacSim.setSimObjects(net1, pacEvolver.getAlgorithm(), game, GenericAgent.PACMAN);
//		pacSim.setSimulationProperties(gen, runs, 5);
//		
//		ghostSim.setSelectionProperties(elite, boltz, temp, 1);
//		ghostSim.setSimObjects(net2, ghostEvolver.getAlgorithm(), game, GenericAgent.GHOST);
//		ghostSim.setSimulationProperties(gen, runs, 5);
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
