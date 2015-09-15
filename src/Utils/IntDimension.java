package Utils;

public class IntDimension {
	

	public int getX() {
		return x;
	}

	public void setX(int x) {
		this.x = x;
	}

	public int getY() {
		return y;
	}

	public void setY(int y) {
		this.y = y;
	}

	private int x;
	private int y;
	
	public IntDimension (int x, int y){
		this.x = x;
		this.y = y;
	}
}
