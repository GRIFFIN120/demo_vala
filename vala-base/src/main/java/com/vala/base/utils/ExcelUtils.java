package com.vala.base.utils;

import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class ExcelUtils {

    public static List<Object[]> read( MultipartFile file) throws Exception {
        String fileName = file.getOriginalFilename();
        String[] ars = getNameAndExtension(fileName);
        List<Object[]> read = ExcelUtils.read(file.getInputStream(), ars[1]);
        return read;

    }


    public static List<Object[]> read(InputStream inputStream, String type) throws Exception {
        Workbook book = getWorkBook(inputStream,type);
        return read(book,0);
    }



    public static Workbook getWorkBook(InputStream inputStream, String type) throws Exception {
        if ( "xls".equals(type)) return new HSSFWorkbook(inputStream);
        else if ("xlsx".equals(type)) return new XSSFWorkbook(inputStream);
        else throw new Exception("文件类型错误!");
    }

    public static List<Object[]> read(Workbook book, Integer sheetNum) {
        List<Object[]> list = new ArrayList<>();
        Sheet sheet = book.getSheetAt(sheetNum);
        int rowCount = sheet.getLastRowNum()+1;
        // 获得列数
        int colCount = 0;
        for (int i = 0; i < rowCount; i++) {
            Row row = sheet.getRow(i);
            int temp = row.getLastCellNum();
            colCount = temp > colCount ? temp : colCount;
        }
        // 读取数据


        for (int i = 0; i < rowCount; i++) {
            Row row = sheet.getRow(i);
            Object[] array = new Object[colCount];
            for (int j = 0; j < row.getLastCellNum(); j++) {
                Cell cell = row.getCell(j);
                if(cell==null) continue;
                CellType cellTypeEnum = cell.getCellTypeEnum();
                switch (cellTypeEnum) {
                    case NUMERIC:
                        array[j] = HSSFDateUtil.isCellDateFormatted(cell)? cell.getDateCellValue(): cell.getNumericCellValue();
                        break;
                    case STRING:
                        array[j] = cell.getStringCellValue();
                        break;
                    case FORMULA:
                        System.out.println(cell.getStringCellValue());
                        break;
                    case BLANK:
                        break;
                    case BOOLEAN:
                        array[j] = cell.getBooleanCellValue();
                        break;
                    case ERROR:
                        break;
                    default:
                        break;
                }
            }

            if(isNotNull(array)){
                list.add(array);
            }


        }
        return list;
    }

    private static boolean isNotNull(Object[] objects){
        for (Object object : objects) {
            if(object!=null) return true;
        }
        return false;
    }

    public static final String DOT = ".";
    public static String[] getNameAndExtension(String fileName) {
        if (StringUtils.INDEX_NOT_FOUND == StringUtils.indexOf(fileName, DOT))
            return new String[]{fileName, StringUtils.EMPTY};
        int DOT_INDEX = StringUtils.lastIndexOf(fileName, DOT);
        String name = StringUtils.substring(fileName,0,DOT_INDEX).trim();
        String ext = StringUtils.substring(fileName,DOT_INDEX+1).trim();
        return new String[]{name,ext};
    }

}
