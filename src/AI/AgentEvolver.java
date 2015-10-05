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
	
	
	public AgentEvolver (NeuralNetwork network, int population,int agentType){
		if (agentType == GenericAgent.PACMAN){
			 initAlgorithm (network, population ,"move","enemyAvoid","points","enemyKills");
		}else{
			 initAlgorithm(network, population, "move","enemyAvoid","enemyKills");
		}
		initPerformanceMap ();
		initMoveMap ();
	}
	private synchronized void increamentPerformance (Genome genome, double increament, String property){
		double currentVal = genome.getFitnessProperties().get(property);
		System.out.println(currentVal + " **CURRENT VALL MOIT* " + property);
		currentVal = currentVal + increament;
		genome.getFitnessProperties().replace(property, currentVal);
	}
	
	private  void decreamentPerformance (Genome genome, double decreament, String property){
		Double currentVal = genome.getFitnessProperties().get(property);
		currentVal = currentVal - decreament;
		genome.getFitnessProperties().replace(property, currentVal);
		System.out.println(currentVal + " **CURRENT VAL lol MOIT * " + property);
		
	}
	private void initPerformanceMap (){
		
		ratePerformance.put( Move.GOT_GRAPE_PILL, new PerformanceCommand () {
			@Override
			public void run(Genome genome) {
				increamentPerformance(genome, Move.GOT_GRAPE_PILL/10.0, "points");
				increamentPerformance(genome, 0.05, "move");
				increamentPerformance(genome, 0.1, "enemyAvoid");
			}
		});
		ratePerformance.put( Move.GOT_POWER_PILL, new PerformanceCommand () {
			@Override
			public void run(Genome genome) {
				increamentPerformance(genome, Move.GOT_POWER_PILL/10.0, "points");
				increamentPerformance(genome, 0.05, "move");
				increamentPerformance(genome, 0.1, "enemyAvoid");}});
		
		ratePerformance.put( Move.GOT_STANDARD_PILL, new PerformanceCommand () {
			@Override
			public void run(Genome genome) {
				increamentPerformance(genome, Move.GOT_STANDARD_PILL/10.0, "points");
				increamentPerformance(genome, 0.05, "move");
				increamentPerformance(genome, 0.1, "enemyAvoid");}});
		
		ratePerformance.put( Move.GOT_NONE_PILL, new PerformanceCommand () {
			@Override
			public void run(Genome genome) {
				increamentPerformance(genome, 0.05, "move");
				increamentPerformance(genome, 0.1, "enemyAvoid");}});
		
		ratePerformance.put( Move.GOT_KILLED, new PerformanceCommand () {
			@Override
			public void run(Genome genome) {
				decreamentPerformance(genome, 2.0, "enemyAvoid");
				increamentPerformance(genome,0.05, "move");}});
		
		ratePerformance.put( Move.KILLED_ENEMY, new PerformanceCommand () {
			@Override
			public void run(Genome genome) {
				increamentPerformance(genome, 1.0, "enemyKills");
				increamentPerformance(genome, 0.05, "move");
				increamentPerformance(genome, 0.1, "enemyAvoid");}});
		
		ratePerformance.put( Move.HITWALL, new PerformanceCommand () {
			@Override
			public void run(Genome genome) {
				decreamentPerformance(genome, 0.1, "move");;}});
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
		for (double value : network.getOutputs()){
			if (value >= 0.5){builder.append("1");}
			else { builder.append("0");}
		}
		return builder.toString();
	}
	
	private void initAlgorithm (NeuralNetwork network, int population, String...parms){
		algorithm =   new Evolve (network, population ,parms){
			@Override
			public void evaluateGenome (Genome genome, Object...parms){
				NeuralNetwork network =(NeuralNetwork) parms[0];
				Game game = (Game)parms[1];
				GenericAgent agent = (GenericAgent)parms[2];
				String key = outputToKey (network);
				MoveCommand command = agentMovement.get(key);
				for (String parm : genome.getFitnessProperties().keySet()){
					if (genome.getFitnessProperties().get(parm) == null){
						System.out.println(parm +" g parms = " + genome.getFitnessProperties().get(parm));
					}
				}
				if (command != null){
					int result = command.run(network, agent, game);
					System.out.println("RESULT == " + result);
					ratePerformance.get(result).run (genome);
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