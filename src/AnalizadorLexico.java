/*
 * Criado por: Henrique Dreyer
 * Data Inicio: 17/08/2015
 * 
 * 2015.2 Compiladores - Silvio Bandeira
 * 
 *	site: https://github.com/HenriqueDreyer/Compiladores
 */


import java.io.FileReader;
import java.io.IOException;


public class AnalizadorLexico {	
	private FileReader arquivo;
	private AnalizadorLexicoException Exception = new AnalizadorLexicoException();
	private TokenTable tokenTable = new TokenTable();
	private static int linha = 1;
	private static int coluna = 0;
	private static char caracter = ' ';
	private static final char EoF = '\uffff';
	private static Enum palavraReservada;
	
	public AnalizadorLexico(FileReader arquivo){
		this.arquivo = arquivo;
	}
	
	public Token Scanner() throws Exception{		
		
		
			while(caracter != EoF){			
				String lexema = "";
				
				while(caracter == ' ' || caracter == '\n' || caracter == '\r' || caracter == '\t'){
					proximoToken();
				}
				
				if(caracter == EoF){
					return new Token(linha, coluna, Gramatica.EOF, "EndOfFile");
				
				}else if(Character.isLetter(caracter) || caracter == '_'){
					
					while(Character.isLetterOrDigit(caracter) || caracter == '_'){
						lexema+=caracter;
						proximoToken();
					}
					
					palavraReservada = tokenTable.isPalavraReservada(lexema);
					return new Token(linha, coluna, palavraReservada, lexema);
					
					
				}else if(Character.isDigit(caracter)){
					lexema+=caracter;
					proximoToken();
					
					while(Character.isDigit(caracter)){
						lexema+=caracter;
						proximoToken();
					}
					
					if(caracter == '.'){
						lexema+=caracter;
						proximoToken();
						
						if(!Character.isDigit(caracter)){
							Exception.FloatException(linha, coluna, lexema);
						}
						
						while(Character.isDigit(caracter)){
							lexema+=caracter;
							proximoToken();
						}
						return new Token(linha, coluna, Gramatica.TIPOFLOAT, lexema);
						
					}
					
					return new Token(linha, coluna, Gramatica.TIPOINT, lexema);
					
				}else if(caracter == '.'){
					lexema+=caracter;
					proximoToken();
					
					if(!Character.isDigit(caracter)){
						Exception.FloatException(linha, coluna, lexema);
					}
					
					while(Character.isDigit(caracter)){
						lexema+=caracter;
						proximoToken();
					}
					return new Token(linha, coluna, Gramatica.TIPOFLOAT, lexema);
					
				}else if(caracter == '\''){
					lexema+=caracter;
					proximoToken();
					
					if(!Character.isLetterOrDigit(caracter)){
						Exception.CharException(linha, coluna, lexema);
					}
					
					lexema+=caracter;
					proximoToken();
					
					if(caracter != '\''){											
						Exception.CharException(linha, coluna, lexema);
					}
					
					lexema+=caracter;
					proximoToken();
					return new Token(linha, coluna, Gramatica.TIPOCHAR, lexema);
					
				}else if(caracter == '+'){
					lexema+=caracter;
					proximoToken();
					return new Token(linha, coluna, Gramatica.SOMA, lexema);
					
				}else if(caracter == '-'){
					lexema+=caracter;
					proximoToken();
					return new Token(linha, coluna, Gramatica.SUBTRACAO, lexema);
					
				}else if(caracter == '*'){
					lexema+=caracter;
					proximoToken();
					return new Token(linha, coluna, Gramatica.MULTIPLICACAO, lexema);
					
				}else if(caracter == '/'){
					lexema+=caracter;
					proximoToken();
					
					if(caracter == '/'){
						while(caracter != '\n'){
							lexema+=caracter;
							proximoToken();
						}
					}					
					else if(caracter == '*'){
						lexema+=caracter;
						proximoToken();
						while(true){							
							if(caracter == '*'){
								proximoToken();
								if(caracter == '/'){									
									proximoToken();
									break;
								}else if(caracter == EoF){
									Exception.ComentarioException(linha, coluna, lexema);
								}
								
							}else if(caracter == EoF){
								Exception.ComentarioException(linha, coluna, lexema);
							}
							proximoToken();
						}
						
					}else {
						return new Token(linha, coluna, Gramatica.DIVISAO, lexema);
					}
					
				}else if(caracter == '='){
					lexema+=caracter;
					proximoToken();
					
					if(caracter == '='){
						lexema+=caracter;
						proximoToken();
						return new Token(linha, coluna, Gramatica.IGUAL, lexema);
					
					}else{
						return new Token(linha, coluna, Gramatica.ATRIBUICAO, lexema);
					}
					
					
				}else if(caracter == '<'){
					lexema+=caracter;
					proximoToken();
					
					if(caracter == '='){
						lexema+=caracter;
						proximoToken();
						return new Token(linha, coluna, Gramatica.MENORIGUAL, lexema);
						
					}else{
						return new Token(linha, coluna, Gramatica.MENOR, lexema);
					}
					
				}else if(caracter == '>'){
					lexema+=caracter;
					proximoToken();
					
					if(caracter == '='){
						lexema+=caracter;
						proximoToken();
						return new Token(linha, coluna, Gramatica.MAIORIGUAL, lexema);
					
					}else{					
						return new Token(linha, coluna, Gramatica.MAIOR, lexema);
					}
					
				}else if(caracter == '!'){
					lexema+=caracter;
					proximoToken();
					
					if(caracter != '='){
						lexema+=caracter;
						proximoToken();
						Exception.DeferencaException(linha, coluna, lexema);
						
					}else{
						lexema+=caracter;
						proximoToken();
						return new Token(linha, coluna, Gramatica.DIFERENCA, lexema);
					}
					
				}else if(caracter == '('){
					lexema+=caracter;
					proximoToken();
					return new Token(linha, coluna, Gramatica.ABREPARENTESES, lexema);
					
				}else if(caracter == ')'){
					lexema+=caracter;
					proximoToken();
					return new Token(linha, coluna, Gramatica.FECHAPARENTESES, lexema);
					
				}else if(caracter == '{'){
					lexema+=caracter;
					proximoToken();
					return new Token(linha, coluna, Gramatica.ABRECHAVE, lexema);
					
				}else if(caracter == '}'){
					lexema+=caracter;
					proximoToken();
					return new Token(linha, coluna, Gramatica.FECHACHAVE, lexema);
					
				}else if(caracter == ';'){
					lexema+=caracter;
					proximoToken();
					return new Token(linha, coluna, Gramatica.PONTOVIRGULA, lexema);
					
				}else if(caracter == ','){
					lexema+=caracter;
					proximoToken();
					return new Token(linha, coluna, Gramatica.VIRGULA, lexema);
				}else{
					lexema+=caracter;
					Exception.NotValidException(linha, coluna, lexema);
				}
				
			}
			return new Token(linha, coluna, Gramatica.EOF, "EndOfFile");	
		
		
	}
	
	private void proximoToken() throws IOException{		
		caracter = (char) arquivo.read();			
		isBlank();
	}	
	
	private void isBlank(){		
		if(caracter == '\t'){				
			coluna+=4;
		
		}else if(caracter == '\n'){
			linha+=1;
			coluna=0;
			
		}else{
			coluna+=1;
		}
	}
	
}
