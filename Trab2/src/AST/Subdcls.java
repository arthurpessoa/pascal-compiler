package AST;

import java.io.*;

abstract public class Subdcls {
    
    abstract public void genC( PW pw );

    public String getName() {
        return name;
    }
    
    public void setParamList( ParamList paramList ) {
        this.paramList = paramList;
    }
    
    public ParamList getParamList() {
        return paramList;
    }
    
    public void setLocalVarList( LocalVarList localVarList ) {
        this.localVarList = localVarList;
    }
    
    public void setBody( Body body ) {
        this.body = body;
    }
    
      // fields should be accessible in subclasses
    protected String name;
    protected LocalVarList localVarList;
    protected Body body;
    protected ParamList paramList;
    protected Subhead subhead;
    
}