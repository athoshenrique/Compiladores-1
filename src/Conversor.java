/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author henrique.dreyer
 */
public class Conversor {
    
    
    public void intToFloat(Variavel variavel){
        System.out.println("(Float) " + variavel.getLexema());
        
        if(variavel.getTipo().equals(Gramatica.ID)){            
            variavel.setTipo(Gramatica.FLOAT);
        }else{            
            variavel.setTipo(Gramatica.TIPOFLOAT);
            variavel.setLexema(variavel.getLexema()+".0");
        }        
    }
    
}
