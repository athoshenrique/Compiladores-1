
public class Variavel{
	private Token token;
        private String lexema;
	private Integer blocoDeDeclaracao;
	private Enum tipo;

	public Variavel(String lexema, Enum tipo, Integer blocoDeDeclaracao, Token token){		
            this.token = token;
            this.blocoDeDeclaracao = blocoDeDeclaracao;
            this.tipo = tipo;
            this.lexema = lexema;
	}
	
	public Integer getBlocoDeDeclaracao(){
		return this.blocoDeDeclaracao;
	}
	
	public Token getToken(){
		return this.token;
	}
	
	public Enum getTipo(){
		
		return this.tipo;
	}
        
        public void setTipo(Enum tipo){
            this.tipo = tipo;
        }
        
        public String getLexema(){
            return this.lexema;
        }
        
        public void setLexema(String lexema){
            this.lexema = lexema;
        }
}
