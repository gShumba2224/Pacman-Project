package Game;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import Actions.Move;
import Agents.GenericAgent;
import Agents.Ghost;
import Agents.Pacman;
import PacmanGrid.Grid;
import PacmanGrid.GridDrawer;
import Utils.IntDimension;
import javafx.scene.image.Image;

public class Game {
	
	private int score;
	private Map <Integer,ArrayList<GenericAgent>> agents = new HashMap <Integer,ArrayList<GenericAgent>> ();
	private Grid grid;
	public final static int SINGLE_PLAYER = 0;
	public final static int NO_PLAYER = 1;
	private int gameMode = 0;
	private long duration = 1000*30;
	private Window window = null;
	private Map<Integer,ArrayList<IntDimension>>initialAgentPositions=new HashMap<Integer,ArrayList<IntDimension>>();
	
	
	public Game() throws IOException{
		initMaps();
		drawGrid();
	}
	
	public Game (int numGhosts, int numPacmen) throws IOException{
		initMaps();
		drawGrid ();
		generateAgents(numGhosts, numPacmen);
		initAgents();
	}
	
	public Game (Grid grid, int numGhosts, int numPacmen) throws IOException{
		initMaps();
		drawGrid();
		generateAgents (numGhosts , numPacmen);
		initAgents();
	}
	
	public Game (Grid grid, Map<Integer,ArrayList<GenericAgent>>  agents) {
		initMaps();
		this.grid = grid;
		this.agents = agents;
		initAgents();
	}
	
	public void end(){
	}
	public void reset (int agentType){
//		if (agentType == GenericAgent.PACMAN){
//			score = 0;
//			grid.resetGrid();
//		}
//		//IntDimension [] agentPos = new IntDimension[initialAgentPositions.get(agentType).size()];
//		//initAgents(initialAgentPositions.get(agentType).toArray(agentPos),agentType);
	}
	
	
	public void start (){
	}
	
	public void restart (){
		score = 0;
		generateAgents(agents.get(GenericAgent.GHOST).size(), agents.get(GenericAgent.PACMAN).size());
		initAgents();
		//System.out.println(agents.get(GenericAgent.GHOST).size());
		//System.out.println(agents.get(GenericAgent.PACMAN).size());
	}

	public void generateAgents (int numGhosts, int numPacmen){
		
		Ghost ghost;
		Pacman pacman;
		agents.get(GenericAgent.GHOST).clear();
		agents.get(GenericAgent.PACMAN).clear();
		for (int i = 0 ; i < numGhosts ; i++){
			ghost = new Ghost (new Image (new File ("C:\\Users\\GMAN\\Desktop\\Temp Stuff\\agentRedGhost.png").toURI().toString()));
			agents.get(GenericAgent.GHOST).add(ghost);
		}
		
		for (int i = 0 ; i < numGhosts ; i++){
			pacman = new Pacman (new Image (new File ("C:\\Users\\GMAN\\Desktop\\Temp Stuff\\agentPacman.png").toURI().toString()));
			agents.get(GenericAgent.PACMAN).add(pacman);
		}
	}
	
	public void editNumberOfAgents (int numAgents , int type){
		int num = numAgents - agents.get(type).size();
		if (num < 0){
			num = Math.abs(num);
			for (int i = 0; i < num; i++){
				 agents.get(type).remove(i);
			}
		}else{
			if (type == GenericAgent.GHOST) {generateAgents(num,0);}
			else {generateAgents(0,num);}
		}
	}
	
	public  void initAgents (){
		
		IntDimension defaultDimension = new IntDimension (1,1);
		int x = 1;
		for (GenericAgent pacman : agents.get(GenericAgent.PACMAN)){
			pacman.setLocation(defaultDimension);
			Move.moveAgent(pacman, this, new IntDimension (x,1));
			x++;
		}
		defaultDimension = new IntDimension (1,13);
		x = 1;
		GenericAgent[] ghosts = new GenericAgent[agents.get(GenericAgent.GHOST).size()];
		for (GenericAgent ghost : agents.get(GenericAgent.GHOST).toArray(ghosts)){
			ghost.setLocation(defaultDimension);
			Move.moveAgent(ghost, this, new IntDimension (x,13));
			x++;
		}
	}
	
	public void initAgents (IntDimension[] positions, int agentType){
		for (int i = 0; i < positions.length ; i++){
			IntDimension defaultDimension = new IntDimension (1,1);
			GenericAgent agent =  agents.get(agentType).get(i);
			agent.setLocation(defaultDimension);
			Move.moveAgent(agent, this, positions[i]);
		}
	}
	
	public void initAgents (IntDimension[] ghostPositions, IntDimension[] pacmanPositions){
		initAgents (ghostPositions,GenericAgent.GHOST);
		initAgents (pacmanPositions,GenericAgent.PACMAN);
	}
	
	public void drawGrid () throws IOException{
		grid = GridDrawer.drawFromImage(new File ("C:\\Users\\GMAN\\Desktop\\Temp Stuff\\grid01.jpg"),new IntDimension (50,50));
		File file = new File ("C:\\Users\\GMAN\\Desktop\\Temp Stuff\\grid01.jpg");
		grid.setBackgroundImage(new Image (file.toURI().toString()));
		grid.drawPills();
		grid.drawGridExtras();
	}
	
	private void initMaps (){
		agents.put(GenericAgent.PACMAN, new ArrayList <GenericAgent> ());
		agents.put(GenericAgent.GHOST, new ArrayList <GenericAgent> ());
		initialAgentPositions.put (GenericAgent.PACMAN, new ArrayList <IntDimension> ());
		initialAgentPositions.put (GenericAgent.GHOST, new ArrayList <IntDimension> ());
	}
	
	public void setInitialAgentPositions (int agentType ,IntDimension...dimensions){
		initialAgentPositions.get(agentType).clear();
		for (int i = 0 ; dimensions.length > i; i++){
			initialAgentPositions.get(agentType).add(dimensions[i]);
		}
	}

	public int getScore() {
		return score;
	}

	public void setScore(int score) {
		this.score = score;
	}

	public long getDuration() {
		return duration;
	}

	public void setDuration(long duration) {
		this.duration = duration;
	}

	public ArrayList<GenericAgent> getGhosts() {
		return agents.get(GenericAgent.GHOST);
	}

	public void setGhosts(ArrayList<GenericAgent> ghosts) {
		agents.put(GenericAgent.GHOST, ghosts);
	}

	public  Grid getGrid() {
		return grid;
	}

	public void setGrid(Grid grid) {
		this.grid = grid;
	}
	
	public ArrayList<GenericAgent> getPacmen() {
		return agents.get(GenericAgent.PACMAN);
	}

	public void setPacmen(ArrayList<GenericAgent> pacmen) {
		agents.put(GenericAgent.PACMAN, pacmen);
	}
	
	public  Map <Integer,ArrayList<GenericAgent>> getAgents() {
		return agents;
	}

	public void setAgents (Map <Integer,ArrayList<GenericAgent>> agents) {
		this.agents = agents;
	}
	
}
