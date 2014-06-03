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

public class ProcedureCall extends Statement {
    
    public ProcedureCall( Procedure procedure, ExprList exprList ) {
        this.procedure = procedure;
        this.exprList = exprList;
    }
    
    public void genC( PW pw ) {
        pw.print( procedure.getName() + "(" );
        if ( exprList != null ) 
          exprList.genC(pw);
        pw.out.println(");");
    }
    
    Procedure procedure;
    ExprList exprList;
    
}