import java.util.LinkedList;


public class Main {

	public static void main(String[] args) {
		if (args.length < 2) {
			System.err.println("not enough aguments specified.");
			return;
		}
		
		final boolean dfs = (args[0].equals("dfs"));
		final String filename = args[1];
		
		try {
			World world = new World(filename);
			Agent agent = new Agent(world, dfs);
			LinkedList<String> resPath = agent.solveMaze();
			
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
