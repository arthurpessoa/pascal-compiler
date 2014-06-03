/*
    Universidade Federal de São Carlos - Campus Sorocaba
    Compiladores: Trabalho 2
    Professora: Tiemi Christine Sakata

    Alunos:
        Arthur Pessoa de Souza
        João Eduardo Brandes Luiz
*/

package AST;

abstract public class Type {
    
    public Type( String name ) {
        this.name = name;
    }
    
    public static Type stringType  = new StringType();
    public static Type integerType = new IntegerType();
    public static Type charType    = new CharType();
    public static Type realType    = new RealType();
    public static Type sentenceType = new SentenceType();
    public static Type booleanType = new BooleanType();
    
    public String getName() {
        return name;
    }
    
    abstract public String getCname();
    
    private String name;
}
