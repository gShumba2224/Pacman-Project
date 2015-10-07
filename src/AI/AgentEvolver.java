package AI;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import Actions.Move;
import Agents.GenericAgent;
import Game.Game;
import GeneticAlgorithm.Evolve;
import GeneticAlgorithm.GeneticAlgorithm;
import GeneticAlgorithm.Genome;
import Neurons.NeuralNetwork;
import Neurons.Neuron;
import Utils.IntDimension;

interface PerformanceCommand {
	public void run (Genome genome);
}

interface MoveCommand {
	public int run (NeuralNetwork network, GenericAgent agent, Game game);
}

public class AgentEvolver  {
	
	private GeneticAlgorithm algorithm; 
	private Map <Integer,PerformanceCommand> ratePerformance = new HashMap <Integer,PerformanceCommand> ();
	private Map <String,MoveCommand> agentMovement =new HashMap <String,MoveCommand> ();
	private static final String LEFT = "1000";
	private static final String RIGHT = "0100";
	private static final String UP = "0010";
	private static final String DOWN = "0001";

	public AgentEvolver (NeuralNetwork network, int population,int agentType, double minBias, double maxBias, 
			double minWeight,double maxWeight,String...fitnessProperties){
		
		if (agentType == GenericAgent.PACMAN){
			 initAlgorithm (network, population ,minBias,maxBias,minWeight,maxWeight,
					 "move","enemyAvoid","points","enemyKills");
		}else{
			 initAlgorithm(network, population,minBias,maxBias,minWeight,maxWeight,
					 "move","enemyAvoid","enemyKills");
		}
		initPerformanceMap ();
		initMoveMap ();
	}
	
	public AgentEvolver (NeuralNetwork network, int population,int agentType){
		if (agentType == GenericAgent.PACMAN){
			 initAlgorithm (network, population,0.0, 1.0, 0.0, 1.0,"move","enemyAvoid","points","enemyKills");
		}else{
			 initAlgorithm(network, population ,0.0, 1.0, 0.0, 1.0, "move","enemyAvoid","enemyKills");
		}
		initPerformanceMap ();
		initMoveMap ();
	}
	private  void increamentPerformance (Genome genome, double increament, String property){
		double currentVal = genome.getFitnessProperties().get(property);
		currentVal = currentVal + increament;
		genome.getFitnessProperties().replace(property, currentVal);
	}
	
	private  void decreamentPerformance (Genome genome, double decreament, String property){
		Double currentVal = genome.getFitnessProperties().get(property);
		currentVal = currentVal - decreament;
		genome.getFitnessProperties().replace(property, currentVal);
		//System.out.println(currentVal + " **CURRENT VAL lol MOIT * " + property);
		
	}
	private void initPerformanceMap (){
		
		ratePerformance.put( Move.GOT_GRAPE_PILL, new PerformanceCommand () {
			@Override
			public void run(Genome genome) {
				increamentPerformance(genome, Move.GOT_GRAPE_PILL, "points");
				increamentPerformance(genome, 5, "move");
				increamentPerformance(genome, 5, "enemyAvoid");
			}
		});
		ratePerformance.put( Move.GOT_POWER_PILL, new PerformanceCommand () {
			@Override
			public void run(Genome genome) {
				increamentPerformance(genome, Move.GOT_POWER_PILL, "points");
				increamentPerformance(genome, 5, "move");
				increamentPerformance(genome, 5, "enemyAvoid");
			}
		});
		
		ratePerformance.put( Move.GOT_STANDARD_PILL, new PerformanceCommand () {
			@Override
			public void run(Genome genome) {
				increamentPerformance(genome, Move.GOT_STANDARD_PILL, "points");
				increamentPerformance(genome, 5, "move");
				increamentPerformance(genome, 5, "enemyAvoid");
			}
		});
		
		ratePerformance.put( Move.GOT_NONE_PILL, new PerformanceCommand () {
			@Override
			public void run(Genome genome) {
				increamentPerformance(genome, 5, "move");
				increamentPerformance(genome, 5, "enemyAvoid");
			}
		});
		
		ratePerformance.put( Move.GOT_KILLED, new PerformanceCommand () {
			@Override
			public void run(Genome genome) {
				increamentPerformance(genome,1, "move");
			}
		});
		
		ratePerformance.put( Move.KILLED_ENEMY, new PerformanceCommand () {
			@Override
			public void run(Genome genome) {
				increamentPerformance(genome, 20, "enemyKills");
				increamentPerformance(genome, 5, "move");
			}
		});
		
		ratePerformance.put( Move.HITWALL, new PerformanceCommand () {
			@Override
			public void run(Genome genome) {
				//decreamentPerformance(genome, 10, "move");
			}
		});
	}
	
	private void initMoveMap (){
		agentMovement.put(LEFT, new MoveCommand () {
			@Override
			public int run(NeuralNetwork network, GenericAgent agent, Game game) {
				IntDimension newPos = new IntDimension (agent.getLocation().X-1, agent.getLocation().Y);
				return Move.moveAgent(agent, game, newPos);}} );
		
		agentMovement.put(RIGHT, new MoveCommand () {
			@Override
			public int run(NeuralNetwork network, GenericAgent agent, Game game) {
				IntDimension newPos = new IntDimension (agent.getLocation().X+1, agent.getLocation().Y);
				return Move.moveAgent(agent, game, newPos);}} );
		
		agentMovement.put(UP, new MoveCommand () {
			@Override
			public int run(NeuralNetwork network, GenericAgent agent, Game game) {
				IntDimension newPos = new IntDimension (agent.getLocation().X, agent.getLocation().Y+1);
				return Move.moveAgent(agent, game, newPos);}} );
		
		agentMovement.put(DOWN, new MoveCommand () {
			@Override
			public int run(NeuralNetwork network, GenericAgent agent, Game game) {
				IntDimension newPos = new IntDimension (agent.getLocation().X, agent.getLocation().Y-1);
				return Move.moveAgent(agent, game, newPos);}} );
	}
	
	private String outputToKey (NeuralNetwork network){
		StringBuilder builder = new StringBuilder ();
		System.out.println("$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$");
		int i= 0;
		double highest = network.getOutputs().get(0);
		for (double value : network.getOutputs()){
			System.out.println(i);
//			if (value >= 0.5){builder.append("1");}
//			else { builder.append("0");}
			if (value > highest ){
				highest = value;
				i++;
			}
		
		}
		String x = "";
		if (i == 0) {x = "1000";}
		if (i == 1) {x = "0100";}
		if (i == 2) {x = "0010";}
		if (i == 3) {x = "0001";}
		//System.out.println(x);
		return x;//builder.toString();
	}
	

	private void initAlgorithm (NeuralNetwork network, int population, double minBias, double maxBias, 
			double minWeight,double maxWeight,String...parms){
		algorithm =   new Evolve (network, population ,minBias,maxBias,minWeight,maxWeight,parms){
			@Override
			public void evaluateGenome (Genome genome, Object...parms){
				NeuralNetwork network =(NeuralNetwork) parms[0];
				Game game = (Game)parms[1];
				GenericAgent agent = (GenericAgent)parms[2];
				String key = outputToKey (network);
				MoveCommand command = agentMovement.get(key);
				if (command != null){
					int result = command.run(network, agent, game);
					PerformanceCommand pefCommand = ratePerformance.get(result);
					pefCommand.run (genome);
				}
			}
		};
	}
	
	public GeneticAlgorithm getAlgorithm() {return algorithm;}

	public void setAlgorithm(GeneticAlgorithm algorithm) {this.algorithm = algorithm;}

	public Map<Integer, PerformanceCommand> getRatePerformance() {return ratePerformance;}

	public void setRatePerformance(Map<Integer, PerformanceCommand> ratePerformance) {this.ratePerformance = ratePerformance;}

	public Map<String, MoveCommand> getMoveAgent() {return agentMovement;}

	public void setMoveAgent(Map<String, MoveCommand> agentMovement) {this.agentMovement = agentMovement;}

	
	
//	public static void main (String[] args){
//
//		
//	}
	//algorithm.evaluateGenome(genome,network,playerType, game, agent);	
//	@Override
//	public void evaluateGenome (Genome genome , Object...extraParms){
		
//	}
	
//    public int moveAction (NeuralNetwork network , GenericAgent agent) {
//		List<Double> outputs = network.getOutputs();
//    }
}

//interface Command {
//    void runCommand();
//}
//
//public class Test {
//
//    public static void main(String[] args) throws Exception {
//        Map<Character, Command> methodMap = new HashMap<Character, Command>();
//
//        methodMap.put('h', new Command() {
//            public void runCommand() { System.out.println("help"); };
//        });
//
//        methodMap.put('t', new Command() {
//            public void runCommand() { System.out.println("teleport"); };
//        });
//
//        char cmd = 'h';
//        methodMap.get(cmd).runCommand();  // prints "Help"
//
//        cmd = 't';
//        methodMap.get(cmd).runCommand();  // prints "teleport"
//
//    }
//}