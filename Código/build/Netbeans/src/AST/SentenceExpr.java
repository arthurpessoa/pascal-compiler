/*
    Universidade Federal de São Carlos - Campus Sorocaba
    Compiladores: Trabalho 2
    Professora: Tiemi Christine Sakata

    Alunos:
        Arthur Pessoa de Souza
        João Eduardo Brandes Luiz
*/

package AST;

public class SentenceExpr extends Expr {
    
    public SentenceExpr( String value ) {
        this.value = value; 
    }
    
    public void genC( PW pw, boolean putParenthesis  ) {
        if(putParenthesis)
            pw.out.print("\""+value+"\"");
        else
            pw.out.print(value);
    }
    
    public String getValue() {
        return value;
    }
    
    public Type getType() {
        return Type.sentenceType;
    }
    
    private String value;
}