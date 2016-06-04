/*
 * Criado por: Henrique Dreyer
 * Data Inicio: 17/08/2015
 * 
 * 2015.2 Compiladores - Silvio Bandeira
 * 
 *	site: https://github.com/HenriqueDreyer/Compiladores
 */

import java.io.FileNotFoundException;
import java.io.FileReader;

public class Main {

	public static void main(String[] args) {
		FileReader arquivo;
		AnalizadorSintatico analizadorSintatico;
		
		
		try {
			//arquivo = new FileReader(args[0]);
			arquivo = new FileReader("c:/Users/Henrique/Desktop/Henrique/Unicap/Compiladores/Compiladores-master/test.c");
			//arquivo = new FileReader("c:/Users/Henrique/Documents/NetBeansProjects/Compiladores/test.c");
			analizadorSintatico = new AnalizadorSintatico(arquivo);
			analizadorSintatico.parser();
			
			
			
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}		
		
	}

}
