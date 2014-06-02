
package AST;

import java.util.ArrayList;
import java.util.Iterator;

public class Dcls {
    
    public Dcls(ArrayList<Variable> arrayVariable){
        this.arrayVariable = arrayVariable;
    }
    
    public int getSize(){
        return arrayVariable.size();
    }
    public ArrayList<Variable> getParamList(){
        return arrayVariable;
    }
    
    void genC(PW pw, boolean isParamList){
        Iterator i = arrayVariable.iterator();
        Variable v;
        while(i.hasNext()){
            v = (Variable)i.next();
            if(isParamList){
                if(v.getSize()==0){
                    pw.print(v.getType().getCname()+ " " + v.getName());
                }else{
                    pw.print(v.getType().getCname() +" "+  v.getName()+ "["+ v.getSize()+"]");
                }
                if(i.hasNext()){
                    pw.print(",");
                }
            }else{
            if(v.getSize()==0){
                pw.println(v.getType().getCname()+ " " + v.getName()+";");
            }else{
                pw.println(v.getType().getCname() +" "+  v.getName()+ "["+ v.getSize()+"];");
            }
             
        }
       }
    }
    
    private ArrayList<Variable> arrayVariable;
}
