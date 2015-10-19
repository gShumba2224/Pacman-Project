package AI;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import com.sun.javafx.scene.traversal.Algorithm;

import Agents.GenericAgent;
import Agents.Ghost;
import Game.Game;
import Game.Move;
import GeneticAlgorithm.Evolve;
import GeneticAlgorithm.GeneticAlgorithm;
import GeneticAlgorithm.Genome;
import Neurons.DuplicateNeuronID_Exception;
import Neurons.NeuralNetwork;
import Neurons.NeuralNetworkReader;
import Neurons.Neuron;
import PacmanGrid.Block;
import PacmanGrid.Grid;
import PacmanGrid.Pill;
import PacmanGrid.Road;
import Utils.IntDimension;
import javafx.application.Platform;

public class AiSimulator implements Runnable{
	
	private NeuralNetwork pacNetwork;
	private NeuralNetwork ghostNetwork;
	private GeneticAlgorithm pacAlgorithm;
	private GeneticAlgorithm ghostAlgorithm;
	private Game game;
	private int pacEliteSample;
	private int pacBoltzSample;
	private double pacTemperature;
	private double pacCoolRate;
	private int generations;
	private int runs;
	private long delay;
	private int ghostEliteSample;
	private int ghostBoltzSample;
	private double ghostTemperature;
	private double ghostCoolRate;
	

	public  AiSimulator(Game game,NeuralNetwork pacNetwork,GeneticAlgorithm pacAlgorithm,
							NeuralNetwork ghostNetwork, GeneticAlgorithm ghostAlgorithm) {
		this.game = game;
		this.pacNetwork = pacNetwork;
		this.ghostNetwork = ghostNetwork;
		this.pacAlgorithm = pacAlgorithm;
		this.ghostAlgorithm = ghostAlgorithm;
	}
	
	public void startThread(String threadName){
		new Thread (this, threadName).start();;
	}
	
	@Override
	public void run() {
		setStartPosition ();
		for (int i = 0 ; i < generations; i++){
			try {
				System.out.println("GENERATION == WOOO" + i);
				if (i >= 50 && (i%50) == 0){setStartPosition();}
				simulateGeneration(runs, delay);
				pacTemperature = pacTemperature-pacCoolRate;
				ghostTemperature = ghostTemperature-ghostCoolRate;
				if (pacTemperature <= 0){pacTemperature =1;}
				if (ghostTemperature <= 0){ghostTemperature =1;}
				
				pacAlgorithm.newGeneration(pacEliteSample, pacBoltzSample, pacTemperature,pacAlgorithm.getPopulation().size());
				ghostAlgorithm.newGeneration(ghostEliteSample, ghostBoltzSample,ghostTemperature,ghostAlgorithm.getPopulation().size());
			} catch (DuplicateNeuronID_Exception e) {}
		}
	}
	
	public void setSimulationProperties (int generations, int runs, int delay){
		this.generations = generations;
		this.runs = runs;
		this.delay = delay;
	}
	
	public void setPacSimulationProperties (int eliteSample,int boltzSample,double temperature,double coolRate){
			
		this.pacEliteSample = eliteSample;
		this.pacBoltzSample = boltzSample;
		this.pacTemperature = temperature;
		this.pacCoolRate = coolRate;
	}
	
	public void setGhostSimulationProperties (int eliteSample,int boltzSample,double temperature,double coolRate){
		this.ghostEliteSample = eliteSample;
		this.ghostBoltzSample = boltzSample;
		this.ghostTemperature = temperature;
		this.ghostCoolRate = coolRate;
	}		

	
	public void simulateGame(int run,long delay) throws DuplicateNeuronID_Exception{
		if (run == 0){return;}
		for (GenericAgent agent : game.getPacmen()){
			for (int i = 0; i < agent.getSpeed(); i++){
				play (pacNetwork,pacAlgorithm,agent);}
		}
		delay(delay);
		for (GenericAgent agent : game.getGhosts()){
			for (int i = 0; i < agent.getSpeed(); i++){ 
				play (ghostNetwork,ghostAlgorithm,agent);}
		}
		run--;
		simulateGame(run, delay);
	}
	
	public void simulateGeneration (int runs, long delay) throws DuplicateNeuronID_Exception{
		int pacIndex = 0;
		int ghostIndex = 0;
		
		while (pacIndex < pacAlgorithm.getPopulation().size() 
				&& ghostIndex < ghostAlgorithm.getPopulation().size()){
			
			Platform.runLater(new Runnable() {
			    public void run() {game.reset();}
			});
			
			delay(30);
			for (GenericAgent agent: game.getPacmen()){
				Genome genome = pacAlgorithm.getPopulation().get(pacIndex);
				agent.setController(genome);
				pacIndex++;
			}
			for (GenericAgent agent: game.getGhosts()){
				Genome genome = ghostAlgorithm.getPopulation().get(ghostIndex);
				agent.setController(genome);
				ghostIndex++;
			}
			simulateGame(runs, delay);
		}
	}
	
	public void play (NeuralNetwork network,GeneticAlgorithm algorithm,GenericAgent agent) throws DuplicateNeuronID_Exception{
		
		network.getInputReader().readInputs (network, agent, game);
		network.setWeights(agent.getController());
		network.update();
		if (agent instanceof Ghost){
			int index = 0;
			for (Neuron neuron : network.getInputLayer().getNeurons()){
				double val = neuron.getOutputValue();
				network.getOutputLayer().getNeurons().get(index).setOutputValue(val);
				index++;
			}
		}
		algorithm.evaluateGenome(agent.getController(),network,game,agent);
	}
	
	private void delay(long delay){
		long stopTime = System.currentTimeMillis() + (delay);
		long currentTime = System.currentTimeMillis();
		while (currentTime < stopTime){
			 currentTime = System.currentTimeMillis();
		}
	}
	
	private void setStartPosition (){
		Block block = randStartPosition();
		IntDimension startPos = new IntDimension(block.getGridPosition().X,block.getGridPosition().Y);
		for (GenericAgent agent : game.getPacmen()){
			agent.setResetPos(new IntDimension(startPos.X, startPos.Y));
		}
		System.out.println("pac rest =" + block.getGridPosition().X+","+block.getGridPosition().Y);
		block = randStartPosition();
		startPos = new IntDimension(block.getGridPosition().X,block.getGridPosition().Y);
		for (GenericAgent agent : game.getGhosts()){
			agent.setResetPos(new IntDimension(startPos.X, startPos.Y));
		}
		System.out.println("ghost rest =" + block.getGridPosition().X+","+block.getGridPosition().Y);
	}
	
	private Block randStartPosition (){
		boolean found = false;
		Random rand = new Random ();
		Block block = null;
		while (found == false){
			int x = rand.nextInt(14);
			int y = rand.nextInt(13);
			block = game.getGrid().getBlock(new IntDimension(x, y));
			if (block instanceof Road &&((Road)block).getOccupiedBy()==null &&
					((Road)block).getPill()!= Pill.POWERPILL){found = true;}
		}
		return block;
	}
	
	
}

