package AI;

import Agents.GenericAgent;
import Game.Game;
import GeneticAlgorithm.GeneticAlgorithm;
import GeneticAlgorithm.Genome;
import Neurons.DuplicateNeuronID_Exception;
import Neurons.NeuralNetwork;

public class AiMonitor {

	public int nextPlayer = GenericAgent.GHOST;
	public AiMonitor (int player){
		nextPlayer = player;
	}
	public synchronized void play (NeuralNetwork network,GeneticAlgorithm algorithm,Genome genome, 
			GenericAgent agent, Game game, int playerType) throws DuplicateNeuronID_Exception{
		while (playerType != nextPlayer){
	      try {  wait();} 
	      catch (InterruptedException e) {}
		}
		network.getInputReader().readInputs (network, agent);
		network.setWeights(genome);
		network.update();
		algorithm.evaluateGenome(genome,network,playerType, game, agent);	
		if (nextPlayer == GenericAgent.GHOST){nextPlayer = GenericAgent.PACMAN;}
		else if (nextPlayer == GenericAgent.PACMAN){ nextPlayer = GenericAgent.GHOST;}
		notifyAll();
	}

}
