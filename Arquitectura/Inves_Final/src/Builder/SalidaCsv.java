/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Builder;

import java.util.Arrays;

/**
 *
 * @author David
 */
public class SalidaCsv extends SalidaBuilder{

    @Override
    public void salida() {
        System.out.println(Arrays.toString(datos.getDistancias()));
    }

    
}
