package Semantica;

public class Identifier extends Expression{
	private String name;
	
	public Identifier(String name){
		this.name = name;
	}
	
	public String  getName(){
		return name;
	}
	
	public void setName (String name){
		this.name = name;
	}
	
	public String toString(){
		return name;
	}
}
