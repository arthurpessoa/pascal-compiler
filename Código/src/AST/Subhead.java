/*
    Universidade Federal de São Carlos - Campus Sorocaba
    Compiladores: Trabalho 2
    Professora: Tiemi Christine Sakata

    Alunos:
        Arthur Pessoa de Souza
        João Eduardo Brandes Luiz
*/

package AST;

//subhead ::= FUNCTION pid args ':' stdtype | PROCEDURE pid args
public class Subhead {
    protected String pid;
    private Dcls dcls;
    
    public Subhead(Dcls dcls, String pid){
        this.dcls = dcls;
        this.pid= pid;
    }
    public void genC( PW pw ) {
        dcls.genC(pw,false);
        
    }
}
