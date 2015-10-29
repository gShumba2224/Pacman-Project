package Game;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import Agents.GenericAgent;
import Agents.Ghost;
import Agents.Pacman;
import PacmanGrid.Grid;
import PacmanGrid.GridDrawer;
import Utils.IntDimension;
import javafx.application.Platform;
import javafx.scene.image.Image;

public class Game {
	
	private int score;
	private Map <Integer,ArrayList<GenericAgent>> agents = new HashMap <Integer,ArrayList<GenericAgent>> ();
	private Grid grid;
	private long duration = 1000*30;
	private int scaredGhostsDuration = 0;
	private static final int PACMAN_LIVES = 0;
	private static final int GHOST_LIVES = 0; 
	private static final int PACMAN_SPEED = 3;
	private static final int GHOST_SPEED = 1;
	
	
	public Game() throws IOException{
		initMaps();
		drawGrid();
	}
	
	public Game (int numGhosts, int numPacmen) throws IOException{
		initMaps();
		drawGrid ();
		generateAgents(numGhosts, numPacmen);
	}
	
	public Game (Grid grid, int numGhosts, int numPacmen) throws IOException{
		initMaps();
		drawGrid();
		generateAgents (numGhosts , numPacmen);
	}
	
	public void end(){
	}
	
	public void  reset (){
		score = 0;
		resetAgents();
		grid.resetGrid();
	}
	
	
	public void start (){
	}
	
	public void generateAgents (int numGhosts, int numPacmen){
		
		Ghost ghost;
		Pacman pacman;
		agents.get(GenericAgent.GHOST).clear();
		agents.get(GenericAgent.PACMAN).clear();
		for (int i = 0 ; i < numGhosts ; i++){
			ghost = new Ghost (new Image (new File ("C:\\Users\\GMAN\\Desktop\\Temp Stuff\\agentRedGhost.png").toURI().toString()));
			ghost.setResetPos(new IntDimension(1, 2));
			ghost.setLives(GHOST_LIVES);
			ghost.setSpeed(GHOST_SPEED);
			agents.get(GenericAgent.GHOST).add(ghost);
		}
		
		for (int i = 0 ; i < numPacmen ; i++){
			pacman = new Pacman (new Image (new File ("C:\\Users\\GMAN\\Desktop\\Temp Stuff\\agentPacman.png").toURI().toString()));
			pacman.setResetPos(new IntDimension(13, 2));
			pacman.setLives(PACMAN_LIVES);
			pacman.setSpeed(PACMAN_SPEED);
			agents.get(GenericAgent.PACMAN).add(pacman);
		}
		resetAgents();
	}
	
	private void resetAgents (){
		for (List <GenericAgent> agentList : agents.values()){
			for (GenericAgent agent : agentList){
				if (agent.isDead() == true && agent instanceof Ghost){
					agent.revive(  GHOST_LIVES, false);
				}else if (agent.isDead() && agent instanceof Pacman){
					agent.revive(  PACMAN_LIVES , true);
				}
				Move.resetPosition(this, agent);
			}
		}
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
	}
	
	
	public int getScore() {
		return score;
	}

	public void setScore(int score) {
		this.score = score;
		if (this.score >= grid.getTotalScores()){
			this.reset();
		}
	}

	public int getScaredGhostsDuration() {
		return scaredGhostsDuration;
	}

	public void setScaredGhostsDuration(int scaredGhostsDuration) {
		this.scaredGhostsDuration = scaredGhostsDuration;
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
	
	public int getDeadGhostsCount (){
		int count = 0;
		for (GenericAgent agent : getGhosts()){
			if (agent.isDead() == true){count++;}
		}
		return count;
	}
	
	public int getDeadPacmenCount (){
		int count = 0;
		for (GenericAgent agent : getPacmen()){
			if (agent.isDead() == true){count++;}
		}
		return count;
	}
	
	public GenericAgent[] getAllAgents(){
		int totalSize = getPacmen().size()+getGhosts().size();
		GenericAgent[] allAgents = new GenericAgent[totalSize] ;
		for (int i = 0; i < getPacmen().size(); i++){
			allAgents[i] = getPacmen().get(i);
		}
		for (int i = getPacmen().size(); i < totalSize; i++){
			allAgents[i] = getGhosts().get(i -getPacmen().size());
		}
		return allAgents;
	}
	
}
