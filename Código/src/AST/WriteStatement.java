/*
    Universidade Federal de São Carlos - Campus Sorocaba
    Compiladores: Trabalho 2
    Professora: Tiemi Christine Sakata

    Alunos:
        Arthur Pessoa de Souza
        João Eduardo Brandes Luiz
*/

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
        
        for(i=0;i<size;i++){
            e = expr.getElement(i);
            if ( e.getType() == Type.charType ) {
                pw.out.print("%c");
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
                            if(e.getType() == Type.sentenceType)
                                e.genC(pw, false);
                }   
            }
            
        }
        pw.out.print("\"");
        for(i=0;i<size;i++){
            e = expr.getElement(i);
            if(e.getType()!=Type.sentenceType){
                pw.out.print(",");
                e.genC(pw, false);       
            }
        }
        pw.out.println(");");
    }
    
    
    private ExprList expr;
}