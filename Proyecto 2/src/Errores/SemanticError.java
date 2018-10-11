package Errores;

public class SemanticError extends Exception{
	private static final long serialVersionUID = 1L;
	public SemanticError (){
		super();
	}
	public SemanticError(String mensaje){
		super(mensaje);
	}
	public SemanticError(String mensaje, Throwable causa){
		super(mensaje, causa);
	}
	public SemanticError(Throwable causa){
		super(causa);
	}
}
