import java.awt.Point;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.Set;


public class CSP {

	@SuppressWarnings("unchecked")
	public LinkedList<Integer>[][] domains = (LinkedList<Integer>[][])new LinkedList[9][9];
	
	/**
	 * Constructs a <code>CSP</code> form a file.
	 * @param filename
	 */
	public CSP(String filename) throws IOException {
		for (int i = 0; i < 9; i++) {
			for (int j = 0; j < 9; j++) {
				domains[i][j] = new LinkedList<>(Arrays.asList(1,2,3,4,5,6,7,8,9));
			}
		}
		
		loadFromFile(filename);
		
	}

	/**
	 * Initializes <code>domains</code> with a Sudoku loaded form a file.
	 * @param filename
	 */
	private void loadFromFile(String filename) throws IOException {
		try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
			for (int i = 0; i < 9; i++) {
				for (int j = 0; j < 9; j++) {
					char c = (char) reader.read();
//					int number = Integer.parseInt(String.valueOf((char)reader.read()));
					System.err.println(String.format("(%d, %d) = %s", j, i, c));
					int number = Integer.parseInt(String.valueOf(c));
					if (number != 0) {
						LinkedList<Integer> domain = new LinkedList<>();
						domain.add(number);
						domains[i][j] = domain;
					}
				}
				reader.readLine();		// skip linebreak
			}
		}
	}
	
	public void AC3() throws NotSolvableException {
		Set<Arc> todo = getInitArcs();
		
		Iterator<Arc> iter = todo.iterator();
		while (iter.hasNext()) {
			Arc arc = iter.next();
			iter.remove();
			
			Set<Arc> neighbors = arc.arcReduce();
			todo.addAll(neighbors);
			iter = todo.iterator();
			
		}
		
	}
	

	/**
	 * Get all initial Constrains.
	 * @return
	 */
	private Set<Arc> getInitArcs() {
		Set<Arc> todo = new LinkedHashSet<>(3240);		// 3240 = binomial coefficient(81,2)
		for (int i = 0; i < 9; i++) {
			for (int j = 0; j < 9; j++) {
				todo.addAll(getNeighbors(i,j));
			}
		}
		return todo;
	}
	

	/**
	 * Prints the domains to stdout.
	 */
	public void print() {
		for (int i = 0; i < 9; i++) {
			System.out.print("[");
			for (int j = 0; j < 9; j++) {
				LinkedList<Integer> domain = domains[i][j];
				if (domain.size() == 1) {
					System.out.print(domain.peek());
				} else {
					System.out.print(domain);
				}
				
				if (j != 8) {
					System.out.print(",");
				}
			}
			System.out.print("]\n");
		}
	}

	private Set<Arc> getNeighbors(Point a) {
		return getNeighbors(a.x, a.y);
	}
	
	private Set<Arc> getNeighbors(int i, int j) {
		Set<Arc> constrains = new LinkedHashSet<CSP.Arc>(24);
		for (int k = 0; k < 9; k++) {
			if (k != j) {
				constrains.add(new Arc(j, i, k, i));	// row constrain
			}
			if (k != i) {
				constrains.add(new Arc(j, i, j, k));	// column constrain
			}
			
			final int xOffset = 3 * (j / 3);
			final int yOffset = 3 * (i / 3);
			final int x = xOffset + k % 3;
			final int y = yOffset + k / 3;

			if ((x != j || y != i) && (x != i || y != j)) {
				constrains.add(new Arc(j, i, x, y));	// block constrain
			}
		}

		return constrains;
	}

	/**
	 * This class models binary constrains which are arcs in the constrain graph.
	 */
	class Arc {
		public Point a, b;
		
		public Arc(int x1, int y1, int x2, int y2) {
			if (x1 == x2 && y1 == y2 || x1 == y2 && y1 == x2) {
//				throw new IllegalArgumentException("invalid constrain");
			}
			a = new Point(x1, y1);
			b = new Point(x2, y2);
		}
		
		/**
		 * reduces all values in the domains of a and b that do not satisfy this constrain.
		 * @return a <code>Set</code> of constrains which could be affected by this reduction.
		 * @throws NotSolvableException if one domain has length zero.
		 */
		public Set<Arc> arcReduce() throws NotSolvableException {
			Point var1, var2;
			if (domains[a.y][a.x].size() > domains[b.y][b.x].size()) {	// reduce domain of the variable with the larger domain
				var1 = a;
				var2 = b;
			} else {
				var1 = b;
				var2 = a;
			}
			
			// remove all values in the domain of var1 which can not satisfy this constrain.
			boolean hasChanged = false;
			Iterator<Integer> iter1 = domains[var1.y][var1.x].iterator();
			while (iter1.hasNext()) {
				int val1 = iter1.next();
				
				boolean isSatisfiable = false;
				for (int val2 : domains[var2.y][var2.x]) {
					if (val1 != val2) {
						isSatisfiable = true;
						break;
					}
				}
				
				if (!isSatisfiable) {
					hasChanged = true;
					iter1.remove();
				}
			}
			
			if (domains[var1.y][var1.x].isEmpty()) {
				throw new NotSolvableException();
			}
			
			if (hasChanged) {
				Set <Arc> arcs = getNeighbors(var1);
				arcs.remove(this);
				return arcs;
			} else {
				return new LinkedHashSet<CSP.Arc>();	// add no new arcs
			}
		}
		
		@Override
		public boolean equals(Object obj) {
			if (! (obj instanceof Arc)) {
				return false;
			}
			Arc comp = (Arc)obj;
			// Arc(a,b) == Arc(b,a)
			return comp.a.equals(this.a) && comp.b.equals(this.b) 
					|| comp.a.equals(this.b) && comp.b.equals(this.a);
		}
		
		@Override
		public String toString() {
			return String.format("{(%d,%d)!=(%d,%d)}", a.x, a.y, b.x, b.y);
		}
	}
}
