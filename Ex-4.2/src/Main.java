import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;


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
			if (solve) {
				Solver s = new Solver(csp);
				writeToFile(s.printSolution());
			} else {
				writeToFile(csp.printDomains());
			}
		} catch (NotSolvableException e) {
			System.out.println("This Sudoku is not solvable.");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private static void writeToFile(String data) {
		System.out.println(data);
		try (BufferedWriter writer = new BufferedWriter(new FileWriter("output"))) {
			writer.write(data);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
