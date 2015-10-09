package Utils;

public class DistanceCalculator {
	
	private DistanceCalculator (){
		
	}
	
	public static int manhattanDistance (IntDimension from, IntDimension to){
		int x = Math.abs( from.X - to.X );
		int y =  Math.abs(from.Y - to.Y );
		return (x+y);
	}
	
	public static double diagnalDistance (IntDimension from, IntDimension to){
		double x = Math.abs(to.X - from.X);
		double y = Math.abs(to.Y - from.Y);
		double result = Math.sqrt((x*x)  + (y*y));
		return result;
	}

}
