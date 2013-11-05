import java.awt.Point;
import java.util.LinkedList;


public class Cell {

	// Fields
	
	private LinkedList<String> path;
	private Point location;
	
	
	// Getter + Setter
	//test
	
	public LinkedList<String> getPath() {
		return path;
	}

	public Point getLocation() {
		return location;
	}

	
	// Constructor
	
	public Cell(Point location, LinkedList<String> path) {
		this.path = path;
		this.location = location;
	}
	
	
	// Methods
	
	@Override
	public String toString() {
		return this.path.toString() + String.format("@(%d,%d)", this.location.x, this.location.y);
	}
	
}
