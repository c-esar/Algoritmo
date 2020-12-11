/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Variables;

import Constantes.Constantes;
import java.util.ArrayList;
import java.util.HashMap;

/**
 *
 * @author David
 */
public class AtributosSistema {
 
    private static AtributosSistema metodosCargaAB;
    private int numeroProvedores = 0;
    private HashMap<String, Double> CapVolVehiculo;
    private HashMap<String, HashMap<String, ArrayList<Double>>> PesoVolProvedores; // <Dia,<peso/vol(nombre), provedores(pesoKG)>
    private HashMap<String, HashMap<String, ArrayList<ArrayList<Double>>>> MatrizPuntos;
    private HashMap<String, HashMap<String, ArrayList<ArrayList<Double>>>> ImprimirNodos;
    private HashMap<String, Double> CargaTotalSistema;
    private HashMap<String, HashMap<String, Double>> PesoTotalPorDia; //Para saber si todos los nodos se unen
    private HashMap<String, ArrayList<Integer>> NodosSuperanVehiculo; // el nodo supera la carga del vehiculo
    private HashMap<String, HashMap<String, ArrayList<Double>>> PesoVolProvedoresTemporal;
    private ArrayList<Double> Nodos;
    private double[][] distancias;
    private HashMap<String, double[][]> distanciasProvicional;
    private String error;
    private static ArrayList<String> variablesNombresExcel;
    private double porRuta;

    //nuevo
    private double CargaMinima; //cargar el numero por pantalla
    private HashMap<String, HashMap<String, ArrayList<Double>>> NodosCargaMinima; // el nodos que estan por debajo de la carga minima
    private HashMap<String, ArrayList<Integer>> distanciasNoEvaluar;
    private boolean entreFunciones; //cuando elo sistema tiene
    private HashMap<String, HashMap<String, ArrayList<Double>>> resultadosFinales;

    //proeceoss
    private double kilometrosDesviar;
    private ArrayList<String> provedoresNombre;
    private HashMap<String, ArrayList<Double>> centroConsolidacion;
    private HashMap<String, ArrayList<ArrayList<Double>>> puntosDivicion;

    //emisiones
    private HashMap<String, double[][]> distanciasEmision;
    
    static {
        variablesNombresExcel = new ArrayList<>();
        variablesNombresExcel.add(Constantes.Demanda_kg);
        variablesNombresExcel.add(Constantes.Volumen);
        variablesNombresExcel.add("Dia");
    }

    public AtributosSistema() {
        this.Nodos = new ArrayList<>();
        this.centroConsolidacion = new HashMap<>();
        this.puntosDivicion = new HashMap<>();
        this.provedoresNombre = new ArrayList<>();
        this.NodosSuperanVehiculo = new HashMap<>();
        this.PesoVolProvedores = new HashMap<>();
        this.CapVolVehiculo = new HashMap<>();
        this.MatrizPuntos = new HashMap<>();
        this.ImprimirNodos = new HashMap<>();
        this.CargaTotalSistema = new HashMap<>();
        this.PesoTotalPorDia = new HashMap<>();
        this.NodosCargaMinima = new HashMap<>();
        this.PesoVolProvedoresTemporal = this.PesoVolProvedores;
        this.distanciasNoEvaluar = new HashMap<>();
        this.entreFunciones = true;
        this.resultadosFinales = new HashMap<>();
        this.distanciasProvicional = new HashMap<>();
        this.distanciasEmision = new HashMap<>();
    }

    public void IniciarSistema() {
        this.Nodos = new ArrayList<>();
        this.centroConsolidacion = new HashMap<>();
        this.puntosDivicion = new HashMap<>();
        this.provedoresNombre = new ArrayList<>();
        this.NodosSuperanVehiculo = new HashMap<>();
        this.PesoVolProvedores = new HashMap<>();
        this.CapVolVehiculo = new HashMap<>();
        this.MatrizPuntos = new HashMap<>();
        this.ImprimirNodos = new HashMap<>();
        this.CargaTotalSistema = new HashMap<>();
        this.PesoTotalPorDia = new HashMap<>();
        this.NodosCargaMinima = new HashMap<>();
        this.PesoVolProvedoresTemporal = this.PesoVolProvedores;
        this.distanciasNoEvaluar = new HashMap<>();
        this.entreFunciones = true;
        this.resultadosFinales = new HashMap<>();
        this.distanciasProvicional = new HashMap<>();
    }

    public static AtributosSistema getInstance() {
        if (metodosCargaAB == null) {
            metodosCargaAB = new AtributosSistema();
        }
        return metodosCargaAB;
    }

    public int getNumeroProvedores() {
        return numeroProvedores;
    }

    public void setNumeroProvedores(int numeroProvedores) {
        this.numeroProvedores = numeroProvedores;
    }

    public double[][] getDistancias() {
        return distancias;
    }

    public void arregloMatriz(int valor) {
        this.distancias = new double[valor][valor];
    }

    public void setDistancias(double[][] distancias) {
        this.distancias = distancias;
    }

    public HashMap<String, Double> getCapVolVehiculo() {
        return CapVolVehiculo;
    }

    public void setCapVolVehiculo(HashMap<String, Double> CapVolVehiculo) {
        this.CapVolVehiculo = CapVolVehiculo;
    }

    public HashMap<String, HashMap<String, ArrayList<Double>>> getPesoVolProvedores() {
        return PesoVolProvedores;
    }

    public void setPesoVolProvedores(HashMap<String, HashMap<String, ArrayList<Double>>> PesoVolProvedores) {
        this.PesoVolProvedores = PesoVolProvedores;
    }

    public HashMap<String, HashMap<String, ArrayList<ArrayList<Double>>>> getMatrizPuntos() {
        return MatrizPuntos;
    }

    public void setMatrizPuntos(HashMap<String, HashMap<String, ArrayList<ArrayList<Double>>>> MatrizPuntos) {
        this.MatrizPuntos = MatrizPuntos;
    }

    public HashMap<String, HashMap<String, ArrayList<ArrayList<Double>>>> getImprimirNodos() {
        return ImprimirNodos;
    }

    public void setImprimirNodos(HashMap<String, HashMap<String, ArrayList<ArrayList<Double>>>> ImprimirNodos) {
        this.ImprimirNodos = ImprimirNodos;
    }

    public static AtributosSistema getMetodosCargaAB() {
        return metodosCargaAB;
    }

    public static void setMetodosCargaAB(AtributosSistema metodosCargaAB) {
        AtributosSistema.metodosCargaAB = metodosCargaAB;
    }

    public ArrayList<Double> getNodos() {
        return Nodos;
    }

    public void setNodos(ArrayList<Double> Nodos) {
        this.Nodos = Nodos;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public static ArrayList<String> getVariablesNombresExcel() {
        return variablesNombresExcel;
    }

    public static void setVariablesNombresExcel(ArrayList<String> variablesNombresExcel) {
        AtributosSistema.variablesNombresExcel = variablesNombresExcel;
    }

    public HashMap<String, Double> getCargaTotalSistema() {
        return CargaTotalSistema;
    }

    public void setCargaTotalSistema(HashMap<String, Double> CargaTotalSistema) {
        this.CargaTotalSistema = CargaTotalSistema;
    }

    public HashMap<String, HashMap<String, Double>> getPesoTotalPorDia() {
        return PesoTotalPorDia;
    }

    public void setPesoTotalPorDia(HashMap<String, HashMap<String, Double>> PesoTotalPorDia) {
        this.PesoTotalPorDia = PesoTotalPorDia;
    }

    public HashMap<String, ArrayList<Integer>> getNodosSuperanVehiculo() {
        return NodosSuperanVehiculo;
    }

    public void setNodosSuperanVehiculo(HashMap<String, ArrayList<Integer>> NodosSuperanVehiculo) {
        this.NodosSuperanVehiculo = NodosSuperanVehiculo;
    }

    public double getCargaMinima() {
        return CargaMinima;
    }

    public void setCargaMinima(double CargaMinima) {
        this.CargaMinima = CargaMinima;
    }

    public HashMap<String, HashMap<String, ArrayList<Double>>> getNodosCargaMinima() {
        return NodosCargaMinima;
    }

    public void setNodosCargaMinima(HashMap<String, HashMap<String, ArrayList<Double>>> NodosCargaMinima) {
        this.NodosCargaMinima = NodosCargaMinima;
    }

    public HashMap<String, HashMap<String, ArrayList<Double>>> getPesoVolProvedoresTemporal() {
        return PesoVolProvedoresTemporal;
    }

    public void setPesoVolProvedoresTemporal(HashMap<String, HashMap<String, ArrayList<Double>>> PesoVolProvedoresTemporal) {
        this.PesoVolProvedoresTemporal = PesoVolProvedoresTemporal;
    }

    public HashMap<String, ArrayList<Integer>> getDistanciasNoEvaluar() {
        return distanciasNoEvaluar;
    }

    public void setDistanciasNoEvaluar(HashMap<String, ArrayList<Integer>> distanciasNoEvaluar) {
        this.distanciasNoEvaluar = distanciasNoEvaluar;
    }

    @Override
    public String toString() {
        return "AtributosSistema{" + "distanciasNoEvaluar=" + distanciasNoEvaluar + '}';
    }

    public boolean isEntreFunciones() {
        return entreFunciones;
    }

    public void setEntreFunciones(boolean entreFunciones) {
        this.entreFunciones = entreFunciones;
    }

    public double getPorRuta() {
        return porRuta;
    }

    public void setPorRuta(double porRuta) {
        this.porRuta = porRuta;
    }

    public double getKilometrosDesviar() {
        return kilometrosDesviar;
    }

    public void setKilometrosDesviar(double kilometrosDesviar) {
        this.kilometrosDesviar = kilometrosDesviar;
    }

    public ArrayList<String> getProvedoresNombre() {
        return provedoresNombre;
    }

    public void setProvedoresNombre(ArrayList<String> provedoresNombre) {
        this.provedoresNombre = provedoresNombre;
    }

    public HashMap<String, HashMap<String, ArrayList<Double>>> getResultadosFinales() {
        return resultadosFinales;
    }

    public void setResultadosFinales(HashMap<String, HashMap<String, ArrayList<Double>>> resultadosFinales) {
        this.resultadosFinales = resultadosFinales;
    }

    public HashMap<String, ArrayList<Double>> getCentroConsolidacion() {
        return centroConsolidacion;
    }

    public void setCentroConsolidacion(HashMap<String, ArrayList<Double>> centroConsolidacion) {
        this.centroConsolidacion = centroConsolidacion;
    }

    public HashMap<String, ArrayList<ArrayList<Double>>> getPuntosDivicion() {
        return puntosDivicion;
    }

    public void setPuntosDivicion(HashMap<String, ArrayList<ArrayList<Double>>> puntosDivicion) {
        this.puntosDivicion = puntosDivicion;
    }

    public HashMap<String, double[][]> getDistanciasProvicional() {
        return distanciasProvicional;
    }

    public void setDistanciasProvicional(HashMap<String, double[][]> distanciasProvicional) {
        this.distanciasProvicional = distanciasProvicional;
    }

    public HashMap<String, double[][]> getDistanciasEmision() {
        return distanciasEmision;
    }

    public void setDistanciasEmision(HashMap<String, double[][]> distanciasEmision) {
        this.distanciasEmision = distanciasEmision;
    }

}
