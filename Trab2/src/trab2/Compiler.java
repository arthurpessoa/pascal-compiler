
//prog ::= PROGRAM pid ';' body '.'
//
//body ::= dclpart compstmt | compstmt
//
//dclpart ::= VAR dcls | subdcls | VAR dcls subdcls
//dcls ::= dcl | dcls dcl
//dcl ::= idlist ':' type ';'
//
//idlist ::= id | idlist ',' id
//
//type ::= stdtype | arraytype
//arraytype ::= ARRAY '[' num '..' num ']' OF stdtype
//
//subdcls ::= subdcl | subdcls subdcl
//subdcl ::= subhead ';' body ';'
//subhead ::= FUNCTION pid args ':' stdtype | PROCEDURE pid args
//args ::= '(' dcls ')' | '(' ')'
//
//compstmt ::= BEGIN stmts END
//stmts ::= stmt | stmts ';' stmt
//stmt ::= elstmt
//	| IF expr THEN stmt [ ELSE stmt ] ENDIF
//	| WHILE expr DO stmt ENDWHILE
//
//elstmt ::= vbl ':=' expr
//	| RETURN expr /* in a function */
//	| RETURN      /* in a procedure */
//	| procfunc 	  /* should be procedure */
//	| compstmt
//	| READ '(' vbllist ')'
//	| WRITE '(' exprlist ')'
//	| WRITELN '(' exprlist ')'
//	| WRITELN '(' ')'
//
//vbl ::= id | id '[' expr ']'
//vbllist ::= vbl | vbllist ',' vbl
//
//procfunc ::= pid '(' ')' | pid '(' exprlist ')'
//
//exprlist ::= expr | exprlist ',' expr
//expr ::= simexp | simexp relop expr
//simexp ::= term | unary term | simexp addop term
//term ::= factor | term mulop factor
//factor ::= vbl
//	| num
//	| '(' expr ')'
//	| procfunc /* should be FUNCTION */
//
//
//pid ::=  letter { letter | digit }
//id ::=  letter { letter | digit }
//num ::= ['+' | '-' ] digit ['.'] { digit }
//relop ::=  '=' | '<' | '>' | '<=' | '>=' | '<>'
//addop ::=  '+' | '-' | OR  /* + também representa concatenação de string */
//mulop ::= '*' | '/' | AND | MOD | DIV
//unary ::=  '+' | '-' | NOT
//stdtype ::= INTEGER | REAL | CHAR | STRING
//
// 

package trab2;

import AST.*;
import AuxComp.*;
import Lexer.*;
import java.io.*;
import java.util.*;
 
public class Compiler {
   
    private SymbolTable symbolTable;
    private Lexer lexer;
    private CompilerError error;
    private int size;
    
      // keeps a pointer to the current function being compiled
    private Function currentFunction;
    
     public Program compile( char []input, PrintWriter outError ) {
        
        symbolTable = new SymbolTable();
        error = new AuxComp.CompilerError( lexer, new PrintWriter(outError) );
        lexer = new Lexer(input, error);
        error.setLexer(lexer);
        lexer.nextToken();
        Program p = null;
        try {
          p = prog();
        } catch ( Exception e ) {
            e.printStackTrace();
        }
        if ( error.wasAnErrorSignalled() )
          return null;
        else
          return p;
        }
     
     //prog ::= PROGRAM pid ';' body '.'
     private Program prog() {
        Program program = null;
        if(lexer.token == Symbol.PROGRAM){
            lexer.nextToken();
            String programName = null;
            
            if(lexer.token != Symbol.NUMBER){
                programName = lexer.getStringValue();
            }else{
                error.signal("Identificador não pode iniciar com um número");
            }
            lexer.nextToken();

            if(lexer.token == Symbol.SEMICOLON){
                 program = new Program(programName, body());
            }else{
                error.signal("Simbolo ';' faltando");
            }
            if(lexer.token == Symbol.ENDPROG){
                return program;
            }else{
                error.signal("Programa deve encerrar com um '.'" + lexer.token.toString());
            }
        }
       return null;
     }
     //body ::= dclpart compstmt | compstmt
     private Body body(){
         
         Dclpart dclpart = dclpart();
         CompositeStatement compstmt = compstmt();
         Body body = new Body(dclpart, compstmt);
         return body;
     }
     //dclpart ::= VAR dcls | subdcls | VAR dcls subdcls
     private Dclpart dclpart(){
         Dcls dcls = null;
         Subdcls subdcls = null;
         lexer.nextToken();
         
         if(lexer.token == Symbol.VAR){
             lexer.nextToken();
             dcls = dcls();
             subdcls = subdcls();
         }
         Dclpart dclpart = new Dclpart(dcls,subdcls);
         return dclpart;
     }
     //dcls ::= dcl | dcls dcl
     private Dcls dcls(){
        ArrayList<Variable> varList = new ArrayList<Variable>();
        
        dcl(varList);
        while ( lexer.token == Symbol.IDENT )
          dcl(varList);
       
        Dcls dcls = new Dcls(varList);
        return dcls;
     }
     //dcl ::= idlist ':' type ';'
     private void dcl(ArrayList<Variable> varList){
         
         ArrayList<Variable> lastVarList = new ArrayList<>();
         
         while(true){
             
             if(lexer.token == Symbol.NUMBER){
                 error.show("Identificador não pode iniciar com Números ");
                 lexer.nextToken();
             }else{
                 if(lexer.token != Symbol.IDENT){
                     error.signal("Identificador esperado");
                 }else{
                     String name = (String) lexer.getStringValue();
             lexer.nextToken();
             
             if(symbolTable.getInLocal(name)!=null)
                 error.show("Variavel "+name+" já foi declarada");
             
             Variable v = new Variable(name);
             symbolTable.putInLocal(name, v);
             lastVarList.add(v);
             
             if(lexer.token == Symbol.COMMA){
                 lexer.nextToken();
             }else{
                 break;
             }
                 }
             }
             
             
         }
         
         if(lexer.token != Symbol.COLON){
             error.signal("Símbolo ':' faltando");
             lexer.skipPunctuation();
         }else{
             lexer.nextToken();
         }
         
         Type typeVar = type();
         
         for(Variable v: lastVarList){
             v.setType(typeVar);
             v.setSize(size);
             varList.add(v);
         }
         
         if(lexer.token!=Symbol.SEMICOLON)
             error.signal("Simbolo ';' faltando");
         
         lexer.nextToken();
     }
     
     
     //type ::= stdtype | arraytype
    //arraytype ::= ARRAY '[' num '..' num ']' OF stdtype
     private Type type() {
        Type result;
        size = 0;
        if(lexer.token == Symbol.ARRAY){
           typeArray();
        }
       
        switch ( lexer.token ) {
            case INTEGER :
              result = Type.integerType;
              break;
            case CHAR :
              result = Type.charType;
              break;
            case REAL:
                result = Type.realType;
                break;
            case STRING:
                result = Type.stringType;
                break;
            default :
                System.out.println("Valor :" +lexer.getStringValue());
              error.signal("Type expected");
              result = null;
        }
        lexer.nextToken();
        return result;
    }
     
     private void typeArray(){
         
         int size1,size2;
         lexer.nextToken();
               
         if(lexer.token != Symbol.LEFTSQBRACKET)
             error.show("Símbolo '[' faltando");
         
         lexer.nextToken();
         
         if(lexer.token != Symbol.NUMBER)
             error.show("Número esperado");
        
         size1 = lexer.getNumberValue();             
         lexer.nextToken();        
         if(lexer.token != Symbol.ENDPROG)
               error.show("Esperado '..' após o primeiro número");
         
         lexer.nextToken();
         if(lexer.token != Symbol.ENDPROG)
                error.show("Esperado '..' após o primeiro número");
         
         lexer.nextToken();
         if(lexer.token != Symbol.NUMBER)
              error.show("Número esperado");
         
         size2 = lexer.getNumberValue();
         lexer.nextToken();
         if(lexer.token != Symbol.RIGHTSQBRACKET)
             error.show("Símbolo ']' esperado");
         lexer.nextToken();
         if(lexer.token != Symbol.OF)
             error.signal("Identificador 'OF' faltando.\nSintaxe de uso: ARRAY [integer .. integer]OF tipo");
         lexer.nextToken();
         size = size2-size1;
     }
    //compstmt ::= BEGIN stmts END
    //stmts ::= stmt | stmts ';' stmt
    private CompositeStatement compstmt() {
        
       
        if(lexer.token != Symbol.BEGIN){
            error.signal("BEGIN não encontrado");
        }
        
        lexer.nextToken();
        StatementList st = stmts();
       
        if(lexer.token != Symbol.END){
            error.signal("END não encontrado");
        }
        lexer.nextToken();
        return new CompositeStatement(st);
    }

    private Subdcls subdcls() {
       return null;
    }

    private StatementList stmts() {
        Symbol tk=lexer.token;
        Statement astatement;
        ArrayList<Statement> v = new ArrayList<Statement>();
        
        while(tk != Symbol.END && tk!=Symbol.ELSE && tk!=Symbol.ENDIF){
            astatement = null;
            try {
                // statement() should return null in a serious error
              astatement = stmt();
            } catch (  StatementException e ) {
                lexer.skipToNextStatement();
            }
            if(astatement != null){
                v.add(astatement);
                if(lexer.token!=Symbol.SEMICOLON){
                    error.show("; faltando");
                    lexer.skipPunctuation();
                }else{
                    lexer.nextToken();
                }
            }
            tk = lexer.token;
        }
        return new StatementList(v);
    }
    //stmt ::= elstmt
//	| IF expr THEN stmt [ ELSE stmt ] ENDIF
//	| WHILE expr DO stmt ENDWHILE
    private Statement stmt() throws StatementException{
        switch(lexer.token){
            case WRITE:
                return writeStatement();
            default:
                error.signal("Erro");
                break;
        }
        return null;
    }

    private WriteStatement writeStatement() {
        lexer.nextToken();
        if(lexer.token != Symbol.LEFTPAR){
            error.show("'(' faltando");
            lexer.skipBraces();
        }else{
            lexer.nextToken();
        }
        ExprList e = exprList();
        if(lexer.token != Symbol.RIGHTPAR){
            error.show("')' faltando");
            lexer.skipBraces();
            lexer.skipToNextStatement();
            return null;
        }else{
            lexer.nextToken();
            return new WriteStatement(e);
        }
        
    }
    //exprlist ::= expr | exprlist ',' expr
    private ExprList exprList() {
        ExprList exList = new ExprList();
        Expr e = null;
        while(lexer.token != Symbol.RIGHTPAR && lexer.token != Symbol.RIGHTSQBRACKET){
            e = expr();
            exList.addElement(e); 
            if(lexer.token != Symbol.COLON && lexer.token != Symbol.RIGHTPAR && lexer.token !=Symbol.RIGHTSQBRACKET){
                System.out.println("Valor do token = "+lexer.token.toString());
                error.signal("Use ',' para separar expressões");
            }     
        }
        
        return exList;
    }
//expr ::= simexp | simexp relop expr
//simexp ::= term | unary term | simexp addop term
//term ::= factor | term mulop factor
//factor ::= vbl
//	| num
//	| '(' expr ')'
//	| procfunc /* should be FUNCTION */
//relop ::=  '=' | '<' | '>' | '<=' | '>=' | '<>'
    private Expr expr() {
        Expr left, right;
        left = simexp();
        Symbol s;
        if(lexer.token == Symbol.ASSIGN || lexer.token == Symbol.LT
                || lexer.token == Symbol.GT || lexer.token == Symbol.LE
                || lexer.token == Symbol.GE || lexer.token == Symbol.NEQ){
            s = lexer.token;
            lexer.nextToken();
            right = expr();
            left = new CompositeExpr(left, s, right);   
        }
        return left;
    }
//simexp ::= term | unary term | simexp addop term
//term ::= factor | term mulop factor
//addop ::=  '+' | '-' | OR  /* + também representa concatenação de string */
//mulop ::= '*' | '/' | AND | MOD | DIV
//unary ::=  '+' | '-' | NOT
    private Expr simexp() {
        Expr left;
        if(lexer.token == Symbol.PLUS || lexer.token == Symbol.MINUS || lexer.token == Symbol.NOT){
            Symbol s = lexer.token;
            lexer.nextToken();
            Expr right = term();
            return new CompositeExpr(null,s,right);
        }else{
            left = term();        
        }
        return left;
    }
//term ::= factor | term mulop factor
//addop ::=  '+' | '-' | OR  /* + também representa concatenação de string */
//mulop ::= '*' | '/' | AND | MOD | DIV
//unary ::=  '+' | '-' | NOT
//factor ::= vbl
//	| num
//	| '(' expr ')'
//	| procfunc /* should be FUNCTION */
    private Expr term() {
        
        if(lexer.token == Symbol.NUMBER){
            NumberExpr n = new NumberExpr(lexer.getNumberValue());
            lexer.nextToken();
            return n;
        }
        if(lexer.token == Symbol.IDENT){
            StringExpr e = null;
            if( symbolTable.getInLocal(lexer.token)==null){
                e = new StringExpr(lexer.getStringValue().toString());
            }
            lexer.nextToken();
            if(lexer.token == Symbol.RIGHTPAR){
                return e;
            }else{
                error.show("')' faltando");
            }
            
        }
        return null;
    }
}

 