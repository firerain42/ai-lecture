
public class Main {

	public static void main(String[] args) {
		if (args.length != 2) {
			System.err.println("usage: Ex-4.2 TESTFILE (solve|arcconsistency)");
			return;
		}

		boolean solve = args[1].equals("solve");
		
		try {
			CSP csp = new CSP(args[0]);
			csp.AC3();
			csp.print();
			if (solve) {
				Solver s = new Solver(csp);
				s.printSolution();
			}
		} catch (NotSolvableException e) {
			System.out.println("This Sudoku is not solvable.");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
