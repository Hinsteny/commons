package com.github.hinsteny.commons.warp.io.excel;

import com.github.hinsteny.commons.core.utils.StringUtil;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

/**
 * @author Hinsteny
 * @version ExcelUtil: ExcelUtil 2019-05-10 09:41 All rights reserved.$
 */
public class ExcelUtil {

    private static String CELL_PREFIX = "CELL";
    private static String KEY_PREFIX = "KEY";
    private static String NULL_VALUE = "NULL";

    /**
     * 导入: 从excel到SheetData. tbody data保存到 SheetData.dataList (List<List<String>>)
     * @param filePath 文件路径
     * @return list List<Map<String,Object>>
     * @throws Exception 异常
     */
    public static SheetData convertSheetDataAsList(String filePath) throws Exception{
        validateParams(filePath);
        List<HeaderKey> headerKeys = new ArrayList<>();
        List<List<String>> dataList = new ArrayList<>();
        List<String> data;

        // 读取文件
        FileInputStream fis = null;
        Workbook workbook = null;
        try {
            fis = new FileInputStream(filePath);
            if (filePath.endsWith(".xls")) {
                workbook = new HSSFWorkbook(fis);
            } else if (filePath.endsWith(".xlsx")) {
                workbook = new XSSFWorkbook(fis);
            }
            if (null == workbook) {
                throw new Exception("get excel failed.");
            }
            //获取第一个sheet页
            Sheet sheet = workbook.getSheetAt(0);

            //获得数据的总列数. 会剔除空格，计算所有不为空的数。
            //int totalCellNum = sheet.getRow(0).getPhysicalNumberOfCells()
            //获取数据总列数，拿到最后一列的Num，故不会剔除空格。
            int totalCellNum = sheet.getRow(0).getLastCellNum();
            //获得数据的总行数
            int totalRowNum = sheet.getLastRowNum();

            Cell cell;
            Object value;
            // 获取表头, 遍历第一行
            for (short i = 0; i < totalCellNum; i++) {
                cell = sheet.getRow(0).getCell(i);
                if (null == cell) {
                    // 为空时，设置随机值
                    headerKeys.add(new HeaderKey (KEY_PREFIX + i, CELL_PREFIX + i));
                    continue;
                }
                value = getCellValue(cell);
                headerKeys.add(new HeaderKey (KEY_PREFIX + i, String.valueOf(value)));
            }

            // 获取数据, 从第二行开始遍历，行列
            for (int i =1; i<totalRowNum+1; i++) {
                data = new ArrayList<>();
                for (short j = 0; j < totalCellNum; j++) {
                    cell = sheet.getRow(i).getCell(j);
                    if (null == cell) {
                        // 为空时，设置随机值
                        data.add(NULL_VALUE);
                        continue;
                    }
                    value = getCellValue(cell);
                    data.add(String.valueOf(value));
                }
                dataList.add(data);
            }



        } catch (Exception e) {
            e.printStackTrace();
            throw new Exception("analysis excel exception", e);
        } finally {
            if (null != fis) {
                fis.close();
            }
        }
        return new SheetData(headerKeys,null, dataList);
    }

    /**
     * 导入: 从excel到SheetData, tbody data保存到 SheetData.dataMap (List<Map<String,String>>)
     * @param filePath 文件路径
     * @return list List<Map<String,Object>>
     * @throws Exception 异常
     */
    public static SheetData convertSheetDataAsMap(String filePath) throws Exception {
        //校验入参是否正确
        validateParams(filePath);

        List<HeaderKey> headerKeys = new ArrayList<>();
        ArrayList<Map<String, String>> dataList = new ArrayList<>();
        Map<String, String> data;

        // 读取文件
        FileInputStream fis = null;
        Workbook workbook = null;

        try {
            fis = new FileInputStream(filePath);
            if (filePath.endsWith(".xls")) {
                workbook = new HSSFWorkbook(fis);
            } else if (filePath.endsWith(".xlsx")) {
                workbook = new XSSFWorkbook(fis);
            }
            if (null == workbook) {
                throw new Exception("get excel failed.");
            }
            //获取第一个sheet页
            Sheet sheet = workbook.getSheetAt(0);
            //int totalCellNum = sheet.getRow(0).getPhysicalNumberOfCells()
            //获取数据总列数，拿到最后一列的Num，故，不会剔除空格。
            int cellNum = sheet.getRow(0).getLastCellNum();

            //获得数据的总行数
            int totalRowNum = sheet.getLastRowNum();
            Cell cell;
            Object value;
            // 获取表头,遍历第一行
            for (short i = 0; i < cellNum; i++) {
                cell = sheet.getRow(0).getCell(i);
                if (null == cell) {
                    // 为空时，设置随机值
                    headerKeys.add(new HeaderKey(KEY_PREFIX + i, CELL_PREFIX + i));
                    continue;
                }
                value = getCellValue(cell);
                headerKeys.add(new HeaderKey(KEY_PREFIX + i, String.valueOf(value)));
            }

            // 获取数据，遍历行，列. 从第二行开始，一行一行的读
            for (int i = 1; i < totalRowNum + 1; i++) {
                data = new HashMap<>(16);
                for (short j = 0; j < cellNum; j++) {
                    cell = sheet.getRow(i).getCell(j);
                    if (null == cell) {
                        // 为空时，设置为空
                        data.put(KEY_PREFIX + j, NULL_VALUE);
                        continue;
                    }
                    value = getCellValue(cell);
                    data.put(KEY_PREFIX + j, String.valueOf(value));
                }
                dataList.add(data);
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new Exception("analysis excel exception", e);
        } finally {
            if (null != fis) {
                fis.close();
            }
        }
        return new SheetData(headerKeys, dataList);
    }

    /**
     * excel导出 （单表） 由实体类对象导出。 需要getter方法。
     * // 需优化为，无需getter方法 和支持 Map导出
     * @param fileNamePath 文件路径
     * @param sheetName sheet名称
     * @param list 数据来源
     * @param titles  表头
     * @param fieldNames  字段名称数组
     * @param <T> 实体类型
     * @return file 文件
     */
    public static <T> File export(String fileNamePath, String sheetName, List<T> list, String[] titles,
        String[] fieldNames) throws Exception {
        //生成一个新的sheet,并以表名命名
        sheetName = StringUtil.isNullOrEmpty(sheetName) ? "sheet1" : sheetName;

        HSSFWorkbook workbook = new HSSFWorkbook();
        HSSFSheet sheet = workbook.createSheet(sheetName);

        // 设置表头的说明
        HSSFRow topRow = sheet.createRow(0);
        for (int i = 0; i < titles.length; i++) {
            setCellValue(topRow.createCell(i), titles[i]);
        }

        //填入内容
        Method method;
        HSSFRow row;
        T t;
        Object res;
        // 遍历生成数据行，通过反射获取字段的get方法
        for (int i = 0; i < list.size(); i++) {
            t = list.get(i);
            row = sheet.createRow(i + 1);
            Class<?> clazz = t.getClass();
            for (int j = 0; j < fieldNames.length; j++) {
                //通过类和字段名 获取对应的method
                method = getMethod(clazz, fieldNames[j]);
                res = method.invoke(t);
                setCellValue(row.createCell(j), res + "");
            }
        }

        //写入文件
        OutputStream os = null;
        File file = new File(fileNamePath);
        try {
            os = new FileOutputStream(file);
            workbook.write(os);
        } catch (Exception e) {
            throw new Exception("write excel file error!", e);
        } finally {
            if (null != os) {
                os.flush();
                os.close();
            }
        }
        return file;
    }

    /**
     * 把数据填入到单元格中
     * @param cell 单元格
     * @param value 值
     */
    private static void setCellValue(HSSFCell cell, String value) {
        cell.setCellType(CellType.STRING);
        cell.setCellValue(value);
    }

    /**
     * 从单元格中获取数据
     * @param cell 单元格
     * @return Object value
     * @throws Exception 异常
     */
    private static Object getCellValue(Cell cell) throws Exception {
        Object value;
        // 不同的类型，做相应的处理
        if (CellType.STRING == cell.getCellType()) {
            value = cell.getStringCellValue();
        } else if (CellType.NUMERIC == cell.getCellType()) {
            value = cell.getNumericCellValue();
            // 日期类型格式也被认为是数值型，此处判断是否是日期的格式，若为时间，则读取为日期类型
            if (cell.getCellStyle().getDataFormat() > 0) {
                value = cell.getDateCellValue();
            }
        } else if (CellType.BOOLEAN == cell.getCellType()) {
            value = cell.getBooleanCellValue();

        } else if (CellType.BLANK == cell.getCellType()) {
            value = cell.getDateCellValue();
        } else {
            throw new Exception("At row: %s, col: %s, unknown type!");
        }
        return value;
    }

    /**
     * 获取method
     * @param clazz 类
     * @param fieldName 字段名
     * @return Method method
     * @throws NoSuchMethodException 异常
     */
    private static Method getMethod(Class<?> clazz, String fieldName) throws NoSuchMethodException {
        Method method = null;
        String methodName = "get" + capitalize(fieldName);
        try {
            method = clazz.getDeclaredMethod(methodName);
        } catch (NoSuchMethodException e) { //不存在该方法，查看父类是否存在。此处只支持一级父类
            if (null != clazz.getSuperclass()) {
                method = clazz.getSuperclass().getDeclaredMethod(methodName);
            }
        }
        if (null == method) {
            throw new NoSuchMethodException(clazz.getName() + " don't have method --> " + methodName);
        }
        return method;
    }

    /**
     * 校验导入方法参数是否正确
     * @param filePath 文件路径
     * @throws Exception 异常
     */
    private static void validateParams(String filePath) throws Exception {
        if (!(filePath.endsWith(".xls") || filePath.endsWith(".xlsx"))) {
            throw new Exception("incorrect file format.");
        }
    }

    /**
     * 首字母大写
     */
    private static String capitalize(final String str) {
        int strLen;
        if (str == null || (strLen = str.length()) == 0) {
            return str;
        }

        final char firstChar = str.charAt(0);
        final char newChar = Character.toTitleCase(firstChar);
        if (firstChar == newChar) {
            // already capitalized
            return str;
        }

        char[] newChars = new char[strLen];
        newChars[0] = newChar;
        str.getChars(1, strLen, newChars, 1);
        return String.valueOf(newChars);
    }

}
