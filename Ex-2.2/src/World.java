import java.awt.Point;
import java.io.File;
import java.io.IOException;
import java.util.LinkedList;
import java.util.Scanner;


public class World {

	// Fields
	
	private Point startPos;
	private Point endPos;
	private Point size;
	private EnvItem[][] world;
	
	
	
	// Getter + Setter
	
	public Point getSize() {
		return size;
	}

	public Point getStartPos() {
		return startPos;
	}
	
	public Point getEndPos() {
		return endPos;
	}
	
	
	// Constructor

	/**
	 * creates a world from a file.
	 * @param filename
	 * @throws IOException
	 * @throws Exception
	 */
	public World(String filename) throws Exception {
		LinkedList<String> rawInput = new LinkedList<>();
		
		// read file
		Scanner scn = new Scanner(new File(filename));
		while(scn.hasNextLine()) {
			rawInput.add(scn.nextLine());
		}
		scn.close();
		
		// parse the list of strings
		final int width = rawInput.get(0).length();
		final int height = rawInput.size();
		this.size = new Point(width, height);
		world = new EnvItem[width][height];
		
		for (int j = 0; j < height; j++) {
			final String row = rawInput.get(j);
			for (int i = 0; i < width; i++) {
				switch (row.charAt(i)) {
				case ' ':
					this.world[i][j] = EnvItem.Free;
					break;
				case '.':
					this.endPos = new Point(i, j);
					break;
				case '@':
					this.startPos = new Point(i, j);
					break;
				case '#':
					this.world[i][j] = EnvItem.Wall;
					break;
				default:
					throw new Exception("could not parse file.");
				}
			}
		}
		
		
		
		
	}

	
	// Methods
	
	/**
	 * returns true if <code>p</code> is the end position
	 * @param p
	 * @return
	 */
	public boolean isEndPos(Point p) {
		return p.equals(this.endPos);
	}
	
	
	/**
	 * returns a list of all free neighbor cells. expands state graph in a way 
	 * such that the cells are ordered left, right, up, down
	 * @param p
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public LinkedList<Cell> expand(Cell c) {
		LinkedList<Cell> res = new LinkedList<>();
		LinkedList<String> basePath = c.getPath();
		Point l = c.getLocation();
		
		// do not allow point on the edge of the world
		if (l.x < 1 || l.x >= this.size.x-1 
				|| l.y < 1 || l.y >= this.size.y-1) {
			throw new IllegalArgumentException();
		}
		
		// Left
		if (this.world[l.x-1][l.y] != EnvItem.Wall) {
			LinkedList<String> path = (LinkedList<String>) basePath.clone();
			path.add("L");
			res.add(new Cell(new Point(l.x-1, l.y), path));
		}

		// Right
		if (this.world[l.x+1][l.y] != EnvItem.Wall) {
			LinkedList<String> path = (LinkedList<String>) basePath.clone();
			path.add("R");
			res.add(new Cell(new Point(l.x+1, l.y), path));
		}
		
		// Up
		if (this.world[l.x][l.y-1] != EnvItem.Wall) {
			LinkedList<String> path = (LinkedList<String>) basePath.clone();
			path.add("U");
			res.add(new Cell(new Point(l.x, l.y-1), path));
		}
		
		// Down
		if (this.world[l.x][l.y+1] != EnvItem.Wall) {
			LinkedList<String> path = (LinkedList<String>) basePath.clone();
			path.add("D");
			res.add(new Cell(new Point(l.x, l.y+1), path));
		}
		return res;
	}
	

}
