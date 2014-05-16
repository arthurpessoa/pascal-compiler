package AST;
public class WriteStatement extends Statement {
    
    public WriteStatement( ExprList expr ) {
        this.expr = expr;
    }
 
    public void genC( PW pw ) {
       
        int size = expr.getSize();
        int i;
        
        Expr e = null;
        pw.print("printf(\"");
        
        int j = pw.currentIndent;
        pw.set(0);
        for(i=0;i<size;i++){
            e = expr.getElement(i);
            if ( e.getType() == Type.charType ) {
                pw.print("%c");
            }else{
                if(e.getType() == Type.stringType){
                    pw.print("%s");
                }else{
                    if(e.getType() == Type.integerType)
                        pw.print("%d");
                    else
                        if(e.getType() == Type.realType)
                            pw.print("%f");
                        else
                            if(e.getType() == Type.sentenceType)
                                e.genC(pw, true);
                }   
            }
            if(i<size-1)
                pw.print(",");
        }
        pw.print("\"");
        for(i=0;i<size;i++){
            e = expr.getElement(i);
            if(e.getType()!=Type.sentenceType)
                e.genC(pw, false);
            if(i+1<size)
                pw.print(",");
        }
        pw.out.println(");");
        pw.set(j);
    }
    
    
    private ExprList expr;
}