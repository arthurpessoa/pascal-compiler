package AST;

public class CharExpr extends Expr {
    
    public CharExpr( String value ) {
        this.value = value; 
    }
    
    public void genC( PW pw, boolean putParenthesis  ) {
        pw.out.print(value);
    }
    
    public String getValue() {
        return value;
    }
    
    public Type getType() {
        return Type.charType;
    }
    
    private String value;
}