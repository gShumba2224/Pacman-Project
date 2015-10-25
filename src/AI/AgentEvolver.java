package AI;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import Agents.GenericAgent;
import Agents.Ghost;
import Agents.Pacman;
import Game.Game;
import Game.Move;
import GeneticAlgorithm.Evolve;
import GeneticAlgorithm.GeneticAlgorithm;
import GeneticAlgorithm.Genome;
import Neurons.InputConnection;
import Neurons.NeuralNetwork;
import Neurons.Neuron;
import PacmanGrid.Block;
import PacmanGrid.Pill;
import PacmanGrid.Road;
import Search.ArtifactSearch;
import Utils.DistanceCalculator;
import Utils.IntDimension;
import javafx.application.Platform;


public class AgentEvolver  {
	
	private GeneticAlgorithm algorithm; 
	private static final int LEFT = 0;
	private static final int RIGHT = 1;
	private static final int UP = 2;
	private static final int DOWN = 3;
	private Game game;

	protected AgentEvolver (){
	}
	
	public AgentEvolver (Game game,NeuralNetwork network, int population,int agentType, double minWeight,double maxWeight){
		this.game = game;
		if (agentType == GenericAgent.PACMAN){
			 initAlgorithm (network, population, minWeight,maxWeight,"points");
			// "powerPill","standardPill","enemyKills","enemyAvoid"
			
//			 for (Genome genome : algorithm.getPopulation()){
//					genome.getFitnessProperties().replace("enemyAvoid", 3.0);}
			 
		}else{initAlgorithm(network, population,minWeight,maxWeight,"moveDir","enemyKills","enemyAvoid");}
	}

	protected  void increamentPerformance (Genome genome, double increament, String property){
		double currentVal = genome.getFitnessProperties().get(property);
		currentVal = currentVal + increament;
		genome.getFitnessProperties().replace(property, currentVal);
	}
	
	protected  void decreamentPerformance (Genome genome, double decreament, String property){
		double currentVal = genome.getFitnessProperties().get(property);
		currentVal = currentVal - decreament;
		if (currentVal < 0){currentVal = 0;}
		genome.getFitnessProperties().replace(property, currentVal);
	}

	protected Integer outputToDirection (NeuralNetwork network){
		int index= 0;
		Random rand = new Random ();
		double highest = network.getOutputs().get(0);
		for (int i = 0; i <network.getOutputs().size();i++){
			if (network.getOutputs().get(i) > highest){
				highest = network.getOutputs().get(i);
				index = i;
			}else if (highest == network.getOutputs().get(i) ){
				if (rand.nextDouble() >= 0.5){index = i;}
			}
		}
		return index;
	}
	
	private int moveAgent (Game game,GenericAgent agent, int direction){
		
		IntDimension newPos = new IntDimension(agent.getLocation().X, agent.getLocation().Y);
		if (direction == LEFT){newPos.X = newPos.X-1;}
		else if (direction == RIGHT){newPos.X = newPos.X+1;}
		else if (direction == UP){newPos.Y = newPos.Y-1;}
		else if (direction == DOWN){newPos.Y = newPos.Y+1;}
		int result = Move.moveAgent(agent, game, newPos);
		return result;
	}
	
	private void evaluatePacman (int moveResult, GenericAgent agent){
		Genome genome = agent.getController();
		if (moveResult == Move.GOT_POWER_PILL){
			increamentPerformance(genome, 1, "powerPill");
		}else if (moveResult == Move.GOT_STANDARD_PILL){
			increamentPerformance(genome, 0.1, "standardPill");
		}else if (moveResult == Move.KILLED_ENEMY){
			increamentPerformance(genome, 2, "enemyKills");
			decreamentPerformance(Move.getVictim().getController(),0.5, "enemyAvoid");
		}else if (moveResult == Move.GOT_KILLED){
			decreamentPerformance(genome,0.5, "enemyAvoid");
		}
	}
	
	private void evaluateGhost (NeuralNetwork network,GenericAgent agent,int direction, int result){
		int correctDirection = 0;
		int count = 0;
		double highest = network.getInputLayer().getNeurons().get(0).getOutputValue();
		for (Neuron neuron : network.getInputLayer().getNeurons()){
			double value = neuron.getOutputValue();
			if ( highest < value){
				correctDirection = count;
				highest = value;
			}
			count++;
		}
		if (direction == correctDirection){increamentPerformance(agent.getController(),0.1, "moveDir");}
		else {decreamentPerformance(agent.getController(),0.1, "moveDir");}
		if (result == Move.KILLED_ENEMY){
			increamentPerformance(agent.getController(),1, "enemyKills");
			increamentPerformance(agent.getController(),0.1, "moveDir");
			//decreamentPerformance(Move.getVictim().getController(),0.5, "enemyAvoid");
		}else if (result == Move.GOT_KILLED){
			increamentPerformance(agent.getController(),1, "enemyAvoid");
		}
	}

	
	protected void initAlgorithm (NeuralNetwork network, int populationSize, 
			double minWeight,double maxWeight,String...parms){
		algorithm =   new Evolve (network, populationSize ,minWeight,maxWeight,parms){
			@Override
			public void evaluateGenome (Genome genome, Object...parms){
				NeuralNetwork network =(NeuralNetwork) parms[0];
				Game game = (Game) parms[1];
				GenericAgent agent = (GenericAgent)parms[2];
				
				int direction = outputToDirection(network);
				int result = moveAgent(game,agent,direction);
				if (agent instanceof Ghost){evaluateGhost(network,agent,direction,result);}
				else { evaluatePacman(result,agent);}
				
			}
			@Override
			public void preEvolutionActions (){
				generationLog();
			}
//			@Override
//			public void postEvolutionActions (){
//			for (Genome genome : algorithm.getPopulation()){
//			genome.getFitnessProperties().replace("enemyAvoid", 3.0);}
//			}
		};
		algorithm.postEvolutionActions();
	}
	
	public GeneticAlgorithm getAlgorithm() {return algorithm;}

	public void setAlgorithm(GeneticAlgorithm algorithm) {this.algorithm = algorithm;}

	public Game getGame() {return game;}

	public void setGame(Game game) {this.game = game;}
	

}
