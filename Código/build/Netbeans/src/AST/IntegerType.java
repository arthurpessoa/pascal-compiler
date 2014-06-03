/*
    Universidade Federal de São Carlos - Campus Sorocaba
    Compiladores: Trabalho 2
    Professora: Tiemi Christine Sakata

    Alunos:
        Arthur Pessoa de Souza
        João Eduardo Brandes Luiz
*/

package AST;

public class IntegerType extends Type {
    
    public IntegerType() {
        super("integer");
    }
    
   public String getCname() {
      return "int";
   }
   
}