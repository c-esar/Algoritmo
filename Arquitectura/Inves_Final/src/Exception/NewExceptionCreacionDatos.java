/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Exception;

import javax.swing.JOptionPane;

/**
 *
 * @author David
 */
public class NewExceptionCreacionDatos extends ExceptionSistema{

    public void ExceptionFunciones(String error) {
        switch (error) {
            case "1": {
                JOptionPane.showMessageDialog(null, "Error al Crear puntos con Nodo", "Información", JOptionPane.ERROR_MESSAGE);
                break;
            }
        }
    }
}
