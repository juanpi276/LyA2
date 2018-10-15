package Analizador;

public class StringLinea {
	private String cad;
	private int linea;
	
	public StringLinea(String cad,int linea) {
		this.cad = cad;
		this.linea = linea;
	}
	
	public void setString(String cad) {
		this.cad = cad;
	}
	public String getString() {
		return cad;
	}
	public void setLinea(int linea) {
		this.linea = linea;
	}
	public int getLinea() {
		return linea;
	}
}
