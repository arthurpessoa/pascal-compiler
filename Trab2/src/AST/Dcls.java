/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package AST;

import java.util.ArrayList;

/**
 *
 * @author JoaoEduardo
 */
public class Dcls {
    
    public Dcls(ArrayList<Variable> arrayVariable){
        this.arrayVariable = arrayVariable;
    }
    
    public int getSize(){
        return arrayVariable.size();
    }
    public ArrayList<Variable> getParamList(){
        return arrayVariable;
    }
    
    void genC(PW pw){
        for( Variable v : arrayVariable ){
            if(v.getSize()==0){
                pw.println(v.getType().getCname()+ " " + v.getName()+";");
                
            }else{
                pw.println(v.getType().getCname() +" "+  v.getName()+ "["+ v.getSize()+"];");
            }
        }
    }
    
    private ArrayList<Variable> arrayVariable;
}
