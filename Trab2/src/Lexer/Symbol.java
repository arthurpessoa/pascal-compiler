package Lexer;

public enum Symbol {

      EOF("eof"),
      PROGRAM("PROGRAM"),
      VAR("VAR"),
      ARRAY("ARRAY"),
      OF("OF"),
      FUNCTION("FUNCTION"),
      PROCEDURE("PROCEDURE"),
      BEGIN("BEGIN"),
      END("END"),
      IF("IF"),
      THEN("THEN"),
      ELSE("ELSE"),
      ENDIF("ENDIF"),
      WHILE("WHILE"),
      DO("DO"),
      ENDWHILE("ENDWHILE"),
      RETURN("RETURN"),
      READ("READ"),
      WRITE("WRITE"),
      WRITELN("WRITELN"),
      OR("OR"),
      AND("AND"),
      MOD("MOD"),
      DIV("DIV"),
      NOT("NOT"),
      INTEGER("INTEGER"),
      REAL("REAL"),
      CHAR("CHAR"),
      STRING("STRING"),
      IDENT("IDENT"),
      NUMBER("NUMBER"),
      PLUS("+"),
      MINUS("-"),
      MULT("*"),
      DIVI("/"),
      LT("<"),
      LE("<="),
      GT(">"),
      GE(">="),
      NEQ("<>"),
      EQ("=="),
      ASSIGN("="),
      LEFTPAR("("),
      RIGHTPAR(")"),
      SEMICOLON(";"),
      COMMA(","), 
      COLON(":"),
      BOOLEAN("BOOLEAN"),
      CHARACTER("CHARACTER"),
      TRUE("TRUE"),
      FALSE("FALSE"),
      REMAINDER("%"),
      ENDPROG("."),
      CURLYLEFTBRACE("{"),
      CURLYRIGHTBRACE("}"),
      LEFTSQBRACKET("["),
      RIGHTSQBRACKET("]"),;
      
      Symbol(String name) {
          this.name = name;
      }

      public String toString() {
          return name;
      }

      private String name;

}