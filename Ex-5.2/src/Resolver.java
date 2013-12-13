import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;


public class Resolver {
	Set<Clause> allClauses = new HashSet<Clause>();
	Set<Clause> newClauses = new HashSet<Clause>();
	
	public Resolver (String s){
		try {
			BufferedReader in = new BufferedReader(new FileReader(s));
			String line = null;
			while ((line = in.readLine()) != null) {
				allClauses.add(new Clause(line));
			}
			in.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public boolean isInconsistent(Set<Clause> alpha) {
		allClauses.addAll(alpha);
		while (true){
			newClauses.clear();
			for(Clause c1 :allClauses)
				for(Clause c2:allClauses)
				{
					Set<Clause> resolvents = c1.resolvents(c2);
					//return true if empty clause is found
					for(Clause c: resolvents)
						if(c.isEmptyClause())
							return true;
					newClauses.addAll(resolvents);
				}
			int k = allClauses.size();
			allClauses.addAll(newClauses);
			if(allClauses.size() == k)
				return false;
		}		
	}
}
