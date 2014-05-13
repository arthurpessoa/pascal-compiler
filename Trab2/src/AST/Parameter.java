package AST;

public class Parameter extends Variable {
    public Parameter( String name, Type type, int size ) {
        super(name, type, size);
    }
    
    public Parameter( String name ) {
        super(name);
    }

}