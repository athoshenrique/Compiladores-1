
public class AnalizadorSemanticoException {
	
    public void VariavelDeclaradaException(Token lookAHead) throws Exception{
        throw new Exception("ERRO na linha "+lookAHead.getLinha()+", coluna "+lookAHead.getColuna()+", ultimo token lido: "+lookAHead.getLexema()+" : Existe uma variavel de mesmo nome declarada no mesmo escopo.");
    }
	
    public void VariavelNaoDeclaradaException(Token lookAHead) throws Exception{
        throw new Exception("ERRO na linha "+lookAHead.getLinha()+", coluna "+lookAHead.getColuna()+", ultimo token lido: "+lookAHead.getLexema()+" : Variavel nao declarada!");
    }
    
    public void TipoInvalidoIntException(Token lookAHead) throws Exception{
        throw new Exception("ERRO na linha "+lookAHead.getLinha()+", coluna "+lookAHead.getColuna()+", ultimo token lido: "+lookAHead.getLexema()+" : Tipo nao compativel com INT");
    }
    
    public void TipoInvalidoFloatException(Token lookAHead) throws Exception{
        throw new Exception("ERRO na linha "+lookAHead.getLinha()+", coluna "+lookAHead.getColuna()+", ultimo token lido: "+lookAHead.getLexema()+" : Variavel FLOAT nao compativel com CHAR");
    }
    
    public void TipoInvalidoCharException(Token lookAHead) throws Exception{
        throw new Exception("ERRO na linha "+lookAHead.getLinha()+", coluna "+lookAHead.getColuna()+", ultimo token lido: "+lookAHead.getLexema()+" : Tipo CHAR so e compativel com CHAR");
    }
    
    public void ExpressaoRelacionalException(Token lookAHead) throws Exception{
        throw new Exception("ERRO na linha "+lookAHead.getLinha()+", coluna "+lookAHead.getColuna()+", ultimo token lido: "+lookAHead.getLexema()+" : Tipos nao compativeis na expressao relacional");
    }
}
