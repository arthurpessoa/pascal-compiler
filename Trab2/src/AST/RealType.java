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
public class RealType extends Type{
     public RealType() {
        super("real");
    }
    
   public String getCname() {
      return "real";
   }
}
