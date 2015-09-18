package Game;

import java.util.ArrayList;

import Agents.GenericAgent;
import Agents.Ghost;
import Agents.Pacman;
import PacmanGrid.Grid;

public class Game {
	
	private int Score;
	private GenericAgent pacman;
	private ArrayList<Ghost> ghosts;
	private Grid grid;
	public final static int SINGLE_PLAYER = 0;
	public final static int NO_PLAYER = 1;
	private int gameMode = 0;
	
	public Game(){
	}
	
	public Game (Grid grid, Pacman pacman, ArrayList <Ghost> ghosts){
		this.grid = grid;
		this.pacman = pacman;
		this.ghosts = ghosts;
	}
	
	public int getScore() {
		return Score;
	}

	public void setScore(int score) {
		Score = score;
	}

	public GenericAgent getPacman() {
		return pacman;
	}

	public void setPacman(GenericAgent pacman) {
		this.pacman = pacman;
	}

	public ArrayList<Ghost> getGhosts() {
		return ghosts;
	}

	public void setGhosts(ArrayList<Ghost> ghosts) {
		this.ghosts = ghosts;
	}

	public  Grid getGrid() {
		return grid;
	}

	public void setGrid(Grid grid) {
		this.grid = grid;
	}
	
	public void end(){
	}
	public void reset (){
	}
	public void restart (){
	}
	public void start (){
	}

}
