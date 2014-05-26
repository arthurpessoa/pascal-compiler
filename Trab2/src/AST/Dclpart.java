/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package AST;

import java.util.ArrayList;
import java.util.Iterator;

/**
 *
 * @author JoaoEduardo
 */
public class Dclpart {
    
    public Dclpart(Dcls dcls, ArrayList<Subdcls> subdcls){
        this.dcls = dcls;
        this.subdcls = subdcls;
    }
    
    public void genC( PW pw ) {
       
        if(dcls != null)
         dcls.genC(pw);
        
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
