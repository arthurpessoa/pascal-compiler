/*
    Universidade Federal de São Carlos - Campus Sorocaba
    Compiladores: Trabalho 2
    Professora: Tiemi Christine Sakata

    Alunos:
        Arthur Pessoa de Souza
        João Eduardo Brandes Luiz
*/
package AST;

import java.util.ArrayList;
import java.util.Iterator;

public class Dclpart {
    
    public Dclpart(Dcls dcls, ArrayList<Subdcls> subdcls){
        this.dcls = dcls;
        this.subdcls = subdcls;
    }
    
    public void genC( PW pw ) {
       
        if(dcls != null)
         dcls.genC(pw,false);
        
    }
    
    public void gen(PW pw){
        Subdcls s = null;
        Iterator e = subdcls.iterator();
        while(e.hasNext()){
            s=(Subdcls)e.next();
            s.genC(pw);
        }
    }
    
    private Dcls dcls;
    private ArrayList<Subdcls> subdcls;
    
}
