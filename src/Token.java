/*
 * Criado por: Henrique Dreyer
 * Data Inicio: 17/08/2015
 * 
 * 2015.2 Compiladores - Silvio Bandeira
 * 
 *	site: https://github.com/HenriqueDreyer/Compiladores
 */

public class Token {
	private int linha;
	private int coluna;
	private Enum gramatica;
	private String lexema;
	
	public Token(int linha, int coluna, Enum token, String lexema){
		this.linha = linha;
		this.coluna = coluna;
		this.gramatica = token;
		this.lexema = lexema;
	}

	public int getLinha() {
		return linha;
	}

	public int getColuna() {
		return coluna;
	}

	public Enum getGramatica() {
		return gramatica;
	}

	public String getLexema() {
		return lexema;
	}

	public void setLinha(int linha) {
		this.linha = linha;
	}

	public void setColuna(int coluna) {
		this.coluna = coluna;
	}

	public void setGramatica(Enum token) {
		this.gramatica = token;
	}

	public void setLexema(String lexema) {
		this.lexema = lexema;
	}
	
	
	
}
