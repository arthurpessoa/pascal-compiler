package AST;

public class CharType extends Type {
    
    public CharType() {
        super("char");
    }
    
   public String getCname() {
      return "char";
   }
   
}