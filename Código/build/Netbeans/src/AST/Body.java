/*
    Universidade Federal de São Carlos - Campus Sorocaba
    Compiladores: Trabalho 2
    Professora: Tiemi Christine Sakata

    Alunos:
        Arthur Pessoa de Souza
        João Eduardo Brandes Luiz
*/

package AST;

public class Body {
    
    public Body (Dclpart dclpart, CompositeStatement compstmt){
        this.dclpart = dclpart;
        this.compstmt = compstmt;
    }
    
    public void gen(PW pw){
        dclpart.gen(pw);
    }
    
    public void genC( PW pw ) {
        if(dclpart != null)
            dclpart.genC(pw);
        if(compstmt != null)
            compstmt.genC(pw);
    }
    
    private Dclpart dclpart;
    private CompositeStatement compstmt;
      
}
