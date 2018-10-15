package Semantica;

public class Comparation extends Expression{
	private Expression exp1, exp2;
	private String simbolo;
	
	public Comparation (Expression exp1, String simbolo, Expression exp2){
		this.exp1 = exp1;
		this.simbolo = simbolo;
		this.exp2 = exp2;
	}
	
	public Expression getExp1 (){
		return exp1;
	}
	
	public void setExp1 (Expression exp1){
		this.exp1 = exp1;
	}
	
	public String getSimbolo (){
		return simbolo;
	}
	
	public void setExp1 (String simbolo){
		this.simbolo = simbolo;
	}
	
	public Expression getExp2 (){
		return exp2;
	}
	
	public void setExp2 (Expression exp2){
		this.exp2 = exp2;
	}
	
	public String toString(){
		return exp1 + " " + simbolo + " "+ exp2;
	}
}
