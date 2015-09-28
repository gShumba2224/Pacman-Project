package Actions;

import org.junit.Test;

import Game.Game;
import GeneticAlgorithm.Evolve;
import GeneticAlgorithm.Genome;
import Neurons.DuplicateNeuronID_Exception;
import Neurons.NeuralNetwork;
import Neurons.Neuron;

public class AiSimulatorTest {
	AiSimulator unit ;
	
	@Test
	public void runSimulationTest (){
		NeuralNetwork network = new NeuralNetwork(3, 3, 3, 2);
		for (Neuron neuron : network.getInputLayer().getNeurons()){
			neuron.setOutputValue(10);
		}
		Evolve evolve = new Evolve(network, 100, "a","b"){
			@Override
			public void evaluateGenome (Object...parm){
				//System.out.println("evaluate");
				Genome g =(Genome) parm[0];
				if (network.getOutputs().get(0) > 0.5){
					System.out.println("fitness_" + g.getOverallFitness() + "_ID=" + g.getID() + " gen " + this.getGeneration());
					g.setOverallFitness(g.getOverallFitness()+1);
					double i = g.getFitnessProperties().get("a");
					g.getFitnessProperties().put("a", i);
					//System.out.println(g.getOverallFitness());
				}
			}
		};
		Game game = new Game ();
		unit = new AiSimulator(network, evolve, game);
		try {
			unit.runSimulation(3, 100 * 1,8);
		} catch (DuplicateNeuronID_Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
