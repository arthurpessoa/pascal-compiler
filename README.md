Compilador2 - Especificação
===========


Esta gramática é uma versão simples do pascal baseada na gramática do livro do Aho, Sethi and Ullman.
Você deve levar em consideração:
1. comentários delimitados por '{'  e '}'
2. linguagem não é case sensitive, isto é, letras maiúsculas e minúsculas são equivalentes
3. Palavras reservadas da linguagem estão em letras maiúsculas na gramática abaixo. Palavras reservadas não podem ser utilizadas como nome de identificador.


prog ::= PROGRAM pid ';' body '.'   

body ::= dclpart compstmt | compstmt   
   
dclpart ::= VAR dcls | subdcls | VAR dcls subdcls   
dcls ::= dcl | dcls dcl  
dcl ::= idlist ':' type ';'  

idlist ::= id | idlist ',' id  
  
type ::= stdtype | arraytype   
arraytype ::= ARRAY '[' num '..' num ']' OF stdtype  
  
subdcls ::= subdcl | subdcls subdcl  
subdcl ::= subhead ';' body ';'  
subhead ::= FUNCTION pid args ':' stdtype | PROCEDURE pid args  
args ::= '(' dcls ')' | '(' ')'  
  
compstmt ::= BEGIN stmts END  
stmts ::= stmt | stmts ';' stmt  
stmt ::= elstmt  
	| IF expr THEN stmt [ ELSE stmt ] ENDIF  
	| WHILE expr DO stmt ENDWHILE    
    
elstmt ::= vbl ':=' expr  
	| RETURN expr /* in a function */  
	| RETURN      /* in a procedure */  
	| procfunc 	  /* should be procedure */  
	| compstmt  
	| READ '(' vbllist ')'    
	| WRITE '(' exprlist ')'  
	| WRITELN '(' exprlist ')'  
	| WRITELN '(' ')'  

vbl ::= id | id '[' expr ']'  
vbllist ::= vbl | vbllist ',' vbl  

procfunc ::= pid '(' ')' | pid '(' exprlist ')'  
  
exprlist ::= expr | exprlist ',' expr  
expr ::= simexp | simexp relop expr  
simexp ::= term | unary term | simexp addop term  
term ::= factor | term mulop factor  
factor ::= vbl  
	| num  
	| '(' expr ')'  
	| procfunc /* should be FUNCTION */  
  
  
pid ::=  letter { letter | digit }  
id ::=  letter { letter | digit }  
num ::= ['+' | '-' ] digit ['.'] { digit }  
relop ::=  '=' | '<' | '>' | '<=' | '>=' | '<>'  
addop ::=  '+' | '-' | OR  /* + também representa concatenação de string */  
mulop ::= '*' | '/' | AND | MOD | DIV  
unary ::=  '+' | '-' | NOT  
stdtype ::= INTEGER | REAL | CHAR | STRING  
  
Você deve entregar, além do código do compilador em java, 4 pastas, cada uma com um conjunto de testes:   
1. testes com código correto   
2. testes com erros léxicos  
3. testes com erros sintáticos  
4. testes com erros semânticos  

