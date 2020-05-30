/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Builder;

import Variables.AtributosSistema;

/**
 *
 * @author cesard.chacond
 */
public class DatosDirector {
    private SalidaBuilder salidaBuilder;

    public void setSalidaBuilder(SalidaBuilder salidaBuilder) {
        this.salidaBuilder = salidaBuilder;
    }
    
    public AtributosSistema getDatos(){
        return salidaBuilder.getDatos();
    }
    public void creacionSalida(){
        salidaBuilder.crear();
        salidaBuilder.salida();
    }
}
