package logic.report;

import logic.Item;
import org.apache.poi.xssf.usermodel.*;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.util.IOUtils;

import java.awt.*;
import java.io.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class ExcelGenerateReport {

    private static ArrayList<Item> itemList;

    private static CellStyle stTop = null;//font 15 bold left border bottom
    private static CellStyle stTop1 = null;//font 12
    private static CellStyle stTop2 = null;//font 12
    private static CellStyle stTop3 = null;//font 11
    private static CellStyle stHead = null;//font 12 bold with bold top, left and thin right,bottom
    private static CellStyle stHead1 = null;//font 12 bold with bold top and thin left,right,bottom
    private static CellStyle stHead2 = null;//font 12 bold with bold top,right and thin left,bottom

    private static XSSFWorkbook  wb = null;
    private static XSSFSheet sheet = null;

    public ExcelGenerateReport(ArrayList<Item> list){
        this.itemList = list;
    }

    public static void write_xlsx() {
        try {
            wb = new XSSFWorkbook();
            sheet = wb.createSheet("Накладна");
            XSSFPrintSetup ps = (XSSFPrintSetup) sheet.getPrintSetup();
            ps.setLandscape(true);
            //Setup some styles that we need for the Cells
            setCellStyles(wb);
            //insert info in the table
            insertDetailInfo(sheet);
            //Write the Excel file

            FileOutputStream fileOut = new FileOutputStream(System.getProperty("user.home") + "/Desktop/new.xlsx");
            wb.write(fileOut);
            fileOut.close();
            System.out.println("finished succesfully");
        } catch (IOException e) {

            e.printStackTrace();
        }
    }

    protected static void setCellStyles(Workbook wb) {
        //font 15 bold
        Font ftop = wb.createFont();
        ftop.setFontName("Arial");
        ftop.setFontHeightInPoints((short) 15);
        ftop.setBoldweight(Font.BOLDWEIGHT_BOLD);

        //font 12
        Font fmid = wb.createFont();
        fmid.setFontName("Arial");
        fmid.setFontHeightInPoints((short)12);
        fmid.setUnderline(XSSFFont.U_SINGLE);

        //font 12 bold
        Font fmid1 = wb.createFont();
        fmid1.setFontName("Arial");
        fmid1.setFontHeightInPoints((short)12);
        fmid1.setBoldweight(Font.BOLDWEIGHT_BOLD);

        //font 11
        Font fmid2 = wb.createFont();
        fmid.setFontName("Arial");
        fmid.setFontHeightInPoints((short)11);

        //font 15 bold without borders right
        stTop = wb.createCellStyle();
        stTop.setFont(ftop);
        stTop.setAlignment(CellStyle.ALIGN_LEFT);
        stTop.setBorderBottom(CellStyle.BORDER_MEDIUM);
        stTop.setBottomBorderColor(IndexedColors.BLACK.getIndex());

        //font 12
        stTop1 = wb.createCellStyle();
        stTop1.setFont(fmid);
        stTop1.setAlignment(CellStyle.ALIGN_LEFT);

        //font 15 bold without borders
        stTop2 = wb.createCellStyle();
        stTop2.setFont(fmid1);
        stTop2.setAlignment(CellStyle.ALIGN_LEFT);

        //font 11 without borders
        stTop3 = wb.createCellStyle();
        stTop3.setFont(fmid2);
        stTop3.setAlignment(CellStyle.ALIGN_LEFT);
        
        //font 12 bold center top-left
        stHead = wb.createCellStyle();
        stHead.setFont(fmid1);
        stHead.setAlignment(CellStyle.ALIGN_CENTER);
        stHead.setBorderTop(CellStyle.BORDER_MEDIUM);
        stHead.setTopBorderColor(IndexedColors.BLACK.getIndex());
        stHead.setBorderLeft(CellStyle.BORDER_MEDIUM);
        stHead.setLeftBorderColor(IndexedColors.BLACK.getIndex());
        stHead.setBorderRight(CellStyle.BORDER_THIN);
        stHead.setRightBorderColor(IndexedColors.BLACK.getIndex());
        stHead.setBorderBottom(CellStyle.BORDER_THIN);
        stHead.setBottomBorderColor(IndexedColors.BLACK.getIndex());
        stHead.setFillPattern(CellStyle.SOLID_FOREGROUND);
        stHead.setFillBackgroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
        stHead.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());  
        
        //font 12 bold center top-center
        stHead1 = wb.createCellStyle();
        stHead1.setFont(fmid1);
        stHead1.setAlignment(CellStyle.ALIGN_CENTER);
        stHead1.setBorderTop(CellStyle.BORDER_MEDIUM);
        stHead1.setTopBorderColor(IndexedColors.BLACK.getIndex());
        stHead1.setBorderLeft(CellStyle.BORDER_THIN);
        stHead1.setLeftBorderColor(IndexedColors.BLACK.getIndex());
        stHead1.setBorderRight(CellStyle.BORDER_THIN);
        stHead1.setRightBorderColor(IndexedColors.BLACK.getIndex());
        stHead1.setBorderBottom(CellStyle.BORDER_THIN);
        stHead1.setBottomBorderColor(IndexedColors.BLACK.getIndex());
        stHead1.setFillPattern(CellStyle.SOLID_FOREGROUND);
        stHead1.setFillBackgroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
        stHead1.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());  
        
        //font 12 bold center top-right
        stHead2 = wb.createCellStyle();
        stHead2.setFont(fmid1);
        stHead2.setAlignment(CellStyle.ALIGN_CENTER);
        stHead2.setBorderTop(CellStyle.BORDER_MEDIUM);
        stHead2.setTopBorderColor(IndexedColors.BLACK.getIndex());
        stHead2.setBorderLeft(CellStyle.BORDER_THIN);
        stHead2.setLeftBorderColor(IndexedColors.BLACK.getIndex());
        stHead2.setBorderRight(CellStyle.BORDER_MEDIUM);
        stHead2.setRightBorderColor(IndexedColors.BLACK.getIndex());
        stHead2.setBorderBottom(CellStyle.BORDER_THIN);
        stHead2.setBottomBorderColor(IndexedColors.BLACK.getIndex());
        stHead2.setFillPattern(CellStyle.SOLID_FOREGROUND);
        stHead2.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
        stHead2.setFillBackgroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
        
        
    }

    protected static void insertDetailInfo(Sheet sheet) {

        //Set Column Widths
        sheet.setColumnWidth(0, 500);
        sheet.setColumnWidth(1, 1500);
        sheet.setColumnWidth(2, 1300);
        sheet.setColumnWidth(3, 1200);
        sheet.setColumnWidth(4, 2000);
        sheet.setColumnWidth(5, 1300);
        sheet.setColumnWidth(6, 9000);
        sheet.setColumnWidth(7, 4000);
        sheet.setColumnWidth(8, 1650);
        sheet.setColumnWidth(9, 1500);
        sheet.setColumnWidth(10, 1000);
        sheet.setColumnWidth(11, 3000);
        sheet.setColumnWidth(12, 3000);

        //ROW 1 Замовлення покупця
        Row row1 = sheet.createRow(1);
        Cell c1_0 = row1.createCell(0);    //col 0
        Cell c1_1 = row1.createCell(1);
        c1_1.setCellStyle(stTop);
        Cell c1_2 = row1.createCell(2);
        c1_2.setCellStyle(stTop);
        Cell c1_3 = row1.createCell(3);
        c1_3.setCellStyle(stTop);
        Cell c1_4 = row1.createCell(4);
        c1_4.setCellStyle(stTop);
        Cell c1_5 = row1.createCell(5);
        c1_5.setCellStyle(stTop);
        Cell c1_6 = row1.createCell(6);
        c1_6.setCellStyle(stTop);
        Cell c1_7 = row1.createCell(7);
        c1_7.setCellStyle(stTop);
        Cell c1_8 = row1.createCell(8);
        c1_8.setCellStyle(stTop);
        Cell c1_9 = row1.createCell(9);
        c1_9.setCellStyle(stTop);
        Cell c1_10 = row1.createCell(10);
        c1_10.setCellStyle(stTop);
        Cell c1_11 = row1.createCell(11);
        c1_11.setCellStyle(stTop);
        Cell c1_12 = row1.createCell(12);
        c1_12.setCellStyle(stTop);

        CellRangeAddress region = new CellRangeAddress(1, 1, 1, 12);
        sheet.addMergedRegion(region);

        SimpleDateFormat sd1 = new SimpleDateFormat("dd.MM.YYYY");

        c1_1.setCellValue("Замовлення покупця № T000007517 від " + sd1.format(new java.util.Date().getTime()) + " р.");

        //ROW 2 empty
        Row row2 = sheet.createRow(2);
        Cell c2;
        for (int i = 0; i <= 12; i++) {
            c2 = row2.createCell(i);
        }
        region = new CellRangeAddress(2, 2, 1, 12);
        sheet.addMergedRegion(region);

        //ROW 3
        Row row3 = sheet.createRow(3);
        Cell c3_0 = row3.createCell(0);
        Cell c3_1 = row3.createCell(1);
        c3_1.setCellStyle(stTop1);
        Cell c3_2 = row3.createCell(2);
        c3_2.setCellStyle(stTop1);
        Cell c3_3 = row3.createCell(3);
        c3_3.setCellStyle(stTop1);
        region = new CellRangeAddress(3, 3, 1, 3);
        sheet.addMergedRegion(region);
        c3_1.setCellValue("Постачальник:");

        Cell c3_4 = row3.createCell(4);
        Cell c3_5 = row3.createCell(5);
        c3_5.setCellStyle(stTop2);
        Cell c3_6 = row3.createCell(6);
        c3_6.setCellStyle(stTop2);
        Cell c3_7 = row3.createCell(7);
        c3_7.setCellStyle(stTop2);
        region = new CellRangeAddress(3, 3, 5, 7);
        sheet.addMergedRegion(region);
        c3_5.setCellValue("   ФОП Шитов Максим Анатолiйович");


        //ROW 4
        putPicture();
        Row row4 = sheet.createRow(4);
        Cell c4_6 = row4.createCell(6);
        c4_6.setCellStyle(stTop3);
        Cell c4_7 = row4.createCell(7);
        c4_7.setCellStyle(stTop3);
        Cell c4_8 = row4.createCell(8);
        c4_8.setCellStyle(stTop3);
        region = new CellRangeAddress(4, 4, 6, 8);
        sheet.addMergedRegion(region);
        c4_6.setCellValue("Р/р 25009000077020, у банку ПАТ \"Укрсоцбанк\", МФО 300023,");

        //ROW 5
        Row row5 = sheet.createRow(5);
        Cell c5_6 = row5.createCell(6);
        c5_6.setCellStyle(stTop3);
        Cell c5_7 = row5.createCell(7);
        c5_7.setCellStyle(stTop3);
        Cell c5_8 = row5.createCell(8);
        c5_8.setCellStyle(stTop3);
        Cell c5_9 = row5.createCell(9);
        c5_9.setCellStyle(stTop3);
        Cell c5_10 = row5.createCell(10);
        c5_10.setCellStyle(stTop3);
        Cell c5_11 = row5.createCell(11);
        c5_11.setCellStyle(stTop3);
        Cell c5_12 = row5.createCell(12);
        c5_12.setCellStyle(stTop3);
        region = new CellRangeAddress(5, 5, 6, 12);
        sheet.addMergedRegion(region);
        c5_6.setCellValue("юр. адреса: ВУЛИЦЯ ЯСНА, БУДИНОК 4 МIСТО ХАРКIВ, ХАРКIВСЬКА ОБЛ, 61089, тел.: 0673551585;");

        //ROW 6
        Row row6 = sheet.createRow(6);
        Cell c6_6 = row6.createCell(6);
        c6_6.setCellStyle(stTop3);
        c6_6.setCellValue("0955159725; 0577002100,");

        //ROW 7
        Row row7 = sheet.createRow(7);
        Cell c7_6 = row7.createCell(6);
        c7_6.setCellStyle(stTop3);
        Cell c7_7 = row7.createCell(7);
        c7_7.setCellStyle(stTop3);
        region = new CellRangeAddress(7, 7, 6, 7);
        sheet.addMergedRegion(region);
        c7_6.setCellValue("код за ЄДРПОУ 2885002739, IПН 2885902739,");

        //ROW 8
        Row row8 = sheet.createRow(8);
        Cell c8_6 = row8.createCell(6);
        c8_6.setCellStyle(stTop3);
        Cell c8_7 = row8.createCell(7);
        c8_7.setCellStyle(stTop3);
        Cell c8_8 = row8.createCell(8);
        c8_8.setCellStyle(stTop3);
        region = new CellRangeAddress(8, 8, 6, 8);
        sheet.addMergedRegion(region);
        c8_6.setCellValue("Не є платником податку на прибуток на загальних засадах");

        //ROW 9 empty
        Row row9 = sheet.createRow(9);
        Cell c9;
        for (int i = 0; i <= 12; i++) {
            c9 = row9.createCell(i);
        }
        region = new CellRangeAddress(9, 9, 1, 12);
        sheet.addMergedRegion(region);

        //ROW 10
        Row row10 = sheet.createRow(10);
        Cell c10_1 = row10.createCell(1);
        c10_1.setCellStyle(stTop1);
        Cell c10_2 = row10.createCell(2);
        c10_2.setCellStyle(stTop1);

        region = new CellRangeAddress(10, 10, 1, 2);
        sheet.addMergedRegion(region);
        c10_1.setCellValue("Покупець:");

        Cell c10_4 = row10.createCell(4);
        Cell c10_5 = row10.createCell(5);
        c10_5.setCellStyle(stTop2);
        Cell c10_6 = row10.createCell(6);
        c10_6.setCellStyle(stTop2);
        Cell c10_7 = row10.createCell(7);
        c10_7.setCellStyle(stTop2);
        region = new CellRangeAddress(10, 10, 5, 7);
        sheet.addMergedRegion(region);
        c10_5.setCellValue("   Кравченко Сергiй, Кременчуг №1");

        //ROW 11
        Row row11 = sheet.createRow(11);
        Cell c11_6 = row11.createCell(6);
        c11_6.setCellStyle(stTop3);
        c11_6.setCellValue("Тел.: 380973352023");

        //ROW 12 TABLE
        Row row12 = sheet.createRow(12);
        Cell c12_1 = row12.createCell(1);
        c12_1.setCellStyle(stHead);
        c12_1.setCellValue("№");

        Cell c12_2 = row12.createCell(2);
        c12_2.setCellStyle(stHead1);
        Cell c12_3 = row12.createCell(3);
        c12_3.setCellStyle(stHead1);
        Cell c12_4 = row12.createCell(4);
        c12_4.setCellStyle(stHead1);
        region = new CellRangeAddress(12, 12, 2, 4);
        sheet.addMergedRegion(region);
        c12_2.setCellValue("Код");

        Cell c12_5 = row12.createCell(5);
        c12_5.setCellStyle(stHead1);
        Cell c12_6 = row12.createCell(6);
        c12_6.setCellStyle(stHead1);
        Cell c12_7 = row12.createCell(7);
        c12_7.setCellStyle(stHead1);
        region = new CellRangeAddress(12, 12, 5, 7);
        sheet.addMergedRegion(region);
        c12_5.setCellValue("Товар");

        Cell c12_8 = row12.createCell(8);
        c12_8.setCellStyle(stHead1);
        Cell c12_9 = row12.createCell(9);
        c12_9.setCellStyle(stHead1);
        Cell c12_10 = row12.createCell(10);
        c12_10.setCellStyle(stHead1);
        region = new CellRangeAddress(12, 12, 8, 10);
        sheet.addMergedRegion(region);
        c12_8.setCellValue("Кiлькiсть");

        Cell c12_11 = row12.createCell(11);
        c12_11.setCellStyle(stHead1);
        c12_11.setCellValue("Цiна");

        Cell c12_12 = row12.createCell(12);
        c12_12.setCellStyle(stHead2);
        c12_12.setCellValue("Сума");


    }

    public static void putPicture(){
        try {
            InputStream my_banner_image = new FileInputStream("src/main/java/logic/report/resources/1.jpg");
            byte[] bytes = IOUtils.toByteArray(my_banner_image);
            int my_picture_id = wb.addPicture(bytes, Workbook.PICTURE_TYPE_PNG);
            XSSFDrawing drawing = (XSSFDrawing) sheet.createDrawingPatriarch();
            my_banner_image.close();
            XSSFClientAnchor my_anchor = new XSSFClientAnchor();
            my_anchor.setCol1(1);
            my_anchor.setCol2(2);
            my_anchor.setRow1(4);
            my_anchor.setRow2(5);
            XSSFPicture my_picture = drawing.createPicture(my_anchor, my_picture_id);
            my_picture.resize();
            FileOutputStream out = new FileOutputStream(new File(System.getProperty("user.home") + "/Desktop/new.xlsx"));
            wb.write(out);
            out.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        ExcelGenerateReport exg = new ExcelGenerateReport(new ArrayList<Item>());
        exg.write_xlsx();
        Desktop desktop = null;
        if (Desktop.isDesktopSupported()) {
            desktop = Desktop.getDesktop();

        try {
            desktop.open(new File(System.getProperty("user.home") + "/Desktop/new.xlsx"));
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }}
    }
}