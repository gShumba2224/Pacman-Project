package AI;

import java.io.EOFException;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import Agents.GenericAgent;
import Agents.Ghost;
import Game.Game;
import Game.Move;
import Game.UnstickAgent;
import GeneticAlgorithm.Evolve;
import GeneticAlgorithm.Genome;
import Neurons.NeuralNetwork;
import PacmanGrid.Grid;

public class AgentEvolver2 extends AgentEvolver {
	
	public final int  EVADE = 0;
	public final int  EAT = 1;
	private InputStream fileInputStream ;
	private ObjectInputStream objInputStream;
	private URL resource;
	private FileOutputStream fileOutStream;
	private ObjectOutputStream objOutStream;
	
	

	public AgentEvolver2(Game game, NeuralNetwork network, int population, int agentType,
			double minWeight,double maxWeight) {
		super();
		initAlgorithm (network, population, minWeight,maxWeight,"points");
	}

	protected Integer outputToDirection (NeuralNetwork network){
		Random rand = new Random();
		int action = EAT;
		List <Double> outputs = network.getOutputs();
		if (outputs.get(0) > outputs.get(1) ){
			action = EAT;
		}else if (outputs.get(0) < outputs.get(1) ){
			action = EVADE;
		}else if (outputs.get(0) == outputs.get(1) ){
			if (rand.nextDouble() >= 0.5){action = EVADE;}
			else {action = EAT;}
		}
		return action;
	}
	
	protected void evaluatePacman (int moveResult, GenericAgent agent){
		Genome genome = agent.getController();
		if (moveResult == Move.GOT_POWER_PILL){
			increamentPerformance(genome, 1, "points");
		}else if (moveResult == Move.GOT_STANDARD_PILL){
			increamentPerformance(genome, 0.05, "points");
		}else if (moveResult == Move.KILLED_ENEMY){
			increamentPerformance(genome, 2, "points");
		}else if (moveResult == Move.GOT_KILLED){
			decreamentPerformance(genome, 5, "points");
		}
	}
	
	private void saveFittest (double threshold){
		List <Genome> fittest = readFittestList();
		try {
			resource = AgentEvolver2.class.getClassLoader().getResource("AI/FitGenomes.ai");
			fileOutStream = new FileOutputStream(new File (resource.getPath()),false);
			objOutStream = new ObjectOutputStream(fileOutStream);
			for (Genome genome : this.getAlgorithm().getPopulation()){
				if (genome.getOverallFitness() > threshold){fittest.add(genome);}
			}
			objOutStream.writeObject(fittest);
			objOutStream.close();
		} catch (IOException e) {e.printStackTrace();}
	}
	
	private List <Genome> readFittestList (){
		List <Genome>  fittestList = null;
		fileInputStream =  AgentEvolver2.class.getClassLoader().getResourceAsStream("AI/FitGenomes.ai");
		try {
			objInputStream = new ObjectInputStream(fileInputStream);
			fittestList = (ArrayList <Genome>) objInputStream.readObject();
			objInputStream.close();
		} catch (ClassNotFoundException | IOException  e) {
			if (e instanceof  EOFException){ fittestList = new ArrayList<Genome>();}
			else{e.printStackTrace();}
		}
		return fittestList;
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
				saveFittest(180.0);
			}
		};
		this.setAlgorithm(evole);
	}

}
