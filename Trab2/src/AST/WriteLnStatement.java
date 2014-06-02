package AST;

import java.util.Iterator;

public class WriteLnStatement extends Statement {
    
    public WriteLnStatement( ExprList expr ) {
        this.expr = expr;
    }
 
    public void genC( PW pw ) {
       
        int size = expr.getSize();
        int i;
        
        Expr e = null;
        pw.print("printf(\"");
        
        for(i=0;i<size;i++){
            e = expr.getElement(i);
            if ( e.getType() == Type.sentenceType ) {
                e.genC(pw, false);
            }else{
                if(e.getType() == Type.stringType){
                    pw.out.print("%s");
                }else{
                    if(e.getType() == Type.integerType)
                        pw.out.print("%d");
                    else
                        if(e.getType() == Type.realType)
                            pw.out.print("%f");
                        else
                            if(e.getType() == Type.charType)
                                pw.print("%c");
                }   
            }
            if(i<size-1)
                pw.print(",");
        }
        pw.print("\\n\"");
        for(i=0;i<size;i++){
            e = expr.getElement(i);
            if(e.getType()!=Type.sentenceType){
                pw.print(",");
                e.genC(pw, false);       
            }
        }
        pw.out.println(");");
    }
    
    
    private ExprList expr;
}