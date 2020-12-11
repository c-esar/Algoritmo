package Constantes;
import Variables.AtributosSistema;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author cesard.chacond
 */
public interface Constantes {
 
    final AtributosSistema datos = AtributosSistema.getInstance();
    final String Capacidad_Vehiculo = "Capacidad Vehiculo";
    final String Volumen_Vehiculo = "Volumen Vehiculo";
    final String Nodos_Con_Ahorro = "NodosConAhorro";
    final String Nodos_Sin_Ahorro = "NodosSinAhorro";
    final String Nodos_Superan_Capacidad_Vehiculo = "NodosSuperanCapacidadVehiculo"; //Con Ahorro pero Supera la capacidad del vehiculo o el volumen
    final String Nodos_Directos = "NodosDirectos";
    final String Imprimir_Nodos = "ImprimirNodos";
    final String Demanda_kg = "Demanda kg";
    final String Volumen = "Volumen";
    final String PesoTotalSistema = "Peso";
    final String DistanciaTOtalSistema = "Distancia";
    final String VolumenTotalSistema = "Volumen";
    final String PesoTotalPorDia= "PesoDia";
    final String TablaConversionPeso0 = "0";
    final String TablaConversionPeso5999 = "5999";
    final String TablaConversionPeso6000 = "6000";
    final String TablaConversionPeso11999 = "11999";
    final String TablaConversionPeso12000 = "12000";
    final String TablaConversionPeso17999 = "17999";
    final String TablaConversionPeso18000 = "18000";
    final String TablaConversionPeso20999 = "20999";
    final String TablaConversionPeso21000 = "21000";
    final String TablaConversionPeso24000 = "24000";
    final String TablaMultiplicador1 ="0.73190";
    final String TablaMultiplicador2 ="0.81480";
    final String TablaMultiplicador3 ="0.90790";
    final String TablaMultiplicador4 ="1.00890";
    final String TablaMultiplicador5 ="1.08380";
    
}
