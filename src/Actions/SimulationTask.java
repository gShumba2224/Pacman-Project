package Actions;

import java.util.TimerTask;

import GeneticAlgorithm.Genome;
import Neurons.DuplicateNeuronID_Exception;

public class SimulationTask extends TimerTask{
	
	private int count = 0;
	@Override
	public void run() {
		 
//			Genome genome = evolve.getPopulation().get( genomeIndex);
//			try {network.setWeights(genome);} 
//			catch (DuplicateNeuronID_Exception e) {
//				e.printStackTrace();
//				System.out.println(e.getDuplicatedIDs() );
//				this.cancel();
//		
		count = count + 1;
		System.out.println(count);
	
	}

}
