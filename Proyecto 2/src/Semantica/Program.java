package Semantica;

public class Program {
	private Declaration d;
	private Statement s;
	
	public Program (Declaration d, Statement s){
		this.d = d;
		this.s = s;
	}
	
	public Declaration getDeclaration (){
		return d;
	}
	
	public void setDeclaration (Declaration d){
		this.d = d;
	}
	
	public Statement getStatement () {
		return s;
	}
	
	public void setStatement (Statement s){
		this.s = s; 
	}
}
