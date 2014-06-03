/*
    Universidade Federal de São Carlos - Campus Sorocaba
    Compiladores: Trabalho 2
    Professora: Tiemi Christine Sakata

    Alunos:
        Arthur Pessoa de Souza
        João Eduardo Brandes Luiz
*/

package AST;

import Lexer.Symbol;


public class UnaryExpr extends Expr {
    
    public UnaryExpr( Expr expr, Symbol op ) {
        this.expr = expr;
        this.op = op;
    }
    
    public void genC( PW pw, boolean putParenthesis ) {
        switch ( op ) {
            case PLUS : 
              pw.out.print("+");
              break;
            case MINUS :
              pw.out.print("-");
              break;
            case NOT :
              pw.out.print("!");
              break;
        }
        expr.genC(pw, false);
    }
    
    public Type getType() {
        return expr.getType();
    }
    
    private Expr expr;
    private Symbol op;
}
              