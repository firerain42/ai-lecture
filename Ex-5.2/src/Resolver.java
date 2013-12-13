import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;


public class Resolver {
	Set<Clause> allClauses = new HashSet();
	Set<Clause> newClauses = new HashSet();
	
	public Resolver (String s){
		try {
			BufferedReader in = new BufferedReader(new FileReader(s));
			String line = null;
			while ((line = in.readLine()) != null) {
				allClauses.add(new Clause(line));
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public boolean isInconsistent(Set<Clause> alpha) {
		allClauses.addAll(alpha);
		while (true){
			
			int k = allClauses.size();
			allClauses.addAll(newClauses);
			if(allClauses.size() == k)
				return false;
		}		
	}
}
