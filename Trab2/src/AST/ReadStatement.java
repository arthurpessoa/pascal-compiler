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
        int size = v.size();
        PW writer =pw;
        writer.set(0);
        writer.print("scanf(\"");
        while(e.hasNext()){
            p = (Variable) e.next();
            if ( p.getType() == Type.charType ){ 
                pw.print("%c ");
            }else{
                if(p.getType() == Type.integerType){
                    pw.print("%d ");
                }else{
                    if(p.getType() == Type.realType){
                        pw.print("%f ");
                    }else{
                        pw.print("%s ");
                    }
                }
            } 
        }
        pw.print("\",");
        e = v.iterator();
        while(e.hasNext()){
            p = (Variable)e.next();
            pw.print("&"+p.getName());
            if(e.hasNext())
                pw.print(",");
        }
        pw.println(");");
    }
    private ArrayList<Variable> v;
}