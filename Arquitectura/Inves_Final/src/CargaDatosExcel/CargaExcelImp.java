/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package CargaDatosExcel;

import Variables.AtributosSistema;
import Constantes.Constantes;
import Constantes.ExceptionLeerExcel;
import Exception.NewExceptionExcel;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import Exception.ExceptionSistema;
import java.util.Date;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;

/**
 *
 * @author David
 */
public class CargaExcelImp implements CargaExcel, Constantes {

    private String file;
    private FileInputStream files;
    private XSSFWorkbook wb;
    private String nombreDelDia;
    private String nombreVariable;
    private boolean entreProvedores = true;
    private int ContadorDia = 0;

    public CargaExcelImp(String file, int numeroProvedores) {
        this.file = file;
        this.datos.setNumeroProvedores(numeroProvedores);
    }

    public CargaExcelImp() {
    }

    @Override
    public boolean IniciarLecturaExcel() {
        try {
            VerificarArchivoExcel();
            if (Leer()) {
                files.close();
                return true;
            } else {
                files.close();
                return false;
            }

        } catch (Exception e) {
            System.out.println(e.getMessage());
            return false;
        }
    }

    private boolean VerificarArchivoExcel() throws NewExceptionExcel, ExceptionSistema {
        try {
            files = new FileInputStream(new File(file));
            wb = new XSSFWorkbook(files);
            return true;
        } catch (FileNotFoundException ex) {
            datos.setError("1");

        } catch (IOException ex) {
            datos.setError("1");

        }
        return false;
    }

    @Override
    public boolean Leer() {
        XSSFSheet sheet = wb.getSheetAt(0);
        int numFilas = sheet.getLastRowNum();
        for (int x = 1; x <= numFilas; x++) {
            Row fila = sheet.getRow(x);
            while (fila == null) {
                x++;
                fila = sheet.getRow(x);
            }
            int numCols = fila.getLastCellNum();
            if (x == 1) {
                datos.setNumeroProvedores(numCols - 3);
            }
            ArrayList<Double> a = new ArrayList<>();
            for (int y = 0, j = 0; y < numCols; y++) {
                Cell celda = fila.getCell(y);
                if (!(celda == null)) {
                    switch (celda.getCellTypeEnum().toString()) {
                        case "NUMERIC": {
                            if (y == 1) {
                                datos.getProvedoresNombre().add(String.valueOf(celda.getNumericCellValue()));
                            } else {
                                if (datos.getPesoVolProvedores().containsKey(nombreDelDia)) {
                                    a = datos.getPesoVolProvedores().get(nombreDelDia).get(nombreVariable);
                                    a.add(celda.getNumericCellValue());
                                    datos.getPesoVolProvedores().get(nombreDelDia).put(nombreVariable, a);
                                } else {
                                    if (entreProvedores) {
                                        datos.arregloMatriz(datos.getNumeroProvedores() + 1);
                                        entreProvedores = false;
                                    }
                                    datos.getDistancias()[j][x - 1] = celda.getNumericCellValue();
                                    j++;
                                }
                            }
                            System.out.print(celda.getNumericCellValue() + " ");
                            break;
                        }
                        case "STRING": {
                            for (String v : datos.getVariablesNombresExcel()) {
                                if (celda.getStringCellValue().contains(v)) {
                                    switch (v) {
                                        case "Dia": {
                                            ContadorDia += 1;
                                            String vi = v + String.valueOf(ContadorDia);
                                            datos.getPesoVolProvedores().put(vi, new HashMap<>());
                                            nombreDelDia = vi;
                                            break;
                                        }
                                        case Constantes.Demanda_kg: {
                                            datos.getPesoVolProvedores().get(nombreDelDia).put(v, new ArrayList<>());
                                            nombreVariable = v;
                                            break;
                                        }
                                        case Constantes.Volumen: {
                                            datos.getPesoVolProvedores().get(nombreDelDia).put(v, new ArrayList<>());
                                            nombreVariable = v;
                                            break;
                                        }
                                        default: {
                                            System.out.println("Error de carga");
                                        }
                                    }

                                }
                            }
                            System.out.print(celda.getStringCellValue() + " ");
                            break;
                        }
                        default: {
                        }
                    }
                }
            }
            System.out.println("");
        }
        //System.out.println(Arrays.toString(datos.getDistancias()));
        return true;
    }

    @Override
    public boolean crearExcel() {
        try {
            Workbook book = new HSSFWorkbook();
            Sheet sheet = book.createSheet("Rutas");
            //fila
            int countFilas = 0;
            int countColumnas = 1;
            String armogrupo = "";
            String dia = null;
            boolean entre = true;
            int diaobtenido = 0;
            CellStyle cellStyle = book.createCellStyle();
            Font cellFont = book.createFont();
            cellFont.setColor(Font.COLOR_RED);
            cellStyle.setFont(cellFont);
            for (int i = 0; i < datos.getResultadosFinales().size(); i++) {
                diaobtenido++;
                dia = "Dia" + String.valueOf(diaobtenido);
                entre = true;
                do {
                    if (datos.getResultadosFinales().containsKey(dia)) {
                        entre = false;
                        if (!dia.equalsIgnoreCase("dia1")) {
                            countFilas++;
                            countFilas++;
                        }
                        Row row = sheet.createRow(countFilas);
                        row.createCell(0).setCellValue("Rutas optimas dia " + diaobtenido);
                        row.setRowStyle(cellStyle);
                        countFilas++;
                        Row rowEspacio2 = sheet.createRow(countFilas);
                        rowEspacio2.createCell(0).setCellValue("");
                        countFilas++;
                        Row rowdirecta = sheet.createRow(countFilas);
                        rowdirecta.createCell(0).setCellValue("Rutas Directas");
                        for (int j = 0; j <= datos.getResultadosFinales().get(dia).get("valoresFinales").get(datos.getResultadosFinales().get(dia).get("valoresFinales").size() - 1).intValue(); j++) {
                            countFilas++;
                            Row rowgrupos = sheet.createRow(countFilas);
                            rowgrupos.createCell(0).setCellValue("Grupo " + (j + 1));
                            armogrupo = "";
                            for (int p = 0; p < datos.getResultadosFinales().get(dia).get("grupo" + (j + 1)).size(); p++) {
                                int num = (int) datos.getResultadosFinales().get(dia).get("grupo" + (j + 1)).get(p).intValue();
                                for (int u = 0; u < datos.getPuntosDivicion().get(dia).size(); u++) {
                                    if (num == datos.getPuntosDivicion().get(dia).get(u).get(0).intValue()) {
                                        num = datos.getPuntosDivicion().get(dia).get(u).get(1).intValue();
                                        break;
                                    }
                                }

                                armogrupo += String.valueOf((int) Double.parseDouble(String.valueOf(datos.getProvedoresNombre().get(num))));
                                if (p < datos.getResultadosFinales().get(dia).get("grupo" + (j + 1)).size() - 1) {
                                    armogrupo += ",";
                                } else {
                                    armogrupo += "," + String.valueOf((int) Double.parseDouble(String.valueOf(datos.getProvedoresNombre().get(0))));
                                }
                            }
                            rowgrupos.createCell(countColumnas).setCellValue(armogrupo);
                            countFilas++;
                            Row rowCarga = sheet.createRow(countFilas);
                            rowCarga.createCell(0).setCellValue("Carga " + (j + 1));
                            rowCarga.createCell(1).setCellValue(datos.getResultadosFinales().get(dia).get("carga").get(j));
                            countFilas++;
                            Row rowVolumen = sheet.createRow(countFilas);
                            rowVolumen.createCell(0).setCellValue("Volumen " + (j + 1));
                            rowVolumen.createCell(1).setCellValue(datos.getResultadosFinales().get(dia).get("volumen").get(j));
                            countFilas++;
                            Row rowDistancia = sheet.createRow(countFilas);
                            rowDistancia.createCell(0).setCellValue("Distancia " + (j + 1));
                            rowDistancia.createCell(1).setCellValue(datos.getResultadosFinales().get(dia).get("distancia").get(j));
                            countFilas++;
                            Row rowEspacio = sheet.createRow(countFilas);
                            rowEspacio.createCell(0).setCellValue("");
                        }
                        String centroConsolidacion = "";
                        for (int l = 0; l < datos.getCentroConsolidacion().get(dia).size(); l++) {
                            centroConsolidacion +=String.valueOf((int) Double.parseDouble(String.valueOf(datos.getProvedoresNombre().get((int) datos.getCentroConsolidacion().get(dia).get(l).intValue()))));
                            if (l < datos.getCentroConsolidacion().get(dia).size() - 1) {
                                centroConsolidacion += ",";
                            }
                        }
                        countFilas++;
                        Row rowCentroConsolidacion = sheet.createRow(countFilas);
                        rowCentroConsolidacion.createCell(0).setCellValue("Centro consolidacion dia " + diaobtenido);
                        rowCentroConsolidacion.createCell(1).setCellValue("".equals(centroConsolidacion) ? "Ninguno" : centroConsolidacion);
                        rowCentroConsolidacion.setRowStyle(cellStyle);
                        countFilas++;
                        Row rowEspacio = sheet.createRow(countFilas);
                        rowEspacio.createCell(0).setCellValue("");
                        countFilas++;
                        Row rowCargaFinal = sheet.createRow(countFilas);
                        rowCargaFinal.createCell(0).setCellValue("Carga Total dia " + diaobtenido);
                        rowCargaFinal.createCell(1).setCellValue(datos.getResultadosFinales().get(dia).get("valoresFinales").get(1));
                        rowCargaFinal.setRowStyle(cellStyle);
                        countFilas++;
                        Row rowVolumenFinal = sheet.createRow(countFilas);
                        rowVolumenFinal.createCell(0).setCellValue("Volumen Total dia " + diaobtenido);
                        rowVolumenFinal.createCell(1).setCellValue(datos.getResultadosFinales().get(dia).get("valoresFinales").get(2));
                        rowVolumenFinal.setRowStyle(cellStyle);
                        countFilas++;
                        Row rowDistanciaFinal = sheet.createRow(countFilas);
                        rowDistanciaFinal.createCell(0).setCellValue("Distancia Total dia " + diaobtenido);
                        rowDistanciaFinal.createCell(1).setCellValue(datos.getResultadosFinales().get(dia).get("valoresFinales").get(0));
                        rowDistanciaFinal.setRowStyle(cellStyle);
                        countFilas++;
                    } else {
                        diaobtenido++;
                        dia = "Dia" + String.valueOf(diaobtenido);
                    }
                } while (entre);

            }
            int valor = (int) Math.floor(Math.random() * 1000 + 1);
            FileOutputStream file = new FileOutputStream(javax.swing.filechooser.FileSystemView.getFileSystemView().getHomeDirectory() + "\\rutas" + valor + ".xls");
            book.write(file);
            file.close();
        } catch (FileNotFoundException ex) {
            JOptionPane.showMessageDialog(null, ExceptionLeerExcel.ErrorCrearExcel, "Información Crear Excel", JOptionPane.INFORMATION_MESSAGE);
            Logger.getLogger(CargaExcelImp.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(CargaExcelImp.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
    }

    @Override
    public boolean crearExcelEmision() {
        try {
            Workbook book = new HSSFWorkbook();
            Sheet sheet = book.createSheet("Rutas");
            //fila
            int countFilas = 0;
            int countColumnas = 1;
            String armogrupo = "";
            String dia = null;
            boolean entre = true;
            int diaobtenido = 0;
            CellStyle cellStyle = book.createCellStyle();
            Font cellFont = book.createFont();
            cellFont.setColor(Font.COLOR_RED);
            cellStyle.setFont(cellFont);
            for (int i = 0; i < datos.getResultadosFinales().size(); i++) {
                diaobtenido++;
                dia = "Dia" + String.valueOf(diaobtenido);
                entre = true;
                do {
                    if (datos.getResultadosFinales().containsKey(dia)) {
                        entre = false;
                        if (!dia.equalsIgnoreCase("dia1")) {
                            countFilas++;
                            countFilas++;
                        }
                        Row row = sheet.createRow(countFilas);
                        row.createCell(0).setCellValue("Rutas optimas dia " + diaobtenido);
                        row.setRowStyle(cellStyle);
                        countFilas++;
                        Row rowEspacio2 = sheet.createRow(countFilas);
                        rowEspacio2.createCell(0).setCellValue("");
                        countFilas++;
                        Row rowdirecta = sheet.createRow(countFilas);
                        rowdirecta.createCell(0).setCellValue("Rutas Directas");
                        for (int j = 0; j <= datos.getResultadosFinales().get(dia).get("valoresFinales").get(datos.getResultadosFinales().get(dia).get("valoresFinales").size() - 1).intValue(); j++) {
                            countFilas++;
                            Row rowgrupos = sheet.createRow(countFilas);
                            rowgrupos.createCell(0).setCellValue("Grupo " + (j + 1));
                            armogrupo = "";
                            for (int p = 0; p < datos.getResultadosFinales().get(dia).get("grupo" + (j + 1)).size(); p++) {
                                int num = (int) datos.getResultadosFinales().get(dia).get("grupo" + (j + 1)).get(p).intValue();
                                for (int u = 0; u < datos.getPuntosDivicion().get(dia).size(); u++) {
                                    if (num == datos.getPuntosDivicion().get(dia).get(u).get(0).intValue()) {
                                        num = datos.getPuntosDivicion().get(dia).get(u).get(1).intValue();
                                        break;
                                    }
                                }
                                armogrupo += String.valueOf((int) Double.parseDouble(String.valueOf(datos.getProvedoresNombre().get(num))));
                                if (p < datos.getResultadosFinales().get(dia).get("grupo" + (j + 1)).size() - 1) {
                                    armogrupo += ",";
                                } else {
                                    armogrupo += "," + String.valueOf((int) Double.parseDouble(String.valueOf(datos.getProvedoresNombre().get(0))));
                                }
                            }
                            rowgrupos.createCell(countColumnas).setCellValue(armogrupo);
                            countFilas++;
                            Row rowCarga = sheet.createRow(countFilas);
                            rowCarga.createCell(0).setCellValue("Carga " + (j + 1));
                            rowCarga.createCell(1).setCellValue(datos.getResultadosFinales().get(dia).get("carga").get(j));
                            countFilas++;
                            Row rowVolumen = sheet.createRow(countFilas);
                            rowVolumen.createCell(0).setCellValue("Volumen " + (j + 1));
                            rowVolumen.createCell(1).setCellValue(datos.getResultadosFinales().get(dia).get("volumen").get(j));
                            countFilas++;
                            Row rowDistancia = sheet.createRow(countFilas);
                            rowDistancia.createCell(0).setCellValue("Distancia " + (j + 1));
                            rowDistancia.createCell(1).setCellValue(datos.getResultadosFinales().get(dia).get("distancia").get(j));
                            countFilas++;
                            Row rowEmision = sheet.createRow(countFilas);
                            rowEmision.createCell(0).setCellValue("Emision " + (j + 1));
                            rowEmision.createCell(1).setCellValue(datos.getResultadosFinales().get(dia).get("emision").get(j));
                            countFilas++;
                            Row rowEspacio = sheet.createRow(countFilas);
                            rowEspacio.createCell(0).setCellValue("");
                        }
                        String centroConsolidacion = "";
                        for (int l = 0; l < datos.getCentroConsolidacion().get(dia).size(); l++) {
                            centroConsolidacion +=String.valueOf((int) Double.parseDouble(String.valueOf(datos.getProvedoresNombre().get((int) datos.getCentroConsolidacion().get(dia).get(l).intValue()))));
                            if (l < datos.getCentroConsolidacion().get(dia).size() - 1) {
                                centroConsolidacion += ",";
                            }
                        }
                        countFilas++;
                        Row rowCentroConsolidacion = sheet.createRow(countFilas);
                        rowCentroConsolidacion.createCell(0).setCellValue("Centro consolidacion dia " + diaobtenido);
                        rowCentroConsolidacion.createCell(1).setCellValue("".equals(centroConsolidacion) ? "Ninguno" : centroConsolidacion);
                        rowCentroConsolidacion.setRowStyle(cellStyle);
                        countFilas++;
                        Row rowEspacio = sheet.createRow(countFilas);
                        rowEspacio.createCell(0).setCellValue("");
                        countFilas++;
                        Row rowCargaFinal = sheet.createRow(countFilas);
                        rowCargaFinal.createCell(0).setCellValue("Carga Total dia " + diaobtenido);
                        rowCargaFinal.createCell(1).setCellValue(datos.getResultadosFinales().get(dia).get("valoresFinales").get(1));
                        rowCargaFinal.setRowStyle(cellStyle);
                        countFilas++;
                        Row rowVolumenFinal = sheet.createRow(countFilas);
                        rowVolumenFinal.createCell(0).setCellValue("Volumen Total dia " + diaobtenido);
                        rowVolumenFinal.createCell(1).setCellValue(datos.getResultadosFinales().get(dia).get("valoresFinales").get(2));
                        rowVolumenFinal.setRowStyle(cellStyle);
                        countFilas++;
                        Row rowDistanciaFinal = sheet.createRow(countFilas);
                        rowDistanciaFinal.createCell(0).setCellValue("Distancia Total dia " + diaobtenido);
                        rowDistanciaFinal.createCell(1).setCellValue(datos.getResultadosFinales().get(dia).get("valoresFinales").get(0));
                        rowDistanciaFinal.setRowStyle(cellStyle);
                        countFilas++;
                        Row rowEmisionFinal = sheet.createRow(countFilas);
                        rowEmisionFinal.createCell(0).setCellValue("Emision Total dia " + diaobtenido);
                        rowEmisionFinal.createCell(1).setCellValue(datos.getResultadosFinales().get(dia).get("valoresFinales").get(3));
                        rowEmisionFinal.setRowStyle(cellStyle);
                        countFilas++;
                    } else {
                        diaobtenido++;
                        dia = "Dia" + String.valueOf(diaobtenido);
                    }
                } while (entre);

            }
            int valor = (int) Math.floor(Math.random() * 1000 + 1);
            FileOutputStream file = new FileOutputStream(javax.swing.filechooser.FileSystemView.getFileSystemView().getHomeDirectory() + "\\rutas" + valor + ".xls");
            book.write(file);
            file.close();
        } catch (FileNotFoundException ex) {
            JOptionPane.showMessageDialog(null, ExceptionLeerExcel.ErrorCrearExcel, "Información Crear Excel", JOptionPane.INFORMATION_MESSAGE);
            Logger.getLogger(CargaExcelImp.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(CargaExcelImp.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
    }

}


//command
