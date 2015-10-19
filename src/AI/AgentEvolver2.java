package AI;

import java.util.List;

import Agents.GenericAgent;
import Agents.Ghost;
import Game.Game;
import Game.Move;
import GeneticAlgorithm.Evolve;
import GeneticAlgorithm.Genome;
import Neurons.NeuralNetwork;

public class AgentEvolver2 extends AgentEvolver {
	
	private final int  EVADE = 0;
	private final int  EAT = 1;
	

	public AgentEvolver2(Game game, NeuralNetwork network, int population, int agentType,
			double minWeight,double maxWeight) {
		super();
		initAlgorithm (network, population, minWeight,maxWeight,"points");
	}

	protected Integer outputToDirection (NeuralNetwork network){
		List <Double> outputs = network.getOutputs();
		if (outputs.get(0) > outputs.get(1) ){
			return EVADE;
		}else{
			return EAT;
		}
	}
	
	protected void evaluatePacman (int moveResult, GenericAgent agent){
		Genome genome = agent.getController();
		if (moveResult == Move.GOT_POWER_PILL){
			increamentPerformance(genome, 1, "points");
		}else if (moveResult == Move.GOT_STANDARD_PILL){
			increamentPerformance(genome, 0.1, "points");
		}else if (moveResult == Move.KILLED_ENEMY){
			increamentPerformance(genome, 2, "points");
		}else if (moveResult == Move.GOT_KILLED){
			decreamentPerformance(genome, 5, "points");
		}
	}
	
	protected void initAlgorithm (NeuralNetwork network, int populationSize, 
			double minWeight,double maxWeight,String...parms){
		Evolve evole =   new Evolve (network, populationSize ,minWeight,maxWeight,parms){
			@Override
			public void evaluateGenome (Genome genome, Object...parms){
				NeuralNetwork network =(NeuralNetwork) parms[0];
				Game game = (Game) parms[1];
				GenericAgent agent = (GenericAgent)parms[2];
				
				InputReader2 reader = (InputReader2) network.getInputReader();
				int action = outputToDirection(network);
				int result = 0;
				if (action == EVADE){
				result = Move.moveAgent(agent, game, reader.getSafeBlock().getGridPosition());}
				else  { result = Move.moveAgent(agent, game, reader.getPointBlock().getGridPosition());}
				evaluatePacman(result,agent);
				
			}
			@Override
			public void preEvolutionActions (){
				generationLog();
			}
		};
		this.setAlgorithm(evole);
	}

}
