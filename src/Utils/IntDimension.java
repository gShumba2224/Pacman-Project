package Utils;

import java.io.Serializable;

public class IntDimension implements Serializable{
	

	public int getX() {
		return X;
	}

	public void setX(int x) {
		this.X = x;
	}

	public int getY() {
		return Y;
	}

	public void setY(int y) {
		this.Y = y;
	}

	public IntDimension (int x, int y){
		this.X = x;
		this.Y = y;
	}

	public int X;
	public int Y;

}
