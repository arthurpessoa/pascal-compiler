package AST;

import java.util.*;

public class Program {
    
    public Program( String programName,Body body ) {
        this.programName = programName;
        this.body = body;
    }
    
    public void genC( PW pw ) {
        
        pw.out.println("#include <stdio.h>");
        pw.out.println("#include <stdlib.h>");
        pw.out.println();
        body.gen(pw);
        pw.println("int main(){");
        pw.add();         
        body.genC(pw);
        pw.println("return 0;");
        pw.sub();
        pw.out.println("}");
    }                             
        
    private String programName;
    private Body body;
    
}