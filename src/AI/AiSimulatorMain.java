package AI;

import java.io.IOException;

import Agents.GenericAgent;
import Game.Game;
import GeneticAlgorithm.Evolve;
import GeneticAlgorithm.GeneticAlgorithm;
import GeneticAlgorithm.Genome;
import Neurons.NeuralNetwork;
import Neurons.NeuralNetworkReader;
import Neurons.Neuron;
import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

public class AiSimulatorMain  extends Application {
	
	StackPane stackPane = new StackPane();
	Game game; 
	@Override
	public void start(Stage primaryStage) throws Exception {
		layoutWindow();
        Scene scene = new Scene(stackPane, 750, 750);
       // scene.setOnKeyPressed(keyEventHandler);
        primaryStage.setScene(scene);
        primaryStage.show();
	}

	public void layoutWindow (){
		try {
			game = new Game(4, 4);
		} catch (IOException e) {}
		stackPane.getChildren().add(game.getGrid().getCanvas());
		for (GenericAgent agent : game.getAgents().get(GenericAgent.GHOST)){
			stackPane.getChildren().add(agent.getGraphic());}
		for (GenericAgent agent : game.getAgents().get(GenericAgent.PACMAN)){
			stackPane.getChildren().add(agent.getGraphic());}
		stackPane.setAlignment(Pos.TOP_LEFT);
	}
	
  public static void main(String[] args) {
	 // launch(args);
	  
	NeuralNetwork net1 = new NeuralNetwork(3, 3, 3, 3);
	NeuralNetworkReader reader = new NeuralNetworkReader() {
		@Override
		public void readInputs(NeuralNetwork network, Object... parameters) {
			int count = 0;
			for (Neuron neuron : network.getInputLayer().getNeurons()){
				neuron.setOutputValue(count);
				count ++;
			}
		}
	};
	net1.setInputReader(reader);
	GeneticAlgorithm alg = new Evolve(net1, 20, "a","b"){
	@Override
	public void evaluateGenome (Genome genome , Object...p){
		NeuralNetwork network = (NeuralNetwork)p[0];
		if (network.getOutputs().get(0) > 0.5 ){
			genome.getFitnessProperties().put("a", 1.0);
			}
		}
	};
	
	try {
		alg.writeLogFile(null);
	} catch (IOException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
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
