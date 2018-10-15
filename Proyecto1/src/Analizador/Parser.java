package Analizador;
import java.util.*;
import Semantica.*;
import Errores.*;
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;

public class Parser {
	final int Int = 1, Float = 2, Semi = 3, Equal = 4, If = 5, Then = 6, Else = 7, Begin = 8, End = 9, Print = 10, Id = 11;
	int tokenCode, cont, noLinea;
	String  token;
	LinkedList<String> linea;
	LinkedList<LinkedList<String>> tokens;
	
	//Variables de los arboles,
	JTree program;
	DefaultMutableTreeNode declaraciones;
	DefaultMutableTreeNode declaracion;
	DefaultMutableTreeNode statements;
	DefaultMutableTreeNode statement;
	
	//El constructor del parser le llega una lista con los tokens del programa, previamente obtenidos.
	public Parser (LinkedList<LinkedList<String>> tokens){
		this.tokens = tokens;
		declaraciones = new DefaultMutableTreeNode ();
		statements = new DefaultMutableTreeNode ();
	}
	
	public void ComenzarParser() throws UnexpectedTokenException, SemanticError{
		//Inicializa los contadores.
		cont = 0;
		noLinea = 0;
		Declarations();
		Statements();
		//ImprimeArbol();
		//Si llega a este punto todo salio correcto.
		new Semantica(declaraciones, statements);
		System.out.println("Exito!");
	}
	
	public void ImprimeArbol(){
		System.out.print("Declaraciones de variables.");
		//Declara un nuevo jTree mandando el arbol de declaraciones.
		program  = new JTree (declaraciones);
		//Toma la raiz del arbol de declaraciones
		DefaultMutableTreeNode raiz = (DefaultMutableTreeNode) program.getModel().getRoot();
		//Lo transforma a un objeto de tipo Enumeration en preorden.
		Enumeration < ? > e = raiz.preorderEnumeration();
		//Recorre los elementos y los imprime
		while(e.hasMoreElements()){
			System.out.println(e.nextElement());
		}
		//Hace el mismo procedimiento con los estatutos.
		System.out.print("Estatutos.");
		program = new JTree(statements);
		raiz = (DefaultMutableTreeNode) program.getModel().getRoot();
		e = raiz.preorderEnumeration();
		while(e.hasMoreElements()){
			System.out.println(e.nextElement());
		}
	}
	
	//Toma la siguiente linea de la lista de tokens.
	public void siguienteLinea (){
		//Si la lista esta vacia, termina el metodo
		if (tokens.isEmpty())
			return;
		//Remueve el primer elemento de la lista.
		linea = tokens.removeFirst();
		//Aumenta la linea.
		noLinea ++;
	}
	
	void Avanza(){
		//Si el token es nulo lo salta.
		if(token == null)
			siguienteLinea();
		//Toma el primer elemento de la lista.
		token = linea.pollFirst();
		//Si es nulo termina el metodo
		if(token == null)
			return;
		//De lo contrario transforma el token a un codigo.
		tokenCode = StringToCode(token);
	}
	
	void Come (int token) throws UnexpectedTokenException{
		if(tokenCode == token){
			Avanza();
		}
		else {
			throw new UnexpectedTokenException("Token inesperado en la linea "+ noLinea + ".\nCodigo de token esperado: "+ token + ".\nToken encontrado: " + tokenCode);
		}
	}
	
	void Declarations () throws UnexpectedTokenException{
		Avanza();
		//Verifica que tipo de token es el que se utiliza, si no se sale del m�todo.
		if(tokenCode == Int || tokenCode == Float){
			//Toma el token como tipo, siguiendo el orden pre-establecido.
			String type = token;
			//Avanza al siguiente token.
			Avanza();
			//Toma el token como identificador.
			String identifier = token;
			//Come el token y avanza.
			Come(Id);
			//Come un punto y coma.
			Come(Semi);
			//Agrega al arbol de declaraciones una nueva declaraci�n.
			declaracion = new DefaultMutableTreeNode(type);
			declaracion.add(new DefaultMutableTreeNode(identifier));
			declaracion.add(new DefaultMutableTreeNode(noLinea));
			//A�ade el nodo declaracion al arbol declaraciones.
			declaraciones.add(declaracion);
			//Llamado recursivo.
			Declarations();
		}
	}
	//M�todo para la creacion y verificacion de los estatutos, regresa un estatuto.
	Statement Statements() throws UnexpectedTokenException{
		switch(tokenCode){
			//Aqui se verifica el if.
			case If:
				//Come un token if.
				Come(If);
				//Crea un objeto de tipo expresion, al llamar al metodo para crear la expresion.
				Comparation exp = (Comparation) Expresion();
				Avanza();
				//Agrega al arbol de estatutos.
				statement = new DefaultMutableTreeNode ("If");
				//Asgina un nodo con la expresi�n.
				statement.add(new DefaultMutableTreeNode(exp.getSimbolo()));
				statement.add(new DefaultMutableTreeNode(exp.getExp1()));
				statement.add(new DefaultMutableTreeNode(exp.getExp2()));
				//A�ade el nodo statement al arbol statements.
				statements.add(statement);
				//Come un token then.
				Come(Then);
				//Guarda un estatuto del llamado recursivo
				Statement sta = Statements();
				Come(Else);
				Statement sta2 = Statements();
				Avanza();
				break;
			case Begin:
				//Come un begin
				Come(Begin);
				//Llamado recursivo
				Statements();
				//si hay punto y coma hace otro estatuto
				while(tokenCode == Semi){
					Come(Semi);
					Statements();
				}
				//Termina con un end
				Come(End);
				Avanza();
				break;
			case Print:
				Come(Print);
				//guarda una expresi�n de tipo comparacion
				Comparation e = (Comparation) Expresion();
				Avanza();
				//Guarda en nodos de arbol la informaci�n.
				statement = new DefaultMutableTreeNode("Print");
				//Hijos del nodo de statement
				statement.add(new DefaultMutableTreeNode(e.getSimbolo()));
				statement.add(new DefaultMutableTreeNode(e.getExp1()));
				statement.add(new DefaultMutableTreeNode(e.getExp2()));
				statement.add(new DefaultMutableTreeNode(noLinea));
				//Guarda los nodos en un arbol de estatutos
				statements.add(statement);
				break;
			default:
				//Si el token es nulo lo salta
				if(token == null)
					return null;
				//de lo contrario lanza una excepci�n.
				throw new UnexpectedTokenException("Token inesperado en la linea "+ noLinea + ".\nToken encontrado: " + tokenCode);
		}
		return null;
	}
	//M�todo para realizar la parte de la expression de igualdad.
	Expression Expresion() throws UnexpectedTokenException{
		//Crea un objeto de tipo identificador con el token.
		Identifier identifier = new Identifier(token);
		Come(Id);
		String oper = token;
		Come(Equal);
		Identifier identifier2 = new Identifier(token);
		Come(Id);
		//Regresa un objeto de tipo comparacion.
		return new Comparation(identifier, oper, identifier2);
	}
	
	
	
	//Cambia una cadena dada que tenga palabra clave a su codigo.
	public int StringToCode(String cad){
		int code = 0;
		switch(cad){
			case "int":
				code = Int;
				break;
			case "float":
				code = Float;
				break;                                                   
			case ";":
				code = Semi;
				break;
			case "=":
				code = Equal;
				break;
			case "if":
				code = If;
				break;
			case "then":
				code = Then;
				break;
			case "else":
				code = Else;
				break;
			case "begin":
				code = Begin;
				break;
			case "end":
				code = End;
				break;
			case "print":
				code = Print;
				break;
			default:
				//Codigo por default para los identificadores
				code = Id;
		}
		return code;
	}

	void Error(String tipo){
		System.out.println("Error en el programa: " + tipo);
	}
}
