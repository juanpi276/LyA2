package Semantica;

public class If extends Statement{
	private Expression exp;
	private Statement s,s2;
	
	public If(Expression exp, Statement s, Statement s2){
		this.exp = exp;
		this.s = s;
		this.s2 = s2;
	}
	
	public Expression getExp(){
		return exp;
	}
	
	public void setExp(Expression exp){
		this.exp = exp;
	}
	
	public Statement getS (){
		return s;
	}
	
	public void setS (Statement s){
		this.s = s;
	}
	
	public Statement getS2 (){
		return s2;
	}
	
	public void setS2 (Statement s2){
		this.s2 = s2;
	}
	
	/*public String toString(){
		return exp.toString()+ " " + s;
	}*/
	
}
