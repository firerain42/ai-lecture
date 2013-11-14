import java.util.LinkedList;


public class Main {

	public static void main(String[] args) {
		if (args.length < 3) {
			System.err.println("not enough aguments specified.");
			return;
		}
		
		final String heuristic = args[0];
		final String filenameOfInitialState = args[1];
		final String filenameOfGoalState = args[2];
		
		try {
			World world = new World(heuristic,filenameOfInitialState,filenameOfGoalState);
			Agent agent = new Agent(world);
			LinkedList<String> resPath = agent.solve();
			
			// show output
			if (resPath == null || resPath.isEmpty()) {
				System.err.println("could not solve maze");
				return;
			}

			for (int i = 0; i < resPath.size() - 1; i++) {
				System.out.print(resPath.get(i) + ",");
			}
			System.out.print(resPath.getLast());
			System.out.println();
		} catch(Exception e) {
			e.printStackTrace();
		}
		

	}

}
