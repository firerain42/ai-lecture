
public class Clause {
	String[] literals;
	boolean[] b;
	
	public Clause(String s){
		literals = s.split(";");
	}
	
	public boolean isEmptyClause(){
		return literals.length == 0;
	}
}
