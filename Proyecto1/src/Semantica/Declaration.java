package Semantica;

public class Declaration {
	private Type type;
	private Identifier identifier;
	
	public Declaration(Type type, Identifier identifier){
		this.type = type;
		this.identifier = identifier;
	}
	
	public Type getType(){
		return type;
	}
	
	public void setType(Type type){
		this.type = type;
	}
	
	public Identifier getIdentifier (){
		return identifier;
	}
	
	public void setIdentifier (Identifier identifier){
		this.identifier = identifier;
	}
}
