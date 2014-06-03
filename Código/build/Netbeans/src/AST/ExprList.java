/*
    Universidade Federal de São Carlos - Campus Sorocaba
    Compiladores: Trabalho 2
    Professora: Tiemi Christine Sakata

    Alunos:
        Arthur Pessoa de Souza
        João Eduardo Brandes Luiz
*/

package AST;

import java.io.*;
import java.util.*;

public class ExprList {
    
    public ExprList() {
        v = new ArrayList<Expr>();
    }
    
    public void addElement( Expr expr ) {
        v.add(expr);
    }
    
    public Expr getElement(int pos){
        if(v.size() < pos){
            System.out.println("pos > size");
        }
        Expr e = v.get(pos);
        if(e == null){
            System.out.println("RETORNANDO NULL");
        }
        return e;
        
    }
    
   
    
    public int getSize(){
        return v.size();
    }
    
    public void genC( PW pw ) {
        
        int size = v.size();
        Iterator e = v.iterator();
        while ( e.hasNext() ) {
            
            ((Expr ) e.next()).genC(pw,true);
            if ( --size > 0 ) 
              pw.out.print(", ");
        }



    }
    
    private ArrayList<Expr> v;
    
}
            