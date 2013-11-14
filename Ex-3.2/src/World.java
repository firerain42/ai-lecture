import java.awt.Point;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.LinkedList;
import java.util.Scanner;

public class World {

	// Fields

	private final String heuristic;
	private int size;
	private int numberOfNodesExpanded = 0;
	private int numberOfNodesGenerated = 0;
	private int[][] initialState;
	private int[][] goalState;

	// Getter

	public int getSize() {
		return size;
	}

	public int getNumberOfNodesExpanded() {
		return numberOfNodesExpanded;
	}


	public int getNumberOfNodesGenerated() {
		return numberOfNodesGenerated;
	}

	// Constructor

	/**
	 * creates a world from a file.
	 * 
	 * @param filename
	 * @throws IOException
	 * @throws Exception
	 */
	public World(String heuristic, String filenameOfInitialState,
			String filenameOfGoalState) throws Exception {
		this.heuristic = heuristic;
		initialState = parseTextFile(filenameOfInitialState);
		goalState = parseTextFile(filenameOfGoalState);
	}

	private int[][] parseTextFile(String filename) throws FileNotFoundException {
		LinkedList<String> rawInput = new LinkedList<>();

		// read file
		Scanner scn = new Scanner(new File(filename));
		while (scn.hasNextLine()) {
			rawInput.add(scn.nextLine());
		}
		scn.close();

		// parse the list of strings
		size = rawInput.size();
		int[][] arr = new int[size][size];

		for (int i = 0; i < size; i++) {
			final String row = rawInput.get(i);
			for (int j = 0; j < size; j++) {
				arr[j][i] = Character.getNumericValue(row.charAt(j));
			}
		}
		return arr;
	}

	// Methods

	/**
	 * returns a list of all free neighbor cells. expands state graph in a way
	 * such that the cells are ordered left, right, up, down
	 * 
	 * @param p
	 * @return
	 * @throws Exception 
	 */
	@SuppressWarnings("unchecked")
	public LinkedList<Cell> expand(Cell c) throws Exception {
		numberOfNodesExpanded++;
		LinkedList<Cell> res = new LinkedList<>();
		LinkedList<String> basePath = c.getPath();
		Point emptyField = c.getLocationOfEmptyField();

		// Left (moves empty field to the left)
		if (emptyField.x > 0) {
			LinkedList<String> path = (LinkedList<String>) basePath.clone();
			path.add("L");
			int[][] newState = deepCopyIntMatrix(c.getCurrentState());
			int tmp = newState[emptyField.x - 1][emptyField.y];
			newState[emptyField.x - 1][emptyField.y] = 0;
			newState[emptyField.x][emptyField.y] = tmp;
			res.add(new Cell(newState, path, this));
			numberOfNodesGenerated++;
		}

		// Right
		if (emptyField.x + 1 < size) {
			LinkedList<String> path = (LinkedList<String>) basePath.clone();
			path.add("R");
			int[][] newState = deepCopyIntMatrix(c.getCurrentState());
			int tmp = newState[emptyField.x + 1][emptyField.y];
			newState[emptyField.x + 1][emptyField.y] = 0;
			newState[emptyField.x][emptyField.y] = tmp;
			res.add(new Cell(newState, path, this));
			numberOfNodesGenerated++;
		}

		// Up
		if (emptyField.y > 0) {
			LinkedList<String> path = (LinkedList<String>) basePath.clone();
			path.add("U");
			int[][] newState = deepCopyIntMatrix(c.getCurrentState());
			int tmp = newState[emptyField.x][emptyField.y-1];
			newState[emptyField.x ][emptyField.y-1] = 0;
			newState[emptyField.x][emptyField.y] = tmp;
			res.add(new Cell(newState,path, this));
			numberOfNodesGenerated++;
		}

		// Down
		if (emptyField.y + 1 < size) {
			LinkedList<String> path = (LinkedList<String>) basePath.clone();
			path.add("D");
			int[][] newState = deepCopyIntMatrix(c.getCurrentState());
			int tmp = newState[emptyField.x][emptyField.y+1];
			newState[emptyField.x][emptyField.y+1] = 0;
			newState[emptyField.x][emptyField.y] = tmp;
			res.add(new Cell(newState,path, this));
			numberOfNodesGenerated++;
		}
		return res;
	}

	public int heuristic(int[][] currentState) throws Exception{
		switch(heuristic)
		{
		case "goal": return goalHeuristic(currentState);
		case "misplaced": return misplacedHeuristic(currentState);
		case "manhattan": return manhattanHeuristic(currentState);
		}
		throw new Exception();
	}

	private int goalHeuristic(int[][] currentState) {
		if (goalStateReached(currentState))
			return 0;
		else return 1;
	}

	public boolean goalStateReached(int[][] currentState) {
		for (int i = 0; i < size; i++) {
			for (int j = 0; j < size; j++) {
				if (currentState[i][j] != goalState[i][j])
					return false;
			}
		}
		return true;
	}

	private int misplacedHeuristic(int[][] currentState) {
		int cnt = 0;
		for (int i = 0; i < size; i++) {
			for (int j = 0; j < size; j++) {
				if (currentState[i][j] != goalState[i][j] && currentState[i][j] != 0)
					cnt++; //evtl. noch ändern, Hinweis aus Aufgabe!
			}
		}
		return cnt;
	}

	private int manhattanHeuristic(int[][] currentState) {
		int sum = 0;
		for (int i = 0; i < size; i++) {
			for (int j = 0; j < size; j++) {
				int value = currentState[i][j];
				if (value == 0)
					continue;
				Point positionOfValueInGoalState = getPositionOf(value);
				sum += Math.abs(positionOfValueInGoalState.x-i)+Math.abs(positionOfValueInGoalState.y-j);
			}
		}
		return sum;
	}

	private Point getPositionOf(int value) {
		int i = 0;
		int j = 0;
		outer: while (i < size)
		{
			j = 0;
			while (j < size)
			{
				if (goalState[i][j] == value)
					break outer;
				j++;
			}
			i++;
		}
		return new Point(i,j);
	}

	public int[][] getInitialState() {
		return initialState;
	}

	public static int[][] deepCopyIntMatrix(int[][] input) {
	    if (input == null)
	        return null;
	    int[][] result = new int[input.length][];
	    for (int r = 0; r < input.length; r++) {
	        result[r] = input[r].clone();
	    }
	    return result;
	}
}
