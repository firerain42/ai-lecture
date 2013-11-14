import java.awt.Point;
import java.util.LinkedList;


public class Cell {

	// Fields
	
	private LinkedList<String> path;
	private int f;
	private int[][] currentState;
	private World world;
	private Point locationOfEmptyField;
	
	
	// Getter + Setter
	
	public LinkedList<String> getPath() {
		return path;
	}

	public Point getLocationOfEmptyField() {
		return locationOfEmptyField;
	}
	
	public int getF() {
		return f;
	}
	

	public int[][] getCurrentState() {
		return currentState;
	}
	
	// Constructor
	
	public Cell(int[][] currentState, LinkedList<String> path, World world) throws Exception {
		this.currentState = currentState;
		this.locationOfEmptyField = locationOfEmptyField(currentState);
		this.path = path;
		this.world = world;
		int g = path.size();
		int h = world.heuristic(currentState);
		f = (g + h);
	}
	
	
	// Methods
	
	private Point locationOfEmptyField(int[][] currentState) {
		for (int i = 0; i < currentState.length; i++) {
			for (int j = 0; j < currentState.length; j++) {
				if (currentState[i][j] == 0)
					return new Point(i, j);
			}
		}
		return null;
	}

	@Override
	public String toString() {
		return this.path.toString() + String.format("@(%d,%d)", this.locationOfEmptyField.x, this.locationOfEmptyField.y);
	}

	public boolean goalStateReached() {
		return world.goalStateReached(currentState);
	}
	
}
