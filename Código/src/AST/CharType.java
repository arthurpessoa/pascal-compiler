/*
    Universidade Federal de São Carlos - Campus Sorocaba
    Compiladores: Trabalho 2
    Professora: Tiemi Christine Sakata

    Alunos:
        Arthur Pessoa de Souza
        João Eduardo Brandes Luiz
*/

package AST;

public class CharType extends Type {
    
    public CharType() {
        super("char");
    }
    
   public String getCname() {
      return "char";
   }
   
}