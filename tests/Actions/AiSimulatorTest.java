package Actions;

import static org.junit.Assert.*;

import java.io.IOException;
import java.util.ArrayList;

import org.junit.Test;

import AI.AiMonitor;
import AI.AiSimulator;
import Agents.GenericAgent;
import Agents.Ghost;
import Agents.Pacman;
import Game.Game;
import GeneticAlgorithm.Evolve;
import GeneticAlgorithm.GeneticAlgorithm;
import GeneticAlgorithm.Genome;
import Neurons.DuplicateNeuronID_Exception;
import Neurons.InputConnection;
import Neurons.NeuralNetwork;
import Neurons.Neuron;

public class AiSimulatorTest {
	AiSimulator unit;
	@Test
	public void test() {
			
		ArrayList <Integer> idList = new ArrayList <Integer>();
		NeuralNetwork network = new NeuralNetwork(3, 3, 3, 3);
		Game game = null;
		Evolve evolve = new Evolve(network, 20, "a","b"){
			@Override 
			public void evaluateGenome (Genome genome,Object...parameters){
				idList.add(genome.getID());
				System.out.println("/////////////////////////////////////");
			}
		};

		game = new Game();
		for (int i = 0 ; i < evolve.getPopulation().size() ; i++){
			game.getPacmen().add(new Pacman ());
			game.getGhosts().add(new Ghost () );
		}
		game.setDuration(1000*30);
		unit = new AiSimulator(network, evolve, game ,GenericAgent.PACMAN){

			@Override
			public void readInputs(NeuralNetwork network) {
				for (int i = 0; i < network.getInputLayer().getNeurons().size(); i++){
					network.getInputLayer().getNeurons().get(i).setOutputValue( i + evolve.getGeneration());
				}
			}
			@Override
			public int applyAction(Genome genome, Object... parameters) {
				return 0;
			}
		};
		unit.setSelectionProperties(5, 5, 20);
		
		NeuralNetwork network2 = new NeuralNetwork(4, 4, 4, 4);
		Evolve evolve2 = new Evolve(network2, evolve.getPopulation().size(), "x","y");
		AiSimulator unit2 =  new AiSimulator(network, evolve2, game ,GenericAgent.GHOST){

			@Override
			public void readInputs(NeuralNetwork network) {
				for (int i = 0; i < network.getInputLayer().getNeurons().size(); i++){
					network2.getInputLayer().getNeurons().get(i).setOutputValue( i + evolve2.getGeneration());
					System.out.println("****************************");
				}
			}
			@Override
			public int applyAction(Genome genome, Object... parameters) {
				return 0;
			}
		};
		
		AiMonitor monitor = new AiMonitor(GenericAgent.PACMAN);

		unit.setRunsPerGenome(1);
		unit.setGenerations(3);
		unit2.setRunsPerGenome(1);
		unit2.setGenerations(3);
		unit.setMonitor(monitor);
		unit2.setMonitor(monitor);
	
		( new Thread(unit,"lol")).start();
	     //(new Thread(unit2,"dad")).start();
	//	assertEquals(evolve.getGeneration(), 3);
//		for (int i = 0 ; i < network.getInputLayer().getNeurons().size(); i++){
//			double input = network.getInputLayer().getNeurons().get(i).getOutputValue();
//			Neuron neuron = network.getInputLayer().getNeurons().get(i);
//			assertEquals(input + 1.0, i + evolve.getGeneration(),0.001);
//		}
	}
	


}
