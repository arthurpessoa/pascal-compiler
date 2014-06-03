/*
    Universidade Federal de São Carlos - Campus Sorocaba
    Compiladores: Trabalho 2
    Professora: Tiemi Christine Sakata

    Alunos:
        Arthur Pessoa de Souza
        João Eduardo Brandes Luiz
*/

package AST;

import Lexer.*;

public class CompositeExpr extends Expr {
    
    public CompositeExpr( Expr pleft, Symbol poper, Expr pright ) {
        left = pleft;
        oper = poper;
        right = pright;
    }
    public void genC( PW pw, boolean putParenthesis  ) {
        pw.out.print("(");
        left.genC(pw, true);
        pw.out.print(" " + oper.toString() + " ");
        right.genC(pw, true);
        pw.out.print(")");
    }
    
    public Type getType() {
          // left and right must be the same type
            
            return left.getType();
    }
    
    private Expr left, right;
    private Symbol oper;
}
