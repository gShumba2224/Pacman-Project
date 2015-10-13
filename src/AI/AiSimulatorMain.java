package AI;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
import Search.A_StarNode;
import Search.A_StarSearch;
import Search.ArtifactSearch;
import Search.Container;
import Utils.IntDimension;
import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

public class AiSimulatorMain  extends Application {
	
	StackPane stackPane = new StackPane();
	Game game; 
	AiSimulator pacSim ;//= new AiSimulator(GenericAgent.PACMAN, monitor);
	AiSimulator ghostSim; //= new AiSimulator(GenericAgent.GHOST, monitor);
	
	@Override
	public void start(Stage primaryStage) throws Exception {
		layoutWindow();
		buildNetwork1();
        Scene scene = new Scene(stackPane, 750, 750);
       // scene.setOnKeyPressed(keyEventHandler);
        primaryStage.setScene(scene);
        primaryStage.show();
        
	}

	public void layoutWindow (){
		try {
			game = new Game(5, 5){
				@Override 
				public void reset (int agentType){
					if (agentType == GenericAgent.PACMAN){
						this.getGrid().resetGrid();
						this.setScore(0);
						for (GenericAgent agent : game.getPacmen()){
							IntDimension defaultPos = new IntDimension(1, 2);
							agent.setLocation(defaultPos);
							Move.moveAgent(agent, game,defaultPos);
						}
					}else{
						for (GenericAgent agent : game.getGhosts()){
							IntDimension defaultPos = new IntDimension(1, 1);
							agent.setLocation(defaultPos);
							Move.moveAgent(agent, game, new IntDimension(12,1));
						}
					}
				}
			};
			game.reset(GenericAgent.PACMAN);
			game.reset(GenericAgent.GHOST);
		} catch (IOException e) {}
		stackPane.getChildren().add(game.getGrid().getCanvas());
		for (GenericAgent agent : game.getAgents().get(GenericAgent.GHOST)){
			stackPane.getChildren().add(agent.getGraphic());}
		for (GenericAgent agent : game.getAgents().get(GenericAgent.PACMAN)){
			stackPane.getChildren().add(agent.getGraphic());}
		stackPane.setAlignment(Pos.TOP_LEFT);
	}
	
	public void buildNetwork1 () throws DuplicateNeuronID_Exception{
		
		
		NeuralNetwork net1 =  new NeuralNetwork(28, 4, 0, 0);
		NeuralNetwork net2 = new NeuralNetwork(4, 4, 0, 0);
		InputReader reader1= new InputReader(game);
		InputReader reader2= new InputReader(game);
		net1.setInputReader(reader1);
		net2.setInputReader(reader2);
		net1.getOutputLayer().getBiasNeuron().setOutputValue(-1);
		net2.getOutputLayer().getBiasNeuron().setOutputValue(-1);
		AgentEvolver e = new AgentEvolver(net1, 100, GenericAgent.PACMAN);
		AgentEvolver e2 = new AgentEvolver(net2, 100, GenericAgent.GHOST);
		
		pacSim = new AiSimulator(game, net1, e.getAlgorithm(), net2, e2.getAlgorithm());
		pacSim.setGhostSimulationProperties(3, 3, 10, 0.1);
		pacSim.setPacSimulationProperties(3, 3, 10, 0.1);
		pacSim.setSimulationProperties(10, 10,10);
		//pacSim.setGhostSimulationProperties(eliteSample, boltzSample, temperature, coolRate, generations, runs, delay);
		pacSim.startThread("Pacman Simulation");

	}
	
	
  public static void main(String[] args) {
	 launch(args);

  }
}
