/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Exception;

import Variables.AtributosSistema;
import Constantes.ExceptionCrearExcel;
import javax.swing.JOptionPane;
import Constantes.ExceptionLeerExcel;

/**
 *
 * @author David
 */
public class NewExceptionExcel extends ExceptionSistema {

    public NewExceptionExcel() {
    }

    public void LeerExcel() {
        switch (AtributosSistema.getInstance().getError()) {
            case "1": {
                JOptionPane.showMessageDialog(null, ExceptionLeerExcel.ErrorLectura, "Información", JOptionPane.ERROR_MESSAGE);
                break;
            }
            case "2": {
                JOptionPane.showMessageDialog(null, ExceptionLeerExcel.ErrorCargaDatos, "Información", JOptionPane.ERROR_MESSAGE);
                break;
            }
            case "3": {
                JOptionPane.showMessageDialog(null, ExceptionLeerExcel.ErrorCerrarExcel, "Información", JOptionPane.ERROR_MESSAGE);
                break;
            }
        }
    }

    public void CrearExcel(String option) {
        switch (option) {
            case "1": {
                JOptionPane.showMessageDialog(null, ExceptionCrearExcel.ErrorCrear, "Información", JOptionPane.ERROR_MESSAGE);
                break;
            }
            case "2": {
                JOptionPane.showMessageDialog(null, ExceptionCrearExcel.ErrorCargaDatos, "Información", JOptionPane.ERROR_MESSAGE);
                break;
            }
            case "3": {
                JOptionPane.showMessageDialog(null, ExceptionCrearExcel.ErrorCerrarExcel, "Información", JOptionPane.ERROR_MESSAGE);
                break;
            }
        }
    }
}
