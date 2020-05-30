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
}
