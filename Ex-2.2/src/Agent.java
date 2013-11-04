import java.util.Collections;
import java.util.LinkedList;


public class Agent {
	
	// Fields
	
	private World world;
	private boolean visited[][];
	private boolean dfs;
	
	
	// Constructor
	
	public Agent(World world, boolean dfs) {
		this.world = world;
		this.visited = new boolean[world.getSize().x][world.getSize().y];
		this.dfs = dfs;
	}
	
	// Methods
	
	public LinkedList<String> solveMaze() {
		Cell currentCell = new Cell(world.getStartPos(), new LinkedList<String>());
		LinkedList<Cell> todo = new LinkedList<Cell>();
		todo.add(currentCell);
		
		while (!todo.isEmpty()) {

			if (dfs) {		// depth-first
				
				currentCell = todo.pollFirst();
				if (hasVisited(currentCell)) {
					continue;	// get next cell
				}
				
				LinkedList<Cell> adjCells = world.expand(currentCell);
				todo.addAll(0, adjCells);
				
				setVisited(currentCell);

			} else {		// breadth-first

				currentCell = todo.pollLast();
				if (hasVisited(currentCell)) {
					continue;	// get next cell
				}
				
				LinkedList<Cell> adjCells = world.expand(currentCell);
				Collections.reverse(adjCells);			// reverse, so the L,R,U,D order remains for the LIFO
				todo.addAll(0, adjCells);
				
				setVisited(currentCell);
			}
			
			if (world.isEndPos(currentCell.getLocation())) {
				return currentCell.getPath();
			}
		}
		
		return null;		// no solution was found
		
	}
	
	/**
	 * returns <code>true</code> if the cell <code>p</code> has been visited
	 * @param p
	 * @return
	 */
	private boolean hasVisited(Cell p) {
		return visited[p.getLocation().x][p.getLocation().y];
	}
	
	/**
	 * sets the state of cell <code>p</code> to visited
	 * @param p
	 */
	private void setVisited(Cell p) {
		this.visited[p.getLocation().x][p.getLocation().y] = true;
	}
	
}















