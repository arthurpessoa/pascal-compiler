package AST;

public class BooleanType extends Type {
    
   public BooleanType() { super("boolean"); }
   
   public String getCname() {
      return "boolean";
   }
}
