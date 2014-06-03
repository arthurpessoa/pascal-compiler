/*
    Universidade Federal de São Carlos - Campus Sorocaba
    Compiladores: Trabalho 2
    Professora: Tiemi Christine Sakata

    Alunos:
        Arthur Pessoa de Souza
        João Eduardo Brandes Luiz
*/

package AST;

public class BooleanExpr extends Expr {
    
    public BooleanExpr( boolean value ) {
        this.value = value;
    }
    
    public void genC( PW pw, boolean putParenthesis ) {
       pw.out.print( value ? "1" : "0" );
    }
    
    public Type getType() {
        return Type.booleanType;
    }
    
    public static BooleanExpr True  = new BooleanExpr(true);
    public static BooleanExpr False = new BooleanExpr(false);
    
    private boolean value;
}
