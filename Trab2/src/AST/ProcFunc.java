/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package AST;

import java.util.Iterator;

/**
 *
 * @author JoaoEduardo
 */
public class ProcFunc extends Subdcls{
    private String pid;
    private ExprList exprList;
    
    public ProcFunc(String pid, ExprList exprList){
        this.pid = pid;
        this.exprList = exprList;
    }

    @Override
    public void genC(PW pw) {
        pw.print(pid+"( ");
        exprList.genC(pw);
        pw.print(" );");
    }
}
