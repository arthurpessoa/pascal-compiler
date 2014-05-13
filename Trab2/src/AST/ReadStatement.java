package AST;

public class ReadStatement extends Statement {
    public ReadStatement( Variable v ) {
        this.v = v;
    }
 
    public void genC( PW pw ) {
        if ( v.getType() == Type.charType ) 
          pw.print("{ char s[256]; gets(s); sscanf(s, \"%c\", &"  );
        else // should only be an integer
          pw.print("{ char s[256]; gets(s); sscanf(s, \"%d\", &"  );
        pw.out.println(  v.getName() + "); }" ); 
    }
    private Variable v;
}