
public class AnalizadorSintaticoException {
    
    public void IntException(Token lookAHead) throws Exception{
        throw new Exception("ERRO na linha "+lookAHead.getLinha()+", coluna "+lookAHead.getColuna()+", ultimo token lido: "+lookAHead.getLexema()+" :int nao encontrado na declaração do main!");
    }
    
    public void MainException(Token lookAHead) throws Exception{
        throw new Exception("ERRO na linha "+lookAHead.getLinha()+", coluna "+lookAHead.getColuna()+", ultimo token lido: "+lookAHead.getLexema()+" :Nao encontrado a declaracao do main");
    }
    
    public void AbreParentesesException(Token lookAHead) throws Exception{
        throw new Exception("ERRO na linha "+lookAHead.getLinha()+", coluna "+lookAHead.getColuna()+", ultimo token lido: "+lookAHead.getLexema()+" :'(' nao encontrado");
    }
    
    public void FechaParentesesException(Token lookAHead) throws Exception{
        throw new Exception("ERRO na linha "+lookAHead.getLinha()+", coluna "+lookAHead.getColuna()+", ultimo token lido: "+lookAHead.getLexema()+" :')' nao encontrado");
    }
    
    public void EoFException(Token lookAHead) throws Exception{
        throw new Exception("ERRO na linha "+lookAHead.getLinha()+", coluna "+lookAHead.getColuna()+", ultimo token lido: "+lookAHead.getLexema()+" :Fim de arquivo nao encontrado");
    }
    
    public void AbreChaveException(Token lookAHead) throws Exception{
        throw new Exception("ERRO na linha "+lookAHead.getLinha()+", coluna "+lookAHead.getColuna()+", ultimo token lido: "+lookAHead.getLexema()+" :'{' nao encontrado na abertura de bloco");
    }
    
    public void FechaChaveException(Token lookAHead) throws Exception{
        throw new Exception("ERRO na linha "+lookAHead.getLinha()+", coluna "+lookAHead.getColuna()+", ultimo token lido: "+lookAHead.getLexema()+" :'}' nao encontrado no fim de bloco");
    }
    
    public void IdentificadorException(Token lookAHead) throws Exception{
        throw new Exception("ERRO na linha "+lookAHead.getLinha()+", coluna "+lookAHead.getColuna()+", ultimo token lido: "+lookAHead.getLexema()+" : Variavel Identificadora nao declarada");
    }
    
    public void PontoVirgulaException(Token lookAHead) throws Exception{
        throw new Exception("ERRO na linha "+lookAHead.getLinha()+", coluna "+lookAHead.getColuna()+", ultimo token lido: "+lookAHead.getLexema()+" : ';' nao localizado no fim da instrucao");
    }
    
    public void AtribuicaoException(Token lookAHead) throws Exception{
        throw new Exception("ERRO na linha "+lookAHead.getLinha()+", coluna "+lookAHead.getColuna()+", ultimo token lido: "+lookAHead.getLexema()+" : Atribuicao invalida!");
    }
    
    public void WhileException(Token lookAHead) throws Exception{
        throw new Exception("ERRO na linha "+lookAHead.getLinha()+", coluna "+lookAHead.getColuna()+", ultimo token lido: "+lookAHead.getLexema()+" : Espera-se um token 'while' depois do token '}' !");
    }
    
    public void OperadorRelacionalException(Token lookAHead) throws Exception{
        throw new Exception("ERRO na linha "+lookAHead.getLinha()+", coluna "+lookAHead.getColuna()+", ultimo token lido: "+lookAHead.getLexema()+" : Erro na operacao relacional !");
    }   
    
    public void FatorException(Token lookAHead) throws Exception{
        throw new Exception("ERRO na linha "+lookAHead.getLinha()+", coluna "+lookAHead.getColuna()+", ultimo token lido: "+lookAHead.getLexema()+" : Fator invalido !");
    }  
}
