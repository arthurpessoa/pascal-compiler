package AST;

import java.io.*;

public class Procedure extends Subroutine {
    
    public Procedure( String name ) {
        this.name = name;
    }
    
    public void genC( PW pw ) {
        pw.out.print("void " + name + "(");
        if ( paramList != null ) 
          paramList.genC(pw);
        pw.out.println(") {");
        pw.add();
        if ( localVarList != null ) 
          localVarList.genC(pw);
        compositeStatement.getStatementList().genC(pw);
        pw.sub();
        pw.out.println("}");
    }
        
    
}