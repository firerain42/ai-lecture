import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Set;


public class Clause {
	private LinkedList<Literal> literals = new LinkedList<Literal>();
	int resolutionIndex = 0;
	
	public LinkedList<Literal> getLiterals() {
		return literals;
	}

	public Clause(String s){
		String[] temp = s.split(",");
		for(String lit: temp)
			literals.add(new Literal(lit));
		Collections.sort(literals);
		System.out.println(this);
	}
	
	public Clause(Set<Literal> newLiterals){
		literals.addAll(newLiterals);
		Collections.sort(literals);
	}
	
	public boolean isEmptyClause(){
		return literals.isEmpty();
	}
	
	@Override
	public boolean equals(Object c){
		LinkedList<Literal> cLiterals = ((Clause)c).getLiterals();
		return literals.equals(cLiterals);
	}
	
	public Set<Clause> resolvents(Clause c){
		Set<Clause> resolvents = new HashSet<Clause>();
		LinkedList<Literal> cLiterals = c.getLiterals();
		for(Literal l : literals)
		{
			for(Literal cl : cLiterals)
			{
				if (l.isResolvableWith(cl))
				{
					Set<Literal> newLiterals = new HashSet<Literal>();
					newLiterals.addAll(this.getLiterals());
					newLiterals.addAll(cLiterals);
					newLiterals.remove(l);
					newLiterals.remove(cl);
					Clause resolvent = new Clause(newLiterals);

					System.out.println(String.format("%s ; %s => %s", this,c, resolvent.toString()));
					resolvents.add(resolvent);
				}
			}
		}
		return resolvents;
	}
	
	public int hashCode(){
		int sum = 0;
		for(Literal l: literals)
			sum += l.hashCode();
		return sum;
	}
	
	public String toString(){
		if(isEmptyClause())
			return "leere Klausel";
		else
		{
			String s = "";
			for(Literal l: literals)
				s = s + l.toString() + ",";
			return s.substring(0, s.length()-1);
		}
	}
}
