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
