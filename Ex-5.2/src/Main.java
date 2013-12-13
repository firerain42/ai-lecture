import java.util.HashSet;
import java.util.Set;

public class Main {

	public static void main(String[] args) {
		Resolver res = new Resolver(args[0]);
		String[] clauses = args[1].split(";");
		Set<Clause> alpha = new HashSet<Clause>();
		for(String s: clauses)
			alpha.add(new Clause(s));
		System.out.println(res.isInconsistent(alpha));
	}

}
