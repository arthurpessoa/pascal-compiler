/*
    Universidade Federal de São Carlos - Campus Sorocaba
    Compiladores: Trabalho 2
    Professora: Tiemi Christine Sakata

    Alunos:
        Arthur Pessoa de Souza
        João Eduardo Brandes Luiz
*/

package AST;

public class Variable {
    
    public Variable( String name, Type type, int size ) {
        this.name = name;
        this.type = type;
        this.size = size;
    }
    
    public Variable( String name ) {
        this.name = name;
    }
    
    public void setType( Type type ) {
        this.type = type;
    }
    
    public String getName() { return name; }
    
    public Type getType() {
        return type;
    }
    
    public int getSize(){ return size; }
    
    public void setSize(int size){
        this.size = size;
    }

    
    private String name;
    private Type type;
    private int size;
}