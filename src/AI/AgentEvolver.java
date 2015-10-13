package AI;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import Actions.Move;
import Agents.GenericAgent;
import Agents.Ghost;
import Agents.Pacman;
import Game.Game;
import GeneticAlgorithm.Evolve;
import GeneticAlgorithm.GeneticAlgorithm;
import GeneticAlgorithm.Genome;
import Neurons.InputConnection;
import Neurons.NeuralNetwork;
import Neurons.Neuron;
import Search.ArtifactSearch;
import Utils.DistanceCalculator;
import Utils.IntDimension;


public class AgentEvolver extends Evolve  {
	
	private GeneticAlgorithm algorithm; 
	private static final int LEFT = 0;
	private static final int RIGHT = 1;
	private static final int UP = 2;
	private static final int DOWN = 3;

	public AgentEvolver (NeuralNetwork network, int population,int agentType, 
			double minWeight,double maxWeight,String...fitnessProperties){
		
		if (agentType == GenericAgent.PACMAN){
			 initAlgorithm (network, population, minWeight,maxWeight,
					 "powerPill","standardPill","enemyKills","enemyAvoid","moveDir");
		}else{
			 initAlgorithm(network, population,minWeight,maxWeight,"enemyKills","moveDir","enemyAvoid");
		}
	}

	public AgentEvolver (NeuralNetwork network, int population,int agentType){
		if (agentType == GenericAgent.PACMAN){
			 initAlgorithm (network, population, -1, 1.0,"powerPill",
					 "standardPill","enemyKills","enemyAvoid","moveDir");
					
		}else{
			 initAlgorithm(network, population ,-1.0, 1.0,"enemyKills","moveDir","enemyAvoid");
		}
	}
	private  void increamentPerformance (Genome genome, double increament, String property){
		double currentVal = genome.getFitnessProperties().get(property);
		currentVal = currentVal + increament;
		genome.getFitnessProperties().replace(property, currentVal);
	}
	
	private  void decreamentPerformance (Genome genome, double decreament, String property){
		Double currentVal = genome.getFitnessProperties().get(property);
		currentVal = currentVal - decreament;
		if (currentVal < 0.0){currentVal = 0.0;}
		genome.getFitnessProperties().replace(property, currentVal);
	}
	
	private Integer outputToDirection (NeuralNetwork network){
		int index= 0;
		double highest = network.getOutputs().get(0);
		for (int i = 0; i <network.getOutputs().size();i++){
			if (network.getOutputs().get(i) > highest){
				highest = network.getOutputs().get(i);
				index = i;
			}
		}
		return index;
	}
	
	private int moveAgent (Game game,GenericAgent agent, int direction){
		
		IntDimension newPos = new IntDimension(agent.getLocation().X, agent.getLocation().Y);
		IntDimension old = agent.getLocation();
		if (direction == LEFT){newPos.X = newPos.X-1;}
		else if (direction == RIGHT){newPos.X = newPos.X+1;}
		else if (direction == UP){newPos.Y = newPos.Y-1;}
		else if (direction == DOWN){newPos.Y = newPos.Y+1;}
		int result = Move.moveAgent(agent, game, newPos);
		return result;
	}
	
	private void evaluatePerformance (int moveResult, Genome genome){
		if (moveResult == Move.GOT_POWER_PILL){
			increamentPerformance(genome, 1, "powerPill");
		}else if (moveResult == Move.GOT_STANDARD_PILL){
			increamentPerformance(genome, 0.1, "standardPill");
		}else if (moveResult == Move.KILLED_ENEMY){
			increamentPerformance(genome, 2, "enemyKills");
		}else if (moveResult == Move.GOT_KILLED){
			decreamentPerformance(genome, 3, "enemyAvoid");
		}else if (moveResult == Move.HITWALL){
			decreamentPerformance(genome, 0.2, "moveDir");
		}
	}


	private void initAlgorithm (NeuralNetwork network, int population, 
			double minWeight,double maxWeight,String...parms){
		algorithm =   new Evolve (network, population ,minWeight,maxWeight,parms){
			@Override
			public void evaluateGenome (Genome genome, Object...parms){
				NeuralNetwork network =(NeuralNetwork) parms[0];
				Game game = (Game)parms[1];
				GenericAgent agent = (GenericAgent)parms[2];
				
				int direction = outputToDirection(network);
				int result = moveAgent(game,agent,direction);
				evaluatePerformance(result,genome);
			}
		};
	}
	
	public GeneticAlgorithm getAlgorithm() {return algorithm;}

	public void setAlgorithm(GeneticAlgorithm algorithm) {this.algorithm = algorithm;}

}
