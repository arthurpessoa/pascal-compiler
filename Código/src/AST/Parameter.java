/*
    Universidade Federal de São Carlos - Campus Sorocaba
    Compiladores: Trabalho 2
    Professora: Tiemi Christine Sakata

    Alunos:
        Arthur Pessoa de Souza
        João Eduardo Brandes Luiz
*/

package AST;

public class Parameter extends Variable {
    public Parameter( String name, Type type, int size ) {
        super(name, type, size);
    }
    
    public Parameter( String name ) {
        super(name);
    }

}