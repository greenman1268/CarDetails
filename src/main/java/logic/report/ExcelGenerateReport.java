package logic.report;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;

public class ExcelGenerateReport {

    private static CellStyle stTop = null;//font 15 bold right
    private static CellStyle stTop1 = null;//font 15 bold middle
    private static CellStyle stTop3 = null;//font 14
    private static CellStyle stMid = null;//font 14 bold middle


    public static void write_xlsx() {
        try {
            Workbook wb = new XSSFWorkbook();
            Sheet sheet = wb.createSheet("Расход");
            //Setup some styles that we need for the Cells
            setCellStyles(wb);
            //insert info in the table
            insertDetailInfo(sheet);
            //Write the Excel file
            FileOutputStream fileOut = new FileOutputStream("new.xlsx");
            wb.write(fileOut);
            fileOut.close();
            System.out.println("finished succesfully");
        } catch (IOException e) {

            e.printStackTrace();
        }
    }

    protected static void setCellStyles(Workbook wb) {
        //font 15
        Font ftop = wb.createFont();
        ftop.setFontName("Times New Roman");
        ftop.setFontHeightInPoints((short) 15);
        ftop.setBoldweight(Font.BOLDWEIGHT_BOLD);

        //font 14
        Font fmid = wb.createFont();
        fmid.setFontName("Times New Roman");
        fmid.setFontHeightInPoints((short)14);
        fmid.setBoldweight(Font.BOLDWEIGHT_BOLD);

        //font 12
        Font fmid2 = wb.createFont();
        fmid2.setFontName("Times New Roman");
        fmid2.setFontHeightInPoints((short)12);
        fmid2.setBoldweight(Font.BOLDWEIGHT_BOLD);

        //font 15 bold without borders right
        stTop = wb.createCellStyle();
        stTop.setFont(ftop);
        stTop.setAlignment(CellStyle.ALIGN_RIGHT);

        //font 15 bold without borders middle
        stTop1 = wb.createCellStyle();
        stTop1.setFont(ftop);
        stTop1.setAlignment(CellStyle.ALIGN_CENTER);

        //font 14 bold without borders
        stTop3 = wb.createCellStyle();
        stTop3.setFont(fmid);
        stTop3.setAlignment(CellStyle.ALIGN_RIGHT);

        //font 14 bold with borders middle bg = 'gray'
        stMid = wb.createCellStyle();
        stMid.setFont(fmid2);
        stMid.setAlignment(CellStyle.ALIGN_CENTER);
        stMid.setBorderTop(CellStyle.BORDER_THIN);
        stMid.setTopBorderColor(IndexedColors.BLACK.getIndex());
        stMid.setBorderRight(CellStyle.BORDER_THIN);
        stMid.setRightBorderColor(IndexedColors.BLACK.getIndex());
        stMid.setBorderLeft(CellStyle.BORDER_THIN);
        stMid.setLeftBorderColor(IndexedColors.BLACK.getIndex());
        stMid.setBorderBottom(CellStyle.BORDER_THIN);
        stMid.setBottomBorderColor(IndexedColors.BLACK.getIndex());
        stMid.setFillBackgroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
        stMid.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
        stMid.setFillPattern(CellStyle.SOLID_FOREGROUND);
    }

    protected static void insertDetailInfo(Sheet sheet) {

        //Set Column Widths
        sheet.setColumnWidth(0, 500);
        sheet.setColumnWidth(1, 1500);
        sheet.setColumnWidth(2, 8000);
        sheet.setColumnWidth(3, 8000);
        sheet.setColumnWidth(4, 5000);
        sheet.setColumnWidth(5, 4000);
        sheet.setColumnWidth(6, 4000);
        sheet.setColumnWidth(7, 6000);

        Row row1 = sheet.createRow(1);
        Cell c1_1 = row1.createCell(0);    //col 0
        c1_1.setCellStyle(stTop);
        Cell c1_2 = row1.createCell(1);
        c1_2.setCellStyle(stTop);
        Cell c1_3 = row1.createCell(2);
        c1_3.setCellStyle(stTop);
        Cell c1_4 = row1.createCell(3);
        c1_4.setCellStyle(stTop);
        Cell c1_5 = row1.createCell(4);
        c1_5.setCellStyle(stTop1);
        Cell c1_6 = row1.createCell(5);
        c1_6.setCellStyle(stTop1);
        Cell c1_7 = row1.createCell(6);
        c1_7.setCellStyle(stTop1);

        CellRangeAddress region = new CellRangeAddress(1, 1, 3, 5);
        sheet.addMergedRegion(region);

        SimpleDateFormat sd1 = new SimpleDateFormat("dd.MM.YYYY");

        c1_4.setCellValue("ТОВАРНЫЙ ЧЕК №      2059      от      " + sd1.format(new java.util.Date().getTime()));

        Row row2 = sheet.createRow(2);
        Cell c2_1 = row2.createCell(0);
        c2_1.setCellStyle(stTop3);
        Cell c2_2 = row2.createCell(1);
        c2_2.setCellStyle(stTop3);
        Cell c2_3 = row2.createCell(2);
        c2_3.setCellStyle(stTop3);
        Cell c2_4 = row2.createCell(3);
        c2_4.setCellStyle(stTop3);
        Cell c2_5 = row2.createCell(4);
        c2_5.setCellStyle(stTop3);
        Cell c2_6 = row2.createCell(5);
        c2_6.setCellStyle(stTop3);
        Cell c2_7 = row2.createCell(6);
        c2_7.setCellStyle(stTop3);


        c2_7.setCellValue("Человенко Евгений Владимирович");

        Row row3 = sheet.createRow(3);
        Cell c3_1 = row3.createCell(0);
        c3_1.setCellStyle(stTop);
        Cell c3_2 = row3.createCell(1);
        c3_2.setCellStyle(stMid);
        c3_2.setCellValue("№");
        Cell c3_3 = row3.createCell(2);
        c3_3.setCellStyle(stMid);
        Cell c3_4 = row3.createCell(3);
        c3_4.setCellStyle(stMid);
        Cell c3_5 = row3.createCell(4);
        c3_5.setCellStyle(stMid);
        c3_5.setCellValue("Ед. изм.");
        Cell c3_6 = row3.createCell(5);
        c3_6.setCellStyle(stMid);
        c3_6.setCellValue("Кол-во");
        Cell c3_7 = row3.createCell(6);
        c3_7.setCellStyle(stMid);
        c3_7.setCellValue("Цена");
        Cell c3_8 = row3.createCell(7);
        c3_8.setCellStyle(stMid);
        c3_8.setCellValue("Сумма");

        region = new CellRangeAddress(3, 3, 2, 3);
        sheet.addMergedRegion(region);
        c3_3.setCellValue("Наименование товара");
       }

    public static void main(String[] args) {
        ExcelGenerateReport exg = new ExcelGenerateReport();
        exg.write_xlsx();
    }
}