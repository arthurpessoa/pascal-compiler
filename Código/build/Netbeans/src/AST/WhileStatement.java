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

public class WhileStatement extends Statement {
    
    public WhileStatement( Expr whilePart, StatementList doPart ) {
        this.whilePart = whilePart;
        this.doPart = doPart;
    }
 
    public void genC( PW pw ) {
        
        pw.print("while ( ");
        whilePart.genC(pw, false);
        pw.out.println(" ) { ");
        if ( doPart != null ) {
          pw.add();
          doPart.genC(pw);
          pw.sub();
          pw.println("}");
        }
        
    }
    
    private StatementList doPart;
    private Expr whilePart;
}