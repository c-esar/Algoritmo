/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Exception;

/**
 *
 * @author cesard.chacond
 */
public abstract class ExceptionSistema extends Exception{
    
    public NewExceptionCreacionDatos exceptionCreacionDatos = new NewExceptionCreacionDatos();
    public NewExceptionExcel exceptionExcel = new NewExceptionExcel();

}
