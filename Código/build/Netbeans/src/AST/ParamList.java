/*
    Universidade Federal de São Carlos - Campus Sorocaba
    Compiladores: Trabalho 2
    Professora: Tiemi Christine Sakata

    Alunos:
        Arthur Pessoa de Souza
        João Eduardo Brandes Luiz
*/

package AST;

import java.io.*;
import java.util.*;

public class ParamList {
    
    public ParamList() {
        v = new ArrayList<Parameter>();
    }
    
    public void addElement( Parameter parameter) {
        v.add(parameter);
    }
    
    public int getSize() {
        return v.size();
    }
    
/*    public Enumeration elements() {
        return v.elements();
    }
 *
 */
    public ArrayList<Parameter> getParamList() {
        return v;
    }
    
    public void genC( PW pw ) {
        Parameter p;
        Iterator e = v.iterator();
        int size = v.size();
        while ( e.hasNext() ) {
          p = (Parameter ) e.next();
          pw.out.print( p.getType().getCname() + " " + p.getName() );
          
          if ( --size > 0 )
            pw.out.print(", ");
        }
    }
    
    ArrayList<Parameter> v;
}
