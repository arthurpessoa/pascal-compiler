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

public class ReturnStatement extends Statement {
    
    public ReturnStatement( Expr expr ) {
        this.expr = expr;
    }
    
    public void genC( PW pw ) {
        pw.print("return ");
        expr.genC(pw, false);
        pw.out.println(";");
    }
    
    private Expr expr;
    
}
    
        
        