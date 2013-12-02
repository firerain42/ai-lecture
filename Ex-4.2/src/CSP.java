import java.awt.Point;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.PriorityQueue;
import java.util.Set;


public class CSP implements Cloneable {

	
	// ---------------------------------------------------------------------------------
	// Inner Classes
	// ---------------------------------------------------------------------------------
	
	/**
	 * This class models binary constrains which are arcs in the constrain graph.
	 */
	class Arc {
		public Point a, b;
		
		public Arc(int x1, int y1, int x2, int y2) {
			if (x1 == x2 && y1 == y2 || x1 == y2 && y1 == x2) {
				throw new IllegalArgumentException("invalid constrain");
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
		
		/**
		 * returns <code>true</code> if there is any value for which this 
		 * constrain is satisfied with the assignment of 
		 * the variable at <code>(x1, y1)</code> with <code>value</code>.
		 * @param value
		 * @return
		 */
		public boolean isSatisfied(int value) {
			for (int valB : domains[b.y][b.x]) {
				if (valB != value) {
					return true;
				}
			}
			return false;
		}
		
		@Override
		public String toString() {
			return String.format("{(%d,%d)!=(%d,%d)}", a.x, a.y, b.x, b.y);
		}
	}


	/**
	 * tie breaking for the values.
	 */
	class ValueComperator implements Comparator<Integer> {
		
		Point varPos; 
		
		/**
		 * @param varPos the position of the variable for which the values should be ordered.
		 */
		public ValueComperator(Point varPos) {
			this.varPos = varPos;
		}

		@Override
		public int compare(Integer val1, Integer val2) {
			int conflicts1 = countNotSatisfiedConstrains(varPos, val1);
			int conflicts2 = countNotSatisfiedConstrains(varPos, val2);
			
			if (conflicts1 != conflicts2) {
				return (conflicts1 < conflicts2 ? -1 : 1);
			}
			
			if (val1 != val2) {
				return (val1 < val2 ? -1 : 1);
			}
			
			return 0;
		}
		
	}


	/**
	 * tie breaking for the variable selection.
	 */
	class VariableComperator implements Comparator<Point> {
		@Override
		public int compare(Point var1, Point var2) {
			int size1 = domains[var1.y][var1.x].size(); 
			int size2 = domains[var2.y][var2.x].size(); 

			if (size1 != size2) {
				return (size1 < size2 ? -1 : 1);		// variables with small domains first
			} 
			if (var1.y != var2.y) {
				return (var1.y < var2.y ? -1 : 1);		// then the upper variable
			} 
			if (var1.x != var2.x) {
				return (var1.x < var2.x ? -1 : 1);		// and finally the leftmost
			} 

			return 0;
		}
	}

	
	
	// ---------------------------------------------------------------------------------
	// Fields
	// ---------------------------------------------------------------------------------
	
	
	@SuppressWarnings("unchecked")
	public LinkedList<Integer>[][] domains = (LinkedList<Integer>[][])new LinkedList[9][9];
	
	
	// ---------------------------------------------------------------------------------
	// Constructor
	// ---------------------------------------------------------------------------------
	
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
	
	
	// ---------------------------------------------------------------------------------
	// Methods
	// ---------------------------------------------------------------------------------
	
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
	
	@Override
	@SuppressWarnings("unchecked")
	public Object clone() throws CloneNotSupportedException {
		CSP clone = (CSP)super.clone();

		LinkedList<Integer>[][] domains = (LinkedList<Integer>[][])new LinkedList[9][9];
		for (int i = 0; i < 9; i++) {
			for (int j = 0; j < 9; j++) {
				domains[i][j] = (LinkedList<Integer>)this.domains[i][j].clone();
			}
		}

		clone.setDomains(domains);
		
		return clone;
	}
	
	/**
	 * reduces the domain of the variable at <code>pos</code> to the value <code>val</code>.
	 * @param pos
	 * @param val
	 */
	public void fixVariable(Point pos, int val) {
		LinkedList<Integer> values = new LinkedList<Integer>();
		values.add(val);
		this.domains[pos.y][pos.x] = values;
	}
	
	public LinkedList<Integer> getDomain(int x, int y) {
		return domains[y][x];
	}

	/**
	 * adds the position of all unbound variables to <code>variables</code>.
	 * @param variables
	 */
	public void getUnboundVariables( PriorityQueue<Point> variables) {
		for (int i = 0; i < 9; i++) {
			for (int j = 0; j < 9; j++) {
				if (domains[i][j].size() > 1) {
					variables.add(new Point(j, i));
				}
			}
		}
		
	}
	
	/**
	 * returns <code>true</code> if all domains consist only of one value 
	 * and this value does not violate any constrain.
	 * @return
	 */
	public boolean isSolution() {
		for (int i = 0; i < 9; i++) {
			for (int j = 0; j < 9; j++) {
				if (domains[i][j].size() != 1) {
					return false;
				}
				
				int val = domains[i][j].peek();
				if (countNotSatisfiedConstrains(new Point(j, i), val) != 0) {
					return false;
				}
			}
		}
		return true;
		
	}
	
	/**
	 * Prints the domains to stdout.
	 */
	public void print() {
		System.out.println(this.toString());
	}


	

	/**
	 * @return a <code>String</code> of the format specified in the exercise sheet.
	 */
	public String printDomains() {
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < 9; i++) {
			for (int j = 0; j < 9; j++) {
				sb.append(String.format("%d,%d: ", j, i));
				Collections.sort(domains[i][j]);
				for (int k = 0; k < domains[i][j].size(); k++) {
					sb.append(domains[i][j].get(k));
					if (k != domains[i][j].size()-1) {
						sb.append(",");
					}
				}
				sb.append("\n");
			}

		}
		return sb.toString();
	}
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < 9; i++) {
			sb.append("[");
			for (int j = 0; j < 9; j++) {
				LinkedList<Integer> domain = domains[i][j];
				if (domain.size() == 1) {
					sb.append(domain.peek());
				} else {
					sb.append(domain);
				}
				
				if (j != 8) {
					sb.append(",");
				}
			}
			sb.append("]\n");
		}
		return sb.toString();
	}
	
	public ValueComperator valueComperator(Point varPos) {
		return new ValueComperator(varPos);
	}
	
	
	public VariableComperator variableComperator() {
		return new VariableComperator();
	}

	protected void setDomains(LinkedList<Integer>[][] domains) {
		this.domains = domains;
	}

	

	/**
	 * count the constrains which are not satisfied with the assignment of the 
	 * variable at <code>pos</code> with <code>value</code>.
	 * @param pos position of the variable
	 * @param value 
	 * @return
	 */
	private int countNotSatisfiedConstrains(Point pos, int value) {
		int counter = 0;
		Set<Arc> constrains = getNeighbors(pos);
		for (Arc con : constrains) {
			if (!con.isSatisfied(value)) {
				counter++;
			}
		}
		return counter;
	}
	
	/**
	 * Get all initial Constrains.
	 * @return
	 */
	private Set<Arc> getInitArcs() {
		Set<Arc> todo = new LinkedHashSet<>(3240);		// 3240 = binomial coefficient(81,2)
		for (int i = 0; i < 9; i++) {
			for (int j = 0; j < 9; j++) {
				todo.addAll(getNeighbors(j,i));
			}
		}
		return todo;
	}

	/**
	 * get all constrains which involve the variable at <code>(i,j)</code>.
	 * It is guaranteed that every arc has <code>(i,j)</code> as the first variable.
	 * @param x
	 * @param y
	 * @return
	 */
	private Set<Arc> getNeighbors(int x, int y) {
		Set<Arc> constrains = new LinkedHashSet<CSP.Arc>(24);
		for (int k = 0; k < 9; k++) {
			if (k != x) {
				constrains.add(new Arc(x, y, k, y));	// row constrain
			}
			if (k != y) {
				constrains.add(new Arc(x, y, x, k));	// column constrain
			}
			
			final int xOffset = 3 * (x / 3);
			final int yOffset = 3 * (y / 3);
			final int xBox = xOffset + k % 3;
			final int yBox = yOffset + k / 3;

			if ((xBox != x || yBox != y) && (xBox != y || yBox != x)) {
				constrains.add(new Arc(x, y, xBox, yBox));	// block constrain
			}
		}

		return constrains;
	}
	
	
	private Set<Arc> getNeighbors(Point a) {
		return getNeighbors(a.x, a.y);
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
	

	
}
