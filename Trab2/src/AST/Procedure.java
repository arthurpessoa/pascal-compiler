package AST;

import java.io.*;

public class Procedure extends Subdcls {
    
    public Procedure( String name ) {
        this.name = name;
    }
    
    public void genC( PW pw ) {
        pw.out.print("void " + name + "(");
        if ( dcls != null ) 
          dcls.genC(pw,true);
        pw.out.println(") {");
        pw.add();
        if ( localVarList != null ) 
          localVarList.genC(pw);
        body.genC(pw);
        pw.sub();
        pw.out.println("}");
    }
        
    
}