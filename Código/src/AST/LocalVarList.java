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

public class LocalVarList {
    
    public LocalVarList() {
        v = new ArrayList<Variable>();
    }
    
    public void addElement( Variable variable ) {
        v.add(variable);
    }
    
    public void genC( PW pw ) {

        for( Variable variable : v )
             pw.println( variable.getType().getCname() + " " +
                variable.getName() + ";" );
    }
    
    ArrayList<Variable> v;
}
    