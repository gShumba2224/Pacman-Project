package Actions;

import Neurons.DuplicateNeuronID_Exception;
import Neurons.InputConnection;
import  Neurons.NeuralNetwork;
import Neurons.Neuron;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Timer;
import java.util.TimerTask;

import Game.Game;
import GeneticAlgorithm.Evolve;
import GeneticAlgorithm.Genome;
import GeneticAlgorithm.GeneticAlgorithm;

public class AiSimulator {
	
	
	private NeuralNetwork network;
	private Game game;
	private GeneticAlgorithm evolve;
	private int eliteSampleSize = 1;
	private int boltzSampleSize = 1;
	private double boltzTemperature = 1; 
	
	public AiSimulator (NeuralNetwork network, GeneticAlgorithm algorithm ,Game game){
		this.network = network;
		this.game = game;
		this.evolve = algorithm;
	}
	
	public void runSimulation (int generations, long generationDuration, int runsPerGeneration ) throws DuplicateNeuronID_Exception{
		long currentTime ;
		long stopTime;
		
		while (evolve.getGeneration() < generations){
			currentTime = System.currentTimeMillis();
			stopTime = currentTime + generationDuration;
			int genomeIndex = 0;
			int runs = 0;
			while (currentTime < stopTime && runs < runsPerGeneration){
				Genome genome = evolve.getPopulation().get(genomeIndex);
				network.setWeights(genome);
				network.update();
				evolve.evaluateGenome(genome);
				currentTime = System.currentTimeMillis();
				if (genomeIndex == evolve.getPopulation().size()-1){genomeIndex = 0;}
				else {genomeIndex++;}
				runs++;
			}
			evolve.evaluateGeneration(null);
			evolve.newGeneration( eliteSampleSize , boltzSampleSize, boltzTemperature, evolve.getPopulation().size());
		}
	}
}
