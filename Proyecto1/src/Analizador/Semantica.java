package Analizador;

import java.util.*;
import javax.swing.*;
import javax.swing.tree.*;
import Errores.*;

public class Semantica {
	//Arboles.
	JTree arbol;
	DefaultMutableTreeNode declaraciones;
	DefaultMutableTreeNode statements;
	//Tabla de simbolos en forma de hastable
	Hashtable<String,String> tablaSimbolos;
	LinkedList<String> codigoIntermedio;
	//Contadores.
	int linea, auxiliar, salto, salto2, instruc;
	
	public Semantica(DefaultMutableTreeNode declaraciones,DefaultMutableTreeNode statements) throws SemanticError{
            //Asignacion de variables
		this.declaraciones = declaraciones;
		this.statements = statements;
		tablaSimbolos = new Hashtable<String,String>();
		codigoIntermedio = new LinkedList<String>();
		linea = 1;
		auxiliar = 1;
		
		//Metodos que generan la tabla de simbolos y codigo intermedio.
		GeneraTabla();
		Statements();
		
		System.out.println("TABLA DE SIMBOLOS.");
      		//Transforma todas las keys de una hashtable a enumeration de tipo string.
		Enumeration<String> k = tablaSimbolos.keys();
		String key;
               	//Mientras existan elementos
		while(k.hasMoreElements()){
			//Toma el siguiente y lo almacena
			key = k.nextElement().toString();
                        System.out.println(key + "   "+ tablaSimbolos.get(key));
		}
	}
	
	public void GeneraTabla() throws SemanticError{
		String tipo, identificador, noLinea;
		//Toma el arbol de declaraciones
		arbol = new JTree(declaraciones);
		//Toma el nodo raiz.
		DefaultMutableTreeNode raiz = (DefaultMutableTreeNode) arbol.getModel().getRoot();
		//Transforma en enumeration el arbol.
		Enumeration<?> e = raiz.preorderEnumeration();
		//Toma un elemento que es vacio.
		e.nextElement();
		//Mientras existan elementos en el enumeration
		while(e.hasMoreElements()){
			//Toma el siguiente elemento que es un tipo
			tipo = e.nextElement().toString();
			//Toma el siguiente elemento que es un identificador.
			identificador = e.nextElement().toString();
			
			noLinea = e.nextElement().toString();
			//Pregunta si el elemento ya existe en la tabla de simbolos, si es asi es un error y lanza una exepcion
			if(tablaSimbolos.get(identificador)!= null)
				throw new SemanticError("--Error semantico-- El operador " +identificador+ " tipo "+tipo+" ya existe, en linea:"+ noLinea);
			//Si no tira la exepcion lo inserta en la tabla de simbolos.
			tablaSimbolos.put(identificador, tipo);
		}
	}
	
	public void Statements() throws SemanticError{
		//Toma el arbol de declaraciones
		arbol = new JTree(statements);
		//Toma el nodo raiz.
		DefaultMutableTreeNode raiz = (DefaultMutableTreeNode) arbol.getModel().getRoot();
		//Transforma en enumeration el arbol.
		Enumeration <?> e = raiz.preorderEnumeration();
		//Toma un elemento que es vacio.
		e.nextElement();
		//Mientras existan elementos en el enumeration
		while(e.hasMoreElements()){
			//Llama al metodo para que siga con al ejecucion.
			Opciones(e);
		}
		//Termina a�adiendo una linea final al codigo intermedio.
		codigoIntermedio.add(linea+"");
		linea++;
	}
	
	public void Opciones(Enumeration<?> e) throws SemanticError{
		String operator, aux, valor1, valor2, tipo1, tipo2, noLinea;
		//Toma el siguiente elemento que deberia de ser print o if.
		operator = e.nextElement().toString();
		switch(operator){
			case "Print":
				//Toma los elementos correspondientes
				aux = e.nextElement().toString();
				valor1 = e.nextElement().toString();
				valor2 = e.nextElement().toString();
				noLinea = e.nextElement().toString();
				tipo1 = ""; tipo2 = "";
				//Toma el primer tipo
				tipo1 = tablaSimbolos.get(valor1);
				if(tablaSimbolos.get(valor1) == null){
					//Si entra aqui es que no existe y pregunta si es numero, si lo es asigna un int a tipo1
					try{Integer.parseInt(valor1);tipo1 = "int";}
					//Si no puede parsear a entero tira una exepcion.
					catch(Exception err){try {Float.parseFloat(valor1);tipo1 = "float";}
						catch(Exception er){throw new SemanticError("--Error Semantico-- el operador "+ valor1 +" no existe, linea: "+noLinea);}}
				}
				//Toma el segundo tipo
				tipo2 = tablaSimbolos.get(valor2);
				if(tablaSimbolos.get(valor2) == null){
					//Si entra aqui es que no existe y pregunta si es numero, si lo es asigna un int a tipo2
					try{Integer.parseInt(valor2);tipo2 = "int";}
					//Si no puede parsear a entero tira una exepcion.
					catch(Exception err){try {Float.parseFloat(valor2);tipo2 = "float";}
						catch(Exception er){throw new SemanticError("--Error Semantico-- el operador "+ valor1 +" no existe, linea: "+noLinea);}}
				}
				//Si los tipos no son iguales tira una exepcion.
				if(!tipo1.equals(tipo2)){
					throw new SemanticError("--Error semantico-- un tipo "+ tipo2 + " no puede ser transformado a "+tipo1+ " varible " + valor1+ ", linea: "  +noLinea);
				}
				break;
			case "If":
				//Toma los elementos correspondientes
				aux = e.nextElement().toString();
				valor1 = e.nextElement().toString();
				valor2 = e.nextElement().toString();
				tipo1 = ""; tipo2 = "";
				//Toma el primer tipo
				tipo1 = tablaSimbolos.get(valor1);
				if(tablaSimbolos.get(valor1) == null){
					//Si entra aqui es que no existe y pregunta si es numero, si lo es asigna un int a tipo1
					try{Integer.parseInt(valor1);tipo1 = "int";}
					//Si no puede parsear a entero tira una exepcion.
					catch(Exception err){throw new SemanticError("--Error Semantico-- el operador "+ valor1 +" no existe");}
				}
				//Toma el segundo tipo
				tipo2 = tablaSimbolos.get(valor2);
				if(tablaSimbolos.get(valor2) == null){
					//Si entra aqui es que no existe y pregunta si es numero, si lo es asigna un int a tipo2
					try{Integer.parseInt(valor2);tipo2 = "int";}
					//Si no puede parsear a entero tira una exepcion.
					catch(Exception err){throw new SemanticError("--Error semantico-- el operador "+ valor2 + " no existe");}
				}
				//Si los tipos no son iguales tira una exepcion.
				if(!tipo1.equals(tipo2)){
					throw new SemanticError("--Error semantico-- un tipo "+ tipo2 + " no puede ser transformado a "+tipo1);
				}
				//Llamado recursivo a opciones para que continue.
				Opciones(e);
				//Llamado recursivo a opciones para que continue.
				Opciones(e);
				break;
		}
	}
}
