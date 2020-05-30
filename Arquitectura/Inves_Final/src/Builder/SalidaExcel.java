/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Builder;

import Variables.AtributosSistema;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Arrays;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

/**
 *
 * @author David
 */
public class SalidaExcel extends SalidaBuilder {

    @Override
    public void salida() {
        System.out.println(Arrays.toString(datos.getDistancias()));
//        int filas = -1;
//        int columnas = -1;
//        datos = AtributosSistema.getInstance();
//        Workbook book = new XSSFWorkbook();
//        Sheet sheet = book.createSheet("Resultado");
//
//        // Row row = sheet.createRow(sheet.addMergedRegion(new CellRangeAddress(0,0,0,10)));       
//        filas++;
//        Row row = sheet.createRow(filas);
//        columnas++;
//        row.createCell(columnas).setCellValue("Secuencia");
//        for (int c = 1; c <= datos.getImprimirNodos().size(); c++) {
//            filas++;
//            Row dia = sheet.createRow(filas);
//            columnas++;
//            dia.createCell(columnas).setCellValue("Dia" + (c));
////            for (int i = 0; i < datos.getImprimirNodos().get("Dia" + (c)).get(Constantes.Nodos_Con_Ahorro).size(); i++) {
////                Row rown = sheet.createRow(filas++);
////                rown.createCell(columnas++).setCellValue("Ruta" + (i));
//////                for (int y = 0; y < datos.getImprimirNodos().get(Constantes.Nodos_Con_Ahorro).get(0).size(); y++) {
//////                    rown.createCell(y + 1).setCellValue(datos.getImprimirNodos().get(Constantes.Nodos_Con_Ahorro).get(0).get(y));
//////                    rown.createCell(y + 2).setCellValue(datos.getImprimirNodos().get(Constantes.Nodos_Con_Ahorro).get(1).get(y));
//////                }
////            }
//        }
//
//        try {
//            FileOutputStream fileout = new FileOutputStream("Excel.xlsx");
//            book.write(fileout);
//            fileout.close();
//        } catch (FileNotFoundException ex) {
//
//        } catch (IOException ex) {
//
//        }
    }

}
