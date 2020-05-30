/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Strategia;

import java.util.ArrayList;
import java.util.HashMap;

/**
 *
 * @author cesard.chacond
 */
public interface MetodosCalculo {

    public boolean InicioProceso();

    public void CrearPuntosArrayList(double[][] Puntos, String dia, int numeroDias);

    public void CompararNodosConCapacidadVehiculo(String dia, Double CapacidadVehiculo, Double Volumen, ArrayList<Double> PesoProvedor, ArrayList<Double> VolumenProvedor);

    public void VerificarNodos(String dia, HashMap<String, ArrayList<ArrayList<Double>>> matrizPuntos, int numeroProvedores, ArrayList<Double> Peso, ArrayList<Double> Volumen);

    public void ImprimirResultado(String dia, HashMap<String, ArrayList<ArrayList<Double>>> matrizPuntos, double[][] distancias, ArrayList<Double> Peso, ArrayList<Double> Volumen);

    public ArrayList<ArrayList<Double>> NodosDirectosSinPuntos(ArrayList<ArrayList<Double>> Sin, String dia);

    public void ImprimirResultadoSistema(boolean par, String dia);

    public void SacarPesoPorDia();

    public String CrearNodosDirectos(String dia);

    public double CalcularAhorros(double[][] distancia, int PuntoX, int PuntoY);

    public boolean CalcularPeso(double PuntoX, double PuntoY, double CapVehiculo);

    public boolean CalcularVolumen(double PuntoX, double PuntoY, double CapVolumen);

    public ArrayList<ArrayList<Double>> OrdenarAhorro(ArrayList<ArrayList<Double>> Puntos); // ahorro mayor a menor

    public void CrearListas(HashMap<String, HashMap<String, ArrayList<ArrayList<Double>>>> matrizPuntos, String numero, int dias, String nombreDia);

    public ArrayList<ArrayList<Double>> CalcularNodosCapacidadVehiculo(ArrayList<ArrayList<Double>> CA, Double CapacidadVehiculo, Double Volumen,
            ArrayList<Double> PesoProvedor, ArrayList<Double> VolumenProvedor, ArrayList<ArrayList<Double>> SinA, String dia);

    public void CrearListas();

    public void VerificarNodosConRuta(HashMap<String, ArrayList<ArrayList<Double>>> matrizPuntos,
            int numeroProvedores, ArrayList<Double> Peso, ArrayList<Double> Volumen, String dia);

    public void ImprimirPuntos(ArrayList<ArrayList<Double>> CA, ArrayList<ArrayList<Double>> SinA,
            double[][] distancias, ArrayList<Double> Peso, ArrayList<Double> Volumen, String dia);

    public double CalcularPesoImprimir(double PuntoX, double PuntoY, double CapVehiculo);

    public double CalcularVolumenImprimir(double PuntoX, double PuntoY, double CapVolumen);

    public double CalcularDistancia(double[][] distancia, int PuntoX, int PuntoY);
}
