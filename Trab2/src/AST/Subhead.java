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
//subhead ::= FUNCTION pid args ':' stdtype | PROCEDURE pid args
public class Subhead {
    protected String pid;
    private Dcls dcls;
    
    public Subhead(Dcls dcls, String pid){
        this.dcls = dcls;
        this.pid= pid;
    }
    public void genC( PW pw ) {
        dcls.genC(pw);
        
    }
}
