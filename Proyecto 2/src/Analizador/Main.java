package Analizador;
import java.io.*;
import java.util.*;

import javax.swing.JTextArea;

import Errores.SemanticError;
import Errores.UnexpectedTokenException;

public class Main {
	Parser p;
	//Crea un lista de listas de strings para almacenar los tokens.
	LinkedList<LinkedList<String>> tokens;
	
	public Main()throws UnexpectedTokenException, SemanticError{
		//inicializa la lista.
		tokens = new LinkedList<LinkedList<String>>();
		try {
			//Llama al metodo para que lea el archivo y lo parta en tokens.
			LeerArchivo("C:\\Users\\JuanPablo\\Desktop\\Proyecto 2\\Ejemplo.txt");
		}catch (FileNotFoundException e) {e.printStackTrace();} 
		catch (IOException e) {e.printStackTrace();}
		//Crea un nuevo objeto de tipo parser y comienza a hacer las operaciones.
		p = new Parser(tokens);
		p.ComenzarParser();
	}
	
	public static void main(String [] args) throws UnexpectedTokenException, SemanticError{
		new Main();
	}
	
	public void LeerArchivo(String ruta) throws FileNotFoundException, IOException{
		String cad;
		//Lee el archivo en la ruta especificada.
		FileReader f = new FileReader(ruta);
		//Crea un buffer con el archivo
		BufferedReader b = new BufferedReader(f);
		//Lee linea por linea.
		while((cad = b.readLine()) !=null){
			//llama al metodo que lo convierte en tokenizer.
			Tokenizar(cad);
		}
		//Cierra el buffer de lectura.
		b.close();
	}
	
	public void Tokenizar(String cad){
		//lista provisional donde se almacenan los tokens por linea
		LinkedList<String> l = new LinkedList<String> ();
		//Parte en tokens la cadena que llega.
		StringTokenizer t = new StringTokenizer(cad);
		//Hace esta funcion mientras haya mas tokens.
		while(t.hasMoreTokens()){
			l.add(t.nextToken());
		}
		//Agrega la lista de tokens a la lista de listas.
		tokens.add(l);
	}
}
