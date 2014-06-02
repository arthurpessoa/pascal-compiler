package AST;

import java.util.ArrayList;
import java.util.Iterator;

public class ReadStatement extends Statement {
    public ReadStatement( ArrayList<Variable> v ) {
        this.v = v;
    }
 
    public void genC( PW pw ) {
        Variable p;
        Iterator e = v.iterator();
        pw.print("scanf(\"");
        while(e.hasNext()){
            p = (Variable) e.next();
            
            if ( p.getType() == Type.charType ){ 
                pw.out.print("%c");
            }else{
                if(p.getType() == Type.integerType){
                    pw.out.print("%d");
                }else{
                    if(p.getType() == Type.realType){
                        pw.out.print("%f");
                    }else{
                        pw.out.print("%s");
                    }
                }
            } 
            pw.out.print(" ");
        }
        pw.out.print("\",");
        e = v.iterator();
        while(e.hasNext()){
            p = (Variable)e.next();
            pw.out.print("&"+p.getName());
            if(e.hasNext())
                pw.out.print(",");
        }
        pw.println(");");
    }
    private ArrayList<Variable> v;
}