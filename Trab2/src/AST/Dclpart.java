/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package AST;

/**
 *
 * @author JoaoEduardo
 */
public class Dclpart {
    
    public Dclpart(Dcls dcls, Subdcls subdcls){
        this.dcls = dcls;
        this.subdcls = subdcls;
    }
    
    public void genC( PW pw ) {
        dcls.genC(pw);
        //subdcls.genC(pw);
    }
    
    private Dcls dcls;
    private Subdcls subdcls;
    
}
