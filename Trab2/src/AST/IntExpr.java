package AST;

public class IntExpr extends Expr {
    
    public IntExpr( String value ) { 
        this.value = value;
    }
    
    public String getValue() {
        return value;
    }
    public void genC( PW pw, boolean putParenthesis ) {
        pw.out.print(value);
    }
    
    public Type getType() {
        return Type.integerType;
    }
    
    private String value;
}
