
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
//      |''' . ''' *qualquer conjunto de caracteres entre ' ' (aspas simples).
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
                 //lexer.nextToken();
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
         //lexer.nextToken();
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
         }
         subdcls = subdcls();
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
        Symbol tk=null;
        Statement astatement;
        ArrayList<Statement> v = new ArrayList<>();
        
        while ( (tk = lexer.token) != Symbol.END && 
                tk != Symbol.ELSE &&
                tk != Symbol.ENDIF) {
            astatement = stmt();
            if(astatement != null){
                v.add(astatement);  
                if(lexer.token!=Symbol.SEMICOLON && lexer.token!=Symbol.END && lexer.token!=Symbol.ENDIF){
                    error.signal("; faltando "+lexer.getStringValue());
                    lexer.skipPunctuation();
                }else{
                    if(lexer.token!=Symbol.END)
                     lexer.nextToken();
                }
            }   
        }
        return new StatementList(v);
    }
    //stmt ::= elstmt
//	| IF expr THEN stmt [ ELSE stmt ] ENDIF
//	| WHILE expr DO stmt ENDWHILE
    private Statement stmt(){
        switch(lexer.token){
            case IF:
                return ifStmt();
            case WHILE:
                return whileStmt();
            default:
                return elstmt();
        }
        
    }
    private Statement elstmt(){
        
        switch(lexer.token){
            case BEGIN:
                return compstmt();
            case WRITE:
                return writeStatement();
            case READ:
                return readStatement();
            case IDENT:
                Variable v = (Variable ) symbolTable.getInLocal( lexer.getStringValue() );
                if(v!=null){
                    lexer.nextToken();
                    if(lexer.token == Symbol.ASSIGN){ 
                        lexer.nextToken();
                        Expr e = expr();
                        //lexer.nextToken();
                        if(e.getType() == v.getType())
                            return new AssignmentStatement(v,e);
                    }
                }else{
                    error.signal("Variavel não declarada");
                }
                break;
            default:
                error.signal(lexer.getStringValue()+" Comando inválido");
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
        do{
            e = expr();
            //lexer.nextToken();
            exList.addElement(e); 
           
            if(lexer.token != Symbol.COMMA && lexer.token != Symbol.RIGHTPAR && lexer.token !=Symbol.RIGHTSQBRACKET){
                System.out.println("Valor do token = "+lexer.getStringValue());
                error.signal("Use ',' para separar expressões");
            }  
            if(lexer.token == Symbol.COMMA)
                lexer.nextToken();
        }while(lexer.token != Symbol.RIGHTPAR && lexer.token != Symbol.RIGHTSQBRACKET && lexer.token != Symbol.SEMICOLON);
        
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
        lexer.nextToken();
        Symbol s;
        if(lexer.token == Symbol.EQ || lexer.token == Symbol.LT
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
//vbl ::= id | id '[' expr ']'
//vbllist ::= vbl | vbllist ',' vbl
    private Expr term() {
        if(lexer.token == Symbol.NUMBER){
            NumberExpr n = new NumberExpr(lexer.getNumberValue());
            return n;
        }
        if(lexer.token == Symbol.IDENT){
            Expr x = null;
            Symbol s = null;
            Variable v = (Variable ) symbolTable.getInLocal( lexer.getStringValue() );
            if( v ==null){
                error.signal("Variavel "+lexer.getStringValue() +" não declarada");
            }
            Type t = v.getType();          
            if(t == Type.integerType){
                x = new IntExpr(lexer.getStringValue());  
            }
            if(t == Type.stringType){
                x = new StringExpr(lexer.getStringValue());  
            }
            if(t == Type.charType)
                x = new CharExpr(lexer.getStringValue());
            return x;
            
        }
        if(lexer.token == Symbol.ASPAS){
            lexer.nextToken();
            SentenceExpr s = null;
            s = new SentenceExpr(lexer.getStringValue());
            lexer.nextToken();
            if(lexer.token == Symbol.ASPAS){      
                //lexer.nextToken();
                return s;
            }
            else
                error.signal("Aspas não fechadas");
        }
        return null;
    }
    //IF expr THEN stmt [ ELSE stmt ] ENDIF
    private Statement ifStmt() {
        if(lexer.token == Symbol.IF){
            lexer.nextToken();
            Expr e = expr();
            //lexer.nextToken();
            if(lexer.token == Symbol.THEN){
                lexer.nextToken();
                Statement sThen = stmt();
                lexer.nextToken();
                //lexer.nextToken();
                Statement sElse = null;
                if(lexer.token == Symbol.ELSE){
                    lexer.nextToken();
                    sElse = stmt();
                    lexer.nextToken();                    
                }
                if(lexer.token == Symbol.ENDIF){
                    return new IfStatement(e,sThen,sElse);
                }else{
                    error.signal("Faltando a expressão ENDIF");
                }
            }else{
                error.signal("Faltando a expressão THEN");
            }
        }
        return null;
    }

    private Statement whileStmt() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    private Statement readStatement() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}

 