/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Builder;

import Variables.AtributosSistema;

/**
 *
 * @author David
 */
public abstract class SalidaBuilder{
    
    protected AtributosSistema datos;

    public AtributosSistema getDatos(){
        return datos;
    }
    public void crear(){
        datos = AtributosSistema.getInstance();
    }
    public abstract void salida(); 
}
