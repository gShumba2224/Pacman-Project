package AI;

import java.util.ArrayList;
import java.util.List;

import Agents.GenericAgent;
import Agents.Pacman;
import GeneticAlgorithm.Evolve;
import GeneticAlgorithm.Genome;
import Neurons.NeuralNetwork;

public class GalgTest {
	
	public static void main (String[] args){
		GenericAgent x = new Pacman ();
		
		if (x instanceof Pacman){
			System.out.println("woo");
		}
		
	}

}
