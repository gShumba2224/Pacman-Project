package AI;

import java.util.Timer;
import java.util.TimerTask;

import com.sun.media.jfxmedia.events.PlayerTimeListener;

import Agents.GenericAgent;
import Game.Game;
import GeneticAlgorithm.GeneticAlgorithm;
import GeneticAlgorithm.Genome;
import Neurons.DuplicateNeuronID_Exception;
import Neurons.NeuralNetwork;

public class AiMonitor {

	
	public int nextPlayer = GenericAgent.GHOST;
	public int resetKey = GenericAgent.GHOST;
	private Timer timer = new Timer();
	public AiMonitor (int player){
		nextPlayer = player;
	}
	

	public synchronized void play (NeuralNetwork network,GeneticAlgorithm algorithm,Genome genome, 
			int agentIndex, Game game, int playerType) throws DuplicateNeuronID_Exception{
		
		while (playerType != nextPlayer){
			try {  wait();} 
			catch (InterruptedException e) {}
		}
		
		GenericAgent agent = game.getAgents().get(playerType).get(agentIndex);
		network.getInputReader().readInputs (network, agent, game);
		network.setWeights(genome);
		network.update();
		algorithm.evaluateGenome(genome,network, game, agent);	
		long stopTime = System.currentTimeMillis() + (20);
		long currentTime = System.currentTimeMillis();
		while (currentTime < stopTime && algorithm.getGeneration() > 700){
			 currentTime = System.currentTimeMillis();
			//System.out.println("waitng");
		}
		if (nextPlayer == GenericAgent.GHOST){nextPlayer = GenericAgent.PACMAN;}
		else if (nextPlayer == GenericAgent.PACMAN){ nextPlayer = GenericAgent.GHOST;}
		//System.out.println("doingsz >>"  + playerType);
		notifyAll();
	}
	
	public synchronized void restAgents (int agentType, Game game){
		while (resetKey != agentType){
			try {  wait();} 
			catch (InterruptedException e) {}
		}
		game.reset(agentType);
		notifyAll();
	}
	


}
