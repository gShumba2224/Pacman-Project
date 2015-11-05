package AI;

import java.io.EOFException;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.CountDownLatch;

import com.sun.javafx.scene.traversal.Algorithm;

import Agents.GenericAgent;
import Agents.Ghost;
import Agents.Pacman;
import Game.Game;
import Game.Move;
import GeneticAlgorithm.Evolve;
import GeneticAlgorithm.GeneticAlgorithm;
import GeneticAlgorithm.Genome;
import Neurons.DuplicateNeuronID_Exception;
import Neurons.NeuralNetwork;
import Neurons.NeuralNetworkReader;
import Neurons.Neuron;
import PacmanGrid.Block;
import PacmanGrid.Grid;
import PacmanGrid.Pill;
import PacmanGrid.Road;
import Utils.IntDimension;
import javafx.application.Platform;

public class AiSimulator implements Runnable{
	
	private NeuralNetwork pacNetwork;
	private NeuralNetwork ghostNetwork;
	private GeneticAlgorithm pacAlgorithm;
	private GeneticAlgorithm ghostAlgorithm;
	private Game game;
	private int pacEliteSample;
	private int pacBoltzSample;
	private double pacTemperature;
	private double pacCoolRate;
	private int generations;
	private int runs;
	private long delay;
	private int ghostEliteSample;
	private int ghostBoltzSample;
	private double ghostTemperature;
	private double ghostCoolRate;
	private double minFitness;
	
	private IntDimension ghostMax;
	private IntDimension ghostMin;
	private IntDimension pacMax;
	private IntDimension pacMin;
	private Map <GenericAgent,ArrayList<IntDimension>> startPositions;
	private int positions = 5;
	public boolean useDefaultGenomes = false;
	

	public  AiSimulator(Game game,NeuralNetwork pacNetwork,GeneticAlgorithm pacAlgorithm,
							NeuralNetwork ghostNetwork, GeneticAlgorithm ghostAlgorithm) {
		this.game = game;
		this.pacNetwork = pacNetwork;
		this.ghostNetwork = ghostNetwork;
		this.pacAlgorithm = pacAlgorithm;
		this.ghostAlgorithm = ghostAlgorithm;
		generateStartPositions();
	}
	
	public void startThread(String threadName, boolean useDefaultGenomes){
		this.useDefaultGenomes = useDefaultGenomes;
		new Thread (this, threadName).start();;
	}
	
	@Override
	public void run() {
		try{
			if (useDefaultGenomes == false){doSimulation ();}
			else {doGame();}
		}catch (DuplicateNeuronID_Exception e){e.printStackTrace();}
	}
	
	public void doSimulation () throws DuplicateNeuronID_Exception{
		setStartPosition ();
		for (int i = 0 ; i < generations; i++){
				System.out.println("GENERATION == WOOO" + i);
				//if (i >= 50 && (i%50) == 0){setStartPosition();}
				simulateGeneration(0,0,runs, delay);
				pacTemperature = pacTemperature-pacCoolRate;
				ghostTemperature = ghostTemperature-ghostCoolRate;
				if (pacTemperature <= 0){pacTemperature =1;}
				if (ghostTemperature <= 0){ghostTemperature =1;}
				
				pacAlgorithm.newGeneration(pacEliteSample, pacBoltzSample, pacTemperature,pacAlgorithm.getPopulation().size());
				ghostAlgorithm.newGeneration(ghostEliteSample, ghostBoltzSample,ghostTemperature,ghostAlgorithm.getPopulation().size());
		}
	}
	
	private List <Genome> readDefaultGenomes (){
		List <Genome>  fittestList = null;
		InputStream fileInputStream = AiSimulator.class.getClassLoader().getResourceAsStream("AI/DefaultGenomes.ai");
		try {
			ObjectInputStream objInputStream = new ObjectInputStream(fileInputStream);
			fittestList = (ArrayList <Genome>) objInputStream.readObject();
			objInputStream.close();
		} catch (ClassNotFoundException | IOException  e) {
			if (e instanceof  EOFException){ fittestList = new ArrayList<Genome>();}
			else{e.printStackTrace();}
		}
		return fittestList;
		
	}
	
	public void doGame () throws DuplicateNeuronID_Exception{
		List <Genome> genomes = readDefaultGenomes();
		for (Genome g: genomes){System.out.println("IDDD= "+ g.getID() + " F= "+g.getOverallFitness());}
		Genome ghostGenome = new Genome(1, "moveDir","enemyKills","enemyAvoid");
		for (GenericAgent agent : game.getGhosts()){agent.setController(ghostGenome);}
		
		for (Genome genome : genomes){
			genome = genomes.get(2);
			game.getPacmen().get(0).setController(genome);
			int posIndex = 0;
			while (posIndex < positions){
				for (GenericAgent agent : startPositions.keySet()){
					agent.setResetPos(startPositions.get(agent).get(posIndex));}
				resetGame();
				simulateGame(runs, delay);
				posIndex++;
			}
		}
	}
	
	public void setSimulationProperties (int generations, int runs, int delay){
		this.generations = generations;
		this.runs = runs;
		this.delay = delay;
	}
	
	public void setPacSimulationProperties (int eliteSample,int boltzSample,double temperature,double coolRate){
			
		this.pacEliteSample = eliteSample;
		this.pacBoltzSample = boltzSample;
		this.pacTemperature = temperature;
		this.pacCoolRate = coolRate;
	}
	
	public void setGhostSimulationProperties (int eliteSample,int boltzSample,double temperature,double coolRate){
		this.ghostEliteSample = eliteSample;
		this.ghostBoltzSample = boltzSample;
		this.ghostTemperature = temperature;
		this.ghostCoolRate = coolRate;
	}	
	public void play (List <GenericAgent> agents,NeuralNetwork network, GeneticAlgorithm algorithm) throws DuplicateNeuronID_Exception{

		for (GenericAgent agent : agents){
			for (int i = 0; i < agent.getSpeed(); i++){
				if (game.getPacmen().size() == game.getDeadPacmenCount()){return;}
				if (game.getGrid().getPillsLeft() <= 0){resetGame();}
				play (network,algorithm,agent);
				delay(delay);
			}
		}
	}
	
	public void simulateGame(int run,long delay) throws DuplicateNeuronID_Exception{
		if (run == 0){return;}
		play (game.getPacmen(),pacNetwork,pacAlgorithm);
		play (game.getGhosts(),ghostNetwork,ghostAlgorithm);
		run--;
		simulateGame(run, delay);
	}
	
	
	private int assignControllers (List <GenericAgent> agents, List<Genome> population,int index){
		for (GenericAgent agent: agents){
			try{
				Genome genome = population.get(index);
				agent.setController(genome);
				index++;
			}catch (NullPointerException e){
				index = -1;
			}
		}
		return index;
	}
	
	public void simulateGeneration (int ghostStart,int pacStart,int runs,long delay) throws DuplicateNeuronID_Exception{

		if (pacStart >= pacAlgorithm.getPopulation().size()||
				ghostStart >= ghostAlgorithm.getPopulation().size()
				|| pacStart == -1 || ghostStart == -1){ return;}
		else{
			pacStart = assignControllers(game.getPacmen(), pacAlgorithm.getPopulation(), pacStart);
			ghostStart = assignControllers(game.getGhosts(), ghostAlgorithm.getPopulation(), ghostStart);
			int posIndex = 0;
			while (posIndex < positions){
				for (GenericAgent agent : startPositions.keySet()){
					agent.setResetPos(startPositions.get(agent).get(posIndex));}
				resetGame();
				simulateGame(runs, delay);
				posIndex++;
			}
			simulateGeneration(ghostStart, pacStart, runs, delay);
		}
	}
	
	public void play (NeuralNetwork network,GeneticAlgorithm algorithm,GenericAgent agent) throws DuplicateNeuronID_Exception{
		if (agent.isDead() == true){return;}
		network.getInputReader().readInputs (network, agent, game);
		if (agent instanceof Ghost){
			int index = 0;
			for (Neuron neuron : network.getInputLayer().getNeurons()){
				double val = neuron.getOutputValue();
				network.getOutputLayer().getNeurons().get(index).setOutputValue(val);
				index++;
			}
		}else{
			network.setWeights(agent.getController());
			network.update();
		}
		algorithm.evaluateGenome(agent.getController(),network,game,agent);
	}
	
	private void delay(long delay){
		long stopTime = System.currentTimeMillis() + (delay);
		long currentTime = System.currentTimeMillis();
		while (currentTime < stopTime){
			 currentTime = System.currentTimeMillis();
		}
	}
	
	private void setAgentSides (){
		double side = new Random().nextDouble();
		if (side > 0.5){
			ghostMin = new IntDimension(0,0);
			ghostMax = new IntDimension(14,6);
			pacMin = new IntDimension(0,7);
			pacMax = new IntDimension(14,14);
		}else{
			pacMin = new IntDimension(0,0);
			pacMax = new IntDimension(14,6);
			ghostMin = new IntDimension(0,7);
			ghostMax = new IntDimension(14,14);
		}
	}
	
	private void setStartPosition (){
		setAgentSides();
		setStartPosition(game.getGhosts(), ghostMin, ghostMax, false);
		setStartPosition(game.getPacmen(), pacMin, pacMax, true);
	}
	
	private void generateStartPositions (){
		startPositions = new HashMap<GenericAgent,ArrayList<IntDimension>>();
		for (int i = 0; i < positions; i++){
			setStartPosition();
			for (GenericAgent agent : game.getAllAgents()){
				if (startPositions.containsKey(agent) == false){
					startPositions.put(agent, new ArrayList <IntDimension> ());}
				startPositions.get(agent).add(agent.getResetPos());
			}
		}
	}
	
	
	private void setStartPosition (List <GenericAgent> agents,IntDimension min,
									IntDimension max,boolean doSamePosition){

		Block block = randStartPosition(min.X, max.X, min.Y, max.Y);
		IntDimension startPos = new IntDimension(block.getGridPosition().X,block.getGridPosition().Y);
		GenericAgent agent = agents.get(0);
		agent.setResetPos(new IntDimension(startPos.X, startPos.Y));
		
		for (int i = 1; i < agents.size(); i++){
			agent = agents.get(i);
			if ( doSamePosition == false){
				block = randStartPosition(min.X, max.X, min.Y, max.Y);
				startPos = new IntDimension(block.getGridPosition().X,block.getGridPosition().Y);
			}
			agent.setResetPos(new IntDimension(startPos.X, startPos.Y));
		}
	}
	
	private Block randStartPosition (int minX, int maxX, int minY, int maxY){
		boolean found = false;
		Random rand = new Random ();
		Block block = null;
		while (found == false){
			int x = rand.nextInt((maxX - minX) + 1) + minX;
			int y = rand.nextInt((maxY - minY) + 1) + minY;
			block = game.getGrid().getBlock(new IntDimension(x, y));
			if (block instanceof Road &&((Road)block).getOccupiedBy()==null &&
					((Road)block).getPill()!= Pill.POWERPILL){found = true;}
		}
		return block;
	}
	
	private void resetGame (){
		final CountDownLatch latch = new CountDownLatch(1);
		Platform.runLater(new Runnable() {
		    public void run() {
		    	game.reset();
		    	latch.countDown();}
			});
		try {latch.await();} 
		catch (InterruptedException e) {e.printStackTrace();}
		delay(15);
	}

	public int getPositions() {
		return positions;
	}

	public void setPositions(int positions) {
		this.positions = positions;
	}

	public Map<GenericAgent, ArrayList<IntDimension>> getStartPositions() {
		return startPositions;
	}
	
	
}

