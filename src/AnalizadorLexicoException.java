/*
 * Criado por: Henrique Dreyer
 * Data Inicio: 17/08/2015
 * 
 * 2015.2 Compiladores - Silvio Bandeira
 * 
 *	site: https://github.com/HenriqueDreyer/Compiladores
 */

public class AnalizadorLexicoException {
	
	public void FloatException(int linha, int coluna, String lexema) throws Exception{
		throw new Exception("ERRO na linha "+linha+", coluna "+coluna+", ultimo token lido: "+lexema+" :float mal formado!");
	}
	
	public void CharException(int linha, int coluna, String lexema) throws Exception{
		throw new Exception("ERRO na linha "+linha+", coluna "+coluna+", ultimo token lido: "+lexema+" :char mal formado!");
	}
	
	public void ComentarioException(int linha, int coluna, String lexema) throws Exception{
		throw new Exception("ERRO na linha "+linha+", coluna "+coluna+", ultimo token lido: "+lexema+" :bloco de comentario nao fechado!");
	}
	
	public void DeferencaException(int linha, int coluna, String lexema) throws Exception{
		throw new Exception("ERRO na linha "+linha+", coluna "+coluna+", ultimo token lido: "+lexema+" :diferenca mal formada!");
	}
	
	public void NotValidException(int linha, int coluna, String lexema) throws Exception{
		throw new Exception("ERRO na linha "+linha+", coluna "+coluna+", ultimo token lido: "+lexema+" :caractere invalido!");
	}

}
