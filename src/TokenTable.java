import java.util.Iterator;
import java.util.Stack;
import java.util.logging.Level;
import java.util.logging.Logger;

/*
 * Criado por: Henrique Dreyer
 * Data Inicio: 17/08/2015
 * 
 * 2015.2 Compiladores - Silvio Bandeira
 * 
 *	site: https://github.com/HenriqueDreyer/Compiladores
 */

public class TokenTable {
    private Stack<Variavel> tabelaDeTokens;
    private AnalizadorSemanticoException analizadorSemanticoException;

    public TokenTable(){
            this.tabelaDeTokens = new Stack<>();
    }

    public Enum isPalavraReservada(String lexema){
            switch(lexema){
            case "main":
                    return Gramatica.MAIN;
            case "int":
                    return Gramatica.INT;
            case "float":
                    return Gramatica.FLOAT;
            case "char":
                    return Gramatica.CHAR;
            case "if":
                    return Gramatica.IF;
            case "else":
                    return Gramatica.ELSE;
            case "while":
                    return Gramatica.WHILE;
            case "do":
                    return Gramatica.DO;
            default:
                    return Gramatica.ID;
            }
    }

    public boolean contains(Token token){

        Iterator<Variavel> variables = tabelaDeTokens.iterator();

        while(variables.hasNext()){
            Variavel v = variables.next();
            if(token.getLexema().equals(v.getLexema())){
                return true;
            }
        }
        return false;
    }

    public boolean exist(Variavel variavel){
        Iterator<Variavel> variables = tabelaDeTokens.iterator();
        
        while(variables.hasNext()){
            Variavel v = variables.next();
            
            if(variavel.getLexema().equals(v.getLexema())){
                if(variavel.getBlocoDeDeclaracao().equals(v.getBlocoDeDeclaracao())){
                    return true;
                }
            }
        }
        return false;
    }
    
    public void add(Variavel variavel){
        tabelaDeTokens.push(variavel);        
    }

    public void matarVariaveisDeBloco(Integer bloco){
        Iterator<Variavel> iterator;		
        iterator = tabelaDeTokens.iterator();
        int cont = 0;

        if(!tabelaDeTokens.isEmpty()){
            while(iterator.hasNext()){

                if(iterator.next().getBlocoDeDeclaracao() == bloco){
                    cont++;
                }
            }
            while(cont > 0){
                    tabelaDeTokens.pop();
                    cont--;
            }			
        }
    }
    
    public Variavel get(Token token, Integer bloco){
        Variavel variavel = null;
        
        for(Variavel v : tabelaDeTokens){
            
            if(v.getLexema().equals(token.getLexema())){
                variavel = v;
                if(v.getBlocoDeDeclaracao().equals(bloco)){
                    variavel = v;
                    break;
                }
            }            
        }        
        return variavel;
    }
}
