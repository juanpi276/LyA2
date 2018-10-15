package Errores;

public class LexicalError extends Exception{
	private static final long serialVersionUID = 1L;
	public LexicalError (){
		super();
	}
	public LexicalError(String mensaje){
		super(mensaje);
	}
	public LexicalError(String mensaje, Throwable causa){
		super(mensaje, causa);
	}
	public LexicalError(Throwable causa){
		super(causa);
	}
}
