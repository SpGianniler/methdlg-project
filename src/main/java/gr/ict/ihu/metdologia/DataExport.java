package gr.ict.ihu.metdologia;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static gr.ict.ihu.metdologia.Main.*;

public class DataExport {

    private static String[] columnsvard = {"Ημέρα","Βάρδια","Όνομα","Επίθετο","Πόστο"};
    private static String[] columnserg = {"Όνομα","Επίθετο","Ειδικότητα","Ηλικία","Ώρες"};
    private static String[] columnshours = {"Όνομα","Επίθετο","Ειδικότητα","Ώρες Πρωί","Ώρες Απόγευμα","Ώρες Βράδυ", "Ώρες Σύνολο"};

    public static void export_erg() throws IOException {
        Workbook workbook = new HSSFWorkbook();

        Sheet sheetErg = workbook.createSheet("Εργαζόμενοι");

        Font headerFont = workbook.createFont();
        headerFont.setBold(true);
        headerFont.setFontHeightInPoints((short) 14);
        headerFont.setColor(IndexedColors.BLUE_GREY.getIndex());

        CellStyle headerCellStyle = workbook.createCellStyle();
        headerCellStyle.setAlignment(HorizontalAlignment.CENTER);
        headerCellStyle.setFont(headerFont);

        Row headerErgRow = sheetErg.createRow(0);

        for (int i=0;i<columnserg.length;i++){
            Cell cellErg = headerErgRow.createCell(i);
            cellErg.setCellValue(columnserg[i]);
            cellErg.setCellStyle(headerCellStyle);
        }

        CellStyle normCellStyle = workbook.createCellStyle();
        normCellStyle.setAlignment(HorizontalAlignment.CENTER);

        int rowNum =1;
        for(Ergazomenoi erg : ergazomenoi){
            Row rowErg = sheetErg.createRow(rowNum++);
            rowErg.createCell(0).setCellValue(erg.onoma);
            rowErg.createCell(1).setCellValue(erg.epitheto);
            rowErg.createCell(2).setCellValue(erg.eidikotita);
            rowErg.createCell(3).setCellValue(erg.hlikia);
            rowErg.createCell(4).setCellValue(erg.evWres);
            for(int j=0;j<5;j++)
                rowErg.getCell(j).setCellStyle(normCellStyle);
        }
        for(int i=0;i<columnserg.length;i++){
            sheetErg.autoSizeColumn(i);
        }

        FileOutputStream fileOutputStream = new FileOutputStream("Εργαζόμενοι.xls");
        workbook.write(fileOutputStream);
        fileOutputStream.close();

        workbook.close();
    }

    public static void export_vardia() throws IOException {
        Workbook workbook = new HSSFWorkbook();
        CreationHelper creationHelper = workbook.getCreationHelper();

        Sheet sheetVard = workbook.createSheet("Βάρδιες");

        Font headerFont = workbook.createFont();
        headerFont.setBold(true);
        headerFont.setFontHeightInPoints((short) 14);
        headerFont.setColor(IndexedColors.BLUE_GREY.getIndex());

        CellStyle headerCellStyle = workbook.createCellStyle();
        headerCellStyle.setAlignment(HorizontalAlignment.CENTER);
        headerCellStyle.setFont(headerFont);

        Row headerVardRow = sheetVard.createRow(0);

        for (int i=0;i<columnsvard.length;i++){
            Cell cellVard = headerVardRow.createCell(i);
            cellVard.setCellValue(columnsvard[i]);
            cellVard.setCellStyle(headerCellStyle);
        }
        CellStyle dateCellStyle = workbook.createCellStyle();
        dateCellStyle.setDataFormat(creationHelper.createDataFormat().getFormat("dd-MM-yyyy"));
        dateCellStyle.setAlignment(HorizontalAlignment.CENTER);

        CellStyle normCellStyle = workbook.createCellStyle();
        normCellStyle.setAlignment(HorizontalAlignment.CENTER);

        int rowNum =1;
        for(Vardia var : vardiaArrayList){
            Row rowVar = sheetVard.createRow(rowNum++);
            Cell dateVard = rowVar.createCell(0);
            dateVard.setCellValue(var.date);
            dateVard.setCellStyle(dateCellStyle);
            rowVar.createCell(1).setCellValue(var.shift);
            rowVar.createCell(2).setCellValue(var.name);
            rowVar.createCell(3).setCellValue(var.surname);
            rowVar.createCell(4).setCellValue(var.post);
            for(int j=1;j<5;j++)
                rowVar.getCell(j).setCellStyle(normCellStyle);
        }
        for(int i=0;i<columnsvard.length;i++){
            sheetVard.autoSizeColumn(i);
        }

        FileOutputStream fileOutputStream = new FileOutputStream("Βάρδιες.xls");
        workbook.write(fileOutputStream);
        fileOutputStream.close();

        workbook.close();
    }

    public static void export_Wres() throws IOException {
        Workbook workbook = new HSSFWorkbook();

        Sheet sheetPer = workbook.createSheet("Ώρες Εργαζομένων");
        Font headerFont = workbook.createFont();
        headerFont.setBold(true);
        headerFont.setFontHeightInPoints((short) 14);
        headerFont.setColor(IndexedColors.BLUE_GREY.getIndex());

        CellStyle headerCellStyle = workbook.createCellStyle();
        headerCellStyle.setAlignment(HorizontalAlignment.CENTER);
        headerCellStyle.setFont(headerFont);

        Row headerPerRow = sheetPer.createRow(0);

        for (int i=0;i<columnshours.length;i++){
            Cell cellPer = headerPerRow.createCell(i);
            cellPer.setCellValue(columnshours[i]);
            cellPer.setCellStyle(headerCellStyle);
        }

        CellStyle normCellStyle = workbook.createCellStyle();
        normCellStyle.setAlignment(HorizontalAlignment.CENTER);

        int rowNum =1;
        for(Person per : people){
            Row rowPer = sheetPer.createRow(rowNum++);
            rowPer.createCell(0).setCellValue(per.firstName);
            rowPer.createCell(1).setCellValue(per.lastName);
            rowPer.createCell(2).setCellValue(per.eidikotita);
            rowPer.createCell(3).setCellValue(per.wresPrwi);
            rowPer.createCell(4).setCellValue(per.wresApog);
            rowPer.createCell(5).setCellValue(per.wresVrad);
            rowPer.createCell(6).setCellValue(per.wresTot);
            for(int j=0;j<7;j++)
                rowPer.getCell(j).setCellStyle(normCellStyle);
        }
        for(int i=0;i<columnshours.length;i++){
            sheetPer.autoSizeColumn(i);
        }

        FileOutputStream fileOutputStream = new FileOutputStream("Ωρες Εργαζομένων.xls");
        workbook.write(fileOutputStream);
        fileOutputStream.close();

        workbook.close();
    }

    public static void export() throws IOException {
        Workbook workbook = new HSSFWorkbook();
        CreationHelper creationHelper = workbook.getCreationHelper();

        Sheet sheetVard = workbook.createSheet("Βάρδιες");
        Sheet sheetErg = workbook.createSheet("Εργαζόμενοι");
        Sheet sheetPer = workbook.createSheet("Ώρες Εργαζομένων");
        Font headerFont = workbook.createFont();
        headerFont.setBold(true);
        headerFont.setFontHeightInPoints((short) 14);
        headerFont.setColor(IndexedColors.BLUE_GREY.getIndex());

        CellStyle headerCellStyle = workbook.createCellStyle();
        headerCellStyle.setAlignment(HorizontalAlignment.CENTER);
        headerCellStyle.setFont(headerFont);

        Row headerVardRow = sheetVard.createRow(0);
        Row headerErgRow = sheetErg.createRow(0);
        Row headerPerRow = sheetPer.createRow(0);

        for (int i=0;i<columnsvard.length;i++){
            Cell cellVard = headerVardRow.createCell(i);
            cellVard.setCellValue(columnsvard[i]);
            cellVard.setCellStyle(headerCellStyle);
        }
        for (int i=0;i<columnserg.length;i++){
            Cell cellErg = headerErgRow.createCell(i);
            cellErg.setCellValue(columnserg[i]);
            cellErg.setCellStyle(headerCellStyle);
        }
        for (int i=0;i<columnshours.length;i++){
            Cell cellPer = headerPerRow.createCell(i);
            cellPer.setCellValue(columnshours[i]);
            cellPer.setCellStyle(headerCellStyle);
        }

        CellStyle dateCellStyle = workbook.createCellStyle();
        dateCellStyle.setDataFormat(creationHelper.createDataFormat().getFormat("dd-MM-yyyy"));
        dateCellStyle.setAlignment(HorizontalAlignment.CENTER);

        CellStyle normCellStyle = workbook.createCellStyle();
        normCellStyle.setAlignment(HorizontalAlignment.CENTER);

        int rowNum =1;
        for(Vardia var : vardiaArrayList){
            Row rowVar = sheetVard.createRow(rowNum++);
            Cell dateVard = rowVar.createCell(0);
            dateVard.setCellValue(var.date);
            dateVard.setCellStyle(dateCellStyle);
            rowVar.createCell(1).setCellValue(var.shift);
            rowVar.createCell(2).setCellValue(var.name);
            rowVar.createCell(3).setCellValue(var.surname);
            rowVar.createCell(4).setCellValue(var.post);
            for(int j=1;j<5;j++)
                rowVar.getCell(j).setCellStyle(normCellStyle);
        }
        for(int i=0;i<columnsvard.length;i++){
            sheetVard.autoSizeColumn(i);
        }

        rowNum =1;
        for(Ergazomenoi erg : ergazomenoi){
            Row rowErg = sheetErg.createRow(rowNum++);
            rowErg.createCell(0).setCellValue(erg.onoma);
            rowErg.createCell(1).setCellValue(erg.epitheto);
            rowErg.createCell(2).setCellValue(erg.eidikotita);
            rowErg.createCell(3).setCellValue(erg.hlikia);
            rowErg.createCell(4).setCellValue(erg.evWres);
            for(int j=0;j<5;j++)
                rowErg.getCell(j).setCellStyle(normCellStyle);
        }
        for(int i=0;i<columnserg.length;i++){
            sheetErg.autoSizeColumn(i);
        }

        rowNum =1;
        for(Person per : people){
            Row rowPer = sheetPer.createRow(rowNum++);
            rowPer.createCell(0).setCellValue(per.firstName);
            rowPer.createCell(1).setCellValue(per.lastName);
            rowPer.createCell(2).setCellValue(per.eidikotita);
            rowPer.createCell(3).setCellValue(per.wresPrwi);
            rowPer.createCell(4).setCellValue(per.wresApog);
            rowPer.createCell(5).setCellValue(per.wresVrad);
            rowPer.createCell(6).setCellValue(per.wresTot);
            for(int j=0;j<7;j++)
                rowPer.getCell(j).setCellStyle(normCellStyle);
        }
        for(int i=0;i<columnshours.length;i++){
            sheetPer.autoSizeColumn(i);
        }

        FileOutputStream fileOutputStream = new FileOutputStream("Πρόγραμμα.xls");
        workbook.write(fileOutputStream);
        fileOutputStream.close();

        workbook.close();
    }

}