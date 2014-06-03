/*
    Universidade Federal de São Carlos - Campus Sorocaba
    Compiladores: Trabalho 2
    Professora: Tiemi Christine Sakata

    Alunos:
        Arthur Pessoa de Souza
        João Eduardo Brandes Luiz
*/

package AST;

import java.util.*;
import java.io.*;

public class CompositeStatement extends Statement {
    
    public CompositeStatement( StatementList statementList ) {
        this.statementList = statementList;
    }
    
    public void genC( PW pw ) {
       
        if ( statementList != null ) {
           
           statementList.genC(pw);
          
        }
       
    }
    
    public StatementList getStatementList() { return statementList; }
    
    private StatementList statementList;
}
