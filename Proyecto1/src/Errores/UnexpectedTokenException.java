package Errores;

public class UnexpectedTokenException extends Exception{
	private static final long serialVersionUID = 1L;
	public UnexpectedTokenException (){
		super();
	}
	public UnexpectedTokenException(String mensaje){
		super(mensaje);
	}
	public UnexpectedTokenException(String mensaje, Throwable causa){
		super(mensaje, causa);
	}
	public UnexpectedTokenException(Throwable causa){
		super(causa);
	}
}
