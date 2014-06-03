/*
    Universidade Federal de São Carlos - Campus Sorocaba
    Compiladores: Trabalho 2
    Professora: Tiemi Christine Sakata

    Alunos:
        Arthur Pessoa de Souza
        João Eduardo Brandes Luiz
*/

package AST;

public class AssignmentStatement extends Statement {
    
    public AssignmentStatement( Variable v, Expr expr ) {
        this.v = v;
        this.expr = expr;
    }
 
    public void genC( PW pw ) {
        pw.print(v.getName() + " = " );
        expr.genC(pw,false);
        pw.out.println(";");
    }
    private Variable v;
    private Expr expr;
}