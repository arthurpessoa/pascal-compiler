
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
    private int isFunction = 0;
    
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
                error.signal("Nome do programa não pode iniciar com um número");
            }
            lexer.nextToken();

            if(lexer.token == Symbol.SEMICOLON){
                lexer.nextToken();
                 program = new Program(programName, body());
                 //lexer.nextToken();
            }else{
                error.signal("Simbolo ';' faltando");
            }
            if(lexer.token == Symbol.ENDPROG){
                return program;
            }else{
                error.signal("Programa deve encerrar com um '.'");
            }
        }else{
            error.signal("Programas devem iniciar com o identificador PROGRAM.\n"
                    + "Identificador encontrado: "+lexer.getStringValue());
        }
       return null;
     }
     //body ::= dclpart compstmt | compstmt
     private Body body(){
         
         Dclpart dclpart = dclpart();
         CompositeStatement compstmt = compstmt();
         //lexer.nextToken();
         Body body = new Body(dclpart, compstmt);
         //symbolTable.removeLocalIdent();
         return body;
     }
     //dclpart ::= VAR dcls | subdcls | VAR dcls subdcls
     private Dclpart dclpart(){
         Dcls dcls = null;
         ArrayList<Subdcls> subdcls = new ArrayList<>();
         if(lexer.token == Symbol.VAR){
             lexer.nextToken();
             dcls = dcls();
         }
         while(lexer.token == Symbol.PROCEDURE ||
               lexer.token == Symbol.FUNCTION){
            subdcls.add(subdcls());
         }
         Dclpart dclpart = new Dclpart(dcls,subdcls);
         return dclpart;
     }
     //dcls ::= dcl | dcls dcl
     private Dcls dcls(){
        ArrayList<Variable> varList = new ArrayList<>();
        
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
                     error.signal("Palavra "+lexer.token.toString() +" é reservada"
                             + " e não pode ser usada como identificador");
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
             error.signal("Nome da variavel deve ser seguido pelo simbolo ':'");
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
    //subdcls ::= subdcl | subdcls subdcl
//subdcl ::= subhead ';' body ';'
//subhead ::= FUNCTION pid args ':' stdtype | PROCEDURE pid args
    private Subdcls subdcls() {
       
        if(lexer.token == Symbol.FUNCTION){
            isFunction = 1;
            lexer.nextToken();
            return subFunction();
        }
        if(lexer.token == Symbol.PROCEDURE){
            isFunction = 0;
            lexer.nextToken();
            return subProcedure();
        }
        return null;
    }

    private StatementList stmts() {
        Symbol tk;
        Statement astatement;
        ArrayList<Statement> v = new ArrayList<>();
        
        while ( (tk = lexer.token) != Symbol.END && 
                tk != Symbol.ELSE &&
                tk != Symbol.ENDIF &&
                tk != Symbol.ENDWHILE
                ) {
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
        Statement s = elstmt();
        if(s == null){
            switch(lexer.token){
                case IF:
                    return ifStmt();
                case WHILE:
                    return whileStmt();
                default:
                    error.signal("Identificador desconhecido: "+lexer.getStringValue());
                    break;
            }
        }
        return s;
    }
    //elstmt ::= vbl ':=' expr
//	| RETURN expr /* in a function */
//	| RETURN      /* in a procedure */
//	| procfunc 	  /* should be procedure */
//	| compstmt
//	| READ '(' vbllist ')'
//	| WRITE '(' exprlist ')'
//	| WRITELN '(' exprlist ')'
//	| WRITELN '(' ')'
    private Statement elstmt(){
        switch(lexer.token){
            case BEGIN:
                return compstmt();
            case WRITE:
                return writeStatement();
            case WRITELN:
                return writeLnStatement();
            case READ:
                return readStatement();
            case IDENT:
                if (symbolTable.getInGlobal(lexer.getStringValue()) instanceof Procedure){   
                    return procedureCall();
                }
                Variable v = (Variable) symbolTable.getInLocal(lexer.getStringValue());
                if(v!=null){   
                    lexer.nextToken();
                    if(lexer.token == Symbol.ASSIGN){ 
                        lexer.nextToken();
                        Expr e = expr();
                        //lexer.nextToken();
                        
                        if(e.getType() == v.getType()){
                            return new AssignmentStatement(v,e);
                        }
                        else
                            error.signal("Tipos incompativeis na atribuição");
                    }
                }else{
                    error.signal("Identificador "+lexer.getStringValue()+" deve ser uma variavel ou uma procedure");
                    
                }
                break;
            case RETURN:
                    return returnAssignment();
            default:
                return null;
              
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
        Expr e;
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
        Symbol s ;
        Expr right;
        if(lexer.token == Symbol.PLUS || lexer.token == Symbol.MINUS || lexer.token == Symbol.NOT){
            s = lexer.token;
            lexer.nextToken();
             right = term();
             
            return new CompositeExpr(null,s,right);
        }else{
            left = term();        
        }
        //lexer.nextToken();
        if(lexer.token == Symbol.PLUS || lexer.token == Symbol.MINUS ||
                lexer.token == Symbol.OR){
            s = lexer.token;
            lexer.nextToken();
            right = term();
            //lexer.nextToken();
            if(s==Symbol.PLUS){
                if(left.getType() != right.getType()){
                    if(left.getType()==Type.stringType || right.getType()==Type.stringType){
                        error.signal("Tipos incompativeis");
                    }
                }
            }else{
                if(left.getType()==Type.stringType || left.getType()==Type.charType ||
                        right.getType() == Type.stringType || right.getType()==Type.charType
                        && s==Symbol.MINUS){
                    error.signal("Operação incompativel com letras");
                }
            }
            return new CompositeExpr(left,s,right);
            
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
        Expr left = factor();
        lexer.nextToken();
        if(lexer.token == Symbol.MULT || lexer.token==Symbol.DIV || lexer.token == Symbol.DIVI||
                lexer.token == Symbol.AND || lexer.token == Symbol.MOD){
            Symbol s = lexer.token;
            lexer.nextToken();
            Expr right = factor();
            lexer.nextToken();
            if(left.getType() == right.getType()){
                return new CompositeExpr(left,s,right);
            }
            else{
                error.signal("Operacao com tipos incompativeis");
            }
        }
            return left;
        
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
                    lexer.nextToken();
                    return new IfStatement(e,sThen,sElse);
                }else{
                    error.signal("Faltando a expressão ENDIF "+lexer.token);
                }
            }else{
                error.signal("Faltando a expressão THEN");
            }
        }
        return null;
    }
    //WHILE expr DO stmt ENDWHILE
    private Statement whileStmt() {
        if(lexer.token == Symbol.WHILE){
            lexer.nextToken();
            Expr whilePart = expr();
            //lexer.nextToken();
            if(lexer.token != Symbol.DO){
                error.signal("Comando DO não encontrado "+lexer.token);
            }
                lexer.nextToken();
                StatementList doPart = stmts();
                //lexer.nextToken();
                if(lexer.token != Symbol.ENDWHILE)
                    error.signal("Comando while deve encerrar com ENDWHILE");
                lexer.nextToken();
                return new WhileStatement(whilePart, doPart);
                
            
        }
        return null;
    }
    //READ '(' vbllist ')'
    private Statement readStatement() {
        if(lexer.token == Symbol.READ){
            lexer.nextToken();
            if(lexer.token != Symbol.LEFTPAR)
                error.signal("'(' faltando");
            lexer.nextToken();
            ArrayList<Variable> var = vblist();
            if(lexer.token != Symbol.RIGHTPAR)
                error.signal("')' faltando  "+lexer.token);
            lexer.nextToken();
            return new ReadStatement(var);
        }
        return null;
    }

    private ArrayList<Variable> vblist() {
        ArrayList<Variable> var = new ArrayList<>();
        Variable v;
        while(lexer.token == Symbol.IDENT){
            v = (Variable)symbolTable.getInLocal(lexer.getStringValue());
            if(v==null)
                error.signal("Variável "+lexer.getStringValue()+" não declarada");
            var.add(v);
            lexer.nextToken();
            if(lexer.token != Symbol.COMMA && lexer.token != Symbol.RIGHTPAR)
                error.signal("Variaveis devem ser separadas por ','   "+lexer.token);
            if(lexer.token != Symbol.RIGHTPAR)
                lexer.nextToken();
        }
        if(var.isEmpty()){
            error.signal("Nenhuma variável foi digitada");
        }
        return var;
    }

    private Statement writeLnStatement() {
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
            return new WriteLnStatement(e);
        }
        
    }
    //subdcl ::= subhead ';' body ';'
    //subhead ::= FUNCTION pid args ':' stdtype | PROCEDURE pid args
    private Function subFunction() {
        if(lexer.token!=Symbol.IDENT)
            error.signal("Nome da função esperada");
        String name = (String) lexer.getStringValue();
        Function s  =  (Function) symbolTable.getInGlobal(name);
        if(s!= null)
            error.show("Função "+name+" não foi declarada");
        lexer.nextToken();
        s = currentFunction = new Function(name);
        symbolTable.putInGlobal(name, s);
        if(lexer.token != Symbol.LEFTPAR){
            error.show("'(' esperado");
            lexer.skipBraces();
        }else{
            lexer.nextToken();
        }
        s.setParamList(dcls());
        if(lexer.token != Symbol.RIGHTPAR){
            error.show("')' esperado "+lexer.token);
            lexer.skipBraces();
        }else{
            lexer.nextToken();
        }
        if(lexer.token != Symbol.COLON){
            error.show(": esperado "+lexer.token);
            lexer.skipPunctuation();
        }else{
            lexer.nextToken();
        }
        ((Function ) s).setReturnType( type() );
        s.setBody(body());
        return s;
    }

    private Subdcls subProcedure() {
        if(lexer.token!=Symbol.IDENT)
            error.signal("Nome da função esperada");
        String name = (String) lexer.getStringValue();
        Procedure s  =  (Procedure) symbolTable.getInGlobal(name);
        if(s!= null)
            error.show("Procedure "+name+" não foi declarada");
        lexer.nextToken();
        s  = new Procedure(name);
        symbolTable.putInGlobal(name, s);
        if(lexer.token != Symbol.LEFTPAR){
            error.show("'(' esperado");
            lexer.skipBraces();
        }else{
            lexer.nextToken();
        }
        s.setParamList(dcls());
        if(lexer.token != Symbol.RIGHTPAR){
            error.show("')' esperado "+lexer.token);
            lexer.skipBraces();
        }else{
            lexer.nextToken();
        }
       
        s.setBody(body());
        return s;
    }

    private ReturnStatement returnAssignment() {
      
        lexer.nextToken();
        if(isFunction == 1){
            Expr e = expr();
            if( currentFunction.getReturnType() != e.getType())
                error.signal("Retorno não tem o mesmo tipo da função");
            return new ReturnStatement(e);
        }
        return null;
    }
    //procfunc ::= pid '(' ')' | pid '(' exprlist ')'
    private Subdcls procFunc() {
        String pid = lexer.getStringValue();
        lexer.nextToken();
        if(lexer.token != Symbol.LEFTPAR)
            error.signal("'(' esperado");
        lexer.nextToken();
        ExprList e;
        e = exprList();
        if(lexer.token != Symbol.RIGHTPAR)
            error.signal("')' esperado "+lexer.token);
        lexer.nextToken();
        return new ProcFunc(pid,e);
    }
//factor ::= vbl
//      |''' . ''' *qualquer conjunto de caracteres entre ' ' (aspas simples).
//	| num
//	| '(' expr ')'
//	| procfunc /* should be FUNCTION */
    private Expr factor() {
        if(lexer.token == Symbol.NUMBER){
            NumberExpr n = new NumberExpr(lexer.getNumberValue());
            return n;
        }
        if(lexer.token == Symbol.IDENT){
            Expr x = null;
            Symbol s = null;
            
            if( symbolTable.getInGlobal( lexer.getStringValue()) instanceof Function){
                return functionCall();
            }
            Variable v = (Variable ) symbolTable.getInLocal( lexer.getStringValue() );
            if(v==null)
                error.signal("Identificador "+lexer.getStringValue()+" deve ser uma variavel ou uma função");
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
            Expr s = null;
            StringBuffer sentence = lexer.getSentence();
            lexer.nextToken();
           
                s = new SentenceExpr(sentence.toString());
            if(lexer.token == Symbol.ASPAS){      
                return s;
            }else{
                error.signal("Aspas não fechadas "+lexer.getStringValue());
            }
        }
        return null;
    }

    private Statement procedureCall() {
        ExprList e = null;
        String pid = (String) lexer.getStringValue();
        lexer.nextToken();
        Procedure p = (Procedure) symbolTable.getInGlobal(pid);
        if ( lexer.token != Symbol.LEFTPAR ) {
          error.show("( expected");
          lexer.skipBraces();
        }else
            lexer.nextToken();
        
        if(lexer.token != Symbol.RIGHTPAR){
            e = checkParams(p.getParamList());
            if ( lexer.token != Symbol.RIGHTPAR )
            error.show("Error in expression");
          else
            lexer.nextToken();
        }else{
            if ( p.getParamList() != null)
                error.signal("Parameter expected");
          lexer.nextToken();
        }
     return new ProcedureCall(p,e);   
    }

    private ExprList checkParams(Dcls paramList) {
        ExprList anExprList;
        boolean firstErrorMessage = true;
        if ( lexer.token == Symbol.RIGHTPAR ) 
          return null;
        else{
            Variable parameter;
            int sizeParamList = paramList.getSize();
            Iterator e = paramList.getParamList().iterator();
            anExprList = new ExprList();
            while(true){
                parameter = (Variable) e.next();
                if(sizeParamList < 1 && firstErrorMessage){
                    error.show("Wrong number of parameters in call");
                    firstErrorMessage = false;
                }
                sizeParamList--;
                Expr anExpr = expr();
                if(parameter.getType() != anExpr.getType()){
                    if(parameter.getType() != Type.stringType || anExpr.getType() != Type.sentenceType){
                       
                        error.show("Tipo da variavel esperado: "+parameter.getType().getCname()+"\n"
                                + "Tipo da variavel digitada: "+anExpr.getType().getCname()+"\n"
                                + "Tipos incompativeis");
                    }
                    
                }
                anExprList.addElement(anExpr);
                if(lexer.token == Symbol.COMMA)
                    lexer.nextToken();
                else
                    break;
            }
            if(sizeParamList > 0 && firstErrorMessage)
                error.show("Numero de parametros incompativel");
            return anExprList;
        }
    }

    private FunctionCall functionCall() {
         // we already know the identifier is a function. So we 
          // need not to check it again.
        ExprList anExprList = null;
        
        String name = (String ) lexer.getStringValue();
        lexer.nextToken();
        Function p = (Function ) symbolTable.getInGlobal(name);
        if(p==null){
            error.signal("Função " +name+" não declarada");
        }
        if ( lexer.token != Symbol.LEFTPAR ) {
          error.show("( expected");
          lexer.skipBraces();
        }
        else
          lexer.nextToken();
        
        if ( lexer.token != Symbol.RIGHTPAR ) {
            // The parameter list is used to check if the arguments to the
            // procedure have the correct types 
          anExprList = checkParams(p.getParamList());
          if ( lexer.token != Symbol.RIGHTPAR )
            error.show("Error in expression");
        }
        else {
            // semantic analysis
            // does the procedure has no parameter ?
          if ( p.getParamList() != null && p.getParamList().getSize() != 0 )
            error.show("Parameter expected");
          lexer.nextToken();
        }
        return new FunctionCall(p,anExprList);
    }
}

 