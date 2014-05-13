package AST;

abstract public class Type {
    
    public Type( String name ) {
        this.name = name;
    }
    
    public static Type stringType = new StringType();
    public static Type integerType = new IntegerType();
    public static Type charType    = new CharType();
    public static Type realType    = new RealType();
    
    public String getName() {
        return name;
    }
    
    abstract public String getCname();
    
    private String name;
}
