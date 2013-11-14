import java.util.LinkedList;

public class Agent {

	// Fields

	private World world;

	// Constructor

	public Agent(World world) {
		this.world = world;
	}

	// Methods

	public LinkedList<String> solve() throws Exception {
		Cell currentCell = new Cell(world.getInitialState(),
				new LinkedList<String>(), world);
		LinkedList<Cell> todo = new LinkedList<Cell>();
		todo.add(currentCell);

		while (!todo.isEmpty()) {

			currentCell = getCellWithBestF(todo);
			if (currentCell.goalStateReached()) {
				System.out.println("Number of nodes expanded: "
						+ world.getNumberOfNodesExpanded()
						+ "; Number of nodes generated: "
						+ world.getNumberOfNodesGenerated());
				return currentCell.getPath();
			}
			todo.remove(currentCell);

			LinkedList<Cell> adjCells = world.expand(currentCell);
			todo.addAll(0, adjCells);

		}

		return null; // no solution was found

	}

	private Cell getCellWithBestF(LinkedList<Cell> todo) {
		Cell best = todo.get(0);
		int min = best.getF();
		for (Cell c : todo) {
			if (c.getF() < min) {
				best = c;
				min = c.getF();
			}
		}
		return best;
	}

}
