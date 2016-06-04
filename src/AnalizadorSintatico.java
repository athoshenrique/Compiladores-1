/*
 * Criado por: Henrique Dreyer
 * Data Inicio: 17/08/2015
 * 
 * 2015.2 Compiladores - Silvio Bandeira
 * 
 *	site: https://github.com/HenriqueDreyer/Compiladores
 */

import java.io.FileReader;

public class AnalizadorSintatico {
    private AnalizadorLexico analizadorLexico;
    private AnalizadorSintaticoException exception;
    private AnalizadorSemanticoException analizadorSemanticoException;    
    private FileReader arquivo;
    private Token lookAHead;
    private TokenTable tokenTable;
    private Integer bloco = 0;
    private Integer nLabel = 0;    
    private Integer tLabel = 0; 
    
    public AnalizadorSintatico(FileReader arquivo){ //ok
        this.arquivo = arquivo;
        this.analizadorLexico = new AnalizadorLexico(arquivo);
        this.exception = new AnalizadorSintaticoException();
        this.analizadorSemanticoException = new AnalizadorSemanticoException();
        this.tokenTable = new TokenTable();        
    }
    
    private void proximoToken() throws Exception{ //ok        
        lookAHead = analizadorLexico.Scanner();        
    }
    
    // First's
    
    private boolean firstDeclaracaoVariaveis(){ //ok
    	
    	if(lookAHead.getGramatica() == Gramatica.INT){
    		return true;
    	}else if(lookAHead.getGramatica() == Gramatica.FLOAT){
    		return true;
    	}else if(lookAHead.getGramatica() == Gramatica.CHAR){
    		return true;
    	}    	
    	return false;
    }
    
    private boolean firstComandoBasico(){ //ok
    	
    	if(lookAHead.getGramatica() == Gramatica.ID){
    		return true;
    	}else if(lookAHead.getGramatica() == Gramatica.ABRECHAVE){
    		return true;
    	}    	
    	return false;
    }
    
    private boolean firstComando(){
    	if(lookAHead.getGramatica() == Gramatica.DO){
    		return true;
    	}else if(lookAHead.getGramatica() == Gramatica.WHILE){
    		return true;
    	}else if(lookAHead.getGramatica() == Gramatica.ID){
    		return true;
    	}else if(lookAHead.getGramatica() == Gramatica.IF){
    		return true;
    	}else if(lookAHead.getGramatica() == Gramatica.ABRECHAVE){
    		return true;
    	}
    	return false;
    }
    
    private boolean firstIteracao(){
    	if(lookAHead.getGramatica() == Gramatica.WHILE){
    		return true;
    		
    	}else if(lookAHead.getGramatica() == Gramatica.DO){
    		return true;
    	}
    	return false;
    }
    
    // Outros métodos
    
    public void parser(){   //ok
        //<programa> ::= int main"("")" <bloco>
    	
    	try{
            proximoToken();

            if(lookAHead.getGramatica() != Gramatica.INT){
                exception.IntException(lookAHead);
            }
            proximoToken();

            if(lookAHead.getGramatica() != Gramatica.MAIN){
                exception.MainException(lookAHead);
            }
            proximoToken();

            if(lookAHead.getGramatica() != Gramatica.ABREPARENTESES){
                exception.AbreParentesesException(lookAHead);
            }
            proximoToken();

            if(lookAHead.getGramatica() != Gramatica.FECHAPARENTESES){
                exception.FechaParentesesException(lookAHead);
            }
            proximoToken();

            bloco();

            if(lookAHead.getGramatica() != Gramatica.EOF){
                exception.EoFException(lookAHead);
            }
	        
    	}catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
    
    private void bloco() throws Exception{ //ok
        //<bloco> ::= “{“ {<decl_var>}* {<comando>}* “}”     	
    	
        if(lookAHead.getGramatica() != Gramatica.ABRECHAVE){
            exception.AbreChaveException(lookAHead);
        }   
        proximoToken();
        this.bloco+=1;
        
        while(firstDeclaracaoVariaveis()){
            declaracaoVariaveis();
        }
        
        while(firstComando()){
            comando();
        }
        
        if(lookAHead.getGramatica() != Gramatica.FECHACHAVE){
            exception.FechaChaveException(lookAHead);
        }
        proximoToken();
        
        this.tokenTable.matarVariaveisDeBloco(bloco);
        this.bloco-=1;
    }
    
    private void declaracaoVariaveis() throws Exception{ //ok 
    	
    	//pega o tipo da variavel declarada
    	Token tipoDeclaracao = lookAHead;
    	
    	proximoToken();
    	
    	if(lookAHead.getGramatica() != Gramatica.ID){
            exception.IdentificadorException(lookAHead);
    	}
    	
    	//Adiciona uma variavel a tabela de variaveis declaradas
    	Variavel variavel = new Variavel(lookAHead.getLexema(), tipoDeclaracao.getGramatica(), bloco, lookAHead);
        
        if(tokenTable.exist(variavel)){
            this.analizadorSemanticoException.VariavelDeclaradaException(variavel.getToken());
        }
        
    	this.tokenTable.add(variavel);            	   
        
    	proximoToken();
    	
    	if(lookAHead.getGramatica() == Gramatica.VIRGULA){
            while(lookAHead.getGramatica() == Gramatica.VIRGULA){
                proximoToken();

                if(lookAHead.getGramatica() != Gramatica.ID){
                exception.IdentificadorException(lookAHead);
            }
                //Adiciona uma variavel a tabela de variaveis declaradas
                variavel = new Variavel(lookAHead.getLexema(), tipoDeclaracao.getGramatica(), bloco, lookAHead);
                this.tokenTable.add(variavel);

                proximoToken();
            }
    	}    	
    	if(lookAHead.getGramatica() != Gramatica.PONTOVIRGULA){
    		exception.PontoVirgulaException(lookAHead);
    	}
    	proximoToken();    	
    }  
    
    private void comando() throws Exception{
    	//<comando> ::= <comando_básico> | <iteração> | if "("<expr_relacional>")" <comando> {else <comando>}?
    	
    	if(firstComandoBasico()){
    		comandoBasico();
    		
    	}else if(firstIteracao()){
    		iteracao();
    		
    	}else if(lookAHead.getGramatica() == Gramatica.IF){
            proximoToken();

            if(lookAHead.getGramatica() != Gramatica.ABREPARENTESES){
                exception.AbreParentesesException(lookAHead);
            }
            proximoToken();
            
            Variavel expRelacional = expressaoRelacional();
            
            String label = newLabel();
            System.out.println("if "+expRelacional.getLexema()+" != 0 goto "+label);
            
            if(lookAHead.getGramatica() != Gramatica.FECHAPARENTESES){
                exception.FechaParentesesException(lookAHead);
            }
            proximoToken();
              
            comando();
            
            String label2 = newLabel();
            System.out.println("goto "+label2);
            
            System.out.println(label+":");
            
            if(lookAHead.getGramatica() == Gramatica.ELSE){
                proximoToken();
                
                comando();
                
            }
            
            System.out.println(label2+":");
    	}
    }
    
    public void comandoBasico() throws Exception{ //ok
    	//<comando_básico> ::= <atribuição> | <bloco>
    	
    	if(lookAHead.getGramatica() == Gramatica.ABRECHAVE){
            bloco();
    		
    	}else if(lookAHead.getGramatica() == Gramatica.ID){
            if(!tokenTable.contains(lookAHead)){
                    analizadorSemanticoException.VariavelNaoDeclaradaException(lookAHead);
            }
            atribuicao();
    	}
    }
    
    public void iteracao() throws Exception{
    	//<iteração> ::= while "("<expr_relacional>")" <comando> | do <comando> while "("<expr_relacional>")"";"
    	
    	if(lookAHead.getGramatica() == Gramatica.WHILE){
            proximoToken();

            if(lookAHead.getGramatica() != Gramatica.ABREPARENTESES){    			
                    exception.AbreParentesesException(lookAHead);
            }
            proximoToken();  
            
            String label = newLabel();
            System.out.println(label+":");
            
            Variavel expRelacional = expressaoRelacional();
            
            
            String label2 = newLabel(); 
            System.out.println("if "+expRelacional.getLexema()+" != 0 goto "+label2);
            
            if(lookAHead.getGramatica() != Gramatica.FECHAPARENTESES){
                    exception.FechaParentesesException(lookAHead);
            }
            proximoToken();
                    
            comando();
            
            System.out.println("goto "+label);
            System.out.println(label2+":");
            
    	}else if(lookAHead.getGramatica() == Gramatica.DO){
            proximoToken();          
            
            String label = newLabel();
            System.out.println(label+":");
            
            comando();
            
            if(lookAHead.getGramatica() != Gramatica.WHILE){
                    exception.WhileException(lookAHead);
            }
            proximoToken();

            if(lookAHead.getGramatica() != Gramatica.ABREPARENTESES){
                    exception.AbreParentesesException(lookAHead);
            }
            proximoToken();

            Variavel expRelacional = expressaoRelacional();
            
            String label2 = newLabel();
            System.out.println("if "+expRelacional.getLexema()+" != 0 goto "+label2);
            System.out.println("goto "+label);
            System.out.println(label2+":");
            
            if(lookAHead.getGramatica() != Gramatica.FECHAPARENTESES){
                    exception.FechaParentesesException(lookAHead);
            }
            proximoToken();

            if(lookAHead.getGramatica() != Gramatica.PONTOVIRGULA){
                    exception.PontoVirgulaException(lookAHead);
            }
            proximoToken();
    	}
    }
    
    public void atribuicao() throws Exception{
    	//<atribuição> ::= <id> "=" <expr_arit> ";"
    	
    	if(lookAHead.getGramatica() == Gramatica.ID){
            if(!tokenTable.contains(lookAHead)){
                    analizadorSemanticoException.VariavelNaoDeclaradaException(lookAHead);
            }

            //Busca na tabela a variavel
            Variavel operando1 = tokenTable.get(lookAHead,bloco);            
            proximoToken();

            if(lookAHead.getGramatica() != Gramatica.ATRIBUICAO){
                    exception.AtribuicaoException(lookAHead);
            }
            proximoToken();

            Variavel operando2 = expressaoAritmetica();
                        
            //Comparacao de tipos final # x = (algumacoisa)            
            if(operando1.getTipo().equals(Gramatica.INT)){
                if(!operando2.getTipo().equals(Gramatica.TIPOINT) && 
                        !operando2.getTipo().equals(Gramatica.INT)){
                    
                    analizadorSemanticoException.TipoInvalidoIntException(operando2.getToken());
                }
                System.out.println(operando1.getLexema()+" = "+operando2.getLexema());
            }
            else if(operando1.getTipo().equals(Gramatica.FLOAT)){
                if(operando2.getTipo().equals(Gramatica.TIPOINT) || 
                        operando2.getTipo().equals(Gramatica.INT)){
                    
                    Variavel variavel = new Variavel(operando2.getLexema(), operando2.getTipo(), operando2.getBlocoDeDeclaracao(), operando2.getToken());
                    String temp = newVarTemporaria();
                    System.out.print(temp+" = ");
                    intTOfloat(variavel);
                    variavel.setLexema(temp);
                    
                    System.out.println(operando1.getLexema()+" = "+variavel.getLexema());                                                            
                }
                else if(operando2.getTipo().equals(Gramatica.CHAR) ||
                        operando2.getTipo().equals(Gramatica.TIPOCHAR)){
                    
                    analizadorSemanticoException.TipoInvalidoFloatException(lookAHead);
                }else{
                	System.out.println(operando1.getLexema()+" = "+operando2.getLexema());
                }
            }
            else if(operando1.getTipo().equals(Gramatica.CHAR)){
                if(!operando2.getTipo().equals(Gramatica.CHAR) &&
                        !operando2.getTipo().equals(Gramatica.TIPOCHAR)){
                    
                    analizadorSemanticoException.TipoInvalidoCharException(operando2.getToken());
                }
                System.out.println(operando1.getLexema()+" = "+operando2.getLexema());
            }          
            
            if(lookAHead.getGramatica() != Gramatica.PONTOVIRGULA){
                    exception.PontoVirgulaException(lookAHead);
            }
            proximoToken();
    	}
    }
    
    public Variavel expressaoRelacional() throws Exception{
    	//<expr_relacional> ::= <expr_arit> <op_relacional> <expr_arit>
    	
    	Variavel operando1 = expressaoAritmetica();
    	
    	Token opRelacional = operadorRelacional();
    	
    	Variavel operando2 = expressaoAritmetica();
        
        if(operando1.getTipo().equals(Gramatica.TIPOCHAR) ||
                operando1.getTipo().equals(Gramatica.CHAR)){
            
            if(!operando2.getTipo().equals(Gramatica.TIPOCHAR) &&
                    !operando2.getTipo().equals(Gramatica.CHAR)){
                
                analizadorSemanticoException.ExpressaoRelacionalException(operando2.getToken());
            }
        }else{
            if(!operando1.getTipo().equals(Gramatica.TIPOCHAR) &&
                !operando1.getTipo().equals(Gramatica.CHAR)){
                
                if(operando2.getTipo().equals(Gramatica.TIPOCHAR) ||
                    operando2.getTipo().equals(Gramatica.CHAR)){
                
                    analizadorSemanticoException.ExpressaoRelacionalException(operando2.getToken());
                }
            }
        }
        String temp = newVarTemporaria();
        System.out.println(temp+" = "+operando1.getLexema()+opRelacional.getLexema()+operando2.getLexema());
        operando1.setLexema(temp);
        return operando1;
    }
    
    public Variavel expressaoAritmetica() throws Exception{    	
    	Variavel operando1 = termo();
        Token operador = lookAHead; // operação aritimetica (+,-,*,/)
    	Variavel operando2 = expressaoAritmetica_();
        Variavel variavel = null;
        
        if(operando2 != null){
                     
            if(operando1.getTipo().equals(Gramatica.INT) || 
                    operando1.getTipo().equals(Gramatica.TIPOINT)){               
                
                if(operando2.getTipo().equals(Gramatica.TIPOFLOAT) || 
                        operando2.getTipo().equals(Gramatica.FLOAT)){
                    
                    variavel = new Variavel(operando1.getLexema(), operando1.getTipo(), operando1.getBlocoDeDeclaracao(), operando2.getToken());
                    String temp = newVarTemporaria();
                    System.out.print(temp+" = ");
                    intTOfloat(variavel);
                    variavel.setLexema(temp);
                    
                    String temp2 = newVarTemporaria();
                    System.out.println(temp2+" = "+variavel.getLexema()+operador.getLexema()+operando2.getLexema());
                    variavel.setLexema(temp2);
                    
                    return variavel;
                
                }else if(operando2.getTipo().equals(Gramatica.CHAR) ||
                        operando2.getTipo().equals(Gramatica.TIPOCHAR)){

                    analizadorSemanticoException.TipoInvalidoCharException(operando2.getToken());
                
                }else{
                	String temp = newVarTemporaria();
                	System.out.println(temp+" = "+operando1.getLexema()+operador.getLexema()+operando2.getLexema());
                	
                	operando2.setLexema(temp);
                	return operando2;
                }
            }
            else if(operando1.getTipo().equals(Gramatica.FLOAT) ||
                    operando1.getTipo().equals(Gramatica.TIPOFLOAT)){
                
                if(operando2.getTipo().equals(Gramatica.TIPOINT) || 
                        operando2.getTipo().equals(Gramatica.INT)){
                    
                    variavel = new Variavel(operando2.getLexema(), operando2.getTipo(), operando2.getBlocoDeDeclaracao(), operando2.getToken());
                    String temp = newVarTemporaria();
                    System.out.print(temp+" = ");
                    intTOfloat(variavel);
                    variavel.setLexema(temp);
                    
                    String temp2 = newVarTemporaria();
                    System.out.println(temp2+" = "+variavel.getLexema()+operador.getLexema()+operando1.getLexema());
                    variavel.setLexema(temp2);
                    
                    return variavel;
                    
                }else if(operando2.getTipo().equals(Gramatica.CHAR) ||
                        operando2.getTipo().equals(Gramatica.TIPOCHAR)){

                    analizadorSemanticoException.TipoInvalidoCharException(operando2.getToken());
                }else{
                	String temp = newVarTemporaria();
                	System.out.println(temp+" = "+operando1.getLexema()+operador.getLexema()+operando2.getLexema());
                	operando2.setLexema(temp);
                	return operando2;
                }                
            }
            else if(operando1.getTipo().equals(Gramatica.CHAR) ||
                    operando1.getTipo().equals(Gramatica.TIPOCHAR)){
                
                if(!operando2.getTipo().equals(Gramatica.CHAR) &&
                        !operando2.getTipo().equals(Gramatica.TIPOCHAR)){

                    analizadorSemanticoException.TipoInvalidoCharException(operando2.getToken());
                }
            }
            String temp = newVarTemporaria();
            System.out.println(temp+" = "+operando1.getLexema()+operador.getLexema()+operando2.getLexema());
            operando2.setLexema(temp);
            return operando2;
        }
        return operando1;        
    }    
    
    public Variavel termo() throws Exception{    	
    	Variavel operando1 = fator();
        Token operador = lookAHead; // operação aritimetica (+,-,*,/)
    	Variavel operando2 = termo_();
        Variavel variavel = null;
        
        if(operando2 != null){
            
            if(operando1.getTipo().equals(Gramatica.INT) || 
                    operando1.getTipo().equals(Gramatica.TIPOINT)){               
                
                if(operando2.getTipo().equals(Gramatica.TIPOFLOAT) || 
                        operando2.getTipo().equals(Gramatica.FLOAT)){
                    
                    variavel = new Variavel(operando1.getLexema(), operando1.getTipo(), operando1.getBlocoDeDeclaracao(), operando2.getToken());
                    String temp = newVarTemporaria();
                    System.out.print(temp+" = ");
                    intTOfloat(variavel);
                    variavel.setLexema(temp);
                    
                    String temp2 = newVarTemporaria();
                    System.out.println(temp2+" = "+variavel.getLexema()+operador.getLexema()+operando2.getLexema());
                    variavel.setLexema(temp2);
                                       
                    return variavel;
                    
                }else if(operando2.getTipo().equals(Gramatica.CHAR) ||
                        operando2.getTipo().equals(Gramatica.TIPOCHAR)){

                    analizadorSemanticoException.TipoInvalidoCharException(operando2.getToken());
                }else{
                	String temp = newVarTemporaria();
                	System.out.println(temp+" = "+operando1.getLexema()+operador.getLexema()+operando2.getLexema());
                	operando2.setLexema(temp);
                	return operando2;
                }
            }
            else if(operando1.getTipo().equals(Gramatica.FLOAT) ||
                    operando1.getTipo().equals(Gramatica.TIPOFLOAT)){
                
                if(operando2.getTipo().equals(Gramatica.TIPOINT) || 
                        operando2.getTipo().equals(Gramatica.INT)){
                    
                    variavel = new Variavel(operando2.getLexema(), operando2.getTipo(), operando2.getBlocoDeDeclaracao(), operando2.getToken());
                    String temp = newVarTemporaria();
                    System.out.print(temp+" = ");
                    intTOfloat(variavel);
                    variavel.setLexema(temp);
                    
                    String temp2 = newVarTemporaria();
                    System.out.println(temp2+" = "+variavel.getLexema()+operador.getLexema()+operando1.getLexema());
                    variavel.setLexema(temp2);
                                       
                    return variavel;
                    
                }else if(operando2.getTipo().equals(Gramatica.CHAR) ||
                        operando2.getTipo().equals(Gramatica.TIPOCHAR)){

                    analizadorSemanticoException.TipoInvalidoCharException(operando2.getToken());
                }else{
                	String temp = newVarTemporaria();
                	System.out.println(temp+" = "+operando1.getLexema()+operador.getLexema()+operando2.getLexema());
                	operando2.setLexema(temp);
                	return operando2;
                }
            }
            else if(operando1.getTipo().equals(Gramatica.CHAR) ||
                    operando1.getTipo().equals(Gramatica.TIPOCHAR)){
                
                if(!operando2.getTipo().equals(Gramatica.CHAR) &&
                        !operando2.getTipo().equals(Gramatica.TIPOCHAR)){

                    analizadorSemanticoException.TipoInvalidoCharException(operando2.getToken());
                }
            }
            String temp = newVarTemporaria();
            System.out.println(temp+" = "+operando1.getLexema()+operador.getLexema()+operando2.getLexema());
            operando2.setLexema(temp);
            return operando2;
        }
        
        return operando1;
    }
    
    public Variavel expressaoAritmetica_() throws Exception{
        Variavel variavel = null;
    	
    	if(lookAHead.getGramatica() == Gramatica.SOMA){            
            proximoToken();
            Variavel operando1 = termo();
            Token operador = lookAHead; // operação aritimetica (+,-,*,/)
            Variavel operando2 = expressaoAritmetica_();
            
            if(operando2 != null){
            
                if(operando1.getTipo().equals(Gramatica.INT) || 
                    operando1.getTipo().equals(Gramatica.TIPOINT)){               
                
                    if(operando2.getTipo().equals(Gramatica.TIPOFLOAT) || 
                            operando2.getTipo().equals(Gramatica.FLOAT)){
                        
                        variavel = new Variavel(operando1.getLexema(), operando1.getTipo(), operando1.getBlocoDeDeclaracao(), operando2.getToken());
                        String temp = newVarTemporaria();
                        System.out.print(temp+" = ");
                        intTOfloat(variavel);
                        variavel.setLexema(temp);
                        
                        String temp2 = newVarTemporaria();
                        System.out.println(temp2+" = "+variavel.getLexema()+operador.getLexema()+operando2.getLexema());
                        variavel.setLexema(temp2);
                        
                        return variavel;
                        
                    }else if(operando2.getTipo().equals(Gramatica.CHAR) ||
                        operando2.getTipo().equals(Gramatica.TIPOCHAR)){

                        analizadorSemanticoException.TipoInvalidoCharException(operando2.getToken());
                    }else{
                    	String temp = newVarTemporaria();
                    	System.out.println(temp+" = "+operando1.getLexema()+operador.getLexema()+operando2.getLexema());
                    	operando2.setLexema(temp);
                    	return operando2;
                    }
                }
                else if(operando1.getTipo().equals(Gramatica.FLOAT) ||
                        operando1.getTipo().equals(Gramatica.TIPOFLOAT)){

                    if(operando2.getTipo().equals(Gramatica.TIPOINT) || 
                            operando2.getTipo().equals(Gramatica.INT)){
                        
                        variavel = new Variavel(operando2.getLexema(), operando2.getTipo(), operando2.getBlocoDeDeclaracao(), operando2.getToken());
                        String temp = newVarTemporaria();
                        System.out.print(temp+" = ");
                        intTOfloat(variavel);
                        variavel.setLexema(temp);
                        
                        String temp2 = newVarTemporaria();
                        System.out.println(temp2+" = "+variavel.getLexema()+operador.getLexema()+operando1.getLexema());
                        variavel.setLexema(temp2);
                                             
                        return variavel;
                        
                    }else if(operando2.getTipo().equals(Gramatica.CHAR) ||
                        operando2.getTipo().equals(Gramatica.TIPOCHAR)){

                        analizadorSemanticoException.TipoInvalidoCharException(operando2.getToken());
                    }else{
                    	String temp = newVarTemporaria();
                    	System.out.println(temp+" = "+operando1.getLexema()+operador.getLexema()+operando2.getLexema());
                    	operando2.setLexema(temp);
                    	return operando2;
                    }
                }
                else if(operando1.getTipo().equals(Gramatica.CHAR) ||
                        operando1.getTipo().equals(Gramatica.TIPOCHAR)){

                    if(!operando2.getTipo().equals(Gramatica.CHAR) &&
                            !operando2.getTipo().equals(Gramatica.TIPOCHAR)){

                        analizadorSemanticoException.TipoInvalidoCharException(operando2.getToken());
                    }
                }
                String temp = newVarTemporaria();
                System.out.println(temp+" = "+operando1.getLexema()+operador.getLexema()+operando2.getLexema());
                operando2.setLexema(temp);
                return operando2;
            }            
            return operando1;
    		
    	}else if(lookAHead.getGramatica() == Gramatica.SUBTRACAO){            
            proximoToken();
            Variavel operando1 = termo();
            Token operador = lookAHead; // operação aritimetica (+,-,*,/)
            Variavel operando2 = expressaoAritmetica_();
            
            if(operando2 != null){
            
                 if(operando1.getTipo().equals(Gramatica.INT) || 
                    operando1.getTipo().equals(Gramatica.TIPOINT)){               
                
                    if(operando2.getTipo().equals(Gramatica.TIPOFLOAT) || 
                            operando2.getTipo().equals(Gramatica.FLOAT)){
                        
                        variavel = new Variavel(operando1.getLexema(), operando1.getTipo(), operando1.getBlocoDeDeclaracao(), operando2.getToken());
                        String temp = newVarTemporaria();
                        System.out.print(temp+" = ");
                        intTOfloat(variavel);
                        variavel.setLexema(temp);
                        
                        String temp2 = newVarTemporaria();
                        System.out.println(temp2+" = "+variavel.getLexema()+operador.getLexema()+operando2.getLexema());
                        variavel.setLexema(temp2);
                                                
                        return variavel;
                        
                    }else if(operando2.getTipo().equals(Gramatica.CHAR) ||
                        operando2.getTipo().equals(Gramatica.TIPOCHAR)){

                        analizadorSemanticoException.TipoInvalidoCharException(operando2.getToken());
                    }else{
                    	String temp = newVarTemporaria();
                    	System.out.println(temp+" = "+operando1.getLexema()+operador.getLexema()+operando2.getLexema());
                    	operando2.setLexema(temp);
                    	return operando2;
                    }
                }
                else if(operando1.getTipo().equals(Gramatica.FLOAT) ||
                        operando1.getTipo().equals(Gramatica.TIPOFLOAT)){

                    if(operando2.getTipo().equals(Gramatica.TIPOINT) || 
                            operando2.getTipo().equals(Gramatica.INT)){
                        
                        variavel = new Variavel(operando2.getLexema(), operando2.getTipo(), operando2.getBlocoDeDeclaracao(), operando2.getToken());
                        String temp = newVarTemporaria();
                        System.out.print(temp+" = ");
                        intTOfloat(variavel);
                        variavel.setLexema(temp);
                        
                        String temp2 = newVarTemporaria();
                        System.out.println(temp2+" = "+variavel.getLexema()+operador.getLexema()+operando1.getLexema());
                        variavel.setLexema(temp2);
                                                
                        return variavel;
                        
                    }else if(operando2.getTipo().equals(Gramatica.CHAR) ||
                        operando2.getTipo().equals(Gramatica.TIPOCHAR)){

                        analizadorSemanticoException.TipoInvalidoCharException(operando2.getToken());
                    }else{
                    	String temp = newVarTemporaria();
                    	System.out.println(temp+" = "+operando1.getLexema()+operador.getLexema()+operando2.getLexema());
                    	operando2.setLexema(temp);
                    	return operando2;
                    }
                }
                else if(operando1.getTipo().equals(Gramatica.CHAR) ||
                        operando1.getTipo().equals(Gramatica.TIPOCHAR)){

                    if(!operando2.getTipo().equals(Gramatica.CHAR) &&
                            !operando2.getTipo().equals(Gramatica.TIPOCHAR)){

                        analizadorSemanticoException.TipoInvalidoCharException(operando2.getToken());
                    }
                }
                String temp = newVarTemporaria();
                System.out.println(temp+" = "+operando1.getLexema()+operador.getLexema()+operando2.getLexema());
                operando2.setLexema(temp);
                return operando2;
            }            
            return operando1;            
    	}
        return null;
    }
    
    public Variavel termo_() throws Exception{
        Variavel variavel = null;
    	
    	if(lookAHead.getGramatica() == Gramatica.MULTIPLICACAO){            
            proximoToken();
            Variavel operando1 = termo();
            Token operador = lookAHead; // operação aritimetica (+,-,*,/)
            Variavel operando2 = termo_();
            
            if(operando2 != null){
            
                 if(operando1.getTipo().equals(Gramatica.INT) || 
                    operando1.getTipo().equals(Gramatica.TIPOINT)){               
                
                    if(operando2.getTipo().equals(Gramatica.TIPOFLOAT) || 
                            operando2.getTipo().equals(Gramatica.FLOAT)){
                        
                        variavel = new Variavel(operando1.getLexema(), operando1.getTipo(), operando1.getBlocoDeDeclaracao(), operando2.getToken());
                        String temp = newVarTemporaria();
                        System.out.print(temp+" = ");
                        intTOfloat(variavel);
                        variavel.setLexema(temp);
                        
                        String temp2 = newVarTemporaria();
                        System.out.println(temp2+" = "+variavel.getLexema()+operador.getLexema()+operando2.getLexema());
                        variavel.setLexema(temp2);
                                               
                        return variavel;
                        
                    }else if(operando2.getTipo().equals(Gramatica.CHAR) ||
                        operando2.getTipo().equals(Gramatica.TIPOCHAR)){

                        analizadorSemanticoException.TipoInvalidoCharException(operando2.getToken());
                    }else{
                    	String temp = newVarTemporaria();
                    	System.out.println(temp+" = "+operando1.getLexema()+operador.getLexema()+operando2.getLexema());
                    	operando2.setLexema(temp);
                    	return operando2;
                    }
                }
                else if(operando1.getTipo().equals(Gramatica.FLOAT) ||
                        operando1.getTipo().equals(Gramatica.TIPOFLOAT)){

                    if(operando2.getTipo().equals(Gramatica.TIPOINT) || 
                            operando2.getTipo().equals(Gramatica.INT)){
                        
                        variavel = new Variavel(operando2.getLexema(), operando2.getTipo(), operando2.getBlocoDeDeclaracao(), operando2.getToken());
                        String temp = newVarTemporaria();
                        System.out.print(temp+" = ");
                        intTOfloat(variavel);
                        variavel.setLexema(temp);
                        
                        String temp2 = newVarTemporaria();
                        System.out.println(temp2+" = "+variavel.getLexema()+operador.getLexema()+operando1.getLexema());
                        variavel.setLexema(temp2);
                        
                        return variavel;
                        
                    }else if(operando2.getTipo().equals(Gramatica.CHAR) ||
                        operando2.getTipo().equals(Gramatica.TIPOCHAR)){

                        analizadorSemanticoException.TipoInvalidoCharException(operando2.getToken());
                    }else{
                    	String temp = newVarTemporaria();
                    	System.out.println(temp+" = "+operando1.getLexema()+operador.getLexema()+operando2.getLexema());
                    	operando2.setLexema(temp);
                    	return operando2;
                    }
                }
                else if(operando1.getTipo().equals(Gramatica.CHAR) ||
                        operando1.getTipo().equals(Gramatica.TIPOCHAR)){

                    if(!operando2.getTipo().equals(Gramatica.CHAR) &&
                            !operando2.getTipo().equals(Gramatica.TIPOCHAR)){

                        analizadorSemanticoException.TipoInvalidoCharException(operando2.getToken());
                    }
                }
                String temp = newVarTemporaria();
                System.out.println(temp+" = "+operando1.getLexema()+operador.getLexema()+operando2.getLexema());
                operando2.setLexema(temp);
                return operando2;
            }
            
            return operando1;
    		
    	}else if(lookAHead.getGramatica() == Gramatica.DIVISAO){    		
            proximoToken();
            Variavel operando1 = termo();
            Token operador = lookAHead; // operação aritimetica (+,-,*,/)
            Variavel operando2 = termo_();
            
            if(operando2 != null){
            
                 if(operando1.getTipo().equals(Gramatica.INT) || 
                    operando1.getTipo().equals(Gramatica.TIPOINT)){               
                
                    if(operando2.getTipo().equals(Gramatica.TIPOFLOAT) || 
                            operando2.getTipo().equals(Gramatica.FLOAT)){
                        
                        variavel = new Variavel(operando1.getLexema(), operando1.getTipo(), operando1.getBlocoDeDeclaracao(), operando2.getToken());
                        String temp = newVarTemporaria();
                        System.out.print(temp+" = ");
                        intTOfloat(variavel);
                        variavel.setLexema(temp);
                        
                        String temp2 = newVarTemporaria();
                        System.out.println(temp2+" = "+variavel.getLexema()+operador.getLexema()+operando2.getLexema());
                        variavel.setLexema(temp2);
                        
                        return variavel;
                        
                    }else if(operando2.getTipo().equals(Gramatica.CHAR) ||
                        operando2.getTipo().equals(Gramatica.TIPOCHAR)){

                        analizadorSemanticoException.TipoInvalidoCharException(operando2.getToken());
                    }else{
                    	String temp = newVarTemporaria();
                    	System.out.println(temp+" = "+operando1.getLexema()+operador.getLexema()+operando2.getLexema());
                    	operando2.setLexema(temp);
                    	return operando2;
                    }
                }
                else if(operando1.getTipo().equals(Gramatica.FLOAT) ||
                        operando1.getTipo().equals(Gramatica.TIPOFLOAT)){

                    if(operando2.getTipo().equals(Gramatica.TIPOINT) || 
                            operando2.getTipo().equals(Gramatica.INT)){
                        
                        variavel = new Variavel(operando2.getLexema(), operando2.getTipo(), operando2.getBlocoDeDeclaracao(), operando2.getToken());
                        String temp = newVarTemporaria();
                        System.out.print(temp+" = ");
                        intTOfloat(variavel);
                        variavel.setLexema(temp);
                        
                        String temp2 = newVarTemporaria();
                        System.out.println(temp2+" = "+variavel.getLexema()+operador.getLexema()+operando1.getLexema());
                        variavel.setLexema(temp2);
                        
                        return variavel;
                        
                    }else if(operando2.getTipo().equals(Gramatica.CHAR) ||
                        operando2.getTipo().equals(Gramatica.TIPOCHAR)){

                        analizadorSemanticoException.TipoInvalidoCharException(operando2.getToken());
                    }else{
                    	String temp = newVarTemporaria();
                    	System.out.println(temp+" = "+operando1.getLexema()+operador.getLexema()+operando2.getLexema());
                    	operando2.setLexema(temp);
                    	return operando2;
                    }
                }
                else if(operando1.getTipo().equals(Gramatica.CHAR) ||
                        operando1.getTipo().equals(Gramatica.TIPOCHAR)){

                    if(!operando2.getTipo().equals(Gramatica.CHAR) &&
                            !operando2.getTipo().equals(Gramatica.TIPOCHAR)){

                        analizadorSemanticoException.TipoInvalidoCharException(operando2.getToken());
                    }
                }
                 String temp = newVarTemporaria();
                 System.out.println(temp+" = "+operando1.getLexema()+operador.getLexema()+operando2.getLexema());
                 operando2.setLexema(temp);
                return operando2;
            }
            return operando1;
    	}
        return null;
    }
    
    public Token operadorRelacional() throws Exception{
    	
    	if(lookAHead.getGramatica() == Gramatica.MAIOR){
            Token opRelacional = lookAHead;
            proximoToken();  
            return opRelacional;
    		
    	}else if(lookAHead.getGramatica() == Gramatica.MAIORIGUAL){
            Token opRelacional = lookAHead;
            proximoToken();
            return opRelacional;
    		
    	}else if(lookAHead.getGramatica() == Gramatica.MENOR){
            Token opRelacional = lookAHead;
            proximoToken();
            return opRelacional;
    		
    	}else if(lookAHead.getGramatica() == Gramatica.MENORIGUAL){
            Token opRelacional = lookAHead;
            proximoToken();
            return opRelacional;
    		
    	}else if(lookAHead.getGramatica() == Gramatica.IGUAL){
            Token opRelacional = lookAHead;
            proximoToken();
            return opRelacional;
    		
    	}else if(lookAHead.getGramatica() == Gramatica.DIFERENCA){
            Token opRelacional = lookAHead;
            proximoToken();
            return opRelacional;
    		
    	}else{
            exception.OperadorRelacionalException(lookAHead);
    	}
        return null;
    }
    
    public Variavel fator() throws Exception{
    	//<fator> ::= “(“ <expr_arit> “)” | <id> | <real> | <inteiro> | <char>
    	
    	if(lookAHead.getGramatica() == Gramatica.ABREPARENTESES){
            proximoToken();

            Variavel variavel = expressaoAritmetica();

            if(lookAHead.getGramatica() != Gramatica.FECHAPARENTESES){
                    exception.FechaParentesesException(lookAHead);
            }
            proximoToken();
            return variavel;
    		
    	}else if(lookAHead.getGramatica() == Gramatica.ID){
            if(!tokenTable.contains(lookAHead)){
                    analizadorSemanticoException.VariavelNaoDeclaradaException(lookAHead);
            }
            Variavel v = tokenTable.get(lookAHead,bloco);
            Variavel variavel = new Variavel(lookAHead.getLexema(), v.getTipo(), v.getBlocoDeDeclaracao(), lookAHead);
            proximoToken();
            return variavel;
    	
    	}else if(lookAHead.getGramatica() == Gramatica.TIPOFLOAT){
            Variavel variavel = new Variavel(lookAHead.getLexema(), Gramatica.TIPOFLOAT, this.bloco, lookAHead);
            proximoToken();
            return variavel;
    		
    	}else if(lookAHead.getGramatica() == Gramatica.TIPOINT){
            Variavel variavel = new Variavel(lookAHead.getLexema(), Gramatica.TIPOINT, this.bloco, lookAHead);
            proximoToken();
            return variavel;
    	
    	}else if(lookAHead.getGramatica() == Gramatica.TIPOCHAR){
            Variavel variavel = new Variavel(lookAHead.getLexema(), Gramatica.TIPOCHAR, this.bloco, lookAHead);
            proximoToken();
            return variavel;
    	
    	}else{
    		exception.FatorException(lookAHead);
    	}
        return null;
    }
    
    public String newLabel(){
    	String label = "L" + this.nLabel;
    	this.nLabel+=1;
    	return label;
    }
    
    public String newVarTemporaria(){
    	String t = "T"+tLabel;
    	this.tLabel+=1;
    	return t;
    }
    
    public void intTOfloat(Variavel variavel){    	
    	System.out.println("(Float) "+variavel.getLexema());      	
    	variavel.setTipo(Gramatica.TIPOFLOAT);    	
    }
    
}