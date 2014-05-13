package AuxComp;

import Lexer.*;
import java.io.*;

public class CompilerError {
    
    public CompilerError( Lexer lexer, PrintWriter out ) {
          // output of an error is done in out
        this.lexer = lexer;
        this.out = out;
        thereWasAnError = false;
    }
    
    public void setLexer( Lexer lexer ) {
        this.lexer = lexer;
    }
    
    public boolean wasAnErrorSignalled() {
        return thereWasAnError;
    }

    public void show( String strMessage ) {
        show( strMessage, false );
    }
    
    public void show( String strMessage, boolean goPreviousToken ) {
        // is goPreviousToken is true, the error is signalled at the line of the
        // previous token, not the last one.
        if ( goPreviousToken ) {
          out.println("Error at line " + lexer.getLineNumberBeforeLastToken() + ": ");
          out.println( lexer.getLineBeforeLastToken() );
        }
        else {
          out.println("Error at line " + lexer.getLineNumber() + ": ");
          out.println(lexer.getCurrentLine());
        }
        
        out.println( strMessage );
        out.flush();
        if ( out.checkError() )
          System.out.println("Error in signaling an error");
        thereWasAnError = true;
    }
       




    public void signal( String strMessage ) {
        show( strMessage );
        out.flush();
        thereWasAnError = true;
        throw new RuntimeException();
    }
    
    private Lexer lexer;
    private PrintWriter out;
    private boolean thereWasAnError;
}
    
