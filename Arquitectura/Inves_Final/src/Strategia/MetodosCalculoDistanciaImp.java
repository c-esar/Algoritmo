/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Strategia;

import CargaDatosExcel.CargaExcelImp;
import Variables.AtributosSistema;
import static Constantes.Constantes.Capacidad_Vehiculo;
import static Constantes.Constantes.Nodos_Con_Ahorro;
import static Constantes.Constantes.Nodos_Directos;
import static Constantes.Constantes.Nodos_Sin_Ahorro;
import static Constantes.Constantes.Nodos_Superan_Capacidad_Vehiculo;
import static Constantes.Constantes.Volumen_Vehiculo;
import static java.lang.Math.random;
import java.sql.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import java.util.Random;

/**
 *
 * @author cesard.chacond
 */
public class MetodosCalculoDistanciaImp extends MetodosCalculoDistancia {

    private static ArrayList<ArrayList<Double>> puntosOriginal = new ArrayList<>();
    private String dias = null;

    public MetodosCalculoDistanciaImp(double Volumen, double Capacidad, double CargaMinima, double PorRuta, double KilometrosDesviar) {
        datos.getCapVolVehiculo().put(Capacidad_Vehiculo, Capacidad);
        datos.getCapVolVehiculo().put(Volumen_Vehiculo, Volumen);
        datos.setCargaMinima(CargaMinima);
        datos.setPorRuta(PorRuta);
        datos.setKilometrosDesviar(KilometrosDesviar);
        PesosNormal = new ArrayList<>();
        PesosNosuperados = new ArrayList<>();
        Aux = new ArrayList<>();
    }

    @Override
    public boolean InicioProceso() {
        SacarPesoPorDia();
        CargaExcelImp excel = new CargaExcelImp();
        try {
            for (int i = 0; i < datos.getPesoVolProvedores().size(); i++) {
                dias = "Dia" + String.valueOf(i + 1);
                datos.getDistanciasProvicional().put(dias, datos.getDistancias());
                System.out.println("Ruta para " + dias);
                System.out.println("");
//                if (datos.getPesoTotalPorDia().get(dia).get(Constantes.Constantes.Demanda_kg) < datos.getCapVolVehiculo().get(Constantes.Constantes.Capacidad_Vehiculo)) {
//                    System.out.println("Sistema se puede recoger con un solo Camion");
//                    System.out.println(CrearNodosDirectos(dia));
//                    ImprimirResultadoSistema(false, dia);
//                } else {
                CrearPuntosArrayList(datos.getDistancias(), dias, datos.getPesoVolProvedores().size()); // unir parejas con ahorro
                if (datos.isEntreFunciones()) {
                    CompararNodosConCapacidadVehiculo(dias, datos.getCapVolVehiculo().get(Capacidad_Vehiculo), datos.getCapVolVehiculo().get(Volumen_Vehiculo),
                            datos.getPesoVolProvedores().get(dias).get(Constantes.Constantes.Demanda_kg), datos.getPesoVolProvedores().get(dias).get(Constantes.Constantes.Volumen));

                    VerificarNodos(dias, datos.getMatrizPuntos().get(dias), datos.getNumeroProvedores(),
                            datos.getPesoVolProvedores().get(dias).get(Constantes.Constantes.Demanda_kg), datos.getPesoVolProvedores().get(dias).get(Constantes.Constantes.Volumen));

                    this.puntosOriginal = CrearArreglo(datos.getMatrizPuntos().get(dias).get(Nodos_Con_Ahorro), datos.getMatrizPuntos().get(dias).get(Nodos_Superan_Capacidad_Vehiculo),
                            datos.getMatrizPuntos().get(dias).get(Nodos_Directos), dias);
                    CambiarNodos(this.getPuntosOriginal(), dias, datos.getPorRuta());

                    //ImprimirResultado(dia, datos.getMatrizPuntos().get(dia), datos.getDistancias(),
                    //datos.getPesoVolProvedores().get(dia).get(Constantes.Constantes.Demanda_kg), datos.getPesoVolProvedores().get(dia).get(Constantes.Constantes.Volumen));
                    //ImprimirProvesoresCargaMinima(datos.getDistanciasNoEvaluar(), dia);
                    //ImprimirResultadoSistema(true, dia);
                    System.out.println("");
                } else {
                    //ImprimirResultadoSistema(true, dias);
                }
//                }
            }
            excel.crearExcel();
            return true;
        } catch (Exception e) {
            return false;
        }

    }

    @Override
    public void CrearPuntosArrayList(double[][] Puntos, String dia, int numeroDias) {
        ArrayList<ArrayList<Double>> CA = new ArrayList<>(); // con ahorro
        ArrayList<ArrayList<Double>> SinA = new ArrayList<>(); // sin ahorro
        boolean NSV = true;
        boolean entreNVS = true;
        try {
            for (int x = 1; x < Puntos.length; x++) {
                if (SacarProvedoresPeso(datos.getPesoVolProvedores().get(dia).get(Constantes.Constantes.Demanda_kg).get(x), datos.getPesoVolProvedores().get(dia).get(Constantes.Constantes.Volumen).get(x), datos.getCargaMinima(), datos.getCapVolVehiculo().get(Constantes.Constantes.Volumen_Vehiculo), dia)) { // sacar los provedores que esten por debajo de la carga minima) {
                    for (int y = x + 1; y < Puntos[x].length; y++) {
                        if (SacarProvedoresPeso(datos.getPesoVolProvedores().get(dia).get(Constantes.Constantes.Demanda_kg).get(y), datos.getPesoVolProvedores().get(dia).get(Constantes.Constantes.Volumen).get(x), datos.getCargaMinima(), datos.getCapVolVehiculo().get(Constantes.Constantes.Volumen_Vehiculo), dia)) {
                            if (NSV) {
                                datos.getNodosSuperanVehiculo().put(dia, new ArrayList<>());
                                NSV = false;
                            }
                            if (datos.getPesoVolProvedores().get(dia).get(Constantes.Constantes.Demanda_kg).get(x) > datos.getCapVolVehiculo().get(Constantes.Constantes.Capacidad_Vehiculo) || datos.getPesoVolProvedores().get(dia).get(Constantes.Constantes.Volumen).get(x) > datos.getCapVolVehiculo().get(Constantes.Constantes.Volumen_Vehiculo)) {
                                for (int z = 0; z < datos.getNodosSuperanVehiculo().get(dia).size(); z++) {
                                    if (datos.getNodosSuperanVehiculo().get(dia).get(z) == x) {
                                        entreNVS = false;
                                        break;
                                    }
                                }
                                if (entreNVS) {
                                    datos.getNodosSuperanVehiculo().get(dia).add(x);
                                }
                                entreNVS = true;
                                break;
                            } else if (datos.getPesoVolProvedores().get(dia).get(Constantes.Constantes.Demanda_kg).get(y) > datos.getCapVolVehiculo().get(Constantes.Constantes.Capacidad_Vehiculo) || datos.getPesoVolProvedores().get(dia).get(Constantes.Constantes.Volumen).get(y) > datos.getCapVolVehiculo().get(Constantes.Constantes.Volumen_Vehiculo)) {
                                for (int z = 0; z < datos.getNodosSuperanVehiculo().get(dia).size(); z++) {
                                    if (datos.getNodosSuperanVehiculo().get(dia).get(z) == y) {
                                        entreNVS = false;
                                        break;
                                    }
                                }
                                if (entreNVS) {
                                    datos.getNodosSuperanVehiculo().get(dia).add(y);
                                }
                                entreNVS = true;
                            } else {
                                double result = CalcularAhorros(Puntos, x, y);
                                if (result > 0) {
                                    if (CAB) {
                                        CrearListas(datos.getMatrizPuntos(), Nodos_Con_Ahorro, numeroDias, dia);
                                        CAB = false;
                                    }
                                    CA = datos.getMatrizPuntos().get(dia).get(Nodos_Con_Ahorro);
                                    CA.get(0).add((double) x);
                                    CA.get(1).add((double) y);
                                    CA.get(2).add(result);
                                    datos.getMatrizPuntos().get(dia).put(Nodos_Con_Ahorro, CA);
                                } else {
                                    if (SinAB) {
                                        CrearListas(datos.getMatrizPuntos(), Nodos_Sin_Ahorro, numeroDias, dia);
                                        SinAB = false;
                                    }
                                    SinA = datos.getMatrizPuntos().get(dia).get(Nodos_Sin_Ahorro);
                                    SinA.get(0).add((double) x);
                                    SinA.get(1).add((double) y);
                                    SinA.get(2).add(result);
                                    datos.getMatrizPuntos().get(dia).put(Nodos_Sin_Ahorro, SinA);
                                }
                            }
                        } else {
                            GuardarCargasMinimas(y, dia);
                        }
                    }
                } else {
                    GuardarCargasMinimas(x, dia);
                }

            }
            CAB = true;
            SinAB = true;
            if (datos.getMatrizPuntos().size() == 0) {
                datos.setEntreFunciones(false);
            } else {
                System.out.println("Lista de ahorro");
                System.out.println(datos.getMatrizPuntos().size() == 0 ? " " : datos.getMatrizPuntos().get(dia).get(Nodos_Con_Ahorro)); // cuando queda null no hay ahorro
                System.out.println(datos.getMatrizPuntos().size() == 0 ? " " : datos.getMatrizPuntos().get(dia).get(Nodos_Sin_Ahorro));
                System.out.println("");
                datos.setEntreFunciones(true);
            }

        } catch (Exception e) {
            AtributosSistema.getInstance().setError("1");
        }

    }

    @Override
    public void CompararNodosConCapacidadVehiculo(String dia, Double CapacidadVehiculo, Double Volumen, ArrayList<Double> PesoProvedor, ArrayList<Double> VolumenProvedor) {
        ArrayList<ArrayList<Double>> SinA = new ArrayList<>(); // sin ahorro
        SinA = CalcularNodosCapacidadVehiculo(datos.getMatrizPuntos().get(dia).get(Nodos_Con_Ahorro),
                CapacidadVehiculo, Volumen, PesoProvedor, VolumenProvedor,
                datos.getMatrizPuntos().get(dia).get(Nodos_Sin_Ahorro), dia);
        if (SinA != null) {
            datos.getMatrizPuntos().get(dia).put(Nodos_Superan_Capacidad_Vehiculo, SinA);
        }
        System.out.println("Milk Run// Con ahorro");
        System.out.println(datos.getMatrizPuntos().get(dia).get(Nodos_Con_Ahorro));

        System.out.println("Superan la capacidad del vehiculo// Nodos Superan");
        System.out.println(datos.getMatrizPuntos().get(dia).get(Nodos_Superan_Capacidad_Vehiculo));
        System.out.println("");
    }

    @Override
    public void VerificarNodos(String dia, HashMap<String, ArrayList<ArrayList<Double>>> matrizPuntos, int numeroProvedores, ArrayList<Double> Peso, ArrayList<Double> Volumen) {
        VerificarNodosConRuta(matrizPuntos, numeroProvedores, Peso, Volumen, dia);
        System.out.println("Rutas directas");
        System.out.println(datos.getMatrizPuntos().get(dia).get(Nodos_Directos));
        System.out.println("");
    }

    @Override
    public void ImprimirResultado(String dia, HashMap<String, ArrayList<ArrayList<Double>>> matrizPuntos, double[][] distancias, ArrayList<Double> Peso, ArrayList<Double> Volumen) {
        ArrayList<ArrayList<Double>> CA = new ArrayList<>(); // con ahorro
        ArrayList<ArrayList<Double>> SinA = new ArrayList<>(); // puntos directos
        if (matrizPuntos.get(Nodos_Con_Ahorro) != null) {
            CA = OrdenarAhorro(matrizPuntos.get(Nodos_Con_Ahorro));
        }
        if (matrizPuntos.get(Nodos_Directos) != null) {
            SinA = matrizPuntos.get(Nodos_Directos);
        } else {
            SinA = NodosDirectosSinPuntos(SinA, dia);
            matrizPuntos.put(Nodos_Directos, SinA);
            SinA = matrizPuntos.get(Nodos_Directos);
        }

        ImprimirPuntos(CA, SinA, distancias, Peso, Volumen, dia);
        System.out.println("Funcionalidades.EvaluarPuntos.ImprimirResultado()");
    }

    @Override
    public ArrayList<ArrayList<Double>> NodosDirectosSinPuntos(ArrayList<ArrayList<Double>> Sin, String dia) {
        for (int i = 1; i <= datos.getNumeroProvedores(); i++) {
            Sin.add(new ArrayList<>());
            Sin.get(i - 1).add((double) i);
            Sin.get(i - 1).add(datos.getPesoVolProvedores().get(dia).get("Demanda  kg").get(i)); //Obtener el peso del punto
            Sin.get(i - 1).add(datos.getPesoVolProvedores().get(dia).get("Volumen").get(i)); // OBtener el Volumen del punto
        }

        return Sin;
    }

    @Override
    public void ImprimirResultadoSistema(boolean par, String dia) {
        if (par) {
            System.out.println(" ");
            System.out.println("Resultado del sistema " + " Peso: " + datos.getCargaTotalSistema().get(Constantes.Constantes.PesoTotalSistema));
            System.out.println("Resultado del sistema " + " Distancia: " + datos.getCargaTotalSistema().get(Constantes.Constantes.DistanciaTOtalSistema));
            System.out.println("Resultado del sistema " + " Volumen: " + datos.getCargaTotalSistema().get(Constantes.Constantes.VolumenTotalSistema));
            if (datos.getNodosSuperanVehiculo().size() > 0) {
                for (int i = 0; i < datos.getNodosSuperanVehiculo().get(dia).size(); i++) {
                    System.out.println("Nodos Con cargar Mayor al vehiculo Provedor " + datos.getNodosSuperanVehiculo().get(dia).get(i));
                }
            }
            System.out.println(" ");
        } else {
            System.out.println(" ");
            System.out.println("Resultado del sistema " + " Peso: " + datos.getPesoTotalPorDia().get(dia).get(Constantes.Constantes.Demanda_kg));
            //System.out.println("Resultado del sistema " + " Distancia: " + atributosCarga.getPesoTotalPorDia().get(dia).get(Constantes.Constantes.DistanciaTOtalSistema));
            System.out.println("Resultado del sistema " + " Volumen: " + datos.getPesoTotalPorDia().get(dia).get(Constantes.Constantes.Volumen));
//            if (datos.getNodosSuperanVehiculo().size() > 0) {
//                for (int i = 0; i < datos.getNodosSuperanVehiculo().get(dia).size(); i++) {
//                    System.out.println("Nodos Con cargar Mayor al vehiculo Provedor " + datos.getNodosSuperanVehiculo().get(dia).get(i));
//                }
//            }
            System.out.println(" ");
        }
    }

    @Override
    public void SacarPesoPorDia() {
        double resultadoPeso = 0.0;
        double resultadoVolumen = 0.0;
        for (int i = 1; i <= datos.getPesoVolProvedores().size(); i++) {
            datos.getPesoTotalPorDia().put("Dia" + i, new HashMap<>());
            for (int y = 0; y < datos.getPesoVolProvedores().get("Dia" + i).get(Constantes.Constantes.Demanda_kg).size(); y++) {
                resultadoPeso += datos.getPesoVolProvedores().get("Dia" + i).get(Constantes.Constantes.Demanda_kg).get(y);
                resultadoVolumen += datos.getPesoVolProvedores().get("Dia" + i).get(Constantes.Constantes.Volumen).get(y);
            }
            datos.getPesoTotalPorDia().get("Dia" + i).put(Constantes.Constantes.Demanda_kg, resultadoPeso);
            datos.getPesoTotalPorDia().get("Dia" + i).put(Constantes.Constantes.Volumen, resultadoVolumen);
            resultadoPeso = 0.0;
            resultadoVolumen = 0.0;
        }
    }

    @Override
    public String CrearNodosDirectos(String dia) {
        String imprimir = "";
        for (int i = 1; i <= datos.getNumeroProvedores(); i++) {

            imprimir += " Posicion " + i + " a ";
        }

        return imprimir += " Posicion 0 ";
    }

    @Override
    public ArrayList<ArrayList<Double>> OrdenarAhorro(ArrayList<ArrayList<Double>> Puntos) {
        for (int i = 0; i < Puntos.get(2).size() - 1; i++) {
            for (int j = 0; j < Puntos.get(2).size() - 1; j++) {
                if (Puntos.get(2).get(j) < Puntos.get(2).get(j + 1)) {
                    double tmp0 = Puntos.get(0).get(j + 1);
                    Puntos.get(0).set(j + 1, Puntos.get(0).get(j));
                    Puntos.get(0).set(j, tmp0);

                    double tmp1 = Puntos.get(1).get(j + 1);
                    Puntos.get(1).set(j + 1, Puntos.get(1).get(j));
                    Puntos.get(1).set(j, tmp1);

                    double tmp = Puntos.get(2).get(j + 1);
                    Puntos.get(2).set(j + 1, Puntos.get(2).get(j));
                    Puntos.get(2).set(j, tmp);
                }
            }
        }
        return Puntos;
    }

    @Override
    public double CalcularAhorros(double[][] distancia, int PuntoX, int PuntoY) {
        int I = (int) PuntoX;
        int J = (int) PuntoY;
        return /*distancia[0][I] +*/ distancia[J][0] - distancia[I][J];
    }

    @Override
    public void CrearListas(HashMap<String, HashMap<String, ArrayList<ArrayList<Double>>>> matrizPuntos, String numero, int dias, String nombreDia) {
        ArrayList<ArrayList<Double>> a = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            a.add(new ArrayList<>());
        }
        if (matrizPuntos.isEmpty() || !(datos.getMatrizPuntos().containsKey(nombreDia))) {
            datos.getMatrizPuntos().put(nombreDia, new HashMap<>());
            datos.getImprimirNodos().put(nombreDia, new HashMap<>());
        }
        datos.getMatrizPuntos().get(nombreDia).put(numero, a);
//        matrizPuntos.put("1", a); // Matriz con ahorro
//        matrizPuntos.put("2", a); // sin ahorro
    }

    @Override
    public boolean CalcularPeso(double PuntoX, double PuntoY, double CapVehiculo) {
        double resul = 0;
        boolean resulFinal = false;
        resul = PuntoX + PuntoY;
        if (resul <= CapVehiculo) {
            resulFinal = true;
        }
        return resulFinal;
    }

    @Override
    public boolean CalcularVolumen(double PuntoX, double PuntoY, double CapVolumen) {
        double resul = 0;
        boolean resulFinal = false;
        resul = PuntoX + PuntoY;
        if (resul <= CapVolumen) {
            resulFinal = true;
        }
        return resulFinal;
    }

    @Override
    public ArrayList<ArrayList<Double>> CalcularNodosCapacidadVehiculo(ArrayList<ArrayList<Double>> CA, Double CapacidadVehiculo, Double Volumen, ArrayList<Double> PesoProvedor,
            ArrayList<Double> VolumenProvedor, ArrayList<ArrayList<Double>> SinA, String dia) {
// falta crear inicio metodos arraylist
        if (CA != null) {
            CrearListas();
            for (int x = 0; x < CA.get(0).size(); x++) {
                boolean a = CalcularPeso(PesoProvedor.get(CA.get(0).get(x).intValue()), PesoProvedor.get(CA.get(1).get(x).intValue()), CapacidadVehiculo);
                boolean b = CalcularVolumen(VolumenProvedor.get(CA.get(0).get(x).intValue()),
                        VolumenProvedor.get(CA.get(1).get(x).intValue()), Volumen);
                if (a == false || b == false) {
                    PesosNosuperados.get(0).add((double) CA.get(0).get(x).intValue());
                    PesosNosuperados.get(1).add((double) CA.get(1).get(x).intValue());
                } else {
                    PesosNormal.get(0).add((double) CA.get(0).get(x).intValue());
                    PesosNormal.get(1).add((double) CA.get(1).get(x).intValue());
                    PesosNormal.get(2).add((double) CA.get(2).get(x).intValue());
                }
            }

            datos.getMatrizPuntos().get(dia).put(Nodos_Con_Ahorro, PesosNormal);
            return PesosNosuperados;
        }
        return null;
    }

    @Override
    public void CrearListas() {
        PesosNormal.clear();
        PesosNosuperados.clear();
        for (int i = 0; i < 3; i++) {
            PesosNormal.add(new ArrayList<>());
            PesosNosuperados.add(new ArrayList<>());
        }
    }

    @Override
    public void VerificarNodosConRuta(HashMap<String, ArrayList<ArrayList<Double>>> matrizPuntos, int numeroProvedores, ArrayList<Double> Peso, ArrayList<Double> Volumen, String dia) {
        PesosNormal = matrizPuntos.get(Nodos_Con_Ahorro);
        PesosNosuperados = new ArrayList<>();
        boolean entre = true;
        int count = -1;
        if (PesosNormal != null) {
            if (PesosNormal.get(0).size() > 0) {
                for (int i = 1; i <= numeroProvedores; i++) {

                    for (int y = 0; y < PesosNormal.get(0).size(); y++) {
                        if (PesosNormal.get(0).get(y).intValue() == i || PesosNormal.get(1).get(y).intValue() == i) {
                            entre = false;
                            break;
                        } else {
                            entre = true;
                        }
                    }
                    if (entre) {
                        for (int z = 0; z < datos.getNodosSuperanVehiculo().get(dia).size(); z++) {
                            if (datos.getNodosSuperanVehiculo().get(dia).get(z) == i) {
                                entre = false;
                                break;
                            }
                        }
                        if (entre) {
                            PesosNosuperados.add(new ArrayList<>());
                            count += 1;
                            PesosNosuperados.get(count).add((double) i);
                            PesosNosuperados.get(count).add(Peso.get(i)); //Obtener el peso del punto
                            PesosNosuperados.get(count).add(Volumen.get(i)); // OBtener el Volumen del punto
                            //entre = false;  
                        }
                    }
                }

                /*
        Cuando un nodo tiene mas un punto
                 */
                Aux = VerificarCantidadNodos(PesosNormal, numeroProvedores, Peso, Volumen); // solo queda un nodo
                entre = true;
                for (int z = 0; z < Aux.size(); z++) {
                    for (int i = 0; i < PesosNormal.get(0).size(); i++) {
                        System.out.println(Aux.get(z).get(0).intValue() + " == " + PesosNormal.get(0).get(i).intValue() + " 0 " + Aux.get(z).get(0).intValue() + " == " + PesosNormal.get(1).get(i).intValue());
                        if (Aux.get(z).get(0).intValue() == PesosNormal.get(0).get(i).intValue() || Aux.get(z).get(0).intValue() == PesosNormal.get(1).get(i).intValue()) {
                            entre = false;
                            break;
                        } else {
                            entre = true;
                        }
                    }
                    if (entre) {
                        for (int p = 0; p < datos.getNodosSuperanVehiculo().get(dia).size(); p++) {
                            if (datos.getNodosSuperanVehiculo().get(dia).get(p) == Aux.get(z).get(0).intValue()) {
                                entre = false;
                                break;
                            }
                        }
                        if (entre) {
                            PesosNosuperados.add(Aux.get(z));
                        }
                    }
                }
                datos.getMatrizPuntos().get(dia).put(Nodos_Directos, PesosNosuperados);
                datos.getMatrizPuntos().get(dia).put(Nodos_Con_Ahorro, PesosNormal);
            } else {
                count = -1;
                for (int i = 1; i <= numeroProvedores; i++) {
                    if (SacarProvedoresPeso(datos.getPesoVolProvedores().get(dia).get(Constantes.Constantes.Demanda_kg).get(i), datos.getPesoVolProvedores().get(dia).get(Constantes.Constantes.Volumen).get(i), datos.getCargaMinima(), datos.getCapVolVehiculo().get(Constantes.Constantes.Volumen_Vehiculo), dia)) {
                        PesosNosuperados.add(new ArrayList<>());
                        count += 1;
                        PesosNosuperados.get(count).add((double) i);
                        PesosNosuperados.get(count).add(Peso.get(i)); //Obtener el peso del punto
                        PesosNosuperados.get(count).add(Volumen.get(i)); // OBtener el Volumen del punto
                    }

                }
                datos.getMatrizPuntos().get(dia).put(Nodos_Directos, PesosNosuperados);
                datos.getMatrizPuntos().get(dia).put(Nodos_Con_Ahorro, PesosNormal);
            }
        }

    }

    @Override
    public void ImprimirPuntos(ArrayList<ArrayList<Double>> CA, ArrayList<ArrayList<Double>> SinA, double[][] distancias,
            ArrayList<Double> Peso, ArrayList<Double> Volumen, String dia) {
        double resultadoSistemaPeso = 0.0;
        double resultadoSistemaDistancia = 0.0;
        double resultadosSistemaVolumen = 0.0;
        int count = 0;
        PesosNormal = new ArrayList<>();
        PesosNosuperados = new ArrayList<>();
        if (CA.size() > 0) {
            for (int i = 0; i < CA.get(0).size(); i++) {
//            PesosNormal.get(i).addAll();
                PesosNormal.add(new ArrayList<>());
                PesosNormal.get(i).add(CA.get(0).get(i)); // pos
                PesosNormal.get(i).add(CA.get(1).get(i)); // pos
                PesosNormal.get(i).add(CA.get(2).get(i)); // ahorro
                PesosNormal.get(i).add(CalcularPesoImprimir(Peso.get(CA.get(0).get(i).intValue()), Peso.get(CA.get(1).get(i).intValue()), 0));
                PesosNormal.get(i).add(CalcularVolumenImprimir(Volumen.get(CA.get(0).get(i).intValue()), Volumen.get(CA.get(1).get(i).intValue()), 0));
                PesosNormal.get(i).add(CalcularDistancia(distancias, CA.get(0).get(i).intValue(), CA.get(1).get(i).intValue()) + retornarDistanciaCero(CA.get(1).get(i).intValue()));
            }
        }
        if (SinA.size() > 0) {

            boolean entre;
            for (int i = 0; i < SinA.size(); i++) {
                entre = true;
                for (int z = 0; z < datos.getNodosSuperanVehiculo().get(dia).size(); z++) {
                    if (datos.getNodosSuperanVehiculo().get(dia).get(z) == SinA.get(i).get(0).intValue()) {
                        entre = false;
                        break;
                    }
                }
                if (entre) {
                    PesosNosuperados.add(new ArrayList<>());
                    PesosNosuperados.get(count).add(SinA.get(i).get(0)); // pos
                    PesosNosuperados.get(count).add(SinA.get(i).get(1)); // peso
                    PesosNosuperados.get(count).add(SinA.get(i).get(2)); // volumen
                    PesosNosuperados.get(count).add(CalcularDistancia(distancias, PesosNosuperados.get(count).get(0).intValue(), Integer.parseInt("0"))); // distancia
                    count += 1;
                }
            }
        }

        datos.getImprimirNodos().get(dia).put(Nodos_Con_Ahorro, PesosNormal);
        datos.getImprimirNodos().get(dia).put(Nodos_Directos, PesosNosuperados);

        for (int i = 0; i < datos.getImprimirNodos().get(dia).get(Nodos_Con_Ahorro).size(); i++) {
            System.out.println(
                    " Posicion " + (datos.getImprimirNodos().get(dia).get(Nodos_Con_Ahorro).get(i).get(0).intValue())
                    + " a " + datos.getImprimirNodos().get(dia).get(Nodos_Con_Ahorro).get(i).get(1).intValue()
                    + " Ahorro Nodos: " + datos.getImprimirNodos().get(dia).get(Nodos_Con_Ahorro).get(i).get(2)
                    + " Peso Nodos: " + datos.getImprimirNodos().get(dia).get(Nodos_Con_Ahorro).get(i).get(3).intValue()
                    + " Volumen Nodos: " + datos.getImprimirNodos().get(dia).get(Nodos_Con_Ahorro).get(i).get(4).intValue()
                    + " Distancia Nodos: " + datos.getImprimirNodos().get(dia).get(Nodos_Con_Ahorro).get(i).get(5).intValue()
            );
            resultadoSistemaPeso += datos.getImprimirNodos().get(dia).get(Nodos_Con_Ahorro).get(i).get(3);
            resultadoSistemaDistancia += datos.getImprimirNodos().get(dia).get(Nodos_Con_Ahorro).get(i).get(5);
            resultadosSistemaVolumen += datos.getImprimirNodos().get(dia).get(Nodos_Con_Ahorro).get(i).get(4);

        }

        for (int i = 0; i < datos.getImprimirNodos().get(dia).get(Nodos_Directos).size(); i++) {
            System.out.println(
                    " Posición Directa " + (datos.getImprimirNodos().get(dia).get(Nodos_Directos).get(i).get(0).intValue())
                    + " a posición 0 "
                    + " Peso Nodo: " + datos.getImprimirNodos().get(dia).get(Nodos_Directos).get(i).get(1).intValue()
                    + " Volumen Nodos: " + datos.getImprimirNodos().get(dia).get(Nodos_Directos).get(i).get(2).intValue()
                    + " Distancia Nodos: " + datos.getImprimirNodos().get(dia).get(Nodos_Directos).get(i).get(3).intValue()
            );
            resultadoSistemaPeso += datos.getImprimirNodos().get(dia).get(Nodos_Directos).get(i).get(1);
            resultadoSistemaDistancia += datos.getImprimirNodos().get(dia).get(Nodos_Directos).get(i).get(3);
            resultadosSistemaVolumen += datos.getImprimirNodos().get(dia).get(Nodos_Directos).get(i).get(2);
        }

        datos.getCargaTotalSistema().put(Constantes.Constantes.PesoTotalSistema, resultadoSistemaPeso);
        datos.getCargaTotalSistema().put(Constantes.Constantes.DistanciaTOtalSistema, resultadoSistemaDistancia);
        datos.getCargaTotalSistema().put(Constantes.Constantes.VolumenTotalSistema, resultadosSistemaVolumen);
    }

    @Override
    public double CalcularPesoImprimir(double PuntoX, double PuntoY, double CapVehiculo) {
        double resul = 0;
        resul = PuntoX + PuntoY;
        return resul;
    }

    @Override
    public double CalcularVolumenImprimir(double PuntoX, double PuntoY, double CapVolumen) {
        double resul = 0;
        resul = PuntoX + PuntoY;
        return resul;
    }

    @Override
    public double CalcularDistancia(double[][] distancia, int PuntoX, int PuntoY) {
        int I = (int) PuntoX;
        int J = (int) PuntoY;
        return distancia[I][J];
    }

    private ArrayList<ArrayList<Double>> VerificarCantidadNodos(ArrayList<ArrayList<Double>> PesosNormall, int numeroProvedores, ArrayList<Double> Peso, ArrayList<Double> Volumen) {
        ArrayList<Integer> countX = new ArrayList<>();
        ArrayList<Integer> countY = new ArrayList<>();
        ArrayList<ArrayList<Double>> directo = new ArrayList<>();
        ArrayList<ArrayList<Double>> auxV = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            auxV.add(new ArrayList<>());
        }
        int contar = -1;
        for (int i = 1; i <= numeroProvedores; i++) {
            for (int y = 0; y < PesosNormall.get(0).size(); y++) {
                if (PesosNormall.get(0).get(y).intValue() == i) {
                    countX.add(y);
                }
                if (PesosNormall.get(1).get(y).intValue() == i) {
                    countY.add(y);
                }
            }
            if (countX.size() > 1) {
                double result = 0;
                int pos = 0;
                for (int b = 0; b < countX.size(); b++) {
                    if (b == 0) {
                        result = PesosNormall.get(2).get(countX.get(b));
                        pos = b;
                    }
                    if (PesosNormall.get(2).get(countX.get(b)) > result) {
                        result = PesosNormall.get(2).get(countX.get(b));
                        pos = countX.get(b);
                    }
                }
                for (int b = 0; b < countX.size(); b++) {
                    if (b != pos) {
                        directo.add(new ArrayList<>());
                        contar += 1;
                        directo.get(contar).add(PesosNormall.get(1).get(countX.get(b)));
                        directo.get(contar).add(Peso.get(PesosNormall.get(1).get(countX.get(b)).intValue()));
                        directo.get(contar).add(Volumen.get(PesosNormall.get(1).get(countX.get(b)).intValue()));
                    }
                }
                for (int b = 0; b < countX.size(); b++) {
                    if (b != pos) {
                        for (int z = 0; z < 3; z++) {
                            PesosNormall.get(z).set(countX.get(b), 0.0);
                            // auxV.get(z).add(PesosNormall.get(z).get(pos));
                        }
                    }
                }
            }
            if (countY.size() > 1) {
                double result = 0;
                int pos = 0;
                for (int b = 0; b < countY.size(); b++) {
                    if (b == 0) {
                        result = PesosNormall.get(2).get(countY.get(b));
                        pos = countY.get(b);
                    }
                    if (PesosNormall.get(2).get(countY.get(b)) > result) {
                        result = PesosNormall.get(2).get(countY.get(b));
                        pos = countY.get(b);
                    }
                }
                for (int b = 0; b < countY.size(); b++) {
                    if (countY.get(b).intValue() != pos) {
                        directo.add(new ArrayList<>());
                        contar += 1;
                        directo.get(contar).add(PesosNormall.get(0).get(countY.get(b)));
                        directo.get(contar).add(Peso.get(PesosNormall.get(0).get(countY.get(b)).intValue()));
                        directo.get(contar).add(Volumen.get(PesosNormall.get(0).get(countY.get(b)).intValue()));
                    }
                }
                for (int b = 0; b < countY.size(); b++) {
                    if (countY.get(b).intValue() != pos) {
                        for (int z = 0; z < 3; z++) {
                            PesosNormall.get(z).set(countY.get(b), 0.0);
                            // auxV.get(z).add(PesosNormall.get(z).get(pos));
                        }
                    }
                }

                countX = new ArrayList<>();
                countY = new ArrayList<>();
            } else {
                countX = new ArrayList<>();
                countY = new ArrayList<>();
            }
        }

        for (int y = 0; y < PesosNormall.get(0).size(); y++) {
            if (PesosNormall.get(0).get(y) == 0.0 || PesosNormall.get(1).get(y) == 0.0 || PesosNormall.get(2).get(y) == 0.0) {
                PesosNormall.get(0).remove(y);
                PesosNormall.get(1).remove(y);
                PesosNormall.get(2).remove(y);
                y = 0;
            }
        }

        for (int y = 0; y < PesosNormall.get(0).size(); y++) {
            if (PesosNormall.get(0).get(y) == 0.0 || PesosNormall.get(1).get(y) == 0.0 || PesosNormall.get(2).get(y) == 0.0) {
                PesosNormall.get(0).remove(y);
                PesosNormall.get(1).remove(y);
                PesosNormall.get(2).remove(y);
                y = 0;
            }
        }

        for (int y = 0; y < directo.size(); y++) {
            if (directo.get(y).get(0) == 0.0) {
                directo.remove(y);
                y = 0;
            }
        }

        PesosNormal = PesosNormall;
        return directo;
    }

    public double retornarDistanciaCero(int pos) {
        return datos.getDistancias()[0][pos];
    }

    private void ImprimirProvesoresCargaMinima(HashMap<String, ArrayList<Integer>> distanciasNoEvaluar, String dia) {
        if (datos.getDistanciasNoEvaluar().containsKey(dia)) {
            for (int i = 0; i < distanciasNoEvaluar.get(dia).size(); i++) {
                System.out.println("Puntos No evaluar son: " + distanciasNoEvaluar.get(dia).get(i) + " Carga por debajo del minimo");
            }
        }

    }

    private boolean SacarProvedoresPeso(Double get, Double volumen, double cargaMinima, Double volumenVehiculo, String dia) {
        if (get < cargaMinima && volumen < volumenVehiculo) {
            return false;
        }
        return true;
    }

    private void GuardarCargasMinimas(int x, String dia) {
        boolean tmp = false;
        if (datos.getDistanciasNoEvaluar().containsKey(dia)) {
            for (int i = 0; i < datos.getDistanciasNoEvaluar().get(dia).size(); i++) {
                if (datos.getDistanciasNoEvaluar().get(dia).get(i) == x) {
                    tmp = false;
                    break;
                } else {
                    tmp = true;
                }
            }
            if (tmp) {
                datos.getDistanciasNoEvaluar().get(dia).add(x);
            }
        } else {
            datos.getDistanciasNoEvaluar().put(dia, new ArrayList<>());
            datos.getDistanciasNoEvaluar().get(dia).add(x);
        }
    }

    private void CambiarNodos(ArrayList<ArrayList<Double>> copiaOrigina, String dia, double porRuta) {
        int distanciaOriginal = 0;
        int distanciaPrevia = 0;
        int distanciaNuevaruta = 0;
        double[][] copiaOriginal = new double[copiaOrigina.size()][];
        double[][] rutas = new double[copiaOrigina.size()][];
        boolean entreRutas = false;
        ArrayList<ArrayList<Double>> bloqueo = new ArrayList<>();
        for (int i = 0; i < copiaOrigina.size(); i++) {
            copiaOriginal[i] = new double[copiaOrigina.get(i).size()];
            for (int j = 0; j < copiaOrigina.get(i).size(); j++) {
                copiaOriginal[i][j] = copiaOrigina.get(i).get(j);
            }
        }
        distanciaOriginal = onValorDistanciaTotal(copiaOrigina);
        distanciaNuevaruta = distanciaOriginal;
        double[][] copiaPuntos = copiaOriginal;
        try {
            Random random = new Random();
            int valor = (int) Math.floor(Math.random() * 50 + 1);
            for (int i = 0; i < 1000; i++) {
                ArrayList<ArrayList<Double>> tmpCopia = new ArrayList<>();
                int numero = random.nextInt(copiaPuntos.length - 1);
                if (copiaPuntos[numero].length == 0 || numero < 0 || (copiaPuntos[numero].length) >= porRuta || evaluarBloqueoPunto(bloqueo, numero)) {
                    numero = cambioNumero(copiaPuntos, numero, -1);
                    if (numero == -1) {
                        break;
                    }
//                    while ((copiaPuntos[numero].length) >= porRuta || evaluarBloqueoPunto(bloqueo, numero)) {
//                        numero = cambioNumero(copiaPuntos, numero, -1);
//                    }
                }
                //bloqueo = bloqueoPuntos(numero, -1, bloqueo);
                int numero2 = random.nextInt(copiaPuntos.length - 1);
                if (numero == numero2 || copiaPuntos[numero2].length == 0 || numero2 < 0 || evaluarBloqueoPunto(bloqueo, numero2)) {
                    numero2 = cambioNumero(copiaPuntos, numero2, numero);
                    if (numero2 == -1) {
                        break;
                    }
                }
                //bloqueo = bloqueoPuntos(numero2, -1, bloqueo);
                for (int y = 0; y < copiaPuntos.length; y++) {
                    tmpCopia.add(new ArrayList<>());
                    for (int z = 0; z < copiaPuntos[y].length; z++) {
                        if (copiaPuntos[y].length == 1) {
                            tmpCopia.get(y).add(copiaPuntos[y][0]);
                            break;
                        } else {
                            tmpCopia.get(y).add(copiaPuntos[y][z]);
                        }
                    }
                }
                int pos = tmpCopia.get(numero2).size() - 1;
                int numeroRevolver = 0;
                if (pos > 0) {
                    numeroRevolver = random.nextInt(pos);
                } else {
                    numeroRevolver = pos;
                }
                if (numeroRevolver >= 0) {
                    double numerosacar = tmpCopia.get(numero2).get(numeroRevolver);
                    tmpCopia.get(numero2).remove(numeroRevolver);
                    tmpCopia.get(numero).add(numerosacar);
                    //bloqueo = bloqueoPuntos(-1, 1, bloqueo);
                    //verificar volumen y carga
                    ArrayList<Double> organizar = null;
                    if (verificacionVolumenCarga(tmpCopia.get(numero), dia)) {
                        if (tmpCopia.get(numero).size() >= 2) {
                            //organizar puntos
                            organizar = organizarPuntos(tmpCopia.get(numero));
                            if (organizar != null) {
                                tmpCopia.set(numero, organizar);
                            }
                        }
                        if (organizar != null) {
                            distanciaPrevia = onValorDistanciaTotal(tmpCopia);
                            if (distanciaPrevia < distanciaNuevaruta) {
                                distanciaNuevaruta = distanciaPrevia;
                                entreRutas = true;
                                for (int p = 0; p < tmpCopia.size(); p++) {
                                    rutas[p] = new double[tmpCopia.get(p).size()];
                                    for (int t = 0; t < tmpCopia.get(p).size(); t++) {
                                        rutas[p][t] = tmpCopia.get(p).get(t);
                                    }
                                }
                                copiaPuntos = rutas;
                            } else {
                                distanciaPrevia = distanciaNuevaruta;
                            }
                        }
                    }
                }
            }

            ArrayList<ArrayList<Double>> tmpCopia = new ArrayList<>();
            ArrayList<ArrayList<Double>> tmpCopiaFinal = new ArrayList<>();
            ArrayList<Double> volumen = new ArrayList<>();
            ArrayList<Double> carga = new ArrayList<>();
            ArrayList<Double> distancia = new ArrayList<>();
            datos.getResultadosFinales().put(dia, new HashMap());
            datos.getResultadosFinales().get(dia).put("volumen", new ArrayList<>());
            datos.getResultadosFinales().get(dia).put("carga", new ArrayList<>());
            datos.getResultadosFinales().get(dia).put("distancia", new ArrayList<>());
            if (entreRutas) {
                for (int y = 0; y < rutas.length; y++) {
                    tmpCopia.add(new ArrayList<>());
                    for (int z = 0; z < rutas[y].length; z++) {
                        if (rutas[y].length == 1) {
                            tmpCopia.get(y).add(rutas[y][0]);
                            break;
                        } else {
                            tmpCopia.get(y).add(rutas[y][z]);
                        }
                    }
                }
            } else {
                tmpCopia = copiaOrigina;
            }
            for (int i = 0; i < tmpCopia.size(); i++) {
                if (tmpCopia.get(i).size() > 1) {
                    tmpCopia.set(i, organizarPuntos(tmpCopia.get(i)));
                }
            }

            for (int i = 0; i < tmpCopia.size(); i++) {
                if (tmpCopia.get(i).isEmpty()) {
                    tmpCopia.remove(i);
                    i = 0;
                }
            }
            for (int i = 0; i < tmpCopia.size(); i++) {
                if (tmpCopia.get(i).size() == 1) {
                    tmpCopiaFinal.add(tmpCopia.get(i));
                }
            }
            for (int i = 0; i < tmpCopia.size(); i++) {
                if (tmpCopia.get(i).size() != 1) {
                    tmpCopiaFinal.add(tmpCopia.get(i));
                }
            }
            tmpCopia = (ArrayList<ArrayList<Double>>) tmpCopiaFinal.clone();
            int grupos = 0;
            for (int i = 0; i < tmpCopia.size(); i++) {
                datos.getResultadosFinales().get(dia).put("grupo" + (i + 1), tmpCopia.get(i));
                grupos = i;
            }
            for (int i = 0; i < tmpCopia.size(); i++) {
                volumen.add(onObtenerVolumenFinalGrupo(tmpCopia.get(i), dia));
            }
            datos.getResultadosFinales().get(dia).put("volumen", volumen);
            for (int i = 0; i < tmpCopia.size(); i++) {
                carga.add(onObtenerCargaFinalGrupo(tmpCopia.get(i), dia));
            }
            datos.getResultadosFinales().get(dia).put("carga", carga);
            for (int i = 0; i < tmpCopia.size(); i++) {
                distancia.add(onValorDistanciaGruposProvicional(tmpCopia.get(i)));
            }
            datos.getResultadosFinales().get(dia).put("distancia", distancia);
            double distanciaTotaldia = 0;
            double cargatotaldia = 0;
            double volumentotaldia = 0;
            for (int i = 0; i < datos.getResultadosFinales().get(dia).get("distancia").size(); i++) {
                distanciaTotaldia += datos.getResultadosFinales().get(dia).get("distancia").get(i);
                cargatotaldia += datos.getResultadosFinales().get(dia).get("carga").get(i);
                volumentotaldia += datos.getResultadosFinales().get(dia).get("volumen").get(i);
            }
            ArrayList<Double> valoresFinales = new ArrayList<>();
            valoresFinales.add(distanciaTotaldia);
            valoresFinales.add(cargatotaldia);
            valoresFinales.add(volumentotaldia);
            valoresFinales.add((double) grupos);
            datos.getResultadosFinales().get(dia).put("valoresFinales", valoresFinales);
            //falta armar excel
            System.out.print(distanciaPrevia + " " + distanciaOriginal);
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }

    }

    private int onValorDistanciaTotal(ArrayList<ArrayList<Double>> copiaOriginal) {
        int distanciaOriginal = 0;
        for (int i = 0; i < copiaOriginal.size(); i++) {
            double tmp = 0.0;
            for (int j = 0; j < copiaOriginal.get(i).size(); j++) {
                if (datos.getDistancias().length != datos.getDistanciasProvicional().get(dias).length) {
                    if (copiaOriginal.get(i).size() > (j + 1)) {
                        tmp += CalcularDistancia(datos.getDistanciasProvicional().get(dias), (int) copiaOriginal.get(i).get(j).intValue(), (int) copiaOriginal.get(i).get(j + 1).intValue());
                    } else {
                        tmp += CalcularDistancia(datos.getDistanciasProvicional().get(dias), (int) copiaOriginal.get(i).get(j).intValue(), 0);
                    }
                } else {
                    if (copiaOriginal.get(i).size() > (j + 1)) {
                        tmp += CalcularDistancia(datos.getDistancias(), (int) copiaOriginal.get(i).get(j).intValue(), (int) copiaOriginal.get(i).get(j + 1).intValue());
                    } else {
                        tmp += CalcularDistancia(datos.getDistancias(), (int) copiaOriginal.get(i).get(j).intValue(), 0);
                    }
                }

            }
            distanciaOriginal += tmp;
        }
        return distanciaOriginal;
    }

    private int cambioNumero(double[][] copiaPuntos, int numero, int numero2) {
        Random random = new Random();
        int cont = 0;
        while (numero == numero2 || copiaPuntos[numero].length == 0 || numero < 0 || copiaPuntos[numero].length >= (int) datos.getPorRuta()) {
            numero = random.nextInt(copiaPuntos.length - 1);
            if (cont < 100) {
                cont++;
            } else {
                numero = -1;
                break;
            }
        }
        return numero;
    }

    private ArrayList<Double> organizarPuntos(ArrayList<Double> grupo) {
        double distancia = onValorDistanciaGrupos(grupo);
        ArrayList<Double> nuevoGrupo = new ArrayList<>();
        nuevoGrupo = (ArrayList) grupo.clone();
        Random random = new Random();
        boolean entre = false;
        double kilometrosdesviar = onPuntomasLejadoOrigen(grupo);
        if (grupo.size() == 2) {
            double aux = 0D;
            aux = grupo.get(0);
            grupo.set(0, grupo.get(1));
            grupo.set(1, aux);
            double tmp = onValorDistanciaGrupos(grupo);
            if (tmp < kilometrosdesviar) {
                entre = true;
                if (tmp < distancia) {
                    nuevoGrupo = (ArrayList) grupo.clone();
                }
            }
        } else {
            kilometrosdesviar = onPuntomasLejadoOrigen(grupo);
            int valor = (int) Math.floor(Math.random() * (grupo.size() * 2) + 1);
            for (int i = 0; i < 50; i++) {
                int numero = random.nextInt(grupo.size());
                double obtenerNumero = grupo.get(numero);
                int numero2 = random.nextInt(grupo.size());
                while (numero == numero2) {
                    numero2 = random.nextInt(grupo.size());
                }
                double obtenerNumero2 = grupo.get(numero2);
                grupo.set(numero, obtenerNumero2);
                grupo.set(numero2, obtenerNumero);
                double tmp = onValorDistanciaGrupos(grupo);
                if (tmp < kilometrosdesviar) {
                    entre = true;
                    if (tmp < distancia) {
                        nuevoGrupo = (ArrayList) grupo.clone();
                        distancia = tmp;
                    }
                }
            }
        }
        if (grupo.size() != 2) {
            if (!(entre == true)) {
                nuevoGrupo = null;
            } else {
                nuevoGrupo = grupo;
            }
        }

        return nuevoGrupo;
    }

    private double onValorDistanciaGrupos(ArrayList<Double> grupo) {
        double tmp = 0.0;
        for (int j = 0; j < grupo.size(); j++) {
            if (datos.getDistancias().length != datos.getDistanciasProvicional().get(dias).length) {
                if (grupo.size() > (j + 1)) {
                    tmp += CalcularDistancia(datos.getDistanciasProvicional().get(dias), (int) grupo.get(j).intValue(), (int) grupo.get(j + 1).intValue());
                } else {
                    tmp += CalcularDistancia(datos.getDistanciasProvicional().get(dias), (int) grupo.get(j).intValue(), 0);
                }
            } else {
                if (grupo.size() > (j + 1)) {
                    tmp += CalcularDistancia(datos.getDistancias(), (int) grupo.get(j).intValue(), (int) grupo.get(j + 1).intValue());
                } else {
                    tmp += CalcularDistancia(datos.getDistancias(), (int) grupo.get(j).intValue(), 0);
                }
            }

        }
        return tmp;
    }

    private double onValorDistanciaGruposProvicional(ArrayList<Double> grupo) {
        double tmp = 0.0;
        for (int j = 0; j < grupo.size(); j++) {
            if (datos.getDistancias().length != datos.getDistanciasProvicional().get(dias).length) {
                if (grupo.size() > (j + 1)) {
                    tmp += CalcularDistancia(datos.getDistanciasProvicional().get(dias), (int) grupo.get(j).intValue(), (int) grupo.get(j + 1).intValue());
                } else {
                    tmp += CalcularDistancia(datos.getDistanciasProvicional().get(dias), (int) grupo.get(j).intValue(), 0);
                }
            } else {
                if (grupo.size() > (j + 1)) {
                    tmp += CalcularDistancia(datos.getDistancias(), (int) grupo.get(j).intValue(), (int) grupo.get(j + 1).intValue());
                } else {
                    tmp += CalcularDistancia(datos.getDistancias(), (int) grupo.get(j).intValue(), 0);
                }
            }
        }
        return tmp;
    }

    private boolean verificacionVolumenCarga(List<Double> grupo, String dia) {
        double pesoGrupo = 0;
        double volumenGrupo = 0;
        double pesoVehiculo = datos.getCapVolVehiculo().get(Constantes.Constantes.Capacidad_Vehiculo);
        double volumenVehiculo = datos.getCapVolVehiculo().get(Constantes.Constantes.Volumen_Vehiculo);
        for (int i = 0; i < grupo.size(); i++) {
            pesoGrupo += datos.getPesoVolProvedores().get(dia).get(Constantes.Constantes.Demanda_kg).get(grupo.get(i).intValue());
            volumenGrupo += datos.getPesoVolProvedores().get(dia).get(Constantes.Constantes.Volumen).get(grupo.get(i).intValue());
        }
        if (pesoGrupo < pesoVehiculo && volumenGrupo < volumenVehiculo) {
            return true;
        } else {
            return false;
        }
    }

    private double onObtenerVolumenFinalGrupo(List<Double> grupo, String dia) {
        double volumenGrupo = 0;
        for (int i = 0; i < grupo.size(); i++) {
            volumenGrupo += datos.getPesoVolProvedores().get(dia).get(Constantes.Constantes.Volumen).get(grupo.get(i).intValue());
        }
        return volumenGrupo;
    }

    private double onObtenerCargaFinalGrupo(List<Double> grupo, String dia) {
        double CargaGrupo = 0;
        for (int i = 0; i < grupo.size(); i++) {
            CargaGrupo += datos.getPesoVolProvedores().get(dia).get(Constantes.Constantes.Demanda_kg).get(grupo.get(i).intValue());
        }
        return CargaGrupo;
    }

    private ArrayList<ArrayList<Double>> CrearArreglo(ArrayList<ArrayList<Double>> nodosAhorro, ArrayList<ArrayList<Double>> superanCapacidad, ArrayList<ArrayList<Double>> nodosDirectos, String dia) {
        ArrayList<ArrayList<Double>> copiaOriginal = new ArrayList<>();
        for (int i = 0; i < nodosDirectos.size(); i++) {
            double tmp = nodosDirectos.get(i).get(0).doubleValue();
            if ((i + 1) < nodosDirectos.size()) {
                for (int j = (i + 1); j < nodosDirectos.size(); j++) {
                    if (tmp == nodosDirectos.get(j).get(0).doubleValue()) {
                        nodosDirectos.remove(j);
                        break;
                    }
                }
            } else {
                break;
            }
        }

        for (int i = 0; i < nodosAhorro.get(0).size(); i++) {
            double tmp = nodosAhorro.get(0).get(i);
            for (int j = 0; j < nodosAhorro.get(1).size(); j++) {
                if (tmp == nodosAhorro.get(1).get(j)) {
                    if (nodosAhorro.get(2).get(i) < nodosAhorro.get(2).get(j)) {
                        ArrayList<Double> tmp2 = new ArrayList<>();
                        ArrayList<Double> tmp3 = new ArrayList<>();
                        tmp2.add(nodosAhorro.get(0).get(j));
                        tmp2.add(datos.getPesoVolProvedores().get(dia).get(Constantes.Constantes.Demanda_kg).get(nodosAhorro.get(0).get(j).intValue()));
                        tmp2.add(datos.getPesoVolProvedores().get(dia).get(Constantes.Constantes.Volumen).get(nodosAhorro.get(0).get(j).intValue()));
                        tmp3.add(nodosAhorro.get(1).get(j));
                        tmp3.add(datos.getPesoVolProvedores().get(dia).get(Constantes.Constantes.Demanda_kg).get(nodosAhorro.get(1).get(j).intValue()));
                        tmp3.add(datos.getPesoVolProvedores().get(dia).get(Constantes.Constantes.Volumen).get(nodosAhorro.get(1).get(j).intValue()));
                        nodosDirectos.add(tmp2);
                        nodosDirectos.add(tmp3);
                        nodosAhorro.get(0).remove(j);
                        nodosAhorro.get(1).remove(j);
                        nodosAhorro.get(2).remove(j);
                    } else {
                        ArrayList<Double> tmp2 = new ArrayList<>();
                        ArrayList<Double> tmp3 = new ArrayList<>();
                        tmp2.add(nodosAhorro.get(0).get(i));
                        tmp2.add(datos.getPesoVolProvedores().get(dia).get(Constantes.Constantes.Demanda_kg).get(nodosAhorro.get(0).get(i).intValue()));
                        tmp2.add(datos.getPesoVolProvedores().get(dia).get(Constantes.Constantes.Volumen).get(nodosAhorro.get(0).get(i).intValue()));
                        tmp3.add(nodosAhorro.get(1).get(i));
                        tmp3.add(datos.getPesoVolProvedores().get(dia).get(Constantes.Constantes.Demanda_kg).get(nodosAhorro.get(1).get(i).intValue()));
                        tmp3.add(datos.getPesoVolProvedores().get(dia).get(Constantes.Constantes.Volumen).get(nodosAhorro.get(1).get(i).intValue()));
                        nodosDirectos.add(tmp2);
                        nodosDirectos.add(tmp3);
                        nodosAhorro.get(0).remove(i);
                        nodosAhorro.get(1).remove(i);
                        nodosAhorro.get(2).remove(i);
                    }
                    break;
                }
            }
        }

        try {
            for (int i = 0; i < nodosDirectos.size(); i++) {
                if (nodosDirectos.get(i).size() != 3) {
                    nodosDirectos.get(i).add(datos.getPesoVolProvedores().get(dia).get(Constantes.Constantes.Demanda_kg).get(nodosDirectos.get(i).get(0).intValue()));
                    nodosDirectos.get(i).add(datos.getPesoVolProvedores().get(dia).get(Constantes.Constantes.Volumen).get(nodosDirectos.get(i).get(0).intValue()));
                }
                if (evaluarPuntonodosAhorro(nodosDirectos.get(i).get(0), nodosAhorro)) {
                    nodosDirectos.remove(i);
                    i = 0;
                }
            }
            for (int i = 0; i < nodosDirectos.size(); i++) {
                for (int j = (i + 1); j < nodosDirectos.size(); j++) {
                    if (Objects.equals(nodosDirectos.get(i).get(0), nodosDirectos.get(j).get(0))) {
                        nodosDirectos.remove(j);
                        i = 0;
                        break;
                    }
                }
            }
        } catch (Exception e) {
            System.err.print("Problema nodosDirector ".concat(e.getMessage()));
        }

        for (int i = 0; i < nodosAhorro.get(0).size(); i++) {
            ArrayList<Double> tmp = new ArrayList<>();
            tmp.add(nodosAhorro.get(0).get(i));
            tmp.add(nodosAhorro.get(1).get(i));
            copiaOriginal.add(tmp);
        }
        ArrayList<ArrayList<Double>> tmp3 = new ArrayList<>();
        int cont = 0;
        for (int i = 0; i < datos.getNodosSuperanVehiculo().get(dia).size(); i++) {
            double valorProvedorCarga = datos.getPesoVolProvedores().get(dia).get(Constantes.Constantes.Demanda_kg).get(datos.getNodosSuperanVehiculo().get(dia).get(i));
            double valorProvedorVolumen = datos.getPesoVolProvedores().get(dia).get(Constantes.Constantes.Volumen).get(datos.getNodosSuperanVehiculo().get(dia).get(i));
            int cantidadVehiculos = (int) Math.ceil(valorProvedorCarga / datos.getCapVolVehiculo().get(Constantes.Constantes.Capacidad_Vehiculo));
            int cantidadVehiculoVolumen = (int) Math.ceil(valorProvedorVolumen / datos.getCapVolVehiculo().get(Constantes.Constantes.Volumen_Vehiculo));
            if (!(cantidadVehiculos > cantidadVehiculoVolumen)) {
                cantidadVehiculos = cantidadVehiculoVolumen;
            }
            for (int j = 0; j < cantidadVehiculos; j++) {
                ArrayList<Double> tmp2 = new ArrayList<>();
                datos.getPesoVolProvedores().get(dia).get(Constantes.Constantes.Demanda_kg).add(valorProvedorCarga / cantidadVehiculos);
                datos.getPesoVolProvedores().get(dia).get(Constantes.Constantes.Volumen).add(valorProvedorVolumen / cantidadVehiculos);
                tmp2.add((double) (datos.getPesoVolProvedores().get(dia).get(Constantes.Constantes.Demanda_kg).size() - 1));
                ArrayList<ArrayList<Double>> tmpCopia = new ArrayList<>();
                for (int y = 0; y < datos.getDistanciasProvicional().get(dias).length; y++) {
                    tmpCopia.add(new ArrayList<>());
                    for (int z = 0; z < datos.getDistanciasProvicional().get(dias)[y].length; z++) {
                        if (datos.getDistanciasProvicional().get(dias)[y].length == 1) {
                            tmpCopia.get(y).add(datos.getDistanciasProvicional().get(dias)[y][0]);
                            break;
                        } else {
                            tmpCopia.get(y).add(datos.getDistanciasProvicional().get(dias)[y][z]);
                        }
                    }
                }
                for (int p = 0; p < tmpCopia.size(); p++) {
                    if (tmp2.get(0) != p) {
                        double tmp = tmpCopia.get(p).get(datos.getNodosSuperanVehiculo().get(dia).get(i));
                        tmpCopia.get(p).add(tmp);
                    } else {
                        tmpCopia.get(p).add(0D);
                    }
                }
                tmpCopia.add(tmpCopia.get(datos.getNodosSuperanVehiculo().get(dia).get(i)));
                double[][] tmp = new double[tmpCopia.size()][tmpCopia.size()];
                for (int p = 0; p < tmpCopia.size(); p++) {
                    tmp[p] = new double[tmpCopia.get(p).size()];
                    for (int t = 0; t < tmpCopia.get(p).size(); t++) {
                        tmp[p][t] = tmpCopia.get(p).get(t);
                    }
                }
                datos.getDistanciasProvicional().remove(dias);
                datos.getDistanciasProvicional().put(dias, tmp);
                tmp2.add(datos.getPesoVolProvedores().get(dia).get(Constantes.Constantes.Demanda_kg).get(tmp2.get(0).intValue()));
                tmp2.add(datos.getPesoVolProvedores().get(dia).get(Constantes.Constantes.Volumen).get(tmp2.get(0).intValue()));
                nodosDirectos.add(tmp2);
                tmp3.add(new ArrayList<>());
                tmp3.get(cont).add(tmp2.get(0));
                tmp3.get(cont).add(datos.getNodosSuperanVehiculo().get(dia).get(i).doubleValue());
                tmp3.get(cont).add((double) cantidadVehiculos);
                cont++;
            }

        }
        datos.getPuntosDivicion().put(dia, tmp3);
        ArrayList<Double> centroC = new ArrayList<>();
        for (int i = 0; i < nodosDirectos.size(); i++) {
            ArrayList<Double> tmp = new ArrayList<>();
            if (nodosDirectos.get(i).get(1) != 0 && nodosDirectos.get(i).get(2) != 0) {
                if (nodosDirectos.get(i).get(0) < datos.getDistancias().length) {
                    if (nodosDirectos.get(i).get(1) >= datos.getCargaMinima()) {
                        tmp.add(nodosDirectos.get(i).get(0));
                        copiaOriginal.add(tmp);
                    } else {
                        centroC.add(nodosDirectos.get(i).get(0));
                    }
                } else {
                    tmp.add(nodosDirectos.get(i).get(0));
                    copiaOriginal.add(tmp);
                }
            }
        }

        datos.getCentroConsolidacion().put(dia, centroC);

        return copiaOriginal;
    }

    private boolean evaluarPuntonodosAhorro(Double remove, ArrayList<ArrayList<Double>> nodosAhorro) {
        boolean entrei = true;
        for (int i = 0; i < nodosAhorro.get(0).size(); i++) {
            if (Objects.equals(nodosAhorro.get(0).get(i), remove) || Objects.equals(nodosAhorro.get(1).get(i), remove)) {
                return true;
            }
        }
        return false;
    }

    private double onPuntomasLejadoOrigen(ArrayList<Double> grupo) {
        double tmp = 0;
        for (int i = 0; i < grupo.size(); i++) {
            if (datos.getDistancias().length != datos.getDistanciasProvicional().get(dias).length) {
                if (tmp < CalcularDistancia(datos.getDistanciasProvicional().get(dias), (int) grupo.get(i).intValue(), 0)) {
                    tmp = CalcularDistancia(datos.getDistanciasProvicional().get(dias), (int) grupo.get(i).intValue(), 0);
                }
            } else {
                if (tmp < CalcularDistancia(datos.getDistancias(), (int) grupo.get(i).intValue(), 0)) {
                    tmp = CalcularDistancia(datos.getDistancias(), (int) grupo.get(i).intValue(), 0);
                }
            }

        }
        return tmp + datos.getKilometrosDesviar();
    }

    public static ArrayList<ArrayList<Double>> getPuntosOriginal() {
        return puntosOriginal;
    }

    public static void setPuntosOriginal(ArrayList<ArrayList<Double>> puntosOriginal) {
        MetodosCalculoDistanciaImp.puntosOriginal = puntosOriginal;
    }

    private ArrayList<ArrayList<Double>> bloqueoPuntos(int numero, int iteracion, ArrayList<ArrayList<Double>> bloqueo) {
        if (numero == -1) {
            for (int i = 0; i < bloqueo.size(); i++) {
                if (bloqueo.get(i).get(1).intValue() == 1) {
                    bloqueo.remove(i);
                } else {
                    bloqueo.get(i).set(1, (bloqueo.get(i).get(1) + 1D));
                }
            }
        }
        if (iteracion == -1) {
            bloqueo.add(new ArrayList<>());
            bloqueo.get(bloqueo.size() - 1).add((double) numero);
            bloqueo.get(bloqueo.size() - 1).add(0D);
        }

        return bloqueo;
    }

    private boolean evaluarBloqueoPunto(ArrayList<ArrayList<Double>> bloqueo, int numero) {
        for (int i = 0; i < bloqueo.size(); i++) {
            if (bloqueo.get(i).get(0).intValue() == numero) {
                return true;
            }
        }
        return false;
    }

}
