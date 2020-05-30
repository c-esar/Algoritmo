/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Factory;


import Strategia.MetodosCalculo;
import CargaDatosExcel.CargaExcel;
import CargaDatosExcel.CargaExcelImp;
import Strategia.MetodosCalculoDistanciaImp;

/**
 *
 * @author cesard.chacond
 */
public class FactoryImp implements FactoryImplementacion {

    @Override
    public boolean inicioMetodos(Object a) {
        if (a instanceof MetodosCalculoDistanciaImp){
           return ((MetodosCalculoDistanciaImp) a).InicioProceso(); 
        } else if (a instanceof CargaExcelImp){
            return ((CargaExcelImp) a).IniciarLecturaExcel();
        }  
        return false;
    }
}
