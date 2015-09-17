package Game;

import java.util.ArrayList;

import Agents.GenericAgent;
import PacmanGrid.Grid;

public class Game {
	
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

	public ArrayList<GenericAgent> getGhosts() {
		return ghosts;
	}

	public void setGhosts(ArrayList<GenericAgent> ghosts) {
		this.ghosts = ghosts;
	}

	public  Grid getGrid() {
		return grid;
	}

	public void setGrid(Grid grid) {
		this.grid = grid;
	}

	private int Score;
	private GenericAgent pacman;
	private ArrayList<GenericAgent> ghosts;
	private Grid grid;
	
	public Game(){
		
	}
}
