/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Strategia;

import java.util.ArrayList;


/**
 *
 * @author cesard.chacond
 */
public abstract class MetodosCalculoDistancia implements MetodosCalculo, Constantes.Constantes{
    protected boolean CAB = true;
    protected boolean SinAB = true;
    protected ArrayList<ArrayList<Double>> PesosNosuperados; // SinA
    protected ArrayList<ArrayList<Double>> PesosNormal; // CA
    protected ArrayList<ArrayList<Double>> Aux; // auxiliar
    
    public boolean isCAB() {
        return CAB;
    }

    public void setCAB(boolean CAB) {
        this.CAB = CAB;
    }

    public boolean isSinAB() {
        return SinAB;
    }

    public void setSinAB(boolean SinAB) {
        this.SinAB = SinAB;
    }

    public ArrayList<ArrayList<Double>> getPesosNosuperados() {
        return PesosNosuperados;
    }

    public void setPesosNosuperados(ArrayList<ArrayList<Double>> PesosNosuperados) {
        this.PesosNosuperados = PesosNosuperados;
    }

    public ArrayList<ArrayList<Double>> getPesosNormal() {
        return PesosNormal;
    }

    public void setPesosNormal(ArrayList<ArrayList<Double>> PesosNormal) {
        this.PesosNormal = PesosNormal;
    }

    public ArrayList<ArrayList<Double>> getAux() {
        return Aux;
    }

    public void setAux(ArrayList<ArrayList<Double>> Aux) {
        this.Aux = Aux;
    }
    
    
}
