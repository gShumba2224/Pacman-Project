package AI;

import java.io.IOException;
import java.util.ArrayList;

import Agents.GenericAgent;
import Game.Game;
import GeneticAlgorithm.Evolve;
import GeneticAlgorithm.GeneticAlgorithm;
import GeneticAlgorithm.Genome;
import Neurons.DuplicateNeuronID_Exception;
import Neurons.NeuralNetwork;
import Neurons.NeuralNetworkReader;
import Neurons.Neuron;

public class AiSimulator implements Runnable{
	public AiMonitor monitor = null;
	public Thread thread ;
	private int eliteSampleSize;
	private int boltzSampleSize;
	private double boltzTemperature;
	private double boltzCoolRate = 1.0;
	private int generations;
	private int runsPerGenome;
	private int playersPerGame;
	private int agentType;
	private Game game;
	private GeneticAlgorithm algorithm;
	private NeuralNetwork network;
	private ArrayList <ArrayList <Genome>> genomes = new ArrayList<ArrayList <Genome>> ();
	private ArrayList <ArrayList <GenericAgent>> agents = new ArrayList<ArrayList <GenericAgent>> ();
	
	public AiSimulator (int agentType ,AiMonitor monitor){
		this.monitor = monitor;
		this.agentType = agentType;
	}
	
	public void setSimObjects ( NeuralNetwork network,GeneticAlgorithm algorithm ,Game game,int agentType){
		this.game = game;
		this.algorithm = algorithm;
		this.network = network;
		this.agentType = agentType;
	}
	
	public void setSimulationProperties (int numGenerations, int numRuns, int playersPerGame){
		this.generations = numGenerations;
		this.runsPerGenome = numRuns;
		this.playersPerGame = playersPerGame;
	}
	
	public void setSelectionProperties(int eliteSampleSize,int boltzSampleSize,double boltzTemperature, double boltzCoolRate){
		this.eliteSampleSize = eliteSampleSize;
		this.boltzSampleSize = boltzSampleSize;
		this.boltzTemperature = boltzTemperature;
		this.boltzCoolRate = boltzCoolRate;
	}
	
	private void dividePopulation(){
		genomes.clear();
		ArrayList <Genome> genomeList = new ArrayList <Genome> ();
		//ArrayList <GenericAgent> agentList = new ArrayList <GenericAgent> ();
		genomes.add(genomeList);
		//agents.add(agentList);
		int count = 0;
		for (int i = 0; i < algorithm.getPopulation().size() ; i++){
			if  (count >= playersPerGame){
				count = 0;
				genomeList =  new ArrayList <Genome> ();
				//agentList =  new ArrayList <GenericAgent> ();
				genomes.add(genomeList);
				//agents.add(agentList);
			}
			genomeList.add(algorithm.getPopulation().get(i));
			//agentList.add(game.getAgents().get(agentType).get(i));
			count ++;
		}
	}
	
	public void startThread(String threadName){
		thread = new Thread (this, threadName);
		thread.start();
	}

	@Override
	public void run() {
		int ddd = 0;
		while (this.generations > algorithm.getGeneration()){
			ddd++;
			dividePopulation();
			for (int i = 0; i < genomes.size(); i++){
				int index = 0;
				int totalRuns = runsPerGenome * genomes.get(i).size();
				int runs = 0;
				while (runs < totalRuns){
					//System.out.println(game.getAgents().get(agentType).size() + " AAAA " + agentType);
					//GenericAgent agent = game.getAgents().get(agentType).get(index);//agents.get(i).get(index);
					Genome genome = genomes.get(i).get(index);
					try {monitor.play(network, algorithm, genome, index, game, agentType);
					} catch (DuplicateNeuronID_Exception e) {
						e.printStackTrace();
						System.out.println("shieet");
					}
					if (index == genomes.get(i).size()-1){index = 0;}
					else {index++;}
					runs ++;
				}
				//monitor.restartGame(game, agentType);
			}
		//	System.out.println("Generatdfsdf = $" + algorithm.getGeneration());
			//try { algorithm.writeLogFile();
			//} catch (IOException e) {e.printStackTrace();}
			boltzTemperature = boltzTemperature - boltzCoolRate;
			if (boltzTemperature < 1.0){boltzTemperature = 1.0;}
			algorithm.newGeneration( eliteSampleSize , boltzSampleSize, boltzTemperature, algorithm.getPopulation().size());
			//monitor.restartGame(game, agentType);
			System.out.println(ddd + " gens gens " + algorithm.getGeneration());
		}
	}
}
