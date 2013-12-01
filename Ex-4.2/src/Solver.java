import java.awt.Point;
import java.util.PriorityQueue;


public class Solver {
	
	private CSP csp;
	
	public Solver(CSP csp) {
		this.csp = csp;
	}
	
	public int[][] solve() throws NotSolvableException {
		return backtrack(csp);
	}

	private int[][] backtrack(CSP csp) throws NotSolvableException {
		PriorityQueue<Point> variables = new PriorityQueue<>(81, csp.variableComperator());
		csp.getUnboundVariables(variables);	
		
		if (variables.isEmpty()) {
			if (csp.isSolution()) {
				int[][] solution = new int[9][9];
				for (int i = 0; i < 9; i++) {
					for (int j = 0; j < 9; j++) {
						solution[i][j] = csp.getDomain(j, i).peek();
					}
				}
				return solution;
			} else {
				throw new NotSolvableException();
			}

		}
		

		Point varPos = variables.poll();
		PriorityQueue<Integer> values = new PriorityQueue<Integer>(9, csp.valueComperator(varPos));
		values.addAll(csp.getDomain(varPos.x, varPos.y));
		while (!values.isEmpty()) {
			try {
				CSP current = (CSP) csp.clone();
				current.fixVariable(varPos, values.poll());
				current.AC3();
				return backtrack(current);
			} catch (NotSolvableException e) {
				continue;
			} catch (CloneNotSupportedException e) {
				e.printStackTrace();
			}
		}
		
		// no value solves the Sudoku.
		throw new NotSolvableException();
	}
	
	public void printSolution() throws NotSolvableException {
		int[][] solution = solve();
		for (int i = 0; i < 9; i++) {
			System.out.print("[");
			for (int j = 0; j < 9; j++) {
				System.out.print(solution[i][j]);
				if (j != 8) {
					System.out.print(",");
				}
			}
			System.out.println("]");
		}
	}

}
