/*
    Universidade Federal de São Carlos - Campus Sorocaba
    Compiladores: Trabalho 2
    Professora: Tiemi Christine Sakata

    Alunos:
        Arthur Pessoa de Souza
        João Eduardo Brandes Luiz
*/
package AST;

import java.util.Iterator;

public class ProcFunc extends Subdcls{
    private String pid;
    private ExprList exprList;
    
    public ProcFunc(String pid, ExprList exprList){
        this.pid = pid;
        this.exprList = exprList;
    }

    @Override
    public void genC(PW pw) {
        pw.print(pid+"( ");
        exprList.genC(pw);
        pw.print(" );");
    }
}
