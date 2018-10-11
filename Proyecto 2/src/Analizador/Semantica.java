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
		GeneraCodigo();
		
		System.out.println("TABLA DE SIMBOLOS.");
		//Transforma todas las keys de una hashtable a enumeration de tipo string.
		Enumeration<String> k = tablaSimbolos.keys();
		String key;
		//Mientras existan elementos
		while(k.hasMoreElements()){
			//Toma el siguiente y lo almacena
			key = k.nextElement();
			System.out.println(key + "   "+ tablaSimbolos.get(key));
		}
		//Imprime el codigo intermedio
		System.out.println("\nCODIGO INTERMEDIO (CUADRUPLOS).");
		for(String c: codigoIntermedio){
			System.out.println(c);
		}
		
	}
	
	public void GeneraTabla() throws SemanticError{
		String tipo, identificador;
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
			//Pregunta si el elemento ya existe en la tabla de simbolos, si es asi es un error y lanza una exepcion
			if(tablaSimbolos.get(identificador)!= null)
				throw new SemanticError("--Error semantico-- El operador " +identificador+ " ya existe");
			//Si no tira la exepcion lo inserta en la tabla de simbolos.
			tablaSimbolos.put(identificador, tipo);
		}
	}
	
	public void GeneraCodigo() throws SemanticError{
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
		//Termina añadiendo una linea final al codigo intermedio.
		codigoIntermedio.add(linea+"");
		linea++;
	}
	
	public void Opciones(Enumeration<?> e) throws SemanticError{
		String operator, aux, valor1, valor2, tipo1, tipo2;
		//Toma el siguiente elemento que deberia de ser print o if.
		operator = e.nextElement().toString();
		switch(operator){
			case "Print":
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
				//Añade una instruccion cuadruplo de impresion.
				codigoIntermedio.add(linea+"\tPRINT\t\t\t"+valor1+aux+valor2);
				//Aumenta la linea de iteracion.
				linea++;
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
				//Añade un instruccion al intermedio como resta.
				codigoIntermedio.add(linea+ "\t-\t"+valor1+"\t"+valor2+"\tt"+auxiliar);
				//Aumenta la linea y la variable temporal
				linea ++;
				auxiliar++;
				//Guarda el salto, para ver a que lado saltara.
				salto = codigoIntermedio.size();
				//Añade una instruccion incompleta de salto ante un no cero.
				codigoIntermedio.add(linea+"\tJNZ\tt"+(auxiliar-1)+"\t\t");
				linea++;
				//Llamado recursivo a opciones para que continue.
				Opciones(e);
				//Guarda el segundo salto para pasar el else.
				salto2 = codigoIntermedio.size();
				//Añade una instruccion incompleta de salto a alguna instruccion.
				codigoIntermedio.add(linea+"\tJP\t\t\t");
				linea++;
				//Almacena la linea donde inicia el else.
				instruc = linea;
				//Llamado recursivo a opciones para que continue.
				Opciones(e);
				//Edita el primer salto ante 0 para que llegue al else.
				codigoIntermedio.set(salto, codigoIntermedio.get(salto)+instruc);
				//Edita el segundo salto para que pase el else y conitnue.
				codigoIntermedio.set(salto2, codigoIntermedio.get(salto2)+linea);
				break;
		}
	}
}
