/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package AST;

/**
 *
 * @author JoaoEduardo
 */
public class StringType extends Type {
    public StringType() {
        super("String");
    }
    
   public String getCname() {
      return "String";
   }
}
