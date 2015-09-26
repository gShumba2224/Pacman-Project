package AI;

import Neurons.DuplicateNeuronID_Exception;
import Neurons.InputConnection;
import  Neurons.NeuralNetwork;
import Neurons.Neuron;
import GeneticAlgorithm.Evolve;
import GeneticAlgorithm.Genome;

public class AI {
	
	public NeuralNetwork network;
	
	public AI (){
		try {
			network = new NeuralNetwork(3, 1, 3, 3);
			
			//current pOS
			network.getInputLayer().getNeurons().get(0).setOutputValue(2);
			
			// GOOD POS
			network.getInputLayer().getNeurons().get(1).setOutputValue(1);
			
			// BAD POS
			network.getInputLayer().getNeurons().get(2).setOutputValue(3);
			
			Evolve alg = new Evolve();
			alg.setMaxGeneVal(1.0);
			alg.setMinGeneVal(-1.0);
			alg.setMutationRate(0.1);
			alg.setMutationScale(1);
			alg.newRandomPopulation(network, 20, "a");
			int temp = 30;
			for (int i = 0; i < 10000 ; i++){
				for (Genome genome : alg.getPopulation()){
					network.setWeights(genome);
					network.update();
					genome.getFitnessProperties().put("a", 0.0);
					if (network.getOutputs().get(0) > 0.9 && network.getOutputs().get(1) < 0.1){
						genome.setOverallFitness(genome.getOverallFitness() + 1);
						genome.getFitnessProperties().put("a", genome.getOverallFitness()+1);
					}
					System.out.println(genome.getOverallFitness() + "__ID=_" + genome.getID() + "_(weig1_=" +
					network.getOutputs().get(0) +  "_weig2_=" + network.getOutputs().get(1) + ")");
					temp--;
				}
				alg.evaluateGeneration(null);
				System.out.println("___________________________________"+ alg.getOverallFitnessAverage());
				if (alg.getOverallFitnessAverage() > 0.79){
					break;
				}
				alg.newGeneration(7, 7, 10 , 20);
			}
//			network.setWeights(alg.getPopulation().get(0));
//			network.update();
//			for (Neuron neuron : network.getAllNeurons().values()){
//				int count = 0;
//				for (InputConnection connection :neuron.getInputConnections() ){
//					System.out.println(connection.getWeight()+ "__"+ neuron.getBias()+ " <" +count+"_~Index~_"+neuron.getID() + ">");
//					count++;
//				}
//			}
		} catch (DuplicateNeuronID_Exception e) {
			System.out.println(e.getDuplicatedIDs());
		}
		
	}
	
	public static void main(String[] args){
		AI x = new AI();
	}
		
}
