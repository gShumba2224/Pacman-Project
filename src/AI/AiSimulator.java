package AI;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import com.sun.javafx.scene.traversal.Algorithm;

import Actions.Move;
import Agents.GenericAgent;
import Agents.Ghost;
import Game.Game;
import GeneticAlgorithm.Evolve;
import GeneticAlgorithm.GeneticAlgorithm;
import GeneticAlgorithm.Genome;
import Neurons.DuplicateNeuronID_Exception;
import Neurons.NeuralNetwork;
import Neurons.NeuralNetworkReader;
import Neurons.Neuron;
import PacmanGrid.Block;
import PacmanGrid.Grid;
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
		for (int i = 0 ; i < generations; i++){
			try {
				System.out.println("GENERATION == WOOO" + i);
				simulateGeneration(runs, delay);
				pacTemperature = (1-pacCoolRate)*pacTemperature;
				ghostTemperature = (1-ghostCoolRate)*ghostTemperature;
				
				pacAlgorithm.newGeneration(pacEliteSample, pacBoltzSample, pacTemperature,pacAlgorithm.getPopulation().size());
				ghostAlgorithm.newGeneration(ghostEliteSample, ghostBoltzSample,ghostTemperature,pacAlgorithm.getPopulation().size());
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

	
	public void simulateGame(Genome pacGenome,Genome ghostGenome,int run,long delay) throws DuplicateNeuronID_Exception{
		if (run == 0){return;}
		for (GenericAgent agent : game.getPacmen()){
			play (pacNetwork,agent,pacAlgorithm,pacGenome);
		}
		delay(delay);
		for (GenericAgent agent : game.getGhosts()){
			play (ghostNetwork,agent,ghostAlgorithm,ghostGenome);
		}
		run--;
		simulateGame( pacGenome,ghostGenome ,run, delay);
	}
	
	public void simulateGeneration (int runs, long delay) throws DuplicateNeuronID_Exception{
		resetGame();
		for (int i = 0; i < pacAlgorithm.getPopulation().size(); i++ ){
			Genome pacGenome = pacAlgorithm.getPopulation().get(i);
			Genome ghostGenome = ghostAlgorithm.getPopulation().get(i);
			simulateGame(pacGenome, ghostGenome, runs, delay);
		}
	}
	
	public void play (NeuralNetwork network, GenericAgent agent, GeneticAlgorithm algorithm,Genome genome) throws DuplicateNeuronID_Exception{
		network.getInputReader().readInputs (network, agent, game);
		network.setWeights(genome);
		network.update();
		algorithm.evaluateGenome(genome,network, game, agent);
	}
	
	private void delay(long delay){
		long stopTime = System.currentTimeMillis() + (delay);
		long currentTime = System.currentTimeMillis();
		while (currentTime < stopTime){
			 currentTime = System.currentTimeMillis();
		}
	}
	
	private void resetGame (){
		game.getGrid().resetGrid();
		game.setScore(0);
		randomlyPositionAgents (game.getPacmen(),8,14);
		randomlyPositionAgents (game.getGhosts(),0,6);
	}
	
	private void randomlyPositionAgents (List<GenericAgent> agentList, int min, int max){
		Random rand = new Random ();
		Block block = null;
		boolean found = false;

		for (GenericAgent agent :agentList){
			found = false;
			while (found == false){
				IntDimension location = new IntDimension(rand.nextInt(14), rand.nextInt((max-min)+1)+min);
				block = game.getGrid().getBlock(location);
				if (block instanceof Road){
					Move.moveAgent(agent, game,block.getGridPosition());
					found = true;
				}
			}
		}
	}
	
	
	
	
	
	
	
	
	
//	public AiMonitor monitor = null;
//	public Thread thread ;
//	private int eliteSampleSize;
//	private int boltzSampleSize;
//	private double boltzTemperature;
//	private double boltzCoolRate = 1.0;
//	private int generations;
//	private int runsPerGenome;
//	private int playersPerGame;
//	private int agentType;
//	private Game game;
//	private GeneticAlgorithm algorithm;
//	private NeuralNetwork network;
//	private ArrayList <ArrayList <Genome>> genomes = new ArrayList<ArrayList <Genome>> ();
//	
//	public AiSimulator (int agentType ,AiMonitor monitor, GeneticAlgorithm algorithm
//								,Game game, NeuralNetwork network, int playersPerGame){
//		this.monitor = monitor;
//		this.agentType = agentType;
//		this.algorithm = algorithm;
//		this.game = game;
//		this.network =  network;
//		this.playersPerGame = playersPerGame;
//		divideGenomesToGames();
//		replacePopulation();
//	}
//	
//	public void setSimObjects ( NeuralNetwork network,GeneticAlgorithm algorithm ,Game game,int agentType){
//		this.game = game;
//		this.algorithm = algorithm;
//		this.network = network;
//		this.agentType = agentType;
//	}
//	
//	public void setSimulationProperties (int numGenerations, int numRuns){
//		this.generations = numGenerations;
//		this.runsPerGenome = numRuns;
//	}
//	
//	public void setSelectionProperties(int eliteSampleSize,int boltzSampleSize,double boltzTemperature, double boltzCoolRate){
//		this.eliteSampleSize = eliteSampleSize;
//		this.boltzSampleSize = boltzSampleSize;
//		this.boltzTemperature = boltzTemperature;
//		this.boltzCoolRate = boltzCoolRate;
//	}
//	
//	public void divideGenomesToGames(){
//		int totalGames  = algorithm.getPopulation().size()/playersPerGame;
//		for (int i = 0; i < totalGames; i++){
//			ArrayList <Genome> genomeList = new ArrayList <Genome> ();
//			genomes.add(genomeList);
//		}
//	}
//	
//	public void replacePopulation(){
//		int genomeIndex = 0;
//		int count = 0;
//		for (int i = 0; i < genomes.size() ; i++){
//			genomes.get(i).clear();
//			while ( count < playersPerGame){
//				genomes.get(i).add(algorithm.getPopulation().get(genomeIndex));
//				genomeIndex++;
//				count ++;
//			}
//			count = 0;
//		}
//	}
//	
//	public void startThread(String threadName){
//		thread = new Thread (this, threadName);
//		thread.start();
//	}
//
//
//
//	@Override
//	public void run() {
//		while (this.generations > algorithm.getGeneration()){
//			replacePopulation();
//			for (int i = 0; i < genomes.size(); i++){
//				int index = 0;
//				int totalRuns = 1000;//runsPerGenome * genomes.get(i).size();
//				int runs = 0;
//				boolean newGame = false;
//				while (runs <= totalRuns){
//					Genome genome = genomes.get(i).get(index);
//					
//					if (runs ==( totalRuns-1)){newGame = true;}
//					else {newGame =false;}
//					
//					try {monitor.play(network, algorithm, genome, index, game, agentType, newGame);
//					} catch (DuplicateNeuronID_Exception e) {e.printStackTrace();}
//					index++;
//					if (index >= genomes.get(i).size()){index = 0;}
//					runs ++;
//				}
//			}
//			
////			boltzTemperature = boltzTemperature *(1 - boltzCoolRate);
////			if (boltzTemperature < 1.0){boltzTemperature = 1.0;}
////			algorithm.newGeneration( eliteSampleSize , boltzSampleSize, boltzTemperature, algorithm.getPopulation().size());
////			System.out.println("currentGen = " + algorithm.getGeneration());
//		}
//	}
//
//	public NeuralNetwork getNetwork() {
//		return network;
//	}
//
//	public void setNetwork(NeuralNetwork network) {
//		this.network = network;
//	}
	
	
}
