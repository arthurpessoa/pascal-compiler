package AST;

public class WriteStatement extends Statement {
    
    public WriteStatement( Expr expr ) {
        this.expr = expr;
    }
 
    public void genC( PW pw ) {
        
       
        if ( expr.getType() == Type.charType ) {
          pw.print("printf(\"%c\\n\", ");
        }else{
         if(expr.getType() == Type.stringType){
             pw.print("printf(\"%s\\n\", ");
         }else{
             if(expr.getType() == Type.integerType)
                pw.print("printf(\"%d\\n\", ");
             else
                 pw.print("printf(\"%f\\n\", ");
         }   
        }
        expr.genC(pw, false);
        
        pw.out.println(" );");
    }
    
    
    private Expr expr;
}